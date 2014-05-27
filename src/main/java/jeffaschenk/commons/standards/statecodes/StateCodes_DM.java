package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Dominica
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_DM {
    /**
     * State Code Enumerator
     */
    DM_02("Saint Andrew","DM-02","Parish","13286", CountryCode.DOMINICA),
    DM_03("Saint David","DM-03","Parish","13289",CountryCode.DOMINICA),
    DM_04("Saint George","DM-04","Parish","13293",CountryCode.DOMINICA),
    DM_05("Saint John","DM-05","Parish","13292",CountryCode.DOMINICA),
    DM_06("Saint Joseph","DM-06","Parish","13291",CountryCode.DOMINICA),
    DM_07("Saint Luke","DM-07","Parish","13294",CountryCode.DOMINICA),
    DM_08("Saint Mark","DM-08","Parish","13290",CountryCode.DOMINICA),
    DM_09("Saint Patrick","DM-09","Parish","13285",CountryCode.DOMINICA),
    DM_10("Saint Paul","DM-10","Parish","13287",CountryCode.DOMINICA),
    DM_11("Saint Peter","DM-11","Parish","13288",CountryCode.DOMINICA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_DM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
