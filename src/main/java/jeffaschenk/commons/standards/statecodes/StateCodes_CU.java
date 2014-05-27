package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Cuba
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CU {
    /**
     * State Code Enumerator
     */
    CU_09("Camag\u0252ey", "CU-09", "province", "13152", CountryCode.CUBA),
    CU_08("Ciego de \u0225vila", "CU-08", "province", "13153", CountryCode.CUBA),
    CU_06("Cienfuegos", "CU-06", "province", "13154", CountryCode.CUBA),
    CU_03("Ciudad de La Habana", "CU-03", "province", "13142", CountryCode.CUBA),
    CU_12("Granma", "CU-12", "province", "13155", CountryCode.CUBA),
    CU_14("Guant\u0225namo", "CU-14", "province", "13157", CountryCode.CUBA),
    CU_11("Holgu\u0237n", "CU-11", "province", "13156", CountryCode.CUBA),
    CU_99("Isla de la Juventud", "CU-99", "special municipality", "13143", CountryCode.CUBA),
    CU_02("La Habana", "CU-02", "province", "13159", CountryCode.CUBA),
    CU_10("Las Tunas", "CU-10", "province", "13144", CountryCode.CUBA),
    CU_04("Matanzas", "CU-04", "province", "13145", CountryCode.CUBA),
    CU_01("Pinar del R\u0237o", "CU-01", "province", "13160", CountryCode.CUBA),
    CU_07("Sancti Sp\u0237ritus", "CU-07", "province", "13146", CountryCode.CUBA),
    CU_13("Santiago de Cuba", "CU-13", "province", "13158", CountryCode.CUBA),
    CU_05("Villa Clara", "CU-05", "province", "13147", CountryCode.CUBA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CU(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
