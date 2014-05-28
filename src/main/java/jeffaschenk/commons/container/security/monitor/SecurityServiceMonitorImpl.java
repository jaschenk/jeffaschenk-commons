package jeffaschenk.commons.container.security.monitor;

import jeffaschenk.commons.container.security.object.SecurityServiceMonitorObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * SecurityServiceMonitor
 * Security Service Monitor for collecting statistics and information regarding
 * usage of the Login principal.
 *
 * @author jeffaschenk@gmail.com
 */
@Service("securityServiceMonitor")
public class SecurityServiceMonitorImpl implements SecurityServiceMonitor {

    /**
     * Logging Constant <code>log</code>
     */
    protected static Log log = LogFactory
            .getLog(SecurityServiceMonitor.class);

    /**
     * Monitor by Request and Principal
     */
    private Map<String, SecurityServiceMonitorObject> securityServiceMonitorObjects;

    /**
     * Global Properties and Environment Injected
     */
    @Value("#{systemEnvironmentProperties['security.checkFailedLoginAttempts']}")
    private boolean checkFailedLoginAttempts;

    @Value("#{systemEnvironmentProperties['security.maxFailedLoginAttempts']}")
    private long maxFailedLoginAttempts;

    @Value("#{systemEnvironmentProperties['security.failedLoginSuspendedTimeout']}")
    private int failedLoginSuspendedTimeout;   // In Seconds

    /**
     * Initialize the Singleton Session Cache.
     */
    @PostConstruct
    public synchronized void init() {
        // ************************************
        // Initialization
        log.info("SecurityServiceMonitor starting to Initialize.");

        // ***************************************
        // Create an In-Memory Cache for various
        // statistics such as:
        // Users Log-in Attempts.
        //
        this.securityServiceMonitorObjects
                = new HashMap<String, SecurityServiceMonitorObject>();

        // ***************************************
        // Show Configuration Environment
        log.info("Check Failed Log-In attempts has been " + ((this.checkFailedLoginAttempts) ? "enabled." : "disabled."));
        if (this.checkFailedLoginAttempts) {
            log.info("Check Failed Log-In Maximum attempts before Lock Out is " + this.maxFailedLoginAttempts + " attempts.");
            log.info("Check Failed Log-In Lock-Out or Suspend Time " + this.failedLoginSuspendedTimeout + " seconds.");
        }

        // ***************************************
        // Proceed with additional Initialization
        log.info("SecurityServiceMonitor has been Initialized.");

    }


    /**
     * <p/>
     * Destroy Implemented Spring Interface Method, invoked when bean is removed
     * from container.
     */
    @PreDestroy
    public void destroy() {

        log
                .info("SecurityServiceMonitor has been Removed from the Container.");
        securityServiceMonitorObjects.clear();
        securityServiceMonitorObjects = null;
    }

    /**
     * Monitor Login attempt Requests
     *
     * @param principal
     * @param request
     */
    @Override
    public void monitorLogInAttemptRequest(Object principal, HttpServletRequest request) {
        if (principal == null) {
            return;
        }
        SecurityServiceMonitorObject securityServiceMonitorObject = null;

        if (securityServiceMonitorObjects.containsKey(principal.toString().toLowerCase())) {
            // **********************************
            // Update Existing Monitor Object.
            securityServiceMonitorObject
                    = securityServiceMonitorObjects.get(principal.toString().toLowerCase());
            if (securityServiceMonitorObject != null) {
                securityServiceMonitorObject.incrementTotalAttempts();
                securityServiceMonitorObject.setLastLogInAttempt(new Date());
                securityServiceMonitorObject.setLocalAddress(request.getLocalAddr());
                securityServiceMonitorObject.setLocalName(request.getLocalName());
                securityServiceMonitorObject.setLocalPort(request.getLocalPort());
                securityServiceMonitorObject.setRemoteAddress(request.getRemoteAddr());
                securityServiceMonitorObject.setRemoteName(request.getRemoteHost());
                securityServiceMonitorObject.setRemotePort(request.getRemotePort());
            }
        }
        // **********************************
        // Add a new Monitor Object for
        // future checks.
        if (securityServiceMonitorObject == null) {
            securityServiceMonitorObject = new
                    SecurityServiceMonitorObject(principal,
                    request.getContextPath(),
                    request.getLocalAddr(),
                    request.getLocalName(),
                    request.getLocalPort(),
                    request.getRemoteAddr(),
                    request.getRemoteHost(),
                    request.getRemotePort());
        }
        securityServiceMonitorObjects.put(principal.toString().toLowerCase(), securityServiceMonitorObject);
    }


    /**
     * Monitor Login Request Results
     *
     * @param principal
     * @param request
     * @param successful
     */
    @Override
    public void monitorLogInAttemptRequest(Object principal, HttpServletRequest request, boolean successful) {
        if (log.isDebugEnabled()) {
            log.debug("Monitoring " +
                    ((successful) ? "Successful" : "Unsuccessful")
                    + " Login Attempt for Principal:[" + principal + "] for Request:[" + request.toString() + "]");
        }
        // *****************************
        // Ensure we have a Principal.
        if (principal == null) {
            return;
        }
        SecurityServiceMonitorObject securityServiceMonitorObject = null;

        if (securityServiceMonitorObjects.containsKey(principal.toString().toLowerCase())) {
            // **********************************
            // Update Existing Monitor Object.
            securityServiceMonitorObject
                    = securityServiceMonitorObjects.get(principal.toString().toLowerCase());
            if (securityServiceMonitorObject != null) {
                securityServiceMonitorObject.setLocalAddress(request.getLocalAddr());
                securityServiceMonitorObject.setLocalName(request.getLocalName());
                securityServiceMonitorObject.setLocalPort(request.getLocalPort());
                securityServiceMonitorObject.setRemoteAddress(request.getRemoteAddr());
                securityServiceMonitorObject.setRemoteName(request.getRemoteHost());
                securityServiceMonitorObject.setRemotePort(request.getRemotePort());
                if (successful) {
                    securityServiceMonitorObject.incrementSuccessfulAttempts();
                    securityServiceMonitorObject.setLastSuccessfulLogInAttempt(new Date());
                } else {
                    securityServiceMonitorObject.incrementUnsuccessfulAttempts();
                    securityServiceMonitorObject.setLastUnsuccessfulLogInAttempt(new Date());
                }
            }
        }
        // **********************************
        // Add a new Monitor Object for
        // future checks.
        if (securityServiceMonitorObject == null) {
            securityServiceMonitorObject = new
                    SecurityServiceMonitorObject(principal,
                    request.getContextPath(),
                    request.getLocalAddr(),
                    request.getLocalName(),
                    request.getLocalPort(),
                    request.getRemoteAddr(),
                    request.getRemoteHost(),
                    request.getRemotePort(),
                    successful);
        }
        securityServiceMonitorObjects.put(principal.toString().toLowerCase(), securityServiceMonitorObject);
    }

    /**
     * Report Log-In Attempts for Principal
     *
     * @param principal
     * @return SecurityServiceMonitorObject -- null if nothing contained for specified Principal
     */
    public SecurityServiceMonitorObject reportLogInAttempts(Object principal) {
        return securityServiceMonitorObjects.get(principal.toString().toLowerCase());
    }

    /**
     * Is Login Allowed?
     * <p/>
     * Will determines if a Login is even allowed, if the account is in a lock-out mode due
     * to consistent failed entry attempts
     * then this method will return false, otherwise true.
     *
     * @param principal
     * @param request
     * @return boolean indicating if login attempt allowed at this time or not.
     */
    @Override
    public boolean isLoginAttemptAllowed(String principal, HttpServletRequest request) {
        if (!this.checkFailedLoginAttempts) {
            return true;
        }
        SecurityServiceMonitorObject securityServiceMonitorObject =
                this.reportLogInAttempts(principal);
        if (securityServiceMonitorObject == null) {
            return true;
        }
        // **************************************************
        // We have to determine if the current login attempt
        // can be granted or denied based upon the number
        // of times a log-in has been attempted within a
        // determined time frame or window.
        //
        if (securityServiceMonitorObject.getUnsuccessfulAttemptsInSequence() < this.maxFailedLoginAttempts) {
            return true;
        } else if (securityServiceMonitorObject.getLastUnsuccessfulLogInAttempt() == null) {
            return true;
        }
        if (log.isDebugEnabled()) {
            log.debug("Checking Login Attempts for:[" + securityServiceMonitorObject.toString() + "]");
        }
        // ****************************************
        // Determine if the last attempt has been
        // within the lock-out window?
        // calendar
        Calendar window_begin = Calendar.getInstance();
        window_begin.setTime(securityServiceMonitorObject.getLastUnsuccessfulLogInAttempt());
        Calendar window_end = Calendar.getInstance();
        if ((window_end.getTimeInMillis() - window_begin.getTimeInMillis()) < (this.failedLoginSuspendedTimeout * 1000)) {
            log.warn("Locking out Principal:[" + principal + "], for at least " + this.failedLoginSuspendedTimeout + " seconds, due to Excessive Failed Log-In Attempts!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get the number of Login attempts for this instance.
     *
     * @param principal
     * @return long
     */
    @Override
    public long getPrincipalAttempts(String principal) {
        SecurityServiceMonitorObject securityServiceMonitorObject =
                this.reportLogInAttempts(principal);
        if (securityServiceMonitorObject == null) {
            return 0;
        }
        return securityServiceMonitorObject.getTotalAttempts();
    }

    /**
     * Get the number of Successful Login attempts for this instance.
     *
     * @param principal
     * @return long
     */
    @Override
    public long getPrincipalSuccessfulAttempts(String principal) {
        SecurityServiceMonitorObject securityServiceMonitorObject =
                this.reportLogInAttempts(principal);
        if (securityServiceMonitorObject == null) {
            return 0;
        }
        return securityServiceMonitorObject.getSuccessfulAttemptsInSequence();
    }

    /**
     * Get the number of UnSuccessful Login attempts for this instance.
     *
     * @param principal
     * @return long
     */
    @Override
    public long getPrincipalUnsuccessfulAttempts(String principal) {
        SecurityServiceMonitorObject securityServiceMonitorObject =
                this.reportLogInAttempts(principal);
        if (securityServiceMonitorObject == null) {
            return 0;
        }
        return securityServiceMonitorObject.getUnsuccessfulAttemptsInSequence();
    }

    /**
     * Clear Monitoring Object by principal.
     *
     * @param principal
     */
    @Override
    public void clearPrincipal(String principal) {
        this.securityServiceMonitorObjects.remove(principal.toLowerCase());
    }

    /**
     * Clear All Monitoring Objects.
     */
    @Override
    public void clearAll() {
        this.securityServiceMonitorObjects.clear();
    }

}
