package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Turkmenistan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TM {
    /**
     * State Code Enumerator
     */
    TM_A("Ahal","TM-A","Province","18569", CountryCode.TURKMENISTAN),
    TM_B("Balkan","TM-B","Province","18568",CountryCode.TURKMENISTAN),
    TM_D("Dasoguz","TM-D","Province","18571",CountryCode.TURKMENISTAN),
    TM_L("Lebap","TM-L","Province","18572",CountryCode.TURKMENISTAN),
    TM_M("Mary","TM-M","Province","18566",CountryCode.TURKMENISTAN),
    TM_X("","TM-X","City","18567",CountryCode.TURKMENISTAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
