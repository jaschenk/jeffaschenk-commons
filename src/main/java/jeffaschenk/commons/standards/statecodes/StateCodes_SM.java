package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: San Marino
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SM {
    /**
     * State Code Enumerator
     */
    SM_01("Acquaviva", "SM-01", "Municipality", "17972", CountryCode.SAN_MARINO),
    SM_06("Borgo Maggiore", "SM-06", "Municipality", "17971", CountryCode.SAN_MARINO),
    SM_02("Chiesanuova", "SM-02", "Municipality", "17973", CountryCode.SAN_MARINO),
    SM_03("Domagnano", "SM-03", "Municipality", "17970", CountryCode.SAN_MARINO),
    SM_04("Faetano", "SM-04", "Municipality", "17974", CountryCode.SAN_MARINO),
    SM_05("Fiorentino", "SM-05", "Municipality", "17969", CountryCode.SAN_MARINO),
    SM_08("Montegiardino", "SM-08", "Municipality", "17975", CountryCode.SAN_MARINO),
    SM_07("San Marino", "SM-07", "Municipality", "17976", CountryCode.SAN_MARINO),
    SM_09("Serravalle", "SM-09", "Municipality", "17968", CountryCode.SAN_MARINO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SM(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
