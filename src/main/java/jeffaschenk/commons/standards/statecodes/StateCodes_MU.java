package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Mauritius
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_MU {
    /**
     * State Code Enumerator
     */
    MU_AG("Agalega Islands","MU-AG","dependency","19026", CountryCode.MAURITIUS),
    MU_BR("Beau Bassin-Rose Hill","MU-BR","city","19027",CountryCode.MAURITIUS),
    MU_BL("Black River","MU-BL","district","15392",CountryCode.MAURITIUS),
    MU_CC("Cargados Carajos Shoals [Saint Brandon Islands]","MU-CC","dependency","19028",CountryCode.MAURITIUS),
    MU_CU("Curepipe","MU-CU","city","19029",CountryCode.MAURITIUS),
    MU_FL("Flacq","MU-FL","district","15395",CountryCode.MAURITIUS),
    MU_GP("Grand Port","MU-GP","district","15393",CountryCode.MAURITIUS),
    MU_MO("Moka","MU-MO","district","15394",CountryCode.MAURITIUS),
    MU_PA("Pamplemousses","MU-PA","district","15396",CountryCode.MAURITIUS),
    MU_PW("Plaines Wilhems","MU-PW","district","15391",CountryCode.MAURITIUS),
    MU_PL("Port Louis City","MU-PL","district","15397",CountryCode.MAURITIUS),
    MU_PU("Port Louis District","MU-PU","city","19030",CountryCode.MAURITIUS),
    MU_QB("Quatre Bornes","MU-QB","city","19033",CountryCode.MAURITIUS),
    MU_RR("Rivi\u0232re du Rempart","MU-RR","district","15399",CountryCode.MAURITIUS),
    MU_RO("Rodrigues Island","MU-RO","dependency","15398",CountryCode.MAURITIUS),
    MU_SA("Savanne","MU-SA","district","15390",CountryCode.MAURITIUS),
    MU_VP("Vacoas-Phoenix","MU-VP","city","19032",CountryCode.MAURITIUS);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_MU(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
