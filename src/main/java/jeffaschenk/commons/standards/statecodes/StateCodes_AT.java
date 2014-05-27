package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Austria
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AT {
    /**
     * State Code Enumerator
     */
    AT_1("Burgenland", "AT-1", "State", "12480", CountryCode.AUSTRIA),
    AT_2("K\u0228rnten", "AT-2", "State", "12481", CountryCode.AUSTRIA),
    AT_3("Nieder\u0246sterreich", "AT-3", "State", "12485", CountryCode.AUSTRIA),
    AT_4("Ober\u0246sterreich", "AT-4", "State", "12482", CountryCode.AUSTRIA),
    AT_5("Salzburg", "AT-5", "State", "12479", CountryCode.AUSTRIA),
    AT_6("Steiermark", "AT-6", "State", "12483", CountryCode.AUSTRIA),
    AT_7("Tirol", "AT-7", "State", "12486", CountryCode.AUSTRIA),
    AT_8("Vorarlberg", "AT-8", "State", "12484", CountryCode.AUSTRIA),
    AT_9("Wien", "AT-9", "State", "12478", CountryCode.AUSTRIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AT(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
