package jeffaschenk.commons.touchpoint.model.dao;

import jeffaschenk.commons.touchpoint.model.SysEnvironment;

import java.util.Map;

public interface SystemDAO extends EntityDAO {

    /**
     * Create a System Environment Global Property.
     *
     * @param key   Global Property Key or Name
     * @param value Global property value.
     */
    public void createSysEnvironmentProperty(String key, String value);

    /**
     * Create a System Environment Global Property.
     *
     * @param sysEnvironment Global Property Object to Persist.
     */
    public void createSysEnvironment(SysEnvironment sysEnvironment);

    /**
     * Update a System Environment Global Property Value.
     *
     * @param key   Global Property Key or Name
     * @param value Global property value.
     */
    public void updateSysEnvironmentProperty(String key, String value);

    /**
     * Update a System Environment Global Property Value.
     *
     * @param sysEnvironment
     */
    public void updateSysEnvironment(SysEnvironment sysEnvironment);

    /**
     * Remove a System Environment Global Property.
     *
     * @param key Global Property Key or Name to be removed.
     */
    public void removeSysEnvironmentProperty(String key);

    /**
     * Remove a System Environment Global Property.
     *
     * @param sysEnvironment - Object to be Removed.
     */
    public void removeSysEnvironment(SysEnvironment sysEnvironment);


    /**
     * Find a System Environment Global Property Value.
     *
     * @param key   Global Property Key or Name
     * @param value Global property value.
     */
    public Map<String, String> findSysEnvironmentProperty(String key, String value);

    /**
     * Get a System Environment Global Property Value.
     *
     * @param key Global Property Key or Name
     */
    public String getSysEnvironmentPropertyValue(String key);

    /**
     * Get a System Environment Global Object.
     *
     * @param key Global Property Key or Name
     */
    public SysEnvironment getSysEnvironment(String key);

}

