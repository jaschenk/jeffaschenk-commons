package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Andorra
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AD {
    /**
     * State Code Enumerator
     */
    AD_07("Andorra la Vella","AD-07","Parish","12382", CountryCode.ANDORRA),
    AD_02("Canillo","AD-02","Parish","12381",CountryCode.ANDORRA),
    AD_03("Encamp","AD-03","Parish","12383",CountryCode.ANDORRA),
    AD_08("Escaldes-Engordany","AD-08","Parish","12380",CountryCode.ANDORRA),
    AD_04("La Massana","AD-04","Parish","12384",CountryCode.ANDORRA),
    AD_05("Ordino","AD-05","Parish","12379",CountryCode.ANDORRA),
    AD_06("Sant Juli\u0224 de L\u0242ria","AD-06","Parish","12378",CountryCode.ANDORRA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AD(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
