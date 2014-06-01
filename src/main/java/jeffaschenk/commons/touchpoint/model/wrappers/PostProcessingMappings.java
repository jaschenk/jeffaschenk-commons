package jeffaschenk.commons.touchpoint.model.wrappers;

import jeffaschenk.commons.util.StringUtils;

import java.util.Properties;

/**
 * Update Mapping Helper
 * <p/>
 * Provides any Post Table / Object Processing after
 * an Extract has been completed.
 *
 * <p/>
 * This Class will normally be instantiated from the Spring Bean Definitions.
 *
 * @author jeffaschenk@gmail.com
 *
 */
public class PostProcessingMappings {


    /**
     * Filter Mappings
     */
    private Properties postProcessingMappings = new Properties();

    /**
     * Getter to obtain Filter Mappings
     *
     * @return Properties
     */
    public Properties getPostProcessingMappings() {
        return postProcessingMappings;
    }

    public void setPostProcessingMappings(Properties postProcessingMappings) {
        this.postProcessingMappings = postProcessingMappings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Post Processing Mappings:{ ");
        for (Object key : postProcessingMappings.keySet()) {
            String value = (String) postProcessingMappings.get(key);
            if (value != null) {
                sb.append(System.getProperty("line.separator"));
                sb.append("   Entity ClassName:[" + key + "] references Post Processing BeanName:[" + value + "] ");
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
        for (Object mappingclassName : getPostProcessingMappings().keySet()) {
            if (entityClassName.equals( (String) mappingclassName)) {
                return (String) getPostProcessingMappings().get(mappingclassName);
            }
        }
        return null;
    }


}
