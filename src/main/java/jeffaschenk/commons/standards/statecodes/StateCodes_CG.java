package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Congo
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CG {
    /**
     * State Code Enumerator
     */
    CG_11("Bouenza", "CG-11", "Department", "13078", CountryCode.CONGO),
    CG_BZV("Brazzaville", "CG-BZV", "Commune", "13080", CountryCode.CONGO),
    CG_8("Cuvette", "CG-8", "Department", "13077", CountryCode.CONGO),
    CG_15("Cuvette-Ouest", "CG-15", "Department", "13076", CountryCode.CONGO),
    CG_5("Kouilou", "CG-5", "Department", "13081", CountryCode.CONGO),
    CG_7("Likouala", "CG-7", "Department", "13079", CountryCode.CONGO),
    CG_2("L\u0233koumou", "CG-2", "Department", "13082", CountryCode.CONGO),
    CG_9("Niari", "CG-9", "Department", "13083", CountryCode.CONGO),
    CG_14("Plateaux", "CG-14", "Department", "13086", CountryCode.CONGO),
    CG_12("Pool", "CG-12", "Department", "13084", CountryCode.CONGO),
    CG_13("Sangha", "CG-13", "Department", "13085", CountryCode.CONGO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
