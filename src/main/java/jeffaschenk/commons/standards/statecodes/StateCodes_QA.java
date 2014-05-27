package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Qatar
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_QA {
    /**
     * State Code Enumerator
     */
    QA_DA("Ad Dawhah","QA-DA","Municipality","17769", CountryCode.QATAR),
    QA_GH("Al Ghuwayriyah","QA-GH","Municipality","17770",CountryCode.QATAR),
    QA_JU("Al Jumayliyah","QA-JU","Municipality","17764",CountryCode.QATAR),
    QA_KH("Al Khawr","QA-KH","Municipality","17765",CountryCode.QATAR),
    QA_WA("Al Wakrah","QA-WA","Municipality","17763",CountryCode.QATAR),
    QA_RA("Ar Rayyan","QA-RA","Municipality","17771",CountryCode.QATAR),
    QA_JB("Jariyan al Batnah","QA-JB","Municipality","17768",CountryCode.QATAR),
    QA_MS("Madinat ash Shamal","QA-MS","Municipality","17766",CountryCode.QATAR),
    QA_X1("Umm Sa'id","QA-X1","Municipality","17767",CountryCode.QATAR),
    QA_US("Umm Salal","QA-US","Municipality","17762",CountryCode.QATAR);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_QA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
