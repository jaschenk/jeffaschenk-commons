package jeffaschenk.commons.system.external.geocoding;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import jeffaschenk.commons.touchpoint.model.serviceprovider.GeoLocation;
import jeffaschenk.commons.touchpoint.model.serviceprovider.GeoLocationCoordinates;
import jeffaschenk.commons.types.GeoCodingServiceProviderResponseStatus;
import jeffaschenk.commons.util.StringUtils;
import net.iharder.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

/**
 *
 * Provides Common GeoCoding Service Provider Implementation using the Google Maps API
 * for performing location lookup and validation.
 * <p/>
 * Online API Documentation: http://code.google.com/apis/maps/documentation/geocoding/
 *
 * @author jeffaschenk@gmail.com
 */
@Service("geoCodingServiceProvider")
public class GeoCodingServiceProviderImpl implements GeoCodingServiceProvider, ApplicationContextAware {
    /**
     * Global Constants
     */
    private static final String ADDRESS = "address=";
    private static final String LATLNG = "latlng=";

    /**
     * Geocoding GeoLocation Provider Injected Properties
     */
    @Value("#{systemEnvironmentProperties['geocoding.enabled']}")
    private String geoCodingEnabledStringValue;
    private boolean geoCodingEnabled;

    @Value("#{systemEnvironmentProperties['geocoding.service.providerUrl']}")
    private String geoCodingServiceProviderUrl;

    @Value("#{systemEnvironmentProperties['geocoding.service.provider.clientId']}")
    private String geoCodingServiceProviderClientId;

    @Value("#{systemEnvironmentProperties['geocoding.service.provider.clientSignature']}")
    private String geoCodingServiceProviderClientSignature;
    private byte[] binaryClientSignature;

    @Value("#{systemEnvironmentProperties['geocoding.service.provider.allowed.requests.per.day']}")
    private Long geoCodingServiceProviderAllowedRequestsPerDay;

    @Value("#{systemEnvironmentProperties['geocoding.service.provider.allowed.requests.per.second']}")
    private Long geoCodingServiceProviderAllowedRequestsPerSecond;

    @Value("#{systemEnvironmentProperties['geocoding.service.provider.throttle.seconds.wait.per.request']}")
    private Long geoCodingServiceProviderThrottleSecondsWaitPerRequest;

     //
     // TODO - Have Geo Coding service Provider use Memcached as Internal cache for Queries.
     //


    /**
     * Explicitly Set
     */
    private static final String geoCodingServiceProviderOutput = "json";  // Using JSON as opposed to XML.

    private static final String geoCodingSensorForServer = "false";  // Our Server has no notion of GPS, Why Not?

    /**
     * Logging
     */
    private final static Log log = LogFactory.getLog(GeoCodingServiceProviderImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Statistically Counters and other Variables for
     * performing throttling of request to this service
     * provider.
     */
    private Calendar currentTime = Calendar.getInstance();

    private long totalNumberOfRequestsPerformed = 0;

    private long dailyNumberOfRequestsPerformed = 0;

    private long totalNumberOfRequestsBlocked = 0;

    private long dailyNumberOfRequestsBlocked = 0;

    private long numberOfRequestsPerSecond = 0;

    private long maximumNumberOfRequestsPerSecondReached = 0;

    private long whenMaximumNumberOfRequestsPerSecondReached = -1;

    private long lastRequestBlocked = -1;

    private long lastRequestPerformed = -1;

    private int dayInterval = 0;

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
     * Initialize the Content Management System Interface
     */
    @PostConstruct
    public void initialize() {
        this.geoCodingEnabled = StringUtils.toBoolean(this.geoCodingEnabledStringValue, false);
        if (this.geoCodingEnabled) {
            log.info("Activating GeoCoding Service Provider Interface - Google Maps Rest Services Implementation.");

            log.info("Configured Allowed Requests Per Day:[" +
                    ((this.geoCodingServiceProviderAllowedRequestsPerDay != null) &&
                            (this.geoCodingServiceProviderAllowedRequestsPerDay > 0) ?
                            this.geoCodingServiceProviderAllowedRequestsPerDay :
                            "OFF")
                    + "]");

            log.info("Configured Allowed Requests Per Second:[" +
                    ((this.geoCodingServiceProviderAllowedRequestsPerSecond != null) &&
                            (this.geoCodingServiceProviderAllowedRequestsPerSecond > 0) ?
                            this.geoCodingServiceProviderAllowedRequestsPerSecond :
                            "OFF")
                    + "]");

            log.info("Configured Throttle Wait:[" +
                    ((this.geoCodingServiceProviderThrottleSecondsWaitPerRequest != null) &&
                            (this.geoCodingServiceProviderThrottleSecondsWaitPerRequest > 0) ?
                            this.geoCodingServiceProviderThrottleSecondsWaitPerRequest + " seconds" :
                            "OFF")
                    + "]");

            // Determine if I have a Signature or Not.
            if (StringUtils.isNotEmpty(this.geoCodingServiceProviderClientSignature)) {
                // Convert the key from 'web safe' base 64 to binary
                String keyString = this.geoCodingServiceProviderClientSignature.replace('-', '+');
                keyString = keyString.replace('_', '/');
                try {
                    this.binaryClientSignature = Base64.decode(keyString);
                } catch (IOException ioe) {
                    log.error("Base64 decoding Exception occurred for Client Signature, removing Client Information so GMap Requests will not be Signed!");
                    this.geoCodingServiceProviderClientSignature = null;
                    this.geoCodingServiceProviderClientId = null;
                }
            } else {
                log.warn("No Client Signature Supplied, GMap Requests will not be Signed!");
            }

            // ***********************************************
            // Now Prepare Internal Cache for all Geo
            // Queries

            //
            // TODO - Have Geo Coding service Provider use Memcached as Internal cache for Queries.
            //






            // Initialized.
            this.initialized = true;
        } else {
            log.info("GeoCoding Service Provider Interface has been Disabled.");

            this.initialized = false;
        }
    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public void destroy() {
        if (this.initialized) {
            log.info("Deactivating GeoCoding Service Provider Interface");

            // ***************************
            // Indicate not initialized!
            initialized = false;
            log.info("Deactivation of GeoCoding Service Provider Interface was Successful.");
        }
    }

    /**
     * getGeoCodingByAddress
     *
     * @param address - The textual Location Address for obtaining the GeoCoding.
     * @return String json representation
     */
    @Override
    public GeoLocation getGeoCodingByAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return new GeoLocation(GeoCodingServiceProviderResponseStatus.INVALID_REQUEST);
        }
        return this.sendRequestToServiceProvider(ADDRESS + address.replace(" ","+"));
    }

    /**
     * getGeoCodingByCoordinates
     * <p/>
     * Additional optional parameters, which are we are not supporting at the moment.
     * o bounds (optional) — The bounding box of the viewport within which to bias geocode results more prominently.
     * (For more information see Viewport Biasing below.)
     * <p/>
     * o region (optional) — The region code, specified as a ccTLD ("top-level domain") two-character value. (For more information see Region Biasing below.)
     * <p/>
     * o language (optional) — The language in which to return results. See the supported list of domain languages.
     * Note that we often update supported languages so this list may not be exhaustive. If language is not supplied,
     * the geocoder will attempt to use the native language of the domain from which the request is sent wherever possible.
     *
     * @param coordinates - The latitude/longitude values for which you wish to obtain the closest, human-readable address.
     * @return String json representation
     */
    @Override
    public GeoLocation getGeoCodingByCoordinates(GeoLocationCoordinates coordinates) {
        return this.sendRequestToServiceProvider(LATLNG + coordinates.getLatitude() + "," + coordinates.getLongitude());
    }

    /**
     * getGeoCodingByCoordinates -- Aka Reverse Geo Lookup.
     *
     * @param latitude
     * @param longitude
     * @return GeoLocation
     */
    @Override
    public GeoLocation getGeoCodingByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        return this.sendRequestToServiceProvider(LATLNG + latitude + "," + longitude);
    }

    @Override
    public boolean isGeoCodingEnabled() {
        return geoCodingEnabled;
    }

    @Override
    public String getGeoCodingServiceProviderUrl() {
        return geoCodingServiceProviderUrl;
    }

    @Override
    public String getGeoCodingServiceProviderOutput() {
        return geoCodingServiceProviderOutput;
    }

    @Override
    public String getGeoCodingSensorForServer() {
        return geoCodingSensorForServer;
    }

    @Override
    public long getTotalNumberOfRequestsPerformed() {
        return totalNumberOfRequestsPerformed;
    }

    @Override
    public long getDailyNumberOfRequestsPerformed() {
        return dailyNumberOfRequestsPerformed;
    }

    @Override
    public long getTotalNumberOfRequestsBlocked() {
        return totalNumberOfRequestsBlocked;
    }

    @Override
    public long getDailyNumberOfRequestsBlocked() {
        return dailyNumberOfRequestsBlocked;
    }

    @Override
    public long getNumberOfRequestsPerSecond() {
        return numberOfRequestsPerSecond;
    }

    @Override
    public long getMaximumNumberOfRequestsPerSecondReached() {
        return maximumNumberOfRequestsPerSecondReached;
    }

    @Override
    public long getWhenMaximumNumberOfRequestsPerSecondReached() {
        return whenMaximumNumberOfRequestsPerSecondReached;
    }

    @Override
    public long getLastRequestBlocked() {
        return lastRequestBlocked;
    }

    @Override
    public long getLastRequestPerformed() {
        return lastRequestPerformed;
    }

    @Override
    public int getDayInterval() {
        return dayInterval;
    }

    /**
     * Provide Request to Geocoding Service Provider to Obtain the Location based upon
     * the specified parameter information.
     *
     * @param requestParameters
     * @return GeoLocation
     */
    private GeoLocation sendRequestToServiceProvider(String requestParameters) {
        if (!this.geoCodingEnabled) {
            return new GeoLocation(GeoCodingServiceProviderResponseStatus.DISABLED);
        }

          // ***********************************************
          // Immediately check cache if available.
          //
          // TODO - Have Geo Coding service Provider use Memcached as Internal cache for Queries.
          // TODO - Jeff Schenk
          //

        // Throttle Requests
        if (!canRequestBeSentToProvider()) {
            return new GeoLocation(GeoCodingServiceProviderResponseStatus.OVER_QUERY_LIMIT);
        }
        if (log.isDebugEnabled())
            { log.debug("GeoLocation Request being Sent:[" + requestParameters + "]"); }
        if (StringUtils.isEmpty(requestParameters)) {
            return new GeoLocation(GeoCodingServiceProviderResponseStatus.INVALID_REQUEST);
        }
        // Prepare for sending request
        RestTemplate restTemplate = new RestTemplate();
        //
        // must be CommonsClientHttpRequestFactory or else the location header
        // in an HTTP 302 won't be followed
        // restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());
        // TODO .. repair the refacoting...
        //
        MappingJacksonHttpMessageConverter json = new MappingJacksonHttpMessageConverter();
        json.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "javascript")));
        restTemplate.getMessageConverters().add(json);
        RestOperations restOperations = restTemplate;

        // geoCodingServiceProviderClientId
        if ((StringUtils.isEmpty(this.geoCodingServiceProviderClientId)) ||
                (StringUtils.isEmpty(this.geoCodingServiceProviderClientSignature))) {
            // Perform the Call, without a specified ClientID
            GeoLocation geoLocation = restOperations.getForObject(this.geoCodingServiceProviderUrl + "{output}" + "?{requestParameters}" +
                    "&sensor={sensorSetting}", GeoLocation.class, this.geoCodingServiceProviderOutput,
                    requestParameters, this.getGeoCodingSensorForServer());
            // TODO Place in Cache....
            return geoLocation;
        } else {
            // Sign the Call and Specify the clientId as Well.
            try {
                String signedRequest = this.signGMapRequest(this.geoCodingServiceProviderUrl + this.geoCodingServiceProviderOutput + "?" +
                        requestParameters +
                        "&client=" + this.geoCodingServiceProviderClientId +
                        "&sensor=" + this.getGeoCodingSensorForServer());
                if (log.isDebugEnabled())
                    { log.debug("Signed URL:["+signedRequest+"]"); }

                GeoLocation geoLocation = restOperations.getForObject(signedRequest, GeoLocation.class);
                // TODO Place in Cache....
                return geoLocation;
            } catch (Exception e) {
                log.error("Exception encountered while attempting to sign GMap Request, Sending request without Client and Signature:[" + e.getMessage() + "]");
                // Try Request without ClientID and being signed...
                GeoLocation geoLocation = restOperations.getForObject(this.geoCodingServiceProviderUrl + "{output}" + "?{requestParameters}" +
                        "&sensor={sensorSetting}", GeoLocation.class, this.geoCodingServiceProviderOutput,
                        requestParameters, this.getGeoCodingSensorForServer());
                 // TODO Place in Cache....
                return geoLocation;
            }
        }
    }

    /**
     * Private helper method to determine if a Request can be sent to a Service provider
     * based upon contractual or specified usage so we do not create excessive requests
     * during a major registration event and cause a request storm to the service provider.
     * <p/>
     * F1- High Priority
     * - Google Maps API has a limit on the number of requests per second
     * that an application can make.
     * - That limit must be configurable and will either be 5 or 10 requests
     * per second.
     * - We have to implement a first-in/first-out queue for all map and
     * location requests to throttle at this configured limit.
     * <p/>
     * F2 TBD
     * - The throttling code will also have to limit the number of requests
     * per day
     * - Error handling so user does not get a generic good api error
     * - Some kind of audit on how many requests per day occur
     *
     * @return boolean
     */
    private synchronized boolean canRequestBeSentToProvider() {
        // Initialize
        long now = System.currentTimeMillis();
        // Check for Clearing Daily Totals
        if (this.currentTime.get(Calendar.DAY_OF_MONTH) != this.dayInterval) {
            log.warn("Rolling Daily Totals for Day:[" + this.dayInterval + "], Blocked:[" + this.dailyNumberOfRequestsBlocked + "],"
                    + " Performed:[" + this.dailyNumberOfRequestsPerformed + "]");
            // TODO - Perform Roll of Data to a Log for additional Auditing.
            this.dailyNumberOfRequestsBlocked = 0;
            this.dailyNumberOfRequestsPerformed = 0;
            this.dayInterval = this.currentTime.get(Calendar.DAY_OF_MONTH);
        }
        // Check for Requests allowed per Day
        if ((this.geoCodingServiceProviderAllowedRequestsPerDay != null) &&
                (this.geoCodingServiceProviderAllowedRequestsPerDay > -1)) {
            if (this.dailyNumberOfRequestsPerformed > this.geoCodingServiceProviderAllowedRequestsPerDay) {
                this.lastRequestBlocked = now;
                this.dailyNumberOfRequestsBlocked++;
                this.totalNumberOfRequestsBlocked++;
                log.warn("GeoCoding Provider Request has been Blocked due to the Maximum Allowed Requests per Day have been Reached!");
                if (log.isDebugEnabled()) {
                    log.debug(this.toString());
                }
                return false;
            }
        }
        // Check Interval of Time
        if ((now - this.lastRequestPerformed) <= 1000) {
            this.numberOfRequestsPerSecond++;
            if (this.maximumNumberOfRequestsPerSecondReached < this.numberOfRequestsPerSecond) {
                this.maximumNumberOfRequestsPerSecondReached = this.numberOfRequestsPerSecond;
                this.whenMaximumNumberOfRequestsPerSecondReached = now;
            }
        } else {
            this.numberOfRequestsPerSecond = 0;
        }
        // Check for Requests allowed per Second
        if ((this.geoCodingServiceProviderAllowedRequestsPerSecond != null) &&
                (this.geoCodingServiceProviderAllowedRequestsPerSecond > -1)) {
            if (this.numberOfRequestsPerSecond > this.geoCodingServiceProviderAllowedRequestsPerSecond) {
                this.lastRequestBlocked = now;
                this.dailyNumberOfRequestsBlocked++;
                this.totalNumberOfRequestsBlocked++;
                log.warn("GeoCoding Provider Request has been Blocked due to the Maximum Allowed Requests per Second have been Reached!");
                if (log.isDebugEnabled()) {
                    log.debug(this.toString());
                }
                return false;
            }
        }
        // Final Check and Sleep if Enabled.
        if ((this.geoCodingServiceProviderThrottleSecondsWaitPerRequest != null) &&
                (this.geoCodingServiceProviderThrottleSecondsWaitPerRequest > 0)) {
            try {
                Thread.sleep(this.geoCodingServiceProviderThrottleSecondsWaitPerRequest * 1000);
            } catch (InterruptedException e) {
            }
        }
        // Increment Total Requests Performed
        this.totalNumberOfRequestsPerformed++;
        this.dailyNumberOfRequestsPerformed++;
        this.lastRequestPerformed = now;
        // Allow
        return true;
    }

    @Override
    public String toString() {
        return "GeoCodingServiceProviderImpl Current Statistic:{" +
                "currentTime=" + currentTime +
                ", totalNumberOfRequestsPerformed=" + totalNumberOfRequestsPerformed +
                ", dailyNumberOfRequestsPerformed=" + dailyNumberOfRequestsPerformed +
                ", totalNumberOfRequestsBlocked=" + totalNumberOfRequestsBlocked +
                ", dailyNumberOfRequestsBlocked=" + dailyNumberOfRequestsBlocked +
                ", numberOfRequestsPerSecond=" + numberOfRequestsPerSecond +
                ", maximumNumberOfRequestsPerSecondReached=" + maximumNumberOfRequestsPerSecondReached +
                ", whenMaximumNumberOfRequestsPerSecondReached=" + whenMaximumNumberOfRequestsPerSecondReached +
                ", lastRequestBlocked=" + lastRequestBlocked +
                ", lastRequestPerformed=" + lastRequestPerformed +
                ", dayInterval=" + dayInterval +
                '}';
    }

    /**
     * Private Helper Method to Sign Requests.
     *
     * @param resource
     * @return  String
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.InvalidKeyException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    private String signGMapRequest(String resource) throws NoSuchAlgorithmException,
            InvalidKeyException, URISyntaxException, IOException {

        URL url = new URL(resource);
        log.warn("Signing URL Path:["+url.getPath()+"], URL Query:["+url.getQuery()+"]");
         // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(this.binaryClientSignature, SecurityConstants.HMAC_SHA1);
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(SecurityConstants.HMAC_SHA1);
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] sigBytes = mac.doFinal((url.getPath() + "?" + url.getQuery()).getBytes());

        // base 64 encode the binary signature
        String signature = Base64.encodeBytes(sigBytes);
        signature = signature.replace('+', '-');
        signature = signature.replace('/', '_');
        return resource + "&signature=" + signature;
    }

}
