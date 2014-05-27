package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Central African Republic
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CF {
    /**
     * State Code Enumerator
     */
    CF_BB("Bamingui-Bangoran","CF-BB","prefecture","12972", CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_BGF("Bangui","CF-BGF","capital","12969",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_BK("Basse-Kotto","CF-BK","prefecture","12973",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_HM("Haut-Mbomou","CF-HM","prefecture","12974",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_HK("Haute-Kotto","CF-HK","prefecture","12968",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_KG("K\u0233mo","CF-KG","prefecture","12967",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_LB("Lobaye","CF-LB","prefecture","12975",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_HS("Mamb\u0233r\u0233-Kad\u0233\u0239","CF-HS","prefecture","12966",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_MB("Mbomou","CF-MB","prefecture","12976",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_KB("Nana-Gr\u0233bizi","CF-KB","prefecture","12965",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_NM("Nana-Mamb\u0233r\u0233","CF-NM","prefecture","12977",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_MP("Ombella-Mpoko","CF-MP","prefecture","12964",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_UK("Ouaka","CF-UK","prefecture","12979",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_AC("Ouham","CF-AC","prefecture","12978",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_OP("Ouham-Pend\u0233","CF-OP","prefecture","12970",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_SE("Sangha-Mba\u0233r\u0233","CF-SE","prefecture","12963",CountryCode.CENTRAL_AFRICAN_REPUBLIC),
    CF_VK("Vakaga","CF-VK","prefecture","12971",CountryCode.CENTRAL_AFRICAN_REPUBLIC);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CF(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
