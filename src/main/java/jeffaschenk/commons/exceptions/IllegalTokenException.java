package jeffaschenk.commons.exceptions;

import org.springframework.validation.Errors;

/**
 * THis exception is thrown when we have an incoming Token
 * from the Internet which has been deemed invalid.
 *
 * @author jeffaschenk@gmail.com
 *         Date: Apr 29, 2010
 *         Time: 8:34:24 AM
 */
public class IllegalTokenException extends RuntimeException {
    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    public IllegalTokenException(String errorCode) {
        super(errorCode);
    }

    public IllegalTokenException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    public IllegalTokenException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    public IllegalTokenException(String errorCode, String defaultMessage, Object[] args) {
        super(errorCode, defaultMessage, args);
    }

    public IllegalTokenException(Errors errors) {
        super(errors);
    }

    public IllegalTokenException(String message, Errors errors) {
        super(message, errors);
    }
}
