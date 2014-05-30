package jeffaschenk.commons.touchpoint.model.serviceprovider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * FacebookEventAttendance
 * <p/>
 * Provides RollUp of Event Attendance Counts from FB.
 *
 * @author jeffaschenk@gmail.com
 */
public class FacebookEventAttendance {

    /**
     * Event Members from FQL Query results.
     */
    private List<FacebookEventMember> eventMembers = new ArrayList<FacebookEventMember>(0);

    /**
     * Totals for Attendance within Service Provider Facebook.
     * The reply status of the user for the event being queried.
     * There are four possible return values: attending, unsure, declined, and not_replied.
     * <p/>
     * Total attending will contain total attending including the owner of the Event.
     * UpStream will deduct if required.
     */
    private int totalAttending = 0;

    private int totalUnsure = 0;

    private int totalDeclined = 0;

    private int totalNotReplied = 0;


    /**
     * Default Constructor.
     */
    public FacebookEventAttendance() {
        super();
    }

    public List<FacebookEventMember> getEventMembers() {
        return eventMembers;
    }

    public void setEventMembers(List<FacebookEventMember> eventMembers) {
        if (eventMembers == null) {
            this.eventMembers = new ArrayList<FacebookEventMember>(0);
            this.totalAttending = 0;
            this.totalUnsure = 0;
            this.totalDeclined = 0;
            this.totalNotReplied = 0;
            return;
        }
        // *******************************
        // Save our Reference to the
        // Event Members.
        this.eventMembers = eventMembers;
        Iterator<FacebookEventMember> itr = this.eventMembers.iterator();
        while (itr.hasNext()) {
            FacebookEventMember facebookEventMember = itr.next();
            if (facebookEventMember.isAttending()) {
                this.incrementTotalAttending();
            } else if (facebookEventMember.isUnsure()) {
                this.incrementTotalUnsure();
            } else if (facebookEventMember.isDeclined()) {
                this.incrementTotalDeclined();
            } else if (facebookEventMember.isNotReplied()) {
                this.incrementTotalNotReplied();
            }
        }
    }

    public int getTotalAttending() {
        return totalAttending;
    }

    public void incrementTotalAttending() {
        this.totalAttending++;
    }

    public int getTotalUnsure() {
        return totalUnsure;
    }

    public void incrementTotalUnsure() {
        this.totalUnsure++;
    }

    public int getTotalDeclined() {
        return totalDeclined;
    }

    public void incrementTotalDeclined() {
        this.totalDeclined++;
    }

    public int getTotalNotReplied() {
        return totalNotReplied;
    }

    public void incrementTotalNotReplied() {
        this.totalNotReplied++;
    }

}
