package jeffaschenk.commons.exceptions;


/**
 * Qnow Errors Context Holder Class
 *
 * @author Jeff
 * @version $Id: $
 */
public abstract class ErrorsContextHolder {

    private static final ThreadLocal<Errors> errorsHolder = new ThreadLocal<Errors>();

    /**
     * Clear the errors for the current thread.
     */
    public static void clearErrors() {
        errorsHolder.set(null);
    }

    /**
     * Assign the given Errors object to the current Thread.
     *
     * @param errors a {@link Errors} object.
     */
    public static void setErrors(Errors errors) {
        errorsHolder.set(errors);
    }

    /**
     * <p>getErrors</p>
     *
     * @return The current Thread's Errors implementation.
     */
    public static Errors getErrors() {
        Errors errors = errorsHolder.get();
        if (errors == null) {
            errors = new ErrorsImpl();
            setErrors(errors);
        }
        return errors;
    }
}
