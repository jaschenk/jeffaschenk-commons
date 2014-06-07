
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
 * line parameters to load a new Framework Knowledge Entry into the
 * Directory.   This module will also provide the ability to build new
 * vendorobject trees for new vendors, types, models and Operating Systems.
 * <p/>
 * <br>
 * <b>Usage:</b><br>
 * IRRUploadKnowledge &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --xsdfile
 * 	Specify Full Path of XSD (XML SCHEMA) File.
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


public class IRRUploadKnowledge implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2001-09-07, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRUploadKnowledge: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String xsdInfileName = null;

    private static String Kcustomer = null;
    private static String Kvendor = null;
    private static String Ktype = null;
    private static String Kmodel = null;
    private static String Kos = null;

    private static boolean CREATE_KTREE = false;
    private static boolean OVERWRITE_KENTRY = false;
    private static boolean TRIM_WHITESPACE = false;
    private static boolean TRIM_NEWLINES = false;

    private static String Kcomment = null;
    private static String Kdescription = null;

    private static boolean VERBOSE = false;
    private static boolean COMPRESS = false;

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
        System.err.println(MP + "UploadKnowledge <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
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
        System.err.println(MP + "--createktree");
        System.err.println("\tSpecify to Create Knowledge Tree Containers if they do not exist.");
        System.err.println(MP + "--overwrite");
        System.err.println("\tSpecify Existing Knowledge Entry will be overwritten.");

        System.err.println(MP + "--trimws");
        System.err.println("\tSpecify to Trim all Whitespace from XSD file.");

        System.err.println(MP + "--trimnl");
        System.err.println("\tSpecify not to preserver all NewLines in XSD file.");

        System.err.println(MP + "--compress");
        System.err.println("\tSpecify to Compress the XSD file and load as a Binary Attribute in the Directory.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of class.

    /**
     * IRRUploadKnowledge Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _xsdInfileName  XML Input File.
     * @param _Kcustomer  Customer of Entry.
     * @param _Kvendor  Vendor of Entry.
     * @param _Ktype  Vendor Type of Entry.
     * @param _Kmodel  Vendor Model of Entry.
     * @param _Kos  Vendor OS of Entry.
     * @param _Kcomment  Comment to be added to Entry.
     * @param _Kdescription  Description to be added to Entry.
     * @param _CREATE_KTREE Indicate if Knowledge Tree should be Built.
     * @param _OVERWRITE_KENTRY Indicate if Existing Entry will be overwritten.
     * @param _TRIM_WHITESPACE Indicate to Trim all Whitspace from Input File.
     * @param _TRIM_NEWLINES Indicate to Trim all NewLines from Input File.
     * @param _COMPRESS Indicate COMPRESS Attribute to Binary Form.
     * @param _VERBOSE Indicate Verbosity.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRUploadKnowledge(String _IRRHost,
                              String _IRRPrincipal,
                              String _IRRCredentials,
                              String _xsdInfileName,
                              String _Kcustomer,
                              String _Kvendor,
                              String _Ktype,
                              String _Kmodel,
                              String _Kos,
                              String _Kcomment,
                              String _Kdescription,
                              boolean _CREATE_KTREE,
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
        xsdInfileName = _xsdInfileName;
        Kcustomer = _Kcustomer;
        Kvendor = _Kvendor;
        Ktype = _Ktype;
        Kmodel = _Kmodel;
        Kos = _Kos;
        Kcomment = _Kcomment;
        Kdescription = _Kdescription;
        CREATE_KTREE = _CREATE_KTREE;
        OVERWRITE_KENTRY = _OVERWRITE_KENTRY;
        TRIM_WHITESPACE = _TRIM_WHITESPACE;
        TRIM_NEWLINES = _TRIM_NEWLINES;
        COMPRESS = _COMPRESS;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRUploadKnowledge.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors
     *                         during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "UploadKnowledge");

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
        idxIRRdit dit = new idxIRRdit();
        util.setVerbose(VERBOSE);

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
        // Now, start check to see if we have
        // a valid Knowledge tree to implant our
        // Entry.
        //

        // *****************************************
        // First make sure the customer exists.
        //
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, Kcustomer)) {
                System.out.println(MP + "Customer DN [" + Kcustomer + "] is valid.");
            } else {
                if (ExitOnException) {
                    System.err.println(MP + "Customer DN [" + Kcustomer + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please use a valid Customer base.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Customer DN [" + Kcustomer + "] is not valid.");
                }

            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ****************************************************
        // Second make sure the Framework Vendorobjects
        // container exists.
        //
        KentryDN = "ou=vendorobjects,ou=framework," + Kcustomer;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Framework DN [" + KentryDN + "] is valid.");
            } else {
                System.err.println(MP + "Framework DN [" + KentryDN + "] is not valid.");
                System.err.println(MP + "Unable to continue, please use a valid Customer base.");
                if (ExitOnException) {
                    System.err.println(MP + "Framework DN [" + KentryDN + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please use a valid Customer base.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Framework DN [" + KentryDN + "] is not valid.");
                }

            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }

        } // End of Exception

        // ****************************************************
        // Now make sure the Vendor Container Exists
        //
        KentryDN = "ou=" + Kvendor + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Vendor [" + Kvendor + "] is valid.");
            } else if (CREATE_KTREE) {
                System.out.println(MP + "Vendor [" + Kvendor + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Vendor [" + Kvendor + "].");
                } else {
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Vendor [" + Kvendor + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Vendor [" + Kvendor + "].");
                    }
                } // End of Else.
            } // End of Else if.
            else {
                if (ExitOnException) {
                    System.err.println(MP + "Vendor [" + Kvendor + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please respecify or use --createktree option.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Vendor [" + Kvendor + "] is not valid.");
                }
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ****************************************************
        // Now make sure the Device Type Container Exists
        //
        KentryDN = "ou=" + Ktype + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Device Type [" + Ktype + "] is valid.");
            } else if (CREATE_KTREE) {
                System.out.println(MP + "Device Type [" + Ktype + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Device Type [" + Ktype + "].");
                } else {
                    System.out.println(MP + "Add Unsuccessful for Device Type [" + Ktype + "].");
                    System.err.println(MP + "Unable to continue.");
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Device Type [" + Ktype + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Device Type [" + Ktype + "].");
                    }
                } // End of Else.
            } // End of Else if.
            else {
                if (ExitOnException) {
                    System.err.println(MP + "Device Type [" + Ktype + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please respecify or use --createktree option.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Device Type [" + Ktype + "] is not valid.");
                }
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ****************************************************
        // Now make sure the Device Model Container Exists
        //
        KentryDN = "ou=" + Kmodel + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Device Model [" + Kmodel + "] is valid.");
            } else if (CREATE_KTREE) {
                System.out.println(MP + "Device Model [" + Kmodel + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Device Model [" + Kmodel + "].");
                    // ************************************
                    // Ok, we need to add several children
                    // Assume all is well....
                    dit.CreateOUContainersForVCObjectTree(IRRSource.irrctx,
                            KentryDN);

                } else {
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Device Model [" + Kmodel + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Device Model [" + Kmodel + "].");
                    }
                } // End of Else.
            } // End of Else if.
            else {
                if (ExitOnException) {
                    System.err.println(MP + "Device Model [" + Kmodel + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please respecify or use --createktree option.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Device Model [" + Kmodel + "] is not valid.");
                }
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ************************************************************
        // Now make sure the Device Operating System Container Exists
        //
        KentryDN = "ou=" + Kos + "," + KentryDN;
        try {
            if (util.DoesEntryExist(IRRSource.irrctx, KentryDN)) {
                System.out.println(MP + "Device OS [" + Kos + "] is valid.");
            } else if (CREATE_KTREE) {
                System.out.println(MP + "Device OS [" + Kos + "] was not found, but attempting add.");
                if (dit.CreateOUContainer(IRRSource.irrctx, KentryDN)) {
                    System.out.println(MP + "Add Successful for Device OS [" + Kos + "].");
                    // ************************************
                    // Ok, we need to add several children
                    // Assume all is well....
                    dit.CreateOUContainersForVCObjectTree(IRRSource.irrctx,
                            KentryDN);
                } else {
                    if (ExitOnException) {
                        System.out.println(MP + "Add Unsuccessful for Device OS [" + Kos + "].");
                        System.err.println(MP + "Unable to continue.");
                        System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Add Unsuccessful for Device OS [" + Kos + "].");
                    }
                } // End of Else.
            } // End of Else if.
            else {
                if (ExitOnException) {
                    System.err.println(MP + "Device OS [" + Kos + "] is not valid.");
                    System.err.println(MP + "Unable to continue, please use a valid Customer base.");
                    System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                } else {
                    throw new idxIRRException(MP + "Device OS [" + Kos + "] is not valid.");
                }
            } // End of Else.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception

        // ************************************************************
        // Now make sure the our Final Knowledge Container Exists
        //
        try {
            if (!util.DoesEntryExist(IRRSource.irrctx, "ou=" + "knowledge" + "," + KentryDN)) {
                if (CREATE_KTREE) {
                    System.out.println(MP + " Knowledge Container was not found, but attempting add.");
                    if (dit.CreateOUContainersForVCObjectTree(IRRSource.irrctx, KentryDN)) {
                        System.out.println(MP + "Add Successful for Knowledge Container.");
                    } else {
                        if (ExitOnException) {
                            System.out.println(MP + "Add Unsuccessful for Knowledge Container.");
                            System.err.println(MP + "Unable to continue.");
                            System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
                        } else {
                            throw new idxIRRException(MP + "Add Unsuccessful for Knowledge Container.");
                        }
                    } // End of Else.
                } // End of Else if.
                else {
                    if (ExitOnException) {
                        System.err.println(MP + "Knowledge Container[" + "ou=" + "knowledge" + "," + KentryDN + "] was not found.");
                        System.err.println(MP + "Unable to continue, please respecify or use --createktree option.");
                        System.exit(EXIT_IRR_KNOWLEDGE_INVALID_TREE);
                    } else {
                        throw new idxIRRException(MP + "Knowledge Container[" + "ou=" + "knowledge" + "," + KentryDN + "] was not found.");
                    }
                } // End of Else.
            } // End of If.
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception encountered " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_FAILURE);
            } else {
                throw e;
            }
        } // End of Exception


        // *****************************************
        // Bind Entry to the Directory.
        // (That's an Add for LDAPies...)
        //
        System.out.println(MP + "Formulating Knowledge Entry.");
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
        attrs.put("commonname", "kentry");

        if (COMPRESS) {
            attrs.put("cnxidaXCompressionFormat", "gzip");
        }

        if (Kcomment == null) {
            attrs.put("cnxidaComment", "FRAMEWORK, INCORPORATED");
        } else {
            attrs.put("cnxidaComment", Kcomment);
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

        attrs.put("cnxidaInstallBy", IRRPrincipal);

        // Perform bind
        System.out.println(MP + "Attempting Knowledge Entry Add.");
        try {

            if (attrs.size() == 0) {
                if (ExitOnException) {
                    System.err.println(MP + "Knowledge Entry [" + KentryDN + "], contains no Attributes.");
                    System.exit(EXIT_IRR_GMD_FAILURE);
                } else {
                    System.err.println(MP + "Knowledge Entry [" + KentryDN + "], contains no Attributes.");
                    throw new idxIRRException("Knowledge Entry [" +
                            KentryDN +
                            "], contains no Attributes, unable to continue.");
                } // End of If Exit on Exception.

            } // end of If.

            IRRSource.irrctx.bind(KentryDN, null, attrs);
        } catch (NameAlreadyBoundException e) {
            // ****************************************************
            // Ok, we caught ourselves adding an existing entry.
            // If our OVERWRITE Flag is Set, simple Rebind the Entry.
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
                        System.err.println(MP + "Knowledge Entry [" + KentryDN + "] Modification Exception, " + emod);
                        System.exit(EXIT_IRR_KNOWLEDGE_ENTRY_MOD_FAILURE);
                    } else {
                        System.err.println(MP + "Knowledge Entry [" + KentryDN + "] was Not Found, " + emod);
                        throw emod;

                    } // End of If Exit on Exception.
                } // End of exception
            } else {
                if (ExitOnException) {
                    System.err.println(MP + "Unable to Add Existing entry, since Overwrite flag was not set.");
                    System.exit(EXIT_IRR_KNOWLEDGE_ENTRY_EXISTS_FOR_ADD);
                } else {
                    throw new idxIRRException(MP + "Unable to Add Existing entry, since Overwrite flag was not set.");
                } // End of If Exit on Exception.
            } // End of Else.

        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Knowledge Entry [" + KentryDN + "] Write Exception, " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_ENTRY_WRITE_FAILURE);
            } else {
                System.err.println(MP + "Knowledge Entry [" + KentryDN + "] Write Exception, " + e);
                throw e;
            } // End of If Exit on Exception.
        } // End of exception

        // ****************************************
        // Search for the entry just written.
        System.out.println(MP + "Knowledge Entry written successfully.");
        System.out.println(MP + "Now Searching for Kentry DN:[" + KentryDN + "]");
        try {
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(util.OpAttrIDs);
            ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

            idxIRROutput.PrintSearchList(
                    IRRSource.irrctx.search(KentryDN, "(objectclass=*)", ctls), KentryDN);

            System.out.println(MP + "Knowledge Entry Load successfully Completed.");

        } catch (NameNotFoundException e) {
            if (ExitOnException) {
                System.err.println(MP + "Knowledge Entry [" + KentryDN + "] was Not Found, " + e);
                System.exit(EXIT_IRR_KNOWLEDGE_ENTRY_NOTFOUND_AFTER_WRITE);
            } else {
                System.err.println(MP + "Knowledge Entry [" + KentryDN + "] was Not Found, " + e);
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

    } // End of Perform method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRUploadKnowledge
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

        VAR.add(new idxArgVerificationRules("xsdfile",
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

        VAR.add(new idxArgVerificationRules("overwrite",
                false, false));

        VAR.add(new idxArgVerificationRules("createktree",
                false, false));

        VAR.add(new idxArgVerificationRules("trimws",
                false, false));

        VAR.add(new idxArgVerificationRules("trimnl",
                false, false));

        VAR.add(new idxArgVerificationRules("compress",
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
        if (Zin.doesNameExist("createktree")) {
            CREATE_KTREE = true;
        }

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
            TRIM_NEWLINES = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        xsdInfileName = ((String) Zin.getValue("xsdfile")).trim();
        System.out.println(MP + "XSDFile:[" + xsdInfileName + "]");

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
        if (CREATE_KTREE) {
            System.out.println(MP + "Will Create Knowledge Tree for non-existing element nodes.");
        } else {
            System.out.println(MP + "Will NOT Create Knowledge Tree for non-existing element nodes.");
        }

        if (OVERWRITE_KENTRY) {
            System.out.println(MP + "Will Overwrite existing Knowledge Entry with new entry.");
        } else {
            System.out.println(MP + "Will NOT Overwrite existing Knowledge Entry with new entry.");
        }

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

        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRUploadKnowledge FUNCTION = new IRRUploadKnowledge(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                xsdInfileName,
                Kcustomer,
                Kvendor,
                Ktype,
                Kmodel,
                Kos,
                Kcomment,
                Kdescription,
                CREATE_KTREE,
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
            System.err.println(MP + "IRR Exception Performing IRRUploadKnowledge.\n" + e);
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

} // End of Class IRRUploadKnowledge
