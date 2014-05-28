package jeffaschenk.commons.parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides EntityPath Parameter Object
 *
 * @author jeffaschenk@gmail.com
 */
public class EntityPathGroup implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private List<EntityPath> entityPaths = new ArrayList<EntityPath>();

    /**
     * Default Constructor
     */
    public EntityPathGroup() {
    }

    /**
     * IS the Path Group Empty.
     *
     * @return boolean indicating if Entity Paths is empty or not.
     */
    public boolean isEmpty() {
        return ((this.entityPaths == null) || (this.entityPaths.isEmpty()));
    }

    /**
     * Get Contain Entity Paths in this group.
     *
     * @return List<EntityPath>
     */
    public List<EntityPath> getEntityPaths() {
        return entityPaths;
    }

    /**
     * Set our EntityPaths
     *
     * @param entityPaths
     */
    public void setEntityPaths(List<EntityPath> entityPaths) {
        this.entityPaths = entityPaths;
    }

    /**
     * Helper method to add an EntityPath to a Group.
     *
     * @param entityPath
     * @return boolean indicating Path added to Group successfully or not.
     */
    public boolean addEntityPath(EntityPath entityPath) {
        if (entityPath == null) {
            return false;
        }
        return this.entityPaths.add(entityPath);
    }

    /**
     * Get the Entity Array from the Group.
     *
     * @return EntityPath[]
     */
    public EntityPath[] getEntityPathArray() {
        EntityPath[] paths = new EntityPath[this.getEntityPaths().size()];
        int i = 0;
        for (EntityPath entityPath : this.getEntityPaths()) {
            paths[i] = entityPath;
            i++;
        }
        return paths;
    }

}
