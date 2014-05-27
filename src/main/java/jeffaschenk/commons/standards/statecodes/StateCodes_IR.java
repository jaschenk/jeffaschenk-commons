package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Iran
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_IR {
    /**
     * State Code Enumerator
     */
    IR_03("Ardabil","IR-03","Province","14748", CountryCode.IRAN),
    IR_02("Az\u0175arbayjan-e Gharbi","IR-02","Province","14756",CountryCode.IRAN),
    IR_01("Az\u0175arbayjan-e Sharqi","IR-01","Province","14749",CountryCode.IRAN),
    IR_06("Bushehr","IR-06","Province","14757",CountryCode.IRAN),
    IR_08("Chahar Mah\u0184all va Bakhtiari","IR-08","Province","14750",CountryCode.IRAN),
    IR_04("Esfahan","IR-04","Province","14758",CountryCode.IRAN),
    IR_14("Fars","IR-14","Province","14751",CountryCode.IRAN),
    IR_19("Gilan","IR-19","Province","14759",CountryCode.IRAN),
    IR_27("Golestan","IR-27","Province","14760",CountryCode.IRAN),
    IR_24("Hamadan","IR-24","Province","14752",CountryCode.IRAN),
    IR_23("Hormozgan","IR-23","Province","14761",CountryCode.IRAN),
    IR_05("Ilam","IR-05","Province","14753",CountryCode.IRAN),
    IR_15("Kerman","IR-15","Province","14762",CountryCode.IRAN),
    IR_17("Kermanshah","IR-17","Province","14739",CountryCode.IRAN),
    IR_09("Khorasan","IR-09","","14740",CountryCode.IRAN),
    IR_29("Khorasan-e Janubi","IR-29","Province","19012",CountryCode.IRAN),
    IR_30("Khorasan-e Razavi","IR-30","Province","19013",CountryCode.IRAN),
    IR_31("Khorasan-e Shemali","IR-31","Province","19014",CountryCode.IRAN),
    IR_10("Khuzestan","IR-10","Province","14741",CountryCode.IRAN),
    IR_18("Kohkiluyeh va Buyer Ahmad","IR-18","Province","14742",CountryCode.IRAN),
    IR_16("Kordestan","IR-16","Province","14743",CountryCode.IRAN),
    IR_20("Lorestan","IR-20","Province","14744",CountryCode.IRAN),
    IR_22("Markazi","IR-22","Province","14745",CountryCode.IRAN),
    IR_21("Mazandaran","IR-21","Province","14746",CountryCode.IRAN),
    IR_28("Qazvin","IR-28","Province","14747",CountryCode.IRAN),
    IR_26("Qom","IR-26","Province","14738",CountryCode.IRAN),
    IR_12("Semnan","IR-12","Province","14754",CountryCode.IRAN),
    IR_13("Sistan va Baluchestan","IR-13","Province","14764",CountryCode.IRAN),
    IR_07("Tehran","IR-07","Province","14755",CountryCode.IRAN),
    IR_25("Yazd","IR-25","Province","14763",CountryCode.IRAN),
    IR_11("Zanjan","IR-11","Province","14737",CountryCode.IRAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_IR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
