package jeffaschenk.commons.container.security.vote;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 *
 * Expression Voter
 *
 * @author jeffaschenk@gmail.com
 */
public class ExpressionVoter extends org.springframework.security.web.access.expression.WebExpressionVoter
        implements SecurityConstants {

    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(ExpressionVoter.class);

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        return super.vote(authentication, object, attributes);

    }
}
