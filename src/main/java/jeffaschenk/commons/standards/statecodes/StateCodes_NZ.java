package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: New Zealand
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_NZ {
    /**
     * State Code Enumerator
     */
     NZ_AUK("Auckland","NZ-AUK","Regional Council","15748", CountryCode.NEW_ZEALAND),
    NZ_BOP("Bay of Plenty","NZ-BOP","Regional Council","15753",CountryCode.NEW_ZEALAND),
    NZ_CAN("Canterbury","NZ-CAN","Regional Council","15754",CountryCode.NEW_ZEALAND),
    NZ_X1("Chatham  Islands","NZ-X1","Special Territory","19036",CountryCode.NEW_ZEALAND),
    NZ_GIS("Gisborne","NZ-GIS","Unitary Authority","15749",CountryCode.NEW_ZEALAND),
    NZ_HKB("Hawkes's Bay","NZ-HKB","Regional Council","15755",CountryCode.NEW_ZEALAND),
    NZ_MWT("Manawatu-Wanganui","NZ-MWT","Regional Council","15750",CountryCode.NEW_ZEALAND),
    NZ_MBH("Marlborough","NZ-MBH","Unitary Authority","15756",CountryCode.NEW_ZEALAND),
    NZ_NSN("Nelson","NZ-NSN","Unitary Authority","15757",CountryCode.NEW_ZEALAND),
    NZ_NTL("Northland","NZ-NTL","Regional Council","15747",CountryCode.NEW_ZEALAND),
    NZ_OTA("Otago","NZ-OTA","Regional Council","15758",CountryCode.NEW_ZEALAND),
    NZ_STL("Southland","NZ-STL","Regional Council","15759",CountryCode.NEW_ZEALAND),
    NZ_TKI("Taranaki","NZ-TKI","Regional Council","15751",CountryCode.NEW_ZEALAND),
    NZ_TAS("Tasman","NZ-TAS","Unitary Authority","15760",CountryCode.NEW_ZEALAND),
    NZ_WKO("Waikato","NZ-WKO","Regional Council","15746",CountryCode.NEW_ZEALAND),
    NZ_WGN("Wellington","NZ-WGN","Regional Council","15761",CountryCode.NEW_ZEALAND),
    NZ_WTC("West Coast","NZ-WTC","Regional Council","15762",CountryCode.NEW_ZEALAND);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_NZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
