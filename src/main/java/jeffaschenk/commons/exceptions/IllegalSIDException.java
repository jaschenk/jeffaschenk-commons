package jeffaschenk.commons.exceptions;

import org.springframework.validation.Errors;

/**
 * Provides Exception for any Invalid SID presented to DAO layer
 * which have been determined to be invalid.
 *
 * @author jeffaschenk@gmail.com
 *         Date: Apr 7, 2010
 *         Time: 8:33:45 AM
 */
public class IllegalSIDException extends RuntimeException {
    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    public IllegalSIDException(String errorCode) {
        super(errorCode);
    }

    public IllegalSIDException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    public IllegalSIDException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    public IllegalSIDException(String errorCode, String defaultMessage, Object[] args) {
        super(errorCode, defaultMessage, args);
    }

    public IllegalSIDException(Errors errors) {
        super(errors);
    }

    public IllegalSIDException(String message, Errors errors) {
        super(message, errors);
    }
}
