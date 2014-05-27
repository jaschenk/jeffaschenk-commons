package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Slovakia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SK {
    /**
     * State Code Enumerator
     */
    SK_BC("Banskobystrick\u0253 kraj","SK-BC","Region","18060", CountryCode.SLOVAKIA),
    SK_BL("Bratislavsk\u0253 kraj","SK-BL","Region","18053",CountryCode.SLOVAKIA),
    SK_KI("Ko\u0353ick\u0253 kraj","SK-KI","Region","18059",CountryCode.SLOVAKIA),
    SK_NI("Nitriansky kraj","SK-NI","Region","18054",CountryCode.SLOVAKIA),
    SK_PV("Pre\u0353ovsk\u0253 kraj","SK-PV","Region","18055",CountryCode.SLOVAKIA),
    SK_TC("Trenciansky kraj","SK-TC","Region","18052",CountryCode.SLOVAKIA),
    SK_TA("Trnavsk\u0253 kraj","SK-TA","Region","18056",CountryCode.SLOVAKIA),
    SK_ZI("\u0382ilinsk\u0253 kraj","SK-ZI","Region","18057",CountryCode.SLOVAKIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SK(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
