package jeffaschenk.commons.constraints;

import java.util.List;

/**
 * SQL Constraint Object
 * <p/>
 * Helper Support Object to pass obtained Constraint Information back to
 * Exception and other processes for examination.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class SQLConstraint implements java.io.Serializable {

    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constraint Owner
     */
    private String owner;

    /**
     * constraint Name
     */
    private String constraintName;

    /**
     * Constraint Type
     */
    private String constraintType;

    /**
     * Table Name
     */
    private String tableName;

    /**
     * Search Condition
     */
    private String searchCondition;

    /**
     * List of Columns specified for this Constraint
     */
    private List<SQLConstraintColumn> SQLConstraintColumns;

    /**
     * owner
     *
     * @return  {@link java.lang.String} object.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * <p>Setter for the field <code>owner</code>.</p>
     *
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * constraintName
     *
     * @return  {@link java.lang.String} object.
     */
    public String getConstraintName() {
        return constraintName;
    }

    /**
     * <p>Setter for the field <code>constraintName</code>.</p>
     *
     * @param constraintName the constraintName to set
     */
    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    /**
     * constraintType
     *
     * @return  {@link java.lang.String} object.
     */
    public String getConstraintType() {
        return constraintType;
    }

    /**
     * <p>Setter for the field <code>constraintType</code>.</p>
     *
     * @param constraintType the constraintType to set
     */
    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    /**
     * tableName
     *
     * @return  {@link java.lang.String} object.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * <p>Setter for the field <code>tableName</code>.</p>
     *
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * searchCondition
     *
     * @return  {@link java.lang.String} object.
     */
    public String getSearchCondition() {
        return searchCondition;
    }

    /**
     * <p>Setter for the field <code>searchCondition</code>.</p>
     *
     * @param searchCondition the searchCondition to set
     */
    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    /**
     * SQLConstraintColumns
     *
     * @return  {@link java.util.List} object.
     */
    public List<SQLConstraintColumn> getSQLConstraintColumns() {
        return SQLConstraintColumns;
    }

    /**
     * <p>Setter for the field <code>SQLConstraintColumns</code>.</p>
     *
     * @param SQLConstraintColumns the SQLConstraintColumns to set
     */
    public void setSQLConstraintColumns(
            List<SQLConstraintColumn> SQLConstraintColumns) {
        this.SQLConstraintColumns = SQLConstraintColumns;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * HashCode override
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((constraintName == null) ? 0 : constraintName.hashCode());
        result = prime * result
                + ((constraintType == null) ? 0 : constraintType.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result
                + ((tableName == null) ? 0 : tableName.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Equals Override.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SQLConstraint other = (SQLConstraint) obj;
        if (constraintName == null) {
            if (other.constraintName != null)
                return false;
        } else if (!constraintName.equals(other.constraintName))
            return false;
        if (constraintType == null) {
            if (other.constraintType != null)
                return false;
        } else if (!constraintType.equals(other.constraintType))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (tableName == null) {
            if (other.tableName != null)
                return false;
        } else if (!tableName.equals(other.tableName))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Override for toString Method.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" Constraint Owner:[" + this.getOwner() + "], ");
        sb.append("Name:[" + this.getConstraintName() + "], ");
        sb.append("Type:[" + this.getConstraintType() + "], ");
        sb.append("Table:[" + this.getTableName() + "], ");
        sb.append("Search Condition:[" + this.getSearchCondition() + "]");
        if ((this.getSQLConstraintColumns() != null) &&
                (!this.getSQLConstraintColumns().isEmpty())) {
            sb.append("\n Columns:[\n");
            for (SQLConstraintColumn column : this
                    .getSQLConstraintColumns()) {
                sb.append("   " + column.toString() + "\n");
            }
            sb.append("\n]");
        }
        // **********************************
        // return String
        return sb.toString();
    }

}
