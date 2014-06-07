package jeffaschenk.commons.exceptions;

/**
 * The general exception class for file exceptions
 */
public class XmlException extends FrameworkException {

    //---------------------------
    // Constructors
    //---------------------------

    /**
     * XmlException constructor with no message.
     */
    public XmlException() {
        super();
    }

    /**
     * Xml Exception constructor with String message.
     *
     * @param messageKey The error message key to be saved.
     */
    public XmlException(String messageKey) {
        super(messageKey);
    }

    /**
     * Xml Exception constructor with String message.
     *
     * @param messageKey The error message key to be saved.
     * @param arguments  The error message arguments to be saved.
     */
    public XmlException(String messageKey, String[] arguments) {
        super(messageKey, arguments);
    }

    /**
     * FrameworkException constructor with String message and causing exception.
     *
     * @param messageKey The error message key to be saved.
     * @param throwable  The underlying throwable exception causing this exception.
     */
    public XmlException(String messageKey, Throwable throwable) {
        super(messageKey, throwable);
    }

    /**
     * FrameworkException constructor with String message and causing exception.
     *
     * @param messageKey The error message key to be saved.
     * @param arguments  The error message arguments to be saved.
     * @param throwable  The underlying throwable exception causing this exception.
     */
    public XmlException(String messageKey, String[] arguments, Throwable throwable) {
        super(messageKey, arguments, throwable);
    }
}
