package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Madagascar
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MG {
    /**
     * State Code Enumerator
     */
    MG_T("Antananarivo","MG-T","Province","15255", CountryCode.MADAGASCAR),
    MG_D("Antsiranana","MG-D","Province","15254",CountryCode.MADAGASCAR),
    MG_F("Fianarantsoa","MG-F","Province","15258",CountryCode.MADAGASCAR),
    MG_M("Mahajanga","MG-M","Province","15257",CountryCode.MADAGASCAR),
    MG_A("Toamasina","MG-A","Province","15253",CountryCode.MADAGASCAR),
    MG_U("Toliara","MG-U","Province","15256",CountryCode.MADAGASCAR);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
