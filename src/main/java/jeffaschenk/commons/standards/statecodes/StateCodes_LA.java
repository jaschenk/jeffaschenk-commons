package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Lao Peoples Democratic Republic
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LA {
    /**
     * State Code Enumerator
     */
    LA_AT("Attapu [Attopeu]","LA-AT","province","15080", CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_BK("Bok\u0232o","LA-BK","province","15081",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_BL("Bolikhamxai [Borikhane]","LA-BL","province","15089",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_CH("Champasak [Champassak]","LA-CH","province","15082",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_HO("Houaphan","LA-HO","province","15093",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_KH("Khammouan","LA-KH","province","15094",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_LM("Louang Namtha","LA-LM","province","15083",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_LP("Louangphabang [Louang Prabang]","LA-LP","province","15092",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_OU("Oud\u0244mxai [Oudomsai]","LA-OU","province","15084",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_PH("Ph\u0244ngsali [Phong Saly]","LA-PH","province","15085",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_SL("Salavan [Saravane]","LA-SL","province","15091",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_SV("Savannakh\u0233t","LA-SV","province","15086",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_VI("Vientiane","LA-VI","province","15095",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_VT("Vientiane Prefecture","LA-VT","prefecture","15087",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_XA("Xaignabouli [Sayaboury]","LA-XA","province","15088",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_XN("Xais\u0244mboun","LA-XN","special zone","15096",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_XI("Xiangkhoang [Xieng Khouang]","LA-XI","province","15079",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC),
        LA_XE("X\u0233kong [S\u0233kong]","LA-XE","province","15090",CountryCode.LAO_PEOPLES_DEMOCRATIC_REPUBLIC);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
