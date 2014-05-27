package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Democratic Peoples Republic of Korea
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KP {
    /**
     * State Code Enumerator
     */
    KP_04("Chagang-do","KP-04","province","21370", CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_09("Hamgyong-bukdo","KP-09","province","21369",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_08("Hamgyong-namdo","KP-08","province","21368",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_06("Hwanghae-bukto","KP-06","province","21367",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_05("Hwanghae-namdo","KP-05","province","21366",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_07("Kangwon-do","KP-07","province","15053",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_13("Nason","KP-13","Special City","21371",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_03("Pyongan-bukdo","KP-03","province","21363",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_02("Pyongan-namdo","KP-02","province","21364",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_01("Pyongyang","KP-01","Capital City","21372",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA),
    KP_10("Yanggang-do","KP-10","province","21365",CountryCode.DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KP(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
