package jeffaschenk.commons.exceptions;

import org.springframework.validation.Errors;

/**
 * This Exception is used to inform users of a service that the requested entity does not exist.
 *
 * @author Jeff
 * @version $Id: $
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errorCode a {@link java.lang.String} object.
     */
    public EntityNotFoundException(String errorCode) {
        super(errorCode);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errorCode      a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     */
    public EntityNotFoundException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errorCode a {@link java.lang.String} object.
     * @param args      an array of {@link java.lang.Object} objects.
     */
    public EntityNotFoundException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errodeCode     a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     * @param args           an array of {@link java.lang.Object} objects.
     */
    public EntityNotFoundException(String errodeCode, String defaultMessage, Object[] args) {
        super(errodeCode, defaultMessage, args);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errors a {@link org.springframework.validation.Errors} object.
     */
    public EntityNotFoundException(Errors errors) {
        super(errors);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param errors  a {@link org.springframework.validation.Errors} object.
     */
    public EntityNotFoundException(String message, Errors errors) {
        super(message, errors);
    }

}
