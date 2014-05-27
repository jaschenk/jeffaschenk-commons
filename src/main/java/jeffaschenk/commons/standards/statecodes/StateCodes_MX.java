package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Mexico
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MX {
    /**
     * State Code Enumerator
     */
    MX_AGU("Aguascalientes", "MX-AGU", "state", "15423", CountryCode.MEXICO),
    MX_BCN("Baja California", "MX-BCN", "state", "15412", CountryCode.MEXICO),
    MX_BCS("Baja California Sur", "MX-BCS", "state", "15413", CountryCode.MEXICO),
    MX_CAM("Campeche", "MX-CAM", "state", "15424", CountryCode.MEXICO),
    MX_CHP("Chiapas", "MX-CHP", "state", "15414", CountryCode.MEXICO),
    MX_CHH("Chihuahua", "MX-CHH", "state", "15425", CountryCode.MEXICO),
    MX_COA("Coahuila", "MX-COA", "state", "15415", CountryCode.MEXICO),
    MX_COL("Colima", "MX-COL", "state", "15416", CountryCode.MEXICO),
    MX_DIF("Distrito Federal", "MX-DIF", "federal district", "15426", CountryCode.MEXICO),
    MX_DUR("Durango", "MX-DUR", "state", "15417", CountryCode.MEXICO),
    MX_GUA("Guanajuato", "MX-GUA", "state", "15418", CountryCode.MEXICO),
    MX_GRO("Guerrero", "MX-GRO", "state", "15427", CountryCode.MEXICO),
    MX_HID("Hidalgo", "MX-HID", "state", "15419", CountryCode.MEXICO),
    MX_JAL("Jalisco", "MX-JAL", "state", "15420", CountryCode.MEXICO),
    MX_MIC("Michoac\u0225n", "MX-MIC", "state", "15421", CountryCode.MEXICO),
    MX_MOR("Morelos", "MX-MOR", "state", "15422", CountryCode.MEXICO),
    MX_MEX("M\u0233xico", "MX-MEX", "state", "15428", CountryCode.MEXICO),
    MX_NAY("Nayarit", "MX-NAY", "state", "15429", CountryCode.MEXICO),
    MX_NLE("Nuevo Le\u0243n", "MX-NLE", "state", "15403", CountryCode.MEXICO),
    MX_OAX("Oaxaca", "MX-OAX", "state", "15430", CountryCode.MEXICO),
    MX_PUE("Puebla", "MX-PUE", "state", "15404", CountryCode.MEXICO),
    MX_QUE("Quer\u0233taro", "MX-QUE", "state", "15405", CountryCode.MEXICO),
    MX_ROO("Quintana Roo", "MX-ROO", "state", "15432", CountryCode.MEXICO),
    MX_SLP("San Luis Potos\u0237", "MX-SLP", "state", "15406", CountryCode.MEXICO),
    MX_SIN("Sinaloa", "MX-SIN", "state", "15407", CountryCode.MEXICO),
    MX_SON("Sonora", "MX-SON", "state", "15433", CountryCode.MEXICO),
    MX_TAB("Tabasco", "MX-TAB", "state", "15408", CountryCode.MEXICO),
    MX_TAM("Tamaulipas", "MX-TAM", "state", "15431", CountryCode.MEXICO),
    MX_TLA("Tlaxcala", "MX-TLA", "state", "15409", CountryCode.MEXICO),
    MX_VER("Veracruz", "MX-VER", "state", "15410", CountryCode.MEXICO),
    MX_YUC("Yucat\u0225n", "MX-YUC", "state", "15402", CountryCode.MEXICO),
    MX_ZAC("Zacatecas", "MX-ZAC", "state", "15411", CountryCode.MEXICO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MX(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
