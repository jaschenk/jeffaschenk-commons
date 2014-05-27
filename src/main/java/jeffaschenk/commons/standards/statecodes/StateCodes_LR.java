package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Liberia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LR {
    /**
     * State Code Enumerator
     */
    LR_BM("Bomi","LR-BM","County","15152", CountryCode.LIBERIA),
    LR_BG("Bong","LR-BG","County","15148",CountryCode.LIBERIA),
    LR_X1("Gbarpolu","LR-X1","County","19021",CountryCode.LIBERIA),
    LR_GB("Grand Bassa","LR-GB","County","15153",CountryCode.LIBERIA),
    LR_CM("Grand Cape Mount","LR-CM","County","15156",CountryCode.LIBERIA),
    LR_GG("Grand Gedeh","LR-GG","County","15151",CountryCode.LIBERIA),
    LR_GK("Grand Kru","LR-GK","County","19023",CountryCode.LIBERIA),
    LR_LO("Lofa","LR-LO","County","15154",CountryCode.LIBERIA),
    LR_MG("Margibi","LR-MG","County","15158",CountryCode.LIBERIA),
    LR_MY("Maryland","LR-MY","County","15150",CountryCode.LIBERIA),
    LR_MO("Montserrado","LR-MO","County","15157",CountryCode.LIBERIA),
    LR_NI("Nimba","LR-NI","County","15155",CountryCode.LIBERIA),
    LR_X2("River Gee","LR-X2","County","19022",CountryCode.LIBERIA),
    LR_RI("Rivercess","LR-RI","County","15149",CountryCode.LIBERIA),
    LR_SI("Sinoe","LR-SI","County","15147",CountryCode.LIBERIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
