package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Mauritania
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MR {
    /**
     * State Code Enumerator
     */
    MR_07("Adrar","MR-07","region","15379", CountryCode.MAURITANIA),
    MR_03("Assaba","MR-03","region","15378",CountryCode.MAURITANIA),
    MR_05("Brakna","MR-05","region","15384",CountryCode.MAURITANIA),
    MR_08("Dakhlet Nou\u0224dhibou","MR-08","region","15380",CountryCode.MAURITANIA),
    MR_04("Gorgol","MR-04","region","15387",CountryCode.MAURITANIA),
    MR_10("Guidimaka","MR-10","region","15388",CountryCode.MAURITANIA),
    MR_01("Hodh ech Chargui","MR-01","region","15386",CountryCode.MAURITANIA),
    MR_02("Hodh el Gharbi","MR-02","region","15381",CountryCode.MAURITANIA),
    MR_12("Inchiri","MR-12","region","15382",CountryCode.MAURITANIA),
    MR_NKC("Nouakchott","MR-NKC","district","15389",CountryCode.MAURITANIA),
    MR_09("Tagant","MR-09","region","15385",CountryCode.MAURITANIA),
    MR_11("Tiris Zemmour","MR-11","region","15383",CountryCode.MAURITANIA),
    MR_06("Trarza","MR-06","region","15377",CountryCode.MAURITANIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
