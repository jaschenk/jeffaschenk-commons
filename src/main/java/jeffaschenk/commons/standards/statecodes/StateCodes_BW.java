package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Botswana
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BW {
    /**
     * State Code Enumerator
     */
    BW_CE("Central","BW-CE","District","19401", CountryCode.BOTSWANA),
    BW_GH("Ghanzi","BW-GH","District","12759",CountryCode.BOTSWANA),
    BW_KG("Kgalagadi","BW-KG","District","19402",CountryCode.BOTSWANA),
    BW_KL("Kgatleng","BW-KL","District","12770",CountryCode.BOTSWANA),
    BW_KW("Kweneng","BW-KW","District","12762",CountryCode.BOTSWANA),
    BW_NE("North-East","BW-NE","District","12749",CountryCode.BOTSWANA),
    BW_NW("North-West","BW-NW","District","19403",CountryCode.BOTSWANA),
    BW_SE("South-East","BW-SE","District","12751",CountryCode.BOTSWANA),
    BW_SO("Southern","BW-SO","District","19404",CountryCode.BOTSWANA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BW(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
