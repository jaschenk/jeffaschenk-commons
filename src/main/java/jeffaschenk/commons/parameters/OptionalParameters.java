package jeffaschenk.commons.parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides Helper Class for Optional Parameters.
 *
 * @author jeffaschenk@gmail.com
 */
public class OptionalParameters {

    /**
     * Default Constructor
     */
    private OptionalParameters() {
    }

    /**
     * Checks if there are any OptionalParameters at all to be processed?
     *
     * @param optionalParameters
     * @return boolean indicator
     */
    public static boolean areOptionalParametersUsed(Object... optionalParameters) {
        return ((optionalParameters == null) || (optionalParameters.length <= 0) ? false : true);
    }

    /**
     * Check Optional Parameters if they Exist or not for a specific Method.
     *
     * @param optionalParameters Optional Parameters Specified on a Variable Argument Method.
     * @return boolean indicator if Optional Parameters exist or not.
     */
    public static boolean isOptionalParametersEmpty(Object... optionalParameters) {
        return ((optionalParameters == null) || (optionalParameters.length <= 0));
    }

    /**
     * Is there an Entity Path Object Specified within our optionalParameter?
     *
     * @param optionalParameters
     * @return
     */
    public static boolean isEntityPathSpecifiedAsOptionalParameter(Object... optionalParameters) {
        return isClassSpecifiedAsOptionalParameter(EntityPath.class, optionalParameters);
    }

    /**
     * Get any Entity Path Objects Specified within our optionalParameters
     *
     * @param optionalParameters
     * @return
     */
    public static Object[] getAllEntityPathsSpecifiedAsOptionalParameter(Object... optionalParameters) {
        return getAllOptionalParamatersByClass(EntityPath.class, optionalParameters);
    }

    /**
     * Is there an Entity Path Group Object Specified within our optionalParameter?
     *
     * @param optionalParameters
     * @return
     */
    public static boolean isEntityPathGroupSpecifiedAsOptionalParameter(Object... optionalParameters) {
        return isClassSpecifiedAsOptionalParameter(EntityPathGroup.class, optionalParameters);
    }

    /**
     * Get any Entity Path Group Objects Specified within our optionalParameters
     *
     * @param optionalParameters
     * @return
     */
    public static Object[] getAllEntityPathGroupsSpecifiedAsOptionalParameter(Object... optionalParameters) {
        return getAllOptionalParamatersByClass(EntityPathGroup.class, optionalParameters);
    }

    /**
     * Is there an Class Object Specified within our optionalParameter?
     *
     * @param optionalParameters
     * @return
     */
    private static boolean isClassSpecifiedAsOptionalParameter(Class<?> clazz, Object... optionalParameters) {
        if ((optionalParameters == null) || (optionalParameters.length <= 0)) {
            return false;
        }
        int count = 0;
        for (Object optionalParameter : optionalParameters) {
            if (optionalParameter.getClass().getName().endsWith(clazz.getName())) {
                count++;
            }
        }
        return (count > 0);
    }

    /**
     * Get all optionalParameters By Class.
     *
     * @param optionalParameters
     * @return Object[] Object Array of Only those Object filtered by Class.
     */
    private static Object[] getAllOptionalParamatersByClass(Class<?> clazz, Object... optionalParameters) {
        if ((optionalParameters == null) || (optionalParameters.length <= 0)) {
            return new Object[0];
        }
        List<Object> objects = new ArrayList<Object>();
        for (Object optionalParameter : optionalParameters) {
            if (optionalParameter.getClass().getName().endsWith(clazz.getName())) {
                objects.add(optionalParameter);
            }
        }
        return objects.toArray();
    }

}
