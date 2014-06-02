package jeffaschenk.commons.identifiers;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AlternateId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String alternateId;

    public AlternateId() {
        super();
        this.alternateId = new RandomGUID().toString();
    }

    public AlternateId(String alt_id) {
        this.alternateId = alt_id;
    }

    public AlternateId(int alt_id) {
            this.alternateId = Integer.toString(alt_id);
    }

    @Column(name = "alternateId", nullable = false, length = 64)
    public String getAlternateId() {
        return this.alternateId;
    }

    public void setAlternateId(String alternateId) {
        this.alternateId = alternateId;
    }
    
    public void setAlternateId(int alt_id) {
            this.alternateId = Integer.toString(alt_id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlternateId alt_id1 = (AlternateId) o;

        if (alternateId != null ? !alternateId.equals(alt_id1.alternateId) : alt_id1.alternateId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return alternateId != null ? alternateId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "[" + ((this.alternateId == null) ? "No ID assigned]" : this.alternateId.toString()) + "]";
    }

}
