package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.security.FrameworkDirectoryUser;

import java.util.*;
import java.io.*;

/**
 * Java Service Driver for the IRRLogRestoreDriver.
 *
 * @author jeff.schenk
 * @version 3.1 $Revision
 * Developed 2003
 */


public class IRRChangeLogRestoreService implements idxCMDReturnCodes {

    public static final String VERSION = "FRAMEWORK, Incorporated Version: 3.1 2003-09-12";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreService.class.getName();
    public static idxLogger IDXLOG = new idxLogger();

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     */
    public static void main(String[] args) {

        // ****************************************
        // Local Objects
        Properties RunConfig = new Properties();

        String IRRHost = null;
        String IRRPrincipal = null;
        String IRRCredentials = null;
        String IRRIDU = null;

        String INPUT_PATH = null;
        String STATE_TAGNAME = null;
        String LDIF_FILTER_FILE = null;

        String METHODNAME = "main";

        // ****************************************
        // Send the Greeting.
        IDXLOG.info(CLASSNAME, METHODNAME, VERSION);
        IDXLOG.config(CLASSNAME, METHODNAME, "Configuring Service");

        // ****************************************
        // Obtain the RunTime Properties from
        // a local Property file.
        String PFileName = System.getProperty("framework.config", "service.properties");
        File PF = new File(PFileName);
        try {
            if (!PF.exists()) {
                IDXLOG.severe(CLASSNAME, METHODNAME, "Unable to obtain Configuration Properties" +
                        " from File:[" + PFileName + "], file does not exist.");
                System.exit(EXIT_GENERIC_FAILURE);
            } // End of If.

            // *************************************
            // Try to load the Properties.
            RunConfig.load(new FileInputStream(PF));
            IDXLOG.config(CLASSNAME, METHODNAME, "Properties Obtained.");
        } catch (Exception e) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "Unable to obtain Configuration Properties" +
                    " from File:[" + PFileName + "], Reason: " + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // *****************************************
        // Obtain the Properties and formulate into
        // our variables.
        IRRHost = RunConfig.getProperty("IRR.HOST.URL");
        IRRPrincipal = RunConfig.getProperty("IRR.PRINCIPAL");
        IRRCredentials = RunConfig.getProperty("IRR.CREDENTIALS");
        IRRIDU = RunConfig.getProperty("IRR.IDU");
        //
        INPUT_PATH = RunConfig.getProperty("LDIF.EXPORT.INPUT.FILE.PATH");
        STATE_TAGNAME = RunConfig.getProperty("LDIF.EXPORT.PROCESSED.TAGNAME");
        LDIF_FILTER_FILE = RunConfig.getProperty("LDIF.EXPORT.FILTER.FILE.PATH");

        //
        // *********************************************************************

        // *********************************************************************
        // Verify all the properties are present and available to continue.
        IDXLOG.config(CLASSNAME, METHODNAME, "Verifying Properties.");

        // ******************
        // Check IRRHost
        if ((IRRHost == null) ||
                (IRRHost.equalsIgnoreCase(""))) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "IRR.HOST.URL Property not Specified.");
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.           

        // ******************
        // Check IRRIDU
        if ((IRRIDU != null) &&
                (!IRRIDU.equalsIgnoreCase(""))) {
            // *****************************
            // We have an IDU Specified,
            // try to obtain Principal
            // and Credentials.
            //
            try {
                FrameworkDirectoryUser idu = new FrameworkDirectoryUser(IRRIDU);
                IRRPrincipal = idu.getDN();
                IRRCredentials = idu.getPassword();
            } catch (Exception e) {
                IDXLOG.severe(CLASSNAME, METHODNAME, "Unable to Obtain Framework Directory User " +
                        "Principal and Credentials, Exception: " + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } // End of Exception.
        } // End of If. 

        // ******************
        // Check IRRPrincipal
        if ((IRRPrincipal == null) ||
                (IRRPrincipal.equalsIgnoreCase(""))) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "IRR Principal Property not Specified, or unable to obtain.");
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.  

        // ******************
        // Check IRRCredentials
        if ((IRRCredentials == null) ||
                (IRRCredentials.equalsIgnoreCase(""))) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "IRR Credentials Property not Specified, or unable to obtain.");
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.  

        // ********************
        // Check INPUT_PATH
        if ((INPUT_PATH == null) ||
                (INPUT_PATH.equalsIgnoreCase(""))) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "LDIF.EXPORT.INPUT.FILE.PATH Property not Specified.");
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.  

        // ********************
        // Check the Paths 
        // Existence.
        File EIP = new File(INPUT_PATH);
        if ((!EIP.exists()) ||
                (!EIP.isDirectory()) ||
                (!EIP.canRead()) ||
                (!EIP.canWrite())) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "LDIF.EXPORT.INPUT.FILE.PATH:[" +
                    INPUT_PATH + "], does not exists, not accessible or " +
                    "not a File System Directory.");
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.       

        // **********************
        // Check STATE_TAGNAME
        if ((STATE_TAGNAME == null) ||
                (STATE_TAGNAME.equalsIgnoreCase(""))) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "LDIF.EXPORT.PROCESSED.TAGNAME Property not Specified.");
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.  

        // *********************************************************
        // Ok almost ready to go.
        IDXLOG.config(CLASSNAME, METHODNAME, "Properties Verified and usable for Service Startup.");

        // *********************************************************
        // Now create a Worker Thread.
        Thread workerThread = new IRRChangeLogRestoreServiceWorkerThread(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                INPUT_PATH,
                STATE_TAGNAME,
                LDIF_FILTER_FILE);

        // **************************
        // Start the Thread.                          
        workerThread.start();

        // **************************
        // Attach a Shutdown Hook
        // Thread.
        Runtime.getRuntime().addShutdownHook(
                new IRRChangeLogRestoreServiceShutdownThread(workerThread));

    } // End of Main

} // End of Class IRRChangeLogRestoreService
