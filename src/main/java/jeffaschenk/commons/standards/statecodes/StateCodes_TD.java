package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Chad
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TD {
    /**
     * State Code Enumerator
     */
    TD_BA("Batha","TD-BA","Region","12981", CountryCode.CHAD),
    TD_BET("Borkou-Ennedi-Tibesti","TD-BET","Region","12982",CountryCode.CHAD),
    TD_CB("Chari-Baguirmi","TD-CB","Region","12990",CountryCode.CHAD),
    TD_GR("Gu\u0233ra","TD-GR","Region","12983",CountryCode.CHAD),
    TD_HL("Hadjer Lamis","TD-HL","Region","19416",CountryCode.CHAD),
    TD_KA("Kanem","TD-KA","Region","12984",CountryCode.CHAD),
    TD_LC("Lac","TD-LC","Region","12985",CountryCode.CHAD),
    TD_LO("Logone-Occidental","TD-LO","Region","12986",CountryCode.CHAD),
    TD_LR("Logone-Oriental","TD-LR","Region","12987",CountryCode.CHAD),
    TD_MA("Mandoul","TD-MA","Region","19417",CountryCode.CHAD),
    TD_ME("Mayo-K\u0233bbi-Est","TD-ME","Region","19418",CountryCode.CHAD),
    TD_MO("Mayo-K\u0233bbi-Ouest","TD-MO","Region","19419",CountryCode.CHAD),
    TD_MC("Moyen-Chari","TD-MC","Region","12991",CountryCode.CHAD),
    TD_ND("Ndjamena","TD-ND","Region","19420",CountryCode.CHAD),
    TD_OD("Ouadda\u0239","TD-OD","Region","12988",CountryCode.CHAD),
    TD_SA("Salamat","TD-SA","Region","12992",CountryCode.CHAD),
    TD_TA("Tandjil\u0233","TD-TA","Region","12980",CountryCode.CHAD),
    TD_WF("Wadi Fira","TD-WF","Region","19421",CountryCode.CHAD);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TD(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
