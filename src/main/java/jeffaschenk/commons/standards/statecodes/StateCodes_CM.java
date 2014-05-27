package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Cameroon
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CM {
    /**
     * State Code Enumerator
     */
    CM_AD("Adamaoua","CM-AD","Region","12932", CountryCode.CAMEROON),
    CM_CE("Centre","CM-CE","Region","12928",CountryCode.CAMEROON),
    CM_ES("East","CM-ES","Region","12933",CountryCode.CAMEROON),
    CM_EN("Far North","CM-EN","Region","12934",CountryCode.CAMEROON),
    CM_LT("Littoral","CM-LT","Region","12929",CountryCode.CAMEROON),
    CM_NO("North","CM-NO","Region","12931",CountryCode.CAMEROON),
    CM_NW("North-West","CM-NW","Region","12927",CountryCode.CAMEROON),
    CM_SU("South","CM-SU","Region","12935",CountryCode.CAMEROON),
    CM_SW("South-West","CM-SW","Region","12926",CountryCode.CAMEROON),
    CM_OU("West","CM-OU","Region","12930",CountryCode.CAMEROON);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
