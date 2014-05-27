package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Burkina Faso
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_BF {
    /**
     * State Code Enumerator
     */
    BF_BAL("Bal\u0233","BF-BAL","Province","12846", CountryCode.BURKINA_FASO),
    BF_BAM("Bam","BF-BAM","Province","12855",CountryCode.BURKINA_FASO),
    BF_BAN("Banwa","BF-BAN","Province","12847",CountryCode.BURKINA_FASO),
    BF_BAZ("Baz\u0232ga","BF-BAZ","Province","12856",CountryCode.BURKINA_FASO),
    BF_BGR("Bougouriba","BF-BGR","Province","12857",CountryCode.BURKINA_FASO),
    BF_BLG("Boulgou","BF-BLG","Province","12848",CountryCode.BURKINA_FASO),
    BF_BLK("Boulkiemd\u0233","BF-BLK","Province","12858",CountryCode.BURKINA_FASO),
    BF_COM("Como\u0233","BF-COM","Province","12849",CountryCode.BURKINA_FASO),
    BF_GAN("Ganzourgou","BF-GAN","Province","12859",CountryCode.BURKINA_FASO),
    BF_GNA("Gnagna","BF-GNA","Province","12860",CountryCode.BURKINA_FASO),
    BF_GOU("Gourma","BF-GOU","Province","12850",CountryCode.BURKINA_FASO),
    BF_HOU("Houet","BF-HOU","Province","12861",CountryCode.BURKINA_FASO),
    BF_IOB("Ioba","BF-IOB","Province","12851",CountryCode.BURKINA_FASO),
    BF_KAD("Kadiogo","BF-KAD","Province","12862",CountryCode.BURKINA_FASO),
    BF_KMD("Komondjari","BF-KMD","Province","12863",CountryCode.BURKINA_FASO),
    BF_KMP("Kompienga","BF-KMP","Province","12864",CountryCode.BURKINA_FASO),
    BF_KOS("Kossi","BF-KOS","Province","12853",CountryCode.BURKINA_FASO),
    BF_KOP("Koulp\u0233logo","BF-KOP","Province","12865",CountryCode.BURKINA_FASO),
    BF_KOT("Kouritenga","BF-KOT","Province","12854",CountryCode.BURKINA_FASO),
    BF_KOW("Kourw\u0233ogo","BF-KOW","Province","12866",CountryCode.BURKINA_FASO),
    BF_KEN("K\u0233n\u0233dougou","BF-KEN","Province","12852",CountryCode.BURKINA_FASO),
    BF_LOR("Loroum","BF-LOR","Province","12837",CountryCode.BURKINA_FASO),
    BF_LER("L\u0233raba","BF-LER","Province","12867",CountryCode.BURKINA_FASO),
    BF_MOU("Mouhoun","BF-MOU","Province","12868",CountryCode.BURKINA_FASO),
    BF_NAO("Nahouri","BF-NAO","Province","12838",CountryCode.BURKINA_FASO),
    BF_NAM("Namentenga","BF-NAM","Province","12869",CountryCode.BURKINA_FASO),
    BF_NAY("Nayala","BF-NAY","Province","12870",CountryCode.BURKINA_FASO),
    BF_NOU("Noumbiel","BF-NOU","Province","12839",CountryCode.BURKINA_FASO),
    BF_OUB("Oubritenga","BF-OUB","Province","12871",CountryCode.BURKINA_FASO),
    BF_OUD("Oudalan","BF-OUD","Province","12840",CountryCode.BURKINA_FASO),
    BF_PAS("Passor\u0233","BF-PAS","Province","12872",CountryCode.BURKINA_FASO),
    BF_PON("Poni","BF-PON","Province","12873",CountryCode.BURKINA_FASO),
    BF_SNG("Sangui\u0233","BF-SNG","Province","12841",CountryCode.BURKINA_FASO),
    BF_SMT("Sanmatenga","BF-SMT","Province","12874",CountryCode.BURKINA_FASO),
    BF_SIS("Sissili","BF-SIS","Province","12843",CountryCode.BURKINA_FASO),
    BF_SOM("Soum","BF-SOM","Province","12844",CountryCode.BURKINA_FASO),
    BF_SOR("Sourou","BF-SOR","Province","12845",CountryCode.BURKINA_FASO),
    BF_SEN("S\u0233no","BF-SEN","Province","12842",CountryCode.BURKINA_FASO),
    BF_TAP("Tapoa","BF-TAP","Province","12879",CountryCode.BURKINA_FASO),
    BF_TUI("Tui","BF-TUI","Province","12880",CountryCode.BURKINA_FASO),
    BF_YAG("Yagha","BF-YAG","Province","12878",CountryCode.BURKINA_FASO),
    BF_YAT("Yatenga","BF-YAT","Province","12877",CountryCode.BURKINA_FASO),
    BF_ZIR("Ziro","BF-ZIR","Province","12876",CountryCode.BURKINA_FASO),
    BF_ZON("Zondoma","BF-ZON","Province","12881",CountryCode.BURKINA_FASO),
    BF_ZOU("Zoundw\u0233ogo","BF-ZOU","Province","12882",CountryCode.BURKINA_FASO);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_BF(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
