package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: El Salvador
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SV {
    /**
     * State Code Enumerator
     */
    SV_AH("Ahuachap\u0225n","SV-AH","Department","13619", CountryCode.EL_SALVADOR),
    SV_CA("Caba\u0241as","SV-CA","Department","13620",CountryCode.EL_SALVADOR),
    SV_CH("Chalatenango","SV-CH","Department","13618",CountryCode.EL_SALVADOR),
    SV_CU("Cuscatl\u0225n","SV-CU","Department","13621",CountryCode.EL_SALVADOR),
    SV_LI("La Libertad","SV-LI","Department","13627",CountryCode.EL_SALVADOR),
    SV_PA("La Paz","SV-PA","Department","13622",CountryCode.EL_SALVADOR),
    SV_UN("La Uni\u0243n","SV-UN","Department","13623",CountryCode.EL_SALVADOR),
    SV_MO("Moraz\u0225n","SV-MO","Department","13617",CountryCode.EL_SALVADOR),
    SV_SM("San Miguel","SV-SM","Department","13624",CountryCode.EL_SALVADOR),
    SV_SS("San Salvador","SV-SS","Department","13628",CountryCode.EL_SALVADOR),
    SV_SV("San Vicente","SV-SV","Department","13616",CountryCode.EL_SALVADOR),
    SV_SA("Santa Ana","SV-SA","Department","13625",CountryCode.EL_SALVADOR),
    SV_SO("Sonsonate","SV-SO","Department","13626",CountryCode.EL_SALVADOR),
    SV_US("Usulut\u0225n","SV-US","Department","13615",CountryCode.EL_SALVADOR);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SV(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
