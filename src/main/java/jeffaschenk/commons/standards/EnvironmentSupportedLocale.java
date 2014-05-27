package jeffaschenk.commons.standards;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * EnvironmentSupportedLocale
 * Supported Language Locale's
 *
 * @author jeffaschenk@gmail.com
 *         Date: May 27, 2010
 *         Time: 2:33:33 PM
 */
public class EnvironmentSupportedLocale implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private static final Locale[] supportedLocales = {
                            new Locale("en","US"),  // Language: English, Country: US
                            new Locale("es","US"),  // Language: Spanish, Country: US
                            Locale.FRENCH,
                            Locale.GERMAN,
                            new Locale("sv","SE")   // Language: Swedish, Country: Sweden
    };

    public static Locale[] getSupportedLocales() {
                return EnvironmentSupportedLocale.supportedLocales;
    }

    public static Map<String,String> getSupportedLocalesForDropDown() {
       Map<String,String> localeMap = new TreeMap<String,String>();
       for(Locale locale : EnvironmentSupportedLocale.getSupportedLocales())
       {
                String information = locale.getDisplayLanguage() +
                        (((locale.getCountry() == null)||(locale.getCountry().isEmpty())) ? "" : ", Country: "+locale.getCountry() );
                localeMap.put(locale.toString(), information);
       }
        return localeMap;
    }

}

