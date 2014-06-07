package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to install a new Framework Vendor Object Tree into the
 * Directory.   This module will also provide the ability to build new
 * vendorobject trees for new vendors, types, models and Operating Systems.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRinstallVTMOS &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --customer
 * 	Specify Customer DN, dc=acme,dc=com
 * --vendor
 * 	Specify Knowledge Entry Vendor, Cisco|Juniper|Acme
 * --type
 * 	Specify Knowledge Device Type, Router|Switch|FooBar
 * --model
 * 	Specify Knowledge Device Model, 72XX|75XX|FooBar
 * --os
 * 	Specify Knowledge Device OS, 12.4(11)|1.1.(FooBar)
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */


public class IRRinstallVTMOS implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2001-09-07, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRinstallVTMOS: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;

    private static String Kcustomer = null;
    private static String Kvendor = null;
    private static String Ktype = null;
    private static String Kmodel = null;
    private static String Kos = null;

    private static boolean VERBOSE = false;

    private boolean ExitOnException = false;

    private String KentryDN = null;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "installVTMOS <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--customer ");
        System.err.println("\tSpecify Customer DN, dc=acme,dc=com");
        System.err.println(MP + "--vendor ");
        System.err.println("\tSpecify Knowledge Entry Vendor, Cisco|Juniper|Acme");
        System.err.println(MP + "--type");
        System.err.println("\tSpecify Knowledge Device Type, Router|Switch|FooBar");
        System.err.println(MP + "--model");
        System.err.println("\tSpecify Knowledge Device Model, 72XX|75XX|FooBar");
        System.err.println(MP + "--os");
        System.err.println("\tSpecify Knowledge Device OS, 12.4(11)|1.1.(FooBar)");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of class.

    /**
     * IRRinstallVTMOS Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost         Source IRR LDAP URL.
     * @param _IRRPrincipal    Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _Kcustomer       Customer of Entry.
     * @param _Kvendor         Vendor of Entry.
     * @param _Ktype           Vendor Type of Entry.
     * @param _Kmodel          Vendor Model of Entry.
     * @param _Kos             Vendor OS of Entry.
     * @param _VERBOSE         Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRinstallVTMOS(String _IRRHost,
                           String _IRRPrincipal,
                           String _IRRCredentials,
                           String _Kcustomer,
                           String _Kvendor,
                           String _Ktype,
                           String _Kmodel,
                           String _Kos,
                           boolean _VERBOSE,
                           boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        Kcustomer = _Kcustomer;
        Kvendor = _Kvendor;
        Ktype = _Ktype;
        Kmodel = _Kmodel;
        Kos = _Kos;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRinstallVTMOS.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors
     *                         during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "installVTMOS");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + e);
                System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            } else {
                throw e;
            }
        } // End of exception

        // **************************************************
        // Obtain IRR Utility Object.
        idxIRRutil util = new idxIRRutil();
        idxIRRdit dit = new idxIRRdit();
        util.setVerbose(VERBOSE);

        // *****************************************
        // First make sure the customer exists.
        //
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, Kcustomer)) {
                System.out.println(MP + "Customer DN [" + Kcustomer + "] is valid.");
            } else {
                if (ExitOnException) {
                    System.err.println(MP + "Customer DN [" + Kcustomer + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please use a valid Customer base.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Customer DN [" + Kcustomer + "] is not valid.");
                }

            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ****************************************************
        // Second make sure the Framework Vendorobjects
        // container exists.
        //
        KentryDN = "ou=vendorobjects,ou=framework," + Kcustomer;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Framework DN [" + KentryDN + "] is valid.");
            } else {
                System.err.println(MP + "Framework DN [" + KentryDN + "] is not valid.");
                System.err.println(MP + "Unable to continue, please use a valid Customer base.");
                if (ExitOnException) {
                    System.err.println(MP + "Framework DN [" + KentryDN + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please use a valid Customer base.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Framework DN [" + KentryDN + "] is not valid.");
                }

            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }

        } // End of Exception

        // ****************************************************
        // Now make sure the Vendor Container Exists
        //
        KentryDN = "ou=" + Kvendor + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Vendor [" + Kvendor + "] is valid.");
            } else {
                System.out.println(MP + "Vendor [" + Kvendor + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Vendor [" + Kvendor + "].");
                } else {
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Vendor [" + Kvendor + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Vendor [" + Kvendor + "].");
                    }
                } // End of Else.
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ****************************************************
        // Now make sure the Device Type Container Exists
        //
        KentryDN = "ou=" + Ktype + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Device Type [" + Ktype + "] is valid.");
            } else {
                System.out.println(MP + "Device Type [" + Ktype + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Device Type [" + Ktype + "].");
                } else {
                    System.out.println(MP + "Add Unsuccessful for Device Type [" + Ktype + "].");
                    System.err.println(MP + "Unable to continue.");
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Device Type [" + Ktype + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Device Type [" + Ktype + "].");
                    }
                } // End of Else.
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ****************************************************
        // Now make sure the Device Model Container Exists
        //
        KentryDN = "ou=" + Kmodel + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Device Model [" + Kmodel + "] is valid.");
            } else {
                System.out.println(MP + "Device Model [" + Kmodel + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Device Model [" + Kmodel + "].");
                    // ************************************
                    // Ok, we need to add several children
                    // Assume all is well....
                    dit.CreateOUContainersForVCObjectTree(IRRSource.irrctx,
                            KentryDN);

                } else {
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Device Model [" + Kmodel + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Device Model [" + Kmodel + "].");
                    }
                } // End of Else.
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ************************************************************
        // Now make sure the Device Operating System Container Exists
        //
        KentryDN = "ou=" + Kos + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Device OS [" + Kos + "] is valid.");
            } else {
                System.out.println(MP + "Device OS [" + Kos + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Device OS [" + Kos + "].");
                    // ************************************
                    // Ok, we need to add several children
                    // Assume all is well....
                    dit.CreateOUContainersForVCObjectTree(IRRSource.irrctx,
                            KentryDN);
                } else {
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Device OS [" + Kos + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Device OS [" + Kos + "].");
                    }
                } // End of Else.
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ************************************************************
        // Now make sure the our Final Knowledge Container Exists
        //
        KentryDN = "ou=" + "knowledge" + "," + KentryDN;
        try {
            if (!util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + " Knowledge Container was not found, but attempting add.");
                if (dit.CreateOUContainersForVCObjectTree(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Knowledge Container.");
                } else {
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Knowledge Container.");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Knowledge Container.");
                    }
                } // End of Else.
            } // End of if.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception


        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Directory Context.");
        try {
            IRRSource.close();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(e);
                System.exit(EXIT_IRR_CLOSE_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

    } // End of Perform method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRinstallVTMOS
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

        VAR.add(new idxArgVerificationRules("customer",
                true, true));

        VAR.add(new idxArgVerificationRules("vendor",
                true, true));

        VAR.add(new idxArgVerificationRules("type",
                true, true));

        VAR.add(new idxArgVerificationRules("model",
                true, true));

        VAR.add(new idxArgVerificationRules("os",
                true, true));

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

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        Kcustomer = ((String) Zin.getValue("customer")).trim();
        System.out.println(MP + "Customer:[" + Kcustomer + "]");

        Kvendor = ((String) Zin.getValue("vendor")).trim();
        System.out.println(MP + "Vendor:[" + Kvendor + "]");

        Ktype = ((String) Zin.getValue("type")).trim();
        System.out.println(MP + "Device Type:[" + Ktype + "]");

        Kmodel = ((String) Zin.getValue("model")).trim();
        System.out.println(MP + "Device Model:[" + Kmodel + "]");

        Kos = ((String) Zin.getValue("os")).trim();
        System.out.println(MP + "Device Operating System:[" + Kos + "]");

        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRinstallVTMOS FUNCTION = new IRRinstallVTMOS(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                Kcustomer,
                Kvendor,
                Ktype,
                Kmodel,
                Kos,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRinstallVTMOS.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } /// End of Main.

} // End of Class IRRinstallVTMOS
