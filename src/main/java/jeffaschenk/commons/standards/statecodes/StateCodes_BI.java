package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Burundi
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BI {
    /**
     * State Code Enumerator
     */
    BI_BB("Bubanza","BI-BB","Province","12893", CountryCode.BURUNDI),
    BI_BJ("Bujumbura","BI-BJ","Province","12884",CountryCode.BURUNDI),
    BI_BR("Bururi","BI-BR","Province","12895",CountryCode.BURUNDI),
    BI_CA("Cankuzo","BI-CA","Province","12885",CountryCode.BURUNDI),
    BI_CI("Cibitoke","BI-CI","Province","12896",CountryCode.BURUNDI),
    BI_GI("Gitega","BI-GI","Province","12886",CountryCode.BURUNDI),
    BI_KR("Karuzi","BI-KR","Province","12887",CountryCode.BURUNDI),
    BI_KY("Kayanza","BI-KY","Province","12894",CountryCode.BURUNDI),
    BI_KI("Kirundo","BI-KI","Province","12888",CountryCode.BURUNDI),
    BI_MA("Makamba","BI-MA","Province","12889",CountryCode.BURUNDI),
    BI_MU("Muramvya","BI-MU","Province","12897",CountryCode.BURUNDI),
    BI_MY("Muyinga","BI-MY","Province","12890",CountryCode.BURUNDI),
    BI_MW("Mwaro","BI-MW","Province","12898",CountryCode.BURUNDI),
    BI_NG("Ngozi","BI-NG","Province","12891",CountryCode.BURUNDI),
    BI_RT("Rutana","BI-RT","Province","12892",CountryCode.BURUNDI),
    BI_RY("Ruyigi","BI-RY","Province","12883",CountryCode.BURUNDI);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BI(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
