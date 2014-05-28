package jeffaschenk.commons.environment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * SpringAccessor Utility Class. Provides Spring+AOP+Hibernate configuration and
 * setup as underlying Persistence Manager via Injection.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class SpringAccessor implements ApplicationContextAware {

    // ***************************************
    // Logging
    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(SpringAccessor.class);

    // ***************************************
    // Globals
    private static final String sessionFactoryBeanName = "sessionFactory";

    // ***************************************
    // Spring Injected Application Context
    private static ApplicationContext applicationContext = null;

    // ***************************************
    // Inject our Java Security Implementation

    static {
        java.security.Security
                .addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Injects Spring Application Context.
     */
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringAccessor.applicationContext = applicationContext;
    }

    /**
     * Get Application Context
     *
     * @return ApplicationContext for Spring+Hibernate context.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Obtain a Native Session based upon the Application Context's Default
     * Session Factory.
     *
     * @return Session - Native Hibernate Session.
     */
    protected static Session getSession() {
        return ((SessionFactory) applicationContext
                .getBean(sessionFactoryBeanName)).openSession();
    }

    /**
     * Obtain a Specified BeanName.
     *
     * @param beanName a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    protected static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    /**
     * Extend Session Reach Setup Call this prior to accessing DAOs to extend
     * the Session Reach and eliminate Lazy Implications.
     */
    public static void startExtendedSessionReach() {
        if (log.isDebugEnabled()) {
            log.debug("Starting Extended Session Reach");
        }

        try {
            SessionFactory sessionFactory = (SessionFactory) getBean(sessionFactoryBeanName);
            Session session = SessionFactoryUtils.getSession(sessionFactory,
                    true);
            if (TransactionSynchronizationManager.getResource(sessionFactory) == null) {
                TransactionSynchronizationManager.bindResource(sessionFactory,
                        new SessionHolder(session));
            }
        } catch (Exception e) {
            log.error(
                    "Exception encountered while starting Extended Session Reach:"
                            + e.getMessage(), e);
            throw new java.lang.RuntimeException(
                    "Extended Session Reach Start Up Exception Encountered", e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Successfully Started Extended Session Reach");
        }
    }

    /**
     * Obtain the Current Session.
     *
     * @return Session which is Current Active Session or newly created Session.
     */
    public static Session getCurrentSession() {
        try {
            SessionFactory sessionFactory = (SessionFactory) getBean(sessionFactoryBeanName);
            SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(sessionFactory);
            if (holder == null) {
                return getSession();
            }
            // **********************************
            // Obtain Session within Holder.
            Session session = holder.getSession();
            if (session != null) {
                return session;
            } else {
                return getSession();
            }
        } catch (Exception e) {
            log.error("Exception encountered while Obtaining Current Session:"
                    + e.getMessage(), e);
            throw new java.lang.RuntimeException(
                    "Exception encountered while Obtaining Current Session Encountered",
                    e);
        }
    }

    /**
     * Extend Session Reach Tear Down Call this prior to any JUnit TearDown, or
     * when done with Session Reach Extension.
     *
     * @throws Exception if any.
     */
    public static void finishExtendedSessionReach() {
        if (log.isDebugEnabled()) {
            log.debug("Finishing Extended Session Reach");
        }
        try {
            SessionFactory sessionFactory = (SessionFactory) getBean(sessionFactoryBeanName);
            SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(sessionFactory);
            if (holder != null) {
                Session s = holder.getSession();
                s.flush();
                TransactionSynchronizationManager
                        .unbindResource(sessionFactory);
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
            throw new java.lang.RuntimeException(
                    "Extended Session Reach Tear Down Exception Encountered", e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Successfully Finished Extended Session Reach");
        }
    }

    /**
     * Extend Session Reach Tear Down Call this prior to any JUnit TearDown, or
     * when done with Session Reach Extension.  We do not flush after this call.
     *
     * @throws Exception if any.
     */
    public static void finishExtendedSessionReachAfterException() {
        if (log.isDebugEnabled()) {
            log.debug("Finishing Extended Session Reach");
        }
        try {
            SessionFactory sessionFactory = (SessionFactory) getBean(sessionFactoryBeanName);
            SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(sessionFactory);
            if (holder != null) {
                Session s = holder.getSession();
                TransactionSynchronizationManager
                        .unbindResource(sessionFactory);
                SessionFactoryUtils.closeSession(s);
            }
        } catch (Exception e) {
            log.error(
                    "Exception encountered while finishing Extended Session Reach:"
                            + e.getMessage(), e);
            throw new java.lang.RuntimeException(
                    "Extended Session Reach Tear Down Exception Encountered", e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Successfully Finished Extended Session Reach");
        }
    }

    /**
     * Get the System Environment Bean injected Implementation.
     *
     * @return SystemEnvironmentBean
     */
    public static SystemEnvironmentBean getSystemEnvironmentBean() {
        return (SystemEnvironmentBean) applicationContext.getBean("systemEnvironmentPropertyAccessor");
    }


} // /:> End of SpringAccessor class.
