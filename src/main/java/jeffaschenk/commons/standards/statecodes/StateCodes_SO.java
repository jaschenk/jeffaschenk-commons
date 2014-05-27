package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Somalia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SO {
    /**
     * State Code Enumerator
     */
    SO_AW("Awdal", "SO-AW", "Region", "18093", CountryCode.SOMALIA),
    SO_BK("Bakool", "SO-BK", "Region", "18094", CountryCode.SOMALIA),
    SO_BN("Banaadir", "SO-BN", "Region", "18098", CountryCode.SOMALIA),
    SO_BR("Bari", "SO-BR", "Region", "18095", CountryCode.SOMALIA),
    SO_BY("Bay", "SO-BY", "Region", "18097", CountryCode.SOMALIA),
    SO_GA("Galguduud", "SO-GA", "Region", "18084", CountryCode.SOMALIA),
    SO_GE("Gedo", "SO-GE", "Region", "18085", CountryCode.SOMALIA),
    SO_HI("Hiiraan", "SO-HI", "Region", "18099", CountryCode.SOMALIA),
    SO_JD("Jubbada Dhexe", "SO-JD", "Region", "18086", CountryCode.SOMALIA),
    SO_JH("Jubbada Hoose", "SO-JH", "Region", "18087", CountryCode.SOMALIA),
    SO_MU("Mudug", "SO-MU", "Region", "18096", CountryCode.SOMALIA),
    SO_NU("Nugaal", "SO-NU", "Region", "18088", CountryCode.SOMALIA),
    SO_SA("Sanaag", "SO-SA", "Region", "18090", CountryCode.SOMALIA),
    SO_SD("Shabeellaha Dhexe", "SO-SD", "Region", "18100", CountryCode.SOMALIA),
    SO_SH("Shabeellaha Hoose", "SO-SH", "Region", "18089", CountryCode.SOMALIA),
    SO_SO("Sool", "SO-SO", "Region", "18083", CountryCode.SOMALIA),
    SO_TO("Togdheer", "SO-TO", "Region", "18091", CountryCode.SOMALIA),
    SO_WO("Woqooyi Galbeed", "SO-WO", "Region", "18092", CountryCode.SOMALIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
