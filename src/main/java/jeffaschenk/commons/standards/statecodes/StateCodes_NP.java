package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Nepal
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NP {
    /**
     * State Code Enumerator
     */
    NP_BA("Bagmati","NP-BA","","20177", CountryCode.NEPAL),
    NP_BH("Bheri","NP-BH","","20178",CountryCode.NEPAL),
    NP_DH("Dhawalagiri","NP-DH","","20179",CountryCode.NEPAL),
    NP_GA("Gandaki","NP-GA","","20180",CountryCode.NEPAL),
    NP_JA("Janakpur","NP-JA","","20181",CountryCode.NEPAL),
    NP_KA("Karnali","NP-KA","","20182",CountryCode.NEPAL),
    NP_KO("Kosi [Koshi]","NP-KO","","20183",CountryCode.NEPAL),
    NP_LU("Lumbini","NP-LU","","20184",CountryCode.NEPAL),
    NP_MA("Mahakali","NP-MA","","20185",CountryCode.NEPAL),
    NP_ME("Mechi","NP-ME","","20186",CountryCode.NEPAL),
    NP_NA("Narayani","NP-NA","","20187",CountryCode.NEPAL),
    NP_RA("Rapti","NP-RA","","20188",CountryCode.NEPAL),
    NP_SA("Sagarmatha","NP-SA","","20189",CountryCode.NEPAL),
    NP_SE("Seti","NP-SE","","20190",CountryCode.NEPAL);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NP(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
