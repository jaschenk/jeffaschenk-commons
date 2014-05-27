package jeffaschenk.commons.touchpoint.model;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * RootElement
 * Abstract Class for common Inclusion by other Objects and Providing common root for all objects.
 *
 * @author Jeff Schenk
 * @version $Id: $
 */
@MappedSuperclass
public abstract class RootElement implements Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    protected RootElement() {
        this.id = new Integer(0);
    }

    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RootElement other = (RootElement) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " ID:[" + ((this.id == null) ? "No ID yet" : this.id.toString()) + "]";
    }

}
