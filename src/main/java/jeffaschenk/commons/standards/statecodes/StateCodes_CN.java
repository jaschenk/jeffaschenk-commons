package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: China
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CN {
    /**
     * State Code Enumerator
     */
    CN_34("Anhui","CN-34","province","13012", CountryCode.CHINA),
        CN_92("Aomen (zh) ***","CN-92","special administrative region","13034",CountryCode.CHINA),
        CN_11("Beijing","CN-11","municipality","13013",CountryCode.CHINA),
        CN_50("Chongqing","CN-50","municipality","13014",CountryCode.CHINA),
        CN_35("Fujian","CN-35","province","13035",CountryCode.CHINA),
        CN_62("Gansu","CN-62","province","13015",CountryCode.CHINA),
        CN_44("Guangdong","CN-44","province","13016",CountryCode.CHINA),
        CN_45("Guangxi","CN-45","autonomous region","13036",CountryCode.CHINA),
        CN_52("Guizhou","CN-52","province","13017",CountryCode.CHINA),
        CN_46("Hainan","CN-46","province","13018",CountryCode.CHINA),
        CN_13("Hebei","CN-13","province","13019",CountryCode.CHINA),
        CN_23("Heilongjiang","CN-23","province","13028",CountryCode.CHINA),
        CN_41("Henan","CN-41","province","13029",CountryCode.CHINA),
        CN_42("Hubei","CN-42","province","13020",CountryCode.CHINA),
        CN_43("Hunan","CN-43","province","13030",CountryCode.CHINA),
        CN_32("Jiangsu","CN-32","province","13031",CountryCode.CHINA),
        CN_36("Jiangxi","CN-36","province","13021",CountryCode.CHINA),
        CN_22("Jilin","CN-22","province","13022",CountryCode.CHINA),
        CN_21("Liaoning","CN-21","province","13023",CountryCode.CHINA),
        CN_15("Nei Mongol (mn)","CN-15","autonomous region","13024",CountryCode.CHINA),
        CN_64("Ningxia","CN-64","autonomous region","13025",CountryCode.CHINA),
        CN_63("Qinghai","CN-63","province","13026",CountryCode.CHINA),
        CN_61("Shaanxi","CN-61","province","13027",CountryCode.CHINA),
        CN_37("Shandong","CN-37","province","13011",CountryCode.CHINA),
        CN_31("Shanghai","CN-31","municipality","13037",CountryCode.CHINA),
        CN_14("Shanxi","CN-14","province","13010",CountryCode.CHINA),
        CN_51("Sichuan","CN-51","province","13038",CountryCode.CHINA),
        CN_71("Taiwan *","CN-71","province","18995",CountryCode.CHINA),
        CN_12("Tianjin","CN-12","municipality","13009",CountryCode.CHINA),
        CN_91("Xianggang (zh) **","CN-91","special administrative region","13039",CountryCode.CHINA),
        CN_65("Xinjiang","CN-65","autonomous region","13008",CountryCode.CHINA),
        CN_54("Xizang","CN-54","autonomous region","13032",CountryCode.CHINA),
        CN_53("Yunnan","CN-53","province","13033",CountryCode.CHINA),
        CN_33("Zhejiang","CN-33","province","13007",CountryCode.CHINA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CN(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
