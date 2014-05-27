package jeffaschenk.commons.touchpoint.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ancestry_elements")
public class AncestryElement extends RootElement implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Ancestry ancestry;

    private Element element;


    public AncestryElement() {
        super();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ancestryElementSequenceGenerator")
    @GenericGenerator(name = "ancestryElementSequenceGenerator", strategy = "jeffaschenk.commons.touchpoint.model.dao.support.hibernate.PlatformSequenceGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "sequence", value = "ancestry_elements_seq_id")})
    public Integer getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ancestry_id", nullable = false)
    public Ancestry getAncestry() {
        return ancestry;
    }

    public void setAncestry(Ancestry ancestry) {
        this.ancestry = ancestry;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", nullable = false)
    public Element getElement() {
        return this.element;
    }

    public void setElement(Element element) {
        this.element = element;
    }



}
