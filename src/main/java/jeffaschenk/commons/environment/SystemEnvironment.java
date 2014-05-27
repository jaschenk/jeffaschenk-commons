package jeffaschenk.commons.environment;

import jeffaschenk.JASVersion;
import jeffaschenk.commons.types.EnvironmentType;
import jeffaschenk.commons.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * Environment
 * Dynamic System Environment Properties
 *
 * @author jeffaschenk@gmail.com
 */
public class SystemEnvironment implements SystemEnvironmentBean, ApplicationContextAware, DisposableBean {

    /**
     * Logging
     */
    private static final Log log = LogFactory
            .getLog(SystemEnvironment.class);

    /**
     * Spring Injected Application Context
     */
    private static ApplicationContext applicationContext;

    /**
     * Inject our Java Security Implementation
     */
    static {
        java.security.Security
                .addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Define a ClassLoader Delegate
     */
    private static ClassLoader systemEnvironmentClassLoader;

    /**
     * Runtime Environment Type, Cached Value.
     */
    private static EnvironmentType runtimeEnvironmentType;

    /**
     * Number of Times we are Called to Initialize!
     */
    private int numberOfTimesInitializeCalled = 0;

    /**
     * Inject our System Environment Properties seeded from
     * the system-config.properties file or other specified source.
     */
    @Autowired
    private Properties systemEnvironmentProperties;

    public synchronized void setSystemEnvironmentProperties(Properties systemEnvironmentProperties) {
        if (this.systemEnvironmentProperties != null) {
            log.info("System Environment Properties Bean has been Autowired, refreshing Properties.");
        } else {
            log.info("System Environment Properties Bean setting Initial Properties.");
        }
        // *****************************************
        // Establish Delegate ClassLoader
        systemEnvironmentClassLoader = ClassLoader.getSystemClassLoader();
        // *****************************************
        // Perform the Set.
        this.systemEnvironmentProperties = systemEnvironmentProperties;
    }

    /**
     * Default Constructor
     */
    public SystemEnvironment() {
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Injects Spring Application Context.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
        log.info("Application Context now Established:[" +
                ((this.applicationContext == null) ? "NOT SET" : this.applicationContext.toString()) + "]");
    }

    /**
     * Initialize Bean
     */
    @PostConstruct
    public synchronized void initialize() {
        this.numberOfTimesInitializeCalled++;
        if (this.numberOfTimesInitializeCalled > 1) {
            return;
        }
        log.info("Activating System Environment Properties Bean.");
        if (applicationContext == null) {
            log.error("System Environment does not have Application Context, Application and System issues may exist!");
            return;
        }
        // *****************************************
        // Check Property Object
        if (this.systemEnvironmentProperties == null) {
            log.error("System Environment Properties has not been Autowired Successfully, Application and System issues may exist!");
            return;
        }
        // *******************************************
        // Initialize and Inject any Necessary Global
        // application properties.
        //this.systemEnvironmentProperties.put(APPLICATION_INSTANCE_DEFINITION_NAME_KEY,
        //        (String) this.systemEnvironmentProperties.get(APPLICATION_INSTANCE_DEFINITION_NAME_PREFIX_KEY) +
        //System.currentTimeMillis() +  System.getSecurityManager().toString());
        // ******************************************
        // Show Properties.
        log.info("System Environment Properties have been Autowired Successfully.");
        if (log.isInfoEnabled()) {
            // *************************************
            // Show All Resolved Properties from
            // External Sources, in some Order.
            StringBuffer sb = new StringBuffer();
            sb.append("System Environment Configuration Properties:[");
            sb.append(System.getProperty("line.separator"));
            Object[] propertyNames = this.systemEnvironmentProperties.stringPropertyNames().toArray();
            java.util.Arrays.sort(propertyNames);
            for (Object propertyName : propertyNames) {
                sb.append("   Property Name:[" + propertyName.toString() +
                        "], Value:[" + ((propertyName.toString().toLowerCase().contains("pass")) ||
                        (propertyName.toString().toLowerCase().contains("cred")) ||
                        (propertyName.toString().toLowerCase().contains("access")) ||
                        (propertyName.toString().toLowerCase().contains("secret"))
                        ? "PROTECTED" : (this.systemEnvironmentProperties.getProperty(propertyName.toString()))) + "]");
                sb.append(System.getProperty("line.separator"));
            }
            sb.append("]");
            sb.append(System.getProperty("line.separator"));
            log.info(sb.toString());
        }
        // *****************************************
        // Show Code Base Version
        log.info("Running JAS CodeBase Version:[" + JASVersion.getVersion() + "]");
        // *****************************************
        // Show Runtime Environment
        log.info("Running within Environment Type:[" + this.runningEnvironmentType().toString() + "]");

        // Additional Initialization can appear here...

    }

    @Override
    public void destroy() throws Exception {
        log.info("System Environment Bean Ending LifeCycle, Initialization Calls made:[" + this.numberOfTimesInitializeCalled + "]");
    }

    /**
     * Returns a message source that can be used to translate messages in the system of record.
     *
     * @return a {@link org.springframework.context.MessageSource}
     */
    public static MessageSource getMessageSource() {
        return applicationContext.getBean("messageSource", MessageSource.class);
    }

    /**
     * Is Validation Enabled ?
     *
     * @return boolean
     */
    @Override
    public boolean isValidationEnabled() {
        return StringUtils.toBoolean(systemEnvironmentProperties.getProperty("perform.validation"), true);
    }

    /**
     * Is Validation Annotations Enabled ?
     *
     * @return boolean
     */
    @Override
    public boolean isValidationAnnotationsEnabled() {
        return StringUtils.toBoolean(systemEnvironmentProperties.getProperty("validation.annotations"), true);
    }

    /**
     * Is Validation Bean Annotation Enabled ?
     *
     * @return boolean
     */
    @Override
    public boolean isValidationBeanAnnotationsEnabled() {
        return StringUtils.toBoolean(systemEnvironmentProperties.getProperty("validation.annotations.bean"), true);
    }

    /**
     * Is Validation JPA Annotations Enabled ?
     *
     * @return boolean
     */
    @Override
    public boolean isValidationJPAAnnotationsEnabled() {
        return StringUtils.toBoolean(systemEnvironmentProperties.getProperty("validation.annotations.jpa"), false);
    }

    /**
     * Obtain the Specified Deauthorize Callback Suffix as specified as the ending portion of the Facebook Deauthorize URL.
     *
     * @return
     */
    @Override
    public String getFacebookDeauthorizeCallback() {
        return systemEnvironmentProperties.getProperty("facebook.serviceProvider.deauthorization.callback");
    }

    /**
     * Obtain the Specified Facebook Application Secret for Decoding Signed Requests.
     *
     * @return
     */
    @Override
    public String getFacebookApplicationSecret() {
        return systemEnvironmentProperties.getProperty("facebook.serviceProvider.secret");
    }

    /**
     * @return The prefix of all urls when the pages are served from Facebook.
     */
    public static String getFacebookApplicationDomain() {
        return applicationContext.getBean("facebookApplicationDomain", String.class);
    }

    /**
     * @return The URL that the live event panel redirects to when the live auction is over.
     */
    public static String getLiveEventCheckoutURL() {
        return applicationContext.getBean("liveEventCheckoutURL", String.class);
    }

    /**
     * @return The URL that the Facebook application is launched with.
     */
    public static String getFacebookApplicationURL() {
        return applicationContext.getBean("facebookApplicationURL", String.class);
    }

    /**
     * Provides Determination of the Environment Type we are Running.
     *
     * @return EnvironmentType
     */
    @Override
    public EnvironmentType runningEnvironmentType() {
        if (runtimeEnvironmentType == null) {

            // ****************************************
            // Attempt to Determine the Type of
            // Environment we are running in based
            // upon being able to access various
            // Classes which will be present in a specific
            // Environment.
            //
            if (this.findEnvironmentClass("jeffaschenk.JASVersion")) {
                runtimeEnvironmentType = EnvironmentType.JAS;
            } else if (this.findEnvironmentClass("org.junit.Test")) {
                runtimeEnvironmentType = EnvironmentType.JUNIT;
            } else {
                runtimeEnvironmentType = EnvironmentType.JVM;
            }
        }
        // *************************************
        // return the Cached Environment Type.
        return runtimeEnvironmentType;
    }

    /**
     * Private Helper Method to determine if a Class is Available within this current
     * runtime context or not.
     *
     * @param className
     * @return
     */
    private boolean findEnvironmentClass(String className) {
        try {
            systemEnvironmentClassLoader.loadClass(className);
            return true;
        } catch (ClassNotFoundException cnf) {
            return false;
        }
    }

}
