package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Brazil
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BR {
    /**
     * State Code Enumerator
     */
    BR_AC("Acre","BR-AC","state","12793", CountryCode.BRAZIL),
    BR_AL("Alagoas","BR-AL","state","12784",CountryCode.BRAZIL),
    BR_AP("Amap\u0225","BR-AP","state","12775",CountryCode.BRAZIL),
    BR_AM("Amazonas","BR-AM","state","12794",CountryCode.BRAZIL),
    BR_BA("Bahia","BR-BA","state","12776",CountryCode.BRAZIL),
    BR_CE("Cear\u0225","BR-CE","state","12795",CountryCode.BRAZIL),
    BR_DF("Distrito Federal","BR-DF","federal district","12777",CountryCode.BRAZIL),
    BR_ES("Esp\u0237rito Santo","BR-ES","state","12778",CountryCode.BRAZIL),
    BR_GO("Goi\u0225s","BR-GO","state","12796",CountryCode.BRAZIL),
    BR_MA("Maranh\u0227o","BR-MA","state","12779",CountryCode.BRAZIL),
    BR_MT("Mato Grosso","BR-MT","state","12780",CountryCode.BRAZIL),
    BR_MS("Mato Grosso do Sul","BR-MS","state","12774",CountryCode.BRAZIL),
    BR_MG("Minas Gerais","BR-MG","state","12781",CountryCode.BRAZIL),
    BR_PR("Paran\u0225","BR-PR","state","12783",CountryCode.BRAZIL),
    BR_PB("Para\u0237ba","BR-PB","state","12782",CountryCode.BRAZIL),
    BR_PA("Par\u0225","BR-PA","state","12797",CountryCode.BRAZIL),
    BR_PE("Pernambuco","BR-PE","state","12773",CountryCode.BRAZIL),
    BR_PI("Piau\u0237","BR-PI","state","12785",CountryCode.BRAZIL),
    BR_RJ("Rio de Janeiro","BR-RJ","state","12786",CountryCode.BRAZIL),
    BR_RN("Rio Grande do Norte","BR-RN","state","12798",CountryCode.BRAZIL),
    BR_RS("Rio Grande do Sul","BR-RS","state","12787",CountryCode.BRAZIL),
    BR_RO("Rond\u0244nia","BR-RO","state","12772",CountryCode.BRAZIL),
    BR_RR("Roraima","BR-RR","state","12788",CountryCode.BRAZIL),
    BR_SC("Santa Catarina","BR-SC","state","12789",CountryCode.BRAZIL),
    BR_SE("Sergipe","BR-SE","state","12790",CountryCode.BRAZIL),
    BR_SP("S\u0227o Paulo","BR-SP","state","12771",CountryCode.BRAZIL),
    BR_TO("Tocantins","BR-TO","state","12791",CountryCode.BRAZIL);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
