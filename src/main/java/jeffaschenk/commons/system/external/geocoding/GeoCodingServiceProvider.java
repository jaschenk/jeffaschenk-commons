package jeffaschenk.commons.system.external.geocoding;


import jeffaschenk.commons.touchpoint.model.serviceprovider.GeoLocation;
import jeffaschenk.commons.touchpoint.model.serviceprovider.GeoLocationCoordinates;

import java.math.BigDecimal;

/**
 *
 * Provides Common GeoCoding Service Provider Interface for performing location lookup and
 * validation.
 *
 * @author jeffaschenk@gmail.com
 */
public interface GeoCodingServiceProvider {

    /**
     * getGeoCodingByAddress
     *
     * @param address - The textual Location Address for obtaining the GeoCoding.
     * @return GeoLocation Object formulated from json representation
     */
    GeoLocation getGeoCodingByAddress(String address);

    /**
     * getGeoCodingByCoordinates -- Aka Reverse Geo Lookup.
     *
     * @param coordinates - The specificed coordinates wrapper for latitude/longitude value for which you wish to
     * obtain the closest, human-readable address.
     *
     * @return GeoLocation Object formulated from json representation
     */
    GeoLocation getGeoCodingByCoordinates(GeoLocationCoordinates coordinates);

    /**
     * getGeoCodingByCoordinates -- Aka Reverse Geo Lookup.
     *
     * @param latitude
     * @param longitude
     * @return GeoLocation
     */
    GeoLocation getGeoCodingByCoordinates(BigDecimal latitude, BigDecimal longitude);

    /**
     * Provides information if GeoCoding Service is available for Use and enabled to
     * service Location requests.
     *
     * @return boolean
     */
    boolean isGeoCodingEnabled();

    /**
     * GeoCoding Service Provider URL
     * @return String
     */
    String getGeoCodingServiceProviderUrl();

    /**
     * GeoCoding Service Provider Output.
     * This Value can be either "json" or "xml".
     *
     * @return String - containing either "json" or "xml"
     *
     */
    String getGeoCodingServiceProviderOutput();

    /**
     * GeoCoding Sensor.  Usually statically set to False
     * as on the Server side we have no known location.
     *
     * @return String
     */
    String getGeoCodingSensorForServer();

    long getTotalNumberOfRequestsPerformed();

    long getDailyNumberOfRequestsPerformed();

    long getTotalNumberOfRequestsBlocked();

    long getDailyNumberOfRequestsBlocked();

    long getNumberOfRequestsPerSecond();

    long getMaximumNumberOfRequestsPerSecondReached();

    long getWhenMaximumNumberOfRequestsPerSecondReached();

    long getLastRequestBlocked();

    long getLastRequestPerformed();

    int getDayInterval();

}
