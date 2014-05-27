package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Guyana
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GY {
    /**
     * State Code Enumerator
     */
    GY_BA("Barima-Waini","GY-BA","Region","14605", CountryCode.GUYANA),
    GY_CU("Cuyuni-Mazaruni","GY-CU","Region","14612",CountryCode.GUYANA),
    GY_DE("Demerara-Mahaica","GY-DE","Region","14606",CountryCode.GUYANA),
    GY_EB("East Berbice-Corentyne","GY-EB","Region","14611",CountryCode.GUYANA),
    GY_ES("Essequibo Islands-West Demerara","GY-ES","Region","14607",CountryCode.GUYANA),
    GY_MA("Mahaica-Berbice","GY-MA","Region","14608",CountryCode.GUYANA),
    GY_PM("Pomeroon-Supenaam","GY-PM","Region","14604",CountryCode.GUYANA),
    GY_PT("Potaro-Siparuni","GY-PT","Region","14609",CountryCode.GUYANA),
    GY_UD("Upper Demerara-Berbice","GY-UD","Region","14603",CountryCode.GUYANA),
    GY_UT("Upper Takutu-Upper Essequibo","GY-UT","Region","14610",CountryCode.GUYANA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
