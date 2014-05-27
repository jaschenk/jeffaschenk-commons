package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Gabon
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GA {
    /**
     * State Code Enumerator
     */
    GA_1("Estuaire","GA-1","Province","13839", CountryCode.GABON),
    GA_2("Haut-Ogoou\u0233","GA-2","Province","13845",CountryCode.GABON),
    GA_3("Moyen-Ogoou\u0233","GA-3","Province","13841",CountryCode.GABON),
    GA_4("Ngouni\u0233","GA-4","Province","13844",CountryCode.GABON),
    GA_5("Nyanga","GA-5","Province","13846",CountryCode.GABON),
    GA_6("Ogoou\u0233-Ivindo","GA-6","Province","13842",CountryCode.GABON),
    GA_7("Ogoou\u0233-Lolo","GA-7","Province","13843",CountryCode.GABON),
    GA_8("Ogoou\u0233-Maritime","GA-8","Province","13840",CountryCode.GABON),
    GA_9("Woleu-Ntem","GA-9","Province","13838",CountryCode.GABON);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
