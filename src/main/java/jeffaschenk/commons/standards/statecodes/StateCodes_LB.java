package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Lebanon
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LB {
    /**
     * State Code Enumerator
     */
    LB_BA("Beirut","LB-BA","Province","15134", CountryCode.LEBANON),
    LB_BI("El B\u0233qaa","LB-BI","Province","15135",CountryCode.LEBANON),
    LB_JL("Jabal Loubn\u0224ne","LB-JL","Province","15133",CountryCode.LEBANON),
    LB_AS("Loubn\u0224ne ech Chem\u0224li","LB-AS","Province","15131",CountryCode.LEBANON),
    LB_JA("Loubn\u0224ne ej Jno\u0251bi","LB-JA","Province","15136",CountryCode.LEBANON),
    LB_NA("Nabat\u0238y\u0233","LB-NA","Province","15132",CountryCode.LEBANON);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LB(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
