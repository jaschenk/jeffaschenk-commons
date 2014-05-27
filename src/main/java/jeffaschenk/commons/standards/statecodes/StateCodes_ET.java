package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Ethiopia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_ET {
    /**
     * State Code Enumerator
     */
    ET_AA("Adis Abeba","ET-AA","administration","13661", CountryCode.ETHIOPIA),
    ET_AF("Afar","ET-AF","state","13662",CountryCode.ETHIOPIA),
    ET_AM("Amara","ET-AM","state","13658",CountryCode.ETHIOPIA),
    ET_BE("Binshangul Gumuz","ET-BE","state","13663",CountryCode.ETHIOPIA),
    ET_DD("Dire Dawa","ET-DD","administration","13667",CountryCode.ETHIOPIA),
    ET_GA("Gambela Hizboch","ET-GA","state","13666",CountryCode.ETHIOPIA),
    ET_HA("Hareri Hizb","ET-HA","state","13665",CountryCode.ETHIOPIA),
    ET_OR("Oromiya","ET-OR","state","13668",CountryCode.ETHIOPIA),
    ET_SO("Sumale","ET-SO","state","13664",CountryCode.ETHIOPIA),
    ET_TI("Tigray","ET-TI","state","13659",CountryCode.ETHIOPIA),
    ET_SN("YeDebub Biheroch Bihereseboch na Hizboch","ET-SN","state","13657",CountryCode.ETHIOPIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_ET(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
