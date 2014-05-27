package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Czech Republic
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_CZ {
    /**
     * State Code Enumerator
     */
    CZ_JC("Jihocesk\u0253 kraj","CZ-JC","Region","13219", CountryCode.CZECH_REPUBLIC),
    CZ_JM("Jihomoravsk\u0253 kraj","CZ-JM","Region","13220",CountryCode.CZECH_REPUBLIC),
    CZ_KA("Karlovarsk\u0253 kraj","CZ-KA","Region","13183",CountryCode.CZECH_REPUBLIC),
    CZ_KR("Kr\u0225lov\u0233hradeck\u0253 kraj","CZ-KR","Region","13221",CountryCode.CZECH_REPUBLIC),
    CZ_LI("Libereck\u0253 kraj","CZ-LI","Region","13184",CountryCode.CZECH_REPUBLIC),
    CZ_MO("Moravskoslezsk\u0253 kraj","CZ-MO","Region","13222",CountryCode.CZECH_REPUBLIC),
    CZ_OL("Olomouck\u0253 kraj","CZ-OL","Region","13185",CountryCode.CZECH_REPUBLIC),
    CZ_PA("Pardubick\u0253 kraj","CZ-PA","Region","13223",CountryCode.CZECH_REPUBLIC),
    CZ_PL("Plzensk\u0253 kraj","CZ-PL","Region","13186",CountryCode.CZECH_REPUBLIC),
    CZ_PR("Praha, hlavn\u0237 mesto","CZ-PR","Region","13224",CountryCode.CZECH_REPUBLIC),
    CZ_ST("Stredocesk\u0253 kraj","CZ-ST","Region","13187",CountryCode.CZECH_REPUBLIC),
    CZ_VY("Vysocina","CZ-VY","Region","13188",CountryCode.CZECH_REPUBLIC),
    CZ_ZL("Zl\u0237nsk\u0253 kraj","CZ-ZL","Region","13226",CountryCode.CZECH_REPUBLIC),
    CZ_US("\u0218steck\u0253 kraj","CZ-US","Region","13225",CountryCode.CZECH_REPUBLIC);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_CZ(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
