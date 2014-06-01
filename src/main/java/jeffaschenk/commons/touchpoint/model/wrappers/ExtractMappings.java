package jeffaschenk.commons.touchpoint.model.wrappers;

import jeffaschenk.commons.util.StringUtils;

import java.util.Properties;

/**
 * Extract Mapping Helper
 * <p/>
 * Helps map the Extract Files to Java Classes.
 * <p/>
 * This Class will normally be instantiated from the Spring Bean Definitions.
 *
 * @author jeffaschenk@gmail.com
 *
 */
public class ExtractMappings {


    /**
     * Filter Mappings
     */
    private Properties filterMappings = new Properties();

    /**
     * Getter to obtain Filter Mappings
     *
     * @return Properties
     */
    public Properties getFilterMappings() {
        return filterMappings;
    }

    public void setFilterMappings(Properties filterMappings) {
        this.filterMappings = filterMappings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ExtractMappings:{ ");
        for (Object key : filterMappings.keySet()) {
            String value = (String) filterMappings.get(key);
            if (value != null) {
                sb.append(System.getProperty("line.separator"));
                sb.append("   FileName Filter:[" + key + "] references ClassName:[" + value + "] ");
            }
        }
        sb.append(" }");
        return sb.toString();
    }

    /**
     * Get the Mapped Class Name for the supplied Extract FileName.
     *
     * @param extractFileName
     * @return String - of the found ClassName or Null if not Found.
     */
    public String getMappedClassName(final String extractFileName) {
        if (StringUtils.isEmpty(extractFileName)) {
            return null;
        }
        // **************************************
        // Lookup our fileName to Class Mapping
        for (Object filter : getFilterMappings().keySet()) {
            if (match(extractFileName, (String) filter)) {
                return (String) getFilterMappings().get(filter);
            }
        }
        return null;
    }

    /**
     * Private Helper Method.
     *
     * @param fileName
     * @param pattern
     * @return boolean
     */
    private static boolean match(final String fileName, final String pattern) {
        return (fileName.toUpperCase().trim().matches(pattern));
    }

}
