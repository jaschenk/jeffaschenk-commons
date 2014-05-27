package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Switzerland
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CH {
    /**
     * State Code Enumerator
     */
    CH_AG("Aargau (de)","CH-AG","Canton","18257", CountryCode.SWITZERLAND),
    CH_AR("Appenzell Ausserrhoden (de)","CH-AR","Canton","18274",CountryCode.SWITZERLAND),
    CH_AI("Appenzell Innerrhoden (de)","CH-AI","Canton","18258",CountryCode.SWITZERLAND),
    CH_BL("Basel-Landschaft (de)","CH-BL","Canton","18275",CountryCode.SWITZERLAND),
    CH_BS("Basel-Stadt (de)","CH-BS","Canton","18259",CountryCode.SWITZERLAND),
    CH_BE("Bern (de)","CH-BE","Canton","18276",CountryCode.SWITZERLAND),
    CH_FR("Fribourg (fr)","CH-FR","Canton","18260",CountryCode.SWITZERLAND),
    CH_GE("Gen\u0232ve (fr)","CH-GE","Canton","18277",CountryCode.SWITZERLAND),
    CH_GL("Glarus (de)","CH-GL","Canton","18261",CountryCode.SWITZERLAND),
    CH_GR("Graub\u0252nden (de)","CH-GR","Canton","18265",CountryCode.SWITZERLAND),
    CH_JU("Jura (fr)","CH-JU","Canton","18262",CountryCode.SWITZERLAND),
    CH_LU("Luzern (de)","CH-LU","Canton","18266",CountryCode.SWITZERLAND),
    CH_NE("Neuch\u0224tel (fr)","CH-NE","Canton","18263",CountryCode.SWITZERLAND),
    CH_NW("Nidwalden (de)","CH-NW","Canton","18267",CountryCode.SWITZERLAND),
    CH_OW("Obwalden (de)","CH-OW","Canton","18264",CountryCode.SWITZERLAND),
    CH_SG("Sankt Gallen (de)","CH-SG","Canton","18268",CountryCode.SWITZERLAND),
    CH_SH("Schaffhausen (de)","CH-SH","Canton","18279",CountryCode.SWITZERLAND),
    CH_SZ("Schwyz (de)","CH-SZ","Canton","18269",CountryCode.SWITZERLAND),
    CH_SO("Solothurn (de)","CH-SO","Canton","18270",CountryCode.SWITZERLAND),
    CH_TG("Thurgau (de)","CH-TG","Canton","18280",CountryCode.SWITZERLAND),
    CH_TI("Ticino (it)","CH-TI","Canton","18271",CountryCode.SWITZERLAND),
    CH_UR("Uri (de)","CH-UR","Canton","18278",CountryCode.SWITZERLAND),
    CH_VS("Valais (fr)","CH-VS","Canton","18272",CountryCode.SWITZERLAND),
    CH_VD("Vaud (fr)","CH-VD","Canton","18255",CountryCode.SWITZERLAND),
    CH_ZG("Zug (de)","CH-ZG","Canton","18273",CountryCode.SWITZERLAND),
    CH_ZH("Z\u0252rich (de)","CH-ZH","Canton","18256",CountryCode.SWITZERLAND);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CH(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
