package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Bolivia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BO {
    /**
     * State Code Enumerator
     */
    BO_H("Chuquisaca","BO-H","Department","12739", CountryCode.BOLIVIA),
    BO_C("Cochabamba","BO-C","Department","12741",CountryCode.BOLIVIA),
    BO_B("El Beni","BO-B","Department","12740",CountryCode.BOLIVIA),
    BO_L("La Paz","BO-L","Department","12742",CountryCode.BOLIVIA),
    BO_O("Oruro","BO-O","Department","12738",CountryCode.BOLIVIA),
    BO_N("Pando","BO-N","Department","12743",CountryCode.BOLIVIA),
    BO_P("Potos\u0237","BO-P","Department","12737",CountryCode.BOLIVIA),
    BO_S("Santa Cruz","BO-S","Department","12744",CountryCode.BOLIVIA),
    BO_T("Tarija","BO-T","Department","12745",CountryCode.BOLIVIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
