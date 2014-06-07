package jeffaschenk.commons.frameworks.cnxidx.shell;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;

import java.util.*;
import java.io.*;

/**
 * Java IRR Shell Command to display the current
 * DIB and Journal Sizes.
 * <p/>
 * The input cnd Output onforms to the LDIF Specification: RFC2849.
 * <br>
 * <b>Usage:</b><br>
 * IRRdibSize &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
 * <br>
 * <b>Required Parameters are:</b>
 * <pre>
 * --database_dirname
 * 	Specify Full File System Directory Path of the IRR Directory Database.
 * --journal_dirname
 * 	Specify Full File System Directory Path of the IRR Directory Journal.
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2002
 */


public class IRRdibSize implements idxCMDReturnCodes {

    public static String VERSION = "Version: 2.0 2002-08-14, " +
            "FRAMEWORK, Incorporated.";

    public static String MP = "IRRdibSize: ";

    private static String DATABASE_DIRNAME = null;
    private static String JOURNAL_DIRNAME = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    // **************************************
    // Directory database file sizes
    private long DSAV001; // Lookup Volume.
    private long DSAV002; // Root Index.
    private long DSAV003; // Temp Volume.
    private long DSAV004; // Standard Attributes.
    private long DSAV005; // Fast Attributes.
    private long DSAV006; // Large Attributes.
    private long DSAV007; // Rare Attributes.
    private long DSAV008; // Search Index Node.
    private long DSAV009; // Search Index Data.

    // **************************************
    // Accumulated Totals
    private long DATABASEBYTESIZE;
    private long INDEXBYTESIZE;

    // **************************************
    // Directory journal file size
    private long JOURNALBYTESIZE;

    // **************************************
    // Warning limits for Jounral Size.
    public static long WARN_JLIMIT_MIN = ((256 * 1024) * 1024);  // 256MB
    public static long WARN_JLIMIT_MAX = ((1024 * 1024) * 1024); // 1GB

    // **************************************
    // String Formatting Utility Object
    private StringFormat SF = new StringFormat();

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRdibSize <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--database_dirname ");
        System.err.println("\tSpecify Full File System Directory path of the IRR Directory Database.");

        System.err.println(MP + "--journal_dirname ");
        System.err.println("\tSpecify Full File System Directory path of the IRR Directory Journal.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRdibSize Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _DATABASE_DIRNAME  Database File System Directory Name.
     * @param _JOURNAL_DIRNAME  Journal File System Directory Name.
     * @param _VERBOSE Indicate Verbosity.
     */
    public IRRdibSize(String _DATABASE_DIRNAME,
                      String _JOURNAL_DIRNAME,
                      boolean _VERBOSE) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        DATABASE_DIRNAME = _DATABASE_DIRNAME;
        JOURNAL_DIRNAME = _JOURNAL_DIRNAME;
        VERBOSE = _VERBOSE;

    } // End of Constructor for IRRdibSize.

    /**
     * toString, formats output of the DIB Size in a String format suitable for framing
     */
    public String toString() {
        String _lsep = System.getProperty("line.separator");
        return ("Framework Status: IRR DIB Size: " +
                getHumanSize(DATABASEBYTESIZE) + ", " +
                "Index Size: " +
                getHumanSize(INDEXBYTESIZE) +
                "." + _lsep +
                "Framework Status: IRR Detail DIB Volume Sizes:" + _lsep +
                getDIBDetailInformation() +
                _lsep +
                "Framework Status: IRR Journal Size: " +
                getHumanSize(JOURNALBYTESIZE) + "." +
                getJournalWarningInformation());
    } // End of toString method.

    /**
     * Obtain and calculate the size of the DIB and Journal for the IRR Directory.
     */
    public void obtainsize() {

        // *****************************************
        // Obtain proper file seperator
        String _fsep = System.getProperty("file.separator");

        // **************************************************************
        // Obtain the Sizes for all relevant DC-Directory database file
        DSAV001 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV001.DAT");
        DSAV002 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV002.DAT");
        DSAV003 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV003.DAT");
        DSAV004 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV004.DAT");
        DSAV005 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV005.DAT");
        DSAV006 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV006.DAT");
        DSAV007 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV007.DAT");
        DSAV008 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV008.DAT");
        DSAV009 = getFileSize(DATABASE_DIRNAME + _fsep + "DSAV009.DAT");

        // **************************************************************
        // Obtain the Size for Journal file
        JOURNALBYTESIZE = getFileSize(JOURNAL_DIRNAME + _fsep + "JOURNAL.DAT");

        // **************************************************************
        // Caluculate the Entire DIB Size
        DATABASEBYTESIZE = (DSAV001 + DSAV002 + DSAV003 + DSAV004 + DSAV005 + DSAV006 + DSAV007);

        // **************************************************************
        // Calculate the Entire Index Size.
        INDEXBYTESIZE = (DSAV008 + DSAV009);

        // ****************************************
        // Show the Statistics
        if (VERBOSE) {
            System.out.println(MP +
                    SF.JLeft("DATABASE SIZE:", 16) +
                    SF.JRight(getHumanSize(DATABASEBYTESIZE), 8));

            System.out.println(MP +
                    SF.JLeft("INDEX SIZE:", 16) +
                    SF.JRight(getHumanSize(INDEXBYTESIZE), 8));

            System.out.println(MP +
                    SF.JLeft("JOURNAL SIZE:", 16) +
                    SF.JRight(getHumanSize(JOURNALBYTESIZE), 8));

        } // End of Verbose output.

    } // End of Perform Method.

    /**
     * getFileSize method to obtain the length of the specified filename.
     *
     * @param _filename FileName.
     * @return long Length in bytes of Specified File.
     */
    private long getFileSize(String _filename) {
        long flength = 0;
        // ****************************************
        // Build a File Object.
        try {
            File _file = new File(_filename);
            flength = _file.length();
        } catch (Exception e) {
            ; // NOOP, Must not have access to the File.
        } // End of Exception.

        // ****************************************
        // Return the length of the file.
        return (flength);
    } // End of getFileSize Private Method.

    /**
     * getHumanSize method to calculate the byte size into a
     * human readable form.
     *
     * @param _bytes file length in bytes.
     * @return String Human Readable form of the numeric value..
     */
    private String getHumanSize(long _bytes) {
        String hsize = "B";
        double xb = _bytes;

        // **********************************************
        // Convert to KiloBytes, Megabytes or Gigabytes.
        //
        if (xb > 1024) {
            xb = xb / 1024;
            hsize = "K";
            if (xb > 1024) {
                xb = xb / 1024;
                hsize = "M";
                if (xb > 1024) {
                    xb = xb / 1024;
                    hsize = "G";
                } // end of Inner If.
            } // End of Outer If.
        } // End of Initial Nested If.

        // ****************************************
        // Round the Result.
        xb = xb + 0.9;
        Double XB = new Double(xb);
        long xl = XB.longValue();

        // ****************************************
        // Return the length of the file.
        return (xl + hsize);
    } // End of getHumanSize Private Method.

    /**
     * getDIBDetailInformation method to provide detail information
     * in string form.
     *
     * @return String Formatted Detail Information.
     */
    private String getDIBDetailInformation() {
        String _lsep = System.getProperty("line.separator");

        // ***************************************
        // Formulate the Detail Information.
        String _rs =
                SF.JRight("Lookup Volume:", 27) +
                        SF.JRight(getHumanSize(DSAV001), 10) +
                        _lsep +
                        SF.JRight("Root Index:", 27) +
                        SF.JRight(getHumanSize(DSAV002), 10) +
                        _lsep +
                        SF.JRight("Temp Volume:", 27) +
                        SF.JRight(getHumanSize(DSAV003), 10) +
                        _lsep +
                        SF.JRight("Standard Attributes:", 27) +
                        SF.JRight(getHumanSize(DSAV004), 10) +
                        _lsep +
                        SF.JRight("Fast Attributes:", 27) +
                        SF.JRight(getHumanSize(DSAV005), 10) +
                        _lsep +
                        SF.JRight("Large Attributes:", 27) +
                        SF.JRight(getHumanSize(DSAV006), 10) +
                        _lsep +
                        SF.JRight("Rare Attributes:", 27) +
                        SF.JRight(getHumanSize(DSAV007), 10) +
                        _lsep +
                        SF.JRight("Search Index Node:", 27) +
                        SF.JRight(getHumanSize(DSAV008), 10) +
                        _lsep +
                        SF.JRight("Search Index Data:", 27) +
                        SF.JRight(getHumanSize(DSAV009), 10);

        // *****************************************
        // Return the String....
        return (_rs);
    } // End of getDIBDetailInformation.

    /**
     * getJournalWarningInformation method to provide detail information
     * in string form.
     *
     * @return String Formatted Detail Information.
     */
    private String getJournalWarningInformation() {
        String _lsep = System.getProperty("line.separator");

        // ***************************************
        // Formulate Any Warning Information.
        String _rs = "";

        if (JOURNALBYTESIZE > WARN_JLIMIT_MAX) {
            _rs = _lsep + "Framework Status: **Warning: Journal Volume Size at Maximum!" + _lsep +
                    "Framework Status: **Warning: Backup or Pause Directory to Truncate Journal." + _lsep;
        } else if (JOURNALBYTESIZE > WARN_JLIMIT_MIN) {
            _rs = _lsep + "Framework Status: Journal Volume nearing Maximum." + _lsep +
                    "Framework Status: Recommadation to Backup or Pause Directory to Truncate Journal." + _lsep;
        }

        // *****************************************
        // Return the String....
        return (_rs);
    } // End of getJournalDetailInformation.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.shell.IRRdibSize
     */
    public static void main(String[] args) {

        // ****************************************
        // Send the Greeting.
        if (VERBOSE) {
            System.out.println(MP + VERSION);
        }

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
        if (Zin.doesNameExist("?")) {
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
        // Show Arguments if Debugging Selected.
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

        VAR.add(new idxArgVerificationRules("database_dirname",
                true, true));

        VAR.add(new idxArgVerificationRules("journal_dirname",
                true, true));

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

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //

        // **************************************************
        // Load up the RunTime Arguments.
        //
        DATABASE_DIRNAME = ((String) Zin.getValue("database_dirname")).trim();
        JOURNAL_DIRNAME = ((String) Zin.getValue("journal_dirname")).trim();

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Initailize Constructor.
        IRRdibSize dib = new IRRdibSize(
                DATABASE_DIRNAME,
                JOURNAL_DIRNAME,
                VERBOSE);

        // ****************************************
        // Perform Function.
        try {
            dib.obtainsize();
            System.out.println(dib);
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRdibSize.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        if (VERBOSE) {
            System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
            System.exit(EXIT_SUCCESSFUL);
        }
    } // End of Main

} ///~ End of Class IRRdibSize
