package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Egypt
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_EG {
    /**
     * State Code Enumerator
     */
    EG_DK("Ad Daqahliyah", "EG-DK", "Governorate", "13607", CountryCode.EGYPT),
    EG_BA("Al Bahr al Ahmar", "EG-BA", "Governorate", "13605", CountryCode.EGYPT),
    EG_BH("Al Buhayrah", "EG-BH", "Governorate", "13606", CountryCode.EGYPT),
    EG_FYM("Al Fayyum", "EG-FYM", "Governorate", "13608", CountryCode.EGYPT),
    EG_GH("Al Gharbiyah", "EG-GH", "Governorate", "13609", CountryCode.EGYPT),
    EG_ALX("Al Iskandariyah", "EG-ALX", "Governorate", "13595", CountryCode.EGYPT),
    EG_IS("Al Ism\u0257`\u0299l\u0299yah", "EG-IS", "Governorate", "13610", CountryCode.EGYPT),
    EG_GZ("Al Jizah", "EG-GZ", "Governorate", "13596", CountryCode.EGYPT),
    EG_MNF("Al Minufiyah", "EG-MNF", "Governorate", "13612", CountryCode.EGYPT),
    EG_MN("Al Minya", "EG-MN", "Governorate", "13591", CountryCode.EGYPT),
    EG_C("Al Qahirah", "EG-C", "Governorate", "13598", CountryCode.EGYPT),
    EG_KB("Al Qalyubiyah", "EG-KB", "Governorate", "13613", CountryCode.EGYPT),
    EG_WAD("Al Wadi al Jadid", "EG-WAD", "Governorate", "13588", CountryCode.EGYPT),
    EG_LX("Al-Uqsur", "EG-LX", "Governorate", "13589", CountryCode.EGYPT),
    EG_SUZ("As Suways", "EG-SUZ", "Governorate", "13601", CountryCode.EGYPT),
    EG_SHR("Ash Sharqiyah", "EG-SHR", "Governorate", "13600", CountryCode.EGYPT),
    EG_ASN("Aswan", "EG-ASN", "Governorate", "13602", CountryCode.EGYPT),
    EG_AST("Asyut", "EG-AST", "Governorate", "13604", CountryCode.EGYPT),
    EG_BNS("Bani Suwayf", "EG-BNS", "Governorate", "13603", CountryCode.EGYPT),
    EG_PTS("B\u0363r Sa`\u0299d", "EG-PTS", "Governorate", "13593", CountryCode.EGYPT),
    EG_DT("Dumyat", "EG-DT", "Governorate", "13594", CountryCode.EGYPT),
    EG_JS("Janub Sina'", "EG-JS", "Governorate", "13611", CountryCode.EGYPT),
    EG_KFS("Kafr ash Shaykh", "EG-KFS", "Governorate", "13592", CountryCode.EGYPT),
    EG_MT("Matr\u0363h", "EG-MT", "Governorate", "13597", CountryCode.EGYPT),
    EG_KN("Qina", "EG-KN", "Governorate", "13599", CountryCode.EGYPT),
    EG_SIN("Shamal Sina'", "EG-SIN", "Governorate", "13590", CountryCode.EGYPT),
    EG_SHG("Suhaj", "EG-SHG", "Governorate", "13614", CountryCode.EGYPT);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_EG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
