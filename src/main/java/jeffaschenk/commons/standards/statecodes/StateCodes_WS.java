package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Samoa
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_WS {
    /**
     * State Code Enumerator
     */
    WS_AA("A'ana", "WS-AA", "District", "20647", CountryCode.SAMOA),
    WS_AL("Aiga-i-le-Tai", "WS-AL", "District", "20648", CountryCode.SAMOA),
    WS_AT("Atua", "WS-AT", "District", "20649", CountryCode.SAMOA),
    WS_FA("Fa'asaleleaga", "WS-FA", "District", "20650", CountryCode.SAMOA),
    WS_GE("Gaga'emauga", "WS-GE", "District", "20651", CountryCode.SAMOA),
    WS_GI("Gagaifomauga", "WS-GI", "District", "20652", CountryCode.SAMOA),
    WS_PA("Palauli", "WS-PA", "District", "20653", CountryCode.SAMOA),
    WS_SA("Satupa'itea", "WS-SA", "District", "20654", CountryCode.SAMOA),
    WS_TU("Tuamasaga", "WS-TU", "District", "20655", CountryCode.SAMOA),
    WS_VF("Va'a-o-Fonoti", "WS-VF", "District", "20656", CountryCode.SAMOA),
    WS_VS("Vaisigano", "WS-VS", "District", "20657", CountryCode.SAMOA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_WS(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
