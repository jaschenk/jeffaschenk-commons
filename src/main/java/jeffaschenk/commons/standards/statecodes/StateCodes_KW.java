package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Kuwait
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KW {
    /**
     * State Code Enumerator
     */
    KW_AH("Al Ahmadi", "KW-AH", "Governorate", "15064", CountryCode.KUWAIT),
    KW_FA("Al Farwaniyah", "KW-FA", "Governorate", "15068", CountryCode.KUWAIT),
    KW_JA("Al Jahrah", "KW-JA", "Governorate", "15063", CountryCode.KUWAIT),
    KW_KU("Al Kuwayt", "KW-KU", "Governorate", "15066", CountryCode.KUWAIT),
    KW_HA("Hawalli", "KW-HA", "Governorate", "15067", CountryCode.KUWAIT),
    KW_MU("Mubarak al-Kabir", "KW-MU", "Governorate", "15065", CountryCode.KUWAIT);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KW(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
