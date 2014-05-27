package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Korea
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_KR {
    /**
     * State Code Enumerator
     */
    KR_26("Busan Gwang'yeogsi [Pusan-Kwangyokshi]", "KR-26", "metropolitan city", "20247", CountryCode.KOREA),
    KR_43("Chungcheongbugdo [Ch'ungch'ongbuk-do]", "KR-43", "province", "20255", CountryCode.KOREA),
    KR_44("Chungcheongnamdo [Ch'ungch'ongnam-do]", "KR-44", "province", "20256", CountryCode.KOREA),
    KR_27("Daegu Gwang'yeogsi [Taegu-Kwangyokshi]", "KR-27", "metropolitan city", "20248", CountryCode.KOREA),
    KR_30("Daejeon Gwang'yeogsi [Taejon-Kwangyokshi]", "KR-30", "metropolitan city", "20251", CountryCode.KOREA),
    KR_42("Gang'weondo [Kang-won-do]", "KR-42", "province", "15042", CountryCode.KOREA),
    KR_29("Gwangju Gwang'yeogsi [Kwangju-Kwangyokshi]", "KR-29", "metropolitan city", "20250", CountryCode.KOREA),
    KR_41("Gyeonggido [Kyonggi-do]", "KR-41", "province", "20253", CountryCode.KOREA),
    KR_47("Gyeongsangbugdo [Kyongsangbuk-do]", "KR-47", "province", "20259", CountryCode.KOREA),
    KR_48("Gyeongsangnamdo [Kyongsangnam-do]", "KR-48", "province", "20260", CountryCode.KOREA),
    KR_28("Incheon Gwang'yeogsi [Inch'n-Kwangyokshi]", "KR-28", "metropolitan city", "20249", CountryCode.KOREA),
    KR_49("Jejudo [Cheju-do]", "KR-49", "province", "20261", CountryCode.KOREA),
    KR_45("Jeonrabugdo[Chollabuk-do]", "KR-45", "province", "20257", CountryCode.KOREA),
    KR_46("Jeonranamdo [Chollanam-do]", "KR-46", "province", "20258", CountryCode.KOREA),
    KR_11("Seoul Teugbyeolsi [Seoul-T'ukpyolshi]", "KR-11", "capital metropolitan city", "20246", CountryCode.KOREA),
    KR_31("Ulsan Gwang'yeogsi [Ulsan-Kwangyokshi]", "KR-31", "metropolitan city", "20252", CountryCode.KOREA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_KR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
