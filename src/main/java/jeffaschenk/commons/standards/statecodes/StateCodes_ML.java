package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Mali
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ML {
    /**
     * State Code Enumerator
     */
    ML_BKO("Bamako","ML-BKO","district","15327", CountryCode.MALI),
    ML_7("Gao","ML-7","region","15329",CountryCode.MALI),
    ML_1("Kayes","ML-1","region","15330",CountryCode.MALI),
    ML_8("Kidal","ML-8","region","15328",CountryCode.MALI),
    ML_2("Koulikoro","ML-2","region","15331",CountryCode.MALI),
    ML_5("Mopti","ML-5","region","15326",CountryCode.MALI),
    ML_3("Sikasso","ML-3","region","15333",CountryCode.MALI),
    ML_4("S\u0233gou","ML-4","region","15332",CountryCode.MALI),
    ML_6("Tombouctou","ML-6","region","15325",CountryCode.MALI);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ML(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
