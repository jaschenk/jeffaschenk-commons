package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Namibia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NA {
    /**
     * State Code Enumerator
     */
    NA_CA("Caprivi","NA-CA","Region","15625", CountryCode.NAMIBIA),
    NA_ER("Erongo","NA-ER","Region","15626",CountryCode.NAMIBIA),
    NA_HA("Hardap","NA-HA","Region","15623",CountryCode.NAMIBIA),
    NA_KA("Karas","NA-KA","Region","15627",CountryCode.NAMIBIA),
    NA_KH("Khomas","NA-KH","Region","15624",CountryCode.NAMIBIA),
    NA_KU("Kunene","NA-KU","Region","15629",CountryCode.NAMIBIA),
    NA_OW("Ohangwena","NA-OW","Region","15630",CountryCode.NAMIBIA),
    NA_OK("Okavango","NA-OK","Region","15628",CountryCode.NAMIBIA),
    NA_OH("Omaheke","NA-OH","Region","15622",CountryCode.NAMIBIA),
    NA_OS("Omusati","NA-OS","Region","15631",CountryCode.NAMIBIA),
    NA_ON("Oshana","NA-ON","Region","15632",CountryCode.NAMIBIA),
    NA_OT("Oshikoto","NA-OT","Region","15621",CountryCode.NAMIBIA),
    NA_OD("Otjozondjupa","NA-OD","Region","15633",CountryCode.NAMIBIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
