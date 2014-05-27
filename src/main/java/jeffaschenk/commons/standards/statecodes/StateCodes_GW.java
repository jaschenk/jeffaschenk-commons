package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Guinea Bissau
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GW {
    /**
     * State Code Enumerator
     */
    GW_BA("Bafat\u0225","GW-BA","region","14595", CountryCode.GUINEA_BISSAU),
    GW_BM("Biombo","GW-BM","region","14598",CountryCode.GUINEA_BISSAU),
    GW_BS("Bissau","GW-BS","autonomous sector","14597",CountryCode.GUINEA_BISSAU),
    GW_BL("Bolama","GW-BL","region","14601",CountryCode.GUINEA_BISSAU),
    GW_CA("Cacheu","GW-CA","region","14599",CountryCode.GUINEA_BISSAU),
    GW_GA("Gab\u0250","GW-GA","region","14602",CountryCode.GUINEA_BISSAU),
    GW_OI("Oio","GW-OI","region","14596",CountryCode.GUINEA_BISSAU),
    GW_QU("Quinara","GW-QU","region","14594",CountryCode.GUINEA_BISSAU),
    GW_TO("Tombali","GW-TO","region","14600",CountryCode.GUINEA_BISSAU);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GW(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
