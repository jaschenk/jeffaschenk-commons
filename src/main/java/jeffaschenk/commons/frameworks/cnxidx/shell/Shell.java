package jeffaschenk.commons.frameworks.cnxidx.shell;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;

import java.util.*;
import java.util.regex.*;
import java.io.*;

import java.net.InetAddress;

/**
 * Frontend class to provide a IRR Shell Environement to run all commands
 * requests and facilities via a single Shell.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

public class Shell {

    // **************************************
    // Generic Globals.
    public String HOSTNAME = null;
    public String RUNDIR = null;
    public String LDAPPORT = null;
    public String DAPPORT = null;
    public String RUNASDIR = null;
    public String RUNASUSERNAME = null;
    public String HOMEDIR = null;
    public String ICOSDIR = null;

    // **************************************
    // Last External Command Output
    public LinkedList<String> lastExternalCMDOutput = new LinkedList<>();
    public LinkedList<String> lastExternalCMDError = new LinkedList<>();

    // **************************************
    // Indicators
    public boolean BatchMode = true;
    public boolean VERBOSE = true;
    public boolean DEBUG = false;
    public boolean UNIX = true;
    public boolean WINDOWS = false;

    // **************************************
    // BackEnd Information
    public String BackEndName = "";

    // **************************************
    // Prompt Information.
    public static final String DEFAULT_PROMPT = "irrShell> ";
    public String Prompt = DEFAULT_PROMPT;

    // **************************************
    // Common Return Codes.
    public static int SHELL_RETURN_CODE_SUCCESSFUL = 0;
    public static int SHELL_RETURN_CODE_EXIT = 99;

    // **************************************
    // OS Specific Literals.
    public static final String WIN_RUNDLL32 = "rundll32 SHELL32.DLL,ShellExec_RunDLL";
    public static final String WIN_RUNCMD = "cmd.exe /C";

    // **************************************
    // Global Literals.
    public static final String UNKNOWN = "UNKNOWN";
    public static final String NONE = "NONE";

    // **************************************
    // String Formatting Utility Object
    public StringFormat SF = new StringFormat();

    /**
     * Initial Constructor.
     */
    public Shell() {
        this(true);
    } // End of Constructor

    /**
     * Constructor for Specifying Mode.
     */
    public Shell(boolean _mode) {
        BatchMode = _mode;
        determineOS();

        // *************************
        // Obtain Hostname.
        HOSTNAME = getHostName();

        // *************************
        // Obtain Environment
        // Globals.
        RUNDIR = System.getProperty("user.dsae.directory.dir", "");
        RUNASDIR = System.getProperty("user.dsae.runas.home.dir", "");
        RUNASUSERNAME = System.getProperty("user.dsae.runas.username", "");
        HOMEDIR = System.getProperty("user.home", "");
        ICOSDIR = System.getProperty("user.icos.dir", "");
    } // End of Constructor

    /**
     * Set Prompt for this Shell.
     *
     * @param String Prompt String.
     */
    public void setPrompt(String _prompt) {
        Prompt = _prompt;
    } // End of setPrompt Method.

    /**
     * Set BackEnd for this Shell.
     *
     * @param String BackEnd Name String.
     */
    public void setBackEnd(String _BackEndName) {
        BackEndName = _BackEndName;
    } // End of setBackEnd Method.

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

    /**
     * Command Line Interactive Mode Shell.
     */
    public int CMDInteractiveShell() {
        BatchMode = false;

        // ***************************
        // Gain Access to Our
        // System Input.
        Reader r = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer st = new StreamTokenizer(r);

        // ***************************
        // Customize our Tokenizer.
        st.resetSyntax();
        st.wordChars('0', '9');       // Make digit chars Word Characters.
        st.wordChars('a', 'z');       // Word Characters.
        st.wordChars('A', 'Z');       // Word Characters.
        st.wordChars('!', '!');       // ! as a Word Character.
        st.wordChars('(', '/');       // ( ) * + , - . / as Word Characters.
        st.wordChars('$', '&');       // $ % & as Word Characters.
        st.wordChars(':', '@');       // : ; < = > ? @ as Word Characters.
        st.wordChars('[', '`');       // [ \ ] ^ _ ` as Word Characters.
        st.wordChars('{', '~');       // { | } ~ as Word Characters.
        st.quoteChar('"');            // Double Quote.
        st.quoteChar('\047');         // Single Quote.
        st.eolIsSignificant(true);    // End Of Line is Significant.
        st.slashStarComments(false);   // Slash Star Comments Ignored.
        st.slashSlashComments(false);  // C-C++ Slash Comments Ignored.
        st.commentChar('#');          // Pound Sign, Single Line Comment.
        st.whitespaceChars(0, ' ');

        // **************************
        // Show Initial Prompt
        displayMsg("Framework Interactive Shell Session Started.");
        displayPrompt();
        int cmdrc = 0;
        LinkedList<String> CMDLINE = new LinkedList<>();
        boolean ILOOP = true;
        try {
            while (ILOOP) {
                switch (st.nextToken()) {
                    case StreamTokenizer.TT_NUMBER:
                        Double num = new Double(st.nval);
                        CMDLINE.addLast(num.toString());
                        break;
                    case StreamTokenizer.TT_WORD:
                        CMDLINE.addLast(st.sval);
                        break;
                    case StreamTokenizer.TT_EOL:
                        // **********************
                        // Process the Request
                        try {
                            cmdrc = CMDprocess(CMDLINE);
                        } catch (Exception cx) {
                            displayErrorMsg(cx.getMessage());
                            displayErrorMsg(cx.toString());
                            cx.printStackTrace();
                        } // End of Exception

                        // **********************
                        // Check for Exit...
                        if (cmdrc == SHELL_RETURN_CODE_EXIT) {
                            ILOOP = false;
                            break;
                        } // End of Exit If.

                        // ***********************
                        // Ready for Next Command.
                        CMDLINE.clear();
                        displayPrompt();
                        break;
                    case StreamTokenizer.TT_EOF:
                        ILOOP = false;
                        break;
                    default:
                        if ((st.sval != null) &&
                                (!st.sval.equalsIgnoreCase(""))) {
                            CMDLINE.addLast("\042" + st.sval + "\042");
                        }
                        break;
                } // End of Switch
            } // End of While Loop.

        } catch (IOException ioe) {
            // won't happen too often from the keyboard
        } // End Of Exception.

        // *****************************
        // Return from Interactive Shell
        displayMsg("Framework Interactive Shell Session Complete.");
        return (0);

    } // End of CMDInteractiveShell.

    /**
     * Command Line Prompt to obtain a Yes|No, True|False Indication.
     */
    public boolean CMDPrompt() {
        // ***********************
        // Initialize.

        // ***************************
        // Gain Access to Our
        // System Input.
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        // ************************
        // Loop to Obtain Data.
        String response = null;
        while (response == null) {
            try {
                response = r.readLine();
            } catch (IOException ioe) {
                // Ignore Exception.
            } // End of Exception Processing.
        } // End of While Loop Obtaining Data from StdInput.

        // *************************
        // Now Check the Response.
        if ((response.trim().equalsIgnoreCase("yes")) ||
                (response.trim().equalsIgnoreCase("y")) ||
                (response.trim().equalsIgnoreCase("true")) ||
                (response.trim().equalsIgnoreCase("t"))) {
            return true;
        }

        // *****************************
        // Falling Through we have a
        // False Condition.
        return false;
    } // End of CMDPrompt.

    /**
     * Command Line Prompt to obtain a String.
     */
    public String CMDPromptForString() {
        // ***********************
        // Initialize.

        // ***************************
        // Gain Access to Our
        // System Input.
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        // ************************
        // Loop to Obtain Data.
        String response = null;
        while (response == null) {
            try {
                response = r.readLine();
            } catch (IOException ioe) {
                // Ignore Exception.
            } // End of Exception Processing.
        } // End of While Loop Obtaining Data from StdInput.

        // *****************************
        // Return the String Data.
        return response.trim();
    } // End of CMDPromptForString Method..

    /**
     * Set indicators for OS we are running on.
     */
    public void determineOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("windows")) {
            UNIX = false;
            WINDOWS = true;
        } // End of If.
        else {
            UNIX = true;
            WINDOWS = false;
        } // End of Else.
    } // End of determineOS Method.

    /**
     * getHostName Helper Method for Methods.
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException uhe) {
            return ("localhost");
        } // End of Exception.
    } // End of getHostName Method.

    /**
     * Display Prompt in common format.
     */
    public void displayPrompt() {
        System.out.print(Prompt);
    } // End of displayPrompt Method.

    /**
     * Display Messages in common format.
     */
    public void displayMsg(String _message) {
        if (_message == null) {
            return;
        }
        if (!VERBOSE) {
            return;
        }
        // **************************
        // Display Message.
        //if (!BatchMode) { System.out.print(Prompt); }
        System.out.println(_message);
    } // End of displayMsg Method.

    /**
     * Display Error Messages in common format.
     */
    public void displayErrorMsg(String _message) {
        if (_message == null) {
            return;
        }
        // **************************
        // Display Message.
        //if (!BatchMode) { System.out.print(Prompt); }
        System.out.println("** " + _message);
    } // End of displayMsg Method.

    /**
     * Perform an External Command Function, wait until completed while obtaining the results.
     *
     * @param String Command To be Executed.
     * @param String Arguments for the Command, Space Seperated.
     * @return int Return code from External Command.
     */
    public int performExternalCommand(String _ExecName, String _ExecArgs)
            throws InterruptedException, IOException {
        return (performExternalCommand(_ExecName, _ExecArgs, true, true));
    } // End of performExternalCommand Method.

    /**
     * Perform an External Command Function, wait until completed while obtaining the results.
     *
     * @param String  Command To be Executed.
     * @param String  Arguments for the Command, Space Seperated.
     * @param boolean Indicator whether or not to obtain output from Command completion.
     * @param boolean Indicator whether or not to wait for Command completion.
     * @return int Return code from External Command.
     */
    public int performExternalCommand(final String _ExecName, final String _ExecArgs,
                                      final boolean _obtainoutput, final boolean _wait)
            throws InterruptedException, IOException {

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // *************************************
        // Initialize the results.
        lastExternalCMDOutput.clear();
        lastExternalCMDError.clear();
        int lastExternalRC = 0;

        // ************************************
        // Perform the External Command.
        try {
            String zExecName = _ExecName;
            if (WINDOWS) {
                zExecName = WIN_RUNCMD + " " + _ExecName;
            }

            Process p =
                    Runtime.getRuntime().exec(zExecName + " " + _ExecArgs);
            BufferedReader se = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            BufferedReader so = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));

            // ******************************
            // Wait for Completion.
            if (_wait) {
                if (_obtainoutput) {
                    // **************************
                    // Obtain the Output.
                    String buf;
                    while ((buf = so.readLine()) != null) {
                        lastExternalCMDOutput.addLast(buf);
                    }

                    // **************************
                    // Obtain the Error Messages.
                    while ((buf = se.readLine()) != null) {
                        lastExternalCMDError.addLast(buf);
                    }
                } // End of obtain output If.

                // **************************
                // Wait for Completion.
                lastExternalRC = p.waitFor();
            } // End of Wait

        } catch (IOException ioe) {
            // **************************************
            // If we receive an Error Code = 2,
            // then we have a file not found, or a
            // command not found condition.
            displayMsg("*** IOE:" + ioe.getMessage());
            throw (ioe);
        } // End of Trap of IOException      

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // **************************
        // Return
        return (lastExternalRC);

    } ///:~ End of performExternalCommand Method.

    /**
     * Find in List a String and Return List of Responses from the Stack.
     */
    public LinkedList<String> findInList(LinkedList _LL, String _pattern) {
        LinkedList<String> resultsList = new LinkedList<>();

        // *********************
        // Compile Our Pattern.
        Pattern pattern = null;
        Matcher pm = null;
        try {
            pattern = Pattern.compile(_pattern);
        } catch (PatternSyntaxException pse) {
            displayErrorMsg("Error Using RegEx Pattern in findinList Method, " + pse.getMessage());
            return (resultsList);
        } // End of Exception.

        // **********************
        // Scan our Link List
        // for the Expression.
        //
        for (int i = 0; i < _LL.size(); i++) {
            if (pm == null) {
                pm = pattern.matcher((String) _LL.get(i));
            } else {
                pm.reset((String) _LL.get(i));
            }
            if (pm.find()) {
                resultsList.addLast((String) _LL.get(i));
            } // End of find if.
        } // End of For Loop.

        // *******************
        // Return.
        return (resultsList);
    } // End of findInList Method.

    /**
     * Find in List a String and Return List of Responses from the Stack.
     */
    public LinkedList getFromList(LinkedList<String> _LL, int[] _tokens, int _max) {
        LinkedList<String> resultsList = new LinkedList<>();

        // **********************
        // Scan our Link List
        // for the Expression.
        //
        for (int i = 0; i < _LL.size(); i++) {
            if (i >= _max) {
                break;
            }

            int b = 0;
            String nstr = "";
            StringTokenizer st = new StringTokenizer((String) _LL.get(i));
            while (st.hasMoreTokens()) {
                String NT = st.nextToken();
                b++;
                for (int j = 0; j < _tokens.length; j++) {
                    if (b == _tokens[j]) {
                        if (nstr.equalsIgnoreCase("")) {
                            nstr = NT;
                        } else {
                            nstr = nstr + " " + NT;
                        }
                        break;
                    } // End of Token Hit.
                } // End of For Loop.
            } // End of Tokenizer While.
            // ****************************************
            // Stuff the new String into the new List.
            resultsList.addLast(nstr);
        } // End of Outer For Loop.

        // *******************
        // Return.
        return (resultsList);
    } // End of getFromList Method.

    /**
     * Show a Directory Listing using a Pattern, with no sizes.
     */
    public int showDirListing(String _dirname, String _pattern) {
        return (showDirListing(_dirname, _pattern, false));
    } // End of showDirListing Method.

    /**
     * Show a Directory Listing using a Pattern.
     */
    public int showDirListing(String _dirname, String _pattern, boolean _showsize) {

        // *********************
        // Compile Our Pattern.
        Pattern pattern = null;
        Matcher pm = null;
        try {
            pattern = Pattern.compile(_pattern);
        } catch (PatternSyntaxException pse) {
            displayErrorMsg("Error Using RegEx Pattern in showDirList Method, " + pse.getMessage());
            return (0);
        } // End of Exception.


        // ******************************
        // Obtain the Directory Listing.
        int count = 0;
        File ld = new File(_dirname);
        if (!ld.isDirectory()) {
            return (0);
        }


        String contents[] = ld.list();
        for (int i = 0; i < contents.length; i++) {
            if (pm == null) {
                pm = pattern.matcher((String) contents[i]);
            } else {
                pm.reset((String) contents[i]);
            }
            // ********************************
            // Display the Filtered Contents
            if (pm.find()) {
                count++;
                File DGD = new File(_dirname +
                        File.separator +
                        contents[i]);

                String type = "-";
                if (DGD.isDirectory()) {
                    type = "d";
                }

                if (_showsize) {
                    Long bytesize = new Long(DGD.length());
                    displayMsg(type + " " +
                            SF.JRight(bytesize.toString(), 10) +
                            " " +
                            getTimeStamp(DGD.lastModified()) +
                            " " +
                            DGD.getAbsolutePath());
                } else {
                    displayMsg(type + " " +
                            getTimeStamp(DGD.lastModified()) +
                            " " +
                            DGD.getAbsolutePath());
                } // End of If Else.


            } // End of find if.
        } // End of For Loop.

        // *******************
        // Return.
        return (count);
    } // End of showDirListing Method.

    /**
     * getFTS will obtain a timestamp suitable for printing
     * in a Log file.
     *
     * @return String the value containing the timestamp in the for of
     *         <B>YYYY-MM-DY.HH:MM:SS</B>
     *         All timestamps generated will be relative to GMT+0 if LOCALTIME
     *         is set to false.
     */
    public String getTimeStamp(long _ms) {

        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

        String myTimeStamp = "";

        Calendar rightNow = Calendar.getInstance();
        if (_ms > 0) {
            rightNow.setTimeInMillis(_ms);
        }

        // ***************************
        // Formulate the Time Stamp.
        year = rightNow.get(Calendar.YEAR);
        myTimeStamp = String.valueOf(year);

        myTimeStamp = myTimeStamp.concat("-");

        month = rightNow.get(Calendar.MONTH) + 1;
        if (month <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(String.valueOf(month));

        myTimeStamp = myTimeStamp.concat("-");

        day = rightNow.get(Calendar.DAY_OF_MONTH);
        if (day <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(String.valueOf(day));

        myTimeStamp = myTimeStamp.concat(".");

        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if (hour <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(String.valueOf(hour));

        myTimeStamp = myTimeStamp.concat(":");

        minute = rightNow.get(Calendar.MINUTE);
        if (minute <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(String.valueOf(minute));

        myTimeStamp = myTimeStamp.concat(":");

        second = rightNow.get(Calendar.SECOND);
        if (second <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(String.valueOf(second));

        return (myTimeStamp);

    } // End of getTimeStamp Method.

    /**
     * Display STDOUT from Command.
     */
    public void displayLastSTDOUT() {
        for (int i = 0; i < lastExternalCMDOutput.size(); i++) {
            displayMsg((String) lastExternalCMDOutput.get(i));
        }
    } // End of displayLastSTDOUT Method.

    /**
     * Display Error from Command.
     */
    public void displayLastSTDERR() {
        for (int i = 0; i < lastExternalCMDError.size(); i++) {
            displayErrorMsg((String) lastExternalCMDError.get(i));
        }
    } // End of displayLastSTDERR Method.    

    /**
     * Obtain a Return Code Value from the STDOUT Stream.
     */
    public int obtainReturnCodeFromLastSTDOUT() {
        int rc = -1;
        for (int i = 0; i < lastExternalCMDOutput.size(); i++) {
            String rcstr = (String) lastExternalCMDOutput.get(i);
            try {
                rc = Integer.parseInt(rcstr);
            } catch (NumberFormatException nfe) {
                rc = -1;  // Ignore it.
            } // End of Exception Processing
        } // End of For Loop.
        // ****************************
        // Return a Numeric Value.
        return rc;
    } // End of obtainReturnCodeFromLastSTDOUT Method.

    /*
     * Write Logs to a Real LOG File.
     */
    public void saveLogs(final String LogFileName) {

        // ***************************************
        // Open up our Log File for Writing....
        // Always Append.
        try {
            BufferedWriter LOGOUT = new BufferedWriter(
                    new FileWriter(LogFileName, true));

            // ***************************************
            // Loop to write out Standard Output.
            for (int i = 0; i < lastExternalCMDOutput.size(); i++) {
                LOGOUT.write((String) lastExternalCMDOutput.get(i));
                LOGOUT.newLine();
            } // End of For Loop.

            // ***************************************
            // Loop to write out Error Output.
            for (int i = 0; i < lastExternalCMDError.size(); i++) {
                LOGOUT.write((String) lastExternalCMDError.get(i));
                LOGOUT.newLine();
            } // End of For Loop.

            // ***************************************
            // Now Close up the Log File.
            LOGOUT.flush();
            LOGOUT.close();

        } catch (IOException ioe) {
            displayErrorMsg("IOException encountered when Saving Log File:[" + ioe + "]");
            ioe.printStackTrace();
        } // End of Exception Processing.
    } // End of saveLogs Method. 


    /**
     * backupFile from source file to new destination file.
     * Wraps the copyFile Method.
     */
    public void backupFile(String filename, String srcdir, String destdir) throws IOException {
        // ***********************************
        // Copy a File from one location to
        // another for Backup.
        try {
            long bc = this.copyFile(new File(srcdir + File.separator + filename),
                    new File(destdir + File.separator + filename));
            if (bc > 0) {
                displayMsg("Backup of " + filename + ", Successful, File Size:[" + bc + "] Bytes");
            } else {
                displayMsg("Backup of " + filename + ", Not Successful!");
            }
        } catch (FileNotFoundException fnotfound) {
            //displayMsg("Backup of "+filename+", not performed, since source file does not exist.");
        } // End of Exception.
    } // End of backupFile.


    /**
     * restoreFile from a backup source file to new destination file.
     * Wraps the copyFile Method.
     */
    public void restoreFile(String filename, String srcdir, String destdir) throws IOException {
        // ***********************************
        // Copy a File from one location to
        // another for Restoration.
        try {
            long bc = this.copyFile(new File(srcdir + File.separator + filename),
                    new File(destdir + File.separator + filename));
            if (bc > 0) {
                displayMsg("Restore of " + filename + ", Successful, File Size:[" + bc + "] Bytes");
            } else {
                displayMsg("restore of " + filename + ", Not Successful!");
            }
        } catch (FileNotFoundException fnotfound) {
            //displayMsg("Restore of "+filename+", not performed, since backup file does not exist.");
        } // End of Exception.
    } // End of copyFile.

    /**
     * copyFile from source file to new destination file.
     */
    public long copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // ******************************
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len = 0;
        long bytecount = 0;
        while ((len = in.read(buf)) > 0) {
            bytecount = bytecount + len;
            out.write(buf, 0, len);
        } // End of While Loop for Copy.

        // ****************************
        // Close off the Files.
        in.close();
        out.close();

        // ****************************
        // Return the byte count.
        return (bytecount);
    } // End of copyFile Shell Method.

    /**
     * showFile Contents.
     */
    public void showFile(File src) throws IOException {
        InputStream in = new FileInputStream(src);

        // ******************************
        // Transfer bytes from in to out
        StringBuffer sb = new StringBuffer();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = in.read(buf)) > 0) {
            for (int ib = 0; ib < len; ib++) {
                sb.append((char) buf[ib]);
            }
        } // End of While Loop for Copy.

        // ****************************
        // Close off the File.
        in.close();

        // ****************************
        // Now Dump the Contents.
        displayMsg(sb.toString());

        // ****************************
        // Return.
        return;
    } // End of showFile Shell Method.


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
        // /i option to specify
        // interactive mode.
        if ((args.length == 1) &&
                ((args[0].equalsIgnoreCase("-i")) ||
                        (args[0].equalsIgnoreCase("--interactive")) ||
                        (args[0].equalsIgnoreCase("/i")))) {
            Shell shell = new Shell(false);
            System.exit(shell.CMDInteractiveShell());
        } // End of Interactive Shell Check.

        // *********************
        // Establish new 
        // Batch Shell to process
        // Single Command.
        //
        Shell shell = new Shell();
        try {
            // *********************
            // Process Command
            System.exit(shell.CMDprocess(args));
        } catch (Exception x) {
            System.err.println(x);
        } // End of Exception.
    } // End of Main.

} ///~ End of Shell Class
