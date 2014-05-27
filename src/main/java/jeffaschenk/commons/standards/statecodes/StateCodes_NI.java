package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Nicaragua
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NI {
    /**
     * State Code Enumerator
     */
    NI_AN("Atl\u0225ntico Norte", "NI-AN", "autonomous region", "15779", CountryCode.NICARAGUA),
    NI_AS("Atl\u0225ntico Sur", "NI-AS", "autonomous region", "15771", CountryCode.NICARAGUA),
    NI_BO("Boaco", "NI-BO", "department", "15772", CountryCode.NICARAGUA),
    NI_CA("Carazo", "NI-CA", "department", "15766", CountryCode.NICARAGUA),
    NI_CI("Chinandega", "NI-CI", "department", "15770", CountryCode.NICARAGUA),
    NI_CO("Chontales", "NI-CO", "department", "15765", CountryCode.NICARAGUA),
    NI_ES("Estel\u0237", "NI-ES", "department", "15773", CountryCode.NICARAGUA),
    NI_GR("Granada", "NI-GR", "department", "15777", CountryCode.NICARAGUA),
    NI_JI("Jinotega", "NI-JI", "department", "15769", CountryCode.NICARAGUA),
    NI_LE("Le\u0243n", "NI-LE", "department", "15774", CountryCode.NICARAGUA),
    NI_MD("Madriz", "NI-MD", "department", "15778", CountryCode.NICARAGUA),
    NI_MN("Managua", "NI-MN", "department", "15768", CountryCode.NICARAGUA),
    NI_MS("Masaya", "NI-MS", "department", "15764", CountryCode.NICARAGUA),
    NI_MT("Matagalpa", "NI-MT", "department", "15775", CountryCode.NICARAGUA),
    NI_NS("Nueva Segovia", "NI-NS", "department", "15767", CountryCode.NICARAGUA),
    NI_RI("Rivas", "NI-RI", "department", "15776", CountryCode.NICARAGUA),
    NI_SJ("R\u0237o San Juan", "NI-SJ", "department", "15763", CountryCode.NICARAGUA);


    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NI(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
