package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;

import java.util.*;

/**
 * Service Shutdown Thread for the IRRChangeLogRestoreService Facility.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */


public class IRRChangeLogRestoreServiceShutdownThread extends Thread {

    public static final String VERSION = "FRAMEWORK, Incorporated Version: 3.1 2003-09-12";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreServiceShutdownThread.class.getName();
    public static idxLogger IDXLOG = new idxLogger();

    // *******************************
    // Work Thread to attach to.
    private Thread workerThread;

    /**
     * IRRChangeLogRestoreServiceShutdownThread Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRChangeLogRestoreServiceShutdownThread(Thread workerThread) {
        this.workerThread = workerThread;
    } // End of Constructor for IRRChangeLogRestoreServiceShutdownThread.

    /**
     * Shutdown Thread.
     */
    public void run() {
        String METHODNAME = "run";
        IDXLOG.info(CLASSNAME, METHODNAME, "Shutdown requested at " + new Date());

        // **********************************
        // Request worker thread to finish
        workerThread.interrupt();

        // ************************************
        // Wait for the worker thread to finish
        try {
            synchronized (workerThread) {
                workerThread.wait();
            } // End of Synchronized Code Area.
        } catch (InterruptedException e) {
        } // End of Exception.
        IDXLOG.info(CLASSNAME, METHODNAME, "Shutdown completed at " + new Date());
    } // End of run Thread.

} // End of Class IRRChangeLogRestoreServiceShutdownThread
