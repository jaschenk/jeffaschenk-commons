package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Marshall Islands
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MH {
    /**
     * State Code Enumerator
     */
    MH_ALL("Ailinglaplap", "MH-ALL", "Municipality", "15362", CountryCode.MARSHALL_ISLANDS),
    MH_ALK("Ailuk", "MH-ALK", "Municipality", "15347", CountryCode.MARSHALL_ISLANDS),
    MH_ARN("Arno", "MH-ARN", "Municipality", "15348", CountryCode.MARSHALL_ISLANDS),
    MH_AUR("Aur", "MH-AUR", "Municipality", "15363", CountryCode.MARSHALL_ISLANDS),
    MH_EBO("Ebon", "MH-EBO", "Municipality", "15351", CountryCode.MARSHALL_ISLANDS),
    MH_ENI("Enewetak", "MH-ENI", "Municipality", "15365", CountryCode.MARSHALL_ISLANDS),
    MH_JAB("Jabat", "MH-JAB", "Municipality", "48101", CountryCode.MARSHALL_ISLANDS),
    MH_JAL("Jaluit", "MH-JAL", "Municipality", "15353", CountryCode.MARSHALL_ISLANDS),
    MH_KIL("Kili", "MH-KIL", "Municipality", "15367", CountryCode.MARSHALL_ISLANDS),
    MH_KWA("Kwajalein", "MH-KWA", "Municipality", "15355", CountryCode.MARSHALL_ISLANDS),
    MH_LAE("Lae", "MH-LAE", "Municipality", "15368", CountryCode.MARSHALL_ISLANDS),
    MH_LIB("Lib", "MH-LIB", "Municipality", "15356", CountryCode.MARSHALL_ISLANDS),
    MH_LIK("Likiep", "MH-LIK", "Municipality", "15357", CountryCode.MARSHALL_ISLANDS),
    MH_MAJ("Majuro", "MH-MAJ", "Municipality", "15345", CountryCode.MARSHALL_ISLANDS),
    MH_MAL("Maloelap", "MH-MAL", "Municipality", "15358", CountryCode.MARSHALL_ISLANDS),
    MH_MEJ("Mejit", "MH-MEJ", "Municipality", "15369", CountryCode.MARSHALL_ISLANDS),
    MH_MIL("Mili", "MH-MIL", "Municipality", "15359", CountryCode.MARSHALL_ISLANDS),
    MH_NMK("Namdrik", "MH-NMK", "Municipality", "15360", CountryCode.MARSHALL_ISLANDS),
    MH_NMU("Namu", "MH-NMU", "Municipality", "15344", CountryCode.MARSHALL_ISLANDS),
    MH_RON("Rongelap", "MH-RON", "Municipality", "15361", CountryCode.MARSHALL_ISLANDS),
    MH_UJA("Ujae", "MH-UJA", "Municipality", "15371", CountryCode.MARSHALL_ISLANDS),
    MH_UTI("Utirik", "MH-UTI", "Municipality", "15372", CountryCode.MARSHALL_ISLANDS),
    MH_WTH("Wotho", "MH-WTH", "Municipality", "15341", CountryCode.MARSHALL_ISLANDS),
    MH_WTJ("Wotje", "MH-WTJ", "Municipality", "15340", CountryCode.MARSHALL_ISLANDS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MH(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
