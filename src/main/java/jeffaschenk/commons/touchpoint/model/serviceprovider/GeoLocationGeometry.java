package jeffaschenk.commons.touchpoint.model.serviceprovider;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * GeoLocationGeometry
 * Provides a JSON driven class to use for Restlet calls to
 * house the Location Information obtained from a Google Maps
 * API Request,
 * <p/>
 * <p/>
 * geometry contains the following information:
 * <p/>
 * location contains the geocoded latitude,longitude value.
 * For normal address lookups, this field is typically the most important.
 * <p/>
 * location_type stores additional data about the specified location.
 * The following values are currently supported:
 * o "ROOFTOP" indicates that the returned result is a precise geocode for which we have location information accurate down to street address precision.
 * o "RANGE_INTERPOLATED" indicates that the returned result reflects an approximation (usually on a road) interpolated between two precise points (such as intersections). Interpolated results are generally returned when rooftop geocodes are unavailable for a street address.
 * o "GEOMETRIC_CENTER" indicates that the returned result is the geometric center of a result such as a polyline (for example, a street) or polygon (region).
 * o "APPROXIMATE" indicates that the returned result is approximate.
 * <p/>
 * viewport contains the recommended viewport for displaying the returned result,
 * specified as two latitude,longitude values defining the southwest and northeast corner of the viewport bounding box.
 * Generally the viewport is used to frame a result when displaying it to a user.
 * <p/>
 * bounds (optionally returned) stores the bounding box which can fully contain the returned result.
 * Note that these bounds may not match the recommended viewport. (For example, San Francisco includes the Farallon islands, which are technically part of the city, but probably should not be returned in the viewport.)
 * <p/>
 * <p/>
 * <p/>
 * The Results are wrapped by @see GeoLocation
 *
 * @author jeffaschenk@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocationGeometry {

    @JsonProperty("location")
    private GeoLocationCoordinates geoLocationCoordinates;

    @JsonProperty("location_type")
    private String geoLocationType;

    @JsonProperty("viewport")
    private GeoLocationGeometryViewPort geoLocationGeometryViewPort;

    /**
     * Ignoring Bounds for future implementation if needed.
     *
     */
    //@JsonProperty("bounds")
    private String geoLocationBounds;

    /**
     * Default Constructor
     */
    public GeoLocationGeometry() {}


    public GeoLocationCoordinates getGeoLocationCoordinates() {
        return geoLocationCoordinates;
    }

    public String getGeoLocationType() {
        return geoLocationType;
    }

    public String getGeoLocationBounds() {
        return geoLocationBounds;
    }

    public void setGeoLocationCoordinates(GeoLocationCoordinates geoLocationCoordinates) {
        this.geoLocationCoordinates = geoLocationCoordinates;
    }

    public void setGeoLocationType(String geoLocationType) {
        this.geoLocationType = geoLocationType;
    }

    public void setGeoLocationBounds(String geoLocationBounds) {
        this.geoLocationBounds = geoLocationBounds;
    }

    public GeoLocationGeometryViewPort getGeoLocationGeometryViewPort() {
        return geoLocationGeometryViewPort;
    }

    public void setGeoLocationGeometryViewPort(GeoLocationGeometryViewPort geoLocationGeometryViewPort) {
        this.geoLocationGeometryViewPort = geoLocationGeometryViewPort;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
