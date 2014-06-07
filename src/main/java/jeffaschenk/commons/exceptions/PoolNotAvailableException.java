package jeffaschenk.commons.exceptions;

/**
 * @author Mark A. Miller
 * @version $Revision: #3 $
 * @since 1.0
 */
public class PoolNotAvailableException extends NoFreeResourcesException {

    public PoolNotAvailableException() {
        super();
    }

    public PoolNotAvailableException(String errorKey) {
        super(errorKey);
    }

    public PoolNotAvailableException(String errorKey, String[] arguments) {
        super(errorKey, arguments);
    }

    public PoolNotAvailableException(String errorKey, String[] arguments, Throwable throwable) {
        super(errorKey, arguments);
    }
}
