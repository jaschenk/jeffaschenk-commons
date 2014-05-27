package jeffaschenk.commons.container.security.mapper;

import jeffaschenk.commons.container.security.object.SecuritySessionUserObject;
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
public class SecuritySessionObjectRowMapper implements RowMapper {

    /**
     * MapRow Method Implementations
     */
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        /**
         * Construct our Data Object
         */
        SecuritySessionUserObject securitySessionUserObject = new SecuritySessionUserObject();
        /**
         * ID
         */
        if (rs.getBigDecimal("ID") != null) {
            securitySessionUserObject.setRegisteredUserId(rs.getBigDecimal("ID").toBigInteger());
        }
        /**
         * REGISTEREDUSERSID
         */
        if (rs.getString("REGISTEREDUSERSID") != null) {
            securitySessionUserObject.setRegisteredUserSid(rs.getString("REGISTEREDUSERSID"));
        }
        /**
         * PASSWORD
         */
        if (rs.getString("PASSWORD") != null) {
            securitySessionUserObject.setPassword(rs.getString("PASSWORD"));
        }
        /**
         * EMAILADDRESS
         */
        if (rs.getString("EMAILADDRESS") != null) {
            securitySessionUserObject.setEmailAddress(rs.getString("EMAILADDRESS"));
        }
        /**
         * SCREENNAME
         */
        if (rs.getString("SCREENNAME") != null) {
            securitySessionUserObject.setScreenName(rs.getString("SCREENNAME"));
        }
        /**
         * STATUS
         */
        if (rs.getString("STATUS") != null) {
            securitySessionUserObject.setUserStatus(rs.getString("STATUS"));
        }
        if (rs.getBigDecimal("REGISTEREDUSERPROFILEID") != null) {
            securitySessionUserObject.setRegisteredUserProfileId(rs.getBigDecimal("REGISTEREDUSERPROFILEID").toBigInteger());
        }
        // **************************
        // Return this Object.
        return securitySessionUserObject;
    }

}
