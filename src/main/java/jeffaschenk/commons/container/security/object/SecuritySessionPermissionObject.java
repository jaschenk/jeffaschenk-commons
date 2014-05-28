package jeffaschenk.commons.container.security.object;

import java.math.BigInteger;

/**
 *
 * Security Session Object
 * Provides Authenticated User or Process Session Information.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class SecuritySessionPermissionObject implements java.io.Serializable {
    private static final long serialVersionUID = 1109L;

    private BigInteger permissionId;

    private String permissionName;

    /**
     * Default Constructor
     */
    public SecuritySessionPermissionObject() {
        super();
    }

    /**
     * Default Constructor
     */
    public SecuritySessionPermissionObject(BigInteger permissionId, String permissionName) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
    }

    public BigInteger getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(BigInteger permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
