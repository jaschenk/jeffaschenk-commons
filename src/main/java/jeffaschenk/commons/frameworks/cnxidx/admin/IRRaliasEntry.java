package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to create a Directory Alias for the
 * current source DN.
 * <br>
 * <b>Usage:</b><br>
 * IRRaliasEntry &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --aliasdn
 * 	Specify Full DN of Alias Entry.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
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
 * Developed 2002
 */

public class IRRaliasEntry implements idxCMDReturnCodes {

    private static String VERSION = "Version: 3.1 2003-09-15, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRaliasEntry: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String ENTRY_ALIAS_DN = null;

    private static boolean VERBOSE = false;

    private boolean ExitOnException = false;

    private static String[] NO_Attributes = {"1.1"};

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRaliasEntry <Required Parameters> <Optional Parameters>");

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
        System.err.println(MP + "--aliasdn ");
        System.err.println("\tSpecify Full DN of Alias Entry.");

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
     * IRRaliasEntry Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN  Source DN.
     * @param _ENTRY_ALIAS_DN  Alias DN.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRaliasEntry(String _IRRHost,
                         String _IRRPrincipal,
                         String _IRRCredentials,
                         String _ENTRY_SOURCE_DN,
                         String _ENTRY_ALIAS_DN,
                         boolean _VERBOSE,
                         boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        ENTRY_SOURCE_DN = _ENTRY_SOURCE_DN;
        ENTRY_ALIAS_DN = _ENTRY_ALIAS_DN;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRaliasEntry.

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
        idxParseDN zSdn = new idxParseDN(ENTRY_SOURCE_DN);
        if (!zSdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Source DN [" +
                        ENTRY_SOURCE_DN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_COPY_FAILURE);
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
        idxParseDN zAdn = new idxParseDN(ENTRY_ALIAS_DN);
        if (!zAdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Alias DN [" +
                        ENTRY_ALIAS_DN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_COPY_FAILURE);
            } else {
                throw new idxIRRException(MP + "Alias DN [" +
                        ENTRY_ALIAS_DN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // ***********************************************
        // Replace the Alias DN with the parsed DN.
        ENTRY_ALIAS_DN = zAdn.getDN();
        System.out.println(MP + "Alias DN:[" + ENTRY_ALIAS_DN + "]");

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Source Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "Alias Source Entry");

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

        // ****************************************************
        // Diable and Shoe the Current Alias Derefencing State.
        try {
            IRRSource.disableAliasDereferencing();
            IRRSource.showAliasDereferencing();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // **************************************************
        // Obtain IRR Directory Schema from our Source.
        idxIRRschema schema = new idxIRRschema(IRRSource.irrctx);

        // **************************************************
        // Obtain IRR Utility Object.
        idxIRRutil util = new idxIRRutil(schema);
        util.setVerbose(VERBOSE);

        // **************************************************
        // First Verify the SourceDN does in fact Exist.
        // TODO Verify the Entry is a Resource Only.
        try {
            if (!util.DoesEntryExist(IRRSource.irrctx, ENTRY_SOURCE_DN)) {
                if (ExitOnException) {
                    System.err.println(MP + "Source DN does not exist, unable to create new Alias.");
                    System.exit(EXIT_GENERIC_FAILURE);
                } else {
                    throw new idxIRRException("Source DN does not exist, unable to create new Alias.");
                } // End of Else.
            } // End of Outer If.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception on Lookup for Source DN. " + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception.

        // **************************************************
        // Second Verify the AliasDN does NOT Exist.
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, ENTRY_ALIAS_DN)) {
                if (ExitOnException) {
                    System.err.println(MP + "Alias Already exists, unable to create new Alias.");
                    System.exit(EXIT_GENERIC_FAILURE);
                } else {
                    throw new idxIRRException("Alias Already exists, unable to create new Alias.");
                } // End of Else.
            } // End of Outer If.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception on Lookup for Alias DN. " + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception.

        // **************************************************
        // Ok, create the Attributes Necessary to realize the
        // Alias.
        //
        Attributes attrs = new BasicAttributes(true);    // case-ignore

        Attribute oc = new BasicAttribute("objectclass");
        oc.add("Alias");
        oc.add("AliasFrameworkEntry");

        attrs.put(oc);
        attrs.put("AliasedObjectName", ENTRY_SOURCE_DN);

        // **************************************************
        // Perform bind to persist the Alias.
        System.out.println(MP + "Attempting Alias Entry Add.");
        try {

            IRRSource.irrctx.bind(ENTRY_ALIAS_DN, null, attrs);
            System.out.println(MP + "Alias Entry Successfully Added.");

        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Exception Creating Alias, " + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // **************************************************
        // Now Obtain all Aliases for our Current SourceDN.
        try {
            System.out.println(MP + "The following Aliases Exist for DN:[" +
                    ENTRY_SOURCE_DN + "]: ");

            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(NO_Attributes);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String SearchFilter = "(AliasedObjectName=" + ENTRY_SOURCE_DN + ")";

            NamingEnumeration nes = IRRSource.irrctx.search("", SearchFilter, ctls);
            // ****************************************
            // Loop Through Entries.
            while (nes.hasMore()) {
                SearchResult srs = (SearchResult) nes.next();
                String RDN = srs.getName();
                System.out.println(MP + "Alias DN:[" + RDN + "]: ");
            } // End of While Loop.

        } catch (NameNotFoundException e) {
            System.err.println(MP + "No Alias Entries Found, this is an Error, since the Add alias was Successful.");
            if (ExitOnException) {
                System.err.println(MP + "No Alias Entries Found, this is an Error, since the Add alias was Successful, " + e);
                System.exit(EXIT_IRR_GET_FAILURE);
            } else {
                throw e;
            }
        } // End of exception
        catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception on Searching for Alias Entries:\n" + e);
                System.exit(EXIT_IRR_GET_FAILURE);
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

    } // End of perform Method

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRaliasEntry
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

        VAR.add(new idxArgVerificationRules("aliasdn",
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

        ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        ENTRY_ALIAS_DN = ((String) Zin.getValue("aliasdn")).trim();
        System.out.println(MP + "Alias DN:[" + ENTRY_ALIAS_DN + "]");

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRaliasEntry FUNCTION = new IRRaliasEntry(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                ENTRY_ALIAS_DN,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRaliasEntry.\n" + e);
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

} // End of Class IRRaliasEntry
