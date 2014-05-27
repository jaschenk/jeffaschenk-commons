package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Sudan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SD {
    /**
     * State Code Enumerator
     */
    SD_23("Ali an Nil","SD-23","State","18194", CountryCode.SUDAN),
    SD_26("Red Sea","SD-26","State","18206",CountryCode.SUDAN),
    SD_18("Bihr","SD-18","State","18207",CountryCode.SUDAN),
    SD_07("Al Jazirah","SD-07","State","18193",CountryCode.SUDAN),
    SD_03("Al Khartum","SD-03","State","18197",CountryCode.SUDAN),
    SD_06("Qadrif","SD-06","State","18189",CountryCode.SUDAN),     
    SD_22("Ittihd,","SD-22","State","18187",CountryCode.SUDAN),
    SD_04("An Nil","SD-04","State","18200",CountryCode.SUDAN),
    SD_08("An Nil al Abya","SD-08","State","18192",CountryCode.SUDAN),
    SD_24("An Nil al Azraq","SD-24","State","18201",CountryCode.SUDAN),
    SD_01("Ash Shamaliyah","SD-01","State","18190",CountryCode.SUDAN),
    SD_17("Bahr al Jabal","SD-17","State","18195",CountryCode.SUDAN),     
    SD_16("Gharb al Istiwa'iyah","SD-16","State","18208",CountryCode.SUDAN),
    SD_14("West Bahr al Ghazal","SD-14","State","18196",CountryCode.SUDAN),    
    SD_12("Gharb Darfur","SD-12","State","18209",CountryCode.SUDAN),
    SD_10("Gharb Kurdufan","SD-10","","18210",CountryCode.SUDAN),
    SD_11("Janub Darfur","SD-11","State","18211",CountryCode.SUDAN),
    SD_13("Janub Kurdufan","SD-13","State","18198",CountryCode.SUDAN),
    SD_20("Junqali","SD-20","State","18199",CountryCode.SUDAN),
    SD_05("Kassala","SD-05","State","18212",CountryCode.SUDAN),
    SD_15("North Bahr al Ghazal","SD-15","State","18188",CountryCode.SUDAN),   
    SD_02("Shamal Darfur","SD-02","State","18202",CountryCode.SUDAN),
    SD_09("Shamal Kurdufan","SD-09","State","18203",CountryCode.SUDAN),
    SD_19("Sharq al Istiwa'iyah","SD-19","State","18191",CountryCode.SUDAN),
    SD_25("Sinnar","SD-25","State","18204",CountryCode.SUDAN),
    SD_21("Warab","SD-21","State","18205",CountryCode.SUDAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SD(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
