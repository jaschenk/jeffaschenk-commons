package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Cote D Ivoire
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CI {
    /**
     * State Code Enumerator
     */
    CI_06("18 Montagnes (R\u0233gion des)","CI-06","Region","18974", CountryCode.COTE_D_IVOIRE),
    CI_16("Agn\u0233bi (R\u0233gion de l')","CI-16","Region","18975",CountryCode.COTE_D_IVOIRE),
    CI_17("Bafing (R\u0233gion du)","CI-17","Region","18976",CountryCode.COTE_D_IVOIRE),
    CI_09("Bas-Sassandra (R\u0233gion du)","CI-09","Region","18977",CountryCode.COTE_D_IVOIRE),
    CI_10("Dengu\u0233l\u0233 (R\u0233gion du)","CI-10","Region","18978",CountryCode.COTE_D_IVOIRE),
    CI_18("Fromager (R\u0233gion du)","CI-18","Region","18979",CountryCode.COTE_D_IVOIRE),
    CI_02("Haut-Sassandra (R\u0233gion du)","CI-02","Region","18980",CountryCode.COTE_D_IVOIRE),
    CI_07("Lacs (R\u0233gion des)","CI-07","Region","18981",CountryCode.COTE_D_IVOIRE),
    CI_01("Lagunes (R\u0233gion des)","CI-01","Region","18982",CountryCode.COTE_D_IVOIRE),
    CI_12("Marahou\u0233 (R\u0233gion de la)","CI-12","Region","18983",CountryCode.COTE_D_IVOIRE),
    CI_19("Moyen-Cavally (R\u0233gion du)","CI-19","Region","18984",CountryCode.COTE_D_IVOIRE),
    CI_05("Moyen-Como\u0233 (R\u0233gion du)","CI-05","Region","18985",CountryCode.COTE_D_IVOIRE),
    CI_11("Nzi-Como\u0233 (R\u0233gion)","CI-11","Region","18986",CountryCode.COTE_D_IVOIRE),
    CI_03("Savanes (R\u0233gion des)","CI-03","Region","18987",CountryCode.COTE_D_IVOIRE),
    CI_15("Sud-Bandama (R\u0233gion du)","CI-15","Region","18988",CountryCode.COTE_D_IVOIRE),
    CI_13("Sud-Como\u0233 (R\u0233gion du)","CI-13","Region","18989",CountryCode.COTE_D_IVOIRE),
    CI_04("Vall\u0233e du Bandama (R\u0233gion de la)","CI-04","Region","18990",CountryCode.COTE_D_IVOIRE),
    CI_14("Worodougou (R\u0233gion du)","CI-14","Region","18991",CountryCode.COTE_D_IVOIRE),
    CI_08("Zanzan (R\u0233gion du)","CI-08","Region","18992",CountryCode.COTE_D_IVOIRE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CI(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
