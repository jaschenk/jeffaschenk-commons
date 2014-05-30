package jeffaschenk.commons.touchpoint.model.serviceprovider;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.social.facebook.api.FacebookProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * FacebookExtendedProfile
 * Provides a JSON driven class to use for Restlet calls to
 * pull the Facebook User's Extended Profile.
 * <p/>
 * Additional Information to set within Application.
 *
 * @author jeffaschenk@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookExtendedProfile {

    /**
     * Base Facebook Profile
     */
    @JsonProperty("profile")
    FacebookProfile facebookProfile;

    /**
     * The user's timezone offset from UTC, adjusts with Day List Savings / Summer Time.
     *
     * I would think this should be constant.
     *
     */
    @JsonProperty("timezone")
    int timezone;

    /**
     * The User's Locale.
     * A JSON string containing the ISO Language Code and ISO Country Code
     */
    @JsonProperty("locale")
    String locale;

    /**
     * The User's Location.
     * Containing an Id and Name of Facebook Location Object.
     */
    @JsonProperty("location")
    Map<String, String> location = new HashMap<String,String>(2);

    /**
     * The User's Gender.
     */
    @JsonProperty("gender")
    String gender;

    /**
     * The User's Time of Last Update to Profile.
     */
    @JsonProperty("updated_time")
    String updatedTime;


    public int getTimezone() {
        return timezone;
    }

    public String getLocale() {
        return locale;
    }

    public Map<String, String> getLocation() {
        return location;
    }

    public void setLocation(Map<String, String> location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public FacebookProfile getFacebookProfile() {
        return facebookProfile;
    }

    public void setFacebookProfile(FacebookProfile facebookProfile) {
        this.facebookProfile = facebookProfile;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" Id:["+ facebookProfile.getId() +"]");
        sb.append(", Name:["+ facebookProfile.getName() +"]");
        sb.append(", Email:["+ facebookProfile.getEmail() +"]");
        sb.append(", Locale:[" + this.locale + "]");
        sb.append(", TimeZone:[" + this.timezone + "]");
        sb.append(", Location:[" + this.location + "]");
        sb.append(", Gender:[" + this.gender + "]");
        sb.append(", Last Time Users Profile Updated:[" + this.updatedTime + "]");
        return sb.toString();
    }



}
