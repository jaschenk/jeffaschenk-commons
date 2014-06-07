package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.io.*;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;
import org.jgroups.Address;
import org.jgroups.Channel;

/**
 * IRR Change Log Restore Service Publisher will publish StackCommands which
 * can contain replication data or control data to the Members of the current
 * Group established in the Control Thread.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

/**
 * IRRChangeLogRestoreServicePublisherThread
 * Class to run Publisher Thread.
 */
class IRRChangeLogRestoreServicePublisherThread implements Runnable {

    // *******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = IRRChangeLogRestoreServicePublisherThread.class.getName();

    /**
     * IRRChangeLogRestoreServicePbblisherThread
     * Class to provide common thread to publish replication or control commands
     * to the Members of our Group.
     */
    Thread t;
    private static final String THREAD_NAME
            = "IRRChangeLogRestoreServicePublisherThread";

    private CircularObjectStack cosin;       // Input Stack.
    private Channel my_channel;  // Output to Membership Community.
    private long messages_published = 0;     // Count of Messages Published.

    // ***********************************************
    // Initialize my LAP Timers
    private idxLapTime LP_ENTRY_TO_CHANNEL = new idxLapTime();
    private idxLapTime LP_ENTRY_FROM_COS = new idxLapTime();

    /**
     * IRRBackupOutputThread Contructor class driven.
     *
     * @param my_channel
     * @param cosin      Circular Object Stack for Obtaining Control Commands.
     */
    IRRChangeLogRestoreServicePublisherThread(
            Channel my_channel,
            CircularObjectStack cosin) {

        // ****************************************
        // Set My Incoming Parameters.
        this.cosin = cosin;
        this.my_channel = my_channel;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        t = new Thread(this, IRRChangeLogRestoreServicePublisherThread.THREAD_NAME);
        t.start(); // Start the Thread
    } // End of Contructor.


    /**
     * run
     * Thread to Publish Message from various threads to the JGroups Cloud.
     */
    public void run() {

        // ***********************************************
        // Initialize our StopWatch to measure Duration
        // of Thread.
        String METHODNAME = "run";
        StopWatch sw = new StopWatch();
        sw.start();

        // ***********************************************
        // Initialize Thread Variables.
        StackCommand instackcommand = null;
        boolean running = true;

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.PUBLISHER_THREAD_ESTABLISHED,
                new String[]{Thread.currentThread().getName()});

        // **************************************
        // Loop to process commands from Walker
        // Thread.
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
                continue;
            } // End of Nothing in Stack yet to Process.

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

            // ******************************
            // Ok, Assume we are to Publish.
            //
            try {
                LP_ENTRY_TO_CHANNEL.Start();
                if (instackcommand.getDestination() != null) {
                    my_channel.send((Address) instackcommand.getDestination(),
                            // (Address)instackcommand.getOriginator(),
                            instackcommand);
                } else {
                    my_channel.send(null, instackcommand);
                }
                LP_ENTRY_TO_CHANNEL.Stop();
                messages_published++;
            } catch (Exception ce) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.PUBLISH_TO_CHANNEL_EXCEPTION,
                        new String[]{instackcommand.toString(), ce.toString()});

                this.logStackTrace(ce, METHODNAME,
                        ErrorConstants.PUBLISH_TO_CHANNEL_EXCEPTION_STACK_TRACE);

                // TODO Count Number of Exceptions, we really need to retry this....
            } // End of Exception Processing.

        } // End of Outer While Loop.

        // ***************************************
        // Show the Lap Timings.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.PUBLISHER_LAPTIME_FROM_STACK,
                new String[]{LP_ENTRY_FROM_COS.toString()});

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.PUBLISHER_LAPTIME_TO_CHANNEL,
                new String[]{LP_ENTRY_TO_CHANNEL.toString()});

        // ***************************************
        // Show the Duration of Thread.
        sw.stop();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.PUBLISHER_THREAD_SHUTDOWN,
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
        sb.append("Publisher Thread");
        sb.append(WebAdminResponderThread.COMPONENT_END);

        // **************************************
        // Show Detail.
        sb.append(WebAdminResponderThread.BEGIN_TABLE);

        sb.append(WebAdminResponderThread.build2ColumnRow("Messages Published",
                Long.toString(this.messages_published)));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Messages to Channel",
                LP_ENTRY_TO_CHANNEL.toString()));

        sb.append(WebAdminResponderThread.build2ColumnRow("Lap Time Message From Input Stack",
                LP_ENTRY_FROM_COS.toString()));

        sb.append(WebAdminResponderThread.END_TABLE);
        sb.append(WebAdminResponderThread.BREAK);

        // **************************************
        // Return Formatted Status.
        return sb.toString();
    } // End of Protected GetStatus Method.

    /**
     * Helper Method to reset Component Statistics.
     */
    protected void resetStatistics() {
        this.messages_published = 0;
        LP_ENTRY_TO_CHANNEL.Reset();
        LP_ENTRY_FROM_COS.Reset();
    } // End of resetStatistics protected method.

    /**
     * Provides Logging of Stack Traces from Exceptions detail on the Nested Stack Trace
     * from Severe Runtime Errors.
     */
    private void logStackTrace(Exception e, String METHODNAME, String MSGNUM) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        FrameworkLogger.log(CLASSNAME, METHODNAME,
                FrameworkLoggerLevel.SEVERE, MSGNUM,
                new String[]{e.getMessage(), sw.toString()});
    } // End of logStackTrace private Method.

} ///:~ End of Class IRRChangeLogRestoreServicePUblisherThread
