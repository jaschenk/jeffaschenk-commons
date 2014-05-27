package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Iceland
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_IS {
    /**
     * State Code Enumerator
     */
    IS_7("Austurland", "IS-7", "Region", "14670", CountryCode.ICELAND),
    IS_1("H\u0246fu\u0240borgarsv\u0230\u0240i utan Reykjav\u0237kur", "IS-1", "Region", "14666", CountryCode.ICELAND),
    IS_6("Nor\u0240urland eystra", "IS-6", "Region", "14669", CountryCode.ICELAND),
    IS_5("Nor\u0240urland vestra", "IS-5", "Region", "14664", CountryCode.ICELAND),
    IS_0("Reykjav\u0237k", "IS-0", "City", "19009", CountryCode.ICELAND),
    IS_8("Su\u0240urland", "IS-8", "Region", "14668", CountryCode.ICELAND),
    IS_2("Su\u0240urnes", "IS-2", "Region", "14667", CountryCode.ICELAND),
    IS_4("Vestfir\u0240ir", "IS-4", "Region", "14663", CountryCode.ICELAND),
    IS_3("Vesturland", "IS-3", "Region", "14662", CountryCode.ICELAND);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_IS(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
