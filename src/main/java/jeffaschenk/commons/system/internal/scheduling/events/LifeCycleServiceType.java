package jeffaschenk.commons.system.internal.scheduling.events;

import java.util.Map;
import java.util.TreeMap;

/**
 * Simple Enum for Type of Life Cycle Services Event
 *
 * @author jeffaschenk@gmail.com
 */
public enum LifeCycleServiceType {

    /**
     * Enumerator Values
     */
    EXTRACT(1, "Extract", "E"),
    EXPORT(2, "Export", "O"),
    STATUS(3, "Status", "S"),
    ZONE(4, "Zone", "Z"),
    DEMOGRAPHIC_UPDATES_COMPLETED_LIFECYCLE(5, "DemographicsUpdatesCompletedLifecycle", "D"),
    IMPORT(6, "Import", "I"),
    LIST_GENERATION(7, "ListGeneration", "L");

    /**
     * Index
     */
    private final int index;
    /**
     * Text for Type
     */
    private final String text;
    /**
     * Type Code
     */
    private final String code;

    /**
     * Default enum Constructor
     *
     * @param index Index of Code
     * @param text  String Text of Code
     * @param code  Code
     */
    LifeCycleServiceType(int index, String text, String code) {
        this.index = index;
        this.text = text;
        this.code = code;
    }

    public int index() {
        return index;
    }

    public String text() {
        return text;
    }

    public String code() {
        return code;
    }

    public static Map<String, String> getTypes() {
        Map<String, String> typeMap = new TreeMap<String, String>();
        for (LifeCycleServiceType type : LifeCycleServiceType.values()) {
            typeMap.put(String.valueOf(type.code()), type.text());
        }
        return typeMap;
    }

    public static String getTypeText(String code) {
        for (LifeCycleServiceType type : LifeCycleServiceType.values()) {
            if (type.code().equalsIgnoreCase(code)) {
                return type.text();
            }
        }
        return null;
    }

}
