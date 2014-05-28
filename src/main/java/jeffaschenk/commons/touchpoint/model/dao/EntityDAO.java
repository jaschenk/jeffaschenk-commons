package jeffaschenk.commons.touchpoint.model.dao;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import jeffaschenk.commons.touchpoint.model.RootElement;

import java.util.List;


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


}
