package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Senegal
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SN {
    /**
     * State Code Enumerator
     */
    SN_DK("Dakar","SN-DK","","17998", CountryCode.SENEGAL),
    SN_DB("Diourbel","SN-DB","","17991",CountryCode.SENEGAL),
    SN_FK("Fatick","SN-FK","","17997",CountryCode.SENEGAL),
    SN_KL("Kaolack","SN-KL","","17999",CountryCode.SENEGAL),
    SN_KD("Kolda","SN-KD","","17993",CountryCode.SENEGAL),
    SN_LG("Louga","SN-LG","","17996",CountryCode.SENEGAL),
    SN_MT("Matam","SN-MT","","17994",CountryCode.SENEGAL),
    SN_SL("Saint-Louis","SN-SL","","17992",CountryCode.SENEGAL),
    SN_TC("Tambacounda","SN-TC","","18000",CountryCode.SENEGAL),
    SN_TH("Thi\u0232s","SN-TH","","17995",CountryCode.SENEGAL),
    SN_ZG("Ziguinchor","SN-ZG","","17990",CountryCode.SENEGAL);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
