package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Greece
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_GR {
    /**
     * State Code Enumerator
     */
    GR_13("Acha\u0239a","GR-13","department","14414", CountryCode.GREECE),
    GR_69("Agio Oros","GR-69","self-governed part","14417",CountryCode.GREECE),
    GR_01("Aitolia kai Akarnania","GR-01","department","14423",CountryCode.GREECE),
    GR_11("Argolida","GR-11","department","14415",CountryCode.GREECE),
    GR_12("Arkadia","GR-12","department","14424",CountryCode.GREECE),
    GR_31("Arta","GR-31","department","14425",CountryCode.GREECE),
    GR_A1("Attiki","GR-A1","department","14416",CountryCode.GREECE),
    GR_64("Chalkidiki","GR-64","department","14421",CountryCode.GREECE),
    GR_94("Chania","GR-94","department","14432",CountryCode.GREECE),
    GR_85("Chios","GR-85","department","14422",CountryCode.GREECE),
    GR_81("Dodekanisos","GR-81","department","14426",CountryCode.GREECE),
    GR_52("Drama","GR-52","department","14427",CountryCode.GREECE),
    GR_71("Evros","GR-71","department","14428",CountryCode.GREECE),
    GR_05("Evrytania","GR-05","department","14418",CountryCode.GREECE),
    GR_04("Evvoia","GR-04","department","14419",CountryCode.GREECE),
    GR_63("Florina","GR-63","department","14429",CountryCode.GREECE),
    GR_07("Fokida","GR-07","department","14430",CountryCode.GREECE),
    GR_06("Fthiotida","GR-06","department","14420",CountryCode.GREECE),
    GR_51("Grevena","GR-51","department","14431",CountryCode.GREECE),
    GR_14("Ileia","GR-14","department","14433",CountryCode.GREECE),
    GR_53("Imathia","GR-53","department","14450",CountryCode.GREECE),
    GR_33("Ioannina","GR-33","department","14451",CountryCode.GREECE),
    GR_91("Irakleio","GR-91","department","14434",CountryCode.GREECE),
    GR_41("Karditsa","GR-41","department","14435",CountryCode.GREECE),
    GR_56("Kastoria","GR-56","department","14452",CountryCode.GREECE),
    GR_55("Kavala","GR-55","department","14436",CountryCode.GREECE),
    GR_23("Kefallonia","GR-23","department","14453",CountryCode.GREECE),
    GR_22("Kerkyra","GR-22","department","14454",CountryCode.GREECE),
    GR_57("Kilkis","GR-57","department","14455",CountryCode.GREECE),
    GR_15("Korinthia","GR-15","department","14438",CountryCode.GREECE),
    GR_58("Kozani","GR-58","department","14456",CountryCode.GREECE),
    GR_82("Kyklades","GR-82","department","14437",CountryCode.GREECE),
    GR_16("Lakonia","GR-16","department","14439",CountryCode.GREECE),
    GR_42("Larisa","GR-42","department","14457",CountryCode.GREECE),
    GR_92("Lasithi","GR-92","department","14440",CountryCode.GREECE),
    GR_24("Lefkada","GR-24","department","14441",CountryCode.GREECE),
    GR_83("Lesvos","GR-83","department","14413",CountryCode.GREECE),
    GR_43("Magnisia","GR-43","department","14458",CountryCode.GREECE),
    GR_17("Messinia","GR-17","department","14442",CountryCode.GREECE),
    GR_59("Pella","GR-59","department","14443",CountryCode.GREECE),
    GR_61("Pieria","GR-61","department","14412",CountryCode.GREECE),
    GR_34("Preveza","GR-34","department","14444",CountryCode.GREECE),
    GR_93("Rethymno","GR-93","department","14459",CountryCode.GREECE),
    GR_73("Rodopi","GR-73","department","14445",CountryCode.GREECE),
    GR_84("Samos","GR-84","department","14411",CountryCode.GREECE),
    GR_62("Serres","GR-62","department","14446",CountryCode.GREECE),
    GR_32("Thesprotia","GR-32","department","14410",CountryCode.GREECE),
    GR_54("Thessaloniki","GR-54","department","14447",CountryCode.GREECE),
    GR_44("Trikala","GR-44","department","14409",CountryCode.GREECE),
    GR_03("Voiotia","GR-03","department","14448",CountryCode.GREECE),
    GR_72("Xanthi","GR-72","department","14408",CountryCode.GREECE),
    GR_21("Zakynthos","GR-21","department","14449",CountryCode.GREECE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_GR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
