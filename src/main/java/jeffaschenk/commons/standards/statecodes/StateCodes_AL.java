package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Albania
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AL {
    /**
     * State Code Enumerator
     */
    AL_BR("Berat", "AL-BR", "District", "12316", CountryCode.ALBANIA),
    AL_BU("Bulqiz\u0235", "AL-BU", "District", "12309", CountryCode.ALBANIA),
    AL_DL("Delvin\u0235", "AL-DL", "District", "12310", CountryCode.ALBANIA),
    AL_DV("Devoll", "AL-DV", "District", "12317", CountryCode.ALBANIA),
    AL_DI("Dib\u0235r", "AL-DI", "District", "12311", CountryCode.ALBANIA),
    AL_DR("Durr\u0235s", "AL-DR", "District", "12312", CountryCode.ALBANIA),
    AL_EL("Elbasan", "AL-EL", "District", "12315", CountryCode.ALBANIA),
    AL_FR("Fier", "AL-FR", "District", "12313", CountryCode.ALBANIA),
    AL_GJ("Gjirokast\u0235r", "AL-GJ", "District", "12314", CountryCode.ALBANIA),
    AL_GR("Gramsh", "AL-GR", "District", "12318", CountryCode.ALBANIA),
    AL_HA("Has", "AL-HA", "District", "12300", CountryCode.ALBANIA),
    AL_KA("Kavaj\u0235", "AL-KA", "District", "12301", CountryCode.ALBANIA),
    AL_ER("Kolonj\u0235", "AL-ER", "District", "12290", CountryCode.ALBANIA),
    AL_KO("Kor\u0231\u0235", "AL-KO", "District", "12302", CountryCode.ALBANIA),
    AL_KR("Kruj\u0235", "AL-KR", "District", "12303", CountryCode.ALBANIA),
    AL_KU("Kuk\u0235s", "AL-KU", "District", "12304", CountryCode.ALBANIA),
    AL_KB("Kurbin", "AL-KB", "District", "12305", CountryCode.ALBANIA),
    AL_KC("Ku\u0231ov\u0235", "AL-KC", "District", "12319", CountryCode.ALBANIA),
    AL_LE("Lezh\u0235", "AL-LE", "District", "12320", CountryCode.ALBANIA),
    AL_LB("Librazhd", "AL-LB", "District", "12306", CountryCode.ALBANIA),
    AL_LU("Lushnj\u0235", "AL-LU", "District", "12307", CountryCode.ALBANIA),
    AL_MK("Mallakast\u0235r", "AL-MK", "District", "12321", CountryCode.ALBANIA),
    AL_MM("Mal\u0235si e Madhe", "AL-MM", "District", "12308", CountryCode.ALBANIA),
    AL_MT("Mat", "AL-MT", "District", "12291", CountryCode.ALBANIA),
    AL_MR("Mirdit\u0235", "AL-MR", "District", "12289", CountryCode.ALBANIA),
    AL_PQ("Peqin", "AL-PQ", "District", "12292", CountryCode.ALBANIA),
    AL_PG("Pogradec", "AL-PG", "District", "12288", CountryCode.ALBANIA),
    AL_PU("Puk\u0235", "AL-PU", "District", "12294", CountryCode.ALBANIA),
    AL_PR("P\u0235rmet", "AL-PR", "District", "12293", CountryCode.ALBANIA),
    AL_SR("Sarand\u0235", "AL-SR", "District", "12295", CountryCode.ALBANIA),
    AL_SH("Shkod\u0235r", "AL-SH", "District", "12287", CountryCode.ALBANIA),
    AL_SK("Skrapar", "AL-SK", "District", "12296", CountryCode.ALBANIA),
    AL_TE("Tepelen\u0235", "AL-TE", "District", "12297", CountryCode.ALBANIA),
    AL_TR("Tiran\u0235", "AL-TR", "District", "12286", CountryCode.ALBANIA),
    AL_TP("Tropoj\u0235", "AL-TP", "District", "12298", CountryCode.ALBANIA),
    AL_VL("Vlor\u0235", "AL-VL", "District", "12299", CountryCode.ALBANIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AL(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
