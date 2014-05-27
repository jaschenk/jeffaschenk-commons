package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Taiwan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TW {
    /**
     * State Code Enumerator
     */
    TW_CHA("Changhua","TW-CHA","district","18312", CountryCode.TAIWAN),
    TW_CYQ("Chiayi","TW-CYQ","district","18313",CountryCode.TAIWAN),
    TW_CYI("Chiayi Municipality","TW-CYI","municipality","18308",CountryCode.TAIWAN),
    TW_HSQ("Hsinchu","TW-HSQ","district","18310",CountryCode.TAIWAN),
    TW_HSZ("Hsinchu Municipality","TW-HSZ","municipality","18309",CountryCode.TAIWAN),
    TW_HUA("Hualien","TW-HUA","district","18314",CountryCode.TAIWAN),
    TW_ILA("Ilan","TW-ILA","district","18311",CountryCode.TAIWAN),
    TW_KHQ("Kaohsiung","TW-KHQ","district","18315",CountryCode.TAIWAN),
    TW_KHH("Kaohsiung Special Municipality","TW-KHH","special municipality","18320",CountryCode.TAIWAN),
    TW_KEE("Keelung Municipality","TW-KEE","municipality","18316",CountryCode.TAIWAN),
    TW_MIA("Miaoli","TW-MIA","district","18317",CountryCode.TAIWAN),
    TW_NAN("Nantou","TW-NAN","district","18323",CountryCode.TAIWAN),
    TW_PEN("Penghu","TW-PEN","district","18324",CountryCode.TAIWAN),
    TW_PIF("Pingtung","TW-PIF","district","18318",CountryCode.TAIWAN),
    TW_TXQ("Taichung","TW-TXQ","district","18307",CountryCode.TAIWAN),
    TW_TXG("Taichung Municipality","TW-TXG","municipality","18325",CountryCode.TAIWAN),
    TW_TNQ("Tainan","TW-TNQ","district","18306",CountryCode.TAIWAN),
    TW_TNN("Tainan Municipality","TW-TNN","municipality","18319",CountryCode.TAIWAN),
    TW_TPQ("Taipei","TW-TPQ","district","18305",CountryCode.TAIWAN),
    TW_TPE("Taipei Special Municipality","TW-TPE","special municipality","18326",CountryCode.TAIWAN),
    TW_TTT("Taitung","TW-TTT","district","18304",CountryCode.TAIWAN),
    TW_TAO("Taoyuan","TW-TAO","district","18303",CountryCode.TAIWAN),
    TW_YUN("Yunlin","TW-YUN","district","18302",CountryCode.TAIWAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TW(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
