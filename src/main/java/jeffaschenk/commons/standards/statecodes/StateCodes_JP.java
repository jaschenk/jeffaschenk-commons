package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Japan
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_JP {
    /**
     * State Code Enumerator
     */
    JP_23("Aiti [Aichi]","JP-23","Perfecture","14931", CountryCode.JAPAN),
    JP_05("Akita","JP-05","Perfecture","14954",CountryCode.JAPAN),
    JP_02("Aomori","JP-02","Perfecture","14932",CountryCode.JAPAN),
    JP_38("Ehime","JP-38","Perfecture","14955",CountryCode.JAPAN),
    JP_21("Gihu [Gifu]","JP-21","Perfecture","14936",CountryCode.JAPAN),
    JP_10("Gunma","JP-10","Perfecture","14937",CountryCode.JAPAN),
    JP_34("Hirosima [Hiroshima]","JP-34","Perfecture","14938",CountryCode.JAPAN),
    JP_01("Hokkaid\u0244 [Hokkaido]","JP-01","Territory","14957",CountryCode.JAPAN),
    JP_18("Hukui [Fukui]","JP-18","Perfecture","14934",CountryCode.JAPAN),
    JP_40("Hukuoka [Fukuoka]","JP-40","Perfecture","14935",CountryCode.JAPAN),
    JP_07("Hukusima [Fukushima]","JP-07","Perfecture","14956",CountryCode.JAPAN),
    JP_28("Hy\u0244go [Hyogo]","JP-28","Perfecture","14939",CountryCode.JAPAN),
    JP_08("Ibaraki","JP-08","Perfecture","14958",CountryCode.JAPAN),
    JP_17("Isikawa [Ishikawa]","JP-17","Perfecture","14940",CountryCode.JAPAN),
    JP_03("Iwate","JP-03","Perfecture","14941",CountryCode.JAPAN),
    JP_37("Kagawa","JP-37","Perfecture","14942",CountryCode.JAPAN),
    JP_46("Kagosima [Kagoshima]","JP-46","Perfecture","14959",CountryCode.JAPAN),
    JP_14("Kanagawa","JP-14","Perfecture","14943",CountryCode.JAPAN),
    JP_43("Kumamoto","JP-43","Perfecture","14960",CountryCode.JAPAN),
    JP_26("Ky\u0244to [Kyoto]","JP-26","Urban Perfecture","14945",CountryCode.JAPAN),
    JP_39("K\u0244ti [Kochi]","JP-39","Perfecture","14944",CountryCode.JAPAN),
    JP_24("Mie","JP-24","Perfecture","14946",CountryCode.JAPAN),
    JP_04("Miyagi","JP-04","Perfecture","14921",CountryCode.JAPAN),
    JP_45("Miyazaki","JP-45","Perfecture","14947",CountryCode.JAPAN),
    JP_20("Nagano","JP-20","Perfecture","14948",CountryCode.JAPAN),
    JP_42("Nagasaki","JP-42","Perfecture","14961",CountryCode.JAPAN),
    JP_29("Nara","JP-29","Perfecture","14949",CountryCode.JAPAN),
    JP_15("Niigata","JP-15","Perfecture","14950",CountryCode.JAPAN),
    JP_33("Okayama","JP-33","Perfecture","14951",CountryCode.JAPAN),
    JP_47("Okinawa","JP-47","Perfecture","14952",CountryCode.JAPAN),
    JP_41("Saga","JP-41","Perfecture","14953",CountryCode.JAPAN),
    JP_11("Saitama","JP-11","Perfecture","14922",CountryCode.JAPAN),
    JP_25("Siga [Shiga]","JP-25","Perfecture","14919",CountryCode.JAPAN),
    JP_32("Simane [Shimane]","JP-32","Perfecture","14923",CountryCode.JAPAN),
    JP_22("Sizuoka [Shizuoka]","JP-22","Perfecture","14924",CountryCode.JAPAN),
    JP_12("Tiba [Chiba]","JP-12","Perfecture","14933",CountryCode.JAPAN),
    JP_36("Tokusima [Tokushima]","JP-36","Perfecture","14925",CountryCode.JAPAN),
    JP_09("Totigi [Tochigi]","JP-09","Perfecture","14918",CountryCode.JAPAN),
    JP_31("Tottori","JP-31","Perfecture","14917",CountryCode.JAPAN),
    JP_16("Toyama","JP-16","Perfecture","14927",CountryCode.JAPAN),
    JP_13("T\u0244ky\u0244 [Tokyo]","JP-13","Metropolis","14926",CountryCode.JAPAN),
    JP_30("Wakayama","JP-30","Perfecture","14928",CountryCode.JAPAN),
    JP_06("Yamagata","JP-06","Perfecture","14916",CountryCode.JAPAN),
    JP_35("Yamaguti [Yamaguchi]","JP-35","Perfecture","14929",CountryCode.JAPAN),
    JP_19("Yamanasi [Yamanashi]","JP-19","Perfecture","14930",CountryCode.JAPAN),
    JP_44("\u0244ita [Oita]","JP-44","Perfecture","14920",CountryCode.JAPAN),
    JP_27("\u0244saka [Osaka]","JP-27","Urban Perfecture","14962",CountryCode.JAPAN);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_JP(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
