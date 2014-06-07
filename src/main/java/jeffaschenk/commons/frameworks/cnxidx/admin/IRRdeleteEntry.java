package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to delete one entry from the Directory.
 * Using the --withchildren option, all children entries can be deleted
 * as well.
 * <br>
 * <b>Usage:</b><br>
 * IRRdeleteEntry &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * 	Specify Full DN of Source Entry to be deleted.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --verbose
 * 	Additional Logging information.
 * --withchildren
 * 	Specify Delete of Source children entries as well as parent entry.
 * --onlychildren
 * 	Specify Delete of Source children entries only, parent entry will remain intact.
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2001-2002
 */


public class IRRdeleteEntry implements idxCMDReturnCodes {

    public static String VERSION = "Version: 3.0 2003-09-04, " +
            "FRAMEWORK, Incorporated.";

    public static String MP = "IRRdeleteEntry: ";

    private idxStatus StatSource = null;

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRdeleteEntry.class.getName();

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRdeleteEntry <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--sourcedn ");
        System.err.println("\tSpecify Full DN of Source Entry to be deleted.");
        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--verbose");
        System.err.println("\tSpecify Additional Logging.");

        System.err.println(MP + "--withchildren");
        System.err.println("\tSpecify Delete of Source children entries as well as parent entry.");
        System.err.println(MP + "--onlychildren");
        System.err.println("\tSpecify Delete of Source children entries only, parent entry will remain intact.");
        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRdeleteEntry Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRdeleteEntry() {
    } // End of Constructor for IRRdeleteEntry.

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
     * Show Statistics, normally after the completion of a Copy.
     */
    public void showStatistics() {
        // ***************************************
        // Show the Statistics.
        StatSource.show();
    } // End of showStatistics Method.  

    /**
     * Show Statistics, only nuber of entries deleted.
     */
    public void showDeletedCount() {
        // ***************************************
        // Show the Deletion Count.
        System.out.println(MP + "Deleted Entries: ....... " +
                StatSource.getCounter("DeletedEntries"));
    } // End of showDeletedCount Method.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @param IRRSourceCTX Source Directory Context.
     * @param ENTRY_SOURCE_DN     Source DN to Delete.
     * @param DELETE_WITH_CHILDREN    Indicate if Children should be Deleted as well.
     * @param DELETE_ONLY_CHILDREN    Indicate if Only Children should be Deleted and not Parent or Source.
     * @param VERBOSE    Indicate Verbosity.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform(DirContext IRRSourceCTX,
                        String ENTRY_SOURCE_DN,
                        boolean DELETE_WITH_CHILDREN,
                        boolean DELETE_ONLY_CHILDREN,
                        boolean VERBOSE)
            throws Exception, idxIRRException {

        // ***********************************************
        // Now determine if SourceDN is Valid.
        idxParseDN zSdn = new idxParseDN(ENTRY_SOURCE_DN);
        if (!zSdn.isValid()) {
            throw new idxIRRException(MP + "Source DN [" +
                    ENTRY_SOURCE_DN +
                    "] is Invalid, unable to continue.",
                    EXIT_IRR_DELETE_FAILURE);
        } // End of If.

        // ***********************************************
        // Replace the Source DN with the parsed DN.
        ENTRY_SOURCE_DN = zSdn.getDN().toLowerCase();

        // ************************************************
        // Set up our Status Objects
        StatSource = new idxStatus("DeleteEntry Source");
        StatSource.setOpStatus(1);

        // **************************************************
        // Obtain IRR Utility Object.
        idxIRRutil util = new idxIRRutil();
        util.setVerbose(VERBOSE);

        // ****************************************
        // Delete Children if requested.
        // Must always delete children first, then
        // Source Entry.
        //
        if (DELETE_WITH_CHILDREN) {

            idxDNLinkList myChildrenList = new idxDNLinkList();

            // *******************************
            // Acquire all Children Entries
            try {
                util.AcquireChildren(IRRSourceCTX, ENTRY_SOURCE_DN,
                        myChildrenList);
            } catch (Exception e) {
                throw new idxIRRException(MP + "IRR Exception Acquiring Child Entries, " + e,
                        EXIT_IRR_DELETE_FAILURE);
            } // End of exception

            // ***************************
            // Delete All Children in
            // reverse order.
            while (myChildrenList.IsNotEmpty()) {
                String entryDN = myChildrenList.poplast();

                try {
                    util.DeleteEntry(IRRSourceCTX,
                            entryDN,
                            StatSource);
                } catch (Exception e) {
                    throw new idxIRRException(MP + "IRR Exception Deleting Child Entry:[" +
                            entryDN + "],\n" + e,
                            EXIT_IRR_DELETE_FAILURE);
                } // End of exception

            } // End of While Loop.

        } // End of Delete with Children If.

        // *******************************************
        // Now Delete our Source Entry, if no children
        // are to be deleted.
        if (!DELETE_ONLY_CHILDREN) {
            try {
                util.DeleteEntry(IRRSourceCTX,
                        ENTRY_SOURCE_DN, StatSource);
            } catch (Exception e) {
                throw new idxIRRException(MP + "IRR Exception Deleting Source Entry:[" +
                        ENTRY_SOURCE_DN + "],\n" + e,
                        EXIT_IRR_DELETE_FAILURE);
            } // End of exception
        } // End of NOT DELETE ONLY CHILDREN Flag Test.

    } // End of Perform Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRdeleteEntry
     */
    public static void main(String[] args) {

        idxManageContext IRRSource = null;

        String IRRHost = null;
        String IRRPrincipal = null;
        String IRRCredentials = null;
        String ENTRY_SOURCE_DN = null;

        boolean DELETE_WITH_CHILDREN = false;
        boolean DELETE_ONLY_CHILDREN = false;
        boolean VERBOSE = false;
        boolean DEBUG = false;

        // ****************************************
        // Parse the incoming Arguments and
        // create objects for each entity.
        //
        idxArgParser Zin = new idxArgParser();
        Zin.parse(args);

        // ***************************************
        // Do I have any unnamed Values?
        if (!Zin.IsUnNamedEmpty()) {
            System.out.println(MP + VERSION);
            System.out.println(MP + "Unknown Values Encountered, Terminating Process.");
            Zin.showUnNamed();
            Usage();
        } // End of If.

        // ***************************************
        // Was Version Info Requested?
        if (Zin.doesNameExist("version")) {
            System.out.println(MP + VERSION);
            System.exit(EXIT_VERSION);
        } // End of If.

        // ***************************************
        // Was Help Info Requested?
        if ((Zin.doesNameExist("?")) ||
                (Zin.doesNameExist("usage"))) {
            System.out.println(MP + VERSION);
            Usage();
        } // End of If.

        // ***************************************
        // Was Verbosity Requested?
        if (Zin.doesNameExist("verbose")) {
            VERBOSE = true;
        }

        // ***************************************
        // Was Verbosity Requested?
        if (Zin.doesNameExist("debug")) {
            DEBUG = true;
        }

        // ****************************************
        // Send the Greeting.
        if (VERBOSE) {
            System.out.println(MP + VERSION);
        }

        // ***************************************
        // Show Arguments if Verbose Selected.
        if (DEBUG) {
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

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("onlychildren",
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
        AV.setVerbose(DEBUG);
        if (!AV.Verify(MP, Zin, VAR)) {
            System.out.println(MP + VERSION);
            Usage();
        } // End of If.

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
            if (VERBOSE) {
                System.out.println(MP + "IRR ID:[" + IRRPrincipal + "]");
            }

            IRRCredentials = clPC.getCredentials();
            // if (VERBOSE)
            // { System.out.println(MP+"IRR Password:["+IRRCredentials+"]"); }
        } else {
            System.out.println(MP + VERSION);
            System.out.println(MP + "Required Principal and Credentials not Specified, unable to continue.");
            Usage();
        } // End of Else.

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("withchildren")) {
            DELETE_WITH_CHILDREN = true;
        }

        if (Zin.doesNameExist("onlychildren")) {
            DELETE_ONLY_CHILDREN = true;
            DELETE_WITH_CHILDREN = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        ENTRY_SOURCE_DN = ENTRY_SOURCE_DN.toLowerCase();
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        // ************************************************
        // Show Operational Parameters
        if ((DELETE_WITH_CHILDREN) && (!DELETE_ONLY_CHILDREN)) {
            System.out.println(MP + "Will Delete Source Children along with this Entry.");
        } else if (DELETE_ONLY_CHILDREN) {
            System.out.println(MP + "Will Delete Source Children Only, this Entry will remain intact.");
        } else {
            System.out.println(MP + "Will NOT Delete any Source Children, only this Entry will be Deleted.");
        }

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "DeleteEntry Source");

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

        // ****************************************
        // Initialize Constructor.
        IRRdeleteEntry FUNCTION = new IRRdeleteEntry();

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform(IRRSource.irrctx,
                    ENTRY_SOURCE_DN,
                    DELETE_WITH_CHILDREN,
                    DELETE_ONLY_CHILDREN,
                    VERBOSE);

            if (VERBOSE) {
                FUNCTION.showStatistics();
            } else {
                FUNCTION.showDeletedCount();
            }
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRdeleteEntry.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ***************************************
        // Close up Shop.
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(EXIT_IRR_CLOSE_FAILURE);
        } // End of exception

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        if (VERBOSE) {
            System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        }
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRdeleteEntry
