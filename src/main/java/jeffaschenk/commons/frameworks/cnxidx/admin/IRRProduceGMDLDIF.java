package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.io.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to produce LDIF from a new Framework Knowledge
 * GUI metadata Entry.  The Directory is not accessed.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRProduceGMDLDIF &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
 * <br>
 * <b>Required Parameters are:</b>
 * <pre>
 * --xslfile
 * 	Specify Full Path of XSL (XML TRANSLATION) File.
 * --ldifout
 * 	Specify Full Path of LDIF Output File.
 * --entrydn
 * 	Specify full entry DN.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --notnice
 * 	Specify Standard LDIF DN Output, Default will output Non-Continued DNs.
 * --comment
 * 	Specify GMD Entry Comment.
 * --description
 * 	Specify GMD Entry Description.
 * --trimws
 * 	Specify to Trim all Whitespace from XSL file.
 * --trimnl
 * 	Specify not to preserver all NewLines in XSL file.
 * --version
 * 	Display Version information and exit.
 * --compress
 * 	Compress the XSL and load as a Binary Attribute in the directory.
 * --append
 * 	Append LDIF Output to Named LDIF Output File.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */


public class IRRProduceGMDLDIF implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2001-09-07, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRProduceGMDLDIF: ";

    private static String ldifoutputfilename = null;

    private static String xslInfileName = null;

    private static String KentryDN = null;

    private static boolean TRIM_WHITESPACE = false;
    private static boolean TRIM_NEWLINES = false;

    private static String Kcomment = null;
    private static String Kdescription = null;

    private static boolean VERBOSE = false;
    private static boolean COMPRESS = false;
    private static boolean APPEND = false;
    private static boolean NICE = true;

    private boolean ExitOnException = false;

    private byte[] bufwork = null;
    private StringBuffer xslStringBuffer = null;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRProduceGMDLDIF <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--xslfile ");
        System.err.println("\tSpecify Full Path of XSL (XML TRANSLATION) File.");
        System.err.println(MP + "--ldifout ");
        System.err.println("\tSpecify Full Path of LDIF Output File.");
        System.err.println(MP + "--entrydn ");
        System.err.println("\tSpecify full DN of Entry");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--comment");
        System.err.println("\tSpecify GMD Entry Comment.");
        System.err.println(MP + "--description");
        System.err.println("\tSpecify GMD Entry Description.");

        System.err.println(MP + "--trimws");
        System.err.println("\tSpecify to Trim all Whitespace from XSL file.");

        System.err.println(MP + "--trimnl");
        System.err.println("\tSpecify not to preserver all NewLines in XSL file.");

        System.err.println(MP + "--compress");
        System.err.println("\tSpecify to Compress the XSL file and load as a Binary Attribute in the Directory.");
        System.err.println(MP + "--append");
        System.err.println("\tSpecify to Append LDIF Output to Existing File.");
        System.err.println(MP + "--notnice");
        System.err.println("\tSpecify to Output Standard LDIF Output, default is Nice DNs.");
        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of class.

    /**
     * IRRProduceGMDLDIF Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _KentryDN  Entry DN to be written.
     * @param _xslInfileName  XML Input File.
     * @param _ldifoutputfilename  LDIF Output File.
     * @param _Kcomment  Comment to be added to Entry.
     * @param _Kdescription  Description to be added to Entry.
     * @param _TRIM_WHITESPACE Indicate to Trim all Whitspace from Input File.
     * @param _TRIM_NEWLINES Indicate to Trim all NewLines from Input File.
     * @param _COMPRESS Indicate COMPRESS Attribute to Binary Form.
     * @param _APPEND Indicate APPEND LDIF Output to Existing File.
     * @param _NICE Indicate NICE LDIF Output for DNs.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRProduceGMDLDIF(String _KentryDN,
                             String _xslInfileName,
                             String _ldifoutputfilename,
                             String _Kcomment,
                             String _Kdescription,
                             boolean _TRIM_WHITESPACE,
                             boolean _TRIM_NEWLINES,
                             boolean _COMPRESS,
                             boolean _APPEND,
                             boolean _NICE,
                             boolean _VERBOSE,
                             boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        KentryDN = _KentryDN;
        xslInfileName = _xslInfileName;
        ldifoutputfilename = _ldifoutputfilename;
        Kcomment = _Kcomment;
        Kdescription = _Kdescription;
        TRIM_WHITESPACE = _TRIM_WHITESPACE;
        TRIM_NEWLINES = _TRIM_NEWLINES;
        COMPRESS = _COMPRESS;
        APPEND = _APPEND;
        NICE = _NICE;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRProduceGMDLDIF.

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
        // Formulate the Directory Entry for Output.
        System.out.println(MP + "Formulating GMD Entry for Output.");

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
        attrs.put(zKdn.getNamingAttribute(), zKdn.getNamingValue());

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

        // **************************************
        // Open up the File Output Stream.
        System.out.println(MP + "Opening LDIF Output File...");
        BufferedWriter LDIFOUT = null;
        try {
            LDIFOUT = new BufferedWriter(new FileWriter(ldifoutputfilename, APPEND));

            if (!APPEND) {
                LDIFOUT.write("version: 1\n");
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("# FRAMEWORK IRR Produce GMD LDIF.\n");
                LDIFOUT.write("# Start Time: " + CurrentTimeStamp.get() + "\n");
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("\n");
            }

        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Exception opening LDIF Output File. " + e);
                System.exit(EXIT_IRR_GMD_LDIF_OUTPUT_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // ****************************************
        // Indicate the Output is starting.
        System.out.println(MP + "Starting Write...");

        // *****************************************************
        // Now output our Formulated Attributes Object to LDIF.
        try {
            idxIRROutput.EntryToLDIF(KentryDN,
                    attrs,
                    LDIFOUT,
                    NICE);

        } catch (Exception e) {
            LDIFOUT.flush();
            LDIFOUT.close();
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception on Writing LDIF Output, " + e);
                System.exit(EXIT_IRR_GMD_LDIF_OUTPUT_FAILURE);
            } else {
                throw e;
            }
        } // End of exception


        // ***************************************
        // Close our Output File.
        try {
            LDIFOUT.flush();
            LDIFOUT.close();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Exception closing Output File. " + e);
                System.exit(EXIT_IRR_GMD_LDIF_OUTPUT_CLOSE_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // ****************************************
        // Show Completion.
        System.out.println(MP + "GMD Entry in LDIF written successfully.");

    } // End of perform Method.


    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRProduceGMDLDIF
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

        VAR.add(new idxArgVerificationRules("xslfile",
                true, true));

        VAR.add(new idxArgVerificationRules("ldifout",
                true, true));

        VAR.add(new idxArgVerificationRules("entrydn",
                true, true));

        VAR.add(new idxArgVerificationRules("description",
                false, true));

        VAR.add(new idxArgVerificationRules("comment",
                false, true));

        VAR.add(new idxArgVerificationRules("trimws",
                false, false));

        VAR.add(new idxArgVerificationRules("trimnl",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
                false, false));

        VAR.add(new idxArgVerificationRules("compress",
                false, false));

        VAR.add(new idxArgVerificationRules("append",
                false, false));

        VAR.add(new idxArgVerificationRules("notnice",
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

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("trimws")) {
            TRIM_WHITESPACE = true;
        }

        if (Zin.doesNameExist("trimnl")) {
            TRIM_NEWLINES = true;
        }

        if (Zin.doesNameExist("compress")) {
            COMPRESS = true;
        }

        if (Zin.doesNameExist("append")) {
            APPEND = true;
        }

        if (Zin.doesNameExist("notnice")) {
            NICE = false;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        xslInfileName = ((String) Zin.getValue("xslfile")).trim();
        System.out.println(MP + "XSLFile:[" + xslInfileName + "]");

        ldifoutputfilename = ((String) Zin.getValue("ldifout")).trim();
        System.out.println(MP + "LDIF Output File:[" + ldifoutputfilename + "]");

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

        if (APPEND) {
            System.out.println(MP + "Will Append LDIF Output to Existing File.");
        }

        if (NICE) {
            System.out.println(MP + "Will Create Nice LDIF Output, Non-Continued DNs.");
        }

        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRProduceGMDLDIF FUNCTION = new IRRProduceGMDLDIF(
                KentryDN,
                xslInfileName,
                ldifoutputfilename,
                Kcomment,
                Kdescription,
                TRIM_WHITESPACE,
                TRIM_NEWLINES,
                COMPRESS,
                APPEND,
                NICE,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRProduceGMDLDIF.\n" + e);
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

} // End of Class IRRProduceGMDLDIF
