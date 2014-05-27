package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Myanmar
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MM {
    /**
     * State Code Enumerator
     */
    MM_07("Ayeyarwady", "MM-07", "division", "15608", CountryCode.MYANMAR),
    MM_02("Bago", "MM-02", "division", "15609", CountryCode.MYANMAR),
    MM_14("Chin", "MM-14", "state", "15617", CountryCode.MYANMAR),
    MM_11("Kachin", "MM-11", "state", "15610", CountryCode.MYANMAR),
    MM_12("Kayah", "MM-12", "state", "15618", CountryCode.MYANMAR),
    MM_13("Kayin", "MM-13", "state", "15611", CountryCode.MYANMAR),
    MM_03("Magway", "MM-03", "division", "15612", CountryCode.MYANMAR),
    MM_04("Mandalay", "MM-04", "division", "15620", CountryCode.MYANMAR),
    MM_15("Mon", "MM-15", "state", "15613", CountryCode.MYANMAR),
    MM_16("Rakhine", "MM-16", "state", "15619", CountryCode.MYANMAR),
    MM_01("Sagaing", "MM-01", "division", "15614", CountryCode.MYANMAR),
    MM_17("Shan", "MM-17", "state", "15615", CountryCode.MYANMAR),
    MM_05("Tanintharyi", "MM-05", "division", "15607", CountryCode.MYANMAR),
    MM_06("Yangon", "MM-06", "division", "15616", CountryCode.MYANMAR);


    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
