package jeffaschenk.commons.parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Criteria Object provides an Abstraction for upstream layers
 * to allow the necessary Search Criteria to be specified
 * for a given search.
 *
 * @author Jeff Schenk
 */
public class SearchCriteria implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Ordered List of Search Restrictions
     */
    private List<SearchRestriction> searchRestrictions = new ArrayList<SearchRestriction>();

    /**
     * Ordered List of Result Ordering
     */
    private List<SearchOrder> ordering = new ArrayList<SearchOrder>();

    /**
     * Obtain a List of all Search Restrictions contained in this Criteria.
     *
     * @return List<SearchRestriction> Established Search Restrictions.
     */
    public List<SearchRestriction> getSearchRestrictions() {
        return searchRestrictions;
    }

    /**
     * Obtain a List of all Search Order contained in this Criteria.
     *
     * @return List<SearchOrder> Established Search Order.
     */
    public List<SearchOrder> getOrdering() {
        return ordering;
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void eq(String name, Object value) {
        this.eq(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void eq(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.EQ, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void ne(String name, Object value) {
        this.ne(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void ne(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.NE, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void like(String name, Object value) {
        this.like(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void like(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.LIKE, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void ilike(String name, Object value) {
        this.ilike(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void ilike(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.ILIKE, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void gt(String name, Object value) {
        this.gt(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void gt(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.GT, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void lt(String name, Object value) {
        this.lt(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void lt(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.LT, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void le(String name, Object value) {
        this.le(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void le(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.LE, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name  Attribute/Property/Field/Column Name
     * @param value Value to be applied to Search Criteria
     */
    public void ge(String name, Object value) {
        this.ge(name, value, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name       Attribute/Property/Field/Column Name
     * @param value      Value to be applied to Search Criteria
     * @param ignoreCase indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void ge(String name, Object value, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, value, SearchRestriction.Operation.GE, ignoreCase));
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name         Attribute/Property/Field/Column Name
     * @param value        Value to be applied to Search Criteria
     * @param anotherValue the upper value for use within a between operation.
     */
    public void between(String name, Object value, Object anotherValue) {
        this.between(name, value, anotherValue, false);
    }

    /**
     * Helper Method to provide Simple Search Expression
     *
     * @param name         Attribute/Property/Field/Column Name
     * @param value        Value to be applied to Search Criteria
     * @param anotherValue the upper value for use within a between operation.
     * @param ignoreCase   indicates if Textual String Case should be ignored or not, default is not to ignore case.
     */
    public void between(String name, Object value, Object anotherValue, boolean ignoreCase) {
        searchRestrictions.add(SearchRestriction.SearchRestriction(name, new Object[]{value, anotherValue}, SearchRestriction.Operation.BETWEEN, ignoreCase));
    }

}
