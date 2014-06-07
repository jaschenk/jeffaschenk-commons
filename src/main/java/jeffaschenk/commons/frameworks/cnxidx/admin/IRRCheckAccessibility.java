package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to check the accessibility of the Directory Instance.
 * Tests of adding, removing and obtaining a test entry can be performed.
 * <br>
 * <b>Usage:</b><br>
 * IRRCheckAccessibility &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
 * <br>
 * <b>Required Parameters are:</b>
 * <pre>
 * --hosturl
 * 	Specify IRR(Directory) LDAP URL, ldap://hostname.acme.com
 *
 * --irrid
 * 	Specify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa
 * --irrpw
 * 	Specify IRR(Directory) LDAP BIND Password
 * --idu
 * 	Specify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.
 * --container
 * 	Specify Full DN of container where Test Entry will be exercised.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --functions
 * 	Function or set of functions to be performed.
 * 	Example: create,read | create,read,delete,read
 * --rconly
 * 	Only return with a Operating System return code.
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

public class IRRCheckAccessibility implements idxCMDReturnCodes {

    public static final String VERSION = "Version: 3.0 2003-09-02, " +
            "FRAMEWORK, Incorporated.";

    public static String MP = "IRRCheckAccessibility: ";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRCheckAccessibility.class.getName();
    public static idxLogger IDXLOG = new idxLogger();

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRCheckAccessibility <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--container ");
        System.err.println("\tSpecify Full DN of Container where Test Entry will be Exercised.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--functions");
        System.err.println("\tFunction test to Perform.");
        System.err.println("\tExample: create,read | create,read,delete,read.");
        System.err.println("\tDefault: create,read,delete,read.");

        System.err.println(MP + "--rconly");
        System.err.println("\tOnly Return with an Operating System Return code.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRCheckAccessibility Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRCheckAccessibility() {
    } // End of Constructor for IRRCheckAccessibility.

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
     * @param irrctx    Destination for Accessibility Tests.
     * @param CONTAINER Source DN Container to Test.
     * @param FUNCTIONS Function tests to Perform in Sequence.
     * @param RCONLY    Indicate RC ONLY.
     * @param VERBOSE   Indicate Verbosity.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public int perform(DirContext irrctx,
                       String CONTAINER,
                       String FUNCTIONS,
                       boolean RCONLY,
                       boolean VERBOSE)
            throws Exception, idxIRRException {

        // ***********************************************
        // Now determine if Container is Valid.
        if (CONTAINER.equals("")) {
            throw new idxIRRException(MP + "DN Container [" +
                    CONTAINER +
                    "] is Invalid, unable to continue.");
        } // End of If.

        // **************************************************
        // Determine if the Container is specified as an
        // X500 Name.
        if (CONTAINER.substring(0, 1).equalsIgnoreCase("/")) {
            CONTAINER = convertX500NameToLDAPName(CONTAINER);
        }

        // **************************************************
        // Parse Container DN.
        idxParseDN zSdn = new idxParseDN(CONTAINER);
        if (!zSdn.isValid()) {
            throw new idxIRRException(MP + "DN Container [" +
                    CONTAINER +
                    "] is Invalid, unable to continue.");
        } // End of If.


        // *****************************************
        // Create a Functions Array to drive the
        // Operations to be performed.
        StringTokenizer st = new StringTokenizer(FUNCTIONS, ",", false);
        int numFunctions = st.countTokens();
        String[] ProcessFunctions = new String[numFunctions];
        for (int a = 0; a < numFunctions; a++) {
            ProcessFunctions[a] = st.nextToken();
        } // End of For Loop.

        // ****************************************
        // Note The Start Time.
        idxElapsedTime funelt = new idxElapsedTime();

        // ********************************************
        // now Obtain a new TestEntry Utility Instance.
        idxIRRXEntry TESTENTRY = new idxIRRXEntry();

        // ****************************************
        // Loop to Exercise the container.
        //
        for (int cfun = 0; cfun < ProcessFunctions.length; cfun++) {

            // ***************************************
            // Exercise the Accessibility of this
            // Specified container.
            //

            // ******************
            // Create Entry
            if ((ProcessFunctions[cfun].equalsIgnoreCase("create")) ||
                    (ProcessFunctions[cfun].equalsIgnoreCase("add"))) {
                funelt.setStart();

                TESTENTRY.create(irrctx, CONTAINER);

                funelt.setEnd();
                if (!RCONLY) {
                    System.out.println(MP + "create performed within: " + funelt.getElapsed());
                }

            } // End of function.


            // ********************
            // Check for Existence
            else if ((ProcessFunctions[cfun].equalsIgnoreCase("mustexist")) ||
                    (ProcessFunctions[cfun].equalsIgnoreCase("me"))) {
                funelt.setStart();

                if (!TESTENTRY.doesEntryExist(irrctx, CONTAINER)) {
                    System.exit(EXIT_GENERIC_FAILURE);
                }

                funelt.setEnd();
                if (!RCONLY) {
                    System.out.println(MP + "mustexist performed within: " + funelt.getElapsed());
                }
            } // End of else if.

            // ************************
            // Check for Non-Existence
            else if ((ProcessFunctions[cfun].equalsIgnoreCase("mustnotexist")) ||
                    (ProcessFunctions[cfun].equalsIgnoreCase("mne"))) {
                funelt.setStart();

                if (TESTENTRY.doesEntryExist(irrctx, CONTAINER)) {
                    System.exit(EXIT_GENERIC_FAILURE);
                }

                funelt.setEnd();
                if (!RCONLY) {
                    System.out.println(MP + "mustnotexist performed within: " + funelt.getElapsed());
                }
            } // End of else if.

            // ******************
            // Remove Entry
            else if ((ProcessFunctions[cfun].equalsIgnoreCase("remove")) ||
                    (ProcessFunctions[cfun].equalsIgnoreCase("delete"))) {
                funelt.setStart();

                TESTENTRY.remove(irrctx, CONTAINER);

                funelt.setEnd();
                if (!RCONLY) {
                    System.out.println(MP + "remove performed within: " + funelt.getElapsed());
                }

            } // End of else if.


        } // End of For Loop.

        // ****************************
        // Return
        return (EXIT_SUCCESSFUL);

    } // End of Perform Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRCheckAccessibility
     */
    public static void main(String[] args) {

        // ***********************************
        // Variables.
        idxManageContext IRRSource = null;

        String IRRHost = null;
        String IRRPrincipal = null;
        String IRRCredentials = null;
        String CONTAINER = null;
        String FUNCTIONS = null;

        boolean VERBOSE = false;
        boolean RCONLY = false;

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
        if (Zin.doesNameExist("?")) {
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

        VAR.add(new idxArgVerificationRules("container",
                true, true));

        VAR.add(new idxArgVerificationRules("rconly",
                false, false));

        VAR.add(new idxArgVerificationRules("functions",
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
            //System.out.println(MP+"IRR ID:["+IRRPrincipal+"]");

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
        if (Zin.doesNameExist("rconly")) {
            RCONLY = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        CONTAINER = ((String) Zin.getValue("container")).trim();

        // *****************************************
        // Load up Functions.
        //
        if (Zin.doesNameExist("functions")) {
            FUNCTIONS = (String) Zin.getValue("functions");
        } else {
            FUNCTIONS = "create,mustexist,delete,mustnotexist";
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
                "CheckAccessibility Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(true);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            if (!RCONLY) {
                System.err.println(MP + e);
            }
            System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
        } // End of exception

        // ************************************************
        // Disable the Factories.
        try {
            IRRSource.disableDSAEFactories();
        } catch (Exception e) {
            if (!RCONLY) {
                System.err.println(MP + e);
            }
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of exception

        // ****************************************
        // Initailize Constructor.
        IRRCheckAccessibility CA = new IRRCheckAccessibility();

        // ****************************************
        // Perform Function.
        int FRC = 0;
        try {
            FRC = CA.perform(IRRSource.irrctx, CONTAINER, FUNCTIONS, RCONLY, VERBOSE);
        } catch (Exception e) {
            if (!RCONLY) {
                System.err.println(MP + "IRR Exception Performing IRRCheckAccessibility.\n" + e);
            }
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ***************************************
        // Close up Shop.
        try {
            IRRSource.close();
        } catch (Exception e) {
            if (!RCONLY) {
                System.err.println(e);
            }
            System.exit(EXIT_IRR_CLOSE_FAILURE);
        } // End of exception

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        if (!RCONLY) {
            System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        }
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

    /**
     * convertX500NameToLDAPName
     *
     * @param _x500name X500Name
     * @return String LDAPName
     */
    private String convertX500NameToLDAPName(String _x500name) {

        // ***************************************
        // Initialize.
        String _ldapname = "";

        // ***************************************
        // Now Parse out the X500 Domains to
        // Formulate the LDAP Name.
        //
        StringTokenizer NODES = new StringTokenizer(_x500name, "/");
        while (NODES.hasMoreTokens()) {
            String node = (String) NODES.nextToken();
            if ((node == null) || (node.equals(""))) {
                continue;
            }

            // **********************************
            // Place the Node at the Begining of
            // the LDAP Name.
            if (_ldapname.equals("")) {
                _ldapname = node;
            } else {
                _ldapname = node + "," + _ldapname;
            }
        } // End of While.

        // ***************************************
        // Return the LDAP Name.
        return (_ldapname);
    } // End of convertX500NameToLDAPName.

} // End of Class IRRCheckAccessibility
