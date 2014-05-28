package jeffaschenk.commons.container.security.vote;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 *
 * Role Voter
 *
 * @author jeffaschenk@gmail.com
 */
public class RoleVoter extends org.springframework.security.access.vote.RoleVoter
        implements SecurityConstants {

    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(RoleVoter.class);

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        return AbstractVoter.simpleVote(log, authentication, object, attributes, RoleVoter.ACCESS_GRANTED, RoleVoter.ACCESS_ABSTAIN);
    }

}
