package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Cayman Islands
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KY {
    /**
     * State Code Enumerator
     */
    KY_01("Bodden Town","KY-01","District","48080", CountryCode.CAYMAN_ISLANDS),
    KY_02("Cayman Brac","KY-02","District","12962",CountryCode.CAYMAN_ISLANDS),
    KY_03("East End","KY-03","District","20891",CountryCode.CAYMAN_ISLANDS),
    KY_04("George Town","KY-04","District","48081",CountryCode.CAYMAN_ISLANDS),
    KY_05("Little Cayman","KY-05","District","12958",CountryCode.CAYMAN_ISLANDS),
    KY_06("North Side","KY-06","District","48082",CountryCode.CAYMAN_ISLANDS),
    KY_07("West Bay","KY-07","District","48083",CountryCode.CAYMAN_ISLANDS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
