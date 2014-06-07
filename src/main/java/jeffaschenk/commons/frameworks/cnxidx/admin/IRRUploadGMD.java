package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;


/**
 * Java Command line utility, driven from properties and command
 * line parameters to load a new Framework Knowledge GUI metadata Entry
 * into the Directory.
 * This module will assume the parent tree is in place and available.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRUploadGMD &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --xslfile
 * 	Specify Full Path of XSL (XML TRANSLATION) File.
 * --entrydn
 * 	Specify full entry DN.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --comment
 * 	Specify GMD Entry Comment.
 * --description
 * 	Specify GMD Entry Description.
 * --overwrite
 * 	Specify Existing GMD Entry will be overwritten.
 * --trimws
 * 	Specify to Trim all Whitespace from XSL file.
 * --trimnl
 * 	Specify not to preserver all NewLines in XSL file.
 * --version
 * 	Display Version information and exit.
 * --compress
 * 	Compress the XSL and load as a Binary Attribute in the directory.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */


public class IRRUploadGMD implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2001-09-07, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRUploadGMD: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;

    private static String xslInfileName = null;

    private static String KentryDN = null;

    private static boolean OVERWRITE_KENTRY = false;
    private static boolean TRIM_WHITESPACE = false;
    private static boolean TRIM_NEWLINES = false;

    private static String Kcomment = null;
    private static String Kdescription = null;

    private static boolean VERBOSE = false;
    private static boolean COMPRESS = false;

    private boolean ExitOnException = false;

    private byte[] bufwork = null;
    private StringBuffer xslStringBuffer = null;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRUploadGMD <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--xslfile ");
        System.err.println("\tSpecify Full Path of XSL (XML TRANSLATION) File.");
        System.err.println(MP + "--entrydn ");
        System.err.println("\tSpecify full DN of Entry");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--comment");
        System.err.println("\tSpecify GMD Entry Comment.");
        System.err.println(MP + "--description");
        System.err.println("\tSpecify GMD Entry Description.");
        System.err.println(MP + "--overwrite");
        System.err.println("\tSpecify Existing GMD Entry will be overwritten.");

        System.err.println(MP + "--trimws");
        System.err.println("\tSpecify to Trim all Whitespace from XSL file.");

        System.err.println(MP + "--trimnl");
        System.err.println("\tSpecify not to preserver all NewLines in XSL file.");

        System.err.println(MP + "--compress");
        System.err.println("\tSpecify to Compress the XSL file and load as a Binary Attribute in the Directory.");
        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of class.

    /**
     * IRRUploadGMD Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _KentryDN  Entry DN to be written.
     * @param _xslInfileName  XML Input File.
     * @param _Kcomment  Comment to be added to Entry.
     * @param _Kdescription  Description to be added to Entry.
     * @param _OVERWRITE_KENTRY Indicate if Existing Entry and will be overwritten.
     * @param _TRIM_WHITESPACE Indicate to Trim all Whitspace from Input File.
     * @param _TRIM_NEWLINES Indicate to Trim all NewLines from Input File.
     * @param _COMPRESS Indicate COMPRESS Attribute to Binary Form.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRUploadGMD(String _IRRHost,
                        String _IRRPrincipal,
                        String _IRRCredentials,
                        String _KentryDN,
                        String _xslInfileName,
                        String _Kcomment,
                        String _Kdescription,
                        boolean _OVERWRITE_KENTRY,
                        boolean _TRIM_WHITESPACE,
                        boolean _TRIM_NEWLINES,
                        boolean _COMPRESS,
                        boolean _VERBOSE,
                        boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        KentryDN = _KentryDN;
        xslInfileName = _xslInfileName;
        Kcomment = _Kcomment;
        Kdescription = _Kdescription;
        OVERWRITE_KENTRY = _OVERWRITE_KENTRY;
        TRIM_WHITESPACE = _TRIM_WHITESPACE;
        TRIM_NEWLINES = _TRIM_NEWLINES;
        COMPRESS = _COMPRESS;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRUploadGMD.

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
        idxParseDN zKdn = new idxParseDN(KentryDN);
        if (!zKdn.isValid()) {
            if (ExitOnException) {
                System.err.println(MP + "Entry DN [" +
                        KentryDN +
                        "] is Invalid, unable to continue.");
                System.exit(EXIT_IRR_GMD_FAILURE);
            } else {
                throw new idxIRRException(MP + "Entry DN [" +
                        KentryDN +
                        "] is Invalid, unable to continue.");
            } // End of Inner Else.
        } // End of If.

        if ((!zKdn.getNamingAttribute().equalsIgnoreCase("xen")) &&
                (!zKdn.getNamingAttribute().equalsIgnoreCase("xfen"))) {
            if (ExitOnException) {
                System.err.println(MP + "Entry DN [" +
                        KentryDN +
                        "] has Invalid Naming Attribute, unable to continue.");
                System.exit(EXIT_IRR_GMD_FAILURE);
            } else {
                throw new idxIRRException(MP + "Entry DN [" +
                        KentryDN +
                        "] has Invalid Naming Attribute,  unable to continue.");
            } // End of Inner Else.
        } // End of If.

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "UploadGMD");

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

        // *******************************************
        // Read input from XML Input File
        // Create a Large Buffered String.
        System.out.println(MP + "Attempting Open of XSL Input File:[" + xslInfileName + "]");
        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(xslInfileName), 16384);
            System.out.println(MP + "Open of XSL Input File Successful.");
            System.out.println(MP + "Processing XSL... ");
            String str;
            while ((str = in.readLine()) != null) {

                // *******************************
                // Build our Attribute Buffer
                //
                if (xslStringBuffer != null) {
                    if (!TRIM_NEWLINES) {
                        xslStringBuffer = xslStringBuffer.append("\n");
                    }

                    if (TRIM_WHITESPACE) {
                        xslStringBuffer = xslStringBuffer.append(str.trim());
                    } else {
                        xslStringBuffer = xslStringBuffer.append(str);
                    }
                } else {
                    if (TRIM_WHITESPACE) {
                        xslStringBuffer = new StringBuffer(str.trim());
                    } else {
                        xslStringBuffer = new StringBuffer(str);
                    }
                }
            } // End of While
            in.close();
            System.out.println(MP + "Processing of XSL Input File Successful.");
        } catch (IOException e) {
            if (ExitOnException) {
                System.err.println(MP + "XSL Input File Error " + e);
                System.exit(EXIT_IRR_ERROR_PROCESSING_INPUT_FILE);
            } else {
                throw e;
            }
        } // End of Exception

        // **************************************
        // Show our processed Blob Length.
        byte[] xslBytes = xslStringBuffer.toString().getBytes();
        System.out.println(MP + "XSL Blob Byte Length: [" + xslBytes.length + "]");

        // **************************************
        // Perform Compression if Requested.
        if (COMPRESS) {
            try {
                int originalLength = xslBytes.length;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream zout = new GZIPOutputStream(
                        baos,
                        (xslBytes.length * 10) / 8); // assume 80% or better compression

                zout.write(xslBytes);
                zout.close();
                xslBytes = baos.toByteArray();

                System.out.println(MP + "Compressed XSL Blob Byte Length: ["
                        + xslBytes.length + "]  [" +
                        (100 - (xslBytes.length * 100 / originalLength))
                        + "% Compression]");
            } catch (IOException ioe) {
                if (ExitOnException) {
                    System.err.println(MP + "Error Compressing Data, " + ioe);
                    System.exit(EXIT_IRR_ERROR_COMPRESSING_DATA);
                } else {
                    throw ioe;
                }
            } // End of Exception.
        } // End of Compress If.


        // *****************************************
        // Bind Entry to the Directory.
        // (That's an Add for LDAPies...)
        //
        System.out.println(MP + "Formulating GMD Entry.");

        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("cnxidoXKnowledge");

        // Create an attribute with a byte array
        BasicAttribute cnxXblob;

        if (COMPRESS) {
            cnxXblob = new
                    BasicAttribute("cnxidaxcompressedobjectblob", xslBytes);
        } else {
            cnxXblob = new
                    BasicAttribute("cnxidaxobjectblob", xslBytes);
        }

        // Create attributes to be associated with object
        Attributes attrs = new BasicAttributes(true); // case-ignore
        attrs.put(oc);
        attrs.put(cnxXblob);

        if (COMPRESS) {
            attrs.put("cnxidaXCompressionFormat", "gzip");
        }

        if (Kcomment == null) {
            attrs.put("cnxidaComment", "FRAMEWORK, INCORPORATED");
        } else {
            attrs.put("cnxidaComment", Kcomment);
        }

        if (Kdescription == null) {
            attrs.put("cnxidaDesc", "FRAMEWORK XSL GMD Knowledge Entry");
        } else {
            attrs.put("cnxidaDesc", Kdescription);
        }

        attrs.put("cnxidaType", "XSL");
        attrs.put("cnxidaState", "running");

        idxTimeStamp CurrentTimeStamp = new idxTimeStamp();
        attrs.put("cnxidaInstallTime", CurrentTimeStamp.get());

        attrs.put("cnxidaInstallBy", IRRPrincipal);

        // Perform bind
        System.out.println(MP + "Attempting Knowledge Entry Add.");
        try {
            if (attrs.size() == 0) {
                if (ExitOnException) {
                    System.err.println(MP + "GMD Entry [" + KentryDN + "], contains no Attributes.");
                    System.exit(EXIT_IRR_GMD_FAILURE);
                } else {
                    System.err.println(MP + "GMD Entry [" + KentryDN + "], contains no Attributes.");
                    throw new idxIRRException("GMD Entry [" +
                            KentryDN +
                            "], contains no Attributes, unable to continue.");
                } // End of If Exit on Exception.

            } // end of If.

            IRRSource.irrctx.bind(KentryDN, null, attrs);
        } catch (NameAlreadyBoundException e) {
            // ****************************************************
            // Ok, we caught ourselves adding an existing entry.
            // If our OVERWRITE Flag is Set,
            // DO NOT REBIND, but perform a modification of the
            // attributes.  Rebind are not allowed for entries
            // which have children/lead entries.
            //
            if (OVERWRITE_KENTRY) {
                try {
                    System.out.println(MP + "Existing entry detected, attempting modification of Knowledge Entry");
                    // *******************************************
                    // Remove possible existing attributes.
                    util.RemoveAttribute(IRRSource.irrctx,
                            KentryDN,
                            "cnxidaxcompressedobjectblob",
                            true);

                    util.RemoveAttribute(IRRSource.irrctx,
                            KentryDN,
                            "cnxidaxobjectblob",
                            true);

                    util.RemoveAttribute(IRRSource.irrctx,
                            KentryDN,
                            "cnxidaxcompressionformat",
                            true);

                    // ***********************************************
                    // Now Add the Necessary Attributes.
                    attrs.remove("objectclass");       // Remove the Objectclass Attribute.
                    attrs.remove("cnxidaInstallTime"); // Remove the Install Time Attribute.
                    attrs.remove("cnxidaInstallBy");   // Remove the InstallBy Attribute.

                    attrs.put("cnxidaLastModifyTime", CurrentTimeStamp.get());
                    attrs.put("cnxidaLastModifyBy", IRRPrincipal);

                    IRRSource.irrctx.modifyAttributes(KentryDN,
                            IRRSource.irrctx.REPLACE_ATTRIBUTE, attrs);

                } catch (Exception emod) {
                    if (ExitOnException) {
                        System.err.println(MP + "GMD Entry [" + KentryDN + "] Modification Exception, " + emod);
                        System.exit(EXIT_IRR_GMD_ENTRY_MOD_FAILURE);
                    } else {
                        System.err.println(MP + "GMD Entry [" + KentryDN + "] was Not Found, " + emod);
                        throw emod;

                    } // End of If Exit on Exception.
                } // End of exception
            } else {
                if (ExitOnException) {
                    System.err.println(MP + "Unable to Add Existing entry, since Overwrite flag was not set.");
                    System.exit(EXIT_IRR_GMD_ENTRY_EXISTS_FOR_ADD);
                } else {
                    throw new idxIRRException(MP + "Unable to Add Existing entry, since Overwrite flag was not set.");

                } // End of If Exit on Exception.
            } // End of Else.

        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "GMD Entry [" + KentryDN + "] Write Exception, " + e);
                System.exit(EXIT_IRR_GMD_ENTRY_WRITE_FAILURE);
            } else {
                System.err.println(MP + "GMD Entry [" + KentryDN + "] Write Exception, " + e);
                throw e;
            } // End of If Exit on Exception.
        } // End of exception

        // ****************************************
        // Search for the entry just written.
        System.out.println(MP + "GMD Entry written successfully.");
        System.out.println(MP + "Now Searching for GMD DN:[" + KentryDN + "]");
        try {
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(util.OpAttrIDs);
            ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

            idxIRROutput.PrintSearchList(
                    IRRSource.irrctx.search(KentryDN, "(objectclass=*)", ctls), KentryDN);

            System.out.println(MP + "GMD Entry Load successfully Completed.");

        } catch (NameNotFoundException e) {
            if (ExitOnException) {
                System.err.println(MP + "GMD Entry [" + KentryDN + "] was Not Found, " + e);
                System.exit(EXIT_IRR_GMD_ENTRY_NOTFOUND_AFTER_WRITE);
            } else {
                System.err.println(MP + "GMD Entry [" + KentryDN + "] was Not Found, " + e);
                throw e;
            }

        } // End of exception
        catch (Exception e) {
            System.err.println(MP + "Directory Search Error " + e);
            if (ExitOnException) {
                System.err.println(MP + "IRR Directory Search Error, " + e);
                System.exit(EXIT_IRR_SEARCH_OPERATION_FAILURE);
            } else {
                System.err.println(MP + "IRR Directory Search Error, " + e);
                throw e;
            }

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
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRUploadGMD
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

        VAR.add(new idxArgVerificationRules("xslfile",
                true, true));

        VAR.add(new idxArgVerificationRules("entrydn",
                true, true));

        VAR.add(new idxArgVerificationRules("description",
                false, true));

        VAR.add(new idxArgVerificationRules("comment",
                false, true));

        VAR.add(new idxArgVerificationRules("overwrite",
                false, false));

        VAR.add(new idxArgVerificationRules("trimws",
                false, false));

        VAR.add(new idxArgVerificationRules("trimnl",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
                false, false));

        VAR.add(new idxArgVerificationRules("compress",
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
            OVERWRITE_KENTRY = true;
        }

        if (Zin.doesNameExist("trimws")) {
            TRIM_WHITESPACE = true;
        }

        if (Zin.doesNameExist("trimnl")) {
            TRIM_NEWLINES = true;
        }

        if (Zin.doesNameExist("compress")) {
            COMPRESS = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        xslInfileName = ((String) Zin.getValue("xslfile")).trim();
        System.out.println(MP + "XSLFile:[" + xslInfileName + "]");

        KentryDN = ((String) Zin.getValue("entrydn")).trim();
        System.out.println(MP + "KentryDN:[" + KentryDN + "]");

        if (Zin.doesNameExist("comment")) {
            Kcomment = ((String) Zin.getValue("comment")).trim();
        }

        if (Zin.doesNameExist("description")) {
            Kdescription = ((String) Zin.getValue("description")).trim();
        }

        // ************************************************
        // Show Operational Parameters
        if (OVERWRITE_KENTRY) {
            System.out.println(MP + "Will Overwrite existing GMD Entry with new entry.");
        } else {
            System.out.println(MP + "Will NOT Overwrite existing GMD Entry with new entry.");
        }

        if (TRIM_WHITESPACE) {
            System.out.println(MP + "Will Trim all Whitespace from XSL.");
        } else {
            System.out.println(MP + "Will NOT Trim all Whitespace from XSL.");
        }

        if (TRIM_NEWLINES) {
            System.out.println(MP + "Will Not Preserve NewLines in XSL.");
        } else {
            System.out.println(MP + "Will Preserve NewLines in XSL.");
        }

        if (COMPRESS) {
            System.out.println(MP + "Will compress the XSL.");
        } else {
            System.out.println(MP + "Will NOT compress the XSL.");
        }


        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRUploadGMD FUNCTION = new IRRUploadGMD(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                KentryDN,
                xslInfileName,
                Kcomment,
                Kdescription,
                OVERWRITE_KENTRY,
                TRIM_WHITESPACE,
                TRIM_NEWLINES,
                COMPRESS,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRUploadGMD.\n" + e);
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

} // End of Class IRRUploadGMD
