package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Latvia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_LV {
    /**
     * State Code Enumerator
     */
    LV_AI("Aizkraukles Aprinkis","LV-AI","district","15106", CountryCode.LATVIA),
    LV_AL("Aluksnes Aprinkis","LV-AL","district","15107",CountryCode.LATVIA),
    LV_BL("Balvu Aprinkis","LV-BL","district","15108",CountryCode.LATVIA),
    LV_BU("Bauskas Aprinkis","LV-BU","district","15109",CountryCode.LATVIA),
    LV_CE("Cesu Aprinkis","LV-CE","district","15110",CountryCode.LATVIA),
    LV_DGV("Daugavpils","LV-DGV","city","15111",CountryCode.LATVIA),
    LV_DA("Daugavpils Aprinkis","LV-DA","district","15112",CountryCode.LATVIA),
    LV_DO("Dobeles Aprinkis","LV-DO","district","15113",CountryCode.LATVIA),
    LV_GU("Gulbenes Aprinkis","LV-GU","district","15124",CountryCode.LATVIA),
    LV_JK("Jekabpils Aprinkis","LV-JK","district","15114",CountryCode.LATVIA),
    LV_JEL("Jelgava","LV-JEL","city","15125",CountryCode.LATVIA),
    LV_JL("Jelgavas Aprinkis","LV-JL","district","15126",CountryCode.LATVIA),
    LV_JUR("Jurmala","LV-JUR","city","15127",CountryCode.LATVIA),
    LV_KR("Kraslavas Aprinkis","LV-KR","district","15115",CountryCode.LATVIA),
    LV_KU("Kuldigas Aprinkis","LV-KU","district","15116",CountryCode.LATVIA),
    LV_LPX("Liepaja","LV-LPX","city","15128",CountryCode.LATVIA),
    LV_LE("Liepajas Aprinkis","LV-LE","district","15117",CountryCode.LATVIA),
    LV_LM("Limba\u0382u Aprinkis","LV-LM","district","15118",CountryCode.LATVIA),
    LV_LU("Ludzas Aprinkis","LV-LU","district","15123",CountryCode.LATVIA),
    LV_MA("Madonas Aprinkis","LV-MA","district","15119",CountryCode.LATVIA),
    LV_OG("Ogres Aprinkis","LV-OG","district","15098",CountryCode.LATVIA),
    LV_PR("Preilu Aprinkis","LV-PR","district","15122",CountryCode.LATVIA),
    LV_REZ("Rezekne","LV-REZ","city","15099",CountryCode.LATVIA),
    LV_RE("Rezeknes Aprinkis","LV-RE","district","15100",CountryCode.LATVIA),
    LV_RIX("Riga","LV-RIX","city","15121",CountryCode.LATVIA),
    LV_RI("Rigas Aprinkis","LV-RI","district","15101",CountryCode.LATVIA),
    LV_SA("Saldus Aprinkis","LV-SA","district","15102",CountryCode.LATVIA),
    LV_TA("Talsu Aprinkis","LV-TA","district","15129",CountryCode.LATVIA),
    LV_TU("Tukuma Aprinkis","LV-TU","district","15103",CountryCode.LATVIA),
    LV_VK("Valkas Aprinkis","LV-VK","district","15104",CountryCode.LATVIA),
    LV_VM("Valmieras Aprinkis","LV-VM","district","15130",CountryCode.LATVIA),
    LV_VEN("Ventspils","LV-VEN","city","15105",CountryCode.LATVIA),
    LV_VE("Ventspils Aprinkis","LV-VE","district","15097",CountryCode.LATVIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_LV(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
