package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxTimeStamp;

import java.io.*;

/**
 * Java utility class to provide a wrapper for the Change Log Restore
 * State File.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */


public class IRRChangeLogRestoreStateFile {

    public static final String VERSION = "Version: 3.0 2003-09-12, " +
            "FRAMEWORK, Incorporated.";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreStateFile.class.getName();
    public static idxLogger IDXLOG = new idxLogger();
    public static final String MP = CLASSNAME + ": ";


    // ****************************
    // Runtime Object Vaiables.
    private File STATEFILE = null;

    /**
     * IRRChangeLogRestoreStateFile Contructor class driven from
     * Main or other Class Caller.
     *
     * @param _statefilename
     */
    public IRRChangeLogRestoreStateFile(String _statefilename) {
        // ******************************
        // Obtain new File Object for
        // this State File.
        STATEFILE = new File(_statefilename);
    } // End of Constructor for IRRChangeLogRestoreStateFile.

    public void persistAsProcessed(String _filename, int[] stats)
            throws IOException {

        // **************************************
        // Open up a Output File for IDS Schema.
        BufferedWriter STATE_OUTPUT = new BufferedWriter(new FileWriter(STATEFILE));

        // **************************************
        // Write the Header of the File.
        idxTimeStamp TSMP = new idxTimeStamp();
        TSMP.enableLocalTime();
        STATE_OUTPUT.write("# *****\n");
        STATE_OUTPUT.write("# " + VERSION + "\n");
        STATE_OUTPUT.write("# LDIF Export File Processed: " + _filename + "\n");
        STATE_OUTPUT.write("# Process Completion Time: " + TSMP.getFTS() + ".\n");
        STATE_OUTPUT.write("#\n");

        // *************************************
        // Write the Body with the Stats.
        STATE_OUTPUT.write("# Entries Processed: [" + stats[2] + "]\n");
        STATE_OUTPUT.write("#  Entry Exceptions: [" + stats[0] + "]\n");
        STATE_OUTPUT.write("#   Entries Skipped: [" + stats[1] + "]\n");
        STATE_OUTPUT.write("#     Entries Added: [" + stats[6] + "]\n");
        STATE_OUTPUT.write("#   Entries Deleted: [" + stats[5] + "]\n");
        STATE_OUTPUT.write("#   Entries Renamed: [" + stats[4] + "]\n");
        STATE_OUTPUT.write("#  Entries Modified: [" + stats[3] + "]\n");
        STATE_OUTPUT.write("# *****\n");

        // *************************************
        // Flush and Close Output Stream.
        STATE_OUTPUT.flush();
        STATE_OUTPUT.close();
        STATE_OUTPUT = null;
    } // End of persistEntry Method.

    public void persistAsBlocked(String _filename)
            throws IOException {

        // **************************************
        // Open up a Output File for IDS Schema.
        try {
            BufferedWriter STATE_OUTPUT = new BufferedWriter(new FileWriter(STATEFILE));

            // **************************************
            // Write the Header of the File.
            STATE_OUTPUT.write("# *****\n");
            STATE_OUTPUT.write("# " + VERSION + "\n");
            STATE_OUTPUT.write("# LDIF Export File Blocked: " + _filename + "\n");
            STATE_OUTPUT.write("#\n");
            STATE_OUTPUT.write("# *****\n");

            // *************************************
            // Flush and Close Output Stream.
            STATE_OUTPUT.flush();
            STATE_OUTPUT.close();
            STATE_OUTPUT = null;
        } catch (Exception e) {
            throw new IOException("Problem with writing to the Block file");
        }

    } // End of persistAsBlocked Method.

} // End of Class IRRChangeLogRestoreStateFile
