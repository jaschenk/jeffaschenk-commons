package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Provides a Change Log Status for the entire Log file.
 * This Object is used by the IRRChangeLogRestoreServiceReaderThread for tracking
 * changes against ChangeResponseIdentifier.
 *
 * @author jeff.schenk
 * @since 2005.11.10
 */
public class ChangeLogStatus implements Serializable {

    // *****************************************
    // Fields Established by Primary Coordinator
    private Object originator = null;
    private String logfilename = null;

    // *****************************************
    // Fields Updated By Primary Coordinator.
    // This Map will Contain the Following:
    protected Map<Integer,ChangeLogStatusElement> changelog_elements = new TreeMap<>();

    /**
     * Constructor specifying All Necessary Fields.
     */
    public ChangeLogStatus(Object originator,
                           String logfilename) {
        // ********************************
        // Save our Originator Object.
        this.originator = originator;

        // ********************************
        // Save the logfile Name.
        this.logfilename = logfilename;

    } // End of Constructor.

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
     * toString.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Change Log Status:[ logfilename:[" + this.logfilename + "]");
        sb.append(", Originator:[" + this.originator + "]");

        // *************************************
        // Now Loop to Show all Change Elements
        sb.append(", Change Elements:[");
        Set set = this.changelog_elements.keySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Integer chgnum = (Integer) itr.next();
            sb.append(" Change Number in Log:[" + chgnum.toString() + "] Responses:[");
            sb.append((changelog_elements.get(chgnum)).toString());
            sb.append("] ");
        } // End of For Loop.
        sb.append("]");
        return sb.toString();
    } // End of toString Method.

    /**
     * toStateFileString, provides helper method to output
     * current Object State into a State File.
     */
    public String toStateFileString() {
        StringBuffer sb = new StringBuffer();
        sb.append("# Change Log Status:[ logfilename:[" + this.logfilename + "]\n");
        sb.append("#  Originator:[" + this.originator + "]\n");

        // *************************************
        // Now Loop to Show all Change Elements
        sb.append("#  Change Elements:\n");
        Set set = this.changelog_elements.keySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Integer chgnum = (Integer) itr.next();
            sb.append("#   Change Number in Log:[" + chgnum.toString() + "]\n");
            sb.append("#    Responses:\n");
            sb.append((changelog_elements.get(chgnum)).toStateFileString(false));
            sb.append("#   \n");
        } // End of For Loop.
        sb.append("# \n");
        sb.append("# \n");
        return sb.toString();
    } // End of toStateString Method.

    /**
     * Determine State of a specific Change Log Element for this Change Log Object.
     *
     * @param change_identifier Specific Change Number within Log to be checked.
     * @return boolean Indicator if Element Change is successful or not.
     */
    public boolean determineState(ChangeIdentifier change_identifier) {

        // ********************************
        // Initialize
        if (change_identifier == null) {
            return false;
        }

        // ************************************
        // Obtain the Change Log Element.
        ChangeLogStatusElement clse =
                 changelog_elements.get(
                        new Integer(change_identifier.getChangeNumberWithinLog()));

        // ****************************************
        // If we did not get a Change Log Element
        // Returned, we may have an issue.
        // TODO FIX
        if (clse == null) {
            return false;
        }

        // ************************************
        // Now Allow the Change Element
        // to determine the state.
        return clse.determineElementState();
    } // End of Public Method to Determine State of this Change Log Object.

    /**
     * Helper Method to formulate the proper Change Log Status Idenitifier Key.
     *
     * @param originator
     * @param logfilename
     * @return String -- Formulated Key
     */
    public static String formulateKey(Object originator,
                                      String logfilename) {
        return originator.toString() + ":" + logfilename;
    } // End of formulateKey.

} ///:~ End of ChangeIdentifier Class.
