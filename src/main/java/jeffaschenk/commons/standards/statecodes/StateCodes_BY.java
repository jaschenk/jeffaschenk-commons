package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Belarus
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BY {
    /**
     * State Code Enumerator
     */
    BY_BR("Brestskaya voblasts","BY-BR","Region","12669", CountryCode.BELARUS),
    BY_HO("Homyel'skaya voblasts","BY-HO","Region","12670",CountryCode.BELARUS),
    BY_X1("Horad Minsk","BY-X1","Municipality","12674",CountryCode.BELARUS),
    BY_HR("Hrodzenskaya voblasts","BY-HR","Region","12672",CountryCode.BELARUS),
    BY_MA("Mahilyowskaya voblasts","BY-MA","Region","12671",CountryCode.BELARUS),
    BY_MI("Minskaya voblasts","BY-MI","Region","12673",CountryCode.BELARUS),
    BY_VI("Vitsyebskaya voblasts","BY-VI","Region","12668",CountryCode.BELARUS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
