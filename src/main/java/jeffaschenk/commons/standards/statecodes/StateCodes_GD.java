package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Grenada
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GD {
    /**
     * State Code Enumerator
     */
    GD_01("Saint Andrew","GD-01","Parish","14484", CountryCode.GRENADA),
    GD_02("Saint David","GD-02","Parish","14486",CountryCode.GRENADA),
    GD_03("Saint George","GD-03","Parish","14485",CountryCode.GRENADA),
    GD_04("Saint John","GD-04","Parish","14483",CountryCode.GRENADA),
    GD_05("Saint Mark","GD-05","Parish","14480",CountryCode.GRENADA),
    GD_06("Saint Patrick","GD-06","Parish","14482",CountryCode.GRENADA),
    GD_10("Southern Grenadine Islands","GD-10","Dependency","14481",CountryCode.GRENADA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GD(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
