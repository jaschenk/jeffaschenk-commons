package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Armenia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AM {
    /**
     * State Code Enumerator
     */
    AM_AG("Aragatsotn","AM-AG","Province","12466", CountryCode.ARMENIA),
    AM_AR("Ararat","AM-AR","Province","12461",CountryCode.ARMENIA),
    AM_AV("Armavir","AM-AV","Province","12467",CountryCode.ARMENIA),
    AM_ER("Erevan","AM-ER","city","12464",CountryCode.ARMENIA),
    AM_GR("Gegark'unik'","AM-GR","Province","12460",CountryCode.ARMENIA),
    AM_KT("Kotayk'","AM-KT","Province","12462",CountryCode.ARMENIA),
    AM_LO("Lori","AM-LO","Province","12465",CountryCode.ARMENIA),
    AM_SU("Syunik'","AM-SU","Province","12463",CountryCode.ARMENIA),
    AM_TV("Tavu\u0353","AM-TV","Province","12457",CountryCode.ARMENIA),
    AM_VD("Vayoc Jor","AM-VD","Province","12458",CountryCode.ARMENIA),
    AM_SH("\u0352irak","AM-SH","Province","12459",CountryCode.ARMENIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
