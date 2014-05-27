package com.outbid.sor.commons.standards;

import jeffaschenk.commons.util.StringUtils;
import com.outbid.sor.model.orm.system.OlsonTimeZoneElement;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OUTBID
 * <p/>
 * TimeZone Enumeration
 * <p/>
 * "ID","UTC","UTCSUMMER","NATOALPHA","NATOPHONETIC","TIMEZONENAME","COUNTRY","COMMENTS"
 * <p/>
 * Created by IntelliJ IDEA.
 * User: jeffschenk
 * Date: Jul 26, 2010
 * Time: 11:54:11 AM
 */
@SuppressWarnings("unused")
public enum TimeZone {
    SYSTEM_DEFAULT(new OlsonTimeZoneElement(0,
            "",  // utc
            "",  // utcsummer,
            "",  // NatoAlpha,
            "",  // Natophonetic,
            "Default",  // Unique timezonename,
            "",  // country
            "")), //Comments
    Antarctica_Vostok(new OlsonTimeZoneElement(1, "UTC+00", "", "Z", "Zulu", "Antarctica/Vostok", "AQ", "Vostok Station, S Magnetic Pole")),
    Africa_Abidjan(new OlsonTimeZoneElement(3, "UTC+00", "", "Z", "Zulu", "Africa/Abidjan", "CI", "")),
    Africa_Accra(new OlsonTimeZoneElement(9, "UTC+00", "", "Z", "Zulu", "Africa/Accra", "GH", "")),
    Africa_Addis_Ababa(new OlsonTimeZoneElement(115, "UTC+03", "", "C", "Charlie", "Africa/Addis Ababa", "ET", "")),
    Africa_Algiers(new OlsonTimeZoneElement(45, "UTC+01", "", "A", "Alpha", "Africa/Algiers", "DZ", "")),
    Africa_Asmara(new OlsonTimeZoneElement(114, "UTC+03", "", "C", "Charlie", "Africa/Asmara", "ER", "")),
    Africa_Bamako(new OlsonTimeZoneElement(20, "UTC+00", "", "Z", "Zulu", "Africa/Bamako", "ML", "")),
    Africa_Bangui(new OlsonTimeZoneElement(38, "UTC+01", "", "A", "Alpha", "Africa/Bangui", "CF", "")),
    Africa_Banjul(new OlsonTimeZoneElement(11, "UTC+00", "", "Z", "Zulu", "Africa/Banjul", "GM", "")),
    Africa_Bissau(new OlsonTimeZoneElement(13, "UTC+00", "", "Z", "Zulu", "Africa/Bissau", "GW", "")),
    Africa_Blantyre(new OlsonTimeZoneElement(95, "UTC+02", "", "B", "Bravo", "Africa/Blantyre", "MW", "")),
    Africa_Brazzaville(new OlsonTimeZoneElement(39, "UTC+01", "", "A", "Alpha", "Africa/Brazzaville", "CG", "")),
    Africa_Bujumbura(new OlsonTimeZoneElement(78, "UTC+02", "", "B", "Bravo", "Africa/Bujumbura", "BI", "")),
    Africa_Cairo(new OlsonTimeZoneElement(84, "UTC+02", "", "B", "Bravo", "Africa/Cairo", "EG", "")),
    Africa_Casablanca(new OlsonTimeZoneElement(19, "UTC+00", "", "Z", "Zulu", "Africa/Casablanca", "MA", "")),
    Africa_Ceuta(new OlsonTimeZoneElement(47, "UTC+01", "UTC+02", "A", "Alpha", "Africa/Ceuta", "ES", "Ceuta & Melilla")),
    Africa_Conakry(new OlsonTimeZoneElement(12, "UTC+00", "", "Z", "Zulu", "Africa/Conakry", "GN", "")),
    Africa_Dakar(new OlsonTimeZoneElement(27, "UTC+00", "", "Z", "Zulu", "Africa/Dakar", "SN", "")),
    Africa_Dar_es_Salaam(new OlsonTimeZoneElement(128, "UTC+03", "", "C", "Charlie", "Africa/Dar es Salaam", "TZ", "")),
    Africa_Djibouti(new OlsonTimeZoneElement(113, "UTC+03", "", "C", "Charlie", "Africa/Djibouti", "DJ", "")),
    Africa_Douala(new OlsonTimeZoneElement(41, "UTC+01", "", "A", "Alpha", "Africa/Douala", "CM", "")),
    Africa_El_Aaiun(new OlsonTimeZoneElement(4, "UTC+00", "", "Z", "Zulu", "Africa/El Aaiun", "EH", "")),
    Africa_Freetown(new OlsonTimeZoneElement(26, "UTC+00", "", "Z", "Zulu", "Africa/Freetown", "SL", "")),
    Africa_Gaborone(new OlsonTimeZoneElement(79, "UTC+02", "", "B", "Bravo", "Africa/Gaborone", "BW", "")),
    Africa_Harare(new OlsonTimeZoneElement(110, "UTC+02", "", "B", "Bravo", "Africa/Harare", "ZW", "")),
    Africa_Johannesburg(new OlsonTimeZoneElement(108, "UTC+02", "", "B", "Bravo", "Africa/Johannesburg", "ZA", "")),
    Africa_Kampala(new OlsonTimeZoneElement(129, "UTC+03", "", "C", "Charlie", "Africa/Kampala", "UG", "")),
    Africa_Khartoum(new OlsonTimeZoneElement(126, "UTC+03", "", "C", "Charlie", "Africa/Khartoum", "SD", "")),
    Africa_Kigali(new OlsonTimeZoneElement(100, "UTC+02", "", "B", "Bravo", "Africa/Kigali", "RW", "")),
    Africa_Kinshasa(new OlsonTimeZoneElement(37, "UTC+01", "", "A", "Alpha", "Africa/Kinshasa", "CD", "west Dem. Rep. of Congo")),
    Africa_Lagos(new OlsonTimeZoneElement(63, "UTC+01", "", "A", "Alpha", "Africa/Lagos", "NG", "")),
    Africa_Libreville(new OlsonTimeZoneElement(49, "UTC+01", "", "A", "Alpha", "Africa/Libreville", "GA", "")),
    Africa_Lome(new OlsonTimeZoneElement(29, "UTC+00", "", "Z", "Zulu", "Africa/Lome", "TG", "")),
    Africa_Luanda(new OlsonTimeZoneElement(32, "UTC+01", "", "A", "Alpha", "Africa/Luanda", "AO", "")),
    Africa_Lubumbashi(new OlsonTimeZoneElement(81, "UTC+02", "", "B", "Bravo", "Africa/Lubumbashi", "CD", "east Dem. Rep. of Congo")),
    Africa_Lusaka(new OlsonTimeZoneElement(109, "UTC+02", "", "B", "Bravo", "Africa/Lusaka", "ZM", "")),
    Africa_Malabo(new OlsonTimeZoneElement(51, "UTC+01", "", "A", "Alpha", "Africa/Malabo", "GQ", "")),
    Africa_Maputo(new OlsonTimeZoneElement(96, "UTC+02", "", "B", "Bravo", "Africa/Maputo", "MZ", "")),
    Africa_Maseru(new OlsonTimeZoneElement(90, "UTC+02", "", "B", "Bravo", "Africa/Maseru", "LS", "")),
    Africa_Mbabane(new OlsonTimeZoneElement(102, "UTC+02", "", "B", "Bravo", "Africa/Mbabane", "SZ", "")),
    Africa_Mogadishu(new OlsonTimeZoneElement(127, "UTC+03", "", "C", "Charlie", "Africa/Mogadishu", "SO", "")),
    Africa_Monrovia(new OlsonTimeZoneElement(18, "UTC+00", "", "Z", "Zulu", "Africa/Monrovia", "LR", "")),
    Africa_Nairobi(new OlsonTimeZoneElement(117, "UTC+03", "", "C", "Charlie", "Africa/Nairobi", "KE", "")),
    Africa_Ndjamena(new OlsonTimeZoneElement(73, "UTC+01", "", "A", "Alpha", "Africa/Ndjamena", "TD", "")),
    Africa_Niamey(new OlsonTimeZoneElement(62, "UTC+01", "", "A", "Alpha", "Africa/Niamey", "NE", "")),
    Africa_Nouakchott(new OlsonTimeZoneElement(21, "UTC+00", "", "Z", "Zulu", "Africa/Nouakchott", "MR", "")),
    Africa_Ouagadougou(new OlsonTimeZoneElement(2, "UTC+00", "", "Z", "Zulu", "Africa/Ouagadougou", "BF", "")),
    Africa_Porto_Novo(new OlsonTimeZoneElement(36, "UTC+01", "", "A", "Alpha", "Africa/Porto-Novo", "BJ", "")),
    Africa_Sao_Tome(new OlsonTimeZoneElement(28, "UTC+00", "", "Z", "Zulu", "Africa/Sao Tome", "ST", "")),
    Africa_Tripoli(new OlsonTimeZoneElement(93, "UTC+02", "", "B", "Bravo", "Africa/Tripoli", "LY", "")),
    Africa_Tunis(new OlsonTimeZoneElement(74, "UTC+01", "UTC+02", "A", "Alpha", "Africa/Tunis", "TN", "")),
    Africa_Windhoek(new OlsonTimeZoneElement(61, "UTC+01", "UTC+02", "A", "Alpha", "Africa/Windhoek", "NA", "")),
    America_Adak(new OlsonTimeZoneElement(400, "UTC-10", "UTC-09", "W", "Whiskey", "America/Adak", "US", "Aleutian Islands")),
    America_Anchorage(new OlsonTimeZoneElement(391, "UTC-09", "UTC-08", "V", "Victor", "America/Anchorage", "US", "Alaska Time")),
    America_Anguilla(new OlsonTimeZoneElement(278, "UTC-04", "", "Q", "Quebec", "America/Anguilla", "AI", "")),
    America_Antigua(new OlsonTimeZoneElement(277, "UTC-04", "", "Q", "Quebec", "America/Antigua", "AG", "")),
    America_Araguaina(new OlsonTimeZoneElement(266, "UTC-03", "", "P", "Papa", "America/Araguaina", "BR", "Tocantins")),
    America_Argentina_Buenos_Aires(new OlsonTimeZoneElement(252, "UTC-03", "UTC-02", "P", "Papa", "America/Argentina/Buenos Aires", "AR", "Buenos Aires (BA, CF)")),
    America_Argentina_Catamarca(new OlsonTimeZoneElement(257, "UTC-03", "", "P", "Papa", "America/Argentina/Catamarca", "AR", "Catamarca (CT), Chubut (CH)")),
    America_Argentina_Cordoba(new OlsonTimeZoneElement(253, "UTC-03", "UTC-02", "P", "Papa", "America/Argentina/Cordoba", "AR", "most locations (CB, CC, CN, ER, FM, MN, SE, SF)")),
    America_Argentina_Jujuy(new OlsonTimeZoneElement(255, "UTC-03", "", "P", "Papa", "America/Argentina/Jujuy", "AR", "Jujuy (JY)")),
    America_Argentina_La_Rioja(new OlsonTimeZoneElement(258, "UTC-03", "", "P", "Papa", "America/Argentina/La Rioja", "AR", "La Rioja (LR)")),
    America_Argentina_Mendoza(new OlsonTimeZoneElement(260, "UTC-03", "", "P", "Papa", "America/Argentina/Mendoza", "AR", "Mendoza (MZ)")),
    America_Argentina_Rio_Gallegos(new OlsonTimeZoneElement(261, "UTC-03", "", "P", "Papa", "America/Argentina/Rio Gallegos", "AR", "Santa Cruz (SC)")),
    America_Argentina_Salta(new OlsonTimeZoneElement(254, "UTC-03", "", "P", "Papa", "America/Argentina/Salta", "AR", "(SA, LP, NQ, RN)")),
    America_Argentina_San_Juan(new OlsonTimeZoneElement(259, "UTC-03", "", "P", "Papa", "America/Argentina/San Juan", "AR", "San Juan (SJ)")),
    America_Argentina_San_Luis(new OlsonTimeZoneElement(281, "UTC-04", "UTC-03", "Q", "Quebec", "America/Argentina/San Luis", "AR", "San Luis (SL)")),
    America_Argentina_Tucuman(new OlsonTimeZoneElement(256, "UTC-03", "UTC-02", "P", "Papa", "America/Argentina/Tucuman", "AR", "Tucuman (TM)")),
    America_Argentina_Ushuaia(new OlsonTimeZoneElement(262, "UTC-03", "", "P", "Papa", "America/Argentina/Ushuaia", "AR", "Tierra del Fuego (TF)")),
    America_Aruba(new OlsonTimeZoneElement(282, "UTC-04", "", "Q", "Quebec", "America/Aruba", "AW", "")),
    America_Asuncion(new OlsonTimeZoneElement(314, "UTC-04", "UTC-03", "Q", "Quebec", "America/Asuncion", "PY", "")),
    America_Atikokan(new OlsonTimeZoneElement(327, "UTC-05", "", "R", "Romeo", "America/Atikokan", "CA", "Eastern Standard Time - Atikokan, Ontario and Southampton I, Nunavut")),
    America_Bahia(new OlsonTimeZoneElement(268, "UTC-03", "", "P", "Papa", "America/Bahia", "BR", "Bahia")),
    America_Barbados(new OlsonTimeZoneElement(283, "UTC-04", "", "Q", "Quebec", "America/Barbados", "BB", "")),
    America_Belem(new OlsonTimeZoneElement(263, "UTC-03", "", "P", "Papa", "America/Belem", "BR", "Amapa, E Para")),
    America_Belize(new OlsonTimeZoneElement(347, "UTC-06", "", "S", "Sierra", "America/Belize", "BZ", "")),
    America_Blanc_Sablon(new OlsonTimeZoneElement(299, "UTC-04", "", "Q", "Quebec", "America/Blanc-Sablon", "CA", "Atlantic Standard Time - Quebec - Lower North Shore")),
    America_Boa_Vista(new OlsonTimeZoneElement(290, "UTC-04", "", "Q", "Quebec", "America/Boa Vista", "BR", "Roraima")),
    America_Bogota(new OlsonTimeZoneElement(328, "UTC-05", "", "R", "Romeo", "America/Bogota", "CO", "")),
    America_Boise(new OlsonTimeZoneElement(380, "UTC-07", "UTC-06", "T", "Tango", "America/Boise", "US", "Mountain Time - south Idaho & east Oregon")),
    America_Cambridge_Bay(new OlsonTimeZoneElement(372, "UTC-07", "UTC-06", "T", "Tango", "America/Cambridge Bay", "CA", "Mountain Time - west Nunavut")),
    America_Campo_Grande(new OlsonTimeZoneElement(287, "UTC-04", "UTC-03", "Q", "Quebec", "America/Campo Grande", "BR", "Mato Grosso do Sul")),
    America_Cancun(new OlsonTimeZoneElement(359, "UTC-06", "UTC-05", "S", "Sierra", "America/Cancun", "MX", "Central Time - Quintana Roo")),
    America_Caracas(new OlsonTimeZoneElement(319, "UTC-04:30", "", "Q", "Quebec", "America/Caracas", "VE", "")),
    America_Cayenne(new OlsonTimeZoneElement(271, "UTC-03", "", "P", "Papa", "America/Cayenne", "GF", "")),
    America_Cayman(new OlsonTimeZoneElement(333, "UTC-05", "", "R", "Romeo", "America/Cayman", "KY", "")),
    America_Chicago(new OlsonTimeZoneElement(365, "UTC-06", "UTC-05", "S", "Sierra", "America/Chicago", "US", "Central Time")),
    America_Chihuahua(new OlsonTimeZoneElement(377, "UTC-07", "UTC-06", "T", "Tango", "America/Chihuahua", "MX", "Mexican Mountain Time - Chihuahua away from US border")),
    America_Costa_Rica(new OlsonTimeZoneElement(354, "UTC-06", "", "S", "Sierra", "America/Costa Rica", "CR", "")),
    America_Cuiaba(new OlsonTimeZoneElement(288, "UTC-04", "UTC-03", "Q", "Quebec", "America/Cuiaba", "BR", "Mato Grosso")),
    America_Curacao(new OlsonTimeZoneElement(279, "UTC-04", "", "Q", "Quebec", "America/Curacao", "AN", "")),
    America_Danmarkshavn(new OlsonTimeZoneElement(10, "UTC+00", "", "Z", "Zulu", "America/Danmarkshavn", "GL", "east coast, north of Scoresbysund")),
    America_Dawson(new OlsonTimeZoneElement(385, "UTC-08", "UTC-07", "U", "Uniform", "America/Dawson", "CA", "Pacific Time - north Yukon")),
    America_Dawson_Creek(new OlsonTimeZoneElement(375, "UTC-07", "", "T", "Tango", "America/Dawson Creek", "CA", "Mountain Standard Time - Dawson Creek & Fort Saint John, British Columbia")),
    America_Denver(new OlsonTimeZoneElement(379, "UTC-07", "UTC-06", "T", "Tango", "America/Denver", "US", "Mountain Time")),
    America_Detroit(new OlsonTimeZoneElement(338, "UTC-05", "UTC-04", "R", "Romeo", "America/Detroit", "US", "Eastern Time - Michigan - most locations")),
    America_Dominica(new OlsonTimeZoneElement(301, "UTC-04", "", "Q", "Quebec", "America/Dominica", "DM", "")),
    America_Edmonton(new OlsonTimeZoneElement(371, "UTC-07", "UTC-06", "T", "Tango", "America/Edmonton", "CA", "Mountain Time - Alberta, east British Columbia & west Saskatchewan")),
    America_Eirunepe(new OlsonTimeZoneElement(292, "UTC-04", "", "Q", "Quebec", "America/Eirunepe", "BR", "W Amazonas")),
    America_El_Salvador(new OlsonTimeZoneElement(364, "UTC-06", "", "S", "Sierra", "America/El Salvador", "SV", "")),
    America_Fortaleza(new OlsonTimeZoneElement(264, "UTC-03", "", "P", "Papa", "America/Fortaleza", "BR", "NE Brazil (MA, PI, CE, RN, PB)")),
    America_Glace_Bay(new OlsonTimeZoneElement(296, "UTC-04", "UTC-03", "Q", "Quebec", "America/Glace Bay", "CA", "Atlantic Time - Nova Scotia - places that did not observe DST 1966-1971")),
    America_Godthab(new OlsonTimeZoneElement(272, "UTC-03", "UTC-02", "P", "Papa", "America/Godthab", "GL", "most locations")),
    America_Goose_Bay(new OlsonTimeZoneElement(298, "UTC-04", "UTC-03", "Q", "Quebec", "America/Goose Bay", "CA", "Atlantic Time - Labrador - most locations")),
    America_Grand_Turk(new OlsonTimeZoneElement(336, "UTC-05", "UTC-04", "R", "Romeo", "America/Grand Turk", "TC", "")),
    America_Grenada(new OlsonTimeZoneElement(304, "UTC-04", "", "Q", "Quebec", "America/Grenada", "GD", "")),
    America_Guadeloupe(new OlsonTimeZoneElement(306, "UTC-04", "", "Q", "Quebec", "America/Guadeloupe", "GP", "")),
    America_Guatemala(new OlsonTimeZoneElement(356, "UTC-06", "", "S", "Sierra", "America/Guatemala", "GT", "")),
    America_Guayaquil(new OlsonTimeZoneElement(330, "UTC-05", "", "R", "Romeo", "America/Guayaquil", "EC", "mainland")),
    America_Guyana(new OlsonTimeZoneElement(307, "UTC-04", "", "Q", "Quebec", "America/Guyana", "GY", "")),
    America_Halifax(new OlsonTimeZoneElement(295, "UTC-04", "UTC-03", "Q", "Quebec", "America/Halifax", "CA", "Atlantic Time - Nova Scotia (most places), PEI")),
    America_Havana(new OlsonTimeZoneElement(329, "UTC-05", "UTC-04", "R", "Romeo", "America/Havana", "CU", "")),
    America_Hermosillo(new OlsonTimeZoneElement(378, "UTC-07", "", "T", "Tango", "America/Hermosillo", "MX", "Mountain Standard Time - Sonora")),
    America_Indiana_Indianapolis(new OlsonTimeZoneElement(341, "UTC-05", "UTC-04", "R", "Romeo", "America/Indiana/Indianapolis", "US", "Eastern Time - Indiana - most locations")),
    America_Indiana_Knox(new OlsonTimeZoneElement(367, "UTC-06", "UTC-05", "S", "Sierra", "America/Indiana/Knox", "US", "Central Time - Indiana - Starke County")),
    America_Indiana_Marengo(new OlsonTimeZoneElement(344, "UTC-05", "UTC-04", "R", "Romeo", "America/Indiana/Marengo", "US", "Eastern Time - Indiana - Crawford County")),
    America_Indiana_Petersburg(new OlsonTimeZoneElement(345, "UTC-05", "UTC-04", "R", "Romeo", "America/Indiana/Petersburg", "US", "Eastern Time - Indiana - Pike County")),
    America_Indiana_Tell_City(new OlsonTimeZoneElement(366, "UTC-06", "UTC-05", "S", "Sierra", "America/Indiana/Tell City", "US", "Central Time - Indiana - Perry County")),
    America_Indiana_Vevay(new OlsonTimeZoneElement(346, "UTC-05", "UTC-04", "R", "Romeo", "America/Indiana/Vevay", "US", "Eastern Time - Indiana - Switzerland County")),
    America_Indiana_Vincennes(new OlsonTimeZoneElement(342, "UTC-05", "UTC-04", "R", "Romeo", "America/Indiana/Vincennes", "US", "Eastern Time - Indiana - Daviess, Dubois, Knox & Martin Counties")),
    America_Indiana_Winamac(new OlsonTimeZoneElement(343, "UTC-05", "UTC-04", "R", "Romeo", "America/Indiana/Winamac", "US", "Eastern Time - Indiana - Pulaski County")),
    America_Inuvik(new OlsonTimeZoneElement(374, "UTC-07", "UTC-06", "T", "Tango", "America/Inuvik", "CA", "Mountain Time - west Northwest Territories")),
    America_Iqaluit(new OlsonTimeZoneElement(324, "UTC-05", "UTC-04", "R", "Romeo", "America/Iqaluit", "CA", "Eastern Time - east Nunavut - most locations")),
    America_Jamaica(new OlsonTimeZoneElement(332, "UTC-05", "", "R", "Romeo", "America/Jamaica", "JM", "")),
    America_Juneau(new OlsonTimeZoneElement(392, "UTC-09", "UTC-08", "V", "Victor", "America/Juneau", "US", "Alaska Time - Alaska panhandle")),
    America_Kentucky_Louisville(new OlsonTimeZoneElement(339, "UTC-05", "UTC-04", "R", "Romeo", "America/Kentucky/Louisville", "US", "Eastern Time - Kentucky - Louisville area")),
    America_Kentucky_Monticello(new OlsonTimeZoneElement(340, "UTC-05", "UTC-04", "R", "Romeo", "America/Kentucky/Monticello", "US", "Eastern Time - Kentucky - Wayne County")),
    America_La_Paz(new OlsonTimeZoneElement(286, "UTC-04", "", "Q", "Quebec", "America/La Paz", "BO", "")),
    America_Lima(new OlsonTimeZoneElement(335, "UTC-05", "", "R", "Romeo", "America/Lima", "PE", "")),
    America_Los_Angeles(new OlsonTimeZoneElement(389, "UTC-08", "UTC-07", "U", "Uniform", "America/Los Angeles", "US", "Pacific Time")),
    America_Maceio(new OlsonTimeZoneElement(267, "UTC-03", "", "P", "Papa", "America/Maceio", "BR", "Alagoas, Sergipe")),
    America_Managua(new OlsonTimeZoneElement(363, "UTC-06", "", "S", "Sierra", "America/Managua", "NI", "")),
    America_Manaus(new OlsonTimeZoneElement(291, "UTC-04", "", "Q", "Quebec", "America/Manaus", "BR", "E Amazonas")),
    America_Marigot(new OlsonTimeZoneElement(310, "UTC-04", "", "Q", "Quebec", "America/Marigot", "MF", "")),
    America_Martinique(new OlsonTimeZoneElement(311, "UTC-04", "", "Q", "Quebec", "America/Martinique", "MQ", "")),
    America_Matamoros(new OlsonTimeZoneElement(362, "UTC-06", "UTC-05", "S", "Sierra", "America/Matamoros", "MX", "US Central Time - Coahuila, Durango, Nuevo Leon, Tamaulipas near US border")),
    America_Mazatlan(new OlsonTimeZoneElement(376, "UTC-07", "UTC-06", "T", "Tango", "America/Mazatlan", "MX", "Mountain Time - S Baja, Nayarit, Sinaloa")),
    America_Menominee(new OlsonTimeZoneElement(368, "UTC-06", "UTC-05", "S", "Sierra", "America/Menominee", "US", "Central Time - Michigan - Dickinson, Gogebic, Iron & Menominee Counties")),
    America_Merida(new OlsonTimeZoneElement(360, "UTC-06", "UTC-05", "S", "Sierra", "America/Merida", "MX", "Central Time - Campeche, Yucatan")),
    America_Mexico_City(new OlsonTimeZoneElement(358, "UTC-06", "UTC-05", "S", "Sierra", "America/Mexico City", "MX", "Central Time - most locations")),
    America_Miquelon(new OlsonTimeZoneElement(273, "UTC-03", "UTC-02", "P", "Papa", "America/Miquelon", "PM", "")),
    America_Moncton(new OlsonTimeZoneElement(297, "UTC-04", "UTC-03", "Q", "Quebec", "America/Moncton", "CA", "Atlantic Time - New Brunswick")),
    America_Monterrey(new OlsonTimeZoneElement(361, "UTC-06", "UTC-05", "S", "Sierra", "America/Monterrey", "MX", "Mexican Central Time - Coahuila, Durango, Nuevo Leon, Tamaulipas away from US border")),
    America_Montevideo(new OlsonTimeZoneElement(275, "UTC-03", "UTC-02", "P", "Papa", "America/Montevideo", "UY", "")),
    America_Montreal(new OlsonTimeZoneElement(320, "UTC-05", "UTC-04", "R", "Romeo", "America/Montreal", "CA", "Eastern Time - Quebec - most locations")),
    America_Montserrat(new OlsonTimeZoneElement(312, "UTC-04", "", "Q", "Quebec", "America/Montserrat", "MS", "")),
    America_Nassau(new OlsonTimeZoneElement(294, "UTC-04", "UTC-03", "Q", "Quebec", "America/Nassau", "BS", "")),
    America_New_York(new OlsonTimeZoneElement(337, "UTC-05", "UTC-04", "R", "Romeo", "America/New York", "US", "Eastern Time")),
    America_Nipigon(new OlsonTimeZoneElement(322, "UTC-05", "UTC-04", "R", "Romeo", "America/Nipigon", "CA", "Eastern Time - Ontario & Quebec - places that did not observe DST 1967-1973")),
    America_Nome(new OlsonTimeZoneElement(394, "UTC-09", "UTC-08", "V", "Victor", "America/Nome", "US", "Alaska Time - west Alaska")),
    America_Noronha(new OlsonTimeZoneElement(249, "UTC-02", "", "O", "Oscar", "America/Noronha", "BR", "Atlantic islands")),
    America_North_Dakota_Center(new OlsonTimeZoneElement(369, "UTC-06", "UTC-05", "S", "Sierra", "America/North Dakota/Center", "US", "Central Time - North Dakota - Oliver County")),
    America_North_Dakota_New_Salem(new OlsonTimeZoneElement(370, "UTC-06", "UTC-05", "S", "Sierra", "America/North Dakota/New Salem", "US", "Central Time - North Dakota - Morton County (except Mandan area)")),
    America_Ojinaga(new OlsonTimeZoneElement(22, "UTC+00", "", "Z", "Zulu", "America/Ojinaga", "MX", "US Mountain Time - Chihuahua near US border")),
    America_Panama(new OlsonTimeZoneElement(334, "UTC-05", "", "R", "Romeo", "America/Panama", "PA", "")),
    America_Pangnirtung(new OlsonTimeZoneElement(325, "UTC-05", "UTC-04", "R", "Romeo", "America/Pangnirtung", "CA", "Eastern Time - Pangnirtung, Nunavut")),
    America_Paramaribo(new OlsonTimeZoneElement(274, "UTC-03", "", "P", "Papa", "America/Paramaribo", "SR", "")),
    America_Phoenix(new OlsonTimeZoneElement(382, "UTC-07", "", "T", "Tango", "America/Phoenix", "US", "Mountain Standard Time - Arizona")),
    America_Port_au_Prince(new OlsonTimeZoneElement(331, "UTC-05", "", "R", "Romeo", "America/Port-au-Prince", "HT", "")),
    America_Port_of_Spain(new OlsonTimeZoneElement(315, "UTC-04", "", "Q", "Quebec", "America/Port of Spain", "TT", "")),
    America_Porto_Velho(new OlsonTimeZoneElement(289, "UTC-04", "", "Q", "Quebec", "America/Porto Velho", "BR", "Rondonia")),
    America_Puerto_Rico(new OlsonTimeZoneElement(313, "UTC-04", "", "Q", "Quebec", "America/Puerto Rico", "PR", "")),
    America_Rainy_River(new OlsonTimeZoneElement(350, "UTC-06", "UTC-05", "S", "Sierra", "America/Rainy River", "CA", "Central Time - Rainy River & Fort Frances, Ontario")),
    America_Rankin_Inlet(new OlsonTimeZoneElement(348, "UTC-06", "UTC-05", "S", "Sierra", "America/Rankin Inlet", "CA", "Central Time - central Nunavut")),
    America_Recife(new OlsonTimeZoneElement(265, "UTC-03", "", "P", "Papa", "America/Recife", "BR", "Pernambuco")),
    America_Regina(new OlsonTimeZoneElement(351, "UTC-06", "", "S", "Sierra", "America/Regina", "CA", "Central Standard Time - Saskatchewan - most locations")),
    America_Resolute(new OlsonTimeZoneElement(326, "UTC-05", "UTC-04", "R", "Romeo", "America/Resolute", "CA", "Eastern Standard Time - Resolute, Nunavut")),
    America_Rio_Branco(new OlsonTimeZoneElement(293, "UTC-04", "", "Q", "Quebec", "America/Rio Branco", "BR", "Acre")),
    America_Santa_Isabel(new OlsonTimeZoneElement(387, "UTC-08", "UTC-07", "U", "Uniform", "America/Santa Isabel", "MX", "Mexican Pacific Time - Baja California away from US border")),
    America_Santarem(new OlsonTimeZoneElement(270, "UTC-03", "", "P", "Papa", "America/Santarem", "BR", "W Para")),
    America_Santiago(new OlsonTimeZoneElement(300, "UTC-04", "UTC-03", "Q", "Quebec", "America/Santiago", "CL", "most locations")),
    America_Santo_Domingo(new OlsonTimeZoneElement(302, "UTC-04", "", "Q", "Quebec", "America/Santo Domingo", "DO", "")),
    America_Sao_Paulo(new OlsonTimeZoneElement(269, "UTC-03", "UTC-02", "P", "Papa", "America/Sao Paulo", "BR", "S & SE Brazil (GO, DF, MG, ES, RJ, SP, PR, SC, RS)")),
    America_Scoresbysund(new OlsonTimeZoneElement(247, "UTC-01", "UTC+00", "N", "November", "America/Scoresbysund", "GL", "Scoresbysund / Ittoqqortoormiit")),
    America_Shiprock(new OlsonTimeZoneElement(381, "UTC-07", "UTC-06", "T", "Tango", "America/Shiprock", "US", "Mountain Time - Navajo")),
    America_St_Barthelemy(new OlsonTimeZoneElement(284, "UTC-04", "", "Q", "Quebec", "America/St Barthelemy", "BL", "")),
    America_St_Johns(new OlsonTimeZoneElement(276, "UTC-03:30", "UTC-02:30", "P", "Papa", "America/St Johns", "CA", "Newfoundland Time, including SE Labrador")),
    America_St_Kitts(new OlsonTimeZoneElement(308, "UTC-04", "", "Q", "Quebec", "America/St Kitts", "KN", "")),
    America_St_Lucia(new OlsonTimeZoneElement(309, "UTC-04", "", "Q", "Quebec", "America/St Lucia", "LC", "")),
    America_St_Thomas(new OlsonTimeZoneElement(318, "UTC-04", "", "Q", "Quebec", "America/St Thomas", "VI", "")),
    America_St_Vincent(new OlsonTimeZoneElement(316, "UTC-04", "", "Q", "Quebec", "America/St Vincent", "VC", "")),
    America_Swift_Current(new OlsonTimeZoneElement(352, "UTC-06", "", "S", "Sierra", "America/Swift Current", "CA", "Central Standard Time - Saskatchewan - midwest")),
    America_Tegucigalpa(new OlsonTimeZoneElement(357, "UTC-06", "", "S", "Sierra", "America/Tegucigalpa", "HN", "")),
    America_Thule(new OlsonTimeZoneElement(305, "UTC-04", "UTC-03", "Q", "Quebec", "America/Thule", "GL", "Thule / Pituffik")),
    America_Thunder_Bay(new OlsonTimeZoneElement(323, "UTC-05", "UTC-04", "R", "Romeo", "America/Thunder Bay", "CA", "Eastern Time - Thunder Bay, Ontario")),
    America_Tijuana(new OlsonTimeZoneElement(386, "UTC-08", "UTC-07", "U", "Uniform", "America/Tijuana", "MX", "US Pacific Time - Baja California near US border")),
    America_Toronto(new OlsonTimeZoneElement(321, "UTC-05", "UTC-04", "R", "Romeo", "America/Toronto", "CA", "Eastern Time - Ontario - most locations")),
    America_Tortola(new OlsonTimeZoneElement(317, "UTC-04", "", "Q", "Quebec", "America/Tortola", "VG", "")),
    America_Vancouver(new OlsonTimeZoneElement(383, "UTC-08", "UTC-07", "U", "Uniform", "America/Vancouver", "CA", "Pacific Time - west British Columbia")),
    America_Whitehorse(new OlsonTimeZoneElement(384, "UTC-08", "UTC-07", "U", "Uniform", "America/Whitehorse", "CA", "Pacific Time - south Yukon")),
    America_Winnipeg(new OlsonTimeZoneElement(349, "UTC-06", "UTC-05", "S", "Sierra", "America/Winnipeg", "CA", "Central Time - Manitoba & west Ontario")),
    America_Yakutat(new OlsonTimeZoneElement(393, "UTC-09", "UTC-08", "V", "Victor", "America/Yakutat", "US", "Alaska Time - Alaska panhandle neck")),
    America_Yellowknife(new OlsonTimeZoneElement(373, "UTC-07", "UTC-06", "T", "Tango", "America/Yellowknife", "CA", "Mountain Time - central Northwest Territories")),
    Antarctica_Casey(new OlsonTimeZoneElement(178, "UTC+08", "", "H", "Hotel", "Antarctica/Casey", "AQ", "Casey Station, Bailey Peninsula")),
    Antarctica_Davis(new OlsonTimeZoneElement(168, "UTC+07", "", "G", "Golf", "Antarctica/Davis", "AQ", "Davis Station, Vestfold Hills")),
    Antarctica_DumontDUrville(new OlsonTimeZoneElement(208, "UTC+10", "", "K", "Kilo", "Antarctica/DumontDUrville", "AQ", "Dumont-d'Urville Station, Terre Adelie")),
    Antarctica_Mawson(new OlsonTimeZoneElement(155, "UTC+06", "", "F", "Foxtrot", "Antarctica/Mawson", "AQ", "Mawson Station, Holme Bay")),
    Antarctica_McMurdo(new OlsonTimeZoneElement(229, "UTC+12", "UTC+13", "M", "Mike", "Antarctica/McMurdo", "AQ", "McMurdo Station, Ross Island")),
    Antarctica_Palmer(new OlsonTimeZoneElement(280, "UTC-04", "UTC-03", "Q", "Quebec", "Antarctica/Palmer", "AQ", "Palmer Station, Anvers Island")),
    Antarctica_Rothera(new OlsonTimeZoneElement(251, "UTC-03", "", "P", "Papa", "Antarctica/Rothera", "AQ", "Rothera Station, Adelaide Island")),
    Antarctica_South_Pole(new OlsonTimeZoneElement(230, "UTC+12", "UTC+13", "M", "Mike", "Antarctica/South Pole", "AQ", "Amundsen-Scott Station, South Pole")),
    Antarctica_Syowa(new OlsonTimeZoneElement(111, "UTC+03", "", "C", "Charlie", "Antarctica/Syowa", "AQ", "Syowa Station, E Ongul I")),
    Arctic_Longyearbyen(new OlsonTimeZoneElement(70, "UTC+01", "UTC+02", "A", "Alpha", "Arctic/Longyearbyen", "SJ", "")),
    Asia_Aden(new OlsonTimeZoneElement(130, "UTC+03", "", "C", "Charlie", "Asia/Aden", "YE", "")),
    Asia_Almaty(new OlsonTimeZoneElement(160, "UTC+06", "", "F", "Foxtrot", "Asia/Almaty", "KZ", "most locations")),
    Asia_Amman(new OlsonTimeZoneElement(88, "UTC+02", "UTC+03", "B", "Bravo", "Asia/Amman", "JO", "")),
    Asia_Anadyr(new OlsonTimeZoneElement(238, "UTC+12", "UTC+13", "M", "Mike", "Asia/Anadyr", "RU", "Moscow+10 - Bering Sea")),
    Asia_Aqtau(new OlsonTimeZoneElement(143, "UTC+05", "", "E", "Echo", "Asia/Aqtau", "KZ", "Atyrau (Atirau, Gur'yev), Mangghystau (Mankistau)")),
    Asia_Aqtobe(new OlsonTimeZoneElement(142, "UTC+05", "", "E", "Echo", "Asia/Aqtobe", "KZ", "Aqtobe (Aktobe)")),
    Asia_Ashgabat(new OlsonTimeZoneElement(149, "UTC+05", "", "E", "Echo", "Asia/Ashgabat", "TM", "")),
    Asia_Baghdad(new OlsonTimeZoneElement(116, "UTC+03", "", "C", "Charlie", "Asia/Baghdad", "IQ", "")),
    Asia_Bahrain(new OlsonTimeZoneElement(112, "UTC+03", "", "C", "Charlie", "Asia/Bahrain", "BH", "")),
    Asia_Baku(new OlsonTimeZoneElement(135, "UTC+04", "UTC+05", "D", "Delta", "Asia/Baku", "AZ", "")),
    Asia_Bangkok(new OlsonTimeZoneElement(176, "UTC+07", "", "G", "Golf", "Asia/Bangkok", "TH", "")),
    Asia_Beirut(new OlsonTimeZoneElement(89, "UTC+02", "UTC+03", "B", "Bravo", "Asia/Beirut", "LB", "")),
    Asia_Bishkek(new OlsonTimeZoneElement(159, "UTC+06", "", "F", "Foxtrot", "Asia/Bishkek", "KG", "")),
    Asia_Brunei(new OlsonTimeZoneElement(180, "UTC+08", "", "H", "Hotel", "Asia/Brunei", "BN", "")),
    Asia_Choibalsan(new OlsonTimeZoneElement(189, "UTC+08", "", "H", "Hotel", "Asia/Choibalsan", "MN", "Dornod, Sukhbaatar")),
    Asia_Chongqing(new OlsonTimeZoneElement(183, "UTC+08", "", "H", "Hotel", "Asia/Chongqing", "CN", "central China - Sichuan, Yunnan, Guangxi, Shaanxi, Guizhou, etc.")),
    Asia_Colombo(new OlsonTimeZoneElement(153, "UTC+05:30", "", "E", "Echo", "Asia/Colombo", "LK", "")),
    Asia_Damascus(new OlsonTimeZoneElement(101, "UTC+02", "UTC+03", "B", "Bravo", "Asia/Damascus", "SY", "")),
    Asia_Dhaka(new OlsonTimeZoneElement(156, "UTC+06", "", "F", "Foxtrot", "Asia/Dhaka", "BD", "")),
    Asia_Dili(new OlsonTimeZoneElement(204, "UTC+09", "", "I", "India", "Asia/Dili", "TL", "")),
    Asia_Dubai(new OlsonTimeZoneElement(133, "UTC+04", "", "D", "Delta", "Asia/Dubai", "AE", "")),
    Asia_Dushanbe(new OlsonTimeZoneElement(148, "UTC+05", "", "E", "Echo", "Asia/Dushanbe", "TJ", "")),
    Asia_Gaza(new OlsonTimeZoneElement(97, "UTC+02", "UTC+03", "B", "Bravo", "Asia/Gaza", "PS", "")),
    Asia_Harbin(new OlsonTimeZoneElement(182, "UTC+08", "", "H", "Hotel", "Asia/Harbin", "CN", "Heilongjiang (except Mohe), Jilin")),
    Asia_Ho_Chi_Minh(new OlsonTimeZoneElement(177, "UTC+07", "", "G", "Golf", "Asia/Ho Chi Minh", "VN", "")),
    Asia_Hong_Kong(new OlsonTimeZoneElement(186, "UTC+08", "", "H", "Hotel", "Asia/Hong Kong", "HK", "")),
    Asia_Hovd(new OlsonTimeZoneElement(174, "UTC+07", "", "G", "Golf", "Asia/Hovd", "MN", "Bayan-Olgiy, Govi-Altai, Hovd, Uvs, Zavkhan")),
    Asia_Irkutsk(new OlsonTimeZoneElement(194, "UTC+08", "UTC+09", "H", "Hotel", "Asia/Irkutsk", "RU", "Moscow+05 - Lake Baikal")),
    Asia_Jakarta(new OlsonTimeZoneElement(170, "UTC+07", "", "G", "Golf", "Asia/Jakarta", "ID", "Java & Sumatra")),
    Asia_Jayapura(new OlsonTimeZoneElement(198, "UTC+09", "", "I", "India", "Asia/Jayapura", "ID", "Irian Jaya & the Moluccas")),
    Asia_Jerusalem(new OlsonTimeZoneElement(87, "UTC+02", "UTC+03", "B", "Bravo", "Asia/Jerusalem", "IL", "")),
    Asia_Kabul(new OlsonTimeZoneElement(141, "UTC+04:30", "", "D", "Delta", "Asia/Kabul", "AF", "")),
    Asia_Kamchatka(new OlsonTimeZoneElement(237, "UTC+12", "UTC+13", "M", "Mike", "Asia/Kamchatka", "RU", "Moscow+09 - Kamchatka")),
    Asia_Karachi(new OlsonTimeZoneElement(162, "UTC+06", "", "F", "Foxtrot", "Asia/Karachi", "PK", "")),
    Asia_Kashgar(new OlsonTimeZoneElement(185, "UTC+08", "", "H", "Hotel", "Asia/Kashgar", "CN", "west Tibet & Xinjiang")),
    Asia_Kathmandu(new OlsonTimeZoneElement(154, "UTC+05:45", "", "E", "Echo", "Asia/Kathmandu", "NP", "")),
    Asia_Kolkata(new OlsonTimeZoneElement(152, "UTC+05:30", "", "E", "Echo", "Asia/Kolkata", "IN", "Note: Different zones in history, seeTime in India")),
    Asia_Krasnoyarsk(new OlsonTimeZoneElement(175, "UTC+07", "UTC+08", "G", "Golf", "Asia/Krasnoyarsk", "RU", "Moscow+04 - Yenisei River")),
    Asia_Kuala_Lumpur(new OlsonTimeZoneElement(191, "UTC+08", "", "H", "Hotel", "Asia/Kuala Lumpur", "MY", "peninsular Malaysia")),
    Asia_Kuching(new OlsonTimeZoneElement(192, "UTC+08", "", "H", "Hotel", "Asia/Kuching", "MY", "Sabah & Sarawak")),
    Asia_Kuwait(new OlsonTimeZoneElement(119, "UTC+03", "", "C", "Charlie", "Asia/Kuwait", "KW", "")),
    Asia_Macau(new OlsonTimeZoneElement(190, "UTC+08", "", "H", "Hotel", "Asia/Macau", "MO", "")),
    Asia_Magadan(new OlsonTimeZoneElement(225, "UTC+11", "UTC+12", "L", "Lima", "Asia/Magadan", "RU", "Moscow+08 - Magadan")),
    Asia_Makassar(new OlsonTimeZoneElement(187, "UTC+08", "", "H", "Hotel", "Asia/Makassar", "ID", "east & south Borneo, Celebes, Bali, Nusa Tengarra, west Timor")),
    Asia_Manila(new OlsonTimeZoneElement(193, "UTC+08", "", "H", "Hotel", "Asia/Manila", "PH", "")),
    Asia_Muscat(new OlsonTimeZoneElement(138, "UTC+04", "", "D", "Delta", "Asia/Muscat", "OM", "")),
    Asia_Nicosia(new OlsonTimeZoneElement(82, "UTC+02", "UTC+03", "B", "Bravo", "Asia/Nicosia", "CY", "")),
    Asia_Novokuznetsk(new OlsonTimeZoneElement(165, "UTC+06", "UTC+07", "F", "Foxtrot", "Asia/Novokuznetsk", "RU", "Moscow+03 - Novokuznetsk")),
    Asia_Novosibirsk(new OlsonTimeZoneElement(164, "UTC+06", "UTC+07", "F", "Foxtrot", "Asia/Novosibirsk", "RU", "Moscow+03 - Novosibirsk")),
    Asia_Omsk(new OlsonTimeZoneElement(163, "UTC+06", "UTC+07", "F", "Foxtrot", "Asia/Omsk", "RU", "Moscow+03 - west Siberia")),
    Asia_Oral(new OlsonTimeZoneElement(144, "UTC+05", "", "E", "Echo", "Asia/Oral", "KZ", "West Kazakhstan")),
    Asia_Phnom_Penh(new OlsonTimeZoneElement(172, "UTC+07", "", "G", "Golf", "Asia/Phnom Penh", "KH", "")),
    Asia_Pontianak(new OlsonTimeZoneElement(171, "UTC+07", "", "G", "Golf", "Asia/Pontianak", "ID", "west & central Borneo")),
    Asia_Pyongyang(new OlsonTimeZoneElement(200, "UTC+09", "", "I", "India", "Asia/Pyongyang", "KP", "")),
    Asia_Qatar(new OlsonTimeZoneElement(121, "UTC+03", "", "C", "Charlie", "Asia/Qatar", "QA", "")),
    Asia_Qyzylorda(new OlsonTimeZoneElement(161, "UTC+06", "", "F", "Foxtrot", "Asia/Qyzylorda", "KZ", "Qyzylorda (Kyzylorda, Kzyl-Orda)")),
    Asia_Rangoon(new OlsonTimeZoneElement(167, "UTC+06:30", "", "F", "Foxtrot", "Asia/Rangoon", "MM", "")),
    Asia_Riyadh(new OlsonTimeZoneElement(125, "UTC+03", "", "C", "Charlie", "Asia/Riyadh", "SA", "")),
    Asia_Sakhalin(new OlsonTimeZoneElement(220, "UTC+10", "UTC+11", "K", "Kilo", "Asia/Sakhalin", "RU", "Moscow+07 - Sakhalin Island")),
    Asia_Samarkand(new OlsonTimeZoneElement(150, "UTC+05", "", "E", "Echo", "Asia/Samarkand", "UZ", "west Uzbekistan")),
    Asia_Seoul(new OlsonTimeZoneElement(201, "UTC+09", "", "I", "India", "Asia/Seoul", "KR", "")),
    Asia_Shanghai(new OlsonTimeZoneElement(181, "UTC+08", "", "H", "Hotel", "Asia/Shanghai", "CN", "east China - Beijing, Guangdong, Shanghai, etc.")),
    Asia_Singapore(new OlsonTimeZoneElement(195, "UTC+08", "", "H", "Hotel", "Asia/Singapore", "SG", "")),
    Asia_Taipei(new OlsonTimeZoneElement(196, "UTC+08", "", "H", "Hotel", "Asia/Taipei", "TW", "")),
    Asia_Tashkent(new OlsonTimeZoneElement(151, "UTC+05", "", "E", "Echo", "Asia/Tashkent", "UZ", "east Uzbekistan")),
    Asia_Tbilisi(new OlsonTimeZoneElement(136, "UTC+04", "", "D", "Delta", "Asia/Tbilisi", "GE", "")),
    Asia_Tehran(new OlsonTimeZoneElement(132, "UTC+03:30", "UTC+04:30", "C", "Charlie", "Asia/Tehran", "IR", "")),
    Asia_Thimphu(new OlsonTimeZoneElement(157, "UTC+06", "", "F", "Foxtrot", "Asia/Thimphu", "BT", "")),
    Asia_Tokyo(new OlsonTimeZoneElement(199, "UTC+09", "", "I", "India", "Asia/Tokyo", "JP", "")),
    Asia_Ulaanbaatar(new OlsonTimeZoneElement(188, "UTC+08", "", "H", "Hotel", "Asia/Ulaanbaatar", "MN", "most locations")),
    Asia_Urumqi(new OlsonTimeZoneElement(184, "UTC+08", "", "H", "Hotel", "Asia/Urumqi", "CN", "most of Tibet & Xinjiang")),
    Asia_Vientiane(new OlsonTimeZoneElement(173, "UTC+07", "", "G", "Golf", "Asia/Vientiane", "LA", "")),
    Asia_Vladivostok(new OlsonTimeZoneElement(219, "UTC+10", "UTC+11", "K", "Kilo", "Asia/Vladivostok", "RU", "Moscow+07 - Amur River")),
    Asia_Yakutsk(new OlsonTimeZoneElement(203, "UTC+09", "UTC+10", "I", "India", "Asia/Yakutsk", "RU", "Moscow+06 - Lena River")),
    Asia_Yekaterinburg(new OlsonTimeZoneElement(146, "UTC+05", "UTC+06", "E", "Echo", "Asia/Yekaterinburg", "RU", "Moscow+02 - Urals")),
    Asia_Yerevan(new OlsonTimeZoneElement(134, "UTC+04", "UTC+05", "D", "Delta", "Asia/Yerevan", "AM", "")),
    Atlantic_Azores(new OlsonTimeZoneElement(248, "UTC-01", "UTC+00", "N", "November", "Atlantic/Azores", "PT", "Azores")),
    Atlantic_Bermuda(new OlsonTimeZoneElement(285, "UTC-04", "", "Q", "Quebec", "Atlantic/Bermuda", "BM", "")),
    Atlantic_Canary(new OlsonTimeZoneElement(5, "UTC+00", "UTC+01", "Z", "Zulu", "Atlantic/Canary", "ES", "Canary Islands")),
    Atlantic_Cape_Verde(new OlsonTimeZoneElement(246, "UTC-01", "", "N", "November", "Atlantic/Cape Verde", "CV", "")),
    Atlantic_Faroe(new OlsonTimeZoneElement(6, "UTC+00", "UTC+01", "Z", "Zulu", "Atlantic/Faroe", "FO", "")),
    Atlantic_Madeira(new OlsonTimeZoneElement(24, "UTC+00", "UTC+01", "Z", "Zulu", "Atlantic/Madeira", "PT", "Madeira Islands")),
    Atlantic_Reykjavik(new OlsonTimeZoneElement(16, "UTC+00", "", "Z", "Zulu", "Atlantic/Reykjavik", "IS", "")),
    Atlantic_South_Georgia(new OlsonTimeZoneElement(250, "UTC-02", "", "O", "Oscar", "Atlantic/South Georgia", "GS", "")),
    Atlantic_St_Helena(new OlsonTimeZoneElement(25, "UTC+00", "", "Z", "Zulu", "Atlantic/St Helena", "SH", "")),
    Atlantic_Stanley(new OlsonTimeZoneElement(303, "UTC-04", "UTC-03", "Q", "Quebec", "Atlantic/Stanley", "FK", "")),
    Australia_Adelaide(new OlsonTimeZoneElement(206, "UTC+09:30", "UTC+10:30", "I", "India", "Australia/Adelaide", "AU", "South Australia")),
    Australia_Brisbane(new OlsonTimeZoneElement(213, "UTC+10", "", "K", "Kilo", "Australia/Brisbane", "AU", "Queensland - most locations")),
    Australia_Broken_Hill(new OlsonTimeZoneElement(205, "UTC+09:30", "UTC+10:30", "I", "India", "Australia/Broken Hill", "AU", "New South Wales - Yancowinna")),
    Australia_Currie(new OlsonTimeZoneElement(210, "UTC+10", "UTC+11", "K", "Kilo", "Australia/Currie", "AU", "Tasmania - King Island")),
    Australia_Darwin(new OlsonTimeZoneElement(207, "UTC+09:30", "", "I", "India", "Australia/Darwin", "AU", "Northern Territory")),
    Australia_Eucla(new OlsonTimeZoneElement(197, "UTC+08:45", "UTC+09:45", "H", "Hotel", "Australia/Eucla", "AU", "Western Australia - Eucla area")),
    Australia_Hobart(new OlsonTimeZoneElement(209, "UTC+10", "UTC+11", "K", "Kilo", "Australia/Hobart", "AU", "Tasmania - most locations")),
    Australia_Lindeman(new OlsonTimeZoneElement(214, "UTC+10", "", "K", "Kilo", "Australia/Lindeman", "AU", "Queensland - Holiday Islands")),
    Australia_Lord_Howe(new OlsonTimeZoneElement(221, "UTC+10:30", "UTC+11", "K", "Kilo", "Australia/Lord Howe", "AU", "Lord Howe Island")),
    Australia_Melbourne(new OlsonTimeZoneElement(211, "UTC+10", "UTC+11", "K", "Kilo", "Australia/Melbourne", "AU", "Victoria")),
    Australia_Perth(new OlsonTimeZoneElement(179, "UTC+08", "", "H", "Hotel", "Australia/Perth", "AU", "Western Australia - most locations")),
    Australia_Sydney(new OlsonTimeZoneElement(212, "UTC+10", "UTC+11", "K", "Kilo", "Australia/Sydney", "AU", "New South Wales - most locations")),
    Europe_Amsterdam(new OlsonTimeZoneElement(64, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Amsterdam", "NL", "")),
    Europe_Andorra(new OlsonTimeZoneElement(30, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Andorra", "AD", "")),
    Europe_Athens(new OlsonTimeZoneElement(86, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Athens", "GR", "")),
    Europe_Belgrade(new OlsonTimeZoneElement(67, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Belgrade", "RS", "")),
    Europe_Berlin(new OlsonTimeZoneElement(43, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Berlin", "DE", "In 1945, the Trizone did not follow Berlin's switch to DST, see Time in Germany")),
    Europe_Bratislava(new OlsonTimeZoneElement(71, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Bratislava", "SK", "")),
    Europe_Brussels(new OlsonTimeZoneElement(35, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Brussels", "BE", "")),
    Europe_Bucharest(new OlsonTimeZoneElement(98, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Bucharest", "RO", "")),
    Europe_Budapest(new OlsonTimeZoneElement(53, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Budapest", "HU", "")),
    Europe_Chisinau(new OlsonTimeZoneElement(94, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Chisinau", "MD", "")),
    Europe_Copenhagen(new OlsonTimeZoneElement(44, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Copenhagen", "DK", "")),
    Europe_Dublin(new OlsonTimeZoneElement(14, "UTC+00", "UTC+01", "Z", "Zulu", "Europe/Dublin", "IE", "")),
    Europe_Gibraltar(new OlsonTimeZoneElement(50, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Gibraltar", "GI", "")),
    Europe_Guernsey(new OlsonTimeZoneElement(8, "UTC+00", "UTC+01", "Z", "Zulu", "Europe/Guernsey", "GG", "")),
    Europe_Helsinki(new OlsonTimeZoneElement(85, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Helsinki", "FI", "")),
    Europe_Isle_of_Man(new OlsonTimeZoneElement(15, "UTC+00", "UTC+01", "Z", "Zulu", "Europe/Isle of Man", "IM", "")),
    Europe_Istanbul(new OlsonTimeZoneElement(103, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Istanbul", "TR", "")),
    Europe_Jersey(new OlsonTimeZoneElement(17, "UTC+00", "UTC+01", "Z", "Zulu", "Europe/Jersey", "JE", "")),
    Europe_Kaliningrad(new OlsonTimeZoneElement(99, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Kaliningrad", "RU", "Moscow-01 - Kaliningrad")),
    Europe_Kiev(new OlsonTimeZoneElement(104, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Kiev", "UA", "most locations")),
    Europe_Lisbon(new OlsonTimeZoneElement(23, "UTC+00", "UTC+01", "Z", "Zulu", "Europe/Lisbon", "PT", "mainland")),
    Europe_Ljubljana(new OlsonTimeZoneElement(69, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Ljubljana", "SI", "")),
    Europe_London(new OlsonTimeZoneElement(7, "UTC+00", "UTC+01", "Z", "Zulu", "Europe/London", "GB", "")),
    Europe_Luxembourg(new OlsonTimeZoneElement(56, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Luxembourg", "LU", "")),
    Europe_Madrid(new OlsonTimeZoneElement(46, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Madrid", "ES", "mainland")),
    Europe_Malta(new OlsonTimeZoneElement(60, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Malta", "MT", "")),
    Europe_Mariehamn(new OlsonTimeZoneElement(76, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Mariehamn", "AX", "")),
    Europe_Minsk(new OlsonTimeZoneElement(80, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Minsk", "BY", "")),
    Europe_Monaco(new OlsonTimeZoneElement(57, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Monaco", "MC", "")),
    Europe_Moscow(new OlsonTimeZoneElement(122, "UTC+03", "UTC+04", "C", "Charlie", "Europe/Moscow", "RU", "Moscow+00 - west Russia")),
    Europe_Oslo(new OlsonTimeZoneElement(65, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Oslo", "NO", "")),
    Europe_Paris(new OlsonTimeZoneElement(48, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Paris", "FR", "")),
    Europe_Podgorica(new OlsonTimeZoneElement(58, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Podgorica", "ME", "")),
    Europe_Prague(new OlsonTimeZoneElement(42, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Prague", "CZ", "")),
    Europe_Riga(new OlsonTimeZoneElement(92, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Riga", "LV", "")),
    Europe_Rome(new OlsonTimeZoneElement(54, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Rome", "IT", "")),
    Europe_Samara(new OlsonTimeZoneElement(124, "UTC+03", "UTC+04", "C", "Charlie", "Europe/Samara", "RU", "Moscow+00 - Samara, Udmurtia")),
    Europe_San_Marino(new OlsonTimeZoneElement(72, "UTC+01", "UTC+02", "A", "Alpha", "Europe/San Marino", "SM", "")),
    Europe_Sarajevo(new OlsonTimeZoneElement(34, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Sarajevo", "BA", "")),
    Europe_Simferopol(new OlsonTimeZoneElement(107, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Simferopol", "UA", "central Crimea")),
    Europe_Skopje(new OlsonTimeZoneElement(59, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Skopje", "MK", "")),
    Europe_Sofia(new OlsonTimeZoneElement(77, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Sofia", "BG", "")),
    Europe_Stockholm(new OlsonTimeZoneElement(68, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Stockholm", "SE", "")),
    Europe_Tallinn(new OlsonTimeZoneElement(83, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Tallinn", "EE", "")),
    Europe_Tirane(new OlsonTimeZoneElement(31, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Tirane", "AL", "")),
    Europe_Uzhgorod(new OlsonTimeZoneElement(105, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Uzhgorod", "UA", "Ruthenia")),
    Europe_Vaduz(new OlsonTimeZoneElement(55, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Vaduz", "LI", "")),
    Europe_Vatican(new OlsonTimeZoneElement(75, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Vatican", "VA", "")),
    Europe_Vienna(new OlsonTimeZoneElement(33, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Vienna", "AT", "")),
    Europe_Vilnius(new OlsonTimeZoneElement(91, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Vilnius", "LT", "")),
    Europe_Volgograd(new OlsonTimeZoneElement(123, "UTC+03", "UTC+04", "C", "Charlie", "Europe/Volgograd", "RU", "Moscow+00 - Caspian Sea")),
    Europe_Warsaw(new OlsonTimeZoneElement(66, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Warsaw", "PL", "")),
    Europe_Zagreb(new OlsonTimeZoneElement(52, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Zagreb", "HR", "")),
    Europe_Zaporozhye(new OlsonTimeZoneElement(106, "UTC+02", "UTC+03", "B", "Bravo", "Europe/Zaporozhye", "UA", "Zaporozh'ye, E Lugansk / Zaporizhia, E Luhansk")),
    Europe_Zurich(new OlsonTimeZoneElement(40, "UTC+01", "UTC+02", "A", "Alpha", "Europe/Zurich", "CH", "")),
    Indian_Antananarivo(new OlsonTimeZoneElement(120, "UTC+03", "", "C", "Charlie", "Indian/Antananarivo", "MG", "")),
    Indian_Chagos(new OlsonTimeZoneElement(158, "UTC+06", "", "F", "Foxtrot", "Indian/Chagos", "IO", "")),
    Indian_Christmas(new OlsonTimeZoneElement(169, "UTC+07", "", "G", "Golf", "Indian/Christmas", "CX", "")),
    Indian_Cocos(new OlsonTimeZoneElement(166, "UTC+06:30", "", "F", "Foxtrot", "Indian/Cocos", "CC", "")),
    Indian_Comoro(new OlsonTimeZoneElement(118, "UTC+03", "", "C", "Charlie", "Indian/Comoro", "KM", "")),
    Indian_Kerguelen(new OlsonTimeZoneElement(147, "UTC+05", "", "E", "Echo", "Indian/Kerguelen", "TF", "")),
    Indian_Mahe(new OlsonTimeZoneElement(140, "UTC+04", "", "D", "Delta", "Indian/Mahe", "SC", "")),
    Indian_Maldives(new OlsonTimeZoneElement(145, "UTC+05", "", "E", "Echo", "Indian/Maldives", "MV", "")),
    Indian_Mauritius(new OlsonTimeZoneElement(137, "UTC+04", "", "D", "Delta", "Indian/Mauritius", "MU", "")),
    Indian_Mayotte(new OlsonTimeZoneElement(131, "UTC+03", "", "C", "Charlie", "Indian/Mayotte", "YT", "")),
    Indian_Reunion(new OlsonTimeZoneElement(139, "UTC+04", "", "D", "Delta", "Indian/Reunion", "RE", "")),
    Pacific_Apia(new OlsonTimeZoneElement(405, "UTC-11", "", "X", "X-Ray", "Pacific/Apia", "WS", "")),
    Pacific_Auckland(new OlsonTimeZoneElement(236, "UTC+12", "UTC+13", "M", "Mike", "Pacific/Auckland", "NZ", "most locations")),
    Pacific_Chatham(new OlsonTimeZoneElement(242, "UTC+12:45", "UTC+13:45", "M", "Mike", "Pacific/Chatham", "NZ", "Chatham Islands")),
    Pacific_Easter(new OlsonTimeZoneElement(353, "UTC-06", "UTC-05", "S", "Sierra", "Pacific/Easter", "CL", "Easter Island & Sala y Gomez")),
    Pacific_Efate(new OlsonTimeZoneElement(227, "UTC+11", "", "L", "Lima", "Pacific/Efate", "VU", "")),
    Pacific_Enderbury(new OlsonTimeZoneElement(243, "UTC+13", "", "M", "Mike", "Pacific/Enderbury", "KI", "Phoenix Islands")),
    Pacific_Fakaofo(new OlsonTimeZoneElement(398, "UTC-10", "", "W", "Whiskey", "Pacific/Fakaofo", "TK", "")),
    Pacific_Fiji(new OlsonTimeZoneElement(231, "UTC+12", "", "M", "Mike", "Pacific/Fiji", "FJ", "")),
    Pacific_Funafuti(new OlsonTimeZoneElement(239, "UTC+12", "", "M", "Mike", "Pacific/Funafuti", "TV", "")),
    Pacific_Galapagos(new OlsonTimeZoneElement(355, "UTC-06", "", "S", "Sierra", "Pacific/Galapagos", "EC", "Galapagos Islands")),
    Pacific_Gambier(new OlsonTimeZoneElement(390, "UTC-09", "", "V", "Victor", "Pacific/Gambier", "PF", "Gambier Islands")),
    Pacific_Guadalcanal(new OlsonTimeZoneElement(226, "UTC+11", "", "L", "Lima", "Pacific/Guadalcanal", "SB", "")),
    Pacific_Guam(new OlsonTimeZoneElement(216, "UTC+10", "", "K", "Kilo", "Pacific/Guam", "GU", "")),
    Pacific_Honolulu(new OlsonTimeZoneElement(401, "UTC-10", "", "W", "Whiskey", "Pacific/Honolulu", "US", "Hawaii")),
    Pacific_Johnston(new OlsonTimeZoneElement(399, "UTC-10", "", "W", "Whiskey", "Pacific/Johnston", "UM", "Johnston Atoll")),
    Pacific_Kiritimati(new OlsonTimeZoneElement(245, "UTC+14", "", "M", "Mike", "Pacific/Kiritimati", "KI", "Line Islands")),
    Pacific_Kosrae(new OlsonTimeZoneElement(223, "UTC+11", "", "L", "Lima", "Pacific/Kosrae", "FM", "Kosrae")),
    Pacific_Kwajalein(new OlsonTimeZoneElement(234, "UTC+12", "", "M", "Mike", "Pacific/Kwajalein", "MH", "Kwajalein")),
    Pacific_Majuro(new OlsonTimeZoneElement(233, "UTC+12", "", "M", "Mike", "Pacific/Majuro", "MH", "most locations")),
    Pacific_Marquesas(new OlsonTimeZoneElement(395, "UTC-09:30", "", "V", "Victor", "Pacific/Marquesas", "PF", "Marquesas Islands")),
    Pacific_Midway(new OlsonTimeZoneElement(404, "UTC-11", "", "X", "X-Ray", "Pacific/Midway", "UM", "Midway Islands")),
    Pacific_Nauru(new OlsonTimeZoneElement(235, "UTC+12", "", "M", "Mike", "Pacific/Nauru", "NR", "")),
    Pacific_Niue(new OlsonTimeZoneElement(403, "UTC-11", "", "X", "X-Ray", "Pacific/Niue", "NU", "")),
    Pacific_Norfolk(new OlsonTimeZoneElement(228, "UTC+11:30", "", "L", "Lima", "Pacific/Norfolk", "NF", "")),
    Pacific_Noumea(new OlsonTimeZoneElement(224, "UTC+11", "", "L", "Lima", "Pacific/Noumea", "NC", "")),
    Pacific_Pago_Pago(new OlsonTimeZoneElement(402, "UTC-11", "", "X", "X-Ray", "Pacific/Pago Pago", "AS", "")),
    Pacific_Palau(new OlsonTimeZoneElement(202, "UTC+09", "", "I", "India", "Pacific/Palau", "PW", "")),
    Pacific_Pitcairn(new OlsonTimeZoneElement(388, "UTC-08", "", "U", "Uniform", "Pacific/Pitcairn", "PN", "")),
    Pacific_Ponape(new OlsonTimeZoneElement(222, "UTC+11", "", "L", "Lima", "Pacific/Ponape", "FM", "Ponape (Pohnpei)")),
    Pacific_Port_Moresby(new OlsonTimeZoneElement(218, "UTC+10", "", "K", "Kilo", "Pacific/Port Moresby", "PG", "")),
    Pacific_Rarotonga(new OlsonTimeZoneElement(396, "UTC-10", "", "W", "Whiskey", "Pacific/Rarotonga", "CK", "")),
    Pacific_Saipan(new OlsonTimeZoneElement(217, "UTC+10", "", "K", "Kilo", "Pacific/Saipan", "MP", "")),
    Pacific_Tahiti(new OlsonTimeZoneElement(397, "UTC-10", "", "W", "Whiskey", "Pacific/Tahiti", "PF", "Society Islands")),
    Pacific_Tarawa(new OlsonTimeZoneElement(232, "UTC+12", "", "M", "Mike", "Pacific/Tarawa", "KI", "Gilbert Islands")),
    Pacific_Tongatapu(new OlsonTimeZoneElement(244, "UTC+13", "", "M", "Mike", "Pacific/Tongatapu", "TO", "")),
    Pacific_Truk(new OlsonTimeZoneElement(215, "UTC+10", "", "K", "Kilo", "Pacific/Truk", "FM", "Truk (Chuuk) and Yap")),
    Pacific_Wake(new OlsonTimeZoneElement(240, "UTC+12", "", "M", "Mike", "Pacific/Wake", "UM", "Wake Island")),
    Pacific_Wallis(new OlsonTimeZoneElement(241, "UTC+12", "", "M", "Mike", "Pacific/Wallis", "WF", "")),
    UTC_12(new OlsonTimeZoneElement(406, "UTC-12", "", "Y", "Yankee", "UTC-12", "", ""));

    /**
     * US TimeZones
     */
    public static final TimeZone[] USTimeZones = new TimeZone[]{
            America_New_York,
            America_Chicago,
            America_Denver,
            America_Phoenix,
            America_Los_Angeles,
            America_Anchorage,
            Pacific_Honolulu
    };

    /**
     * Our Default Time Zone
     */
    public static final TimeZone DEFAULT_TIME_ZONE = America_Los_Angeles;

    /**
     * Definition
     */
    private OlsonTimeZoneElement olsonTimeZoneElement;

    TimeZone(OlsonTimeZoneElement olsonTimeZoneElement) {
        this.olsonTimeZoneElement = olsonTimeZoneElement;
    }

    public OlsonTimeZoneElement ConstTimeZone() {
        return this.olsonTimeZoneElement;
    }

    public String UTC() {
        return this.olsonTimeZoneElement.getUtc();
    }

    public int UTCOffSet() {
        int offset = 0;
        String utc = UTC();
        if ((utc == null) || (utc.isEmpty())) {
            return offset;
        }
        if (utc.startsWith("UTC")) {
            utc = utc.substring(3);
        }
        if (utc.startsWith("+")) {
            utc = utc.substring(1);
        }
        try {
            offset = Integer.parseInt(utc);
        } catch (NumberFormatException nfe) {
            offset = 0;
        }

        return offset;
    }

    public boolean isDayLightSavingsTimeSupported() {
        if ((this.olsonTimeZoneElement.getUtcsummer() == null) ||
                (this.olsonTimeZoneElement.getUtcsummer().trim().isEmpty())) {
            return false;
        }
        return true;
    }

    public String UTCSummer() {
        return this.olsonTimeZoneElement.getUtcsummer();
    }

    public int UTCSummerOffSet() {
        int offset = 0;
        String utc = UTCSummer();
        if ((utc == null) || (utc.isEmpty())) {
            return offset;
        }
        if (utc.startsWith("UTC")) {
            utc = utc.substring(3);
        }
        if (utc.startsWith("+")) {
            utc = utc.substring(1);
        }
        try {
            offset = Integer.parseInt(utc);
        } catch (NumberFormatException nfe) {
            offset = 0;
        }

        return offset;
    }

    public String NatoAlpha() {
        return this.olsonTimeZoneElement.getNatoalpha();
    }

    public String NatoPhonetic() {
        return this.olsonTimeZoneElement.getNatophonetic();
    }

    public String TimeZoneName() {
        return this.olsonTimeZoneElement.getTimezonename();
    }

    public String TimeZoneNameForDropDown() {
        return this.olsonTimeZoneElement.getTimezonename().replace("_", " ");
    }

    public String TimeZoneStandardName() {
        return this.olsonTimeZoneElement.getTimezonename().replace(" ", "_");
    }

    public java.util.TimeZone JavaTimeZone() {
        return java.util.TimeZone.getTimeZone(this.TimeZoneStandardName());
    }

    public String CountryCode() {
        return this.olsonTimeZoneElement.getCountryCode();
    }

    public String Comments() {
        return this.olsonTimeZoneElement.getComments();
    }

    public String localMilitaryTime() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(this.JavaTimeZone());
        return sdf.format(now.getTime());
    }

    public String localTime() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a z");
        sdf.setTimeZone(this.JavaTimeZone());
        return sdf.format(now.getTime());
    }

    public static List<String> getTimeZonesByRegion() {
        List<String> timeZoneList = new ArrayList<String>(TimeZone.values().length);
        for (TimeZone tz : TimeZone.values()) {
            if (tz.equals(TimeZone.SYSTEM_DEFAULT)) {
                continue;
            }
            timeZoneList.add(tz.TimeZoneName());
        }
        // Sort by TimeZone Name.
        Collections.sort(timeZoneList);
        timeZoneList.add(0, TimeZone.SYSTEM_DEFAULT.TimeZoneName());
        return timeZoneList;
    }

    /**
     * Get Only US Applicable TimeZones.
     *
     * @return
     */
    public static List<String> getUSTimeZones() {
        List<String> timeZoneList = new ArrayList<String>(TimeZone.USTimeZones.length);
        for (TimeZone tz : TimeZone.USTimeZones) {
            if (tz.equals(TimeZone.SYSTEM_DEFAULT)) {
                continue;
            }
            timeZoneList.add(tz.TimeZoneName());
        }
        timeZoneList.add(0, TimeZone.SYSTEM_DEFAULT.TimeZoneName());
        return timeZoneList;
    }

    /**
     * Get the Timezone by Name
     *
     * @param timeZoneName
     * @return
     */
    public static TimeZone getTimeZoneByName(String timeZoneName) {
        if (StringUtils.isEmpty(timeZoneName))
            { return TimeZone.SYSTEM_DEFAULT; }
        for (TimeZone tz : TimeZone.values()) {
            if ((tz.TimeZoneName().equalsIgnoreCase(timeZoneName)) ||
                    (tz.TimeZoneNameForDropDown().equalsIgnoreCase(timeZoneName)) ||
                    (tz.TimeZoneStandardName().equalsIgnoreCase(timeZoneName))) {
                return tz;
            }
        }
        return TimeZone.SYSTEM_DEFAULT;
    }

    /**
     * Determine if the specified Timezone Name is Valid or not.
     *
     * @param timeZoneName
     * @return
     */
    public static boolean isTimeZoneByNameValid(String timeZoneName) {
        if (StringUtils.isEmpty(timeZoneName))
            { return false; }
        for (TimeZone tz : TimeZone.values()) {
            if ((tz.TimeZoneName().equalsIgnoreCase(timeZoneName)) ||
                    (tz.TimeZoneNameForDropDown().equalsIgnoreCase(timeZoneName)) ||
                    (tz.TimeZoneStandardName().equalsIgnoreCase(timeZoneName))) {
                return true;
            }
        }
        return false;
    }


    /**
     * Get the correct TimeZone Name for a Drop Down Menu
     * @param timeZoneName
     * @return
     */
    public static String getTimeZoneNameForDropDown(String timeZoneName) {
        return TimeZone.getTimeZoneByName(timeZoneName).TimeZoneNameForDropDown();
    }

    /**
     * Get the correct TimeZone name for Standard Olson DB Lookup and Conversions.
     * @param timeZoneName
     * @return
     */
    public static String getTimeZoneStandardName(String timeZoneName) {
        return TimeZone.getTimeZoneByName(timeZoneName).TimeZoneStandardName();
    }

    /**
     * Obtain the corresponding java.util.Timezone object for the Specified TimeZone.
     * @param timeZoneName
     * @return
     */
    public static java.util.TimeZone getJavaTimeZone(String timeZoneName) {
        return java.util.TimeZone.getTimeZone(TimeZone.getTimeZoneByName(timeZoneName).TimeZoneStandardName());
    }


    /**
     * Determine if we are in Day Light Savings Time?
     *
     * @return boolean - True DST/Summer Time Enabled.
     */
    public static boolean isDayLightSavingsTimeEnabled() {
        java.util.TimeZone tz = DEFAULT_TIME_ZONE.JavaTimeZone();
        return tz.inDaylightTime(new Date());
    }

    /**
     * Main
     * Dumps All US Time Zone
     *
     * @param args
     */
    public static void main(String[] args) {

        TimeZone[] SomeTimeZones = new TimeZone[]{
                // Zulu
                Antarctica_Vostok,
                // Across US
                America_New_York,
                America_Chicago,
                America_Denver,
                America_Phoenix,
                America_Los_Angeles,
                America_Anchorage,
                Pacific_Honolulu,
                // Add some wacky TimeZones
                America_St_Johns,
                Pacific_Chatham
        };
        System.out.println("DST/Summer Enabled:[" + isDayLightSavingsTimeEnabled() + "]");

        for (TimeZone ustz : SomeTimeZones) {
            System.out.println("** Name:[" + ustz.TimeZoneName() + "], UTC:[" + ustz.UTC() + "], Summer UTC:[" +
                    ustz.UTCSummer() + "], Raw offset:[" +
                    ustz.UTCOffSet() + "], Time:[" + ustz.localTime() + "], DST Supported:[" +
                    ustz.isDayLightSavingsTimeSupported() + "]");
        }

    }

}
