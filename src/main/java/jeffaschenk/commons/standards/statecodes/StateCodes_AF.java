package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Afghanistan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_AF {
    /**
     * State Code Enumerator
     */
    AF_BDS("Badakhshan","AF-BDS","Province","12267", CountryCode.AFGHANISTAN),
    AF_BDG("Badghis","AF-BDG","Province","12272",CountryCode.AFGHANISTAN),
    AF_BGL("Baghlan","AF-BGL","Province","12268",CountryCode.AFGHANISTAN),
    AF_BAL("Balkh","AF-BAL","Province","12273",CountryCode.AFGHANISTAN),
    AF_BAM("Bamian","AF-BAM","Province","12269",CountryCode.AFGHANISTAN),
    AF_DAY("Daykondi","AF-DAY","Province","20221",CountryCode.AFGHANISTAN),
    AF_FRA("Farah","AF-FRA","Province","12274",CountryCode.AFGHANISTAN),
    AF_FYB("Faryab","AF-FYB","Province","12270",CountryCode.AFGHANISTAN),
    AF_GHA("Ghazni","AF-GHA","Province","12271",CountryCode.AFGHANISTAN),
    AF_GHO("Ghowr","AF-GHO","Province","12275",CountryCode.AFGHANISTAN),
    AF_HEL("Helmand","AF-HEL","Province","12277",CountryCode.AFGHANISTAN),
    AF_HER("Herat","AF-HER","Province","12262",CountryCode.AFGHANISTAN),
    AF_JOW("Jowzjan","AF-JOW","Province","12283",CountryCode.AFGHANISTAN),
    AF_KAB("Kabul [Kabol]","AF-KAB","Province","12278",CountryCode.AFGHANISTAN),
    AF_KAN("Kandahar","AF-KAN","Province","12263",CountryCode.AFGHANISTAN),
    AF_KAP("Kapisa","AF-KAP","Province","12261",CountryCode.AFGHANISTAN),
    AF_KHO("Khowst","AF-KHO","Province","12276",CountryCode.AFGHANISTAN),
    AF_KNR("Konar [Kunar]","AF-KNR","Province","12284",CountryCode.AFGHANISTAN),
    AF_KDZ("Kondoz [Kunduz]","AF-KDZ","Province","12256",CountryCode.AFGHANISTAN),
    AF_LAG("Laghman","AF-LAG","Province","12279",CountryCode.AFGHANISTAN),
    AF_LOW("Lowgar","AF-LOW","Province","12260",CountryCode.AFGHANISTAN),
    AF_NAN("Nangrahar [Nangarhar]","AF-NAN","Province","12280",CountryCode.AFGHANISTAN),
    AF_NIM("Nimruz","AF-NIM","Province","12285",CountryCode.AFGHANISTAN),
    AF_NUR("Nurestan","AF-NUR","Province","12281",CountryCode.AFGHANISTAN),
    AF_ORU("Oruzgan [Uruzgan]","AF-ORU","Province","12255",CountryCode.AFGHANISTAN),
    AF_PIA("Paktia","AF-PIA","Province","12282",CountryCode.AFGHANISTAN),
    AF_PKA("Paktika","AF-PKA","Province","12259",CountryCode.AFGHANISTAN),
    AF_PAN("Panjshir","AF-PAN","Province","20222",CountryCode.AFGHANISTAN),
    AF_PAR("Parwan","AF-PAR","Province","12257",CountryCode.AFGHANISTAN),
    AF_SAM("Samangan","AF-SAM","Province","12264",CountryCode.AFGHANISTAN),
    AF_SAR("Sar-e Pol","AF-SAR","Province","12258",CountryCode.AFGHANISTAN),
    AF_TAK("Takhar","AF-TAK","Province","12265",CountryCode.AFGHANISTAN),
    AF_WAR("Wardak [Wardag]","AF-WAR","Province","12266",CountryCode.AFGHANISTAN),
    AF_ZAB("Zabol [Zabul]","AF-ZAB","Province","12254",CountryCode.AFGHANISTAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_AF(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
