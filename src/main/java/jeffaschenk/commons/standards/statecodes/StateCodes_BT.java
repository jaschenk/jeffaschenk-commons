package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Bhutan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BT {
    /**
     * State Code Enumerator
     */
    BT_33("Bumthang","BT-33","District","12724", CountryCode.BHUTAN),
    BT_12("Chhukha","BT-12","District","12735",CountryCode.BHUTAN),
    BT_22("Dagana","BT-22","District","12723",CountryCode.BHUTAN),
    BT_GA("Gasa","BT-GA","District","12733",CountryCode.BHUTAN),
    BT_13("Ha","BT-13","District","12736",CountryCode.BHUTAN),
    BT_44("Lhuentse","BT-44","District","12727",CountryCode.BHUTAN),
    BT_42("Monggar","BT-42","District","12722",CountryCode.BHUTAN),
    BT_11("Paro","BT-11","District","12729",CountryCode.BHUTAN),
    BT_43("Pemagatshel","BT-43","District","12728",CountryCode.BHUTAN),
    BT_23("Punakha","BT-23","District","12719",CountryCode.BHUTAN),
    BT_45("Samdrup Jongkha","BT-45","District","12730",CountryCode.BHUTAN),
    BT_14("Samtse","BT-14","District","12718",CountryCode.BHUTAN),
    BT_31("Sarpang","BT-31","District","12726",CountryCode.BHUTAN),
    BT_15("Thimphu","BT-15","District","12731",CountryCode.BHUTAN),
    BT_TY("Trashi Yangtse","BT-TY","District","12734",CountryCode.BHUTAN),
    BT_41("Trashigang","BT-41","District","12721",CountryCode.BHUTAN),
    BT_32("Trongsa","BT-32","District","12717",CountryCode.BHUTAN),
    BT_21("Tsirang","BT-21","District","12725",CountryCode.BHUTAN),
    BT_24("Wangdue Phodrang","BT-24","District","12732",CountryCode.BHUTAN),
    BT_34("Zhemgang","BT-34","District","12720",CountryCode.BHUTAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BT(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
