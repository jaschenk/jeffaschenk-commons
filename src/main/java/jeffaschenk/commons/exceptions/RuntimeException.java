package jeffaschenk.commons.exceptions;

import org.springframework.validation.Errors;

/**
 * This Exception is the base class of all runtime exceptions in the OUTBID-SOR Layer.
 * The exception will contain an <code>Errors</code> object that will describe the exception in more detail.
 * <p/>
 * Please note that the errorCode can be used to try and resolve it to a message in a ResourceBundle
 *
 * @author Jeff
 * @version $Id: $
 * @see ErrorsImpl
 */
public abstract class RuntimeException extends java.lang.RuntimeException {

    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;


    private Errors errors;

    /**
     * Constructor that uses the specified errorCode when creating this exception. Note
     * that since no default message is specified, errorCode will also be used as the default
     * message
     *
     * @param errorCode errorCode of error message
     */
    public RuntimeException(String errorCode) {
        this(errorCode, errorCode);
    }

    /**
     * Constructor that uses the specified errorCode and defaultMessage when creating this exception.
     *
     * @param errorCode      errorCode of error message
     * @param defaultMessage default message for exception
     */
    public RuntimeException(String errorCode, String defaultMessage) {
        this(errorCode, defaultMessage, null);
    }

    /**
     * Constructor that uses the specified errorCode when creating this exception. Note
     * that since no default message is specified, errorCode will also be used as the default
     * message
     *
     * @param errorCode errorCode of error message
     * @param args      arguments to be used when creating the message specified by the errorCode
     */
    public RuntimeException(String errorCode, Object[] args) {
        this(errorCode, errorCode, args);
    }

    /**
     * Constructor that uses the specified errorCode and defaultMessage when creating this exception.
     *
     * @param errorCode      errorCode of error message
     * @param defaultMessage default message for exception
     * @param args           arguments to be used when creating the message specifid by the errorCode
     */
    public RuntimeException(String errorCode, String defaultMessage, Object[] args) {
        super(defaultMessage);
        // create a new errors object and add a global error for this exception
        errors = new ErrorsImpl();
        // TODO: This maintains the errors between requests.
        //errors = ErrorsContextHolder.getErrors();
        errors.reject(errorCode, args, defaultMessage);
    }

    /**
     * Constructor that uses the specifid errors to describe the exception
     *
     * @param errors errors objet
     */
    public RuntimeException(Errors errors) {
        this.errors = errors;
    }

    /**
     * Constructor that uses the specified errors to describe the exception along with the message that the
     * exception should use
     *
     * @param message exception message
     * @param errors  errors object
     */
    public RuntimeException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    /**
     * <p>Getter for the field <code>errors</code>.</p>
     *
     * @return a {@link org.springframework.validation.Errors} object.
     */
    public Errors getErrors() {
        return errors;
    }

}
