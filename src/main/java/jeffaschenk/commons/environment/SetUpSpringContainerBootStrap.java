package jeffaschenk.commons.environment;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * SetUpSpringContainerBootStrap Utility Class. Provides Spring+AOP+Hibernate
 * configuration and setup as underlying Persistence Manager via Injection for a
 * non-container Environment only.
 *
 * @author jeff.schenk
 * @version $Id: SetUpSpringContainerBootStrap.java 8201 2011-05-17 19:58:21Z jschenk $
 */
public class SetUpSpringContainerBootStrap {

    /**
     * Logging
     */
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SetUpSpringContainerBootStrap.class);

    // *******************************************
    // Spring+AOP+Hibernate Default configurations
    private static final String defaultEnvironmentProperties
            = "TouchPointEnvironmentProperties.xml";
    private static final String defaultDAOBeans
            = "TouchPointDAOBeans.xml";
    private static final String defaultSecurity
            = "TouchPointSecurity.xml";
    private static final String defaultJMXContext
            = "TouchPointJMX.xml";


    private static final String[] defaultBeanDefinitionsToLoad =
            new String[]{defaultEnvironmentProperties, defaultDAOBeans, defaultSecurity, defaultJMXContext};

    private static String[] beanDefinitionsToLoad = defaultBeanDefinitionsToLoad;

    /**
     * Provides an Initialization point for the Spring Framework container. A
     * benign constructor which provides us away to allow our static definition
     * to be instantiated.
     */
    public static AbstractApplicationContext init() {
        return buildContext();
    }

    public static void setBeanDefinitionsToLoad(String[] beanDefinitionsToLoad) {
        if (beanDefinitionsToLoad == null)
            throw new IllegalArgumentException("Null not acceptable");
        SetUpSpringContainerBootStrap.beanDefinitionsToLoad = beanDefinitionsToLoad;
    }

    public static void restoreDefaultBeansToLoad() {
        beanDefinitionsToLoad = defaultBeanDefinitionsToLoad;
    }

    // ******************************************
    // Private Helper Method.
    // ******************************************

    /**
     * Build Context
     */
    private static AbstractApplicationContext buildContext() {
        // *************************************
        // Build our Initial Application Context
        AbstractApplicationContext ctx = null;
        try {
            ctx
                = new ClassPathXmlApplicationContext(beanDefinitionsToLoad);
        } catch (final Exception e) {
            logger
                    .error(
                            "Application Context Initialization Failed!",
                            e);
            throw new RuntimeException(
                    "Unable to initialize due to possible Spring configuration Issue!");
        } // End of Exception Processing.
        logger.info("Application Context Bootstrapped Successfully.");
        return ctx;
    }

    /**
     * Wrapper for Load Bean Definitions
     *
     * @param xmlReader
     * @param resourceName
     */
    private static void loadBeanDefinitions(XmlBeanDefinitionReader xmlReader, String resourceName) throws RuntimeException {
        try {
            xmlReader.loadBeanDefinitions(new ClassPathResource(resourceName));
        } catch (final BeanDefinitionParsingException bdpe) {
            if (resourceName.equalsIgnoreCase(defaultSecurity)) {
                return;
            } else {
                throw bdpe;
            }
        }
    }

} // /:> End of SetUpSpringContainerBootStrap class.
