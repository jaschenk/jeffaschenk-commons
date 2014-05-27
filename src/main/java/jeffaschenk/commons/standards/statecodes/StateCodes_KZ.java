package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Kazakhstan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KZ {
    /**
     * State Code Enumerator
     */
    KZ_ALA("Almaty","KZ-ALA","city","14993", CountryCode.KAZAKHSTAN),
    KZ_ALM("Almaty oblysy","KZ-ALM","region","20264",CountryCode.KAZAKHSTAN),
    KZ_AKM("Aqmola oblysy","KZ-AKM","region","14991",CountryCode.KAZAKHSTAN),
    KZ_AKT("Aqt\u0246be oblysy","KZ-AKT","region","14992",CountryCode.KAZAKHSTAN),
    KZ_AST("Astana","KZ-AST","city","20265",CountryCode.KAZAKHSTAN),
    KZ_ATY("Atyrau oblysy","KZ-ATY","region","14994",CountryCode.KAZAKHSTAN),
    KZ_ZAP("Batys Qazaqstan oblysy","KZ-ZAP","region","14988",CountryCode.KAZAKHSTAN),
    KZ_BAY("Bayqongyr","KZ-BAY","city","21376",CountryCode.KAZAKHSTAN),
    KZ_MAN("Mangghystau oblysy","KZ-MAN","region","14995",CountryCode.KAZAKHSTAN),
    KZ_YUZ("Ongt\u0252stik Qazaqstan oblysy","KZ-YUZ","region","14999",CountryCode.KAZAKHSTAN),
    KZ_PAV("Pavlodar oblysy","KZ-PAV","region","14998",CountryCode.KAZAKHSTAN),
    KZ_KAR("Qaraghandy oblysy","KZ-KAR","region","14997",CountryCode.KAZAKHSTAN),
    KZ_KUS("Qostanay oblysy","KZ-KUS","region","15000",CountryCode.KAZAKHSTAN),
    KZ_KZY("Qyzylorda oblysy","KZ-KZY","region","14990",CountryCode.KAZAKHSTAN),
    KZ_VOS("Shyghys Qazaqstan oblysy","KZ-VOS","region","14996",CountryCode.KAZAKHSTAN),
    KZ_SEV("Solt\u0252stik Qazaqstan oblysy","KZ-SEV","region","14989",CountryCode.KAZAKHSTAN),
    KZ_ZHA("Zhambyl oblysy","KZ-ZHA","region","14987",CountryCode.KAZAKHSTAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
