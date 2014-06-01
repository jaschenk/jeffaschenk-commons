package jeffaschenk.commons.types;

import java.util.Map;
import java.util.TreeMap;

/**
 * simple Enum Type for the Status Output Type.
 *
 * @author jeffaschenk@gmail.com
 */
public enum StatusOutputType {

    /**
     * Enumerator Values
     */
    TEXT(1, "Text", "T"),
    HTML(2, "HTML", "H");

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
    StatusOutputType(int index, String text, String code) {
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
        for (StatusOutputType type : StatusOutputType.values()) {
            typeMap.put(String.valueOf(type.code()), type.text());
        }
        return typeMap;
    }

    public static String getTypeText(String code) {
        for (StatusOutputType type : StatusOutputType.values()) {
            if (type.code().equalsIgnoreCase(code)) {
                return type.text();
            }
        }
        return null;
    }

}
