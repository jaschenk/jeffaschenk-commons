package jeffaschenk.commons.model.oxm.searchresultsgroup;

import jeffaschenk.commons.util.StringUtils;
import net.sf.oval.constraint.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SearchResultGroupMetaSpringBeanReference
 * JAVA Pojo representation of the contents of the <code>SearchResultsGroup</code> "meta" field/property.
 * <p/>
 * Specific for DB Query.
 *
 * @author jeffaschenk@gmail.com
 *         <p/>
 *         Date: Oct 6, 2010
 *         Time: 12:44:45 PM
 */
@XmlRootElement(name = "spring-bean")
public class SearchResultGroupMetaSpringBeanReference implements java.io.Serializable {

    private static final long serialVersionUID = 1102L;

    /**
     * String of Spring Bean Reference Name
     */
    @NotNull
    @NotEmpty
    private String springBeanReferenceName;

    /**
     * Default Constructor
     */
    public SearchResultGroupMetaSpringBeanReference() {
    }

    /**
     * Name of Spring Bean reference name to be used
     * to resolve the necessary query and in-turn the resultant object(s).
     *
     * @return String
     */
    @XmlAttribute(name = "ref")
    public String getSpringBeanReferenceName() {
        return springBeanReferenceName;
    }

    public void setSpringBeanReferenceName(String springBeanReferenceName) {
        this.springBeanReferenceName = springBeanReferenceName;
    }

    /**
     * toString Override.
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":["
                + (StringUtils.isEmpty(springBeanReferenceName) ? "" : "SPRING BEAN REF:[" + this.getSpringBeanReferenceName() + "]");
    }

    /**
     * Override Equals
     *
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchResultGroupMetaSpringBeanReference)) {
            return false;
        }

        SearchResultGroupMetaSpringBeanReference that = (SearchResultGroupMetaSpringBeanReference) o;

        if (springBeanReferenceName != null ? !springBeanReferenceName.equals(that.springBeanReferenceName) : that.springBeanReferenceName != null) {
            return false;
        }

        return true;
    }

    /**
     * Override HashCode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        int result = springBeanReferenceName != null ? springBeanReferenceName.hashCode() : 0;
        return result;
    }
}
