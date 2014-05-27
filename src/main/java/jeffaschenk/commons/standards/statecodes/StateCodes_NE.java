package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Niger
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NE {
    /**
     * State Code Enumerator
     */
    NE_1("Agadez","NE-1","Region","15782", CountryCode.NIGER),
    NE_2("Diffa","NE-2","Region","15783",CountryCode.NIGER),
    NE_3("Dosso","NE-3","Region","15784",CountryCode.NIGER),
    NE_4("Maradi","NE-4","Region","15785",CountryCode.NIGER),
    NE_8("Niamey","NE-8","Capital District","15786",CountryCode.NIGER),
    NE_5("Tahoua","NE-5","Region","15787",CountryCode.NIGER),
    NE_6("Tillab\u0233ri","NE-6","Region","15788",CountryCode.NIGER),
    NE_7("Zinder","NE-7","Region","15789",CountryCode.NIGER);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
