package jeffaschenk.commons.touchpoint.model.dao;

import jeffaschenk.commons.types.StatusOutputType;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import jeffaschenk.commons.touchpoint.model.RootElement;

import java.util.List;
import java.util.Map;


/**
 * EntityDAO
 * Entity Service Interface.
 *
 * @author jeffascehnk@gmail.com
 * @version $Id: $
 */
public abstract interface EntityDAO {

    /**
     * Get a Low-Level Session for performing Query Specific Functions.
     *
     * @return Session
     */
    Session getDAOSession();

    /**
     * Obtain the Current Session.
     *
     * @return Session which is Current Active Session or newly created Session.
     */
    Session getCurrentDAOSession();

    /**
     * Finalize the Session Obtained, Free Resources and Transactions.
     */
    void finalizeDAOSession();

    /**
     * Finalize the Session Obtained and Free Resources.
     */
    void finalizeDAOSessionAfterException();

    Integer createEntity(RootElement rootElement);

    void createOrUpdateEntity(RootElement rootElement);

    void updateEntity(RootElement rootElement);

    void removeEntity(RootElement rootElement);

    <T extends RootElement> T readDistinctEntity(Class<T> clazz, Integer id, Object... optionalParameters);

    List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values);

    List findByCriteria(DetachedCriteria detachedCriteria, int start, int pageSize);

    List findByCriteria(DetachedCriteria detachedCriteria);

    Long getRowCount(Class<? extends RootElement> clazz);

    /**
     * Obtain Entity Counts all Element Classes.
     *
     * @return Map<Class<? extends wga_root_element>, Long>
     */
    Map<Class<? extends RootElement>, Long> getAllElementClassCounts();


    /**
     * Obtain all Entities for the Specified Element Class.
     *
     * @param clazz
     * @return List<? extends RootElement>
     */
    List<? extends RootElement> getAllElementsForClass(Class<? extends RootElement> clazz);

    /**
     * Obtain the Status for the Database Component.
     *
     * @param statusOutputType
     * @return String
     */
    String status(StatusOutputType statusOutputType);

    /**
     * Obtain all Entities for the Specified Element Class.
     *
     * @param clazz
     * @return Number of Elements Removed from Table.
     */
    Number removeAllElementsForClass(Class<? extends RootElement> clazz);

}
