package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Peru
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PE {
    /**
     * State Code Enumerator
     */
    PE_AMA("Amazonas","PE-AMA","Region","15982", CountryCode.PERU),
    PE_ANC("Ancash","PE-ANC","Region","15981",CountryCode.PERU),
    PE_APU("Apur\u0237mac","PE-APU","Region","15983",CountryCode.PERU),
    PE_ARE("Arequipa","PE-ARE","Region","15972",CountryCode.PERU),
    PE_AYA("Ayacucho","PE-AYA","Region","15984",CountryCode.PERU),
    PE_CAJ("Cajamarca","PE-CAJ","Region","15985",CountryCode.PERU),
    PE_CUS("Cuzco [Cusco]","PE-CUS","Region","15989",CountryCode.PERU),
    PE_CAL("El Callao","PE-CAL","Region","15992",CountryCode.PERU),
    PE_HUV("Huancavelica","PE-HUV","Region","15986",CountryCode.PERU),
    PE_HUC("Hu\u0225nuco","PE-HUC","Region","15987",CountryCode.PERU),
    PE_ICA("Ica","PE-ICA","Region","15971",CountryCode.PERU),
    PE_JUN("Jun\u0237n","PE-JUN","Region","15988",CountryCode.PERU),
    PE_LAL("La Libertad","PE-LAL","Region","15990",CountryCode.PERU),
    PE_LAM("Lambayeque","PE-LAM","Region","15973",CountryCode.PERU),
    PE_LIM("Lima","PE-LIM","Region","15969",CountryCode.PERU),
    PE_X1("Lima Metropolitana","PE-X1","Province","42646",CountryCode.PERU),
    PE_LOR("Loreto","PE-LOR","Region","15974",CountryCode.PERU),
    PE_MDD("Madre de Dios","PE-MDD","Region","15970",CountryCode.PERU),
    PE_MOQ("Moquegua","PE-MOQ","Region","15975",CountryCode.PERU),
    PE_PAS("Pasco","PE-PAS","Region","15991",CountryCode.PERU),
    PE_PIU("Piura","PE-PIU","Region","15976",CountryCode.PERU),
    PE_PUN("Puno","PE-PUN","Region","15977",CountryCode.PERU),
    PE_SAM("San Mart\u0237n","PE-SAM","Region","15994",CountryCode.PERU),
    PE_TAC("Tacna","PE-TAC","Region","15978",CountryCode.PERU),
    PE_TUM("Tumbes","PE-TUM","Region","15979",CountryCode.PERU),
    PE_UCA("Ucayali","PE-UCA","Region","15993",CountryCode.PERU);
    
    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
