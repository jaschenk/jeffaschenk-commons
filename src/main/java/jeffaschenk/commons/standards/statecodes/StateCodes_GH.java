package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Ghana
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GH {
    /**
     * State Code Enumerator
     */
    GH_AH("Ashanti","GH-AH","Region","14403", CountryCode.GHANA),
    GH_BA("Brong-Ahafo","GH-BA","Region","14400",CountryCode.GHANA),
    GH_CP("Central","GH-CP","Region","14404",CountryCode.GHANA),
    GH_EP("Eastern","GH-EP","Region","14405",CountryCode.GHANA),
    GH_AA("Greater Accra","GH-AA","Region","14399",CountryCode.GHANA),
    GH_NP("Northern","GH-NP","Region","14402",CountryCode.GHANA),
    GH_UE("Upper East","GH-UE","Region","14406",CountryCode.GHANA),
    GH_UW("Upper West","GH-UW","Region","14397",CountryCode.GHANA),
    GH_TV("Volta","GH-TV","Region","14401",CountryCode.GHANA),
    GH_WP("Western","GH-WP","Region","14398",CountryCode.GHANA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GH(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
