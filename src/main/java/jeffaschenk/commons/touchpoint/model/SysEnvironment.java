package jeffaschenk.commons.touchpoint.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * SysEnvironment
 * Simple key Value Pair of system Environment Variables.
 *
 * @author jeffaschenk@gmail.com
 *
 */
@Entity
@Table(name = "SYSENVIRONMENT", uniqueConstraints = @UniqueConstraint(columnNames = "PROPERTYKEY"))
public class SysEnvironment extends RootElement implements java.io.Serializable {

    private static final long serialVersionUID = 1109L;

    private String propertyKey;
    private String propertyValue;
    private String description;

    /**
     * <p>Constructor for SysEnvironment.</p>
     */
    public SysEnvironment() {
    }

    /**
     * Constructor for SysEnvironment Object with all necessary fields.
     *
     * @param propertyKey
     * @param propertyValue
     */
    public SysEnvironment(String propertyKey, String propertyValue) {
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Overriding Id to Inject proper Sequence Identity.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sysEnvironmentSequenceGenerator")
    @GenericGenerator(name = "sysEnvironmentSequenceGenerator", strategy = "jeffaschenk.commons.touchpoint.model.dao.support.hibernate.PlatformSequenceGenerator",
            parameters = {@Parameter(name = "sequence", value = "seq_SysEnvironment")})
    public Integer getId() {
        return super.getId();
    }

    /**
     * <p>Getter for the field <code>propertyKey</code>.</p>
     *
     * @return a {@link String} object.
     */
    @Column(name = "PROPERTYKEY", unique = true, nullable = false, length = 100)
    public String getPropertyKey() {
        return this.propertyKey;
    }

    /**
     * <p>Setter for the field <code>propertyKey</code>.</p>
     *
     * @param propertyKey a {@link String} object.
     */
    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
     * <p>Getter for the field <code>propertyValue</code>.</p>
     *
     * @return a {@link String} object.
     */
    @Column(name = "PROPERTYVALUE", length = 1000)
    public String getPropertyValue() {
        return this.propertyValue;
    }

    /**
     * <p>Setter for the field <code>propertyValue</code>.</p>
     *
     * @param propertyValue a {@link String} object.
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Column(name = "DESCRIPTION", length = 100)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
