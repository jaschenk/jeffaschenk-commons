package jeffaschenk.commons.types;

/**
 * User Roles
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public enum UserRoles {

    /**
     * Enumerator Values
     */
    ADMIN(1, "admin", "Administrator"),
    SYSTEM(2, "system", "System"),
    USER(3, "user", "Client User");

    /**
     * Index
     */
    private final int index;
    /**
     * Role Text
     */
    private final String text;
    /**
     * Role Description
     */
    private final String description;

    /**
     * Default enum Constructor
     *
     * @param index       Index of Role
     * @param text        of Role
     * @param description of Role
     */
    UserRoles(int index, String text, String description) {
        this.index = index;
        this.text = text;
        this.description = description;
    }

    public int index() {
        return index;
    }

    public String text() {
        return text;
    }

    public String description() {
        return description;
    }

}
