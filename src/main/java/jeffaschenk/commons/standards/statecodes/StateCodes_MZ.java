package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Mozambique
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MZ {
    /**
     * State Code Enumerator
     */
    MZ_P("Cabo Delgado", "MZ-P", "province", "15599", CountryCode.MOZAMBIQUE),
    MZ_G("Gaza", "MZ-G", "province", "15602", CountryCode.MOZAMBIQUE),
    MZ_I("Inhambane", "MZ-I", "province", "15589", CountryCode.MOZAMBIQUE),
    MZ_B("Manica", "MZ-B", "province", "15590", CountryCode.MOZAMBIQUE),
    MZ_L("Maputo", "MZ-L", "province", "15591", CountryCode.MOZAMBIQUE),
    MZ_MPM("Maputo City", "MZ-MPM", "city", "15603", CountryCode.MOZAMBIQUE),
    MZ_N("Nampula", "MZ-N", "province", "15604", CountryCode.MOZAMBIQUE),
    MZ_A("Niassa", "MZ-A", "province", "15592", CountryCode.MOZAMBIQUE),
    MZ_S("Sofala", "MZ-S", "province", "15605", CountryCode.MOZAMBIQUE),
    MZ_T("Tete", "MZ-T", "province", "15593", CountryCode.MOZAMBIQUE),
    MZ_Q("Zamb\u0233zia", "MZ-Q", "province", "15606", CountryCode.MOZAMBIQUE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
