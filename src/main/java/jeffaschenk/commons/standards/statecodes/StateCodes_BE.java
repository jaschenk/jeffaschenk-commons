package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Belgium
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BE {
    /**
     * State Code Enumerator
     */
    BE_VAN("Antwerpen","BE-VAN","Province","12683", CountryCode.BELGIUM),
    BE_WBR("Brabant Wallon","BE-WBR","Province","12679",CountryCode.BELGIUM),
    BE_BRU("Brussels","BE-BRU","Capital Region","12682",CountryCode.BELGIUM),
    BE_WHT("Hainaut","BE-WHT","Province","12678",CountryCode.BELGIUM),
    BE_VLI("Limburg","BE-VLI","Province","12680",CountryCode.BELGIUM),
    BE_WLG("Li\u0232ge","BE-WLG","Province","12684",CountryCode.BELGIUM),
    BE_WLX("Luxembourg","BE-WLX","Province","12676",CountryCode.BELGIUM),
    BE_WNA("Namur","BE-WNA","Province","12685",CountryCode.BELGIUM),
    BE_VOV("Oost-Vlaanderen","BE-VOV","Province","12677",CountryCode.BELGIUM),
    BE_VBR("Vlaams Brabant","BE-VBR","Province","12675",CountryCode.BELGIUM),
    BE_VWV("West-Vlaanderen","BE-VWV","Province","12681",CountryCode.BELGIUM);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
