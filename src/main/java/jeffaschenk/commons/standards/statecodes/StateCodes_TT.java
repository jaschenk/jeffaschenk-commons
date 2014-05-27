package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Trinidad and Tobago
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TT {
    /**
     * State Code Enumerator
     */
    TT_ARI("Arima","TT-ARI","municipality","18446", CountryCode.TRINIDAD_AND_TOBAGO),
    TT_CHA("Chaguanas","TT-CHA","municipality","18456",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_CTT("Couva-Tabaquite-Talparo","TT-CTT","region","18447",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_DMN("Diego Martin","TT-DMN","region","18448",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_ETO("Eastern Tobago","TT-ETO","region","20483",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_PED("Penal-Debe","TT-PED","region","18449",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_PTF("Point Fortin","TT-PTF","municipality","18457",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_POS("Port of Spain","TT-POS","municipality","18450",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_PRT("Princes Town","TT-PRT","region","18445",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_RCM("Rio Claro-Mayaro","TT-RCM","region","18455",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_SFO("San Fernando","TT-SFO","municipality","18451",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_SJL("San Juan-Laventille","TT-SJL","region","18452",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_SGE("Sangre Grande","TT-SGE","region","18458",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_SIP("Siparia","TT-SIP","region","18453",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_TUP("Tunapuna-Piarco","TT-TUP","region","18454",CountryCode.TRINIDAD_AND_TOBAGO),
    TT_WTO("Western Tobago","TT-WTO","region","20488",CountryCode.TRINIDAD_AND_TOBAGO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TT(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
