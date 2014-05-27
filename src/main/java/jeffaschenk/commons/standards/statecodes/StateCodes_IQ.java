package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Iraq
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_IQ {
    /**
     * State Code Enumerator
     */
    IQ_AN("Al Anbar","IQ-AN","Province","14771", CountryCode.IRAQ),
    IQ_BA("Al Basrah","IQ-BA","Province","14773",CountryCode.IRAQ),
    IQ_MU("Al Muthann\u0225","IQ-MU","Province","14777",CountryCode.IRAQ),
    IQ_QA("Al Qadisiyah","IQ-QA","Province","14766",CountryCode.IRAQ),
    IQ_NA("An Najaf","IQ-NA","Province","14768",CountryCode.IRAQ),
    IQ_AR("Arbil","IQ-AR","Province","14769",CountryCode.IRAQ),
    IQ_SU("As Sulaymaniyah","IQ-SU","Province","14779",CountryCode.IRAQ),
    IQ_TS("At Ta'mim","IQ-TS","Province","14765",CountryCode.IRAQ),
    IQ_BB("Babil","IQ-BB","Province","14772",CountryCode.IRAQ),
    IQ_BG("Baghdad","IQ-BG","Province","14770",CountryCode.IRAQ),
    IQ_DA("Dahuk","IQ-DA","Province","14774",CountryCode.IRAQ),
    IQ_DQ("Dhi Qar","IQ-DQ","Province","14781",CountryCode.IRAQ),
    IQ_DI("Diyal\u0225","IQ-DI","Province","14775",CountryCode.IRAQ),
    IQ_KA("Karbala'","IQ-KA","Province","14776",CountryCode.IRAQ),
    IQ_MA("Maysan","IQ-MA","Province","14782",CountryCode.IRAQ),
    IQ_NI("Ninaw\u0225","IQ-NI","Province","14778",CountryCode.IRAQ),
    IQ_SD("Salah ad Din","IQ-SD","Province","14767",CountryCode.IRAQ),
    IQ_WA("Wasit","IQ-WA","Province","14780",CountryCode.IRAQ);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_IQ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
