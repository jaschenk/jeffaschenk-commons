package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Moldova
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MD {
    /**
     * State Code Enumerator
     */
    MD_BA("Balti", "MD-BA", "district", "15453", CountryCode.MOLDOVA),
    MD_CA("Cahul", "MD-CA", "district", "15455", CountryCode.MOLDOVA),
    MD_CU("Chisinau", "MD-CU", "district", "15459", CountryCode.MOLDOVA),
    MD_CH("Chisinau City", "MD-CH", "city", "20309", CountryCode.MOLDOVA),
    MD_ED("Edinet", "MD-ED", "district", "15477", CountryCode.MOLDOVA),
    MD_GA("Gagauzia, Unitate Teritoriala Autonoma (UTAG)", "MD-GA", "autonomous territory", "15446", CountryCode.MOLDOVA),
    MD_LA("Lapusna", "MD-LA", "district", "20171", CountryCode.MOLDOVA),
    MD_OR("Orhei", "MD-OR", "district", "15451", CountryCode.MOLDOVA),
    MD_SO("Soroca", "MD-SO", "district", "15466", CountryCode.MOLDOVA),
    MD_SN("St\u0238nga Nistrului, unitatea teritoriala din", "MD-SN", "territorial unit", "20172", CountryCode.MOLDOVA),
    MD_TA("Taraclia", "MD-TA", "district", "15481", CountryCode.MOLDOVA),
    MD_TI("Tighina [Bender]", "MD-TI", "district", "15469", CountryCode.MOLDOVA),
    MD_UN("Ungheni", "MD-UN", "district", "15470", CountryCode.MOLDOVA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MD(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
