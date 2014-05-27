package jeffaschenk.commons.constraints.annotation;

import jeffaschenk.commons.validation.validators.EmailAddressValidator;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CheckEmailAddressValidator,
 * Custom Validator for checking the Auction Direction.
 *
 * @author jeffaschenk@gmail.com
 *         Date: Sep 13, 2010
 *         Time: 12:49:21 PM
 */
public class CheckEmailAddressValidator extends AbstractAnnotationCheck<CheckEmailAddress> {

    /**
     * Logging Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(CheckEmailAddressValidator.class);

    boolean nullable = false;

    @Override
    public void configure(CheckEmailAddress checkEmailAddress) {
        this.nullable = checkEmailAddress.nullable();
        setMessage(checkEmailAddress.message());
    }

    /**
     * Standard isSatisfied Check for Validation Annotaion
     *
     * @param validatedObject
     * @param valueToValidate
     * @param context
     * @param validator
     * @return
     */
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator) {
        this.requireMessageVariablesRecreation();
        if (log.isDebugEnabled()) {
            log.debug("Attempting Email Validation for Object:[" + validatedObject.getClass().getSimpleName() + "], Value:[" + valueToValidate + "]");
        }
        if (nullable) {
            return (valueToValidate == null) ? true : EmailAddressValidator.isValidEmailAddress((String) valueToValidate);
        } else {
            return EmailAddressValidator.isValidEmailAddress((String) valueToValidate);
        }
    }
}
