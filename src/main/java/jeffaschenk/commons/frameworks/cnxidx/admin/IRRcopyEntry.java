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
 * line parameters to copy one entry to a new entry in the Directory.
 * Using the --withchildren option, all children entries can be copied
 * as well.
 * <br>
 * This utility command can copy entries from one Directory to another, since
 * both a Source and Destination Directory Contexts are established.
 * <br>
 * <b>Usage:</b><br>
 * IRRcopyEntry &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --debug
 * 	Specify Additional Debugging Information.
 * --overwrite
 * 	Specify Existing Destination Entry will be overwritten.
 * --withchildren
 * 	Specify Copy of Source children entries to Destination as well as parent entry.
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

public class IRRcopyEntry implements idxCMDReturnCodes {

    private static String VERSION = "Version: 3.1 2003-09-15, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRcopyEntry: ";

    private idxStatus StatSource = null;
    private idxStatus StatDest = null;

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRcopyEntry.class.getName();

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRcopyEntry <Required Parameters> <Optional Parameters>");

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

        System.err.println(MP + "--debug");
        System.err.println("\tSpecify Additional Debugging Information.");

        System.err.println(MP + "--overwrite");
        System.err.println("\tSpecify Existing Destination Entry will be overwritten.");

        System.err.println(MP + "--withchildren");
        System.err.println("\tSpecify Copy of Source children entries to Destination as well as parent entry.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRcopyEntry Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRcopyEntry() {
    } // End of Constructor for IRRcopyEntry.


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
        StatDest.show();
    } // End of showStatistics Method.      

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @param IRRSourceCTX Source Directory Context.
     * @param ENTRY_SOURCE_DN     Source DN to Copy.
     * @param IRRDestCTX Destination Directory Context.
     * @param ENTRY_DESTINATION_DN     Destination DN.
     * @param OVERWRITE_DESTINATION_ENTRY    Indicate if Destination should be overwritten with source.
     * @param COPY_WITH_CHILDREN    Indicate if Children should be copied as well.
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
                        boolean COPY_WITH_CHILDREN,
                        boolean VERBOSE,
                        boolean DEBUG)
            throws Exception, idxIRRException {

        // ***********************************************
        // Now determine if SourceDN is Valid.
        idxParseDN zSdn = new idxParseDN(ENTRY_SOURCE_DN);
        if (!zSdn.isValid()) {
            throw new idxIRRException(MP + "Source DN [" +
                    ENTRY_SOURCE_DN +
                    "] is Invalid, unable to continue.",
                    EXIT_IRR_COPY_FAILURE);
        } // End of If.

        // ***********************************************
        // Replace the Source DN with the parsed DN.
        ENTRY_SOURCE_DN = zSdn.getDN();
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        // ***********************************************
        // Now determine if Destination is Valid.
        idxParseDN zDdn = new idxParseDN(ENTRY_DESTINATION_DN);
        if (!zDdn.isValid()) {
            throw new idxIRRException(MP + "Destination DN [" +
                    ENTRY_DESTINATION_DN +
                    "] is Invalid, unable to continue.",
                    EXIT_IRR_COPY_FAILURE);
        } // End of If.

        // ***********************************************
        // Replace the Destination DN with the parsed DN.
        ENTRY_DESTINATION_DN = zDdn.getDN();

        // ************************************************
        // Set up our Status Objects
        StatSource = new idxStatus("CopyEntry Source");
        StatDest = new idxStatus("CopyEntry Destination");

        StatSource.setOpStatus(1);
        StatDest.setOpStatus(1);

        // **************************************************
        // Obtain IRR Directory Schema from our Destination.
        idxIRRschema schema = new idxIRRschema(IRRDestCTX);

        // **************************************************
        // Obtain IRR Utility Object.
        idxIRRutil util = new idxIRRutil(schema);
        util.setVerbose(VERBOSE);
        util.setDebug(DEBUG);

        // *******************************************
        // If this is an Overwrite Operation,
        // Perform a delete of the entry and it's
        // children if required on the destination.
        //
        if (OVERWRITE_DESTINATION_ENTRY) {
            System.out.println(MP + "Performing Selected Delete on Destination Entries...");
            try {
                util.DeleteExistingEntries(IRRDestCTX,
                        ENTRY_DESTINATION_DN,
                        COPY_WITH_CHILDREN,
                        StatDest);
            } catch (Exception e) {
                throw new idxIRRException(MP + "IRR Exception on CopyEntry, during Deletion of Existing Destination Entries. " + e,
                        EXIT_IRR_DELETE_FAILURE);
            } // End of exception

            // *****************************************
            // Show Statistics.
            if (StatDest.getCounter("DeletedEntries") > 0) {
                System.out.println(MP + "Number Entries Deleted on Destination:[" +
                        StatDest.getCounter("DeletedEntries") + "].");
            } else {
                System.out.println(MP + "No Entries Deleted on Destination.");
            }

            if (StatDest.getCounter("NonDeletableEntries") > 0) {
                System.out.println(MP + "Number Non-Deletable Entries on Destination:[" +
                        StatDest.getCounter("NonDeletableEntries") + "].");
            } else {
                System.out.println(MP + "No Non-Deletable Entries found on Destination.");
            }

        } // End of if Overwrite.

        // *******************************************
        // Now Copy our Source Entry.
        System.out.println(MP + "Starting Copy...");
        try {
            util.CopyEntry(IRRSourceCTX, ENTRY_SOURCE_DN,
                    IRRDestCTX, ENTRY_DESTINATION_DN,
                    OVERWRITE_DESTINATION_ENTRY,
                    null,
                    StatSource,
                    StatDest);

        } catch (Exception e) {
            throw new idxIRRException(MP + "IRR Exception on CopyEntry," + e,
                    EXIT_IRR_COPY_FAILURE);
        } // End of exception

        // ****************************************
        // Are we to copy any Children?
        //
        if (COPY_WITH_CHILDREN) {

            idxDNLinkList myChildrenList = new idxDNLinkList();

            // ***************************
            // Copy First Level
            try {
                util.CopyChildren(IRRSourceCTX, ENTRY_SOURCE_DN,
                        IRRDestCTX, ENTRY_DESTINATION_DN,
                        OVERWRITE_DESTINATION_ENTRY,
                        myChildrenList,
                        StatSource,
                        StatDest);

            } catch (Exception e) {
                throw new idxIRRException(MP + "IRR Exception on Copy Child Entry," + e,
                        EXIT_IRR_COPY_FAILURE);
            } // End of exception

            // ***************************
            // Copy all Subsequent Levels
            while (myChildrenList.IsNotEmpty()) {
                String RDN = myChildrenList.popfirst();
                String xRDN = RDN.toLowerCase();
                String zESDN = ENTRY_SOURCE_DN.toLowerCase();

                int IxRDN = xRDN.indexOf(zESDN);
                if (IxRDN > 0) {
                    try {
                        RDN = RDN.substring(0, (IxRDN - 1));
                        util.CopyChildren(IRRSourceCTX, RDN + "," + ENTRY_SOURCE_DN,
                                IRRDestCTX, RDN + "," + ENTRY_DESTINATION_DN,
                                OVERWRITE_DESTINATION_ENTRY,
                                myChildrenList,
                                StatSource,
                                StatDest);

                    } catch (Exception e) {
                        throw new idxIRRException(MP + "IRR Exception on Copy Child Entry," + e,
                                EXIT_IRR_COPY_FAILURE);
                    } // End of exception
                } // End of If.
                else {
                    throw new idxIRRException(MP + "Unable to Obtain Source DN [" +
                            ENTRY_SOURCE_DN +
                            "] from current Child Entry DN:[" +
                            RDN +
                            "] to copy.",
                            EXIT_IRR_COPY_FAILURE);
                } // End of Else.

            } // End of While Loop.

        } // End of Copy with Children If.

    } // End of perform Method

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRcopyEntry
     */
    public static void main(String[] args) {

        long starttime, endtime;

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
        boolean COPY_WITH_CHILDREN = false;
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

        VAR.add(new idxArgVerificationRules("desthosturl",
                false, true));

        VAR.add(new idxArgVerificationRules("destidu",
                false, true));

        VAR.add(new idxArgVerificationRules("destirrid",
                false, true));

        VAR.add(new idxArgVerificationRules("destirrpw",
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
            COPY_WITH_CHILDREN = true;
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
        ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        ENTRY_DESTINATION_DN = ((String) Zin.getValue("destdn")).trim();
        ENTRY_DESTINATION_DN = ENTRY_DESTINATION_DN;
        System.out.println(MP + "Destination DN:[" + ENTRY_DESTINATION_DN + "]");

        // ************************************************
        // Show Operational Parameters
        if (OVERWRITE_DESTINATION_ENTRY) {
            System.out.println(MP + "Will Overwrite existing Destination Entry with new entry.");
        } else {
            System.out.println(MP + "Will NOT Overwrite existing Destination Entry with new entry.");
        }

        if (COPY_WITH_CHILDREN) {
            System.out.println(MP + "Will Copy Source Children along with this to new Destination.");
        } else {
            System.out.println(MP + "Will NOT copy any Source Children.");
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
                "CopyEntry Source");

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
                "CopyEntry Destination");

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
        IRRcopyEntry FUNCTION = new IRRcopyEntry();

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform(IRRSource.irrctx,
                    ENTRY_SOURCE_DN,
                    IRRDest.irrctx,
                    ENTRY_DESTINATION_DN,
                    OVERWRITE_DESTINATION_ENTRY,
                    COPY_WITH_CHILDREN,
                    VERBOSE,
                    DEBUG);

            FUNCTION.showStatistics();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRcopyEntry.\n" + e);
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

} // End of Class IRRcopyEntry
