package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Syrian Arab Republic
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SY {
    /**
     * State Code Enumerator
     */
    SY_HL("Halab", "SY-HL", "Province", "18285", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_HM("Hamah", "SY-HM", "Province", "18293", CountryCode.SYRIAN_ARAB_REPUBLIC),   
    SY_HI("Hims", "SY-HI", "Province", "18284", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_HA("Hassetche", "SY-HA", "Province", "18288", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_LA("Al Ladhiqiyah", "SY-LA", "Province", "18289", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_QU("Al Qunaytirah", "SY-QU", "Province", "18290", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_RA("Ar Raqqah", "SY-RA", "Province", "18282", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_SU("As Suwayda'", "SY-SU", "Province", "18291", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_DR("Dara", "SY-DR", "Province", "18286", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_DY("Dayr az Zawr", "SY-DY", "Province", "18292", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_DI("Dimashq", "SY-DI", "Province", "18283", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_ID("Idlib", "SY-ID", "Province", "18294", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_RD("Rif Dimashq", "SY-RD", "Province", "18287", CountryCode.SYRIAN_ARAB_REPUBLIC),
    SY_TA("Tartus", "SY-TA", "Province", "18281", CountryCode.SYRIAN_ARAB_REPUBLIC);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
