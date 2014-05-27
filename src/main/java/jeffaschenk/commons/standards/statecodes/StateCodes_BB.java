package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Barbados
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BB {
    /**
     * State Code Enumerator
     */
    BB_01("Christ Church", "BB-01", "Parish", "12661", CountryCode.BARBADOS),
    BB_02("Saint Andrew", "BB-02", "Parish", "12663", CountryCode.BARBADOS),
    BB_03("Saint George", "BB-03", "Parish", "12662", CountryCode.BARBADOS),
    BB_04("Saint James", "BB-04", "Parish", "12660", CountryCode.BARBADOS),
    BB_05("Saint John", "BB-05", "Parish", "12664", CountryCode.BARBADOS),
    BB_06("Saint Joseph", "BB-06", "Parish", "12659", CountryCode.BARBADOS),
    BB_07("Saint Lucy", "BB-07", "Parish", "12665", CountryCode.BARBADOS),
    BB_08("Saint Michael", "BB-08", "Parish", "12658", CountryCode.BARBADOS),
    BB_09("Saint Peter", "BB-09", "Parish", "12666", CountryCode.BARBADOS),
    BB_10("Saint Philip", "BB-10", "Parish", "12667", CountryCode.BARBADOS),
    BB_11("Saint Thomas", "BB-11", "Parish", "12657", CountryCode.BARBADOS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BB(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
