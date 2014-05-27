package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Equatorial Guinea
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GQ {
    /**
     * State Code Enumerator
     */
    GQ_AN("Annob\u0243n","GQ-AN","province","13635", CountryCode.EQUATORIAL_GUINEA),
    GQ_BN("Bioko Norte","GQ-BN","province","13632",CountryCode.EQUATORIAL_GUINEA),
    GQ_BS("Bioko Sur","GQ-BS","province","13634",CountryCode.EQUATORIAL_GUINEA),
    GQ_CS("Centro Sur","GQ-CS","province","13633",CountryCode.EQUATORIAL_GUINEA),
    GQ_KN("Kie-Ntem","GQ-KN","province","13630",CountryCode.EQUATORIAL_GUINEA),
    GQ_LI("Litoral","GQ-LI","province","13631",CountryCode.EQUATORIAL_GUINEA),
    GQ_C("Regi\u0243n Continental","GQ-C","region","19440",CountryCode.EQUATORIAL_GUINEA),
    GQ_I("Regi\u0243n Insular","GQ-I","region","19441",CountryCode.EQUATORIAL_GUINEA),
    GQ_WN("Wele-Nz\u0225s","GQ-WN","province","13629",CountryCode.EQUATORIAL_GUINEA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GQ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
