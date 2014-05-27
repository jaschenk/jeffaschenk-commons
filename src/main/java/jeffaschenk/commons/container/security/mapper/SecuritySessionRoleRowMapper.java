package jeffaschenk.commons.container.security.mapper;

import jeffaschenk.commons.container.security.object.SecuritySessionRoleObject;
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
public class SecuritySessionRoleRowMapper implements RowMapper {
    /**
     * MapRow Method Implementations
     */
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        /**
         * Construct our Data Object
         */
        SecuritySessionRoleObject securitySessionRoleObject = new SecuritySessionRoleObject();
        /**
         * ID
         */
        if (rs.getBigDecimal("ID") != null) {
            securitySessionRoleObject.setRoleId(rs.getBigDecimal("ID").toBigInteger());
        }
        /**
         * NAME
         */
        if (rs.getString("NAME") != null) {
            securitySessionRoleObject.setRoleName(rs.getString("NAME"));
        }
        // **************************
        // Return this Object.
        return securitySessionRoleObject;
    }

}
