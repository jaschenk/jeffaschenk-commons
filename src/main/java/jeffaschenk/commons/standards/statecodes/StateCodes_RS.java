package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Serbia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_RS {
    /**
     * State Code Enumerator
     */
    RS_00("Belgrade", "RS-00", "city", "47890", CountryCode.SERBIA),
    RS_14("Borski okrug", "RS-14", "district", "19066", CountryCode.SERBIA),
    RS_11("Brani?evski okrug", "RS-11", "district", "19067", CountryCode.SERBIA),
    RS_23("Jablani?ki okrug", "RS-23", "district", "19068", CountryCode.SERBIA),
    RS_04("Ju\u0382nobanatski okrug", "RS-04", "district", "19070", CountryCode.SERBIA),
    RS_06("Ju\u0382nobanatski okrug", "RS-06", "district", "19069", CountryCode.SERBIA),
    RS_09("Kolubarski okrug", "RS-09", "district", "19071", CountryCode.SERBIA),
    RS_25("Kosovski okrug", "RS-25", "district", "18004", CountryCode.SERBIA),
    RS_28("Kosovsko-Mitrova?ki okrug", "RS-28", "district", "19074", CountryCode.SERBIA),
    RS_29("Kosovsko-Pomoravski okrug", "RS-29", "district", "19073", CountryCode.SERBIA),
    RS_08("Ma?vanski okrug", "RS-08", "district", "19075", CountryCode.SERBIA),
    RS_17("Moravi?ki okrug", "RS-17", "district", "19076", CountryCode.SERBIA),
    RS_20("Ni\u0353avski okrug", "RS-20", "district", "19077", CountryCode.SERBIA),
    RS_26("Pe?ki okrug", "RS-26", "district", "19079", CountryCode.SERBIA),
    RS_22("Pirotski okrug", "RS-22", "district", "19080", CountryCode.SERBIA),
    RS_10("Podunavski okrug", "RS-10", "district", "19081", CountryCode.SERBIA),
    RS_13("Pomoravski okrug", "RS-13", "district", "19082", CountryCode.SERBIA),
    RS_27("Prizrenski okrug", "RS-27", "district", "19083", CountryCode.SERBIA),
    RS_24("P?injski okrug", "RS-24", "district", "19078", CountryCode.SERBIA),
    RS_19("Rasinski okrug", "RS-19", "district", "19084", CountryCode.SERBIA),
    RS_18("Ra\u0353ka okrug", "RS-18", "district", "19085", CountryCode.SERBIA),
    RS_03("Severnobanatski okrug", "RS-03", "district", "19087", CountryCode.SERBIA),
    RS_01("Severnoba?ki okrug", "RS-01", "district", "19086", CountryCode.SERBIA),
    RS_02("Srednjebanatski okrug", "RS-02", "district", "19088", CountryCode.SERBIA),
    RS_07("Sremski okrug", "RS-07", "district", "19089", CountryCode.SERBIA),
    RS_21("Topli\u0232ki okrug", "RS-21", "district", "19091", CountryCode.SERBIA),
    RS_15("Zaje\u0232arski okrug", "RS-15", "district", "19092", CountryCode.SERBIA),
    RS_05("Zapadnoba\u0232ki okrug", "RS-05", "district", "19093", CountryCode.SERBIA),
    RS_16("Zlatiborski okrug", "RS-16", "district", "19094", CountryCode.SERBIA),
    RS_12("\u0353umadijski okrug", "RS-12", "district", "19090", CountryCode.SERBIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_RS(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
