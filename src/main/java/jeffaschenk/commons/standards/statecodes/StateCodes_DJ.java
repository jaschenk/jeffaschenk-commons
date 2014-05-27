package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Djibouti
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_DJ {
    /**
     * State Code Enumerator
     */
    DJ_AS("Ali Sabieh","DJ-AS","Region","13283", CountryCode.DJIBOUTI),
    DJ_AR("Arta","DJ-AR","Region","19439",CountryCode.DJIBOUTI),
    DJ_DI("Dikhil","DJ-DI","Region","13282",CountryCode.DJIBOUTI),
    DJ_DJ("Djibouti","DJ-DJ","City","13280",CountryCode.DJIBOUTI),
    DJ_OB("Obock","DJ-OB","Region","13281",CountryCode.DJIBOUTI),
    DJ_TA("Tadjourah","DJ-TA","Region","13284",CountryCode.DJIBOUTI);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_DJ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
