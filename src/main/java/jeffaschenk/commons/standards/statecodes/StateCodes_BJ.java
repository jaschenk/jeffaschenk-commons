package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Benin
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BJ {
    /**
     * State Code Enumerator
     */
    BJ_AL("Alibori","BJ-AL","Department","12696", CountryCode.BENIN),
    BJ_AK("Atakora","BJ-AK","Department","12702",CountryCode.BENIN),
    BJ_AQ("Atlantique","BJ-AQ","Department","12697",CountryCode.BENIN),
    BJ_BO("Borgou","BJ-BO","Department","12698",CountryCode.BENIN),
    BJ_CO("Collines","BJ-CO","Department","12704",CountryCode.BENIN),
    BJ_DO("Donga","BJ-DO","Department","12705",CountryCode.BENIN),
    BJ_KO("Kouffo","BJ-KO","Department","12699",CountryCode.BENIN),
    BJ_LI("Littoral","BJ-LI","Department","12694",CountryCode.BENIN),
    BJ_MO("Mono","BJ-MO","Department","12700",CountryCode.BENIN),
    BJ_OU("Ou\u0233m\u0233","BJ-OU","Department","12701",CountryCode.BENIN),
    BJ_PL("Plateau","BJ-PL","Department","12693",CountryCode.BENIN),
    BJ_ZO("Zou","BJ-ZO","Department","12692",CountryCode.BENIN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BJ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
