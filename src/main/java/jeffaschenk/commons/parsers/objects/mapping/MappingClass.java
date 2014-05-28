package jeffaschenk.commons.parsers.objects.mapping;

/**
 * Mapping Class Object used by ORMParser and Digester.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class MappingClass {

    private String name;

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

}
