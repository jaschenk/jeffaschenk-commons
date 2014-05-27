package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Lesotho
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LS {
    /**
     * State Code Enumerator
     */
    LS_D("Berea","LS-D","District","15138", CountryCode.LESOTHO),
    LS_B("Butha-Buthe","LS-B","District","15139",CountryCode.LESOTHO),
    LS_C("Leribe","LS-C","District","15145",CountryCode.LESOTHO),
    LS_E("Mafeteng","LS-E","District","15140",CountryCode.LESOTHO),
    LS_A("Maseru","LS-A","District","15146",CountryCode.LESOTHO),
    LS_F("Mohale's Hoek","LS-F","District","15141",CountryCode.LESOTHO),
    LS_J("Mokhotlong","LS-J","District","15142",CountryCode.LESOTHO),
    LS_H("Qacha's Nek","LS-H","District","15137",CountryCode.LESOTHO),
    LS_G("Quthing","LS-G","District","15143",CountryCode.LESOTHO),
    LS_K("Thaba-Tseka","LS-K","District","15144",CountryCode.LESOTHO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LS(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
