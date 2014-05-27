package jeffaschenk.commons.container.security.constants;

import jeffaschenk.commons.types.UserRoles;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * OUTBID
 * Security Constants
 *
 * @author (jschenk) jeff.schenk@thought-matirx.com
 */
public interface SecurityConstants {

    /**
     * Globals Security constants and constraints
     */
    static final String ANY = "any";
    static final String HTTPS = "https";

    /**
     * Encoding
     */
    static final String UTF8 = "UTF-8";

    /**
     * Standard Spring Security Request Hook
     */
    static final String SPRING_SECURITY_CHECK_URL_HOOK = "/j_spring_security_check";

    /**
     * Standard Spring Header Names
     */
    static final String USERNAME_HeaderPropertyName = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;
    static final String PASSWORD_HeaderPropertyName = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
    static final String REMEMBER_ME_HeaderPropertyName = "_spring_security_remember_me";

    /**
     * Standard Default Role
     */
    static final String ROLE_PREFIX = "ROLE_OUTBID_";

    static final String ROLE_OUTBID_ADMIN = ROLE_PREFIX + UserRoles.ADMIN.text().toUpperCase();
    static final String ROLE_OUTBID_SYSTEM = ROLE_PREFIX + UserRoles.SYSTEM.text().toUpperCase();
    static final String ROLE_OUTBID_USER = ROLE_PREFIX + UserRoles.USER.text().toUpperCase();

    static final String DEFAULT_ANY_ROLES =
            new String(ROLE_OUTBID_USER + "," + ROLE_OUTBID_ADMIN + "," + ROLE_OUTBID_SYSTEM);


    static final String HAS_ROLE_BEGIN = "hasRole('";
    static final String HAS_ROLE_END = "')";

    static final String PERMISSION_PREFIX = "PERMISSION_OUTBID_";

    static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";


    /**
     * Service Provider Constants
     */
    static final String SIGNED_REQUEST = "signed_request";
    static final String HMAC_SHA256_ALGORITHM = "HMAC-SHA256";
    static final String HMAC_SHA256_TRUE_ALGORITHM_NAME = "HMACSHA256";
    static final String HMAC_SHA1 = "HMACSHA1";
    static final String ACCESS_TOKEN = "access_token";
    static final String CODE = "code";
    static final String OAUTH_REDIRECT = "oauth_redirect";

}
