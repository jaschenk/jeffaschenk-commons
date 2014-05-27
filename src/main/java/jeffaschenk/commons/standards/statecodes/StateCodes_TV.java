package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Tuvalu
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TV {
    /**
     * State Code Enumerator
     */
    TV_FUN("Funafuti", "TV-FUN", "Island Council", "18586", CountryCode.TUVALU),
    TV_NMG("Nanumanga", "TV-NMG", "Island Council", "18580", CountryCode.TUVALU),
    TV_NMA("Nanumea", "TV-NMA", "Island Council", "18581", CountryCode.TUVALU),
    TV_NIT("Niutao", "TV-NIT", "Island Council", "18582", CountryCode.TUVALU),
    TV_NIU("Nui", "TV-NIU", "Island Council", "18583", CountryCode.TUVALU),
    TV_NKF("Nukufetau", "TV-NKF", "Island Council", "18579", CountryCode.TUVALU),
    TV_NKL("Nukulaelae", "TV-NKL", "Island Council", "18584", CountryCode.TUVALU),
    TV_VAI("Vaitupu", "TV-VAI", "Island Council", "18585", CountryCode.TUVALU);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TV(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
