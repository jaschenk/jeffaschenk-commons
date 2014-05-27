package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Romania
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_RO {
    /**
     * State Code Enumerator
     */
    RO_AB("Alba","RO-AB","County","17794", CountryCode.ROMANIA),
        RO_AR("Arad","RO-AR","County","17795",CountryCode.ROMANIA),
        RO_AG("Arges","RO-AG","County","17796",CountryCode.ROMANIA),
        RO_BC("Bacau","RO-BC","County","17797",CountryCode.ROMANIA),
        RO_BH("Bihor","RO-BH","County","17798",CountryCode.ROMANIA),
        RO_BN("Bistrita-Nasaud","RO-BN","County","17799",CountryCode.ROMANIA),
        RO_BT("Botosani","RO-BT","County","17800",CountryCode.ROMANIA),
        RO_BR("Braila","RO-BR","County","17801",CountryCode.ROMANIA),
        RO_BV("Brasov","RO-BV","County","17783",CountryCode.ROMANIA),
        RO_B("Bucuresti","RO-B","City","17802",CountryCode.ROMANIA),
        RO_BZ("Buzau","RO-BZ","County","17784",CountryCode.ROMANIA),
        RO_CL("Calarasi","RO-CL","County","17803",CountryCode.ROMANIA),
        RO_CS("Caras-Severin","RO-CS","County","17785",CountryCode.ROMANIA),
        RO_CJ("Cluj","RO-CJ","County","17804",CountryCode.ROMANIA),
        RO_CT("Constanta","RO-CT","County","17786",CountryCode.ROMANIA),
        RO_CV("Covasna","RO-CV","County","17805",CountryCode.ROMANIA),
        RO_DJ("Dolj","RO-DJ","County","17788",CountryCode.ROMANIA),
        RO_DB("D\u0224mbovita","RO-DB","County","17787",CountryCode.ROMANIA),
        RO_GL("Galati","RO-GL","County","17806",CountryCode.ROMANIA),
        RO_GR("Giurgiu","RO-GR","County","17789",CountryCode.ROMANIA),
        RO_GJ("Gorj","RO-GJ","County","17790",CountryCode.ROMANIA),
        RO_HR("Harghita","RO-HR","County","17807",CountryCode.ROMANIA),
        RO_HD("Hunedoara","RO-HD","County","17791",CountryCode.ROMANIA),
        RO_IL("Ialomita","RO-IL","County","17782",CountryCode.ROMANIA),
        RO_IS("Iasi","RO-IS","County","17792",CountryCode.ROMANIA),
        RO_IF("Ilfov","RO-IF","County","17817",CountryCode.ROMANIA),
        RO_MM("Maramures","RO-MM","County","17793",CountryCode.ROMANIA),
        RO_MH("Mehedinti","RO-MH","County","17808",CountryCode.ROMANIA),
        RO_MS("Mures","RO-MS","County","17821",CountryCode.ROMANIA),
        RO_NT("Neamt","RO-NT","County","17809",CountryCode.ROMANIA),
        RO_OT("Olt","RO-OT","County","17820",CountryCode.ROMANIA),
        RO_PH("Prahova","RO-PH","County","17810",CountryCode.ROMANIA),
        RO_SJ("Salaj","RO-SJ","County","17811",CountryCode.ROMANIA),
        RO_SM("Satu Mare","RO-SM","County","17819",CountryCode.ROMANIA),
        RO_SB("Sibiu","RO-SB","County","17812",CountryCode.ROMANIA),
        RO_SV("Suceava","RO-SV","County","17813",CountryCode.ROMANIA),
        RO_TR("Teleorman","RO-TR","County","17822",CountryCode.ROMANIA),
        RO_TM("Timis","RO-TM","County","17814",CountryCode.ROMANIA),
        RO_TL("Tulcea","RO-TL","County","17815",CountryCode.ROMANIA),
        RO_VS("Vaslui","RO-VS","County","17816",CountryCode.ROMANIA),
        RO_VN("Vrancea","RO-VN","County","17781",CountryCode.ROMANIA),
        RO_VL("V\u0224lcea","RO-VL","County","17818",CountryCode.ROMANIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_RO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
