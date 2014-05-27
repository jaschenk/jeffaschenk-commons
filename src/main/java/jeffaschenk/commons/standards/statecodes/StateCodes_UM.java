package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: United States Minor Outlying Islands
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_UM {
    /**
     * State Code Enumerator
     */
    UM_81("Baker Island","UM-81","Territory","18637", CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_84("Howland Island","UM-84","Territory","18636",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_86("Jarvis Island","UM-86","Territory","18638",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_67("Johnston Atoll","UM-67","Territory","18635",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_89("Kingman Reef","UM-89","Territory","18639",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_71("Midway Islands","UM-71","Territory","18634",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_76("Navassa Island","UM-76","Territory","18633",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_95("Palmyra Atoll","UM-95","Territory","18632",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS),
    UM_79("Wake Island","UM-79","Territory","18631",CountryCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_UM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
