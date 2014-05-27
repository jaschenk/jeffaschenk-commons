package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Cyprus
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CY {
    /**
     * State Code Enumerator
     */
    CY_04("Ammochostos","CY-04","District","20225", CountryCode.CYPRUS),
    CY_06("Keryneia","CY-06","District","19428",CountryCode.CYPRUS),
    CY_03("Larnaka","CY-03","District","19429",CountryCode.CYPRUS),
    CY_01("Lefkosia","CY-01","District","19430",CountryCode.CYPRUS),
    CY_02("Lemesos","CY-02","District","19431",CountryCode.CYPRUS),
	CY_05("Pafos","CY-05","District","19433",CountryCode.CYPRUS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
