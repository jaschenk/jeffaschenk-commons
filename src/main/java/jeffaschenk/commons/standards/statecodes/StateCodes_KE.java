package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Kenya
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KE {
    /**
     * State Code Enumerator
     */
    KE_200("Central","KE-200","province","15009", CountryCode.KENYA),
    KE_300("Coast","KE-300","province","15005",CountryCode.KENYA),
    KE_400("Eastern","KE-400","province","15006",CountryCode.KENYA),
    KE_110("Nairobi Municipality","KE-110","municipality","15010",CountryCode.KENYA),
    KE_500("North-Eastern","KE-500","province","15004",CountryCode.KENYA),
    KE_600("Nyanza","KE-600","province","15001",CountryCode.KENYA),
    KE_700("Rift Valley","KE-700","province","15007",CountryCode.KENYA),
    KE_900("Western","KE-900","province","15003",CountryCode.KENYA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
