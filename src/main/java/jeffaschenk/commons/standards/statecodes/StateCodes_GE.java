package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Georgia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GE {
    /**
     * State Code Enumerator
     */
    GE_AB("Abkhazia","GE-AB","autonomous republic","13902", CountryCode.GEORGIA),
    GE_AJ("Ajaria","GE-AJ","autonomous republic","13896",CountryCode.GEORGIA),
    GE_GU("Guria","GE-GU","region","13897",CountryCode.GEORGIA),
    GE_IM("Imereti","GE-IM","region","13901",CountryCode.GEORGIA),
    GE_KA("Kakheti","GE-KA","region","13898",CountryCode.GEORGIA),
    GE_KK("Kvemo Kartli","GE-KK","region","13900",CountryCode.GEORGIA),
    GE_MM("Mtskheta-Mtianeti","GE-MM","region","13905",CountryCode.GEORGIA),
    GE_RL("Racha-Lechkhumi [and] Kvemo Svaneti","GE-RL","region","13899",CountryCode.GEORGIA),
    GE_SZ("Samegrelo-Zemo Svaneti","GE-SZ","region","13906",CountryCode.GEORGIA),
    GE_SJ("Samtskhe-Javakheti","GE-SJ","region","13903",CountryCode.GEORGIA),
    GE_SK("Shida Kartli","GE-SK","region","13904",CountryCode.GEORGIA),
    GE_TB("Tbilisi","GE-TB","city","13895",CountryCode.GEORGIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
