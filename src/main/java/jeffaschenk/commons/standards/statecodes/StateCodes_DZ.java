package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Algeria
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_DZ {
    /**
     * State Code Enumerator
     */
    DZ_01("Adrar", "DZ-01", "Province", "12363", CountryCode.ALGERIA),
    DZ_16("Alger", "DZ-16", "Province", "12344", CountryCode.ALGERIA),
    DZ_23("Annaba", "DZ-23", "Province", "12332", CountryCode.ALGERIA),
    DZ_44("A\u0239n Defla", "DZ-44", "Province", "12351", CountryCode.ALGERIA),
    DZ_46("A\u0239n T\u0233mouchent", "DZ-46", "Province", "12333", CountryCode.ALGERIA),
    DZ_05("Batna", "DZ-05", "Province", "12334", CountryCode.ALGERIA),
    DZ_07("Biskra", "DZ-07", "Province", "12354", CountryCode.ALGERIA),
    DZ_09("Blida", "DZ-09", "Province", "12336", CountryCode.ALGERIA),
    DZ_34("Bordj Bou Arr\u0233ridj", "DZ-34", "Province", "12342", CountryCode.ALGERIA),
    DZ_10("Bouira", "DZ-10", "Province", "12341", CountryCode.ALGERIA),
    DZ_35("Boumerd\u0232s", "DZ-35", "Province", "12337", CountryCode.ALGERIA),
    DZ_08("B\u0233char", "DZ-08", "Province", "12352", CountryCode.ALGERIA),
    DZ_06("B\u0233ja\u0239a", "DZ-06", "Province", "12335", CountryCode.ALGERIA),
    DZ_02("Chlef", "DZ-02", "Province", "12367", CountryCode.ALGERIA),
    DZ_25("Constantine", "DZ-25", "Province", "12348", CountryCode.ALGERIA),
    DZ_17("Djelfa", "DZ-17", "Province", "12345", CountryCode.ALGERIA),
    DZ_32("El Bayadh", "DZ-32", "Province", "12353", CountryCode.ALGERIA),
    DZ_39("El Oued", "DZ-39", "Province", "12323", CountryCode.ALGERIA),
    DZ_36("El Tarf", "DZ-36", "Province", "12357", CountryCode.ALGERIA),
    DZ_47("Gharda\u0239a", "DZ-47", "Province", "12343", CountryCode.ALGERIA),
    DZ_24("Guelma", "DZ-24", "Province", "12366", CountryCode.ALGERIA),
    DZ_33("Illizi", "DZ-33", "Province", "12340", CountryCode.ALGERIA),
    DZ_18("Jijel", "DZ-18", "Province", "12330", CountryCode.ALGERIA),
    DZ_40("Khenchela", "DZ-40", "Province", "12339", CountryCode.ALGERIA),
    DZ_03("Laghouat", "DZ-03", "Province", "12350", CountryCode.ALGERIA),
    DZ_29("Mascara", "DZ-29", "Province", "12365", CountryCode.ALGERIA),
    DZ_43("Mila", "DZ-43", "Province", "12329", CountryCode.ALGERIA),
    DZ_27("Mostaganem", "DZ-27", "Province", "12347", CountryCode.ALGERIA),
    DZ_28("Msila", "DZ-28", "Province", "12364", CountryCode.ALGERIA),
    DZ_26("M\u0233d\u0233a", "DZ-26", "Province", "12346", CountryCode.ALGERIA),
    DZ_45("Naama", "DZ-45", "Province", "12328", CountryCode.ALGERIA),
    DZ_31("Oran", "DZ-31", "Province", "12362", CountryCode.ALGERIA),
    DZ_30("Ouargla", "DZ-30", "Province", "12322", CountryCode.ALGERIA),
    DZ_04("Oum el Bouaghi", "DZ-04", "Province", "12361", CountryCode.ALGERIA),
    DZ_48("Relizane", "DZ-48", "Province", "12338", CountryCode.ALGERIA),
    DZ_20("Sa\u0239da", "DZ-20", "Province", "12327", CountryCode.ALGERIA),
    DZ_22("Sidi Bel Abb\u0232s", "DZ-22", "Province", "12326", CountryCode.ALGERIA),
    DZ_21("Skikda", "DZ-21", "Province", "12349", CountryCode.ALGERIA),
    DZ_41("Souk Ahras", "DZ-41", "Province", "12356", CountryCode.ALGERIA),
    DZ_19("S\u0233tif", "DZ-19", "Province", "12355", CountryCode.ALGERIA),
    DZ_11("Tamanghasset", "DZ-11", "Province", "12368", CountryCode.ALGERIA),
    DZ_14("Tiaret", "DZ-14", "Province", "12360", CountryCode.ALGERIA),
    DZ_37("Tindouf", "DZ-37", "Province", "12359", CountryCode.ALGERIA),
    DZ_42("Tipaza", "DZ-42", "Province", "12369", CountryCode.ALGERIA),
    DZ_38("Tissemsilt", "DZ-38", "Province", "12325", CountryCode.ALGERIA),
    DZ_15("Tizi Ouzou", "DZ-15", "Province", "12324", CountryCode.ALGERIA),
    DZ_13("Tlemcen", "DZ-13", "Province", "12370", CountryCode.ALGERIA),
    DZ_12("T\u0233bessa", "DZ-12", "Province", "12358", CountryCode.ALGERIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_DZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
