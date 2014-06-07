package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import javax.naming.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to load a new Framework Customer DIT into the IRR
 * Directory.   This module will provide the ability to build new
 * object trees for area of DIT instantiation.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRinstallCustomer &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
 * <br>
 * <b>Required Parameters are:</b>
 * <pre>
 * --hosturl
 * 	Specify IRR(Directory) LDAP URL, ldap://hostname.acme.com
 * --irrid
 * 	Specify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa
 * --irrpw
 * 	Specify IRR(Directory) LDAP BIND Password
 * --idu
 * 	Specify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.
 * --topdomain
 * 	Specify Top Level Domain Name, example: dc=com or dc=co,dc=uk, etc.
 * --customerdomain
 * 	Specify Customer Domain Name, example: acme
 * --adminpassword
 * 	Specify the clear-text administrative password for customer Domain.
 * --readonlypassword
 * 	Specify the clear-text read only password for customer Domain.
 * </pre>
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --verbose
 * Specify additional Informational Output.
 * --overwrite
 * Specify Existing DIT Entries will be overwritten, first deleted then Added.
 * --version
 * Display Version information and exit.
 * --?
 * This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2001-2002
 */


public class IRRinstallCustomer implements idxCMDReturnCodes {

    private static String VERSION = "Version: 2.0 2002-06-19, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRinstallCustomer: ";

    private idxManageContext IRRDest = null;

    private idxStatus StatDest = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;

    private static String Mtopdomain = null;
    private static String Mcustomerdomain = null;

    private static String Mreadonlypassword = null;
    private static String Madminpassword = null;

    private static boolean OVERWRITE = false;
    private static boolean VERBOSE = false;

    private boolean ExitOnException = false;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "InstallCustomer <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--topdomain ");
        System.err.println("\tSpecify Top Domain Name, example: dc=com");
        System.err.println(MP + "--customerdomain ");
        System.err.println("\tSpecify Customer Domain Name, example: acme");
        System.err.println(MP + "--adminpassword ");
        System.err.println(MP + "\tSpecify the clear-text administrative password for customer Domain.");
        System.err.println(MP + "--readonlypassword");
        System.err.println(MP + "\tSpecify the clear-text read only password for customer Domain.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--overwrite");
        System.err.println("\tSpecify Existing Knowledge Entry will be overwritten.");

        System.err.println(MP + "--verbose");
        System.err.println("\tSpecify Additional Informational Output.");
        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of class.

    /**
     * IRRinstallCustomer Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Destination IRR LDAP URL.
     * @param _IRRPrincipal  Destination IRR Principal.
     * @param _IRRCredentials  Destination IRR Credentials.
     * @param _Mtopdomain  Customer Top Level Domain.
     * @param _Mcustomerdomain  Customer Level Domain.
     * @param _Mreadonlypassword  Customer Readonly Password.
     * @param _Madminpassword  Customer Administrative Password.
     * @param _OVERWRITE Indicate if Existing DIT is to be Overwritten.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRinstallCustomer(String _IRRHost,
                              String _IRRPrincipal,
                              String _IRRCredentials,
                              String _Mtopdomain,
                              String _Mcustomerdomain,
                              String _Mreadonlypassword,
                              String _Madminpassword,
                              boolean _OVERWRITE,
                              boolean _VERBOSE,
                              boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        Mtopdomain = _Mtopdomain;
        Mcustomerdomain = _Mcustomerdomain;
        Mreadonlypassword = _Mreadonlypassword;
        Madminpassword = _Madminpassword;
        OVERWRITE = _OVERWRITE;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRinstallCustomer.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // *********************************************
        // Create the Customer Domain DN.
        String CustomerDN = "dc=" + Mcustomerdomain + ", " + Mtopdomain;

        CompoundName cName = null;
        idxNameParser myParser = new idxNameParser();
        try {
            cName = myParser.parse(CustomerDN);
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Formulated Customer DN [" +
                        CustomerDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Formulated Customer DN [" +
                        CustomerDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of exception

        // ***********************************************
        // Now determine if CustomerDN is Valid.
        idxParseDN zDdn = new idxParseDN(CustomerDN);
        if (!zDdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Formulated Customer DN [" +
                        CustomerDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Formulated Customer DN [" +
                        CustomerDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // *************************************************************
        // Now compare both Passwords and make sure they not the same.
        Madminpassword = Madminpassword.trim();
        if (Mreadonlypassword.equals(Madminpassword)) {
            if (ExitOnException) {
                System.out.println(MP + "Admin and ReadOnly Passwords are the same, " +
                        "this is invalid and is a security violation.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Admin and ReadOnly Passwords are the same, " +
                        "this is invalid and is a security violation.");
            } // End of Inner Else.
        } // End of If.

        // ************************************************
        // Set up our Status Objects
        idxStatus StatDest = new idxStatus("InstallCustomer");
        StatDest.setOpStatus(1);

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Destination Context
        System.out.println(MP + "Attempting Source Directory Connection to Host URL:[" + IRRHost + "]");

        IRRDest = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "InstallCustomer Destination");

        // ************************************************
        // Exit on all Exceptions.
        IRRDest.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRDest.open();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + e);
                System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            } else {
                throw e;
            }
        } // End of exception

        // ***********************************************
        // Initialize Worker Classes for DIT Build.
        idxIRRutil util = new idxIRRutil();
        util.setVerbose(VERBOSE);

        idxIRRdit dit = new idxIRRdit(StatDest);
        dit.setVerbose(VERBOSE);

        // ************************************************
        // If OVERWRITE Option Specified, perform a Clean
        //
        if (OVERWRITE) {
            System.out.println(MP + "Performing Selected Delete on Domain Entries...");
            try {

                util.DeleteExistingEntries(IRRDest.irrctx,
                        CustomerDN,
                        true,
                        StatDest);

            } catch (Exception e) {
                if (ExitOnException) {
                    System.err.println(MP + "IRR Exception on InstallCustomer, during Deletion of Existing Domain Entries.\n" + e);
                    System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
                } else {
                    throw e;
                }
            } // End of exception

            // *****************************************
            // Show Statistics.
            if (StatDest.getCounter("DeletedEntries") > 0) {
                System.out.println(MP + "Number Entries Deleted on Exiting Domain:[" +
                        StatDest.getCounter("DeletedEntries") + "].");
            } else {
                System.out.println(MP + "No Entries Deleted on Existing Domain.");
            }

            if (StatDest.getCounter("NonDeletableEntries") > 0) {
                System.out.println(MP + "Number Non-Deletable Entries on Existing Domain:[" +
                        StatDest.getCounter("NonDeletableEntries") + "].");
            } else {
                System.out.println(MP + "No Non-Deletable Entries found on Existing Domain.");
            }

        } // End of if Overwrite.

        // *********************************************
        // Start Installation.
        System.out.println(MP + "Starting Customer DIT Installation...");

        // *********************************************
        // Install initial Top Level Instance.
        if (!dit.CreateDCContainer(IRRDest.irrctx, CustomerDN, true)) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Create Domain Container:["
                        + CustomerDN +
                        "], Terminating Process.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Create Domain Container:["
                        + CustomerDN +
                        "], Terminating Process.");
            } // End of Inner Else.
        } // End of exception

        // *********************************************
        // Install Instance Object Tree.
        if (!dit.CreateOUContainersForInstanceObjectTree(IRRDest.irrctx, CustomerDN)) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Create Instance Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Create Instance Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
            } // End of Inner Else.
        } // End of exception

        // *********************************************
        // Install FRAMEWORK Object Tree.
        if (!dit.CreateOUContainersForFrameworkObjectTree(IRRDest.irrctx, CustomerDN)) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Create FRAMEWORK Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Create FRAMEWORK Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
            } // End of Inner Else.
        } // End of exception

        // *********************************************
        // Install FRAMEWORK Vendor Objects Tree.
        if (!dit.CreateVendorObjectContainers(IRRDest.irrctx, CustomerDN)) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Create FRAMEWORK Vendor Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Create FRAMEWORK Vendor Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
            } // End of Inner Else.
        } // End of exception

        // *********************************************
        // Install Site Object Tree.
        if (!dit.CreateOUContainersForSiteObjectTree(IRRDest.irrctx, CustomerDN)) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Create Site Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Create Site Object Containers for:["
                        + CustomerDN +
                        "], Terminating Process.");
            } // End of Inner Else.
        } // End of exception

        // *********************************************
        // Install Operation Admin Accounts
        if (!dit.CreateOperationalAdminAccounts(IRRDest.irrctx, CustomerDN,
                Madminpassword, Mreadonlypassword)) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Create Operational Accounts for:["
                        + CustomerDN +
                        "], Terminating Process.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Create Operational Accounts for:["
                        + CustomerDN +
                        "], Terminating Process.");
            } // End of Inner Else.
        } // End of exception

        // *********************************************
        // Install Default Resource Container.
        if (!dit.CreateDefaultResourceContainerUnit(IRRDest.irrctx, CustomerDN)) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Create Default Resource Container for:["
                        + CustomerDN +
                        "], Terminating Process.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Default Resource Container for:["
                        + CustomerDN +
                        "], Terminating Process.");
            } // End of Inner Else.
        } // End of exception

        // ***************************************
        // Show Statistics.
        StatDest.show();

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Destination Directory Context.");
        try {
            IRRDest.close();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(e);
                System.exit(EXIT_IRR_CLOSE_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

    } // End of Perform Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRinstallCustomer
     */
    public static void main(String[] args) {

        long starttime, endtime;

        // ****************************************
        // Send the Greeting.
        System.out.println(MP + VERSION);

        // ****************************************
        // Parse the incoming Arguments and
        // create objects for each entity.
        //
        idxArgParser Zin = new idxArgParser();
        Zin.parse(args);

        // ***************************************
        // Do I have any unnamed Values?
        if (!Zin.IsUnNamedEmpty()) {
            System.out.println(MP + "Unknown Values Encountered, Terminating Process.");
            Zin.showUnNamed();
            Usage();
        } // End of If.

        // ***************************************
        // Was Version Info Requested?
        if (Zin.doesNameExist("version")) {
            System.exit(EXIT_VERSION);
        }

        // ***************************************
        // Was Help Info Requested?
        if ((Zin.doesNameExist("?")) ||
                (Zin.doesNameExist("usage"))) {
            Usage();
        }

        // ***************************************
        // Was Verbosity Requested?
        if (Zin.doesNameExist("verbose")) {
            VERBOSE = true;
        }

        // ***************************************
        // Show Arguments if Verbose Selected.
        if (VERBOSE) {
            Zin.show();
        }

        // ***************************************
        // Build our verification Rule Set.
        //
        // idxArgVerificationRules Parameters are:
        // String Name of argument name.
        // Boolean Required Argument Indicator.
        // Boolean StringObject Argument Indicator.
        // String Name of Value Verification Routine.
        //
        LinkedList<idxArgVerificationRules> VAR = new LinkedList<>();

        VAR.add(new idxArgVerificationRules("hosturl",
                true, true));

        VAR.add(new idxArgVerificationRules("irrid",
                false, true));

        VAR.add(new idxArgVerificationRules("irrpw",
                false, true));

        VAR.add(new idxArgVerificationRules("idu",
                false, true));

        VAR.add(new idxArgVerificationRules("topdomain",
                true, true, "verifyTopDomainName"));

        VAR.add(new idxArgVerificationRules("customerdomain",
                true, true, "verifyDCContainerName"));

        VAR.add(new idxArgVerificationRules("readonlypassword",
                true, true, "verifyPassword"));

        VAR.add(new idxArgVerificationRules("adminpassword",
                true, true, "verifyPassword"));

        VAR.add(new idxArgVerificationRules("comment",
                false, true));

        VAR.add(new idxArgVerificationRules("description",
                false, true));

        VAR.add(new idxArgVerificationRules("overwrite",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
                false, false));

        // ***************************************
        // Run the Verification Rule Set.
        // If we do not have a positive return,
        // then an invalid argument was detected,
        // so show Usage and die. 
        //
        idxArgVerifier AV = new idxArgVerifier();
        AV.setVerbose(VERBOSE);
        if (!AV.Verify(MP, Zin, VAR)) {
            Usage();
        }

        // ***************************************
        // Obtain Authentication Principal and
        // Credentials from the KeyStore or
        // the command line.
        //
        CommandLinePrincipalCredentials clPC =
                new CommandLinePrincipalCredentials(Zin);

        // **************************************************
        // Load up the Principal/Credentials.
        //
        if (clPC.wasObtained()) {
            IRRPrincipal = clPC.getPrincipal();
            System.out.println(MP + "IRR ID:[" + IRRPrincipal + "]");

            IRRCredentials = clPC.getCredentials();
            //System.out.println(MP+"IRR Password:["+IRRCredentials+"]");
        } else {
            System.out.println(MP + "Required Principal and Credentials not Specified, unable to continue.");
            Usage();
        } // End of Else.

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("overwrite")) {
            OVERWRITE = true;
        }

        // **************************************************
        // Load up the System Arguments for creating the
        // Directory Context.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        // **************************************************
        // Load up the Runtime Arguments.
        //
        Mtopdomain = (String) Zin.getValue("topdomain");
        Mtopdomain = Mtopdomain.trim();
        System.out.println(MP + "Top Domain:[" + Mtopdomain + "]");

        Mcustomerdomain = (String) Zin.getValue("customerdomain");
        Mcustomerdomain = Mcustomerdomain.trim();
        System.out.println(MP + "Customer Domain:[" + Mcustomerdomain + "]");

        // ************************************************
        // Process the Passwords.
        Mreadonlypassword = (String) Zin.getValue("readonlypassword");
        Mreadonlypassword = Mreadonlypassword.trim();

        Madminpassword = (String) Zin.getValue("adminpassword");
        Madminpassword = Madminpassword.trim();

        // ************************************************
        // Interpret OVERWRITE Option.
        if (OVERWRITE) {
            System.out.println(MP + "Will Overwrite existing Customer DIT.");
        } else {
            System.out.println(MP + "Will NOT Overwrite existing Customer DIT.");
        }

        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRinstallCustomer FUNCTION = new IRRinstallCustomer(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                Mtopdomain,
                Mcustomerdomain,
                Mreadonlypassword,
                Madminpassword,
                OVERWRITE,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRinstallCustomer.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Customer DIT Installation successfully Completed.");
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRinstallCustomer
