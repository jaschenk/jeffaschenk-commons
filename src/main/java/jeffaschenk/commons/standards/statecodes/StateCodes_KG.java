package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Kyrgyzstan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KG {
    /**
     * State Code Enumerator
     */
    KG_B("Batken","KG-B","region","15070", CountryCode.KYRGYZSTAN),
    KG_GB("Bishkek","KG-GB","city","15072",CountryCode.KYRGYZSTAN),
    KG_C("Ch\u0252","KG-C","region","15078",CountryCode.KYRGYZSTAN),
    KG_J("Jalal-Abad","KG-J","region","15077",CountryCode.KYRGYZSTAN),
    KG_N("Naryn","KG-N","region","15071",CountryCode.KYRGYZSTAN),
    KG_O("Osh","KG-O","region","15069",CountryCode.KYRGYZSTAN),
    KG_T("Talas","KG-T","region","15075",CountryCode.KYRGYZSTAN),
    KG_Y("Ysyk-K\u0246l","KG-Y","region","15074",CountryCode.KYRGYZSTAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
