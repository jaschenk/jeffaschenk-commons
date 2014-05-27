package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Zambia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ZM {
    /**
     * State Code Enumerator
     */
    ZM_02("Central","ZM-02","Province","20690", CountryCode.ZAMBIA),
    ZM_08("Copperbelt","ZM-08","Province","18800",CountryCode.ZAMBIA),
    ZM_03("Eastern","ZM-03","Province","21381",CountryCode.ZAMBIA),
    ZM_04("Luapula","ZM-04","Province","18803",CountryCode.ZAMBIA),
    ZM_09("Lusaka","ZM-09","Province","18801",CountryCode.ZAMBIA),
    ZM_06("North-Western","ZM-06","Province","18804",CountryCode.ZAMBIA),
    ZM_05("Northern","ZM-05","Province","18798",CountryCode.ZAMBIA),
    ZM_07("Southern","ZM-07","Province","18802",CountryCode.ZAMBIA),
    ZM_01("Western","ZM-01","Province","18796",CountryCode.ZAMBIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ZM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
