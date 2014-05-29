package jeffaschenk.commons.model.serviceprovider;

import jeffaschenk.commons.types.FacebookRSVPStatusType;

/**
 * FacebookEventMember
 * <p/>
 * Object for Marshaling
 *
 * @author jeffaschenk@gmail.com
 */
public class FacebookEventMember {


    private String eid;

    private String uid;

    private String rsvp_status;

    public FacebookEventMember() {
        super();
    }


    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRsvp_status() {
        return rsvp_status;
    }

    public void setRsvp_status(String rsvp_status) {
        this.rsvp_status = rsvp_status;
    }

    @Override
    public String toString() {
        return "FacebookEventMember{" +
                "eid='" + eid + '\'' +
                ", uid='" + uid + '\'' +
                ", rsvp_status='" + rsvp_status + '\'' +
                '}';
    }

    /**
     * Provide methods for determining RSVP Status.
     * The reply status of the user for the event being queried.
     * There are four possible return values: attending, unsure, declined, and not_replied.
     */

    public boolean isAttending() {
        if (this.rsvp_status.equalsIgnoreCase(FacebookRSVPStatusType.attending.toString())) {
            return true;
        }
        return false;
    }

    public boolean isUnsure() {
        if (this.rsvp_status.equalsIgnoreCase(FacebookRSVPStatusType.unsure.toString())) {
            return true;
        }
        return false;
    }

    public boolean isDeclined() {
        if (this.rsvp_status.equalsIgnoreCase(FacebookRSVPStatusType.declined.toString())) {
            return true;
        }
        return false;
    }

    public boolean isNotReplied() {
        if (this.rsvp_status.equalsIgnoreCase(FacebookRSVPStatusType.not_replied.toString())) {
            return true;
        }
        return false;
    }

}
