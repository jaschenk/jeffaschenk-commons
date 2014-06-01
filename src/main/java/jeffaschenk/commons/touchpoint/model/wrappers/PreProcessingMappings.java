package jeffaschenk.commons.touchpoint.model.wrappers;

import jeffaschenk.commons.util.StringUtils;

import java.util.Properties;

/**
 * Update Mapping Helper
 * <p/>
 * Helps map an Entity Object to a Class to provide
 * any pre-processing aspects, such as clearing
 * a database table or other PreProcessing task nececessary
 * to be performed prior to Extract Processing for that Table
 * and associated Objects.
 * <p/>
 * This Class will normally be instantiated from the Spring Bean Definitions.
 *
 * @author jeffaschenk@gmail.com
 */
public class PreProcessingMappings {


    /**
     * Filter Mappings
     */
    private Properties preProcessingMappings = new Properties();

    /**
     * Getter to obtain Filter Mappings
     *
     * @return Properties
     */
    public Properties getPreProcessingMappings() {
        return preProcessingMappings;
    }

    public void setPreProcessingMappings(Properties preProcessingMappings) {
        this.preProcessingMappings = preProcessingMappings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Post Processing Mappings:{ ");
        for (Object key : preProcessingMappings.keySet()) {
            String value = (String) preProcessingMappings.get(key);
            if (value != null) {
                sb.append(System.getProperty("line.separator"));
                sb.append("   Entity ClassName:[" + key + "] references Pre-Processing BeanName:[" + value + "] ");
            }
        }
        sb.append(" }");
        return sb.toString();
    }

    /**
     * Get the Mapped Class Name for the supplied Extract FileName.
     *
     * @param entityClassName
     * @return String - of the found ClassName or Null if not Found.
     */
    public String getMappedClassName(final String entityClassName) {
        if (StringUtils.isEmpty(entityClassName)) {
            return null;
        }
        // **************************************
        // Lookup our ClassName to Class Mapping
        for (Object mappingclassName : getPreProcessingMappings().keySet()) {
            if (entityClassName.equals( (String) mappingclassName)) {
                return (String) getPreProcessingMappings().get(mappingclassName);
            }
        }
        return null;
    }


}
