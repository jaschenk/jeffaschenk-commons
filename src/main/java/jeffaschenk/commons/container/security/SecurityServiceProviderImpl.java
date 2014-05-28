package jeffaschenk.commons.container.security;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import jeffaschenk.commons.container.security.object.*;
import jeffaschenk.commons.environment.SystemEnvironmentBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Security Service Implementation Provides Implementation interface to Spring
 * Security services and Session information for the Authenticated User.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
@Qualifier("securityService")
public class SecurityServiceProviderImpl extends DaoAuthenticationProvider
        implements AuthenticationProvider, SecurityServiceProvider, SecurityConstants {

    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(SecurityServiceProviderImpl.class);

    /**
     * AUTHENTICATION FAILURE Messages
     */
    private static final String FAILURE_MESSAGE_PRINCIPAL_NULL = "Authenticating Principal is null, unable to perform Authentication";

    /**
     * Autowired Services
     */
    //@Autowired
    //private FacebookServiceProvider facebookServiceProvider;

    //@Autowired
    //RegisteredUserDAO registeredUserDAO;

    @Autowired
    private SystemEnvironmentBean systemEnvironment;

    /**
     * Initialize the Singleton Session Cache.
     */
    @PostConstruct
    public synchronized void init() {
        // ************************************
        // Initialization
        log.info("AuthenticationProvider,SecurityServiceProvider starting to Initialize.");

        log.info("AuthenticationProvider,SecurityServiceProvider has been Initialized");

        // ***************************************
        // Proceed with additional Initialization
    }

    /**
     * <p/>
     * Destroy Implemented Spring Interface Method, invoked when bean is removed
     * from container.
     */
    @PreDestroy
    public void destroy() {

        log
                .info("AuthenticationProvider,SecurityServiceProvider have been Removed from the Container.");
    }

    /**
     * Overriding Authentication Method within Spring Security
     *
     * @param authentication
     * @return uthentication
     * @throws AuthenticationException
     *
     */
    @Override
    public final Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (log.isDebugEnabled()) {
            log.debug("Performing Authentication:[" + authentication + "]");
        }
        // *****************************************
        // Initialize
        boolean authorized = false;
        String principal = null;
        SecuritySessionUserObject securitySessionUserObject = null;
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            // *********************************************
            // Process Normal WEB Form Based Login
            // *********************************************
            // Obtain Registered User via String Principal
            principal = authentication.getPrincipal().toString();
            log.warn("Processing a Normal Authentication Request from Principal:[" + principal + "].");
            if ((principal == null) || (principal.isEmpty())) {
                log.warn(FAILURE_MESSAGE_PRINCIPAL_NULL);
                throw new AuthenticationException(FAILURE_MESSAGE_PRINCIPAL_NULL);
            }
            // **************************************
            // Obtain Session User Object.
            securitySessionUserObject
                    = (SecuritySessionUserObject) this.getUserDetailsService().loadUserByUsername(principal);
            // *******************************************
            // Validate either non-Encrypted Password or
            // a Fully Encrypted Password.
            if (securitySessionUserObject.getPassword().length() <= SecurityServiceProviderUtility.MAX_UNENCRYPTED_CREDENTIALS_LENGTH) {
                if (securitySessionUserObject.getPassword().equals(authentication.getCredentials().toString())) {
                    authorized = true;
                }
            } else if (SecurityServiceProviderUtility.checkPassword(authentication.getCredentials().toString(), securitySessionUserObject.getPassword())) {
                authorized = true;
            }

        // ******************************************
        // If credentials are Authorized Access
        // go deeper to obtain Roles and Profile
        // for this User.
        if (authorized)

        {
            // *********************************************
            // Obtain the optional Registered User Profile
            if (securitySessionUserObject.getRegisteredUserProfileId() != null) {
                List<SecuritySessionProfileObject> profiles =
                        ((SecurityServiceJdbcDao) this.getUserDetailsService()).getRegisteredUserProfileForAuthentication(securitySessionUserObject.getRegisteredUserProfileId());
                if ((profiles != null) && (profiles.size() == 1)) {
                    securitySessionUserObject.setSecuritySessionProfileObject(profiles.get(0));
                    if (log.isInfoEnabled()) {
                        log.info("Authenticating Principal:[" + principal + "], has a Profile Assigned.");
                    }
                }
            }
            // ******************************************
            // Obtain the Roles for this RegisteredUser
            securitySessionUserObject.setSecuritySessionRoles(
                    ((SecurityServiceJdbcDao) this.getUserDetailsService()).getRegisteredUserRolesForAuthentication(securitySessionUserObject.getRegisteredUserId()));

            // ******************************************
            // Obtain the Permissions for this RegisteredUser
            securitySessionUserObject.setSecuritySessionPermissions(
                    ((SecurityServiceJdbcDao) this.getUserDetailsService()).getRegisteredUserPermissionsForAuthentication(securitySessionUserObject.getRegisteredUserId()));

            // ******************************************
            // Provide a Default User Role.
            //
            // TODO
            // This should go away in Production!
            // No Role, Then they are should be considered Anonymous.
            // TODO
            if (securitySessionUserObject.getSecuritySessionRoles().size() <= 0) {
                securitySessionUserObject.getSecuritySessionRoles().add(
                        new SecuritySessionRoleObject(
                                new BigInteger("0"), ROLE_USER));
            }
            // *****************************************
            // Build Up Granted Authorities List
            //
            log.info("Building Authorities for Authenticating Principal:[" + principal + "], has [" +
                    securitySessionUserObject.getSecuritySessionRoles().size() + "] Roles Assigned.");
            for (SecuritySessionRoleObject role : securitySessionUserObject.getSecuritySessionRoles()) {
                String security_roleName = (role.getRoleName().toUpperCase().startsWith(ROLE_PREFIX)
                        ? "" : ROLE_PREFIX) + role.getRoleName().toUpperCase();
                if (log.isInfoEnabled()) {
                    log.info("Authenticating Principal:[" + principal + "], has a Security Role of:[" + security_roleName + "] Assigned.");
                }
                authorities.add(new GrantedAuthority(security_roleName));
            }

            // *****************************************
            // Build Up Granted Authorities List
            //
            log.info("Building Authorities for Authenticating Principal:[" + principal + "], has [" +
                    securitySessionUserObject.getSecuritySessionPermissions().size() + "] Permissions Assigned.");
            for (SecuritySessionPermissionObject permission : securitySessionUserObject.getSecuritySessionPermissions()) {
                String security_permissionName = (permission.getPermissionName().toUpperCase().startsWith(PERMISSION_PREFIX)
                        ? "" : PERMISSION_PREFIX) + permission.getPermissionName().toUpperCase();
                if (log.isInfoEnabled()) {
                    log.info("Authenticating Principal:[" + principal + "], has a Security Permission of:[" + security_permissionName + "] Assigned.");
                }
                authorities.add(new GrantedAuthority(security_permissionName));
            }

        } // End of Authorized Additional Methods for Profile and Roles.
        // ***************************************
        // Finish the Validation
        if (authorized)

        {
            String emsg = "Authentication of Principal:[" + principal + "], was Successful.";
            log.info(emsg);
            ((SecurityServiceJdbcDao) this.getUserDetailsService()).setLogInTimeOfRegisteredUser(securitySessionUserObject.getRegisteredUserId(), new Date());
            //RegisteredUser user =
            //registeredUserDAO.getRegisteredUserById(securitySessionUserObject.getRegisteredUserId(),
            //"registeredUserProfile");
            return new AuthenticationToken(principal, authorities, securitySessionUserObject);
        } else

        {
            String emsg = "Authentication of Principal:[" + principal + "], has Failed due to invalid Credentials";
            log.warn(emsg);
            throw new AuthenticationException(emsg);
        }

    }

    /**
     * Indicate this class can return a UserName and Password Token.
     *
     * @param aClass
     * @return boolean indicating if specified class is supported or not.
     */
    @Override
    public boolean supports(Class aClass) {
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass)) {
            return true;
        }
        return (AuthenticationToken.class.isAssignableFrom(aClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecuritySessionUserObject getLoggedInUser() {
        // **************************
        // Obtain Security Context
        SecurityContext securityCtx = SecurityContextHolder.getContext();
        if (securityCtx == null) {
            log.error("Security Context is Null, possible Security Breach Attempt!");
            return null;
        }
        // **************************
        // Obtain Authentication
        // Object, if no Authentication
        // Object (null), then we are
        // an "Anonymous" User.
        //
        Authentication auth = securityCtx.getAuthentication();
        if (auth == null) {
            return null;
        }
        if (auth instanceof AuthenticationToken) {
            if (((AuthenticationToken) auth).getSecuritySessionUserObject() == null) {

            } else {
                return ((AuthenticationToken) auth).getSecuritySessionUserObject();
            }
        } else if (auth instanceof AnonymousAuthenticationToken) {
            return null;
        } else {
            log.warn("Unknown Security Context Authentication of " + auth.getClass().getName());
        }
        return null;
    }

    // New with Spring Security 3.0.3

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking createSuccessAuthentication");
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Invoking doAfterPropertiesSet");
        }
        super.doAfterPropertiesSet();
    }

    @Override
    public UserCache getUserCache() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getUserCache");
        }
        return super.getUserCache();
    }

    @Override
    public boolean isForcePrincipalAsString() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking isForcePrincipalAsString");
        }
        return super.isForcePrincipalAsString();
    }

    @Override
    public boolean isHideUserNotFoundExceptions() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking isHideUserNotFoundExceptions");
        }
        return super.isHideUserNotFoundExceptions();
    }

    @Override
    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setForcePrincipalAsString");
        }
        super.setForcePrincipalAsString(forcePrincipalAsString);
    }

    @Override
    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setHideUserNotFoundExceptions");
        }
        super.setHideUserNotFoundExceptions(hideUserNotFoundExceptions);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setMessageSource");
        }
        super.setMessageSource(messageSource);
    }

    @Override
    public void setUserCache(UserCache userCache) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setUserCache");
        }
        super.setUserCache(userCache);
    }

    @Override
    protected UserDetailsChecker getPreAuthenticationChecks() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getPreAuthenticationChecks");
        }
        return super.getPreAuthenticationChecks();
    }

    @Override
    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setPreAuthenticationChecks");
        }
        super.setPreAuthenticationChecks(preAuthenticationChecks);
    }

    @Override
    protected UserDetailsChecker getPostAuthenticationChecks() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getPostAuthenticationChecks");
        }
        return super.getPostAuthenticationChecks();
    }

    @Override
    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setPostAuthenticationChecks");
        }
        super.setPostAuthenticationChecks(postAuthenticationChecks);
    }

    @Override
    protected boolean isIncludeDetailsObject() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking isIncludeDetailsObject");
        }
        return super.isIncludeDetailsObject();
    }

    /**
     * References our Configuration User Details Service.
     *
     * @return UserDetailsService
     */
    @Override
    protected UserDetailsService getUserDetailsService() {
        return super.getUserDetailsService();
    }

    /**
     * Configures our User Details Service.
     *
     * @param userDetailsService
     */
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setUserDetailsService");
        }
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    protected PasswordEncoder getPasswordEncoder() {
        if (log.isDebugEnabled()) {
            log.debug("Invoking getPasswordEncoder");
        }
        return super.getPasswordEncoder();
    }

    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        if (log.isDebugEnabled()) {
            log.debug("Invoking setPasswordEncoder");
        }
        super.setPasswordEncoder(passwordEncoder);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws org.springframework.security.core.AuthenticationException {
        if (log.isDebugEnabled()) {
            log.debug("Invoking additionalAuthenticationChecks");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }


}

