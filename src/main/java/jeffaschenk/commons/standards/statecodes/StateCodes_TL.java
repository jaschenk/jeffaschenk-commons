package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Timor Leste
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TL {
    /**
     * State Code Enumerator
     */
    TL_AL("Aileu","TL-AL","District","13554", CountryCode.TIMOR_LESTE),
    TL_AN("Ainaro","TL-AN","District","13553",CountryCode.TIMOR_LESTE),
    TL_BA("Baucau","TL-BA","District","13562",CountryCode.TIMOR_LESTE),
    TL_BO("Bobonaro","TL-BO","District","13556",CountryCode.TIMOR_LESTE),
    TL_CO("Cova Lima","TL-CO","District","13557",CountryCode.TIMOR_LESTE),
    TL_DI("Dili","TL-DI","District","13564",CountryCode.TIMOR_LESTE),
    TL_ER("Ermera","TL-ER","District","13558",CountryCode.TIMOR_LESTE),
    TL_LA("Lautem","TL-LA","District","13563",CountryCode.TIMOR_LESTE),
    TL_LI("Liqui\u0231a","TL-LI","District","13559",CountryCode.TIMOR_LESTE),
    TL_MT("Manatuto","TL-MT","District","13560",CountryCode.TIMOR_LESTE),
    TL_MF("Manufahi","TL-MF","District","13552",CountryCode.TIMOR_LESTE),
    TL_OE("Oecussi","TL-OE","District","13555",CountryCode.TIMOR_LESTE),
    TL_VI("Viqueque","TL-VI","District","13561",CountryCode.TIMOR_LESTE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TL(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
