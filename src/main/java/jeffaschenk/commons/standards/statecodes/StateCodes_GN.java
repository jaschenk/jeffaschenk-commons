package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Guinea
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GN {
    /**
     * State Code Enumerator
     */
    GN_BE("Beyla","GN-BE","Perfecture","14565", CountryCode.GUINEA),
    GN_BF("Boffa","GN-BF","Perfecture","14566",CountryCode.GUINEA),
    GN_BK("Bok\u0233","GN-BK","Perfecture","14567",CountryCode.GUINEA),
    GN_C("Conakry","GN-C","Perfecture","14568",CountryCode.GUINEA),
    GN_CO("Coyah","GN-CO","Perfecture","14569",CountryCode.GUINEA),
    GN_DB("Dabola","GN-DB","Perfecture","14575",CountryCode.GUINEA),
    GN_DL("Dalaba","GN-DL","Perfecture","14570",CountryCode.GUINEA),
    GN_DI("Dinguiraye","GN-DI","Perfecture","14576",CountryCode.GUINEA),
    GN_DU("Dubr\u0233ka","GN-DU","Perfecture","14573",CountryCode.GUINEA),
    GN_FA("Faranah","GN-FA","Perfecture","14577",CountryCode.GUINEA),
    GN_FO("For\u0233cariah","GN-FO","Perfecture","14571",CountryCode.GUINEA),
    GN_FR("Fria","GN-FR","Perfecture","14578",CountryCode.GUINEA),
    GN_GA("Gaoual","GN-GA","Perfecture","14572",CountryCode.GUINEA),
    GN_GU("Gu\u0233k\u0233dou","GN-GU","Perfecture","14579",CountryCode.GUINEA),
    GN_KA("Kankan","GN-KA","Perfecture","14580",CountryCode.GUINEA),
    GN_KD("Kindia","GN-KD","Perfecture","14581",CountryCode.GUINEA),
    GN_KS("Kissidougou","GN-KS","Perfecture","14592",CountryCode.GUINEA),
    GN_KB("Koubia","GN-KB","Perfecture","14582",CountryCode.GUINEA),
    GN_KN("Koundara","GN-KN","Perfecture","14583",CountryCode.GUINEA),
    GN_KO("Kouroussa","GN-KO","Perfecture","14560",CountryCode.GUINEA),
    GN_KE("K\u0233rouan\u0233","GN-KE","Perfecture","14561",CountryCode.GUINEA),
    GN_LA("Lab\u0233","GN-LA","Perfecture","14584",CountryCode.GUINEA),
    GN_LO("Lola","GN-LO","Perfecture","14585",CountryCode.GUINEA),
    GN_LE("L\u0233louma","GN-LE","Perfecture","14593",CountryCode.GUINEA),
    GN_MC("Macenta","GN-MC","Perfecture","14586",CountryCode.GUINEA),
    GN_ML("Mali","GN-ML","Perfecture","14559",CountryCode.GUINEA),
    GN_MM("Mamou","GN-MM","Perfecture","14587",CountryCode.GUINEA),
    GN_MD("Mandiana","GN-MD","Perfecture","14558",CountryCode.GUINEA),
    GN_NZ("Nz\u0233r\u0233kor\u0233","GN-NZ","Perfecture","14588",CountryCode.GUINEA),
    GN_PI("Pita","GN-PI","Perfecture","14557",CountryCode.GUINEA),
    GN_SI("Siguiri","GN-SI","Perfecture","14589",CountryCode.GUINEA),
    GN_TO("Tougu\u0233","GN-TO","Perfecture","14556",CountryCode.GUINEA),
    GN_TE("T\u0233lim\u0233l\u0233","GN-TE","Perfecture","14590",CountryCode.GUINEA),
    GN_YO("Yomou","GN-YO","Perfecture","14591",CountryCode.GUINEA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
