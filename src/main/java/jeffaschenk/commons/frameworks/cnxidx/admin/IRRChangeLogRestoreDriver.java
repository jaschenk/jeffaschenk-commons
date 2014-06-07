package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.filtering.FilterString;
import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxManageContext;

import java.util.*;
import java.util.regex.*;
import java.io.*;

import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to Drive the IRRChangeLogRestore Process.
 * <br>
 * <b>Usage:</b><br>
 * IRRChangeLogRestoreDriver &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --inpath
 * 	Specify Full Input file Path of Change Log Files to be read.
 * --tagname
 *      Specify a Unique Name to Identify Files Processed to a Destination.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --changelogfiles
 *      Indicates that the input Files were created by the IRRChangeLogCollector.
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 * <p/>
 * See <CODE>IRRChangeLogCollector</CODE> for
 * information regarding the contents and name of the files Written this file.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */


public class IRRChangeLogRestoreDriver implements idxCMDReturnCodes {

    public static String VERSION = "Version: 3.1 2003-09-12, " +
            "FRAMEWORK, Incorporated.";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreDriver.class.getName();
    public static idxLogger IDXLOG = new idxLogger();

    public static String MP = CLASSNAME + ": ";

    // ****************************
    // Filename Search Patterns
    // pattern: ^IRRCHGLOG.\d{12}.\d{12}\056ldif$
    public static final String IRRCHGLOG_FILENAME_PATTERN =
            "^IRRCHGLOG.\\d{12}.\\d{12}\\056ldif$";

    // pattern: ^\d{12}.\d{4}\056\d{3}$
    public static final String DCLLDIFEXPORT_FILENAME_PATTERN =
            "^\\d{12}.\\d{4}\\056\\d{3}$";

    private String FILENAME_SEARCH_PATTERN = DCLLDIFEXPORT_FILENAME_PATTERN;

    // ****************************
    // Type of Session we are
    // Driving, either from
    // IRRChangeCollector or
    // from DCL's LDIF Export
    // Facility.
    //
    // MOD_CHANGE_LOG,
    // TRUE = DCL LDIF Export
    // FALSE = IRRChangeCollector.
    //
    private boolean MOD_CHANGE_LOG = true;

    // ****************************
    // Runtime Statistics
    private int files_processed = 0;
    private int entries_exceptions = 0;
    private int entries_skipped = 0;
    private int entries_processed = 0;
    private int entries_modified = 0;
    private int entries_renamed = 0;
    private int entries_deleted = 0;
    private int entries_added = 0;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRChangeLogRestoreDriver <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--inpath ");
        System.err.println("\tSpecify Full Input file Path of Change Log Files to be read.");
        System.err.println(MP + "--tagname ");
        System.err.println("\tSpecify TagName for LogRestore State Files.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRChangeLogRestoreDriver Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRChangeLogRestoreDriver() {
    } // End of Constructor for IRRChangeLogRestoreDriver.

    /**
     * clearStats Method resets all statistics for this utility classs.
     */
    public void clearStats() {
        files_processed = 0;
        entries_exceptions = 0;
        entries_skipped = 0;
        entries_processed = 0;
        entries_modified = 0;
        entries_renamed = 0;
        entries_deleted = 0;
        entries_added = 0;
    } // End of clearStats Method.

    /**
     * dumpStats Method displays all statistics for this utility.
     */
    public void dumpStats() {
        String METHODNAME = "dumpStats";
        if (files_processed == 0) {
            IDXLOG.info(CLASSNAME, METHODNAME, "No LDIF Files Available for Processing.");
        } else {
            IDXLOG.info(CLASSNAME, METHODNAME, "LDIF Log Files Processed: [" + files_processed + "]");
            IDXLOG.info(CLASSNAME, METHODNAME, "Entries Processed: [" + entries_processed + "]");
            IDXLOG.info(CLASSNAME, METHODNAME, "Entry Exceptions: [" + entries_exceptions + "]");
            IDXLOG.info(CLASSNAME, METHODNAME, "Entries Skipped: [" + entries_skipped + "]");
            IDXLOG.info(CLASSNAME, METHODNAME, "Entries Added: [" + entries_added + "]");
            IDXLOG.info(CLASSNAME, METHODNAME, "Entries Deleted: [" + entries_deleted + "]");
            IDXLOG.info(CLASSNAME, METHODNAME, "Entries Renamed: [" + entries_renamed + "]");
            IDXLOG.info(CLASSNAME, METHODNAME, "Entries Modified: [" + entries_modified + "]");
        } // End of Else.
    } // End of dumpStats Method.

    /**
     * wereFilesProcessed Method to allow caller to determine if Stats should be dumped or not.
     */
    public boolean wereFilesProcessed() {
        String METHODNAME = "dumpStats";
        if (files_processed > 0) {
            return (true);
        }
        return (false);
    } // End of wereFilesProcessed Method.

    /**
     * enable the use of IRRChange Log Files, instead of DCL Log Files.
     */
    public void useIRRChangeLogFiles() {
        MOD_CHANGE_LOG = false;
        FILENAME_SEARCH_PATTERN = IRRCHGLOG_FILENAME_PATTERN;
    } // End of useIRRChangeLogFiles Method.

    /**
     * enable the use of DCL Export LDIF Files, instead of IRR Change Log Collector Files.
     */
    public void useDCLExportLDIFFiles() {
        MOD_CHANGE_LOG = true;
        FILENAME_SEARCH_PATTERN = DCLLDIFEXPORT_FILENAME_PATTERN;
    } // End of useDCLExportLDIFFiles Method.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @param irrctx of Directory Destination.
     * @param INPUT_PATH     Input File System Path of Directory where Log Restore Files Reside.
     * @param STATE_TAGNAME     Tagname for our State file.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform(DirContext irrctx,
                        String INPUT_PATH,
                        String STATE_TAGNAME)
            throws idxIRRException {

        String METHODNAME = "perform";
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entering to Drive Restore from Input Path:[" +
                INPUT_PATH + "], TAG Name:[" + STATE_TAGNAME + "].");

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ************************************
        // Ensure STATE TagName is correct.
        STATE_TAGNAME = STATE_TAGNAME.trim();
        STATE_TAGNAME = STATE_TAGNAME.replace(' ', '_');

        // ************************************
        // Obtain the current list of LOG Files
        // that are contained within our
        // Input Path File System Directory.
        //
        TreeMap LOGFILES = obtainDirListing(INPUT_PATH, FILENAME_SEARCH_PATTERN);

        // ************************************
        // Now simple Iterate Through the
        // Log Files and drive the Restore.
        Set mySet = LOGFILES.entrySet();
        Iterator itr = mySet.iterator();
        while (itr.hasNext()) {
            Map.Entry oit = (Map.Entry) itr.next();
            File _infile =
                    (File) oit.getValue();

            // *******************************************
            // Verify the file has yet to be processed or
            // blocked already.
            File _processedfile =
                    new File(_infile.getAbsolutePath() + "_" +
                            "PROCESSED" + "_" + STATE_TAGNAME);

            if (_processedfile.exists()) {
                continue;
            }

            File _blockedfile =
                    new File(_infile.getAbsolutePath() + "_" +
                            "BLOCKED" + "_" + STATE_TAGNAME);

            if (_blockedfile.exists()) {
                continue;
            }
            // ***************************
            // Drive the Restore process
            // to the Destination
            // for this unprocessed
            // input file.
            //
            try {

                IDXLOG.fine(CLASSNAME, METHODNAME, "Processing File:[" +
                        _infile.getAbsolutePath() + "].");

                driveLDIFRestore(irrctx, _infile.getAbsolutePath(),
                        _processedfile.getAbsolutePath());

                IDXLOG.fine(CLASSNAME, METHODNAME, "Processed File:[" +
                        _infile.getAbsolutePath() + "].");

            } catch (idxIRRException ire) {
                IDXLOG.severe(CLASSNAME, METHODNAME, "IRR Processing Exception " + ire);
                throw ire;
            } catch (Exception e) {
                IDXLOG.severe(CLASSNAME, METHODNAME, "Processing Exception " + e);
                throw new idxIRRException(e.getMessage());
            } // End of Exception Processing.

        } // End of While Loop.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Show Restore Timings.
        IDXLOG.fine(CLASSNAME, METHODNAME, "Driven Restore Complete, Elapsed Time: " + elt.getElapsed());

    } // End of Perform Method.

    /**
     * unprocessedFileCount, check for any unprocessed files before a Directory context
     * has been established.
     *
     * @param INPUT_PATH Input File System Path of Directory where Log Restore Files Reside.
     * @param STATE_TAGNAME Tagname for our State file.
     */
    public long unprocessedFileCount(String INPUT_PATH, String STATE_TAGNAME) {
        String METHODNAME = "unprocessedFileCount";
        long count = 0;

        // ************************************
        // Ensure STATE TagName is correct.
        STATE_TAGNAME = STATE_TAGNAME.trim();
        STATE_TAGNAME = STATE_TAGNAME.replace(' ', '_');

        // ************************************
        // Obtain the current list of LOG Files
        // that are contained within our
        // Input Path File System Directory.
        //
        TreeMap LOGFILES = obtainDirListing(INPUT_PATH, FILENAME_SEARCH_PATTERN);

        // ************************************
        // Now simple Iterate Through the
        // Log Files and drive the Restore.
        Set mySet = LOGFILES.entrySet();
        Iterator itr = mySet.iterator();
        while (itr.hasNext()) {
            Map.Entry oit = (Map.Entry) itr.next();
            File _infile =
                    (File) oit.getValue();

            // *******************************************
            // Verify the file has yet to be processed or
            // not.
            File _processedfile =
                    new File(_infile.getAbsolutePath() + "_" +
                            "PROCESSED" + "_" + STATE_TAGNAME);

            File _blockedfile =
                    new File(_infile.getAbsolutePath() + "_" +
                            "BLOCKED" + "_" + STATE_TAGNAME);

            if (!_processedfile.exists() && !_blockedfile.exists()) {
                count++;
            }

        } // End of While Loop.

        // *************************
        // return
        return (count);
    } // End of unprocessedFileCount Method.

    /**
     * Drive the LDIF Restore for this File.
     */
    private void driveLDIFRestore(DirContext irrctx,
                                  String _infilename,
                                  String _processedfilename)
            throws Exception, idxIRRException {

        // *****************************
        // Initialize our Statistics
        int stats[] = new int[7];

        // *****************************
        // Perform Incremental changes
        // to destination.
        // Use appropreiate Object
        // based upon current 
        // configuration.
        //
        if (MOD_CHANGE_LOG) {
            IRRmodifyEntry RESTORE = new IRRmodifyEntry();
            RESTORE.perform(irrctx, _infilename);
            stats = RESTORE.obtainStats();
            RESTORE = null;
        } else {
            IRRChangeLogRestore RESTORE = new IRRChangeLogRestore();
            RESTORE.perform(irrctx, _infilename);
            stats = RESTORE.obtainStats();
            RESTORE = null;
        } // End of Else.

        // ************************************
        // Accumulate the Statistics.
        files_processed++;
        entries_exceptions = +stats[0];
        entries_skipped = +stats[1];
        entries_processed = +stats[2];
        entries_modified = +stats[3];
        entries_renamed = +stats[4];
        entries_deleted = +stats[5];
        entries_added = +stats[6];

        // *******************************
        // CheckPoint a State file
        // indicating this file
        // has been processed.
        IRRChangeLogRestoreStateFile STATEFILE =
                new IRRChangeLogRestoreStateFile(_processedfilename);

        // *******************************
        // Write Stats to the State File
        // for later review.
        STATEFILE.persistAsProcessed(_infilename, stats);

        // **********************
        // Clean-up.
        STATEFILE = null;

        // **********************
        // Return
        return;
    } // End of driveLDIFRestore.

    /**
     * obtain a Directory Listing using a Pattern and return a TreeMap sorted by the filename.
     */
    private TreeMap<String,File> obtainDirListing(String _dirname, String _pattern) {

        String METHODNAME = "obtainDirListing";

        // *********************
        // Compile Our Pattern.
        Pattern pattern = null;
        Matcher pm = null;
        TreeMap<String,File> _tm = new TreeMap<>();
        try {
            pattern = Pattern.compile(_pattern);
        } catch (PatternSyntaxException pse) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "Error Using RegEx Pattern in obtainDirListing Method, " + pse.getMessage());
            return (_tm);
        } // End of Exception.

        // ******************************
        // Obtain the Directory Listing.
        File ld = new File(_dirname);
        if (!ld.isDirectory()) {
            return (_tm);
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
                File DGD = new File(_dirname +
                        File.separator +
                        contents[i]);

                if (DGD.isDirectory()) {
                    continue;
                }

                // *****************************
                // Save to our TreeMap.
                _tm.put(contents[i], DGD);
            } // End of find if.
        } // End of For Loop.

        // *******************
        // Return.
        return (_tm);
    } // End of obtainDirListing Method

    /**
     * filterUnprocessedFiles
     *
     * @param INPUT_PATH Input File System Path of Directory where Log Restore Files Reside.
     * @param STATE_TAGNAME Tagname for our State file.
     * @param LDIF_FILTER_FILE ldif filter file
     */
    public void filterFiles(String INPUT_PATH, String STATE_TAGNAME, String LDIF_FILTER_FILE)
            throws IRRLdifFilterException {
        String METHODNAME = "filterFiles";

        // ************************************
        // Ensure STATE TagName is correct.
        STATE_TAGNAME = STATE_TAGNAME.trim();
        STATE_TAGNAME = STATE_TAGNAME.replace(' ', '_');

        String line;
        String dn = "";
        String changetype = "";
        boolean isReadFlag = false;
        boolean isMatch = false;
        String lineBuffer;
        String emptyString = "";
        String ldifFileString;
        FilterString myFilterStrings;

        // Open the ldif filter file and read any dn's to be blocked
        HashSet filterSet = new HashSet();
        try {
            myFilterStrings = new FilterString(new File(LDIF_FILTER_FILE));
        } catch (IOException e) {
            System.err.println(METHODNAME + "Opening the ldif filter file: (" + LDIF_FILTER_FILE + ") error: " + e);
            throw new IRRLdifFilterException(METHODNAME + "IO Problem with ldif filter file:" + LDIF_FILTER_FILE);
        } catch (Exception e) {
            System.err.println(METHODNAME + "Opening the ldif filter file: (" + LDIF_FILTER_FILE + ") error: " + e);
            throw new IRRLdifFilterException(METHODNAME + "General Problem with ldif filter file:" + LDIF_FILTER_FILE);
        }
        // ************************************
        // Obtain the current list of LOG Files
        // that are contained within our
        // Input Path File System Directory.
        //
        TreeMap LOGFILES = obtainDirListing(INPUT_PATH, FILENAME_SEARCH_PATTERN);

        // ************************************
        // Now simple Iterate Through the
        // Log Files and drive the Restore.
        HashSet<String> readDnSet = new HashSet<>();
        Set mySet = LOGFILES.entrySet();
        Iterator itr = mySet.iterator();

        while (itr.hasNext()) {
            Map.Entry oit = (Map.Entry) itr.next();
            File _infile =
                    (File) oit.getValue();
            String filePath = _infile.getAbsolutePath();

            // *******************************************
            // Verify the file has yet to be processed or blocked aleady
            File _processedfile = new File(filePath + "_" + "PROCESSED" + "_" + STATE_TAGNAME);
            File _blockedfile = new File(filePath + "_" + "BLOCKED" + "_" + STATE_TAGNAME);

            readDnSet.clear();

            if ((!_processedfile.exists()) && (!_blockedfile.exists())) {

                //Open the file and read dn and changetype value
                try {
                    BufferedReader infileBufferReader = new BufferedReader(new FileReader(filePath));
                    //TODO integrate with utility idxLDIFReader class
                    //idxLDIFReader myIdxLDIFReader = new idxLDIFReader(infileBufferReader);

                    //Attributes entryValues = myIdxLDIFReader.getNextEntryPreserveCase();
                    //System.out.println(METHODNAME+" " + entryValues.toString());

                    while ((lineBuffer = infileBufferReader.readLine()) != null) {
                        if (lineBuffer.startsWith("dn: ")) {
                            dn = lineBuffer.replaceFirst("dn: ", emptyString);
                            while (((lineBuffer = infileBufferReader.readLine()) != null) && (!isReadFlag)) {

                                if (lineBuffer.startsWith(" ")) {
                                    dn = dn + lineBuffer.replaceFirst(" ", emptyString);
                                } else {
                                    changetype = lineBuffer.replaceFirst("changetype: ", emptyString);
                                    isReadFlag = true;
                                }
                            }
                            // Build the string to match with
                            ldifFileString = dn + ":" + changetype;
                            readDnSet.add(ldifFileString);
                            isReadFlag = false;
                        }
                    }
                    infileBufferReader.close();
                } catch (IOException e) {
                    System.err.println(MP + "Opening the ldif file for checking: (" + LDIF_FILTER_FILE + ") error: " + e);
                    throw new IRRLdifFilterException(METHODNAME + "IO Problem with reading ldif update file: " + filePath);
                } catch (Exception e) {
                    System.err.println(MP + "Opening the ldif file for checking: (" + LDIF_FILTER_FILE + ") error: " + e);
                    throw new IRRLdifFilterException(METHODNAME + "General Problem with reading ldif update file: " + filePath);
                }

                Iterator itrReadDnSet = readDnSet.iterator();
                String nextFilterValue;
                String nextldifFileString;
                int numberOfMatches = 0;
                isMatch = false;

                //Iterate round set now until we have data to match to block
                while (itrReadDnSet.hasNext()) {
                    nextldifFileString = (String) itrReadDnSet.next();

                    if (myFilterStrings.match(nextldifFileString)) {
                        isMatch = true;
                        numberOfMatches++;
                    }
                }

                // Some or all of ldif file needs to be blocked
                if (isMatch) {

                    String _blockedfilename;

                    if (numberOfMatches < readDnSet.size()) {
                        // remove dn entires from change file which are to be blocked
                        System.out.println(MP + "Calling edit file: " + filePath);
                        editChangeFile(filePath, myFilterStrings);
                    } else {
                        _blockedfilename = filePath + "_" + "BLOCKED" + "_" + STATE_TAGNAME;

                        // *******************************
                        // CheckPoint a State file
                        // indicating this file
                        // has been blocked.
                        IRRChangeLogRestoreStateFile STATEFILE =
                                new IRRChangeLogRestoreStateFile(_blockedfilename);

                        // *******************************
                        // Write Stats to the State File
                        // for later review.
                        try {
                            STATEFILE.persistAsBlocked(_blockedfilename);
                            System.out.println(MP + "Block File created: " + _blockedfilename);
                        } catch (IOException e) {
                            System.err.println(MP + "Opening the ldif file for checking: (" + LDIF_FILTER_FILE + ") error: " + e);
                            throw new IRRLdifFilterException(METHODNAME + "IO Problem with creating ldif block file: " + _blockedfilename);
                        } catch (Exception e) {
                            System.err.println(MP + "Opening the ldif file for checking: (" + LDIF_FILTER_FILE + ") error: " + e);
                            throw new IRRLdifFilterException(METHODNAME + "General Problem with creating ldif block file: " + _blockedfilename);
                        }
                    }
                }
            }// End of If statement
        } // End of While Loop.
        return;

    } // End of filterUnprocessedFiles Method.

    /**
     * editChangeFile
     *
     * @param filePath  Input File System Path of Directory where Log Restore Files Reside.
     * @param myFilterStrings filterSet all dn's to be blocked
     */
    private void editChangeFile(String filePath, FilterString myFilterStrings)
            throws IRRLdifFilterException {
        String METHODNAME = "editChangeFile";
        String tempFileName = filePath + "TEMP";

        //Create temp change file to hold new version of change file
        File tempFILE = new File(tempFileName);
        File originalFILE = new File(filePath);
        String lineBuffer;
        String dn;
        String changetype = "";
        String ldifFileString;
        String emptyString = "";

        try {
            BufferedReader infileBufferReader = new BufferedReader(new FileReader(filePath));
            BufferedWriter infileBufferWriter = new BufferedWriter(new FileWriter(tempFileName));

            while ((lineBuffer = infileBufferReader.readLine()) != null) {
                boolean isEndDnBlockFlag = false;
                boolean isMatch = false;

                if (lineBuffer.startsWith("dn: ")) {
                    dn = lineBuffer.replaceFirst("dn: ", emptyString);
                    while (!isEndDnBlockFlag) {

                        if (lineBuffer.startsWith(" ")) {
                            dn = dn + lineBuffer.replaceFirst(" ", emptyString);
                        } else if (lineBuffer.startsWith("changetype")) {
                            changetype = lineBuffer.replaceFirst("changetype: ", emptyString);

                        }
                        if (lineBuffer.equals("")) {

                            isEndDnBlockFlag = true;
                        } else if ((lineBuffer = infileBufferReader.readLine()) == null) {

                            isEndDnBlockFlag = true;
                        }
                    }
                    // Build the string to match with
                    ldifFileString = dn + ":" + changetype;

                    if (myFilterStrings.match(ldifFileString)) {
                        isMatch = true;
                    }

                    if (!isMatch) {
                        infileBufferReader.reset();

                        while (((lineBuffer = infileBufferReader.readLine()) != null) && (lineBuffer.length() != 0)) {
                            infileBufferWriter.newLine();
                            infileBufferWriter.write(lineBuffer, 0, lineBuffer.length());
                        }
                        if (lineBuffer != null)
                            infileBufferWriter.newLine();
                    }
                } else {
                    if (!lineBuffer.startsWith("version"))
                        infileBufferWriter.newLine();
                    infileBufferWriter.write(lineBuffer, 0, lineBuffer.length());

                }
                infileBufferReader.mark(10000000);
            }// end of while
            infileBufferReader.close();
            infileBufferWriter.close();
            originalFILE.delete();

            if (tempFILE.renameTo(originalFILE)) {
                tempFILE.delete();
            } else {
                System.err.println(METHODNAME + " Failed to rename file to origianl");
            }
        } catch (IOException e) {
            System.err.println(MP + "editFile error: " + e);
            throw new IRRLdifFilterException(METHODNAME + "IO Problem editing file: " + filePath);
        } catch (Exception e) {
            System.err.println(MP + "editFile error: " + e);
            throw new IRRLdifFilterException(METHODNAME + "General Problem editing change file: " + filePath);
        }
        return;
    } // End of editChangeFile Method.

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRChangeLogRestoreDriver
     * @see IRRChangeLogger
     */
    public static void main(String[] args) {

        // ****************************************
        // Local Objects
        idxManageContext IRRDest = null;
        String IRRHost = null;
        String IRRPrincipal = null;
        String IRRCredentials = null;

        String INPUT_PATH = null;
        String STATE_TAGNAME = null;
        String LDIF_FILTER_FILE = null;

        boolean VERBOSE = false;
        boolean CHANGELOGFILES = false;

        String METHODNAME = "main";

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
        if (Zin.doesNameExist("?")) {
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

        VAR.add(new idxArgVerificationRules("inpath",
                true, true));

        VAR.add(new idxArgVerificationRules("tagname",
                true, true));

        VAR.add(new idxArgVerificationRules("changelogfiles",
                false, false));

        VAR.add(new idxArgVerificationRules("filterfile",
                false, true));

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
        if (Zin.doesNameExist("changelogfiles")) {
            CHANGELOGFILES = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        INPUT_PATH = ((String) Zin.getValue("inpath")).trim();
        STATE_TAGNAME = ((String) Zin.getValue("tagname")).trim();
        LDIF_FILTER_FILE = ((String) Zin.getValue("filterfile")).trim();
        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Destination Directory Connection to Host URL:[" + IRRHost + "]");

        IRRDest = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "Restore Destination");

        // ************************************************
        // Exit on all Exceptions.
        IRRDest.setExitOnException(true);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRDest.open();
        } catch (Exception e) {
            System.err.println(MP + "Error Opening Directory Context: " + e);
            System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
        } // End of exception

        // *****************************************
        // Disable the Factories.
        try {
            IRRDest.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + "Error Disabling DSAE Factories: " + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of exception  

        // ****************************************
        // Initailize Constructor.
        IRRChangeLogRestoreDriver LRD = new IRRChangeLogRestoreDriver();

        // ****************************************
        // Set the Type of files, if appropreiate.
        if (CHANGELOGFILES) {
            LRD.useIRRChangeLogFiles();
        }

        // check for files that contain updates that are to be blocked
        try {
            LRD.filterFiles(INPUT_PATH, STATE_TAGNAME, LDIF_FILTER_FILE);
        } catch (IRRLdifFilterException e) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "It was not possible to filter updates" + e);
        } catch (Exception e) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "Unexpected exception occured. It was not possible to filter updates" + e);
        }
        // ****************************************
        // Perform Function.
        try {
            LRD.perform(IRRDest.irrctx, INPUT_PATH, STATE_TAGNAME);
        } catch (idxIRRException ire) {
            System.err.println(MP + " " + ire.getMessage());
            System.exit(ire.getRC());
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRChangeLogRestoreDriver.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Destination Directory Context.");
        try {
            IRRDest.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(EXIT_IRR_CLOSE_FAILURE);
        } // End of exception

        // ****************************************
        // Show the Statistics
        LRD.dumpStats();

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRChangeLogRestoreDriver
