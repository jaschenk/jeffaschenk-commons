package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Ireland
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_IE {
    /**
     * State Code Enumerator
     */
    IE_CW("Carlow","IE-CW","County","14798", CountryCode.IRELAND),
    IE_CN("Cavan","IE-CN","County","14795",CountryCode.IRELAND),
    IE_CE("Clare","IE-CE","County","14799",CountryCode.IRELAND),
    IE_C("Cork","IE-C","County","14800",CountryCode.IRELAND),
    IE_DL("Donegal","IE-DL","County","14796",CountryCode.IRELAND),
    IE_D("Dublin","IE-D","County","14801",CountryCode.IRELAND),
    IE_G("Galway","IE-G","County","14797",CountryCode.IRELAND),
    IE_KY("Kerry","IE-KY","County","14802",CountryCode.IRELAND),
    IE_KE("Kildare","IE-KE","County","14788",CountryCode.IRELAND),
    IE_KK("Kilkenny","IE-KK","County","14803",CountryCode.IRELAND),
    IE_LS("Laois","IE-LS","County","14807",CountryCode.IRELAND),
    IE_LM("Leitrim","IE-LM","County","14804",CountryCode.IRELAND),
    IE_LK("Limerick","IE-LK","County","14805",CountryCode.IRELAND),
    IE_LD("Longford","IE-LD","County","14787",CountryCode.IRELAND),
    IE_LH("Louth","IE-LH","County","14806",CountryCode.IRELAND),
    IE_MO("Mayo","IE-MO","County","14808",CountryCode.IRELAND),
    IE_MH("Meath","IE-MH","County","14789",CountryCode.IRELAND),
    IE_MN("Monaghan","IE-MN","County","14786",CountryCode.IRELAND),
    IE_OY("Offaly","IE-OY","County","14790",CountryCode.IRELAND),
    IE_RN("Roscommon","IE-RN","County","14809",CountryCode.IRELAND),
    IE_SO("Sligo","IE-SO","County","14791",CountryCode.IRELAND),
    IE_TA("Tipperary","IE-TA","County","14792",CountryCode.IRELAND),
    IE_WD("Waterford","IE-WD","County","14784",CountryCode.IRELAND),
    IE_WH("Westmeath","IE-WH","County","14793",CountryCode.IRELAND),
    IE_WX("Wexford","IE-WX","County","14783",CountryCode.IRELAND),
    IE_WW("Wicklow","IE-WW","County","14794",CountryCode.IRELAND);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_IE(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
