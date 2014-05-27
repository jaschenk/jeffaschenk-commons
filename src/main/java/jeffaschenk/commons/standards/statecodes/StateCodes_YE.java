package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Yemen
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_YE {
    /**
     * State Code Enumerator
     */
    YE_AM("'Amran", "YE-AM", "Governorate", "20660", CountryCode.YEMEN),
    YE_AB("Abyan", "YE-AB", "Governorate", "18787", CountryCode.YEMEN),
    YE_DA("Ad\u0184 D\u0184ali'", "YE-DA", "Governorate", "20662", CountryCode.YEMEN),
    YE_HU("Al Hudaydah", "YE-HU", "Governorate", "18790", CountryCode.YEMEN),
    YE_BA("Al Baida", "YE-BA", "Governorate", "18784", CountryCode.YEMEN),
    YE_JA("Al Jawf", "YE-JA", "Governorate", "18792", CountryCode.YEMEN),
    YE_MR("Al Mahrah", "YE-MR", "Governorate", "18793", CountryCode.YEMEN),
    YE_MW("Al Mahwit", "YE-MW", "Governorate", "18781", CountryCode.YEMEN),
    YE_DH("Dhamar", "YE-DH", "Governorate", "18789", CountryCode.YEMEN),
    YE_HD("Hadramawt", "YE-HD", "Governorate", "18785", CountryCode.YEMEN),
    YE_HJ("Hajjah", "YE-HJ", "Governorate", "18786", CountryCode.YEMEN),
    YE_IB("Ibb", "YE-IB", "Governorate", "18791", CountryCode.YEMEN),
    YE_LA("Lahej", "YE-LA", "Governorate", "18782", CountryCode.YEMEN), 
    YE_MA("Ma'rib", "YE-MA", "Governorate", "18794", CountryCode.YEMEN),
    YE_SD("Sa`dah", "YE-SD", "Governorate", "18795", CountryCode.YEMEN),
    YE_SN("San\u0703\u0257", "YE-SN", "Governorate", "18779", CountryCode.YEMEN),
    YE_SH("Shabwah", "YE-SH", "Governorate", "18780", CountryCode.YEMEN),
    YE_TA("Ta\u0703izz", "YE-TA", "Governorate", "18778", CountryCode.YEMEN),
    YE_AD("\u0703Adan", "YE-AD", "Governorate", "18783", CountryCode.YEMEN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_YE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
