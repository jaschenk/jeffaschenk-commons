package jeffaschenk.commons.exceptions;

import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;

/**
 * The Errors object will hold the errors that occur any where in upstream or downstream layers.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class ErrorsImpl implements Errors {

    private String classSimpleName;

    private List<FieldError> fieldErrors = new ArrayList<FieldError>();

    private List<ObjectError> globalErrors = new ArrayList<ObjectError>();

    private List<FieldError> warnings = new ArrayList<FieldError>();

    private String nestedPath = "";

    private final Stack<String> nestedPathStack = new Stack<String>();

    private String currentContextName;

    private final Stack<String> contextNameStack = new Stack<String>();

    private Map<Class<?>, Map<List<String>, List<List<Object>>>> classUniqueConstraintUpdates;

    private Map<Class<?>, Map<List<String>, Map<String, FieldError>>> rectifiableUniqeConstraintErrors;

    private Map<List<String>, Set<String>> updatedConstraintFieldsGuids;

    /**
     * Default Constructor
     */
    public ErrorsImpl() {
        this.classSimpleName = "";
    }

    /**
     * Errors Errors constructor.  Must pass in Asset object to validate against.
     *
     * @param classSimpleName Asset errors occur against
     */
    public ErrorsImpl(String classSimpleName) {
        this.classSimpleName = classSimpleName;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Add all errors from another Errors class
     */
    public void addAllErrors(org.springframework.validation.Errors errors) {
        this.fieldErrors.addAll(errors.getFieldErrors());
        this.globalErrors.addAll(errors.getGlobalErrors());
    }

    /**
     * Actually set the nested path.
     * Delegated to by setNestedPath and pushNestedPath.
     * <p/>
     * Note: Not yet known what to use the path for. Possibly if the Asset has
     * second class Asset contained inside it.
     *
     * @param nestedPath Path
     */
    protected void doSetNestedPath(String nestedPath) {
        if (nestedPath == null) {
            nestedPath = "";
        }
        nestedPath = PropertyAccessorUtils.canonicalPropertyName(nestedPath);
        if (nestedPath.length() > 0 && !nestedPath.endsWith(NESTED_PATH_SEPARATOR)) {
            nestedPath += NESTED_PATH_SEPARATOR;
        }
        this.nestedPath = nestedPath;
    }

    /**
     * Transform the given field into its full path,
     * regarding the nested path of this instance.
     *
     * @param field a {@link java.lang.String} object.
     * @return  {@link java.lang.String} object.
     */
    protected String fixedField(String field) {
        if (StringUtils.hasLength(field)) {
            return getNestedPath() + PropertyAccessorUtils.canonicalPropertyName(field);
        } else {
            String path = getNestedPath();
            return (path.endsWith(org.springframework.validation.Errors.NESTED_PATH_SEPARATOR) ?
                    path.substring(0, path.length() - NESTED_PATH_SEPARATOR.length()) : path);
        }
    }

    /**
     * Get all errors including both Field and Global Errors
     * <p/>
     * return List of all Field and Global Errors
     *
     * @return  {@link java.util.List} object.
     */
    public List<ObjectError> getAllErrors() {
        List<ObjectError> objectErrorsList = new ArrayList<ObjectError>();
        objectErrorsList.addAll(this.fieldErrors);
        objectErrorsList.addAll(this.globalErrors);
        return objectErrorsList;
    }

    /**
     * Get count of all errors
     *
     * @return Get Error Count
     */
    public int getErrorCount() {
        return this.fieldErrors.size() + this.globalErrors.size();
    }

    /**
     * Get First Field Error in the List
     *
     * @return Return first Field Error
     */
    public FieldError getFieldError() {
        if (this.fieldErrors.size() > 0) {
            return this.fieldErrors.iterator().next();
        } else
            return null;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Get first FieldError for a given field value
     */
    public FieldError getFieldError(String field) {
        if (this.fieldErrors.size() > 0) {

            Iterator<FieldError> fieldErrorsIter = this.fieldErrors.iterator();

            while (fieldErrorsIter.hasNext()) {

                FieldError curFieldError = fieldErrorsIter.next();

                if (curFieldError.getField().equals(field))
                    return curFieldError;
            }
            return null;
        } else
            return null;
    }

    /**
     * Get the number of Field Errors
     *
     * @return Number of field errors
     */
    public int getFieldErrorCount() {
        return this.fieldErrors.size();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Get the number of errors for a given field
     */
    public int getFieldErrorCount(String field) {
        return getFieldErrors(field).size();
    }

    /**
     * Get all Field Errors
     *
     * @return Return a list of all field errors
     */
    public List<FieldError> getFieldErrors() {
        return this.fieldErrors;
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Get all Field Errors for a given field
     */
    public List<FieldError> getFieldErrors(String field) {

        List<FieldError> curFieldErrors = new ArrayList<FieldError>();

        Iterator<FieldError> fieldErrorsIter = this.fieldErrors.iterator();

        while (fieldErrorsIter.hasNext()) {

            FieldError curFieldError = fieldErrorsIter.next();

            if (curFieldError.getField().equals(field))
                curFieldErrors.add(curFieldError);
        }

        return curFieldErrors;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Get Class/Object Field Error is associated with
     */
    public Class<? extends String> getFieldType(String field) {
        if (this.fieldErrors.size() > 0) {

            Iterator<FieldError> fieldErrorsIter = this.fieldErrors.iterator();

            while (fieldErrorsIter.hasNext()) {

                FieldError curFieldError = fieldErrorsIter.next();

                if (curFieldError.getField().equals(field))
                    return curFieldError.getObjectName().getClass();
            }

            return null;
        } else
            return null;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Do not know use as of yet
     */
    @Deprecated
    public Object getFieldValue(String field) {
        FieldError fe = getFieldError(field);
        if (fe == null)
            return null;
        else
            return fixedField(field);
    }


    /**
     * Get First Global Error (Object Error)
     *
     * @return Get First Global Error (Object Error)
     */
    public ObjectError getGlobalError() {
        if (this.globalErrors.size() > 0) {
            return this.globalErrors.iterator().next();
        } else
            return null;
    }


    /**
     * Get count of all Global Errors
     *
     * @return Count of all Global Errors
     */
    public int getGlobalErrorCount() {
        return this.globalErrors.size();
    }

    /**
     * Get a list of all Global Errors
     *
     * @return  list of all Global Errors
     */
    public List<ObjectError> getGlobalErrors() {
        return this.globalErrors;
    }

    /**
     * Get Current nested Path
     *
     * @return Current nested path
     */
    public String getNestedPath() {
        return this.nestedPath;
    }

    /**
     * Get Asset Type
     *
     * @return Get Asset Type
     */
    public String getObjectName() {
        return classSimpleName;
    }


    /**
     * Do Errors exist
     *
     * @return Do Errors exist
     */
    public boolean hasErrors() {
        return !getAllErrors().isEmpty();
    }

    /**
     * Do Field Errors exist
     *
     * @return Do Field Errors Exist
     */
    public boolean hasFieldErrors() {
        return (this.fieldErrors.size() > 0);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Do Field Errors exist for a given field
     */
    public boolean hasFieldErrors(String field) {

        return this.getFieldErrors(field).size() > 0;
    }

    /**
     * Do Global Errors exist
     *
     * @return Do Global Errors exist
     */
    public boolean hasGlobalErrors() {
        return (this.globalErrors.size() > 0);
    }

    /*
    * (non-Javadoc)
    * @see com.sungard.cmdb.CMDBErrors#popContextName()
    */

    /**
     * {@inheritDoc}
     */
    @Override
    public void popContextName() {
        try {
            currentContextName = contextNameStack.pop();
        } catch (EmptyStackException ex) {
            throw new IllegalStateException("Cannot pop context name: empty stack");
        }
    }

    /*
    * (non-Javadoc)
    * @see com.sungard.cmdb.CMDBErrors#pushContextName(java.lang.String)
    */

    /**
     * {@inheritDoc}
     */
    @Override
    public void pushContextName(String contextName) {
        contextNameStack.push(currentContextName);
        currentContextName = contextName;
    }

    /*
    * (non-Javadoc)
    * @see com.sungard.cmdb.CMDBErrors#getCurrentContextName()
    */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentContextName() {
        return currentContextName;
    }

    /**
     * Pop NestedPath
     *
     * @throws java.lang.IllegalStateException
     *          if any.
     */
    public void popNestedPath() throws IllegalStateException {
        try {
            String formerNestedPath = this.nestedPathStack.pop();
            doSetNestedPath(formerNestedPath);
        } catch (EmptyStackException ex) {
            throw new IllegalStateException("Cannot pop nested path: no nested path on stack");
        }

    }

    /**
     * {@inheritDoc}
     * <p/>
     * Push a subpath on current path
     */
    public void pushNestedPath(String subPath) {
        this.nestedPathStack.push(getNestedPath());
        doSetNestedPath(getNestedPath() + subPath);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Reject: Add Global Error
     */
    public void reject(String errorCode) {
        reject(errorCode, null, null);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Reject: Add Global Error
     */
    public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
        //ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage)
        String[] codes = new String[]{errorCode};
        ObjectError objectError = new ObjectError(classSimpleName, codes, errorArgs, defaultMessage);
        this.globalErrors.add(objectError);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Reject: Add Global Error
     */
    public void reject(String errorCode, String defaultMessage) {
        reject(errorCode, null, defaultMessage);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Reject: Add Field Error
     */
    public void rejectValue(String field, String errorCode) {
        rejectValue(field, errorCode, null, null);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Reject: Add Field Error
     */
    public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
        //Register a field error for the specified field of the current object (respecting the current nested path, if any), using the given error description.
        //FieldError(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes, Object[] arguments, String defaultMessage)
        String fixedField = fixedField(field);
        String[] codes = new String[]{errorCode};
        FieldError fe = new FieldError(classSimpleName, fixedField, null, false, codes, errorArgs, defaultMessage);
        this.fieldErrors.add(fe);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Reject: Add Field Error
     */
    public void rejectValue(String field, String errorCode, String defaultMessage) {
        rejectValue(field, errorCode, null, defaultMessage);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Set Path
     */
    public void setNestedPath(String nestedPath) {
        doSetNestedPath(nestedPath);
        this.nestedPathStack.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FieldError> getWarnings() {
        return warnings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasWarnings() {
        return warnings.size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String field, String errorCode) {
        warn(field, errorCode, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String field, String errorCode, String defaultMessage) {
        String fixedField = fixedField(field);
        String[] codes = new String[]{errorCode};
        FieldError warning = new FieldError(classSimpleName, fixedField, null, false, codes, null, defaultMessage);
        warnings.add(warning);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Class<?>, Map<List<String>, List<List<Object>>>> getClassUniqueConstraintUpdates() {
        if (classUniqueConstraintUpdates == null) {
            classUniqueConstraintUpdates = new HashMap<Class<?>, Map<List<String>, List<List<Object>>>>();
        }
        return classUniqueConstraintUpdates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<List<String>, Set<String>> getUpdatedConstraintFieldsGuids() {
        if (updatedConstraintFieldsGuids == null) {
            updatedConstraintFieldsGuids = new HashMap<List<String>, Set<String>>();
        }
        return updatedConstraintFieldsGuids;
    }

    private Map<Class<?>, Map<List<String>, Map<String, FieldError>>> getRectifiableUniqueConstraintErrors() {
        if (rectifiableUniqeConstraintErrors == null) {
            rectifiableUniqeConstraintErrors = new HashMap<Class<?>, Map<List<String>, Map<String, FieldError>>>();
        }
        return rectifiableUniqeConstraintErrors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRectifiableUniqueConstraintViolation(
            Class<?> targetEntity,
            List<String> uniqueConstraintFields,
            String guid,
            String errorCode,
            Object[] values,
            String defaultMessage) {
        Map<List<String>, Map<String, FieldError>> uniqueConstraintFieldsGuidFieldErrors = getRectifiableUniqueConstraintErrors().get(targetEntity);
        if (uniqueConstraintFieldsGuidFieldErrors == null) {
            uniqueConstraintFieldsGuidFieldErrors = new HashMap<List<String>, Map<String, FieldError>>();
            rectifiableUniqeConstraintErrors.put(targetEntity, uniqueConstraintFieldsGuidFieldErrors);
        }
        Map<String, FieldError> guidFieldError = uniqueConstraintFieldsGuidFieldErrors.get(uniqueConstraintFields);
        if (guidFieldError == null) {
            guidFieldError = new HashMap<String, FieldError>();
            uniqueConstraintFieldsGuidFieldErrors.put(uniqueConstraintFields, guidFieldError);
        }

        //add a normal field error
        String[] codes = new String[]{errorCode};
        FieldError fieldError = new FieldError(classSimpleName, fixedField(null), null, false, codes, values, defaultMessage);
        fieldErrors.add(fieldError);

        //map the fieldError to a key so it can subsequently be rectified
        guidFieldError.put(guid, fieldError);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean rectifyUniqueConstraintViolation(
            Class<?> targetEntity,
            List<String> uniqueConstraintFields,
            String guid) {
        boolean rectified = false;
        Map<List<String>, Map<String, FieldError>> uniqueConstraintFieldsGuidFieldErrors = getRectifiableUniqueConstraintErrors().get(targetEntity);
        if (uniqueConstraintFieldsGuidFieldErrors != null) {
            Map<String, FieldError> guidFieldErrors = uniqueConstraintFieldsGuidFieldErrors.get(uniqueConstraintFields);
            if (guidFieldErrors != null) {
                FieldError fieldError = guidFieldErrors.get(guid);
                if (fieldError != null) {
                    rectified = fieldErrors.remove(fieldError);
                }
            }
        }
        return rectified;
    }
}
