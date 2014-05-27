package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Saint Vincent and The Grenadines
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_VC {
    /**
     * State Code Enumerator
     */
    VC_01("Charlotte","VC-01","Parish","17963", CountryCode.SAINT_VINCENT_AND_THE_GRENADINES),
    VC_06("Grenadines","VC-06","Parish","17958",CountryCode.SAINT_VINCENT_AND_THE_GRENADINES),
    VC_02("Saint Andrew","VC-02","Parish","17962",CountryCode.SAINT_VINCENT_AND_THE_GRENADINES),
    VC_03("Saint David","VC-03","Parish","17959",CountryCode.SAINT_VINCENT_AND_THE_GRENADINES),
    VC_04("Saint George","VC-04","Parish","17957",CountryCode.SAINT_VINCENT_AND_THE_GRENADINES),
    VC_05("Saint Patrick","VC-05","Parish","17960",CountryCode.SAINT_VINCENT_AND_THE_GRENADINES);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_VC(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
