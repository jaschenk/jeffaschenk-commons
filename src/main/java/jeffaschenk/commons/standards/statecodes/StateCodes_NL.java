package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Netherlands
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NL {
    /**
     * State Code Enumerator
     */
    NL_DR("Drenthe","NL-DR","Province","15727", CountryCode.NETHERLANDS),
    NL_FL("Flevoland","NL-FL","Province","15731",CountryCode.NETHERLANDS),
    NL_FR("Friesland","NL-FR","Province","15730",CountryCode.NETHERLANDS),
    NL_GE("Gelderland","NL-GE","Province","15726",CountryCode.NETHERLANDS),
    NL_GR("Groningen","NL-GR","Province","15732",CountryCode.NETHERLANDS),
    NL_LI("Limburg","NL-LI","Province","15735",CountryCode.NETHERLANDS),
    NL_NB("Noord-Brabant","NL-NB","Province","15729",CountryCode.NETHERLANDS),
    NL_NH("Noord-Holland","NL-NH","Province","15736",CountryCode.NETHERLANDS),
    NL_OV("Overijssel","NL-OV","Province","15733",CountryCode.NETHERLANDS),
    NL_UT("Utrecht","NL-UT","Province","15725",CountryCode.NETHERLANDS),
    NL_ZE("Zeeland","NL-ZE","Province","15728",CountryCode.NETHERLANDS),
    NL_ZH("Zuid-Holland","NL-ZH","Province","15734",CountryCode.NETHERLANDS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NL(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
