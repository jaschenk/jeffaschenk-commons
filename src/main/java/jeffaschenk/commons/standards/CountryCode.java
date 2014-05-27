package jeffaschenk.commons.standards;

import jeffaschenk.commons.standards.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Official ISO-3166 Country Codes
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 19, 2010
 *         Time: 12:12:11 PM
 */
public enum CountryCode {
    /**
     * County Code Enumerator Values
     */
    AFGHANISTAN("AFGHANISTAN", "AF", "AFG", "004"),
    ALAND_ISLANDS("ALAND ISLANDS", "AX", "ALA", "248"),
    ALBANIA("ALBANIA", "AL", "ALB", "008"),
    ALGERIA("ALGERIA", "DZ", "DZA", "012"),
    AMERICAN_SAMOA("AMERICAN SAMOA", "AS", "ASM", "016"),
    ANDORRA("ANDORRA", "AD", "AND", "020"),
    ANGOLA("ANGOLA", "AO", "AGO", "024"),
    ANGUILLA("ANGUILLA", "AI", "AIA", "660"),
    ANTARCTICA("ANTARCTICA", "AQ", "ATA", "010"),
    ANTIGUA_AND_BARBUDA("ANTIGUA AND BARBUDA", "AG", "ATG", "028"),
    ARGENTINA("ARGENTINA", "AR", "ARG", "032"),
    ARMENIA("ARMENIA", "AM", "ARM", "051"),
    ARUBA("ARUBA", "AW", "ABW", "533"),
    AUSTRALIA("AUSTRALIA", "AU", "AUS", "036"),
    AUSTRIA("AUSTRIA", "AT", "AUT", "040"),
    AZERBAIJAN("AZERBAIJAN", "AZ", "AZE", "031"),
    BAHAMAS("BAHAMAS", "BS", "BHS", "044"),
    BAHRAIN("BAHRAIN", "BH", "BHR", "048"),
    BANGLADESH("BANGLADESH", "BD", "BGD", "050"),
    BARBADOS("BARBADOS", "BB", "BRB", "052"),
    BELARUS("BELARUS", "BY", "BLR", "112"),
    BELGIUM("BELGIUM", "BE", "BEL", "056"),
    BELIZE("BELIZE", "BZ", "BLZ", "084"),
    BENIN("BENIN", "BJ", "BEN", "204"),
    BERMUDA("BERMUDA", "BM", "BMU", "060"),
    BHUTAN("BHUTAN", "BT", "BTN", "064"),
    BOLIVIA("BOLIVIA", "BO", "BOL", "068"),
    BOSNIA_AND_HERZEGOVINA("BOSNIA AND HERZEGOVINA", "BA", "BIH", "070"),
    BOTSWANA("BOTSWANA", "BW", "BWA", "072"),
    BOUVET_ISLAND("BOUVET ISLAND", "BV", "BVT", "074"),
    BRAZIL("BRAZIL", "BR", "BRA", "076"),
    BRITISH_INDIAN_OCEAN_TERRITORY("BRITISH INDIAN OCEAN TERRITORY", "IO", "IOT", "086"),
    BRUNEI_DARUSSALAM("BRUNEI DARUSSALAM", "BN", "BRN", "096"),
    BULGARIA("BULGARIA", "BG", "BGR", "100"),
    BURKINA_FASO("BURKINA FASO", "BF", "BFA", "854"),
    BURUNDI("BURUNDI", "BI", "BDI", "108"),
    CAMBODIA("CAMBODIA", "KH", "KHM", "116"),
    CAMEROON("CAMEROON", "CM", "CMR", "120"),
    CANADA("CANADA", "CA", "CAN", "124"),
    CAPE_VERDE("CAPE VERDE", "CV", "CPV", "132"),
    CAYMAN_ISLANDS("CAYMAN ISLANDS", "KY", "CYM", "136"),
    CENTRAL_AFRICAN_REPUBLIC("CENTRAL AFRICAN REPUBLIC", "CF", "CAF", "140"),
    CHAD("CHAD", "TD", "TCD", "148"),
    CHILE("CHILE", "CL", "CHL", "152"),
    CHINA("CHINA", "CN", "CHN", "156"),
    CHRISTMAS_ISLAND("CHRISTMAS ISLAND", "CX", "CXR", "162"),
    COCOS__KEELING__ISLANDS("COCOS (KEELING) ISLANDS", "CC", "CCK", "166"),
    COLOMBIA("COLOMBIA", "CO", "COL", "170"),
    COMOROS("COMOROS", "KM", "COM", "174"),
    CONGO("CONGO", "CG", "COG", "178"),
    THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO("CONGO, THE DEMOCRATIC REPUBLIC OF THE", "CD", "COD", "180"),
    COOK_ISLANDS("COOK ISLANDS", "CK", "COK", "184"),
    COSTA_RICA("COSTA RICA", "CR", "CRI", "188"),
    COTE_D_IVOIRE("COTE D'IVOIRE", "CI", "CIV", "384"),
    CROATIA("CROATIA", "HR", "HRV", "191"),
    CUBA("CUBA", "CU", "CUB", "192"),
    CYPRUS("CYPRUS", "CY", "CYP", "196"),
    CZECH_REPUBLIC("CZECH REPUBLIC", "CZ", "CZE", "203"),
    DENMARK("DENMARK", "DK", "DNK", "208"),
    DJIBOUTI("DJIBOUTI", "DJ", "DJI", "262"),
    DOMINICA("DOMINICA", "DM", "DMA", "212"),
    DOMINICAN_REPUBLIC("DOMINICAN REPUBLIC", "DO", "DOM", "214"),
    ECUADOR("ECUADOR", "EC", "ECU", "218"),
    EGYPT("EGYPT", "EG", "EGY", "818"),
    EL_SALVADOR("EL SALVADOR", "SV", "SLV", "222"),
    EQUATORIAL_GUINEA("EQUATORIAL GUINEA", "GQ", "GNQ", "226"),
    ERITREA("ERITREA", "ER", "ERI", "232"),
    ESTONIA("ESTONIA", "EE", "EST", "233"),
    ETHIOPIA("ETHIOPIA", "ET", "ETH", "231"),
    FALKLAND_ISLANDS_MALVINAS("FALKLAND ISLANDS (MALVINAS)", "FK", "FLK", "238"),
    FAROE_ISLANDS("FAROE ISLANDS", "FO", "FRO", "234"),
    FIJI("FIJI", "FJ", "FJI", "242"),
    FINLAND("FINLAND", "FI", "FIN", "246"),
    FRANCE("FRANCE", "FR", "FRA", "250"),
    FRENCH_GUIANA("FRENCH GUIANA", "GF", "GUF", "254"),
    FRENCH_POLYNESIA("FRENCH POLYNESIA", "PF", "PYF", "258"),
    FRENCH_SOUTHERN_TERRITORIES("FRENCH SOUTHERN TERRITORIES", "TF", "ATF", "260"),
    GABON("GABON", "GA", "GAB", "266"),
    GAMBIA("GAMBIA", "GM", "GMB", "270"),
    GEORGIA("GEORGIA", "GE", "GEO", "268"),
    GERMANY("GERMANY", "DE", "DEU", "276"),
    GHANA("GHANA", "GH", "GHA", "288"),
    GIBRALTAR("GIBRALTAR", "GI", "GIB", "292"),
    GREECE("GREECE", "GR", "GRC", "300"),
    GREENLAND("GREENLAND", "GL", "GRL", "304"),
    GRENADA("GRENADA", "GD", "GRD", "308"),
    GUADELOUPE("GUADELOUPE", "GP", "GLP", "312"),
    GUAM("GUAM", "GU", "GUM", "316"),
    GUATEMALA("GUATEMALA", "GT", "GTM", "320"),
    GUINEA("GUINEA", "GN", "GIN", "324"),
    GUINEA_BISSAU("GUINEA-BISSAU", "GW", "GNB", "624"),
    GUYANA("GUYANA", "GY", "GUY", "328"),
    HAITI("HAITI", "HT", "HTI", "332"),
    HEARD_ISLAND_AND_MCDONALD_ISLANDS("HEARD ISLAND AND MCDONALD ISLANDS", "HM", "HMD", "334"),
    HOLY_SEE_VATICAN_CITY_STATE_("HOLY SEE (VATICAN CITY STATE)", "VA", "VAT", "336"),
    HONDURAS("HONDURAS", "HN", "HND", "340"),
    HONG_KONG("HONG KONG", "HK", "HKG", "344"),
    HUNGARY("HUNGARY", "HU", "HUN", "348"),
    ICELAND("ICELAND", "IS", "ISL", "352"),
    INDIA("INDIA", "IN", "IND", "356"),
    INDONESIA("INDONESIA", "ID", "IDN", "360"),
    IRAN("IRAN, ISLAMIC REPUBLIC OF", "IR", "IRN", "364"),
    IRAQ("IRAQ", "IQ", "IRQ", "368"),
    IRELAND("IRELAND", "IE", "IRL", "372"),
    ISRAEL("ISRAEL", "IL", "ISR", "376"),
    ITALY("ITALY", "IT", "ITA", "380"),
    JAMAICA("JAMAICA", "JM", "JAM", "388"),
    JAPAN("JAPAN", "JP", "JPN", "392"),
    JORDAN("JORDAN", "JO", "JOR", "400"),
    KAZAKHSTAN("KAZAKHSTAN", "KZ", "KAZ", "398"),
    KENYA("KENYA", "KE", "KEN", "404"),
    KIRIBATI("KIRIBATI", "KI", "KIR", "296"),
    DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", "KP", "PRK", "408"),
    KOREA("KOREA, REPUBLIC OF", "KR", "KOR", "410"),
    KUWAIT("KUWAIT", "KW", "KWT", "414"),
    KYRGYZSTAN("KYRGYZSTAN", "KG", "KGZ", "417"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("LAO PEOPLE'S DEMOCRATIC REPUBLIC", "LA", "LAO", "418"),
    LATVIA("LATVIA", "LV", "LVA", "428"),
    LEBANON("LEBANON", "LB", "LBN", "422"),
    LESOTHO("LESOTHO", "LS", "LSO", "426"),
    LIBERIA("LIBERIA", "LR", "LBR", "430"),
    LIBYAN_ARAB_JAMAHIRIYA("LIBYAN ARAB JAMAHIRIYA", "LY", "LBY", "434"),
    LIECHTENSTEIN("LIECHTENSTEIN", "LI", "LIE", "438"),
    LITHUANIA("LITHUANIA", "LT", "LTU", "440"),
    LUXEMBOURG("LUXEMBOURG", "LU", "LUX", "442"),
    MACAO("MACAO", "MO", "MAC", "446"),
    MACEDONIA("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", "MK", "MKD", "807 "),
    MADAGASCAR("MADAGASCAR", "MG", "MDG", "450"),
    MALAWI("MALAWI", "MW", "MWI", "454"),
    MALAYSIA("MALAYSIA", "MY", "MYS", "458"),
    MALDIVES("MALDIVES", "MV", "MDV", "462"),
    MALI("MALI", "ML", "MLI", "466"),
    MALTA("MALTA", "MT", "MLT", "470"),
    MARSHALL_ISLANDS("MARSHALL ISLANDS", "MH", "MHL", "584"),
    MARTINIQUE("MARTINIQUE", "MQ", "MTQ", "474"),
    MAURITANIA("MAURITANIA", "MR", "MRT", "478"),
    MAURITIUS("MAURITIUS", "MU", "MUS", "480"),
    MAYOTTE("MAYOTTE", "YT", "MYT", "175"),
    MEXICO("MEXICO", "MX", "MEX", "484"),
    MICRONESIA("MICRONESIA, FEDERATED STATES OF", "FM", "FSM", "583"),
    MOLDOVA("MOLDOVA, REPUBLIC OF", "MD", "MDA", "498"),
    MONACO("MONACO", "MC", "MCO", "492"),
    MONGOLIA("MONGOLIA", "MN", "MNG", "496"),
    MONTSERRAT("MONTSERRAT", "MS", "MSR", "500"),
    MOROCCO("MOROCCO", "MA", "MAR", "504"),
    MOZAMBIQUE("MOZAMBIQUE", "MZ", "MOZ", "508"),
    MYANMAR("MYANMAR", "MM", "MMR", "104"),
    NAMIBIA("NAMIBIA", "NA", "NAM", "516"),
    NAURU("NAURU", "NR", "NRU", "520"),
    NEPAL("NEPAL", "NP", "NPL", "524"),
    NETHERLANDS("NETHERLANDS", "NL", "NLD", "528"),
    NETHERLANDS_ANTILLES("NETHERLANDS ANTILLES", "AN", "ANT", "530"),
    NEW_CALEDONIA("NEW CALEDONIA", "NC", "NCL", "540"),
    NEW_ZEALAND("NEW ZEALAND", "NZ", "NZL", "554"),
    NICARAGUA("NICARAGUA", "NI", "NIC", "558"),
    NIGER("NIGER", "NE", "NER", "562"),
    NIGERIA("NIGERIA", "NG", "NGA", "566"),
    NIUE("NIUE", "NU", "NIU", "570"),
    NORFOLK_ISLAND("NORFOLK ISLAND", "NF", "NFK", "574"),
    NORTHERN_MARIANA_ISLANDS("NORTHERN MARIANA ISLANDS", "MP", "MNP", "580"),
    NORWAY("NORWAY", "NO", "NOR", "578"),
    OMAN("OMAN", "OM", "OMN", "512"),
    PAKISTAN("PAKISTAN", "PK", "PAK", "586"),
    PALAU("PALAU", "PW", "PLW", "585"),
    PALESTINIAN_TERRITORY("PALESTINIAN TERRITORY, OCCUPIED", "PS", "PSE", "275"),
    PANAMA("PANAMA", "PA", "PAN", "591"),
    PAPUA_NEW_GUINEA("PAPUA NEW GUINEA", "PG", "PNG", "598"),
    PARAGUAY("PARAGUAY", "PY", "PRY", "600"),
    PERU("PERU", "PE", "PER", "604"),
    PHILIPPINES("PHILIPPINES", "PH", "PHL", "608"),
    PITCAIRN("PITCAIRN", "PN", "PCN", "612"),
    POLAND("POLAND", "PL", "POL", "616"),
    PORTUGAL("PORTUGAL", "PT", "PRT", "620"),
    PUERTO_RICO("PUERTO RICO", "PR", "PRI", "630"),
    QATAR("QATAR", "QA", "QAT", "634"),
    REUNION("REUNION", "RE", "REU", "638"),
    ROMANIA("ROMANIA", "RO", "ROU", "642"),
    RUSSIAN_FEDERATION("RUSSIAN FEDERATION", "RU", "RUS", "643"),
    RWANDA("RWANDA", "RW", "RWA", "646"),
    SAINT_HELENA("SAINT HELENA", "SH", "SHN", "654"),
    SAINT_KITTS_AND_NEVIS("SAINT KITTS AND NEVIS", "KN", "KNA", "659"),
    SAINT_LUCIA("SAINT LUCIA", "LC", "LCA", "662"),
    SAINT_PIERRE_AND_MIQUELON("SAINT PIERRE AND MIQUELON", "PM", "SPM", "666"),
    SAINT_VINCENT_AND_THE_GRENADINES("SAINT VINCENT AND THE GRENADINES", "VC", "VCT", "670"),
    SAMOA("SAMOA", "WS", "WSM", "882"),
    SAN_MARINO("SAN MARINO", "SM", "SMR", "674"),
    SAO_TOME_AND_PRINCIPE("SAO TOME AND PRINCIPE", "ST", "STP", "678"),
    SAUDI_ARABIA("SAUDI ARABIA", "SA", "SAU", "682"),
    SENEGAL("SENEGAL", "SN", "SEN", "686"),
    SEYCHELLES("SEYCHELLES", "SC", "SYC", "690"),
    SIERRA_LEONE("SIERRA LEONE", "SL", "SLE", "694"),
    SINGAPORE("SINGAPORE", "SG", "SGP", "702"),
    SLOVAKIA("SLOVAKIA", "SK", "SVK", "703"),
    SLOVENIA("SLOVENIA", "SI", "SVN", "705"),
    SOLOMON_ISLANDS("SOLOMON ISLANDS", "SB", "SLB", "090"),
    SOMALIA("SOMALIA", "SO", "SOM", "706"),
    SOUTH_AFRICA("SOUTH AFRICA", "ZA", "ZAF", "710"),
    SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "GS", "SGS", "239"),
    SPAIN("SPAIN", "ES", "ESP", "724"),
    SRI_LANKA("SRI LANKA", "LK", "LKA", "144"),
    SUDAN("SUDAN", "SD", "SDN", "736"),
    SURINAME("SURINAME", "SR", "SUR", "740"),
    SVALBARD_AND_JAN_MAYEN("SVALBARD AND JAN MAYEN", "SJ", "SJM", "744"),
    SWAZILAND("SWAZILAND", "SZ", "SWZ", "748"),
    SWEDEN("SWEDEN", "SE", "SWE", "752"),
    SWITZERLAND("SWITZERLAND", "CH", "CHE", "756"),
    SYRIAN_ARAB_REPUBLIC("SYRIAN ARAB REPUBLIC", "SY", "SYR", "760"),
    TAIWAN("TAIWAN", "TW", "TWN", "158"),
    TAJIKISTAN("TAJIKISTAN", "TJ", "TJK", "762"),
    TANZANIA("TANZANIA, UNITED REPUBLIC OF", "TZ", "TZA", "834"),
    THAILAND("THAILAND", "TH", "THA", "764"),
    TIMOR_LESTE("TIMOR-LESTE", "TL", "TLS", "626"),
    TOGO("TOGO", "TG", "TGO", "768"),
    TOKELAU("TOKELAU", "TK", "TKL", "772"),
    TONGA("TONGA", "TO", "TON", "776"),
    TRINIDAD_AND_TOBAGO("TRINIDAD AND TOBAGO", "TT", "TTO", "780"),
    TUNISIA("TUNISIA", "TN", "TUN", "788"),
    TURKEY("TURKEY", "TR", "TUR", "792"),
    TURKMENISTAN("TURKMENISTAN", "TM", "TKM", "795"),
    TURKS_AND_CAICOS_ISLANDS("TURKS AND CAICOS ISLANDS", "TC", "TCA", "796"),
    TUVALU("TUVALU", "TV", "TUV", "798"),
    UGANDA("UGANDA", "UG", "UGA", "800"),
    UKRAINE("UKRAINE", "UA", "UKR", "804"),
    UNITED_ARAB_EMIRATES("UNITED ARAB EMIRATES", "AE", "ARE", "784"),
    UNITED_KINGDOM("UNITED KINGDOM", "GB", "GBR", "826"),
    UNITED_KINGDOM_ALT("UNITED KINGDOM", "UK", "GBR", "826"),
    UNITED_STATES("UNITED STATES", "US", "USA", "840"),
    UNITED_STATES_MINOR_OUTLYING_ISLANDS("UNITED STATES MINOR OUTLYING ISLANDS", "UM", "UMI", "581"),
    URUGUAY("URUGUAY", "UY", "URY", "858"),
    UZBEKISTAN("UZBEKISTAN", "UZ", "UZB", "860"),
    VANUATU("VANUATU", "VU", "VUT", "548"),
    VENEZUELA("VENEZUELA", "VE", "VEN", "862"),
    VIET_NAM("VIET NAM", "VN", "VNM", "704"),
    VIRGIN_ISLANDS_BRITISH("VIRGIN ISLANDS, BRITISH", "VG", "VGB", "092"),
    VIRGIN_ISLANDS_US("VIRGIN ISLANDS, U.S.", "VI", "VIR", "850"),
    WALLIS_AND_FUTUNA("WALLIS AND FUTUNA", "WF", "WLF", "876"),
    WESTERN_SAHARA("WESTERN SAHARA", "EH", "ESH", "732"),
    YEMEN("YEMEN", "YE", "YEM", "887"),
    SERBIA_AND_MONTENEGRO_YU("SERBIA AND MONTENEGRO YU", "YU", "SCG", "891"),
    SERBIA_AND_MONTENEGRO_CS("SERBIA AND MONTENEGRO CS", "CS", "SCG", "891"),
    ZAMBIA("ZAMBIA", "ZM", "ZMB", "894"),
    ZIMBABWE("ZIMBABWE", "ZW", "ZWE", "716"),
    JERSEY("JERSEY", "JE", "JEY", "832"),
    GUERNSEY("GUERNSEY", "GG", "GGY", "831"),
    ISLE_OF_MAN("ISLE OF MAN", "IM", "IMN", "833"),
    SERBIA("SERBIA", "RS", "SRB", "688"),
    MONTENEGRO("MONTENEGRO", "ME", "MNE", "499");


    private final String countryName;

    private final String alpha2Code;

    private final String alpha3Code;

    private final String numericCode;


    private CountryCode(String countryName, String alpha2Code, String alpha3Code, String numericCode) {
        this.countryName = countryName;
        this.alpha2Code = alpha2Code;
        this.alpha3Code = alpha3Code;
        this.numericCode = numericCode;
    }

    public String countryName() {
        return this.countryName;
    }

    public String alpha2Code() {
        return this.alpha2Code;
    }

    public String alpha3Code() {
        return this.alpha3Code;
    }

    public String numericCode() {
        return this.numericCode;
    }

    /**
     * Generates a JSON String Array for consumption in JavaScript.
     * @param filtered
     * @return
     */
    public static String getJSONString(boolean filtered) {
        StringBuffer sb = new StringBuffer(" var vCountryCode = { ");
        // Supply Immediate Countries
        sb.append("\"" + camelCaseString(UNITED_STATES.countryName()) + "\":" + "\"" + UNITED_STATES.alpha2Code() + "\"" + ",");
        sb.append("\"" + camelCaseString(CANADA.countryName()) + "\":" + "\"" + CANADA.alpha2Code() + "\"" + ",");
        sb.append("\"" + camelCaseString(UNITED_KINGDOM.countryName()) + "\":" + "\"" + UNITED_KINGDOM.alpha2Code() + "\"" + ",");
        // Insert a Separator
        sb.append("\"" + " " + "\":" + "\"" + "" + "\"" + ",");
        // Generate the all of the Countries now in Country Code Order.
        ccLoop:
        for (CountryCode cc : CountryCode.values()) {
            // If filtered, requested, exclude specific countries.
            if (filtered) {
                for (String excludedAlpha2Code : excludedCountries) {
                    if (excludedAlpha2Code.equalsIgnoreCase(cc.alpha2Code())) {
                        continue ccLoop;
                    }
                }
            }
            String countryName = cc.countryName();
            String countryCode = cc.alpha2Code();
            sb.append("\"" + camelCaseString(countryName) + "\":" + "\"" + countryCode + "\"" + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" };");
        return sb.toString();
    }

    public static Map<String, String> getCountryCodes() {
        Map<String, String> countryMap = new TreeMap<String, String>();
        for (CountryCode cc : CountryCode.values()) {
            countryMap.put(cc.alpha2Code(), cc.countryName());
        }
        return countryMap;
    }

    public static CountryCode getCountryCodeByAlpha2CountryCode(String alpha2Code) {
        if (StringUtils.isEmpty(alpha2Code)) {
            return null;
        }
        for (CountryCode cc : CountryCode.values()) {
            if (cc.alpha2Code().equalsIgnoreCase(alpha2Code)) {
                return cc;
            }
        }
        return null;
    }

    public static boolean isValidAlpha2CountryCode(String alpha2Code) {
        if (StringUtils.isEmpty(alpha2Code)) {
            return false;
        }
        for (CountryCode cc : CountryCode.values()) {
            if (cc.alpha2Code().equalsIgnoreCase(alpha2Code)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidAlpha3CountryCode(String alpha3Code) {
        if (StringUtils.isEmpty(alpha3Code)) {
            return false;
        }
        for (CountryCode cc : CountryCode.values()) {
            if (cc.alpha3Code().equalsIgnoreCase(alpha3Code)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidNumericCountryCode(String numericCode) {
        if (StringUtils.isEmpty(numericCode)) {
            return false;
        }
        for (CountryCode cc : CountryCode.values()) {
            if (cc.numericCode().equalsIgnoreCase(numericCode)) {
                return true;
            }
        }
        return false;
    }

    public static CountryCode getNamedCountryCode(String country) {
        for (CountryCode cc : CountryCode.values()) {
            if(cc.countryName().trim().equalsIgnoreCase(country))
           return cc;
        }
        return null;
    }

    private static String camelCaseString(String string) {
        if ((string == null) || (string.isEmpty())) {
            return string;
        } else {
            StringBuffer sb = new StringBuffer();
            for (String str : string.split(" ")) {
                if (str.equalsIgnoreCase("and")) {
                    sb.append(str.toLowerCase() + " ");
                } else if (str.charAt(0) == '(') {
                    sb.append(str.substring(0, 1) + StringUtils.capitalize(str.substring(1).toLowerCase()) + " ");
                } else {
                    sb.append(StringUtils.capitalize(str.toLowerCase()) + " ");
                }
            }
            return sb.toString().trim();
        }
    }

    /**
     * All Countries which have been excluded.
     */
    public static String[] excludedCountries = {
            AFGHANISTAN.alpha2Code(),
            ALAND_ISLANDS.alpha2Code(),
            ALBANIA.alpha2Code(),
            ALGERIA.alpha2Code(),
            ANDORRA.alpha2Code(),
            ANGOLA.alpha2Code(),
            ANGUILLA.alpha2Code(),
            ANTARCTICA.alpha2Code(),
            ANTIGUA_AND_BARBUDA.alpha2Code(),
            ARGENTINA.alpha2Code(),
            ARMENIA.alpha2Code(),
            ARUBA.alpha2Code(),
            AZERBAIJAN.alpha2Code(),
            BAHRAIN.alpha2Code(),
            BANGLADESH.alpha2Code(),
            BARBADOS.alpha2Code(),
            BELARUS.alpha2Code(),
            BELIZE.alpha2Code(),
            BENIN.alpha2Code(),
            BHUTAN.alpha2Code(),
            BOLIVIA.alpha2Code(),
            BOSNIA_AND_HERZEGOVINA.alpha2Code(),
            BOTSWANA.alpha2Code(),
            BOUVET_ISLAND.alpha2Code(),
            BRITISH_INDIAN_OCEAN_TERRITORY.alpha2Code(),
            BRUNEI_DARUSSALAM.alpha2Code(),
            BULGARIA.alpha2Code(),
            BURKINA_FASO.alpha2Code(),
            BURUNDI.alpha2Code(),
            CAMBODIA.alpha2Code(),
            CAMEROON.alpha2Code(),
            CAPE_VERDE.alpha2Code(),
            CAYMAN_ISLANDS.alpha2Code(),
            CENTRAL_AFRICAN_REPUBLIC.alpha2Code(),
            CHAD.alpha2Code(),
            CHILE.alpha2Code(),
            CHINA.alpha2Code(),
            CHRISTMAS_ISLAND.alpha2Code(),
            COCOS__KEELING__ISLANDS.alpha2Code(),
            COLOMBIA.alpha2Code(),
            COMOROS.alpha2Code(),
            CONGO.alpha2Code(),
            THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO.alpha2Code(),
            COOK_ISLANDS.alpha2Code(),
            COSTA_RICA.alpha2Code(),
            COTE_D_IVOIRE.alpha2Code(),
            CROATIA.alpha2Code(),
            CUBA.alpha2Code(),
            CYPRUS.alpha2Code(),
            CZECH_REPUBLIC.alpha2Code(),
            DJIBOUTI.alpha2Code(),
            DOMINICA.alpha2Code(),
            DOMINICAN_REPUBLIC.alpha2Code(),
            ECUADOR.alpha2Code(),
            EGYPT.alpha2Code(),
            EL_SALVADOR.alpha2Code(),
            EQUATORIAL_GUINEA.alpha2Code(),
            ERITREA.alpha2Code(),
            ESTONIA.alpha2Code(),
            ETHIOPIA.alpha2Code(),
            FALKLAND_ISLANDS_MALVINAS.alpha2Code(),
            FAROE_ISLANDS.alpha2Code(),
            FIJI.alpha2Code(),
            FRENCH_GUIANA.alpha2Code(),
            FRENCH_POLYNESIA.alpha2Code(),
            FRENCH_SOUTHERN_TERRITORIES.alpha2Code(),
            GABON.alpha2Code(),
            GAMBIA.alpha2Code(),
            GEORGIA.alpha2Code(),
            GHANA.alpha2Code(),
            GIBRALTAR.alpha2Code(),
            GREENLAND.alpha2Code(),
            GRENADA.alpha2Code(),
            GUADELOUPE.alpha2Code(),
            GUAM.alpha2Code(),
            GUATEMALA.alpha2Code(),
            GUINEA.alpha2Code(),
            GUINEA_BISSAU.alpha2Code(),
            GUYANA.alpha2Code(),
            HAITI.alpha2Code(),
            HEARD_ISLAND_AND_MCDONALD_ISLANDS.alpha2Code(),
            HOLY_SEE_VATICAN_CITY_STATE_.alpha2Code(),
            HONDURAS.alpha2Code(),
            HUNGARY.alpha2Code(),
            INDONESIA.alpha2Code(),
            IRAN.alpha2Code(),
            IRAQ.alpha2Code(),
            ISRAEL.alpha2Code(),
            JORDAN.alpha2Code(),
            KAZAKHSTAN.alpha2Code(),
            KENYA.alpha2Code(),
            KIRIBATI.alpha2Code(),
            DEMOCRATIC_PEOPLES_REPUBLIC_OF_KOREA.alpha2Code(),
            KUWAIT.alpha2Code(),
            KYRGYZSTAN.alpha2Code(),
            LAO_PEOPLES_DEMOCRATIC_REPUBLIC.alpha2Code(),
            LATVIA.alpha2Code(),
            LEBANON.alpha2Code(),
            LESOTHO.alpha2Code(),
            LIBERIA.alpha2Code(),
            LIBYAN_ARAB_JAMAHIRIYA.alpha2Code(),
            MACAO.alpha2Code(),
            MACEDONIA.alpha2Code(),
            MADAGASCAR.alpha2Code(),
            MALAWI.alpha2Code(),
            MALAYSIA.alpha2Code(),
            MALDIVES.alpha2Code(),
            MALI.alpha2Code(),
            MALTA.alpha2Code(),
            MARSHALL_ISLANDS.alpha2Code(),
            MARTINIQUE.alpha2Code(),
            MAURITANIA.alpha2Code(),
            MAURITIUS.alpha2Code(),
            MAYOTTE.alpha2Code(),
            MEXICO.alpha2Code(),
            MICRONESIA.alpha2Code(),
            MOLDOVA.alpha2Code(),
            MONACO.alpha2Code(),
            MONGOLIA.alpha2Code(),
            MONTSERRAT.alpha2Code(),
            MOROCCO.alpha2Code(),
            MOZAMBIQUE.alpha2Code(),
            MYANMAR.alpha2Code(),
            NAMIBIA.alpha2Code(),
            NAURU.alpha2Code(),
            NEPAL.alpha2Code(),
            NETHERLANDS_ANTILLES.alpha2Code(),
            NEW_CALEDONIA.alpha2Code(),
            NICARAGUA.alpha2Code(),
            NIGER.alpha2Code(),
            NIGERIA.alpha2Code(),
            NIUE.alpha2Code(),
            NORFOLK_ISLAND.alpha2Code(),
            NORTHERN_MARIANA_ISLANDS.alpha2Code(),
            OMAN.alpha2Code(),
            PAKISTAN.alpha2Code(),
            PALAU.alpha2Code(),
            PALESTINIAN_TERRITORY.alpha2Code(),
            PANAMA.alpha2Code(),
            PAPUA_NEW_GUINEA.alpha2Code(),
            PARAGUAY.alpha2Code(),
            PERU.alpha2Code(),
            PHILIPPINES.alpha2Code(),
            PITCAIRN.alpha2Code(),
            QATAR.alpha2Code(),
            REUNION.alpha2Code(),
            RWANDA.alpha2Code(),
            SAINT_HELENA.alpha2Code(),
            SAINT_KITTS_AND_NEVIS.alpha2Code(),
            SAINT_LUCIA.alpha2Code(),
            SAINT_PIERRE_AND_MIQUELON.alpha2Code(),
            SAINT_VINCENT_AND_THE_GRENADINES.alpha2Code(),
            SAMOA.alpha2Code(),
            SAN_MARINO.alpha2Code(),
            SAO_TOME_AND_PRINCIPE.alpha2Code(),
            SAUDI_ARABIA.alpha2Code(),
            SENEGAL.alpha2Code(),
            SEYCHELLES.alpha2Code(),
            SIERRA_LEONE.alpha2Code(),
            SINGAPORE.alpha2Code(),
            SLOVAKIA.alpha2Code(),
            SLOVENIA.alpha2Code(),
            SOLOMON_ISLANDS.alpha2Code(),
            SOMALIA.alpha2Code(),
            SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS.alpha2Code(),
            SRI_LANKA.alpha2Code(),
            SUDAN.alpha2Code(),
            SURINAME.alpha2Code(),
            SVALBARD_AND_JAN_MAYEN.alpha2Code(),
            SWAZILAND.alpha2Code(),
            SYRIAN_ARAB_REPUBLIC.alpha2Code(),
            TAIWAN.alpha2Code(),
            TAJIKISTAN.alpha2Code(),
            TANZANIA.alpha2Code(),
            THAILAND.alpha2Code(),
            TIMOR_LESTE.alpha2Code(),
            TOGO.alpha2Code(),
            TOKELAU.alpha2Code(),
            TONGA.alpha2Code(),
            TRINIDAD_AND_TOBAGO.alpha2Code(),
            TUNISIA.alpha2Code(),
            TURKMENISTAN.alpha2Code(),
            TURKS_AND_CAICOS_ISLANDS.alpha2Code(),
            TUVALU.alpha2Code(),
            UGANDA.alpha2Code(),
            UKRAINE.alpha2Code(),
            UNITED_ARAB_EMIRATES.alpha2Code(),
            URUGUAY.alpha2Code(),
            UZBEKISTAN.alpha2Code(),
            VANUATU.alpha2Code(),
            VENEZUELA.alpha2Code(),
            VIET_NAM.alpha2Code(),
            WALLIS_AND_FUTUNA.alpha2Code(),
            WESTERN_SAHARA.alpha2Code(),
            YEMEN.alpha2Code(),
            SERBIA_AND_MONTENEGRO_YU.alpha2Code(),
            SERBIA_AND_MONTENEGRO_CS.alpha2Code(),
            ZAMBIA.alpha2Code(),
            ZIMBABWE.alpha2Code(),
            JERSEY.alpha2Code(),
            GUERNSEY.alpha2Code(),
            ISLE_OF_MAN.alpha2Code(),
            SERBIA.alpha2Code(),
            MONTENEGRO.alpha2Code()
    };

}
