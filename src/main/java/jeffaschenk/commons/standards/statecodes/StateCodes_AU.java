package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Australia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AU {
    /**
     * State Code Enumerator
     */
    AU_ACT("Australian Capital Territory","AU-ACT","territory","12471", CountryCode.AUSTRALIA),
    AU_NSW("New South Wales","AU-NSW","state","12477",CountryCode.AUSTRALIA),
    AU_NT("Northern Territory","AU-NT","territory","12475",CountryCode.AUSTRALIA),
    AU_QLD("Queensland","AU-QLD","state","12476",CountryCode.AUSTRALIA),
    AU_SA("South Australia","AU-SA","state","12472",CountryCode.AUSTRALIA),
    AU_TAS("Tasmania","AU-TAS","state","12474",CountryCode.AUSTRALIA),
    AU_VIC("Victoria","AU-VIC","state","12473",CountryCode.AUSTRALIA),
    AU_WA("Western Australia","AU-WA","state","12470",CountryCode.AUSTRALIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AU(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
