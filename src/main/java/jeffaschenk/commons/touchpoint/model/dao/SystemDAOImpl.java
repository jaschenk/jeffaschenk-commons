package jeffaschenk.commons.touchpoint.model.dao;

import jeffaschenk.commons.touchpoint.model.SysEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository("systemDAO")
public class SystemDAOImpl extends EntityDAOImpl implements SystemDAO {

    // ***************************************
    // Logging
    private static final Log log = LogFactory
            .getLog(SystemDAOImpl.class);

    // *******************************************
    // System Related Functions
    // *******************************************

    /**
     * Create a System Environment Property Object for use within
     * the System.  At this time there is no encryption, but perhaps maybe applied.
     * Or a new SecureSysEnvironment Object may be the resolution.
     *
     * @param key
     * @param value
     */
    @Override
    public void createSysEnvironmentProperty(String key, String value) {
        // **********************************************
        // Validate Parameters
        if ((key == null) || (key.isEmpty())) {
            throw new IllegalArgumentException(
                    "System Environment Property Key is Invalid, unable to persist System Environment Object!");
        }
        if ((value == null) || (value.isEmpty())) {
            throw new IllegalArgumentException(
                    "System Environment Property Value is Invalid, unable to persist System Environment Object!");
        }
        // ************************************************
        // Create a System Environment Object
        SysEnvironment sysEnvironment = new SysEnvironment(key, value);
        // ************************************************
        // Persist the new Environment Object
        Integer id = this
                .createEntity(sysEnvironment);
        log.info("System Environment Variable Created with Row ID:[" + id + "], Key:["
                + sysEnvironment.getPropertyKey() + "], Value:[" + sysEnvironment.getPropertyValue() + "].");
    }

    /**
     * Update the Value for the specified Key.
     *
     * @param key
     * @param value
     */
    public void updateSysEnvironmentProperty(String key, String value) {
        // **********************************************
        // Validate Parameters
        if ((key == null) || (key.isEmpty())) {
            throw new IllegalArgumentException(
                    "System Environment Property Key is Invalid, unable to update System Environment Object!");
        }
        if ((value == null) || (value.isEmpty())) {
            throw new IllegalArgumentException(
                    "System Environment Property Value is Invalid, unable to update System Environment Object!");
        }
        // **********************************************
        // Get the System Environment Object.
        SysEnvironment sysEnvironment = this.getSysEnvironmentByKey(key);
        if (sysEnvironment == null) {
            // ****************************
            // perform a Create
            this.createSysEnvironmentProperty(key, value);
        } else {
            // ****************************
            // Perform the Update,
            sysEnvironment.setPropertyValue(value);
            this.updateEntity(sysEnvironment);
        }
    }

    /**
     * Physically Remove a SysEnvironment Object.
     *
     * @param key
     */
    public void removeSysEnvironmentProperty(String key) {
        SysEnvironment sysEnvironment = this.getSysEnvironmentByKey(key);
        if (sysEnvironment == null) {
            return;
        }
        // *******************************
        // Allow the removal
        this.removeEntity(sysEnvironment);
    }

    /**
     * Find a System Environment Property by key and value expressions.
     *
     * @param key   - can have a like '%' to indicate a like query instead of exact, can be null to exclude from search.
     * @param value - can have a like '%' to indicate a like query instead of exact, can be null to exclude from search.
     * @return Map<String,String> - TreeMap of System Environment Properties for ordered Map.
     */
    public Map<String, String> findSysEnvironmentProperty(String key, String value) {
        // **********************************
        // Initialize
        Map<String, String> resultMap = new TreeMap<String, String>();
        // *******************************************
        // Create the Detached Criteria,
        // simply only limiting by Class.
        DetachedCriteria criteria = DetachedCriteria
                .forClass(SysEnvironment.class);
        // ****************************
        // Check for A Key Specified
        if ((key != null) && (!key.isEmpty())) {
            if (key.contains("%")) {
                criteria.add(Restrictions.like("propertyKey", key)
                        .ignoreCase());
            } else {
                criteria.add(Restrictions.eq("propertyKey", key)
                        .ignoreCase());
            }
        }
        // ****************************
        // Check for A Value Specified
        if ((value != null) && (!value.isEmpty())) {
            if (value.contains("%")) {
                criteria.add(Restrictions.like("propertyValue", value)
                        .ignoreCase());
            } else {
                criteria.add(Restrictions.eq("propertyValue", value)
                        .ignoreCase());
            }
        }
        // ****************************
        // Set Result Ordering and
        // Obtain the ResultList.
        criteria.addOrder(Order.asc("propertyKey"));
        List<SysEnvironment> resultList = this
                .findByCriteria(criteria);
        // ****************************
        // Iterate over Result List
        // to create Result Map Result.
        for (SysEnvironment sysEnvironment : resultList) {
            resultMap.put(sysEnvironment.getPropertyKey(), sysEnvironment.getPropertyValue());
        }
        // **********************************
        // Return Results.
        return resultMap;
    }

    /**
     * Get System Environment Property
     *
     * @param key to lookup SysEnvironment value for key.
     * @return String of Value
     */
    public String getSysEnvironmentPropertyValue(String key) {
        // **********************************************
        // Validate Parameters
        if ((key == null) || (key.isEmpty())) {
            throw new IllegalArgumentException(
                    "System Environment Property Key is Invalid, unable to update System Environment Object!");
        }
        // **********************************************
        // Get the System Environment Object.
        SysEnvironment sysEnvironment = this.getSysEnvironmentByKey(key);
        return ((sysEnvironment == null) ? null : sysEnvironment.getPropertyValue());
    }

    /**
     * Private Helper Method to Obtain the System Environment Objects
     * by key.
     *
     * @param propertyKey
     * @return SysEnvironment - associated with specified key.
     */
    private SysEnvironment getSysEnvironmentByKey(String propertyKey) {
        // **********************************************
        // Validate
        if ((propertyKey == null) || (propertyKey.isEmpty())) {
            throw new IllegalArgumentException(
                    "System Environment Property Key is Invalid, unable to Read System Environment Object by Key!");
        }
        // **********************************************
        // Set Query Name.
        String queryName = "sysenvironment.findByKey";
        // *******************************************
        // Set our Parameters.
        String[] paramNames = new String[]{"propertyKey"};
        Object[] values = new Object[]{propertyKey};
        // *******************************************
        // Perform Search based upon Native HQL.
        List<SysEnvironment> resultList = this
                .findByNamedQueryAndNamedParam(queryName, paramNames, values);
        if ((resultList == null) || (resultList.isEmpty())) {
            log
                    .warn("Reading System Environment by Key:[" + propertyKey
                            + "], yielded no results!");
            return null;
        }
        if (resultList.size() > 1) {
            log.warn("Reading System Environment by Key:[" + propertyKey + "], yielded "
                    + (resultList.size() - 1) + " more rows than expected!");
        }
        // ************************************
        // Hydrate Necessary Results
        SysEnvironment sysEnvironment = resultList.get(0);
        sysEnvironment.getPropertyKey();
        sysEnvironment.getPropertyValue();
        return sysEnvironment;
    }


    // TODO POST 1106 ORM Refactoring to add additional functionality.

    @Override
    public void createSysEnvironment(SysEnvironment sysEnvironment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSysEnvironment(SysEnvironment sysEnvironment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeSysEnvironment(SysEnvironment sysEnvironment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SysEnvironment getSysEnvironment(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
