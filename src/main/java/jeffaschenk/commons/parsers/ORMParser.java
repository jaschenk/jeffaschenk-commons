package jeffaschenk.commons.parsers;

import jeffaschenk.commons.parsers.objects.ClassAnnotationData;
import jeffaschenk.commons.parsers.objects.mapping.MappingClass;
import jeffaschenk.commons.parsers.objects.mapping.MappingClasses;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * ORM Parser to obtain the HIBERNATE ORM Mapping configuration and
 * place into an accessible Object form.
 * <p/>
 * Used in Various Tools
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class ORMParser {

    // ***************************************
    // Logging
    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(ORMParser.class);

    // ****************************************
    // System Property Defaults
    public static final String defaultORMConfiguration = "hibernate-mapping.cfg.xml";


    /**
     * Default Protected and Secure Constructor.
     */
    private ORMParser() {
    }

    /**
     * This is a helper to the helper to use the Default
     * ORM Configuration Mapping Resource.
     *
     * @return n array of {@link java.lang.Class} objects.
     */
    public static final Class<?>[] getORMResourceBundle() {
        return getORMResourceBundle(defaultORMConfiguration);
    }

    /**
     * This helper method will read in the contents of the HIBERNATE Mapping
     * Resource and parse all of the Mapped Class Names and in turn create an
     * instantiated list of Classes for the Upstream Caller to perform
     * annotation Parsing on those defined Mapped Classes.
     *
     * @param ormResourceName a {@link java.lang.String} object.
     * @return an array of {@link java.lang.Class} objects.
     */
    public static final Class<?>[] getORMResourceBundle(String ormResourceName) {
        BufferedReader bisr = null;
        Digester digester = null;
        try {
            // ******************************************
            // Ensure we get our Thread ClassLoader.
            // If we get the System ClassLoader and
            // we are in a container, such as TOMCAT,
            // we will not find our specific application
            // Resource.
            bisr = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(ormResourceName)));
            digester = new Digester();
            if (log.isTraceEnabled()) {
                digester.setLogger(log);
            }
            digester.setValidating(false);

            // *****************************
            // Obtain Mapping Class Objects
            digester.addObjectCreate("hibernate-configuration/session-factory",
                    MappingClasses.class);
            digester.addObjectCreate(
                    "hibernate-configuration/session-factory/mapping",
                    MappingClass.class);
            digester.addSetProperties(
                    "hibernate-configuration/session-factory/mapping", "class",
                    "name");
            digester.addSetNext(
                    "hibernate-configuration/session-factory/mapping",
                    "addMappingClass",
                    "jeffaschenk.commons.parsers.objects.mapping.MappingClass");

            // **********************************
            // Parse the Resource Bundle.
            MappingClasses mappingClasses = (MappingClasses) digester
                    .parse(bisr);
            Class<?>[] objectModelClasses = new Class<?>[mappingClasses.getMappingClasses().size()];
            // **********************************
            // Show Mapping Classes.
            int c = 0;
            for (MappingClass mappingClass : mappingClasses.getMappingClasses()) {
                try {
                    objectModelClasses[c] = Class.forName(mappingClass.getName());
                } catch (ClassNotFoundException cnfe) {
                    log.warn("Unable to instantiate ClassName:[" + mappingClass.getName() + "], due to Class Not Found, will Ignore.");
                }
                c++;
            } // End of For Each Loop.
            // ***********************************************
            // return our Class Array.
            return objectModelClasses;
        } catch (Exception e) {
            log.error("Error Parsing ORM Configuration: " + e.getMessage(), e);
            return null;
        } finally {
            digester = null;
            if (bisr != null) {
                try {
                    bisr.close();
                } catch (IOException ioe) {
                    log.error(
                            "IO Exception Encountered while Closing ORM Resource Bundle: "
                                    + ioe.getMessage(), ioe);
                }
            }
        }
    }

    /**
     * Produces the Mapping by parsing the Class File Annotations.
     *
     * @param objectModelClasses an array of {@link java.lang.Class} objects.
     * @return  {@link java.util.Map} object.
     */
    public static final Map<String, ClassAnnotationData> generateMapping(
            Class<?>[] objectModelClasses) {
        Map<String, ClassAnnotationData> mapping = new HashMap<String, ClassAnnotationData>();
        if (objectModelClasses == null) {
            return mapping;
        }
        // *****************************************
        // Establish our Annotation Parser.
        AnnotationParser ap = new AnnotationParser();
        // *****************************************
        // Iterate over all of the Class and
        // use reflection to obtain Class and
        // field Annotations.
        for (Class<?> clazz : objectModelClasses) {
            if (clazz == null) {
                continue;
            }
            // ***********************************
            // Obtain all Class annotations
            ClassAnnotationData annotationData = ap.parse(clazz);
            if (annotationData == null) {
                continue;
            }
            // ***********************************
            // Save Mapping Data For Lookup.
            mapping.put(annotationData.getClassName(), annotationData);
        } // End of ObjectClass For Each Loop.

        // *****************************************
        // Return the Mapping Object
        return mapping;
    }

}
