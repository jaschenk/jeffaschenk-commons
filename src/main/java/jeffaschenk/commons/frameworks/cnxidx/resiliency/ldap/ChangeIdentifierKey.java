package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.io.Serializable;

/**
 * Provides a Change Identifier Key for every change issued.
 * This Object is used by the IRRChangeLogRestoreServiceReaderThread for tracking
 * changes against ChangeResponseIdentifier.
 *
 * @author jeff.schenk
 * @since 2005.11.10
 */
public class ChangeIdentifierKey implements Serializable, Comparable {

    // *****************************************
    // Fields Established by Primary Coordinator
    private Object originator = null;
    private String logfilename = null;
    private int change_number_within_log = 0;

    /**
     * Constructor specifying All Necessary Fields.
     */
    public ChangeIdentifierKey(Object originator,
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
     * Constructor specifying All Necessary Fields from Identifier Object.
     */
    public ChangeIdentifierKey(ChangeIdentifier changeidentifier) {
        // ********************************
        // Save our Originator Object.
        this.originator = changeidentifier.getOriginator();

        // ********************************
        // Save the logfile Name.
        this.logfilename = changeidentifier.getLogFileName();

        // ********************************
        // Save the change within the log.
        this.change_number_within_log = changeidentifier.getChangeNumberWithinLog();

    } // End of Constructor.

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
     * Equality must be implemented in terms of primary key field
     * equality, and must use instanceof rather than comparing
     * classes directly (some JDO implementations may subclass JDO
     * identity class).
     */
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ChangeIdentifierKey))
            return false;

        ChangeIdentifierKey cik = (ChangeIdentifierKey) other;
        return ((this.originator == null && cik.getOriginator() == null)
                || (this.originator != null && this.originator.equals(cik.getOriginator())))
                && ((this.logfilename == null && cik.getLogFileName() == null)
                || (this.logfilename != null && this.logfilename.equals(cik.getLogFileName())))
                && (this.change_number_within_log == cik.getChangeNumberWithinLog());
    } // End of Equals Method.

    /**
     * Hashcode must also depend on primary key values.
     */
    public int hashCode() {
        return ((this.originator == null) ? 0 : this.originator.hashCode())
                + ((this.logfilename == null) ? 0 : this.logfilename.hashCode())
                + (new Integer(this.change_number_within_log).hashCode())
                % Integer.MAX_VALUE;
    } // end of hashcode method.

    /**
     * toString.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.originator.toString());
        sb.append(":");
        sb.append(this.logfilename);
        sb.append(":");
        sb.append(this.change_number_within_log);
        return sb.toString();
    } // End of tostring Method.

    /**
     * Get Change Log Key Identifier.
     */
    public String getChangeLogIdentifierKey() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.originator.toString());
        sb.append(":");
        sb.append(this.logfilename);
        return sb.toString();
    } // End of getChangeLogIdentifierKey Method.

    /**
     * Comparable Implementation of Comparable for use in TreeMaps as Keys.
     *
     * @param other object to be Compared to this object.
     */
    public int compareTo(Object other) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (other == this) {
            return EQUAL;
        } // Object are Equal.

        if (!(other instanceof ChangeIdentifierKey))
            return AFTER; // Object has Less Precedence.

        // ******************************************
        // Cast the Object for easy usage.
        ChangeIdentifierKey otherkey = (ChangeIdentifierKey) other;

        // ***************
        // Originator
        // objects, including type-safe enums, follow this form
        // note that null objects will throw an exception here
        int comparison = this.originator.toString().compareTo(otherkey.getOriginator().toString());
        if (comparison != EQUAL) return comparison;

        // **************
        // logfilename
        comparison = this.logfilename.compareTo(otherkey.getLogFileName());
        if (comparison != EQUAL) return comparison;

        // *************************
        // change_number_within_log
        if (this.change_number_within_log < otherkey.getChangeNumberWithinLog()) {
            return BEFORE;
        }
        if (this.change_number_within_log > otherkey.getChangeNumberWithinLog()) {
            return AFTER;
        }

        // *************************
        // All Checks Pass, Equal.
        return EQUAL;
    } // End of compareTo Implementation of Comparable for use in TreeMaps as Keys.

} ///:~ End of ChangeIdentifier Class.
