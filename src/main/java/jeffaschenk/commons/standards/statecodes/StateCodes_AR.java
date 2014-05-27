package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Argentina
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AR {
    /**
     * State Code Enumerator
     */
    AR_B("Buenos Aires","AR-B","province","12439", CountryCode.ARGENTINA),
    AR_C("Capital federal","AR-C","federal district","12437",CountryCode.ARGENTINA),
    AR_K("Catamarca","AR-K","province","12438",CountryCode.ARGENTINA),
    AR_H("Chaco","AR-H","province","12440",CountryCode.ARGENTINA),
    AR_U("Chubut","AR-U","province","12454",CountryCode.ARGENTINA),
    AR_W("Corrientes","AR-W","province","12442",CountryCode.ARGENTINA),
    AR_X("C\u0243rdoba","AR-X","province","12441",CountryCode.ARGENTINA),
    AR_E("Entre R\u0237os","AR-E","province","12443",CountryCode.ARGENTINA),
    AR_P("Formosa","AR-P","province","12455",CountryCode.ARGENTINA),
    AR_Y("Jujuy","AR-Y","province","12444",CountryCode.ARGENTINA),
    AR_L("La Pampa","AR-L","province","12445",CountryCode.ARGENTINA),
    AR_F("La Rioja","AR-F","province","12436",CountryCode.ARGENTINA),
    AR_M("Mendoza","AR-M","province","12446",CountryCode.ARGENTINA),
    AR_N("Misiones","AR-N","province","12456",CountryCode.ARGENTINA),
    AR_Q("Neuqu\u0233n","AR-Q","province","12447",CountryCode.ARGENTINA),
    AR_R("R\u0237o Negro","AR-R","province","12448",CountryCode.ARGENTINA),
    AR_A("Salta","AR-A","province","12435",CountryCode.ARGENTINA),
    AR_J("San Juan","AR-J","province","12449",CountryCode.ARGENTINA),
    AR_D("San Luis","AR-D","province","12450",CountryCode.ARGENTINA),
    AR_Z("Santa Cruz","AR-Z","province","12434",CountryCode.ARGENTINA),
    AR_S("Santa Fe","AR-S","province","12451",CountryCode.ARGENTINA),
    AR_G("Santiago del Estero","AR-G","province","12433",CountryCode.ARGENTINA),
    AR_V("Tierra del Fuego","AR-V","province","12452",CountryCode.ARGENTINA),
    AR_T("Tucum\u0225n","AR-T","province","12453",CountryCode.ARGENTINA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
