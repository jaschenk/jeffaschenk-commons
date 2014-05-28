package jeffaschenk.commons.parsers;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import jeffaschenk.commons.parsers.objects.ClassAnnotationData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Annotation Parser Simple Parsers a Classes Annotations and returns a
 * Collection Map of those attributes and properties of the Object class. This
 * parser uses Javassist to perform introspection of the Java ByteCode.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class AnnotationParser {

    // ***************************************
    // Logging
    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(AnnotationParser.class);

    // *******************************************
    // ClassPool for Loading Compile time Classes
    ClassPool cp;

    /**
     * Prevent the Instantiation
     */
    public AnnotationParser() {
        cp = ClassPool.getDefault();
        cp.insertClassPath(new ClassClassPath(this.getClass()));
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Class, Field and Method Annotations.
     *
     * @param clazz a {@link java.lang.Class} object.
     * @return a {@link jeffaschenk.commons.parsers.objects.ClassAnnotationData} object.
     */
    public ClassAnnotationData parse(Class<?> clazz) {
        // ************************************
        // Obtain Our Class.
        CtClass cc = null;
        try {
            cc = cp.get(clazz.getName());
        } catch (javassist.NotFoundException nfe) {
            log.error("Class Not Found:[" + clazz.getName() + "], "
                    + nfe.getMessage() + ".");
        }
        // ************************************
        // Did we obtain the Javassist Object?
        if (cc == null) {
            return null;
        }

        // ************************************
        // Obtain the Class Annotations.
        ClassAnnotationData ad = new ClassAnnotationData(cc.getName());
        ad.setSimpleName(cc.getSimpleName());
        ad.setClassAnnotations(this.parseClassAnnotations(cc));
        ad.setFieldAnnotations(this.parseClassFieldAnnotations(cc));
        ad.setMethodAnnotations(this
                .parseClassMethodAnnotations(cc));

        // ************************************
        // Return the Association Map.
        return ad;
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Class Annotations.
     *
     * @param clazz a {@link java.lang.Class} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> parseClassAnnotations(Class<?> clazz) {
        CtClass cc = null;
        try {
            cc = cp.get(clazz.getName());
            // ************************************
            // Return the Association Map.
            return parseClassAnnotations(cc);
        } catch (javassist.NotFoundException nfe) {
            log.error("Class Not Found:[" + clazz.getName() + "], "
                    + nfe.getMessage() + ".");
            return null;
        }
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Class, Field and Method Annotations.
     *
     * @param cc a {@link javassist.CtClass} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> parseClassAnnotations(CtClass cc) {
        // *************************************
        // Initialize.
        Map<String, String> annotations = new HashMap<String, String>();
        // *************************************
        // Parse the Annotations for this Class.
        if (log.isTraceEnabled()) {
            log.trace("Class:[" + cc.getSimpleName() + "]");
        }

        // ***************************************************
        // Check for all Visible Annotations.
        AnnotationsAttribute attr = (AnnotationsAttribute) cc.getClassFile()
                .getAttribute(AnnotationsAttribute.visibleTag);
        if (attr != null) {
            for (javassist.bytecode.annotation.Annotation an : attr
                    .getAnnotations()) {
                // **********************************************
                // Check for any Annotation Members
                if (an.getMemberNames() != null) {
                    for (Object name : an.getMemberNames()) {
                        if (log.isTraceEnabled()) {
                            log.trace("*** Class Annotation:["
                                    + an.getTypeName()
                                    + "], Member Name:["
                                    + name
                                    + "], Value:["
                                    + an.getMemberValue(name.toString())
                                    .toString().replace("\042", "").trim() + "]");
                        }
                        // ************************************************
                        // Save Annotations in association Named Value Map.
                        annotations.put(an.getTypeName() + "."
                                + name.toString(), an.getMemberValue(
                                name.toString()).toString().replace("\042", "").trim());
                    } // End of Inner For Each Loop for Annotation Members
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("*** Class Annotation:[" + an.getTypeName()
                                + "]");
                    }
                    // ************************************************
                    // Save Annotations in association Named Value Map.
                    annotations.put(an.getTypeName(), "");
                }
            } // End of Outer For Loop for Visible Annotations
        } // End of Check for Visible Annotations.

        // ************************************
        // Return the Association Map.
        return annotations;
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Field Annotations.
     *
     * @param clazz a {@link java.lang.Class} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> parseClassFieldAnnotations(Class<?> clazz) {
        CtClass cc = null;
        try {
            cc = cp.get(clazz.getName());
            // ************************************
            // Return the Association Map.
            return parseClassFieldAnnotations(cc);
        } catch (javassist.NotFoundException nfe) {
            log.error("Class Not Found:[" + clazz.getName() + "], "
                    + nfe.getMessage() + ".");
            return null;
        }
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Field Annotations.
     *
     * @param cc a {@link javassist.CtClass} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> parseClassFieldAnnotations(CtClass cc) {
        // *************************************
        // Initialize.
        Map<String, String> annotations = new HashMap<String, String>();
        // *******************************************
        // Parse the Field Annotations for this Class.

        CtField[] fields = cc.getDeclaredFields();
        if (fields == null) {
            return annotations;
        }
        for (CtField field : fields) {
            // ******************************************************
            // Skip standard serialVersionUID Fields.
            if (field.getName().equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            if (log.isTraceEnabled()) {
                log.trace("*** Field:[" + field.getName() + "]");
            }
            // ***************************************************
            // Check for all Visible Field Annotations.
            AnnotationsAttribute attr = (AnnotationsAttribute) field
                    .getFieldInfo().getAttribute(
                            AnnotationsAttribute.visibleTag);
            if (attr != null) {
                for (javassist.bytecode.annotation.Annotation an : attr
                        .getAnnotations()) {
                    // **********************************************
                    // Check for any Annotation Members
                    if (an.getMemberNames() != null) {
                        for (Object name : an.getMemberNames()) {
                            if (log.isTraceEnabled()) {
                                log.trace("*** Field:["
                                        + field.getName()
                                        + "] Annotation:["
                                        + an.getTypeName()
                                        + "], Member Name:["
                                        + name
                                        + "], Value:["
                                        + an.getMemberValue(name.toString())
                                        .toString().replace("\042", "").trim() + "]");
                            }
                            // ************************************************
                            // Save Annotations in association Named Value Map.
                            annotations.put(field.getName() + ":" + an.getTypeName() + "."
                                    + name.toString(), an.getMemberValue(
                                    name.toString()).toString().replace("\042", "").trim());
                        } // End of Inner For Each Loop for Annotation
                        // Members
                    } else {
                        if (log.isTraceEnabled()) {
                            log
                                    .trace("*** Field:[" + field.getName()
                                            + "] Annotation:["
                                            + an.getTypeName() + "]");
                        }
                        // ************************************************
                        // Save Annotations in association Named Value Map.
                        annotations.put(an.getTypeName(), "");
                    }
                } // End of Outer For Loop for Visible Annotations
            } // End of Check for Visible Annotations.

        } // End of Fields For Each Loop.

        // ************************************
        // Return the Association Map.
        return annotations;
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Method Annotations.
     *
     * @param clazz a {@link java.lang.Class} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> parseClassMethodAnnotations(Class<?> clazz) {
        CtClass cc = null;
        try {
            ClassPool cp = new ClassPool();
            cc = cp.get(clazz.getName());
            // ************************************
            // Return the Association Map.
            return parseClassMethodAnnotations(cc);
        } catch (javassist.NotFoundException nfe) {
            log.error("Class Not Found:[" + clazz.getName() + "], "
                    + nfe.getMessage() + ".");
            return null;
        }
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Method Annotations.
     *
     * @param cc a {@link javassist.CtClass} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> parseClassMethodAnnotations(CtClass cc) {
        // *************************************
        // Initialize.
        Map<String, String> annotations = new HashMap<String, String>();
        // *******************************************
        // Parse the Field Annotations for this Class.

        CtMethod[] methods = cc.getDeclaredMethods();
        if (methods == null) {
            return annotations;
        }
        for (CtMethod method : methods) {
            if (log.isTraceEnabled()) {
                log.trace("*** Method:[" + method.getName() + "]");
            }
            // ***************************************************
            // Check for all Visible Field Annotations.
            AnnotationsAttribute attr = (AnnotationsAttribute) method
                    .getMethodInfo().getAttribute(
                            AnnotationsAttribute.visibleTag);
            if (attr != null) {
                for (javassist.bytecode.annotation.Annotation an : attr
                        .getAnnotations()) {
                    // **********************************************
                    // Check for any Annotation Members
                    if (an.getMemberNames() != null) {
                        for (Object name : an.getMemberNames()) {
                            if (log.isTraceEnabled()) {
                                log.trace("*** Method:["
                                        + method.getName()
                                        + "] Annotation:["
                                        + an.getTypeName()
                                        + "], Member Name:["
                                        + name
                                        + "], Value:["
                                        + an.getMemberValue(name.toString())
                                        .toString().replace("\042", "").trim() + "]");
                            }
                            // ************************************************
                            // Save Annotations in association Named Value Map.
                            annotations.put(method.getName() + ":" + an.getTypeName() + "."
                                    + name.toString(), an.getMemberValue(
                                    name.toString()).toString().replace("\042", "").trim());
                        } // End of Inner For Each Loop for Annotation
                        // Members
                    } else {
                        if (log.isTraceEnabled()) {
                            log
                                    .trace("*** Method:[" + method.getName()
                                            + "] Annotation:["
                                            + an.getTypeName() + "]");
                        }
                        // ************************************************
                        // Save Annotations in association Named Value Map.
                        annotations.put(an.getTypeName(), "");
                    }
                } // End of Outer For Loop for Visible Annotations
            } // End of Check for Visible Annotations.

        } // End of Fields For Each Loop.

        // ************************************
        // Return the Association Map.
        return annotations;
    }

    /**
     * Parse a Classes Annotations and return in an Named Value Association
     * Form, return Method Annotations.
     *
     * @param method a {@link java.lang.reflect.Method} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> parseSpecificClassMethodAnnotations(Method method) {
        // *************************************
        // Initialize.
        Map<String, String> annotations = new HashMap<String, String>();
        // ************************************
        // Obtain Our Class.
        CtClass cc = null;
        try {
            cc = cp.get(method.getDeclaringClass().getName());
        } catch (javassist.NotFoundException nfe) {
            log.error("Class Not Found:[" + method.getDeclaringClass().getName() + "], "
                    + nfe.getMessage() + ".");
        }
        // ************************************
        // Did we obtain the Javassist Object?
        if (cc == null) {
            return null;
        }
        // *******************************************
        // Parse the Annotations for this Method.
        CtMethod[] methods = cc.getDeclaredMethods();
        if (methods == null) {
            return annotations;
        }
        for (CtMethod ctmethod : methods) {
            if (log.isTraceEnabled()) {
                log.trace("*** Method:[" + method.getName() + "]");
            }
            // ***************************************************
            // Check for all Visible Field Annotations.
            AnnotationsAttribute attr = (AnnotationsAttribute) ctmethod
                    .getMethodInfo().getAttribute(
                            AnnotationsAttribute.visibleTag);
            if (attr != null) {
                for (javassist.bytecode.annotation.Annotation an : attr
                        .getAnnotations()) {
                    // **********************************************
                    // Check for any Annotation Members
                    if (an.getMemberNames() != null) {
                        for (Object name : an.getMemberNames()) {
                            if (log.isTraceEnabled()) {
                                log.trace("*** Method:["
                                        + method.getName()
                                        + "] Annotation:["
                                        + an.getTypeName()
                                        + "], Member Name:["
                                        + name
                                        + "], Value:["
                                        + an.getMemberValue(name.toString())
                                        .toString().replace("\042", "").trim() + "]");
                            }
                            // ************************************************
                            // Save Annotations in association Named Value Map.
                            annotations.put(method.getName() + ":" + an.getTypeName() + "."
                                    + name.toString(), an.getMemberValue(
                                    name.toString()).toString().replace("\042", "").trim());
                        } // End of Inner For Each Loop for Annotation
                        // Members
                    } else {
                        if (log.isTraceEnabled()) {
                            log
                                    .trace("*** Method:[" + method.getName()
                                            + "] Annotation:["
                                            + an.getTypeName() + "]");
                        }
                        // ************************************************
                        // Save Annotations in association Named Value Map.
                        annotations.put(an.getTypeName(), "");
                    }
                } // End of Outer For Loop for Visible Annotations
            } // End of Check for Visible Annotations.

        } // End of Fields For Each Loop.

        // ************************************
        // Return the Association Map.
        return annotations;
    }

}
