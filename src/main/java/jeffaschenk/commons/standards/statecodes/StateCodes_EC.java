package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Equador
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_EC {
    /**
     * State Code Enumerator
     */
    EC_A("Azuay","EC-A","Province","13570", CountryCode.ECUADOR),
    EC_B("Bol\u0237var","EC-B","Province","13571",CountryCode.ECUADOR),
    EC_C("Carchi","EC-C","Province","13572",CountryCode.ECUADOR),
    EC_F("Ca\u0241ar","EC-F","Province","13578",CountryCode.ECUADOR),
    EC_H("Chimborazo","EC-H","Province","13579",CountryCode.ECUADOR),
    EC_X("Cotopaxi","EC-X","Province","13573",CountryCode.ECUADOR),
    EC_O("El Oro","EC-O","Province","13574",CountryCode.ECUADOR),
    EC_E("Esmeraldas","EC-E","Province","13580",CountryCode.ECUADOR),
    EC_W("Gal\u0225pagos","EC-W","Province","13575",CountryCode.ECUADOR),
    EC_G("Guayas","EC-G","Province","13576",CountryCode.ECUADOR),
    EC_I("Imbabura","EC-I","Province","13581",CountryCode.ECUADOR),
    EC_L("Loja","EC-L","Province","13577",CountryCode.ECUADOR),
    EC_R("Los R\u0237os","EC-R","Province","13582",CountryCode.ECUADOR),
    EC_M("Manab\u0237","EC-M","Province","13583",CountryCode.ECUADOR),
    EC_S("Morona-Santiago","EC-S","Province","13584",CountryCode.ECUADOR),
    EC_N("Napo","EC-N","Province","13569",CountryCode.ECUADOR),
    EC_D("Orellana","EC-D","Province","13585",CountryCode.ECUADOR),
    EC_Y("Pastaza","EC-Y","Province","13568",CountryCode.ECUADOR),
    EC_P("Pichincha","EC-P","Province","13586",CountryCode.ECUADOR),
    EC_X1("Santa Elena","EC-X1","Province","21342",CountryCode.ECUADOR),
    EC_X2("Santo Domingo de los Tsachilas","EC-X2","Province","21343",CountryCode.ECUADOR),
    EC_U("Sucumb\u0237os","EC-U","Province","13567",CountryCode.ECUADOR),
    EC_T("Tungurahua","EC-T","Province","13587",CountryCode.ECUADOR),
    EC_Z("Zamora-Chinchipe","EC-Z","Province","13566",CountryCode.ECUADOR);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_EC(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
