
package jeffaschenk.commons.exceptions;

/**
 * Exception thrown by the ResourceProvider when there is a problem
 * with a particular resource being managed by the ResourcePool.
 */
public class ResourceProviderException
        extends FrameworkException {

    /**
     * A constructor that specifies the exception string.
     *
     * @param s Error key.
     */
    public ResourceProviderException(String s) {
        super(s);
    }

    /**
     * A constructor that specifies the exception string.
     *
     * @param s Error key.
     * @param s Error arguments.
     */
    public ResourceProviderException(String s, String[] arguments) {
        super(s, arguments);
    }

    /**
     * Constructor with String message and execption
     *
     * @param messageKey The error message key to be saved.
     * @param throwable  The underlying exception causing this exception.
     */
    public ResourceProviderException(String messageKey, Throwable throwable) {
        super(messageKey, throwable);
    }

    /**
     * Constructor with String message, arguments and exception
     *
     * @param messageKey The error message key to be saved.
     * @param arguments  The error message arguments to be saved.
     * @param throwable  The underlying exception causing this exception.
     */
    public ResourceProviderException(String messageKey, String[] arguments, Throwable throwable) {
        super(messageKey, arguments, throwable);
    }
}
