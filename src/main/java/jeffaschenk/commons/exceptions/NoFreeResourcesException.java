package jeffaschenk.commons.exceptions;

/**
 * An exception thrown by the ResourcePool in situations where
 * there are no free resources available within the time specified.
 */
public class NoFreeResourcesException
        extends FrameworkException {

    /**
     * Default constructor.
     */
    public NoFreeResourcesException() {
        super();
    }

    /**
     * Constructor specifying the exception message.
     *
     * @param message Error text key.
     */
    public NoFreeResourcesException(String message) {
        super(message);
    }

    /**
     * Constructor specifying the exception message.
     *
     * @param message   Error text key.
     * @param arguments Error text arguments.
     */
    public NoFreeResourcesException(String message, String[] arguments) {
        super(message, arguments);
    }

    /**
     * Constructor with String message, arguments and exception
     *
     * @param messageKey The error message key to be saved.
     * @param arguments  The error message arguments to be saved.
     * @param throwable  The underlying exception causing this exception.
     */
    public NoFreeResourcesException(String messageKey, String[] arguments, Throwable throwable) {
        super(messageKey, arguments, throwable);
    }
}
