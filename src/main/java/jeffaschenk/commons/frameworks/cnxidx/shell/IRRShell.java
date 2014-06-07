package jeffaschenk.commons.frameworks.cnxidx.shell;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxTimeStamp;

import java.util.*;
import java.io.*;

/**
 * Frontend class to provide a IRR Shell Environement to run all commands
 * requests and facilities via a single Shell.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

public class IRRShell extends Shell {

    // ******************************************
    // Generic Globals Specific to this Instance.

    // **************************************
    // BackEnd Information
    protected static final String DCDIRECTORY = "DCDIRECTORY";

    // **************************************
    // Helper Scripts.
    protected static final String IRRPOKEICOS = "irrpokeicos";
    protected static final String COMPILESCHEMA = "compileschema";

    // **************************************
    // DCL Admin Executables.
    protected static final String CDSADMIN = "cdsadmin";

    // **************************************
    // CheckPoint Policy Globals.
    protected static final String readCurrentCheckPointPolicy
            = "readCurrentCheckPointPolicy";

    protected static final String setCurrentCheckPointPolicy
            = "setCurrentCheckPointPolicy";

    // **************************************
    // Prompt Information.
    protected static final String IRR_DEFAULT_PROMPT = "irrShell> ";

    // **************************************
    // Specific Return Codes.
    public static int IRR_SHELL_RETURN_CODE_SUCCESSFUL = 0;
    public static int IRR_SHELL_RETURN_CODE_EXIT = 99;

    // **************************************
    // FRAMEWORK Specific Literals.
    // TODO, make a XML Configuration file
    // for other files to be copied during
    // the Backup process and be available for
    // restore.
    public static final String FRAMEWORK_KEYSTORE_FILENAME
            = ".framework.keystore";
    public static final String FRAMEWORK_DIRUSER_FILENAME
            = ".framework.directoryuser";
    public static final String FRAMEWORK_LOGGER_FILENAME
            = ".framework.logger";
    public static final String FRAMEWORK_PROPERTIES_FILENAME
            = ".framework.properties";
    public static final String FRAMEWORK_REGMGR_FILENAME
            = ".framework.registrationmanager";
    public static final String FRAMEWORK_REGMGRBS_FILENAME
            = ".framework.registrationmanagerbootstrap";
    public static final String FRAMEWORK_SERVERINIT_FILENAME
            = ".framework.serverinit";
    public static final String FRAMEWORK_ICOSDB_PROPERTIES_FILENAME
            = "icosdb.properties";
    public static final String FRAMEWORK_ICOSDBLOGGING_PROPERTIES_FILENAME
            = "icosdblogging.properties";
    public static final String FRAMEWORK_PROFILE_FILENAME
            = ".framework_profile";
    public static final String FRAMEWORK_AUTHXML_FILENAME
            = "auth.xml";

    // *********************************************
    // PAUSE Conditions
    public static final int PAUSECONDITION_FAIL = 0;
    public static final int PAUSECONDITION_PROMPT = 1;
    public static final int PAUSECONDITION_CONTINUE = 2;

    // *********************************************
    // Failure Messages to Trap.
    public static final String NMI_BIND_FAILURE
            = "^Bind failed at NMI with error code";

    /**
     * Initial Constructor.
     */
    public IRRShell() {
        this(true);
    } // End of Constructor

    /**
     * Constructor for Specifying Mode.
     */
    public IRRShell(boolean _mode) {
        super(_mode);

        // *****************************
        // Obtain our Ports
        LDAPPORT = System.getProperty("install.dcl.dcd.port", "389");
        DAPPORT = "102";

        // ************************
        // Set The BackEnd.
        setBackEnd(DCDIRECTORY);

        // ************************
        // Set the Shell Prompt.
        setPrompt(IRR_DEFAULT_PROMPT);

    } // End of Constructor

    /**
     * Command Line Interactive Mode Shell.
     */
    public int CMDprocess(String[] _cmdargs)
            throws InterruptedException, IOException {

        // ***************************
        // Convert Array to LinkedList
        LinkedList<String> _cmdargsLL = new LinkedList<>();
        for (int i = 0; i < _cmdargs.length; i++) {
            _cmdargsLL.addLast(_cmdargs[i]);
        }
        // ********************
        // Return
        return (CMDprocess(_cmdargsLL));
    } // End of CMDprocess Method.

    /**
     * Command Line Interactive Mode Shell.
     */
    public int CMDprocess(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {

        // ********************
        // Do we have any
        // Command at All?
        if (_cmdargs.size() < 1) {
            return (0);
        }

        // ********************
        // Check for Command
        String COMMAND = (String) _cmdargs.get(0);
        if (COMMAND.equalsIgnoreCase("start")) {
            return (CMDstart());
        }

        if (COMMAND.equalsIgnoreCase("stop")) {
            return (CMDstop());
        }

        if (COMMAND.equalsIgnoreCase("restart")) {
            return (CMDrestart());
        }

        if (COMMAND.equalsIgnoreCase("echo")) {
            return (CMDecho(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("status")) {
            return (CMDstatus(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("version")) {
            return (CMDversion());
        }

        if (COMMAND.equalsIgnoreCase("dibcount")) {
            return (CMDdibcount());
        }

        if (COMMAND.equalsIgnoreCase("dibsize")) {
            return (CMDdibsize());
        }

        if (COMMAND.equalsIgnoreCase("pause")) {
            return (CMDpause());
        }

        if (COMMAND.equalsIgnoreCase("resume")) {
            return (CMDresume());
        }

        if (COMMAND.equalsIgnoreCase("backup")) {
            return (CMDbackup(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("dibbackup")) {
            return (CMDdibbackup(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("ldifbackup")) {
            return (CMDldifbackup(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("restore")) {
            return (CMDrestore(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("icos")) {
            return (CMDicos(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("startldaptrace")) {
            return (CMDstartldaptrace());
        }

        if (COMMAND.equalsIgnoreCase("stopldaptrace")) {
            return (CMDstopldaptrace());
        }

        if (COMMAND.equalsIgnoreCase("diags")) {
            return (CMDdiags());
        }

        if (COMMAND.equalsIgnoreCase("checkpointpolicy")) {
            return (CMDcheckpointpolicy(_cmdargs));
        }

        // *******************************
        // Check for a Help/usage Command.
        if ((COMMAND.equalsIgnoreCase("help")) ||
                (COMMAND.equalsIgnoreCase("usage")) ||
                (COMMAND.startsWith("?"))) {
            return (CMDusage());
        }

        // ***************************
        // Check for an Exit Command.
        if ((COMMAND.equalsIgnoreCase("exit")) ||
                (COMMAND.equalsIgnoreCase("end")) ||
                (COMMAND.equalsIgnoreCase("quit"))) {
            return (SHELL_RETURN_CODE_EXIT);
        }

        // ***************************
        // If we fell through then we
        // have an invalid command.
        displayMsg("Specified Command '" + COMMAND + "', is Invalid.");
        displayMsg("Please use 'help' for Commands and Usage.");

        // ********************
        // Return
        return (0);
    } // End of CMDprocess Method.

    // **************************************************************
    // Commands
    // **************************************************************

    /**
     * usage command to simply display valid commands.
     */
    public int CMDusage() {
        // **********************
        // Show Usage.
        displayMsg("FRAMEWORK IRR Directory Server Operational Admin Facility");
        displayMsg("Usage: irrshell {function}");
        displayMsg("\tAvailable Functions:");
        displayMsg("\tstart       - Start IRR Directory Server.");
        displayMsg("\tstop        - Stop IRR Directory Server.");
        displayMsg("\trestart     - Restart IRR Directory Server.");
        displayMsg("\tpause       - Pauses IRR Directory Server, Places server in read-only State.");
        displayMsg("\tresume      - Resumes IRR Directory Server Normal Operation from a read-only State.");
        displayMsg("\tstatus      - Current Running Status of IRR Directory Server.");
        displayMsg("\tbackup      - Backup IRR Directory Server DIB, Physical and Logical backups performed.");
        displayMsg("\t                Backup Options when ICOS not Available to Issue Pause to ICOS:");
        displayMsg("\t                --prompt   | --promptonnopause   " +
                " Will Prompt User to Continue Backup or not.");
        displayMsg("\t                --fail     | --failonnopause     " +
                " Will Automatically Fail Backup if unable to Pause ICOS.");
        displayMsg("\t                --continue | --continueonnopause " +
                " Will Continue with Backup if unable to Pause ICOS.");
        displayMsg("\tdibbackup   - Backup Physical DIB of IRR Directory Server, no Pause of ICOS performed.");
        displayMsg("\tldifbackup  - Backup Logical DIB of IRR Directory Server, no Pause of ICOS performed.");
        displayMsg("\ticos status - Show Status of ICOS.");
        displayMsg("\ticos pause  - Pause ICOS.");
        displayMsg("\ticos resume - Resume ICOS.");
        displayMsg("\trestore     - Restore IRR Directory Server DIB from a previous backup.");
        displayMsg("\tdiags       - Run Internal Diagnostics for FRAMEWORK Support.");
        displayMsg("\tstartldaptrace - Start an LDAP API Protocol Trace.");
        displayMsg("\tstopldaptrace  - Stop an LDAP API Protocol Trace and Dump Trace.");
        displayMsg("\tcheckpointpolicy status - Displays the current check point policy.");
        displayMsg("\tcheckpointpolicy set    - Provides Interface to set the Check Point Policy.");
        displayMsg("\tcheckpointpolicy reset  - Resets all CheckPoint Policies to Default Settings.");
        displayMsg("\tcheckpointpolicy help   - Provides information about Check Point Policy.");
        displayMsg("\tversion     - Display Current IRR Directory Version.");

        // **********************
        // Return
        return (0);
    } // End of usage Command.

    /**
     * echo command to simply echo back information to the requestor.
     */
    public int CMDecho(LinkedList<String> _cmdargs) {
        String echoMsg = "";
        for (int i = 1; i < _cmdargs.size(); i++) {
            if ((echoMsg == null) || (echoMsg.equalsIgnoreCase(""))) {
                echoMsg = (String) _cmdargs.get(i);
            } else {
                echoMsg = echoMsg + " " + (String) _cmdargs.get(i);
            }
        } // End of For Loop.

        // **********************
        // Perform the Echo.
        displayMsg(echoMsg);

        // **********************
        // Return
        return (0);
    } // End of echo Command.

    /**
     * Status command to display status of BackEnd Persistence Mechanism and Other Areas.
     */
    public int CMDstatus(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {

        // *****************************
        // Show Status Header.
        displayMsg("FRAMEWORK Status: IRR Directory Server Host: " + "ldap://" +
                HOSTNAME + ":" + LDAPPORT);

        // *****************************
        // Show Process Information
        // for UNIX.
        if (UNIX) {
            String os = System.getProperty("os.name").toLowerCase();
            String PROCESS = "";
            String RPROCESS = "";
            String PROCESSES = "";
            String THREADS = "";

            performExternalCommand("ps", "-e");
            if (lastExternalCMDOutput.size() > 0) {
                int[] awk = {1};
                LinkedList<String> DP = findInList(lastExternalCMDOutput, "dcx500");
                if (DP.size() > 0) {
                    PROCESS = (String) getFromList(DP, awk, 1).get(0);
                } // End of If.
            } // End of If.

            if (PROCESS.equalsIgnoreCase("")) {
                displayMsg("FRAMEWORK Status: IRR Directory Server Process NOT Running.");
            } else {

                if (os.equalsIgnoreCase("LINUX")) {
                    performExternalCommand("ps", "-ef");
                    if (lastExternalCMDOutput.size() > 0) {
                        PROCESSES = Integer.toString(findInList(lastExternalCMDOutput, "dcx500").size());
                    }

                    displayMsg("FRAMEWORK Status: IRR Directory Main Process ID: " + PROCESS + ".");
                    displayMsg("FRAMEWORK Status: IRR Directory Total Processes Running: " + PROCESSES + ".");
                } // End of LINUX Specific.
                else {
                    performExternalCommand("ps", "-Lp " + PROCESS);
                    if (lastExternalCMDOutput.size() > 0) {
                        THREADS = Integer.toString(lastExternalCMDOutput.size());
                    }

                    displayMsg("FRAMEWORK Status: IRR Directory Main Process ID: " + PROCESS + ".");
                    displayMsg("FRAMEWORK Status: IRR Directory Threads Running: " + THREADS + ".");
                } // End of Inner Else.
            } // End of Else.

            // ***************************************
            // Check for Resilience Process
            performExternalCommand("ps", "-e");
            if (lastExternalCMDOutput.size() > 0) {
                int[] awk = {1};
                LinkedList<String> DP = findInList(lastExternalCMDOutput, "dcdoctor");
                if (DP.size() > 0) {
                    RPROCESS = (String) getFromList(DP, awk, 1).get(0);
                } // End of If.
            } // End of If.
            if (RPROCESS.equalsIgnoreCase("")) {
                displayMsg("FRAMEWORK Status: IRR Resilience Process NOT Running.");
            } else {
                displayMsg("FRAMEWORK Status: IRR Resilience Process ID: " + RPROCESS + ", Running.");
            }

        } // End of If UNIX.
        // ******************************
        // Show Process Information
        // for WINDOWS.
        else {
            // TODO - Find a way to obtain the Service Property Information.
            // We need to license PsList or write some C Code to get this information.
        } // End of Else.

        // ***************************************
        // Perform a Netstat and Show
        // Stats.
        performExternalCommand("netstat", "-an");

        if (lastExternalCMDOutput.size() > 0) {
            LinkedList LDAPSTAT = findInList(lastExternalCMDOutput, "[^0-9]" + LDAPPORT);
            LinkedList DAPSTAT = findInList(lastExternalCMDOutput, "[^0-9]" + DAPPORT);
            if (LDAPSTAT.size() > 0) {
                int LDAPLISTENERS = findInList(LDAPSTAT, "LIST").size();
                int LDAPESTABLISHED = findInList(LDAPSTAT, "ESTABLISH").size();
                int LDAPINWAIT = findInList(LDAPSTAT, "WAIT").size();

                displayMsg("FRAMEWORK Status: IRR LDAP Connection LISTENERS: " +
                        LDAPLISTENERS);
                displayMsg("FRAMEWORK Status: IRR LDAP Connections ESTABLISHED: " +
                        LDAPESTABLISHED);
                displayMsg("FRAMEWORK Status: IRR LDAP Connections in WAIT State: " +
                        LDAPINWAIT);

            } // End of If.

            if (DAPSTAT.size() > 0) {
                int DAPLISTENERS = findInList(DAPSTAT, "LIST").size();

                displayMsg("FRAMEWORK Status: IRR DAP Connection LISTENERS: " +
                        DAPLISTENERS);

            } // End of If.
        } // End of If.

        // ***********************
        // Show LOG File Counts.
        CMDshowlogcount();

        // ***********************
        // Show the DIB Count.
        CMDdibcount();

        // ***********************
        // Show the DIB Count.
        CMDdibsize();

        // ****************************
        // Show the Journal Entry Count
        CMDjournalsize();

        // ***********************
        // Show the Version
        CMDversion();

        // **********************
        // Return
        return (0);
    } // End of status Command.

    /**
     * version command to show version of BackEnd Persistence Mechanism.
     */
    public int CMDversion()
            throws InterruptedException, IOException {
        int rc = 0;

        // *******************************
        // Obtain the Run Directory.
        String AFILE = RUNDIR;

        // ********************************
        // Check how to show version?
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            AFILE = AFILE + "/run/dcx500/dcx500.out";

        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            AFILE = AFILE + "\\run\\dcx500\\dcx500.out";
        } // End of Else.

        // *********************************
        // Now Grep the File.
        File f = new File(AFILE);
        Grep gx = new Grep();
        gx.grep(f, ".build");

        // *****************************
        // Dip into the File contents
        // to find the Build Info...
        String VERSTAMP = UNKNOWN;
        if (gx.obtainResults().size() > 0) {
            int[] awk = {4, 5, 6};
            LinkedList<String> DP = findInList(gx.obtainResults(), ".build");
            if (DP.size() > 0) {
                VERSTAMP = (String) getFromList(DP, awk, 1).get(0);
            } // End of If.
        } // End of If.

        displayMsg("FRAMEWORK Status: IRR Directory Server Version: " + VERSTAMP);

        // ***************************
        // Return
        return (rc);
    } // End of version Command Method.

    /**
     * dibcnt command to show DIB Count of BackEnd Persistence Mechanism.
     */
    public int CMDdibcount()
            throws InterruptedException, IOException {
        int rc = 0;

        // ******************************
        // Obtain the DIB Count.
        String DIBCNT = UNKNOWN;
        performExternalCommand("dcdmcnt", "");
        if (lastExternalCMDOutput.size() > 0) {
            int[] awk = {6};
            DIBCNT = (String) getFromList(lastExternalCMDOutput, awk, 1).get(0);
            if (DIBCNT.equalsIgnoreCase("error")) {
                DIBCNT = UNKNOWN;
            }
        } // End of If.

        // *****************************
        // Display the Count.
        displayMsg("FRAMEWORK Status: IRR DIB Entry Count: " + DIBCNT);

        // ***************************
        // Return
        return (rc);
    } // End of dibcount Command Method.

    /**
     * Journal Size to obtain Journal Entry Count.
     */
    public int CMDjournalsize()
            throws InterruptedException, IOException {

        // **********************************
        // This command is not available on
        // WINDOWS environment, since there
        // is a Lock on the File and we will 
        // not be able to run this command.
        if (WINDOWS) {
            return (0);
        }

        // *******************************
        // Obtain the Run Directory.
        String JOURNAL_FILENAME = RUNDIR + File.separator +
                "run" + File.separator +
                "dcx500" + File.separator +
                "journal" + File.separator +
                "JOURNAL.DAT";

        // ******************************
        // Obtain the Journal Size.
        String JSIZE = UNKNOWN;
        performExternalCommand("dcdjlist", JOURNAL_FILENAME);
        if (lastExternalCMDOutput.size() > 0) {
            JSIZE = Integer.toString(
                    findInList(lastExternalCMDOutput, "Dispatcher APDU").size());
        }

        // *****************************
        // Display the Information, if
        // available.
        if (!JSIZE.equalsIgnoreCase(UNKNOWN)) {
            displayMsg("FRAMEWORK Status: IRR Journal Entry Count: " + JSIZE);
        }

        // ***************************
        // Return
        return (0);
    } // End of CMDjournalsize Command Method.

    /**
     * dibsize command to show DIB File Size of BackEnd Persistence Mechanism.
     */
    public int CMDdibsize()
            throws InterruptedException, IOException {
        int rc = 0;

        // *******************************
        // Obtain the Run Directory.
        String DATABASE_DIRNAME = RUNDIR;
        String JOURNAL_DIRNAME = RUNDIR;

        // ********************************
        // Check how to show version?
        if (UNIX) {
            // ********************************
            // Obtain the DirNames on UNIX.
            DATABASE_DIRNAME = DATABASE_DIRNAME + "/run/dcx500/database";
            JOURNAL_DIRNAME = JOURNAL_DIRNAME + "/run/dcx500/journal";
        } else {
            // ******************************
            // Obtain the DirNames on WINDOWS.
            DATABASE_DIRNAME = DATABASE_DIRNAME + "\\run\\dcx500\\database";
            JOURNAL_DIRNAME = JOURNAL_DIRNAME + "\\run\\dcx500\\journal";
        } // End of Else.

        // ******************************
        // Obtain the DIB Size.
        IRRdibSize dibsize = new IRRdibSize(DATABASE_DIRNAME,
                JOURNAL_DIRNAME, false);
        dibsize.obtainsize();

        // ******************************
        // Display the Info.
        displayMsg(dibsize.toString());

        // ***************************
        // Return
        return (rc);
    } // End of dibsize Command Method.

    /**
     * showlogcount command to show LOG Count of BackEnd Persistence Mechanism.
     */
    public int CMDshowlogcount()
            throws InterruptedException, IOException {
        int rc = 0;

        // *******************************
        // Obtain the Run Directory.
        String ALOGDIR = RUNDIR;

        // ********************************
        // Check how to show version?
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            ALOGDIR = ALOGDIR + "/run/dcx500";
        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            ALOGDIR = ALOGDIR + "\\run\\dcx500";
        } // End of Else.

        // ******************************
        // Obtain the Directory Listing.
        int count = 0;
        File ld = new File(ALOGDIR);
        if (ld.isDirectory()) {
            String contents[] = ld.list();
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].startsWith("EV")) {
                    count++;
                }
            } // End of For Loop.
        } // End of If.

        // *****************************
        // Display the Count.
        displayMsg("FRAMEWORK Status: IRR Log Count: " + count);

        // ***************************
        // Return
        return (rc);
    } // End of show log count Command Method.

    /**
     * start command to start BackEnd Persistence Mechanism.
     */
    public int CMDstart()
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":) Starting DSA...");
        // ********************************
        // Check how to start?
        if (UNIX) {
            // ********************************
            // Start the UNIX BackEnd.
            // For the Start Command on *NIX
            // we do not wait for Completion.
            //
            rc = performExternalCommand("dcdstart", "", false, true);
            if (isStartSuccessful()) {
                displayMsg("DSA Instance Startup Successful");
            } else {
                displayMsg("DSA Instance Startup was NOT Successful, Please Check Logs for Reason.");
            }
        } else {
            // ******************************
            // Start the WINDOWS BackEnd.
            rc = performExternalCommand("net start", DCDIRECTORY);
        } // End of Else.

        // ****************************
        // Display Output of Command.
        displayLastSTDOUT();
        displayLastSTDERR();

        // ***************************
        // Return
        return (rc);
    } // End of start Command Method.

    /**
     * stop command to stop BackEnd Persistence Mechanism.
     */
    public int CMDstop()
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":) Stopping DSA...");
        // ********************************
        // Check how to stop?
        if (UNIX) {
            // ********************************
            // Start the UNIX BackEnd.
            rc = performExternalCommand("dcdstop", "");

        } else {
            // ******************************
            // Start the WINDOWS BackEnd.
            rc = performExternalCommand("net stop", DCDIRECTORY);

        } // End of Else.

        // ****************************
        // Display Output of Command.
        displayLastSTDOUT();
        displayLastSTDERR();

        // ***************************
        // Return
        return (rc);
    } // End of stop Command Method.

    /**
     * restart command to stop and then start BackEnd Persistence Mechanism.
     */
    public int CMDrestart()
            throws InterruptedException, IOException {
        int rc = -1;
        rc = CMDstop();
        rc = CMDstart();
        // ***************************
        // Return
        return (rc);
    } // End of start Command Method.

    /**
     * pause command to pause BackEnd Persistence Mechanism.
     */
    public int CMDpause()
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":) Pausing DSA...");

        // ********************************
        // Check how to pause?
        if (UNIX) {
            // ********************************
            // Pause the UNIX BackEnd.
            rc = performExternalCommand("dcdpause", "");
            if (lastExternalCMDOutput.size() > 0) {
                if ((findInList(lastExternalCMDOutput,
                        NMI_BIND_FAILURE).size() > 0) ||
                        (findInList(lastExternalCMDError,
                                NMI_BIND_FAILURE).size() > 0)) {
                    displayMsg("Pause Not Successful, since Directory is not Running.");
                } else {
                    // ****************************
                    // Display Output of Command.
                    displayLastSTDOUT();
                    displayLastSTDERR();
                } // End of Else.
            } // End of Check for any Output from out Pause.
        } else {
            // ******************************
            // Start the WINDOWS BackEnd.
            rc = performExternalCommand("net pause", DCDIRECTORY);
            // ****************************
            // Display Output of Command.
            displayLastSTDOUT();
            displayLastSTDERR();
        } // End of Else.

        // ***************************
        // Return
        return (rc);
    } // End of pause Command Method.

    /**
     * resume command to Resume Normal Operations of BackEnd Persistence Mechanism.
     */
    public int CMDresume()
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":) Resuming DSA...");

        //	 *******************************
        // Check how to resume?
        if (UNIX) {
            // ********************************
            // Pause the UNIX BackEnd.
            rc = performExternalCommand("dcdresume", "");
            if (lastExternalCMDOutput.size() > 0) {
                if ((findInList(lastExternalCMDOutput,
                        NMI_BIND_FAILURE).size() > 0) ||
                        (findInList(lastExternalCMDError,
                                NMI_BIND_FAILURE).size() > 0)) {
                    displayMsg("Resume Not Successful, since Directory is not Running.");
                } else {
                    // ****************************
                    // Display Output of Command.
                    displayLastSTDOUT();
                    displayLastSTDERR();
                } // End of Else.
            } // End of Check for any Output from Resume.
        } else {
            // ******************************
            // Resume the WINDOWS BackEnd.
            rc = performExternalCommand("net continue", DCDIRECTORY);
            // ****************************
            // Display Output of Command.
            displayLastSTDOUT();
            displayLastSTDERR();
        } // End of Else.

        // ***************************
        // Return
        return (rc);
    } // End of resume Command Method.

    /**
     * Backup command to  backup the Backend Store.
     * We perform an ICOS Pause, Backup, then ICOS Resume.
     */
    public int CMDbackup(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {
        int rc = -1;

        // **********************************
        // Initialize.
        LinkedList<String> ourcmdargs = new LinkedList<>();
        boolean PAUSEPERFORMED = false;

        // **********************************
        // Obtain any Options regarding the
        // Pause option in case we fail.
        int PAUSECONDITION = PAUSECONDITION_PROMPT;
        String PAUSECONDITION_NAME = "Prompt";
        for (int ci = 0; ci < _cmdargs.size(); ci++) {
            String cmdoption =  _cmdargs.get(ci);
            if ((cmdoption == null) ||
                    (cmdoption.trim().equalsIgnoreCase(""))) {
                continue;
            } else if ((cmdoption.equalsIgnoreCase("-continueonnopause")) ||
                    (cmdoption.equalsIgnoreCase("-continue"))) {
                PAUSECONDITION = PAUSECONDITION_CONTINUE;
                PAUSECONDITION_NAME = "Continue";
            } // End of Else if.

            else if ((cmdoption.equalsIgnoreCase("-failonnopause")) ||
                    (cmdoption.equalsIgnoreCase("-fail"))) {
                PAUSECONDITION = PAUSECONDITION_FAIL;
                PAUSECONDITION_NAME = "Fail";
            } // End of Else If.

            else if ((cmdoption.equalsIgnoreCase("-promptonnopause")) ||
                    (cmdoption.equalsIgnoreCase("-prompt"))) {
                PAUSECONDITION = PAUSECONDITION_PROMPT;
                PAUSECONDITION_NAME = "Prompt";
            } // End of Else If.

            // ***********************************
            // Falling through we have an Option
            // specifically for Backup, so save it.
            else {
                ourcmdargs.add(cmdoption);
            }
        } // End of Check for Pause Conditional Flags.

        // **********************************
        // Pause ICOS
        displayMsg("Attempting Pause of ICOS to initiate Backup, will " +
                PAUSECONDITION_NAME + " If unsuccessful...");
        rc = PauseIcosForBackup();

        // ***********************************
        // Check for the Condition.
        if (rc == 0) {
            displayMsg("Pause of ICOS Successful.");
            PAUSEPERFORMED = true;
        } else if (rc == 100) {
            displayMsg("Pause of ICOS Not Successful, due to System Unavailable, proceeding with Backup.");
            PAUSEPERFORMED = false;
        } else {
            // *************************************
            // Pause was Unsuccessful, so we need to
            // determine what course of action the
            switch (PAUSECONDITION) {
                // ***************************
                // Continue.
                case PAUSECONDITION_CONTINUE:
                    displayMsg("ICOS PAUSE Not Successful, Assuming due to System Unavailable, but proceeding with Backup.");
                    break;

                // ***************************
                // Prompt User
                case PAUSECONDITION_PROMPT:
                    displayMsg("ICOS PAUSE Not Successful, Assuming due to System Unavailable, Do you wish to proceed with Backup? (Y|N)");
                    if (super.CMDPrompt()) {
                        displayMsg("ICOS Backup will continue per operator request.");
                    } else {
                        displayMsg("ICOS Backup will NOT continue per operator request.");
                        return (202);
                    } // End of Else.
                    break;

                // ***************************
                // Fail
                case PAUSECONDITION_FAIL:
                default:
                    displayMsg("*** WARNING UNABLE TO PAUSE ICOS, BACKUP UNABLE TO PROCEED, RC: " + rc);
                    return (rc);
            } // End of Switch.
        } // End of Else.

        // ******************************************
        // Now Perform Physical Backup of Directory.
        rc = CMDdibbackup(ourcmdargs);

        // *******************************************
        // Now RESUME ICOS.
        if (PAUSEPERFORMED) {
            displayMsg("Attempting Resume of ICOS...");
            rc = ResumeIcosForBackup();
            if (rc == 0) {
                displayMsg("ICOS Resume Successful.");
            } else if (rc == 100) {
                displayMsg("Resume of ICOS Not Successful, due to System Unavailable.");
            } else {
                displayMsg("*** WARNING UNABLE TO RESUME ICOS, NOTIFY FRAMEWORK SUPPORT, RC: " + rc);
                return (203);
            } // End of Resuming ICOS.
        } // End of Resumption of ICOS.
        else {
            displayMsg("ICOS was not Resumed, since it was not Paused.");
        } // End of Else.

        // ***************************************
        // Return
        return (rc);
    } // End of backup Command Method.

    /**
     * DIBBackup command to backup the Backend Store, without a pause or communications to ICOS.
     */
    public int CMDdibbackup(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {
        int rc = -1;

        // *****************************************
        // Show Start Message.
        displayMsg(":) FRAMEWORK Backup: Running...");

        // *****************************************
        // Obtain our Current TimeStamp.
        idxTimeStamp TIMESTAMP = new idxTimeStamp();
        TIMESTAMP.enableLocalTime();

        // ***************************************
        // Obtain the User Backup Directory if
        // one is supplied.
        // This is Optional.
        String USERBACKUPDIR = this.obtainUserBackupDir(_cmdargs);

        // *****************************************
        // Initialize.
        String OUTDIR = new String();
        String BDIR = new String();
        String BFILENAME = new String();
        //String IDXENV    = new String();

        // *********************************************
        // Establish Output Directory.
        if ((USERBACKUPDIR != null) &&
                (!USERBACKUPDIR.trim().equalsIgnoreCase(""))) {
            OUTDIR = USERBACKUPDIR;
        } else {
            OUTDIR = RUNASDIR + File.separator + "BACKUP";
        }

        // *****************************************
        // Formulate a Backup Directory File Name.
        BDIR = OUTDIR + File.separator +
                "IRRBACKUP." + HOSTNAME + "." + TIMESTAMP.get();

        // *********************************************
        // Perform Setup.
        if (UNIX) {
            // *****************************************
            // Formulate for UNIX.
            BFILENAME = BDIR + File.separator + "DATABASE.DAT";
            //IDXENV    = BDIR+File.separator+"IDXENV.tar";
        } else {
            // ******************************
            // Formulate for WINDOWS.
            BFILENAME = BDIR + File.separator + "DATABASE.DAT";
            //IDXENV    = BDIR+File.separator+"IDXENV.zip";
        } // End of Else.

        // ************************************
        // Now Create a Directory for this new
        // Backup Directory.
        File fBDIR = new File(BDIR);
        fBDIR.mkdirs();

        // ************************************
        // Remove any existing Backup Files
        // from this Directory.
        File fBACKUPFILE = new File(BFILENAME);
        if (fBACKUPFILE.exists()) {
            fBACKUPFILE.delete();
        }

        // ************************************
        // Specify the Log Files.
        String LOG = BDIR + File.separator + "IRRBackup.log";
        //String ERR = BDIR+File.separator+"IRRBackup.err"; // not used...

        // *****************************************
        // Show the Backup Destination and Log File.
        displayMsg("IRR Backup Destination:[" + BDIR + "]");
        displayMsg("IRR Backup Logfile Destination:[" + LOG + "]");

        // ***********************************
        // Start by Saving off all of the
        // FRAMEWORK Environment.
        displayMsg("Saving Current FRAMEWORK Environment Files...");

        // ***********************************
        // Backup FRAMEWORK All Relevant
        // FRAMEWORK Configuration Files.
        backupFile(FRAMEWORK_KEYSTORE_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_DIRUSER_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_LOGGER_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_PROPERTIES_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_REGMGR_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_REGMGRBS_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_SERVERINIT_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_ICOSDB_PROPERTIES_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_ICOSDBLOGGING_PROPERTIES_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_PROFILE_FILENAME, HOMEDIR, BDIR);

        backupFile(FRAMEWORK_AUTHXML_FILENAME, HOMEDIR, BDIR);

        // ***********************************
        // Perform Physical DIB Backup.
        displayMsg("IRR Physical Backup Starting...");
        if (UNIX) {
            rc = performExternalCommand("dcbckdib", "BACKUP " + BDIR);
        } else {
            rc = performExternalCommand("dcbckdib", "BACKUP \"" + BDIR + "\"");
        }

        // *************************************
        // Ok Now create a LOG File from
        // our LinkedList LOG Streams.
        saveLogs(LOG);

        // *************************************
        // Check out return code for this part.
        if (rc != 0) {
            displayMsg("*** WARNING IRR Physical Backup NOT Successful. (" + rc + ")");
            displayMsg("See FRAMEWORK Physical Backup Run Log:" + LOG + "\n");
            return (rc);
        } else {
            displayMsg("IRR Physical Backup Successful.");
        } // End of Else.

        // *************************************
        // Now Confirm by checking the DCX500
        // Output file.
        if (isBackupSuccessful()) {
            displayMsg("IRR Physical Backup Successfully Confirmed.");
        } else {
            displayMsg("*** WARNING IRR Physical Backup NOT Successful.");
            displayMsg("*** FRAMEWORK Additional Error Information:");

            // ********************************
            // Now Show the Contents of the
            // Error Information...
            String AFILE = RUNDIR;
            if (UNIX) {
                // ********************************
                // Obtain the File on UNIX.
                AFILE = AFILE + "/run/dcx500/dcbckdib.out";
            } else {
                // ******************************
                // Obtain the File on WINDOWS.
                AFILE = AFILE + "\\run\\dcx500\\dcbckdib.out";
            } // End of Else.

            // *****************************
            // Now Show the Contents.
            File fOUTPUTFILE = new File(AFILE);
            if (fOUTPUTFILE.exists()) {
                showFile(fOUTPUTFILE);
            }
        } // End of Else.

        // **********************************
        // Show Backup Directory Contents.
        displayMsg("IRR Backup File System Location:[" + BDIR + "]");
        if (UNIX) {
            rc = performExternalCommand("ls", "-la " + BDIR);
        } else {
            rc = performExternalCommand("dir", "\"" + BDIR + "\"");
        }
        displayLastSTDOUT();

        // **********************************
        // Return.
        return (rc);
    } // End of dibbackup Command Method.

    /**
     * Restore command to restore the Backend Store from a dibbackup or backup.
     */
    public int CMDrestore(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":) FRAMEWORK Restore Running...");

        // *************************************
        // Check for incoming Arguments.
        String USERBACKUPDIR = this.obtainUserBackupDir(_cmdargs);

        // *************************************
        // Did we have a User Backup Directory
        // Specified?  If not, simple exit.
        if (USERBACKUPDIR == null) {
            displayMsg("*** WARNING No Backup File System Directory Specified!");
            displayMsg("*** WARNING Please specify the File System Directory which contains the DATA to be Restored.");
            return 1;
        } // End of If Check for incoming Restore Directory.

        // ***************************************
        // Ok, we have something, so check to make
        // sure this is a Directory and it contains
        // a DIB Image Backup.
        displayMsg("FRAMEWORK Restore: Checking File System Directory:" + USERBACKUPDIR);
        File fRDIR = new File(USERBACKUPDIR);
        if (!fRDIR.exists()) {
            displayMsg("FRAMEWORK Restore: Specified Backup File System Directory does not exist.");
            displayMsg("FRAMEWORK Restore: Unable to Continue, Terminating.");
            return 1;
        } // End of Check for Archive Contents.  

        // ************************************
        // Check for  existing Backup Files
        // from this Directory.
        String BFILENAME = USERBACKUPDIR + File.separator + "DATABASE.DAT";
        File fBACKUPFILE = new File(BFILENAME);
        if (!fBACKUPFILE.exists()) {
            displayMsg("FRAMEWORK Restore: DIB Archive Does not Exist.");
            displayMsg("FRAMEWORK Restore: Unable to Continue, Terminating.");
            return 1;
        } // End of Check for Archive Contents.     

        // **************************************
        // Now prompt the user for one last time 
        // to allow customer to abort or proceed
        // with the restore.
        displayMsg("FRAMEWORK Restore: This will completely restore the IRR DIB from a previous DIB Backup.");
        displayMsg("FRAMEWORK Restore: All Information on the existing DIB will be Lost!");
        displayMsg("");

        displayMsg("FRAMEWORK Restore: Restore of IRR DIB, using Backup Located in: " +
                USERBACKUPDIR);
        displayMsg("FRAMEWORK Restore: Please confirm this Restore Operation: [Y|N]?");
        if (super.CMDPrompt()) {
            displayMsg("FRAMEWORK Restore: Starting Restore of IRR DIB...");
        } else {
            displayMsg("FRAMEWORK Restore: Operation Aborted.");
            return 1;
        } // End of Prompt Check.

        // ************************************
        // Specify the Log Files.
        String LOG = USERBACKUPDIR + File.separator + "IRRRestore.log";
        //String ERR = USERBACKUPDIR+File.separator+"IRRRestore.err"; // not used...

        // ************************************
        // Stop the Directory Instance
        CMDstop();

        // ************************************
        // Perform the DIB Restore.
        displayMsg("FRAMEWORK Restore Physical DIB Restore Starting...");
        if (UNIX) {
            rc = performExternalCommand("dcbckdib", "/y RESTORE " + USERBACKUPDIR);
        } else {
            rc = performExternalCommand("dcbckdib", "/y RESTORE \"" + USERBACKUPDIR + "\"");
        }

        // *************************************
        // Ok Now create a LOG File from
        // our LinkedList LOG Streams.
        saveLogs(LOG);

        // *************************************
        // Check out return code for this part.
        if (rc != 0) {
            displayMsg("*** WARNING IRR Physical Restore NOT Successful. (" + rc + ")");
            displayMsg("See FRAMEWORK Physical Restore Run Log:" + LOG + "\n");
            return (rc);
        } else {
            displayMsg("FRAMEWORK Restore: IRR Physical Restore Successful.");
        } // End of Else.

        // *************************************
        // Now Confirm by checking the DCX500
        // Output file.
        if (isRestoreSuccessful()) {
            displayMsg("IRR Physical Restore Successfully Confirmed.");
        } else {
            displayMsg("*** WARNING IRR Physical Restore NOT Successful.");
            displayMsg("*** FRAMEWORK Additional Error Information:");

            // ********************************
            // Now Show the Contents of the
            // Error Information...
            String AFILE = RUNDIR;
            if (UNIX) {
                // ********************************
                // Obtain the File on UNIX.
                AFILE = AFILE + "/run/dcx500/dcbckdib.out";
            } else {
                // ******************************
                // Obtain the File on WINDOWS.
                AFILE = AFILE + "\\run\\dcx500\\dcbckdib.out";
            } // End of Else.

            // *****************************
            // Now Show the Contents.
            File fOUTPUTFILE = new File(AFILE);
            if (fOUTPUTFILE.exists()) {
                showFile(fOUTPUTFILE);
            }
        } // End of Else.

        // ********************************************
        // Now Restore the existing schema.
        displayMsg("FRAMEWORK Restore: Re-Compiling IRR Directory Schema...");
        CompileDirectorySchema();

        // *********************************************
        // Check for any Error Files associated
        // with the Schema Compile.
        String AFILE = RUNDIR;
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            AFILE = AFILE + "/run/dcx500/config/icosmsf.txt.err";
        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            AFILE = AFILE + "\\run\\dcx500\\icosmsf.txt.err";
        } // End of Else.

        // *****************************
        // Now Show the Contents.
        File fOUTPUTFILE = new File(AFILE);
        if (fOUTPUTFILE.exists()) {
            displayMsg("*** WARNING Schema Compilation Failed.");
            displayMsg("*** Details of Schema Compliation Errors:");
            showFile(fOUTPUTFILE);
        } // End of Show of Error File.
        else {
            displayMsg("FRAMEWORK Restore: Directory Schema Compliation Successful.");
        } // End of Else.

        // *********************************************
        // Now prompt the customer to determine if the
        // FRAMEWORK Environment files should be restored
        // or Not.
        displayMsg("FRAMEWORK Restore: Do you wish FRAMEWORK Environment Files Restored: [Y|N]?");
        if (!super.CMDPrompt()) {
            displayMsg("FRAMEWORK Restore: Environment Files will not be Restored.");
        } // End of Prompt Check.

        // *************************************
        // Restore the FRAMEWORK
        // Environment Files.
        else {
            // ***********************************
            // Restore FRAMEWORK All Relevant
            // FRAMEWORK Configuration Files.
            restoreFile(FRAMEWORK_KEYSTORE_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_DIRUSER_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_LOGGER_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_PROPERTIES_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_REGMGR_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_REGMGRBS_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_SERVERINIT_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_ICOSDB_PROPERTIES_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_ICOSDBLOGGING_PROPERTIES_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_PROFILE_FILENAME, USERBACKUPDIR, HOMEDIR);

            restoreFile(FRAMEWORK_AUTHXML_FILENAME, USERBACKUPDIR, HOMEDIR);

        } // End of FRAMEWORK Restore Environment.

        // ***********************************
        // Now Prompt if Directory Should be
        // Restarted at this time.
        displayMsg("FRAMEWORK Restore: Do you wish the Directory Restarted, for Normal Operation: [Y|N]?");
        if (!super.CMDPrompt()) {
            displayMsg("FRAMEWORK Restore: IRR Directory will not be restarted at this time.");
            displayMsg("FRAMEWORK Restore: Be sure to Start IRR Directory Server when Ready to resume Normal Operation.");
            return 0;
        } // End of Prompt Check.

        // *************************************
        // Ok, start the Directory.
        rc = CMDstart();
        return (rc);
    } // End of restore Command Method.

    /**
     * ldifbackup command to backup the Backend Store in LDIF Mode,
     * without a pause or communications to ICOS.
     */
    public int CMDldifbackup(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":( Command not currently Implemented within IRRShell.");

        // ***************************
        // Return
        return (rc);
    } // End of ldifbackup Command Method.

    /**
     * icos command to communicate with FRAMEWORK ICOS.
     * This will invoke a common command shell named irrpokeicos
     * with the appropreiate command arguments.
     */
    public int CMDicos(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {
        int rc = -1;

        // *****************************************
        // Obtain the Function
        String Function = "status";
        if (_cmdargs.size() >= 2) {
            Function = (String) _cmdargs.get(1);
        }

        // *****************************************
        // Obtain the Reason.
        String Reason = "none";
        if (_cmdargs.size() >= 3) {
            Reason = (String) _cmdargs.get(2);
        }

        // *****************************************
        // Formulate a Command to be Run..
        String CMDname = ICOSDIR + File.separator + "bin" + File.separator + IRRPOKEICOS;

        // *****************************************
        // Execute the Command
        rc = performExternalCommand(CMDname, Function + " " + "\"" + Reason + "\"");

        // *****************************************
        // Show the Result Output.
        displayLastSTDOUT();
        displayLastSTDERR();

        // ***************************
        // Return
        return (rc);
    } // End of icos Command Method.

    /**
     * Start LDAP Trace.
     */
    public int CMDstartldaptrace()
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":) Starting LDAP Trace...");

        // *******************************
        // Obtain the Run Directory.
        String ALOGDIR = RUNDIR;

        // ********************************
        // Check how to show version?
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            ALOGDIR = ALOGDIR + "/run/dcx500";
            rc = performExternalCommand("dcdaptrc", "start ldap");
        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            ALOGDIR = ALOGDIR + "\\run\\dcx500";
            rc = performExternalCommand("dcdaptrc", "start ldap");
        } // End of Else.

        // ****************************
        // Display Output of Command.
        if (DEBUG) {
            displayLastSTDOUT();
            displayLastSTDERR();
        } // End of DEBUG

        // *************************
        // Perform the Diags
        displayMsg("LDAP Trace Started Successfully.");

        // ***************************
        // Show current FileSystem
        // Directory Listing of
        // the Diag Files.
        displayMsg("Current Available Trace Files:");
        showDirListing(ALOGDIR, "^AP", true);

        // ***************************
        // Return
        return (rc);
    } // End of StartLDAPTrace Command Method.

    /**
     * Stop LDAP Trace.
     */
    public int CMDstopldaptrace()
            throws InterruptedException, IOException {
        int rc = -1;
        displayMsg(":) Stopping LDAP Trace...");

        // *******************************
        // Obtain the Run Directory.
        String ALOGDIR = RUNDIR;

        // ********************************
        // Check how to show version?
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            ALOGDIR = ALOGDIR + "/run/dcx500";
            rc = performExternalCommand("dcdaptrc", "stop ldap");
            displayMsg(":) Dumping LDAP Trace...");
            rc = performExternalCommand("dcdaptrc", "dump");
        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            ALOGDIR = ALOGDIR + "\\run\\dcx500";
            rc = performExternalCommand("dcdaptrc", "stop ldap");
            displayMsg(":) Dumping LDAP Trace...");
            rc = performExternalCommand("dcdaptrc", "dump");
        } // End of Else.

        // ****************************
        // Display Output of Command.
        if (DEBUG) {
            displayLastSTDOUT();
            displayLastSTDERR();
        } // End of DEBUG

        // *************************
        // Perform the Diags
        displayMsg("LDAP Trace Stopped and Dumped Successfully.");

        // ***************************
        // Show current FileSystem
        // Directory Listing of
        // the Diag Files.
        displayMsg("Current Available Trace Files:");
        showDirListing(ALOGDIR, "^AP", true);

        // ***************************
        // Return
        return (rc);
    } // End of StopLDAPTrace Command Method.

    /**
     * Perform Diagnostics for Problem determination.
     */
    public int CMDdiags()
            throws InterruptedException, IOException {
        int rc = -1;

        // *******************************
        // Obtain the Run Directory.
        String ALOGDIR = RUNDIR;

        // *************************
        // Perform the Diags
        displayMsg(":) Performing Diagnostics...");

        // ********************************
        // Check how to show version?
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            ALOGDIR = ALOGDIR + "/run/dcx500";
            rc = performExternalCommand("dcddiags", "");
        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            ALOGDIR = ALOGDIR + "\\run\\dcx500";
            rc = performExternalCommand("CMD", "/c dcddiags");
        } // End of Else.

        // ****************************
        // Display Output of Command.
        if (DEBUG) {
            displayLastSTDOUT();
            displayLastSTDERR();
        } // End of DEBUG

        // *************************
        // Perform the Diags
        displayMsg("Diagnostics Obtained Successfully.");

        // ***************************
        // Show current FileSystem
        // Directory Listing of
        // the Diag Files.
        displayMsg("Current Available Diagnostic Directories:");
        showDirListing(ALOGDIR, "^DG");

        // ***************************
        // Return
        return (rc);
    } // End of diags Command Method.

    /**
     * checkpointpolicy provides user interface to display current policy and
     * allow modification of the check point policy.
     */
    public int CMDcheckpointpolicy(LinkedList<String> _cmdargs)
            throws InterruptedException, IOException {
        int rc = 0;

        // *****************************************
        // Obtain the Function
        String Function = "status";
        if (_cmdargs.size() >= 2) {
            Function = (String) _cmdargs.get(1);
        }

        // *****************************************
        // Check for a Help or info subcommand.
        if ((Function.equalsIgnoreCase("info")) ||
                (Function.equalsIgnoreCase("help"))) {
            displayMsg(CheckPointPolicy.Overview_Information);
            displayMsg(CheckPointPolicy.Policy_Description_Header);
            displayMsg(CheckPointPolicy.getPolicyDescriptions());
            displayMsg(CheckPointPolicy.General_Information);
            return rc;
        } // End of Check for Help/info Function.

        // *****************************************
        // Always get Current Check Point Policy
        // before we do anything.
        CheckPointPolicy ccpp = this.obtainCurrentCheckPointPolicy();

        // *****************************************
        // Show the Current Status.
        displayMsg(ccpp.toString());
        displayMsg(CheckPointPolicy.General_Information);
        displayMsg("");

        // ********************************************
        // Was a Reset Requested?
        if (Function.equalsIgnoreCase("reset")) {
            rc = this.resetCurrentCheckPointPolicy(ccpp);
            if (rc == 0) {
                displayMsg(" * Reset of Check Point Policy Successful.");
            } else {
                displayMsg(" * Reset of Check Point Policy Not Successful!");
                displayLastSTDOUT();
                displayLastSTDERR();
            } // End of Inner Else.
            ccpp = this.obtainCurrentCheckPointPolicy();
            displayMsg(ccpp.toString());
        } // End of If Check.

        // ********************************************
        // Are we Done? Or did the User want to change
        // it?
        else if (Function.equalsIgnoreCase("set")) {
            // *********************************************
            // Now we must prompt the user to change
            // each CheckPoint Policy Attribute.
            rc = this.setCurrentCheckPointPolicy(ccpp);
            if (rc == 0) {
                displayMsg(" * Set of Check Point Policy Successful.");
            } else if (rc == -1) {
                displayMsg(" * Set of Check Point Policy Aborted.");
            } else {
                displayMsg(" * Set of Check Point Policy Not Successful!");
                displayLastSTDOUT();
                displayLastSTDERR();
            } // End of Inner Else.
            ccpp = this.obtainCurrentCheckPointPolicy();
            displayMsg(ccpp.toString());
        } // End of Else.

        // ***************************
        // Return
        return (rc);
    } // End of dibsize Command Method.


    // ******************************************************
    // PRIVATE METHODS
    // ******************************************************

    /**
     * Private method to Pause ICOS for Backup.
     */
    private int PauseIcosForBackup()
            throws InterruptedException, IOException {
        int rc = -1;

        // *****************************************
        // Formulate a Command to be Run..
        String CMDname = ICOSDIR + File.separator + "bin" + File.separator + IRRPOKEICOS;

        // *****************************************
        // Execute the Command
        String Function = "pauserc";
        String Reason = "IRR_BACKUP_Running";
        performExternalCommand(CMDname, Function + " " + "\"" + Reason + "\"");

        // *****************************************
        // Now obtain the Stacked Return Code
        // in the STDOUT Stack.
        rc = obtainReturnCodeFromLastSTDOUT();

        // ***************************
        // Return
        return (rc);
    } // End of Private PauseIcosForBackup Method.

    /**
     * Private method to Resume ICOS for Backup.
     */
    private int ResumeIcosForBackup()
            throws InterruptedException, IOException {
        int rc = -1;

        // *****************************************
        // Formulate a Command to be Run..
        String CMDname = ICOSDIR + File.separator + "bin" + File.separator + IRRPOKEICOS;

        // *****************************************
        // Execute the Command
        String Function = "resumerc";
        String Reason = "IRR_BACKUP_Completed";
        performExternalCommand(CMDname, Function + " " + "\"" + Reason + "\"");

        // *****************************************
        // Now obtain the Stacked Return Code
        // in the STDOUT Stack.
        rc = obtainReturnCodeFromLastSTDOUT();

        // ***************************
        // Return
        return (rc);
    } // End of Private ResumeIcosForBackup Method.

    /**
     * Private method to Compile Directory Schema after Restore.
     */
    private int CompileDirectorySchema()
            throws InterruptedException, IOException {
        int rc = -1;

        // *****************************************
        // Formulate a Command to be Run..
        String CMDname = ICOSDIR + File.separator + "bin" + File.separator + COMPILESCHEMA;

        // *****************************************
        // Execute the Command
        rc = performExternalCommand(CMDname, "");

        // ***************************
        // Return 
        return (rc);
    } // End of Private CompileDirectorySchema Method.

    /**
     * Private method to Obtain the Existing CheckPoint Policy.
     */
    private CheckPointPolicy obtainCurrentCheckPointPolicy()
            throws InterruptedException, IOException, FileNotFoundException {

        // *****************************************
        // Formulate a File Name for our TXI FIle.
        TXFile current_script
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.readCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXI_TYPE);

        // *****************************************
        // Now Write the Script to perform Query
        // to obtain policy attributes.
        current_script.put(CheckPointPolicy.getReadScript());

        // ******************************************
        // Now Execute the Command.  CDSADMIN, always
        // returns a zero '0' code, so we must inspect
        // the Output in the stack.  But we actually
        // will let the CheckPointPolicy Object
        // do all that for us.
        performExternalCommand(IRRShell.CDSADMIN,
                IRRShell.readCurrentCheckPointPolicy);

        // *****************************************
        // Formulate Response and Error TXFiles for
        // Check Point Policy Object to be
        // Instantiated.
        TXFile current_script_output
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.readCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXO_TYPE);

        TXFile current_script_error
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.readCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXE_TYPE);

        // ***************************
        // Return
        return (new CheckPointPolicy(current_script_output, current_script_error));
    } // End of Private obtainCurrentCheckPointPolicy Method.

    /**
     * Private method to Obtain the Existing CheckPoint Policy.
     */
    private int resetCurrentCheckPointPolicy(CheckPointPolicy checkpointpolicy)
            throws InterruptedException, IOException, FileNotFoundException {

        // *****************************************
        // Initialize.
        int rc = 0;

        // *****************************************
        // Formulate a File Name for our TXI FIle.
        TXFile current_script
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.setCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXI_TYPE);

        // *****************************************
        // Now force the ControlPointPolicy Object
        // to Rset all Attributes to default values.
        checkpointpolicy.reset();

        // *****************************************
        // Now Write the Script to perform reset.
        current_script.put(checkpointpolicy.getSetScript());

        // ******************************************
        // Now Execute the Command.  CDSADMIN, always
        // returns a zero '0' code, so we must inspect
        // the Output in the stack.
        performExternalCommand(IRRShell.CDSADMIN,
                IRRShell.setCurrentCheckPointPolicy);

        // *****************************************
        // Formulate Response and Error TXFiles for
        // Check Point Policy Object to be
        // Instantiated.
        TXFile current_script_output
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.setCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXO_TYPE);

        TXFile current_script_error
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.setCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXE_TYPE);

        // ***************************
        // Verify if the Command was
        // successful or not.
        if ((!current_script_output.verifyContents()) &&
                (!current_script_error.verifyContents())) {
            rc = 1;
        }

        // ***************************
        // Return
        return rc;
    } // End of Private resetCurrentCheckPointPolicy Method.

    /**
     * Private method to Set the Existing CheckPoint Policy.
     */
    private int setCurrentCheckPointPolicy(CheckPointPolicy checkpointpolicy)
            throws InterruptedException, IOException, FileNotFoundException {

        // *****************************************
        // Initialize.
        int rc = 0;

        // *****************************************
        // Formulate a File Name for our TXI FIle.
        TXFile current_script
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.setCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXI_TYPE);

        // ********************************************
        // Now prompt the user for the new values for
        // each policy Attribute.
        displayMsg("");
        displayMsg(CheckPointPolicy.getPolicyDescriptions());
        displayMsg("");
        displayMsg(" Please Enter for each Prompt the value for the Policy attribute.");
        displayMsg(" If you wish to remove an attribute, enter 'NONE' for the value.");
        displayMsg("");

        // *********************************************
        // Now Prompt for Each Field.
        checkpointpolicy.setPolicyAChkOnModifies(
                this.obtainNewCheckPointPolicySetting(
                        checkpointpolicy.getPolicyAChkOnModifies(),
                        CheckPointPolicy.PolicyAChkOnModifies_Name));

        checkpointpolicy.setPolicyAChkQuietSecs(
                this.obtainNewCheckPointPolicySetting(
                        checkpointpolicy.getPolicyAChkQuietSecs(),
                        CheckPointPolicy.PolicyAChkQuietSecs_Name));

        checkpointpolicy.setPolicyAChkAtTimeOfDaySecs(
                this.obtainNewCheckPointPolicySetting(
                        checkpointpolicy.getPolicyAChkAtTimeOfDaySecs(),
                        CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Name));

        checkpointpolicy.setPolicyAChkEverySecs(
                this.obtainNewCheckPointPolicySetting(
                        checkpointpolicy.getPolicyAChkEverySecs(),
                        CheckPointPolicy.PolicyAChkEverySecs_Name));

        // *****************************************
        // Now show the values one more time
        // before we commit.
        displayMsg("");
        displayMsg(" Values Accepted, please confirm the new Values Below:");
        displayMsg(checkpointpolicy.toString());
        displayMsg("");

        displayMsg("Do you wish to proceed with the new Values? (Y|N)");
        if (!super.CMDPrompt()) {
            return -1;
        }

        // *****************************************
        // Now Write the Script to perform reset.
        current_script.put(checkpointpolicy.getSetScript());

        // ******************************************
        // Now Execute the Command.  CDSADMIN, always
        // returns a zero '0' code, so we must inspect
        // the Output in the stack.
        performExternalCommand(IRRShell.CDSADMIN,
                IRRShell.setCurrentCheckPointPolicy);

        // *****************************************
        // Formulate Response and Error TXFiles for
        // Check Point Policy Object to be
        // Instantiated.
        TXFile current_script_output
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.setCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXO_TYPE);

        TXFile current_script_error
                = new TXFile(this.getDCX500ConfigDirName() + File.separator +
                IRRShell.setCurrentCheckPointPolicy +
                TXFile.TX_SEP + TXFile.TXE_TYPE);

        // ***************************
        // Verify if the Command was
        // successful or not.
        if ((!current_script_output.verifyContents()) &&
                (!current_script_error.verifyContents())) {
            rc = 1;
        }

        // ***************************
        // Return
        return rc;
    } // End of Private setCurrentCheckPointPolicy Method.

    /**
     * Helper Method to Prompt for new Check Point Policy setting.
     *
     * @param defaultvalue
     * @param policyname
     * @return enteredvalue
     */
    private int obtainNewCheckPointPolicySetting(int defaultvalue,
                                                 String policyname) {
        // ********************************************
        // Prompt and verify we got something.
        displayMsg("Enter Value for " + policyname + ":");
        String svalue = this.CMDPromptForString();
        if ((svalue == null) ||
                (svalue.trim().equalsIgnoreCase(""))) {
            return defaultvalue;
        }

        // *********************************************
        // Now check to see if the value specified
        // was None.
        if ((svalue.equalsIgnoreCase(IRRShell.NONE)) ||
                (svalue.toLowerCase().startsWith(IRRShell.NONE))) {
            return CheckPointPolicy.Default_RemoveValue;
        }

        // *********************************************
        // Formulate a Number from the String
        // and return.
        try {
            return Integer.parseInt(svalue);
        } catch (NumberFormatException nfe) {
            return defaultvalue;
        } // End of Exception Processing.
    } // End of obtainNewCheckPointPolicySetting Private Method.

    /**
     * Private method to Obtain the Existing CheckPoint Policy.
     */
    private String getDCX500ConfigDirName() {
        // *****************************************
        // Formulate Proper Directory
        // File Name for the DCX500 Config Directory.
        return this.RUNDIR + File.separator + "run" + File.separator +
                "dcx500" + File.separator + "config";
    } // End of Private getDCX500ConfigDirName Method.

    /**
     * isStartSuccessful command to indicate if the BackEnd Persistence Mechanism has started.
     */
    private boolean isStartSuccessful()
            throws InterruptedException, IOException {

        // *******************************
        // Obtain the Run Directory.
        String AFILE = RUNDIR;

        // ********************************
        // Check how to show version?
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            AFILE = AFILE + "/run/dcx500/dcx500.out";

        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            AFILE = AFILE + "\\run\\dcx500\\dcx500.out";
        } // End of Else.

        // *********************************
        // Now Grep the File.
        File f = new File(AFILE);
        Grep gx = new Grep();
        gx.grep(f, "DC Directory Server initialised");

        // *****************************
        // Dip into the File contents
        if (gx.obtainResults().size() > 0) {
            return (true);
        }

        // ***************************
        // Return
        return (false);
    } // End of isStartSuccessful Command Method.

    /**
     * isBackupSuccessful command to indicate if the BackUp Was Successful.
     */
    private boolean isBackupSuccessful()
            throws InterruptedException, IOException {

        // *******************************
        // Obtain the Run Directory.
        String AFILE = RUNDIR;

        // ********************************
        // Check for File Organization.
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            AFILE = AFILE + "/run/dcx500/dcbckdib.out";

        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            AFILE = AFILE + "\\run\\dcx500\\dcbckdib.out";
        } // End of Else.

        // *********************************
        // Now Grep the File.
        File f = new File(AFILE);
        Grep gx = new Grep();
        gx.grep(f, "DC Directory Server Archive operation succeeded");

        // *****************************
        // Dip into the File contents
        if (gx.obtainResults().size() > 0) {
            return (true);
        }

        // ***************************
        // Return
        return (false);
    } // End of isBackupSuccessful Command Method.


    /**
     * isRestoreSuccessful command to indicate if the Restore Was Successful.
     */
    private boolean isRestoreSuccessful()
            throws InterruptedException, IOException {

        // *******************************
        // Obtain the Run Directory.
        String AFILE = RUNDIR;

        // ********************************
        // Check for File Organizqation.
        if (UNIX) {
            // ********************************
            // Obtain the File on UNIX.
            AFILE = AFILE + "/run/dcx500/dcbckdib.out";

        } else {
            // ******************************
            // Obtain the File on WINDOWS.
            AFILE = AFILE + "\\run\\dcx500\\dcbckdib.out";
        } // End of Else.

        // *********************************
        // Now Grep the File.
        File f = new File(AFILE);
        Grep gx = new Grep();
        gx.grep(f, "succeeded");

        // *****************************
        // Dip into the File contents
        if (gx.obtainResults().size() > 0) {
            return (true);
        }

        // ***************************
        // Return
        return (false);
    } // End of isRestoreSuccessful Command Method.

    /**
     * obtainUserBackupDir
     * Obtains and cleans up the specfied User Backup directory.
     */
    private String obtainUserBackupDir(final LinkedList arguments) {

        String userdir = null;
        // *************************************
        // Check for incoming Arguments.
        try {
            if ((arguments != null) &&
                    (arguments.size() >= 1)) {
                userdir = (String) arguments.get(1);
            }
        } catch (Exception ubde) {
            userdir = null;
        } // End of Exception Process to Obtain Argument.

        // *********************************************
        // Check for a Trailing Slash.
        if ((userdir != null) &&
                (userdir.length() > 1) &&
                ((userdir.endsWith(File.separator)) ||
                        (userdir.endsWith(System.getProperty("file.separator"))))) {
            userdir = userdir.substring(0, (userdir.length() - 1));
        }

        // *********************************************
        // Remove any Trailing Quotes
        if ((userdir != null) &&
                (userdir.endsWith("\042"))) {
            userdir = userdir.substring(0, (userdir.length() - 1));
        }

        // *********************************************
        // Remove any Leading Quotes
        if ((userdir != null) &&
                (userdir.startsWith("\042"))) {
            userdir = userdir.substring(1);
        }

        // *********************************************
        // Remove any Trailing Blanks
        if (userdir != null) {
            userdir = userdir.trim();
        }

        // **************************************
        // Return User Directory if Specified.
        return userdir;
    } // end of obtainuserBackupDir.

    // ******************************************************
    // MAIN
    // ******************************************************

    /**
     * main to provide command line capability.
     */
    public static void main(String[] args) {

        // **********************
        // Check the Command Line
        // Argument for a "-i" or
        // \i option to specify
        // interactive mode.
        if ((args.length == 1) &&
                ((args[0].equalsIgnoreCase("-i")) ||
                        (args[0].equalsIgnoreCase("--interactive")) ||
                        (args[0].equalsIgnoreCase("/i")))) {
            IRRShell shell = new IRRShell(false);
            System.exit(shell.CMDInteractiveShell());
        } // End of Interactive Shell Check.

        // *********************
        // Establish new 
        // Batch Shell to process
        // Single Command.
        //
        IRRShell shell = new IRRShell();
        try {
            // *********************
            // Process Command
            System.exit(shell.CMDprocess(args));
        } catch (Exception x) {
            System.err.println(x);
        } // End of Exception.
    } // End of Main.

} ///~ End of IRRShell Class
