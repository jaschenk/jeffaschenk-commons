package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Canada
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CA {
    /**
     * State Code Enumerator
     */
    CA_AB("Alberta","CA-AB","province","12940", CountryCode.CANADA),
    CA_BC("British Columbia","CA-BC","province","12938",CountryCode.CANADA),
    CA_MB("Manitoba","CA-MB","province","12941",CountryCode.CANADA),
    CA_NB("New Brunswick","CA-NB","province","12937",CountryCode.CANADA),
    CA_NL("Newfoundland and Labrador","CA-NL","province","12942",CountryCode.CANADA),
    CA_NT("Northwest Territories","CA-NT","territory","12939",CountryCode.CANADA),
    CA_NS("Nova Scotia","CA-NS","province","12943",CountryCode.CANADA),
    CA_NU("Nunavut","CA-NU","territory","12944",CountryCode.CANADA),
    CA_ON("Ontario","CA-ON","province","12948",CountryCode.CANADA),
    CA_PE("Prince Edward Island","CA-PE","province","12945",CountryCode.CANADA),
    CA_QC("Quebec","CA-QC","province","12946",CountryCode.CANADA),
    CA_SK("Saskatchewan","CA-SK","province","12936",CountryCode.CANADA),
    CA_YT("Yukon Territory","CA-YT","territory","12947",CountryCode.CANADA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
        this.stateProvinceName = stateProvinceName;
        this.stateCode = stateCode;
        this.stateProvinceType = stateProvinceType;
        this.stateNumericCode = stateNumericCode;
        this.countryCode = countryCode;
    }

    public String stateProvinceName() {
        return this.stateProvinceName;
    }

    public String stateCode() {
        return this.stateCode;
    }

    public String stateProvinceType() {
        return this.stateProvinceType;
    }

    public String stateNumericCode() {
        return this.stateNumericCode;
    }

    public CountryCode countryCode() {
        return this.countryCode;
    }

}
