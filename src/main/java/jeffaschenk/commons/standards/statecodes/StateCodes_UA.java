package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Ukraine
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_UA {
    /**
     * State Code Enumerator
     */
    UA_71("Cherkas'ka Oblast'","UA-71","region","18594", CountryCode.UKRAINE),
    UA_74("Chernihivs'ka Oblast'","UA-74","region","18595",CountryCode.UKRAINE),
    UA_77("Chernivets'ka Oblast'","UA-77","region","18596",CountryCode.UKRAINE),
    UA_12("Dnipropetrovs'ka Oblast'","UA-12","region","18603",CountryCode.UKRAINE),
    UA_14("Donets'ka Oblast'","UA-14","region","18597",CountryCode.UKRAINE),
    UA_26("Ivano-Frankivs'ka Oblast'","UA-26","region","18598",CountryCode.UKRAINE),
    UA_63("Kharkivs'ka Oblast'","UA-63","region","18599",CountryCode.UKRAINE),
    UA_65("Khersons'ka Oblast'","UA-65","region","18600",CountryCode.UKRAINE),
    UA_68("Khmel'nyts'ka Oblast'","UA-68","region","18604",CountryCode.UKRAINE),
    UA_35("Kirovohrads'ka Oblast'","UA-35","region","18601",CountryCode.UKRAINE),
    UA_30("Ky\u0239v","UA-30","region","18607",CountryCode.UKRAINE),
    UA_32("Ky\u0239vs'ka Oblast'","UA-32","region","18608",CountryCode.UKRAINE),
    UA_46("L'vivs'ka Oblast'","UA-46","region","18609",CountryCode.UKRAINE),
    UA_09("Luhans'ka Oblast'","UA-09","region","18605",CountryCode.UKRAINE),
    UA_48("Mykola\u0239vs'ka Oblast'","UA-48","region","18610",CountryCode.UKRAINE),
    UA_51("Odes'ka Oblast'","UA-51","region","18606",CountryCode.UKRAINE),
    UA_53("Poltavs'ka Oblast'","UA-53","region","18611",CountryCode.UKRAINE),
    UA_43("Respublika Krym","UA-43","Autonomous Republic","18602",CountryCode.UKRAINE),
    UA_56("Rivnens'ka Oblast'","UA-56","region","18612",CountryCode.UKRAINE),
    UA_40("Sevastopol'","UA-40","region","18613",CountryCode.UKRAINE),
    UA_59("Sums'ka Oblast'","UA-59","region","18614",CountryCode.UKRAINE),
    UA_61("Ternopil's'ka Oblast'","UA-61","region","18615",CountryCode.UKRAINE),
    UA_05("Vinnyts'ka Oblast'","UA-05","region","18593",CountryCode.UKRAINE),
    UA_07("Volyns'ka Oblast'","UA-07","region","18617",CountryCode.UKRAINE),
    UA_21("Zakarpats'ka Oblast'","UA-21","region","18618",CountryCode.UKRAINE),
    UA_23("Zaporiz'ka Oblast'","UA-23","region","18616",CountryCode.UKRAINE),
    UA_18("Zhytomyrs'ka Oblast'","UA-18","region","18592",CountryCode.UKRAINE);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_UA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
