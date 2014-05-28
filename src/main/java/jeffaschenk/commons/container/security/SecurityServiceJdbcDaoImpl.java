/**
 * JAS
 */
package jeffaschenk.commons.container.security;

import jeffaschenk.commons.container.security.mapper.SecuritySessionObjectRowMapper;
import jeffaschenk.commons.container.security.mapper.SecuritySessionPermissionRowMapper;
import jeffaschenk.commons.container.security.mapper.SecuritySessionProfileRowMapper;
import jeffaschenk.commons.container.security.mapper.SecuritySessionRoleRowMapper;
import jeffaschenk.commons.container.security.object.SecuritySessionPermissionObject;
import jeffaschenk.commons.container.security.object.SecuritySessionProfileObject;
import jeffaschenk.commons.container.security.object.SecuritySessionRoleObject;
import jeffaschenk.commons.container.security.object.SecuritySessionUserObject;
import jeffaschenk.commons.types.UserStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Security Service JDBC Implementation Provides Implementation interface to Spring
 * Security services and Session information for the Authenticated User.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
@SuppressWarnings("unchecked")
@Component("securityServiceJdbcDao")
public class SecurityServiceJdbcDaoImpl extends JdbcUserDetailsManager implements SecurityServiceJdbcDao {

    /**
     * DataSource
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Initialized Indicator
     */
    private boolean initialized = false;

    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(SecurityServiceJdbcDaoImpl.class);

    /**
     * SQL Constants
     */
    private static final String UPPER_BEGIN = "trim(upper(";
    private static final String UPPER_END = ")) ";

    private static final String AND = " and ";
    private static final String OR = " or ";
    private static final String WHERE = " where ";

    /**
     * Registered User Session Query
     * Lookup by either User's Email Address or Screen Name.
     * Obtains additional information to populate to our Session Object for use within the Views.
     */
    private static final String REGISTEREDUSER_QUERY =
            "select u.ID, u.REGISTEREDUSERPROFILEID, u.EMAILADDRESS, u.PASSWORD, u.REGISTEREDUSERSID, u.SCREENNAME, u.STATUS FROM REGISTEREDUSER u "
                    + WHERE + "(" + UPPER_BEGIN + "u.EMAILADDRESS" + UPPER_END + "=" + UPPER_BEGIN + "?" + UPPER_END + " )"
                    + OR
                    + "(" + UPPER_BEGIN + "u.SCREENNAME" + UPPER_END + "=" + UPPER_BEGIN + "?" + UPPER_END + " )";
    /**
     * Registered User Session Query
     * Lookup by Facebook UUID
     * Obtains additional information to populate to our Session Object for use within the Views.
     */
    private static final String REGISTEREDUSER_FACEBOOK_UUID_QUERY =
            "select u.ID, u.REGISTEREDUSERPROFILEID, u.EMAILADDRESS, u.PASSWORD, u.REGISTEREDUSERSID, u.SCREENNAME, u.STATUS FROM REGISTEREDUSER u, REGISTEREDUSERPROFILE p "
                    + WHERE + "( u.REGISTEREDUSERPROFILEID = p.ID)" + AND + "( p.FBUUID" + "=" + " ? )";

    /**
     * Role User Session Query
     * Lookup by either User's Email Address or Screen Name.
     * Obtains additional information to populate to our Session Object for use within the Views.
     */
    private static final String REGISTEREDUSER_ROLE_QUERY =
            "select r.ID, r.NAME FROM REGISTEREDUSER_ROLE ur, ROLE r "
                    + WHERE + "( ur.REGISTEREDUSERID = ? and ur.ROLEID = r.ID )";
    /**
     * Permission User Session Query
     * Lookup by either User's Email Address or Screen Name.
     * Obtains additional information to populate to our Session Object for use within the Views.
     */
    private static final String REGISTEREDUSER_PERMISSION_QUERY =
            "select distinct p.ID, p.NAME FROM REGISTEREDUSER_ROLE ur, ROLE r, PERMISSION p, ROLE_PERMISSION rp "
                    + WHERE + "( ur.REGISTEREDUSERID = ? and ur.ROLEID = r.ID and r.ID = rp.ROLEID and rp.PERMISSIONID = p.ID )";

    /**
     * Registered User Profile Session Query
     * Obtain additional Information Regarding the RegisteredUser for Session Object if Available.
     */
    private static final String REGISTEREDUSER_PROFILE_QUERY =
            "select p.FIRSTNAME, p.LASTNAME, p.ALTHOVERTEXT, p.AVATARURL, p.USERLOCALE, p.USERTIMEZONE FROM REGISTEREDUSERPROFILE p "
                    + WHERE + "( p.ID = ? )";
    /**
     * Registered User Login Time Update
     * Once authenticated, the Users Last Login Time will be Trapped and Updated on the DB.
     */
    private static final String REGISTEREDUSER_LOGIN_TIME_UPDATE =
            "update REGISTEREDUSER SET LASTLOGINTIMESTAMP = ? WHERE ID = ?";

    /**
     * Initialize the Singleton Cache. This method is Inject by the Spring
     * Configuration.
     */
    @PostConstruct
    public synchronized void init() {
        // ************************************
        // Initialization
        log.info("Security Service JDBC DAO starting to Initialize.");
        if (this.dataSource == null) {
            String emsg = "No DataSource or Jdbc Template has been Configured for this Bean, will be unable to perform functions.";
            log.error(emsg);
            throw new IllegalStateException(emsg);
        }
        // *****************************************
        // Continue with Initialization
        this.setJdbcTemplate(this.createJdbcTemplate(this.dataSource));
        this.setEnableGroups(true);
        this.setEnableAuthorities(false);
        this.setRolePrefix(SecurityServiceProviderImpl.ROLE_PREFIX);
        log.info("Security Service JDBC DAO has been Initialized.");
        // ***************************************
        // Proceed with additional Initialization
        this.initialized = true;

    }

    /**
     * <p/>
     * Destroy Implemented Spring Interface Method, invoked when bean is removed
     * from container.
     */
    @PreDestroy
    public void destroy() {
        log
                .info("Security Session Service has been Removed from the Container.");
    }


    /**
     * Obtain the Registered User For Authentication using Principal.
     *
     * @param principal
     * @return List<SecuritySessionUserObject> - Normally will contain Null or One Entry.
     */
    @Override
    public List<SecuritySessionUserObject> getRegisteredUserForAuthentication(String principal) {

        return
                (this.initialized) ? this.getJdbcTemplate().query(REGISTEREDUSER_QUERY, new Object[]{principal, principal},
                        new SecuritySessionObjectRowMapper()) : null;
    }

    /**
     * Obtain the Optional Regiustered User Profile For Authentication using the Registered User's Row Identifier.
     *
     * @param registeredUserProfileId
     * @return List<SecuritySessionProfileObject> - Normally will contain Null or One Entry.
     */
    @Override
    public List<SecuritySessionProfileObject> getRegisteredUserProfileForAuthentication(BigInteger registeredUserProfileId) {

        return
                (this.initialized) ? this.getJdbcTemplate().query(REGISTEREDUSER_PROFILE_QUERY, new Object[]{new BigDecimal(registeredUserProfileId)},
                        new SecuritySessionProfileRowMapper()) : null;

    }

    /**
     * Obtain the Regiustered User Roles For Authentication using the Registered User's Row Identifier.
     *
     * @param registeredUserId
     * @return List<SecuritySessionRoleObject> - Roles Assigned to this User.
     */
    @Override
    public List<SecuritySessionRoleObject> getRegisteredUserRolesForAuthentication(BigInteger registeredUserId) {

        return
                (this.initialized) ? this.getJdbcTemplate().query(REGISTEREDUSER_ROLE_QUERY, new Object[]{new BigDecimal(registeredUserId)},
                        new SecuritySessionRoleRowMapper()) : null;
    }

    /**
     * Obtain the Registered User Permissions For Authentication using the Registered User's Row Identifier.
     *
     * @param registeredUserId
     * @return List<SecuritySessionPermissionObject> - Permissions Assigned to this User.
     */
    @Override
    public List<SecuritySessionPermissionObject> getRegisteredUserPermissionsForAuthentication(BigInteger registeredUserId) {
        return
                (this.initialized) ? this.getJdbcTemplate().query(REGISTEREDUSER_PERMISSION_QUERY, new Object[]{new BigDecimal(registeredUserId)},
                        new SecuritySessionPermissionRowMapper()) : null;
    }

    /**
     * Helper method to perform a Database Query to populate
     * the Session Object so that specific information regarding the User is available
     * especially for the view.
     *
     * @param principal
     * @param sessionId
     * @return SecuritySessionUserObject
     */
    @Override
    public SecuritySessionUserObject createSecuritySessionObject(String principal, String sessionId) {
        // ******************************************
        // Obtain the Result List
        List<SecuritySessionUserObject> results = this.getRegisteredUserForAuthentication(principal);
        if (results.size() > 0) {
            SecuritySessionUserObject securitySessionUserObject = results.get(0);
            securitySessionUserObject.setSessionId(sessionId);
            securitySessionUserObject.setPrincipal(principal);
            // *********************************************
            // Obtain the optional Registered User Profile
            if (securitySessionUserObject.getRegisteredUserProfileId() != null) {
                List<SecuritySessionProfileObject> profiles =
                        this.getRegisteredUserProfileForAuthentication(securitySessionUserObject.getRegisteredUserProfileId());
                if ((profiles != null) && (profiles.size() == 1)) {
                    securitySessionUserObject.setSecuritySessionProfileObject(profiles.get(0));
                }
            }
            // ******************************************
            // Obtain the Roles for this RegisteredUser
            securitySessionUserObject.setSecuritySessionRoles(
                    this.getRegisteredUserRolesForAuthentication(securitySessionUserObject.getRegisteredUserId()));
            // ******************************************
            // Obtain the Permissions for this RegisteredUser
            securitySessionUserObject.setSecuritySessionPermissions(
                    this.getRegisteredUserPermissionsForAuthentication(securitySessionUserObject.getRegisteredUserId()));


            // ******************************************
            // return the Security session object
            return securitySessionUserObject;
        } else {
            return null;
        }
    }

    /**
     * Log the Login Time of the Registered User
     *
     * @param registeredUserId
     * @param loginTime
     */
    @Override
    public void setLogInTimeOfRegisteredUser(BigInteger registeredUserId, Date loginTime) {
        if (this.initialized) {
            try {
                this.getJdbcTemplate().update(REGISTEREDUSER_LOGIN_TIME_UPDATE,
                        new Object[]{new java.sql.Timestamp(loginTime.getTime()), new BigDecimal(registeredUserId)},
                        new int[]{java.sql.Types.TIMESTAMP, java.sql.Types.DECIMAL});
            } catch (Exception e) {
                log.error("Exception occurred while updating the Last Login Time for a Registered User, Ignoring, Login Allowed.", e);
            }
        }
    }

    @Override
    public void createUser(UserDetails user) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking createUser");
        }
        super.createUser(user);
    }

    @Override
    public void setUserCache(UserCache userCache) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setuserCache");
        }
        super.setUserCache(userCache);
    }

    @Override
    public void setFindAllGroupsSql(String findAllGroupsSql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setFindAllGroupsSql");
        }
        super.setFindAllGroupsSql(findAllGroupsSql);
    }

    @Override
    public void setChangePasswordSql(String changePasswordSql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setChangePasswordSql");
        }
        super.setChangePasswordSql(changePasswordSql);
    }

    @Override
    public void setUserExistsSql(String userExistsSql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setUserExistsSql");
        }
        super.setUserExistsSql(userExistsSql);
    }

    @Override
    public void setDeleteUserAuthoritiesSql(String deleteUserAuthoritiesSql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setDeleteUserAuthoritiesSql");
        }
        super.setDeleteUserAuthoritiesSql(deleteUserAuthoritiesSql);
    }

    @Override
    public void setCreateAuthoritySql(String createAuthoritySql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setCreateAuthoritySql");
        }
        super.setCreateAuthoritySql(createAuthoritySql);
    }

    @Override
    public void setUpdateUserSql(String updateUserSql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setUpdateUserSql");
        }
        super.setUpdateUserSql(updateUserSql);
    }

    @Override
    public void setDeleteUserSql(String deleteUserSql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setDeleteUserSql");
        }
        super.setDeleteUserSql(deleteUserSql);
    }

    @Override
    public void setCreateUserSql(String createUserSql) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setCreateUserSql");
        }
        super.setCreateUserSql(createUserSql);
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setAuthenticationManager");
        }
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void addGroupAuthority(String groupName, GrantedAuthority authority) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking addGroupAuthority");
        }
        super.addGroupAuthority(groupName, authority);
    }

    @Override
    public void removeGroupAuthority(String groupName, GrantedAuthority authority) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking removeGroupAuthority");
        }
        super.removeGroupAuthority(groupName, authority);
    }

    @Override
    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking findGroupAuthorities");
        }
        return super.findGroupAuthorities(groupName);
    }

    @Override
    public void removeUserFromGroup(String username, String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking removeUserFromGroup");
        }
        super.removeUserFromGroup(username, groupName);
    }

    @Override
    public void addUserToGroup(String username, String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking addUserToGroup");
        }
        super.addUserToGroup(username, groupName);
    }

    @Override
    public void renameGroup(String oldName, String newName) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking renameGroup");
        }
        super.renameGroup(oldName, newName);
    }

    @Override
    public void deleteGroup(String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking deleteGroup");
        }
        super.deleteGroup(groupName);
    }

    @Override
    public void createGroup(String groupName, List<GrantedAuthority> authorities) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking createGroup");
        }
        super.createGroup(groupName, authorities);
    }

    @Override
    public List<String> findUsersInGroup(String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking findUsersInGroup");
        }
        return super.findUsersInGroup(groupName);
    }

    @Override
    public List<String> findAllGroups() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking findAllGroups");
        }
        return super.findAllGroups();
    }

    @Override
    public boolean userExists(String username) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking userExists");
        }
        return super.userExists(username);
    }

    @Override
    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking createNewAuthentication");
        }
        return super.createNewAuthentication(currentAuth, newPassword);
    }

    @Override
    public void updateUser(UserDetails user) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking updateUser");
        }
        super.updateUser(user);
    }

    @Override
    public void deleteUser(String username) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking deleteUser");
        }
        super.deleteUser(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws org.springframework.security.core.AuthenticationException {
        if (log.isDebugEnabled()) {
            log.debug("Invoking changePassword");
        }
        super.changePassword(oldPassword, newPassword);
    }

    @Override
    protected void addCustomAuthorities(String username, List<GrantedAuthority> authorities) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking addCustomAuthorities");
        }
        super.addCustomAuthorities(username, authorities);
    }

    @Override
    public String getUsersByUsernameQuery() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getUsersByUsernameQuery");
        }
        return super.getUsersByUsernameQuery();
    }

    /**
     * load User By UserName
     *
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        List<SecuritySessionUserObject> results =
                this.getRegisteredUserForAuthentication(username);
        // ******************************************
        // Check for nothing found.
        if ((results == null) || (results.size() <= 0)) {
            String emsg = "Authenticating Principal:[" + username + "], Failed due to invalid (Not Found) Principal, please Register";
            log.warn(emsg);
            throw new UsernameNotFoundException(emsg);
        }
        // ******************************************
        // Only Accept an Exact Hit.
        if (results.size() > 1) {
            String emsg = "Authenticating Principal:[" + username + "], lookup yield more than one hit, can not reliably authenticate User";
            log.warn(emsg);
            throw new AuthenticationException(emsg);
        }
        // ******************************************
        // Only Accept an Exact Hit.
        SecuritySessionUserObject securitySessionUserObject = results.get(0);
        if (securitySessionUserObject == null) {
            String emsg = "Authenticating Principal:[" + username + "], lookup yield exactly one hit, but Session Object is null";
            log.warn(emsg);
            throw new AuthenticationException(emsg);
        }
        // ***********************************
        // Check the User Status
        // Can not Login if the User is New,
        // Suspended or Terminated.
        // Provide specific messages for each
        // state.
        securitySessionUserObject.setPrincipal(username);
        if (securitySessionUserObject.getUserStatus().equalsIgnoreCase(UserStatus.SUSPENDED.code())) {
            String emsg = "Authenticating Principal:[" + username + "], Account has been Suspended, Please Call Customer Support.";
            log.warn(emsg);
            throw new AuthenticationException(emsg);
        } else if (securitySessionUserObject.getUserStatus().equalsIgnoreCase(UserStatus.TERMINATED.code())) {
            String emsg = "Authenticating Principal:[" + username + "], Account has been Terminated, Please Call Customer Support.";
            log.warn(emsg);
            throw new AuthenticationException(emsg);
        } else if (securitySessionUserObject.getUserStatus().equalsIgnoreCase(UserStatus.NEW.code())) {
            String emsg = "Authenticating Principal:[" + username + "], Account has not been verified yet, Please Check your Email Inbox for a Registration Email.";
            log.warn(emsg);
            throw new AuthenticationException(emsg);
        } else if (securitySessionUserObject.getUserStatus().equalsIgnoreCase(UserStatus.DEACTIVATED.code())) {
            String emsg = "Authenticating Principal:[" + username + "], Account has been Deactivated, you will need your Account Reactivated.";
            log.warn(emsg);
            throw new AuthenticationException(emsg);
        }
        // *****************************************
        // Return the Object, never allow an object
        // unless Activated?(new|Validated)
        // Nothing authticated yet, just
        // we found our User Object.
        //
        return securitySessionUserObject;
    }

    /**
     * Load All Applicable Users by User Name.
     *
     * @param username
     * @return List<UserDetails>
     */
    @Override
    protected List<UserDetails> loadUsersByUsername(String username) {
        List<SecuritySessionUserObject> results =
                this.getRegisteredUserForAuthentication(username);
        List<UserDetails> filteredResults = new ArrayList<UserDetails>(results.size());
        for (SecuritySessionUserObject securitySessionUserObject : results) {
            if (securitySessionUserObject.getUserStatus().equalsIgnoreCase(UserStatus.VERIFIED.code())) {
                filteredResults.add(securitySessionUserObject);
            }
        }
        // ************************
        // Return Filtered Results
        return filteredResults;
    }

    @Override
    public void setEnableGroups(boolean enableGroups) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setEnableGroups");
        }
        super.setEnableGroups(enableGroups);
    }

    @Override
    protected boolean getEnableGroups() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getEnableGroups");
        }
        return super.getEnableGroups();
    }

    @Override
    public void setEnableAuthorities(boolean enableAuthorities) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setEnableAuthorities");
        }
        super.setEnableAuthorities(enableAuthorities);
    }

    @Override
    protected boolean getEnableAuthorities() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getEnableAuthorities");
        }
        return super.getEnableAuthorities();
    }

    @Override
    public void setUsersByUsernameQuery(String usersByUsernameQueryString) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setUsersByUsernameQuery");
        }
        super.setUsersByUsernameQuery(usersByUsernameQueryString);
    }

    @Override
    protected boolean isUsernameBasedPrimaryKey() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking isUsernameBasedPrimaryKey");
        }
        return super.isUsernameBasedPrimaryKey();
    }

    @Override
    public void setUsernameBasedPrimaryKey(boolean usernameBasedPrimaryKey) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setUsernameBasedPrimaryKey");
        }
        super.setUsernameBasedPrimaryKey(usernameBasedPrimaryKey);
    }

    @Override
    protected String getRolePrefix() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getRolePrefix");
        }
        return super.getRolePrefix();
    }

    @Override
    public void setRolePrefix(String rolePrefix) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setRolePrefix");
        }
        super.setRolePrefix(rolePrefix);
    }

    @Override
    public void setGroupAuthoritiesByUsernameQuery(String queryString) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setGroupAuthoritiesByUsernameQuery");
        }
        super.setGroupAuthoritiesByUsernameQuery(queryString);
    }

    @Override
    protected String getAuthoritiesByUsernameQuery() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getAuthoritiesByUsernameQuery");
        }
        return super.getAuthoritiesByUsernameQuery();
    }

    @Override
    public void setAuthoritiesByUsernameQuery(String queryString) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setAuthoritiesByUsernameQuery");
        }
        super.setAuthoritiesByUsernameQuery(queryString);
    }

    @Override
    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking createUserDetails");
        }
        return super.createUserDetails(username, userFromUserQuery, combinedAuthorities);
    }

    @Override
    protected List<GrantedAuthority> loadGroupAuthorities(String username) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking loadGroupAuthorities");
        }
        return super.loadGroupAuthorities(username);
    }

    @Override
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking loadUserAuthorities");
        }
        return super.loadUserAuthorities(username);
    }

}

