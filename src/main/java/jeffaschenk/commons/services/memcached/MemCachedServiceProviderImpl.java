package jeffaschenk.commons.services.memcached;


import jeffaschenk.commons.container.shutdown.ServiceInstanceShutdownLogger;
import jeffaschenk.commons.identifiers.RandomGUID;
import jeffaschenk.commons.util.StringUtils;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.ConnectionObserver;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.BulkFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * Memcached service provider Implementation
 *
 * Provides Common MEMCACHED Client Interface.
 * <p/>
 * All Access to MEMCACHED, must be made through this
 * Interface, otherwise you will run the risk to connection
 * memory leakage and other anomalies.
 * <p/>
 * Hibernate is also configured to use MEMCACHED as a second level cache as well, but
 * uses distinct regions.
 *
 * @author (jschenk) Jeff Schenk jeff.a.schenk@gmail.com
 */
@Service("memCachedServiceProvider")
public class MemCachedServiceProviderImpl implements MemCachedServiceProvider,
        ApplicationContextAware {

    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(MemCachedServiceProviderImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Global Properties Injected
     */
    @Value("#{systemEnvironmentProperties['MemCached.Enabled']}")
    private boolean memCachedEnabled;

    @Value("#{systemEnvironmentProperties['MemCached.Server.List']}")
    private String memCachedServerList;

    /**
     * Default Timeout Value in Milliseconds.
     */
    private static final int DEFAULT_TIMEOUT_MILLISECONDS = 300;

    /**
     * Default Timeout Value in Milliseconds.
     * Default 1 Hour data will remain Cached before Expiration
     */
    private static final int DEFAULT_DATA_TIMEOUT_SECONDS = 3600;

    /**
     * Number of times The MemCachedClient went stale.
     */
    private long numberClientStaleConnections = 0;
    private long timeLastClientStaleOutageOccurred = 0;

    /**
     * Default number of times client connection goes stale,
     * will we render this service disabled, as it will to more harm than
     * good at this stage.
     */
    private static final long DEFAULT_FINAL_NUMBER_CLIENT_STALE_CONNECTIONS = 10;

    /**
     * Spring Application Context,
     * used to obtain access to Resources on
     * Classpath.
     */
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Private Globals
     */
    private BinaryConnectionFactory binaryConnectionFactory;
    private MemcachedClient mc;
    private static final String serviceProviderInstanceGUID = new RandomGUID().toString();

    /**
     * Initialize the MemCached Service Provider Interface
     */
    @PostConstruct
    public synchronized void initialize() {
        if (!this.memCachedEnabled) {
            this.initialized = false;
            return;
        }
        logger.info("Starting MemCached Service Provider Facility, this Instance GUID:[" + serviceProviderInstanceGUID + "]");
        if (refreshClientConnection()) {
            // *****************************************
            // Show the Versions of what makes up the
            // memcached Cluster Tier.
            Map<SocketAddress, String> versions = this.mc.getVersions();
            for (SocketAddress socketAddress : versions.keySet()) {
                logger.info("Memcached Cluster Tier Instance:[" + socketAddress.toString() + " " + versions.get(socketAddress) + "]");
            }

        } else {
            logger.warn("Disabling MemcachedServiceProvider for Internal Cache component!");
            this.initialized = false;
            return;
        }
    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public synchronized void destroy() {
        if (this.initialized) {
            // Show Final Statistics
            this.showStatistics();
            ServiceInstanceShutdownLogger.log(this.getClass(), "INFO", "Ending MemCached Service Provider Facility.");
            this.mc.shutdown(3, TimeUnit.SECONDS);
        }
    }

    /**
     * Is Enable check for UpStream service Clients
     * to check if this service providers has been enabled or not.
     *
     * @return boolean indicator indicating if Service is enabled or not.
     */
    @Override
    public boolean isEnabled() {
        return this.initialized;
    }


    /**
     * Common Make Key method to create a new key based upon the specified
     * region and key.
     *
     * @param region
     * @param key
     * @param type
     * @return String containing "region.key"
     */
    @Override
    public String makeRegionKey(String region, String key, MemcachedValueType type) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        return region + DEFAULT_REGION_KEY_SEPARATOR + key
                + DEFAULT_REGION_KEY_SEPARATOR + type.indexString();
    }

    /**
     * Common Make Key method to create a new key based upon the specified
     * region.  A Generic GUID will be used to construct a Unique Key for
     * the Region.
     *
     * @param region
     * @param type
     * @return String containing "region.key"
     */
    @Override
    public String makeRegionKey(String region, MemcachedValueType type) {
        if (StringUtils.isEmpty(region)) {
            throw new IllegalArgumentException("No Region Specified!");
        }
        return region + DEFAULT_REGION_KEY_SEPARATOR + new RandomGUID().toString()
                + DEFAULT_REGION_KEY_SEPARATOR + type.indexString();
    }

    /**
     * Parse the specified Region Key.
     *
     * @param regionKey
     * @return String[] Array consisting of the {"region","key". "type"}.
     */
    @Override
    public String[] parseRegionKey(String regionKey) {
        if ((regionKey == null) || (regionKey.isEmpty())) {
            return new String[3];
        }
        if (StringUtils.countMatches(regionKey, DEFAULT_REGION_KEY_SEPARATOR) > 2) {
            String[] x = regionKey.split("\\" + DEFAULT_REGION_KEY_SEPARATOR, 2);
            return new String[]{x[0], x[1].substring(0, x[1].length() - 2), x[1].substring(x[1].length() - 1)};
        }
        return regionKey.split("\\" + DEFAULT_REGION_KEY_SEPARATOR, 3);
    }

    /**
     * Get the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @return String - Region Key Value
     */
    @Override
    public Object getKeyObjectValue(String region, String key) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        // Try to get a value, for up to our Millisecond Timeout, and cancel if it
        // doesn't return
        Object valueObject = null;
        Future<Object> f = mc.asyncGet(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.OBJECT.indexString());
        try {
            valueObject = f.get(DEFAULT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
            // throws expecting InterruptedException, ExecutionException
            // or TimeoutException
        } catch (Exception e) {
            // Since we don't need this, go ahead and cancel the operation.
            // This is not strictly necessary, but it'll save some work on
            // the server.  It is okay to cancel it if running.
            f.cancel(true);
            // Do other timeout related stuff
        }
        return valueObject;
    }

    /**
     * Asynchronously Obtain a Bulk number of Values based upon specified Key Set.
     *
     * @param keys
     * @return
     */
    @Override
    public BulkFuture<Map<String, Object>> getBulkObjectValues(String region, Collection<String> keys) {
        if ((StringUtils.isEmpty(region)) ||
                (keys == null) || (keys.isEmpty())) {
            throw new IllegalArgumentException("No Region or Keys Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        // Mutate the Keys to region Keys
        Collection<String> regionKeys = new ArrayList<String>();
        for (Object key : keys.toArray()) {
            regionKeys.add(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                    + MemcachedValueType.OBJECT.indexString());
        }
        return this.mc.asyncGetBulk(regionKeys);
    }


    /**
     * Asynchronously Put the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @param value
     * @return
     */
    @Override
    public Future<Boolean> setKeyObjectValue(String region, String key, Object value) {
        return this.setKeyObjectValue(region, key, DEFAULT_DATA_TIMEOUT_SECONDS, value);
    }

    /**
     * Asynchronously Put the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @param expiration
     * @param value
     * @return
     */
    @Override
    public Future<Boolean> setKeyObjectValue(String region, String key, int expiration, Object value) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        return mc.set(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.OBJECT.indexString(), expiration, value);
    }

    /**
     * Remove the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @return
     */
    @Override
    public Boolean removeKeyObjectValue(String region, String key) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        Future<Boolean> asyncResponse = mc.delete(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.OBJECT.indexString());
        try {
            return asyncResponse.get(DEFAULT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
            // throws expecting InterruptedException, ExecutionException
            // or TimeoutException
        } catch (Exception e) {
            // Since we don't need this, go ahead and cancel the operation.
            // This is not strictly necessary, but it'll save some work on
            // the server.  It is okay to cancel it if running.
            asyncResponse.cancel(true);
        }
        return new Boolean(false);
    }

    /**
     * Provides indication if the specified Region and Key exists.
     *
     * @param region
     * @param key
     * @return
     */
    @Override
    public Boolean isKeyObjectValueAvailable(String region, String key) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        if (this.getKeyObjectValue(region, key) != null) {
            return true;
        }
        return false;
    }

    /**
     * @param region
     * @param key
     * @return
     */
    @Override
    public Long incrementKeyCounterValue(String region, String key, int by, long defaultBeginningValue) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        Long valueObject = mc.incr(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.COUNTER.indexString(), by, defaultBeginningValue);
        return valueObject;
    }

    /**
     * @param region
     * @param key
     * @return
     */
    @Override
    public Long decrementKeyCounterValue(String region, String key, int by, long defaultBeginningValue) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        Long valueObject = mc.decr(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.COUNTER.indexString(), by, defaultBeginningValue);
        return valueObject;
    }

    /**
     * Helper method to obtain the current Counter Value.
     *
     * @param region
     * @param key
     * @return Long contains null or current counter value.
     */
    @Override
    public Long getKeyCounterValue(String region, String key) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        Long valueObject = mc.incr(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.COUNTER.indexString(), 0);
        return valueObject;
    }

    /**
     * Provides indication if the specified Region and Key exists.
     *
     * @param region
     * @param key
     * @return
     */
    @Override
    public Boolean isKeyCounterValueAvailable(String region, String key) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        // Try to get a value, for up to our Millisecond Timeout, and cancel if it
        // doesn't return
        Object valueObject = null;
        Future<Object> f = mc.asyncGet(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.COUNTER.indexString());
        try {
            valueObject = f.get(DEFAULT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
            // throws expecting InterruptedException, ExecutionException
            // or TimeoutException
        } catch (Exception e) {
            // Since we don't need this, go ahead and cancel the operation.
            // This is not strictly necessary, but it'll save some work on
            // the server.  It is okay to cancel it if running.
            f.cancel(true);
            // Do other timeout related stuff
        }
        return (valueObject != null) ? true : false;
    }

    /**
     * Remove the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @return
     */
    @Override
    public Boolean removeKeyCounterValue(String region, String key) {
        if ((StringUtils.isEmpty(region)) ||
                (StringUtils.isEmpty(key))) {
            throw new IllegalArgumentException("No Region or Key Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        Future<Boolean> asyncResponse = mc.delete(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                + MemcachedValueType.COUNTER.indexString());
        try {
            return asyncResponse.get(DEFAULT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
            // throws expecting InterruptedException, ExecutionException
            // or TimeoutException
        } catch (Exception e) {
            // Since we don't need this, go ahead and cancel the operation.
            // This is not strictly necessary, but it'll save some work on
            // the server.  It is okay to cancel it if running.
            asyncResponse.cancel(true);
        }
        return new Boolean(false);
    }

    /**
     * Asynchronously Obtain a Bulk number of Values based upon specified Key Set.
     *
     * @param keys
     * @return
     */
    @Override
    public BulkFuture<Map<String, Object>> getBulkCounterValues(String region, Collection<String> keys) {
        if ((StringUtils.isEmpty(region)) ||
                (keys == null) || (keys.isEmpty())) {
            throw new IllegalArgumentException("No Region or Keys Specified!");
        }
        if (!this.initialized) {
            return null;
        }
        // Mutate the Keys to region Keys
        Collection<String> regionKeys = new ArrayList<String>();
        for (Object key : keys.toArray()) {
            regionKeys.add(region + DEFAULT_REGION_KEY_SEPARATOR + key + DEFAULT_REGION_KEY_SEPARATOR
                    + MemcachedValueType.COUNTER.indexString());
        }
        return this.mc.asyncGetBulk(regionKeys);
    }

    /**
     * Helper method to perform a ping to the
     * Memcached Cluster.
     *
     * @return boolean indicator if Memcached Cluster is Alive or not.
     */
    @Override
    public Boolean isMemcachedClusterAlive() {
        if (!this.initialized) {
            return null;
        }
        return this.isMemcachedClusterAlivePrivateMethod();
    }

    /**
     * Helper method to perform a ping to the
     * Memcached Cluster.
     *
     * @return boolean indicator if Memcached Cluster is Alive or not.
     */
    private Boolean isMemcachedClusterAlivePrivateMethod() {
        // ***********************
        // Is our Client Null?
        if (mc == null) {
            return false;
        }
        // ***********************
        // Ensure Client is Alive.
        /**
        if (!this.mc.isAlive()) {
            logger.warn("MemCached Client is no longer Alive!");
            this.initialized = false;  // Pull the Component from the runtime environment.
            // Upstream components which use this service provider that must understand
            // that if the Cache is not available it should always indicate a cache miss.
            return false;
        }
        **/
        // ****************************************
        // Ping Memcached Cluster Bucket
        String key = SYSTEM_REGION_NAME + DEFAULT_REGION_KEY_SEPARATOR + MEMCACHED_PING
                + DEFAULT_REGION_KEY_SEPARATOR + this.serviceProviderInstanceGUID;
        String pingToken =  new RandomGUID().toString();
        this.mc.set(key, 0, pingToken);
        String result = (String) this.mc.get(key);
        if (!result.equals(pingToken)) {
            logger.warn("Did not receive expected value:[" + pingToken + "] received Value:[" +
                    result + "]");
            return false;
        }
        return true;
    }

    /**
     * Private Helper method to show statistics.
     */
    private void showStatistics() {
        if (!this.initialized) {
            return;
        }
        Map<SocketAddress, Map<String, String>> statistics = this.mc.getStats();
        for (SocketAddress socketAddress : statistics.keySet()) {
            ServiceInstanceShutdownLogger.log(this.getClass(), "INFO", "Memcached Statistics for Cluster Tier Instance:[" + socketAddress.toString() + "]");
            for (String statKey : statistics.get(socketAddress).keySet()) {

                ServiceInstanceShutdownLogger.log(this.getClass(), "INFO", " Statistic for:[" + socketAddress.toString()
                        + "], Statistic:[" + statKey + "], Value:[" + statistics.get(socketAddress).get(statKey) + "]");
            }
        }
    }

    /**
     * Private helper method for ensuring a client connection exists, if not will
     * obtain a connection one time, if a failure occurs well remove service provider from component scope so
     * it does not hinder the Upstream Components.
     */
    private synchronized boolean refreshClientConnection() {
        // ************************************
        // If our Connection is Null, refresh
        //it, but only within a given threshold.
        if (this.mc == null) //|| (!this.mc.isAlive()))
        {
            if (this.timeLastClientStaleOutageOccurred != 0) {
                this.numberClientStaleConnections++;
            }
            this.timeLastClientStaleOutageOccurred = System.currentTimeMillis();
            // **************************************************
            // Check for a Threshold.
            if (this.numberClientStaleConnections > DEFAULT_FINAL_NUMBER_CLIENT_STALE_CONNECTIONS) {

                logger.error("*****************************************************************************");
                logger.error("Number of Stale Connections:[" + this.numberClientStaleConnections
                        + "] has exceed our Default Threshold of:[" + DEFAULT_FINAL_NUMBER_CLIENT_STALE_CONNECTIONS + "]");
                logger.error("Potential Memcached Server Latency issue, Please Contact Operations Support!");
                logger.error("Memcached Service Provider will be automatically Disabled.");
                logger.error("*****************************************************************************");

                this.initialized = false;
                return this.initialized;
            } // End of check for Threshold.
        } else {
            // **************************
            // Good Connection so far...
            return this.initialized;
        }
        // **************************************************
        // Attempt a new Connection.
        this.initialized = false;
        try {
            this.mc = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(memCachedServerList));
            if (this.mc == null) {
                logger.warn("Unable to obtain MemCached Client, stopping Initialization.");
            } else if (this.isMemcachedClusterAlivePrivateMethod()) {
                this.initialized = true;
                this.mc.addObserver(new MemcachedServiceProviderConnectionObserver());
                logger.info("MemCached Application Client Services has been initialized successfully.");
            } else {
                logger.warn("Unable to Post and Obtain Initial Private Region value from MemCached Cluster, stopping Initialization.");
            }
        } catch (IOException ioe) {
            logger.error("IO Exception occurred during MemCached Client Initialization: " + ioe.getMessage(), ioe);
        } catch (Exception e) {
            logger.error("Exception occurred during MemCached Client Initialization: " + e.getMessage(), e);
        }
        // ************************************
        // return state.
        return this.initialized;
    }

    protected class MemcachedServiceProviderConnectionObserver implements ConnectionObserver {
        @Override
        public void connectionEstablished(SocketAddress socketAddress, int i) {
            logger.warn("Connection Established for SocketAddress:[" + socketAddress.toString() + "], Port:[" + i + "]");
        }

        @Override
        public void connectionLost(SocketAddress socketAddress) {
            logger.warn("Connection Lost for SocketAddress:[" + socketAddress.toString() + "]!");
        }
    }


}
