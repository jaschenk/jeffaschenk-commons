package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxManageContext;

/**
 * Service Worker Thread to perform Mainloop to Drive Log Restore Facilities.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */

public class IRRChangeLogRestoreServiceWorkerThread extends Thread
        implements idxCMDReturnCodes {

    public static final String VERSION = "FRAMEWORK, Incorporated Version: 3.1 2003-09-12";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreServiceWorkerThread.class.getName();
    public static idxLogger IDXLOG = new idxLogger();

    // *******************************
    // Global Variables.
    private idxManageContext IRRDest = null;
    //
    private String IRRHost = null;
    private String IRRPrincipal = null;
    private String IRRCredentials = null;
    //
    private String INPUT_PATH = null;
    private String STATE_TAGNAME = null;
    private String LDIF_FILTER_FILE = null;
    //
    private long FAILURE_COUNT = 0;
    // ********************************

    // ********************************
    // Loop Timers.
    private final long BEGIN_WAIT_DURATION = (1000 * 2);         // Start at 2 Seconds.
    private final long HIGH_WAIT_DURATION = (1000 * 300);       // High Wait Duration is 5 Minutes.
    private final long INCREMENTAL_WAIT_DURATION = (1000 * 30);  // Increment at 30 Seconds.

    private long CURRENT_WAIT_DURATION = BEGIN_WAIT_DURATION;

    /**
     * IRRChangeLogRestoreServiceWorkerThread Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRChangeLogRestoreServiceWorkerThread(String IRRHost,
                                                  String IRRPrincipal,
                                                  String IRRCredentials,
                                                  String INPUT_PATH,
                                                  String STATE_TAGNAME,
                                                  String LDIF_FILTER_FILE) {
        this.IRRHost = IRRHost;
        this.IRRPrincipal = IRRPrincipal;
        this.IRRCredentials = IRRCredentials;
        this.INPUT_PATH = INPUT_PATH;
        this.STATE_TAGNAME = STATE_TAGNAME;
        this.LDIF_FILTER_FILE = LDIF_FILTER_FILE;
    } // End of Constructor for IRRChangeLogRestoreServiceWorkerThread.

    /**
     * Service Worker Thread
     */
    public void run() {
        String METHODNAME = "run";
        // *********************************************
        // Perform our Log Restore Facility until
        // work until the Thread is interrupted
        // at Shutdown request Time.
        //
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // ***************************
                // Wait for a Tick or two.
                // Reset if we reached our max
                // wait interval.
                if (CURRENT_WAIT_DURATION > HIGH_WAIT_DURATION) {
                    CURRENT_WAIT_DURATION = BEGIN_WAIT_DURATION;
                }

                Thread.sleep(CURRENT_WAIT_DURATION);

                // ***********************************
                // Now create a new Driver Object
                IRRChangeLogRestoreDriver LRD = new IRRChangeLogRestoreDriver();

                // ****************************************
                // Set the Type of files, if appropreiate.
                LRD.useDCLExportLDIFFiles();

                // ****************************************
                // Ok, check for Unprocessed Files.
                if (LRD.unprocessedFileCount(INPUT_PATH, STATE_TAGNAME) == 0) {
                    LRD = null;
                    continue;
                } // End of If.

                // ***********************************
                // Establish new Directory Context.
                if (!establishDirectoryContext()) {
                    LRD = null;
                    CURRENT_WAIT_DURATION = +INCREMENTAL_WAIT_DURATION;
                    continue;
                } // End of If.

                // ***********************************
                // Invoke the Driver Object to Scan for
                // any unprocessed LDIF Log or Export
                // Files.
                //

                // check for files that contain updates that are to be blocked
                try {
                    LRD.filterFiles(INPUT_PATH, STATE_TAGNAME, LDIF_FILTER_FILE);
                } catch (IRRLdifFilterException e) {
                    IDXLOG.severe(CLASSNAME, METHODNAME, "It was not possible to filter updates" + e);
                } catch (Exception e) {
                    IDXLOG.severe(CLASSNAME, METHODNAME, "Unexpected exception occured. It was not possible to filter updates" + e);
                }

                try {
                    LRD.perform(IRRDest.irrctx, INPUT_PATH, STATE_TAGNAME);
                } catch (idxIRRException ire) {
                    IDXLOG.severe(CLASSNAME, METHODNAME, "IRR Processing Exception Detected: " + ire.getMessage());
                } catch (Exception e) {
                    IDXLOG.severe(CLASSNAME, METHODNAME, "Other Processing Exception Detected: " + e);
                } // End of Exception.

                // *************************************
                // Determine if Files were Processed
                if (LRD.wereFilesProcessed()) {
                    LRD.dumpStats();
                    CURRENT_WAIT_DURATION = BEGIN_WAIT_DURATION;
                } // End of if.

                // *************************************
                // Close up current Directory Context.
                LRD = null;
                closeDirectoryContext();

            } // End of While Loop.
        } catch (InterruptedException e) {
            IDXLOG.warning(CLASSNAME, METHODNAME, "Interrupt Exception Detected in run While Loop: " + e);
        } // End of Exception.

        // *********************************
        // Perform Clean-up to ready for
        // Shutdown.
        IDXLOG.info(CLASSNAME, METHODNAME, "Finished Run Loop, Shutdown Commencing.");
        try {
            Thread.sleep(10000); // Wait at least ten Seconds.
        } catch (InterruptedException e) {
            ; // NOOP.
        } // End of Exception.
        // ***********************************************
        // notify controller thread that we have finished
        synchronized (this) {
            notify();
        } // End of Synchronized Code Area.
    } // End of Thread Execution run.

    /**
     * Establish new Directory Context for the worker
     * thread to use with downstream facilities.
     *
     * @return Indicator if Context Established or not.
     */
    private boolean establishDirectoryContext() {

        String METHODNAME = "establishDirectoryContext";

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        IDXLOG.fine(CLASSNAME, METHODNAME, "Attempting FRAMEWORK Directory Connection to Host URL:[" + IRRHost + "]");

        IRRDest = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRChangeLogRestoreServiceWorkerThread Destination");

        // ************************************************
        // Exit on all Exceptions.
        IRRDest.setExitOnException(false);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRDest.open();
            FAILURE_COUNT = 0;
        } catch (Exception e) {
            if (FAILURE_COUNT <= 7) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "Error Opening Directory Context: " + e);
                FAILURE_COUNT++;
            } // End of Failure Count Check, so not to explode a Log.
            return (false);
        } // End of exception

        // *****************************************
        // Disable the Factories.
        try {
            IRRDest.disableDSAEFactories();
            FAILURE_COUNT = 0;
        } catch (Exception e) {
            if (FAILURE_COUNT <= 7) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "Error Disabling DSAE Factories: " + e);
                FAILURE_COUNT++;
            } // End of Failure Count Check, so not to explode a Log.
            return (false);
        } // End of exception  

        // ******************************
        // Return indicating a good
        // Context Established.
        return (true);
    } // End of establishDirectoryContext Method.   

    /**
     * Close an Established Directory Context.
     */
    private void closeDirectoryContext() {

        String METHODNAME = "closeDirectoryContext";

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        IDXLOG.fine(CLASSNAME, METHODNAME, "Closing FRAMEWORK Directory Connection to Host URL:[" + IRRHost + "]");

        // ***************************************
        // Close up Shop.
        try {
            if ((IRRDest != null) &&
                    (IRRDest.irrctx != null)) {
                IRRDest.close();
            }
        } catch (Exception e) {
            IDXLOG.warning(CLASSNAME, METHODNAME, "Exception detected Closing Directory Connection " +
                    e);
        } // End of exception.

        // ****************************************
        // Finalize Objects.
        IRRDest.irrctx = null;
        IRRDest = null;

    } // End of closeDirectoryContext Method.

} // End of Class IRRChangeLogRestoreServiceWorkerThread
