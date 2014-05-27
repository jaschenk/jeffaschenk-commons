package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: The Democratic Republic of the Congo
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CD {
    /**
     * State Code Enumerator
     */
    CD_BN("Bandundu", "CD-BN", "province", "13094", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_BC("Bas-Congo", "CD-BC", "province", "13088", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_KW("Kasai-Occidental", "CD-KW", "province", "13092", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_KE("Kasai-Oriental", "CD-KE", "province", "13090", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_KA("Katanga", "CD-KA", "province", "13097", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_KN("Kinshasa", "CD-KN", "city", "13091", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_MA("Maniema", "CD-MA", "province", "13095", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_NK("Nord-Kivu", "CD-NK", "province", "13087", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_OR("Orientale", "CD-OR", "province", "13089", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_SK("Sud-Kivu", "CD-SK", "province", "13096", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO),
    CD_EQ("\u0233quateur", "CD-EQ", "province", "13093", CountryCode.THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CD(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
