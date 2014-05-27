package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Haiti
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_HT {
    /**
     * State Code Enumerator
     */
    HT_AR("Artibonite","HT-AR","Department","14621", CountryCode.HAITI),
    HT_CE("Centre","HT-CE","Department","14617",CountryCode.HAITI),
    HT_GA("Grande-Anse","HT-GA","Department","14620",CountryCode.HAITI),
    HT_ND("Nord","HT-ND","Department","14616",CountryCode.HAITI),
    HT_NE("Nord-Est","HT-NE","Department","14618",CountryCode.HAITI),
    HT_NO("Nord-Ouest","HT-NO","Department","14614",CountryCode.HAITI),
    HT_OU("Ouest","HT-OU","Department","14615",CountryCode.HAITI),
    HT_SD("Sud","HT-SD","Department","14613",CountryCode.HAITI),
    HT_SE("Sud-Est","HT-SE","Department","14619",CountryCode.HAITI);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_HT(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
