package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Bahamas
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BS {
    /**
     * State Code Enumerator
     */
    BS_AC("Acklins and Crooked Islands","BS-AC","District","12567", CountryCode.BAHAMAS),
    BS_BI("Bimini","BS-BI","District","12574",CountryCode.BAHAMAS),
    BS_CI("Cat Island","BS-CI","District","12575",CountryCode.BAHAMAS),
    BS_EX("Exuma","BS-EX","District","12578",CountryCode.BAHAMAS),
    BS_FP("Freeport","BS-FP","District","12566",CountryCode.BAHAMAS),
    BS_FC("Fresh Creek","BS-FC","District","12572",CountryCode.BAHAMAS),
    BS_GH("Governor's Harbour","BS-GH","District","12577",CountryCode.BAHAMAS),
    BS_GT("Green Turtle Cay","BS-GT","District","12571",CountryCode.BAHAMAS),
    BS_HI("Harbour Island","BS-HI","District","12579",CountryCode.BAHAMAS),
    BS_HR("High Rock","BS-HR","District","20112",CountryCode.BAHAMAS),
    BS_IN("Inagua","BS-IN","District","12565",CountryCode.BAHAMAS),
    BS_KB("Kemps Bay","BS-KB","District","20113",CountryCode.BAHAMAS),
    BS_LI("Long Island","BS-LI","District","12580",CountryCode.BAHAMAS),
    BS_MH("Marsh Harbour","BS-MH","District","20114",CountryCode.BAHAMAS),
    BS_MG("Mayaguana","BS-MG","District","12564",CountryCode.BAHAMAS),
    BS_NP("New Providence","BS-NP","District","12563",CountryCode.BAHAMAS),
    BS_NB("Nicholls Town and Berry Islands","BS-NB","District","12573",CountryCode.BAHAMAS),
    BS_RI("Ragged Island","BS-RI","District","12568",CountryCode.BAHAMAS),
    BS_RS("Rock Sound","BS-RS","District","20115",CountryCode.BAHAMAS),
    BS_SR("San Salvador and Rum Cay","BS-SR","District","12569",CountryCode.BAHAMAS),
    BS_SP("Sandy Point","BS-SP","District","20116",CountryCode.BAHAMAS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BS(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
