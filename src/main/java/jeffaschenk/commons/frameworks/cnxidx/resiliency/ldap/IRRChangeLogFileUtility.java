package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.io.*;

/**
 * IRR Change Log Static utility Methods for Accessing Files
 * produced from either internal change Log or from the LDAP
 * Export change Log produced by the DC-Directory.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */


public class IRRChangeLogFileUtility {

    // *******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = IRRChangeLogFileUtility.class.getName();

    // *****************************
    // Filename Search Patterns
    // This Pattern has been deprecated.
    // pattern: ^IRRCHGLOG.\d{12}.\d{12}\056ldif$
    public static final String IRRCHGLOG_FILENAME_PATTERN =
            "^IRRCHGLOG.\\d{12}.\\d{12}\\056ldif$";
    // This is our Default Pattern,
    // pattern: ^\d{12}.\d{4}(\056\d{3})??$
    public static final String DCLLDIFEXPORT_FILENAME_PATTERN =
            "^\\d{12}.\\d{4}(\\056\\d{3})??$";

    // This is our Default Pattern for obtaining
    // Reference Files which are referred to by our base file.
    // pattern: ^<FILENAME>_ref*
    public static final String DCLLDIFEXPORT_REFERENCE_PATTERN =
            "_ref*";

    // **************************************
    // FileName Globals.
    public static final String PROCESSED = "PROCESSED";
    public static final String BLOCKED = "BLOCKED";
    public static final String UNDERSCORE = "_";


    /**
     * processedFileCount, check for any processed files within the Change Log File System Directory.
     *
     * @param INPUT_PATH Input File System Path of Directory where Log Restore Files Reside.
     */
    protected static long processedFileCount(String INPUT_PATH) {
        String METHODNAME = "processedFileCount";
        long count = 0;

        // **********************************
        // Initialize Logging
        StopWatch sw = new StopWatch();
        sw.start();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        try {
            // ************************************
            // Obtain the current list of LOG Files
            // that are contained within our
            // Input Path File System Directory.
            //
            TreeMap LOGFILES =
                    IRRChangeLogFileUtility.obtainDirListing(
                            INPUT_PATH, DCLLDIFEXPORT_FILENAME_PATTERN);

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
                        getProcessedFile(_infile);

                // Eliminate Blocked File References.
                File _blockedfile =
                        getBlockedFile(_infile);

                if (_processedfile.exists() || _blockedfile.exists()) {
                    count++;
                }
            } // End of While Loop.

            // *************************
            // return
            return (count);

        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing for Method.
    } // End of processedFileCount Method.

    /**
     * obtainProcessedFileMap, check for any unprocessed files within the Change Log File System Directory.
     *
     * @param INPUT_PATH Input File System Path of Directory where Log Restore Files Reside.
     */
    protected static Map<Object,File> obtainProcessedFileMap(String INPUT_PATH) {
        String METHODNAME = "obtainPocessedFileMap";

        // **********************************
        // Initialize Logging
        StopWatch sw = new StopWatch();
        sw.start();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        try {
            // ************************************
            // Obtain the current list of LOG Files
            // that are contained within our
            // Input Path File System Directory.
            //
            TreeMap<String,File> LOGFILES =
                    IRRChangeLogFileUtility.obtainDirListing(
                            INPUT_PATH, DCLLDIFEXPORT_FILENAME_PATTERN);

            // **************************************
            // Create a new Map to return.
            Map<Object,File> RLOGFILES = new TreeMap<>();

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
                        getProcessedFile(_infile);

                // Eliminate Blocked File References.
                File _blockedfile =
                        getBlockedFile(_infile);

                // ***********************************************
                // Add the file to the list if it has been flagged.
                if ((_processedfile.exists()) || (_blockedfile.exists())) {
                    RLOGFILES.put(oit.getKey(), _infile);
                }

            } // End of While Loop.

            // *************************
            // return
            return RLOGFILES;

        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing for Method.
    } // End of obtainProcessedFileMap Method.

    /**
     * unprocessedFileCount, check for any unprocessed files within the Change Log File System Directory.
     *
     * @param INPUT_PATH Input File System Path of Directory where Log Restore Files Reside.
     */
    protected static long unprocessedFileCount(String INPUT_PATH) {
        String METHODNAME = "unprocessedFileCount";
        long count = 0;

        // **********************************
        // Initialize Logging
        StopWatch sw = new StopWatch();
        sw.start();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        try {
            // ************************************
            // Obtain the current list of LOG Files
            // that are contained within our
            // Input Path File System Directory.
            //
            TreeMap LOGFILES =
                    IRRChangeLogFileUtility.obtainDirListing(
                            INPUT_PATH, DCLLDIFEXPORT_FILENAME_PATTERN);

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
                        getProcessedFile(_infile);

                // Eliminate Blocked File References.
                File _blockedfile =
                        getBlockedFile(_infile);


                if (!_processedfile.exists() && !_blockedfile.exists()) {
                    count++;
                }
            } // End of While Loop.

            // *************************
            // return
            return (count);

        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing for Method.
    } // End of unprocessedFileCount Method.

    /**
     * obtainUnprocessedFileMap, check for any unprocessed files within the Change Log File System Directory.
     *
     * @param String Input File System Path of Directory where Log Restore Files Reside.
     * @param String Tagname for our State file.
     */
    protected static TreeMap obtainUnprocessedFileMap(String INPUT_PATH) {
        String METHODNAME = "obtainUnprocessedFileMap";

        // **********************************
        // Initialize Logging
        StopWatch sw = new StopWatch();
        sw.start();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        try {
            // ************************************
            // Obtain the current list of LOG Files
            // that are contained within our
            // Input Path File System Directory.
            //
            TreeMap<String,File> LOGFILES =
                    IRRChangeLogFileUtility.obtainDirListing(
                            INPUT_PATH, DCLLDIFEXPORT_FILENAME_PATTERN);

            // **************************************
            // Create a new Map to return.
            TreeMap<Object,File> RLOGFILES = new TreeMap<>();

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
                        getProcessedFile(_infile);

                // Eliminate Blocked File References.
                File _blockedfile =
                        getBlockedFile(_infile);

                // ***********************************************
                // Add the file to the list if it does not
                // have a flagged file.
                if ((!_processedfile.exists()) && (!_blockedfile.exists())) {
                    RLOGFILES.put(oit.getKey(), _infile);
                }
            } // End of While Loop.

            // *************************
            // return
            return RLOGFILES;

        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing for Method.
    } // End of obtainUnprocessedFileMap Method.

    /**
     * obtainReferenceFileMap, check for any reference files within the Change Log File System Directory.
     *
     * @param String Input File System Path of Directory where Log Restore Files Reside.
     * @param String Change File Log we are Looking for with Reference Files Associated.
     */
    protected static TreeMap obtainReferenceFileMap(final String changelog_filename) {
        String METHODNAME = "obtainReferenceFileMap";

        // **********************************
        // Initialize Logging
        StopWatch sw = new StopWatch();
        sw.start();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        try {
            // ************************************
            // Ensure we have both parameters
            if ((changelog_filename == null) ||
                    (changelog_filename.equalsIgnoreCase(""))) {
                return null;
            }

            // ************************************
            // Obtain the current list of LOG Files
            // that are contained within our
            // Input Path File System Directory
            // applying our filter.
            //
            File f = new File(changelog_filename);
            return
                    IRRChangeLogFileUtility.obtainDirListing(
                            f.getParent(), "^" + f.getName() + DCLLDIFEXPORT_REFERENCE_PATTERN);
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing for Method.
    } // End of obtainReferencedFileMap Method.


    /**
     * deleteReferenceFiles, check for any reference files within the Change Log File System Directory for
     * a specific base change log file and delete those files.
     *
     * @param String Input File System Path of Directory where Log Restore Files Reside.
     * @param String Change File Log we are Looking for with Reference Files Associated.
     */
    protected static long deleteReferenceFiles(final String changelog_filename) {
        String METHODNAME = "deleteReferenceFiles";
        long delete_count = 0;

        // **********************************
        // Initialize Logging
        StopWatch sw = new StopWatch();
        sw.start();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        try {
            // ************************************
            // Ensure we have both parameters
            if ((changelog_filename == null) ||
                    (changelog_filename.equalsIgnoreCase(""))) {
                return 0;
            }

            // ************************************
            // Obtain the current list of LOG Files
            // that are contained within our
            // Input Path File System Directory
            // applying our filter.
            //
            TreeMap RFILES =
                    IRRChangeLogFileUtility.obtainReferenceFileMap(
                            changelog_filename);

            // *************************************
            // Now loop to delete those files.
            Set mySet = RFILES.entrySet();
            Iterator itr = mySet.iterator();
            while (itr.hasNext()) {
                Map.Entry oit = (Map.Entry) itr.next();
                File _reffile =
                        (File) oit.getValue();
                // *******************************
                // Issue File Delete.
                try {
                    if (_reffile.delete()) {
                        delete_count++;
                    }
                } catch (SecurityException se) {
                    // TODO
                } // End of Exception Processing.
            } // End of While Loop.

            // **********************
            // Return number files
            // deleted.
            return delete_count;
        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing for Method.
    } // End of deleteReferenceFiles Method.

    /**
     * obtain a Directory Listing using a Pattern and return a TreeMap sorted by the filename.
     */
    protected static TreeMap<String,File> obtainDirListing(String _dirname, String _pattern) {

        String METHODNAME = "obtainDirListing";

        // **********************************
        // Initialize Logging
        StopWatch sw = new StopWatch();
        sw.start();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ENTERING_METHOD,
                new String[]{METHODNAME});

        // *********************
        // Compile Our Pattern.
        Pattern pattern = null;
        Matcher pm = null;
        TreeMap<String,File> _tm = new TreeMap<>();
        try {
            pattern = Pattern.compile(_pattern);
        } catch (PatternSyntaxException pse) {

            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                    ErrorConstants.DIRLIST_REGEX_PATTERN_SYNTAX_EXCEPTION,
                    new String[]{_pattern, pse.getMessage()});
            return (_tm);
        } // End of Exception.

        try {
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
                            File.separator + contents[i]);

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

        } finally {
            sw.stop();
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.FINALIZING_METHOD,
                    new String[]{METHODNAME, sw.getElapsedTimeString()});
        } // End of Final Processing for Method.
    } // End of obtainDirListing Method.

    /**
     * get Processed File Object for use to persist to indicate
     * log file has been fully processed.
     *
     * @param infile Filename.
     * @return
     */
    protected static File getProcessedFile(File infile) {
        return new File(infile.getAbsolutePath() + UNDERSCORE + PROCESSED);
    } // End of getProcessedFile.

    /**
     * get Processed File Object for use to persist to indicate
     * log file has been fully processed.
     *
     * @param infile Filename.
     * @return
     */
    protected static File getBlockedFile(File infile) {
        return new File(infile.getAbsolutePath() + UNDERSCORE + BLOCKED);
    } // End of getBlockedFile.

    /**
     * get Blocked File Object for use to persist to indicate
     * log file has been fully processed.
     *
     * @param filename.
     * @return
     */
    protected static File getBlockedFile(String filename) {
        return new File(filename + UNDERSCORE + BLOCKED);
    } // End of getBlockedFile.

    /**
     * get Processed File Object for use to persist to indicate
     * log file has been fully processed.
     *
     * @param filename
     * @return
     */
    protected static File getProcessedFile(String filename) {
        return new File(filename + UNDERSCORE + PROCESSED);
    } // End of getProcessedFile.

    /**
     * get Processed File Object for use to persist to indicate
     * log file has been fully processed.
     *
     * @param changelogstatus
     * @return
     */
    protected static File getProcessedFile(ChangeLogStatus changelogstatus) {
        return new File(changelogstatus.getLogFileName() + UNDERSCORE + PROCESSED);
    } // End of getProcessedFile.

} ///:~ End of IRRChangeLogUtility Class.
