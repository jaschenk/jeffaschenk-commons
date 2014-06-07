package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.idxLDIFReader;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to Restore the Entire, a portion (container) or even
 * a single entry and optionally all of its children to
 * the IRR Directory from a LDIF format input file on disk.
 * Backup input conforms to the LDIF Specification: RFC2849.
 * <br>
 * <b>Usage:</b><br>
 * IRRrestore &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --infile
 * 	Specify Full Input file Path for restore to be read.
 * 	Will read a &lt;infile&gt; and &lt;infile&gt;.schema.xml files.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --jar
 * 	Specify LDIF infile specifies a JAR path.
 * --restoreonlyDN
 * 	Specify to restore only current DN.
 * --schemacheck
 * 	Specify to check current and backup schema are in sync.
 * --noschemacheck
 * 	Specify not to check current and backup schema are in sync.
 * --withchildren
 * 	Specify to restore specified restoreonlyDN and all it's children.
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */


public class IRRrestore implements idxCMDReturnCodes {

    public static String VERSION = "Version: 4.4 2005-11-30, " +
            "FRAMEWORK, Incorporated.";

    public static String MP = "IRRrestore: ";

    private idxManageContext IRRDest = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;

    private static String INPUT_FILENAME = null;

    private static String RESTOREONLY_SOURCEDN = null;
    private static boolean RESTOREONLY_WITH_CHILDREN = false;
    private static boolean RESTOREONLY = false;

    private static boolean INJAR = false;

    private static boolean VERBOSE = false;

    private boolean ExitOnException = false;


    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRrestore <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--infile ");
        System.err.println("\tSpecify Full Input file Path for restore to be read.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--jar ");
        System.err.println("\tSpecify LDIF Input is located in a JAR.");

        System.err.println(MP + "--restoreonlyDN ");
        System.err.println("\tSpecify to restore only current DN.");

        System.err.println(MP + "--withchildren ");
        System.err.println("\tSpecify to restore specified restoreonlyDN and all it's children.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRrestore Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost                   Source IRR LDAP URL.
     * @param _IRRPrincipal              Source IRR Principal.
     * @param _IRRCredentials            Source IRR Credentials.
     * @param _INPUT_FILENAME            Input Filename or Input JAR Class.
     * @param _RESTOREONLY_SOURCEDN      RestoreOnly Source DN.
     * @param _INJAR                     Indicate LDIF Input in JAR.
     * @param _RESTOREONLY_WITH_CHILDREN Indicate Restore Only with Children.
     * @param _RESTOREONLY               Indicate Restore Only of Source DN.
     * @param _VERBOSE                   Indicate Verbosity.
     * @param _ExitOnException           Indicate Exit on Exceptions.
     */
    public IRRrestore(String _IRRHost,
                      String _IRRPrincipal,
                      String _IRRCredentials,
                      String _INPUT_FILENAME,
                      String _RESTOREONLY_SOURCEDN,
                      boolean _INJAR,
                      boolean _RESTOREONLY_WITH_CHILDREN,
                      boolean _RESTOREONLY,
                      boolean _VERBOSE,
                      boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        INPUT_FILENAME = _INPUT_FILENAME;
        INJAR = _INJAR;
        RESTOREONLY_SOURCEDN = _RESTOREONLY_SOURCEDN;
        RESTOREONLY_WITH_CHILDREN = _RESTOREONLY_WITH_CHILDREN;
        RESTOREONLY = _RESTOREONLY;
        VERBOSE = _VERBOSE;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRrestore.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException
     *                   for any specific IRR unrecoverable errors during function.
     * @throws Exception for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // *******************************************
        // Initialize.
        CompoundName RESTOREONLY_Name = null;
        CompoundName Incoming_Name = null;

        // ********************************************
        // Create a Compound Name for our SourceDN
        //
        if (RESTOREONLY) {
            try {
                // ***********************************************
                // Now determine if SourceDN is Valid.
                idxParseDN zSdn = new idxParseDN(RESTOREONLY_SOURCEDN);
                if (!zSdn.isValid()) {
                    if (ExitOnException) {
                        System.err.println(MP + "Restore Only Source DN [" +
                                RESTOREONLY_SOURCEDN +
                                "] is Invalid, unable to continue.");
                        System.exit(EXIT_IRR_RESTORE_FAILURE);
                    } else {
                        throw new idxIRRException(MP + "Restore Only Source DN [" +
                                RESTOREONLY_SOURCEDN +
                                "] is Invalid, unable to continue.");
                    } // End of Inner Else.
                } // End of If.

                RESTOREONLY_Name = idxNameParser.parse(RESTOREONLY_SOURCEDN);

            } catch (Exception e) {
                if (ExitOnException) {
                    System.err.println(MP + "Restore Only Source DN [" +
                            RESTOREONLY_SOURCEDN +
                            "] is Invalid, unable to continue.");
                    System.exit(EXIT_IRR_RESTORE_FAILURE);
                } else {
                    throw new idxIRRException(MP + "Restore Only Source DN [" +
                            RESTOREONLY_SOURCEDN +
                            "] is Invalid, unable to continue.");
                } // End of Inner Else.
            } // End of exception
        } // End of If.

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        if (VERBOSE) {
            System.out.println(MP + "Attempting Destination Directory Connection to Host URL:[" + IRRHost + "]");
        }

        IRRDest = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "Restore Destination");

        // ************************************************
        // Exit on all Exceptions.
        IRRDest.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRDest.open();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + e);
                System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            } else {
                throw e;
            }
        } // End of exception

        // ************************************************
        // Disable Factories.
        try {
            IRRDest.disableDSAEFactories();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // *****************************************
        // Open Input File and Start Restore Process
        if (VERBOSE) {
            if (INJAR) {
                System.out.println(MP + "Starting Directory Restore using LDIF JAR Class:[" + INPUT_FILENAME + "]");
            } else {
                System.out.println(MP + "Starting Directory Restore using LDIF Input File:[" + INPUT_FILENAME + "]");
            } // End of Else.
        } // End of Verbose.

        // *****************************************
        // Initialize.
        BufferedReader LDIFIN = null;

        int entries_exceptions = 0;
        int entries_duplicate = 0;
        int entries_skipped = 0;
        int entries_restored = 0;

        Attributes current_entry_attributes = null;
        String current_dn = null;
        String LDIFVersion = null;

        // *****************************************
        // Open up Input.
        try {
            if (INJAR) {
                // ******************************************
                // Open up the Input Stream from a JAR.
                LDIFIN = new BufferedReader(
                        new InputStreamReader(
                                ClassLoader.getSystemResourceAsStream(INPUT_FILENAME)), 16384);
            } else {

                // ******************************************
                // Open up the Input Stream from a File.
                LDIFIN = new BufferedReader(
                        new FileReader(INPUT_FILENAME), 16384);

            } // End of Else.

            // ******************************************
            // Obtain our Reader Instance with our Input.
            idxLDIFReader ldif = new idxLDIFReader(LDIFIN);

            Incoming_Name = null;
            while (ldif.hasMore()) {

                current_entry_attributes = ldif.getNextEntry();
                current_dn = ldif.getCurrentDN();

                if ((current_dn == null) ||
                        (current_dn.equals(""))) {
                    continue;
                }

                // *************************************
                // Detect the Version, if possible.
                if ((LDIFVersion == null) ||
                        (LDIFVersion.equals(""))) {
                    LDIFVersion = ldif.getVersion();
                    if ((LDIFVersion != null) &&
                            (!LDIFVersion.equals(""))) {
                        if (VERBOSE) {
                            System.out.println(MP + "LDIF VERSION Detected:[" +
                                    LDIFVersion + "]");
                        } // End of Verbose.
                    }
                } // End of LDIF Version Detection.

                // ***************************************
                // If we have a RestoreOnlyDN Specified
                // filter out the ones we do not need.
                //
                if (RESTOREONLY) {
                    try {
                        Incoming_Name = idxNameParser.parse(current_dn);
                    } catch (Exception e) {
                        System.err.println(MP + "Exception Creating incoming Compound Name " + e);
                        entries_exceptions++;
                        continue;
                    } // End of exception

                    // ****************************************************
                    // If No Children, Compare Incoming and the RestoreOnly
                    if (!RESTOREONLY_WITH_CHILDREN) {
                        if (!Incoming_Name.equals(RESTOREONLY_Name)) {
                            entries_skipped++;
                            if (VERBOSE) {
                                System.out.println(MP + "Skipping DN-> " + current_dn);
                            }
                            continue;
                        }

                    } else {
                        // **********************************************************
                        // If Children, Compare Incoming has a suffix of RestoreOnly
                        if (!Incoming_Name.endsWith(RESTOREONLY_Name)) {
                            entries_skipped++;
                            if (VERBOSE) {
                                System.out.println(MP + "Skipping DN-> " + current_dn);
                            }
                            continue;
                        }

                    } // End of Else.

                } // End of If RestoreOnly

                // ***************************************
                // Now Perform Bind the Entry.
                if (VERBOSE) {
                    System.out.println(MP + "Processing DN-> " + current_dn);
                }
                try {
                    if (current_entry_attributes.size() == 0) {
                        entries_skipped++;
                        if (VERBOSE) {
                            System.out.println(MP + "Skipping Glue Node (No Attributes) DN-> " + current_dn);
                        }
                        continue;
                    } // end of If.

                    IRRDest.irrctx.bind("\042" + current_dn + "\042",
                            null, current_entry_attributes);

                } catch (NameAlreadyBoundException e) {
                    if (VERBOSE) {
                        System.out.println(MP + "*WARNING, Unable to Restore entry, since it already Exists.");
                    }
                    entries_duplicate++;
                    continue;  // Continue While Loop.
                } catch (Exception e) {
                    if (ExitOnException) {
                        System.err.println(MP + "Error Binding Entry, unable to continue, " + e);
                        System.exit(EXIT_IRR_RESTORE_BINDING_ENTRY);
                    } else {
                        throw e;
                    }
                } // End of exception

                if (VERBOSE) {
                    System.out.println(MP + "Entry Restored.");
                }
                entries_restored++;

            } // End of While Loop.

        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Exception during processing LDIF Input. " + e);
                e.printStackTrace();
                System.exit(EXIT_IRR_RESTORE_LDIF_INPUT_FAILURE);
            } else {
                throw e;
            }
        } // End of exception


        // ***************************************
        // Close our Input File.
        try {
            LDIFIN.close();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(MP + "Exception closing LDIF Input. " + e);
                System.exit(EXIT_IRR_RESTORE_LDIF_INPUT_CLOSE_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // ***************************************
        // Close up Shop.
        if (VERBOSE) {
            System.out.println(MP + "Closing Destination Directory Context.");
        }
        try {
            IRRDest.close();
        } catch (Exception e) {
            if (ExitOnException) {
                System.err.println(e);
                System.exit(EXIT_IRR_CLOSE_FAILURE);
            } else {
                throw e;
            }
        } // End of exception

        // ****************************************
        // Show the Statistics
        if (entries_exceptions > 0) {
            System.out.println(MP + "Entry Exceptions: [" + entries_exceptions + "]");
        }
        if (entries_duplicate > 0) {
            System.out.println(MP + "Entry Duplicates: [" + entries_duplicate + "]");
        }
        if (entries_skipped > 0) {
            System.out.println(MP + "Entries Skipped: [" + entries_skipped + "]");
        }

        // ****************************************
        // Any Entries Restored?
        if (entries_restored == 0) {
            System.out.println(MP + "No Entries were Restored.");
        } else {
            System.out.println(MP + "Entries Restored: [" + entries_restored + "]");
        }

    } // End of Perform Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRrestore
     * @see IRRbackup
     */
    public static void main(String[] args) {

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

        // ****************************************
        // Send the Greeting.
        if (VERBOSE) {
            System.out.println(MP + VERSION);
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

        VAR.add(new idxArgVerificationRules("infile",
                true, true));

        VAR.add(new idxArgVerificationRules("restoreonlydn",
                false, true));

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("jar",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
                false, false));

        // *****************************
        // Deprecated.
        VAR.add(new idxArgVerificationRules("noschemacheck",
                false, false));

        VAR.add(new idxArgVerificationRules("schemacheck",
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
            RESTOREONLY_WITH_CHILDREN = true;
        }

        if (Zin.doesNameExist("jar")) {
            INJAR = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        INPUT_FILENAME = ((String) Zin.getValue("infile")).trim();
        if (!INJAR) {
            System.out.println(MP + "Input File:[" + INPUT_FILENAME + "]");
        } else {
            System.out.println(MP + "Input JAR Class:[" + INPUT_FILENAME + "]");
        } // End of Else.

        if (Zin.doesNameExist("restoreonlydn")) {
            RESTOREONLY_SOURCEDN = ((String) Zin.getValue("restoreonlydn")).trim();
        }

        // ************************************************
        // Show Operational Parameters
        if ((RESTOREONLY_SOURCEDN == null) ||
                (RESTOREONLY_SOURCEDN.equals("")) ||
                (RESTOREONLY_SOURCEDN.equals("*"))) {
            if (VERBOSE) {
                System.out.println(MP + "All Input entries will be Restored.");
            }
            RESTOREONLY_SOURCEDN = "";
            RESTOREONLY = false;
            RESTOREONLY_WITH_CHILDREN = false;
        } else {
            if (VERBOSE) {
                System.out.println(MP + "Only DN: [" + RESTOREONLY_SOURCEDN + "] " +
                        "will be restored from the LDIF Input Source.");
            } // End of Verbose.
            RESTOREONLY = true;
            if ((RESTOREONLY_WITH_CHILDREN) && (VERBOSE)) {
                System.out.println(MP + "Children will be restored for above DN.");
            }
        } // End of Else.

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRrestore FUNCTION = new IRRrestore(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                INPUT_FILENAME,
                RESTOREONLY_SOURCEDN,
                INJAR,
                RESTOREONLY_WITH_CHILDREN,
                RESTOREONLY,
                VERBOSE,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRrestore.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

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

} // End of Class IRRrestore
