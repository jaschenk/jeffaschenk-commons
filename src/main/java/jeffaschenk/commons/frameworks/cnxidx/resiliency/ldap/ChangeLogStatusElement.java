package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.io.Serializable;
import java.util.*;

/**
 * Provides a Change Log Status for the entire Single Change within the Log File.
 * This Object is used by the IRRChangeLogRestoreServiceReaderThread for tracking
 * changes against ChangeResponseIdentifier.
 *
 * @author jeff.schenk
 * @since 2005.11.10
 */
public class ChangeLogStatusElement implements Serializable {

    // *****************************************
    // Fields Established by Primary Coordinator
    private Object originator = null;
    private String logfilename = null;
    private int change_number_within_log = 0;

    // *****************************************
    // Fields Updated By Primary Coordinator.
    protected List<String> member_response_position = new ArrayList<>();
    protected Map<String,ChangeResponseIdentifier> member_responses = new TreeMap<>();

    /**
     * Constructor specifying All Necessary Fields.
     */
    public ChangeLogStatusElement(Object originator,
                                  String logfilename,
                                  int change_number_within_log) {
        // ********************************
        // Save our Originator Object.
        this.originator = originator;

        // ********************************
        // Save the logfile Name.
        this.logfilename = logfilename;

        // ********************************
        // Save the change within the log.
        this.change_number_within_log = change_number_within_log;

    } // End of Constructor.

    /**
     * Get Change Number within the Log.
     */
    public int getChangeNumberWithinLog() {
        return this.change_number_within_log;
    } // End of getChangeNumberWithinLog.

    /**
     * Get Log File Name.
     */
    public String getLogFileName() {
        return this.logfilename;
    } // End of getLogFileName.

    /**
     * Get Originator.
     */
    public Object getOriginator() {
        return this.originator;
    } // End of getOriginator.

    /**
     * Determine State of a specific Change Log Element for this Change Log Object.
     *
     * @return boolean Indicator if this current element can be flagged as successful or not.
     */
    public boolean determineElementState() {

        // ******************************
        // Accumulate Totals to determine
        // State.
        int success = 0;
        int blocked = 0;
        int ignored = 0;
        int failed = 0;
        for (int i = 0; i < member_response_position.size(); i++) {
            ChangeResponseIdentifier cri = (ChangeResponseIdentifier)
                    member_responses.get(member_response_position.get(i).toString());
            if (cri.wasChangeSuccessful()) {
                success++;
            } else if (cri.wasChangeBlocked()) {
                blocked++;
            } else if (cri.wasChangeIgnored()) {
                ignored++;
            } else {
                failed++;
            }
        } // End of For Loop.
        // ********************************
        // Now Determine State based upon
        // Counts.
        // TODO FIX
        if (failed > 0) {
            System.out.println("Element State Failed for Log:[" +
                    this.toStateFileString(true) + "].");
        }

        if (failed > 0) {
            return false;
        }
        // ***********************
        // Return
        return true;
    } // End of Public Method to Determine State of this Change Log Object.

    /**
     * toString.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Change Log Status:[ logfilename:[" + this.logfilename + "]");
        sb.append(", Change Number in Log:[" + this.change_number_within_log + "]\n");
        sb.append(", Originator:[" + this.originator + "]");

        // ********************************
        // Now Loop to Show Members
        // who have confirmed change.
        sb.append(", Member Responses:[  ");
        for (int i = 0; i < member_response_position.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(" ");
            sb.append(member_response_position.get(i).toString());
            sb.append(member_responses.get(member_response_position.get(i).toString()).toString());
        } // End of For Loop.
        sb.append("]");
        return sb.toString();
    } // End of toString Method.

    /**
     * toStateFileString, provides helper method to output
     * current Object State into a State File.
     *
     * @param detail indicator if detail should be provided or suppressed.
     * @return String of Formatted State File of this Current Change Element.
     */
    public String toStateFileString(boolean detail) {
        StringBuffer sb = new StringBuffer();
        if (detail) {
            sb.append("# Change Log Status:[ logfilename:[" + this.logfilename + "]\n");
            sb.append("#  Change Number in Log:[" + this.change_number_within_log + "]\n");
            sb.append("#  Originator:[" + this.originator + "]\n");
        } // End of detail check.

        // ********************************
        // Now Loop to Show Members
        // who have confirmed change.
        sb.append("# Member Responses:\n");
        for (int i = 0; i < member_response_position.size(); i++) {
            sb.append("#   Member: ");
            sb.append(member_response_position.get(i).toString());
            sb.append("\n#   \n");
            sb.append(((ChangeResponseIdentifier) member_responses.get(
                    member_response_position.get(i))).toStateFileString());
            sb.append("#  \n");
            sb.append("#  \n");
        } // End of For Loop.
        sb.append("#  \n");
        return sb.toString();
    } // End of toStateString Method.

    /**
     * helper Method to formulate the proper Change Log Status Idenitifier Key.
     *
     * @param originator
     * @param logfilename
     * @return String -- formulated Change Log Key
     */
    public static String formulateChangeLogKey(Object originator,
                                               String logfilename) {
        return originator.toString() + ":" + logfilename;
    } // End of formulateChangeLogKeyKey.

} ///:~ End of ChangeIdentifier Class.
