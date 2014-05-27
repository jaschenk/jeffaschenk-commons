package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Jamaica
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_JM {
    /**
     * State Code Enumerator
     */
    JM_13("Clarendon", "JM-13", "Parish", "14905", CountryCode.JAMAICA),
    JM_09("Hanover", "JM-09", "Parish", "14915", CountryCode.JAMAICA),
    JM_01("Kingston", "JM-01", "Parish", "14906", CountryCode.JAMAICA),
    JM_12("Manchester", "JM-12", "Parish", "14914", CountryCode.JAMAICA),
    JM_04("Portland", "JM-04", "Parish", "14907", CountryCode.JAMAICA),
    JM_02("Saint Andrew", "JM-02", "Parish", "14908", CountryCode.JAMAICA),
    JM_06("Saint Ann", "JM-06", "Parish", "14904", CountryCode.JAMAICA),
    JM_14("Saint Catherine", "JM-14", "Parish", "14909", CountryCode.JAMAICA),
    JM_11("Saint Elizabeth", "JM-11", "Parish", "14903", CountryCode.JAMAICA),
    JM_08("Saint James", "JM-08", "Parish", "14910", CountryCode.JAMAICA),
    JM_05("Saint Mary", "JM-05", "Parish", "14911", CountryCode.JAMAICA),
    JM_03("Saint Thomas", "JM-03", "Parish", "14902", CountryCode.JAMAICA),
    JM_07("Trelawny", "JM-07", "Parish", "14912", CountryCode.JAMAICA),
    JM_10("Westmoreland", "JM-10", "Parish", "14913", CountryCode.JAMAICA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_JM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
