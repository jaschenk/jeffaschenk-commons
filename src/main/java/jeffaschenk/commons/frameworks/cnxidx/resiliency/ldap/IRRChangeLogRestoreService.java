package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.frameworks.cnxidx.utility.security.FrameworkDirectoryUser;

import java.util.*;
import java.io.*;

/**
 * Java Service Driver for the IRRLogRestoreDriver.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */


public class IRRChangeLogRestoreService implements idxCMDReturnCodes {

    // *******************************
    // Version Information.
    public static final String VERSION = "4.4 2005-12-06";


    // ***********************************
    // Globals Default Property File Name
    public static final String FRAMEWORK_CONFIG_PROPERTIES
            = "framework.config";
    public static final String DEFAULT_PROPERTIES_FILENAME
            = "service.properties";

    // ********************************
    // Global Property Names
    public static final String IRR_HOST_URL_PNAME
            = "IRR.HOST.URL";
    public static final String IRR_PRINCIPAL_PNAME
            = "IRR.PRINCIPAL";
    public static final String IRR_CREDENTIALS_PNAME
            = "IRR.CREDENTIALS";
    public static final String IRR_IDU_PNAME
            = "IRR.IDU";

    public static final String INPUT_PATH_PNAME
            = "RESILIENCY.INPUT.FILE.PATH";

    public static final String PUBLISHER_EXCLUDE_PATH_PNAME
            = "RESILIENCY.PUBLISH.EXCLUDE.FILTER.FILE.PATH";
    public static final String RESTORE_EXCLUDE_PATH_PNAME
            = "RESILIENCY.RESTORE.EXCLUDE.FILTER.FILE.PATH";

    public static final String OPERTIONAL_MODE_PNAME
            = "RESILIENCY.OPERATIONAL.MODE";
    public static final String WEBADMIN_PORT_PNAME
            = "RESILIENCY.WEBADMIN.PORT";
    public static final String WEBADMIN_ALLOW_LIST_PNAME
            = "RESILIENCY.WEBADMIN.ALLOW.LIST";

    public static final String MULTICAST_ADDRESS_PNAME
            = "RESILIENCY.MULTICAST.ADDRESS";
    public static final String MULTICAST_PORT_PNAME
            = "RESILIENCY.MULTICAST.PORT";
    public static final String GROUPNAME_PNAME
            = "RESILIENCY.GROUP.NAME";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreService.class.getName();

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     *
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

        String PUBLISH_EXCLUDE_DN_FILTER_FILE = null;
        String RESTORE_EXCLUDE_DN_FILTER_FILE = null;

        int WEBADMIN_PORT = 0;
        String WEBADMIN_ALLOW_LIST = null;

        String OPERATIONAL_MODE = null;

        String GROUPNAME = null;
        String MULTICAST_ADDRESS = null;
        String MULTICAST_PORT = null;

        String METHODNAME = "main";

        // ****************************************
        // Send the Greeting.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.SERVICE_VERSION, new String[]{VERSION});

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.SERVICE_CONFIGURATION);

        // ****************************************
        // Obtain the RunTime Properties from
        // a local Property file.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.SERVICE_CONFIGURATION);

        String PFileName = System.getProperty(FRAMEWORK_CONFIG_PROPERTIES,
                DEFAULT_PROPERTIES_FILENAME);
        File PF = new File(PFileName);
        try {
            if (!PF.exists()) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.SERVICE_UNABLE_TO_READ_PROPERTIES,
                        new String[]{PFileName.toString()});
                System.exit(EXIT_GENERIC_FAILURE);
            } // End of If.

            // *************************************
            // Try to load the Properties.
            RunConfig.load(new FileInputStream(PF));
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.SERVICE_PROPERTIES_OBTAINED,
                    new String[]{PF.getAbsoluteFile().toString()});
        } catch (Exception e) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.SERVICE_UNABLE_TO_OBTAIN_PROPERTIES,
                    new String[]{PFileName.toString(), e.getMessage().toString()});
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // *****************************************
        // Obtain the Properties and formulate into
        // our variables.
        IRRHost = RunConfig.getProperty(IRR_HOST_URL_PNAME);
        IRRPrincipal = RunConfig.getProperty(IRR_PRINCIPAL_PNAME);
        IRRCredentials = RunConfig.getProperty(IRR_CREDENTIALS_PNAME);
        IRRIDU = RunConfig.getProperty(IRR_IDU_PNAME);
        //
        INPUT_PATH = RunConfig.getProperty(INPUT_PATH_PNAME);
        //
        PUBLISH_EXCLUDE_DN_FILTER_FILE = RunConfig.getProperty(PUBLISHER_EXCLUDE_PATH_PNAME);
        RESTORE_EXCLUDE_DN_FILTER_FILE = RunConfig.getProperty(RESTORE_EXCLUDE_PATH_PNAME);
        OPERATIONAL_MODE = RunConfig.getProperty(OPERTIONAL_MODE_PNAME);
        //
        MULTICAST_ADDRESS = RunConfig.getProperty(MULTICAST_ADDRESS_PNAME);
        MULTICAST_PORT = RunConfig.getProperty(MULTICAST_PORT_PNAME);
        GROUPNAME = RunConfig.getProperty(GROUPNAME_PNAME);
        //
        try {
            WEBADMIN_PORT = Integer.parseInt(
                    RunConfig.getProperty(WEBADMIN_PORT_PNAME, "0"));
        } catch (NumberFormatException nfe) {
            WEBADMIN_PORT = 0;
        } // End of Exception processing.
        //
        WEBADMIN_ALLOW_LIST = RunConfig.getProperty(WEBADMIN_ALLOW_LIST_PNAME);
        // *********************************************************************

        // *********************************************************************
        // Verify all the properties are present and available to continue.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.SERVICE_VERIFYING_PROPERTIES);

        // ******************
        // Check IRRHost
        if ((IRRHost == null) ||
                (IRRHost.equalsIgnoreCase(""))) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.SERVICE_IRR_HOST_URL_NOT_SPECIFIED);
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
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                        ErrorConstants.SERVICE_UNABLE_TO_OBTAIN_AUTHENTICATION_DATA,
                        new String[]{e.getMessage()});
                System.exit(EXIT_GENERIC_FAILURE);
            } // End of Exception.
        } // End of If.

        // ******************
        // Check IRRPrincipal
        if ((IRRPrincipal == null) ||
                (IRRPrincipal.equalsIgnoreCase(""))) {

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.SERVICE_UNABLE_TO_OBTAIN_IRRPRINCIPAL_PROPERTY);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.

        // ******************
        // Check IRRCredentials
        if ((IRRCredentials == null) ||
                (IRRCredentials.equalsIgnoreCase(""))) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.SERVICE_UNABLE_TO_OBTAIN_IRRCREDENTIALS_PROPERTY);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.

        // ********************
        // Check INPUT_PATH
        if ((INPUT_PATH == null) ||
                (INPUT_PATH.equalsIgnoreCase(""))) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.SERVICE_PROPERTY_NOT_SPECIFIED,
                    new String[]{INPUT_PATH_PNAME});
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
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.SERVICE_PROPERTY_FILENAME_NOT_ACCESSIBLE,
                    new String[]{INPUT_PATH_PNAME, INPUT_PATH});
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of If.

        // *********************************************************
        // Now determine which Constructor to use.  If any MCAST or
        // our groupname specified, then use the correct constructor.
        Thread controlThread = null;
        if ((MULTICAST_ADDRESS != null) ||
                (MULTICAST_PORT != null) ||
                (GROUPNAME != null)) {
            // *********************************************************
            // Now create a Control Thread to Boot up the Service.
            controlThread = new IRRChangeLogRestoreServiceControlThread(
                    IRRHost,
                    IRRPrincipal,
                    IRRCredentials,
                    INPUT_PATH,
                    PUBLISH_EXCLUDE_DN_FILTER_FILE,
                    RESTORE_EXCLUDE_DN_FILTER_FILE,
                    WEBADMIN_PORT,
                    OPERATIONAL_MODE,
                    GROUPNAME,
                    MULTICAST_ADDRESS,
                    MULTICAST_PORT,
                    WEBADMIN_ALLOW_LIST);
        } else {
            // *********************************************************
            // Now create a Control Thread to Boot up the Service.
            controlThread = new IRRChangeLogRestoreServiceControlThread(
                    IRRHost,
                    IRRPrincipal,
                    IRRCredentials,
                    INPUT_PATH,
                    PUBLISH_EXCLUDE_DN_FILTER_FILE,
                    RESTORE_EXCLUDE_DN_FILTER_FILE,
                    WEBADMIN_PORT,
                    OPERATIONAL_MODE);
        } // End of Else .

        // **************************
        // Start the Thread.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.SERVICE_BOOTING);

        controlThread.start();

        // **************************
        // Attach a Shutdown Hook
        // Thread.
        Runtime.getRuntime().addShutdownHook(
                new IRRChangeLogRestoreServiceShutdownThread(controlThread));

    } // End of Main

} // End of Class IRRChangeLogRestoreService
