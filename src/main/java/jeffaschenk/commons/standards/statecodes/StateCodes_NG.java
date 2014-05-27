package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Nigeria
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NG {
    /**
     * State Code Enumerator
     */
    NG_AB("Abia", "NG-AB", "state", "21217", CountryCode.NIGERIA),
    NG_FC("Abuja Capital Territory", "NG-FC", "capital territory", "15801", CountryCode.NIGERIA),
    NG_AD("Adamawa", "NG-AD", "state", "15802", CountryCode.NIGERIA),
    NG_AK("Akwa Ibom", "NG-AK", "state", "15803", CountryCode.NIGERIA),
    NG_AN("Anambra", "NG-AN", "state", "15804", CountryCode.NIGERIA),
    NG_BA("Bauchi", "NG-BA", "state", "15805", CountryCode.NIGERIA),
    NG_BY("Bayelsa", "NG-BY", "state", "15826", CountryCode.NIGERIA),
    NG_BE("Benue", "NG-BE", "state", "15817", CountryCode.NIGERIA),
    NG_BO("Borno", "NG-BO", "state", "15806", CountryCode.NIGERIA),
    NG_CR("Cross River", "NG-CR", "state", "15818", CountryCode.NIGERIA),
    NG_DE("Delta", "NG-DE", "state", "15799", CountryCode.NIGERIA),
    NG_EB("Ebonyi", "NG-EB", "state", "15819", CountryCode.NIGERIA),
    NG_ED("Edo", "NG-ED", "state", "15820", CountryCode.NIGERIA),
    NG_EK("Ekiti", "NG-EK", "state", "15807", CountryCode.NIGERIA),
    NG_EN("Enugu", "NG-EN", "state", "15821", CountryCode.NIGERIA),
    NG_GO("Gombe", "NG-GO", "state", "15822", CountryCode.NIGERIA),
    NG_IM("Imo", "NG-IM", "state", "15798", CountryCode.NIGERIA),
    NG_JI("Jigawa", "NG-JI", "state", "15823", CountryCode.NIGERIA),
    NG_KD("Kaduna", "NG-KD", "state", "15824", CountryCode.NIGERIA),
    NG_KN("Kano", "NG-KN", "state", "15828", CountryCode.NIGERIA),
    NG_KT("Katsina", "NG-KT", "state", "15825", CountryCode.NIGERIA),
    NG_KE("Kebbi", "NG-KE", "state", "15808", CountryCode.NIGERIA),
    NG_KO("Kogi", "NG-KO", "state", "15797", CountryCode.NIGERIA),
    NG_KW("Kwara", "NG-KW", "state", "15809", CountryCode.NIGERIA),
    NG_LA("Lagos", "NG-LA", "state", "15810", CountryCode.NIGERIA),
    NG_NA("Nassarawa", "NG-NA", "state", "15794", CountryCode.NIGERIA),
    NG_NI("Niger", "NG-NI", "state", "15811", CountryCode.NIGERIA),
    NG_OG("Ogun", "NG-OG", "state", "15812", CountryCode.NIGERIA),
    NG_ON("Ondo", "NG-ON", "state", "15795", CountryCode.NIGERIA),
    NG_OS("Osun", "NG-OS", "state", "15813", CountryCode.NIGERIA),
    NG_OY("Oyo", "NG-OY", "state", "15814", CountryCode.NIGERIA),
    NG_PL("Plateau", "NG-PL", "state", "15796", CountryCode.NIGERIA),
    NG_RI("Rivers", "NG-RI", "state", "15815", CountryCode.NIGERIA),
    NG_SO("Sokoto", "NG-SO", "state", "15816", CountryCode.NIGERIA),
    NG_TA("Taraba", "NG-TA", "state", "15793", CountryCode.NIGERIA),
    NG_YO("Yobe", "NG-YO", "state", "15827", CountryCode.NIGERIA),
    NG_ZA("Zamfara", "NG-ZA", "state", "15792", CountryCode.NIGERIA);


    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
