package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Uzbekistan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_UZ {
    /**
     * State Code Enumerator
     */
    UZ_AN("Andijon","UZ-AN","region","18715", CountryCode.UZBEKISTAN),
    UZ_BU("Buxoro","UZ-BU","region","18717",CountryCode.UZBEKISTAN),
    UZ_FA("Farg\u8216ona","UZ-FA","region","18714",CountryCode.UZBEKISTAN),
    UZ_JI("Jizzax","UZ-JI","region","18723",CountryCode.UZBEKISTAN),
    UZ_NG("Namangan","UZ-NG","region","18719",CountryCode.UZBEKISTAN),
    UZ_NW("Navoiy","UZ-NW","region","18712",CountryCode.UZBEKISTAN),
    UZ_QA("Qashqadaryo","UZ-QA","region","18718",CountryCode.UZBEKISTAN),
    UZ_QR("Qoraqalpog\u8216iston Respublikasi","UZ-QR","republic","18713",CountryCode.UZBEKISTAN),
    UZ_SA("Samarqand","UZ-SA","region","18720",CountryCode.UZBEKISTAN),
    UZ_SI("Sirdaryo","UZ-SI","region","18711",CountryCode.UZBEKISTAN),
    UZ_SU("Surxondaryo","UZ-SU","region","18710",CountryCode.UZBEKISTAN),
    UZ_TO("Toshkent","UZ-TO","region","18721",CountryCode.UZBEKISTAN),
    UZ_TK("Toshkent City","UZ-TK","city","20580",CountryCode.UZBEKISTAN),
    UZ_XO("Xorazm","UZ-XO","region","18724",CountryCode.UZBEKISTAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_UZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
