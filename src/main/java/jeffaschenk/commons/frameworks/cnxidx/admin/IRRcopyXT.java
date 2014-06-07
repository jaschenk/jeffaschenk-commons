package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxParseDN;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to Copy an Entry from a Source to a Destination,
 * by a specified number of Threads.
 * This provides a Thread and concurrent write of Data by
 * several different Threads. The Destination DN will be modified to
 * specify the thread number, to allow full concurrency.  This will
 * provide a means to test performance and load conditions on the directory.
 * <br>
 * <br>
 * This utility command can copy entries from one Directory to another, since
 * both a Source and Destination Directory Contexts are established.
 * <br>
 * <b>Usage:</b><br>
 * IRRcopyXT &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --destdn
 * 	Specify Full DN of Destination Entry.
 * --tid
 * 	Specify Thread Name prefix.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --desthosturl
 * 	Specify Destination IRR(Directory) LDAP URL, ldap://hostname.acme.com
 * --destirrid
 * 	Specify Destination IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa
 * --destirrpw
 * 	Specify Destination IRR(Directory) LDAP BIND Password
 * --destidu
 * 	Specify FRAMEWORK Keystore Alias to obtain Destination IRRID and IRRPW.
 * --verbose
 * 	Specify Additional Logging Information.
 * --debug
 * 	Specify Additional Debugging Information.
 * --overwrite
 * 	Specify Existing Destination Entry will be overwritten.
 * --withchildren
 * 	Specify Copy of Source children entries to Destination as well as parent entry.
 * --version
 * 	Display Version information and exit.
 * --threads
 * 	Number of Concurrent Threads to run, default is two (2).
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

class IRRcopyXT implements Runnable, idxCMDReturnCodes {
    String tname; // Name of Thread
    Thread t;
    List<String> RunnerArgs;

    IRRcopyXT(String _threadname, List<String> _runnerargs) {
        tname = _threadname;
        RunnerArgs = _runnerargs;
        t = new Thread(this, tname);
        System.out.println("** New Thread Created: " + t);
        t.start(); // Start the Thread.
    } // End of Constructor Class.

    public void run() {

        IRROMRunner xrunner = new IRROMRunner(this.tname);

        // ****************************************
        // Execute the Function.
        try {
            xrunner.setFUNCTION(this.RunnerArgs);

            xrunner.performFUNCTION();

        } catch (Exception e) {
            System.out.println(this.tname + " Exception, existing.");
        } // End of Exception.

    } // End of Run Method.

} //:~ End of IRRcopyXT class

class IRRcopyXTexecutor implements idxCMDReturnCodes {

    private static String VERSION = "Version: 1.0 2001-12-15, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRcopyXTexecutor: ";

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String IRRDestHost = null;
    private static String IRRDestPrincipal = null;
    private static String IRRDestCredentials = null;
    private static String ENTRY_DESTINATION_DN = null;

    private static boolean OVERWRITE_DESTINATION_ENTRY = false;
    private static boolean COPY_WITH_CHILDREN = false;
    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    private static String TID = null;

    private static int THREADS = 2;

    private boolean ExitOnException = false;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRcopyXT <Required Parameters> <Optional Parameters>");

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
        System.err.println(MP + "--destdn ");
        System.err.println("\tSpecify Full DN of Destination Entry.");
        System.err.println(MP + "--tid ");
        System.err.println("\tSpecify Thread Name Prefix.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--desthosturl ");
        System.err.println("\tSpecify Destination IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--destirrid ");
        System.err.println("\tSpecify Destination IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--destirrpw ");
        System.err.println("\tSpecify Destination IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--destidu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain Destination IRRID and IRRPW.");


        System.err.println(MP + "--verbose");
        System.err.println("\tSpecify Additional Logging Information.");
        System.err.println(MP + "--debug");
        System.err.println("\tSpecify Additional Debugging Information.");

        System.err.println(MP + "--overwrite");
        System.err.println("\tSpecify Existing Destination Entry will be overwritten.");

        System.err.println(MP + "--withchildren");
        System.err.println("\tSpecify Copy of Source children entries to Destination as well as parent entry.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--threads");
        System.err.println("\tSpecify number of concurrent Threads, default is 2.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     */
    public static void main(String args[]) {

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
        // Was Debugging Requested?
        if (Zin.doesNameExist("debug")) {
            DEBUG = true;
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

        VAR.add(new idxArgVerificationRules("desthosturl",
                false, true));

        VAR.add(new idxArgVerificationRules("destirrid",
                false, true));

        VAR.add(new idxArgVerificationRules("destirrpw",
                false, true));

        VAR.add(new idxArgVerificationRules("destidu",
                false, true));

        VAR.add(new idxArgVerificationRules("sourcedn",
                true, true));

        VAR.add(new idxArgVerificationRules("destdn",
                true, true));

        VAR.add(new idxArgVerificationRules("tid",
                true, true));

        VAR.add(new idxArgVerificationRules("overwrite",
                false, false));

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("threads",
                false, true));

        VAR.add(new idxArgVerificationRules("verbose",
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
        // Load up the Destination Principal/Credentials.
        //
        if (clPC.wasDestObtained()) {
            IRRDestPrincipal = clPC.getDestPrincipal();
            IRRDestCredentials = clPC.getDestCredentials();
        } else {
            IRRDestPrincipal = IRRPrincipal;
            IRRDestCredentials = IRRCredentials;
        } // End of Else.
        System.out.println(MP + "IRR Destination ID:[" + IRRDestPrincipal + "]");
        //System.out.println(MP+"IRR Destination Password:["+IRRDestCredentials+"]");

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("withchildren")) {
            COPY_WITH_CHILDREN = true;
        }

        if (Zin.doesNameExist("overwrite")) {
            OVERWRITE_DESTINATION_ENTRY = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        TID = (String) Zin.getValue("tid");
        System.out.println(MP + "Thread Name Prefix:[" + TID + "]");

        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        if (!Zin.doesNameExist("desthosturl")) {
            IRRDestHost = IRRHost;
        } else {
            IRRDestHost = (String) Zin.getValue("desthosturl");
        }
        System.out.println(MP + "IRR Destination Host URL:[" + IRRDestHost + "]");

        ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        ENTRY_SOURCE_DN = ENTRY_SOURCE_DN.toLowerCase();
        System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");

        ENTRY_DESTINATION_DN = ((String) Zin.getValue("destdn")).trim();
        ENTRY_DESTINATION_DN = ENTRY_DESTINATION_DN.toLowerCase();
        System.out.println(MP + "Destination DN:[" + ENTRY_DESTINATION_DN + "]");

        if (Zin.doesNameExist("threads")) {
            try {
                THREADS = Integer.parseInt(
                        ((String) Zin.getValue("threads")).trim());
            } catch (Exception e) {
                System.err.println(MP + "Unable to obtain correct Thread Value Specified.\n" + e);
                System.exit(EXIT_GENERIC_FAILURE);
            } // End of Exception.
        } // End of If.

        // ************************************************
        // Show Operational Parameters
        if (OVERWRITE_DESTINATION_ENTRY) {
            System.out.println(MP + "Will Overwrite existing Destination Entry with new entry.");
        } else {
            System.out.println(MP + "Will NOT Overwrite existing Destination Entry with new entry.");
        }

        if (COPY_WITH_CHILDREN) {
            System.out.println(MP + "Will Copy Source Children along with this to new Destination.");
        } else {
            System.out.println(MP + "Will NOT copy any Source Children.");
        }

        System.out.println(MP + "Will spawn:[" + THREADS + "] for Concurrent Copy from same Source to Same Destination.");

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        int tnum;
        IRRcopyXT mythreads[] = new IRRcopyXT[THREADS];

        for (tnum = 0; tnum < THREADS; tnum++) {
            String destDN = ENTRY_DESTINATION_DN;
            idxParseDN tDdn = new idxParseDN(ENTRY_DESTINATION_DN);
            if (tDdn.isValid()) {
                destDN = tDdn.getNamingAttribute() +
                        "=" +
                        tDdn.getNamingValue() +
                        "(" + TID + "." + tnum + ")," +
                        tDdn.getPDN();
            } // End of If.

            // ********************************************************
            // Construct the Running Arguments for this Thread.
            List<String> RunnerArgs = new ArrayList<>();

            RunnerArgs.add("jeffaschenk.commons.frameworks.cnxidx.admin.IRRcopyEntry");

            RunnerArgs.add("--hosturl");
            RunnerArgs.add("\047" + IRRHost + "\047");
            RunnerArgs.add("--irrid");
            RunnerArgs.add("\047" + IRRPrincipal + "\047");
            RunnerArgs.add("--irrpw");
            RunnerArgs.add("\047" + IRRCredentials + "\047");
            RunnerArgs.add("--sourcedn");
            RunnerArgs.add("\047" + ENTRY_SOURCE_DN + "\047");
            RunnerArgs.add("--desthosturl");
            RunnerArgs.add("\047" + IRRDestHost + "\047");
            RunnerArgs.add("--destirrid");
            RunnerArgs.add("\047" + IRRDestPrincipal + "\047");
            RunnerArgs.add("--destirrpw");
            RunnerArgs.add("\047" + IRRDestCredentials + "\047");
            RunnerArgs.add("--destdn");
            RunnerArgs.add("\047" + destDN + "\047");

            // ******************************************
            // Look for any possible Boolean Indicators
            if (COPY_WITH_CHILDREN) {
                RunnerArgs.add("--withchildren");
            }

            if (OVERWRITE_DESTINATION_ENTRY) {
                RunnerArgs.add("--overwrite");
            }

            if (VERBOSE) {
                RunnerArgs.add("--verbose");
            }

            if (DEBUG) {
                RunnerArgs.add("--debug");
            }

            // ****************************************
            // Initialize Thread.
            System.out.println(MP + "Starting Thread Name: " + TID + "." + tnum);
            mythreads[tnum] = new IRRcopyXT(TID + "." + tnum, RunnerArgs);

        } // End of For Loop.

        // ***************************************************
        // Show Thread Status.
        for (tnum = 0; tnum < THREADS; tnum++) {
            if (mythreads[tnum].t.isAlive()) {
                System.out.println(MP + "Thread " + tnum + " is Running...");
            }
        } // End of For Loop.

        // ******************************************
        // Now Join and Wait for Completion.
        //
        try {
            System.out.println(MP + "Main Thread waiting for Worker Threads to Complete...");
            for (tnum = 0; tnum < THREADS; tnum++) {
                mythreads[tnum].t.join();
            }

        } catch (InterruptedException e) {
            System.err.println(MP + "Main Thread Interrupted, Terminating.");
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main.

} //:~ End of IRRcopyXTexecutor

