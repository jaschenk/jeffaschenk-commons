package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Cape Verde
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CV {
    /**
     * State Code Enumerator
     */
    CV_BV("Boa Vista","CV-BV","","12953", CountryCode.CAPE_VERDE),
    CV_BR("Brava","CV-BR","","12957",CountryCode.CAPE_VERDE),
    CV_CS("Calheta de S\u0227o Miguel","CV-CS","","19405",CountryCode.CAPE_VERDE),
    CV_MA("Maio","CV-MA","","12954",CountryCode.CAPE_VERDE),
    CV_MO("Mosteiros","CV-MO","","19406",CountryCode.CAPE_VERDE),
    CV_PA("Pa\u0250l","CV-PA","","19407",CountryCode.CAPE_VERDE),
    CV_PN("Porto Novo","CV-PN","","19408",CountryCode.CAPE_VERDE),
    CV_PR("Praia","CV-PR","","19409",CountryCode.CAPE_VERDE),
    CV_RG("Ribeira Grande","CV-RG","","12955",CountryCode.CAPE_VERDE),
    CV_SL("Sal","CV-SL","","12956",CountryCode.CAPE_VERDE),
    CV_CA("Santa Catarina","CV-CA","","19411",CountryCode.CAPE_VERDE),
    CV_CR("Santa Cruz","CV-CR","","19412",CountryCode.CAPE_VERDE),
    CV_SD("S\u0227o Domingos","CV-SD","","19413",CountryCode.CAPE_VERDE),
    CV_SF("S\u0227o Filipe","CV-SF","","19414",CountryCode.CAPE_VERDE),
    CV_SN("S\u0227o Nicolau","CV-SN","","12949",CountryCode.CAPE_VERDE),
    CV_SV("S\u0227o Vicente","CV-SV","","12950",CountryCode.CAPE_VERDE),
    CV_TA("Tarrafal","CV-TA","","19415",CountryCode.CAPE_VERDE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CV(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
