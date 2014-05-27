package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Saint Kitts and Nevis
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KN {
    /**
     * State Code Enumerator
     */
    KN_01("Christ Church Nichola Town","KN-01","Parish","17933", CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_02("Saint Anne Sandy Point","KN-02","Parish","17940",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_03("Saint George Basseterre","KN-03","Parish","17934",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_04("Saint George Gingerland","KN-04","Parish","17941",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_05("Saint James Windward","KN-05","Parish","17935",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_06("Saint John Capisterre","KN-06","Parish","17932",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_07("Saint John Figtree","KN-07","Parish","17936",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_08("Saint Mary Cayon","KN-08","Parish","17942",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_09("Saint Paul Capisterre","KN-09","Parish","17937",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_10("Saint Paul Charlestown","KN-10","Parish","17944",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_11("Saint Peter Basseterre","KN-11","Parish","17938",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_12("Saint Thomas Lowland","KN-12","Parish","17943",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_13("Saint Thomas Middle Island","KN-13","Parish","17939",CountryCode.SAINT_KITTS_AND_NEVIS),
    KN_15("Trinity Palmetto Point","KN-15","Parish","17931",CountryCode.SAINT_KITTS_AND_NEVIS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
