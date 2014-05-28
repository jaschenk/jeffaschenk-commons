package jeffaschenk.commons.container.security.object;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 *
 * Authentication Token Object
 *
 * @author jeffaschenk@gmail.com
 *         Date: Jun 3, 2010
 *         Time: 6:31:05 AM
 */
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 1109L;

    /**
     * SecuritySessionUserObject
     * <p/>
     */
    private SecuritySessionUserObject securitySessionUserObject;

    /**
     * Default Constructor
     * Used within <code>SecurityServiceProviderImpl</code> Authentication Method.
     *
     * @param principal - Object can be a simple String to an Service Provider Object.
     * @param securitySessionUserObject
     *
     */
    public AuthenticationToken(Object principal,
                               SecuritySessionUserObject securitySessionUserObject) {
        super(principal, securitySessionUserObject.getPassword());
        this.securitySessionUserObject = securitySessionUserObject;
    }

    /**
     * Default Constructor
     * Used within <code>SecurityServiceProviderImpl</code> Authentication Method.
     *
     * @param principal   - Object can be a simple String to an Service Provider Object.
     * @param authorities
     * @param securitySessionUserObject
     *
     */
    public AuthenticationToken(Object principal,
                               Collection<? extends GrantedAuthority> authorities,
                               SecuritySessionUserObject securitySessionUserObject) {
        super(principal, securitySessionUserObject.getPassword(), authorities);
        this.securitySessionUserObject = securitySessionUserObject;
    }

    /**
     * Obtain the Security Session User Object
     *
     * @return
     */
    public SecuritySessionUserObject getSecuritySessionUserObject() {
        return securitySessionUserObject;
    }

    /**
     * Get Details, Overridden to Obtain
     *
     * @return Object Representing securitySessionUserObject
     */
    @Override
    public Object getDetails() {
        return securitySessionUserObject;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.securitySessionUserObject.setPassword(null);
    }

    @Override
    public String getName() {
        return this.getPrincipal().toString();
    }
}
