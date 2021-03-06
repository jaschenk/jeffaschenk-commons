package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Belize
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BZ {
    /**
     * State Code Enumerator
     */
    BZ_BZ("Belize", "BZ-BZ", "District", "12687", CountryCode.BELIZE),
    BZ_CY("Cayo", "BZ-CY", "District", "12691", CountryCode.BELIZE),
    BZ_CZL("Corozal", "BZ-CZL", "District", "12688", CountryCode.BELIZE),
    BZ_OW("Orange Walk", "BZ-OW", "District", "12689", CountryCode.BELIZE),
    BZ_SC("Stann Creek", "BZ-SC", "District", "12686", CountryCode.BELIZE),
    BZ_TOL("Toledo", "BZ-TOL", "District", "12690", CountryCode.BELIZE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
