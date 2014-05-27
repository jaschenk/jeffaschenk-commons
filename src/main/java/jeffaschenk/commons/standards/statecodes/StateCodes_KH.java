package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Cambodia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KH {
    /**
     * State Code Enumerator
     */
    KH_2("Baat Dambang [Batd\u0224mb\u0224ng]", "KH-2", "province", "12918", CountryCode.CAMBODIA),
    KH_1("Banteay Mean Chey [B\u0224nt\u0233ay M\u0233anchey]", "KH-1", "province", "12911", CountryCode.CAMBODIA),
    KH_3("Kampong Chaam [K\u0224mp\u0243ng Cham]", "KH-3", "province", "12912", CountryCode.CAMBODIA),
    KH_4("Kampong Chhnang [K\u0224mp\u0243ng Chhnang]", "KH-4", "province", "12919", CountryCode.CAMBODIA),
    KH_5("Kampong Spueu [K\u0224mp\u0243ng Sp\u0339]", "KH-5", "province", "12920", CountryCode.CAMBODIA),
    KH_6("Kampong Thum [K\u0224mp\u0243ng Thum]", "KH-6", "province", "12902", CountryCode.CAMBODIA),
    KH_7("Kampot [K\u0224mp\u0244t]", "KH-7", "province", "12903", CountryCode.CAMBODIA),
    KH_8("Kandaal [K\u0224ndal]", "KH-8", "province", "12904", CountryCode.CAMBODIA),
    KH_9("Kaoh Kong [Ka\u0244h Kong]", "KH-9", "province", "12905", CountryCode.CAMBODIA),
    KH_10("Kracheh [Kr\u0224ch\u0233h]", "KH-10", "province", "12906", CountryCode.CAMBODIA),
    KH_23("Krong Kaeb [Krong K\u0234b]", "KH-23", "autonomous municipality", "12907", CountryCode.CAMBODIA),
    KH_24("Krong Pailin [Krong Pailin]", "KH-24", "autonomous municipality", "12908", CountryCode.CAMBODIA),
    KH_18("Krong Preah Sihanouk [Krong Preah Sihanouk]", "KH-18", "autonomous municipality", "12909", CountryCode.CAMBODIA),
    KH_11("Mondol Kiri [M\u0244nd\u0243l Kiri]", "KH-11", "province", "12910", CountryCode.CAMBODIA),
    KH_22("Otdar Mean Chey [Otd\u0224r M\u0233anchey]", "KH-22", "province", "12901", CountryCode.CAMBODIA),
    KH_12("Phnom Penh [Phnum P\u0233nh]", "KH-12", "autonomous municipality", "12921", CountryCode.CAMBODIA),
    KH_15("Pousaat [Pouthisat]", "KH-15", "province", "12900", CountryCode.CAMBODIA),
    KH_13("Preah Vihear [Preah Vih\u0233ar]", "KH-13", "province", "12913", CountryCode.CAMBODIA),
    KH_14("Prey Veaeng [Prey V\u0234ng]", "KH-14", "province", "12914", CountryCode.CAMBODIA),
    KH_16("Rotanak Kiri [R\u0244t\u0224n\u0244kiri]", "KH-16", "province", "12922", CountryCode.CAMBODIA),
    KH_17("Siem Reab [Siemr\u0233ab]", "KH-17", "province", "12915", CountryCode.CAMBODIA),
    KH_19("Stueng Traeng [St\u0339ng Tr\u0234ng]", "KH-19", "province", "12925", CountryCode.CAMBODIA),
    KH_20("Svaay Rieng [Svay Rieng]", "KH-20", "province", "12916", CountryCode.CAMBODIA),
    KH_21("Taakaev [Tak\u0234v]", "KH-21", "province", "12924", CountryCode.CAMBODIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KH(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
