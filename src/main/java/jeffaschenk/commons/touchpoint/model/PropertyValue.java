package jeffaschenk.commons.touchpoint.model;

import jeffaschenk.commons.util.TimeUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "values")
public class PropertyValue extends RootElement implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String value;

    private boolean shared;

    private Date timestamp;

    private Property property;

    public PropertyValue() {
        super();
        this.timestamp = TimeUtils.getNow();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "valueSequenceGenerator")
    @GenericGenerator(name = "valueSequenceGenerator", strategy = "jeffaschenk.commons.touchpoint.model.dao.support.hibernate.PlatformSequenceGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "sequence", value = "values_seq_id")})
    public Integer getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = true)
    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Column(name = "value", unique = true, nullable = false, length = 4096)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "shared")
    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Column(name = "timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
