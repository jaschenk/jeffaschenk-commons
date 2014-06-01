package jeffaschenk.commons.touchpoint.model.wrappers;

import jeffaschenk.commons.util.StringUtils;

import java.util.Properties;

/**
 * Update Mapping Helper
 * <p/>
 * Helps map an Entity Object to a Class to provide
 * indication if Entity should be updated or not based
 * upon specified class and associated method.
 * <p/>
 * This Class will normally be instantiated from the Spring Bean Definitions.
 *
 * @author jeffaschenk@gmail.com
 *
 */
public class UpdateProcessingMappings {


    /**
     * Filter Mappings
     */
    private Properties updateProcessingMappings = new Properties();

    /**
     * Getter to obtain Filter Mappings
     *
     * @return Properties
     */
    public Properties getUpdateProcessingMappings() {
        return updateProcessingMappings;
    }

    public void setUpdateProcessingMappings(Properties updateProcessingMappings) {
        this.updateProcessingMappings = updateProcessingMappings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Update Determination Mappings:{ ");
        for (Object key : updateProcessingMappings.keySet()) {
            String value = (String) updateProcessingMappings.get(key);
            if (value != null) {
                sb.append(System.getProperty("line.separator"));
                sb.append("   Entity ClassName:[" + key + "] references Update Determination BeanName:[" + value + "] ");
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
        for (Object mappingclassName : getUpdateProcessingMappings().keySet()) {
            if (entityClassName.equals( (String) mappingclassName)) {
                return (String) getUpdateProcessingMappings().get(mappingclassName);
            }
        }
        return null;
    }


}
