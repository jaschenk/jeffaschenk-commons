package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Pakistan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PK {
    /**
     * State Code Enumerator
     */
    PK_JK("Azad Kashmir","PK-JK","Pakistan administered area","15882", CountryCode.PAKISTAN),
    PK_BA("Baluchistan (en)","PK-BA","province","15881",CountryCode.PAKISTAN),
    PK_TA("Federally Administered Tribal Areas","PK-TA","territory","15878",CountryCode.PAKISTAN),
    PK_IS("Islamabad","PK-IS","federal capital territory","15883",CountryCode.PAKISTAN),
    PK_NW("North-West Frontier","PK-NW","province","15879",CountryCode.PAKISTAN),
    PK_NA("Northern Areas","PK-NA","Pakistan administered area","15877",CountryCode.PAKISTAN),
    PK_PB("Punjab","PK-PB","province","15876",CountryCode.PAKISTAN),
    PK_SD("Sind (en)","PK-SD","province","15880",CountryCode.PAKISTAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PK(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
