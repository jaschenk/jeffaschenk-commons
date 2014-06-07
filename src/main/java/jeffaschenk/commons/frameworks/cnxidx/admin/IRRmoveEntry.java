package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxManageContext;

import java.util.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to move one entry to a new entry in the Directory.
 * The source entry will be deleted after the copy of the entry to the
 * new entry is successful.
 * Using the --withchildren option, all children entries can be deleted
 * as well.
 * <br>
 * This utility command can move entries from one Directory to another, since
 * both a Source and Destination Directory Contexts are established.
 * <br>
 * <b>Usage:</b><br>
 * IRRmoveEntry &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --destdn
 * 	Specify Full DN of Destination Entry.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --desthosturl
 * 	Specify Destination IRR(Directory) LDAP URL, ldap://hostname.acme.com
 * --destirrid
 * 	Specify Destination IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa
 * --destirrpw
 * 	Specify Destination IRR(Directory) LDAP BIND Password
 * --destidu
 * 	Specify FRAMEWORK Keystore Alias to obtain Destination IRRID and IRRPW.
 * --verbose
 * 	Specify Additional Logging Information.
 * --overwrite
 * 	Specify Existing Destination Entry will be overwritten.
 * --withchildren
 * 	Specify Move of Source children entries to Destination as well as parent entry.
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

public class IRRmoveEntry implements idxCMDReturnCodes {

    private static String VERSION = "Version: 3.0 2003-09-04, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRmoveEntry: ";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRmoveEntry.class.getName();

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRmoveEntry <Required Parameters> <Optional Parameters>");

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

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--desthosturl ");
        System.err.println("\tSpecify Destination IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--destirrid ");
        System.err.println("\tSpecify Destination IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--destirrpw ");
        System.err.println("\tSpecify Destination IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--destidu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain Destination IRRID and IRRPW.");

        System.err.println(MP + "--verbose");
        System.err.println("\tSpecify Additional Logging Information.");

        System.err.println(MP + "--overwrite");
        System.err.println("\tSpecify Existing Destination Entry will be overwritten.");

        System.err.println(MP + "--withchildren");
        System.err.println("\tSpecify Move of Source children entries to Destination as well as parent entry.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Method.

    /**
     * IRRmoveEntry Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRmoveEntry() {
    } // End of Constructor for IRRmoveEntry.

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
     * @param IRRSourceCTX Source Directory Context.
     * @param ENTRY_SOURCE_DN     Source DN to Copy.
     * @param IRRDestCTX Destination Directory Context.
     * @param ENTRY_DESTINATION_DN     Destination DN.
     * @param OVERWRITE_DESTINATION_ENTRY    Indicate if Destination should be overwritten with source.
     * @param MOVE_WITH_CHILDREN    Indicate if Children should be copied as well.
     * @param VERBOSE    Indicate Verbosity.
     * @param DEBUG    Indicate Debugging.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform(DirContext IRRSourceCTX,
                        String ENTRY_SOURCE_DN,
                        DirContext IRRDestCTX,
                        String ENTRY_DESTINATION_DN,
                        boolean OVERWRITE_DESTINATION_ENTRY,
                        boolean MOVE_WITH_CHILDREN,
                        boolean VERBOSE,
                        boolean DEBUG)
            throws Exception, idxIRRException {

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initialize Constructor.
        IRRcopyEntry copyFUNCTION = new IRRcopyEntry();

        // ****************************************
        // Perform Function.
        copyFUNCTION.perform(IRRSourceCTX,
                ENTRY_SOURCE_DN,
                IRRDestCTX,
                ENTRY_DESTINATION_DN,
                OVERWRITE_DESTINATION_ENTRY,
                MOVE_WITH_CHILDREN,
                VERBOSE,
                DEBUG);

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Show Copy Phase Timings.
        if (VERBOSE) {
            System.out.println(MP + "Copy Phase Done, Elapsed Time: " + elt.getElapsed());
            copyFUNCTION.showStatistics();
        } // End of Verbosity.

        // ****************************************
        // Note The Start Time.
        elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRdeleteEntry deleteFUNCTION = new IRRdeleteEntry();

        // ****************************************
        // Perform Function.
        deleteFUNCTION.perform(IRRSourceCTX,
                ENTRY_SOURCE_DN,
                MOVE_WITH_CHILDREN,
                false,
                VERBOSE);

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Show Copy Phase Timings.
        if (VERBOSE) {
            System.out.println(MP + "Deletion Phase Done, Elapsed Time: " + elt.getElapsed());
        } // End of Verbosity.

    } // End of Perform Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRmoveEntry
     * @see IRRcopyEntry
     * @see IRRdeleteEntry
     */
    public static void main(String[] args) {

        idxManageContext IRRSource = null;
        idxManageContext IRRDest = null;

        String IRRHost = null;
        String IRRPrincipal = null;
        String IRRCredentials = null;

        String ENTRY_SOURCE_DN = null;

        String IRRDestHost = null;
        String IRRDestPrincipal = null;
        String IRRDestCredentials = null;

        String ENTRY_DESTINATION_DN = null;

        boolean OVERWRITE_DESTINATION_ENTRY = false;
        boolean MOVE_WITH_CHILDREN = false;
        boolean VERBOSE = false;
        boolean DEBUG = false;

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
        // Was Debug Requested?
        if (Zin.doesNameExist("debug")) {
            DEBUG = true;
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

        VAR.add(new idxArgVerificationRules("desthosturl",
                false, true));

        VAR.add(new idxArgVerificationRules("destirrid",
                false, true));

        VAR.add(new idxArgVerificationRules("destirrpw",
                false, true));

        VAR.add(new idxArgVerificationRules("destidu",
                false, true));

        VAR.add(new idxArgVerificationRules("sourcedn",
                true, true));

        VAR.add(new idxArgVerificationRules("destdn",
                true, true));

        VAR.add(new idxArgVerificationRules("overwrite",
                false, false));

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
                false, false));

        VAR.add(new idxArgVerificationRules("debug",
                false, false));

        // ***************************************
        // Run the Verification Rule Set.
        // If we do not have a positive return,
        // then an invalid argument was detected,
        // so show Usage and die. 
        //
        idxArgVerifier AV = new idxArgVerifier();
        AV.setVerbose(DEBUG);
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
        // Load up the Destination Principal/Credentials.
        //
        if (clPC.wasDestObtained()) {
            IRRDestPrincipal = clPC.getDestPrincipal();
            IRRDestCredentials = clPC.getDestCredentials();
        } else {
            IRRDestPrincipal = IRRPrincipal;
            IRRDestCredentials = IRRCredentials;
        } // End of Else.
        System.out.println(MP + "IRR Destination ID:[" + IRRDestPrincipal + "]");
        //System.out.println(MP+"IRR Destination Password:["+IRRDestCredentials+"]");

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("withchildren")) {
            MOVE_WITH_CHILDREN = true;
        }

        if (Zin.doesNameExist("overwrite")) {
            OVERWRITE_DESTINATION_ENTRY = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        if (!Zin.doesNameExist("desthosturl")) {
            IRRDestHost = IRRHost;
        } else {
            IRRDestHost = (String) Zin.getValue("desthosturl");
        }
        System.out.println(MP + "IRR Destination Host URL:[" + IRRDestHost + "]");

        ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        ENTRY_SOURCE_DN = ENTRY_SOURCE_DN.toLowerCase();
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        ENTRY_DESTINATION_DN = ((String) Zin.getValue("destdn")).trim();
        ENTRY_DESTINATION_DN = ENTRY_DESTINATION_DN.toLowerCase();
        System.out.println(MP + "Destination DN:[" + ENTRY_DESTINATION_DN + "]");

        // ************************************************
        // Show Operational Parameters
        if (OVERWRITE_DESTINATION_ENTRY) {
            System.out.println(MP + "Will Overwrite existing Destination Entry with new entry.");
        } else {
            System.out.println(MP + "Will NOT Overwrite existing Destination Entry with new entry.");
        }

        if (MOVE_WITH_CHILDREN) {
            System.out.println(MP + "Will Move Source Children along with this to new Destination.");
        } else {
            System.out.println(MP + "Will NOT move any Source Children, only this Entry will be Moved.");
        }

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Source Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "MoveEntry Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(true);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
        } // End of exception

        // ************************************************
        // Disable Factories.
        try {
            IRRSource.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of exception

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Destination Context
        System.out.println(MP + "Attempting Destination Directory Connection to Host URL:[" + IRRDestHost + "]");

        IRRDest = new idxManageContext(IRRDestHost,
                IRRDestPrincipal,
                IRRDestCredentials,
                "MoveEntry Destination");

        // ************************************************
        // Exit on all Exceptions.
        IRRDest.setExitOnException(true);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRDest.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
        } // End of exception

        // ************************************************
        // Disable Factories.
        try {
            IRRDest.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of exception  

        // ****************************************
        // Initailize Constructor.
        IRRmoveEntry FUNCTION = new IRRmoveEntry();

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform(IRRSource.irrctx,
                    ENTRY_SOURCE_DN,
                    IRRDest.irrctx,
                    ENTRY_DESTINATION_DN,
                    OVERWRITE_DESTINATION_ENTRY,
                    MOVE_WITH_CHILDREN,
                    VERBOSE,
                    DEBUG);
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRmoveEntry.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Source Directory Context.");
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(EXIT_IRR_CLOSE_FAILURE);
        } // End of exception

        System.out.println(MP + "Closing Destination Directory Context.");
        try {
            IRRDest.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(EXIT_IRR_CLOSE_FAILURE);
        } // End of exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRmoveEntry
