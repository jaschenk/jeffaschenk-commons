package jeffaschenk.commons.container.security;

import jeffaschenk.commons.container.security.object.SecuritySessionPermissionObject;
import jeffaschenk.commons.container.security.object.SecuritySessionProfileObject;
import jeffaschenk.commons.container.security.object.SecuritySessionRoleObject;
import jeffaschenk.commons.container.security.object.SecuritySessionUserObject;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Any overrides for the Security Service JDBC DAO which extends the Spring Base
 * UserDetailsService.
 */
public interface SecurityServiceJdbcDao extends UserDetailsService {

    List<SecuritySessionUserObject> getRegisteredUserForAuthentication(String principal);

    List<SecuritySessionUserObject> getRegisteredUserForAuthentication(BigDecimal facebookUserId);

    List<SecuritySessionProfileObject> getRegisteredUserProfileForAuthentication(BigInteger registeredUserProfileId);

    List<SecuritySessionRoleObject> getRegisteredUserRolesForAuthentication(BigInteger registeredUserId);

    List<SecuritySessionPermissionObject> getRegisteredUserPermissionsForAuthentication(BigInteger registeredUserId);

    SecuritySessionUserObject createSecuritySessionObject(String principal, String sessionId);

    void setLogInTimeOfRegisteredUser(BigInteger registeredUserId, Date loginTime);

}
