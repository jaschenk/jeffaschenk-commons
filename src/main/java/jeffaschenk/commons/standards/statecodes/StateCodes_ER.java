package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Eritrea
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ER {
    /**
     * State Code Enumerator
     */
    ER_AN("Anseba","ER-AN","Region","13638", CountryCode.ERITREA),
    ER_DU("Debub","ER-DU","Region","13641",CountryCode.ERITREA),
    ER_DK("Debubawi Keyih Bahri [Debub-Keih-Bahri]","ER-DK","Region","13639",CountryCode.ERITREA),
    ER_GB("Gash-Barka","ER-GB","Region","13637",CountryCode.ERITREA),
    ER_MA("Maakel [Maekel]","ER-MA","Region","13640",CountryCode.ERITREA),
    ER_SK("Semenawi Keyih Bahri [Semien-Keih-Bahri]","ER-SK","Region","13636",CountryCode.ERITREA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ER(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
