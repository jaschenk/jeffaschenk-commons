package jeffaschenk.commons.parameters;

/**
 * Provides EntityPath Parameter Object
 *
 * @author jeffaschenk@gmail.com
 *         Date: Mar 24, 2010
 *         Time: 2:09:11 PM
 */
public class EntityPath implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String entityPath;

    /**
     * Default Constructor
     *
     * @param entityPath
     */
    public EntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    /**
     * Default for Placeholder
     */
    public EntityPath() {
    }

    /**
     * JXPath Interpreted Path
     *
     * @return String of Entity Path.
     */
    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public boolean isEmpty() {
        return ((this.getEntityPath() == null) || (this.getEntityPath().isEmpty()));
    }

    @Override
    public String toString() {
        return "EntityPath{" +
                "entityPath='" + entityPath + '\'' +
                '}';
    }

}
