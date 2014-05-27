package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Tanzania
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TZ {
    /**
     * State Code Enumerator
     */
    TZ_01("Arusha","TZ-01","Region","18349", CountryCode.TANZANIA),
    TZ_02("Dar es Salaam","TZ-02","Region","18350",CountryCode.TANZANIA),
    TZ_03("Dodoma","TZ-03","Region","18351",CountryCode.TANZANIA),
    TZ_04("Iringa","TZ-04","Region","18352",CountryCode.TANZANIA),
    TZ_05("Kagera","TZ-05","Region","18338",CountryCode.TANZANIA),
    TZ_06("Kaskazini Pemba","TZ-06","Region","18333",CountryCode.TANZANIA),
    TZ_07("Kaskazini Unguja","TZ-07","Region","19095",CountryCode.TANZANIA),
    TZ_08("Kigoma","TZ-08","Region","18339",CountryCode.TANZANIA),
    TZ_09("Kilimanjaro","TZ-09","Region","18340",CountryCode.TANZANIA),
    TZ_10("Kusini Pemba","TZ-10","Region","19096",CountryCode.TANZANIA),
    TZ_11("Kusini Unguja","TZ-11","Region","19097",CountryCode.TANZANIA),
    TZ_12("Lindi","TZ-12","Region","18353",CountryCode.TANZANIA),
    TZ_26("Manyara","TZ-26","Region","18341",CountryCode.TANZANIA),
    TZ_13("Mara","TZ-13","Region","18342",CountryCode.TANZANIA),
    TZ_14("Mbeya","TZ-14","Region","18337",CountryCode.TANZANIA),
    TZ_15("Mjini Magharibi","TZ-15","Region","19098",CountryCode.TANZANIA),
    TZ_16("Morogoro","TZ-16","Region","18343",CountryCode.TANZANIA),
    TZ_17("Mtwara","TZ-17","Region","18354",CountryCode.TANZANIA),
    TZ_18("Mwanza","TZ-18","Region","18344",CountryCode.TANZANIA),
    TZ_19("Pwani","TZ-19","Region","18345",CountryCode.TANZANIA),
    TZ_20("Rukwa","TZ-20","Region","18336",CountryCode.TANZANIA),
    TZ_21("Ruvuma","TZ-21","Region","18346",CountryCode.TANZANIA),
    TZ_22("Shinyanga","TZ-22","Region","18347",CountryCode.TANZANIA),
    TZ_23("Singida","TZ-23","Region","18335",CountryCode.TANZANIA),
    TZ_24("Tabora","TZ-24","Region","18348",CountryCode.TANZANIA),
    TZ_25("Tanga","TZ-25","Region","18334",CountryCode.TANZANIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
