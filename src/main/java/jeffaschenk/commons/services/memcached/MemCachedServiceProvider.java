package jeffaschenk.commons.services.memcached;

import net.spy.memcached.internal.BulkFuture;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Memcached Service Provider
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
public interface MemCachedServiceProvider {

    /**
     * Standard Provider Constants
     */
    public static final String DEFAULT_REGION_NAME = "TOUCHPOINT";
    public static final String INTERNAL_REGION_NAME = DEFAULT_REGION_NAME + "_INTERNAL";
    public static final String SYSTEM_REGION_NAME = DEFAULT_REGION_NAME + "_SYSTEM";
    public static final String FACEBOOK_STATISTIC_REGION_NAME = DEFAULT_REGION_NAME + "_FB_STAT";
    public static final String GOOGLE_STATISTIC_REGION_NAME = DEFAULT_REGION_NAME + "_G_STAT";
    public static final String PAGE_CACHE_REGION_NAME = DEFAULT_REGION_NAME + "_PAGE_CACHE";
    public static final String USER_CACHE_REGION_NAME = DEFAULT_REGION_NAME + "_USER_CACHE";

    /**
     * Standard Providers
     */
    public static final String REGIONS[] = {
            DEFAULT_REGION_NAME,
            INTERNAL_REGION_NAME,
            SYSTEM_REGION_NAME,
            FACEBOOK_STATISTIC_REGION_NAME,
            GOOGLE_STATISTIC_REGION_NAME,
            PAGE_CACHE_REGION_NAME,
            USER_CACHE_REGION_NAME
    };

    /**
     * Standard Provider System Keys
     */
    public static final String MEMCACHED_PING = "REGION_PING";

    /**
     * Enumerator for Memcached Value Type.
     */
    public enum MemcachedValueType {
        COUNTER(1),
        OBJECT(2);

        private final int index;

        private MemcachedValueType(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }

        public String indexString() {
            return Integer.toString(index);
        }

    }

    /**
     * Standard Region and Key Separator
     */
    public static final String DEFAULT_REGION_KEY_SEPARATOR = ".";

    /**
     * Is Enable check for UpStream service Clients
     * to check if this service providers has been enabled or not.
     *
     * @return boolean indicator indicating if Service is enabled or not.
     */
    boolean isEnabled();

    /**
     * Common Make Key method to create a new key based upon the specified
     * region and key.
     *
     * @param region
     * @param key
     * @param type
     * @return String containing "region.key"
     */
    String makeRegionKey(String region, String key, MemcachedValueType type);

    /**
     * Common Make Key method to create a new key based upon the specified
     * region and key.
     *
     * @param region
     * @param type
     * @return String containing "region.key"
     */
    String makeRegionKey(String region, MemcachedValueType type);

    /**
     * Parse the specified Region Key.
     *
     * @param regionKey
     * @return String[] Array consisting of the {"region","key"}.
     */
    String[] parseRegionKey(String regionKey);

    /**
     * Provides Status of currect Memcached Instance Cluster.
     *
     * @return boolean indicates if Status was alive or not.
     */
    Boolean isMemcachedClusterAlive();

    /**
     * Get the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @return String - Region Key Value
     */
    Object getKeyObjectValue(String region, String key);

    /**
     * Asynchronously Put the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @param value
     * @return
     */
    Future<Boolean> setKeyObjectValue(String region, String key, Object value);

    /**
     * Asynchronously Put the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @param expiration
     * @param value
     * @return
     */
    Future<Boolean> setKeyObjectValue(String region, String key, int expiration, Object value);

    /**
     * Remove the Specified Value by Region and Key.
     *
     * @param region
     * @param key
     * @return
     */
    Boolean removeKeyObjectValue(String region, String key);

    /**
     * Provides indication if the specified Region and Key exists.
     *
     * @param region
     * @param key
     * @return
     */
    Boolean isKeyObjectValueAvailable(String region, String key);


    /**
     * Asynchronously Obtain a Bulk number of Values based upon specified Key Set.
     *
     * @param region
     * @param keys
     * @return
     */
    BulkFuture<Map<String, Object>> getBulkObjectValues(String region, Collection<String> keys);

    /**
     * Increment a Counter Key Value by a supplied Amount and obtain the
     * current sum.
     *
     * @param region
     * @param key
     * @return Long contains null or current counter value after increment.
     */
    Long incrementKeyCounterValue(String region, String key, int by, long defaultBeginningValue);

    /**
     * Decrement a Counter Key Value by a supplied Amount and obtain the
     * current sum.
     *
     * @param region
     * @param key
     * @return Long contains null or current counter value after decrement.
     */
    Long decrementKeyCounterValue(String region, String key, int by, long defaultBeginningValue);

    /**
     * Helper method to obtain the current Counter Value.
     *
     * @param region
     * @param key
     * @return Long contains null or current counter value.
     */
    Long getKeyCounterValue(String region, String key);

    /**
     * Remove a Key Counter Value.
     *
     * @param region
     * @param key
     * @return
     */
    Boolean removeKeyCounterValue(String region, String key);

    /**
     * Provides indication if the specified Region and Key exists.
     *
     * @param region
     * @param key
     * @return
     */
    Boolean isKeyCounterValueAvailable(String region, String key);

     /**
     * Asynchronously Obtain a Bulk number of Values based upon specified Key Set.
     *
     * @param region
     * @param keys
     * @return
     */
    BulkFuture<Map<String, Object>> getBulkCounterValues(String region, Collection<String> keys);
}
