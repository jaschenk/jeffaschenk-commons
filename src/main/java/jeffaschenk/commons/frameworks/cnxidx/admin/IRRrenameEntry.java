package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to move one entry to a new entry in the Directory.
 * <br>
 * <b>Usage:</b><br>
 * IRRrenameEntry &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --sourcedn
 * 	Specify Full DN of Source Entry.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --destdn
 * 	Specify Full DN of Destination Entry.
 *      If not specified it will be formulated as the following:
 *               NamingAttribute "=" CurrentNameValue + ".YYYYMMDYHHMMSS." + ".OLD" , ExistingSourceDN.
 * --verbose
 * 	Specify Additional Logging Information.
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2001
 */

public class IRRrenameEntry implements idxCMDReturnCodes {

    private static String VERSION = "Version: 3.1 2003-09-15, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRrenameEntry: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;

    private static String ENTRY_SOURCE_DN = null;

    private static String ENTRY_DESTINATION_DN = null;

    private static boolean VERBOSE = false;

    private boolean ExitOnException = false;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRrenameEntry <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify Source IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify Source IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify Source IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--sourcedn ");
        System.err.println("\tSpecify Full DN of Source Entry.");

        System.err.println(MP + "--destdn ");
        System.err.println("\tSpecify Full DN of Destination Entry.");
        System.err.println("\tIf not specified, the new Destination DN will be formulated as:");
        System.err.println("\tNamingAttribute \"=\" CurrentNameValue + \".YYYYMMDYHHMMSS.\" + \".OLD\" , ExistingSourceDN.");


        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--verbose");
        System.err.println("\tSpecify Additional Logging Information.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass


    /**
     * IRRrenameEntry Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN  Source DN to Rename.
     * @param _ENTRY_DESTINATION_DN  Destination DN.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRrenameEntry(String _IRRHost,
                          String _IRRPrincipal,
                          String _IRRCredentials,
                          String _ENTRY_SOURCE_DN,
                          String _ENTRY_DESTINATION_DN,
                          boolean _VERBOSE,
                          boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        ENTRY_SOURCE_DN = _ENTRY_SOURCE_DN;
        ENTRY_DESTINATION_DN = _ENTRY_DESTINATION_DN;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRrenameEntry.

    /**
     * Set the correct Message Prefix for this instance of the Function Utility.
     *
     * @param _mp Name of Message Prefix.
     */
    public void setMP(String _mp) {
        if (_mp != null) {
            MP = _mp + ": ";
        }
    } // End of setMP Method.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        long starttime, endtime;

        // ****************************************
        // Run a Rename Function.
        System.out.println(MP + "Starting Rename Phase...");

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ***********************************************
        // Now determine if SourceDN is Valid.
        idxParseDN zSdn = new idxParseDN(ENTRY_SOURCE_DN);
        if (!zSdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Source DN [" +
                        ENTRY_SOURCE_DN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw new idxIRRException(MP + "Source DN [" +
                        ENTRY_SOURCE_DN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // ***********************************************
        // Replace the Source DN with the parsed DN.
        ENTRY_SOURCE_DN = zSdn.getDN();
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        // ***********************************************
        // Now determine if Destination is Valid.
        idxParseDN zDdn = new idxParseDN(ENTRY_DESTINATION_DN);
        if (!zDdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Destination DN [" +
                        ENTRY_DESTINATION_DN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw new idxIRRException(MP + "Destination DN [" +
                        ENTRY_DESTINATION_DN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // ***********************************************
        // Replace the Destination DN with the parsed DN.
        ENTRY_DESTINATION_DN = zDdn.getDN();
        System.out.println(MP + "Destination DN:[" + ENTRY_DESTINATION_DN + "]");

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Source Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "RenameEntry Source");

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

        // ************************************************
        // Disable Factories.
        try {
            IRRSource.disableDSAEFactories();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // *******************************************
        // Now Perform the Rename.
        System.out.println(MP + "Attempting Rename...");
        try {
            IRRSource.irrctx.rename(ENTRY_SOURCE_DN, ENTRY_DESTINATION_DN);
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception on Rename. " + e);
                e.printStackTrace();
                System.exit(EXIT_IRR_DELETE_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Source Directory Context.");
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

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Show Rename Timings.
        System.out.println(MP + "Rename Done, Elapsed Time: " + elt.getElapsed());

    } // End of Perform Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRrenameEntry
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

        VAR.add(new idxArgVerificationRules("sourcedn",
                true, true));

        VAR.add(new idxArgVerificationRules("destdn",
                false, true));

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

        ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        // ****************************************************
        // Check for the Destination DN, if not specified.
        // Formulated as:
        // NamingAttribute "=" CurrentNameValue + ".YYYYMMDYHHMMSS." + ".OLD" , ExistingSourceDN.
        //
        if (Zin.doesNameExist("destdn")) {
            ENTRY_DESTINATION_DN = ((String) Zin.getValue("destdn")).trim();
            System.out.println(MP + "Destination DN:[" + ENTRY_DESTINATION_DN + "]");
        } else {
            // **************************
            // Auto Formulate the Rename.
            idxParseDN zSdn = new idxParseDN(ENTRY_SOURCE_DN);
            if (zSdn.isValid()) {
                idxTimeStamp CurrentTimeStamp = new idxTimeStamp();
                String Tstamp = ((String) CurrentTimeStamp.get()).substring(0, 14);

                ENTRY_DESTINATION_DN = zSdn.getRDN() + "." +
                        Tstamp + "." +
                        "OLD" + "," +
                        zSdn.getPDN();

                System.out.println(MP + "Auto Formulated Destination DN:[" + ENTRY_DESTINATION_DN + "]");
            } else {
                ENTRY_DESTINATION_DN = null;
            }
        } // End of Else.

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRrenameEntry FUNCTION = new IRRrenameEntry(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                ENTRY_DESTINATION_DN,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRrenameEntry.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRrenameEntry
