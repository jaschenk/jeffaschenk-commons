package jeffaschenk.commons.touchpoint.model.serviceprovider;

import jeffaschenk.commons.types.GeoCodingServiceProviderResponseStatus;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * GeoLocation
 * Provides a JSON driven class to use for Restlet calls to
 * house the Location Information obtained from a Google Maps
 * API Request,
 * <p/>
 *
 * @author jeffaschenk@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocation {

    /**
     * Status of the GeoLocation Request.
     *
     * @see jeffaschenk.commons.types.GeoCodingServiceProviderResponseStatus
     */
    @JsonProperty("status")
    private String status;

    /**
     * Status of the GeoLocation Request.
     */
    @JsonProperty("results")
    private List<GeoLocationResult> results = new ArrayList<GeoLocationResult>();

    public GeoLocation() {
    }

    public GeoLocation(GeoCodingServiceProviderResponseStatus eStatus) {
        this.status = eStatus.name();
    }


    public String getStatus() {
        return status;
    }

    public List<GeoLocationResult> getResults() {
        return results;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResults(List<GeoLocationResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
