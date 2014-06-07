
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
 * line parameters to set a Password for an Existing Entry in the
 * the IRR Directory.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRsetPassword &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * 	Specify full entry DN for which password is to be set.
 * --password
 * 	Specify password to be set.
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


public class IRRsetPassword implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2001-09-14, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRsetPassword: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;

    private static String SourceDN = null;
    private static String Password = null;

    private static boolean REMOVE = false;

    private static boolean VERBOSE = false;

    private boolean ExitOnException = false;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRsetPassword <Required Parameters> <Optional Parameters>");

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
        System.err.println("\tSpecify full DN of Entry for which password is to be set.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--password ");
        System.err.println("\tSpecify password.");

        System.err.println(MP + "--remove");
        System.err.println("\tSpecify Existing Password to be Removed.");
        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of class.

    /**
     * IRRsetPassword Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _SourceDN  Entry DN to be written.
     * @param _Password  Password.
     * @param _REMOVE Indicate if Existing Password to be removed.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRsetPassword(String _IRRHost,
                          String _IRRPrincipal,
                          String _IRRCredentials,
                          String _SourceDN,
                          String _Password,
                          boolean _REMOVE,
                          boolean _VERBOSE,
                          boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        SourceDN = _SourceDN;
        Password = _Password;
        REMOVE = _REMOVE;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRsetPassword.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors
     *                         during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // ***********************************************
        // Now determine if SourceDN is Valid.
        idxParseDN zKdn = new idxParseDN(SourceDN);
        if (!zKdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Entry DN [" +
                        SourceDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw new idxIRRException(MP + "Entry DN [" +
                        SourceDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // *****************************************
        // Check Password and Option.
        if (((Password == null) ||
                ("".equals(Password))) &&
                (!REMOVE)) {
            if (ExitOnException) {
                System.err.println(MP + "Password or REMOVE Option Not Specified, unable to Continue.");
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw new idxIRRException(MP +
                        "Password or REMOVE Option Not Specified Unable to Continue.");
            } // End of Inner Else.
        } // End of Outer If.

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "SetPassword");

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
        util.setVerbose(VERBOSE);

        // *****************************************
        // Formulate the Attribute.
        //
        System.out.println(MP + "Formulating Attribute Change.");

        Attributes attrs = new BasicAttributes(true); // case-ignore
        if (!REMOVE) {
            attrs.put("userpassword", Password);
        }

        idxTimeStamp CurrentTimeStamp = new idxTimeStamp();
        attrs.put("cnxidaLastModifyTime", CurrentTimeStamp.get());
        attrs.put("cnxidaLastModifyBy", IRRPrincipal);

        // ********************************************************
        // Perform the Modification.
        try {

            // ********************************
            // Perform a Remove.
            if (REMOVE) {
                util.RemoveAttribute(IRRSource.irrctx,
                        SourceDN,
                        "userpassword",
                        true);

                util.RemoveAttribute(IRRSource.irrctx,
                        SourceDN,
                        "userpassword;hash-md5",
                        true);

                IRRSource.irrctx.modifyAttributes(SourceDN,
                        IRRSource.irrctx.REPLACE_ATTRIBUTE, attrs);

                System.out.println(MP + "Removed Password from Entry was Successful.");
            } // End of Remove.
            else {
                // ********************************
                // Perform a Modification.
                IRRSource.irrctx.modifyAttributes(SourceDN,
                        IRRSource.irrctx.REPLACE_ATTRIBUTE, attrs);
                System.out.println(MP + "Modification of Password for Entry was Successful.");
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Source Entry [" + SourceDN + "] Modification Exception, " + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                System.err.println(MP + "Source Entry [" + SourceDN + "] Modification Exception, " + e);
                throw e;
            } // End of If Exit on Exception.
        } // End of exception

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


    } // End of perform Method.

    /**
     * verify Method class performs a simple Authentication Request.
     *
     * @return boolean indicator if Authentication was Successful or not.
     * @throws idxIRRException for any specific IRR unrecoverable errors
     *                         during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public boolean verify(String _Principal,
                          String _Credentials) throws Exception, idxIRRException {

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        if ((_Credentials == null) ||
                ("".equals(_Credentials))) {
            _Credentials = "";
        }
        idxManageContext IRRTest = new idxManageContext(IRRHost,
                _Principal,
                _Credentials,
                "TestPassword");

        // ************************************************
        // Exit on all Exceptions.
        IRRTest.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        boolean Xrc = false;
        try {
            IRRTest.open();
            Xrc = true;
            IRRTest.close();
            return (Xrc);
        } catch (Exception e) {
            return (Xrc);
        } // End of exception

    } // End of verify Method.


    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRsetPassword
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

        VAR.add(new idxArgVerificationRules("password",
                false, true, "verifyPassword"));

        VAR.add(new idxArgVerificationRules("remove",
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
        if (Zin.doesNameExist("remove")) {
            REMOVE = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        SourceDN = ((String) Zin.getValue("sourcedn")).trim();
        System.out.println(MP + "SourceDN:[" + SourceDN + "]");

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("password")) {
            Password = ((String) Zin.getValue("password")).trim();
        }

        // ************************************************
        // Show Operational Parameters
        if (REMOVE) {
            System.out.println(MP + "Will Remove existing Password from Entry.");
        } else {
            System.out.println(MP + "Will Replace/Add PAssword to existing Entry.");
        }

        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRsetPassword FUNCTION = new IRRsetPassword(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                SourceDN,
                Password,
                REMOVE,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRsetPassword.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Now Verify.
        boolean myAuthRC = false;
        try {
            myAuthRC = FUNCTION.verify(SourceDN, Password);
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing Verification of IRRsetPassword.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());

        // ****************************************
        // Show the Results.
        if (!REMOVE && myAuthRC) {
            System.out.println(MP + "IRR Entry Password Set and Verified Successfully.");
            System.exit(EXIT_SUCCESSFUL);
        } else if (!REMOVE && !myAuthRC) {
            System.out.println(MP + "IRR Entry Password NOT Verified, Error has Occurred.");
            System.exit(EXIT_GENERIC_FAILURE);
        } else if (REMOVE && myAuthRC) {
            System.out.println(MP + "IRR Entry Password Removed and Verified Successfully.");
            System.exit(EXIT_SUCCESSFUL);
        } else if (REMOVE && !myAuthRC) {
            System.out.println(MP + "IRR Entry Password NOT Removed, Error has Occurred.");
            System.exit(EXIT_GENERIC_FAILURE);
        } else {
            System.out.println(MP + "Logic Error has Occurred.");
            System.exit(EXIT_GENERIC_FAILURE);
        }

    } // End of Main

} // End of Class IRRsetPassword
