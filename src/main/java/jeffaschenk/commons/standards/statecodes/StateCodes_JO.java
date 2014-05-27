package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Jordan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_JO {
    /**
     * State Code Enumerator
     */
    JO_AJ("Ajloan", "JO-AJ", "Province", "14982", CountryCode.JORDAN),
    JO_AM("Amman", "JO-AM", "Province", "14978", CountryCode.JORDAN),
    JO_BA("Al Balqa'", "JO-BA", "Province", "14979", CountryCode.JORDAN),
    JO_KA("Al Karak", "JO-KA", "Province", "14984", CountryCode.JORDAN),
    JO_MA("Al Mafraq", "JO-MA", "Province", "14981", CountryCode.JORDAN),
    JO_AQ("Aqaba", "JO-AQ", "Province", "14985", CountryCode.JORDAN),
    JO_AT("At Tafilah", "JO-AT", "Province", "14975", CountryCode.JORDAN),
    JO_AZ("Az Zarqa'", "JO-AZ", "Province", "14976", CountryCode.JORDAN),
    JO_IR("Irbid", "JO-IR", "Province", "14986", CountryCode.JORDAN),
    JO_JA("Jarash", "JO-JA", "Province", "14980", CountryCode.JORDAN),
    JO_MN("Ma`an", "JO-MN", "Province", "14977", CountryCode.JORDAN),
    JO_MD("Madaba", "JO-MD", "Province", "14983", CountryCode.JORDAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_JO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
