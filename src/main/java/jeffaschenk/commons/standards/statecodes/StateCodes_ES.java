package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 *
 * Country: Spain
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ES {
    /**
     * State Code Enumerator
     */
    ES_C("A Coru\u0241a","ES-C","province","18135", CountryCode.SPAIN),
    ES_AB("Albacete","ES-AB","province","18137",CountryCode.SPAIN),
    ES_A("Alicante","ES-A","province","18136",CountryCode.SPAIN),
    ES_AL("Almer\u0237a","ES-AL","province","18138",CountryCode.SPAIN),
    ES_O("Asturias","ES-O","province","18122",CountryCode.SPAIN),
    ES_BA("Badajoz","ES-BA","province","18140",CountryCode.SPAIN),
    ES_PM("Baleares","ES-PM","province","18123",CountryCode.SPAIN),
    ES_B("Barcelona","ES-B","province","18141",CountryCode.SPAIN),
    ES_BU("Burgos","ES-BU","province","18124",CountryCode.SPAIN),
    ES_S("Cantabria","ES-S","province","18125",CountryCode.SPAIN),
    ES_CS("Castell\u0243n","ES-CS","province","18144",CountryCode.SPAIN),
    ES_CE("Ceuta","ES-CE","municipality","42631",CountryCode.SPAIN),
    ES_CR("Ciudad Real","ES-CR","province","18126",CountryCode.SPAIN),
    ES_CU("Cuenca","ES-CU","province","18154",CountryCode.SPAIN),
    ES_CC("C\u0225ceres","ES-CC","province","18142",CountryCode.SPAIN),
    ES_CA("C\u0225diz","ES-CA","province","18143",CountryCode.SPAIN),
    ES_CO("C\u0243rdoba","ES-CO","province","18127",CountryCode.SPAIN),
    ES_GI("Girona","ES-GI","province","18128",CountryCode.SPAIN),
    ES_GR("Granada","ES-GR","province","18129",CountryCode.SPAIN),
    ES_GU("Guadalajara","ES-GU","province","18155",CountryCode.SPAIN),
    ES_SS("Guip\u0250zcoa","ES-SS","province","18130",CountryCode.SPAIN),
    ES_H("Huelva","ES-H","province","18156",CountryCode.SPAIN),
    ES_HU("Huesca","ES-HU","province","18131",CountryCode.SPAIN),
    ES_J("Ja\u0233n","ES-J","province","18132",CountryCode.SPAIN),
    ES_LO("La Rioja","ES-LO","province","18157",CountryCode.SPAIN),
    ES_GC("Las Palmas","ES-GC","province","18133",CountryCode.SPAIN),
    ES_LE("Le\u0243n","ES-LE","province","18158",CountryCode.SPAIN),
    ES_L("Lleida","ES-L","province","18134",CountryCode.SPAIN),
    ES_LU("Lugo","ES-LU","province","18145",CountryCode.SPAIN),
    ES_M("Madrid","ES-M","province","18159",CountryCode.SPAIN),
    ES_ML("Melilla","ES-ML","municipality","42627",CountryCode.SPAIN),
    ES_MU("Murcia","ES-MU","province","18160",CountryCode.SPAIN),
    ES_MA("M\u0225laga","ES-MA","province","18146",CountryCode.SPAIN),
    ES_NA("Navarra","ES-NA","province","18148",CountryCode.SPAIN),
    ES_OR("Ourense","ES-OR","province","18116",CountryCode.SPAIN),
    ES_P("Palencia","ES-P","province","18149",CountryCode.SPAIN),
    ES_PO("Pontevedra","ES-PO","province","18150",CountryCode.SPAIN),
    ES_SA("Salamanca","ES-SA","province","18161",CountryCode.SPAIN),
    ES_TF("Santa Cruz de Tenerife","ES-TF","province","18151",CountryCode.SPAIN),
    ES_SG("Segovia","ES-SG","province","18115",CountryCode.SPAIN),
    ES_SE("Sevilla","ES-SE","province","18152",CountryCode.SPAIN),
    ES_SO("Soria","ES-SO","province","18117",CountryCode.SPAIN),
    ES_T("Tarragona","ES-T","province","18112",CountryCode.SPAIN),
    ES_TE("Teruel","ES-TE","province","18118",CountryCode.SPAIN),
    ES_TO("Toledo","ES-TO","province","18119",CountryCode.SPAIN),
    ES_V("Valencia","ES-V","province","18111",CountryCode.SPAIN),
    ES_VA("Valladolid","ES-VA","province","18120",CountryCode.SPAIN),
    ES_BI("Vizcaya","ES-BI","province","18113",CountryCode.SPAIN),
    ES_ZA("Zamora","ES-ZA","province","18114",CountryCode.SPAIN),
    ES_Z("Zaragoza","ES-Z","province","18110",CountryCode.SPAIN),
    ES_VI("\u0225lava","ES-VI","province","18121",CountryCode.SPAIN),
    ES_AV("\u0225vila","ES-AV","province","18139",CountryCode.SPAIN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ES(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
