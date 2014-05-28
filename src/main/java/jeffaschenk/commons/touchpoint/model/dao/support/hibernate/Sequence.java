package jeffaschenk.commons.touchpoint.model.dao.support.hibernate;

import jeffaschenk.commons.touchpoint.model.RootElement;

/**
 * Simple Sequence Object
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class Sequence {

    private Class<? extends RootElement> entityClass;
    private String name;
    private String sql;
    private String parameters;

    /**
     * <p>Constructor for Sequence.</p>
     *
     * @param entityClass a {@link Class} object.
     * @param name        a {@link String} object.
     * @param sql         a {@link String} object.
     */
    public Sequence(Class<? extends RootElement> entityClass, String name, String sql) {
        this.entityClass = entityClass;
        this.name = name;
        this.sql = sql;
    }

    /**
     * <p>Getter for the field <code>entityClass</code>.</p>
     *
     * @return a {@link Class} object.
     */
    public Class<? extends RootElement> getEntityClass() {
        return entityClass;
    }

    /**
     * <p>Setter for the field <code>entityClass</code>.</p>
     *
     * @param entityClass a {@link Class} object.
     */
    public void setEntityClass(Class<? extends RootElement> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>sql</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getSql() {
        return sql;
    }

    /**
     * <p>Setter for the field <code>sql</code>.</p>
     *
     * @param sql a {@link String} object.
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * <p>Getter for the field <code>parameters</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * <p>Setter for the field <code>parameters</code>.</p>
     *
     * @param parameters a {@link String} object.
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

}

