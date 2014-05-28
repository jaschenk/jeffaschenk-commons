package jeffaschenk.commons.container.security.object;

/**
 * Security Session Profile Object
 * Provides Authenticated User or Process Session Information.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class SecuritySessionProfileObject implements java.io.Serializable {

    private static final long serialVersionUID = 1109L;

    /**
     * Registered User's First Name
     */
    private String firstName;

    /**
     * Registered User's Last Name
     */
    private String lastName;

    /**
     * Alt Hover Text
     */
    private String altHoverText;

    /**
     * Avatar URL
     */
    private String avatarUrl;

    /**
     * User Locale
     */
    private String userLocale;

    private String timeZone;

    /**
     * Default Constructor
     */
    public SecuritySessionProfileObject() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAltHoverText() {
        return altHoverText;
    }

    public void setAltHoverText(String altHoverText) {
        this.altHoverText = altHoverText;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
