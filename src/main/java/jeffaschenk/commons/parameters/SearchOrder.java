package jeffaschenk.commons.parameters;

/**
 * Defines or specifies the search Order within a <code>SearchCriteria</code>.
 *
 * @author jeffaschenk@gmail.com
 *         Date: Apr 21, 2010
 *         Time: 9:19:55 AM
 */
public class SearchOrder implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Order Direction
     * <p/>
     * 0 = No Order
     * 1 = Ascending
     * 2 = Descending
     */
    private Order orderDirection;

    /**
     * Attribute/Property/Field/Column Name
     */
    private String name;

    public SearchOrder(String name, Order orderDirection) {
        this.name = name;
        this.orderDirection = orderDirection;
    }

    /**
     * Get Attribute/Property/Field/Column Name
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Order to be used for this Named Attribute.
     *
     * @return Order
     */
    public Order getOrderDirection() {
        return orderDirection;
    }

    public enum Order {
        /**
         * Order Enumerations
         */
        ASCENDING("asc"),
        DESCENDING("desc");

        /**
         * Private Fields
         */
        private final String hql; // Associated HQL

        Order(String hql) {
            this.hql = hql;
        }

        public String hql() {
            return this.hql;
        }

    }

}
