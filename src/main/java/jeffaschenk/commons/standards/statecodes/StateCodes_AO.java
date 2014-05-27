package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Angola
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AO {
    /**
     * State Code Enumerator
     */
    AO_BGO("Bengo","AO-BGO","Province","12390", CountryCode.ANGOLA),
    AO_BGU("Benguela","AO-BGU","Province","12391",CountryCode.ANGOLA),
    AO_BIE("Bi\u0233","AO-BIE","Province","12395",CountryCode.ANGOLA),
    AO_CAB("Cabinda","AO-CAB","Province","12392",CountryCode.ANGOLA),
    AO_CCU("Cuando-Cubango","AO-CCU","Province","12394",CountryCode.ANGOLA),
    AO_CNO("Cuanza Norte","AO-CNO","Province","12388",CountryCode.ANGOLA),
    AO_CUS("Cuanza Sul","AO-CUS","Province","12397",CountryCode.ANGOLA),
    AO_CNN("Cunene","AO-CNN","Province","12389",CountryCode.ANGOLA),
    AO_HUA("Huambo","AO-HUA","Province","12393",CountryCode.ANGOLA),
    AO_HUI("Hu\u0237la","AO-HUI","Province","12396",CountryCode.ANGOLA),
    AO_LUA("Luanda","AO-LUA","Province","12387",CountryCode.ANGOLA),
    AO_LNO("Lunda Norte","AO-LNO","Province","12398",CountryCode.ANGOLA),
    AO_LSU("Lunda Sul","AO-LSU","Province","12386",CountryCode.ANGOLA),
    AO_MAL("Malange","AO-MAL","Province","12399",CountryCode.ANGOLA),
    AO_MOX("Moxico","AO-MOX","Province","12401",CountryCode.ANGOLA),
    AO_NAM("Namibe","AO-NAM","Province","12402",CountryCode.ANGOLA),
    AO_UIG("U\u0237ge","AO-UIG","Province","12400",CountryCode.ANGOLA),
    AO_ZAI("Zaire","AO-ZAI","Province","12385",CountryCode.ANGOLA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
