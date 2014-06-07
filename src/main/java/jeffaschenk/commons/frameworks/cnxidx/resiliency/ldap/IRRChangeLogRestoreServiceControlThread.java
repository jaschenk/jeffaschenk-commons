package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.util.*;
import java.io.*;


import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.MembershipListener;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.View;

/**
 * Service Control Thread to perform Mainloop to Drive Log Restore Facilities.
 * In this Control Thread we perform the following:
 * - Setup the JGroups Channel
 * - Determine Responsibility
 * - Maintain Group Membership
 * - Boot Primary Replica Thread
 * - Boot Housecleaning Thread
 * - Boot Full Sync Thread, when needed.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

public class IRRChangeLogRestoreServiceControlThread extends Thread
        implements MessageListener, MembershipListener, IRRChangeLogRestoreServiceVersion {

    // *******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = IRRChangeLogRestoreServiceControlThread.class.getName();

    private static final String THREAD_NAME
            = "ServiceControlThread";

    // ********************************
    // Default Multicast Address.
    private static final String DEFAULT_MULTICAST_ADDRESS_STRING
            = "224.0.0.35";

    // ********************************
    // Default Multicast Port.
    private static final String DEFAULT_MULTICAST_PORT_STRING
            = "45566";

    // ********************************
    // Default Group Name
    public static final String DEFAULT_GROUP_NAME
            = "ICOS-RESILIENCY-DIRECTORY-SYNC";

    // *******************************
    // Default JGroups Properties.
    private static final String DEFAULT_JGROUP_PROPERTY_STRING
            = "UDP(mcast_addr=224.0.0.35;mcast_port=45566;ip_ttl=32;" +
            "mcast_send_buf_size=150000;mcast_recv_buf_size=80000):" +
            "PING(timeout=2000;num_initial_members=3):" +
            "MERGE2(min_interval=5000;max_interval=10000):" +
            "FD_SOCK:" +
            "VERIFY_SUSPECT(timeout=1500):" +
            "pbcast.NAKACK(gc_lag=50;retransmit_timeout=300,600,1200,2400,4800):" +
            "UNICAST(timeout=5000):" +
            "pbcast.STABLE(desired_avg_gossip=20000):" +
            "FRAG(frag_size=4096;down_thread=false;up_thread=false):" +
            "pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;" +
            "shun=false;print_local_addr=false)";

    // *******************************
    // JGroups Static Property Layers
    private static final String UDP_JGROUP_PROPERTY_STRING
            = "UDP(mcast_addr=%1;mcast_port=%2;ip_ttl=32;" +
            "mcast_send_buf_size=150000;mcast_recv_buf_size=80000):";

    private static final String PING_JGROUP_PROPERTY_STRING
            = "PING(timeout=2000;num_initial_members=3):";

    private static final String MERGE2_JGROUP_PROPERTY_STRING
            = "MERGE2(min_interval=5000;max_interval=10000):";

    private static final String FD_JGROUP_PROPERTY_STRING
            = "FD_SOCK:";

    private static final String VERIFY_JGROUP_PROPERTY_STRING
            = "VERIFY_SUSPECT(timeout=1500):";

    private static final String PBCAST_NAKACK_JGROUP_PROPERTY_STRING
            = "pbcast.NAKACK(gc_lag=50;retransmit_timeout=300,600,1200,2400,4800):";

    private static final String UNICAST_JGROUP_PROPERTY_STRING
            = "UNICAST(timeout=5000):";

    private static final String PBCAST_STABLE_JGROUP_PROPERTY_STRING
            = "pbcast.STABLE(desired_avg_gossip=20000):";

    private static final String FRAG_JGROUP_PROPERTY_STRING
            = "FRAG(frag_size=4096;down_thread=false;up_thread=false):";

    private static final String PBCAST_GMS_JGROUP_PROPERTY_STRING
            = "pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;" +
            "shun=false;print_local_addr=false)";


    // *******************************
    // JGroup Channel Objects
    private JChannel my_channel = null;
    //private PullPushAdapter my_adapter = null;
    private String my_mcast_address = null;
    private String my_mcast_port = null;
    private String my_group_name = null;
    private String my_property_string = null;

    // *******************************
    // Memberships
    private boolean MULTIMASTER = false;  // Experimental.
    private boolean PRIMARY_COORDINATOR = false;
    private Object PRIMARY_COORDINATOR_ADDR = null;

    protected List<Address> members = new ArrayList<>();
    protected Object my_addr = null;

    // ********************************
    // Global Indicators.
    private boolean READER_THREAD_READY = false;
    private boolean REPLICA_THREAD_READY = false;

    // *******************************
    // Global Variables.
    private String IRRHost = null;
    private String IRRPrincipal = null;
    private String IRRCredentials = null;
    //
    private String INPUT_PATH = null;
    private String PUBLISH_EXCLUDE_DN_FILTER_FILE = null;
    private String RESTORE_EXCLUDE_DN_FILTER_FILE = null;
    //
    public static final int DEFAULT_WEBADMIN_PORT = 1389;
    protected int WEBADMIN_PORT = DEFAULT_WEBADMIN_PORT;
    //
    private String OPERATIONAL_MODE = null;
    public static final String DEFAULT_OPERATION_MODE = "DEFAULT";
    //
    private LinkedList<String> WEBADMIN_ALLOW_LIST = null;
    public static final String DEFAULT_ALLOWABLE_HOST = "127.0.0.1";
    //
    // **********************************

    // **********************************
    // Global TimeClocks.
    private StopWatch gsw = new StopWatch();

    // **********************************
    // Global Counters.
    private long FAILURE_COUNT = 0;
    private long stackcommands = 0;
    private long receivedstackcommands = 0;
    private long receivedinvalidobjects = 0;
    private long membershipupdatesreceived = 0;

    // ***********************************
    // Control counters used internally to
    // trigger housekeeping for PEER.
    private static final long DEFAULT_PEER_HOUSEKEEPING_THRESOLD = 128;
    private long replicacommands_issued = 0;

    // ********************************
    // Thread Safe Global Stacks.
    private CircularObjectStack cos_control_common = null;
    private CircularObjectStack cos_control_to_changelog = null;
    private CircularObjectStack cos_control_to_replicarestore = null;
    private CircularObjectStack cos_publisher_common = null;
    private CircularObjectStack cos_control_to_webadmin = null;

    // ********************************
    // Global Threads Maintained.
    private IRRChangeLogRestoreServiceReaderThread READER_THREAD = null;
    private IRRChangeLogRestoreServicePublisherThread PUBLISHER_THREAD = null;
    private IRRChangeLogRestoreServiceReplicaRestoreThread REPLICARESTORE_THREAD = null;
    private WebAdminServerThread WEBADMIN_THREAD = null;

    // ********************************
    // Loop Timers.
    private final long BEGIN_WAIT_DURATION = (1000 * 2);         // Start at 2 Seconds.
    private final long HIGH_WAIT_DURATION = (1000 * 300);       // High Wait Duration is 5 Minutes.
    //private final long INCREMENTAL_WAIT_DURATION = (1000*30);  // Increment at 30 Seconds.

    private long CURRENT_WAIT_DURATION = BEGIN_WAIT_DURATION;

    // *********************************
    // Lap Timers.
    private idxLapTime LP_ENTRY_FROM_COS = new idxLapTime();
    private idxLapTime LP_ENTRY_FROM_LOGDIR = new idxLapTime();

    /**
     * IRRChangeLogRestoreServiceControlThread Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRChangeLogRestoreServiceControlThread(String IRRHost,
                                                   String IRRPrincipal,
                                                   String IRRCredentials,
                                                   String INPUT_PATH,
                                                   String PUBLISH_EXCLUDE_DN_FILTER_FILE,
                                                   String RESTORE_EXCLUDE_DN_FILTER_FILE,
                                                   int WEBADMIN_PORT,
                                                   String OPERATIONAL_MODE) {
        this.IRRHost = IRRHost;
        this.IRRPrincipal = IRRPrincipal;
        this.IRRCredentials = IRRCredentials;
        this.INPUT_PATH = INPUT_PATH;
        this.PUBLISH_EXCLUDE_DN_FILTER_FILE
                = PUBLISH_EXCLUDE_DN_FILTER_FILE;
        this.RESTORE_EXCLUDE_DN_FILTER_FILE
                = RESTORE_EXCLUDE_DN_FILTER_FILE;
        this.WEBADMIN_PORT
                = WEBADMIN_PORT;
        this.OPERATIONAL_MODE
                = OPERATIONAL_MODE;

        // **************************************
        // With this constructor we use the
        // Plain Default settings for our
        // Communications Properties.
        this.my_group_name
                = IRRChangeLogRestoreServiceControlThread.DEFAULT_GROUP_NAME;
        this.my_mcast_address
                = IRRChangeLogRestoreServiceControlThread.DEFAULT_MULTICAST_ADDRESS_STRING;
        this.my_mcast_port
                = IRRChangeLogRestoreServiceControlThread.DEFAULT_MULTICAST_PORT_STRING;

        // *****************************************
        // Set our Default Allow List for WEBAdmin
        // Functionality.
        this.formulateWebAdminAllowList(
                IRRChangeLogRestoreServiceControlThread.DEFAULT_ALLOWABLE_HOST);

        // **************************************
        // Name our Thread.
        this.setName(IRRChangeLogRestoreServiceControlThread.THREAD_NAME);
    } // End of Constructor for IRRChangeLogRestoreServiceControlThread.

    /**
     * IRRChangeLogRestoreServiceControlThread Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRChangeLogRestoreServiceControlThread(String IRRHost,
                                                   String IRRPrincipal,
                                                   String IRRCredentials,
                                                   String INPUT_PATH,
                                                   String PUBLISH_EXCLUDE_DN_FILTER_FILE,
                                                   String RESTORE_EXCLUDE_DN_FILTER_FILE,
                                                   int WEBADMIN_PORT,
                                                   String OPERATIONAL_MODE,
                                                   String GROUP_NAME,
                                                   String MULTICAST_ADDRESS,
                                                   String MULTICAST_PORT,
                                                   String WEBADMIN_ALLOW_STRING) {
        this.IRRHost = IRRHost;
        this.IRRPrincipal = IRRPrincipal;
        this.IRRCredentials = IRRCredentials;
        this.INPUT_PATH = INPUT_PATH;
        this.PUBLISH_EXCLUDE_DN_FILTER_FILE
                = PUBLISH_EXCLUDE_DN_FILTER_FILE;
        this.RESTORE_EXCLUDE_DN_FILTER_FILE
                = RESTORE_EXCLUDE_DN_FILTER_FILE;
        this.WEBADMIN_PORT
                = WEBADMIN_PORT;
        this.OPERATIONAL_MODE
                = OPERATIONAL_MODE;

        // ******************************************
        // With this constructor we use the overrides
        // for out Communications Properties.
        if ((GROUP_NAME == null) ||
                (GROUP_NAME.trim().equalsIgnoreCase(""))) {
            this.my_group_name
                    = IRRChangeLogRestoreServiceControlThread.DEFAULT_GROUP_NAME;
        } else {
            this.my_group_name
                    = GROUP_NAME;
        } // End of Else.

        if ((MULTICAST_ADDRESS == null) ||
                (MULTICAST_ADDRESS.trim().equalsIgnoreCase(""))) {
            this.my_mcast_address
                    = IRRChangeLogRestoreServiceControlThread.DEFAULT_MULTICAST_ADDRESS_STRING;
        } else {
            this.my_mcast_address
                    = MULTICAST_ADDRESS;
        } // End of Else.

        if ((MULTICAST_PORT == null) ||
                (MULTICAST_PORT.trim().equalsIgnoreCase(""))) {
            this.my_mcast_port
                    = IRRChangeLogRestoreServiceControlThread.DEFAULT_MULTICAST_PORT_STRING;
        } else {
            try {
                Integer.parseInt(MULTICAST_PORT);
                this.my_mcast_port = MULTICAST_PORT;
            } catch (NumberFormatException nfe) {
                this.my_mcast_port
                        = IRRChangeLogRestoreServiceControlThread.DEFAULT_MULTICAST_PORT_STRING;
            } // End of Exception processing.
        } // End of Else.

        // **********************************************
        // Now determine if there is anything additional
        // Address I need to pick up.
        if ((WEBADMIN_ALLOW_STRING != null) &&
                (!WEBADMIN_ALLOW_STRING.trim().equalsIgnoreCase(""))) {
            this.formulateWebAdminAllowList(WEBADMIN_ALLOW_STRING);
        } else {
            // *****************************************
            // Set our Default Allow List for WEBAdmin
            // Functionality.
            this.formulateWebAdminAllowList(
                    IRRChangeLogRestoreServiceControlThread.DEFAULT_ALLOWABLE_HOST);
        } // End of Else.

        // **************************************
        // Name our Thread.
        this.setName(IRRChangeLogRestoreServiceControlThread.THREAD_NAME);
    } // End of Constructor for IRRChangeLogRestoreServiceControlThread.

    /**
     * Service Control Thread
     */
    public void run() {
        final String METHODNAME = "run";
        gsw.start(); // Start our Global Time Clock for our Duration.

        // *************************************************
        // Boot Service Control Thread.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.BOOT);

        // *************************************************
        // Check Requested Operational Mode.
        if ((this.OPERATIONAL_MODE == null) ||
                (this.OPERATIONAL_MODE.equalsIgnoreCase(""))) {
            this.OPERATIONAL_MODE =
                    IRRChangeLogRestoreServiceControlThread.DEFAULT_OPERATION_MODE;
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_REQUESTED_DEFAULT_OPERATIONAL_MODE);
        } else {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_REQUESTED_OPERATIONAL_MODE,
                    new String[]{this.OPERATIONAL_MODE});
        } // End of Else.

        // *************************************************
        // TODO
        // Determine if this is a restart from a failure?
        // Meaning, did we or our machine fail, if that is
        // the case a new PRIMARY Coordinator should have been
        // picked.
        //
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.DETERMINE_PRIOR_STATE);


        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.PRIOR_STATE_UNKNOWN);


        // **************************************************
        // Establish our Synchronized Circular Stack Objects
        // used for Threads queues to drive work in other
        // threads.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.INITIALIZING_STACKS);

        this.cos_control_common = new CircularObjectStack();
        this.cos_control_to_changelog = new CircularObjectStack();
        this.cos_publisher_common = new CircularObjectStack();
        this.cos_control_to_replicarestore = new CircularObjectStack();
        this.cos_control_to_webadmin = new CircularObjectStack();

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.STACKS_INITIALIZED);

        // ************************************************
        // Set up our JGroups Channel Communications.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.INITIALIZING_RESILIENCY_COMMUNICATION_LAYER);
        try {
            // *****************************
            // Show our Group Name.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.RESILIENCY_MEMBERSHIP_NAME,
                    new String[]{this.my_group_name});

            // *********************************
            // Build JGroups Property String.
            this.my_property_string = this.formulateJGroupsProperties();

            // *********************************
            // Show our JGroups Property
            // String.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.RESILIENCY_PROPERTIES,
                    new String[]{this.my_property_string});

            // *****************************
            // Start JavaGroups.
            this.startJGroups();

        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.COMMUNICATION_EXCEPTION,
                    new String[]{e.toString()});
            this.logStackTrace(e, METHODNAME, ErrorConstants.COMMUNICATION_EXCEPTION_STACKTRACE);
            System.exit(idxCMDReturnCodes.EXIT_GENERIC_FAILURE);
        } // End of Exception Processing.

        // **************************************************
        // Show JGroups Open Successfully.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.RESILIENCY_COMMUNICATIONS_LAYER_INITIALIZED,
                new String[]{this.my_addr.toString()});

        // **************************************************
        // Start the Common Publisher Service Thread.
        //
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.STARTING_PUBLISHER_THREAD);
        this.startPublisherThread();
        this.publishEcho(); // Send out an Echo.

        // **************************************************
        // Start the Change Log Queue Service.
        // This is used when this instance
        // is a Primary Coordinator.
        //
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.STARTING_READER_THREAD);
        this.startReaderThread();

        // **************************************************
        // Start the Replica Restore Service Thread.
        //
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.STARTING_REPLICARESTORE_THREAD);
        this.startReplicaRestoreThread();

        // ***********************************************
        // Start the Web Admin Service Thread.
        //
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.STARTING_WEBADMIN_THREAD);
        this.startWebAdminThread();

        // ***********************************************
        // Indicate Boot Sequence is complete.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.BOOT_SEQUENCE_COMPLETE);

        // ***********************************************
        // Initialize and Main Thread Loop Variables.
        StackCommand instackcommand = null;
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.STARTING_MAIN_CONTROL_THREAD_LOOP);

        // *********************************************
        // Perform our Main Thread Loop until
        // the Thread is interrupted
        // at Shutdown request Time.
        //
        boolean control_running = true;
        try {
            while (control_running) {

                // ******************************
                // Determine if anything is on
                // the common control stack.
                instackcommand = null; // Be sure to Clear Previous Entry.
                LP_ENTRY_FROM_COS.Start();
                if (this.cos_control_common.hasMoreNodes()) {
                    instackcommand = (StackCommand) this.cos_control_common.getNext();
                }
                LP_ENTRY_FROM_COS.Stop();

                // ***************************
                // Did anything get pulled
                // from stack?
                if (instackcommand != null) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                            MessageConstants.CONTROL_STACK_COMMAND_POPPED,
                            new String[]{instackcommand.toString()});

                    this.stackcommands++;

                    // ***************************
                    // Interpret the Stack Command
                    switch (instackcommand.getCommandType()) {
                        case StackCommand.CL_END_OF_THREAD:
                            break;

                        // *************************************
                        // Process Echo Command.
                        case StackCommand.CL_ECHO:
                            // ******************************
                            // We basically turn this object
                            // into a reply hence the echo
                            // and place on JGroups Publisher
                            // Queue at the bottom of the queue.
                            instackcommand.setCommandType(StackCommand.CL_ECHO_REPLY);
                            this.cos_publisher_common.push(instackcommand);
                            break;

                        // *************************************
                        // Process Echo Reply Command.
                        case StackCommand.CL_ECHO_REPLY:
                            if (instackcommand.getObject() != null) {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                                        MessageConstants.CONTROL_STACK_ECHO_REPLY,
                                        new String[]{instackcommand.getObject().toString()});
                            } else {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                                        MessageConstants.CONTROL_STACK_ECHO_REPLY_WITH_EMPTY_OBJECT);
                            } // End of Else.
                            break;

                        // *************************************
                        // Process Remote Status Command for
                        // PEERS other than us and we may get
                        // a status call from ourselves as well.
                        case StackCommand.CL_STATUS:
                            this.processRemoteStatusCommand(instackcommand);
                            break;

                        // *************************************
                        // Reader Thread has indicated it is
                        // ready.
                        case StackCommand.CL_READER_READY:
                            this.READER_THREAD_READY = true;
                            break;

                        // *************************************
                        // Reader Thread has indicated it is
                        // ready.
                        case StackCommand.CL_REPLICA_READY:
                            this.REPLICA_THREAD_READY = true;
                            break;

                        // *************************************
                        // Process Start of Reader Thread.
                        // Only start if we really need to.
                        case StackCommand.CL_START_READER:
                            if ((this.READER_THREAD == null) ||
                                    (!this.READER_THREAD.t.isAlive())) {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_START_READER_ACCEPTED);
                                this.startReaderThread();
                            } else {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_START_READER_IGNORED);
                            } // End of Else.
                            break;

                        // *************************************
                        // Process Stop of Reader Thread.
                        // Only start if we really need to.
                        case StackCommand.CL_STOP_READER:
                            if ((this.READER_THREAD != null) &&
                                    (this.READER_THREAD.t.isAlive())) {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_STOP_READER_ACCEPTED);
                                this.stopReaderThread();
                            } else {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_STOP_READER_IGNORED);
                            } // End of Else.
                            this.READER_THREAD_READY = false;
                            break;

                        // *************************************
                        // Process Start of Reader Thread.
                        // Only start if we really need to.
                        case StackCommand.CL_START_REPLICA:
                            if ((this.REPLICARESTORE_THREAD == null) ||
                                    (!this.REPLICARESTORE_THREAD.t.isAlive())) {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_START_REPLICA_ACCEPTED);
                                this.startReplicaRestoreThread();
                            } else {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_START_REPLICA_IGNORED);
                            } // End of Else.
                            break;

                        // *************************************
                        // Process Stop of Reader Thread.
                        // Only start if we really need to.
                        case StackCommand.CL_STOP_REPLICA:
                            if ((this.REPLICARESTORE_THREAD != null) &&
                                    (this.REPLICARESTORE_THREAD.t.isAlive())) {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_STOP_READER_ACCEPTED);
                                this.stopReplicaRestoreThread();
                            } else {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                        MessageConstants.CONTROL_REQUEST_TO_STOP_READER_IGNORED);
                            } // End of Else.
                            this.REPLICA_THREAD_READY = false;
                            break;

                        // *************************************
                        // Process a Reset of all Statistics.
                        case StackCommand.CL_RESET_STATS:
                            this.resetStatistics();
                            break;

                        // **************************************
                        // Process any Housekeeping Completions.
                        case StackCommand.CL_HOUSEKEEPING_DONE:
                            if (instackcommand.getObject() != null) {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                                        MessageConstants.CONTROL_HOUSEKEEPING_COMPLETED_WITH_FILES_PURGED,
                                        new String[]{((Long) instackcommand.getObject()).toString()});
                            } // End of Null check for any Counts.
                            else {
                                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                                        MessageConstants.CONTROL_HOUSEKEEPING_COMPLETED);
                            } // End of Else.
                            break;

                        // *************************************
                        // Process other Control State Commands
                        // when Necessary.
                        case StackCommand.CL_PRIMARY:
                        case StackCommand.CL_WAS_PRIMARY:
                        case StackCommand.CL_PEER:
                            break;

                        // **************************************
                        // Ok, did someone request shutdown, if so
                        // perform it.
                        case StackCommand.CL_SHUTDOWN:
                            control_running = false;
                            break;

                        // ****************************
                        // Ignore Any unknown Commands.
                        default:
                            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                    MessageConstants.IGNORED_UNKNOWN_COMMAND);
                    } // End of Switch Statement.
                } // End of Check for Null or no Stack Command.

                // ************************************
                // If we received a Shutdown command,
                // then force our thread into shutdown
                // processing before we do anything else.
                if (!control_running) {
                    break;
                }  // Break out of our Main While Loop.

                // ***********************************
                // Only if we are Primary or MM will
                // we perform the Following
                // to check for Change
                // Logs and place those in the
                // Stack to be processed by our Reader.
                // The Reader must be Ready to
                // receive and there must at least
                // another Member in our Group.
                if (((this.PRIMARY_COORDINATOR) ||
                        (this.MULTIMASTER)) &&
                        (this.READER_THREAD_READY) &&
                        (this.members.size() > 1)) {

                    // ****************************************
                    // Obtain a Directory Listing of all
                    // unProcessed Files in time order.
                    LP_ENTRY_FROM_LOGDIR.Start();
                    TreeMap upf =
                            IRRChangeLogFileUtility.obtainUnprocessedFileMap(
                                    this.INPUT_PATH);
                    LP_ENTRY_FROM_LOGDIR.Stop();

                    // *****************************************
                    // Now Send Command to our Reader Thread
                    // to Process the Change Logs.
                    if ((upf != null) &&
                            (!upf.isEmpty())) {
                        this.cos_control_to_changelog.push(
                                new StackCommand(StackCommand.CL_PROCESS_LOGS,
                                        this.my_addr,
                                        upf));

                        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                                MessageConstants.CONTROL_PLACING_LOGS_TO_BE_PROCESSED_ON_STACK,
                                new String[]{Integer.toString(upf.size())});

                        // *************************************
                        // Indicate the Thread will need to ask
                        // when it is ready.
                        this.READER_THREAD_READY = false;
                    } // End of Queuing files to be processed.
                } // End of primary or MultiMaster Check.

                // ********************************************
                // Determine if we are not Primary, just a
                // simple PEER, then we need to Trigger
                // some housekeeping when our threshold
                // has been met.
                else if (((!this.PRIMARY_COORDINATOR) &&
                        (!this.MULTIMASTER)) &&
                        (this.READER_THREAD_READY) &&
                        (this.members.size() > 1) &&
                        (DEFAULT_PEER_HOUSEKEEPING_THRESOLD <=
                                this.replicacommands_issued)) {
                    this.cos_control_to_changelog.push(
                            new StackCommand(StackCommand.CL_HOUSEKEEPING_BEGIN_PEER,
                                    this.my_addr));
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                            MessageConstants.CONTROL_TRIGGERING_HOUSEKEEPING_FOR_PEER);
                    this.replicacommands_issued = 0;
                } // End of Check for PEER.

                // ***************************
                // Did we process anything?
                if (instackcommand == null) {
                    // ***************************
                    // Wait for a Tick or two.
                    // Reset if we reached our max
                    // wait interval.
                    if (CURRENT_WAIT_DURATION > HIGH_WAIT_DURATION) {
                        CURRENT_WAIT_DURATION = BEGIN_WAIT_DURATION;
                    }
                    Thread.sleep(CURRENT_WAIT_DURATION);
                } // End of Nothing in Stack yet to Process.
            } // End of While Loop.
        } catch (InterruptedException e) {
            control_running = false;
        } finally {
            this.shutdown();
        } // End of Final Processing Block.
    } // End of Thread Execution run.

        /* ------------------------------------------------------------ */
        /* MembershipListener Interface                                 */
        /*           Handler Callbacks and Implementation Methods       */
        /* ------------------------------------------------------------ */


    public void viewAccepted(View view) {
        this.setMembershipState(view.getMembers());
    }

    public void suspect(Address suspected_mbr) {
    }

    public void block() {
    }

    @Override
    public void unblock() {
        // TODO --
    }

    @Override
    public void getState(OutputStream outputStream) throws Exception {
    }

    @Override
    public void setState(InputStream inputStream) throws Exception {
    }

    /* ------------------------------------------------------------ */
        /* MessageListener Interface                                    */
        /*           Handler Callbacks and Implementation Methods       */
        /* ------------------------------------------------------------ */

    /**
     * Callback method to handle all Message Receives.
     * We will determine which thread to direct this object to
     * based upon Object Instance type.
     *
     * @param msg Message Received.
     */
    public void receive(Message msg) {
        // **************************************
        // Initialize
        final String METHODNAME = "receive";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        Object o;
        try {
            o = msg.getObject();
            if (!(o instanceof StackCommand)) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        MessageConstants.CONTROL_RECEIVED_UNKNOWN_MESSAGE_TYPE,
                        new String[]{msg.getSrc().toString(),
                                o.getClass().getName(),
                                o.toString()});
                this.receivedinvalidobjects++;
                return;
            } // End of If.

            // *****************************
            // Now we have a StackCommand
            // received, determine what to do
            // with it based upon request.
            StackCommand instackcommand = (StackCommand) o;

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_RECEIVED_MESSAGE,
                    new String[]{msg.getSrc().toString(),
                            instackcommand.toString()});
            this.receivedstackcommands++;

            // ************************************
            // Dispatch command to correct stack.
            switch (instackcommand.getCommandType()) {
                // ****************************
                // Control State Stack Commands.
                case StackCommand.CL_STATUS:
                case StackCommand.CL_ECHO:
                case StackCommand.CL_ECHO_REPLY:
                case StackCommand.CL_PRIMARY:
                case StackCommand.CL_WAS_PRIMARY:
                case StackCommand.CL_PEER:
                    // ************************************
                    // Place Object on Control Stack.
                    this.cos_control_common.push(instackcommand);
                    break;

                // ***********************************************
                // Ensure we were the destination or destination
                // address is null, if so pass on the Shutdown.
                case StackCommand.CL_SHUTDOWN:
                    if ((instackcommand.getDestination() == null) ||
                            (instackcommand.getDestination().equals(this.my_addr))) {
                        this.cos_control_common.push(instackcommand);
                    }
                    break;

                // ************************************************
                // Check for a Global Reset of Stats or for just
                // this instance.
                case StackCommand.CL_RESET_STATS:
                    if ((instackcommand.getDestination() == null) ||
                            (instackcommand.getDestination().equals(this.my_addr))) {
                        this.cos_control_common.push(instackcommand);
                    }
                    break;

                // *****************************************
                // Ensure we were the destination, if not
                // Ignore the command.
                case StackCommand.CL_START_READER:
                case StackCommand.CL_STOP_READER:
                case StackCommand.CL_START_REPLICA:
                case StackCommand.CL_STOP_REPLICA:
                case StackCommand.CL_HOUSEKEEPING_BEGIN_PRIMARY:
                case StackCommand.CL_HOUSEKEEPING_BEGIN_PEER:
                    if ((instackcommand.getDestination() != null) &&
                            (instackcommand.getDestination().equals(this.my_addr))) {
                        this.cos_control_common.push(instackcommand);
                    }
                    break;

                // *****************************************
                // Ensure we were the originator, if not
                // Ignore the command.
                case StackCommand.CL_READER_READY:
                case StackCommand.CL_REPLICA_READY:
                case StackCommand.CL_HOUSEKEEPING_READY:
                case StackCommand.CL_HOUSEKEEPING_DONE:
                    if (instackcommand.getOriginator().equals(this.my_addr)) {
                        this.cos_control_common.push(instackcommand);
                    }
                    break;

                // *******************************
                // WEB Admin Status Reply Commands.
                case StackCommand.CL_STATUS_REPLY:
                    // ************************************
                    // Place Object on WebAdmin Stack if we
                    // are the destination, otherwise we could
                    // trash our local queue.
                    //
                    if (instackcommand.getDestination().equals(this.my_addr)) {
                        this.cos_control_to_webadmin.push(instackcommand);
                    }
                    break;

                // *****************************
                // Replica Restore Commands.
                case StackCommand.CL_REPLICATE:
                    if (((!this.PRIMARY_COORDINATOR)) ||
                            ((this.MULTIMASTER) &&
                                    (!instackcommand.getOriginator().equals(this.my_addr)))) {    // ************************************
                        // Place Object on Control Stack.
                        this.cos_control_to_replicarestore.push(instackcommand);

                        // ************************************
                        // Increment our counter to trigger
                        // of clean up on the PEER Input Path
                        // otherwise we could have lots of
                        // unnecessary duplicated changes.
                        this.replicacommands_issued++;

                        // ************************************
                        // Make sure our replica
                        if (!this.REPLICA_THREAD_READY) {
                            // *********************************************
                            // Indicate Replica is not Ready and Queue
                            // Building.
                            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                    MessageConstants.REPLICA_RESTORE_THREAD_NOT_READY_QUEUE_BUILDING,
                                    new String[]{Integer.toString(this.cos_control_to_replicarestore.size())});
                        } // End of Checking for Replica Thread is Ready.
                    } // End of Check, only perform if not Primary not MM.
                    else {
                        // **********************************************
                        // Produce Information Message indicating we are
                        // dropping a Replica Request.
                        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                MessageConstants.CONTROL_DROPPING_REPLICA_RESTORE_REQUEST,
                                new String[]{instackcommand.getOriginator().toString()});
                    } // End of Else.
                    break;

                // *****************************
                // Replica Restore Commands.
                case StackCommand.CL_REPLICATE_RESPONSE:
                    // ************************************
                    // Ensure this Response did not come
                    // from us, if so ignore.
                    if (!instackcommand.getOriginator().equals(this.my_addr)) {
                        // ************************************
                        // Place Object on Control Stack to be sent to
                        // Reader.
                        this.cos_control_to_changelog.push(instackcommand);
                    } // End of If Check.
                    else {
                        // **********************************************
                        // Produce Information Message indicating we are
                        // dropping a Replica Response.
                        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                                MessageConstants.CONTROL_DROPPING_REPLICA_RESPONSE,
                                new String[]{instackcommand.getOriginator().toString()});
                    } // End of Else.
                    break;

                // ******************************
                // Log Reader Commands.
                case StackCommand.CL_MARK_POINT:
                case StackCommand.CL_MARK_AS_PROCESSED:
                case StackCommand.CL_REMOVE_MARK_POINT:
                case StackCommand.CL_REMOVE_LOG:
                    // ************************************
                    // Place Object on Control Stack.
                    this.cos_control_common.push(instackcommand);
                    break;

                // *********************************
                // Default Ignore the Received.
                default:
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                            MessageConstants.IGNORED_UNKNOWN_COMMAND);
            } // End of Switch Statement.
        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.COMMUNICATION_RECEIVE_EXCEPTION,
                    new String[]{e.toString()});
        } // End of Exception Processing.
    } // End of receive method.

    /* ------------------------------------------------------------ */
        /* Protected Methods                                              */
        /* ------------------------------------------------------------ */

    /**
     * Helper Method to obtain Status from all Components and
     * send status back to WEB Admin to respond to a user
     * requesting status.
     */
    protected String obtainLocalStatus(boolean forremote) {
        final String METHODNAME = "obtainLocalStatus";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        StringBuffer sb = new StringBuffer();

        // ********************************
        // Now Obtain Status from other
        // Components.
        sb.append(WebAdminResponderThread.BEGIN_TABLE);
        sb.append(WebAdminResponderThread.BEGIN_ROW);

        sb.append(WebAdminResponderThread.BEGIN_COL);
        sb.append(this.getStatus());
        sb.append(WebAdminResponderThread.END_COL);
        sb.append(WebAdminResponderThread.BEGIN_COL);
        sb.append(this.PUBLISHER_THREAD.getStatus());
        sb.append(this.WEBADMIN_THREAD.getStatus());
        sb.append(WebAdminResponderThread.END_COL);
        sb.append(WebAdminResponderThread.END_ROW);
        sb.append(WebAdminResponderThread.BEGIN_ROW);
        sb.append(WebAdminResponderThread.BEGIN_COL);

        // ********************************************
        // Now Verify Reader Thread is Running.
        if ((this.READER_THREAD == null) ||
                (!this.READER_THREAD.t.isAlive())) {
            sb.append(IRRChangeLogRestoreServiceReaderThread.getOfflineStatus());
        } else {
            sb.append(this.READER_THREAD.getStatus());
        }
        sb.append(WebAdminResponderThread.END_COL);
        sb.append(WebAdminResponderThread.BEGIN_COL);

        // *********************************************
        // Now Verify Replica Restore Thread is Running.
        if ((this.REPLICARESTORE_THREAD == null) ||
                (!this.REPLICARESTORE_THREAD.t.isAlive())) {
            sb.append(IRRChangeLogRestoreServiceReplicaRestoreThread.getOfflineStatus());
        } else {
            sb.append(this.REPLICARESTORE_THREAD.getStatus());
        }

        // ***************************************
        // Finish up Table.
        sb.append(WebAdminResponderThread.END_COL);
        sb.append(WebAdminResponderThread.END_ROW);
        sb.append(WebAdminResponderThread.END_TABLE);

        // ******************************
        // return Response String.
        return sb.toString();
    } // End of getStatus Method.

    /**
     * Helper Method to reset all Component Statistics.
     */
    protected void resetStatistics() {
        final String METHODNAME = "resetStatistics";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // ********************************
        // Reset this Thread Statistics
        this.FAILURE_COUNT = 0;
        this.stackcommands = 0;
        this.receivedstackcommands = 0;
        this.receivedinvalidobjects = 0;
        this.membershipupdatesreceived = 0;

        this.LP_ENTRY_FROM_COS.Reset();
        this.LP_ENTRY_FROM_LOGDIR.Reset();

        // *********************************
        // Reset Publisher Statistics
        this.PUBLISHER_THREAD.resetStatistics();

        // *********************************
        // Reset WebAdmin Statistics
        this.WEBADMIN_THREAD.resetStatistics();

        // ********************************************
        // Now Verify Reader Thread is Running,
        // if so, reset it's stats.
        if ((this.READER_THREAD != null) &&
                (this.READER_THREAD.t.isAlive())) {
            this.READER_THREAD.resetStatistics();
        }

        // *********************************************
        // Now Verify Replica Restore Thread is Running.
        if ((this.REPLICARESTORE_THREAD != null) &&
                (this.REPLICARESTORE_THREAD.t.isAlive())) {
            this.REPLICARESTORE_THREAD.resetStatistics();
        }

    } // End of resetStatistics Method.


        /* ------------------------------------------------------------ */
        /* Private Methods                                              */
        /* ------------------------------------------------------------ */

    /**
     * Helper Method to obtain Status from all Components and
     * send status back to respond to a remote request.
     */
    private void processRemoteStatusCommand(StackCommand statuscommand) {
        final String METHODNAME = "processRemoteStatusCommand";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // ***********************************
        // Check for NULL StackCommand Object.
        if (statuscommand == null) {
            return;
        }

        // **********************************
        // Flip the Command Type to Reply and
        // place our Status into the Stack
        // Command Wrapper.
        statuscommand.setCommandType(StackCommand.CL_STATUS_REPLY);
        statuscommand.setObject(this.obtainLocalStatus(true));
        statuscommand.setDestination(statuscommand.getOriginator());
        statuscommand.setOriginator(this.my_addr);
        this.cos_publisher_common.push(statuscommand);
    } // End of processRemoteStatusCommand Method.

    /**
     * Helper method to provide current determination of
     * the Membership state and who is the Primary Coordinator.
     * If we are not Primary Coordinator, then we become a
     * slave and anything received will be replicated to our local
     * Directory Instance.
     *
     * @param mbrs Vector Object containing current Members.
     */
    private void setMembershipState(List<Address> mbrs) {

        // *********************************
        // Initialize.
        final String METHODNAME = "setmembershipState";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        this.membershipupdatesreceived++;

        // ********************************
        // Save new List of Members.
        members.removeAll(members);
        for (int i = 0; i < mbrs.size(); i++) {
            members.add(mbrs.get(i));
        }

        // ******************************************
        // Now Determine who is Primary Coordinator.
        if (mbrs.size() <= 1 || (mbrs.size() > 1 && mbrs.get(0).equals(my_addr))) {
            if (this.PRIMARY_COORDINATOR_ADDR == null) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.CONTROL_INITIALIZED_AS_PRIMARY_COORDINATOR,
                        new String[]{this.my_addr.toString()});
            } else if (this.PRIMARY_COORDINATOR_ADDR.equals(my_addr)) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.CONTROL_MAINTAINING_STATE_AS_PRIMARY_COORDINATOR,
                        new String[]{this.my_addr.toString()});
            } else {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.CONTROL_STATE_CHANGE,
                        new String[]{this.my_addr.toString(),
                                this.PRIMARY_COORDINATOR_ADDR.toString()});
            } // End of Inner Else.
            // ****************************
            // Establish As Primary.
            this.PRIMARY_COORDINATOR_ADDR = this.my_addr; // We are now the Primary.
            this.PRIMARY_COORDINATOR = true;
        } else {
            this.PRIMARY_COORDINATOR = false;
            if (mbrs.size() > 0) {
                this.PRIMARY_COORDINATOR_ADDR = mbrs.get(0);

                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.CONTROL_NOT_PRIMARY_COORDINATOR,
                        new String[]{this.PRIMARY_COORDINATOR_ADDR.toString()});
            } else {
                this.PRIMARY_COORDINATOR_ADDR = null;
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        MessageConstants.CONTROL_NO_PRIMARY_COORDINATOR);
            } // End of Else.
        } // End of Outer Else.
    } // End of setMembershipState.

    /**
     * Provide convience method to start Publisher Thread.
     */
    private void startPublisherThread() {
        final String METHODNAME = "startPublisherThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Start the Common Publisher Service Thread.
        //
        PUBLISHER_THREAD = new IRRChangeLogRestoreServicePublisherThread(
                this.my_channel,
                this.cos_publisher_common);

        // ******************************************
        // Check for the Publisher Thread
        // to be running...
        //
        if (PUBLISHER_THREAD.t.isAlive()) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_PUBLISHER_THREAD_RUNNING);
        } else {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    MessageConstants.CONTROL_PUBLISHER_THREAD_NOT_RUNNING);
        } // End of Else.

    } // End of startPublisherThread

    /**
     * Provide convience method to stop Publisher Thread.
     */
    private void stopPublisherThread() {
        final String METHODNAME = "stopPublisherThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Stop the Common Publisher Service Thread.
        // Indicate to my Publisher Thread
        // to End Thread.
        this.cos_publisher_common.push(
                new StackCommand(StackCommand.CL_END_OF_THREAD, this.my_addr));

        // *******************************************
        // Now Join the Reader Thread.
        try {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_PUBLISHER_THREAD_WATING_TO_COMPLETE);
            PUBLISHER_THREAD.t.join();
        } catch (InterruptedException e) {
        } // End of Exception.

        // ****************************
        // Thread Stopped.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.CONTROL_PUBLISHER_THREAD_STOPPED);

    } // End of stopPublisherThread

    /**
     * Provide convience method to Send an Echo Command to our Publisher Thread.
     */
    private void publishEcho() {
        final String METHODNAME = "publishEcho";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Stop the Common Publisher Service Thread.
        // Indicate to my Publisher Thread
        // to End Thread.
        this.cos_publisher_common.push(
                new StackCommand(StackCommand.CL_ECHO, this.my_addr));
    } // End of publishEcho


    /**
     * Provide convience method to start Publisher Thread.
     */
    private void startReaderThread() {
        final String METHODNAME = "startReaderThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Check before we do something stupid.
        if ((this.READER_THREAD != null) &&
                (this.READER_THREAD.t.isAlive())) {
            return;
        }

        // **************************************************
        // Start the Change Log Queue Service.
        // This is used when this instance
        // is a Primary Coordinator.
        //
        READER_THREAD = new IRRChangeLogRestoreServiceReaderThread(
                this,
                this.cos_control_to_changelog,
                this.cos_publisher_common,
                this.INPUT_PATH,
                this.PUBLISH_EXCLUDE_DN_FILTER_FILE);

        // ******************************************
        // Check for the Reader Thread
        // to be running...
        //
        if (READER_THREAD.t.isAlive()) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_READER_THREAD_RUNNING);
        } else {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    MessageConstants.CONTROL_READER_THREAD_NOT_RUNNING);
        } // End of Else.

    } // End of startReaderThread.

    /**
     * Provide convience method to stop Reader Thread.
     */
    private void stopReaderThread() {
        final String METHODNAME = "stopReaderThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Check before we do something stupid.
        if ((this.READER_THREAD == null) ||
                (!this.READER_THREAD.t.isAlive())) {
            return;
        }

        // **************************************************
        // Stop the Reader Service Thread.
        // Indicate to my Reader Thread
        // to End Thread.
        this.cos_control_to_changelog.push(
                new StackCommand(StackCommand.CL_END_OF_THREAD, this.my_addr));

        // *******************************************
        // Now Join the Reader Thread.
        try {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_READER_THREAD_WATING_TO_COMPLETE);
            this.READER_THREAD.t.join();
        } catch (InterruptedException e) {
        } // End of Exception.

        // ****************************
        // Thread Stopped.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.CONTROL_READER_THREAD_STOPPED);
        this.READER_THREAD = null;
    } // End of stopReaderThread

    /**
     * Provide convience method to start Replica Restore Thread.
     */
    private void startReplicaRestoreThread() {
        final String METHODNAME = "startReplicaRestoreThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Check before we do something stupid.
        if ((this.REPLICARESTORE_THREAD != null) &&
                (this.REPLICARESTORE_THREAD.t.isAlive())) {
            return;
        }

        // **************************************************
        // Start the Common Publisher Service Thread.
        //
        REPLICARESTORE_THREAD = new IRRChangeLogRestoreServiceReplicaRestoreThread(
                this,
                this.cos_control_to_replicarestore,
                this.cos_publisher_common,
                this.IRRHost,
                this.IRRPrincipal,
                this.IRRCredentials,
                this.RESTORE_EXCLUDE_DN_FILTER_FILE);

        // ******************************************
        // Check for the Reader Thread
        // to be running...
        //
        if (REPLICARESTORE_THREAD.t.isAlive()) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_REPLICARESTORE_THREAD_RUNNING);
        } else {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    MessageConstants.CONTROL_REPLICARESTORE_THREAD_NOT_RUNNING);
        } // End of Else.


    } // End of startReplicaRestoreThread.

    /**
     * Provide convience method to stop Replica Restore Thread.
     */
    private void stopReplicaRestoreThread() {
        final String METHODNAME = "stopReplicaRestoreThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Check before we do something stupid.
        if ((this.REPLICARESTORE_THREAD == null) ||
                (!this.REPLICARESTORE_THREAD.t.isAlive())) {
            return;
        }

        // **************************************************
        // Stop the Replica Restore Service Thread.
        // Indicate to my Reader Thread
        // to End Thread.
        this.cos_control_to_replicarestore.push(
                new StackCommand(StackCommand.CL_END_OF_THREAD, this.my_addr));

        // *******************************************
        // Now Join the Replica Restore Thread.
        try {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_REPLICARESTORE_THREAD_WATING_TO_COMPLETE);
            this.REPLICARESTORE_THREAD.t.join();
        } catch (InterruptedException e) {
        } // End of Exception.

        // ****************************
        // Thread Stopped.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.CONTROL_REPLICARESTORE_THREAD_STOPPED);
        this.REPLICARESTORE_THREAD = null;
    } // End of stopReplicaRestoreThread.

    /**
     * Provide convience method to start Publisher Thread.
     */
    private void startWebAdminThread() {
        final String METHODNAME = "startWebAdminThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Verify we have a valid Web Admin Port.
        if (this.WEBADMIN_PORT < DEFAULT_WEBADMIN_PORT) {
            this.WEBADMIN_PORT = DEFAULT_WEBADMIN_PORT;
        }
        // **************************************************
        // Start the Change Log Queue Service.
        // This is used when this instance
        // is a Primary Coordinator.
        //
        WEBADMIN_THREAD = new WebAdminServerThread(this,
                this.cos_control_to_webadmin,
                this.cos_publisher_common,
                this.WEBADMIN_PORT,
                this.WEBADMIN_ALLOW_LIST);

        // ******************************************
        // Check for the Reader Thread
        // to be running...
        //
        if (WEBADMIN_THREAD.t.isAlive()) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_WEBADMIN_THREAD_RUNNING);
        } else {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    MessageConstants.CONTROL_WEBADMIN_THREAD_NOT_RUNNING);
        } // End of Else.

    } // End of startWebAdminThread.

    /**
     * Provide convience method to stop Reader Thread.
     */
    private void stopWebAdminThread() {
        final String METHODNAME = "stopWebAdminThread";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **************************************************
        // Stop the Reader Service Thread.
        // Indicate to my Reader Thread
        // to End Thread.
        this.cos_control_to_webadmin.push(
                new StackCommand(StackCommand.CL_END_OF_THREAD, this.my_addr));

        // *******************************************
        // Now Join the Reader Thread.
        try {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.CONTROL_WEBADMIN_THREAD_WATING_TO_COMPLETE);
            WEBADMIN_THREAD.t.join();
        } catch (InterruptedException e) {
        } // End of Exception.

        // ****************************
        // Thread Stopped.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.CONTROL_WEBADMIN_THREAD_STOPPED);

    } // End of stopWebAdminThread

    /**
     * startJGroups convience method to instantiate our
     * Channel Membership.
     */
    private void startJGroups() throws Exception {
        final String METHODNAME = "startJGroups";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // **********************************
        // Create the Channel.
        my_channel = new JChannel(my_property_string);
        //my_channel.setOpt(Channel.AUTO_RECONNECT, Boolean.TRUE);

        // **********************************
        // Connect to Channel Membership.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.CONTROL_CONNECTING_TO_GROUP, new String[]{this.my_group_name});

        my_channel.connect(my_group_name);
        my_addr = my_channel.getAddress();

        // ***********************************
        // Instantiate Adapter to establish
        // Listeners.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.CONTROL_ESTABLISHING_ADAPTER);

        //my_adapter = new PullPushAdapter(my_channel, this, this);

    } // End of startJGroups.

    /**
     * Helper method to formulate the JGroups Property String.
     *
     * @return String Forumlated JGroups Property String.
     */
    private String formulateJGroupsProperties() {
        StringBuffer sb = new StringBuffer();

        // *****************************
        // Initialize with UDP Layer.
        sb.append(IRRChangeLogRestoreServiceControlThread.UDP_JGROUP_PROPERTY_STRING);

        // *****************************
        // Zap in the Multicast Address
        int i = sb.indexOf("%1");
        if (i > 0) {
            sb.replace(i, (i + 2), this.my_mcast_address);
        } // End of Multicast Address Zap.
        else {
            return IRRChangeLogRestoreServiceControlThread.DEFAULT_JGROUP_PROPERTY_STRING;
        } // end of Else.

        // *****************************
        // Zap in the MultiCast Port.
        i = sb.indexOf("%2");
        if (i > 0) {
            sb.replace(i, (i + 2), this.my_mcast_port);
        } // End of Multicast Address Zap.
        else {
            return IRRChangeLogRestoreServiceControlThread.DEFAULT_JGROUP_PROPERTY_STRING;
        } // end of Else.

        // *****************************
        // Set up Remaining Layers
        sb.append(IRRChangeLogRestoreServiceControlThread.PING_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.MERGE2_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.FD_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.VERIFY_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.PBCAST_NAKACK_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.UNICAST_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.PBCAST_STABLE_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.FRAG_JGROUP_PROPERTY_STRING);
        sb.append(IRRChangeLogRestoreServiceControlThread.PBCAST_GMS_JGROUP_PROPERTY_STRING);

        // *****************************
        // Return Formulated String.
        return sb.toString();
    } // End of formulateJGroupsProperties private Method.

    /**
     * Formulate a List from a String seperated by commas.
     *
     * @param str
     */
    private void formulateWebAdminAllowList(String str) {
        final String METHODNAME = "formulateWebAdminAllowList";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // ********************************
        // Initialize our Allow List.
        this.WEBADMIN_ALLOW_LIST = new LinkedList<>();

        // ********************************
        // Ensure we have something in the
        // String.
        if ((str == null) ||
                (str.trim().equalsIgnoreCase(""))) {
            return;
        }

        // ********************************
        // Process each Element.
        String[] elements = str.split("\\,");
        for (int i = 0; i < elements.length; i++) {
            if ((elements[i] != null) &&
                    (!elements[i].trim().equalsIgnoreCase(""))) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                        MessageConstants.CONTROL_ADDING_WEBADMIN_ALLOWABLE_ADDRESS,
                        new String[]{elements[i]});
                this.WEBADMIN_ALLOW_LIST.add(elements[i]);
            } // End of Inner If Check for Valid Element.
        } // End of For Loop.
    } // End of formulateWebAdminAllowList private Method.

    /**
     * Private Shutdown Method to wrap all
     * final processing.
     */
    private void shutdown() {
        final String METHODNAME = "shutdown";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        // *********************************
        // Perform Clean-up to ready for
        // Shutdown.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.SHUTDOWN_STARTED);

        // **********************************
        // Stop All Threads, gracefully.
        this.stopReplicaRestoreThread();

        this.stopReaderThread();

        this.stopPublisherThread();

        this.stopWebAdminThread();

        // ***********************************
        // Now Stop our Adapter.
        //this.my_adapter.stop();

        // ***********************************************
        // notify controller thread that we have finished
        synchronized (this) {
            notify();
        } // End of Synchronized Code Area.
        // *************************************
        // Perform Final Message Indication for
        // Shutdown.
        gsw.stop();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.SHUTDOWN_COMPLETED,
                new String[]{gsw.getElapsedTimeString()});
    } // End of shutdown private method.

    /**
     * Get Current Component Status.
     *
     * @return String Data Representing Current Status of this Component.
     */
    private String getStatus() {

        // **************************************
        // Initialize.
        final String METHODNAME = "getStatus";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_CONTROL_METHOD, new String[]{METHODNAME});

        StringBuffer sb = new StringBuffer();
        // **************************************
        // Build String Buffer with Status for
        // this component.

        // **************************************
        // Show Header
        sb.append(WebAdminResponderThread.COMPONENT_BEGIN);
        sb.append("Control Thread");
        sb.append(WebAdminResponderThread.COMPONENT_END);

        // **************************************
        // Show Detail.
        sb.append(WebAdminResponderThread.BEGIN_TABLE);
        sb.append(WebAdminResponderThread.BEGIN_ROW);
        sb.append(WebAdminResponderThread.BEGIN_COL);

        sb.append(WebAdminResponderThread.BEGIN_TABLE);

        sb.append(WebAdminResponderThread.build2ColumnRow("UpTime",
                this.gsw.getElapsedTimeString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Group Name",
                this.my_group_name));

        sb.append(WebAdminResponderThread.build2ColumnRow("This Address",
                this.my_addr.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Primary Coordinator Address",
                this.PRIMARY_COORDINATOR_ADDR.toString()));

        if (this.PRIMARY_COORDINATOR) {
            sb.append(WebAdminResponderThread.build2ColumnRow("Primary Coordinator",
                    WebAdminResponderThread.POSITIVE_VALUE +
                            Boolean.toString(this.PRIMARY_COORDINATOR)) +
                    WebAdminResponderThread.POSITIVE_VALUE_END);
        } else {
            sb.append(WebAdminResponderThread.build2ColumnRow("Primary Coordinator",
                    WebAdminResponderThread.NEUTRAL_VALUE +
                            Boolean.toString(this.PRIMARY_COORDINATOR)) +
                    WebAdminResponderThread.NEUTRAL_VALUE_END);
        } // End of Else Check for Primary.

        sb.append(WebAdminResponderThread.END_TABLE);
        sb.append(WebAdminResponderThread.END_COL);
        sb.append(WebAdminResponderThread.BEGIN_COL);
        sb.append(WebAdminResponderThread.BEGIN_TABLE);

        sb.append(WebAdminResponderThread.build2ColumnRow("Component Failure Count",
                Long.toString(this.FAILURE_COUNT)));

        sb.append(WebAdminResponderThread.build2ColumnRow("Control Stack Commands",
                Long.toString(this.stackcommands)));

        sb.append(WebAdminResponderThread.build2ColumnRow("Routed Received Messages",
                Long.toString(this.receivedstackcommands)));

        sb.append(WebAdminResponderThread.build2ColumnRow("Invalid Received Messages",
                Long.toString(this.receivedinvalidobjects)));

        sb.append(WebAdminResponderThread.build2ColumnRow("Membership Updates Received",
                Long.toString(this.membershipupdatesreceived)));

        //sb.append(WebAdminResponderThread.build2ColumnRow("Messages Waiting on Channel",
        //        Long.toString(this.my_channel.getNumMessages())));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Messages From Input Stack",
                this.LP_ENTRY_FROM_COS.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Reading Log Directory",
                this.LP_ENTRY_FROM_LOGDIR.toString()));

        sb.append(WebAdminResponderThread.END_TABLE);
        sb.append(WebAdminResponderThread.END_COL);

        sb.append(WebAdminResponderThread.END_ROW);
        sb.append(WebAdminResponderThread.END_TABLE);

        // ****************************
        // Now Provide Channel Detail.
        //sb.append("<PRE>");
        //sb.append( this.my_channel.printProtocolSpec(true) );
        //sb.append("</PRE>");

        // ***************************
        // Return Formatted Status.
        return sb.toString();
    } // End of Protected GetStatus Method.

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

} ///:~ End of Class IRRChangeLogRestoreServiceControlThread
