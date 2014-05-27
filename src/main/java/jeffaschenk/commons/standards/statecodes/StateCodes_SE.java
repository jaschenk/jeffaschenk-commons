package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Sweden
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SE {
    /**
     * State Code Enumerator
     */
    SE_K("Blekinge l\u0228n [SE-10]","SE-K","County","18240", CountryCode.SWEDEN),
    SE_W("Dalarnas l\u0228n [SE-20]","SE-W","County","18248",CountryCode.SWEDEN),
    SE_I("Gotlands l\u0228n [SE-09]","SE-I","County","18249",CountryCode.SWEDEN),
    SE_X("G\u0228vleborgs l\u0228n [SE-21]","SE-X","County","18241",CountryCode.SWEDEN),
    SE_N("Hallands l\u0228n [SE-13]","SE-N","County","18242",CountryCode.SWEDEN),
    SE_Z("J\u0228mtlands l\u0228n [SE-23]","SE-Z","County","18243",CountryCode.SWEDEN),
    SE_F("J\u0246nk\u0246pings l\u0228n [SE-06]","SE-F","County","18250",CountryCode.SWEDEN),
    SE_H("Kalmar l\u0228n [SE-08]","SE-H","County","18244",CountryCode.SWEDEN),
    SE_G("Kronobergs l\u0228n [SE-07]","SE-G","County","18245",CountryCode.SWEDEN),
    SE_BD("Norrbottens l\u0228n [SE-25]","SE-BD","County","18251",CountryCode.SWEDEN),
    SE_M("Sk\u0197ne l\u0228n [SE-12]","SE-M","County","18247",CountryCode.SWEDEN),
    SE_AB("Stockholms l\u0228n [SE-01]","SE-AB","County","18252",CountryCode.SWEDEN),
    SE_D("S\u0246dermanlands l\u0228n [SE-04]","SE-D","County","18231",CountryCode.SWEDEN),
    SE_C("Uppsala l\u0228n [SE-03]","SE-C","County","18232",CountryCode.SWEDEN),
    SE_S("V\u0228rmlands l\u0228n [SE-17]","SE-S","County","18233",CountryCode.SWEDEN),
    SE_AC("V\u0228sterbottens l\u0228n [SE-24]","SE-AC","County","18254",CountryCode.SWEDEN),
    SE_Y("V\u0228sternorrlands l\u0228n [SE-22]","SE-Y","County","18234",CountryCode.SWEDEN),
    SE_U("V\u0228stmanlands l\u0228n [SE-19]","SE-U","County","18253",CountryCode.SWEDEN),
    SE_O("V\u0228stra G\u0246talands l\u0228n [SE-14]","SE-O","County","18235",CountryCode.SWEDEN),
    SE_T("\u0246rebro l\u0228n [SE-18]","SE-T","County","18246",CountryCode.SWEDEN),
    SE_E("\u0246sterg\u0246tlands l\u0228n [SE-05]","SE-E","County","18230",CountryCode.SWEDEN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
