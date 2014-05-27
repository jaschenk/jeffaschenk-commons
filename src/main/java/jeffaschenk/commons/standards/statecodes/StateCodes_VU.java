package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Vanuatu
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_VU {
    /**
     * State Code Enumerator
     */
    	VU_MAP("Malampa","VU-MAP","Province","18726", CountryCode.VANUATU),
    VU_PAM("P\u0233nama","VU-PAM","Province","18728",CountryCode.VANUATU),
    VU_SAM("Sanma","VU-SAM","Province","18730",CountryCode.VANUATU),
    VU_SEE("Sh\u0233fa","VU-SEE","Province","18727",CountryCode.VANUATU),
    VU_TAE("Taf\u0233a","VU-TAE","Province","18725",CountryCode.VANUATU),
    VU_TOB("Torba","VU-TOB","Province","18729",CountryCode.VANUATU);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_VU(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
