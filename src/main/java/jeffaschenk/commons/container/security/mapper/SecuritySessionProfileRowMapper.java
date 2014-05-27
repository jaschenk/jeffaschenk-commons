package jeffaschenk.commons.container.security.mapper;

import jeffaschenk.commons.container.security.object.SecuritySessionProfileObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row Mapper Implementation for Security Session Profile Object
 * Generation.
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 30, 2010
 *         Time: 7:38:51 AM
 */
public class SecuritySessionProfileRowMapper implements RowMapper {

    /**
     * MapRow Method Implementations
     */
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        /**
         * Construct our Data Object
         */
        SecuritySessionProfileObject securitySessionProfileObject = new SecuritySessionProfileObject();
        /**
         * FIRSTNAME
         */
        if (rs.getString("FIRSTNAME") != null) {
            securitySessionProfileObject.setFirstName(rs.getString("FIRSTNAME"));
        }
        /**
         * LASTNAME
         */
        if (rs.getString("LASTNAME") != null) {
            securitySessionProfileObject.setLastName(rs.getString("LASTNAME"));
        }
        /**
         * ALTHOVERTEXT
         */
        if (rs.getString("ALTHOVERTEXT") != null) {
            securitySessionProfileObject.setAltHoverText(rs.getString("ALTHOVERTEXT"));
        }
        /**
         * AVATARURL
         */
        if (rs.getString("AVATARURL") != null) {
            securitySessionProfileObject.setAvatarUrl(rs.getString("AVATARURL"));
        }
        /**
         * USERLOCALE
         */
        if (rs.getString("USERLOCALE") != null) {
            securitySessionProfileObject.setUserLocale(rs.getString("USERLOCALE"));
        }
        if (rs.getString("USERTIMEZONE") != null) {
            securitySessionProfileObject.setTimeZone(rs.getString("USERTIMEZONE"));
        }
        // **************************
        // Return this Object.
        return securitySessionProfileObject;
    }

}
