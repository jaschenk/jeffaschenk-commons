package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Panama
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PA {
    /**
     * State Code Enumerator
     */
    PA_1("Bocas del Toro","PA-1","province","15928", CountryCode.PANAMA),
    PA_4("Chiriqu\u0237","PA-4","province","15920",CountryCode.PANAMA),
    PA_2("Cocl\u0233","PA-2","province","15929",CountryCode.PANAMA),
    PA_3("Col\u0243n","PA-3","province","15921",CountryCode.PANAMA),
    PA_0("Comarca de San Blas","PA-0","Comarca","15925",CountryCode.PANAMA),
    PA_5("Dari\u0233n","PA-5","province","15922",CountryCode.PANAMA),
    PA_6("Herrera","PA-6","province","15923",CountryCode.PANAMA),
    PA_7("Los Santos","PA-7","province","15917",CountryCode.PANAMA),
    PA_8("Panam\u0225","PA-8","province","15916",CountryCode.PANAMA),
    PA_9("Veraguas","PA-9","province","15927",CountryCode.PANAMA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
