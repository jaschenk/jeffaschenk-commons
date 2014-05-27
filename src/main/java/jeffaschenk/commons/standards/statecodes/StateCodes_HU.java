package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Hungary
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_HU {
    /**
     * State Code Enumerator
     */
    HU_BA("Baranya","HU-BA","county","14646", CountryCode.HUNGARY),
    HU_BZ("Borsod-Aba\u0250j-Zempl\u0233n","HU-BZ","county","14660",CountryCode.HUNGARY),
    HU_BU("Budapest","HU-BU","capital city","14649",CountryCode.HUNGARY),
    HU_BK("B\u0225cs-Kiskun","HU-BK","county","14647",CountryCode.HUNGARY),
    HU_BE("B\u0233k\u0233s","HU-BE","county","14648",CountryCode.HUNGARY),
    HU_BC("B\u0233k\u0233scsaba","HU-BC","city of county right","19442",CountryCode.HUNGARY),
    HU_CS("Csongr\u0225d","HU-CS","county","14650",CountryCode.HUNGARY),
    HU_DE("Debrecen","HU-DE","city of county right","19443",CountryCode.HUNGARY),
    HU_DU("Duna\u0250jv\u0225ros","HU-DU","city of county right","19444",CountryCode.HUNGARY),
    HU_EG("Eger","HU-EG","city of county right","19445",CountryCode.HUNGARY),
    HU_X1("Erd","HU-X1","County","21352",CountryCode.HUNGARY),
    HU_FE("Fej\u0233r","HU-FE","county","14645",CountryCode.HUNGARY),
    HU_GY("Gyor","HU-GY","city of county right","19446",CountryCode.HUNGARY),
    HU_GS("Gyor-Moson-Sopron","HU-GS","county","14651",CountryCode.HUNGARY),
    HU_HB("Hajd\u0250-Bihar","HU-HB","county","14661",CountryCode.HUNGARY),
    HU_HE("Heves","HU-HE","county","14652",CountryCode.HUNGARY),
    HU_HV("H\u0243dmezov\u0225s\u0225rhely","HU-HV","city of county right","19447",CountryCode.HUNGARY),
    HU_JN("J\u0225sz-Nagykun-Szolnok","HU-JN","county","14653",CountryCode.HUNGARY),
    HU_KV("Kaposv\u0225r","HU-KV","city of county right","19448",CountryCode.HUNGARY),
    HU_KM("Kecskem\u0233t","HU-KM","city of county right","19449",CountryCode.HUNGARY),
    HU_KE("Kom\u0225rom-Esztergom","HU-KE","county","14644",CountryCode.HUNGARY),
    HU_MI("Miskolc","HU-MI","city of county right","19450",CountryCode.HUNGARY),
    HU_NK("Nagykanizsa","HU-NK","city of county right","19451",CountryCode.HUNGARY),
    HU_NY("Ny\u0237regyh\u0225za","HU-NY","city of county right","19452",CountryCode.HUNGARY),
    HU_NO("N\u0243gr\u0225d","HU-NO","county","14654",CountryCode.HUNGARY),
    HU_PE("Pest","HU-PE","county","14643",CountryCode.HUNGARY),
    HU_PS("P\u0233cs","HU-PS","city of county right","19453",CountryCode.HUNGARY),
    HU_ST("Salg\u0243tarj\u0225n","HU-ST","city of county right","19454",CountryCode.HUNGARY),
    HU_SO("Somogy","HU-SO","county","14655",CountryCode.HUNGARY),
    HU_SN("Sopron","HU-SN","city of county right","19455",CountryCode.HUNGARY),
    HU_SZ("Szabolcs-Szatm\u0225r-Bereg","HU-SZ","county","14656",CountryCode.HUNGARY),
    HU_SD("Szeged","HU-SD","city of county right","19456",CountryCode.HUNGARY),
    HU_SS("Szeksz\u0225rd","HU-SS","city of county right","19458",CountryCode.HUNGARY),
    HU_SK("Szolnok","HU-SK","city of county right","19459",CountryCode.HUNGARY),
    HU_SH("Szombathely","HU-SH","city of county right","19460",CountryCode.HUNGARY),
    HU_SF("Sz\u0233kesfeh\u0233rv\u0225r","HU-SF","city of county right","19457",CountryCode.HUNGARY),
    HU_TB("Tatab\u0225nya","HU-TB","city of county right","19461",CountryCode.HUNGARY),
    HU_TO("Tolna","HU-TO","county","14642",CountryCode.HUNGARY),
    HU_VA("Vas","HU-VA","county","14657",CountryCode.HUNGARY),
    HU_VE("Veszpr\u0233m","HU-VE","county","14658",CountryCode.HUNGARY),
    HU_VM("Veszpr\u0233m City","HU-VM","city of county right","19462",CountryCode.HUNGARY),
    HU_ZA("Zala","HU-ZA","county","14641",CountryCode.HUNGARY),
    HU_ZE("Zalaegerszeg","HU-ZE","city of county right","19463",CountryCode.HUNGARY);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_HU(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
