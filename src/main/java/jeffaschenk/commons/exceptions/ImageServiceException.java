package jeffaschenk.commons.exceptions;

import org.springframework.validation.Errors;

/**
 * This Exception is used to inform upstream components regarding Image Service
 * Failures.
 *
 * @author Jeff
 * @version $Id: $
 */
public class ImageServiceException extends RuntimeException {

    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errorCode a {@link String} object.
     */
    public ImageServiceException(String errorCode) {
        super(errorCode);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errorCode      a {@link String} object.
     * @param defaultMessage a {@link String} object.
     */
    public ImageServiceException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errorCode a {@link String} object.
     * @param args      an array of {@link Object} objects.
     */
    public ImageServiceException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errodeCode     a {@link String} object.
     * @param defaultMessage a {@link String} object.
     * @param args           an array of {@link Object} objects.
     */
    public ImageServiceException(String errodeCode, String defaultMessage, Object[] args) {
        super(errodeCode, defaultMessage, args);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param errors a {@link org.springframework.validation.Errors} object.
     */
    public ImageServiceException(Errors errors) {
        super(errors);
    }

    /**
     * <p>Constructor for EntityNotFoundException.</p>
     *
     * @param message a {@link String} object.
     * @param errors  a {@link org.springframework.validation.Errors} object.
     */
    public ImageServiceException(String message, Errors errors) {
        super(message, errors);
    }

}
