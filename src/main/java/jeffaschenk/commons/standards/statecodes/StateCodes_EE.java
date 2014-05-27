package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Estonia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_EE {
    /**
     * State Code Enumerator
     */
    EE_37("Harjumaa","EE-37","County","13653", CountryCode.ESTONIA),
    EE_39("Hiiumaa","EE-39","County","13654",CountryCode.ESTONIA),
    EE_44("Ida-Virumaa","EE-44","County","13646",CountryCode.ESTONIA),
    EE_51("J\u0228rvamaa","EE-51","County","13652",CountryCode.ESTONIA),
    EE_49("J\u0245gevamaa","EE-49","County","13647",CountryCode.ESTONIA),
    EE_59("L\u0228\u0228ne-Virumaa","EE-59","County","13655",CountryCode.ESTONIA),
    EE_57("L\u0228\u0228nemaa","EE-57","County","13648",CountryCode.ESTONIA),
    EE_67("P\u0228rnumaa","EE-67","County","13649",CountryCode.ESTONIA),
    EE_65("P\u0245lvamaa","EE-65","County","13650",CountryCode.ESTONIA),
    EE_70("Raplamaa","EE-70","County","13643",CountryCode.ESTONIA),
    EE_74("Saaremaa","EE-74","County","13645",CountryCode.ESTONIA),
    EE_78("Tartumaa","EE-78","County","13656",CountryCode.ESTONIA),
    EE_82("Valgamaa","EE-82","County","13651",CountryCode.ESTONIA),
    EE_84("Viljandimaa","EE-84","County","13644",CountryCode.ESTONIA),
    EE_86("V\u0245rumaa","EE-86","County","13642",CountryCode.ESTONIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_EE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
