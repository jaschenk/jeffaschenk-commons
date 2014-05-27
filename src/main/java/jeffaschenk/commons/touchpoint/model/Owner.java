package jeffaschenk.commons.touchpoint.model;

import jeffaschenk.commons.util.TimeUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owners", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Owner extends RootElement implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private Date timestamp;

    private Set<Element> elements = new HashSet<Element>(0);

    private Set<Ancestry> ancestries = new HashSet<Ancestry>(0);

    private Set<Action> actions = new HashSet<Action>(0);

    private Set<Group> groups = new HashSet<Group>(0);

    public Owner() {
        super();
        this.timestamp = TimeUtils.getNow();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ownerSequenceGenerator")
    @GenericGenerator(name = "ownerSequenceGenerator", strategy = "jeffaschenk.commons.touchpoint.model.dao.support.hibernate.PlatformSequenceGenerator",
            parameters = {@Parameter(name = "sequence", value = "owners_seq_id")})
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = {CascadeType.ALL})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    public Set<Element> getElements() {
        return this.elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = {CascadeType.ALL})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = {CascadeType.ALL})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = {CascadeType.ALL})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    public Set<Ancestry> getAncestries() {
        return ancestries;
    }

    public void setAncestries(Set<Ancestry> ancestries) {
        this.ancestries = ancestries;
    }
}
