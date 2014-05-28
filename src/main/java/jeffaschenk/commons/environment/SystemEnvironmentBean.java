package jeffaschenk.commons.environment;

import jeffaschenk.commons.types.EnvironmentType;

/**
 *
 * Dynamic System Environment Properties
 * <p/>
 * Our Bean and Alias are defined in the SORProperties.xml Spring Configuration file.
 *
 * @author jeffaschenk@gmail.com
 */
public interface SystemEnvironmentBean {

    /**
     * Global Property Application Instance Keys
     * Used to distinguish between SystemEnvironment Instances when deployed
     * without a common SystemEnvironment such as an EAR Deployment.
     * <p/>
     * These are set programmatically, within the SystemEnvironment Implementation.
     */
    public static final String APPLICATION_INSTANCE_DEFINITION_NAME_PREFIX_KEY
            = "application.instance.definition.name.prefix";

    public static final String APPLICATION_INSTANCE_DEFINITION_NAME_KEY
            = "application.instance.definition.name";

    /**
     * Provides Determination of the Environment Type we are Running.
     *
     * @return EnvironmentType
     */
    EnvironmentType runningEnvironmentType();

    /**
     * Is Validation Enabled ?
     *
     * @return boolean
     */
    boolean isValidationEnabled();

    /**
     * Is Validation Annotations Enabled ?
     *
     * @return boolean
     */
    boolean isValidationAnnotationsEnabled();

    /**
     * Is Validation Bean Annotation Enabled ?
     *
     * @return boolean
     */
    boolean isValidationBeanAnnotationsEnabled();

    /**
     * Is Validation JPA Annotations Enabled ?
     *
     * @return boolean
     */
    boolean isValidationJPAAnnotationsEnabled();

    /**
     * Obtain the Specified Deauthorize Callback Suffix as specified as the ending portion of the Facebook Deauthorize URL.
     *
     * @return String
     */
    String getFacebookDeauthorizeCallback();

    /**
     * Obtain the Specified Facebook Application Secret for Decoding Signed Requests.
     *
     * @return String
     */
    String getFacebookApplicationSecret();

}
