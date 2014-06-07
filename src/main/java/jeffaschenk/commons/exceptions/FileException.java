package jeffaschenk.commons.exceptions;


import java.lang.*;

/**
 * The general exception class for file exceptions
 */
public class FileException extends FrameworkException {

    //---------------------------
    // Constructors
    //---------------------------

    /**
     * FileException constructor with no message.
     */
    public FileException() {
        super();
    }

    /**
     * FileException constructor with String message.
     *
     * @param messageKey The error message key to be saved.
     */
    public FileException(String messageKey) {
        super(messageKey);
    }

    /**
     * FileException constructor with String message and arguments.
     *
     * @param messageKey The error message key to be saved.
     * @param arguments  The arguments for the message.
     */
    public FileException(String messageKey, String[] arguments) {
        super(messageKey, arguments);
    }

    /**
     * FrameworkException constructor with String message and causing exception.
     *
     * @param message   The error message to be saved.
     * @param throwable The underlying throwable exception causing this exception.
     */
    public FileException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * FrameworkException constructor with String message and causing exception.
     *
     * @param messageKey The error message key to be saved.
     * @param arguments  The error message arguments.
     * @param throwable  The underlying throwable exception causing this exception.
     */
    public FileException(String messageKey, String[] arguments, Throwable throwable) {
        super(messageKey, arguments, throwable);
    }
}
