package jeffaschenk.commons.constraints.validator;

import net.sf.oval.Validator;
import net.sf.oval.configuration.Configurer;
import net.sf.oval.configuration.annotation.AnnotationsConfigurer;
import net.sf.oval.configuration.annotation.BeanValidationAnnotationsConfigurer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Oval Validator Static Factory Bean
 * <p/>
 * @author jeffaschenk@gmail.com
 * Date: Sep 20, 2010
 * Time: 1:00:49 PM
 */
public class OvalValidatorUtil {
    /**
     * Logging Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(OvalValidatorUtil.class);

    /**
     * Static Class Only, Restrict Access
     */
    private OvalValidatorUtil() {
    }

    /**
     * Global configuration Properties Injected
     * Default Configuration from <code>SORProperties.xml</code>
     *
     * But we use the definitions statically defines.
     *
     * validation.annotations = true
     *
     * validation.annotations.bean = true
     *
     * validation.annotations.jpa = false
     *
     * Enable the "jpa" will cause a very strict enforcement of JPA Annotations and will cause
     * Lazy Load Exception due to deep Object graph resolution of
     * Associations and Collections.
     *
     */

    /**
     * Provides Common Component to obtain Validator
     *
     * @return Validator
     */
    public static Validator getValidator() {
        // ****************************
        // Set up Validator Configures
        List<Configurer> configurers = new ArrayList<Configurer>();
        configurers.add(new AnnotationsConfigurer());
        configurers.add(new BeanValidationAnnotationsConfigurer());
        // *********************************************
        // Create new Validator
        Validator validator = new Validator(configurers);
        if (validator == null) {
            String errorMessage = "Unable to Instantiate Validator, this could cause Application to be Compromised, Failing!";
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        return validator;
    }
}
