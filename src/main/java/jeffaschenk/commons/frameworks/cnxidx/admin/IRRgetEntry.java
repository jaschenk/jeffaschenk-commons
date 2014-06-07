package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to search entries in the IRR Directory.
 * <br>
 * <b>Usage:</b><br>
 * IRRgetEntry &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --sourcedn
 * 	Specify Full DN of Source Entry to be used as base.
 * --filter
 * 	Specify Search Filter.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --attributes
 *      Specify attributes to return, the default is all attributes.
 * --withchildren
 * 	Specify Display of Source children entries as well as parent entry.
 * 	scope: (Subtree)
 * --onelevel
 * 	Specify Display of Source Entry and one Level.
 * --ldif
 * 	Specify Display of Source Entry in LDIF format.
 * --nice
 * 	Specify Display of DN LDIF format in Non-Continued Form.
 *      If specified without the --ldif option, then Full cnxidaXObjectBlob will be displayed.
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

public class IRRgetEntry implements idxCMDReturnCodes {

    public static String VERSION = "Version: 1.0 2001-09-07, " +
            "FRAMEWORK, Incorporated.";

    public static String MP = "IRRgetEntry: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String SearchFilter = null;
    private static String myAttributes = null;

    private static boolean WITH_ONELEVEL = false;
    private static boolean WITH_CHILDREN = false;

    private static boolean LDIF_OUTPUT = false;

    private static boolean VERBOSE = false;

    private static boolean NICE = false;

    private static boolean QUIET = false;

    private boolean ExitOnException = false;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRgetEntry <Required Parameters> <Optional Parameters>");

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
        System.err.println("\tSpecify Full DN of Source Entry to be used as base.");

        System.err.println(MP + "--filter");
        System.err.println("\tSpecify Search Filter.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--attributes");
        System.err.println("\tSpecify attributes to return, the default is all attributes.");

        System.err.println(MP + "--withchildren");
        System.err.println("\tSpecify Display of Source children entries as well as parent entry.");

        System.err.println(MP + "--onelevel");
        System.err.println("\tSpecify Display of Source Entry and one Level.");

        System.err.println(MP + "--ldif");
        System.err.println("\tSpecify Display of Source Entry in LDIF format.");

        System.err.println(MP + "--nice");
        System.err.println("\tSpecify LDIF DN output in Non-Continued Form.");
        System.err.println("\tIf Specified without --ldif option, Full cnxidaXObjectBlob will be displayed.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRgetEntry Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN  Source DN Base to Search.
     * @param _SearchFilter  Search Filter.
     * @param _myAttributes
     * @param _WITH_ONELEVEL Indicate if Only One Level Scope.
     * @param _WITH_CHILDREN Indicate if Children or Subtree Scope.
     * @param _LDIF_OUTPUT Indicate if LDIF Output.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRgetEntry(String _IRRHost,
                       String _IRRPrincipal,
                       String _IRRCredentials,
                       String _ENTRY_SOURCE_DN,
                       String _SearchFilter,
                       String _myAttributes,
                       boolean _WITH_ONELEVEL,
                       boolean _WITH_CHILDREN,
                       boolean _LDIF_OUTPUT,
                       boolean _NICE,
                       boolean _VERBOSE,
                       boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        ENTRY_SOURCE_DN = _ENTRY_SOURCE_DN;
        SearchFilter = _SearchFilter;
        myAttributes = _myAttributes;
        WITH_ONELEVEL = _WITH_ONELEVEL;
        WITH_CHILDREN = _WITH_CHILDREN;
        LDIF_OUTPUT = _LDIF_OUTPUT;
        NICE = _NICE;
        QUIET = false;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRcopyEntry.

    /**
     * IRRgetEntry Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN  Source DN Base to Search.
     * @param _SearchFilter  Search Filter.
     * @param _myAttributes
     * @param _WITH_ONELEVEL Indicate if Only One Level Scope.
     * @param _WITH_CHILDREN Indicate if Children or Subtree Scope.
     * @param _LDIF_OUTPUT Indicate if LDIF Output.
     * @param _NICE  Indicate Nice formatted output.
     * @param _QUIET Indicate whether to reduce output produced.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRgetEntry(String _IRRHost,
                       String _IRRPrincipal,
                       String _IRRCredentials,
                       String _ENTRY_SOURCE_DN,
                       String _SearchFilter,
                       String _myAttributes,
                       boolean _WITH_ONELEVEL,
                       boolean _WITH_CHILDREN,
                       boolean _LDIF_OUTPUT,
                       boolean _NICE,
                       boolean _QUIET,
                       boolean _VERBOSE,
                       boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        ENTRY_SOURCE_DN = _ENTRY_SOURCE_DN;
        SearchFilter = _SearchFilter;
        myAttributes = _myAttributes;
        WITH_ONELEVEL = _WITH_ONELEVEL;
        WITH_CHILDREN = _WITH_CHILDREN;
        LDIF_OUTPUT = _LDIF_OUTPUT;
        NICE = _NICE;
        QUIET = _QUIET;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

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
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // ***********************************************
        // Now determine if SourceDN is Valid.
        // If ROOT, assume ok.
        if (!ENTRY_SOURCE_DN.equals("")) {
            idxParseDN zSdn = new idxParseDN(ENTRY_SOURCE_DN);
            if (!zSdn.isValid()) {
                if (ExitOnException) {
                    System.err.println(MP + "Source DN [" +
                            ENTRY_SOURCE_DN +
                            "] is Invalid, unable to continue.");
                    System.exit(EXIT_IRR_GET_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Source DN [" +
                            ENTRY_SOURCE_DN +
                            "] is Invalid, unable to continue.");
                } // End of Inner Else.
            } // End of If.
        } // End of Outer If.

        // **************************************
        // Verify the Search Filter.
        if ((SearchFilter == null) ||
                ("".equals(SearchFilter))) {
            if (ExitOnException) {
                System.err.println(MP + "Search Filter [" +
                        SearchFilter +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_GET_INVALID_FILTER_FAILURE);
            } else {
                throw new idxIRRException(MP + "Search Filter [" +
                        SearchFilter +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.

        } // End of If.

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        if (!QUIET) System.out.println(MP + "Attempting Source Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "GetEntry Source");

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

        // *****************************************
        // Show the Current Alias Derefencing State.
        if (!QUIET) {
            try {
                IRRSource.showAliasDereferencing();
            } catch (Exception e) {
                if (ExitOnException) {
                    System.err.println(MP + e);
                    System.exit(EXIT_GENERIC_FAILURE);
                } else {
                    throw e;
                }
            } // End of exception
        } // End of Check for QUIET Mode.

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

        // **************************************
        // Prepare the Search Controls
        String NameSpace = null;

        idxIRRutil util = new idxIRRutil();
        util.setVerbose(VERBOSE);

        SearchControls ctls = new SearchControls();

        // Tokenize the return attributes
        StringTokenizer st = new StringTokenizer(myAttributes, ",", false);
        int numAttrs = st.countTokens();
        String[] returnAttributes = new String[numAttrs];
        for (int attr = 0; attr < numAttrs; attr++) {
            returnAttributes[attr] = st.nextToken();
        }
        ctls.setReturningAttributes(returnAttributes);

        if (WITH_ONELEVEL) {
            ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        } else if (WITH_CHILDREN) {
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        } else {
            ctls.setSearchScope(SearchControls.OBJECT_SCOPE);
        }

        // *************************************
        // Now Perform Search.
        int Count = 0;
        try {
            NameSpace = IRRSource.irrctx.getNameInNamespace();
        } catch (Exception e) {
            System.err.println(MP + "Exception on getNamInNameSpace\n" + e);
        } // End of exception
        if (NameSpace.equals("")) {
            NameSpace = ENTRY_SOURCE_DN;
        }

        try {
            if (LDIF_OUTPUT) {
                BufferedWriter LDIFOUT = new BufferedWriter(
                        new PrintWriter(new PrintStream(System.out)));
                Count = idxIRROutput.LDIFSearchList(IRRSource.irrctx.search(ENTRY_SOURCE_DN,
                        SearchFilter, ctls), NameSpace, LDIFOUT, NICE);
                LDIFOUT.flush();
            } else {
                Count = idxIRROutput.PrintSearchList(IRRSource.irrctx.search(ENTRY_SOURCE_DN,
                        SearchFilter, ctls), NameSpace, NICE);
            } // End of Else

        } catch (NameNotFoundException e) {
            if (!QUIET) System.out.println(MP + "No Entries Found.");
        } // End of exception
        catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception on Search:\n" + e);
                System.exit(EXIT_IRR_GET_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // ***************************************
        // Close up Shop.
        if (!QUIET) System.out.println(MP + "Closing Source Directory Context.");
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

        // ***************************************
        // Show Number entries returned.
        if (Count > 0) {
            if (!QUIET) System.out.println(MP + "Entries Returned: [" + Count + "]");
        } else {
            if (ExitOnException) {
                System.err.println(MP + "No Entries Found for Base of [" +
                        ENTRY_SOURCE_DN +
                        "] and Filter [" + SearchFilter + "].");
                System.exit(EXIT_IRR_SEARCH_NO_ENTRIES_FOUND);
            } else {
                throw new idxIRRException(MP + "No Entries Found for Base of [" +
                        ENTRY_SOURCE_DN +
                        "] and Filter [" + SearchFilter + "].");
            } // End of Inner Else.
        } // End of Outer Else.

    } // End of Perform Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     */
    public static void main(String[] args) {

        long starttime, endtime;

        // ****************************************
        // Parse the incoming Arguments and
        // create objects for each entity.
        //
        idxArgParser Zin = new idxArgParser();
        Zin.parse(args);

        // ***************************************
        // Do I have any unnamed Values?
        if (!Zin.IsUnNamedEmpty()) {
            System.err.println(MP + "Unknown Values Encountered, Terminating Process.");
            Zin.showUnNamed();
            Usage();
        } // End of If.

        // ***************************************
        // Was Version Info Requested?
        if (Zin.doesNameExist("version")) {
            System.out.println(MP + VERSION);
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

        VAR.add(new idxArgVerificationRules("sourcedn",
                true, true));

        VAR.add(new idxArgVerificationRules("filter",
                true, true));

        VAR.add(new idxArgVerificationRules("attributes",
                false, true));

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("ldif",
                false, false));

        VAR.add(new idxArgVerificationRules("nice",
                false, false));

        VAR.add(new idxArgVerificationRules("onelevel",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
                false, false));

        VAR.add(new idxArgVerificationRules("quiet",
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

        if (Zin.doesNameExist("quiet")) {
            QUIET = true;
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
            if (!QUIET) System.out.println(MP + "IRR ID:[" + IRRPrincipal + "]");

            IRRCredentials = clPC.getCredentials();
            //System.out.println(MP+"IRR Password:["+IRRCredentials+"]");
        } else {
            System.err.println(MP + "Required Principal and Credentials not Specified, unable to continue.");
            Usage();
        } // End of Else.

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("withchildren")) {
            WITH_CHILDREN = true;
        }

        if (Zin.doesNameExist("onelevel")) {
            WITH_ONELEVEL = true;
        }

        if (Zin.doesNameExist("ldif")) {
            LDIF_OUTPUT = true;
        }

        if (Zin.doesNameExist("nice")) {
            NICE = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        if (!QUIET) System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        if ("root".equalsIgnoreCase(ENTRY_SOURCE_DN)) {
            ENTRY_SOURCE_DN = "";
        }

        if (!QUIET) System.out.println(MP + "Specified Source DN:[" + ENTRY_SOURCE_DN + "]");

        if (Zin.doesNameExist("filter")) {
            SearchFilter = ((String) Zin.getValue("filter")).trim();
        }

        if (Zin.doesNameExist("attributes")) {
            myAttributes = ((String) Zin.getValue("attributes")).trim();
        }

        // ************************************************
        // Show Operational Parameters
        if (WITH_ONELEVEL) {
            if (!QUIET) System.out.println(MP + "Will Display Source and OneLevel below.");
        }
        if (WITH_CHILDREN) {
            if (!QUIET) System.out.println(MP + "Will Display Source Children along with this Entry.");
        }

        if ((!WITH_CHILDREN) && (!WITH_ONELEVEL)) {
            if (!QUIET) System.out.println(MP + "Will Display Source Entry Only.");
        }

        if (SearchFilter == null) {
            SearchFilter = "(objectclass=*)";
        }
        if (!QUIET) System.out.println(MP + "Specified Search Filter: " + SearchFilter);

        if (myAttributes == null) {
            myAttributes = "";
        }
        if (!QUIET) System.out.println(MP + "Specified Return Attributes: " + myAttributes);

        if (LDIF_OUTPUT) {
            if (!QUIET) System.out.println(MP + "Output will be LDIF.");
        }

        if (NICE) {
            if (!QUIET) System.out.println(MP + "LDIF DN Output in Non-Continued Form.");
        }

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRgetEntry FUNCTION = new IRRgetEntry(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                SearchFilter,
                myAttributes,
                WITH_ONELEVEL,
                WITH_CHILDREN,
                LDIF_OUTPUT,
                NICE,
                QUIET,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRgetEntry.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        if (!QUIET) System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRgetEntry
