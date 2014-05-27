package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Bahrain
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BH {
    /**
     * State Code Enumerator
     */
    BH_14("Al Janubiyah","BH-14","Governorate","12592", CountryCode.BAHRAIN),
    BH_13("Al Manamah (Al 'Asimah)","BH-13","Governorate","12589",CountryCode.BAHRAIN),
    BH_15("Al Muharraq","BH-15","Governorate","12583",CountryCode.BAHRAIN),
    BH_16("Al Wust\u0225","BH-16","Governorate","12581",CountryCode.BAHRAIN),
    BH_17("Ash Shamaliyah","BH-17","Governorate","12582",CountryCode.BAHRAIN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BH(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
