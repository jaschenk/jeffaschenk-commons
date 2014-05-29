package jeffaschenk.commons.model.serviceprovider;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * GeoLocationGeometryViewPort
 * Provides a JSON driven class to use for Restlet calls to
 * house the Location Information obtained from a Google Maps
 * API Request,
 * <p/>
 * <p/>
 * viewport contains the recommended viewport for displaying the returned result,
 * specified as two latitude,longitude values defining the southwest and northeast corner of the viewport bounding box.
 * Generally the viewport is used to frame a result when displaying it to a user.
 * <p/>
 * Example:
 *  "viewport": {
 *       "southwest": {
 *         "lat": 37.4188244,
 *         "lng": -122.0872906
 *       },
 *       "northeast": {
 *         "lat": 37.4251196,
 *         "lng": -122.0809954
 *      }
 *    }
 *
 * <p/>
 * The Results are wrapped by @see GeoLocation
 *
 * @author jeffaschenk@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocationGeometryViewPort {

    @JsonProperty("southwest")
    private GeoLocationCoordinates southWestGeoLocationCoordinates;

    @JsonProperty("northeast")
    private GeoLocationCoordinates northEastGeoLocationCoordinates;


    public GeoLocationGeometryViewPort() {}

    public GeoLocationCoordinates getSouthWestGeoLocationCoordinates() {
        return southWestGeoLocationCoordinates;
    }

    public void setSouthWestGeoLocationCoordinates(GeoLocationCoordinates southWestGeoLocationCoordinates) {
        this.southWestGeoLocationCoordinates = southWestGeoLocationCoordinates;
    }

    public GeoLocationCoordinates getNorthEastGeoLocationCoordinates() {
        return northEastGeoLocationCoordinates;
    }

    public void setNorthEastGeoLocationCoordinates(GeoLocationCoordinates northEastGeoLocationCoordinates) {
        this.northEastGeoLocationCoordinates = northEastGeoLocationCoordinates;
    }

    @Override
    public String toString() {
       return ToStringBuilder.reflectionToString(this);
    }

}
