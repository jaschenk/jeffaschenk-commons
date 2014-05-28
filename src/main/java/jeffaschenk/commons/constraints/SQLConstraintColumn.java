package jeffaschenk.commons.constraints;


/**
 * SQL Constraint Object
 * <p/>
 * Helper Support Object to pass obtained Constraint Information back to
 * Exception and other processes for examination.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class SQLConstraintColumn implements java.io.Serializable {

    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constraint Column Name
     */
    private String columnName;

    /**
     * Constraint Position
     */
    private Integer position;


    /**
     * Constructor with all necessary Fields
     *
     * @param columnName a {@link java.lang.String} object.
     * @param position   a {@link java.lang.Integer} object.
     */
    public SQLConstraintColumn(String columnName, Integer position) {
        super();
        this.columnName = columnName;
        this.position = position;
    }

    /**
     * columnName
     *
     * @return  {@link java.lang.String} object.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * <p>Setter for the field <code>columnName</code>.</p>
     *
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * position
     *
     * @return  {@link java.lang.Integer} object.
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * <p>Setter for the field <code>position</code>.</p>
     *
     * @param position the position to set
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * HashCode Override
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((columnName == null) ? 0 : columnName.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Equals Override
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SQLConstraintColumn other = (SQLConstraintColumn) obj;
        if (columnName == null) {
            if (other.columnName != null)
                return false;
        } else if (!columnName.equals(other.columnName))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * toString Override, for Debugging
     */
    @Override
    public String toString() {
        return "Column Name:[" + columnName + "], Position:[" +
                ((position == null) ? "null" : position.toString()) + "]";
    }

}
