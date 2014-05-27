package jeffaschenk.commons.exceptions;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>Errors interface.</p>
 *
 * @author Jeff
 * @version $Id: $
 */
public interface Errors extends org.springframework.validation.Errors {


    /**
     * <p>hasWarnings</p>
     *
     * @return a boolean.
     */
    boolean hasWarnings();

    /**
     * <p>getWarnings</p>
     *
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    List getWarnings();

    /**
     * Register a field warning using the given warning description.
     *
     * @param errorCode      error code, interpretable as a message key
     * @param defaultMessage fallback default message
     * @param field          a {@link java.lang.String} object.
     */
    void warn(String field, String errorCode, String defaultMessage);

    /**
     * Register a field warning using the given warning description.
     *
     * @param errorCode error code, interpretable as a message key
     * @param field     a {@link java.lang.String} object.
     */
    void warn(String field, String errorCode);

    /**
     * <p>getClassUniqueConstraintUpdates</p>
     *
     * @return a {@link java.util.Map} object.
     */
    Map<Class<?>, Map<List<String>, List<List<Object>>>> getClassUniqueConstraintUpdates();

    /**
     * <p>getUpdatedConstraintFieldsGuids</p>
     *
     * @return a {@link java.util.Map} object.
     */
    Map<List<String>, Set<String>> getUpdatedConstraintFieldsGuids();

    /**
     * <p>addRectifiableUniqueConstraintViolation</p>
     *
     * @param targetEntity           a {@link java.lang.Class} object.
     * @param uniqueConstraintFields a {@link java.util.List} object.
     * @param guid                   a {@link java.lang.String} object.
     * @param errorCode              a {@link java.lang.String} object.
     * @param values                 an array of {@link java.lang.Object} objects.
     * @param defaultMessage         a {@link java.lang.String} object.
     */
    void addRectifiableUniqueConstraintViolation(
            Class<?> targetEntity,
            List<String> uniqueConstraintFields,
            String guid,
            String errorCode,
            Object[] values,
            String defaultMessage);

    /**
     * <p>rectifyUniqueConstraintViolation</p>
     *
     * @param targetEntity           a {@link java.lang.Class} object.
     * @param uniqueConstraintFields a {@link java.util.List} object.
     * @param guid                   a {@link java.lang.String} object.
     * @return a boolean.
     */
    boolean rectifyUniqueConstraintViolation(
            Class<?> targetEntity,
            List<String> uniqueConstraintFields,
            String guid);

    /**
     * <p>popContextName</p>
     */
    void popContextName();

    /**
     * <p>pushContextName</p>
     *
     * @param contextName a {@link java.lang.String} object.
     */
    void pushContextName(String contextName);

    /**
     * <p>getCurrentContextName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getCurrentContextName();
}
