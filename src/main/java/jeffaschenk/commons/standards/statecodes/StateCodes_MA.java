package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Morocco
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MA {
    /**
     * State Code Enumerator
     */
    MA_HAO("Al Haouz","MA-HAO","province","15543", CountryCode.MOROCCO),
    MA_HOC("Al Hoce\u0239ma","MA-HOC","province","15516",CountryCode.MOROCCO),
    MA_ASZ("Assa-Zag","MA-ASZ","province","15541",CountryCode.MOROCCO),
    MA_AZI("Azilal","MA-AZI","province","15583",CountryCode.MOROCCO),
    MA_BES("Ben Slimane","MA-BES","province","15572",CountryCode.MOROCCO),
    MA_BEM("Beni Mellal","MA-BEM","province","15558",CountryCode.MOROCCO),
    MA_BER("Berkane","MA-BER","province","15526",CountryCode.MOROCCO),
    MA_BOD("Boujdour (EH)","MA-BOD","province","20173",CountryCode.MOROCCO),
    MA_BOM("Boulemane","MA-BOM","province","15576",CountryCode.MOROCCO),
    MA_CHE("Chefchaouen","MA-CHE","province","15559",CountryCode.MOROCCO),
    MA_CHI("Chichaoua","MA-CHI","province","15522",CountryCode.MOROCCO),
    MA_CHT("Chtouka-Ait Baha","MA-CHT","","48100",CountryCode.MOROCCO),
    MA_HAJ("El Hajeb","MA-HAJ","province","15524",CountryCode.MOROCCO),
    MA_JDI("El Jadida","MA-JDI","province","15575",CountryCode.MOROCCO),
    MA_ERR("Errachidia","MA-ERR","province","15547",CountryCode.MOROCCO),
    MA_ESM("Es Smara (EH)","MA-ESM","province","20174",CountryCode.MOROCCO),
    MA_ESI("Essaouira","MA-ESI","province","15523",CountryCode.MOROCCO),
    MA_FIG("Figuig","MA-FIG","province","15549",CountryCode.MOROCCO),
    MA_GUE("Guelmim","MA-GUE","province","15520",CountryCode.MOROCCO),
    MA_IFR("Ifrane","MA-IFR","province","15525",CountryCode.MOROCCO),
    MA_JRA("Jrada","MA-JRA","province","15550",CountryCode.MOROCCO),
    MA_KES("Kelaat es Sraghna","MA-KES","province","15544",CountryCode.MOROCCO),
    MA_KHE("Khemisset","MA-KHE","province","15552",CountryCode.MOROCCO),
    MA_KHN("Khenifra","MA-KHN","province","15548",CountryCode.MOROCCO),
    MA_KHO("Khouribga","MA-KHO","province","15573",CountryCode.MOROCCO),
    MA_KEN("K\u0233nitra","MA-KEN","province","15578",CountryCode.MOROCCO),
    MA_X1("Laayoune-Boujdour-Sakia El Hamra","MA-X1","","21378",CountryCode.MOROCCO),
    MA_LAR("Larache","MA-LAR","province","15560",CountryCode.MOROCCO),
    MA_LAA("La\u0224youne","MA-LAA","province","20175",CountryCode.MOROCCO),
    MA_MED("Mediouna","MA-MED","province","15539",CountryCode.MOROCCO),
    MA_MOU("Moulay Yacoub","MA-MOU","","15537",CountryCode.MOROCCO),
    MA_NAD("Nador","MA-NAD","province","15527",CountryCode.MOROCCO),
    MA_NOU("Nouaceur","MA-NOU","province","15580",CountryCode.MOROCCO),
    MA_OUA("Ouarzazate","MA-OUA","province","15582",CountryCode.MOROCCO),
    MA_OUD("Oued ed Dahab (EH)","MA-OUD","province","20176",CountryCode.MOROCCO),
    MA_SAF("Safi","MA-SAF","province","15535",CountryCode.MOROCCO),
    MA_SEF("Sefrou","MA-SEF","","15577",CountryCode.MOROCCO),
    MA_SET("Settat","MA-SET","province","15574",CountryCode.MOROCCO),
    MA_SIK("Sidi Kacem","MA-SIK","province","15538",CountryCode.MOROCCO),
    MA_TNT("Tan-Tan","MA-TNT","province","15542",CountryCode.MOROCCO),
    MA_TAO("Taounate","MA-TAO","province","15562",CountryCode.MOROCCO),
    MA_TAI("Taourirt","MA-TAI","province","15528",CountryCode.MOROCCO),
    MA_TAR("Taroudant","MA-TAR","province","15556",CountryCode.MOROCCO),
    MA_TAT("Tata","MA-TAT","province","15521",CountryCode.MOROCCO),
    MA_TAZ("Taza","MA-TAZ","province","15563",CountryCode.MOROCCO),
    MA_TIZ("Tiznit","MA-TIZ","province","15517",CountryCode.MOROCCO),
    MA_ZAG("Zagora","MA-ZAG","province","15557",CountryCode.MOROCCO);
    
    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MA(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
