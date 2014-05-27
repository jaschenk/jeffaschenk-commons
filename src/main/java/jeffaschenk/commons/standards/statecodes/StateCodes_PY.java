package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Paraguay
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PY {
    /**
     * State Code Enumerator
     */
    PY_16("Alto Paraguay", "PY-16", "department", "15963", CountryCode.PARAGUAY),
    PY_10("Alto Paran\u0225", "PY-10", "department", "15964", CountryCode.PARAGUAY),
    PY_13("Amambay", "PY-13", "department", "15965", CountryCode.PARAGUAY),
    PY_ASU("Asunci\u0243n", "PY-ASU", "capital", "15954", CountryCode.PARAGUAY),
    PY_19("Boquer\u0243n", "PY-19", "department", "15953", CountryCode.PARAGUAY),
    PY_5("Caaguaz\u0250", "PY-5", "department", "15955", CountryCode.PARAGUAY),
    PY_6("Caazap\u0225", "PY-6", "department", "15966", CountryCode.PARAGUAY),
    PY_14("Canindey\u0250", "PY-14", "department", "15956", CountryCode.PARAGUAY),
    PY_11("Central", "PY-11", "department", "15952", CountryCode.PARAGUAY),
    PY_1("Concepci\u0243n", "PY-1", "department", "15957", CountryCode.PARAGUAY),
    PY_3("Cordillera", "PY-3", "department", "15958", CountryCode.PARAGUAY),
    PY_4("Guair\u0225", "PY-4", "department", "15967", CountryCode.PARAGUAY),
    PY_7("Itap\u0250a", "PY-7", "department", "15959", CountryCode.PARAGUAY),
    PY_8("Misiones", "PY-8", "department", "15968", CountryCode.PARAGUAY),
    PY_9("Paraguar\u0237", "PY-9", "department", "15951", CountryCode.PARAGUAY),
    PY_15("Presidente Hayes", "PY-15", "department", "15961", CountryCode.PARAGUAY),
    PY_2("San Pedro", "PY-2", "department", "15962", CountryCode.PARAGUAY),
    PY_12("\u0209eembuc\u0250", "PY-12", "department", "15960", CountryCode.PARAGUAY);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
