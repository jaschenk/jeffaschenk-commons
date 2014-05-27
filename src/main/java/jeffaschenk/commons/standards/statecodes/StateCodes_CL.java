package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Chile
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CL {
    /**
     * State Code Enumerator
     */
    CL_AI("Ais\u0233n del General Carlos Ib\u0225\u0241ez del Campo","CL-AI","Region","13004", CountryCode.CHILE),
    CL_AN("Antofagasta","CL-AN","Region","12998",CountryCode.CHILE),
    CL_AR("Araucan\u0237a","CL-AR","Region","12997",CountryCode.CHILE),
    CL_AP("Arica and Parinacota","CL-AP","Region","21383",CountryCode.CHILE),
    CL_AT("Atacama","CL-AT","Region","12999",CountryCode.CHILE),
    CL_BI("B\u0237o-B\u0237o","CL-BI","Region","13005",CountryCode.CHILE),
    CL_CO("Coquimbo","CL-CO","Region","12996",CountryCode.CHILE),
    CL_LI("Libertador General Bernardo O'Higgins","CL-LI","Region","13000",CountryCode.CHILE),
    CL_LL("Los Lagos","CL-LL","Region","13006",CountryCode.CHILE),
    CL_LR("Los R\u0237os","CL-LR","Region","21384",CountryCode.CHILE),
    CL_MA("Magallanes","CL-MA","Region","13001",CountryCode.CHILE),
    CL_ML("Maule","CL-ML","Region","12995",CountryCode.CHILE),
    CL_RM("Regi\u0243n Metropolitana de Santiago","CL-RM","Region","13002",CountryCode.CHILE),
    CL_TA("Tarapac\u0225","CL-TA","Region","12994",CountryCode.CHILE),
    CL_VS("Valpara\u0237so","CL-VS","Region","13003",CountryCode.CHILE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CL(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
