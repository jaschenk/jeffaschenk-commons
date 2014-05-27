package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Venezuela
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_VE {
    /**
     * State Code Enumerator
     */
    VE_Z("Amazonas","VE-Z","state","18735", CountryCode.VENEZUELA),
    VE_B("Anzo\u0225tegui","VE-B","state","18745",CountryCode.VENEZUELA),
    VE_C("Apure","VE-C","state","18746",CountryCode.VENEZUELA),
    VE_D("Aragua","VE-D","state","18736",CountryCode.VENEZUELA),
    VE_E("Barinas","VE-E","state","18747",CountryCode.VENEZUELA),
    VE_F("Bol\u0237var","VE-F","state","18737",CountryCode.VENEZUELA),
    VE_G("Carabobo","VE-G","state","18748",CountryCode.VENEZUELA),
    VE_H("Cojedes","VE-H","state","18749",CountryCode.VENEZUELA),
    VE_Y("Delta Amacuro","VE-Y","state","18738",CountryCode.VENEZUELA),
    VE_W("Dependencias Federales","VE-W","federal dependency","18750",CountryCode.VENEZUELA),
    VE_A("Distrito Federal","VE-A","federal district","18739",CountryCode.VENEZUELA),
    VE_I("Falc\u0243n","VE-I","state","18751",CountryCode.VENEZUELA),
    VE_J("Gu\u0225rico","VE-J","state","18740",CountryCode.VENEZUELA),
    VE_K("Lara","VE-K","state","18741",CountryCode.VENEZUELA),
    VE_M("Miranda","VE-M","state","18743",CountryCode.VENEZUELA),
    VE_N("Monagas","VE-N","state","18744",CountryCode.VENEZUELA),
    VE_L("M\u0233rida","VE-L","state","18742",CountryCode.VENEZUELA),
    VE_O("Nueva Esparta","VE-O","state","18734",CountryCode.VENEZUELA),
    VE_P("Portuguesa","VE-P","state","18752",CountryCode.VENEZUELA),
    VE_R("Sucre","VE-R","state","18733",CountryCode.VENEZUELA),
    VE_T("Trujillo","VE-T","state","20682",CountryCode.VENEZUELA),
    VE_S("T\u0225chira","VE-S","state","20681",CountryCode.VENEZUELA),
    VE_X("Vargas","VE-X","state","20683",CountryCode.VENEZUELA),
    VE_U("Yaracuy","VE-U","state","20680",CountryCode.VENEZUELA),
    VE_V("Zulia","VE-V","state","20684",CountryCode.VENEZUELA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_VE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
