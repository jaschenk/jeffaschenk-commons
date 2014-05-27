package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: South Africa
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ZA {
    /**
     * State Code Enumerator
     */
    ZA_EC("Eastern Cape","ZA-EC","Province","18103", CountryCode.SOUTH_AFRICA),
    ZA_FS("Free State","ZA-FS","Province","18104",CountryCode.SOUTH_AFRICA),
    ZA_GT("Gauteng","ZA-GT","Province","18109",CountryCode.SOUTH_AFRICA),
    ZA_NL("Kwazulu-Natal","ZA-NL","Province","18105",CountryCode.SOUTH_AFRICA),
    ZA_LP("Limpopo","ZA-LP","Province","18106",CountryCode.SOUTH_AFRICA),
    ZA_MP("Mpumalanga","ZA-MP","Province","18102",CountryCode.SOUTH_AFRICA),
    ZA_NW("North-West","ZA-NW","Province","18101",CountryCode.SOUTH_AFRICA),
    ZA_NC("Northern Cape","ZA-NC","Province","18107",CountryCode.SOUTH_AFRICA),
    ZA_WC("Western Cape","ZA-WC","Province","18108",CountryCode.SOUTH_AFRICA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ZA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
