package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Zimbabwe
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ZW {
    /**
     * State Code Enumerator
     */
    ZW_BU("Bulawayo","ZW-BU","City","21380", CountryCode.ZIMBABWE),
    ZW_HA("Harare","ZW-HA","City","21379",CountryCode.ZIMBABWE),
    ZW_MA("Manicaland","ZW-MA","Province","21280",CountryCode.ZIMBABWE),
    ZW_MC("Mashonaland Central","ZW-MC","Province","21282",CountryCode.ZIMBABWE),
    ZW_ME("Mashonaland East","ZW-ME","Province","21283",CountryCode.ZIMBABWE),
    ZW_MW("Mashonaland West","ZW-MW","Province","21284",CountryCode.ZIMBABWE),
    ZW_MV("Masvingo","ZW-MV","Province","21287",CountryCode.ZIMBABWE),
    ZW_MN("Matabeleland North","ZW-MN","Province","21285",CountryCode.ZIMBABWE),
    ZW_MS("Matabeleland South","ZW-MS","Province","21286",CountryCode.ZIMBABWE),
    ZW_MI("Midlands","ZW-MI","Province","21281",CountryCode.ZIMBABWE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ZW(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
