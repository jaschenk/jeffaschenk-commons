package jeffaschenk.commons.touchpoint.model.oxm.searchresultsgroup;

import jeffaschenk.commons.util.StringUtils;
import net.sf.oval.constraint.Assert;
import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.NotEmpty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SearchResultGroupMeta
 * JAVA Pojo representation of the contents of the <code>SearchResultsGroup</code> "meta" field/property.
 *
 * @author jeffaschenk@gmail.com
 *         <p/>
 *         Date: Oct 6, 2010
 *         Time: 12:44:45 PM
 */
@XmlRootElement(name = "meta")
public class SearchResultGroupMeta implements java.io.Serializable {
    private static final long serialVersionUID = 1102L;

    /**
     * Static Types
     */
    public final static String META_TYPE_DATABASE = "DB";
    public final static String META_TYPE_INDEX = "IX";

    /**
     * Search Result Group SID
     */
    @NotEmpty
    private String sid;

    /**
     * Search Result Group Owner Registered User SID
     */
    @NotEmpty
    private String ownerSid;

    /**
     * Type of Meta, either DB, Database or IX, Index.
     */
    @Assert(expr = "_value == \"DB\" || _value == \"IX\"", lang = "groovy")
    private String type;

    /**
     * Name of Object to be returned from the represented Search Group.
     * Returns
     */
    @NotEmpty
    private String returns;

    /**
     * Optional Content of Solr/Lucene Search
     */
    @NotEmpty(when = "groovy:_this.isIndexQuery()")
    private String search;

    /**
     * Optional Content of Spring Bean used for Query.
     */
    @AssertValid(when = "groovy:_this.isDatabaseQuery()")
    private SearchResultGroupMetaSpringBeanReference springBeanReference;

    @NotEmpty
    private String title;

    private List<String> statuses;

    /**
     * Default Constructor
     */
    public SearchResultGroupMeta() {
    }

    /**
     * Search Result Group SID, from which this XML Metadata is stored and maintained.
     *
     * @return String - SID of SearchResultGroup
     */
    @XmlAttribute(name = "sid")
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * Search Result Group Owner Registered User SID
     *
     * @return String - SID of Registered User Owner
     */
    @XmlAttribute(name = "ownerSid")
    public String getOwnerSid() {
        return ownerSid;
    }

    public void setOwnerSid(String ownerSid) {
        this.ownerSid = ownerSid;
    }

    /**
     * Type of Meta
     * "DB" or "INDEX"
     *
     * @return String - of Meta
     */
    @XmlAttribute(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Name of the Object to be used to be instantiated to represent results.
     *
     * @return String
     */
    @XmlAttribute(name = "returns")
    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

     /**
     * Search Query String for Solr/Lucene
     * @return String - Search String
     */
    @XmlElement(name="search")
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    /**
     * Spring Bean Reference used to invoke a Bean's backing Query.
     *
     * @return SearchResultGroupMetaSpringBeanReference - contains Bean Reference Name
     */
    @XmlElementRef
    public SearchResultGroupMetaSpringBeanReference getSpringBeanReference() {
        return springBeanReference;
    }

    public void setSpringBeanReference(SearchResultGroupMetaSpringBeanReference springBeanReference) {
        this.springBeanReference = springBeanReference;
    }

    /**
     * Specifies which group detail view template to use depending on the springBeanReferenceName.
     * For now it defaults to the auction detail template and selects templates for two of the
     * search result group names.
     *
     * TODO: make this less primitive
     *
     * @return a string specifying a group detail template
     */
    public String getGroupDetailView() {
        return this.springBeanReference.getSpringBeanReferenceName().toLowerCase();
    }

    @XmlElement(name="title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElementWrapper(name = "statuses")
    @XmlElement(name="status")
    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    /**
     * Provides Indication if Query Type is specified as a Database Query or Not.
     * @return boolean
     */
    public boolean isDatabaseQuery() {
         if (StringUtils.isEmpty(this.type)) {
            return false;
        } else if ((this.type.equalsIgnoreCase(META_TYPE_DATABASE)) ||
                (this.type.equalsIgnoreCase("DATABASE"))) {
            return true;
        }
        return false;
    }

    /**
     * Provides Indication if Query Type is specified as a Database Query or Not.
     * @return boolean
     */
    public boolean isIndexQuery() {
        if (StringUtils.isEmpty(this.type)) {
            return false;
        } else if ((this.type.equalsIgnoreCase(META_TYPE_INDEX)) ||
                (this.type.equalsIgnoreCase("INDEX"))) {
            return true;
        }
        return false;
    }

    /**
     * toString Override.
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"[" + "SID:[" + this.getSid() + "], OWNERSID:[" + this.getOwnerSid() + "], "
                    + "TYPE:[" + this.getType() + "], RETURNS:[" + this.getReturns() + "] "
                    + ((StringUtils.isEmpty(search)) ? "" : ", SEARCH:["+this.getSearch()+"] ")
                    + ((this.getSpringBeanReference() == null) ? "" : this.getSpringBeanReference().toString()+ "]");
    }

    /**
     * Equals override to encompass all Fields
     *
     * @param o
     * @return boolean Equality Indicator
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchResultGroupMeta)) return false;

        SearchResultGroupMeta that = (SearchResultGroupMeta) o;

        if (ownerSid != null ? !ownerSid.equals(that.ownerSid) : that.ownerSid != null) return false;
        if (returns != null ? !returns.equals(that.returns) : that.returns != null) return false;
        if (search != null ? !search.equals(that.search) : that.search != null) return false;
        if (sid != null ? !sid.equals(that.sid) : that.sid != null) return false;
        if (springBeanReference != null ? !springBeanReference.equals(that.springBeanReference) : that.springBeanReference != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sid != null ? sid.hashCode() : 0;
        result = 31 * result + (ownerSid != null ? ownerSid.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (returns != null ? returns.hashCode() : 0);
        result = 31 * result + (search != null ? search.hashCode() : 0);
        result = 31 * result + (springBeanReference != null ? springBeanReference.hashCode() : 0);
        return result;
    }
}