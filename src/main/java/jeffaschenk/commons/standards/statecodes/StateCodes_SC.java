package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Seychelles
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_SC {
    /**
     * State Code Enumerator
     */
    SC_01("Anse aux Pins","SC-01","District","18014", CountryCode.SEYCHELLES),
    SC_02("Anse Boileau","SC-02","District","18017",CountryCode.SEYCHELLES),
    SC_04("Anse Louis","SC-04","District","18019",CountryCode.SEYCHELLES),
    SC_05("Anse Royale","SC-05","District","18015",CountryCode.SEYCHELLES),
    SC_03("Anse \u0233toile","SC-03","District","18018",CountryCode.SEYCHELLES),
    SC_06("Baie Lazare","SC-06","District","18006",CountryCode.SEYCHELLES),
    SC_07("Baie Sainte Anne","SC-07","District","18020",CountryCode.SEYCHELLES),
    SC_08("Beau Vallon","SC-08","District","18021",CountryCode.SEYCHELLES),
    SC_09("Bel Air","SC-09","District","18025",CountryCode.SEYCHELLES),
    SC_10("Bel Ombre","SC-10","District","18022",CountryCode.SEYCHELLES),
    SC_11("Cascade","SC-11","District","18029",CountryCode.SEYCHELLES),
    SC_12("Glacis","SC-12","District","18024",CountryCode.SEYCHELLES),
    SC_13("Grand' Anse (Mah\u0233)","SC-13","District","18028",CountryCode.SEYCHELLES),
    SC_14("Grand' Anse (Praslin)","SC-14","District","18007",CountryCode.SEYCHELLES),
    SC_15("La Digue","SC-15","District","18027",CountryCode.SEYCHELLES),
    SC_16("La Rivi\u0232re Anglaise","SC-16","District","18023",CountryCode.SEYCHELLES),
    SC_17("Mont Buxton","SC-17","District","18009",CountryCode.SEYCHELLES),
    SC_18("Mont Fleuri","SC-18","District","18030",CountryCode.SEYCHELLES),
    SC_19("Plaisance","SC-19","District","18026",CountryCode.SEYCHELLES),
    SC_20("Pointe La Rue","SC-20","District","18011",CountryCode.SEYCHELLES),
    SC_21("Port Glaud","SC-21","District","18012",CountryCode.SEYCHELLES),
    SC_22("Saint Louis","SC-22","District","18013",CountryCode.SEYCHELLES),
    SC_23("Takamaka","SC-23","District","18005",CountryCode.SEYCHELLES);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_SC(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
