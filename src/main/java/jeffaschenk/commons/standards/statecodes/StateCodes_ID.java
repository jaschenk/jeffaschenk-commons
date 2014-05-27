package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Indonesia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ID {
    /**
     * State Code Enumerator
     */
    ID_AC("Aceh", "ID-AC", "autonomous province", "14720", CountryCode.INDONESIA),
    ID_BA("Bali", "ID-BA", "province", "14719", CountryCode.INDONESIA),
    ID_BB("Bangka Belitung", "ID-BB", "province", "14721", CountryCode.INDONESIA),
    ID_BT("Banten", "ID-BT", "province", "14723", CountryCode.INDONESIA),
    ID_BE("Bengkulu", "ID-BE", "province", "14722", CountryCode.INDONESIA),
    ID_GO("Gorontalo", "ID-GO", "province", "14711", CountryCode.INDONESIA),
    ID_JK("Jakarta Raya", "ID-JK", "special district", "14724", CountryCode.INDONESIA),
    ID_JA("Jambi", "ID-JA", "province", "14712", CountryCode.INDONESIA),
    ID_JB("Jawa Barat", "ID-JB", "province", "14725", CountryCode.INDONESIA),
    ID_JT("Jawa Tengah", "ID-JT", "province", "14713", CountryCode.INDONESIA),
    ID_JI("Jawa Timur", "ID-JI", "province", "14726", CountryCode.INDONESIA),
    ID_KB("Kalimantan Barat", "ID-KB", "province", "14714", CountryCode.INDONESIA),
    ID_KS("Kalimantan Selatan", "ID-KS", "province", "14715", CountryCode.INDONESIA),
    ID_KT("Kalimantan Tengah", "ID-KT", "province", "14727", CountryCode.INDONESIA),
    ID_KI("Kalimantan Timur", "ID-KI", "province", "14728", CountryCode.INDONESIA),
    ID_KR("Kepulauan Riau", "ID-KR", "province", "14733", CountryCode.INDONESIA),
    ID_LA("Lampung", "ID-LA", "province", "14729", CountryCode.INDONESIA),
    ID_MA("Maluku", "ID-MA", "province", "14730", CountryCode.INDONESIA),
    ID_MU("Maluku Utara", "ID-MU", "province", "14710", CountryCode.INDONESIA),
    ID_NB("Nusa Tenggara Barat", "ID-NB", "province", "14731", CountryCode.INDONESIA),
    ID_NT("Nusa Tenggara Timur", "ID-NT", "province", "14709", CountryCode.INDONESIA),
    ID_PA("Papua", "ID-PA", "province", "14732", CountryCode.INDONESIA),
    ID_PB("Papua Barat", "ID-PB", "province", "19010", CountryCode.INDONESIA),
    ID_RI("Riau", "ID-RI", "province", "14708", CountryCode.INDONESIA),
    ID_SR("Sulawesi Barat", "ID-SR", "province", "19011", CountryCode.INDONESIA),
    ID_SN("Sulawesi Selatan", "ID-SN", "province", "14707", CountryCode.INDONESIA),
    ID_ST("Sulawesi Tengah", "ID-ST", "province", "14734", CountryCode.INDONESIA),
    ID_SG("Sulawesi Tenggara", "ID-SG", "province", "14736", CountryCode.INDONESIA),
    ID_SA("Sulawesi Utara", "ID-SA", "province", "14716", CountryCode.INDONESIA),
    ID_SB("Sumatera Barat", "ID-SB", "province", "14735", CountryCode.INDONESIA),
    ID_SS("Sumatera Selatan", "ID-SS", "province", "14717", CountryCode.INDONESIA),
    ID_SU("Sumatera Utara", "ID-SU", "province", "14706", CountryCode.INDONESIA),
    ID_YO("Yogyakarta", "ID-YO", "special region", "14718", CountryCode.INDONESIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ID(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
