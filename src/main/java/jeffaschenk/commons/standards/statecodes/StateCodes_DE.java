package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Germany
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_DE {
    /**
     * State Code Enumerator
     */
    DE_BW("Baden-W\u0252rttemberg", "DE-BW", "State", "14337", CountryCode.GERMANY),
    DE_BY("Bayern", "DE-BY", "State", "14044", CountryCode.GERMANY),
    DE_BE("Berlin", "DE-BE", "State", "14338", CountryCode.GERMANY),
    DE_BB("Brandenburg", "DE-BB", "State", "14045", CountryCode.GERMANY),
    DE_HB("Bremen", "DE-HB", "State", "14339", CountryCode.GERMANY),
    DE_HH("Hamburg", "DE-HH", "State", "14046", CountryCode.GERMANY),
    DE_HE("Hessen", "DE-HE", "State", "14340", CountryCode.GERMANY),
    DE_MV("Mecklenburg-Vorpommern", "DE-MV", "State", "14047", CountryCode.GERMANY),
    DE_NI("Niedersachsen", "DE-NI", "State", "14341", CountryCode.GERMANY),
    DE_NW("Nordrhein-Westfalen", "DE-NW", "State", "14048", CountryCode.GERMANY),
    DE_RP("Rheinland-Pfalz", "DE-RP", "State", "14342", CountryCode.GERMANY),
    DE_SL("Saarland", "DE-SL", "State", "14049", CountryCode.GERMANY),
    DE_SN("Sachsen", "DE-SN", "State", "14343", CountryCode.GERMANY),
    DE_ST("Sachsen-Anhalt", "DE-ST", "State", "14050", CountryCode.GERMANY),
    DE_SH("Schleswig-Holstein", "DE-SH", "State", "14344", CountryCode.GERMANY),
    DE_TH("Th\u0252ringen", "DE-TH", "State", "14051", CountryCode.GERMANY);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_DE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
