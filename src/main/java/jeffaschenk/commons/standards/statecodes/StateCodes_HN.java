package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Honduras
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_HN {
    /**
     * State Code Enumerator
     */
    HN_AT("Atl\u0225ntida","HN-AT","Department","14635", CountryCode.HONDURAS),
    HN_CH("Choluteca","HN-CH","Department","14624",CountryCode.HONDURAS),
    HN_CL("Col\u0243n","HN-CL","Department","14625",CountryCode.HONDURAS),
    HN_CM("Comayagua","HN-CM","Department","14636",CountryCode.HONDURAS),
    HN_CP("Cop\u0225n","HN-CP","Department","14626",CountryCode.HONDURAS),
    HN_CR("Cort\u0233s","HN-CR","Department","14637",CountryCode.HONDURAS),
    HN_EP("El Para\u0237so","HN-EP","Department","14638",CountryCode.HONDURAS),
    HN_FM("Francisco Moraz\u0225n","HN-FM","Department","14628",CountryCode.HONDURAS),
    HN_GD("Gracias a Dios","HN-GD","Department","14629",CountryCode.HONDURAS),
    HN_IN("Intibuc\u0225","HN-IN","Department","14623",CountryCode.HONDURAS),
    HN_IB("Islas de la Bah\u0237a","HN-IB","Department","14630",CountryCode.HONDURAS),
    HN_LP("La Paz","HN-LP","Department","14639",CountryCode.HONDURAS),
    HN_LE("Lempira","HN-LE","Department","14631",CountryCode.HONDURAS),
    HN_OC("Ocotepeque","HN-OC","Department","14640",CountryCode.HONDURAS),
    HN_OL("Olancho","HN-OL","Department","14632",CountryCode.HONDURAS),
    HN_SB("Santa B\u0225rbara","HN-SB","Department","14633",CountryCode.HONDURAS),
    HN_VA("Valle","HN-VA","Department","14622",CountryCode.HONDURAS),
    HN_YO("Yoro","HN-YO","Department","14634",CountryCode.HONDURAS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_HN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
