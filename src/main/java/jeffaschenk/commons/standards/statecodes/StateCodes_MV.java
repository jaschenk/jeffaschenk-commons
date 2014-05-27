package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Maldives
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MV {
    /**
     * State Code Enumerator
     */
    MV_02("Alif","MV-02","administrative atoll","15307", CountryCode.MALDIVES),
    MV_X1("Alif Dhaal","MV-X1","","15308",CountryCode.MALDIVES),
    MV_20("Baa","MV-20","administrative atoll","15309",CountryCode.MALDIVES),
    MV_17("Dhaalu","MV-17","administrative atoll","15310",CountryCode.MALDIVES),
    MV_14("Faafu","MV-14","administrative atoll","15311",CountryCode.MALDIVES),
    MV_27("Gaaf Alif","MV-27","administrative atoll","15312",CountryCode.MALDIVES),
    MV_28("Gaafu Dhaalu","MV-28","administrative atoll","15313",CountryCode.MALDIVES),
    MV_29("Gnaviyani","MV-29","administrative atoll","15306",CountryCode.MALDIVES),
    MV_07("Haa Alif","MV-07","administrative atoll","15314",CountryCode.MALDIVES),
    MV_23("Haa Dhaalu","MV-23","administrative atoll","15315",CountryCode.MALDIVES),
    MV_26("Kaafu","MV-26","administrative atoll","15305",CountryCode.MALDIVES),
    MV_05("Laamu","MV-05","administrative atoll","15316",CountryCode.MALDIVES),
    MV_03("Lhaviyani","MV-03","administrative atoll","15321",CountryCode.MALDIVES),
    MV_MLE("Male","MV-MLE","capital","15317",CountryCode.MALDIVES),
    MV_12("Meemu","MV-12","administrative atoll","15324",CountryCode.MALDIVES),
    MV_25("Noonu","MV-25","administrative atoll","15323",CountryCode.MALDIVES),
    MV_13("Raa","MV-13","administrative atoll","15318",CountryCode.MALDIVES),
    MV_01("Seenu","MV-01","administrative atoll","15319",CountryCode.MALDIVES),
    MV_24("Shaviyani","MV-24","administrative atoll","15322",CountryCode.MALDIVES),
    MV_08("Thaa","MV-08","administrative atoll","15304",CountryCode.MALDIVES),
    MV_04("Vaavu","MV-04","administrative atoll","15320",CountryCode.MALDIVES);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MV(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
