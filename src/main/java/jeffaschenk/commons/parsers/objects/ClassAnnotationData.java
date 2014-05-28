package jeffaschenk.commons.parsers.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * POJO to define the Object Relational Mapping information obtained from the
 * Object Class' annotations.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class ClassAnnotationData implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String className;
    private String simpleName;
    private Map<String, String> classAnnotations = new HashMap<String, String>();
    private Map<String, String> fieldAnnotations = new HashMap<String, String>();
    private Map<String, String> methodAnnotations = new HashMap<String, String>();

    /**
     * Default Constructor
     */
    public ClassAnnotationData() {
    }

    /**
     * Constructor with at least Class Name Specified.
     *
     * @param className a {@link java.lang.String} object.
     */
    public ClassAnnotationData(String className) {
        super();
        this.className = className;
    }

    /**
     * ClassName
     *
     * @return a {@link java.lang.String} object.
     */
    public String getClassName() {
        return className;
    }

    /**
     * <p>Setter for the field <code>className</code>.</p>
     *
     * @param className a {@link java.lang.String} object.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Simple ClassName
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * <p>Setter for the field <code>simpleName</code>.</p>
     *
     * @param simpleName a {@link java.lang.String} object.
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    /**
     * Class Annotations.
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> getClassAnnotations() {
        return classAnnotations;
    }

    /**
     * <p>Setter for the field <code>classAnnotations</code>.</p>
     *
     * @param classAnnotations a {@link java.util.Map} object.
     */
    public void setClassAnnotations(Map<String, String> classAnnotations) {
        this.classAnnotations = classAnnotations;
    }

    /**
     * Class Field Annotations.
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> getFieldAnnotations() {
        return fieldAnnotations;
    }

    /**
     * <p>Setter for the field <code>fieldAnnotations</code>.</p>
     *
     * @param fieldAnnotations a {@link java.util.Map} object.
     */
    public void setFieldAnnotations(Map<String, String> fieldAnnotations) {
        this.fieldAnnotations = fieldAnnotations;
    }

    /**
     * Class Method Annotations.
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> getMethodAnnotations() {
        return methodAnnotations;
    }

    /**
     * <p>Setter for the field <code>methodAnnotations</code>.</p>
     *
     * @param methodAnnotations a {@link java.util.Map} object.
     */
    public void setMethodAnnotations(Map<String, String> methodAnnotations) {
        this.methodAnnotations = methodAnnotations;
    }

}
