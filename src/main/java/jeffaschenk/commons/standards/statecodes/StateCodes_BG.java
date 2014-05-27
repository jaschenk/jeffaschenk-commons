package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Bulgaria
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BG {
    /**
     * State Code Enumerator
     */
    BG_01("Blagoevgrad","BG-01","Region","12829", CountryCode.BULGARIA),
    BG_02("Burgas","BG-02","Region","12813",CountryCode.BULGARIA),
    BG_08("Dobrich","BG-08","Region","12830",CountryCode.BULGARIA),
    BG_07("Gabrovo","BG-07","Region","12823",CountryCode.BULGARIA),
    BG_26("Haskovo","BG-26","Region","12824",CountryCode.BULGARIA),
    BG_09("Kardzhali","BG-09","Region","12825",CountryCode.BULGARIA),
    BG_10("Kjustendil","BG-10","Region","12831",CountryCode.BULGARIA),
    BG_11("Lovech","BG-11","Region","12826",CountryCode.BULGARIA),
    BG_12("Montana","BG-12","Region","12811",CountryCode.BULGARIA),
    BG_13("Pazardzhik","BG-13","Region","12827",CountryCode.BULGARIA),
    BG_14("Pernik","BG-14","Region","12832",CountryCode.BULGARIA),
    BG_15("Pleven","BG-15","Region","12828",CountryCode.BULGARIA),
    BG_16("Plovdiv","BG-16","Region","12814",CountryCode.BULGARIA),
    BG_17("Razgrad","BG-17","Region","12810",CountryCode.BULGARIA),
    BG_18("Ruse","BG-18","Region","12815",CountryCode.BULGARIA),
    BG_19("Silistra","BG-19","Region","12833",CountryCode.BULGARIA),
    BG_20("Sliven","BG-20","Region","12816",CountryCode.BULGARIA),
    BG_21("Smolyan","BG-21","Region","12809",CountryCode.BULGARIA),
    BG_23("Sofia","BG-23","Region","12834",CountryCode.BULGARIA),
    BG_22("Sofia-Grad","BG-22","Region","12817",CountryCode.BULGARIA),
    BG_24("Stara Zagora","BG-24","Region","12818",CountryCode.BULGARIA),
    BG_25("Targovishte","BG-25","Region","12835",CountryCode.BULGARIA),
    BG_03("Varna","BG-03","Region","12820",CountryCode.BULGARIA),
    BG_04("Veliko Tarnovo","BG-04","Region","12808",CountryCode.BULGARIA),
    BG_05("Vidin","BG-05","Region","12821",CountryCode.BULGARIA),
    BG_06("Vratsa","BG-06","Region","12822",CountryCode.BULGARIA),
    BG_28("Yambol","BG-28","Region","12812",CountryCode.BULGARIA),
    BG_27("\u0352umen","BG-27","Region","12819",CountryCode.BULGARIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
