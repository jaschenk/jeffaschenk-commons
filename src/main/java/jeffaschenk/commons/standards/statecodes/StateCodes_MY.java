package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Malaysia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MY {
    /**
     * State Code Enumerator
     */
    MY_01("Johor","MY-01","state","15290", CountryCode.MALAYSIA),
    MY_02("Kedah","MY-02","state","15299",CountryCode.MALAYSIA),
    MY_03("Kelantan","MY-03","state","15291",CountryCode.MALAYSIA),
    MY_04("Melaka","MY-04","state","15293",CountryCode.MALAYSIA),
    MY_05("Negeri Sembilan","MY-05","state","15301",CountryCode.MALAYSIA),
    MY_06("Pahang","MY-06","state","15294",CountryCode.MALAYSIA),
    MY_08("Perak","MY-08","state","15295",CountryCode.MALAYSIA),
    MY_09("Perlis","MY-09","state","15302",CountryCode.MALAYSIA),
    MY_07("Pulau Pinang","MY-07","state","15296",CountryCode.MALAYSIA),
    MY_12("Sabah","MY-12","state","15297",CountryCode.MALAYSIA),
    MY_13("Sarawak","MY-13","state","15303",CountryCode.MALAYSIA),
    MY_10("Selangor","MY-10","state","15298",CountryCode.MALAYSIA),
    MY_11("Terengganu","MY-11","state","15289",CountryCode.MALAYSIA),
    MY_14("Wilayah Persekutuan Kuala Lumpur","MY-14","federal territory","15300",CountryCode.MALAYSIA),
    MY_15("Wilayah Persekutuan Labuan","MY-15","federal territory","15292",CountryCode.MALAYSIA),
    MY_16("Wilayah Persekutuan Putrajaya","MY-16","federal territory","19025",CountryCode.MALAYSIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
