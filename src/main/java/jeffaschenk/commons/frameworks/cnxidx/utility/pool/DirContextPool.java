package jeffaschenk.commons.frameworks.cnxidx.utility.pool;

import jeffaschenk.commons.exceptions.NoFreeResourcesException;
import jeffaschenk.commons.exceptions.PoolNotAvailableException;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

/**
 * This class provides access to a pool of DirContext resources and is
 * implemented as a singleton / delegator idiom.
 * <p> The DirContextPool uses several different properties from the
 * properties that are passed in at initialization time. The following
 * properties are must be provided and be prefixed with
 * jeffaschenk.commons.framework.cnxidx.DirContextPool:<br>
 * <p/>
 * <li><b>min</b> - Minimum number of resources to create and keep open.
 * Defaults to <i>10</i>
 * <li><b>max</b> - Maximum number of resources that can be created.
 * Defaults to <i>20</i>
 * <li><b>timeout</b> - Default wait period for obtaining a resource
 * from the free pool. Defaults to <i>20</i>
 * <li><b>resourceProviderClassName</b> - The java class name of the pool's
 * resource provider
 * <li><b>trackUsage</b> - A boolean indicating if we should track
 * detailed usage information of resource allocation.
 * Defaults to <i>False</i>
 * <li><b>trackUsageInterval</b> - Number of seconds between reporting intervals
 * Defaults to <i>15 seconds</i>
 * <li><b>trackUsageResourceAge</b> - # seconds resource must be held
 * (not released) before being reported on.
 * Defaults to <i>15 seconds</i>
 */
public class DirContextPool {

    /**
     * DirContextPool provides access to a ResourcePool
     * containing DirContext objects.
     */
    private static ResourcePool pool;
    private static Properties poolProperties;

    /**
     * Used to validate directory contexts on an asyncronous period cycle.
     * Somewhat robust, not perfect but also not a performance detriment
     */
    protected static Timer validationTimer;
    protected static TimerTask validationTimerTask;

    /**
     * Strictly a developer mechanism.
     * Used primarily to determine if resources have not been released.
     */
    private static boolean trackUsage;
    private static Timer usageTimer;
    private static TimerTask usageTimerTask;
    private static Hashtable<DirContext,DirContextUsage> poolUsage;

    /**
     * Properties from System properties and read in from the ICos
     * properties file (combined.)
     */
    static private Properties resourcePoolProps = null;

    /**
     * My classname for use with CNXLogger.
     */
    static private final String CLASS_NAME = DirContextPool.class.getName();

    /**
     * The key value prefix for this object's properties in the ICos
     * properties file. For example, based on the value of this variable,
     * entries in the properties file would look like:
     * <br><i>cjeffaschenk.commons.framework.cnxidx.DirContextPool.min=10</i>
     * <p> Note that the terminating '.' is important.
     */
    static private final String KEY_PREFIX = CLASS_NAME + ".";
    // terminating '.' important

    /**
     * Owner of the pool. Not really required by ICos, but, by the
     * ResourcePool. Any value is fine.
     */
    static private final String OWNER = "Framework Directory Context Pool";

    // Assume properties missing for ones without reasonable defaults
    // The values in <> designate values that aren't easily defaulted.

    /**
     * Preloaded with a default value and used to create a
     * DirContext object. The preloaded default value, maybe
     * invalid, may be overridden by values placed in the
     * ICos properties file.
     */
    static private final int DEFAULT_MIN = 10;

    /**
     * Preloaded with a default value and used to create a
     * DirContext object. The preloaded default value, maybe
     * invalid, may be overridden by values placed in the
     * ICos properties file.
     */
    static private final int DEFAULT_MAX = 20;

    /**
     * Preloaded with a default value and used to create a
     * DirContext object. The preloaded default value, maybe
     * invalid, may be overridden by values placed in the
     * ICos properties file.
     */
    static private final int DEFAULT_TIMEOUT = 20;


    /**
     * Private constructor per the singleton idiom.
     */
    private DirContextPool() { 
        /* do nothing */
    }

    /**
     * Utilizes the singleton context pool to acquire a DirContext.
     *
     * @return A reference to an DirContext object.
     * @throws jeffaschenk.commons.exceptions.NoFreeResourcesException Thrown when no free DirContexts
     *                                  are available.
     */
    public static DirContext acquireContext()
            throws NoFreeResourcesException {
        DirContext idc = (DirContext) getContextPool().acquireResource();
        if (trackUsage) {
            poolUsage.put(idc, new DirContextUsage(idc));
        }
        return idc;
    }

    /**
     * Utilizes the singleton context pool to release a DirContext.
     *
     * @param idc The DirContext to release.
     */
    public static void releaseContext(DirContext idc) {
        final String METHOD = "releaseContext";

        try {
            if (idc == null) {
                return;
            }
            Hashtable env = idc.getEnvironment();
            if (env.containsKey(Context.OBJECT_FACTORIES)) {
                idc.removeFromEnvironment(Context.OBJECT_FACTORIES);
            }
            if (env.containsKey(Context.STATE_FACTORIES)) {
                idc.removeFromEnvironment(Context.STATE_FACTORIES);
            }
        } catch (NamingException ignore) {
            FrameworkLogger.log(CLASS_NAME, "releaseContext", FrameworkLoggerLevel.INFO,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_FACTORIES_REMOVAL_FAILED);
        }

        try {
            getContextPool().releaseResource(idc);

            if (trackUsage) poolUsage.remove(idc);
        } catch (PoolNotAvailableException pnae) {
        }
    }

    /**
     * Locks the pool. If false is returned, follow-up with
     * isPoolInUse() calls until isPoolInUse() returns false.
     *
     * @param why            Reason for locking the pool. Unused if pool is
     *                       already locked. Reason shows up in exception messages.
     * @param timeoutSeconds wait in seconds to lock the pool and
     *                       ensure that no DirContext connections in the pool are in use.
     * @return Whether there are any pool connections in use.
     */
    public static boolean lockPool(String why, int timeoutSeconds)
            throws PoolNotAvailableException {
        return getContextPool().lockPool(why, timeoutSeconds);
    }

    /**
     * Unlocks the pool - making the pool available for use.
     */
    public static void unlockPool()
            throws PoolNotAvailableException {
        getContextPool().unlockPool();
    }

    /**
     * Returns whether the pool has been locked.
     *
     * @return boolean indicating whether the pool was successfully locked.
     */
    public static boolean isPoolLocked()
            throws PoolNotAvailableException {
        return getContextPool().isPoolLocked();
    }

    /**
     * Convenience routine for checking if the pool is unlocked
     *
     * @return boolean indicating whether the pool is locked or not
     */
    public static boolean isPoolLocked(int timeoutSeconds)
            throws PoolNotAvailableException {
        return getContextPool().isPoolLocked(timeoutSeconds);
    }

    /**
     * Convenience routine for checking if the pool is unlocked and
     * has no connections in use.
     *
     * @return boolean indicating whether the pool was successfully locked.
     */
    public static boolean isPoolLockedAndUnused(int timeoutSeconds)
            throws PoolNotAvailableException {
        return getContextPool().isPoolLockedAndUnused(timeoutSeconds);
    }


    /**
     * Creates a resource pool of DirContexts if the pool does not
     * already exist; and always returns a reference to the pool.
     *
     * @return A reference to a resource pool of DirContext objects.
     */
    private static synchronized ResourcePool getContextPool()
            throws PoolNotAvailableException {

        if (pool == null) {
            throw new PoolNotAvailableException(
                    "Directory Context Pool has not been initialized yet.");
        }

        return pool;
    }

    /**
     * Logically resizes and redirects the dir context pool to the
     * specified parameters.
     *
     * @param typedProps A set of properties to reset the pool to.
     */
    public static synchronized void resetPool(Properties typedProps)
            throws PoolNotAvailableException {

        ResourcePool oldPool = pool;
        Properties oldPoolProps = poolProperties;
        try {
            // default to old properties if new properties is missing stuff
            poolProperties = new Properties(typedProps);//, oldPoolProps);
            pool = createPool(poolProperties);
            oldPool.destroy();
        } catch (PoolNotAvailableException pnae) {
            pool = oldPool;
            poolProperties = oldPoolProps;
            throw pnae;
        }
    }

    /**
     * Refresh the pool with a complete reset and recreate (this seems to be the only
     * way that it really gets reset.  Attempting to just do individual resources
     * doesn't seem to work.
     */
    public static synchronized void refreshPool() {

        if ((pool != null) && (!pool.isPoolLocked())) {

            // drop all the existing dircontexts and recreate as necessary
            try {
                resetPool(poolProperties);
            } catch (PoolNotAvailableException pnae) {
                // ignore this
            }
        }
    }

    public static synchronized void destroyPool() {
        try {
            //while (! isPoolLockedAndUnused(-1)) {
            //isPoolLockedAndUnused(5);
            //}
            getContextPool().destroy();
        } catch (Exception ignore) {
        }
    }


    /**
     * Creates a resource pool of DirContexts if the pool does not
     * already exist; and always returns a reference to the pool.
     *
     * @param typedProps -- Pool Properties.
     */
    public static synchronized void initializePool(Properties typedProps)
            throws PoolNotAvailableException {

        if (pool == null) {
            poolProperties = typedProps;
            pool = createPool(typedProps);
        }
    }

    /**
     * Method to validate all the free resources in the pool.  Validation
     * determines if the resources are still active.
     */
    public static void validatePool()
            throws PoolNotAvailableException {

        FrameworkLogger.log(CLASS_NAME, "validatePool", FrameworkLoggerLevel.INFO,
                MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_VALIDATE);
        getContextPool().validate();
    }

    /**
     * Creates a resource pool of DirContexts if the pool does not
     * already exist; and always returns a reference to the pool.
     *
     * @return A reference to a resource pool of DirContext objects.
     */
    @SuppressWarnings("unchecked")
    private static ResourcePool createPool(Properties typedProps)
            throws PoolNotAvailableException {

        ResourcePool resPool = null;
        int min = Integer.parseInt((String)typedProps.get(KEY_PREFIX + "min"));//, DEFAULT_MIN);
        int max = Integer.parseInt((String)typedProps.get(KEY_PREFIX + "max")); // , DEFAULT_MAX);
        int timeout = Integer.parseInt((String)typedProps.get(KEY_PREFIX + "timeout")); // , DEFAULT_TIMEOUT);
        int validationInterval = Integer.parseInt((String)typedProps.get(KEY_PREFIX + "validateIntervalSeconds")); // , 30);
        boolean fullValidation = false;
        if (validationInterval == 0) {
            fullValidation = true;
        }

        // Obtain the name of the resource pools provider class
        String resourceProviderClassName = (String) typedProps.get(
                KEY_PREFIX + "resourceProviderClassName");

        if (resourceProviderClassName == null ||
                resourceProviderClassName.length() == 0) {

            String[] arguments = new String[]{KEY_PREFIX};
            FrameworkLogger.log(CLASS_NAME, "createPool", FrameworkLoggerLevel.SEVERE,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_INVALID_PROPERTY,
                    arguments);
            throw new PoolNotAvailableException(MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_INVALID_PROPERTY,
                    arguments);
        }

        try {
            Class resProviderClass = Class.forName(resourceProviderClassName);

            Constructor resProvCtor = resProviderClass.getConstructor(
                    new Class[]{Properties.class});

            ResourceProvider resProv = (ResourceProvider)
                    resProvCtor.newInstance(new Object[]{typedProps});

            resPool = new ResourcePool(OWNER, min, max, timeout, resProv, fullValidation);

        } catch (Exception e) {
            String[] arguments = new String[]{resourceProviderClassName};
            FrameworkLogger.log(CLASS_NAME, "createPool", FrameworkLoggerLevel.SEVERE,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_INVALID_RESOURCEPROVIDER,
                    arguments, e);
            throw new PoolNotAvailableException(MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_INVALID_RESOURCEPROVIDER,
                    arguments, e);
        }

        // set up the DirContextPool validation
        setupValidation(validationInterval * 1000);

        // Set up DirContextPool usage reporting
        trackUsage =  false;
        long trackUsageInterval = 1000 * 15;//typedProps.getInt(
                //KEY_PREFIX + "trackUsageInterval", 15);
        long trackUsageResourceAge = 1000 * 15;//typedProps.getInt(
                //KEY_PREFIX + "trackUsageResourceAge", 15);
        setupUsageTracking(trackUsage, trackUsageInterval,
                trackUsageResourceAge);

        return resPool;
    }

    private static void setupValidation(long validationInterval) {
        if (validationInterval > 0) {
            if (validationTimer == null) {
                validationTimer = new Timer(true);
                validationTimerTask = new ValidationTimerTask();
                validationTimer.schedule(validationTimerTask, validationInterval,
                        validationInterval);
            }
        } else {
            if ((validationTimer != null) && (validationTimerTask != null)) {
                validationTimerTask.cancel();
            }
        }
    }

    private static void setupUsageTracking(boolean trackUsage,
                                           long usageInterval,
                                           long resUsedTimeMillis) {
        if (trackUsage) {
            if (poolUsage == null) {
                poolUsage = new Hashtable<>();
            }
            if (usageTimer == null) {
                usageTimer = new Timer(true);
            }
            usageTimerTask = new UsageTimerTask(resUsedTimeMillis);
            if (usageInterval > 0) {
                usageTimer.schedule(usageTimerTask,
                        usageInterval, usageInterval);
            }
        } else {
            if (usageTimer != null && usageTimerTask != null) {
                usageTimerTask.cancel();
            }
        }
    }

    //public static PropertiesChangeListener getPropertiesChangeListener() {
    //    return new DirContextPoolListener();
    //}

    public static class DirContextUsage {
        private Object resource;
        private String callStack;
        private Date acquisitionTime;

        public DirContextUsage(Object resource) {
            this.resource = resource;
            this.acquisitionTime = new Date();
            try {
                Exception callStackException = new Exception();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                callStackException.printStackTrace(pw);
                callStack = sw.toString();
                pw.close();
                sw.close();
                // strip two lines (separated by \n) of text off of the stack
                for (int ii = 0; ii < 2; ii++) {
                    int idx = callStack.indexOf('\n');
                    if (idx > -1) {
                        callStack = callStack.substring(idx + 1);
                    }
                }
            } catch (Exception ignore) {
            }
        }

        public boolean isOlderThan(long milliseconds) {
            return getSecondsUsed() > milliseconds;
        }

        public String getCallStack() {
            return callStack;
        }

        public Date getAcquisitionTime() {
            return acquisitionTime;
        }

        public long getSecondsUsed() {
            long current = System.currentTimeMillis();
            long created = getAcquisitionTime().getTime();
            long difference = current - created;
            return difference;
        }
    }

    public static class ValidationTimerTask extends TimerTask {

        public ValidationTimerTask() {
        }

        public void run() {
            try {
                DirContextPool.validatePool();
            } catch (Exception e) {
                // don't want this to end
            }
        }
    }

    public static class UsageTimerTask extends TimerTask {
        private long resUsedTimeMillis;

        public UsageTimerTask(long resUsedTimeMillis) {
            this.resUsedTimeMillis = resUsedTimeMillis;
        }

        public void run() {
            String txt = "\n\t#######################################";
            txt += "\n\tResources currently in use: " + poolUsage.size();
            if (poolUsage.size() > 0) {
                Enumeration iter = poolUsage.elements();
                while (iter.hasMoreElements()) {
                    DirContextUsage dcu =
                            (DirContextUsage) iter.nextElement();
                    if (dcu.isOlderThan(resUsedTimeMillis)) {
                        txt += "\n\t======= Resource held too long ========";
                        txt += "\n\tTime Acquired:  " + dcu.getAcquisitionTime();
                        txt += "\n\tSeconds In Use: " + dcu.getSecondsUsed();
                        txt += "\n\tAcquisition Stack:\n" + dcu.getCallStack();
                        txt += "\n\t=======================================";
                    }
                }
            }
            txt += "\n\t#######################################";
            String[] arguments = new String[]{txt};
            FrameworkLogger.log(CLASS_NAME, "resourceUsageTracker", FrameworkLoggerLevel.DEBUG,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_USAGETRACKER,
                    arguments);
        }
    }
}


class DirContextPoolListener { //implements PropertiesChangeListener {
    /**
     * My classname for use with CNXLogger.
     */
    static private final String CLASS_NAME =
            DirContextPoolListener.class.getName();

    /**
     * This will be called when a change is made.  To recieve the message
     * you must register with the AdministrationSB.
     *
     * @param propertySetName   The name of the propertie set in question.
     * @param changedProperties The set of properties that have changed.
     */
    public void propertiesChanged(String propertySetName,
                                  Properties changedProperties) {
        if (changedProperties != null) {
            try {
                DirContextPool.resetPool(
                        new Properties(changedProperties));
            } catch (PoolNotAvailableException pnae) {
                FrameworkLogger.log(CLASS_NAME, "propertiesChanged", FrameworkLoggerLevel.SEVERE,
                        MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_POOL_UNABLE_TO_RESET,
                        pnae);
            }
        }
    }
}
