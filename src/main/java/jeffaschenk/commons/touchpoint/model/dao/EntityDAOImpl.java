package jeffaschenk.commons.touchpoint.model.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import jeffaschenk.commons.touchpoint.model.RootElement;

import java.util.List;

/**
 * Generic Entity Service DAO Implementation.
 *
 * @author JeffASchenk@gmail.com
 * @version $Id: $
 */
@Repository("entityDAO")
public abstract class EntityDAOImpl extends HibernateDaoSupport implements EntityDAO {
    /**
     * Logging
     */
    private static Log log = LogFactory.getLog(EntityDAOImpl.class);

    /**
     * Spring Injected Session Factory
     *
     * @param sessionFactory
     */
    @Autowired
    public void setTouchPointdSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    /**
     * Get a Low-Level Session for performing Query Specific Functions.
     *
     * @return Session
     */
    public Session getDAOSession() {
        try {
            Session session = SessionFactoryUtils.getSession(this.getSessionFactory(),
                    true);
            if (TransactionSynchronizationManager.getResource(this.getSessionFactory()) == null) {
                TransactionSynchronizationManager.bindResource(this.getSessionFactory(),
                        new SessionHolder(session));
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully Started DAO Session with Extended Reach");
            }
            return session;
        } catch (Exception e) {
            log.error(
                    "Exception encountered while starting DAO Session with Extended Reach:"
                            + e.getMessage(), e);
            throw new RuntimeException(
                    "DAO Session Reach Start Up Exception Encountered", e);
        }
    }

    /**
     * Obtain the Low-Level Current Session.
     *
     * @return Session which is Current Active Session or newly created Session.
     */
    public Session getCurrentDAOSession() {
        try {
            SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(this.getSessionFactory());
            if (holder == null) {
                return getDAOSession();
            }
            // **********************************
            // Obtain Session within Holder.
            Session session = holder.getSession();
            if (session != null) {
                return session;
            } else {
                return getDAOSession();
            }
        } catch (Exception e) {
            log.error("Exception encountered while Obtaining Current Session:"
                    + e.getMessage(), e);
            throw new RuntimeException(
                    "Exception encountered while Obtaining Current Session Encountered",
                    e);
        }
    }

    /**
     * Finalize Obtained Session
     */
    public void finalizeDAOSession() {
        if (log.isDebugEnabled()) {
            log.debug("Finishing Extended Session Reach");
        }
        try {
            SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(this.getSessionFactory());
            if (holder != null) {
                Session s = holder.getSession();
                s.flush();
                TransactionSynchronizationManager
                        .unbindResource(this.getSessionFactory());
                SessionFactoryUtils.closeSession(s);
            }
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            //getSQLConstraintLookupService().determineConstraintViolation(dive);
            log.error("Data Integrity Violation Exception has occurred!", dive);
        } catch (org.hibernate.exception.ConstraintViolationException cve) {
            //getSQLConstraintLookupService().determineConstraintViolation(cve);
            log.error("Constraint Violation Exception has occurred!", cve);
        } catch (Exception e) {

            log.error(
                    "Exception encountered while finishing Extended Session Reach:"
                            + e.getMessage(), e);
            throw new RuntimeException(
                    "Extended Session Reach Tear Down Exception Encountered", e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Successfully Finished Extended Session Reach");
        }
    }

    /**
     * Finalize Obtained Session after failure, flush not performed.
     */
    public void finalizeDAOSessionAfterException() {
        if (log.isDebugEnabled()) {
            log.debug("Finishing Extended Session Reach");
        }
        try {
            SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(this.getSessionFactory());
            if (holder != null) {
                Session s = holder.getSession();
                TransactionSynchronizationManager
                        .unbindResource(this.getSessionFactory());
                SessionFactoryUtils.closeSession(s);
            }
        } catch (Exception e) {
            log.error(
                    "Exception encountered while finishing Extended Session Reach:"
                            + e.getMessage(), e);
            throw new RuntimeException(
                    "Extended Session Reach Tear Down Exception Encountered", e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Successfully Finished Extended Session Reach");
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Create Entity
     */
    @Override
    public Integer createEntity(RootElement rootElement) {
        if (rootElement == null) {
            throw new IllegalArgumentException(
                    "RootElement is null, unable to persist null Entity Object.");
        }
        // ************************************************
        // Persist the new Object and return Row ID.
        return (Integer) getHibernateTemplate().save(rootElement);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Create or Update Entity
     */
    public void createOrUpdateEntity(RootElement rootElement) {
        if (rootElement == null) {
            throw new IllegalArgumentException(
                    "RootElement is null, unable to persist null Entity Object.");
        }
        // ************************************************
        // Persist the new Object and return Row ID.
        getHibernateTemplate().saveOrUpdate(rootElement);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Update Entity
     */
    @Override
    public void updateEntity(RootElement rootElement) {
        if (rootElement == null) {
            throw new IllegalArgumentException(
                    "RootElement is null, unable to update null Entity Object.");
        }
        // *****************************************
        // Persist the Updated Object
        getHibernateTemplate().update(rootElement);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Delete/Remove Entity
     */
    @Override
    public void removeEntity(RootElement rootElement) {
        if (rootElement == null) {
            throw new IllegalArgumentException(
                    "RootElement is null, unable to remove null Entity Object.");
        }
        getHibernateTemplate().delete(rootElement);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Get the specified Object by it's Distinct Row Identifier or ID. First Hibernate should look in our
     * first level Cache, if not will dip down to Database to resolve. If the
     * object does not exist this will throw an Exception.
     */
    @Override
    public <T extends RootElement> T readDistinctEntity(Class<T> clazz, Integer id, Object... optionalParameters) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class is null, unable to obtain Distinct RootElement Object.");
        } else if (id == null) {
            throw new IllegalArgumentException("Id is null, unable to obtain Distinct RootElement Object.");
        }
        try {
            // *************************************************
            // Perform a get on a specific Asset Object Class.
            // *Note: using "get" as opposed to "load" because "get"
            // will return null if the row is not found whereas
            // "load" will return an empty proxy (and not throw an
            // exception).
            // @see org.hibernate.Session#get(Class, java.io.Serializable)
            T t = getHibernateTemplate().get(clazz, id);
            if (t == null) {
                return t;
            }
            // **************************
            // Check Optional Parameters
            //if (OptionalParameters.areOptionalParametersUsed(optionalParameters)) {
            //    ObjectGraphResolver.resolve(t, optionalParameters);
            //}
            // *********************************
            // Return Obtained Distinct Element.
            return t;
        } catch (org.springframework.orm.ObjectRetrievalFailureException orfe) {
            // This indicates the Object is not within the persistent store.
            return null;
        } catch (org.springframework.dao.DataAccessException dae) {
            log.error("Encountered Data Access Exception:[" + dae.getMessage()
                    + "], returning Null to upstream caller", dae);
            return null;
        } // End of Exception processing.
    }

    /**
     * Find a List of Results using a Named query and Named Parameters
     *
     * @param queryName
     * @param paramNames
     * @param values
     * @return List<RootElement Results List.
     */
    @Override
    public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values) {
        // *******************************************
        // Perform Search based upon Specified
        // Named Query and Specified Paramaters.
        return getHibernateTemplate()
                .findByNamedQueryAndNamedParam(queryName, paramNames, values);
    }

    /**
     * Find by Criteria
     *
     * @param detachedCriteria
     * @param start
     * @param pageSize
     * @return List of Results
     */
    @Override
    public List findByCriteria(DetachedCriteria detachedCriteria, int start, int pageSize) {
        // *******************************************
        // Perform Search based upon Specified
        // Named Query and Specified Paramaters.
        return getHibernateTemplate()
                .findByCriteria(detachedCriteria, start, pageSize);
    }

    /**
     * Find by Criteria
     *
     * @param detachedCriteria
     * @return List of Results
     */
    @Override
    public List findByCriteria(DetachedCriteria detachedCriteria) {
        // *******************************************
        // Perform Search based upon Specified
        // Named Query and Specified Paramaters.
        return getHibernateTemplate()
                .findByCriteria(detachedCriteria);
    }

}
