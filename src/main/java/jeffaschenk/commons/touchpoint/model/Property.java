package jeffaschenk.commons.touchpoint.model;

import jeffaschenk.commons.util.TimeUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "properties", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Property extends RootElement implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private boolean shared;

    private Date timestamp;

    private Element element;

     private Set<PropertyValue> propertyValues = new HashSet<PropertyValue>(0);

    public Property() {
        super();
        this.timestamp = TimeUtils.getNow();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "propertySequenceGenerator")
    @GenericGenerator(name = "propertySequenceGenerator", strategy = "jeffaschenk.commons.touchpoint.model.dao.support.hibernate.PlatformSequenceGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "sequence", value = "properties_seq_id")})
    public Integer getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", nullable = true)
    public Element getElement() {
        return this.element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    @Column(name = "name", unique = true, nullable = false, length = 4096)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "property", cascade = {CascadeType.ALL})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    public Set<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(Set<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }
}
