package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 *
 * Country: United States
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_US {
    /**
     * State Code Enumerator
     */
    US_AL("Alabama", "US-AL", "state", "18655", CountryCode.UNITED_STATES),
    US_AK("Alaska", "US-AK", "state", "18683", CountryCode.UNITED_STATES),
    US_AS("American Samoa", "US-AS", "outlying area", "19386", CountryCode.UNITED_STATES),
    US_AZ("Arizona", "US-AZ", "state", "18656", CountryCode.UNITED_STATES),
    US_AR("Arkansas", "US-AR", "state", "18684", CountryCode.UNITED_STATES),
    US_CA("California", "US-CA", "state", "18657", CountryCode.UNITED_STATES),
    US_CO("Colorado", "US-CO", "state", "18685", CountryCode.UNITED_STATES),
    US_CT("Connecticut", "US-CT", "state", "18658", CountryCode.UNITED_STATES),
    US_DE("Delaware", "US-DE", "state", "18686", CountryCode.UNITED_STATES),
    US_DC("District of Columbia", "US-DC", "district", "18659", CountryCode.UNITED_STATES),
    US_FL("Florida", "US-FL", "state", "18687", CountryCode.UNITED_STATES),
    US_GA("Georgia", "US-GA", "state", "18660", CountryCode.UNITED_STATES),
    US_GU("Guam", "US-GU", "outlying area", "19388", CountryCode.UNITED_STATES),
    US_HI("Hawaii", "US-HI", "state", "18688", CountryCode.UNITED_STATES),
    US_ID("Idaho", "US-ID", "state", "18661", CountryCode.UNITED_STATES),
    US_IL("Illinois", "US-IL", "state", "18674", CountryCode.UNITED_STATES),
    US_IN("Indiana", "US-IN", "state", "18662", CountryCode.UNITED_STATES),
    US_IA("Iowa", "US-IA", "state", "18663", CountryCode.UNITED_STATES),
    US_KS("Kansas", "US-KS", "state", "18675", CountryCode.UNITED_STATES),
    US_KY("Kentucky", "US-KY", "state", "18664", CountryCode.UNITED_STATES),
    US_LA("Louisiana", "US-LA", "state", "18676", CountryCode.UNITED_STATES),
    US_ME("Maine", "US-ME", "state", "18665", CountryCode.UNITED_STATES),
    US_MD("Maryland", "US-MD", "state", "18677", CountryCode.UNITED_STATES),
    US_MA("Massachusetts", "US-MA", "state", "18666", CountryCode.UNITED_STATES),
    US_MI("Michigan", "US-MI", "state", "18678", CountryCode.UNITED_STATES),
    US_MN("Minnesota", "US-MN", "state", "18646", CountryCode.UNITED_STATES),
    US_MS("Mississippi", "US-MS", "state", "18679", CountryCode.UNITED_STATES),
    US_MO("Missouri", "US-MO", "state", "18647", CountryCode.UNITED_STATES),
    US_MT("Montana", "US-MT", "state", "18648", CountryCode.UNITED_STATES),
    US_NE("Nebraska", "US-NE", "state", "18680", CountryCode.UNITED_STATES),
    US_NV("Nevada", "US-NV", "state", "18649", CountryCode.UNITED_STATES),
    US_NH("New Hampshire", "US-NH", "state", "18681", CountryCode.UNITED_STATES),
    US_NJ("New Jersey", "US-NJ", "state", "18650", CountryCode.UNITED_STATES),
    US_NM("New Mexico", "US-NM", "state", "18682", CountryCode.UNITED_STATES),
    US_NY("New York", "US-NY", "state", "18651", CountryCode.UNITED_STATES),
    US_NC("North Carolina", "US-NC", "state", "18645", CountryCode.UNITED_STATES),
    US_ND("North Dakota", "US-ND", "state", "18652", CountryCode.UNITED_STATES),
    US_MP("Northern Mariana Islands", "US-MP", "outlying area", "19390", CountryCode.UNITED_STATES),
    US_OH("Ohio", "US-OH", "state", "18689", CountryCode.UNITED_STATES),
    US_OK("Oklahoma", "US-OK", "state", "18653", CountryCode.UNITED_STATES),
    US_OR("Oregon", "US-OR", "state", "18654", CountryCode.UNITED_STATES),
    US_PA("Pennsylvania", "US-PA", "state", "18644", CountryCode.UNITED_STATES),
    US_PR("Puerto Rico", "US-PR", "outlying area", "19591", CountryCode.UNITED_STATES),
    US_RI("Rhode Island", "US-RI", "state", "18667", CountryCode.UNITED_STATES),
    US_SC("South Carolina", "US-SC", "state", "18690", CountryCode.UNITED_STATES),
    US_SD("South Dakota", "US-SD", "state", "18668", CountryCode.UNITED_STATES),
    US_TN("Tennessee", "US-TN", "state", "18643", CountryCode.UNITED_STATES),
    US_TX("Texas", "US-TX", "state", "18669", CountryCode.UNITED_STATES),
    US_UM("United States Minor Outlying Islands", "US-UM", "outlying area", "19597", CountryCode.UNITED_STATES),
    US_UT("Utah", "US-UT", "state", "18642", CountryCode.UNITED_STATES),
    US_VT("Vermont", "US-VT", "state", "18670", CountryCode.UNITED_STATES),
    US_VI("Virgin Islands, U.S.", "US-VI", "outlying area", "19593", CountryCode.UNITED_STATES),
    US_VA("Virginia", "US-VA", "state", "18641", CountryCode.UNITED_STATES),
    US_WA("Washington", "US-WA", "state", "18671", CountryCode.UNITED_STATES),
    US_WV("West Virginia", "US-WV", "state", "18640", CountryCode.UNITED_STATES),
    US_WI("Wisconsin", "US-WI", "state", "18672", CountryCode.UNITED_STATES),
    US_WY("Wyoming", "US-WY", "state", "18673", CountryCode.UNITED_STATES);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_US(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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

