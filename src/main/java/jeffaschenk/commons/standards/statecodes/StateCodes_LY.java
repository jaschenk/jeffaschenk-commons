package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Libyan Arab Jamahiriya
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LY {
    /**
     * State Code Enumerator
     */
    LY_AJ("Ajdabiya","LY-AJ","","15169", CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_HZ("Al Izam al Akhar","LY-HZ","","15180",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_BU("Al Butnan","LY-BU","District","15171",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_JA("Al Jabal al Akhar","LY-JA","District","15174",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_JI("Al Jifarah","LY-JI","District","15181",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_JU("Al Jufrah","LY-JU","District","15175",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_KF("Al Kufrah","LY-KF","District","15176",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_MJ("Al Marj","LY-MJ","District","15182",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_MB("Al Marqab","LY-MB","District","15160",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_QT("Al Qatrun","LY-QT","","20297",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_QB("Al Qubbah","LY-QB","","15163",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_WA("Al Waah","LY-WA","District","15167",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_NQ("An Nuqat al Khams","LY-NQ","District","15185",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_SH("Ash Shati'","LY-SH","District","15189",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_ZA("Az Zawiyah","LY-ZA","District","15168",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_BA("Banghazi","LY-BA","District","15177",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_BW("Bani Walid","LY-BW","","15170",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_DR("Darnah","LY-DR","District","15178",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_GD("Ghadamis","LY-GD","","15172",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_GR("Gharyan","LY-GR","","15179",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_GT("Ghat","LY-GT","District","15173",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_JB("Jaghbub","LY-JB","","20286",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_MI("Misratah","LY-MI","District","15161",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_MZ("Mizdah","LY-MZ","","15187",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_MQ("Murzuq","LY-MQ","District","15186",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_NL("Nalut","LY-NL","District","15162",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_SB("Sabha","LY-SB","District","15184",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_SS("Sabratah Surman","LY-SS","","15164",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_SR("Surt","LY-SR","District","15183",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_TN("Tajura' wa an Nawahi Arba","LY-TN","","20303",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_TB("Tarabulus","LY-TB","District","15165",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_TM("Tarhunah-Masallatah","LY-TM","","15188",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_WD("Wadi al ayat","LY-WD","District","15166",CountryCode.LIBYAN_ARAB_JAMAHIRIYA),
    LY_YJ("Yafran-Jadu","LY-YJ","","15159",CountryCode.LIBYAN_ARAB_JAMAHIRIYA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
