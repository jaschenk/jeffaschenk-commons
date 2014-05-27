package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Malawi
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MW {
    /**
     * State Code Enumerator
     */
    MW_BA("Balaka","MW-BA","District","15279", CountryCode.MALAWI),
    MW_BL("Blantyre","MW-BL","District","15261",CountryCode.MALAWI),
    MW_CK("Chikwawa","MW-CK","District","15280",CountryCode.MALAWI),
    MW_CR("Chiradzulu","MW-CR","District","15262",CountryCode.MALAWI),
    MW_CT("Chitipa","MW-CT","District","15281",CountryCode.MALAWI),
    MW_DE("Dedza","MW-DE","District","15263",CountryCode.MALAWI),
    MW_DO("Dowa","MW-DO","District","15264",CountryCode.MALAWI),
    MW_KR("Karonga","MW-KR","District","15285",CountryCode.MALAWI),
    MW_KS("Kasungu","MW-KS","District","15265",CountryCode.MALAWI),
    MW_LK("Likoma Island","MW-LK","District","19024",CountryCode.MALAWI),
    MW_LI("Lilongwe","MW-LI","District","15286",CountryCode.MALAWI),
    MW_MH("Machinga","MW-MH","District","15267",CountryCode.MALAWI),
    MW_MG("Mangochi","MW-MG","District","15268",CountryCode.MALAWI),
    MW_MC("Mchinji","MW-MC","District","15284",CountryCode.MALAWI),
    MW_MU("Mulanje","MW-MU","District","15269",CountryCode.MALAWI),
    MW_MW("Mwanza","MW-MW","District","15270",CountryCode.MALAWI),
    MW_MZ("Mzimba","MW-MZ","District","15283",CountryCode.MALAWI),
    MW_NB("Nkhata Bay","MW-NB","District","15282",CountryCode.MALAWI),
    MW_NK("Nkhotakota","MW-NK","District","15272",CountryCode.MALAWI),
    MW_NS("Nsanje","MW-NS","District","15273",CountryCode.MALAWI),
    MW_NU("Ntcheu","MW-NU","District","15287",CountryCode.MALAWI),
    MW_NI("Ntchisi","MW-NI","District","15274",CountryCode.MALAWI),
    MW_PH("Phalombe","MW-PH","District","15275",CountryCode.MALAWI),
    MW_RU("Rumphi","MW-RU","District","15288",CountryCode.MALAWI),
    MW_SA("Salima","MW-SA","District","15276",CountryCode.MALAWI),
    MW_TH("Thyolo","MW-TH","District","15277",CountryCode.MALAWI),
    MW_ZO("Zomba","MW-ZO","District","15278",CountryCode.MALAWI);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MW(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
