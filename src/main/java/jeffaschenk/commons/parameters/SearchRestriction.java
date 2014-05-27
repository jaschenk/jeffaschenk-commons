package jeffaschenk.commons.parameters;


/**
 * Provides Class to Specify Distinct Search Restrictions
 *
 * @author jeffaschenk@gmail.com
 *         Date: Apr 21, 2010
 *         Time: 8:45:10 AM
 */
public class SearchRestriction implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Attribute/Property/Field/Column Name
     */
    private String name;

    /**
     * Object Value to be compared against
     */
    private Object[] values;

    /**
     * Ignore Case Indicator
     */
    private boolean ignoreCase;

    /**
     * Search Restriction Operation.
     */
    private Operation op;

    /**
     * Default Constructor
     */
    private SearchRestriction() {
    }

    /**
     * Default Constructor with all Necessary Fields and single value operation.
     *
     * @param name
     * @param values
     * @param op
     * @param ignoreCase
     */
    protected SearchRestriction(String name, Object[] values, Operation op, boolean ignoreCase) {
        // *************************************
        // Perform constructor Validation.
        if ((name == null) || (name.isEmpty())) {
            throw new IllegalArgumentException("Search Restriction Name of Field/Property/Column is Null!");
        }
        if ((values == null) || (values.length <= 0)) {
            throw new IllegalArgumentException("No Search Restriction Values Specified!");
        }
        if (op == null) {
            throw new IllegalArgumentException("Search Restriction Operation is Null!");
        }
        if (op.allowedNumberOfValues() != values.length) {
            throw new IllegalArgumentException("This Constructor requires that for the " +
                    op.name() + " Operation takes " + op.allowedNumberOfValues() + " Values, there are " + values.length + " values Specified!");

        }
        // ***********************************
        // Set Fields
        this.name = name;
        this.values = values;
        this.ignoreCase = ignoreCase;
        this.op = op;
    }

    /**
     * Static Constructor with all Necessary Fields and single value operation.
     *
     * @param name
     * @param value
     * @param op
     * @param ignoreCase
     */
    public static SearchRestriction SearchRestriction(String name, Object value, Operation op, boolean ignoreCase) {
        return new SearchRestriction(name, new Object[]{value}, op, ignoreCase);
    }

    /**
     * Static Constructor with all Necessary Fields and single value operation.
     *
     * @param name
     * @param value
     * @param op
     */
    public static SearchRestriction SearchRestriction(String name, Object value, Operation op) {
        return new SearchRestriction(name, new Object[]{value}, op, false);
    }

    /**
     * Static Constructor with all Necessary Fields and single value operation.
     *
     * @param name
     * @param values
     * @param op
     */
    public static SearchRestriction SearchRestriction(String name, Object[] values, Operation op) {
        return new SearchRestriction(name, values, op, false);
    }

    /**
     * Get the Attribute/Field/Property/Column Name
     *
     * @return String of A/F/P/C Name
     */
    public String getName() {
        return name;
    }

    /**
     * Helper Method to obtain a single value.
     *
     * @return
     */
    public Object getValue() {
        if ((getValues() != null) && (getValues().length >= 1)) {
            return getValues()[0];
        } else {
            return null;
        }
    }

    /**
     * Get All available Values
     *
     * @return Object[] Array of Values
     */
    public Object[] getValues() {
        return values;
    }

    /**
     * Indicates if IgnoreCase should be applied.
     *
     * @return boolean
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * Get Operation
     *
     * @return Operation
     */
    public Operation getOp() {
        return op;
    }


    /**
     * Search Restriction Operations
     */
    public enum Operation {
        /**
         * Search Operations
         */
        EQ("eq", 1),
        NE("ne", 1),
        LIKE("like", 1),
        ILIKE("ilike", 1),
        GT("gt", 1),
        LT("lt", 1),
        LE("le", 1),
        GE("ge", 1),
        BETWEEN("between", 2);

        /**
         * Associated HQL
         */
        private final String hql;
        /**
         * Allowed Number of Values.
         */
        private final int numberOfValues;

        /**
         * Default Constructor for Enumerator
         *
         * @param hql
         */
        Operation(String hql, int numberOfValues) {
            this.hql = hql;
            this.numberOfValues = numberOfValues;
        }

        public String hql() {
            return this.hql;
        }

        public int allowedNumberOfValues() {
            return this.numberOfValues;
        }

    }

}
