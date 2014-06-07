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
 * line parameters to Backup the Entire Directory or a portion of
 * the IRR Directory to an LDIF format output file on disk.
 * Backup output conforms to the LDIF Specification: RFC2849.
 * <br>
 * <b>Usage:</b><br>
 * IRRbackup &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * @version 1.0 $Revision
 * Developed 2001-2002
 */

public class IRRbackup implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2002-01-22, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRbackup: ";

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String OUTPUT_FILENAME = null;

    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean BACKUP_WITH_CHILDREN = false;
    private static boolean BACKUP_SCHEMA = false;
    private static boolean APPEND = false;
    private static boolean NICE = true;

    private static int OBUFSIZE = 0;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    private PipedWriter BPQout;
    private PipedReader BPQin;


    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRbackup <Required Parameters> <Optional Parameters>");

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
        System.err.println("\tWill write a <outfile>.ldif and <outfile>.schema.xml files.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--filter");
        System.err.println("\tSpecify Search Filter, default is (objectclass=*).");
        System.err.println(MP + "--sourcedn ");
        System.err.println("\tSpecify Full DN of Source Entry to be used as base, default is [ROOT].");

        System.err.println(MP + "--withchildren ");
        System.err.println("\tSpecify Backup of Source Entry and all Children.");

        System.err.println(MP + "--withschema ");
        System.err.println("\tSpecify Backup of Source Schema as well.");
        System.err.println(MP + "--append ");
        System.err.println("\tSpecify to Append LDIF Output to Existing File.");
        System.err.println(MP + "--notnice ");
        System.err.println("\tSpecify to Output Standard LDIF Output, default is Nice DNs.");

        System.err.println(MP + "--outputbufsize ");
        System.err.println("\tSpecify LDIF Output Buffer Size.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--debug");
        System.err.println("\tDisplay Debugging Information.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRbackup Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN  Source DN Starting Base for Backup.
     * @param _SearchFilter  Search Filter.
     * @param _OUTPUT_FILENAME  Destination Output Filename.
     * @param _OBUFSIZE     BufferedWriter Output Buffer Size.
     * @param _BACKUP_WITH_CHILDREN Indicate if Children should be included in backup.
     * @param _BACKUP_SCHEMA Indicate if schema should be included in backup.
     * @param _APPEND Indicate if Output Should be Appended.
     * @param _NICE Indicate if Nice Output.
     * @param _VERBOSE Indicate Verbosity.
     * @param _DEBUG Indicate DEBUGGING.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRbackup(String _IRRHost,
                     String _IRRPrincipal,
                     String _IRRCredentials,
                     String _ENTRY_SOURCE_DN,
                     String _SearchFilter,
                     String _OUTPUT_FILENAME,
                     int _OBUFSIZE,
                     boolean _BACKUP_WITH_CHILDREN,
                     boolean _BACKUP_SCHEMA,
                     boolean _APPEND,
                     boolean _NICE,
                     boolean _VERBOSE,
                     boolean _DEBUG,
                     boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        ENTRY_SOURCE_DN = _ENTRY_SOURCE_DN;
        SearchFilter = _SearchFilter;
        OUTPUT_FILENAME = _OUTPUT_FILENAME;
        OBUFSIZE = _OBUFSIZE;
        BACKUP_WITH_CHILDREN = _BACKUP_WITH_CHILDREN;
        BACKUP_SCHEMA = _BACKUP_SCHEMA;
        APPEND = _APPEND;
        NICE = _NICE;
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRbackup.

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
        // Establish Status Objects.
        IRRbackupStatus WalkerStatus = new IRRbackupStatus();
        IRRbackupStatus WriterStatus = new IRRbackupStatus();

        // ********************************************
        // Set up a PIPE for Thread Communication.
        BPQout = new PipedWriter();
        try {
            BPQin = new PipedReader(BPQout);
        } catch (IOException e) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Obtain PipedReader for Thread Communications, Terminating");
                System.exit(EXIT_IRR_BACKUP_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Obtain PipeReader for Thread Communcations, Terminating.");
            }
        } // End of Exception.

        System.out.println(MP + "Starting Backup Object Obtainer and LDIF Output Thread...");
        IRRbackupOutput OT;
        OT = new IRRbackupOutput(BPQin,
                WriterStatus,
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                SearchFilter,
                OUTPUT_FILENAME,
                OBUFSIZE,
                BACKUP_WITH_CHILDREN,
                BACKUP_SCHEMA,
                APPEND,
                NICE,
                VERBOSE,
                DEBUG,
                ExitOnException);

        System.out.println(MP + "Starting Backup Directory Tree Walker Thread...");
        IRRbackupWalker WT;
        WT = new IRRbackupWalker(BPQout,
                WalkerStatus,
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                SearchFilter,
                OUTPUT_FILENAME,
                OBUFSIZE,
                BACKUP_WITH_CHILDREN,
                BACKUP_SCHEMA,
                APPEND,
                NICE,
                VERBOSE,
                DEBUG,
                ExitOnException);
        // ******************************************
        // Show Status.
        //
        if (OT.t.isAlive()) {
            System.out.println(MP + "Object Obtainer and LDIF Output Thread is Running...");
        }
        if (WT.t.isAlive()) {
            System.out.println(MP + "Tree Walker Thread is Running...");
        }


        // ******************************************
        // Now Join and Wait for Completion.
        //
        try {
            System.out.println(MP + "Main Thread waiting for Worker Threads to Complete...");

            OT.t.join();
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
        // Determine if all went well?
        //
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

        // ********************
        // Done.
        return;

    } // End of Performbackup

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRbackup
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
        if (Zin.doesNameExist("debug")) {
            DEBUG = true;
        }

        // ***************************************
        // Show Arguments if Verbose Selected.
        if (DEBUG) {
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

        VAR.add(new idxArgVerificationRules("outfile",
                true, true));

        VAR.add(new idxArgVerificationRules("sourcedn",
                false, true));

        VAR.add(new idxArgVerificationRules("filter",
                false, true));

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("withschema",
                false, false));

        VAR.add(new idxArgVerificationRules("append",
                false, false));

        VAR.add(new idxArgVerificationRules("notnice",
                false, false));

        VAR.add(new idxArgVerificationRules("outputbufsize",
                false, true));

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

        if (Zin.doesNameExist("withschema")) {
            BACKUP_SCHEMA = true;
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

        if (BACKUP_SCHEMA) {
            System.out.println(MP + "Will Backup IRR Directory Schema in XML Format.");
        } else {
            System.out.println(MP + "Will NOT Backup IRR Directory Schema.");
        }

        if (APPEND) {
            System.out.println(MP + "Will Append LDIF Output to Existing Backup File.");
        }

        if (NICE) {
            System.out.println(MP + "Will Create Nice LDIF Output, Non-Continued DNs.");
        }

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRbackup FUNCTION = new IRRbackup(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                SearchFilter,
                OUTPUT_FILENAME,
                OBUFSIZE,
                BACKUP_WITH_CHILDREN,
                BACKUP_SCHEMA,
                APPEND,
                NICE,
                VERBOSE,
                DEBUG,
                true);

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
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRbackup


class IRRbackupStatus {
    /**
     * IRRbackupStatus
     * Class to provide Status class for Backup Thread.
     */
    private int BackupError = 0;

    IRRbackupStatus() {
    } // Contructor.

    /**
     * Set Error Indicator Value.
     *
     * @param _x Thread Error Code.
     */
    public void setError(int _x) {
        BackupError = _x;
    }

    /**
     * Get Error Indicator Value.
     *
     * @return int Thread Error Code.
     */
    public int getError() {
        return (BackupError);
    }

} // End of Class IRRbackupStatus


/**
 * IRRbackupWalker
 * Class to run Walker Thread.
 */
class IRRbackupWalker implements Runnable, idxCMDReturnCodes {

    /**
     * IRRbackupWalker
     * Class to provide Walker interface to Level Searches.
     */
    Thread t;

    private BufferedWriter BWQ;

    private IRRbackupStatus WalkerStatus;

    private static String MP = "IRRbackupTreeWalker: ";

    private static idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String OUTPUT_FILENAME = null;

    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean BACKUP_WITH_CHILDREN = false;
    private static boolean BACKUP_SCHEMA = false;
    private static boolean APPEND = false;
    private static boolean NICE = true;

    private static int OBUFSIZE = 0;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    /**
     * IRRbackupWalker Contructor class driven.
     *
     * @param BPQout          Pipe Queue.
     * @param _WalkerStatus Object.
     * @param _IRRHost          Source IRR LDAP URL.
     * @param _IRRPrincipal          Source IRR Principal.
     * @param _IRRCredentials          Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN          Source DN Starting Base for Backup.
     * @param _SearchFilter          Search Filter.
     * @param _OUTPUT_FILENAME          Destination Output Filename.
     * @param _OBUFSIZE             BufferedWriter Output Buffer Size.
     * @param _BACKUP_WITH_CHILDREN         Indicate if Children should be included in backup.
     * @param _BACKUP_SCHEMA         Indicate if schema should be included in backup.
     * @param _APPEND         Indicate if Output Should be Appended.
     * @param _NICE         Indicate if Nice Output.
     * @param _VERBOSE         Indicate Verbosity.
     * @param _DEBUG         Indicate DEBUGGING.
     * @param _ExitOnException         Indicate Exit on Exceptions.
     */
    IRRbackupWalker(Writer BPQout,
                    IRRbackupStatus _WalkerStatus,
                    String _IRRHost,
                    String _IRRPrincipal,
                    String _IRRCredentials,
                    String _ENTRY_SOURCE_DN,
                    String _SearchFilter,
                    String _OUTPUT_FILENAME,
                    int _OBUFSIZE,
                    boolean _BACKUP_WITH_CHILDREN,
                    boolean _BACKUP_SCHEMA,
                    boolean _APPEND,
                    boolean _NICE,
                    boolean _VERBOSE,
                    boolean _DEBUG,
                    boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        ENTRY_SOURCE_DN = _ENTRY_SOURCE_DN;
        SearchFilter = _SearchFilter;
        OUTPUT_FILENAME = _OUTPUT_FILENAME;
        OBUFSIZE = _OBUFSIZE;
        BACKUP_WITH_CHILDREN = _BACKUP_WITH_CHILDREN;
        BACKUP_SCHEMA = _BACKUP_SCHEMA;
        APPEND = _APPEND;
        NICE = _NICE;
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

        WalkerStatus = _WalkerStatus;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        //
        this.BWQ = new BufferedWriter(BPQout);
        t = new Thread(this, "IRRbackup_LevelWalker");
        t.start(); // Start the Thread.
    } // End of Contructor.

    public void run() {

        // ***********************************************
        // Initialize
        long memfree;
        String tname = Thread.currentThread().getName();
        System.out.println(MP + "Thread Established for:[" + tname + "]");

        // ***********************************************
        // Initialize my LAP Timers
        idxLapTime LP_LEVEL_SEARCH = new idxLapTime();
        idxLapTime LP_ENTRY_TO_PIPE = new idxLapTime();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Source Walker Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRbackupWalker Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            WalkerStatus.setError(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            return;
        } // End of exception

        // **********************************************
        // Set and Show Current Alias Derefencing State.
        try {
            IRRSource.disableAliasDereferencing();
            IRRSource.showAliasDereferencing();
        } catch (Exception e) {
            System.err.println(MP + e);
            WalkerStatus.setError(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            return;
        } // End of exception

        // **********************************************
        // Disable Factories..
        try {
            IRRSource.disableDSAEFactories();
            IRRSource.showDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            WalkerStatus.setError(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            return;
        } // End of exception

        // **************************************************
        // Obtain IRR Directory Schema from our Destination.
        idxIRRschema schema = new idxIRRschema(IRRSource.irrctx);

        // ****************************************
        // Set up our Search Controls
        String[] NO_Attributes = {"1.1"};
        SearchControls OL_ctls = new SearchControls();
        OL_ctls.setReturningAttributes(NO_Attributes);
        OL_ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        // ****************************************
        // Indicate the Backup is starting.
        System.out.println(MP + "Starting Backup...");

        // ****************************************
        // If Debug, show total Memory.
        if (DEBUG) {
            System.out.println(MP + "Total Memory: [" +
                    rt.totalMemory() + "].");

            memfree = rt.freeMemory();

            System.out.println(MP + "Current Free Memory: [" +
                    memfree + "].");
        }

        // ****************************************
        // Obtain our initial Source Entry.
        try {
            if ((ENTRY_SOURCE_DN != null) &&
                    (!ENTRY_SOURCE_DN.equals(""))) {
                LP_ENTRY_TO_PIPE.Start();
                BWQ.write(ENTRY_SOURCE_DN + "\n");
                LP_ENTRY_TO_PIPE.Stop();
                BWQ.flush();
            } // End of If.
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Writing to Thread Pipe,\n" + e);
            return; // End Thread.
        } // End of Exception

        // *****************************************
        // Now obtain the IRR Entries for Backup.
        //
        idxDNLinkList myChildrenList = new idxDNLinkList();
        myChildrenList.addFirst(ENTRY_SOURCE_DN);
        try {
            // *****************************
            // Obtain all Subsequent Levels
            while (myChildrenList.IsNotEmpty()) {
                String myDN = myChildrenList.popfirst();
                // *****************************************
                // Parse the Destination Entry DN to be sure
                // we have any Quotes....
                idxParseDN Naming_Source = new idxParseDN(myDN);

                LP_LEVEL_SEARCH.Start();

                NamingEnumeration nes =
                        IRRSource.irrctx.search(Naming_Source.getDNwithQuotes(),
                                SearchFilter, OL_ctls);

                LP_LEVEL_SEARCH.Stop();
                if (LP_LEVEL_SEARCH.getCurrentDuration() > 1000) {
                    System.out.println(MP + "Warning ** LEVEL Search took " +
                            LP_LEVEL_SEARCH.getElapsedtoString() +
                            " to Complete for Level:[" +
                            Naming_Source.getDN() + "]");
                } // End of If.

                // *****************************************
                // Loop Throught the Results...
                while (nes.hasMore()) {
                    SearchResult srs = (SearchResult) nes.next();
                    String RDN = srs.getName();

                    // *****************************
                    // Acquire the correct DNs.
                    String SourceDN = RDN;
                    if ((myDN != null) &&
                            (!"".equals(myDN))) {
                        SourceDN = RDN + "," + myDN;
                    }

                    // *****************************
                    // Now place the Childs DN in
                    // the Queue to process it's
                    // Children on the next iteration.
                    //
                    myChildrenList.addLast(SourceDN);

                    // *****************************
                    // Now Send the Search Result
                    // Entry to our output thread.
                    //
                    LP_ENTRY_TO_PIPE.Start();
                    BWQ.write(SourceDN + "\n");
                    LP_ENTRY_TO_PIPE.Stop();
                    BWQ.flush();

                } // End of Inner While.
                // **************************************
                // If we are not obtaining Children, end.
                if (!BACKUP_WITH_CHILDREN) {
                    break;
                }
            } // End of Outer While.

        } catch (Exception e) {
            System.err.println(MP + "IRR Exception on Obtaining Child Entries,\n" + e);
            e.printStackTrace();
            WalkerStatus.setError(EXIT_IRR_BACKUP_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Tell the LDIF Thread to Finish.
        try {
            BWQ.write("</EOP>" + "\n");
            BWQ.flush();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Writing to Thread Pipe,\n" + e);
            return; // End Thread.
        } // End of Exception

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Directory Context.");
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            WalkerStatus.setError(EXIT_IRR_CLOSE_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Show the Lap Timings.
        System.out.println(MP + "Lap Time for Level Searches: " + LP_LEVEL_SEARCH);
        System.out.println(MP + "Lap Time for Pipe Communications: " + LP_ENTRY_TO_PIPE);

        // ***************************************
        // Done.
        return;

    } // End of run.

} // End of Class IRRbackupWalker

/**
 * IRRbackupOutput
 * Class to run Output Thread.
 */
class IRRbackupOutput implements Runnable, idxCMDReturnCodes {

    /**
     * IRRbackupOutput
     * Class to provide Output Thread to Write LDIF from Entries obtain via pipe.
     */
    Thread t;

    private BufferedReader BRQ;

    private IRRbackupStatus WriterStatus;

    private static String MP = "IRRbackupLDIFWriter: ";

    private static idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String OUTPUT_FILENAME = null;

    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean BACKUP_WITH_CHILDREN = false;
    private static boolean BACKUP_SCHEMA = false;
    private static boolean APPEND = false;
    private static boolean NICE = true;

    private static int OBUFSIZE = 0;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    private static String ObjectClassName = "objectclass";

    /**
     * IRRbackupOutput Contructor class driven.
     *
     * @param BPQin  Pipe.
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN  Source DN Starting Base for Backup.
     * @param _SearchFilter  Search Filter.
     * @param _OUTPUT_FILENAME  Destination Output Filename.
     * @param _OBUFSIZE     BufferedWriter Output Buffer Size.
     * @param _BACKUP_WITH_CHILDREN Indicate if Children should be included in backup.
     * @param _BACKUP_SCHEMA Indicate if schema should be included in backup.
     * @param _APPEND Indicate if Output Should be Appended.
     * @param _NICE Indicate if Nice Output.
     * @param _VERBOSE Indicate Verbosity.
     * @param _DEBUG Indicate DEBUGGING.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    IRRbackupOutput(Reader BPQin,
                    IRRbackupStatus _WriterStatus,
                    String _IRRHost,
                    String _IRRPrincipal,
                    String _IRRCredentials,
                    String _ENTRY_SOURCE_DN,
                    String _SearchFilter,
                    String _OUTPUT_FILENAME,
                    int _OBUFSIZE,
                    boolean _BACKUP_WITH_CHILDREN,
                    boolean _BACKUP_SCHEMA,
                    boolean _APPEND,
                    boolean _NICE,
                    boolean _VERBOSE,
                    boolean _DEBUG,
                    boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        ENTRY_SOURCE_DN = _ENTRY_SOURCE_DN;
        SearchFilter = _SearchFilter;
        OUTPUT_FILENAME = _OUTPUT_FILENAME;
        OBUFSIZE = _OBUFSIZE;
        BACKUP_WITH_CHILDREN = _BACKUP_WITH_CHILDREN;
        BACKUP_SCHEMA = _BACKUP_SCHEMA;
        APPEND = _APPEND;
        NICE = _NICE;
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

        WriterStatus = _WriterStatus;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        //
        this.BRQ = new BufferedReader(BPQin);
        t = new Thread(this, "IRRbackup_LDIFWriter");
        t.start(); // Start the Thread
    } // End of Contructor.

    /**
     * run
     * Thread to Output LDIF Backup Image.
     */
    public void run() {

        // ***********************************************
        // Initialize
        String ZEN = null;
        int DNcount = 0;
        long memfree;
        String tname = Thread.currentThread().getName();
        System.out.println(MP + "Thread Established for:[" + tname + "]");

        // ***********************************************
        // Initialize my LAP Timers
        idxLapTime LP_ENTRY_SEARCH = new idxLapTime();
        idxLapTime LP_ENTRY_TO_LDIF = new idxLapTime();
        idxLapTime LP_ENTRY_FROM_PIPE = new idxLapTime();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Source Object Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRbackupWriter Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            WriterStatus.setError(EXIT_IRR_CLOSE_FAILURE);
            return;
        } // End of exception

        // **********************************************
        // Set and Show Current Alias Derefencing State.
        try {
            IRRSource.disableAliasDereferencing();
            IRRSource.showAliasDereferencing();
        } catch (Exception e) {
            System.err.println(MP + e);
            WriterStatus.setError(EXIT_IRR_CLOSE_FAILURE);
            return;
        } // End of exception

        // **********************************************
        // Disable Factories..
        try {
            IRRSource.disableDSAEFactories();
            IRRSource.showDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            WriterStatus.setError(EXIT_IRR_CLOSE_FAILURE);
            return;
        } // End of exception

        // **************************************************
        // Obtain IRR Directory Schema from our Destination.
        idxIRRschema schema = new idxIRRschema(IRRSource.irrctx);

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        //******************************************
        // Set up our Search Filter.
        if (SearchFilter == null) {
            SearchFilter = "(objectclass=*)";
        }

        //******************************************
        // Set up our Search Controls.
        String[] ALL_AttrIDs = {"*"};
        SearchControls OS_ctls = new SearchControls();
        OS_ctls.setReturningAttributes(ALL_AttrIDs);
        OS_ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

        // *******************************************
        // Obtain the Directory Schema and Backup.
        // If Specified.
        if (BACKUP_SCHEMA) {
            try {
                BackupSchema(schema);
            } catch (Exception e) {
                System.err.println(MP + "Exception opening Backup Schema Output File. " + e);
                WriterStatus.setError(EXIT_IRR_BACKUP_SCHEMA_OUTPUT_FAILURE);
                return; // End Thread.
            } // End of exception
        } // End of If Schema Backup.

        // **************************************
        // Open up the File Output Stream.
        System.out.println(MP + "Preparing Backup Output File...");
        BufferedWriter LDIFOUT = null;
        try {
            if (OBUFSIZE <= 0) {
                OBUFSIZE = 131072;
            } // 128KB Buffer.
            System.out.println(MP + "Backup Output File Buffer Size:[" + OBUFSIZE + "]");
            LDIFOUT = new BufferedWriter(new FileWriter(OUTPUT_FILENAME + ".ldif", APPEND), OBUFSIZE);
            if (!APPEND) {
                LDIFOUT.write("version: 1\n");
            }

            LDIFOUT.write("# ***********************************************\n");
            LDIFOUT.write("# FRAMEWORK IRR Directory LDIF Backup.\n");
            if (ENTRY_SOURCE_DN.equals("")) {
                LDIFOUT.write("# Source DN: [" + "ROOT" + "]\n");
            } else {
                LDIFOUT.write("# Source DN: [" + ENTRY_SOURCE_DN + "]\n");
            }
            LDIFOUT.write("# Source Filter: [" + SearchFilter + "]\n");
            LDIFOUT.write("# Source Host: [" + IRRHost + "]\n");
            LDIFOUT.write("# Start Time: " + CurrentTimeStamp.get() + "\n");
            LDIFOUT.write("# ***********************************************\n");
            LDIFOUT.write("\n");
        } catch (Exception e) {
            System.err.println(MP + "Exception opening Backup LDIF Output File. " + e);
            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
            return; // End Thread.
        } // End of exception

        // **************************************
        // Loop to process commands from Walker
        // Thread.
        try {
            while (true) {
                LP_ENTRY_FROM_PIPE.Start();
                ZEN = BRQ.readLine();
                LP_ENTRY_FROM_PIPE.Stop();

                // ***************************
                // Ignore Null.
                if ((ZEN == null) ||
                        ("".equals(ZEN))) {
                    continue;
                }

                // ***************************
                // Should We End Thread?
                if ("</EOP>".equals(ZEN)) {
                    break;
                }

                // *****************************
                // Is the Entry a LDIF Comment?
                // Or a Simple NewLine?
                if ((ZEN.startsWith("#")) ||
                        (ZEN.equals("\n"))) {
                    try {
                        LDIFOUT.write(ZEN);
                        continue;
                    } catch (Exception e) {
                        System.err.println(MP + "Exception Writing to Backup LDIF Output File. " + e);
                        WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
                        return; // End Thread.
                    } // End of exception
                } // End of If.

                // *****************************
                // Ok, this must be a DN, so
                // Process.
                //

                // *****************************************
                // Parse the Destination Entry DN to be sure
                // we have any Quotes....
                idxParseDN Naming_Source = new idxParseDN(ZEN);
                ZEN = Naming_Source.getDNwithQuotes();

                // *****************************************
                // Obtain the Namespace.
                String NameSpace = null;
                try {
                    NameSpace = IRRSource.irrctx.getNameInNamespace();
                    if (NameSpace.equals("")) {
                        NameSpace = ZEN;
                    }
                } catch (Exception e) {
                    {
                        NameSpace = ZEN;
                    }
                } // End of exception

                // *****************************************
                // Now obtain the Source Entry.
                //
                try {

                    LP_ENTRY_SEARCH.Start();
                    NamingEnumeration sl =
                            IRRSource.irrctx.search(ZEN, SearchFilter, OS_ctls);
                    LP_ENTRY_SEARCH.Stop();
                    if (LP_ENTRY_SEARCH.getCurrentDuration() > 1000) {
                        System.out.println(MP + "Warning ** Entry Search took " +
                                LP_ENTRY_SEARCH.getElapsedtoString() +
                                " to Complete to Obtain:[" +
                                ZEN + "]");
                    }

                    if (sl == null) {
                        continue;
                    }

                    LP_ENTRY_TO_LDIF.Start();
                    while (sl.hasMore()) {
                        SearchResult si = (SearchResult) sl.next();
                        DNcount++;

                        String DN = null;
                        if (NameSpace.equals("")) {
                            DN = si.getName();
                        } else if (si.getName().equals("")) {
                            DN = NameSpace;
                        } else {
                            DN = si.getName() + "," + NameSpace;
                        }

                        // ******************************************
                        // Write out the DN.
                        // Do not write out a JNDI Quoted DN.
                        // That is not LDIF Compliant.
                        //
                        idxParseDN pDN = new idxParseDN(DN);
                        if (NICE) {
                            LDIFOUT.write("dn: ");

                            if (pDN.isQuoted()) {
                                LDIFOUT.write(pDN.getDN());
                            } else {
                                LDIFOUT.write(DN);
                            }

                            LDIFOUT.write("\n");

                        } else {
                            if (pDN.isQuoted()) {
                                idxIRROutput.WriteLDIF("dn", pDN.getDN(), LDIFOUT);
                            } else {
                                idxIRROutput.WriteLDIF("dn", DN, LDIFOUT);
                            }
                        } // End of DN Output.

                        // Obtain the entries Attributes.
                        Attributes entryattrs = si.getAttributes();

                        // Obtain ObjectClass First.
                        Attribute eo = entryattrs.get(ObjectClassName);
                        for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                            idxIRROutput.WriteLDIF(eo.getID(), eov.next(), LDIFOUT);
                        }

                        // Obtain Naming Attribute Next.
                        // One will not exist for an Alias.
                        if (!"".equals(pDN.getNamingAttribute())) {
                            Attribute en = entryattrs.get(pDN.getNamingAttribute());
                            if (en != null) {
                                for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                                    idxIRROutput.WriteLDIF(en.getID(), env.next(), LDIFOUT);
                                }
                            } // End of Inner If.
                        } // End of Naming Attribute.

                        // Finish Obtaining remaining Attributes,
                        // in no special sequence.
                        for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                            Attribute attr = (Attribute) ea.next();

                            if ((!ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                                    (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                                for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                                    idxIRROutput.WriteLDIF(attr.getID(), ev.next(), LDIFOUT);
                                }
                            } // End of If
                        } // End of Outer For Loop

                        idxIRROutput.WriteLDIF("", "", LDIFOUT);
                        LP_ENTRY_TO_LDIF.Stop();
                        if (LP_ENTRY_TO_LDIF.getCurrentDuration() > 1000) {
                            System.out.println(MP + "Warning ** Entry to LDIF took " +
                                    LP_ENTRY_TO_LDIF.getElapsedtoString() +
                                    " to Complete for:[" +
                                    DN + "]");
                        }

                    } // End of Inner While Loop
                } catch (Exception e) {
                    System.err.println(MP + "IRR Exception on IRRbackup, Obtaining Source Entry, " + e);
                    e.printStackTrace();
                    WriterStatus.setError(EXIT_IRR_BACKUP_FAILURE);
                    return; // End Thread.
                } // End of exception

            } // End of Outer While Loop.
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception on IRRbackup, Obtaining Data From Thread Pipe, " + e);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Show number of entries backed up.
        try {
            if (DNcount > 0) {
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("# End of IRR Backup\n");
                LDIFOUT.write("# End Time: " + CurrentTimeStamp.get() + "\n");
                LDIFOUT.write("# Entries Contained in LDIF Backup:[" +
                        DNcount + "]\n");
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("\n");

                System.out.println(MP + "Successful Backup, Entries Backed Up:[" +
                        DNcount + "]");
            } else {
                LDIFOUT.write("\n");
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("# IRR BACKUP NOT SUCCESSFUL\n");
                LDIFOUT.write("# End Time: " + CurrentTimeStamp.get() + "\n");
                LDIFOUT.write("# Entries Contained in LDIF Backup:[0]\n");
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("\n");

                System.out.println(MP + "Exception on Backup, Entries Backed Up:[" +
                        DNcount + "]");
            }
        } catch (Exception e) {
            System.err.println(MP + "Exception opening Backup LDIF Output File. " + e);
            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Close our Output File.
        try {
            LDIFOUT.flush();
            LDIFOUT.close();
        } catch (Exception e) {
            System.err.println(MP + "Exception closing Output File. " + e);
            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_CLOSE_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Directory Context.");
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            WriterStatus.setError(EXIT_IRR_CLOSE_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Show the Lap Timings.
        System.out.println(MP + "Lap Time for Entry Search: " + LP_ENTRY_SEARCH);
        System.out.println(MP + "Lap Time for Entry to LDIF Output: " + LP_ENTRY_TO_LDIF);
        System.out.println(MP + "Lap Time for Pipe Communication: " + LP_ENTRY_FROM_PIPE);

        // ***************************************
        // Done.
        return;

    } // End of run.

    /**
     * BackupSchema
     * Backup IRR Directory Schema.
     *
     * @param schema idxIRRSchema Object
     * @throws Exception if errors from accessing schema or writing output of schema.
     */
    private void BackupSchema(idxIRRschema schema) throws Exception {

        System.out.println(MP + "Starting Backup of Schema...");
        BufferedWriter SCHEMAOUT = null;

        // Open our Schema Backup File.
        SCHEMAOUT = new BufferedWriter(
                new FileWriter(OUTPUT_FILENAME + ".schema.xml"));
        // Write out Schema Body
        SCHEMAOUT.write("<!-- *********************************************** -->\n");
        SCHEMAOUT.write("<!-- FRAMEWORK IRR Directory Schema Backup. -->\n");
        SCHEMAOUT.write("<!-- Source Host: [" + IRRHost + "] -->\n");
        SCHEMAOUT.write("<!-- Start Time: " + CurrentTimeStamp.get() + " -->\n");
        SCHEMAOUT.write("<!-- *********************************************** -->\n");

        // Write out Schema Body
        schema.printAll(SCHEMAOUT);

        // Write out the Schema Tail.
        SCHEMAOUT.write("<!-- *********************************************** -->\n");
        SCHEMAOUT.write("<!-- End of IRR Schema Backup -->\n");
        SCHEMAOUT.write("<!-- End Time: " + CurrentTimeStamp.get() + " -->\n");
        SCHEMAOUT.write("<!-- *********************************************** -->\n\n");

        // Close up the Schema Backup.
        SCHEMAOUT.flush();
        SCHEMAOUT.close();

        // **************************************
        // Done with Schema Backup.
        System.out.println(MP + "Schema Backup Complete.");

    } // End of BackupSchema Method.

} // End of Class IRRbackupOutput
