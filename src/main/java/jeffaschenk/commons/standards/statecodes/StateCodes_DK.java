package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Denmark
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_DK {
    /**
     * State Code Enumerator
     */
    DK_040("Bornholm","DK-040","county","13256", CountryCode.DENMARK),
    DK_84("Capital","DK-84","Region","19434",CountryCode.DENMARK),
    DK_82("Central Jutland","DK-82","Region","19435",CountryCode.DENMARK),
    DK_147("Frederiksberg City","DK-147","city","13275",CountryCode.DENMARK),
    DK_020("Frederiksborg","DK-020","county","13257",CountryCode.DENMARK),
    DK_042("Fyn","DK-042","county","13265",CountryCode.DENMARK),
    DK_015("K\u0248benhavn","DK-015","county","13266",CountryCode.DENMARK),
    DK_101("K\u0248benhavn City","DK-101","city","13258",CountryCode.DENMARK),
    DK_080("Nordjylland","DK-080","county","13267",CountryCode.DENMARK),
    DK_81("North Jutland","DK-81","Region","19436",CountryCode.DENMARK),
    DK_055("Ribe","DK-055","county","13268",CountryCode.DENMARK),
    DK_065("Ringk\u0248bing","DK-065","county","13259",CountryCode.DENMARK),
    DK_025("Roskilde","DK-025","county","13269",CountryCode.DENMARK),
    DK_83("South Denmark","DK-83","Region","19438",CountryCode.DENMARK),
    DK_035("Storstr\u0248m","DK-035","county","13261",CountryCode.DENMARK),
    DK_050("S\u0248nderjylland","DK-050","county","13260",CountryCode.DENMARK),
    DK_060("Vejle","DK-060","county","13262",CountryCode.DENMARK),
    DK_030("Vestsj\u0230lland","DK-030","county","13263",CountryCode.DENMARK),
    DK_076("Viborg","DK-076","county","13264",CountryCode.DENMARK),
    DK_85("Zeeland","DK-85","Region","19437",CountryCode.DENMARK),
    DK_070("\u0197rhus","DK-070","county","13274",CountryCode.DENMARK);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_DK(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
