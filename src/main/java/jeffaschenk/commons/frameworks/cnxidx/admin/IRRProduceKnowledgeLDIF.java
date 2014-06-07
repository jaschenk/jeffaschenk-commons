
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
 * Entry.  The Directory is not accessed.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRProduceKnowledgeLDIF &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
 * <br>
 * <b>Required Parameters are:</b>
 * <pre>
 * --xsdfile
 * 	Specify Full Path of XSD (XML SCHEMA) File.
 * --ldifout
 * 	Specify Full Path of LDIF Output File.
 * --customer
 * 	Specify Customer DN, dc=acme,dc=com
 * --vendor
 * 	Specify Knowledge Entry Vendor, Cisco|Juniper|Acme
 * --type
 * 	Specify Knowledge Device Type, Router|Switch|FooBar
 * --model
 * 	Specify Knowledge Device Model, 72XX|75XX|FooBar
 * --os
 * 	Specify Knowledge Device OS, 12.4(11)|1.1.(FooBar)
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --notnice
 * 	Specify Standard LDIF DN Output, Default will output Non-Continued DNs.
 * --comment
 * 	Specify Knowledge Entry Comment.
 * --description
 * 	Specify Knowledge Entry Description.
 * --createktree
 * 	Specify to Create Knowledge Tree Containers if they do not exist.
 * --overwrite
 * 	Specify Existing Knowledge Entry will be overwritten.
 * --compress
 * 	Compress the XSD and load as a Binary Attribute in the directory.
 * --append
 * 	Append LDIF Output to Named LDIF Output File.
 * --trimws
 * 	Specify to Trim all Whitespace from XSD file.
 * --trimnl
 * 	Specify not to preserver all NewLines in XSD file.
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


public class IRRProduceKnowledgeLDIF implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2001-09-07, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRProduceKnowledgeLDIF: ";

    private static String ldifoutputfilename = null;

    private static String xsdInfileName = null;

    private static String Kcustomer = null;
    private static String Kvendor = null;
    private static String Ktype = null;
    private static String Kmodel = null;
    private static String Kos = null;

    private static String KhashVersion = null; // JLM

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
    private StringBuffer xsdStringBuffer = null;
    private String KentryDN = null;


    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "ProduceKnowledgeLDIF <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--ldifout ");
        System.err.println("\tSpecify Full Path of LDIF File.");
        System.err.println(MP + "--xsdfile ");
        System.err.println("\tSpecify Full Path of XSD (XML SCHEMA) File.");
        System.err.println(MP + "--customer ");
        System.err.println("\tSpecify Customer DN, dc=acme,dc=com");
        System.err.println(MP + "--vendor ");
        System.err.println("\tSpecify Knowledge Entry Vendor, Cisco|Juniper|Acme");
        System.err.println(MP + "--type");
        System.err.println("\tSpecify Knowledge Device Type, Router|Switch|FooBar");
        System.err.println(MP + "--model");
        System.err.println("\tSpecify Knowledge Device Model, 72XX|75XX|FooBar");
        System.err.println(MP + "--os");
        System.err.println("\tSpecify Knowledge Device OS, 12.4(11)|1.1.(FooBar)");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--comment");
        System.err.println("\tSpecify Knowledge Entry Comment.");
        System.err.println(MP + "--description");
        System.err.println("\tSpecify Knowledge Entry Description.");

        System.err.println(MP + "--trimws");
        System.err.println("\tSpecify to Trim all Whitespace from XSD file.");

        System.err.println(MP + "--trimnl");
        System.err.println("\tSpecify not to preserver all NewLines in XSD file.");

        System.err.println(MP + "--compress");
        System.err.println("\tSpecify to Compress the XSD file and load as a Binary Attribute in the Directory.");

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
     * IRRProduceKnowledgeLDIF Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _xsdInfileName  XML Input File.
     * @param _ldifoutputfilename  LDIF Output File.
     * @param _Kcustomer  Customer of Entry.
     * @param _Kvendor  Vendor of Entry.
     * @param _Ktype  Vendor Type of Entry.
     * @param _Kmodel  Vendor Model of Entry.
     * @param _Kos  Vendor OS of Entry.
     * @param _Kcomment  Comment to be added to Entry.
     * @param _Kdescription  Description to be added to Entry.
     * @param _TRIM_WHITESPACE Indicate to Trim all Whitspace from Input File.
     * @param _TRIM_NEWLINES Indicate to Trim all NewLines from Input File.
     * @param _COMPRESS Indicate COMPRESS Attribute to Binary Form.
     * @param _APPEND Indicate APPEND LDIF Output to Existing File.
     * @param _NICE Indicate NICE LDIF Output.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRProduceKnowledgeLDIF(String _xsdInfileName,
                                   String _ldifoutputfilename,
                                   String _Kcustomer,
                                   String _Kvendor,
                                   String _Ktype,
                                   String _Kmodel,
                                   String _Kos,
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
        xsdInfileName = _xsdInfileName;
        ldifoutputfilename = _ldifoutputfilename;
        Kcustomer = _Kcustomer;
        Kvendor = _Kvendor;
        Ktype = _Ktype;
        Kmodel = _Kmodel;
        Kos = _Kos;
        Kcomment = _Kcomment;
        Kdescription = _Kdescription;
        TRIM_WHITESPACE = _TRIM_WHITESPACE;
        TRIM_NEWLINES = _TRIM_NEWLINES;
        COMPRESS = _COMPRESS;
        APPEND = _APPEND;
        NICE = _NICE;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;
        KhashVersion = "r2.0-1"; // default value for V 2.0 of release

    } // End of Constructor for IRRProduceKnowledgeLDIF.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors
     *                         during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // *****************************************
        // Read input from XML Schema Input File
        // Create a Large String.
        System.out.println(MP + "Attempting Open of XSD Input File:[" + xsdInfileName + "]");
        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(xsdInfileName), 16384);
            System.out.println(MP + "Open of XSD Input File Successful.");
            System.out.print(MP + "Processing XSD... ");
            String str;
            while ((str = in.readLine()) != null) {

                // *******************************
                // Build our Attribute Buffer
                //
                if (xsdStringBuffer != null) {
                    if (!TRIM_NEWLINES) {
                        xsdStringBuffer = xsdStringBuffer.append("\n");
                    }

                    if (TRIM_WHITESPACE) {
                        xsdStringBuffer = xsdStringBuffer.append(str.trim());
                    } else {
                        xsdStringBuffer = xsdStringBuffer.append(str);
                    }
                } else {
                    if (TRIM_WHITESPACE) {
                        xsdStringBuffer = new StringBuffer(str.trim());
                    } else {
                        xsdStringBuffer = new StringBuffer(str);
                    }
                }
            } // End of While
            in.close();
            System.out.println("\n" + MP + "Processing of XSD Input File Successful.");
        } catch (IOException e) {
            if (ExitOnException) {
                System.err.println(MP + "XSD Input File Error " + e);
                System.exit(EXIT_IRR_ERROR_PROCESSING_INPUT_FILE);
            } else {
                throw e;
            }
        } // End of Exception

        // **************************************
        // Show our processed Blob Length.
        byte[] xsdBytes = xsdStringBuffer.toString().getBytes();
        System.out.println(MP + "XSD Blob Byte Length: [" + xsdBytes.length + "]");

        // **************************************
        // Perform Compression if Requested.
        if (COMPRESS) {
            try {
                int originalLength = xsdBytes.length;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream zout = new GZIPOutputStream(
                        baos,
                        (xsdBytes.length * 10) / 8); // assume 80% or better compression

                zout.write(xsdBytes);
                zout.close();
                xsdBytes = baos.toByteArray();

                System.out.println(MP + "Compressed XSL Blob Byte Length: ["
                        + xsdBytes.length + "]  [" +
                        (100 - (xsdBytes.length * 100 / originalLength))
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
        // Now Formulate the KEntryDN.
        //
        System.out.println(MP + "Formulating Knowledge Entry.");
        KentryDN = "ou=vendorobjects,ou=framework," + Kcustomer;

        KentryDN = "ou=" + Kvendor + "," + KentryDN;

        KentryDN = "ou=" + Ktype + "," + KentryDN;

        KentryDN = "ou=" + Kmodel + "," + KentryDN;

        KentryDN = "ou=" + Kos + "," + KentryDN;

        KentryDN = "xen=kentry," + "ou=" + "knowledge" + "," + KentryDN;


        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("cnxidoXKnowledge");

        // Create an attribute with a byte array
        BasicAttribute cnxXblob;

        if (COMPRESS) {
            cnxXblob = new
                    BasicAttribute("cnxidaxcompressedobjectblob", xsdBytes);
        } else {
            cnxXblob = new
                    BasicAttribute("cnxidaxobjectblob", xsdBytes);
        }

        // Create attributes to be associated with object
        Attributes attrs = new BasicAttributes(true); // case-ignore
        attrs.put(oc);
        attrs.put(cnxXblob);
        attrs.put("xen", "kentry");

        if (COMPRESS) {
            attrs.put("cnxidaXCompressionFormat", "gzip");
        }

        if (Kcomment == null) {
            attrs.put("cnxidaComment", "FRAMEWORK, INCORPORATED");
        } else {
            attrs.put("cnxidaComment", Kcomment);
        }

        if (KhashVersion == null) // get the hash schema version # and add to LDIf file - JLM
        {
            attrs.put("cnxidaXSchemaVersion", "unknown-1");
        } else {
            attrs.put("cnxidaXSchemaVersion", KhashVersion);
        }

        if (Kdescription == null) {
            attrs.put("cnxidaDesc", "XSD Knowledge Entry for Vendor: " + Kvendor +
                    ", Type: " + Ktype + ", Model: " + Kmodel +
                    ", OS: " + Kos);
        } else {
            attrs.put("cnxidaDesc", Kdescription);
        }

        attrs.put("cnxidaType", "XSD");
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
                LDIFOUT.write("# FRAMEWORK IRR Produce Knowledge LDIF.\n");
                LDIFOUT.write("# Start Time: " + CurrentTimeStamp.get() + "\n");
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("\n");
            }
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Exception opening LDIF Output File. " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_LDIF_OUTPUT_FAILURE);
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
                System.exit(EXIT_IRR_KNOWLEDGE_LDIF_OUTPUT_FAILURE);
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
                System.exit(EXIT_IRR_KNOWLEDGE_LDIF_OUTPUT_CLOSE_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // ****************************************
        // Show Completion.
        System.out.println(MP + "Knowledge Entry in LDIF written successfully.");

    } // End of perform Method.


    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRProduceKnowledgeLDIF
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

        VAR.add(new idxArgVerificationRules("xsdfile",
                true, true));

        VAR.add(new idxArgVerificationRules("ldifout",
                true, true));

        VAR.add(new idxArgVerificationRules("customer",
                true, true));

        VAR.add(new idxArgVerificationRules("vendor",
                true, true));

        VAR.add(new idxArgVerificationRules("type",
                true, true));

        VAR.add(new idxArgVerificationRules("model",
                true, true));

        VAR.add(new idxArgVerificationRules("os",
                true, true));

        VAR.add(new idxArgVerificationRules("description",
                false, true));

        VAR.add(new idxArgVerificationRules("comment",
                false, true));

        VAR.add(new idxArgVerificationRules("trimws",
                false, false));

        VAR.add(new idxArgVerificationRules("trimnl",
                false, false));

        VAR.add(new idxArgVerificationRules("compress",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
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
        xsdInfileName = ((String) Zin.getValue("xsdfile")).trim();
        System.out.println(MP + "XSDFile:[" + xsdInfileName + "]");

        ldifoutputfilename = ((String) Zin.getValue("ldifout")).trim();
        System.out.println(MP + "LDIF OutPut File:[" + ldifoutputfilename + "]");

        Kcustomer = ((String) Zin.getValue("customer")).trim();
        System.out.println(MP + "Customer:[" + Kcustomer + "]");

        Kvendor = ((String) Zin.getValue("vendor")).trim();
        System.out.println(MP + "Vendor:[" + Kvendor + "]");

        Ktype = ((String) Zin.getValue("type")).trim();
        System.out.println(MP + "Device Type:[" + Ktype + "]");

        Kmodel = ((String) Zin.getValue("model")).trim();
        System.out.println(MP + "Device Model:[" + Kmodel + "]");

        Kos = ((String) Zin.getValue("os")).trim();
        System.out.println(MP + "Device Operating System:[" + Kos + "]");

        if (Zin.doesNameExist("comment")) {
            Kcomment = ((String) Zin.getValue("comment")).trim();
        }

        if (Zin.doesNameExist("description")) {
            Kdescription = ((String) Zin.getValue("description")).trim();
        }


        // ************************************************
        // Show Operational Parameters
        if (TRIM_WHITESPACE) {
            System.out.println(MP + "Will Trim all Whitespace from XSD.");
        } else {
            System.out.println(MP + "Will NOT Trim all Whitespace from XSD.");
        }

        if (TRIM_NEWLINES) {
            System.out.println(MP + "Will Not Preserve NewLines in XSD.");
        } else {
            System.out.println(MP + "Will Preserve NewLines in XSD.");
        }

        if (COMPRESS) {
            System.out.println(MP + "Will compress the XSD.");
        } else {
            System.out.println(MP + "Will NOT compress the XSD.");
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
        IRRProduceKnowledgeLDIF FUNCTION = new IRRProduceKnowledgeLDIF(
                xsdInfileName,
                ldifoutputfilename,
                Kcustomer,
                Kvendor,
                Ktype,
                Kmodel,
                Kos,
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
            System.err.println(MP + "IRR Exception Performing IRRProduceKnowledgeLDIF.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } /// End of Main.

} // End of Class IRRProduceKnowledgeLDIF
