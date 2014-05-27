package jeffaschenk.commons.types;

/**
 * User Status Codes Enumerator
 *
 * @author jeffaschenk@gmail.com
 */
public enum UserStatus {

    /**
     * Enumerator Values
     */
    NEW(1, "New", "N"),
    VERIFIED(2, "Verified", "V"),
    SUSPENDED(3, "Suspended", "S"),
    TERMINATED(4, "Terminated", "T"),
    DEACTIVATED(5, "Deactivated", "D"),
    PURGE(6, "Purge", "P");

    /**
     * Index
     */
    private final int index;
    /**
     * Text for Approval Status
     */
    private final String text;
    /**
     * Approval Status Code
     */
    private final String code;

    /**
     * Default enum Constructor
     *
     * @param index Index of Code
     * @param text  String Text of approval Code
     * @param code  Approval Code
     */
    UserStatus(int index, String text, String code) {
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

}
