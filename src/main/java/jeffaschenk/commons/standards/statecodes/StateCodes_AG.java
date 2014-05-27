package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Antigua and Barbuda
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AG {
    /**
     * State Code Enumerator
     */
    AG_10("Barbuda","AG-10","Dependency","12426", CountryCode.ANTIGUA_AND_BARBUDA),
    AG_X2("Redonda","AG-X2","Dependency","12425",CountryCode.ANTIGUA_AND_BARBUDA),
    AG_03("Saint George","AG-03","Parish","12427",CountryCode.ANTIGUA_AND_BARBUDA),
    AG_04("Saint John's","AG-04","Parish","12431",CountryCode.ANTIGUA_AND_BARBUDA),
    AG_05("Saint Mary","AG-05","Parish","12428",CountryCode.ANTIGUA_AND_BARBUDA),
    AG_06("Saint Paul","AG-06","Parish","12429",CountryCode.ANTIGUA_AND_BARBUDA),
    AG_07("Saint Peter","AG-07","Parish","12432",CountryCode.ANTIGUA_AND_BARBUDA),
    AG_08("Saint Philip","AG-08","Parish","12430",CountryCode.ANTIGUA_AND_BARBUDA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
