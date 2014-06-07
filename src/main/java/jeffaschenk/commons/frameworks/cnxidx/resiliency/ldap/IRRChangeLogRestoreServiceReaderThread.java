package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.filtering.FilterString;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;
import jeffaschenk.commons.frameworks.cnxidx.utility.idxLDIFReader;

import java.util.*;
import java.io.*;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.DirContext;

/**
 * IRR Change Log Restore Service Reader will read the change log entries which are
 * RFC2849 compliant and transform the Entry into a JNDI Object and
 * pass element which will be written to the JGroups Cloud only if we are
 * designated as Primary.
 *
 *
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

/**
 * IRRChangeLogRestoreServiceReaderThread
 * Class to run Reader Thread.
 */
class IRRChangeLogRestoreServiceReaderThread implements Runnable, LDIFConstants {

    //	*******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = IRRChangeLogRestoreServiceReaderThread.class.getName();

    /**
     * IRRChangeLogRestoreServiceReaderThread
     * Class to provide Reader Thread to initiate Read of LDIF Change Logs
     * created by the Directory and plce on our output stack.
     */
    Thread t;
    private static final String THREAD_NAME
            = "ServiceReaderThread";

    private Calendar lasttime = Calendar.getInstance();

    private CircularObjectStack cosin;  // Input Stack.
    private CircularObjectStack cosout; // Output Stack.

    private String INPUT_PATH;
    private String DN_FILTER_FILE;

    private FilterString dn_exclusion_filter = null;

    // ********************************
    // My Controller Thread.
    private IRRChangeLogRestoreServiceControlThread
            CONTROL_THREAD = null;    // Provides interface to Local Cmds.

    // ******************************************
    // Reference Pointer to Change Logs.
    private TreeMap unProcessedChangeLogs = null;

    // ******************************************
    // Published Change Logs
    // Map Name is the Log, the Entry Value will be
    // ChangeIdentifier Object.
    private TreeMap<ChangeIdentifierKey, ChangeIdentifier> publishedChangeLogs = new TreeMap<>();

    // ******************************************
    // Published Changes
    // Map Name is the Log, the Entry Value will be
    // an Object Array contains the Change Information
    // to be used in the event of a required retry.
    private TreeMap<ChangeIdentifierKey,Object[]> publishedChanges = new TreeMap<>();

    // ******************************************
    // Change Log Status
    private TreeMap<String,ChangeLogStatus> ChangeLogs = new TreeMap<>();

    // ***********************************************
    // Initialize my LAP Timers
    private idxLapTime LP_ENTRY_READIO = new idxLapTime();
    private idxLapTime LP_ENTRY_WRITEIO = new idxLapTime();
    private idxLapTime LP_ENTRY_TO_COS = new idxLapTime();
    private idxLapTime LP_ENTRY_FROM_COS = new idxLapTime();

    // ************************************
    // Global Thread Counters
    private long DNsread = 0;
    private long DNspublished = 0;
    private long DNsBlocked = 0;
    private long ready_commands_sent = 0;
    private long work_commands_received = 0;
    private long retries_performed = 0;

    // *************************************
    // Last Information Published,
    // designed for status information.
    private long LAST_TIME_CHANGEIDENTIFIER_PUBLISHED = 0;
    private ChangeIdentifier LAST_CHANGEIDENTIFIER_PUBLISHED = null;
    private String LAST_DN_PUBLISHED = null;
    private String LAST_CHANGE_TYPE_PUBLISHED = null;

    private long LAST_TIME_CHANGERESPONSEIDENTIFIER_RECEIVED = 0;
    private ChangeResponseIdentifier LAST_CHANGERESPONSEIDENTIFIER_RECEIVED = null;

    // *************************************
    // Current Modifications
    // DN and LinkedList
    private String CURRENT_MODDN = "";
    private LinkedList<ModificationItem> CURRENT_MODLIST = new LinkedList<>();

    /**
     * IRRChangeLogRestoreServiceReaderThread Contructor class driven.
     *
     * @param CONTROL_THREAD
     * @param cosin          Circular Object Stack for placing DN's on Read Queue.
     * @param cosout         Circular Object Stack for placing LDAP Entries on Output Stack.
     * @param INPUT_PATH     Specified where to Change Logs are Written.
     * @param DN_FILTER_FILE Specifices the File Name which contains
     *                       DN Filters to ignore or bypass these entries to be sent to replicas.
     */
    IRRChangeLogRestoreServiceReaderThread(
            IRRChangeLogRestoreServiceControlThread CONTROL_THREAD,
            CircularObjectStack cosin,
            CircularObjectStack cosout,
            String INPUT_PATH,
            String DN_FILTER_FILE) {

        // ****************************************
        // Set My Incoming Parameters.
        this.CONTROL_THREAD = CONTROL_THREAD;
        this.cosin = cosin;
        this.cosout = cosout;
        this.INPUT_PATH = INPUT_PATH;
        this.DN_FILTER_FILE = DN_FILTER_FILE;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        t = new Thread(this, IRRChangeLogRestoreServiceReaderThread.THREAD_NAME);
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
                MessageConstants.READER_THREAD_ESTABLISHED,
                new String[]{Thread.currentThread().getName(),
                        this.INPUT_PATH});

        // ***********************************************
        // TODO Do more Investigation to determine if
        // we are in a restart mode.


        // ***********************************************
        // Initialize the Exclusion Filter File Object.
        this.prepareFilter();

        // **************************************
        // Loop to process commands from Control
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
                    if (!this.processChangeLogs()) {
                        Thread.sleep(1000);
                    }
                    continue;
                } // End of Nothing in Stack yet to Process.

                // ******************************
                // Did our Running State trip to
                // not based upon a End of Thread
                // command? Close our Loop.
                if (instackcommand.getCommandType() ==
                        StackCommand.CL_END_OF_THREAD) {
                    running = false;
                    continue; // Force our loop to End.
                } // End of Check for End of Thread.

                // *****************************
                // Ok, Interpret the Command.
                switch (instackcommand.getCommandType()) {
                    // ***********************************
                    // Interpret the Process LOGS Command.
                    case StackCommand.CL_PROCESS_LOGS:
                        work_commands_received++;
                        this.unProcessedChangeLogs =
                                (TreeMap) instackcommand.getObject();
                        break;

                    // ******************************************
                    // Interpret the Process Replicate Response.
                    case StackCommand.CL_REPLICATE_RESPONSE:
                        this.receiveResponseEntry(instackcommand);
                        break;

                    // *******************************************
                    // Has our Control Thread requested us to
                    // perform housekeeping for a Primary Instance?
                    case StackCommand.CL_HOUSEKEEPING_BEGIN_PRIMARY:
                        long files_purged = this.performPrimaryHousekeeping();
                        this.cosout.push(new StackCommand(StackCommand.CL_HOUSEKEEPING_DONE,
                                this.CONTROL_THREAD.my_addr,
                                this.CONTROL_THREAD.my_addr,
                                new Long(files_purged)));
                        break;

                    // *******************************************
                    // Has our Control Thread requested us to
                    // perform housekeeping for a PEER Instance?
                    case StackCommand.CL_HOUSEKEEPING_BEGIN_PEER:
                        files_purged = this.performPEERHousekeeping();
                        this.cosout.push(new StackCommand(StackCommand.CL_HOUSEKEEPING_DONE,
                                this.CONTROL_THREAD.my_addr,
                                this.CONTROL_THREAD.my_addr,
                                new Long(files_purged)));
                        break;

                    // *********************************
                    // Interpret the Mark Commands.
                    //
                    // TODO
                    case StackCommand.CL_MARK_POINT:
                    case StackCommand.CL_MARK_AS_PROCESSED:
                        break;

                    // ***********************************
                    // Interpret the Remove Commands.
                    //
                    // TODO
                    case StackCommand.CL_REMOVE_MARK_POINT:
                    case StackCommand.CL_REMOVE_LOG:
                        break;

                    default:
                        // **********************************
                        // Ignore Any Unknown Command Types.
                        FrameworkLogger.log(CLASSNAME, METHODNAME,
                                FrameworkLoggerLevel.WARNING,
                                ErrorConstants.READER_IGNORING_UNKNOWN_STACK_COMMAND);
                } // End of Switch Statement.

                // ************************************
                // Now check to see if there is a file
                // to process.
                this.processChangeLogs();

            } // End of Outer While Loop.
        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.READER_EXCEPTION_IN_MAIN_LOOP,
                    new String[]{e.getMessage()});
            // ******************************
            // Log the Stack Trace.
            this.logStackTrace(e, METHODNAME,
                    ErrorConstants.READER_EXCEPTION_STACKTRACE);
        } finally {

            // ***************************************
            // Show the Lap Timings.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_LAPTIME_READIO,
                    new String[]{LP_ENTRY_READIO.toString()});

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_LAPTIME_WRITEIO,
                    new String[]{LP_ENTRY_WRITEIO.toString()});

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_LAPTIME_FROM_STACK,
                    new String[]{LP_ENTRY_FROM_COS.toString()});

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_LAPTIME_TO_STACK,
                    new String[]{LP_ENTRY_TO_COS.toString()});

            // ***************************************
            // Show the Duration of Thread.
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_THREAD_SHUTDOWN,
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
        // Show Header
        sb.append(WebAdminResponderThread.COMPONENT_ONLINE_BEGIN);
        sb.append("Reader Thread");
        sb.append(WebAdminResponderThread.COMPONENT_ONLINE_END);

        // **************************************
        // Show Detail.
        sb.append(WebAdminResponderThread.BEGIN_TABLE);

        sb.append(WebAdminResponderThread.build2ColumnRow("DNs Read",
                Long.toString(this.DNsread)));

        sb.append(WebAdminResponderThread.build2ColumnRow("DNs Published",
                Long.toString(this.DNspublished)));

        sb.append(WebAdminResponderThread.build2ColumnRow("DNs Blocked By Filter",
                Long.toString(this.DNsBlocked)));

        sb.append(WebAdminResponderThread.build2ColumnRow("Retries Performed",
                Long.toString(this.retries_performed)));

        // ***************************************
        // Show Last Information Processed.
        if (this.LAST_TIME_CHANGEIDENTIFIER_PUBLISHED > 0) {
            this.lasttime.setTimeInMillis(this.LAST_TIME_CHANGEIDENTIFIER_PUBLISHED);
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Time Change Published",
                    this.lasttime.getTime().toString()));
        } // End of If.

        if (this.LAST_CHANGEIDENTIFIER_PUBLISHED != null) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Change Identifier Published",
                    this.LAST_CHANGEIDENTIFIER_PUBLISHED.toString()));
        } // End of If.

        if (this.LAST_DN_PUBLISHED != null) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Last DN Published",
                    this.LAST_DN_PUBLISHED));
        } // End of If.

        if (this.LAST_CHANGE_TYPE_PUBLISHED != null) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Change Type Published",
                    this.LAST_CHANGE_TYPE_PUBLISHED));
        } // End of If.

        if (this.LAST_TIME_CHANGERESPONSEIDENTIFIER_RECEIVED > 0) {
            this.lasttime.setTimeInMillis(this.LAST_TIME_CHANGERESPONSEIDENTIFIER_RECEIVED);
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Time Response Received",
                    this.lasttime.getTime().toString()));
        } // End of If.

        if (this.LAST_CHANGERESPONSEIDENTIFIER_RECEIVED != null) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Last Change Identifier Received",
                    this.LAST_CHANGERESPONSEIDENTIFIER_RECEIVED.toString()));
        } // End of If.

        // **********************************
        // Show Laps.
        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time File Read IO",
                LP_ENTRY_READIO.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time File Write IO",
                LP_ENTRY_WRITEIO.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Message From Input Stack",
                LP_ENTRY_FROM_COS.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Messages To Output Stack",
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
        // ***********************************************
        // Initialize my LAP Timers
        this.LP_ENTRY_READIO.Reset();
        this.LP_ENTRY_WRITEIO.Reset();
        this.LP_ENTRY_TO_COS.Reset();
        this.LP_ENTRY_FROM_COS.Reset();

        // ************************************
        // Global Thread Counters
        this.DNsread = 0;
        this.DNspublished = 0;
        this.DNsBlocked = 0;
        this.ready_commands_sent = 0;
        this.work_commands_received = 0;
        this.retries_performed = 0;

        // ***********************************************
        // Clear Last Saved Information.
        this.LAST_TIME_CHANGEIDENTIFIER_PUBLISHED = 0;
        this.LAST_CHANGEIDENTIFIER_PUBLISHED = null;
        this.LAST_DN_PUBLISHED = null;
        this.LAST_CHANGE_TYPE_PUBLISHED = null;
        this.LAST_TIME_CHANGERESPONSEIDENTIFIER_RECEIVED = 0;
        this.LAST_CHANGERESPONSEIDENTIFIER_RECEIVED = null;

    } // End of resetStatistics protected method.

    /**
     * Get Current Component Status.
     *
     * @return String Data Representing Current Status of this Component.
     */
    protected static String getOfflineStatus() {
        StringBuffer sb = new StringBuffer();

        // **************************************
        // Show Header
        sb.append(WebAdminResponderThread.COMPONENT_OFFLINE_BEGIN);
        sb.append("Reader Thread");
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
     * Helper method to receive a response to our published change.
     * If the response is received by all members,
     * then we can simply state this replication unit is complete.
     */
    private boolean receiveResponseEntry(StackCommand instackcommand) {
        // *****************************
        // Initialize
        final String METHODNAME = "receiveResponseEntry";
        StopWatch sw = new StopWatch();
        sw.start();

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.READER_RECEIVE_RESPONSE_ENTRY,
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
                        ErrorConstants.READER_CORRUPTED_STACK_COMMAND,
                        new String[]{instackcommand.toString()});
                return false;
            } // End of Validation Check.

            // *********************************
            // Cast Identifer for easy access.
            ChangeIdentifier change_identifier
                    = instackcommand.getIdentifier();

            // ******************************************
            // Cast Response Identifier for easy access.
            ChangeResponseIdentifier cri
                    = (ChangeResponseIdentifier) instackcommand.getObject();

            // ******************************************
            // Save Statistic Information for Last CRI
            // Received.
            this.LAST_TIME_CHANGERESPONSEIDENTIFIER_RECEIVED
                    = System.currentTimeMillis();
            this.LAST_CHANGERESPONSEIDENTIFIER_RECEIVED = cri;

            // ***********************************************
            // Now obtain our Status Object for the overall
            // change log, if we have no Change Log Status Object
            // then Create one for use.
            ChangeIdentifierKey cik = new ChangeIdentifierKey(change_identifier);
            ChangeLogStatus changelogstatus
                    = (ChangeLogStatus) this.ChangeLogs.get(cik.getChangeLogIdentifierKey());
            if (changelogstatus == null) {
                changelogstatus = new ChangeLogStatus(cik.getOriginator(),
                        cik.getLogFileName());

                // ****************************************
                // Create a new ChangeLogStatusElement
                // for our Self Response.
                ChangeLogStatusElement clse =
                        new ChangeLogStatusElement(this.CONTROL_THREAD.my_addr.toString(),
                                cri.getLogFileName(),
                                cri.getChangeNumberWithinLog());

                // ****************************************
                // Add our Response Position.
                clse.member_response_position.add(
                        this.CONTROL_THREAD.my_addr.toString());

                // ***************************************
                // Add our response, positive of course,
                // since we have now at least one other
                // response.
                clse.member_responses.put(this.CONTROL_THREAD.my_addr.toString(),
                        new ChangeResponseIdentifier(
                                change_identifier,
                                this.CONTROL_THREAD.my_addr,
                                false, true, "Self Response"));

                changelogstatus.changelog_elements.put(new Integer(clse.getChangeNumberWithinLog()),
                        clse);

            } // End of an Intial ChangeLog.

            // ****************************************
            // Now get our ChangeElement status for this
            // Change Number within the Log.
            ChangeLogStatusElement clse =
                    (ChangeLogStatusElement) changelogstatus.changelog_elements.get(
                            new Integer(cri.getChangeNumberWithinLog()));
            if (clse == null) {
                clse =
                        new ChangeLogStatusElement(cri.getOriginator(),
                                cri.getLogFileName(),
                                cri.getChangeNumberWithinLog());
            } // End of Change Element Check.

            // *********************************
            // Now Update our Change Identifier.
            clse.member_response_position.add(
                    cri.getReplicator().toString());
            clse.member_responses.put(
                    cri.getReplicator().toString(), cri);

            changelogstatus.changelog_elements.put(new Integer(clse.getChangeNumberWithinLog()),
                    clse);

            this.ChangeLogs.put(cik.getChangeLogIdentifierKey(),
                    changelogstatus);

            // *******************************
            // Now obtain our original
            // Identifier.
            ChangeIdentifier original_change_identifier
                    = (ChangeIdentifier) this.publishedChangeLogs.get(cik);
            if (original_change_identifier == null) {
                original_change_identifier = change_identifier;
            }

            // ***********************************************
            // Now show some logging for this single response.
            if (cri.wasChangeSuccessful()) {
                // ********************************************
                // Signify change Successful.
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED,
                        new String[]{cik.toString(),
                                original_change_identifier.toString(),
                                cri.getReplicator().toString()});
            } // End of Inner if.

            // ***********************************************
            // Check to see if this change was blocked
            // by the end PEER.
            else if ((cri.wasChangeBlocked())) {

                // ********************************************
                // Signify that the change was blocked.
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED_WAS_BLOCKED,
                        new String[]{cik.toString(),
                                original_change_identifier.toString(),
                                cri.getReplicator().toString()});
            } // End of else if.
            // ***********************************************
            // Check to see if this change was Ignored due to
            // an issue at that PEER, but we should simply
            // ignore and account as the change was played,
            // not successful, but we can ignore.  This can be
            // anothing from a bad command going across to
            // an add for an Entry that already exists.
            // Most issues which are ignored are generally
            // classified as those which could have been
            // successful but were not due to a different
            // state of the entry.  This can occur due to certain
            // Entries and trees being blocked in some way.  Blocking
            // can have a cascade effect on what does and does not get
            // performed.
            else if ((cri.wasChangeIgnored())) {

                // ********************************************
                // Signify that the change was blocked.
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED_WAS_IGNORED_BY_PEER,
                        new String[]{cik.toString(),
                                original_change_identifier.toString(),
                                cri.getReplicator().toString(),
                                cri.getChangeFinalNotation()});
            } // End of else if.
            // ************************************************
            // Change was not replicated successfully,
            // determine if we need to retry it since the exception
            // was not flagged as Ignored by the PEER.
            // Drats, ok we need to dip into the change and fire it
            // again.
            //
            else {

                // ********************************************
                // Signify that change is not successful.
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED_WAS_UNSUCCESSFUL,
                        new String[]{cik.toString(),
                                original_change_identifier.toString(),
                                cri.getReplicator().toString()});

                // *********************************
                // Issue the Retry.
                if (this.performChangeLogRetry(cik,
                        changelogstatus,
                        cri.getReplicator(),
                        original_change_identifier)) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                            MessageConstants.READER_CHANGE_LOG_ENTRY_RESPONSE_WILL_BE_RETRIED);
                } else {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                            MessageConstants.READER_CHANGE_LOG_ENTRY_RESPONSE_RETRY_IGNORED);
                } // End of Else.

            } // End of Else.

            // ***************************************
            // Now determine if this change was fully
            // processed.  If not we stop here.
            // Possibly a new response if waiting to
            // be processed.  If we did process the
            // entire change log element, remove from
            // published List.
            if (!changelogstatus.determineState(original_change_identifier)) {
                return false;
            } else {
                this.publishedChangeLogs.remove(cik);
                this.publishedChanges.remove(cik);
            } // End of Else to check for processed logs.

            // ***************************************
            // Now determine if every change to all
            // PEERs have been processed, simply
            // by checking to see if all
            // Published Log File References
            // have been removed from our Map.
            //
            Set eset = this.publishedChangeLogs.keySet();
            Iterator itr = eset.iterator();
            boolean logcomplete = true;
            while (itr.hasNext()) {
                ChangeIdentifierKey pcik = (ChangeIdentifierKey) itr.next();
                if (pcik.getLogFileName().equalsIgnoreCase(cik.getLogFileName())) {
                    logcomplete = false;
                }
            } // End of While Loop.

            // **********************************
            // Check to see if we are still
            // Waiting?
            if (!logcomplete) {
                return false;
            }

            // **************************************
            // At this point we have a successfully
            // completed Log, so pull it from our
            // Reference Map.
            this.ChangeLogs.remove(cik.getChangeLogIdentifierKey());

            // **************************************
            // Now We can persist our state for this
            // Change Log File.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.READER_LOG_COMPLETED);
            return checkpointChangeLogFile(changelogstatus);
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of receiveResponseEntry private Method.


    /**
     * Helper method to queue up a retry for a specific published
     * change element within a log.
     *
     * @param cik
     * @param changelogstatus
     * @return
     */
    private boolean performChangeLogRetry(ChangeIdentifierKey cik,
                                          ChangeLogStatus changelogstatus,
                                          Object destination,
                                          ChangeIdentifier change_identifier) {

        // *****************************
        // Initialize
        final String METHODNAME = "performChangeLogRetry";
        StopWatch sw = new StopWatch();
        sw.start();

        try {

            if (destination != null) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                        MessageConstants.READER_PERFORMING_RETRY_TO_DESTINATION,
                        new String[]{destination.toString(), cik.toString()});
            } else {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                        MessageConstants.READER_PERFORMING_RETRY_TO_ALL,
                        new String[]{cik.toString()});
            } // End of Else logging.

            // ************************************
            // First Obtain our Change Log Element
            // from the Published Changes.
            Object[] carray =
                    (Object[]) this.publishedChanges.get(cik);
            if (carray == null) {
                this.dismissChangeLogElement(cik, changelogstatus,
                        change_identifier, destination);
                this.publishedChanges.remove(cik);
                return false;
            } // End of Check for valid Change Element.

            // **************************************
            // Verify we have the proper number of
            // Elements.
            if (carray.length != 3) {
                this.dismissChangeLogElement(cik, changelogstatus,
                        change_identifier, destination);
                this.publishedChanges.remove(cik);
                return false;
            } // End of Validity Check.

            // ******************************************
            // Now unWrap the Object Array.
            // The "change" object can be a
            // Attributes or ModificationItemList[]
            // Object depending upon changetype, but
            // we do not need to care about that.
            String current_dn = (String) carray[0];
            String changetype = (String) carray[1];
            Object change = carray[2];

            // ******************************************
            // Save Statistic Information for Last CI
            // Published.
            this.LAST_TIME_CHANGEIDENTIFIER_PUBLISHED
                    = System.currentTimeMillis();
            this.LAST_CHANGEIDENTIFIER_PUBLISHED = change_identifier;
            this.LAST_DN_PUBLISHED = current_dn;
            this.LAST_CHANGE_TYPE_PUBLISHED =
                    LDIF_MODIFY_CHANGETYPE;

            // *************************************
            // Build the Stack Command.
            // Destination is NULL, since this is a
            // broadcast to all PEERs.
            this.cosout.push(new StackCommand(StackCommand.CL_REPLICATE,
                    new String[]
                            {current_dn,
                                    changetype},
                    this.CONTROL_THREAD.my_addr,
                    destination,
                    change_identifier,
                    change));
            this.DNspublished++;
            this.retries_performed++;

            // **********************
            // Return Indicating we
            // Published.
            return true;

            // *************************************
            // Final Processing.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing.
    } // End of performChangeLogRetry private method.

    /**
     * Helper Method to Dismiss a Change Log Element.
     *
     * @param cik
     * @param changelogstatus
     * @param change_identifier
     * @param destination
     */
    private void dismissChangeLogElement(ChangeIdentifierKey cik,
                                         ChangeLogStatus changelogstatus,
                                         ChangeIdentifier change_identifier,
                                         Object destination) {

        // *****************************
        // Initialize
        final String METHODNAME = "dismissChangeLog";
        StopWatch sw = new StopWatch();
        sw.start();

        // *****************************
        // Dismiss the Change Log.
        try {

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.READER_DISMISSING_CHANGE,
                    new String[]{cik.toString()});

            // ****************************************
            // Now get our ChangeElement status for this
            // Change Number within the Log.
            ChangeLogStatusElement clse =
                    (ChangeLogStatusElement) changelogstatus.changelog_elements.get(
                            new Integer(cik.getChangeNumberWithinLog()));
            if (clse == null) {
                return;
            }

            // *********************************
            // Now Update our Change Identifier.
            if (destination == null) {
                for (int i = 0; i < change_identifier.getMembersAtTimeOfChangeSent().size(); i++) {
                    Object member = change_identifier.getMembersAtTimeOfChangeSent().get(i);
                    clse.member_responses.put(
                            destination.toString(),
                            new ChangeResponseIdentifier(change_identifier,
                                    member,
                                    ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                                    ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                                    ChangeResponseIdentifier.CHANGE_IGNORED,
                                    "Change Dismissed"));
                } // End of Inner For Loop.
            } else {
                clse.member_responses.put(
                        destination.toString(),
                        new ChangeResponseIdentifier(change_identifier,
                                destination,
                                ChangeResponseIdentifier.CHANGE_NOT_BLOCKED,
                                ChangeResponseIdentifier.CHANGE_NOT_SUCCESSFUL,
                                ChangeResponseIdentifier.CHANGE_IGNORED,
                                "Change Dismissed"));
            } // End of Else.

            // **********************************
            // Update the Changelogstatus with the
            // Element Entry we just updated.
            changelogstatus.changelog_elements.put(new Integer(clse.getChangeNumberWithinLog()),
                    clse);

            // *************************************
            // Final Processing.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing.
    } // End of dismissChangeLog private Method.

    /**
     * Helper Method to CheckPoint a Change Log File.  Basically we
     * create a new State File for this Log File to indicate it has been
     * processed.  Housekeeping will then come along a clean it up later.
     *
     * @param changelogstatus
     * @return
     */
    private boolean checkpointChangeLogFile(ChangeLogStatus changelogstatus) {
        // *******************************
        // Initialize.
        final String METHODNAME = "checkpointChangeLogFile";
        StopWatch sw = new StopWatch();
        sw.start();

        // *******************************
        // Indicate what we about to do.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.READER_CHECKPOINTING_LOG_FILE_AS_COMPLETE,
                new String[]{changelogstatus.getLogFileName()});

        // *******************************
        // CheckPoint a State file
        // indicating this file
        // has been processed.
        IRRChangeLogRestoreStateFile STATEFILE =
                new IRRChangeLogRestoreStateFile(
                        IRRChangeLogFileUtility.getProcessedFile(changelogstatus),
                        changelogstatus);

        // *******************************
        // Write Stats to the State File
        // for later review.
        try {
            STATEFILE.persistAsProcessed();

            // **********************
            // Clean-up.
            STATEFILE = null;

            // ******************************
            // Return.
            return true;
        } catch (IOException ioe) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.READER_IOEXCEPTION_WRITING_STATUS_FILE,
                    new String[]{STATEFILE.toString(), ioe.getMessage()});
            return false;
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of checkpointStateFile private Method.

    /**
     * Helper Method to CheckPoint a Change Log File.  Basically we
     * create a new State File for this Log File to indicate it has been
     * processed.  Housekeeping will then come along a clean it up later.
     *
     * @param change_log_filename
     * @return boolean
     */
    private boolean checkpointBlockedChangeLogFile(String change_log_filename) {
        // *******************************
        // Initialize.
        final String METHODNAME = "checkpointBlockedChangeLogFile";
        StopWatch sw = new StopWatch();
        sw.start();

        // *******************************
        // Indicate what we about to do.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.READER_CHECKPOINTING_LOG_FILE_AS_BLOCKED,
                new String[]{change_log_filename});

        // *******************************
        // CheckPoint a State file
        // indicating this file
        // has been processed.
        IRRChangeLogRestoreStateFile STATEFILE =
                new IRRChangeLogRestoreStateFile(
                        IRRChangeLogFileUtility.getBlockedFile(change_log_filename),
                        null);

        // *******************************
        // Write Stats to the State File
        // for later review.
        try {
            STATEFILE.persistAsBlocked();

            // **********************
            // Clean-up.
            STATEFILE = null;

            // ******************************
            // Return.
            return true;
        } catch (IOException ioe) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.READER_IOEXCEPTION_WRITING_STATUS_FILE,
                    new String[]{STATEFILE.toString(), ioe.getMessage()});
            return false;
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Method Final Processing.
    } // End of checkpointBlockedStateFile private Method.

    /**
     * Process a Map of Change Log Files, we will mark and
     * apply an Identifer for each change and drive the
     * processChangeLog method for each file we have to process.
     */
    private boolean processChangeLogs() {

        // *****************************************
        // Initialize.
        final String METHODNAME = "processChangeLogs";
        Object logkey = null;
        StopWatch sw = new StopWatch();
        sw.start();
        boolean processed = false;

        // This is Message will be too verbose for production use,
        // even development.
        //FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
        //          MessageConstants.ENTERING_METHOD, new String[] { METHODNAME});

        try {
            // *****************************************
            // Check to see if we have anything in our
            // incoming Map.
            if ((this.unProcessedChangeLogs == null) ||
                    (this.unProcessedChangeLogs.isEmpty())) {
                // ***********************************
                // Now determine if we are finished
                // with our current Map of files being
                // committed.
                // Also, make sure we received all
                // previous work requested.
                //
                if ((this.publishedChangeLogs.isEmpty()) &&
                        (this.ChangeLogs.isEmpty()) &&
                        (this.work_commands_received ==
                                this.ready_commands_sent)) {

                    // ***********************************************
                    // Ok at this point everything is clean.
                    // Now perform some housekeeping to remove all of
                    // those processed files.
                    this.performPrimaryHousekeeping();

                    // ***********************************************
                    // Auto Check Point.
                    // TODO

                    // ***********************************************
                    // Indicate to our Control Thread I am Ready to
                    // Perform Work.
                    this.ready_commands_sent++;
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                            MessageConstants.READER_READY_TO_PERFORM_WORK,
                            new String[]{Long.toString(this.ready_commands_sent)});

                    this.cosout.push(new StackCommand(StackCommand.CL_READER_READY,
                            this.CONTROL_THREAD.my_addr,
                            this.CONTROL_THREAD.my_addr,
                            Long.toString(this.ready_commands_sent)));
                } // End of Check for all Published/Consumed/Replicated.

                // *********************************************************************
                // *********************************************************************

                // ****************************************************
                //
                // Check for anything stuck or our current settings...
                // We can not move on until we
                // have received all our responses up to
                // this point.
                //
                // TODO Enable the ability to flush or
                // Proceed with next change to discontinue
                // this change we are still waiting on a response for.
                //
                // *****************************************************

                // *********************************************************************
                // *********************************************************************

                // ************************************
                // Return indicating nothing processed
                return processed;
            } // End of Check for anything to process.

            // ********************************************
            // Now Pop one off the stack and allow it to be
            // processed.
            logkey = this.unProcessedChangeLogs.firstKey();
            this.processChangeLog((File)
                    this.unProcessedChangeLogs.get(logkey));

            // ********************************
            // Now remove this file from the
            // Unprocessed queue.
            this.unProcessedChangeLogs.remove(logkey);

            // *********************************
            // Ok now before we continue,
            // determine if the entire Change Log
            // Was Blocked or not.  Simply check to
            // see if we have at least a one entry in
            // the Published Map, if so the file was not
            // blocked.  If not in Map, then yes the
            // entry file was blocked.
            //
            Set pset = this.publishedChangeLogs.keySet();
            Iterator pitr = pset.iterator();
            boolean entire_file_blocked = true;
            while (pitr.hasNext()) {
                ChangeIdentifier change_identifier =
                        (ChangeIdentifier) pitr.next();
                if (change_identifier.getLogFileName().equalsIgnoreCase((String) logkey)) {
                    entire_file_blocked = false;
                }
            } // End of While Loop.

            // ***********************************
            // Now if the entire file was blocked
            // Create the State File for it.
            if (entire_file_blocked) {
                this.checkpointBlockedChangeLogFile((String) logkey);
                return false;
            } // End of Check for entire file Blocked.

            // *******************
            // return our Process
            // Indicator.
            processed = true;
            return processed;

        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.READER_EXCEPTION_PROCESSING_LOG,
                    new String[]{logkey.toString(), e.getMessage()});

            // ******************************
            // Log the Stack Trace.
            this.logStackTrace(e, METHODNAME,
                    ErrorConstants.READER_EXCEPTION_STACKTRACE);
            return false;
        } finally {
            // *******************
            // Final Processing.
            sw.stop();
            if (processed) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.FINALIZING_METHOD,
                        new String[]{METHODNAME, sw.getElapsedTimeString()});
            } // Only Show finalize time when we processed something.
        } // End of Exception and Final Processing.
    } // End of processChangeLogs private Method.

    /**
     * Process Change Log, will read in Change Log File and
     * convert to an Attributes or ModificationItem List
     * Objects to be passed to our PEERs to perform the change.
     */
    private void processChangeLog(File logfile) throws Exception {

        // ****************************************
        // Read the Provided LDIF Input File and
        // turn this Object into a Stack Command to
        // be published.
        final String METHODNAME = "processChangeLog";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.READER_PROCESSING_CHANGE_LOG,
                new String[]{logfile.getAbsoluteFile().toString()});

        // ****************************************
        // Note The Start Time.
        StopWatch sw = new StopWatch();

        // *****************************************
        // Initialize.
        BufferedReader LDIFIN = null;
        Attributes current_entry_attributes = null;
        String current_dn = null;
        String IDXCHANGETYPE = null;
        int change_number_within_changelog = 0;

        // *****************************************
        // Catch our Exceptions.
        try {
            // *****************************************
            // Open up the Input Stream from a File.
            LDIFIN = new BufferedReader(
                    new FileReader(logfile), 16384);

            // ******************************************
            // Obtain our Reader Instance with our Input.
            idxLDIFReader ldif = new idxLDIFReader(LDIFIN);

            // ******************************************
            // Process the Entire Input Stream.
            while (ldif.hasMore()) {

                current_entry_attributes = ldif.getNextModEntry();
                current_dn = ldif.getCurrentDN();
                this.DNsread++;

                // ****************************************************
                // Check for a Pending Modification to Process.
                if ((!CURRENT_MODLIST.isEmpty()) &&
                        (!CURRENT_MODDN.equalsIgnoreCase(current_dn))) {
                    this.process_pending_modification(logfile,
                            change_number_within_changelog);
                }

                // ****************************************************
                // Skip, if nothing here, could be a seperator.
                //
                if ((current_dn == null) ||
                        (current_dn.equals("")) ||
                        (current_entry_attributes == null)) {
                    continue;
                }

                // ****************************************************
                // Obtain Modification Control Information for Entry.
                //
                Attribute myattr = current_entry_attributes.get("changetype");
                if (myattr != null) {
                    Object myValue = myattr.get();
                    IDXCHANGETYPE = ((String) myValue).trim();
                    current_entry_attributes.remove("changetype");

                    // ***************************************
                    // If we have a changetype and we have
                    // something pending, then that indicates that
                    // we need to process the new change before
                    // we move on.
                    //
                    if (!CURRENT_MODLIST.isEmpty()) {
                        this.process_pending_modification(logfile,
                                change_number_within_changelog);
                    }
                    change_number_within_changelog++;
                } else if (!CURRENT_MODLIST.isEmpty()) {
                    // **************************************
                    // If I have a Pending MODLIST,
                    // Assume we are still in that Modification
                    // for the Entry.
                    IDXCHANGETYPE = LDIF_MODIFY_CHANGETYPE;
                } else {
                    // **************************************
                    // Assume Changetype is always Add,
                    // if there are no pending Mods.
                    IDXCHANGETYPE = LDIF_ADD_CHANGETYPE;
                    change_number_within_changelog++;
                } // End of Else.

                // ****************************************************
                // Now either queue Entry if a Modify Entry or for
                // anything else, simple send the change to our group
                // PEERs.
                if (IDXCHANGETYPE.equalsIgnoreCase(LDIF_MODIFY_CHANGETYPE)) {
                    queueEntry(current_dn, current_entry_attributes);
                } else {
                    sendEntry(current_dn, current_entry_attributes, IDXCHANGETYPE, logfile,
                            change_number_within_changelog);
                } // End of Else Check.
            } // End of LDIF Input While Loop.

            // ****************************************************
            // Check for any final Pending Modification to Process.
            if (!CURRENT_MODLIST.isEmpty()) {
                this.process_pending_modification(logfile,
                        change_number_within_changelog);
            }

            // *********************************
            // Exception and Final Processing.
        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.READER_EXCEPTION_PROCESSING_LDIF_INPUT,
                    new String[]{logfile.getAbsoluteFile().toString(), e.getMessage()});
            // ******************************
            // Log the Stack Trace.
            this.logStackTrace(e, METHODNAME,
                    ErrorConstants.READER_EXCEPTION_STACKTRACE);
            throw e;
        } finally {
            // ***************************************
            // Close our Input File.
            try {
                if (LDIFIN != null) {
                    LDIFIN.close();
                }
            } catch (Exception e) {
                //IDXLOG.severe(CLASSNAME,METHODNAME,"Exception closing LDIF Input. " + e);
            } // End of exception
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_PROCESSED_CHANGE_LOG,
                    new String[]{logfile.getAbsoluteFile().toString(),
                            Integer.toString(change_number_within_changelog),
                            sw.getElapsedTimeString()});
        } // End of Final Processing.
    } // End of processChangeLog Private Method.

    /**
     * queueEntry, Performs Modifications on Entry.
     *
     * @param current_dn               Current DN.
     * @param current_entry_attributes Current set of Modification Attributes.
     * @throws Exception for any unrecoverable errors during function.
     */
    private void queueEntry(String current_dn,
                            Attributes current_entry_attributes)
            throws Exception {

        // ******************************
        // Initialize.
        final String METHODNAME = "queueEntry";
        StopWatch sw = new StopWatch();

        // *********************************
        // Only Show the Processing of the
        // Modify for the Entry once.
        if ((CURRENT_MODDN == null) ||
                (CURRENT_MODDN.equalsIgnoreCase(""))) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_QUEUEING_MODIFICATION,
                    new String[]{current_dn});
        } // End of Check for Existing Current DN.

        try {
            // ****************************************************
            // Ok, now we have a valid changetype for Entry,
            // now determine which attributes are added, replaced
            // or deleted.  Obtain all of the maintenance Contructs.
            //
            Attribute Attribute_Deletions = current_entry_attributes.get(LDIF_ATTRIBUTE_DELETE);
            current_entry_attributes.remove(LDIF_ATTRIBUTE_DELETE);
            Attributes Value_Deletions = obtainModifications(Attribute_Deletions, current_entry_attributes, true);

            Attribute Attribute_Replacements = current_entry_attributes.get(LDIF_ATTRIBUTE_REPLACE);
            current_entry_attributes.remove(LDIF_ATTRIBUTE_REPLACE);
            Attributes Value_Replacements = obtainModifications(Attribute_Replacements, current_entry_attributes, false);

            Attribute Attribute_Additions = current_entry_attributes.get(LDIF_ATTRIBUTE_ADD);
            current_entry_attributes.remove(LDIF_ATTRIBUTE_ADD);
            Attributes Value_Additions = obtainModifications(Attribute_Additions, current_entry_attributes, false);

            // *******************************************
            // Setup the Deletions.
            if (Value_Deletions != null) {
                CURRENT_MODDN = current_dn;
                setupModifications(DirContext.REMOVE_ATTRIBUTE, Value_Deletions);
            } // End of If.

            // *******************************************
            // Setup the Replacements.
            if (Value_Replacements != null) {
                CURRENT_MODDN = current_dn;
                setupModifications(DirContext.REPLACE_ATTRIBUTE, Value_Replacements);
            } // End of If.

            // *******************************************
            // Setup the Additions.
            if (Value_Additions != null) {
                CURRENT_MODDN = current_dn;
                setupModifications(DirContext.ADD_ATTRIBUTE, Value_Additions);
            } // End of If.

            // *************************************
            // Final Processing.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing.
    } // End of queueEntry method.

    /**
     * process pending modifications that were sequentially queued to process in bluk.
     *
     * @param logfile File Object associated with this modification.
     * @throws Exception for any unrecoverable errors during function.
     */
    private void process_pending_modification(File logfile,
                                              int change_number_within_changelog)
            throws Exception {

        final String METHODNAME = "process_pending_modification";
        StopWatch sw = new StopWatch();

        // ****************************
        // Count the Entries Processed
        ///entries_processed++;

        try {
            // ****************************
            // Check the Queue.
            if ((CURRENT_MODDN == null) ||
                    (CURRENT_MODDN.equalsIgnoreCase("")) ||
                    (CURRENT_MODLIST.isEmpty())) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.READER_NO_PENDING_MODIFICATION,
                        new String[]{this.CURRENT_MODDN});
                resetModList();
                return;
            } // End of Pending Check.

            // **********************************
            // Indicate we are Processing it.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_PROCESSING_QUEUED_MODIFICATION,
                    new String[]{this.CURRENT_MODDN});

            // ****************************************************
            // Now Create a new Modification Item List for the
            // Entry and then process it.
            ModificationItem[] EntryModSet =
                    new ModificationItem[CURRENT_MODLIST.size()];

            // ****************************************************
            // Clear some counters.
            int attributes_modified = 0;
            int attributes_deleted = 0;
            int attributes_added = 0;

            // *******************************************
            // Iterate through the List List for the
            // Entry to count and get some stats for this
            // Modification.
            for (int modItem = 0; modItem < CURRENT_MODLIST.size(); modItem++) {
                EntryModSet[modItem] = (ModificationItem) CURRENT_MODLIST.get(modItem);

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
            // Now Send Entry to our PEERs.
            this.sendEntry(CURRENT_MODDN, EntryModSet, logfile, change_number_within_changelog);

            // **********************************
            // Clear Modification List.
            resetModList();

            // *************************************
            // Final Processing.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing.
    } // End of process_pending_modification method.

    /**
     * resetModList, clears and resets Mod List for next possible Entry Modification.
     */
    private void resetModList() {
        // ****************************
        // Clear the LinkedList and DN.
        CURRENT_MODDN = "";
        CURRENT_MODLIST.clear();
    } // End of resetModList Method.

    /**
     * Provides Helper Method to Send our Change over our JGroups Bus.
     *
     * @param current_dn
     * @param current_entry_attributes
     * @param logfile                  File Object associated with this Change.
     */
    private boolean sendEntry(String current_dn,
                              Attributes current_entry_attributes,
                              String changetype,
                              File logfile,
                              int changenumberwithinfile) {
        // *****************************
        // Initialize
        final String METHODNAME = "sendEntry";
        StopWatch sw = new StopWatch();

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.READER_SENDING_ENTRY,
                new String[]{current_dn, changetype,
                        logfile.getAbsoluteFile().toString(),
                        Integer.toString(changenumberwithinfile)});
        try {
            // ************************************
            // Check for Null Incoming Parameters.
            if ((current_dn == null) ||
                    (current_dn.trim().equalsIgnoreCase("")) ||
                    (current_entry_attributes == null)) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        ErrorConstants.READER_WILL_NOT_SEND_NULL_ENTRY);
                return false;
            } // End of Check for No Current DN.

            // ************************************
            // Now Filter the DN to see if we have a
            // Match to Exclude this change from
            // being Distributed.
            if (this.dn_exclusion_filter.match(current_dn.toLowerCase() + ":" + changetype)) {
                this.DNsBlocked++;
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.READER_BLOCKING_ENTRY,
                        new String[]{changetype,
                                current_dn,
                                logfile.getAbsoluteFile().toString(),
                                Integer.toString(changenumberwithinfile)});

                // ****************************
                // Indicate we did not publish.
                return false;
            } // End of Check to Block Entry from being Sent.

            // *************************************
            // Build Log Identifier and Key.
            ChangeIdentifier change_identifier
                    = new ChangeIdentifier(this.CONTROL_THREAD.my_addr,
                    this.CONTROL_THREAD.members,
                    logfile.getAbsolutePath(),
                    changenumberwithinfile);

            ChangeIdentifierKey cik
                    = new ChangeIdentifierKey(change_identifier);

            // **************************************
            // Save this Information for use when we
            // get popped when a PEER has successfully
            // processed the Modification.
            this.publishedChangeLogs.put(cik, change_identifier);

            // **************************************
            // Now Save the change information if we
            // need it for a subsequent retry.
            this.publishedChanges.put(cik, new Object[]{current_dn,
                    changetype,
                    current_entry_attributes});

            // ******************************************
            // Save Statistic Information for Last CI
            // Published.
            this.LAST_TIME_CHANGEIDENTIFIER_PUBLISHED
                    = System.currentTimeMillis();
            this.LAST_CHANGEIDENTIFIER_PUBLISHED = change_identifier;
            this.LAST_DN_PUBLISHED = current_dn;
            this.LAST_CHANGE_TYPE_PUBLISHED = changetype;

            // *************************************
            // Build the Stack Command.
            // Destination is NULL, since this is a
            // broadcast to all PEERs.
            this.cosout.push(new StackCommand(StackCommand.CL_REPLICATE,
                    new String[]{current_dn, changetype},
                    this.CONTROL_THREAD.my_addr,
                    null,
                    change_identifier,
                    current_entry_attributes));
            this.DNspublished++;

            // ************************************************************
            // Serialize the Replication Request ourselves to ensure a Mark
            // Checkpoint available.
            // TODO


            // **********************
            // Return Indicating we
            // Published.
            return true;

            // *************************************
            // Final Processing.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing.
    } // End of sendEntry Private Method.


    /**
     * Provides Helper Method to Send our Change over our JGroups Bus.
     *
     * @param current_dn
     * @param EntryModSet Directory Modification item List that was Queued.
     * @param logfile     File Object associated with this Change.
     */
    private boolean sendEntry(String current_dn,
                              ModificationItem[] EntryModSet,
                              File logfile,
                              int changenumberwithinfile) {
        // *****************************
        // Initialize
        final String METHODNAME = "sendEntry";
        StopWatch sw = new StopWatch();

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.READER_SENDING_ENTRY,
                new String[]{current_dn,
                        LDIF_MODIFY_CHANGETYPE,
                        logfile.getAbsoluteFile().toString(),
                        Integer.toString(changenumberwithinfile)});
        try {
            // ************************************
            // Check for Null Incoming Parameters.
            if ((current_dn == null) ||
                    (current_dn.trim().equalsIgnoreCase("")) ||
                    (EntryModSet == null) ||
                    (EntryModSet.length == 0)) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        ErrorConstants.READER_WILL_NOT_SEND_NULL_ENTRY);
                return false;
            } // End of Check for a Null Modification Set or other Null attribute.

            // ************************************
            // Now Filter the DN to see if we have a
            // Match to Exclude this change from
            // being Distributed.
            if (this.dn_exclusion_filter.match(current_dn.toLowerCase() + ":" +
                    LDIF_MODIFY_CHANGETYPE)) {
                this.DNsBlocked++;
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.READER_BLOCKING_ENTRY,
                        new String[]{LDIF_MODIFY_CHANGETYPE,
                                current_dn,
                                logfile.getAbsoluteFile().toString(),
                                Integer.toString(changenumberwithinfile)});

                // ****************************
                // Indicate we did not publish.
                return false;
            } // End of Check to Block Entry from being Sent.

            // *************************************
            // Build Log Identifier and Key.
            ChangeIdentifier change_identifier
                    = new ChangeIdentifier(this.CONTROL_THREAD.my_addr,
                    this.CONTROL_THREAD.members,
                    logfile.getAbsolutePath(),
                    changenumberwithinfile);

            ChangeIdentifierKey cik
                    = new ChangeIdentifierKey(change_identifier);

            // **************************************
            // Save this Information for use when we
            // get popped when a PEER has successfully
            // processed the Modification.
            this.publishedChangeLogs.put(cik, change_identifier);

            // **************************************
            // Now Save the change information if we
            // need it for a subsequent retry.
            this.publishedChanges.put(cik, new Object[]{current_dn,
                    LDIF_MODIFY_CHANGETYPE,
                    EntryModSet});

            // ******************************************
            // Save Statistic Information for Last CI
            // Published.
            this.LAST_TIME_CHANGEIDENTIFIER_PUBLISHED
                    = System.currentTimeMillis();
            this.LAST_CHANGEIDENTIFIER_PUBLISHED = change_identifier;
            this.LAST_DN_PUBLISHED = current_dn;
            this.LAST_CHANGE_TYPE_PUBLISHED =
                    LDIF_MODIFY_CHANGETYPE;

            // *************************************
            // Build the Stack Command.
            // Destination is NULL, since this is a
            // broadcast to all PEERs.
            this.cosout.push(new StackCommand(StackCommand.CL_REPLICATE,
                    new String[]
                            {current_dn,
                                    LDIF_MODIFY_CHANGETYPE},
                    this.CONTROL_THREAD.my_addr,
                    null,
                    change_identifier,
                    EntryModSet));
            this.DNspublished++;

            // ************************************************************
            // Serialize the Replication Request ourselves to ensure a Mark
            // Checkpoint available.
            // TODO

            // **********************
            // Return Indicating we
            // Published.
            return true;

            // *************************************
            // Final Processing.
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing.
    } // End of sendEntry Private Method.

    /**
     * obtainModifications, Obtains Modifications and places into Attributes Object
     * for processing.
     *
     * @param _modattr           Modifcation Attribute Driver.
     * @param _entryattrs        Complete set of Modifications.
     * @param _theseAreDeletions Indicates Modifications are deletions or not.
     * @return Attributes Generated based only on incoming Attribute.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private Attributes obtainModifications(Attribute _modattr,
                                           Attributes _entryattrs,
                                           boolean _theseAreDeletions)
            throws Exception, idxIRRException {

        // **********************************************
        // Check incoming Parameters.
        if ((_modattr == null) ||
                (_entryattrs == null)) {
            return (null);
        }

        // **********************************************
        // Initialize.
        Attributes _valueModifications = new BasicAttributes(true);

        // **********************************************
        // Loop through the Attribute Driver Values.
        for (NamingEnumeration ev = _modattr.getAll(); ev.hasMore(); ) {
            Object Aobject = ev.next();
            String Aname = (String) Aobject;
            Attribute modentry = _entryattrs.get(Aname);

            if (modentry != null) {
                _valueModifications.put(modentry);
            } else if (_theseAreDeletions) {
                _valueModifications.put(new BasicAttribute(Aname));
            } else {
                // NOOP
                // Simple Ignore adding information if this
                // is a replace or add.
                ;
            } // End of Else.
        } // End of Outer For Loop.

        // **********************************************
        // Return Categorized Modifications.
        return (_valueModifications);

    } // End of obtainModifications Method.

    /**
     * setupModifications, Setup Bulk Modificationitem from incoming Attributes Object.
     *
     * @param _modtype    Modifcation Type.
     * @param _entryattrs Incoming attributes to be placed into Modificationitem Queue.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void setupModifications(int _modtype, Attributes _entryattrs)
            throws Exception, idxIRRException {

        // **********************************************
        // Check incoming Parameters.
        if (_entryattrs == null) {
            return;
        }

        // **********************************************************
        // Process the incoming Attributes into the Modification Set.
        for (NamingEnumeration ea = _entryattrs.getAll(); ea.hasMore(); ) {
            Attribute attr = (Attribute) ea.next();
            CURRENT_MODLIST.addLast(new ModificationItem(_modtype, attr));
        } // End of For Loop.

        // **********************************************
        // Return
        return;
    } // End of setupModifications Method.

    /**
     * Prepare our Filter File, if available.
     */
    private void prepareFilter() {
        // ******************************
        // Initialize.
        final String METHODNAME = "prepareFilter";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.READER_PREPARING_DN_EXCLUSION_FILTER,
                new String[]{this.DN_FILTER_FILE});

        // ******************************************
        // Check for Nulls.
        if ((this.DN_FILTER_FILE == null) ||
                (this.DN_FILTER_FILE.trim().equalsIgnoreCase(""))) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    MessageConstants.READER_FILTERING_DISABLED);
            return;
        } // End of Check for a Filter File Specified.

        // ******************************************
        // Obtain the Filter File Object.
        File f = new File(this.DN_FILTER_FILE);
        if ((f.canRead()) && (f.isFile()) && (f.exists())) {
            try {
                this.dn_exclusion_filter = new FilterString(f);
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.READER_FILTERING_ENABLED,
                        new String[]{this.dn_exclusion_filter.toString()});
            } catch (IOException ioe) {
                this.dn_exclusion_filter = null;
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        ErrorConstants.READER_IOEXCEPTION_PROCESSING_FILTER_FILE,
                        new String[]{ioe.getMessage()});
            } // End of Exception processing.
        } // End of File Filter Check.
    } // End of parepareFilter Private Method.

    /**
     * Clean Up the Change Log File System Directory by removing all
     * processed files. You may also trigger any other items at this point
     * since we should be rather calm right now, we hope.
     */
    private long performPrimaryHousekeeping() {

        // *************************************
        // Initialize.
        final String METHODNAME = "performPrimaryHousekeeping";

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.READER_PERFORMING_HOUSEKEEPING);

        // **************************************
        // Perform clean  up of the File System
        // of all processed files.
        long files_deleted = 0;
        long state_files_deleted = 0;
        long reference_files_deleted = 0;

        Map processedmap =
                IRRChangeLogFileUtility.obtainProcessedFileMap(this.INPUT_PATH);
        // ***************************************
        // Now Loop to Process this housekeeping
        // phase by removing processed files.
        try {

            Set mySet = processedmap.entrySet();
            Iterator itr = mySet.iterator();
            while (itr.hasNext()) {
                Map.Entry oit = (Map.Entry) itr.next();
                File _infile =
                        (File) oit.getValue();

                // *******************************************
                // Now verify the file itself exists.
                if (_infile.exists()) {
                    // *****************************
                    // Delete the File.
                    try {
                        if (_infile.delete()) {
                            files_deleted++;
                        }
                    } catch (SecurityException se) {
                        // TODO
                    } // End of Exception Processing.
                } // End of File Exists

                // *****************************
                // Delete any reference files
                // this Change Log may Refer.
                reference_files_deleted =
                        IRRChangeLogFileUtility.deleteReferenceFiles(
                                _infile.getAbsolutePath());

                // *******************************************
                // Now verify the processed state file exists.
                File _processedfile =
                        IRRChangeLogFileUtility.getProcessedFile(_infile);
                if (_processedfile.exists()) {
                    try {
                        if (_processedfile.delete()) {
                            state_files_deleted++;
                        }
                    } catch (SecurityException se) {
                        // TODO
                    } // End of Exception Processing.
                } // End of File Exists

                // *******************************************
                // Now verify the processed state file exists.
                // Eliminate Blocked File References.
                File _blockedfile =
                        IRRChangeLogFileUtility.getBlockedFile(_infile);
                if (_blockedfile.exists()) {
                    try {
                        if (_blockedfile.delete()) {
                            state_files_deleted++;
                        }
                    } catch (SecurityException se) {
                        // TODO
                    } // End of Exception Processing.

                } // End of File Exists
            } // End of While Loop.

            // ******************************
            // return the number of files
            // purged.
            return files_deleted +
                    state_files_deleted +
                    reference_files_deleted;
        } finally {
            // *****************************
            // Report on how many files we
            // removed from the system.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_HOUSEKEEPING_COMPLETED,
                    new String[]{Long.toString(files_deleted),
                            Long.toString(state_files_deleted),
                            Long.toString(reference_files_deleted)});
        } // End of Final Processing.
    } // End of performPrimaryHousekeeping Private Method.

    /**
     * Clean Up the Change Log File System Directory by removing all
     * change files.  This method will run only on a
     * PEER, since we need to conserve disk space, we shall
     * remove all current unprocessed Files, since these have
     * already been applied locally. You may also trigger any
     * other items at this point
     * since we should be rather calm right now, we hope.
     */
    private long performPEERHousekeeping() {

        // *************************************
        // Initialize.
        final String METHODNAME = "performPEERHousekeeping";

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.READER_PERFORMING_PEER_HOUSEKEEPING);

        // **************************************
        // Perform clean  up of the File System
        // of all processed files.
        long files_deleted = 0;
        long state_files_deleted = 0;
        long reference_files_deleted = 0;

        TreeMap unprocessedmap =
                IRRChangeLogFileUtility.obtainUnprocessedFileMap(this.INPUT_PATH);
        // ***************************************
        // Now Loop to Process this housekeeping
        // phase by removing unprocessed files.
        try {

            Set mySet = unprocessedmap.entrySet();
            Iterator itr = mySet.iterator();
            while (itr.hasNext()) {
                Map.Entry oit = (Map.Entry) itr.next();
                File _infile =
                        (File) oit.getValue();

                // *******************************************
                // Now verify the file itself exists.
                if (_infile.exists()) {
                    // *****************************
                    // Delete the File.
                    try {
                        if (_infile.delete()) {
                            files_deleted++;
                        }
                    } catch (SecurityException se) {
                        // TODO
                    } // End of Exception Processing.
                } // End of File Exists

                // *****************************
                // Delete any reference files
                // this Change Log may Refer.
                reference_files_deleted =
                        IRRChangeLogFileUtility.deleteReferenceFiles(
                                _infile.getAbsolutePath());

                // *******************************************
                // Now verify the processed state file exists.
                File _processedfile =
                        IRRChangeLogFileUtility.getProcessedFile(_infile);
                if (_processedfile.exists()) {
                    try {
                        if (_processedfile.delete()) {
                            state_files_deleted++;
                        }
                    } catch (SecurityException se) {
                        // TODO
                    } // End of Exception Processing.
                } // End of File Exists

                // *******************************************
                // Now verify the processed state file exists.
                File _blockedfile =
                        IRRChangeLogFileUtility.getBlockedFile(_infile);
                if (_blockedfile.exists()) {
                    try {
                        if (_blockedfile.delete()) {
                            state_files_deleted++;
                        }
                    } catch (SecurityException se) {
                        // TODO
                    } // End of Exception Processing.

                } // End of File Exists
            } // End of While Loop.

            // ******************************
            // return the number of files
            // purged.
            return files_deleted +
                    state_files_deleted +
                    reference_files_deleted;
        } finally {
            // *****************************
            // Report on how many files we
            // removed from the system.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.READER_HOUSEKEEPING_COMPLETED,
                    new String[]{Long.toString(files_deleted),
                            Long.toString(state_files_deleted),
                            Long.toString(reference_files_deleted)});
        } // End of Final Processing.
    } // End of performPrimaryHousekeeping Private Method.

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
} ///:~ End of Class IRRChangeLogRestoreServiceReaderThread
