package jeffaschenk.commons.touchpoint.model;

import jeffaschenk.commons.util.TimeUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "elements", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Element extends RootElement implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private boolean shared;

    private Date timestamp;

    private Owner owner;

    public Element() {
        super();
        this.timestamp = TimeUtils.getNow();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "elementSequenceGenerator")
    @GenericGenerator(name = "elementSequenceGenerator", strategy = "jeffaschenk.commons.touchpoint.model.dao.support.hibernate.PlatformSequenceGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "sequence", value = "elements_seq_id")})
    public Integer getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
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

}
