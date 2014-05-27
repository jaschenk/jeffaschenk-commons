/**
 * QNow
 */
package jeffaschenk.commons.container.security.object;

import java.math.BigInteger;

/**
 * OUTBID
 * Security Session Object
 * Provides Authenticated User or Process Session Information.
 *
 * @author jeff.schenk
 * @version $Id: $
 */
public class SecuritySessionRoleObject implements java.io.Serializable {
    private static final long serialVersionUID = 1109L;

    private BigInteger roleId;

    private String roleName;

    /**
     * Default Constructor
     */
    public SecuritySessionRoleObject() {
        super();
    }

    /**
     * Default Constructor
     */
    public SecuritySessionRoleObject(BigInteger roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
