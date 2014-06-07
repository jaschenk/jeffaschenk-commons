package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;

import java.util.*;

/**
 * Service Shutdown Thread for the IRRChangeLogRestoreService Facility.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */


public class IRRChangeLogRestoreServiceShutdownThread extends Thread {

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreServiceShutdownThread.class.getName();

    // *******************************
    // Work Thread to attach to.
    private Thread controlThread;

    /**
     * IRRChangeLogRestoreServiceShutdownThread Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRChangeLogRestoreServiceShutdownThread(Thread controlThread) {
        this.controlThread = controlThread;
    } // End of Constructor for IRRChangeLogRestoreServiceShutdownThread.

    /**
     * Shutdown Thread.
     */
    public void run() {
        String METHODNAME = "run";
        this.setName("ServiceShutdownThread");

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                MessageConstants.SHUTDOWN_REQUESTED,
                new String[]{new Date().toString()});

        // **********************************
        // Check to see if the Control Thread
        // is alinve, it may have been shutdown
        // from the webadmin thread.
        if (controlThread.isAlive()) {
            // **********************************
            // Request worker thread to finish
            controlThread.interrupt();

            // ************************************
            // Wait for the worker thread to finish
            try {
                synchronized (controlThread) {
                    controlThread.wait();
                } // End of Synchronized Code Area.
            } catch (InterruptedException e) {
            } // End of Exception.
        } // End of If check for Control Thread Being Alive.

        // ****************************************
        // Give our final Goodbye.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                MessageConstants.SHUTDOWN_FINISHED,
                new String[]{new Date().toString()});
        // *************************
        // Finish up the Service.
        return;
    } // End of run Thread.

} // End of Class IRRChangeLogRestoreServiceShutdownThread
