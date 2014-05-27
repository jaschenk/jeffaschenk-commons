package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Costa Rica
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CR {
    /**
     * State Code Enumerator
     */
    CR_A("Alajuela","CR-A","Province","13119", CountryCode.COSTA_RICA),
    CR_C("Cartago","CR-C","Province","13115",CountryCode.COSTA_RICA),
    CR_G("Guanacaste","CR-G","Province","13116",CountryCode.COSTA_RICA),
    CR_H("Heredia","CR-H","Province","13118",CountryCode.COSTA_RICA),
    CR_L("Lim\u0243n","CR-L","Province","13114",CountryCode.COSTA_RICA),
    CR_P("Puntarenas","CR-P","Province","13113",CountryCode.COSTA_RICA),
    CR_SJ("San Jos\u0233","CR-SJ","Province","13117",CountryCode.COSTA_RICA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
