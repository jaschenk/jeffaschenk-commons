package jeffaschenk.commons.container.security.vote;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 *
 * Authenticated Voter
 *
 * @author ßjeffaschenk@gmail.com
 */
public class AuthenticatedVoter extends org.springframework.security.access.vote.AuthenticatedVoter
        implements SecurityConstants {

    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(AuthenticatedVoter.class);

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // ********************************
        // Log in Debug Mode.
        if (log.isDebugEnabled()) {
            log.debug("Authentication Vote for Principal:[" + authentication.getPrincipal() + "] using Resource:["
                    + object.toString() + "], Attributes:["
                    + attributes.toString() + "]");
        }
        return super.vote(authentication, object, attributes);
    }
}
