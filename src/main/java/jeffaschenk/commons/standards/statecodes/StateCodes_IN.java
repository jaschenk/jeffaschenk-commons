package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: India
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_IN {
    /**
     * State Code Enumerator
     */
    IN_AN("Andaman and Nicobar Islands","IN-AN","union territory","14673", CountryCode.INDIA),
    IN_AP("Andhra Pradesh","IN-AP","state","14695",CountryCode.INDIA),
    IN_AR("Arunachal Pradesh","IN-AR","state","14674",CountryCode.INDIA),
    IN_AS("Assam","IN-AS","state","14675",CountryCode.INDIA),
    IN_BR("Bihar","IN-BR","state","14676",CountryCode.INDIA),
    IN_CH("Chandigarh","IN-CH","union territory","14697",CountryCode.INDIA),
    IN_CT("Chhattisgarh","IN-CT","state","14677",CountryCode.INDIA),
    IN_DN("Dadra and Nagar Haveli","IN-DN","union territory","14678",CountryCode.INDIA),
    IN_DD("Daman and Diu","IN-DD","union territory","14698",CountryCode.INDIA),
    IN_DL("Delhi","IN-DL","union territory","14679",CountryCode.INDIA),
    IN_GA("Goa","IN-GA","state","14699",CountryCode.INDIA),
    IN_GJ("Gujarat","IN-GJ","state","14680",CountryCode.INDIA),
    IN_HR("Haryana","IN-HR","state","14681",CountryCode.INDIA),
    IN_HP("Himachal Pradesh","IN-HP","state","14672",CountryCode.INDIA),
    IN_JK("Jammu and Kashmir","IN-JK","state","14682",CountryCode.INDIA),
    IN_JH("Jharkhand","IN-JH","state","14700",CountryCode.INDIA),
    IN_KA("Karnataka","IN-KA","state","14683",CountryCode.INDIA),
    IN_KL("Kerala","IN-KL","state","14684",CountryCode.INDIA),
    IN_LD("Lakshadweep","IN-LD","union territory","14704",CountryCode.INDIA),
    IN_MP("Madhya Pradesh","IN-MP","state","14685",CountryCode.INDIA),
    IN_MH("Maharashtra","IN-MH","state","14686",CountryCode.INDIA),
    IN_MN("Manipur","IN-MN","state","14703",CountryCode.INDIA),
    IN_ML("Meghalaya","IN-ML","state","14687",CountryCode.INDIA),
    IN_MZ("Mizoram","IN-MZ","state","14702",CountryCode.INDIA),
    IN_NL("Nagaland","IN-NL","state","14688",CountryCode.INDIA),
    IN_OR("Orissa","IN-OR","state","14689",CountryCode.INDIA),
    IN_PY("Pondicherry","IN-PY","union territory","14705",CountryCode.INDIA),
    IN_PB("Punjab","IN-PB","state","14690",CountryCode.INDIA),
    IN_RJ("Rajasthan","IN-RJ","state","14691",CountryCode.INDIA),
    IN_SK("Sikkim","IN-SK","state","14701",CountryCode.INDIA),
    IN_TN("Tamil Nadu","IN-TN","state","14692",CountryCode.INDIA),
    IN_TR("Tripura","IN-TR","state","14693",CountryCode.INDIA),
    IN_UP("Uttar Pradesh","IN-UP","state","14694",CountryCode.INDIA),
    IN_UL("Uttaranchal","IN-UL","state","14671",CountryCode.INDIA),
    IN_WB("West Bengal","IN-WB","state","14696",CountryCode.INDIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_IN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
