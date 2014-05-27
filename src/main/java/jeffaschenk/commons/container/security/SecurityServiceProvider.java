/**
 * QNow
 */
package jeffaschenk.commons.container.security;

import jeffaschenk.commons.container.security.object.SecuritySessionUserObject;
import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Security Service
 * Provides interface to spring security services and
 * Session information for the Authenticated User.
 *
 * @author jeff.schenk
 * @version $Id: $
 */
public interface SecurityServiceProvider extends AuthenticationProvider {

    /**
     * Returns the logged in user.
     *
     * @return the logged in user. If the user is not logged in, it will return null.
     */
    SecuritySessionUserObject getLoggedInUser();
}
