/**
 * QNow
 */
package jeffaschenk.commons.container.security.object;

import jeffaschenk.commons.container.session.PagableSearchCriteria;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.*;


/**
 * OUTBID Security Session Object
 * Provides Authenticated User or Process Session Information.
 *
 * @author jeff.schenk
 * @version $Id: $
 */
public class SecuritySessionUserObject implements UserDetails, java.io.Serializable {
    private static final long serialVersionUID = 1109L;

    public static final String OUTBID_SECURITY_SESSION_USER_OBJECT_NAME = "outbidSecuritySessionUserObject";

    /**
     * Session SId
     */
    private String sessionSid;

    /**
     * Session Id
     */
    private String sessionId;

    /**
     * Security Session authenticated Principal
     * <p/>
     * This contains the emailAddress or ScreeName for an OUTBID User.
     */
    private String principal;
    private String password;

    /**
     * Security Session authenticated Principal's
     * Login Time.
     */
    private long loginTime;

    /**
     * Security Session authenticated Principal's
     * Current Calculated Login Duration.
     */
    private long currentDuration;

    /**
     * Registered User's Row Identifier
     */
    private BigInteger registeredUserId;

    /**
     * Registered User's Optional Profile Row Identifier
     */
    private BigInteger registeredUserProfileId;

    /**
     * Registered User's SID
     */
    private String registeredUserSid;

    /**
     * Registered User's Screen Name
     */
    private String screenName;

    /**
     * Registered User's Email Address
     */
    private String emailAddress;

    /**
     * User Status
     */
    private String userStatus;

    /**
     * Sign-on Attempts
     */
    private int signOnAttempts;

    /**
     * Failed Sign-on Attempts
     */
    private int failedSignOnAttempts;

    /**
     * User Profile
     */
    private SecuritySessionProfileObject securitySessionProfileObject;

    /**
     * User Roles
     */
    private List<SecuritySessionRoleObject> securitySessionRoles = new ArrayList<SecuritySessionRoleObject>();

    /**
     * User Permissions
     */
    private List<SecuritySessionPermissionObject> securitySessionPermissions = new ArrayList<SecuritySessionPermissionObject>();


    /**
     * Saved Search Criteria for this Users Session.
     */
    private Map<String, PagableSearchCriteria> savedSearchCriteria
            = new HashMap<String, PagableSearchCriteria>();

    /**
     * Granted Authorities for this User Session
     */
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    private boolean facebookVipUidVerified;

    /**
     * Default Constructor
     */
    public SecuritySessionUserObject() {
        this.loginTime = System.currentTimeMillis();
        this.currentDuration = 0;
    }

    /**
     * Constructor with specified Principal
     *
     * @param principal a {@link java.lang.String} object.
     */
    public SecuritySessionUserObject(String principal) {
        this.principal = principal;
        this.loginTime = System.currentTimeMillis();
        this.currentDuration = 0;
    }


    /**
     * SessionId
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSessionSid() {
        return sessionSid;
    }

    /**
     * Session ID
     *
     * @return String of Represented Session ID if set.
     */
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Obtain the Principal
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPrincipal() {
        return principal;
    }

    /**
     * <p>Setter for the field <code>principal</code>.</p>
     *
     * @param principal a {@link java.lang.String} object.
     */
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    /**
     * Obtain the loginTime
     *
     * @return a long.
     */
    public long getLoginTime() {
        return loginTime;
    }

    /**
     * <p>Setter for the field <code>loginTime</code>.</p>
     *
     * @param loginTime a long.
     */
    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * Obtain the currentDuration
     *
     * @return a long.
     */
    public long getCurrentDuration() {
        return currentDuration;
    }

    /**
     * <p>Setter for the field <code>currentDuration</code>.</p>
     *
     * @param currentDuration a long.
     */
    public void setCurrentDuration(long currentDuration) {
        this.currentDuration = currentDuration;
    }

    public String getRegisteredUserSid() {
        return registeredUserSid;
    }

    public void setRegisteredUserSid(String registeredUserSid) {
        this.registeredUserSid = registeredUserSid;
    }

    public BigInteger getRegisteredUserId() {
        return registeredUserId;
    }

    public void setRegisteredUserId(BigInteger registeredUserId) {
        this.registeredUserId = registeredUserId;
    }

    public BigInteger getRegisteredUserProfileId() {
        return registeredUserProfileId;
    }

    public void setRegisteredUserProfileId(BigInteger registeredUserProfileId) {
        this.registeredUserProfileId = registeredUserProfileId;
    }

    public String getFirstName() {
        return (securitySessionProfileObject == null) ? null : securitySessionProfileObject.getFirstName();
    }

    public String getLastName() {
        return (securitySessionProfileObject == null) ? null : securitySessionProfileObject.getLastName();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public SecuritySessionProfileObject getSecuritySessionProfileObject() {
        return securitySessionProfileObject;
    }

    public void setSecuritySessionProfileObject(SecuritySessionProfileObject securitySessionProfileObject) {
        this.securitySessionProfileObject = securitySessionProfileObject;
    }

    public List<SecuritySessionRoleObject> getSecuritySessionRoles() {
        return securitySessionRoles;
    }

    public void setSecuritySessionRoles(List<SecuritySessionRoleObject> securitySessionRoles) {
        this.securitySessionRoles = securitySessionRoles;
    }

    public List<SecuritySessionPermissionObject> getSecuritySessionPermissions() {
        return securitySessionPermissions;
    }

    public void setSecuritySessionPermissions(List<SecuritySessionPermissionObject> securitySessionPermissions) {
        this.securitySessionPermissions = securitySessionPermissions;
    }

    /**
     * Saved Search Criteria
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, PagableSearchCriteria> getSavedSearchCriteria() {
        return savedSearchCriteria;
    }

    /**
     * <p>Setter for the field <code>savedSearchCriteria</code>.</p>
     *
     * @param savedSearchCriteria the savedSearchCriteria to set
     */
    public void setSavedSearchCriteria(
            Map<String, PagableSearchCriteria> savedSearchCriteria) {
        this.savedSearchCriteria = savedSearchCriteria;
    }

    /**
     * <p>Setter for the field <code>savedSearchCriteria</code>.</p>
     *
     * @param pagableSearchCriteria a {@link jeffaschenk.commons.container.session.PagableSearchCriteria} object.
     */
    public void setSavedSearchCriteria(PagableSearchCriteria pagableSearchCriteria) {
        if ((pagableSearchCriteria != null) && (pagableSearchCriteria.getName() != null) &&
                (pagableSearchCriteria.getName().trim().length() > 0)) {
            this.savedSearchCriteria.put(pagableSearchCriteria.getName(), pagableSearchCriteria);
        }
    }

    /**
     * <p>Getter for the field <code>savedSearchCriteria</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link jeffaschenk.commons.container.session.PagableSearchCriteria} object.
     */
    public PagableSearchCriteria getSavedSearchCriteria(String name) {
        if ((name != null) &&
                (name.trim().length() > 0) &&
                (this.savedSearchCriteria.get(name) != null)) {
            return this.savedSearchCriteria.get(name);
        } else {
            return new PagableSearchCriteria(name);
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.getScreenName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Override for hasCode based upon Principal.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((principal == null) ? 0 : principal.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Override for equals based upon Principal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecuritySessionUserObject other = (SecuritySessionUserObject) obj;
        if (principal == null) {
            if (other.principal != null)
                return false;
        } else if (!principal.equals(other.principal))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Override for toString Method.
     */
    @Override
    public String toString() {
        return "OUTBID Principal:[" + this.getPrincipal() + "], Session Sid:[" + this.getSessionSid() + "], Login Time:[" + this.getLoginTime() + "]";
    }

    public boolean isFacebookVipUidVerified() {
        return facebookVipUidVerified;
    }

    public void setFacebookVipUidVerified(boolean facebookVipUidVerified) {
        this.facebookVipUidVerified = facebookVipUidVerified;
    }
}
