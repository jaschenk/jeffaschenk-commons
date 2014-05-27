package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Norway
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NO {
    /**
     * State Code Enumerator
     */
    NO_02("Akershus", "NO-02", "County", "15866", CountryCode.NORWAY),
    NO_09("Aust-Agder", "NO-09", "County", "15853", CountryCode.NORWAY),
    NO_06("Buskerud", "NO-06", "County", "15854", CountryCode.NORWAY),
    NO_20("Finnmark", "NO-20", "County", "15852", CountryCode.NORWAY),
    NO_04("Hedmark", "NO-04", "County", "15855", CountryCode.NORWAY),
    NO_12("Hordaland", "NO-12", "County", "15867", CountryCode.NORWAY),
    NO_22("Jan Mayen (Arctic Region)", "NO-22", "", "19038", CountryCode.NORWAY),
    NO_15("M\u0248re og Romsdal", "NO-15", "County", "15856", CountryCode.NORWAY),
    NO_17("Nord-Tr\u0248ndelag", "NO-17", "County", "15851", CountryCode.NORWAY),
    NO_18("Nordland", "NO-18", "County", "15857", CountryCode.NORWAY),
    NO_05("Oppland", "NO-05", "County", "15858", CountryCode.NORWAY),
    NO_03("Oslo", "NO-03", "County", "15859", CountryCode.NORWAY),
    NO_11("Rogaland", "NO-11", "County", "15860", CountryCode.NORWAY),
    NO_14("Sogn og Fjordane", "NO-14", "County", "15861", CountryCode.NORWAY),
    NO_21("Svalbard (Arctic Region)", "NO-21", "", "19039", CountryCode.NORWAY),
    NO_16("S\u0248r-Tr\u0248ndelag", "NO-16", "County", "15849", CountryCode.NORWAY),
    NO_08("Telemark", "NO-08", "County", "15862", CountryCode.NORWAY),
    NO_19("Troms", "NO-19", "County", "15863", CountryCode.NORWAY),
    NO_10("Vest-Agder", "NO-10", "County", "15848", CountryCode.NORWAY),
    NO_07("Vestfold", "NO-07", "County", "15864", CountryCode.NORWAY),
    NO_01("\u0248stfold", "NO-01", "County", "15850", CountryCode.NORWAY);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
