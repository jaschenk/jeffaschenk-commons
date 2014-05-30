package jeffaschenk.commons.touchpoint.model.serviceprovider;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

/**
 * GeoLocationCoordinates
 * Provides a JSON driven class to use for Restlet calls to
 * house the Location Information obtained from a Google Maps
 * API Request,
 * <p/>
 *
 * The Results are wrapped by @see GeoLocation
 *
 * @author jeffaschenk@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocationCoordinates {

     @JsonProperty("lat")
     private BigDecimal latitude;

     @JsonProperty("lng")
	 private BigDecimal longitude;

    public GeoLocationCoordinates() {}

    public GeoLocationCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
