package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxManageContext;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;

import java.util.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to Backup the Entire Directory or a portion of
 * the IRR Directory to an LDIF format output file on disk.
 * Backup output conforms to the LDIF Specification: RFC2849.
 * <br>
 * <b>Usage:</b><br>
 * IRRBackupNew &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --outfile
 * 	Specify Full Output file Path for backup to be written.
 * 	Will write a &lt;outfile&gt;.ldif and &lt;outfile&gt;.schema.xml files.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --outputbufsize
 * 	Specify Output Buffer Size, Default 128KB.
 * --filter
 * 	Specify Search Filter, default is (objectclass=*).
 * --sourcedn
 * 	Specify Full DN of Source Entry to be used as base, default is [ROOT].
 * --withchildren
 * 	Specify Source Entry and All Children Entries to be read for backup.
 * --withschema
 * 	Specify Source Schema will be read for backup as well as data entries.
 * --append
 * 	Specify to Append LDIF Existing Output File.
 * --notnice
 * 	Output Standard LDIF, Default will provide Nice DNs.
 * --ignorevendorobjects
 *      Specify to Ignore and Exclude the VendorObjects Tree from Backup, Default: False.
 * --readerthreads
 *      Specify Number of Object Scope Reader Threads, Default is 1, Maximum is 3.
 * --dnfilterfilename
 *      Specify Filename containing Filters to assign a specific reader thread by DN to improve backup performance.
 * --segmentoutput
 *      Specify Segment Output Size as the number of DN Entries per segment.
 * --version
 * 	Display Version information and exit.
 * --verbose
 *     Output Additional Information.
 * --debug
 *     Output Debugging Information.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

public class IRRBackupNew implements idxCMDReturnCodes {

    private static String VERSION = "Version: 4.4 2005-10-05, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRBackupNew: ";

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String OUTPUT_FILENAME = null;

    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean BACKUP_WITH_CHILDREN = false;
    private static boolean APPEND = false;
    private static boolean NICE = true;
    private static boolean IGNORE_VENDOR_OBJECTS = false;

    private static int OBUFSIZE = 0;

    private static int DN_SEGMENTATION_OUTPUT = 0;

    private static String DN_THREAD_FILTER_CLASSIFIER_FILENAME = null;

    private static final int DEFAULT_READER_THREADS = 1;
    private static final int MAX_READER_THREADS = 3;
    private static int READER_THREADS = DEFAULT_READER_THREADS;

    private static boolean ExitOnException = false;

    public static final String END_OF_DATA = "<EOD/>";

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRbackupNew <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--outfile ");
        System.err.println("\tSpecify Full Output file Path for backup to be written.");
        System.err.println("\tWill write a <outfile>.ldif File.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--filter");
        System.err.println("\tSpecify Search Filter, default is (objectclass=*).");
        System.err.println(MP + "--sourcedn ");
        System.err.println("\tSpecify Full DN of Source Entry to be used as base, default is [ROOT].");

        System.err.println(MP + "--withchildren ");
        System.err.println("\tSpecify Backup of Source Entry and all Children.");

        System.err.println(MP + "--append ");
        System.err.println("\tSpecify to Append LDIF Output to Existing File.");
        System.err.println(MP + "--notnice ");
        System.err.println("\tSpecify to Output Standard LDIF Output, default is Nice DNs.");
        System.err.println(MP + "--ignorevendorobjects ");
        System.err.println("\tSpecify to Ignore and Exclude the VendorObjects Tree from Backup, Default: False.");

        System.err.println(MP + "--outputbufsize ");
        System.err.println("\tSpecify LDIF Output Buffer Size.");

        System.err.println(MP + "--readerthreads ");
        System.err.println("\tSpecify Number of Object Scope Reader Threads, Default is 1, Maximum is 3.");

        System.err.println(MP + "--dnfilterfilename ");
        System.err.println("\tSpecify Filename containing Filters to assign a specific reader thread by DN to improve backup performance.");

        System.err.println(MP + "--segmentoutput ");
        System.err.println("\tSpecify Segment Output Size as the number of DN Entries per segment.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--debug");
        System.err.println("\tDisplay Debugging Information.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /*
     * Default Constructor.
     */
    public IRRBackupNew() {
    } // End of Constructor.      

    /**
     * IRRBackupNew Contructor class driven from
     * Main or other Class Caller.
     *
     * @param IRRHost  Source IRR LDAP URL.
     * @param IRRPrincipal  Source IRR Principal.
     * @param IRRCredentials  Source IRR Credentials.
     * @param ENTRY_SOURCE_DN  Source DN Starting Base for Backup.
     * @param SearchFilter  Search Filter.
     * @param OUTPUT_FILENAME  Destination Output Filename.
     * @param OBUFSIZE     BufferedWriter Output Buffer Size.
     * @param READER_THREADS number of Reader Threads
     * @param DN_THREAD_FILTER_CLASSIFIER_FILENAME
     * @param DN_SEGMENTATION_OUTPUT
     * @param BACKUP_WITH_CHILDREN Indicate if Children should be included in backup.
     * @param APPEND Indicate if Output Should be Appended.
     * @param NICE Indicate if Nice Output.
     * @param IGNORE_VENDOR_OBJECTS
     * @param VERBOSE Indicate Verbosity.
     * @param DEBUG Indicate DEBUGGING.
     * @param ExitOnException Indicate Exit on Exceptions.
     */
    public IRRBackupNew(String IRRHost,
                        String IRRPrincipal,
                        String IRRCredentials,
                        String ENTRY_SOURCE_DN,
                        String SearchFilter,
                        String OUTPUT_FILENAME,
                        int OBUFSIZE,
                        int READER_THREADS,
                        int DN_SEGMENTATION_OUTPUT,
                        String DN_THREAD_FILTER_CLASSIFIER_FILENAME,
                        boolean BACKUP_WITH_CHILDREN,
                        boolean APPEND,
                        boolean NICE,
                        boolean IGNORE_VENDOR_OBJECTS,
                        boolean VERBOSE,
                        boolean DEBUG,
                        boolean ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        this.IRRHost = IRRHost;
        this.IRRPrincipal = IRRPrincipal;
        this.IRRCredentials = IRRCredentials;
        this.ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        this.SearchFilter = SearchFilter;
        this.OUTPUT_FILENAME = OUTPUT_FILENAME;
        this.OBUFSIZE = OBUFSIZE;
        this.BACKUP_WITH_CHILDREN = BACKUP_WITH_CHILDREN;
        this.APPEND = APPEND;
        this.NICE = NICE;
        this.IGNORE_VENDOR_OBJECTS = IGNORE_VENDOR_OBJECTS;
        this.VERBOSE = VERBOSE;
        this.DEBUG = DEBUG;
        this.ExitOnException = ExitOnException;
        this.READER_THREADS = READER_THREADS;

        this.DN_SEGMENTATION_OUTPUT = DN_SEGMENTATION_OUTPUT;

        this.DN_THREAD_FILTER_CLASSIFIER_FILENAME =
                DN_THREAD_FILTER_CLASSIFIER_FILENAME;
    } // End of Constructor for IRRBackupNew.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws Exception       for any unrecoverable errors during function.
     * @throws idxIRRException for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // ********************************************
        // We shall start two threads
        // A Producer or a Thread that will walk the
        // Directory Tree and a Output Thread that
        // will perform the Directory Object read
        // and write the LDIF Output for backup.
        //
        // ********************************************

        // ********************************************
        // Establish Several Directory Connections for
        // Walking.
        idxManageContext IRRSource_WT = obtainManagedContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRBackupWalkerThread");

        // ********************************************
        // Establish Status Objects.
        IRRBackupStatusNew WriterStatus = new IRRBackupStatusNew();

        // *************************************************
        // Set up Object Stacks for Thread Communication.
        // These stacks are Thread Safe Objects using
        // synchronized access methods to and from the stack.
        //
        // This Stack will be used Among the Reader and Output Threads.
        CircularObjectStack cos_among_readers_output = new CircularObjectStack();

        // ***************************************************
        // Set up the Maximum Allowed, just as a precautionary
        // measure,since out of the box we have 4 threads including
        // the main control thread.  So we could have a Max of
        // 7 Threads to do all our work.
        if (READER_THREADS > IRRBackupNew.MAX_READER_THREADS) {
            READER_THREADS = IRRBackupNew.MAX_READER_THREADS;
        }
        System.out.println(MP + "Number of Reader Threads Being Created: " + READER_THREADS + ".");

        // *********************************************************
        // This Stack Will be used Among Walker and Reader Threads.
        // We create an Array of Stacks, one for each Reader Thread.
        CircularObjectStack cos_among_walker_readers[] = new CircularObjectStack[READER_THREADS];

        // ********************************************
        // Set up and Array of Status Object.
        IRRBackupStatusNew ReaderStatus[] = new IRRBackupStatusNew[READER_THREADS];

        // ********************************************
        // Set up an Array of Directory Contexts.
        idxManageContext IRRSource_RT[] = new idxManageContext[READER_THREADS];

        // ********************************************
        // Set up an Array of Reader Threads.
        IRRBackupReaderThread RT[] =
                new IRRBackupReaderThread[READER_THREADS];

        // ********************************************
        // Now Loop to Start Threads.
        for (int reader_instance_number = 0; reader_instance_number < READER_THREADS;
             reader_instance_number++) {

            // ***********************************
            // Establish an Array Stack for 
            // Walker to talk to Reader.
            cos_among_walker_readers[reader_instance_number]
                    = new CircularObjectStack();

            // ***********************************
            // Establish new Status Object.
            ReaderStatus[reader_instance_number] = new IRRBackupStatusNew();

            // ********************************************
            // Establish Several Directory Connection for
            // Reader.
            IRRSource_RT[reader_instance_number] = obtainManagedContext(IRRHost,
                    IRRPrincipal,
                    IRRCredentials,
                    "IRRBackupReaderThread");

            // ***************************************
            // Start the Reader Thread.
            System.out.println(MP + "Starting Backup Element Reader Thread, Instance:[" +
                    (reader_instance_number + 1) + "]");
            RT[reader_instance_number] =
                    new IRRBackupReaderThread(
                            reader_instance_number,
                            cos_among_walker_readers[reader_instance_number],
                            cos_among_readers_output,
                            ReaderStatus[reader_instance_number],
                            IRRSource_RT[reader_instance_number],
                            ENTRY_SOURCE_DN,
                            VERBOSE,
                            DEBUG,
                            ExitOnException);

        } // End of For Loop to Establish All Readers.  

        // *****************************************
        // Start our Walker Thread.
        IRRBackupStatusNew WalkerStatus = new IRRBackupStatusNew();
        System.out.println(MP + "Starting Backup Directory Tree Walker Thread...");
        IRRBackupWalkerThread WT =
                new IRRBackupWalkerThread(cos_among_walker_readers,
                        WalkerStatus,
                        IRRSource_WT,
                        ENTRY_SOURCE_DN,
                        SearchFilter,
                        BACKUP_WITH_CHILDREN,
                        IGNORE_VENDOR_OBJECTS,
                        DN_THREAD_FILTER_CLASSIFIER_FILENAME,
                        VERBOSE,
                        DEBUG,
                        ExitOnException);

        // ******************************************
        // Check All the Reader Threads
        // to be running...
        for (int reader_instance_number = 0; reader_instance_number < READER_THREADS;
             reader_instance_number++) {
            if (RT[reader_instance_number].t.isAlive()) {
                System.out.println(MP + "Elment Reader Thread, Instance:[" +
                        (reader_instance_number + 1) + "], Running...");
            }
        } // End of For Loop to Check for Running Reader Threads.

        // ******************************************
        // Check for the Walker Thread
        // to be running...
        if (WT.t.isAlive()) {
            System.out.println(MP + "Tree Walker Thread is Running...");
        }

        // ********************************************
        // Start the Output Thread, last to go.
        System.out.println(MP + "Starting Backup LDIF Output Thread...");

        IRRBackupOutputThread OT =
                new IRRBackupOutputThread(
                        cos_among_readers_output,
                        WriterStatus,
                        ENTRY_SOURCE_DN,
                        OUTPUT_FILENAME,
                        OBUFSIZE,
                        APPEND,
                        NICE,
                        DN_SEGMENTATION_OUTPUT,
                        VERBOSE,
                        DEBUG,
                        ExitOnException);

        // ****************************
        // Check for the Output Thread
        // to be running...
        if (OT.t.isAlive()) {
            System.out.println(MP + "LDIF Output Thread is Running...");
        }

        // ***********************************************
        // Now Join and Wait for Walker Thread Completion.
        //
        try {
            System.out.println(MP + "Main Thread waiting for Walker Thread to Complete...");
            WT.t.join();
        } catch (InterruptedException e) {
            if (ExitOnException) {
                System.err.println(MP + "Main Thread Interrupted, Terminating.");
                System.exit(EXIT_IRR_BACKUP_FAILURE);
            } else {
                throw new idxIRRException(MP + "Main Thread Interrupted, Terminating.");
            }

        } // End of Exception.

        // ******************************************
        // Entering here indicates that the Walker 
        // Thread has finished, now indicate 
        // to all Reader Threads we have reached
        // End of Data.
        for (int reader_instance_number = 0; reader_instance_number < READER_THREADS;
             reader_instance_number++) {
            // ***************************************
            // Tell all Reader Threads to Finish.
            cos_among_walker_readers[reader_instance_number].push(IRRBackupNew.END_OF_DATA);
        } // End of For Loop to Send an End of Data for Each Reader Thread.

        // *******************************************
        // Now Join the Reader Threads.
        try {
            System.out.println(MP + "Main Thread waiting for Reader Threads to Complete...");

            // ********************************
            // Join all Reader Threads.
            for (int reader_instance_number = 0; reader_instance_number < READER_THREADS;
                 reader_instance_number++) {
                RT[reader_instance_number].t.join();
            }
        } catch (InterruptedException e) {
            if (ExitOnException) {
                System.err.println(MP + "Main Thread Interrupted, Terminating.");
                System.exit(EXIT_IRR_BACKUP_FAILURE);
            } else {
                throw new idxIRRException(MP + "Main Thread Interrupted, Terminating.");
            }
        } // End of Exception.

        // ******************************************
        // Entering here indicates that the Walker and
        // Reader Threads have finished, so tell the 
        // Output thread to finish off.
        // ***************************************
        // Tell the Output Thread to Finish.
        cos_among_readers_output.push(IRRBackupNew.END_OF_DATA);

        // *******************************************
        // Now Join the Output Thread.
        try {
            System.out.println(MP + "Main Thread waiting for Output Thread to Complete...");
            OT.t.join();
        } catch (InterruptedException e) {
            if (ExitOnException) {
                System.err.println(MP + "Main Thread Interrupted, Terminating.");
                System.exit(EXIT_IRR_BACKUP_FAILURE);
            } else {
                throw new idxIRRException(MP + "Main Thread Interrupted, Terminating.");
            }
        } // End of Exception.

        // ******************************************
        // Determine if all went well?
        // Check Walker Status....
        if (WalkerStatus.getError() != 0) {
            if (ExitOnException) {
                System.err.println(MP + "Exception in Walker Thread, Error Code:[" +
                        WalkerStatus.getError() + "]");
                System.exit(WalkerStatus.getError());
            } else {
                throw new idxIRRException(MP + "Exception in Walker Thread, Error Code:[" +
                        WalkerStatus.getError() + "]");
            } // End of Else.
        } // End of If.

        // ******************************************
        // Determine if all went well?
        // Check All Reader Status....
        for (int reader_instance_number = 0; reader_instance_number < READER_THREADS;
             reader_instance_number++) {
            if (ReaderStatus[reader_instance_number].getError() != 0) {
                if (ExitOnException) {
                    System.err.println(MP + "Exception in Reader Thread Instance:" +
                            (reader_instance_number + 1) + ", Error Code:[" +
                            ReaderStatus[reader_instance_number].getError() + "]");
                    System.exit(ReaderStatus[reader_instance_number].getError());
                } else {
                    throw new idxIRRException(MP + "Exception in RReader Thread Instance:" +
                            (reader_instance_number + 1) + ", Error Code:[" +
                            ReaderStatus[reader_instance_number].getError() + "]");
                } // End of Else.
            } // End of If.
        } // End of For Loop Checking all Reader Status Objects.

        // ******************************************
        // Determine if all went well?
        // Check Writer Status....
        if (WriterStatus.getError() != 0) {
            if (ExitOnException) {
                System.err.println(MP + "Exception in Writer Thread, Error Code:[" +
                        WriterStatus.getError() + "]");
                System.exit(WriterStatus.getError());
            } else {
                throw new idxIRRException(MP + "Exception in Writer Thread, Error Code:[" +
                        WriterStatus.getError() + "]");
            } // End of Else.
        } // End of If.

        // *************************************
        // Close the Walker Directory Context.
        IRRSource_WT.close();

        // *************************************
        // Close all Reader Directory Contexts.
        for (int reader_instance_number = 0; reader_instance_number < READER_THREADS;
             reader_instance_number++) {
            IRRSource_RT[reader_instance_number].close();
        } // End of Close of All Reader Directory Contexts.

        // ********************
        // Done.
        return;

    } // End of Performbackup

    /**
     * obtainManagedContext, Provides common method to obtain a Directory
     * context and setup.
     */
    private idxManageContext obtainManagedContext(String IRRHost,
                                                  String IRRPrincipal,
                                                  String IRRCredentials,
                                                  String usage) throws Exception {
        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Source Object Directory Connection to Host URL:[" + IRRHost + "]");

        idxManageContext IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                usage);

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        IRRSource.open();

        // **********************************************
        // Set and Show Current Alias Derefencing State.
        IRRSource.disableAliasDereferencing();
        //IRRSource.showAliasDereferencing();

        // **********************************************
        // Disable Factories..
        IRRSource.disableDSAEFactories();
        //IRRSource.showDSAEFactories();

        // ****************************
        // Return the Ready Context.
        return IRRSource;

    } // End of obtainManagedContext private Method.


    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRBackupNew
     */
    public static void main(String[] args) {

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
        if (Zin.doesNameExist("debug")) {
            DEBUG = true;
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

        VAR.add(new idxArgVerificationRules("outfile",
                true, true));

        VAR.add(new idxArgVerificationRules("sourcedn",
                false, true));

        VAR.add(new idxArgVerificationRules("filter",
                false, true));

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("append",
                false, false));

        VAR.add(new idxArgVerificationRules("notnice",
                false, false));

        VAR.add(new idxArgVerificationRules("outputbufsize",
                false, true));

        VAR.add(new idxArgVerificationRules("readerthreads",
                false, true));

        VAR.add(new idxArgVerificationRules("dnfilterfilename",
                false, true));

        VAR.add(new idxArgVerificationRules("segmentoutput",
                false, true));

        VAR.add(new idxArgVerificationRules("ignorevendorobjects",
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
            if (DEBUG) {
                System.out.println(MP + "IRR Password:[" + IRRCredentials + "]");
            }
        } else {
            System.out.println(MP + "Required Principal and Credentials not Specified, unable to continue.");
            Usage();
        } // End of Else.

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("withchildren")) {
            BACKUP_WITH_CHILDREN = true;
        }

        if (Zin.doesNameExist("append")) {
            APPEND = true;
        }

        if (Zin.doesNameExist("notnice")) {
            NICE = false;
        }

        if (Zin.doesNameExist("ignorevendorobjects")) {
            IGNORE_VENDOR_OBJECTS = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        OUTPUT_FILENAME = ((String) Zin.getValue("outfile")).trim();
        System.out.println(MP + "Output File:[" + OUTPUT_FILENAME + "]");

        if (Zin.doesNameExist("sourcedn")) {
            ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        }

        if (Zin.doesNameExist("filter")) {
            SearchFilter = ((String) Zin.getValue("filter")).trim();
        }

        if (Zin.doesNameExist("outputbufsize")) {
            try {
                OBUFSIZE = Integer.parseInt(
                        (String) Zin.getValue("outputbufsize"));
            } catch (Exception e) {
                System.err.println(MP + "Output Buffer Size is Invalid, Unable to Continue,\n" + e);
                System.exit(EXIT_IRR_BACKUP_FAILURE);
            } // End of Exception.

        } // End of If.

        if (Zin.doesNameExist("readerthreads")) {
            try {
                READER_THREADS = Integer.parseInt(
                        (String) Zin.getValue("readerthreads"));
            } catch (Exception e) {
                System.err.println(MP + "Reader Thread Count is Invalid, Defaulting to One Reader Thread,\n" + e);
                READER_THREADS = IRRBackupNew.DEFAULT_READER_THREADS;
            } // End of Exception.

        } // End of If.

        if (Zin.doesNameExist("segmentoutput")) {
            try {
                DN_SEGMENTATION_OUTPUT = Integer.parseInt(
                        (String) Zin.getValue("segmentoutput"));
            } catch (Exception e) {
                System.err.println(MP + "Segment Output Option is Invalid, Defaulting to No segmentation.");
                DN_SEGMENTATION_OUTPUT = 0;
            } // End of Exception.

        } // End of If.

        if (Zin.doesNameExist("dnfilterfilename")) {
            DN_THREAD_FILTER_CLASSIFIER_FILENAME = ((String) Zin.getValue("dnfilterfilename")).trim();
        }


        // ************************************************
        // Show Operational Parameters
        if (ENTRY_SOURCE_DN == null) {
            System.out.println(MP + "Source DN:[ROOT]");
            ENTRY_SOURCE_DN = "";
        } else {
            System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");
        }

        if (SearchFilter == null) {
            SearchFilter = "(objectclass=*)";
        }
        System.out.println(MP + "Search Filter: " + SearchFilter);

        if (BACKUP_WITH_CHILDREN) {
            System.out.println(MP + "Will Backup Source Children.");
        } else {
            System.out.println(MP + "Will NOT Backup any Source Children.");
        }

        if ((APPEND) && (DN_SEGMENTATION_OUTPUT <= 0)) {
            System.out.println(MP + "Will Append LDIF Output to Existing Backup File.");
        }

        if (DN_SEGMENTATION_OUTPUT > 0) {
            System.out.println(MP + "Will Segment Output with:[" +
                    DN_SEGMENTATION_OUTPUT + "] Entries Per Segment.");
        }

        if (NICE) {
            System.out.println(MP + "Will Create Nice LDIF Output, Non-Continued DNs.");
        }

        if (IGNORE_VENDOR_OBJECTS) {
            System.out.println(MP + "Will Ignore VendorObjects Tree and Exclude from backup Process.");
        }

        if (DN_THREAD_FILTER_CLASSIFIER_FILENAME != null) {
            System.out.println(MP + "Will use Reader Thread Classifier Filter File:[" +
                    DN_THREAD_FILTER_CLASSIFIER_FILENAME + "].");
        }

        // ***********************************************
        // Initialize our StopWatch to measure Duration
        // of Thread.
        StopWatch sw = new StopWatch();
        sw.start();

        // ****************************************
        // Initailize Constructor.
        IRRBackupNew FUNCTION = new IRRBackupNew();
        ExitOnException = true;

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRbackup.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        sw.stop();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Overall Duration: " + sw.getElapsedTimeString());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} ///:~ End of Class IRRbackup