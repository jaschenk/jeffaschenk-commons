package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Poland
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PL {
    /**
     * State Code Enumerator
     */
    PL_DS("Dolnoslaskie", "PL-DS", "Voivodship", "17731", CountryCode.POLAND),
    PL_KP("Kujawsko-pomorskie", "PL-KP", "Voivodship", "17732", CountryCode.POLAND),
    PL_LU("Lubelskie", "PL-LU", "Voivodship", "17733", CountryCode.POLAND),
    PL_LB("Lubuskie", "PL-LB", "Voivodship", "17740", CountryCode.POLAND),
    PL_LD("L\u0243dzkie", "PL-LD", "Voivodship", "17734", CountryCode.POLAND),
    PL_MA("Malopolskie", "PL-MA", "Voivodship", "17744", CountryCode.POLAND),
    PL_MZ("Mazowieckie", "PL-MZ", "Voivodship", "17735", CountryCode.POLAND),
    PL_OP("Opolskie", "PL-OP", "Voivodship", "17743", CountryCode.POLAND),
    PL_PK("Podkarpackie", "PL-PK", "Voivodship", "17736", CountryCode.POLAND),
    PL_PD("Podlaskie", "PL-PD", "Voivodship", "17742", CountryCode.POLAND),
    PL_PM("Pomorskie", "PL-PM", "Voivodship", "17737", CountryCode.POLAND),
    PL_SL("Slaskie", "PL-SL", "Voivodship", "17745", CountryCode.POLAND),
    PL_SK("Swietokrzyskie", "PL-SK", "Voivodship", "17738", CountryCode.POLAND),
    PL_WN("Warminsko-mazurskie", "PL-WN", "Voivodship", "17741", CountryCode.POLAND),
    PL_WP("Wielkopolskie", "PL-WP", "Voivodship", "17739", CountryCode.POLAND),
    PL_ZP("Zachodniopomorskie", "PL-ZP", "Voivodship", "17730", CountryCode.POLAND);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PL(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
