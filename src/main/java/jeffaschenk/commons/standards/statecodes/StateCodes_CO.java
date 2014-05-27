package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Columbia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CO {
    /**
     * State Code Enumerator
     */
    CO_AMA("Amazonas","CO-AMA","department","13045", CountryCode.COLOMBIA),
    CO_ANT("Antioquia","CO-ANT","department","13055",CountryCode.COLOMBIA),
    CO_ARA("Arauca","CO-ARA","department","13046",CountryCode.COLOMBIA),
    CO_ATL("Atl\u0225ntico","CO-ATL","department","13047",CountryCode.COLOMBIA),
    CO_BOL("Bol\u0237var","CO-BOL","department","13048",CountryCode.COLOMBIA),
    CO_BOY("Boyac\u0225","CO-BOY","department","13057",CountryCode.COLOMBIA),
    CO_CAL("Caldas","CO-CAL","department","13049",CountryCode.COLOMBIA),
    CO_CAQ("Caquet\u0225","CO-CAQ","department","13050",CountryCode.COLOMBIA),
    CO_CAS("Casanare","CO-CAS","department","13058",CountryCode.COLOMBIA),
    CO_CAU("Cauca","CO-CAU","department","13051",CountryCode.COLOMBIA),
    CO_CES("Cesar","CO-CES","department","13059",CountryCode.COLOMBIA),
    CO_CHO("Choc\u0243","CO-CHO","department","13052",CountryCode.COLOMBIA),
    CO_CUN("Cundinamarca","CO-CUN","department","13060",CountryCode.COLOMBIA),
    CO_COR("C\u0243rdoba","CO-COR","department","13053",CountryCode.COLOMBIA),
    CO_DC("Distrito Capital de Bogot\u0225","CO-DC","capital district","13056",CountryCode.COLOMBIA),
    CO_GUA("Guain\u0237a","CO-GUA","department","13054",CountryCode.COLOMBIA),
    CO_GUV("Guaviare","CO-GUV","department","13061",CountryCode.COLOMBIA),
    CO_HUI("Huila","CO-HUI","department","13063",CountryCode.COLOMBIA),
    CO_LAG("La Guajira","CO-LAG","department","13064",CountryCode.COLOMBIA),
    CO_MAG("Magdalena","CO-MAG","department","13062",CountryCode.COLOMBIA),
    CO_MET("Meta","CO-MET","department","13065",CountryCode.COLOMBIA),
    CO_NAR("Nari\u0241o","CO-NAR","department","13044",CountryCode.COLOMBIA),
    CO_NSA("Norte de Santander","CO-NSA","department","13066",CountryCode.COLOMBIA),
    CO_PUT("Putumayo","CO-PUT","department","13067",CountryCode.COLOMBIA),
    CO_QUI("Quind\u0237o","CO-QUI","department","13042",CountryCode.COLOMBIA),
    CO_RIS("Risaralda","CO-RIS","department","13068",CountryCode.COLOMBIA),
    CO_SAP("San Andr\u0233s, Providencia y Santa Catalina","CO-SAP","department","13043",CountryCode.COLOMBIA),
    CO_SAN("Santander","CO-SAN","department","13069",CountryCode.COLOMBIA),
    CO_SUC("Sucre","CO-SUC","department","13041",CountryCode.COLOMBIA),
    CO_TOL("Tolima","CO-TOL","department","13070",CountryCode.COLOMBIA),
    CO_VAC("Valle del Cauca","CO-VAC","department","13071",CountryCode.COLOMBIA),
    CO_VAU("Vaup\u0233s","CO-VAU","department","13040",CountryCode.COLOMBIA),
    CO_VID("Vichada","CO-VID","department","13072",CountryCode.COLOMBIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
