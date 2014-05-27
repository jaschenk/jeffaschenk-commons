package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Tunisia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_TN {
    /**
     * State Code Enumerator
     */
    TN_13("Ben Arous","TN-13","Governorate","18464", CountryCode.TUNISIA),
    TN_23("Bizerte","TN-23","Governorate","18470",CountryCode.TUNISIA),
    TN_31("B\u0233ja","TN-31","Governorate","18469",CountryCode.TUNISIA),
    TN_81("Gab\u0232s","TN-81","Governorate","18474",CountryCode.TUNISIA),
    TN_71("Gafsa","TN-71","Governorate","18475",CountryCode.TUNISIA),
    TN_32("Jendouba","TN-32","Governorate","18465",CountryCode.TUNISIA),
    TN_41("Kairouan","TN-41","Governorate","18477",CountryCode.TUNISIA),
    TN_42("Kasserine","TN-42","Governorate","18476",CountryCode.TUNISIA),
    TN_73("Kebili","TN-73","Governorate","18478",CountryCode.TUNISIA),
    TN_12("L'Ariana","TN-12","Governorate","18463",CountryCode.TUNISIA),
    TN_14("La Manouba","TN-14","Governorate","18467",CountryCode.TUNISIA),
    TN_33("Le Kef","TN-33","Governorate","18471",CountryCode.TUNISIA),
    TN_53("Mahdia","TN-53","Governorate","18472",CountryCode.TUNISIA),
    TN_82("Medenine","TN-82","Governorate","18466",CountryCode.TUNISIA),
    TN_52("Monastir","TN-52","Governorate","18473",CountryCode.TUNISIA),
    TN_21("Nabeul","TN-21","Governorate","18468",CountryCode.TUNISIA),
    TN_61("Sfax","TN-61","Governorate","18462",CountryCode.TUNISIA),
    TN_43("Sidi Bouzid","TN-43","Governorate","18479",CountryCode.TUNISIA),
    TN_34("Siliana","TN-34","Governorate","18461",CountryCode.TUNISIA),
    TN_51("Sousse","TN-51","Governorate","18480",CountryCode.TUNISIA),
    TN_83("Tataouine","TN-83","Governorate","18460",CountryCode.TUNISIA),
    TN_72("Tozeur","TN-72","Governorate","18481",CountryCode.TUNISIA),
    TN_11("Tunis","TN-11","Governorate","18482",CountryCode.TUNISIA),
    TN_22("Zaghouan","TN-22","Governorate","18459",CountryCode.TUNISIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_TN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
