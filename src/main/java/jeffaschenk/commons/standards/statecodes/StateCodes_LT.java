package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Lithuania
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LT {
    /**
     * State Code Enumerator
     */
    LT_AL("Alytaus Apskritis","LT-AL","County","15209", CountryCode.LITHUANIA),
    LT_KU("Kauno Apskritis","LT-KU","County","15205",CountryCode.LITHUANIA),
    LT_KL("Klaipedos Apskritis","LT-KL","County","15208",CountryCode.LITHUANIA),
    LT_MR("Marijampoles Apskritis","LT-MR","County","15206",CountryCode.LITHUANIA),
    LT_PN("Paneve\u0382io Apskritis","LT-PN","County","15202",CountryCode.LITHUANIA),
    LT_TA("Taurages Apskritis","LT-TA","County","15210",CountryCode.LITHUANIA),
    LT_TE("Tel\u0353iu Apskritis","LT-TE","County","15207",CountryCode.LITHUANIA),
    LT_UT("Utenos Apskritis","LT-UT","County","15203",CountryCode.LITHUANIA),
    LT_VL("Vilniaus Apskritis","LT-VL","County","15201",CountryCode.LITHUANIA),
    LT_SA("\u0353iauliu Apskritis","LT-SA","County","15204",CountryCode.LITHUANIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LT(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
