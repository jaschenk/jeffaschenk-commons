package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Montenegro
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ME {
    /**
     * State Code Enumerator
     */
    ME_01("Andrijevica", "ME-01", "Commune", "42617", CountryCode.MONTENEGRO),
    ME_02("Bar", "ME-02", "Commune", "42616", CountryCode.MONTENEGRO),
    ME_03("Berane", "ME-03", "Commune", "42615", CountryCode.MONTENEGRO),
    ME_04("Bijelo Polje", "ME-04", "Commune", "42614", CountryCode.MONTENEGRO),
    ME_05("Budva", "ME-05", "Commune", "42613", CountryCode.MONTENEGRO),
    ME_06("Cetinje", "ME-06", "Commune", "42598", CountryCode.MONTENEGRO),
    ME_07("Danilovgrad", "ME-07", "Commune", "42612", CountryCode.MONTENEGRO),
    ME_08("Herceg-Novi", "ME-08", "Commune", "42599", CountryCode.MONTENEGRO),
    ME_09("Kola\u0353in", "ME-09", "Commune", "42610", CountryCode.MONTENEGRO),
    ME_10("Kotor", "ME-10", "Commune", "42609", CountryCode.MONTENEGRO),
    ME_11("Mojkovac", "ME-11", "Commune", "42608", CountryCode.MONTENEGRO),
    ME_12("Nik\u0353ic\u0180", "ME-12", "Commune", "42607", CountryCode.MONTENEGRO),
    ME_13("Plav", "ME-13", "Commune", "42606", CountryCode.MONTENEGRO),
    ME_14("Pljevlja", "ME-14", "Commune", "42604", CountryCode.MONTENEGRO),
    ME_15("Plu\u0382ine", "ME-15", "Commune", "42605", CountryCode.MONTENEGRO),
    ME_16("Podgorica", "ME-16", "Commune", "42603", CountryCode.MONTENEGRO),
    ME_17("Ro\u0382aje", "ME-17", "Commune", "42602", CountryCode.MONTENEGRO),
    ME_19("Tivat", "ME-19", "Commune", "42601", CountryCode.MONTENEGRO),
    ME_20("Ulcinj", "ME-20", "Commune", "42600", CountryCode.MONTENEGRO),
    ME_18("\u0353avnik", "ME-18", "Commune", "42597", CountryCode.MONTENEGRO),
    ME_21("\u0382abljak", "ME-21", "Commune", "42611", CountryCode.MONTENEGRO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ME(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
