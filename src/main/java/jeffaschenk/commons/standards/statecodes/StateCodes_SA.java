package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Saudi Arabia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SA {
    /**
     * State Code Enumerator
     */
    SA_06("Hail", "SA-06", "Region", "17981", CountryCode.SAUDI_ARABIA),
    SA_14("Aseer", "SA-14", "Region", "17984", CountryCode.SAUDI_ARABIA),
    SA_08("Al Hudud Ash Shamaliyah", "SA-08", "Region", "17985", CountryCode.SAUDI_ARABIA),
    SA_11("Baha", "SA-11", "Region", "17980", CountryCode.SAUDI_ARABIA),
    SA_12("Al Jawf", "SA-12", "Region", "17982", CountryCode.SAUDI_ARABIA),
    SA_03("Al Madinah", "SA-03", "Region", "17979", CountryCode.SAUDI_ARABIA),
    SA_05("Al Qasim", "SA-05", "Region", "17988", CountryCode.SAUDI_ARABIA),
    SA_01("Riyadh", "SA-01", "Region", "17978", CountryCode.SAUDI_ARABIA),  
    SA_04("Ash Sharqiyah", "SA-04", "Region", "17989", CountryCode.SAUDI_ARABIA),
    SA_09("Jizan", "SA-09", "Region", "17986", CountryCode.SAUDI_ARABIA),
    SA_02("Makkah", "SA-02", "Region", "17987", CountryCode.SAUDI_ARABIA),
    SA_10("Najran", "SA-10", "Region", "17983", CountryCode.SAUDI_ARABIA),
    SA_07("Tabuk", "SA-07", "Region", "17977", CountryCode.SAUDI_ARABIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
