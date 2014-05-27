package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Palau
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PW {
    /**
     * State Code Enumerator
     */
    PW_002("Aimeliik","PW-002","State","15887", CountryCode.PALAU),
    PW_004("Airai","PW-004","State","15898",CountryCode.PALAU),
    PW_010("Angaur","PW-010","State","15889",CountryCode.PALAU),
    PW_050("Hatobohei","PW-050","State","15886",CountryCode.PALAU),
    PW_100("Kayangel","PW-100","State","15890",CountryCode.PALAU),
    PW_150("Koror","PW-150","State","15891",CountryCode.PALAU),
    PW_212("Melekeok","PW-212","State","15888",CountryCode.PALAU),
    PW_214("Ngaraard","PW-214","State","15892",CountryCode.PALAU),
    PW_218("Ngarchelong","PW-218","State","15899",CountryCode.PALAU),
    PW_222("Ngardmau","PW-222","State","15893",CountryCode.PALAU),
    PW_224("Ngatpang","PW-224","State","15894",CountryCode.PALAU),
    PW_226("Ngchesar","PW-226","State","15895",CountryCode.PALAU),
    PW_227("Ngeremlengui","PW-227","State","15885",CountryCode.PALAU),
    PW_228("Ngiwal","PW-228","State","15896",CountryCode.PALAU),
    PW_350("Peleliu","PW-350","State","15897",CountryCode.PALAU),
    PW_370("Sonsorol","PW-370","State","15884",CountryCode.PALAU);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PW(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
