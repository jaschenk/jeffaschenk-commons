package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Uruguay
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_UY {
    /**
     * State Code Enumerator
     */
    UY_AR("Artigas","UY-AR","Department","18696", CountryCode.URUGUAY),
    UY_DU("Durazno","UY-DU","Department","18699",CountryCode.URUGUAY),
    UY_FS("Flores","UY-FS","Department","18695",CountryCode.URUGUAY),
    UY_FD("Florida","UY-FD","Department","18700",CountryCode.URUGUAY),
    UY_LA("Lavalleja","UY-LA","Department","18709",CountryCode.URUGUAY),
    UY_MA("Maldonado","UY-MA","Department","18701",CountryCode.URUGUAY),
    UY_MO("Montevideo","UY-MO","Department","18702",CountryCode.URUGUAY),
    UY_PA("Paysand\u0250","UY-PA","Department","18694",CountryCode.URUGUAY),
    UY_RV("Rivera","UY-RV","Department","18693",CountryCode.URUGUAY),
    UY_RO("Rocha","UY-RO","Department","18704",CountryCode.URUGUAY),
    UY_RN("R\u0237o Negro","UY-RN","Department","18703",CountryCode.URUGUAY),
    UY_SA("Salto","UY-SA","Department","18705",CountryCode.URUGUAY),
    UY_SJ("San Jos\u0233","UY-SJ","Department","18692",CountryCode.URUGUAY),
    UY_SO("Soriano","UY-SO","Department","18706",CountryCode.URUGUAY),
    UY_TA("Tacuaremb\u0243","UY-TA","Department","18691",CountryCode.URUGUAY),
    UY_TT("Treinta y Tres","UY-TT","Department","18707",CountryCode.URUGUAY),
    UY_X1("","UY-X1","Department","18697",CountryCode.URUGUAY),
    UY_X3("","UY-X3","Department","18698",CountryCode.URUGUAY),
    UY_X2("","UY-X2","Department","18708",CountryCode.URUGUAY);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_UY(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
