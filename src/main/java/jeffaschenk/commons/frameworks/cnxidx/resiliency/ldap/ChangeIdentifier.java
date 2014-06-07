package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import org.jgroups.Address;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.LinkedList;

/**
 * Provides a Change Identifier for every change issued.
 * This Object is used by the IRRChangeLogRestoreServiceReaderThread for tracking
 * changes against ChangeResponseIdentifier.
 *
 * @author jeff.schenk
 * @since 2005.11.08
 */
public class ChangeIdentifier implements Serializable {

    // *****************************************
    // Static Globals.
    public static final boolean CHANGE_NOT_BLOCKED = false;
    public static final boolean CHANGE_BLOCKED = true;

    // *****************************************
    // Fields Established by Primary Coordinator
    private String logfilename = null;
    private int change_number_within_log = 0;
    private Object originator = null;

    private long timestamp_change_sent = 0;
    private Vector<Address> members_at_time_of_change = new Vector<>();
    private boolean blocked = CHANGE_NOT_BLOCKED;

    /**
     * Constructor specifying All Necessary Fields.
     */
    public ChangeIdentifier(Object originator,
                            List<Address> current_members,
                            String logfilename,
                            int change_number_within_log) {
        // ********************************
        // Save the Timestamp.
        this.timestamp_change_sent = System.currentTimeMillis();

        // ********************************
        // Save our Originator Object.
        this.originator = originator;

        // ********************************
        // Save new List of Members.
        members_at_time_of_change.removeAllElements();
        for (int i = 0; i < current_members.size(); i++) {
            members_at_time_of_change.addElement(current_members.get(i));
        }

        // ********************************
        // Save the logfile Name.
        this.logfilename = logfilename;

        // ********************************
        // Save the change within the log.
        this.change_number_within_log = change_number_within_log;

    } // End of Constructor.

    /**
     * Constructor specifying All Necessary Fields.
     */
    public ChangeIdentifier(Object originator,
                            Vector<Address> current_members,
                            String logfilename,
                            int change_number_within_log,
                            boolean blocked) {
        // ********************************
        // Save the Timestamp.
        this.timestamp_change_sent = System.currentTimeMillis();

        // ********************************
        // Save our Originator Object.
        this.originator = originator;

        // ********************************
        // Save new List of Members.
        members_at_time_of_change.removeAllElements();
        for (int i = 0; i < current_members.size(); i++) {
            members_at_time_of_change.addElement(current_members.get(i));
        }

        // ********************************
        // Save the logfile Name.
        this.logfilename = logfilename;

        // ********************************
        // Save the change within the log.
        this.change_number_within_log = change_number_within_log;

        // ********************************
        // Save the Block Indicator.
        this.blocked = blocked;
    } // End of Constructor.

    /**
     * Get TimeStamp Change was Sent.
     */
    public long getTimestampOfChangeSent() {
        return this.timestamp_change_sent;
    } // End of get timestamp_change_sent.

    /**
     * Get Members at Time Change was Sent.
     */
    public Vector getMembersAtTimeOfChangeSent() {
        return this.members_at_time_of_change;
    } // End of getMembersAtTimeOfChangeSent.

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
     * Is this Change Already Blocked?
     */
    public boolean isBlocked() {
        return this.blocked;
    } // End of isBlocked.

    /**
     * toString.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ID:[ logfilename:[" + this.logfilename + "]");
        sb.append(", ChangeNumInLog:[" + this.change_number_within_log + "]");
        sb.append(", Originator:[" + this.originator + "]");
        sb.append(", TimeStamp Change Sent:[" + new Date(this.timestamp_change_sent).toString() + "]");
        sb.append(", Members at Time of Change:[" + this.members_at_time_of_change + "]");
        sb.append(", Blocked before Publish:[" + this.blocked + "]");
        sb.append("]");
        return sb.toString();
    } // End of tostring Method.

} ///:~ End of ChangeIdentifier Class.
