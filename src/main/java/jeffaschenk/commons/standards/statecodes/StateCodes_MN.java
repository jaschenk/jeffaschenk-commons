package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Mongolia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MN {
    /**
     * State Code Enumerator
     */
    MN_073("Arhangay","MN-073","province","15504", CountryCode.MONGOLIA),
    MN_071("Bayan-\u0246lgiy","MN-071","province","15490",CountryCode.MONGOLIA),
    MN_069("Bayanhongor","MN-069","province","15491",CountryCode.MONGOLIA),
    MN_067("Bulgan","MN-067","province","15492",CountryCode.MONGOLIA),
    MN_037("Darhan uul","MN-037","municipality","15493",CountryCode.MONGOLIA),
    MN_061("Dornod","MN-061","province","15505",CountryCode.MONGOLIA),
    MN_063("Dornogovi","MN-063","province","15494",CountryCode.MONGOLIA),
    MN_059("Dundgovi","MN-059","province","15489",CountryCode.MONGOLIA),
    MN_057("Dzavhan","MN-057","province","15503",CountryCode.MONGOLIA),
    MN_065("Govi-Altay","MN-065","province","15495",CountryCode.MONGOLIA),
    MN_064("Govi-S\u0252mber","MN-064","municipality","15506",CountryCode.MONGOLIA),
    MN_039("Hentiy","MN-039","province","15496",CountryCode.MONGOLIA),
    MN_043("Hovd","MN-043","province","15497",CountryCode.MONGOLIA),
    MN_041("H\u0246vsg\u0246l","MN-041","province","15488",CountryCode.MONGOLIA),
    MN_035("Orhon","MN-035","municipality","15507",CountryCode.MONGOLIA),
    MN_049("Selenge","MN-049","province","15487",CountryCode.MONGOLIA),
    MN_051("S\u0252hbaatar","MN-051","province","15500",CountryCode.MONGOLIA),
    MN_047("T\u0246v","MN-047","province","15501",CountryCode.MONGOLIA),
    MN_1("Ulaanbaatar","MN-1","municipality","15486",CountryCode.MONGOLIA),
    MN_046("Uvs","MN-046","province","15502",CountryCode.MONGOLIA),
    MN_053("\u0246mn\u0246govi","MN-053","province","15498",CountryCode.MONGOLIA),
    MN_055("\u0246v\u0246rhangay","MN-055","province","15499",CountryCode.MONGOLIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
