package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Suriname
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SR {
    /**
     * State Code Enumerator
     */
    SR_BR("Brokopondo","SR-BR","District","18220", CountryCode.SURINAME),
    SR_CM("Commewijne","SR-CM","District","18214",CountryCode.SURINAME),
    SR_CR("Coronie","SR-CR","District","18215",CountryCode.SURINAME),
    SR_MA("Marowijne","SR-MA","District","18221",CountryCode.SURINAME),
    SR_NI("Nickerie","SR-NI","District","18216",CountryCode.SURINAME),
    SR_PR("Para","SR-PR","District","18222",CountryCode.SURINAME),
    SR_PM("Paramaribo","SR-PM","District","18217",CountryCode.SURINAME),
    SR_SA("Saramacca","SR-SA","District","18218",CountryCode.SURINAME),
    SR_SI("Sipaliwini","SR-SI","District","18213",CountryCode.SURINAME),
    SR_WA("Wanica","SR-WA","District","18219",CountryCode.SURINAME);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
