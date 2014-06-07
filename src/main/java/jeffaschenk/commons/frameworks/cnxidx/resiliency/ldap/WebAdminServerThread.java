package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;

import java.nio.channels.Selector;
import java.util.*;


/**
 * Change Log Restore WEB Admin Thread.  Creates an Embedded WEB Server to
 * serve up status and provide operational ability to the end customer
 * to operate the Change Log Restore Facility.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

/**
 * WebAdminServerThread
 * Class to run Embedded Web Admin Thread.
 */
class WebAdminServerThread implements Runnable {

    // *******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = WebAdminServerThread.class.getName();

    /**
     * WebAdminServerThread
     * Class to provide Web Admin Interface to Log Restore Facility.
     */
    Thread t;
    private static final String THREAD_NAME
            = "ServiceWebAdminThread";

    private IRRChangeLogRestoreServiceControlThread
            CONTROL_THREAD = null;    // Provides interface to Local Cmds.
    private CircularObjectStack cosin;       // Input Stack.
    private CircularObjectStack cosout;      // Output to Publisher Stack.
    private int WEBADMIN_PORT;               // Embedded Web Server Port.

    // *************************************
    // NIO Socket Channel Objects.
    private WebAdminAcceptorThread acceptor = null;
    private WebAdminResponderThread responder = null;

    // ***********************************************
    // Initialize my LAP Timers
    private idxLapTime LP_ENTRY_TO_COS = new idxLapTime();
    private idxLapTime LP_ENTRY_FROM_COS = new idxLapTime();

    // ***********************************************
    // Allow List for Client Requests.
    private LinkedList WEBADMIN_ALLOW_LIST = null;

    /**
     * IRRBackupOutputThread Contructor class driven.
     *
     * @param cosin         Circular Object Stack for Obtaining Control Commands.
     * @param cosout        Circular Object Stack for placing LDAP Entries on Output Stack.
     * @param WEBADMIN_PORT HTTP port to start embedded web server.
     */
    public WebAdminServerThread(
            IRRChangeLogRestoreServiceControlThread CONTROL_THREAD,
            CircularObjectStack cosin,
            CircularObjectStack cosout,
            int WEBADMIN_PORT,
            LinkedList WEBADMIN_ALLOW_LIST) {

        // ****************************************
        // Set My Incoming Parameters.
        this.CONTROL_THREAD = CONTROL_THREAD;
        this.cosin = cosin;
        this.cosout = cosout;
        this.WEBADMIN_PORT = WEBADMIN_PORT;
        this.WEBADMIN_ALLOW_LIST = WEBADMIN_ALLOW_LIST;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        t = new Thread(this, WebAdminServerThread.THREAD_NAME);
        t.start(); // Start the Thread
    } // End of Contructor.

    /**
     * run
     * Thread to Provide Web Admin Interface to the JGroups Cloud.
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
        boolean running = false;
        int ecount = 0;

        // ***********************************************
        // Initialize our Web Interface.
        while (!running) {
            try {
                this.init();
                running = true;
            } catch (Exception ioe) {
                if (ecount == 0) {
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                            ErrorConstants.WEBADMIN_OBTAINING_HTTP_SOCKET_EXCEPTION,
                            new String[]{Integer.toString(this.WEBADMIN_PORT), ioe.toString()});
                } else if (ecount > 1000) {
                    ecount = 0;
                }
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException ie) {
                }
            } // End of Exception Processing.
        } // End of Loop to Start the HTTP Socket.

        // *************************************************
        // Indicate we have established the Socket, so ready
        // to enter our main Loop.
        //
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_THREAD_ESTABLISHED,
                new String[]{Thread.currentThread().getName()});

        // ******************************************
        // Loop to process commands from our
        // Input Stack, if it is not a End of Thread
        // place it back on the stack.
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
            if (instackcommand != null) {
                // ******************************
                // Did our Running State trip to
                // not based upon a End of Thread
                // command? If so, never place this
                // Command on the BUS.
                if (instackcommand.getCommandType() ==
                        StackCommand.CL_END_OF_THREAD) {
                    running = false;
                    continue; // Force our loop to End.
                } // End of Check for End of Thread.

                // *************************************
                // We need to place whatever we took off
                // the Stack Back on as this command
                // was probably meant for a Client
                // Web Thread.
                cosin.push(instackcommand);
            } // End of Nothing in Stack yet to Process.
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            } // End of Else.
        } // End of Main While Loop.

        // ***************************************
        // Shutdown our Child Threads.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_THREAD_SHUTDOWN_CHILD_THREADS);
        this.acceptor.shutdown();
        this.responder.shutdown();

        // ***************************************
        // Now wait for Threads to finish, if they
        // have not already.
        try {
            acceptor.join();
        } catch (InterruptedException e) {
            // NOOP.
        } // End of Exception.

        try {
            responder.join();
        } catch (InterruptedException e) {
            // NOOP.
        } // End of Exception.

        // ***************************************
        // Show the Lap Timings.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_LAPTIME_FROM_STACK,
                new String[]{LP_ENTRY_FROM_COS.toString()});

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_LAPTIME_TO_STACK,
                new String[]{LP_ENTRY_TO_COS.toString()});

        // ***************************************
        // Show the Duration of Thread.
        sw.stop();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_THREAD_SHUTDOWN,
                new String[]{sw.getElapsedTimeString()});

        // ***************************************
        // Done.
        return;

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
        sb.append(WebAdminResponderThread.COMPONENT_BEGIN);
        sb.append("WEB ADMIN Thread");
        sb.append(WebAdminResponderThread.COMPONENT_END);

        // 	 **************************************
        // Show Detail.
        sb.append(WebAdminResponderThread.BEGIN_TABLE);

        sb.append(WebAdminResponderThread.build2ColumnRow("Web Admin Port",
                Integer.toString(this.WEBADMIN_PORT)));

        // ***********************************
        // Check Acceptor Child Thread Status
        if (this.acceptor != null) {
            if (this.acceptor.isAlive()) {
                sb.append(WebAdminResponderThread.build2ColumnRow("Acceptor SubThread Active",
                        WebAdminResponderThread.POSITIVE_VALUE +
                                Boolean.toString(this.acceptor.isAlive()) +
                                WebAdminResponderThread.POSITIVE_VALUE_END));
            } else {
                sb.append(WebAdminResponderThread.build2ColumnRow("Acceptor SubThread Active",
                        WebAdminResponderThread.CAUTION_VALUE +
                                Boolean.toString(this.acceptor.isAlive()) +
                                WebAdminResponderThread.CAUTION_VALUE_END));
            } // End of Inner Else.
        } else {
            sb.append(WebAdminResponderThread.build2ColumnRow("Acceptor SubThread Active",
                    WebAdminResponderThread.WARNING_VALUE +
                            "Not Running" +
                            WebAdminResponderThread.WARNING_VALUE_END));
        } // End of Else.

        // ***********************************
        // Check Responder Child Thread Status
        if (this.responder != null) {
            if (this.responder.isAlive()) {
                sb.append(WebAdminResponderThread.build2ColumnRow("Responder SubThread Active",
                        WebAdminResponderThread.POSITIVE_VALUE +
                                Boolean.toString(this.responder.isAlive()) +
                                WebAdminResponderThread.POSITIVE_VALUE_END));
            } else {
                sb.append(WebAdminResponderThread.build2ColumnRow("Responder SubThread Active",
                        WebAdminResponderThread.CAUTION_VALUE +
                                Boolean.toString(this.responder.isAlive()) +
                                WebAdminResponderThread.CAUTION_VALUE_END));
            } // End of Inner Else.
        } else {
            sb.append(WebAdminResponderThread.build2ColumnRow("Responder SubThread Active",
                    WebAdminResponderThread.WARNING_VALUE +
                            "Not Running" +
                            WebAdminResponderThread.WARNING_VALUE_END));
        } // End of Else.

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Message From Input Stack",
                LP_ENTRY_FROM_COS.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Message To Output Stack",
                LP_ENTRY_TO_COS.toString()));


        // **************************************
        // Finish our component status table.
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
        this.LP_ENTRY_TO_COS.Reset();
        this.LP_ENTRY_FROM_COS.Reset();
    } // End of resetStatistics protected method.

    /**
     * Private Initialization Method to Start Connection Acceptance and Responder
     * threads.
     *
     * @throws Exception
     */
    private void init() throws Exception {
        Selector acceptSelector = Selector.open();
        Selector readSelector = Selector.open();
        WebAdminConnectionList connections = new WebAdminConnectionList(readSelector);

        // **********************************
        // Start Acceptor Thread.
        acceptor = new WebAdminAcceptorThread(acceptSelector, connections,
                this.WEBADMIN_PORT);

        // **********************************
        // Start Responder Thread.
        responder = new WebAdminResponderThread(readSelector, connections,
                this.CONTROL_THREAD,
                this.cosin,
                this.cosout,
                this.WEBADMIN_ALLOW_LIST);

        // ***************************
        // Start these Threads.
        this.responder.start();
        this.acceptor.start();
    } // End of private init method.

} ///:~ End of Class WebAdminServerThread
