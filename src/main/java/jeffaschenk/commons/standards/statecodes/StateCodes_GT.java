package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Guatemala
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GT {
    /**
     * State Code Enumerator
     */
    GT_AV("Alta Verapaz","GT-AV","Department","14531", CountryCode.GUATEMALA),
    GT_BV("Baja Verapaz","GT-BV","Department","14526",CountryCode.GUATEMALA),
    GT_CM("Chimaltenango","GT-CM","Department","14532",CountryCode.GUATEMALA),
    GT_CQ("Chiquimula","GT-CQ","Department","14527",CountryCode.GUATEMALA),
    GT_PR("El Progreso","GT-PR","Department","14533",CountryCode.GUATEMALA),
    GT_ES("Escuintla","GT-ES","Department","14528",CountryCode.GUATEMALA),
    GT_GU("Guatemala","GT-GU","Department","14529",CountryCode.GUATEMALA),
    GT_HU("Huehuetenango","GT-HU","Department","14534",CountryCode.GUATEMALA),
    GT_IZ("Izabal","GT-IZ","Department","14535",CountryCode.GUATEMALA),
    GT_JA("Jalapa","GT-JA","Department","14536",CountryCode.GUATEMALA),
    GT_JU("Jutiapa","GT-JU","Department","14537",CountryCode.GUATEMALA),
    GT_PE("Pet\u0233n","GT-PE","Department","14538",CountryCode.GUATEMALA),
    GT_QZ("Quetzaltenango","GT-QZ","Department","14525",CountryCode.GUATEMALA),
    GT_QC("Quich\u0233","GT-QC","Department","14539",CountryCode.GUATEMALA),
    GT_RE("Retalhuleu","GT-RE","Department","14524",CountryCode.GUATEMALA),
    GT_SA("Sacatep\u0233quez","GT-SA","Department","14540",CountryCode.GUATEMALA),
    GT_SM("San Marcos","GT-SM","Department","14523",CountryCode.GUATEMALA),
    GT_SR("Santa Rosa","GT-SR","Department","14541",CountryCode.GUATEMALA),
    GT_SO("Solol\u0225","GT-SO","Department","14522",CountryCode.GUATEMALA),
    GT_SU("Suchitep\u0233quez","GT-SU","Department","14542",CountryCode.GUATEMALA),
    GT_TO("Totonicap\u0225n","GT-TO","Department","14521",CountryCode.GUATEMALA),
    GT_ZA("Zacapa","GT-ZA","Department","14530",CountryCode.GUATEMALA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GT(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
