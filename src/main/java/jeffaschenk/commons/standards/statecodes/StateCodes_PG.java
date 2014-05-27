package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Papua New Guinea
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PG {
    /**
     * State Code Enumerator
     */
    PG_CPM("Central","PG-CPM","province","15935", CountryCode.PAPUA_NEW_GUINEA),
    PG_CPK("Chimbu","PG-CPK","province","15931",CountryCode.PAPUA_NEW_GUINEA),
    PG_EBR("East New Britain","PG-EBR","province","15946",CountryCode.PAPUA_NEW_GUINEA),
    PG_ESW("East Sepik","PG-ESW","province","15937",CountryCode.PAPUA_NEW_GUINEA),
    PG_EHG("Eastern Highlands","PG-EHG","province","15936",CountryCode.PAPUA_NEW_GUINEA),
    PG_EPW("Enga","PG-EPW","province","15934",CountryCode.PAPUA_NEW_GUINEA),
    PG_GPK("Gulf","PG-GPK","province","15947",CountryCode.PAPUA_NEW_GUINEA),
    PG_MPM("Madang","PG-MPM","province","15939",CountryCode.PAPUA_NEW_GUINEA),
    PG_MRL("Manus","PG-MRL","province","15933",CountryCode.PAPUA_NEW_GUINEA),
    PG_MBA("Milne Bay","PG-MBA","province","15940",CountryCode.PAPUA_NEW_GUINEA),
    PG_MPL("Morobe","PG-MPL","province","15941",CountryCode.PAPUA_NEW_GUINEA),
    PG_NCD("National Capital District (Port Moresby)","PG-NCD","district","15948",CountryCode.PAPUA_NEW_GUINEA),
    PG_NIK("New Ireland","PG-NIK","province","15932",CountryCode.PAPUA_NEW_GUINEA),
    PG_NSA("North Solomons","PG-NSA","province","15942",CountryCode.PAPUA_NEW_GUINEA),
    PG_NPP("Northern","PG-NPP","province","15949",CountryCode.PAPUA_NEW_GUINEA),
    PG_SAN("Sandaun [West Sepik]","PG-SAN","province","15943",CountryCode.PAPUA_NEW_GUINEA),
    PG_SHM("Southern Highlands","PG-SHM","province","15944",CountryCode.PAPUA_NEW_GUINEA),
    PG_WBK("West New Britain","PG-WBK","province","15945",CountryCode.PAPUA_NEW_GUINEA),
    PG_WPD("Western","PG-WPD","province","15938",CountryCode.PAPUA_NEW_GUINEA),
    PG_WHM("Western Highlands","PG-WHM","province","15950",CountryCode.PAPUA_NEW_GUINEA);
    
    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PG(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
