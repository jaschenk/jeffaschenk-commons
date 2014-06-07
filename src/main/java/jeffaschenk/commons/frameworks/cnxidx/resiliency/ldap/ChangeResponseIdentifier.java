package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.io.Serializable;
import java.util.Date;

/**
 * Provides a Change Response Identifier for every change issued.
 * This Object is used by the IRRChangeLogRestoreServiceReaderThread for tracking
 * changes against ChangeResponseIdentifierS.
 *
 * @author jeff.schenk
 * @since 2005.11.08
 */
public class ChangeResponseIdentifier implements Serializable {

    // *****************************************
    // Static Globals
    public static final boolean CHANGE_SUCCESSFUL = true;
    public static final boolean CHANGE_NOT_SUCCESSFUL = false;
    public static final boolean CHANGE_BLOCKED = true;
    public static final boolean CHANGE_NOT_BLOCKED = false;
    public static final boolean CHANGE_IGNORED = true;
    public static final boolean CHANGE_NOT_IGNORED = false;

    // *****************************************
    // Fields Established by Primary Coordinator
    // within the ChangeIdentifier.
    private Object originator = null;
    private String logfilename = null;
    private int change_number_within_log = 0;

    // *****************************************
    // Fields Updated By a PEER for this change.
    private Object replicator = null;
    private long timestamp_change_issued = 0;
    private boolean change_blocked = false;
    private boolean change_success = false;
    private boolean change_ignored = false;
    private String change_final_notation = null;

    /**
     * Constructor specifying All Necessary Fields.
     */
    public ChangeResponseIdentifier(ChangeIdentifier changeidentifier,
                                    Object replicator,
                                    boolean change_blocked,
                                    boolean change_success,
                                    String change_final_notation) {
        // ********************************
        // Save the Timestamp Issued.
        this.timestamp_change_issued = System.currentTimeMillis();

        // ************************************
        // Save out Original Change Identifier
        // so we match up.
        this.originator
                = changeidentifier.getOriginator();
        this.logfilename
                = changeidentifier.getLogFileName();
        this.change_number_within_log
                = changeidentifier.getChangeNumberWithinLog();

        // ********************************
        // Save our Replicator Object.
        this.replicator = replicator;

        // ********************************
        // Save the Indicators.
        this.change_blocked = change_blocked;
        this.change_success = change_success;
        this.change_ignored = false;

        // ********************************
        // Save the final change notation.
        this.change_final_notation = change_final_notation;

    } // End of Constructor.

    /**
     * Constructor specifying All Necessary Fields.
     */
    public ChangeResponseIdentifier(ChangeIdentifier changeidentifier,
                                    Object replicator,
                                    boolean change_blocked,
                                    boolean change_success,
                                    boolean change_ignored,
                                    String change_final_notation) {
        // ********************************
        // Save the Timestamp Issued.
        this.timestamp_change_issued = System.currentTimeMillis();

        // ************************************
        // Save out Original Change Identifier
        // so we match up.
        this.originator
                = changeidentifier.getOriginator();
        this.logfilename
                = changeidentifier.getLogFileName();
        this.change_number_within_log
                = changeidentifier.getChangeNumberWithinLog();

        // ********************************
        // Save our Replicator Object.
        this.replicator = replicator;

        // ********************************
        // Save the Indicators.
        this.change_blocked = change_blocked;
        this.change_success = change_success;
        this.change_ignored = change_ignored;

        // ********************************
        // Save the final change notation.
        this.change_final_notation = change_final_notation;

    } // End of Constructor.

    /**
     * Get TimeStamp Change was Issued.
     */
    public long getTimestampOfChangeIssued() {
        return this.timestamp_change_issued;
    } // End of getTimestampOfChangeIssued.

    /**
     * Get Log File Name.
     */
    public String getLogFileName() {
        return this.logfilename;
    } // End of getLogFileName.

    /**
     * Get Change Number within the Log.
     */
    public int getChangeNumberWithinLog() {
        return this.change_number_within_log;
    } // End of getChangeNumberWithinLog.

    /**
     * Get Originator.
     */
    public Object getOriginator() {
        return this.originator;
    } // End of getOriginator.

    /**
     * Get Replicator.
     */
    public Object getReplicator() {
        return this.replicator;
    } // End of getReplicator.

    /**
     * Was Change Blocked?
     *
     * @return boolean
     */
    public boolean wasChangeBlocked() {
        return this.change_blocked;
    } // End of wasChangeBlocked.

    /**
     * Set Change Blocked Indicator
     */
    public void setChangeBlocked(final boolean change_blocked) {
        this.change_success = change_blocked;
    } // End of setChangeBlocked.

    /**
     * Was Change Successful?
     *
     * @return boolean
     */
    public boolean wasChangeSuccessful() {
        return this.change_success;
    } // End of wasChangeSuccessful.

    /**
     * Set Change Successful Indicator
     */
    public void setChangeSuccessful(final boolean change_success) {
        this.change_success = change_success;
    } // End of setChangeSuccessful.

    /**
     * Was Change Ignored by some party?
     *
     * @return boolean
     */
    public boolean wasChangeIgnored() {
        return this.change_ignored;
    } // End of wasChangeIgnored.

    /**
     * Set Change Ignored Indicator
     */
    public void setChangeIgnored(final boolean change_ignored) {
        this.change_ignored = change_ignored;
    } // End of setChangeIgnored.

    /**
     * get Change Final Notation.
     *
     * @return
     */
    public String getChangeFinalNotation() {
        return this.change_final_notation;
    } // End of getchangeFinalNotation.

    /**
     * Set the Change Final Notation Information.
     *
     * @param change_final_notation
     */
    public void setChangeFinalNotation(final String change_final_notation) {
        this.change_final_notation = change_final_notation;
    } // End of setchangeFinalNotation.

    /**
     * toString.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Response:[ ");
        sb.append(" Replicator:[" + this.replicator + "]");
        sb.append(", Originator:[" + this.originator + "]");
        sb.append(", LogFileName:[" + this.logfilename + "]");
        sb.append(", ChangeNumInLog:[" + this.change_number_within_log + "]");
        sb.append(", Change Blocked:[" + this.change_blocked + "]");
        sb.append(", Change Success:[" + this.change_success + "]");
        sb.append(", Change Ignored:[" + this.change_ignored + "]");
        sb.append(", TimeStamp Change Issued:[" + new Date(this.timestamp_change_issued).toString() + "]");
        sb.append(", Change Final Notation:[" + this.change_final_notation + "]");
        sb.append("]");
        return sb.toString();
    } // End of tostring Method.

    /**
     * toStateFileString, provides helper method to output
     * current Response Identifier Information in Processed State Form.
     */
    public String toStateFileString() {
        StringBuffer sb = new StringBuffer();
        sb.append("# Response:[\n");
        sb.append("#       Replicator:[" + this.replicator + "]\n");
        sb.append("#       Originator:[" + this.originator + "]\n");
        sb.append("#      LogFileName:[" + this.logfilename + "]\n");
        sb.append("#   ChangeNumInLog:[" + this.change_number_within_log + "]\n");
        sb.append("#   Change Blocked:[" + this.change_blocked + "]\n");
        sb.append("#   Change Success:[" + this.change_success + "]\n");
        sb.append("#   Change Ignored:[" + this.change_ignored + "]\n");
        sb.append("# TimeStamp Change Issued:[" + new Date(this.timestamp_change_issued).toString() + "]\n");
        sb.append("#   Change Final Notation:[" + this.change_final_notation + "]\n");
        sb.append("]\n");
        return sb.toString();
    } // End of toStateFileString Method.

} ///:~ End of ChangeIdentifier Class.
