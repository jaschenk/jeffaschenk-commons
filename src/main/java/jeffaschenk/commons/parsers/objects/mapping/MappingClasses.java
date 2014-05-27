package jeffaschenk.commons.parsers.objects.mapping;

import java.util.Vector;

/**
 * Mapping Classes Object used by ORMParser and Digester.
 *
 * @author jeff.schenk
 * @version $Id: $
 */
public class MappingClasses {

    private Vector<MappingClass> mappingClasses = new Vector<MappingClass>();

    /**
     * <p>Getter for the field <code>mappingClasses</code>.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<MappingClass> getMappingClasses() {
        return mappingClasses;
    }

    /**
     * <p>Setter for the field <code>mappingClasses</code>.</p>
     *
     * @param mappingClasses a {@link java.util.Vector} object.
     */
    public void setMappingClasses(Vector<MappingClass> mappingClasses) {
        this.mappingClasses = mappingClasses;
    }

    /**
     * <p>addMappingClass</p>
     *
     * @param mappingClass a {@link jeffaschenk.commons.parsers.objects.mapping.MappingClass} object.
     */
    public void addMappingClass(MappingClass mappingClass) {
        this.mappingClasses.add(mappingClass);
    }

}
