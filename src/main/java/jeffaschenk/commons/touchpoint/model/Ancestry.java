package jeffaschenk.commons.touchpoint.model;

import jeffaschenk.commons.util.TimeUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ancestries", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Ancestry extends RootElement implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private Date timestamp;

    private Owner owner;

    private Ancestry parent;

    public Ancestry() {
        super();
        this.timestamp = TimeUtils.getNow();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ancestrySequenceGenerator")
    @GenericGenerator(name = "ancestrySequenceGenerator", strategy = "jeffaschenk.commons.touchpoint.model.dao.support.hibernate.PlatformSequenceGenerator",
            parameters = {@Parameter(name = "sequence", value = "ancestries_seq_id")})
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "name", unique = true, nullable = false, length = 4096)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    public Ancestry getParent() {
        return parent;
    }

    public void setParent(Ancestry parent) {
        this.parent = parent;
    }
}
