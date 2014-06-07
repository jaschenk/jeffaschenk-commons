package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxTimeStamp;

import java.io.*;

/**
 * Java utility class to provide a wrapper for the Change Log Restore
 * State File.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */


public class IRRChangeLogRestoreStateFile implements IRRChangeLogRestoreServiceVersion {

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestoreStateFile.class.getName();

    // ****************************
    // Runtime Object Vaiables.
    private File STATEFILE = null;
    private ChangeLogStatus changelogstatus = null;

    /**
     * IRRChangeLogRestoreStateFile Constructor class driven from
     * Main or other Class Caller.
     *
     * @param _statefilename
     * @prarm changelogstatus Object which provides final status of log.
     */
    public IRRChangeLogRestoreStateFile(String _statefilename,
                                        ChangeLogStatus changelogstatus) {
        this.STATEFILE = new File(_statefilename);
        this.changelogstatus = changelogstatus;
    } // End of Constructor for IRRChangeLogRestoreStateFile.

    /**
     * IRRChangeLogRestoreStateFile Constructor class driven from
     * Main or other Class Caller.
     *
     * @param STATEFILE File Object of Processed File Name.
     * @prarm changelogstatus Object which provides final status of log.
     */
    public IRRChangeLogRestoreStateFile(File STATEFILE,
                                        ChangeLogStatus changelogstatus) {
        this.STATEFILE = STATEFILE;
        this.changelogstatus = changelogstatus;
    } // End of Constructor for IRRChangeLogRestoreStateFile.

    /**
     * Persist the State File as a Processed File.
     *
     * @throws java.io.IOException
     */
    public void persistAsProcessed()
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
        STATE_OUTPUT.write("# LDIF Export File Processed: " + STATEFILE.getAbsoluteFile() + "\n");
        STATE_OUTPUT.write("# Process Completion Time: " + TSMP.getFTS() + ".\n");
        STATE_OUTPUT.write("#\n");

        // *************************************
        // Write the Body with the Stats.
        if (changelogstatus != null) {
            STATE_OUTPUT.write(changelogstatus.toStateFileString());
        }
        STATE_OUTPUT.write("# *****\n");

        // *************************************
        // Flush and Close Output Stream.
        STATE_OUTPUT.flush();
        STATE_OUTPUT.close();
        STATE_OUTPUT = null;
    } // End of persistAsProcessed Method.

    /**
     * Persist the State File as a Processed File.
     *
     * @throws java.io.IOException
     */
    public void persistAsBlocked()
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
        STATE_OUTPUT.write("# LDIF Export File Processed: " + changelogstatus.getLogFileName() + "\n");
        STATE_OUTPUT.write("# Process Completion Time: " + TSMP.getFTS() + ".\n");
        STATE_OUTPUT.write("#\n");

        // *************************************
        // Write the Body with the Stats.
        STATE_OUTPUT.write("# All Changes within File have been Blocked.");
        STATE_OUTPUT.write("# *****\n");

        // *************************************
        // Flush and Close Output Stream.
        STATE_OUTPUT.flush();
        STATE_OUTPUT.close();
        STATE_OUTPUT = null;
    } // End of persistAsProcessed Method.

} // End of Class IRRChangeLogRestoreStateFile
