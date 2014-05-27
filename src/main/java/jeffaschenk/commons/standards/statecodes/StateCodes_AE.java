package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: United Arab Emirates
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AE {
    /**
     * State Code Enumerator
     */
    AE_AZ("Abu Z\u0184aby [Abu Dhabi]", "AE-AZ", "Emirate", "18622", CountryCode.UNITED_ARAB_EMIRATES),
    AE_AJ("Ajman", "AE-AJ", "Emirate", "20215", CountryCode.UNITED_ARAB_EMIRATES),
    AE_FU("Al Fujayrah", "AE-FU", "Emirate", "20218", CountryCode.UNITED_ARAB_EMIRATES),
    AE_SH("Ash Shariqah [Sharjah]", "AE-SH", "Emirate", "20219", CountryCode.UNITED_ARAB_EMIRATES),
    AE_DU("Dubayy [Dubai]", "AE-DU", "Emirate", "20217", CountryCode.UNITED_ARAB_EMIRATES),
    AE_RK("Ra's al Khaymah", "AE-RK", "Emirate", "18620", CountryCode.UNITED_ARAB_EMIRATES),
    AE_UQ("Umm al Qaywayn", "AE-UQ", "Emirate", "20220", CountryCode.UNITED_ARAB_EMIRATES);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
