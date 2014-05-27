package jeffaschenk.commons.container.security.monitor;

import jeffaschenk.commons.container.security.object.SecurityServiceMonitorObject;

import javax.servlet.http.HttpServletRequest;

/**
 * SecurityServiceMonitor
 * Security Service Monitor for collecting statistics and information regarding
 * usage of the Login principal.
 *
 * @author jeffaschenk@gmail.com
 */
public interface SecurityServiceMonitor {

    /**
     * Monitor Total Login attempt Requests
     *
     * @param principal
     * @param request
     */
    void monitorLogInAttemptRequest(Object principal, HttpServletRequest request);

    /**
     * Monitor Login Results
     *
     * @param principal
     * @param request
     * @param successful
     */
    void monitorLogInAttemptRequest(Object principal, HttpServletRequest request, boolean successful);

    /**
     * Report Log-In Attempts for Principal
     *
     * @param principal
     * @return SecurityServiceMonitorObject
     */
    SecurityServiceMonitorObject reportLogInAttempts(Object principal);

    /**
     * Is Login Allowed?
     * <p/>
     * Will determines if a Login is even allowed, if the account is in a lock-out mode due
     * to consistent failed antry attempts
     * then this method will return false, otherwise ture.
     *
     * @param principal
     * @param request
     * @return
     */
    boolean isLoginAttemptAllowed(String principal, HttpServletRequest request);

    /**
     * Get the number of Login attempts for this instance.
     *
     * @param principal
     * @return
     */
    long getPrincipalAttempts(String principal);

    /**
     * Get the number of Successful Login attempts for this instance.
     *
     * @param principal
     * @return
     */
    long getPrincipalSuccessfulAttempts(String principal);

    /**
     * Get the number of UnSuccessful Login attempts for this instance.
     *
     * @param principal
     * @return
     */
    long getPrincipalUnsuccessfulAttempts(String principal);

    /**
     * Clear Monitoring Object by principal.
     *
     * @param principal
     */
    void clearPrincipal(String principal);

    /**
     * Clear All Monitoring Objects.
     */
    void clearAll();
}
