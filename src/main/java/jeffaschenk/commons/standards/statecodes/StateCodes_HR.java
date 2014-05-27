package jeffaschenk.commons.standards.statecodes;

import jeffaschenk.commons.standards.CountryCode;

/**
 * Official ISO-3166 State/Province Codes
 * <p/>
 * Country: Croatia
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
@SuppressWarnings("unused")
public enum StateCodes_HR {
    /**
     * State Code Enumerator
     */
    HR_07("Bjelovarsko-bilogorska \u0382upanija","HR-07","county","13135", CountryCode.CROATIA),
    HR_12("Brodsko-posavska \u0382upanija","HR-12","county","13130",CountryCode.CROATIA),
    HR_19("Dubrovacko-neretvanska \u0382upanija","HR-19","county","13122",CountryCode.CROATIA),
    HR_21("Grad Zagreb","HR-21","city","13123",CountryCode.CROATIA),
    HR_18("Istarska \u0382upanija","HR-18","county","13121",CountryCode.CROATIA),
    HR_04("Karlovacka \u0382upanija","HR-04","county","13124",CountryCode.CROATIA),
    HR_06("Koprivnicko-kri\u0382evacka \u0382upanija","HR-06","county","13136",CountryCode.CROATIA),
    HR_02("Krapinsko-zagorska \u0382upanija","HR-02","county","13125",CountryCode.CROATIA),
    HR_09("Licko-senjska \u0382upanija","HR-09","county","13126",CountryCode.CROATIA),
    HR_20("Medimurska \u0382upanija","HR-20","county","13140",CountryCode.CROATIA),
    HR_14("Osjecko-baranjska \u0382upanija","HR-14","county","13127",CountryCode.CROATIA),
    HR_11("Po\u0382e\u0353ko-slavonska \u0382upanija","HR-11","county","13139",CountryCode.CROATIA),
    HR_08("Primorsko-goranska \u0382upanija","HR-08","county","13128",CountryCode.CROATIA),
    HR_03("Sisacko-moslavacka \u0382upanija","HR-03","county","13138",CountryCode.CROATIA),
    HR_17("Splitsko-dalmatinska \u0382upanija","HR-17","county","13137",CountryCode.CROATIA),
    HR_05("Vara\u0382dinska \u0382upanija","HR-05","county","13131",CountryCode.CROATIA),
    HR_10("Viroviticko-podravska \u0382upanija","HR-10","county","13132",CountryCode.CROATIA),
    HR_16("Vukovarsko-srijemska \u0382upanija","HR-16","county","13120",CountryCode.CROATIA),
    HR_13("Zadarska \u0382upanija","HR-13","county","13133",CountryCode.CROATIA),
    HR_01("Zagrebacka \u0382upanija","HR-01","county","13134",CountryCode.CROATIA),
    HR_15("\u0353ibensko-kninska \u0382upanija","HR-15","county","13129",CountryCode.CROATIA);

    // *************************************
    // Common Enum Structure for all
    // States of the World
    private final String stateProvinceName;

    private final String stateCode;

    private final String stateProvinceType;

    private final String stateNumericCode;

    private final CountryCode countryCode;


    StateCodes_HR(String stateProvinceName, String stateCode, String stateProvinceType, String stateNumericCode, CountryCode countryCode) {
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
