package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Finland
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_FI {
    /**
     * State Code Enumerator
     */
    FI_AL("Ahvenanmaan l\u0228\u0228ni","FI-AL","","13703", CountryCode.FINLAND),
    FI_ES("Etel\u0228-Suomen l\u0228\u0228ni","FI-ES","","20230",CountryCode.FINLAND),
    FI_IS("It\u0228-Suomen l\u0228\u0228ni","FI-IS","","13706",CountryCode.FINLAND),
    FI_LL("Lapin l\u0228\u0228ni","FI-LL","","13690",CountryCode.FINLAND),
    FI_LS("L\u0228nsi-Suomen l\u0228\u0228ni","FI-LS","","20233",CountryCode.FINLAND),
    FI_OL("Oulun l\u0228\u0228ni","FI-OL","","20234",CountryCode.FINLAND);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_FI(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
