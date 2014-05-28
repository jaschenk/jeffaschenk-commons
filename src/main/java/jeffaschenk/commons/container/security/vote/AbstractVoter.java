package jeffaschenk.commons.container.security.vote;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import jeffaschenk.commons.container.security.object.GrantedAuthority;
import org.apache.commons.logging.Log;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 *
 * Abstract Voter
 *
 * @author jeffaschenk@gmail.com
 */
public abstract class AbstractVoter implements SecurityConstants {
    /**
     * Simple Rudimentary Vote
     *
     * @param log            -- Voter Log
     * @param authentication -- authentication Object
     * @param object         -- Resource Object being accessed
     * @param attributes     -- Configuration attributes
     * @param ACCESS_GRANTED -- Voter's Access Granted Value
     * @param ACCESS_ABSTAIN -- Voter's Abstained Valued
     * @return int -- Based Upon ACCESSGRANTED or ACCESS_ABSTAIN
     */
    public static int simpleVote(Log log, Authentication authentication, Object object, Collection<ConfigAttribute> attributes, int ACCESS_GRANTED, int ACCESS_ABSTAIN) {
        // ********************************
        // Log in Debug Mode.
        if (log.isDebugEnabled()) {
            log.debug("Voting for Principal:[" + authentication.getPrincipal() + "] using Resource:["
                    + object.toString() + "], Attributes:["
                    + attributes.toString() + "]");
        }
        // ****************************
        // Perform a Rudimentary Vote
        if (hasRole(log, authentication, object, attributes)) {
            return ACCESS_GRANTED;
        }
        log.warn("Abstaining from Granting access for Principal:[" + authentication.getPrincipal() + "] using Resource:[" + object.toString() + "]");
        return ACCESS_ABSTAIN;
    }

    /**
     * Check for a specific Role Name assigned to Authentication against a protected resource
     *
     * @param log
     * @param authentication
     * @param object
     * @param attributes
     * @return boolean indicating if Role applicable or not.
     */
    public static boolean hasRole(Log log, Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // ****************************
        // Perform a Rudimentary Vote
        if (attributes != null) {
            for (ConfigAttribute attribute : attributes) {
                if (attribute.toString() == null) {
                    continue;
                }
                // ********************************
                // Normalize the Attribute
                String roleAttribute = null;
                if ((attribute.toString().startsWith(HAS_ROLE_BEGIN)) && (attribute.toString().endsWith(HAS_ROLE_END))) {
                    roleAttribute = attribute.toString().substring(HAS_ROLE_BEGIN.length(), attribute.toString().lastIndexOf(HAS_ROLE_END));
                } else {
                    roleAttribute = attribute.toString().trim();
                }
                // ********************************
                // Log in Debug Mode.
                if (log.isDebugEnabled()) {
                    log.debug("Does Principal:[" + authentication.getPrincipal() + "] have Role:[" + roleAttribute + "] Assigned?");
                }
                if (authentication.getAuthorities().contains(new GrantedAuthority(roleAttribute))) {
                    if (log.isDebugEnabled()) {
                        log.debug("Role:[" + roleAttribute + "], is been assigned to Principal:[" + authentication.getPrincipal() + "], Granting access to Resource:[" + object.toString() + "]");
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
