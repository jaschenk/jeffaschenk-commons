package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.admin.IRRcopyEntry;
import jeffaschenk.commons.frameworks.cnxidx.admin.IRRdeleteEntry;
import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.filtering.FilterString;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxManageContext;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxParseDN;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;

import java.util.*;
import java.io.*;

import javax.naming.*;
import javax.naming.directory.*;

/**
 * IRR Change Log Restore Service Replica Restore will maintain updates
 * to the configured directory instance for this PEER.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

/**
 * IRRChangeLogRestoreServiceReaderThread
 * Class to run Replica Restore Thread.
 */
class IRRChangeLogRestoreServiceReplicaRestoreThread implements Runnable, LDIFConstants {

    // *******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = IRRChangeLogRestoreServiceReplicaRestoreThread.class.getName();

    /**
     * IRRChangeLogRestoreServiceReplicaRestoreThread
     * Class to provide Replica Restore Thread to perform
     * updates to Directory instance.
     */
    Thread t;
    private static final String THREAD_NAME
            = "ServiceReplicaRestoreThread";

    private Calendar lasttime = Calendar.getInstance();

    private CircularObjectStack cosin;   // Input Stack.
    private CircularObjectStack cosout;  // Output Common Publisher Stack.

    // *******************************
    // Global Variables.
    private String IRRHost = null;
    private String IRRPrincipal = null;
    private String IRRCredentials = null;
    //
    private String DN_FILTER_FILE = null;
    //
    private FilterString dn_exclusion_filter = null;

    // ********************************
    // My Controller Thread.
    private IRRChangeLogRestoreServiceControlThread
            CONTROL_THREAD = null;    // Provides interface to Local Cmds.

    // ***********************************************
    // Initialize my LAP Timers
    private idxLapTime LP_ENTRY_TO_DIRECTORY = new idxLapTime();
    private idxLapTime LP_ENTRY_FROM_COS = new idxLapTime();
    private idxLapTime LP_ENTRY_TO_COS = new idxLapTime();

    // ************************************
    // Global Thread Counters
    private long DNsread = 0;
    private long DNsprocessed = 0;
    private long DNsBlocked = 0;
    private long ready_commands_sent = 0;
    private long work_commands_received = 0;

    // ************************************
    // Change Counters.
    private long entries_processed = 0;
    private long entries_exceptions = 0;
    private long entries_ignored_exceptions = 0;
    private long entries_added = 0;
    private long entries_deleted = 0;
    private long entries_modified = 0;
    private long entries_renamed = 0;

    private long total_attributes_modified = 0;
    private long total_attributes_deleted = 0;
    private long total_attributes_added = 0;

    private long OPEN_FAILURE_COUNT = 0;
    private long OTHER_FAILURE_COUNT = 0;

    // *************************************
    // Last Replica Data Restored,
    // designed for status information.
    private long LAST_TIME_DN_PROCESSED = 0;
    private ChangeIdentifier LAST_CHANGEIDENTIFIER_PROCESSED = null;
    private String LAST_DN_PROCESSED = null;
    private String LAST_CHANGE_TYPE_ISSUED = null;
    private long LAST_TIME_RESPONSE_SENT = 0;

    // ************************************
    // Cache Directory Context.
    private idxManageContext idxc = null;

    /**
     * IRRBackupOutputThread Contructor class driven.
     *
     * @param cosin          Circular Object Stack for Obtaining Control Commands.
     * @param IRRHost        String URL of Directory Instance.
     * @param IRRPrincipal   Distinguished Name to Bind to the Directory.
     * @param IRRCredentials
     * @param DN_FILTER_FILE Filename of the Filter File which will block
     *                       Entries from being updated based upon a match against a filter.
     */
    IRRChangeLogRestoreServiceReplicaRestoreThread(
            IRRChangeLogRestoreServiceControlThread CONTROL_THREAD,
            CircularObjectStack cosin,
            CircularObjectStack cosout,
            String IRRHost,
            String IRRPrincipal,
            String IRRCredentials,
            String DN_FILTER_FILE) {

        // ****************************************
        // Set My Incoming Parameters.
        this.CONTROL_THREAD = CONTROL_THREAD;
        this.cosin = cosin;
        this.cosout = cosout;
        this.IRRHost = IRRHost;
        this.IRRPrincipal = IRRPrincipal;
        this.IRRCredentials = IRRCredentials;
        this.DN_FILTER_FILE = DN_FILTER_FILE;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        t = new Thread(this, IRRChangeLogRestoreServiceReplicaRestoreThread.THREAD_NAME);
        t.start(); // Start the Thread
    } // End of Contructor.


    /**
     * run
     * Thread to Read Control Commands from the Controller
     * Thread and perform reads from the Input Area to obtain the
     * Change Log Data.
     */
    public void run() {

        // ***********************************************
        // Initialize our StopWatch to measure Duration
        // of Thread.
        final String METHODNAME = "run";
        StopWatch sw = new StopWatch();
        sw.start();

        // ***********************************************
        // Initialize Thread Variables.
        StackCommand instackcommand = null;
        boolean running = true;

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.REPLICARESTORE_THREAD_ESTABLISHED,
                new String[]{Thread.currentThread().getName()});

        // ***********************************************
        // Indicate to our Control Thread I am Ready to
        // Perform Work.
        // TODO Do more Investigation to determine if
        // we are in a restart mode.
        this.ready_commands_sent++;
        this.cosout.push(new StackCommand(StackCommand.CL_REPLICA_READY,
                this.CONTROL_THREAD.my_addr,
                this.CONTROL_THREAD.my_addr,
                Long.toString(this.ready_commands_sent)));

        // ***********************************************
        // Initialize the Exclusion Filter File Object.
        this.prepareFilter();

        // **************************************
        // Loop to process commands from Walker
        // Thread.
        try {
            while (running) {
                instackcommand = null; // Be sure to Clear Previous Entry.
                LP_ENTRY_FROM_COS.Start();
                if (cosin.hasMoreNodes()) {
                    instackcommand = (StackCommand) cosin.getNext();
                }
                LP_ENTRY_FROM_COS.Stop();

                // ***************************
                // Did anything get pulled
                // from stack?
                if (instackcommand == null) {
                    Thread.sleep(1000);
                    continue;
                } // End of Nothing in Stack yet to Process.

                // ******************************
                // Did our Running State trip to
                // not based upon a End of Thread
                // command? Close our Loop.
                if (instackcommand.getCommandType() ==
                        StackCommand.CL_END_OF_THREAD) {
                    running = false;

                    // *******************************
                    // Close any open Directory
                    // Context.
                    this.closeDirectoryContext(this.idxc);

                    // **********************************
                    // Force loop to end next iteration.
                    continue; // Force our loop to End.
                } // End of Check for End of Thread.

                // *****************************
                // Ok, Interpret the Command.
                switch (instackcommand.getCommandType()) {
                    case StackCommand.CL_REPLICATE:
                        // *************************************************
                        // Process the Replication Request and obtain our
                        // Response.
                        work_commands_received++;
                        ChangeResponseIdentifier cri =
                                this.processReplication(instackcommand);

                        // *************************************************
                        // Now place Response on the Common Publisher Stack.
                        this.LAST_TIME_RESPONSE_SENT = System.currentTimeMillis();
                        LP_ENTRY_TO_COS.Start();
                        this.cosout.push(new StackCommand(StackCommand.CL_REPLICATE_RESPONSE,
                                instackcommand.getArguments(),
                                this.CONTROL_THREAD.my_addr,
                                cri.getOriginator(),
                                instackcommand.getIdentifier(),
                                cri));
                        LP_ENTRY_TO_COS.Stop();

                        // **************************************************
                        // Show what we published onto the BUS.
                        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                                MessageConstants.REPLICARESTORE_PROCESSED_REPLICATION_ELEMENT_RESPONSE,
                                new String[]{cri.toString()});

                        // **************************************************
                        // Serialize the Response ourselves to ensure a Mark
                        // Checkpoint available.
                        // TODO

                        break;

                    default:
                        // **********************************
                        // Ignore Any Unknown Command Types.
                        FrameworkLogger.log(CLASSNAME, METHODNAME,
                                FrameworkLoggerLevel.WARNING,
                                ErrorConstants.REPLICARESTORE_IGNORING_UNKNOWN_STACK_COMMAND);

                } // End of Switch Statement.
            } // End of Outer While Loop.
        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.REPLICARESTORE_EXCEPTION_IN_MAIN_LOOP,
                    new String[]{e.getMessage()});
            // ******************************
            // Log the Stack Trace.
            this.logStackTrace(e, METHODNAME,
                    ErrorConstants.REPLICARESTORE_EXCEPTION_STACKTRACE);
        } finally {

            // ***************************************
            // Show the Lap Timings.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.REPLICARESTORE_LAPTIME_TO_DIRECTORY,
                    new String[]{LP_ENTRY_TO_DIRECTORY.toString()});

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.REPLICARESTORE_LAPTIME_FROM_STACK,
                    new String[]{LP_ENTRY_FROM_COS.toString()});

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.REPLICARESTORE_LAPTIME_TO_STACK,
                    new String[]{LP_ENTRY_TO_COS.toString()});

            // ***************************************
            // Show the Duration of Thread.
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.REPLICARESTORE_THREAD_SHUTDOWN,
                    new String[]{sw.getElapsedTimeString()});

        } // End of Final Processing for run Thread Method.
    } // End of run.

    /**
     * Get Current Component Status.
     *
     * @return String Data Representing Current Status of this Component.
     */
    protected String getStatus() {
        StringBuffer sb = new StringBuffer();
        // **************************************
        // Build String Buffer with Status for
        // this component.

        // **************************************
        // Show Header
        sb.append(WebAdminResponderThread.COMPONENT_ONLINE_BEGIN);
        sb.append("Replica Restore Thread");
        sb.append(WebAdminResponderThread.COMPONENT_ONLINE_END);

        // **************************************
        // Show Detail.
        sb.append(WebAdminResponderThread.BEGIN_TABLE);


        // ****************************************
        // Build Inner State Tables.
        StringBuffer s1 = new StringBuffer();

        s1.append(WebAdminResponderThread.BEGIN_TABLE);
        s1.append(WebAdminResponderThread.build2ColumnRow("DNs Read",
                Long.toString(this.DNsread)));

        s1.append(WebAdminResponderThread.build2ColumnRow("DNs Processed",
                Long.toString(this.DNsprocessed)));

        s1.append(WebAdminResponderThread.build2ColumnRow("DNs Blocked By Filter",
                Long.toString(this.DNsBlocked)));

        s1.append(WebAdminResponderThread.build2ColumnRow("Entries Processed",
                Long.toString(this.entries_processed)));

        s1.append(WebAdminResponderThread.build2ColumnRow("Exceptions",
                Long.toString(this.entries_exceptions)));

        s1.append(WebAdminResponderThread.build2ColumnRow("Ignored Exceptions",
                Long.toString(this.entries_ignored_exceptions)));

        s1.append(WebAdminResponderThread.build2ColumnRow("Entries Added",
                Long.toString(this.entries_added)));

        s1.append(WebAdminResponderThread.build2ColumnRow("Entries Deleted",
                Long.toString(this.entries_deleted)));

        s1.append(WebAdminResponderThread.build2ColumnRow("Entries Modified",
                Long.toString(this.entries_modified)));

        s1.append(WebAdminResponderThread.build2ColumnRow("Entries Renamed",
                Long.toString(this.entries_renamed)));

        s1.append(WebAdminResponderThread.END_TABLE);

        // ************************************
        // Establish Second Table Column.
        StringBuffer s2 = new StringBuffer();
        s2.append(WebAdminResponderThread.BEGIN_TABLE);
        s2.append(WebAdminResponderThread.build2ColumnRow("Total Attrs Modified",
                Long.toString(this.total_attributes_modified)));

        s2.append(WebAdminResponderThread.build2ColumnRow("Total Attrs Deleted",
                Long.toString(this.total_attributes_deleted)));

        s2.append(WebAdminResponderThread.build2ColumnRow("Total Attrs Added",
                Long.toString(this.total_attributes_added)));

        s2.append(WebAdminResponderThread.build2ColumnRow("LDAP Open Failure Count",
                Long.toString(this.OPEN_FAILURE_COUNT)));

        s2.append(WebAdminResponderThread.build2ColumnRow("Other Failure Count",
                Long.toString(this.OTHER_FAILURE_COUNT)));

        s2.append(WebAdminResponderThread.END_TABLE);

        // ***********************************
        // Place both in the Table.
        sb.append(WebAdminResponderThread.build2ColumnRow(s1.toString(), s2.toString()));

        // ***************************************
        // Show Last Information Processed.
        if (this.LAST_TIME_DN_PROCESSED > 0) {
            this.lasttime.setTimeInMillis(this.LAST_TIME_DN_PROCESSED);
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Time DN Processed",
                    this.lasttime.getTime().toString()));
        } // End of If.

        if (this.LAST_CHANGEIDENTIFIER_PROCESSED != null) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Change Identifier Processed",
                    this.LAST_CHANGEIDENTIFIER_PROCESSED.toString()));
        } // End of If.

        if (this.LAST_DN_PROCESSED != null) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Last DN Processed",
                    this.LAST_DN_PROCESSED));
        } // End of If.

        if (this.LAST_CHANGE_TYPE_ISSUED != null) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Change Type Processed",
                    this.LAST_CHANGE_TYPE_ISSUED));
        } // End of If.

        if (this.LAST_TIME_RESPONSE_SENT > 0) {
            this.lasttime.setTimeInMillis(this.LAST_TIME_RESPONSE_SENT);
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Time Response Sent",
                    this.lasttime.getTime().toString()));
        } // End of If.

        // **********************************
        // Show Laps.
        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Update to Directory",
                LP_ENTRY_TO_DIRECTORY.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Message From Input Stack",
                LP_ENTRY_FROM_COS.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Message To Output Stack",
                LP_ENTRY_TO_COS.toString()));


        sb.append(WebAdminResponderThread.END_TABLE);

        // **************************************
        // Return Formatted Status.
        return sb.toString();
    } // End of Protected GetStatus Method.

    /**
     * Helper Method to reset Component Statistics.
     */
    protected void resetStatistics() {
        // ************************************
        // Global Thread Counters
        this.DNsread = 0;
        this.DNsprocessed = 0;
        this.DNsBlocked = 0;
        this.ready_commands_sent = 0;
        this.work_commands_received = 0;

        // ************************************
        // Change Counters.
        this.entries_processed = 0;
        this.entries_exceptions = 0;
        this.entries_ignored_exceptions = 0;
        this.entries_added = 0;
        this.entries_deleted = 0;
        this.entries_modified = 0;
        this.entries_renamed = 0;

        this.total_attributes_modified = 0;
        this.total_attributes_deleted = 0;
        this.total_attributes_added = 0;

        this.OPEN_FAILURE_COUNT = 0;
        this.OTHER_FAILURE_COUNT = 0;

        // ***********************************************
        // Clear Last Saved Information.
        this.LAST_TIME_DN_PROCESSED = 0;
        this.LAST_DN_PROCESSED = null;
        this.LAST_CHANGEIDENTIFIER_PROCESSED = null;
        this.LAST_CHANGE_TYPE_ISSUED = null;
        this.LAST_TIME_RESPONSE_SENT = 0;

        // ***********************************************
        // Initialize my LAP Timers
        this.LP_ENTRY_TO_DIRECTORY.Reset();
        this.LP_ENTRY_FROM_COS.Reset();
        this.LP_ENTRY_TO_COS.Reset();
    } // End of resetStatistics protected method.

    /**
     * Get Current Component Status.
     *
     * @return String Data Representing Current Status of this Component.
     */
    protected static String getOfflineStatus() {
        StringBuffer sb = new StringBuffer();
        // **************************************
        // Build String Buffer with Status for
        // this component.

        // **************************************
        // Show Header
        sb.append(WebAdminResponderThread.COMPONENT_OFFLINE_BEGIN);
        sb.append("Replica Restore Thread");
        sb.append(WebAdminResponderThread.COMPONENT_OFFLINE_END);

        // **************************************
        // Show Offline.
        sb.append(WebAdminResponderThread.BEGIN_TABLE);

        sb.append(WebAdminResponderThread.build2ColumnRow("Status Not Available",
                "Thread Offline"));

        sb.append(WebAdminResponderThread.END_TABLE);

        // **************************************
        // Return Formatted Status.
        return sb.toString();
    } // End of Protected GetStatus Method.

    // ************************************************
    // Private Method for performing operations.
    // ************************************************

    /**
     * Process a Replication command and issue the change
     * and reply
     */
    private ChangeResponseIdentifier processReplication(StackCommand instackcommand) {

        // *****************************************
        // Initialize.
        final String METHODNAME = "processReplication";
        StopWatch sw = new StopWatch();
        sw.start();

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.REPLICARESTORE_PROCESSING_REPLICATION_ELEMENT,
                new String[]{instackcommand.toString()});

        // *******************************
        // Validate and Process.
        try {
            // *********************************
            // Verify we have a valid response
            // Key.
            if ((instackcommand == null) ||
                    (instackcommand.getIdentifier() == null) ||
                    (instackcommand.getOriginator() == null) ||
                    (instackcommand.getObject() == null)) {
                // *************************
                // We have a corrupted
                // Stack Command.
                // Note it as a Warning
                // Message.
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.REPLICARESTORE_CORRUPTED_STACK_COMMAND,
                        new String[]{instackcommand.toString()});
                return new ChangeResponseIdentifier(instackcommand.getIdentifier(),
                        this.CONTROL_THREAD.my_addr,
                        ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                        ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                        ChangeResponseIdentifier.CHANGE_IGNORED,
                        "Change Object is NULL.");
            } // End of Validation Check.

            // ******************************************
            // Obtain Input arguments.
            String[] rargs = instackcommand.getArguments();
            if (rargs.length != 2) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.REPLICARESTORE_ROGUE_REPLICATION_COMMAND_BAD_ARGS,
                        new String[]{instackcommand.toString()});
                return new ChangeResponseIdentifier(instackcommand.getIdentifier(),
                        this.CONTROL_THREAD.my_addr,
                        ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                        ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                        ChangeResponseIdentifier.CHANGE_IGNORED,
                        "Invalid String Arguments.");
            } // End of Check For Arguments.

            // ***********************************
            // Obtain Replication Elements.
            String current_dn = rargs[0];
            String changetype = rargs[1];

            // ***********************************
            // Check our Change Type.
            if ((changetype == null) ||
                    (changetype.trim().equalsIgnoreCase(""))) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.REPLICARESTORE_ROGUE_REPLICATION_COMMAND_NULL_CHANGETYPE,
                        new String[]{instackcommand.toString()});
                return new ChangeResponseIdentifier(instackcommand.getIdentifier(),
                        this.CONTROL_THREAD.my_addr,
                        ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                        ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                        ChangeResponseIdentifier.CHANGE_IGNORED,
                        "ChangeType is NULL.");
            } // End of Check for bad change.

            // ************************************
            // Accumulate a Count.
            this.DNsread++;

            // ************************************
            // Now Filter the DN to see if we have a
            // Match to Exclude this change from
            // being Distributed.
            if (this.dn_exclusion_filter.match(current_dn.toLowerCase() + ":" + changetype)) {
                this.DNsBlocked++;
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.REPLICARESTORE_BLOCKING_ENTRY,
                        new String[]{changetype,
                                current_dn,
                                instackcommand.getIdentifier().toString()});

                // ********************************
                // Indicate we have blocked it.
                return new ChangeResponseIdentifier(instackcommand.getIdentifier(),
                        this.CONTROL_THREAD.my_addr,
                        ChangeResponseIdentifier.CHANGE_BLOCKED,
                        ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                        ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                        "Change has been Blocked.");
            } // End of Check to Block Entry from being Sent.

            // ****************************************
            // Now Check for a Cached Directory Context
            if ((this.idxc == null) ||
                    (this.idxc.irrctx == null)) {
                this.idxc = this.establishDirectoryContext();
            }

            // *****************************************
            // Gain a reference to a common response
            // Object.
            ChangeResponseIdentifier cri = null;

            // ************************************
            // Delegate the Change based upon
            // replica Change Type.
            if (changetype.equalsIgnoreCase(LDIF_ADD_CHANGETYPE)) {
                cri = this.addEntry(this.idxc.irrctx, current_dn, (Attributes) instackcommand.getObject(),
                        instackcommand.getIdentifier());
            } else if (changetype.equalsIgnoreCase(LDIF_DELETE_CHANGETYPE)) {
                cri = deleteEntry(this.idxc.irrctx, current_dn, (Attributes) instackcommand.getObject(),
                        instackcommand.getIdentifier());
            } else if (changetype.equalsIgnoreCase(LDIF_MODIFY_CHANGETYPE)) {
                cri = modifyEntry(this.idxc.irrctx, current_dn, (ModificationItem[]) instackcommand.getObject(),
                        instackcommand.getIdentifier());
            } else if ((changetype.equalsIgnoreCase(LDIF_MODRDN_CHANGETYPE)) ||
                    (changetype.equalsIgnoreCase(LDIF_MODDN_CHANGETYPE))) {
                cri = modDNEntry(this.idxc.irrctx, current_dn, (Attributes) instackcommand.getObject(),
                        instackcommand.getIdentifier());
            }

            // ****************************************************
            // Falling through indicates we have an invalid
            // Change Type, report it and return to ignore
            // this invalid change type.
            //
            else {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.REPLICARESTORE_ROGUE_REPLICATION_COMMAND_INVALID_CHANGETYPE,
                        new String[]{changetype, current_dn});
                return new ChangeResponseIdentifier(instackcommand.getIdentifier(),
                        this.CONTROL_THREAD.my_addr,
                        ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                        ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                        ChangeResponseIdentifier.CHANGE_IGNORED,
                        "Change Type is Invalid.");
            } // End of Else.

            // ************************************
            // Now Save Last Information for
            // Status.
            this.DNsprocessed++;
            this.LAST_TIME_DN_PROCESSED = System.currentTimeMillis();
            this.LAST_DN_PROCESSED = current_dn;
            this.LAST_CHANGEIDENTIFIER_PROCESSED
                    = instackcommand.getIdentifier();
            this.LAST_CHANGE_TYPE_ISSUED = changetype;

            // *******************
            // Return our Response
            // Indicator Object.
            return cri;
        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.REPLICARESTORE_EXCEPTION_PROCESSING_REPLICATION,
                    new String[]{e.getMessage()});
            // ******************************
            // Log the Stack Trace.
            this.logStackTrace(e, METHODNAME,
                    ErrorConstants.REPLICARESTORE_EXCEPTION_STACKTRACE);
            return new ChangeResponseIdentifier(instackcommand.getIdentifier(),
                    this.CONTROL_THREAD.my_addr,
                    ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                    ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                    ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                    "Change Exception:" + e);
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of processReplication private Method.

    /**
     * addEntry, Performs add of Entry.
     *
     * @param DirContext       of Destination Directory where modifications are to be applied.
     * @param String           Current DN.
     * @param Attributes       Current set of Modification Attributes.
     * @param changeidentifier to be folded into response.
     */
    private ChangeResponseIdentifier addEntry(DirContext irrctx,
                                              String current_dn,
                                              Attributes current_entry_attributes,
                                              ChangeIdentifier changeidentifier)
            throws Exception, idxIRRException {

        // *****************************
        // Initialize.
        final String METHODNAME = "addEntry";
        StopWatch sw = new StopWatch();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.REPLICARESTORE_PROCESSING_ADD,
                new String[]{current_dn});

        // ****************************
        // Count the Entries Processed
        entries_processed++;

        // ***********************************
        // Perform the Add/bind of the Entry.
        try {
            // *******************************
            // Remove the DN Attribute if it
            // exists in the attributes...
            current_entry_attributes.remove("dn");

            // *******************************
            // Perform the Bind.
            irrctx.bind(current_dn, null, current_entry_attributes);
            entries_added++;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.REPLICARESTORE_PROCESSING_ADD_COMPLETED,
                    new String[]{current_dn});

            // *******************************
            // Return indicating Successful
            // Response.
            return new ChangeResponseIdentifier(changeidentifier,
                    this.CONTROL_THREAD.my_addr,
                    ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                    ChangeResponseIdentifier.CHANGE_SUCCESSFUL,
                    ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                    "Add Succesful for DN:[" + current_dn + "].");

        } catch (NameAlreadyBoundException e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    ErrorConstants.REPLICARESTORE_NAME_ALREADY_BOUND,
                    new String[]{current_dn, e.getMessage()});
            // *******************************
            // Return indicating Ignored
            // Response for Name Already Bound.
            entries_ignored_exceptions++;
            return new ChangeResponseIdentifier(changeidentifier,
                    this.CONTROL_THREAD.my_addr,
                    ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                    ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                    ChangeResponseIdentifier.CHANGE_IGNORED,
                    "Add Ignored, since Name Already Bound for DN:[" + current_dn + "].");
        } catch (Exception e_bind) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.REPLICARESTORE_EXCEPTION_PROCESSING_ADD,
                    new String[]{current_dn, e_bind.getMessage()});
            entries_exceptions++;
            throw e_bind;
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of addEntry Method.

    /**
     * deleteEntry, Performs delete of Entry.
     *
     * @param DirContext of Destination Directory where modifications are to be applied.
     * @param String     Current DN.
     * @param Attributes Current set of Modification Attributes.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private ChangeResponseIdentifier deleteEntry(DirContext irrctx,
                                                 String current_dn,
                                                 Attributes current_entry_attributes,
                                                 ChangeIdentifier changeidentifier)
            throws Exception, idxIRRException {

        // *******************************
        // Initialize.
        final String METHODNAME = "deleteEntry";
        StopWatch sw = new StopWatch();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.REPLICARESTORE_PROCESSING_DELETE,
                new String[]{current_dn});

        // ****************************
        // Count the Entries Processed
        entries_processed++;

        // *******************************
        // Obtain Additional Values for
        // performing the rename.
        //
        Attribute eo = null;

        try {
            // ************************************
            // Eventually there is to be a
            // new function to perform a
            // tree delete control.
            // See RFC2849.
            //
            // Now check for that TREE Deletion
            // control.
            // ************************************
            boolean deletetree = false;
            String _control = (
                    (eo = current_entry_attributes.get(CONTROL)) != null ? (String) eo.get() : null);
            if ((_control != null) &&
                    (_control.startsWith(TREE_DELETE_CONTROL))) {
                _control = _control.trim().toLowerCase();
                if (_control.endsWith("true")) {
                    deletetree = true;
                }
            } // End of If.

            // ***************************************
            // Perform a Tree Delete if Required
            // Based upon control.
            if (deletetree) {
                // ****************************************
                // Initailize Constructor.
                IRRdeleteEntry deleteFUNCTION = new IRRdeleteEntry();

                // ****************************************
                // Perform Function.
                try {
                    deleteFUNCTION.perform(irrctx,
                            current_dn,
                            true,
                            false,
                            false);
                    entries_deleted++;
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                            MessageConstants.REPLICARESTORE_PROCESSING_DELETETREE_COMPLETED,
                            new String[]{current_dn});

                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                            "Delete Successful for DN:[" + current_dn + "].");
                } catch (Exception et) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                            ErrorConstants.REPLICARESTORE_EXCEPTION_DELETINGTREE,
                            new String[]{current_dn});
                    entries_exceptions++;
                    throw et;
                } // End of Exception Processing.
            } // End of Delete Tree.
            else {
                // *****************************
                // Perform the delete/unbind of
                // the single Entry.
                try {
                    irrctx.unbind(current_dn);
                    entries_deleted++;
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                            MessageConstants.REPLICARESTORE_PROCESSING_DELETE_COMPLETED,
                            new String[]{current_dn});
                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                            "Delete Successful for DN:[" + current_dn + "].");

                } catch (NameNotFoundException e) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                            ErrorConstants.REPLICARESTORE_EXCEPTION_DELETINGENTRY_NOTFOUND,
                            new String[]{current_dn, e.getMessage()});
                    // *******************************
                    // Return indicating Ignored
                    // Response for Name Not Found
                    // for Delete.
                    entries_ignored_exceptions++;
                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_IGNORED,
                            "Delete Ignored, since Name Not Found for DN:[" + current_dn + "].");

                } catch (ContextNotEmptyException nee) {
                    // *******************************
                    // Return indicating Ignored
                    // Response for a Entry which is
                    // a non-leaf node, which has children.
                    // Delete could have been blocked, so
                    // Ignore for Delete.
                    entries_ignored_exceptions++;
                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_IGNORED,
                            "Delete Ignored, since Named Entry has children under parent DN:[" + current_dn + "].");

                } catch (Exception e_unbind) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                            ErrorConstants.REPLICARESTORE_EXCEPTION_DELETINGENTRY,
                            new String[]{current_dn, e_unbind.getMessage()});
                    entries_exceptions++;
                    throw e_unbind;
                } // End of Exception Processing.
            } // End of Else.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of deleteEntry Method.

    /**
     * modDNEntry, Performs Rename/MODRDN/MODDN of Entry.
     *
     * @param DirContext of Destination Directory where modifications are to be applied.
     * @param String     Current DN.
     * @param Attributes Current set of Modification Attributes.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private ChangeResponseIdentifier modDNEntry(DirContext irrctx,
                                                String current_dn,
                                                Attributes current_entry_attributes,
                                                ChangeIdentifier changeidentifier)
            throws Exception, idxIRRException {

        // *******************************
        // Initialize.
        final String METHODNAME = "modDNEntry";
        StopWatch sw = new StopWatch();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.REPLICARESTORE_PROCESSING_RENAME,
                new String[]{current_dn});

        // ****************************
        // Count the Entries Processed
        entries_processed++;
        try {

            // *******************************
            // Obtain Additional Values for
            // performing the rename.
            //
            Attribute eo = null;

            // *********************
            // New RDN.
            String _newrdn = (
                    (eo = current_entry_attributes.get(NEWRDN)) != null ? (String) eo.get() : null);
            if ((_newrdn == null) ||
                    (_newrdn.equalsIgnoreCase(""))) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.REPLICARESTORE_NO_NEWRDN_FOR_RENAME,
                        new String[]{current_dn});
                // *******************************
                // Return indicating Ignored
                // Response indicating to NEWRDN.
                entries_ignored_exceptions++;
                return new ChangeResponseIdentifier(changeidentifier,
                        this.CONTROL_THREAD.my_addr,
                        ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                        ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                        ChangeResponseIdentifier.CHANGE_IGNORED,
                        "MODDN Ignored, since No NEWRDN Specified Original DN:[" + current_dn + "].");
            } // End of if.

            // *************************
            // Delete Old RDN Indicator.
            boolean deleteoldrdn = true;
            String _deleteoldrdn = (
                    (eo = current_entry_attributes.get(DELETEOLDRDN)) != null ? (String) eo.get() : null);
            if ((_deleteoldrdn == null) ||
                    (_deleteoldrdn.equalsIgnoreCase("0"))) {
                deleteoldrdn = false;
            }

            // *********************
            // New Superior.
            String _newsuperior = (
                    (eo = current_entry_attributes.get(NEWSUPERIOR)) != null ? (String) eo.get() : null);

            // *********************
            // Formulate the new DN
            String _newDN = _newrdn;
            if ((_newsuperior != null) &&
                    (!_newsuperior.equalsIgnoreCase(""))) {
                _newDN = _newDN + "," + _newsuperior;
            } else {
                idxParseDN zDN = new idxParseDN(current_dn);
                if (!zDN.isValid()) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                            ErrorConstants.REPLICARESTORE_EXISTING_DN_FOR_RENAME_INVALID,
                            new String[]{current_dn});
                    // *******************************
                    // Return indicating Ignored
                    // Response indicating to NEWRDN.
                    entries_ignored_exceptions++;
                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_IGNORED,
                            "MODDN Ignored, since Specified DN is Invalid:[" + current_dn + "].");
                } // End of If.

                // ************************
                // Now Construct new DN.
                if (!zDN.getPDN().equalsIgnoreCase("")) {
                    _newDN = _newDN + "," + zDN.getPDN();
                }
            } // End of Else.

            // ***********************************
            // Perform the ModDN/ModRDN/Rename
            // of the Entry.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.REPLICARESTORE_PROCESSING_RENAME_FORMULATED_NEW_NAME,
                    new String[]{_newDN});

            // *************************
            // Determine if we have a
            // simple Rename or a Copy.
            if (deleteoldrdn) {
                // ***********************************
                // Perform the Add/bind of the Entry.
                try {
                    irrctx.rename(current_dn, _newDN);
                    entries_renamed++;
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                            MessageConstants.REPLICARESTORE_RENAME_SUCCESSFULLY_COMPLETED,
                            new String[]{_newDN});

                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                            "MODDN Successful for DN:[" + current_dn + "].");
                } catch (NameAlreadyBoundException e) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                            ErrorConstants.REPLICARESTORE_NAME_ALREADY_BOUND_NEWDN,
                            new String[]{current_dn, _newDN, e.getMessage()});
                    // *******************************
                    // Return indicating Ignored
                    // Response indicating to NEWRDN.
                    entries_ignored_exceptions++;
                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_IGNORED,
                            "MODDN Ignored, since Specified NewDN is Already Bound:[" + _newDN + "], " +
                                    "Original DN:[" + current_dn + "], Message:[" + e.getMessage() + "].");
                } catch (Exception e_bind) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                            ErrorConstants.REPLICARESTORE_EXCEPTION_RENAMINGENTRY,
                            new String[]{current_dn, _newDN, e_bind.getMessage()});
                    entries_exceptions++;
                    throw e_bind;
                } // End of Exception Processing.

                // ****************************
                // Since the delete old rdn
                // is false, we must perform
                // a copy from one container
                // to the other.
                //
            } else {
                // ****************************************
                // Initialize Constructor.
                IRRcopyEntry copyFUNCTION = new IRRcopyEntry();

                // ****************************************
                // Perform Function.
                try {
                    copyFUNCTION.perform(irrctx,
                            current_dn,
                            irrctx,
                            _newDN,
                            false,
                            true,
                            false,
                            false);
                    entries_renamed++;
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                            MessageConstants.REPLICARESTORE_RENAME_SUCCESSFULLY_COMPLETED_LEAVING_EXISTING_DN_IN_PLACE,
                            new String[]{_newDN});
                    return new ChangeResponseIdentifier(changeidentifier,
                            this.CONTROL_THREAD.my_addr,
                            ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                            ChangeResponseIdentifier.CHANGE_SUCCESSFUL,
                            ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                            "MODDN Successful for DN:[" + current_dn + "].");
                } catch (Exception e_bind) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                            ErrorConstants.REPLICARESTORE_EXCEPTION_RENAMINGENTRY_USINGCOPY,
                            new String[]{current_dn, _newDN, e_bind.getMessage()});
                    entries_exceptions++;
                    throw e_bind;
                } // End of Exception Processing.
            } // End of Else.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of modDNEntry Method.

    /**
     * modifyEntry, Performs Modifications on Entry.
     *
     * @param DirContext of Destination Directory where modifications are to be applied.
     * @param String     Current DN.
     * @param Attributes Current set of Modification Attributes.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private ChangeResponseIdentifier modifyEntry(DirContext irrctx,
                                                 String current_dn,
                                                 ModificationItem[] EntryModSet,
                                                 ChangeIdentifier changeidentifier)
            throws Exception, idxIRRException {

        // *******************************
        // Initialize.
        final String METHODNAME = "modifyEntry";
        StopWatch sw = new StopWatch();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.REPLICARESTORE_PROCESSING_MODIFY,
                new String[]{current_dn});

        int attributes_added = 0;
        int attributes_modified = 0;
        int attributes_deleted = 0;

        // *******************************************
        // Iterate through the List List for the
        // Entry.
        for (int modItem = 0; modItem < EntryModSet.length; modItem++) {
            // ***************************
            // Count the Mods.
            if (EntryModSet[modItem].getModificationOp() == DirContext.ADD_ATTRIBUTE) {
                attributes_added++;
            } else if (EntryModSet[modItem].getModificationOp() == DirContext.REPLACE_ATTRIBUTE) {
                attributes_modified++;
            } else if (EntryModSet[modItem].getModificationOp() == DirContext.REMOVE_ATTRIBUTE) {
                attributes_deleted++;
            }

        } // End of For Loop.

        // *******************************************
        // Now Apply the Modifications to the Entry.
        try {
            irrctx.modifyAttributes(current_dn, EntryModSet);
            entries_modified++;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.REPLICARESTORE_PROCESSING_MODIFY_COMPLETED,
                    new String[]{current_dn});

            // ********************************************
            // Show the stats for the Entry.
            if (attributes_added > 0) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.REPLICARESTORE_ENTRY_ATTRIBUTES_ADDED,
                        new String[]{Long.toString(attributes_added)});
            }
            if (attributes_deleted > 0) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.REPLICARESTORE_ENTRY_ATTRIBUTES_MODIFIED,
                        new String[]{Long.toString(attributes_deleted)});
            }
            if (attributes_modified > 0) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.REPLICARESTORE_ENTRY_ATTRIBUTES_DELETED,
                        new String[]{Long.toString(attributes_modified)});
            }

            // ***************************************************
            // Accumulate the Totals for the Maintenance Activity.
            total_attributes_modified
                    = total_attributes_modified + attributes_modified;
            total_attributes_deleted
                    = total_attributes_deleted + attributes_deleted;
            total_attributes_added
                    = total_attributes_added + attributes_added;

            // ***************************************************
            // Return with a Successful Modification.
            return new ChangeResponseIdentifier(changeidentifier,
                    this.CONTROL_THREAD.my_addr,
                    ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                    ChangeResponseIdentifier.CHANGE_SUCCESSFUL,
                    ChangeResponseIdentifier.CHANGE_NOT_IGNORED,
                    "Modify Successful for DN:[" + current_dn + "].");
        } catch (NameNotFoundException nnfe) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    ErrorConstants.REPLICARESTORE_NAME_NOT_FOUND_FOR_MODIFY,
                    new String[]{current_dn});
            // *******************************
            // Return indicating Ignored
            // Response indicating to NEWRDN.
            entries_ignored_exceptions++;
            return new ChangeResponseIdentifier(changeidentifier,
                    this.CONTROL_THREAD.my_addr,
                    ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                    ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                    ChangeResponseIdentifier.CHANGE_IGNORED,
                    "Modify Ignored, since Specified DN does not exist:[" + current_dn + "].");
        } catch (AttributeInUseException aiu) {
            entries_exceptions++;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    ErrorConstants.REPLICARESTORE_ATTR_ALREADY_EXISTS_USE_REPLACE,
                    new String[]{current_dn});
            throw aiu;
        } catch (NoSuchAttributeException nsa) {
            entries_exceptions++;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    ErrorConstants.REPLICARESTORE_NO_SUCH_ATTR_FOR_MODIFY,
                    new String[]{current_dn});
            throw nsa;
        } catch (AttributeModificationException ame) {
            entries_exceptions++;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.REPLICARESTORE_NO_SUCH_ATTR_FOR_MODIFY,
                    new String[]{current_dn, ame.getMessage()});
            throw ame;
        } catch (NamingException ne) {
            entries_exceptions++;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.REPLICARESTORE_NO_SUCH_ATTR_FOR_MODIFY,
                    new String[]{current_dn, ne.getMessage()});
            throw ne;
        } catch (Exception e) {
            entries_exceptions++;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.REPLICARESTORE_NO_SUCH_ATTR_FOR_MODIFY,
                    new String[]{current_dn, e.getMessage()});
            throw e;
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of modifyEntry method.

    /**
     * Establish new Directory Context for the worker
     * thread to use with downstream facilities.
     *
     * @return idxManageContext Directory Managed Context.
     */
    private idxManageContext establishDirectoryContext() {

        // *******************************
        // Initialize.
        final String METHODNAME = "establishDirectoryContext";
        StopWatch sw = new StopWatch();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});


        try {
            idxManageContext dest = null;
            // ***********************************************
            // Loop Forever to Obtain Directory Context.
            while (true) {
                // ***********************************************
                // Now initiate a Connection to the Directory
                // for a LDAP Source Context
                dest = new idxManageContext(IRRHost,
                        IRRPrincipal,
                        IRRCredentials,
                        "IRRChangeLogRestoreServiceReplicaRestoreThread Destination");

                // ************************************************
                // Never Exit on Exceptions.
                dest.setExitOnException(false);

                // ************************************************
                // Now Try to Open and Obtain Context.
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.REPLICARESTORE_ENTRY_ATTRIBUTES_MODIFIED,
                        new String[]{IRRHost});
                try {
                    dest.open();
                    this.OPEN_FAILURE_COUNT = 0;
                    break;
                } catch (Exception e) {
                    if (this.OPEN_FAILURE_COUNT <= 7) {
                        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                                ErrorConstants.REPLICARESTORE_EXCEPTION_OPENING_DIRECTORY,
                                new String[]{e.getMessage()});
                        this.OPEN_FAILURE_COUNT++;
                    } // End of Failure Count Check, so not to explode a Log.
                } // End of exception
            } // End of While Loop.

            // *****************************************
            // Disable the Factories.
            try {
                dest.disableDSAEFactories();
                this.OTHER_FAILURE_COUNT = 0;
            } catch (Exception e) {
                if (this.OTHER_FAILURE_COUNT <= 7) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                            ErrorConstants.REPLICARESTORE_EXCEPTION_DISABLING_FACTORIES,
                            new String[]{e.getMessage()});
                    this.OTHER_FAILURE_COUNT++;
                } // End of Failure Count Check, so not to explode a Log.
            } // End of exception

            // ******************************
            // Return Acquired Context.
            return dest;
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of establishDirectoryContext Method.

    /**
     * Close an Established Directory Context.
     */
    private void closeDirectoryContext(idxManageContext dest) {

        // *******************************
        // Initialize.
        final String METHODNAME = "closeDirectoryContext";
        StopWatch sw = new StopWatch();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        // ***************************************
        // Close up Shop.
        try {
            if ((dest != null) &&
                    (dest.irrctx != null)) {
                dest.close();
            }
        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    ErrorConstants.REPLICARESTORE_EXCEPTION_CLOSING_DIRECTORY,
                    new String[]{e.getMessage()});
        } finally {
            // ****************************************
            // Finalize Object.
            dest = null;
            // ****************************************
            // Show Final Log Message.
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of closeDirectoryContext Method.

    /**
     * Prepare our Filter File, if available.
     */
    private void prepareFilter() {
        // ******************************
        // Initialize.
        final String METHODNAME = "prepareFilter";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.REPLICARESTORE_PREPARING_DN_EXCLUSION_FILTER,
                new String[]{this.DN_FILTER_FILE});

        // ******************************************
        // Check for Nulls.
        if ((this.DN_FILTER_FILE == null) ||
                (this.DN_FILTER_FILE.trim().equalsIgnoreCase(""))) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    MessageConstants.REPLICARESTORE_FILTERING_DISABLED);
            return;
        } // End of Check for a Filter File Specified.

        // ******************************************
        // Obtain the Filter File Object.
        File f = new File(this.DN_FILTER_FILE);
        if ((f.canRead()) && (f.isFile()) && (f.exists())) {
            try {
                this.dn_exclusion_filter = new FilterString(f);
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.REPLICARESTORE_FILTERING_ENABLED,
                        new String[]{this.dn_exclusion_filter.toString()});
            } catch (IOException ioe) {
                this.dn_exclusion_filter = null;
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        ErrorConstants.REPLICARESTORE_IOEXCEPTION_PROCESSING_FILTER_FILE,
                        new String[]{ioe.getMessage()});
            } // End of Exception processing.
        } // End of File Filter Check.
    } // End of parepareFilter Private Method.

    /**
     * Provides Logging of Stack Traces from Exceptions detail on the Nested Stack Trace
     * from Severe Runtime Errors.
     */
    private void logStackTrace(final Exception e,
                               final String METHODNAME,
                               final String MSGNUM) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        FrameworkLogger.log(CLASSNAME, METHODNAME,
                FrameworkLoggerLevel.SEVERE, MSGNUM,
                new String[]{e.getMessage(), sw.toString()});
    } // End of logStackTrace private Method.
} ///:~ End of Class IRRChangeLogRestoreServiceReplicaRestoreThread
