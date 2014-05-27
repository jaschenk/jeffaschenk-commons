package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Liechtenstein
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LI {
    /**
     * State Code Enumerator
     */
    LI_01("Balzers","LI-01","Commune","15193", CountryCode.LIECHTENSTEIN),
    LI_02("Eschen","LI-02","Commune","15194",CountryCode.LIECHTENSTEIN),
    LI_03("Gamprin","LI-03","Commune","15192",CountryCode.LIECHTENSTEIN),
    LI_04("Mauren","LI-04","Commune","15195",CountryCode.LIECHTENSTEIN),
    LI_05("Planken","LI-05","Commune","15196",CountryCode.LIECHTENSTEIN),
    LI_06("Ruggell","LI-06","Commune","15191",CountryCode.LIECHTENSTEIN),
    LI_07("Schaan","LI-07","Commune","15197",CountryCode.LIECHTENSTEIN),
    LI_08("Schellenberg","LI-08","Commune","15200",CountryCode.LIECHTENSTEIN),
    LI_09("Triesen","LI-09","Commune","15198",CountryCode.LIECHTENSTEIN),
    LI_10("Triesenberg","LI-10","Commune","15199",CountryCode.LIECHTENSTEIN),
    LI_11("Vaduz","LI-11","Commune","15190",CountryCode.LIECHTENSTEIN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LI(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
