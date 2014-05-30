package jeffaschenk.commons.system.internal.cms;


import jeffaschenk.commons.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Content Management Service Interface
 * Provides Access to resolve any dynamic content directly from an established cache or
 *
 * @author jeffaschenk@gmail.com
 */
@Service("contentManagementService")
public class ContentManagementServiceImpl implements ContentManagementService,
        ApplicationContextAware {

    /**
     * Logging
     */
    private final static Log log = LogFactory.getLog(ContentManagementServiceImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Global Properties Injected
     */
    @Value("#{systemEnvironmentProperties['cms.enabled']}")
    private String cmsEnabledString;
    private Boolean cmsEnabled;

    @Value("#{systemEnvironmentProperties['cms.access.preference']}")
    private String cmsAccessPreference;

    @Value("#{systemEnvironmentProperties['cms.store.space.name']}")
    private String cmsStoreSpaceName;

    @Value("#{systemEnvironmentProperties['cms.repository.principal']}")
    private String cmsRepositoryPrincipal;

    @Value("#{systemEnvironmentProperties['cms.repository.credentials']}")
    private String cmsRepositoryCredentials;

    @Value("#{systemEnvironmentProperties['cms.repository.ws.location']}")
    private String cmsRepository_WS_Location;

    @Value("#{systemEnvironmentProperties['cms.repository.cmis.location']}")
    private String cmsRepository_CMIS_Location;

    @Value("#{systemEnvironmentProperties['cms.repository.webdav.location']}")
    private String cmsRepository_WEBDAV_Location;

    @Value("#{systemEnvironmentProperties['cms.repository.atompub.location']}")
    private String cmsRepository_ATOMPUB_Location;

    @Value("#{systemEnvironmentProperties['cms.consumer.today.content.location']}")
    private String cmsConsumerTodayContent_Location;

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
        /**
         * Determine if CMS has been Enabled or Not.
         */
        this.cmsEnabled = StringUtils.toBoolean(cmsEnabledString, false);
        if (!this.cmsEnabled) {
            this.initialized = false;
            log.warn("CMS - Client Services Implementation, will not be enabled. \n" +
                    "Dynamic Content will be replaced by Static placeholder Data, if configured as such.");

        } else {
            log.warn("Activating CMS - (Content Management System) Client Services Implementation.");

            log.warn("CMS - Determining breadth of Implementation Based upon Instance Environment.");


            this.initialized = true;
        }
    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public void destroy() {
        if (this.initialized) {


            log.warn("Deactivating CMS - Client Services Implementation, " +
                    "Dynamic Content will be replaced by Static placeholder Data, if configured as such.");

            // ***************************
            // Indicate not initialized!
            initialized = false;
            log.warn("Deactivation of CMS Service Implementation was Successful.");
        }
    }

    @Override
    public String grabDynamicContent(String path) {
        return ""; // Stubbed out  
    }

    @Override
    public String grabLatestFeedContent(String path) {
        return ""; // Stubbed out
    }

    @Override
    public String grabToday() {
        // Create the client resource
        ClientResource resource = new ClientResource(cmsConsumerTodayContent_Location);
        try {
            // return the Obtained Raw Content
            Representation contentRepresentation = resource.get();
            return StringUtils.convertStreamToString(contentRepresentation.getStream());
        } catch (Exception e) {
            log.error("Error Rendering Dynamic Page:[TODAY], Exception:[" + e.getMessage() + "]");
            return "";
        }
    }

   
}
