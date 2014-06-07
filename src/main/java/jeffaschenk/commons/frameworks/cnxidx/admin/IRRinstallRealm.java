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
 * line parameters to install a new Framework Customer Realm DIT into the IRR
 * Directory.   This module will provide the ability to build new
 * object trees for area of DIT instantiation.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRinstallRealm &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --parentdn
 * 	Specify Parent Domain Name, example: dc=com or dc=co,dc=uk, etc.
 * --customerrealm
 * 	Specify Customer Realm Name, example: westcoast
 * </pre>
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --realmna
 * Specify Customer Realm Naming Attribute, example: ou | dc
 * Default is ou for Organizational Unit.
 * --localrealm
 * Specify a local Realm will be created, will not create Subordinate System Containers.
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


public class IRRinstallRealm implements idxCMDReturnCodes {

    private static String VERSION = "Version: 2.0 2002-07-01, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRinstallRealm: ";

    private idxManageContext IRRDest = null;

    private idxStatus StatDest = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;

    private static String MparentDN = null;
    private static String Mcustomerrealm = null;
    private static String McustomerrealmNamingAttribute = null;

    private static boolean LOCALREALM = false;
    private static boolean OVERWRITE = false;
    private static boolean VERBOSE = false;

    private boolean ExitOnException = false;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "InstallRealm <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--parentdn ");
        System.err.println("\tSpecify Parent DN, example: dc=com | ou=east coast,dc=acme,dc=com");

        System.err.println(MP + "--customerrealm ");
        System.err.println("\tSpecify Customer realm Name, example: westcoast");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--realmna ");
        System.err.println("\tSpecify Customer local realm Naming Attribute, example: rcu");
        System.err.println("\tSpecify Customer remote realm Naming Attribute, example: ou | dc");

        System.err.println(MP + "--localrealm");
        System.err.println("\tSpecify to create a Local Realm, will not create Subordinate System Containers.");

        System.err.println(MP + "--overwrite");
        System.err.println("\tSpecify Existing Entry will be overwritten.");

        System.err.println(MP + "--verbose");
        System.err.println("\tSpecify Additional Informational Output.");
        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of class.

    /**
     * IRRinstallRealm Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Destination IRR LDAP URL.
     * @param _IRRPrincipal  Destination IRR Principal.
     * @param _IRRCredentials  Destination IRR Credentials.
     * @param _MparentDN  Parent Level DN.
     * @param _Mcustomerrealm  Customer Realm Level Domain.
     * @param _McustomerrealmNamingAttribute  Customer Realm Naming Attribute.
     * @param _LOCALREALM
     * @param _OVERWRITE Indicate if Existing DIT is to be Overwritten.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRinstallRealm(String _IRRHost,
                           String _IRRPrincipal,
                           String _IRRCredentials,
                           String _MparentDN,
                           String _Mcustomerrealm,
                           String _McustomerrealmNamingAttribute,
                           boolean _LOCALREALM,
                           boolean _OVERWRITE,
                           boolean _VERBOSE,
                           boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        MparentDN = _MparentDN;
        Mcustomerrealm = _Mcustomerrealm;
        McustomerrealmNamingAttribute = _McustomerrealmNamingAttribute;
        LOCALREALM = _LOCALREALM;
        OVERWRITE = _OVERWRITE;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRinstallRealm.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {


        // *********************************************
        // Verify the Parent Customer Domain DN.
        CompoundName cName = null;
        idxNameParser myParser = new idxNameParser();
        try {
            cName = myParser.parse(MparentDN);
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Realm Parent Customer DN [" +
                        MparentDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Realm Parent Customer DN [" +
                        MparentDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of exception

        // ***********************************************
        // Now determine if SourceDN is Valid.
        idxParseDN zDdn = new idxParseDN(MparentDN);
        if (!zDdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Realm Parent DN [" +
                        MparentDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Realm Parent DN [" +
                        MparentDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // *********************************************
        // Create the Customer Realm DN.
        String CustomerRealmDN = null;
        McustomerrealmNamingAttribute = McustomerrealmNamingAttribute.trim();

        // *********************************************
        // Check Naming Attribute for Remote Realm.
        if (!LOCALREALM) {

            if ((!McustomerrealmNamingAttribute.equalsIgnoreCase("ou")) &&
                    (!McustomerrealmNamingAttribute.equalsIgnoreCase("dc"))) {
                if (ExitOnException) {
                    System.err.println(MP + "Customer Remote Realm Naming Attribute [" +
                            McustomerrealmNamingAttribute +
                            "] is Invalid, unable to continue.");
                    System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Customer Remote Realm Naming Attribute [" +
                            McustomerrealmNamingAttribute +
                            "] is Invalid, unable to continue.");
                } // End of Inner Else.

            } // End of If for NamingAttribute.
        } // End of Remote Realm naming Attribute Verification.

        // *********************************************
        // Check Naming Attribute for Local Realm.
        else {

            if (!McustomerrealmNamingAttribute.equalsIgnoreCase("rcu")) {
                if (ExitOnException) {
                    System.err.println(MP + "Customer Local Realm Naming Attribute [" +
                            McustomerrealmNamingAttribute +
                            "] is Invalid, unable to continue.");
                    System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Customer Local Realm Naming Attribute [" +
                            McustomerrealmNamingAttribute +
                            "] is Invalid, unable to continue.");
                } // End of Inner Else.

            } // End of If for NamingAttribute.
        } // End of Local Realm naming Attribute Verification.

        // ************************************************
        // Formulate the Realm DN.
        CustomerRealmDN = McustomerrealmNamingAttribute +
                "=" + Mcustomerrealm +
                ", " + MparentDN;

        cName = null;
        try {
            cName = myParser.parse(CustomerRealmDN);
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Formulated Customer Realm DN [" +
                        CustomerRealmDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Formulated Customer Realm DN [" +
                        CustomerRealmDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of exception

        // ***********************************************
        // Now determine if SourceDN is Valid.
        zDdn = new idxParseDN(CustomerRealmDN);
        if (!zDdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Formulated Customer Realm DN [" +
                        CustomerRealmDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Formulated Customer Realm DN [" +
                        CustomerRealmDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // ************************************************
        // Set up our Status Objects
        idxStatus StatDest = new idxStatus("InstallRealm");
        StatDest.setOpStatus(1);

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Destination Context
        System.out.println(MP + "Attempting Source Directory Connection to Host URL:[" + IRRHost + "]");

        IRRDest = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "InstallRealm Destination");

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
            System.out.println(MP + "Performing Selected Delete on Realm Entries...");
            try {

                util.DeleteExistingEntries(IRRDest.irrctx,
                        CustomerRealmDN,
                        true,
                        StatDest);

            } catch (Exception e) {
                if (ExitOnException) {
                    System.err.println(MP + "IRR Exception on InstallRealm, during Deletion of Existing Domain Entries.\n" + e);
                    System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
                } else {
                    throw e;
                }
            } // End of exception

            // *****************************************
            // Show Statistics.
            if (StatDest.getCounter("DeletedEntries") > 0) {
                System.out.println(MP + "Number Entries Deleted on Exiting Realm:[" +
                        StatDest.getCounter("DeletedEntries") + "].");
            } else {
                System.out.println(MP + "No Entries Deleted on Existing Realm.");
            }

            if (StatDest.getCounter("NonDeletableEntries") > 0) {
                System.out.println(MP + "Number Non-Deletable Entries on Existing Realm:[" +
                        StatDest.getCounter("NonDeletableEntries") + "].");
            } else {
                System.out.println(MP + "No Non-Deletable Entries found on Existing Realm.");
            }

        } // End of if Overwrite.

        // *********************************************
        // Create the Local Realm if specified...
        if (LOCALREALM) {
            // *********************************************
            // Start Local Realm Installation.
            System.out.println(MP + "Starting Customer Local Realm DIT Installation...");

            // *********************************************
            // Install initial Top Level Realm Instance.
            if (!dit.CreateResourceContainerUnit(IRRDest.irrctx, CustomerRealmDN)) {
                if (ExitOnException) {
                    System.err.println(MP + "Unable to Create Local Realm Container:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                    System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Unable to Create Local Realm Container:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                } // End of Inner Else.
            } // End of exception


        } // End of LOCALREALM.

        // *********************************************
        // Create the Remote Realm...
        else {

            // *********************************************
            // Start Installation.
            System.out.println(MP + "Starting Customer Remote/Subordinate Realm DIT Installation...");

            // *********************************************
            // Install initial Top Level Realm Instance.
            boolean rtrc = false;
            if (McustomerrealmNamingAttribute.equals("ou")) {
                rtrc = dit.CreateOUContainer(IRRDest.irrctx, CustomerRealmDN, true, false);
            } else {
                rtrc = dit.CreateDCContainer(IRRDest.irrctx, CustomerRealmDN, true);
            }
            if (!rtrc) {
                if (ExitOnException) {
                    System.err.println(MP + "Unable to Create Realm Container:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                    System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Unable to Create Realm Container:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                } // End of Inner Else.
            } // End of exception

            // **************************************************
            // Install Instance Object Tree for Realm.
            // This was placed back in for V2.0 for Distributed
            // capabilities.
            System.out.println(MP + "Starting Creation of Remote Realm System Subordinate Containers.");

            // ***********************************************
            // Create System Subordinate Containers for Realm
            if (!dit.CreateOUContainersForInstanceObjectTree(IRRDest.irrctx, CustomerRealmDN)) {
                if (ExitOnException) {
                    System.err.println(MP + "Unable to Create System Subordinate Containers for:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                    System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Unable to Create System Subordinate Containers for:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                } // End of Inner Else.
            } // End of exception

            // ***********************************************
            // Create a Default Resource Container Descriptor.
            if (!dit.CreateDefaultResourceContainerUnit(IRRDest.irrctx, CustomerRealmDN)) {
                if (ExitOnException) {
                    System.err.println(MP + "Unable to Create Default Resource Container for:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                    System.exit(EXIT_IRR_INSTALL_CUSTOMER_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Unable to Default Resource Container for:["
                            + CustomerRealmDN +
                            "], Terminating Process.");
                } // End of Inner Else.
            } // End of exception

        } // End of Remote REALM.

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
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRinstallRealm
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

        VAR.add(new idxArgVerificationRules("parentdn",
                true, true));

        VAR.add(new idxArgVerificationRules("realmna",
                false, true));

        VAR.add(new idxArgVerificationRules("customerrealm",
                true, true, "verifyOUContainerName"));

        VAR.add(new idxArgVerificationRules("comment",
                false, true));

        VAR.add(new idxArgVerificationRules("description",
                false, true));

        VAR.add(new idxArgVerificationRules("overwrite",
                false, false));

        VAR.add(new idxArgVerificationRules("localrealm",
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

        if (Zin.doesNameExist("localrealm")) {
            LOCALREALM = true;
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
        MparentDN = (String) Zin.getValue("parentdn");
        MparentDN = MparentDN.trim();
        System.out.println(MP + "Realm Parent DN:[" + MparentDN + "]");

        // ************************************************
        // Show Realm.
        Mcustomerrealm = (String) Zin.getValue("customerrealm");
        Mcustomerrealm = Mcustomerrealm.trim();
        System.out.println(MP + "Customer Realm:[" + Mcustomerrealm + "]");

        // ***************************************************
        // Show Optional Realm Naming Attribute, if Specified.
        // Default to OrganizationalUnit (OU).
        if (Zin.doesNameExist("realmna")) {
            McustomerrealmNamingAttribute = (String) Zin.getValue("realmna");
            McustomerrealmNamingAttribute = McustomerrealmNamingAttribute.trim();

            // **************************************
            // If local Realm Always Override
            // to proper naming attribute.
            //
            if (LOCALREALM) {
                McustomerrealmNamingAttribute = "rcu";
            }
        } else if (!LOCALREALM) {
            McustomerrealmNamingAttribute = "ou";
        } else {
            McustomerrealmNamingAttribute = "rcu";
        }
        System.out.println(MP + "Customer Realm Naming Attribute:[" + McustomerrealmNamingAttribute + "]");

        // ************************************************
        // Interpret OVERWRITE Option.
        if (OVERWRITE) {
            System.out.println(MP + "Will Overwrite existing Customer Realm DIT.");
        } else {
            System.out.println(MP + "Will NOT Overwrite existing Customer Realm DIT.");
        }

        // ************************************************
        // Interpret LOCALREALM Option.
        if (LOCALREALM) {
            System.out.println(MP + "Will Create a Local Customer Realm as an RCU on an existing Directory Instance.");
        } else {
            System.out.println(MP + "Will Create a Full Remote Realm on a new Directory Instance.");
        }

        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRinstallRealm FUNCTION = new IRRinstallRealm(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                MparentDN,
                Mcustomerrealm,
                McustomerrealmNamingAttribute,
                LOCALREALM,
                OVERWRITE,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRinstallRealm.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Customer Realm Installation successfully Completed.");
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRinstallRealm
