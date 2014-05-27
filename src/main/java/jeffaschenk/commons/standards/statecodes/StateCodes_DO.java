package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Dominican Republic
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_DO {
    /**
     * State Code Enumerator
     */
    DO_02("Azua","DO-02","province","13506", CountryCode.DOMINICAN_REPUBLIC),
    DO_03("Bahoruco","DO-03","province","13489",CountryCode.DOMINICAN_REPUBLIC),
    DO_04("Barahona","DO-04","province","13477",CountryCode.DOMINICAN_REPUBLIC),
    DO_05("Dajab\u0243n","DO-05","province","13490",CountryCode.DOMINICAN_REPUBLIC),
    DO_01("Distrito Nacional (Santo Domingo)","DO-01","province","13520",CountryCode.DOMINICAN_REPUBLIC),
    DO_06("Duarte","DO-06","province","13491",CountryCode.DOMINICAN_REPUBLIC),
    DO_08("El Seybo [El Seibo]","DO-08","province","13492",CountryCode.DOMINICAN_REPUBLIC),
    DO_09("Espaillat","DO-09","province","13493",CountryCode.DOMINICAN_REPUBLIC),
    DO_30("Hato Mayor","DO-30","province","13510",CountryCode.DOMINICAN_REPUBLIC),
    DO_10("Independencia","DO-10","province","13494",CountryCode.DOMINICAN_REPUBLIC),
    DO_11("La Altagracia","DO-11","province","13511",CountryCode.DOMINICAN_REPUBLIC),
    DO_07("La Estrelleta [El\u0237as Pi\u0241a]","DO-07","province","13509",CountryCode.DOMINICAN_REPUBLIC),
    DO_12("La Romana","DO-12","province","13487",CountryCode.DOMINICAN_REPUBLIC),
    DO_13("La Vega","DO-13","province","13512",CountryCode.DOMINICAN_REPUBLIC),
    DO_14("Mar\u0237a Trinidad S\u0225nchez","DO-14","province","13496",CountryCode.DOMINICAN_REPUBLIC),
    DO_28("Monse\u0241or Nouel","DO-28","province","13513",CountryCode.DOMINICAN_REPUBLIC),
    DO_15("Monte Cristi","DO-15","province","13497",CountryCode.DOMINICAN_REPUBLIC),
    DO_29("Monte Plata","DO-29","province","13514",CountryCode.DOMINICAN_REPUBLIC),
    DO_16("Pedernales","DO-16","province","13498",CountryCode.DOMINICAN_REPUBLIC),
    DO_17("Peravia","DO-17","province","13499",CountryCode.DOMINICAN_REPUBLIC),
    DO_18("Puerto Plata","DO-18","province","13515",CountryCode.DOMINICAN_REPUBLIC),
    DO_19("Salcedo","DO-19","province","13500",CountryCode.DOMINICAN_REPUBLIC),
    DO_20("Saman\u0225","DO-20","province","13516",CountryCode.DOMINICAN_REPUBLIC),
    DO_21("San Crist\u0243bal","DO-21","province","13517",CountryCode.DOMINICAN_REPUBLIC),
    DO_31("San Jose de Ocoa","DO-31","province","13502",CountryCode.DOMINICAN_REPUBLIC),
    DO_22("San Juan","DO-22","province","13518",CountryCode.DOMINICAN_REPUBLIC),
    DO_23("San Pedro de Macor\u0237s","DO-23","province","13503",CountryCode.DOMINICAN_REPUBLIC),
    DO_25("Santiago","DO-25","province","13519",CountryCode.DOMINICAN_REPUBLIC),
    DO_26("Santiago Rodr\u0237guez","DO-26","province","13504",CountryCode.DOMINICAN_REPUBLIC),
    DO_24("S\u0225nchez Ram\u0237rez","DO-24","province","13501",CountryCode.DOMINICAN_REPUBLIC),
    DO_27("Valverde","DO-27","province","13505",CountryCode.DOMINICAN_REPUBLIC);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_DO(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
