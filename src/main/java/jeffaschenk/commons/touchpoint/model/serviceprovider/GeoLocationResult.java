package jeffaschenk.commons.touchpoint.model.serviceprovider;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * GeoLocationResult
 * Provides a JSON driven class to use for Restlet calls to
 * house the Location Information obtained from a Google Maps
 * API Request,
 * <p/>
 * <p/>
 * The Results are wrapped by @see GeoLocation
 *
 * @author jeffaschenk@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocationResult {

    /**
     * Types contained within this Result.
     * The types[] array indicates the type of the returned result.
     * This array contains a set of one or more tags identifying the type of feature returned in the result.
     * For example, a geocode of "Chicago" returns "locality" which indicates that "Chicago" is a city,
     * and also returns "political" which indicates it is a political entity.
     */
    @JsonProperty("types")
    private List<String> types;

    /**
     * formatted_address is a string containing the human-readable address of this location.
     * Often this address is equivalent to the "postal address," which sometimes differs from country to country.
     * <p/>
     * (Note that some countries, such as the United Kingdom,
     * do not allow distribution of true postal addresses due to licensing restrictions.)
     * <p/>
     * This address is generally composed of one or more address components.
     * For example, the address "111 8th Avenue, New York, NY"
     * contains separate address components for "111" (the street number, "8th Avenue" (the route),
     * "New York" (the city) and "NY" (the US state).
     * These address components contain additional information as noted below.
     */
    @JsonProperty("formatted_address")
    private String formattedAddress;

    /**
     * address_components[] is an array containing the separate address components, as explained above.
     * Each address_component typically contains:
     * o types[] is an array indicating the type of the address component.
     * o long_name is the full text description or name of the address component as returned by the Geocoder.
     * o short_name is an abbreviated textual name for the address component, if available.
     * <p/>
     * For example, an address component for the state of Alaska may have a long_name of "Alaska" and a short_name of "AK"
     * using the 2-letter postal abbreviation.
     * Note that address_components[] may contain more address components than noted within the formatted_address.
     */
    @JsonProperty("address_components")
    private List<GeoLocationAddressComponent> addressComponents = new ArrayList<GeoLocationAddressComponent>();

    /**
     * Location Geometry
     */
    @JsonProperty("geometry")
    private GeoLocationGeometry geometry;


    /**
     * partial_match indicates that the geocoder did not return an exact match for the original request,
     * though it did match part of the requested address.
     * You may wish to examine the original request for misspellings and/or an incomplete address.
     * Partial matches most often occur for street addresses that do not exist within the locality you pass in the request.
     */
    @JsonProperty("partial_match")
    private boolean partialMatch;

    public List<String> getTypes() {
        return types;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public List<GeoLocationAddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public GeoLocationGeometry getGeometry() {
        return geometry;
    }

    public boolean isPartialMatch() {
        return partialMatch;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public void setAddressComponents(List<GeoLocationAddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public void setGeometry(GeoLocationGeometry geometry) {
        this.geometry = geometry;
    }

    public void setPartialMatch(boolean partialMatch) {
        this.partialMatch = partialMatch;
    }

    @Override
    public String toString() {
         return ToStringBuilder.reflectionToString(this);
    }

}
