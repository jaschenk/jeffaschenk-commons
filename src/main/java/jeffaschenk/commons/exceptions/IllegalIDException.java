package jeffaschenk.commons.exceptions;

import org.springframework.validation.Errors;

/**
 * Provides Exception for any Invalid UUID or Alternate ID presented to DAO layer
 * which have been determined to be invalid.
 *
 * @author jeffaschenk@gmail.com
 *         Date: Apr 7, 2010
 *         Time: 8:33:45 AM
 */
public class IllegalIDException extends RuntimeException {
    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    public IllegalIDException(String errorCode) {
        super(errorCode);
    }

    public IllegalIDException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    public IllegalIDException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    public IllegalIDException(String errorCode, String defaultMessage, Object[] args) {
        super(errorCode, defaultMessage, args);
    }

    public IllegalIDException(Errors errors) {
        super(errors);
    }

    public IllegalIDException(String message, Errors errors) {
        super(message, errors);
    }
}
