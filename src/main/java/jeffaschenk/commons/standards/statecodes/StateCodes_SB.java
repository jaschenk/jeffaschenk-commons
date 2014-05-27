package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Solomon Islands
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SB {
    /**
     * State Code Enumerator
     */
    SB_CT("Capital Territory (Honiara)","SB-CT","capital territory","18079", CountryCode.SOLOMON_ISLANDS),
    SB_CE("Central","SB-CE","province","18074",CountryCode.SOLOMON_ISLANDS),
    SB_CH("Choiseul","SB-CH","province","18075",CountryCode.SOLOMON_ISLANDS),
    SB_GU("Guadalcanal","SB-GU","province","18076",CountryCode.SOLOMON_ISLANDS),
    SB_IS("Isabel","SB-IS","province","18081",CountryCode.SOLOMON_ISLANDS),
    SB_MK("Makira","SB-MK","province","18080",CountryCode.SOLOMON_ISLANDS),
    SB_ML("Malaita","SB-ML","province","18082",CountryCode.SOLOMON_ISLANDS),
    SB_RB("Rennell and Bellona","SB-RB","province","18078",CountryCode.SOLOMON_ISLANDS),
    SB_TE("Temotu","SB-TE","province","18073",CountryCode.SOLOMON_ISLANDS),
    SB_WE("Western","SB-WE","province","18077",CountryCode.SOLOMON_ISLANDS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SB(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
