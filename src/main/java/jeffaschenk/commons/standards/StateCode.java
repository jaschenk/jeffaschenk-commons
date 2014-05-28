package jeffaschenk.commons.standards;

import jeffaschenk.commons.standards.statecodes.StateCodes_US;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * Static Utility Helper Class for Accessing Official ISO-3166 State/Province Codes
 *
 * @author jeffaschenk@gmail.com
 *         Date: June 9, 2010
 *         Time: 12:12:11 PM
 */
public class StateCode {

    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(StateCode.class);

    /**
     * @param countryCode
     * @return Map<String, String>
     */
    public static Map<String, String> getCountrySubDivisions(final String countryCode) {
        // *******************************************
        // Initialize
        Map<String, String> resultsMap = new TreeMap<String, String>();
        if ((countryCode == null) || (countryCode.isEmpty())) {
            return resultsMap;
        }
        // ********************************************
        // Verify the Country Code
        //
        if (countryCode.length() > 2) {
            log.error("Country Code for Selection is greater than 2 Characters, Ignoring Request!");
            return resultsMap;
        }
        // ********************************************
        // Determine if we have to perform any
        // transformation of the CountryCode to pick
        // up the correct State/Provices.
        String useCountryCode = new String(countryCode);
        if (useCountryCode.equalsIgnoreCase("UK")) {
            useCountryCode = "GB";
        }
        // ********************************************
        // Now get the Selected Country Code and
        // use Reflection to obtain the associated
        // State/Province/Regions.
        //
        String stateCodesClassName = StateCodes_US.class.getPackage().getName() + "." + "StateCodes_" +
                useCountryCode.toUpperCase();
        try {
            Class<?> clazz = Class.forName(stateCodesClassName);
            Method method = clazz.getMethod("values", new Class[]{});
            Object[] values = (Object[]) method.invoke(clazz, new Object[]{});
            // ********************************
            // Loop Through Enumerator Values
            for (Object value : values) {
                method = clazz.getMethod("valueOf", new Class[]{String.class});
                Object enumValue = method.invoke(clazz, new Object[]{value.toString()});
                method = clazz.getMethod("stateProvinceName", new Class[]{});
                String stateProvinceName = (String) method.invoke(enumValue, new Object[]{});
                if ( (stateProvinceName == null) || (stateProvinceName.isEmpty()) )
                    { continue; }
                resultsMap.put(stateProvinceName, stateProvinceName);
            }
        } catch (ClassNotFoundException cnfe) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to Class Not Found Condition, Ignoring Request!");
        } catch (NoSuchMethodException nsfe) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to No Such Method Condition, Ignoring Request!");
        } catch (IllegalAccessException nsme) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to Illegal Access Exception, Ignoring Request!");
        } catch (InvocationTargetException nsme) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to Invocation Target Exception, Ignoring Request!");
        }

        // *******************************
        // Return Results
        return resultsMap;
    }

     /**
     * @param countryCode
     * @return Map<String, Object>
     */
    public static Map<String, Object> getCountrySubDivisionObjects(final String countryCode) {
        // *******************************************
        // Initialize
        Map<String, Object> resultsMap = new TreeMap<String, Object>();
        if ((countryCode == null) || (countryCode.isEmpty())) {
            return resultsMap;
        }
        // ********************************************
        // Verify the Country Code
        //
        if (countryCode.length() > 2) {
            log.error("Country Code for Selection is greater than 2 Characters, Ignoring Request!");
            return resultsMap;
        }
        // ********************************************
        // Determine if we have to perform any
        // transformation of the CountryCode to pick
        // up the correct State/Provices.
        String useCountryCode = new String(countryCode);
        if (useCountryCode.equalsIgnoreCase("UK")) {
            useCountryCode = "GB";
        }
        // ********************************************
        // Now get the Selected Country Code and
        // use Reflection to obtain the associated
        // State/Province/Regions.
        //
        String stateCodesClassName = StateCodes_US.class.getPackage().getName() + "." + "StateCodes_" +
                useCountryCode.toUpperCase();
        try {
            Class<?> clazz = Class.forName(stateCodesClassName);
            Method method = clazz.getMethod("values", new Class[]{});
            Object[] values = (Object[]) method.invoke(clazz, new Object[]{});
            // ********************************
            // Loop Through Enumerator Values
            for (Object value : values) {
                method = clazz.getMethod("valueOf", new Class[]{String.class});
                Object enumValue = method.invoke(clazz, new Object[]{value.toString()});
                method = clazz.getMethod("stateProvinceName", new Class[]{});
                String stateProvinceName = (String) method.invoke(enumValue, new Object[]{});
                if ( (stateProvinceName == null) || (stateProvinceName.isEmpty()) )
                    { continue; }
                resultsMap.put(stateProvinceName.toUpperCase(),value);
            }
        } catch (ClassNotFoundException cnfe) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to Class Not Found Condition, Ignoring Request!");
        } catch (NoSuchMethodException nsfe) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to No Such Method Condition, Ignoring Request!");
        } catch (IllegalAccessException nsme) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to Illegal Access Exception, Ignoring Request!");
        } catch (InvocationTargetException nsme) {
            log.error("Unable to Find Applicable State Codes for Country Code:[" + useCountryCode + "]"
                    + ", due to Invocation Target Exception, Ignoring Request!");
        }

        // *******************************
        // Return Results
        return resultsMap;
    }


}
