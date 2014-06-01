package jeffaschenk.commons.types;

import java.util.Map;
import java.util.TreeMap;

/**
 * simple Enum Type for the Watcher Statistic Type.
 *
 * @author jeffaschenk@gmail.com
 */
public enum WatcherStatisticType {

    /**
     * Enumerator Values
     */
    DROP_ZONE(1, "DROP_ZONE", "D"),
    PROCESSED_ZONE(2, "PROCESSED_ZONE", "P"),
    EXPORT_ZONE(3, "EXPORT_ZONE", "E");

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
    WatcherStatisticType(int index, String text, String code) {
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
        for (WatcherStatisticType type : WatcherStatisticType.values()) {
            typeMap.put(String.valueOf(type.code()), type.text());
        }
        return typeMap;
    }

    public static String getTypeText(String code) {
        for (WatcherStatisticType type : WatcherStatisticType.values()) {
            if (type.code().equalsIgnoreCase(code)) {
                return type.text();
            }
        }
        return null;
    }

}
