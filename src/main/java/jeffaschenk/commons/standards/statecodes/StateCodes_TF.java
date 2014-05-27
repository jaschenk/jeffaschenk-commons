package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: French Southern Territories
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TF {
    /**
     * State Code Enumerator
     */
    TF_X2("Crozet Islands", "TF-X2", "district", "13836", CountryCode.FRENCH_SOUTHERN_TERRITORIES),
    TF_X1("Ile Saint-Paul et Ile Amsterdam", "TF-X1", "district", "13834", CountryCode.FRENCH_SOUTHERN_TERRITORIES),
    TF_X4("Iles Eparses", "TF-X4", "district", "21356", CountryCode.FRENCH_SOUTHERN_TERRITORIES),
    TF_X3("Kerguelen", "TF-X3", "district", "13837", CountryCode.FRENCH_SOUTHERN_TERRITORIES);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TF(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
