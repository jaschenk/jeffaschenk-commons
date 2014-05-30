package jeffaschenk.commons.touchpoint.model.serviceprovider;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * GeoLocationAddressComponent
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
public class GeoLocationAddressComponent {

    /**
     * Types for Address Component.
     * types[] is an array indicating the type of the address component.
     * <p/>
     * Address Component Types
     * <p/>
     * The types[] array within the returned result indicates the address type. These types may also be returned within address_components[] arrays to indicate the type of the particular address component. Addresses within the geocoder may have multiple types; the types may be considered "tags". For example, many cities are tagged with the political and locality type.
     * <p/>
     * The following types are supported and returned by the HTTP Geocoder:
     * <p/>
     * street_address indicates a precise street address.
     * route indicates a named route (such as "US 101").
     * intersection indicates a major intersection, usually of two major roads.
     * political indicates a political entity. Usually, this type indicates a polygon of some civil administration.
     *
     * country indicates the national political entity, and is typically the highest order type returned by the Geocoder.
     *
     * administrative_area_level_1 indicates a first-order civil entity below the country level.
     *   Within the United States, these administrative levels are states. Not all nations exhibit these administrative levels.
     * administrative_area_level_2 indicates a second-order civil entity below the country level.
     *   Within the United States, these administrative levels are counties. Not all nations exhibit these administrative levels.
     * administrative_area_level_3 indicates a third-order civil entity below the country level.
     *   This type indicates a minor civil division. Not all nations exhibit these administrative levels.
     *
     * colloquial_area indicates a commonly-used alternative name for the entity.
     * locality indicates an incorporated city or town political entity.
     * sublocality indicates an first-order civil entity below a locality
     * neighborhood indicates a named neighborhood
     * premise indicates a named location, usually a building or collection of buildings with a common name
     * subpremise indicates a first-order entity below a named location, usually a singular building within a collection of buildings with a common name
     * postal_code indicates a postal code as used to address postal mail within the country.
     * natural_feature indicates a prominent natural feature.
     * airport indicates an airport.
     * park indicates a named park.
     * point_of_interest indicates a named point of interest. Typically, these "POI"s are prominent local entities that don't easily fit in another category such as "Empire State Building" or "Statue of Liberty."
     * <p/>
     * In addition to the above, address components may exhibit the following types:
     * <p/>
     * post_box indicates a specific postal box.
     * street_number indicates the precise street number.
     * floor indicates the floor of a building address.
     * room indicates the room of a building address.
     */
    @JsonProperty("types")
    private List<String> types = new ArrayList<String>();

    /**
     * Long Name for Address Component.
     * long_name is the full text description or name of the address component as returned by the Geocoder.
     */
    @JsonProperty("long_name")
    private String longName;

    /**
     * Short Name for Address Component.
     * short_name is an abbreviated textual name for the address component, if available. For example, an address
     * component for the state of Alaska may have a long_name of "Alaska" and a short_name of "AK" using
     * the 2-letter postal abbreviation.
     */
    @JsonProperty("short_name")
    private String shortName;

    public GeoLocationAddressComponent() {
    }

    public List<String> getTypes() {
        return types;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
