package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Sri Lanka
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LK {
    /**
     * State Code Enumerator
     */
    LK_52("Amp\u0257ra", "LK-52", "district", "18178", CountryCode.SRI_LANKA),
    LK_71("Anur\u0257dhapura", "LK-71", "district", "18175", CountryCode.SRI_LANKA),
    LK_81("Badulla", "LK-81", "district", "18179", CountryCode.SRI_LANKA),
    LK_12("Gampaha", "LK-12", "district", "18176", CountryCode.SRI_LANKA),
    LK_31("G\u0257lla", "LK-31", "district", "18180", CountryCode.SRI_LANKA),
    LK_33("Hambant\u0335?a", "LK-33", "district", "18181", CountryCode.SRI_LANKA),
    LK_13("Kalutara", "LK-13", "district", "18177", CountryCode.SRI_LANKA),
    LK_92("Kegalla", "LK-92", "district", "18182", CountryCode.SRI_LANKA),
    LK_42("Kilin\u0335chchi", "LK-42", "district", "18168", CountryCode.SRI_LANKA),
    LK_61("Kuru?\u0230gala", "LK-61", "district", "18186", CountryCode.SRI_LANKA),
    LK_11("K\u0335?amba", "LK-11", "district", "18183", CountryCode.SRI_LANKA),
    LK_51("Madakalapuva", "LK-51", "district", "18184", CountryCode.SRI_LANKA),
    LK_21("Mahanuvara", "LK-21", "district", "18167", CountryCode.SRI_LANKA),
    LK_43("Mann\u0257rama", "LK-43", "district", "18185", CountryCode.SRI_LANKA),
    LK_45("Mulativ", "LK-45", "district", "18163", CountryCode.SRI_LANKA),
    LK_22("M\u0257tale", "LK-22", "district", "18169", CountryCode.SRI_LANKA),
    LK_32("M\u0257tara", "LK-32", "district", "18164", CountryCode.SRI_LANKA),
    LK_82("M\u0335?ar\u0257gala", "LK-82", "district", "18170", CountryCode.SRI_LANKA),
    LK_23("Nuvara \u0276liya", "LK-23", "district", "18171", CountryCode.SRI_LANKA),
    LK_62("Puttalama", "LK-62", "district", "18172", CountryCode.SRI_LANKA),
    LK_72("P\u0335?\u0335nnaruva", "LK-72", "district", "18165", CountryCode.SRI_LANKA),
    LK_91("Ratnapura", "LK-91", "district", "18166", CountryCode.SRI_LANKA),
    LK_53("Triku?\u0257malaya", "LK-53", "district", "18173", CountryCode.SRI_LANKA),
    LK_44("Vavuniy\u0257va", "LK-44", "district", "18162", CountryCode.SRI_LANKA),
    LK_41("Y\u0257panaya", "LK-41", "district", "18174", CountryCode.SRI_LANKA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LK(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
