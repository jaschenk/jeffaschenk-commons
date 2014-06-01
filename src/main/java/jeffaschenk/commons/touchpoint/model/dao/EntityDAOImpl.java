package jeffaschenk.commons.touchpoint.model.dao;

import jeffaschenk.commons.touchpoint.model.*;
import jeffaschenk.commons.types.StatusOutputType;
import jeffaschenk.commons.util.NumberUtility;
import jeffaschenk.commons.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.Table;
import java.util.*;

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
    public void setTouchPointSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    /**
     * Helper List of Associated Entity Classes.
     */
    private static List<Class<? extends RootElement>> associatedClasses =
            new ArrayList<>(15);

    static {
        associatedClasses.add(Action.class);
        associatedClasses.add(Ancestry.class);
        associatedClasses.add(AncestryElement.class);
        associatedClasses.add(Element.class);
        associatedClasses.add(Group.class);
        associatedClasses.add(Owner.class);
        associatedClasses.add(Property.class);
        associatedClasses.add(PropertyValue.class);
        associatedClasses.add(SysEnvironment.class);
    }

    ;

    /**
     * Get a Low-Level Session for performing Query Specific Functions.
     *
     * @return Session
     */
    public Session getDAOSession() {
        try {
            Session session = this.getSessionFactory().openSession();

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
     * {@inheritDoc}
     * <p/>
     * Get a Count of Rows for an Entity.  Allows us to determine
     * if this is an initial load or not.
     */
    @Override
    public Long getRowCount(Class<? extends RootElement> clazz) {
        // ***************************************
        // Initialize
        Long resultCount = new Long(0);
        Session session = null;
        try {
            // ***********************************************
            // Create the Criteria from our obtained session,
            // simply only limiting by Class.
            session = this.getDAOSession();
            Criteria criteria = session.createCriteria(clazz);
            ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.rowCount());
            criteria.setProjection(projectionList);

            // ********************************************
            // Now perform the query and return the count.
            List<?> results = criteria.list();
            // ********************************************
            // Check for returned Projection.
            if (results == null) {
                return resultCount;
            }
            // ********************************************
            // Obtain an Iterator to traverse result.
            Iterator<?> resultIterator = results.iterator();
            if (!resultIterator.hasNext()) {
                return resultCount;
            }
            // ********************************************
            // Get the Object Count.
            while (resultIterator.hasNext()) {
                resultCount = (Long) resultIterator.next();
            }
            return resultCount;
        } finally {
            if (session != null) {
                this.finalizeDAOSession();
            }
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<? extends RootElement> getAllElementsForClass(Class<? extends RootElement> clazz) {
        Query q = this.getCurrentDAOSession().getNamedQuery(clazz.getSimpleName() + ".findAllAsc");
        List<? extends RootElement> elements = (List<? extends RootElement>) q.list();
        return elements;
    }

    /**
     * Obtain the Status for the Database Component.
     *
     * @param statusOutputType
     * @return String
     */
    @Override
    public String status(StatusOutputType statusOutputType) {
        StringBuilder sb = new StringBuilder();

        // Provide Row counts of all applicable Tables
        if (statusOutputType.equals(StatusOutputType.TEXT)) {
            sb.append('\t' + "Row Counts: " + '\n');
        }
        List<String> counts = getAllElementCounts(statusOutputType);

        ListIterator<String> countsIterator = counts.listIterator();
        while (countsIterator.hasNext()) {
            if (statusOutputType.equals(StatusOutputType.TEXT)) {
                sb.append('\t' + countsIterator.next() + '\n');
            } else {
                sb.append(countsIterator.next());
            }
        }
        return sb.toString();
    }

    /**
     * Helper Method to Obtain Counts of All Elements
     *
     * @param statusOutputType
     * @return List<String>
     */
    private List<String> getAllElementCounts(StatusOutputType statusOutputType) {
        List<String> elementCounts = new ArrayList<String>();
        if (statusOutputType.equals(StatusOutputType.HTML)) {
            elementCounts.add("<table><tr><td align=\042right\042><b>Entity Name</b></td><td align=\042right\042><b>Table Name</b></td><td align=\042right\042><b>Current Row Count</b></td></tr>");
        }
        Iterator<Class<? extends RootElement>> iterator = associatedClasses.iterator();
        while (iterator.hasNext()) {
            Class<? extends RootElement> elementClass = iterator.next();
            String tableName = getTableName(elementClass);
            // Check Output
            if (statusOutputType.equals(StatusOutputType.HTML)) {
                elementCounts.add(
                        "<tr><td align=\042right\042>" +
                                elementClass.getSimpleName() + "</td><td align=\042right\042>" +
                                tableName +
                                "</td><td align=\042right\042><b>" +
                                NumberUtility.formatRowCount(this.getRowCount(elementClass)) +
                                "</td></tr>");
            } else {
                elementCounts.add(
                        elementClass.getSimpleName() + "-> " +
                                tableName + ": " +
                                NumberUtility.formatRowCount(this.getRowCount(elementClass)));
            }
        }
        if (statusOutputType.equals(StatusOutputType.HTML)) {
            elementCounts.add("</table>");
        }
        return elementCounts;
    }

    /**
     * Helper Method to Obtain Counts of All Elements currently in database.
     *
     * @return Map<Class<? extends RootElement>, Long>
     */
    @Override
    public Map<Class<? extends RootElement>, Long> getAllElementClassCounts() {
        Map<Class<? extends RootElement>, Long> elementCounts = new
                HashMap<>();
        Iterator<Class<? extends RootElement>> iterator = associatedClasses.iterator();
        while (iterator.hasNext()) {
            Class<? extends RootElement> elementClass = iterator.next();

            elementCounts.put(
                    elementClass,
                    this.getRowCount(elementClass));
        }
        return elementCounts;
    }

    /**
     * Obtain all Entities for the Specified Element Class.
     *
     * @param clazz
     * @return Number of Elements Removed from Table.
     */
    @Override
    public Number removeAllElementsForClass(Class<? extends RootElement> clazz) {
        if (clazz == null) {
            return 0L;
        } else if ((clazz.getName().toLowerCase().contains("demographics")) ||
                (clazz.getName().toLowerCase().contains("web_payment_transaction")) ||
                (clazz.getName().toLowerCase().contains("system"))) {
            logger.warn("Ignoring Clear of Class:[" + clazz.getName() + "], as Table needs to Maintain Integrity of Life-Cycles!");
            return 0L;
        }
        // Perform deletion of all Rows and All Data for this Class/Table.
        String tableName = this.getTableName(clazz);
        if (StringUtils.isNotEmpty(tableName)) {
            SQLQuery q = this.getCurrentDAOSession().createSQLQuery("delete from " + tableName + " where 1=1");
            Number resultRowsRemoved = q.executeUpdate();
            logger.info("Clear of Class:[" + clazz.getName() + "], Table:[" + tableName
                    + "], Successfully Cleared:[" + resultRowsRemoved + "] Rows/Objects from Table.");
            return resultRowsRemoved;
        } else {
            logger.warn("Ignoring Clear of Class:[" + clazz.getName() + "], as Table Name is unknown!");
            return 0L;
        }
    }


    /**
     * Obtain the String name of the JDBC table for the Object.
     *
     * @return String Name of the Table annotated on the Entity Object.
     */
    private String getTableName(Class<? extends RootElement> clazz) {
        try {
            RootElement element = clazz.newInstance();
            Table tableAnnotation = element.getClass().getAnnotation(Table.class);
            return tableAnnotation == null ? null : tableAnnotation.name();
        } catch (Exception e) {
            return null;
        }
    }


}
