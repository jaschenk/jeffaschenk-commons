package jeffaschenk.commons.system.internal.scheduling.events;

import java.util.Map;
import java.util.TreeMap;

/**
 * Simple Enum for Type of Life Cycle Services Event
 *
 * @author jeffaschenk@gmail.com
 */
public enum LifeCycleServiceStateType {

    /**
     * Enumerator Values
     */
    BEGIN(1, "Begin", "B"),
    DONE(2, "Done", "D"),
    FAILURE(3, "Failure", "F"),
    UPDATE_CLEANUP(4, "Update CleanUp", "U"),
    NO_STATE(5, "No State", "N");

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
    LifeCycleServiceStateType(int index, String text, String code) {
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
        for (LifeCycleServiceStateType type : LifeCycleServiceStateType.values()) {
            typeMap.put(String.valueOf(type.code()), type.text());
        }
        return typeMap;
    }

    public static String getTypeText(String code) {
        for (LifeCycleServiceStateType type : LifeCycleServiceStateType.values()) {
            if (type.code().equalsIgnoreCase(code)) {
                return type.text();
            }
        }
        return null;
    }

}
