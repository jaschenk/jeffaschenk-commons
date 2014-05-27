package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Portugal
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_PT {
    /**
     * State Code Enumerator
     */
    PT_01("Aveiro", "PT-01", "district", "19045", CountryCode.PORTUGAL),
    PT_02("Beja", "PT-02", "district", "19046", CountryCode.PORTUGAL),
    PT_03("Braga", "PT-03", "district", "19047", CountryCode.PORTUGAL),
    PT_04("Bragan\u0231a", "PT-04", "district", "19048", CountryCode.PORTUGAL),
    PT_05("Castelo Branco", "PT-05", "district", "19049", CountryCode.PORTUGAL),
    PT_06("Coimbra", "PT-06", "district", "19050", CountryCode.PORTUGAL),
    PT_08("Faro", "PT-08", "district", "19052", CountryCode.PORTUGAL),
    PT_09("Guarda", "PT-09", "district", "19053", CountryCode.PORTUGAL),
    PT_10("Leiria", "PT-10", "district", "19054", CountryCode.PORTUGAL),
    PT_11("Lisboa", "PT-11", "district", "19055", CountryCode.PORTUGAL),
    PT_12("Portalegre", "PT-12", "district", "19056", CountryCode.PORTUGAL),
    PT_13("Porto", "PT-13", "district", "19057", CountryCode.PORTUGAL),
    PT_30("Regi\u0227o Aut\u0243noma da Madeira", "PT-30", "autonomous region", "17751", CountryCode.PORTUGAL),
    PT_20("Regi\u0227o Aut\u0243noma dos A\u0231ores", "PT-20", "autonomous region", "17748", CountryCode.PORTUGAL),
    PT_14("Santar\u0233m", "PT-14", "district", "19058", CountryCode.PORTUGAL),
    PT_15("Set\u0250bal", "PT-15", "district", "19059", CountryCode.PORTUGAL),
    PT_16("Viana do Castelo", "PT-16", "district", "19060", CountryCode.PORTUGAL),
    PT_17("Vila Real", "PT-17", "district", "19061", CountryCode.PORTUGAL),
    PT_18("Viseu", "PT-18", "district", "19062", CountryCode.PORTUGAL),
    PT_07("\u0233vora", "PT-07", "district", "19051", CountryCode.PORTUGAL);


    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_PT(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
