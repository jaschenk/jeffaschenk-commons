package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxTimeStamp;

import java.util.*;
import java.io.*;

/**
 * Java Daemon Server utility, driven from properties and command
 * line parameters to obtain all incremental changes from the
 * IRR Directory tnd log those changes to an LDIF format output file on disk.
 * The Output conforms to the LDIF Specification: RFC2849.
 * <br>
 * <b>Usage:</b><br>
 * IRRChangeLogger &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --irrmltuid
 *      Specify the IRR Unique MetaLink Trigger ID.
 * --outdir
 * 	Specify Full Output file system Directory Path for backups to be written.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --filetimespan
 * 	Specify Time in Minutes for new File Creations during incremental changes.
 * --outputbufsize
 * 	Specify Output Buffer Size, Default 128KB.
 * --append (DEPRECATED)
 * 	Specify to Append LDIF Existing Output File.
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
 * @version 2.0 $Revision
 * Developed 2001-2002
 */

public class IRRChangeLogger implements idxCMDReturnCodes {

    private static String VERSION = "Version: 2.0 2002-09-30, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRChangeLogger: ";

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String IRRMLTuid = null;

    private static String OUTPUT_DIRECTORY = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean APPEND = false;

    private static int OBUFSIZE = 0;

    private static long FILETIMESPAN = 0;

    private static idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    private PipedWriter BPQout;
    private PipedReader BPQin;


    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRChangeLogger <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--irrmltuid ");
        System.err.println("\tSpecify IRR(Directory) MetaLink Trigger UID.");
        System.err.println(MP + "--outdir ");
        System.err.println("\tSpecify Full Output file system Directory Path for backups to be written.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--filetimespan ");
        System.err.println("\tSpecify to Time in Minutes to create New Output Files.");
        System.err.println(MP + "--append ");
        System.err.println("\tSpecify to Append LDIF Output to Existing File.");
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
     * IRRChangeLogger Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _IRRMLTuid  Source IRR MetaLink Trigger UID.
     * @param _OUTPUT_DIRECTORY  Destination Output Filename.
     * @param _OBUFSIZE     BufferedWriter Output Buffer Size.
     * @param _FILETIMESPAN    FileTimespan window.
     * @param _APPEND Indicate if Output Should be Appended.
     * @param _VERBOSE Indicate Verbosity.
     * @param _DEBUG Indicate DEBUGGING.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRChangeLogger(String _IRRHost,
                           String _IRRPrincipal,
                           String _IRRCredentials,
                           String _IRRMLTuid,
                           String _OUTPUT_DIRECTORY,
                           int _OBUFSIZE,
                           long _FILETIMESPAN,
                           boolean _APPEND,
                           boolean _VERBOSE,
                           boolean _DEBUG,
                           boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        IRRMLTuid = _IRRMLTuid;
        OUTPUT_DIRECTORY = _OUTPUT_DIRECTORY;
        OBUFSIZE = _OBUFSIZE;
        FILETIMESPAN = _FILETIMESPAN;
        APPEND = _APPEND;
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRChangeLogger.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws Exception       for any unrecoverable errors during function.
     * @throws idxIRRException for any unrecoverable errors during function.
     */
    public void perform() throws Exception, idxIRRException {

        // ********************************************
        //
        // We shall start two threads
        // A Poller Thread that will wait for changes
        // from the IRR Directory using the MetaLink Trigger
        // Facility.
        //
        // A Collector Thread which will receive Operation and
        // DN information over a pipe from the Poller thread.
        // The Collector Thread will then read the Entry
        // from the Directory and write the entry in LDIF
        // to the specified output file.
        //
        // ********************************************
        CurrentTimeStamp.enableLocalTime();  // Show Local Time Not GMT.

        // ********************************************
        // Establish Status Objects.
        IRRChangeStatus PollerStatus = new IRRChangeStatus();
        IRRChangeStatus CollectorStatus = new IRRChangeStatus();

        // ********************************************
        // Set up a PIPE for Thread Communication.
        BPQout = new PipedWriter();
        try {
            BPQin = new PipedReader(BPQout);
        } catch (IOException e) {
            if (ExitOnException) {
                System.err.println(MP + "Unable to Obtain PipedReader for Thread Communications, Terminating");
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw new idxIRRException(MP + "Unable to Obtain PipeReader for Thread Communcations, Terminating.");
            }
        } // End of Exception.

        System.out.println(MP + CurrentTimeStamp.getFTS() + " Starting Change Collector LDIF Writer Thread...");
        IRRChangeCollector CollectorThread;
        CollectorThread = new IRRChangeCollector(BPQin,
                CollectorStatus,
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                IRRMLTuid,
                OUTPUT_DIRECTORY,
                OBUFSIZE,
                FILETIMESPAN,
                APPEND,
                VERBOSE,
                DEBUG,
                ExitOnException);

        String IRRHostnonURI = IRRHost.substring(7);
        if (IRRHostnonURI.indexOf(":") > 0) {
            IRRHostnonURI = IRRHostnonURI.substring(0, IRRHostnonURI.indexOf(":"));
        }
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Starting Changer Poller Thread...");
        IRRChangePoller PollerThread;
        PollerThread = new IRRChangePoller(BPQout,
                PollerStatus,
                IRRHostnonURI,
                IRRPrincipal,
                IRRCredentials,
                IRRMLTuid,
                OUTPUT_DIRECTORY,
                VERBOSE,
                DEBUG,
                ExitOnException);

        // ******************************************
        // Show Status.
        //
        if (CollectorThread.t.isAlive()) {
            System.out.println(MP + CurrentTimeStamp.getFTS() + " Entry Collector LDIF Output Thread is Running...");
        }
        if (PollerThread.t.isAlive()) {
            System.out.println(MP + CurrentTimeStamp.getFTS() + " MetaLink Poller Thread is Running...");
        }


        // ******************************************
        // Now Join and Wait for Completion.
        //
        try {
            System.out.println(MP + CurrentTimeStamp.getFTS() + " Main Thread waiting for Worker Threads to Complete...");

            CollectorThread.t.join();
            PollerThread.t.join();

            System.out.println(MP + CurrentTimeStamp.getFTS() + " MetaLink Poller Thread Running Status:[" +
                    PollerThread.t.isAlive() + "].");

            System.out.println(MP + CurrentTimeStamp.getFTS() + " Entry Collector LDIF Output Thread Running Status:[" +
                    CollectorThread.t.isAlive() + "].");

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
        if (PollerStatus.getError() != 0) {
            if (ExitOnException) {
                System.err.println(MP + "Exception in MetaLink Poller Thread, Error Code:[" +
                        PollerStatus.getError() + "]");
                System.exit(PollerStatus.getError());
            } else {
                throw new idxIRRException(MP + "Exception in MetaLink Poller Thread, Error Code:[" +
                        PollerStatus.getError() + "]");
            } // End of Else.
        } // End of If.

        if (CollectorStatus.getError() != 0) {
            if (ExitOnException) {
                System.err.println(MP + "Exception in Collector LDIF Writer Thread, Error Code:[" +
                        CollectorStatus.getError() + "]");
                System.exit(CollectorStatus.getError());
            } else {
                throw new idxIRRException(MP + "Exception in Collector LDFI Writer Thread, Error Code:[" +
                        CollectorStatus.getError() + "]");
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
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRChangeLogger
     */
    public static void main(String[] args) {

        // ****************************************
        // Send the Greeting.
        CurrentTimeStamp.enableLocalTime();  // Show Local Time Not GMT.
        System.out.println(MP + CurrentTimeStamp.getFTS() + " " + VERSION);

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

        VAR.add(new idxArgVerificationRules("irrmltuid",
                true, true));

        VAR.add(new idxArgVerificationRules("outdir",
                true, true));

        VAR.add(new idxArgVerificationRules("append",
                false, false));

        VAR.add(new idxArgVerificationRules("outputbufsize",
                false, true));

        VAR.add(new idxArgVerificationRules("filetimespan",
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
            System.out.println(MP + CurrentTimeStamp.getFTS() + " IRR ID:[" + IRRPrincipal + "]");

            IRRCredentials = clPC.getCredentials();
            if (DEBUG) {
                System.out.println(MP + CurrentTimeStamp.getFTS() + " IRR Password:[" + IRRCredentials + "]");
            }
        } else {
            System.out.println(MP + "Required Principal and Credentials not Specified, unable to continue.");
            Usage();
        } // End of Else.

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("append")) {
            APPEND = false;
        } // Never append, bad form....

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + CurrentTimeStamp.getFTS() + " IRR Host URL:[" + IRRHost + "]");

        IRRMLTuid = (String) Zin.getValue("irrmltuid");
        System.out.println(MP + CurrentTimeStamp.getFTS() + " IRR MetaLink UID:[" + IRRMLTuid + "]");

        OUTPUT_DIRECTORY = ((String) Zin.getValue("outdir")).trim();
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Output Directory:[" + OUTPUT_DIRECTORY + "]");

        if (Zin.doesNameExist("outputbufsize")) {
            try {
                OBUFSIZE = Integer.parseInt(
                        (String) Zin.getValue("outputbufsize"));
            } catch (Exception e) {
                System.err.println(MP + "Output Buffer Size is Invalid, Unable to Continue,\n" + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } // End of Exception.

        } // End of If.

        if (Zin.doesNameExist("filetimespan")) {
            try {
                FILETIMESPAN = Integer.parseInt(
                        (String) Zin.getValue("filetimespan"));

                // *************************************
                // Now Create the time in milliseconds.
                //
                System.out.println(MP + CurrentTimeStamp.getFTS() + " File Time Span Cycle Creation Window:[" +
                        FILETIMESPAN + "] Minutes.");
                FILETIMESPAN = ((FILETIMESPAN * 60) * 1000);

            } catch (Exception e) {
                System.err.println(MP + "File Time Span is Invalid, Unable to Continue,\n" + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } // End of Exception.

        } // End of If.


        // ************************************************
        // Show Operational Parameters
        if (APPEND) {
            System.out.println(MP + CurrentTimeStamp.getFTS() + " Will Append LDIF Output to Existing Incremental File.");
        } else {
            System.out.println(MP + CurrentTimeStamp.getFTS() + " Will Create new Current LDIF Output Incremental File.");
        }

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRChangeLogger FUNCTION = new IRRChangeLogger(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                IRRMLTuid,
                OUTPUT_DIRECTORY,
                OBUFSIZE,
                FILETIMESPAN,
                APPEND,
                VERBOSE,
                DEBUG,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRChangeLogger.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRChangeLogger
