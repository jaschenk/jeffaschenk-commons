package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to Analyze the Entire Directory or a portion of
 * the IRR Directory.
 * <br>
 * <b>Usage:</b><br>
 * IRRanalyze &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --filter
 * 	Specify Search Filter, default is (objectclass=*).
 * --sourcedn
 * 	Specify Full DN of Source Entry to be used as base, default is [ROOT].
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
 * @version 5.0 $Revision
 * Developed 2001-2005
 * @since 1.0
 */

public class IRRanalyze implements idxCMDReturnCodes {

    private static String VERSION = "Version: 5.0 2005-07-19, " +
            "FRAMEWORK, Incorporated.";

    private static String MP = "IRRanalyze: ";

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;


    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRanalyze <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--filter");
        System.err.println("\tSpecify Search Filter, default is (objectclass=*).");
        System.err.println(MP + "--sourcedn ");
        System.err.println("\tSpecify Full DN of Source Entry to be used as base, default is [ROOT].");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--debug");
        System.err.println("\tDisplay Debugging Information.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRanalyze Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN  Source DN Starting Base for Analyze.
     * @param _SearchFilter  Search Filter.
     * @param _VERBOSE Indicate Verbosity.
     * @param _DEBUG Indicate DEBUGGING.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    public IRRanalyze(String _IRRHost,
                      String _IRRPrincipal,
                      String _IRRCredentials,
                      String _ENTRY_SOURCE_DN,
                      String _SearchFilter,
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
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

    } // End of Constructor for IRRanalyze.

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
        // Directory Tree and a Analyze Thread that
        // will Analyze the Entry based upon the
        // entries Objectclasses only and
        // accmulated status information which will
        // be dumped after all entries have been
        // processed.
        // ********************************************
        IRRanalyzeQueue myBBQ = new IRRanalyzeQueue();
        myBBQ.setVerbose(VERBOSE);

        if (VERBOSE) {
            System.out.println(MP + "Starting Directory Analyzer Thread...");
        }
        IRRanalyzer AT;
        AT = new IRRanalyzer(myBBQ,
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                SearchFilter,
                VERBOSE,
                DEBUG,
                ExitOnException);

        if (VERBOSE) {
            System.out.println(MP + "Starting Directory Tree Walker Thread...");
        }
        IRRanalyzeWalker WT;
        WT = new IRRanalyzeWalker(myBBQ,
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                SearchFilter,
                VERBOSE,
                DEBUG,
                ExitOnException);

        // ******************************************
        // Show Status.
        //
        if (AT.t.isAlive()) {
            if (VERBOSE) {
                System.out.println(MP + "Object Obtainer and LDIF Output Thread is Running...");
            }
        }
        if (WT.t.isAlive()) {
            if (VERBOSE) {
                System.out.println(MP + "Tree Walker Thread is Running...");
            }
        }


        // ******************************************
        // Now Join and Wait for Completion.
        //
        try {
            if (VERBOSE) {
                System.out.println(MP + "Main Thread waiting for Worker Threads to Complete...");
            }

            AT.t.join();
            WT.t.join();

        } catch (InterruptedException e) {
            if (ExitOnException) {
                System.err.println(MP + "Main Thread Interrupted, Terminating.");
                System.exit(EXIT_GENERIC_FAILURE);
            } else {
                throw new idxIRRException(MP + "Main Thread Interrupted, Terminating.");
            }

        } // End of Exception.

        // ******************************************
        // Determine if all went well?
        //
        if (myBBQ.getErrorWalker() != 0) {
            if (ExitOnException) {
                System.err.println(MP + "Exception in Walker Thread, Error Code:[" +
                        myBBQ.getErrorWalker() + "]");
                System.exit(myBBQ.getErrorWalker());
            } else {
                throw new idxIRRException(MP + "Exception in Walker Thread, Error Code:[" +
                        myBBQ.getErrorWalker() + "]");
            }
        } // End of If.

        if (myBBQ.getErrorAnalyzer() != 0) {
            if (ExitOnException) {
                System.err.println(MP + "Exception in Analyzer Thread, Error Code:[" +
                        myBBQ.getErrorAnalyzer() + "]");
                System.exit(myBBQ.getErrorAnalyzer());
            } else {
                throw new idxIRRException(MP + "Exception in Analyzer Thread, Error Code:[" +
                        myBBQ.getErrorAnalyzer() + "]");
            }
        } // End of If.


        // ********************
        // Done.
        return;

    } // End of Perform Analysis

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRanalyze
     */
    public static void main(String[] args) {

        long starttime, endtime;

        // ****************************************
        // Parse the incoming Arguments and
        // create objects for each entity.
        //
        idxArgParser Zin = new idxArgParser();
        Zin.parse(args);

        // ****************************************
        // Send the Greeting.
        if (VERBOSE) {
            System.out.println(MP + VERSION);
        }

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
            System.out.println(MP + VERSION);
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
                false, true));

        VAR.add(new idxArgVerificationRules("filter",
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
            if (VERBOSE) {
                System.out.println(MP + "IRR ID:[" + IRRPrincipal + "]");
            }

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
        if (Zin.doesNameExist("debug")) {
            DEBUG = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        if (VERBOSE) {
            System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");
        }

        if (Zin.doesNameExist("sourcedn")) {
            ENTRY_SOURCE_DN = ((String) Zin.getValue("sourcedn")).trim();
        }

        if (Zin.doesNameExist("filter")) {
            SearchFilter = ((String) Zin.getValue("filter")).trim();
        }

        // ************************************************
        // Show Operational Parameters
        if (ENTRY_SOURCE_DN == null) {
            if (VERBOSE) {
                System.out.println(MP + "Source DN:[ROOT]");
            }
            ENTRY_SOURCE_DN = "";
        } else {
            if (VERBOSE) {
                System.out.println(MP + "Source DN:[" + ENTRY_SOURCE_DN + "]");
            }
        }

        if (SearchFilter == null) {
            SearchFilter = "(objectclass=*)";
        }
        if (VERBOSE) {
            System.out.println(MP + "Search Filter: " + SearchFilter);
        }

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRanalyze FUNCTION = new IRRanalyze(
                IRRHost,
                IRRPrincipal,
                IRRCredentials,
                ENTRY_SOURCE_DN,
                SearchFilter,
                VERBOSE,
                DEBUG,
                true);

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform();
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRanalyze.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.out.println(MP + "FRAMEWORK Incorporated, Copyright 2001");

        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRanalyze


class IRRanalyzeQueue {
    /**
     * IRRanalyzeQueue
     * Class to provide Queue for Threading Analysis of Objects Searches,
     * while main thread performs a subtree walk.
     */
    private int ErrorWalker = 0;
    private int ErrorAnalyzer = 0;

    private boolean VERBOSE = false;

    private idxDNLinkList Queue = new idxDNLinkList();

    /**
     * Pop Entry from Synchronized Analyze Queue.
     *
     * @return current DN to be read and Analyzed.
     */
    public String pop() {

        // ******************************
        // Is there something already in
        // the Queue?
        synchronized (Queue) {
            if (Queue.IsNotEmpty()) {
                String ZpoppedDN = Queue.popfirst();
                if (VERBOSE) {
                    System.out.println("Queue popped: " + ZpoppedDN);
                }
                return (ZpoppedDN);
            } // End of If.
        } // End of Synchronized

        return ("");
    } // End of pop Method.

    /**
     * Push Entry into Synchronized Analyze Queue.
     *
     * @param _ZQDN DN.
     */
    public void push(String _ZQDN) {
        // ************************
        // Place Entry into Queue.
        synchronized (Queue) {
            Queue.addLast(_ZQDN);
        } // End of Synchronized
        if (VERBOSE) {
            System.out.println("Queue pushed: " + _ZQDN);
        }
        return;
    } // End of push Method.

    /**
     * Set Error Indicator Value.
     *
     * @param _x Thread Error Code.
     */
    synchronized void setErrorWalker(int _x) {
        ErrorWalker = _x;
    }

    /**
     * Get Error Indicator Value.
     *
     * @return int Thread Error Code.
     */
    synchronized int getErrorWalker() {
        return (ErrorWalker);
    }

    /**
     * Set Error Indicator Value.
     *
     * @param _x Thread Error Code.
     */
    synchronized void setErrorAnalyzer(int _x) {
        ErrorAnalyzer = _x;
    }

    /**
     * Get Error Indicator Value.
     *
     * @return int Thread Error Code.
     */
    synchronized int getErrorAnalyzer() {
        return (ErrorAnalyzer);
    }

    /**
     * Get Any Existing Error Indicator Value.
     *
     * @return boolean Indicates an Error Does Exist
     *         for one of the Threads.
     */
    synchronized boolean AnyErrors() {
        if (ErrorAnalyzer != 0) {
            return (true);
        } else if (ErrorWalker != 0) {
            return (true);
        }
        return (false);
    }

    /**
     * Set Verbose Indicator.
     *
     * @param _x Verbose Indicator.
     */
    synchronized void setVerbose(boolean _x) {
        VERBOSE = _x;
    }

} // End of Class IRRanalyzeQueue


/**
 * IRRanalyzeWalker
 * Class to run Walker Thread.
 */
class IRRanalyzeWalker implements Runnable, idxCMDReturnCodes {

    /**
     * IRRanalyzeWalker
     * Class to provide Walker interface to Queue.
     */
    IRRanalyzeQueue BBQ;  // Synchronized Object.

    Thread t;

    private static String MP = "IRRanalyzeTreeWalker: ";

    private static idxManageContext IRRSource = null;

    private static idxStatus StatSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    /**
     * IRRanalyzeWalker Contructor class driven.
     *
     * @param BBQ Synchronized Object.
     * @param _IRRHost          Source IRR LDAP URL.
     * @param _IRRPrincipal          Source IRR Principal.
     * @param _IRRCredentials          Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN          Source DN Starting Base for Analyze.
     * @param _SearchFilter          Search Filter.
     * @param _VERBOSE         Indicate Verbosity.
     * @param _DEBUG         Indicate DEBUGGING.
     * @param _ExitOnException         Indicate Exit on Exceptions.
     */
    IRRanalyzeWalker(IRRanalyzeQueue BBQ,
                     String _IRRHost,
                     String _IRRPrincipal,
                     String _IRRCredentials,
                     String _ENTRY_SOURCE_DN,
                     String _SearchFilter,
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
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        //
        this.BBQ = BBQ;
        t = new Thread(this, "IRRanalyzeWalker");
        t.start(); // Start the Thread.
    } // End of Contructor.

    public void run() {

        // ***********************************************
        // Initialize
        long memfree;
        String tname = Thread.currentThread().getName();
        if (VERBOSE) {
            System.out.println(MP + "Thread Established for:[" + tname + "]");
        }


        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        if (VERBOSE) {
            System.out.println(MP + "Attempting Source Walker Directory Connection to Host URL:[" + IRRHost + "]");
        }

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRanalyzeWalker Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            BBQ.setErrorWalker(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            return;
        } // End of exception

        // ************************************************
        // Now Disable DSAE Factories..
        try {
            IRRSource.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            BBQ.setErrorWalker(EXIT_GENERIC_FAILURE);
            return;
        } // End of exception

        // ************************************************
        // Set up our Status Objects
        StatSource = new idxStatus("IRRanalyzeWalker");

        StatSource.setOpStatus(1);

        // **************************************************
        // Obtain IRR Utility Object.
        idxIRRutil util = new idxIRRutil();
        util.setVerbose(VERBOSE);

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        // ****************************************
        // Indicate the Analyze is starting.
        if (VERBOSE) {
            System.out.println(MP + "Starting Analysis...");
        }

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
        if (!BBQ.AnyErrors()) {
            BBQ.push(ENTRY_SOURCE_DN);
        } else {
            return;
        }

        // ****************************************
        // Obtain all of the Children.
        //
        idxDNLinkList myChildrenList = new idxDNLinkList();

        // ***************************
        // Obtain First Level
        try {
            util.ObtainChildrenForQueue(IRRSource.irrctx,
                    ENTRY_SOURCE_DN,
                    SearchFilter,
                    myChildrenList,
                    StatSource);
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception on Obtaining Children Entries,\n" + e);
            BBQ.setErrorWalker(EXIT_GENERIC_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************
        // Obtain all Subsequent Levels
        while (myChildrenList.IsNotEmpty()) {
            String RDN = myChildrenList.popfirst();
            // ****************************************
            // Push the Entry to Output Thread.
            if (!BBQ.AnyErrors()) {
                BBQ.push(RDN);
            } else {
                return;
            }

            try {
                util.ObtainChildrenForQueue(IRRSource.irrctx,
                        RDN,
                        SearchFilter,
                        myChildrenList,
                        StatSource);

                // ****************************************
                // If Verbose, show total Memory.
                if (DEBUG) {
                    memfree = rt.freeMemory();

                    System.out.println(MP + "Current Free Memory: [" +
                            memfree + "].");
                }

            } catch (Exception e) {
                System.err.println(MP + "IRR Exception on Obtaining Child Entries,\n" + e);
                e.printStackTrace();
                BBQ.setErrorWalker(EXIT_GENERIC_FAILURE);
                return; // End Thread.
            } // End of exception

        } // End of While Loop.

        // ***************************************
        // Tell the our Other Thread we are done.
        if (!BBQ.AnyErrors()) {
            BBQ.push("<END THREAD>");
        }

        // ***************************************
        // Close up Shop.
        if (VERBOSE) {
            System.out.println(MP + "Closing Walker Source Directory Context.");
        }
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            BBQ.setErrorWalker(EXIT_IRR_CLOSE_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Done.
        return;

    } // End of run.

} // End of Class IRRanalyzeWalker

/**
 * IRRanalyzer
 * Class to run Output Thread.
 */
class IRRanalyzer implements Runnable, idxCMDReturnCodes {

    /**
     * IRRanalyzer
     * Class to provide Output Thread to read from Queue.
     */
    IRRanalyzeQueue BBQ;

    Thread t;

    private static String MP = "IRRanalyzer: ";

    private static idxManageContext IRRSource = null;

    private static idxStatus StatSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String ENTRY_SOURCE_DN = null;

    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    /**
     * IRRanalyzer Contructor class driven.
     *
     * @param BBQ Synchronized Object.
     * @param _IRRHost          Source IRR LDAP URL.
     * @param _IRRPrincipal          Source IRR Principal.
     * @param _IRRCredentials          Source IRR Credentials.
     * @param _ENTRY_SOURCE_DN          Source DN Starting Base for Analysis.
     * @param _SearchFilter          Search Filter.
     * @param _VERBOSE         Indicate Verbosity.
     * @param _DEBUG         Indicate DEBUGGING.
     * @param _ExitOnException         Indicate Exit on Exceptions.
     */
    IRRanalyzer(IRRanalyzeQueue BBQ,
                String _IRRHost,
                String _IRRPrincipal,
                String _IRRCredentials,
                String _ENTRY_SOURCE_DN,
                String _SearchFilter,
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
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        //
        this.BBQ = BBQ;
        t = new Thread(this, "IRRanalyzer");
        t.start(); // Start the Thread
    } // End of Contructor.

    /**
     * run
     * Thread to Analyze Objects.
     */
    public void run() {

        // ***********************************************
        // Initialize
        String ZEN = null;
        long memfree;
        String tname = Thread.currentThread().getName();
        if (VERBOSE) {
            System.out.println(MP + "Thread Established for:[" + tname + "]");
        }

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        if (VERBOSE) {
            System.out.println(MP + "Attempting Source Object Directory Connection to Host URL:[" + IRRHost + "]");
        }

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRanalyzer Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            BBQ.setErrorAnalyzer(EXIT_IRR_CLOSE_FAILURE);
            return;
        } // End of exception

        // ************************************************
        // Now Disable Factories.
        try {
            IRRSource.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            BBQ.setErrorAnalyzer(EXIT_GENERIC_FAILURE);
            return;
        } // End of exception

        // ************************************************
        // Set up our Status Objects
        StatSource = new idxStatus("IRRanalyzer");

        StatSource.setOpStatus(1);

        // **************************************************
        // Obtain IRR Utility Object.
        idxIRRutil util = new idxIRRutil();
        util.setVerbose(VERBOSE);

        // **************************************************
        // Obtain IRR Analysis Summary Statistics Class.
        IRRAnalysisSummary ASUMMARY = new IRRAnalysisSummary();

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        // **************************************
        // Loop to process commands from Walker
        // Thread.
        while (true) {
            if (!BBQ.AnyErrors()) {
                ZEN = BBQ.pop();
            } else {
                break;
            }

            // ***************************
            // Ignore Null.
            if ((ZEN == null) ||
                    ("".equals(ZEN))) {
                continue;
            }

            // ***************************
            // Should We End Thread?
            if ("<END THREAD>".equals(ZEN)) {
                break;
            }

            // ***********************************
            // Ignore a Comment or simple NewLine
            if ((ZEN.startsWith("#")) ||
                    (ZEN.equals("\n"))) {
                continue;
            }

            // *****************************
            // Ok, this must be a DN, so
            // Process.
            try {
                ObtainEntryForAnalysis(IRRSource.irrctx,
                        ZEN,
                        SearchFilter,
                        StatSource,
                        ASUMMARY);
            } catch (Exception e) {
                System.err.println(MP + "IRR Exception on IRRanalyze, Obtaining Source Entry, " + e);
                BBQ.setErrorAnalyzer(EXIT_GENERIC_FAILURE);
                return; // End Thread.
            } // End of exception

        } // End of While Loop.

        // ***************************************
        // Show Analysis Results.
        System.out.println("\n****\n" +
                "*   <<- FRAMEWORK IRR Analysis Results ->>\n" +
                "****");

        System.out.println("As of " + CurrentTimeStamp.get());
        System.out.println("IRR Host: " + IRRHost);
        System.out.println("IRR Base: " + ENTRY_SOURCE_DN);
        System.out.println("Search Filter Used: " + SearchFilter);

        System.out.println("Analyzed Entries: " +
                StatSource.getCounter("ReadEntries"));

        if ((StatSource.getCounter("ReadEntries") > 0) &&
                (!BBQ.AnyErrors())) {
            System.out.println("Full Analysis was Successful with no Exceptions.");
        } else {
            System.out.println("Full Analysis was NOT Successful, Exceptions Detected.");
        } // end of Else.

        // ***************************************
        // Dump the DIT Analysis.
        System.out.println("\n****\n" +
                "*   <<- FRAMEWORK IRR DIT Analysis ->>\n" +
                "****");
        ASUMMARY.ShowClassificationsForDIT();

        // ***************************************
        // Dump the Data Analysis.
        System.out.println("\n****\n" +
                "*   <<- FRAMEWORK IRR Data Analysis ->>\n" +
                "****");
        ASUMMARY.ShowClassificationsForData();

        // ***************************************

        // ***************************************
        // End Analysis Results.
        System.out.println("\n****\n" +
                "*   <<- End of Results ->>\n" +
                "****");

        // ***************************************
        // Close up Shop.
        if (VERBOSE) {
            System.out.println(MP + "Closing Source Directory Context.");
        }
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            BBQ.setErrorAnalyzer(EXIT_IRR_CLOSE_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Done.
        return;

    } // End of run.

    /**
     * Obtains Entry from Directory Context and performs an Analysis on
     * the entry.
     *
     * @param ctxSource current established Source JNDI Directory Context
     * @param DNSource     DN of source entry.
     * @param SearchFilter     Search Filter.
     * @param _StatusSource  Source Common Status Object.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void ObtainEntryForAnalysis(DirContext ctxSource,
                                       String DNSource,
                                       String SearchFilter,
                                       idxStatus _StatusSource,
                                       IRRAnalysisSummary _ASUMMARY)
            throws idxIRRException {


        //******************************************
        // Initialize Status Counter.
        _StatusSource.setLastOp("ObtainEntryforAnalysis");
        _StatusSource.setLastOpResource(DNSource);
        _StatusSource.setLastOpStatus(-1);

        //******************************************
        // Make sure we have a valid DN.
        // If the Source Entry is Blank or caller
        // trying to obtain ROOT, just ignore and
        // return gracefully.
        //
        if ((DNSource == null) ||
                ("".equals(DNSource)) ||
                (DNSource.equalsIgnoreCase("ROOT"))) {
            return;
        }

        //******************************************
        // Set up our Search Filter.
        if (SearchFilter == null) {
            SearchFilter = "(objectclass=*)";
        }

        //******************************************
        // Set up our Search Controls.
        String[] OCAttr = {"objectclass"};
        SearchControls OS_ctls = new SearchControls();
        OS_ctls.setReturningAttributes(OCAttr);
        OS_ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(DNSource);
        DNSource = Naming_Source.getDNwithQuotes();

        // *****************************************
        // Obtain the Namespace.
        String NameSpace = null;
        try {
            NameSpace = ctxSource.getNameInNamespace();
            if (NameSpace.equals("")) {
                NameSpace = DNSource;
            }
        } catch (Exception e) {
            {
                NameSpace = DNSource;
            }
        } // End of exception

        // *****************************************
        // Now obtain the Source Entry.
        //
        try {
            IRRAnalyzeEntry(ctxSource.search(DNSource, SearchFilter, OS_ctls),
                    DNSource,
                    _ASUMMARY);
            _StatusSource.AccumCounter("ReadEntries");
            _StatusSource.setLastOpStatus(1);

        } catch (NameNotFoundException e) {
            _StatusSource.AccumCounter("ReadErrors");
            _StatusSource.setLastOpStatus(-1);
            throw new idxIRRException("Source DN was not found, in ObtainEntryForAnalysis()");
        } catch (Exception e) {
            e.printStackTrace();
            throw new idxIRRException("Error Performing IRR Analysis Function, in ObtainEntryForAnalysis(),\n" + e);
        } // End of exception

    } // End of ObtainEntryForAnalysis

    /**
     * Analyze Entry based upon Objectclass Type.
     *
     * @param sl  JNDI search results.
     * @param dnbase             Current DN base to properly reflect full DN.
     * @param _ASUMMARY Statistics Class.
     * @throws Exception when unrecoverable error occurs.
     */
    public static void IRRAnalyzeEntry(NamingEnumeration sl,
                                       String dnbase,
                                       IRRAnalysisSummary _ASUMMARY)
            throws Exception, idxIRRException {

        // *****************************************
        // If Empty Enumeration List, simply return.
        if (sl == null) {
            return;
        }

        // *****************************************
        // Analyze Entry.
        SearchResult si = null;
        try {
            if (sl.hasMore()) {
                si = (SearchResult) sl.next();
            } else {
                return;
            }

            // Formulate the DN.
            String DN = null;
            if (dnbase.equals("")) {
                DN = si.getName();
            } else if (si.getName().equals("")) {
                DN = dnbase;
            } else {
                DN = si.getName() + "," + dnbase;
            }

            // ********************************************
            // Obtain the Current DN.
            //
            idxParseDN pDN = new idxParseDN(DN);

            // ********************************************
            // Now Analyze the Current DN for DIT Analysis.
            //
            _ASUMMARY.ClassifyDN(pDN);

            // ********************************************
            // Obtain the entries Attributes.
            Attributes entryattrs = si.getAttributes();

            // ********************************************
            // Obtain only ObjectClass Attributes.
            Attribute eo = entryattrs.get("objectclass");
            for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                // *******************************************
                // Analyze Entry based upon Objectclasses.
                String Aname = eo.getID();
                Object obvalue = eov.next();
                String Avalue = null;
                if (obvalue instanceof String) {
                    Avalue = obvalue.toString();
                } else {
                    continue;
                }

                // *******************************************
                // Now we have the Objectclass Name in Avalue,
                // so analyze it.
                Avalue = Avalue.toLowerCase();
                _ASUMMARY.ClassifyEntry(Avalue);

            } // End of For Loop.


        } catch (NamingException e) {
            System.err.println(MP + "Naming Exception, Cannot continue obtaining search results, " + e);
            throw e;
        } catch (Exception e) {
            System.err.println(MP + "Exception, Cannot continue obtaining search results, " + e);
            e.printStackTrace();
            throw e;
        } // End of Exception

        return;
    } // End of IRRAnalyzeEntry

} ///:~ End of Class IRRanalyzer

/**
 * IRRAnalysisSummary
 * Class to run Output Thread.
 */
class IRRAnalysisSummary {

    private TreeMap<String, EntrySummary> dataTM = new TreeMap<>(); // Data Summary

    private TreeMap<String, DITSummary> ditTM = new TreeMap<>(); // DIT Summary

    private TreeMap<String, ObjectClassSummary> ocTM = new TreeMap<>(); // Objectclass Summary
    private TreeMap<String, AttributeSummary> atTM = new TreeMap<>(); // Attribute Summary

    private int Schema_TotalObjectclasses = 0;
    private int Schema_StructuredObjectclasses = 0;
    private int Schema_AuxObjectclasses = 0;
    private int Schema_AbsObjectclasses = 0;

    private int Schema_TotalAttributes = 0;
    private int Schema_BinaryAttributes = 0;
    private int Schema_DNAttributes = 0;
    private int Schema_UserModifiableAttributes = 0;
    private int Schema_UserNonModifiableAttributes = 0;
    private int Schema_SingleValueAttributes = 0;
    private int Schema_MultiValueAttributes = 0;

    /**
     * Provides Common Analysis Object.
     */
    public IRRAnalysisSummary() {
    } // End of IRRAnalysisSummary Constructor.

    /**
     * Classifies IRR Entry
     */
    public void ClassifyEntry(String _OCname) {
        _OCname = _OCname.toLowerCase();
        EntrySummary es = (EntrySummary) dataTM.get(_OCname);
        if (es == null) {
            es = new EntrySummary();
        }
        es.counter++;
        dataTM.put(_OCname, es);

        ObjectClassSummary os = (ObjectClassSummary) ocTM.get(_OCname);
        if (os != null) {
            os.counter++;
            ocTM.put(_OCname, os);
        } // End of If.

    } // End of ClassifyEntry Method.

    /**
     * Classifies IRR DN.
     */
    public void ClassifyDN(idxParseDN _pDN) {

        int deepth = 0;

        // *********************************
        // Be sure we represent all DIT
        // Containers, if not and the
        // entry has no children, then
        // It will not appear in the summary.
        if ((_pDN.getNamingAttribute().equalsIgnoreCase("ou")) ||
                (_pDN.getNamingAttribute().equalsIgnoreCase("dc")) ||
                (_pDN.getNamingAttribute().equalsIgnoreCase("jsu"))) {
            String X500name = toX500Name(_pDN.getDN().toLowerCase());
            DITSummary ds = (DITSummary) ditTM.get(X500name);
            if (ds == null) {
                ds = new DITSummary();
            }
            ditTM.put(X500name, ds);
        } // End of If.

        // *************************
        // Find the Root DIT Member
        String name = _pDN.getPDN();
        while (true) {
            deepth++;
            idxParseDN xDN = new idxParseDN(name);
            if (!xDN.isValid()) {
                name = "ROOT";
                break;
            }

            if ((xDN.getNamingAttribute().equalsIgnoreCase("ou")) ||
                    (xDN.getNamingAttribute().equalsIgnoreCase("dc")) ||
                    (xDN.getNamingAttribute().equalsIgnoreCase("rcu")) ||
                    (xDN.getNamingAttribute().equalsIgnoreCase("rdu")) ||
                    (xDN.getNamingAttribute().equalsIgnoreCase("jsu"))) {
                name = xDN.getDN();
                break;
            } // End of If.
            else {
                name = xDN.getPDN();
            } // End of Else.
        } // End of While Loop.

        // *******************************************
        // Count the Entries in this DIT Container.
        // Place in as X500name so that it Sorts
        // Properly.
        String X500name = toX500Name(name.toLowerCase());
        DITSummary ds = (DITSummary) ditTM.get(X500name);
        if (ds == null) {
            ds = new DITSummary();
        }
        ds.counter++;
        ds.deepth = deepth;
        ditTM.put(X500name, ds);

    } // End of ClassifyDN Method.

    /**
     * Displays Classifications.
     */
    public void ShowClassificationsForDIT() {

        System.out.println(JLeft("DIT Containers Discovered:", 49) + " " + JRight("Objects", 8));

        Set set = ditTM.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            String Name = (String) e.getKey();
            String FName = Name;
            Integer ec = new Integer(((DITSummary) e.getValue()).counter);
            Integer ed = new Integer(((DITSummary) e.getValue()).deepth);

            if (!Name.equalsIgnoreCase("ROOT")) {
                if (Name.startsWith("/")) {
                    Name = Name.substring(1);
                    if (Name.lastIndexOf("/") > 1) {
                        Name = Name.substring((Name.lastIndexOf("/") + 1));
                    }
                } // End of If.

                int deepth = Count(FName, "/");
                int jl = 48 - deepth;

                System.out.println(" " +
                        JLeft(" ", deepth, ".") +
                        JLeft(Name, jl, ".") + " " +
                        JRight(ec.toString(), 8));
            } else {
                System.out.println(" " + JLeft(Name, 48, ".") + " " +
                        JRight(ec.toString(), 8));
            } // End of Else.

        } // End of While Loop.

    } // End of ShowClassificationsForData Method.

    /**
     * Displays Classifications.
     */
    public void ShowClassificationsForData() {

        System.out.println(JLeft("ObjectClasses Discovered:", 49) + " " + JRight("Count", 8));

        Set set = dataTM.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            String Name = (String) e.getKey();
            Integer ec = new Integer(((EntrySummary) e.getValue()).counter);

            System.out.println(" " + JLeft(Name, 48, ".") + " " +
                    JRight(ec.toString(), 8));
        } // End of While Loop.

    } // End of ShowClassificationsForData Method.

    /**
     * Count Characters in a String.
     *
     * @param _s String incoming LDAP DN.
     * @param _o String to be counted.
     * @return int Number of occurrences.
     */
    private int Count(String _s, String _o) {
        int count = 0;
        String x = _s;
        while (x.indexOf(_o) != -1) {
            count++;
            if ((x.indexOf(_o) + _o.length()) > x.length()) {
                break;
            }
            x = x.substring((x.indexOf(_o) + _o.length()));
        } // End of While Loop.
        return (count);
    } // End of Count Method.

    /**
     * toX500Name.
     *
     * @param _ldapdn String incoming LDAP DN.
     * @return String X500 DN.
     */
    private String toX500Name(String _ldapdn) {
        String X500dn = "";
        StringTokenizer st = new StringTokenizer(_ldapdn, ",");
        while (st.hasMoreTokens()) {
            String ep = st.nextToken();
            X500dn = "/" + ep + X500dn;
        } // End of While Loop.
        return (X500dn);
    } // End of toX500Name Method.

    /**
     * Justify Left a String for Printing.
     *
     * @param _in
     * @param _i    Length
     * @param _gap
     */
    private String JLeft(String _in, int _i, String _gap) {
        while (_in.length() < _i) {
            _in = _in + _gap;
        }
        return (_in);
    } // End of JLeft Method.

    /**
     * Justify Left a String for Printing.
     *
     * @param _in Incoming String.
     * @param _i    Length
     */
    private String JLeft(String _in, int _i) {
        return (JLeft(_in, _i, " "));
    } // End of JLeft Method.

    /**
     * Justify Right a String for Printing.
     *
     * @param _in Incoming String.
     * @param _i    Length
     * @param _gap Gap String.
     */
    private String JRight(String _in, int _i, String _gap) {
        while (_in.length() < _i) {
            _in = _gap + _in;
        }
        return (_in);
    } // End of JRight Method.

    /**
     * Justify Right a String for Printing.
     *
     * @param _in Incoming String.
     * @param _i    Length
     */
    private String JRight(String _in, int _i) {
        return (JRight(_in, _i, " "));
    } // End of JRight Method.

} ///:~ End of IRRAnalysisSummary Class.

/**
 * EntrySummary Object.
 * Class to contain Entry Analysis.
 */
class EntrySummary {

    public int counter = 0;

    /**
     * Provides Analysis Object.
     */
    public EntrySummary() {
    } // End of EntrySummary Constructor.

} ///:~ End of EntrySummary Class.

/**
 * DITSummary Object.
 * Class to contain Entry Analysis.
 */
class DITSummary {

    public int counter = 0;
    public int deepth = 0;

    /**
     * Provides Analysis Object.
     */
    public DITSummary() {
    } // End of DITSummary Constructor.

} ///:~ End of DITSummary Class.

/**
 * AttributeSummary Object.
 * Class to contain Entry Analysis.
 */
class AttributeSummary {

    public int counter = 0;

    /**
     * Provides Analysis Object.
     */
    public AttributeSummary() {
    } // End of AttributeSummary Constructor.

} ///:~ End of AttributeSummary Class.

/**
 * ObjectClassSummary Object.
 * Class to contain Entry Analysis.
 */
class ObjectClassSummary {

    public int counter = 0;

    /**
     * Provides Analysis Object.
     */
    public ObjectClassSummary() {
    } // End of ObjectClassSummary Constructor.

} ///:~ End of ObjectClassSummary Class.

