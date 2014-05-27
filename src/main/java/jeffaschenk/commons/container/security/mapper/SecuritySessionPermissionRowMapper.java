package jeffaschenk.commons.container.security.mapper;

import jeffaschenk.commons.container.security.object.SecuritySessionPermissionObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row Mapper Implementation for Security Session Object
 * Generation.
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 30, 2010
 *         Time: 7:38:51 AM
 */
public class SecuritySessionPermissionRowMapper implements RowMapper {
    /**
     * MapRow Method Implementations
     */
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        /**
         * Construct our Data Object
         */
        SecuritySessionPermissionObject securitySessionPermissionObject = new SecuritySessionPermissionObject();
        /**
         * ID
         */
        if (rs.getBigDecimal("ID") != null) {
            securitySessionPermissionObject.setPermissionId(rs.getBigDecimal("ID").toBigInteger());
        }
        /**
         * NAME
         */
        if (rs.getString("NAME") != null) {
            securitySessionPermissionObject.setPermissionName(rs.getString("NAME"));
        }
        // **************************
        // Return this Object.
        return securitySessionPermissionObject;
    }

}
