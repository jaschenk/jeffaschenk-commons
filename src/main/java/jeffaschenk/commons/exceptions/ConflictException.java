package jeffaschenk.commons.exceptions;

import org.springframework.validation.Errors;


/**
 * Conflict Exception
 *
 * @author Jeff
 * @version $Id: $
 */
public class ConflictException extends RuntimeException {

    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for ConflictException.</p>
     *
     * @param errorCode a {@link java.lang.String} object.
     */
    public ConflictException(String errorCode) {
        super(errorCode);
    }

    /**
     * <p>Constructor for ConflictException.</p>
     *
     * @param errorCode      a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     */
    public ConflictException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    /**
     * <p>Constructor for ConflictException.</p>
     *
     * @param errorCode a {@link java.lang.String} object.
     * @param args      an array of {@link java.lang.Object} objects.
     */
    public ConflictException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    /**
     * <p>Constructor for ConflictException.</p>
     *
     * @param errodeCode     a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     * @param args           an array of {@link java.lang.Object} objects.
     */
    public ConflictException(String errodeCode, String defaultMessage, Object[] args) {
        super(errodeCode, defaultMessage, args);
    }

    /**
     * <p>Constructor for ConflictException.</p>
     *
     * @param errors a {@link org.springframework.validation.Errors} object.
     */
    public ConflictException(Errors errors) {
        super(errors);
    }

    /**
     * <p>Constructor for ConflictException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param errors  a {@link org.springframework.validation.Errors} object.
     */
    public ConflictException(String message, Errors errors) {
        super(message, errors);
    }

}
