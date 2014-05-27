package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Oman
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_OM {
    /**
     * State Code Enumerator
     */
    OM_DA("Ad Dakhiliyah","OM-DA","Region","15871", CountryCode.OMAN),
    OM_ZA("Adh Dhahirah","OM-ZA","Region","15868",CountryCode.OMAN),
    OM_BA("Al Batinah","OM-BA","Region","15873",CountryCode.OMAN),
    OM_X1("Al Buraymi","OM-X1","Governorate","21361",CountryCode.OMAN),
    OM_WU("Al Wust\u0225","OM-WU","Region","15872",CountryCode.OMAN),
    OM_SH("Ash Sharqiyah","OM-SH","Region","15870",CountryCode.OMAN),
    OM_JA("Dhofar","OM-JA","Governorate","15869",CountryCode.OMAN),
    OM_MA("Masqat","OM-MA","Governorate","15875",CountryCode.OMAN),
    OM_MU("Musandam","OM-MU","Governorate","15874",CountryCode.OMAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_OM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
