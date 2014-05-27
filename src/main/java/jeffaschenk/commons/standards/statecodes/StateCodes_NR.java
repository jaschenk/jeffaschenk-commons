package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Nauru
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NR {
    /**
     * State Code Enumerator
     */
    NR_01("Aiwo","NR-01","District","15642", CountryCode.NAURU),
    NR_02("Anabar","NR-02","District","15637",CountryCode.NAURU),
    NR_03("Anetan","NR-03","District","15643",CountryCode.NAURU),
    NR_04("Anibare","NR-04","District","15641",CountryCode.NAURU),
    NR_05("Baiti","NR-05","District","15647",CountryCode.NAURU),
    NR_06("Boe","NR-06","District","15644",CountryCode.NAURU),
    NR_07("Buada","NR-07","District","15640",CountryCode.NAURU),
    NR_08("Denigomodu","NR-08","District","15648",CountryCode.NAURU),
    NR_09("Ewa","NR-09","District","15645",CountryCode.NAURU),
    NR_10("Ijuw","NR-10","District","15639",CountryCode.NAURU),
    NR_11("Meneng","NR-11","District","15646",CountryCode.NAURU),
    NR_12("Nibok","NR-12","District","15636",CountryCode.NAURU),
    NR_13("Uaboe","NR-13","District","15638",CountryCode.NAURU),
    NR_14("Yaren","NR-14","District","15635",CountryCode.NAURU);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
