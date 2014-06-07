package jeffaschenk.commons.exceptions;

/*
 * Standard exception for any errors occurring in classes of this package.
 * 
 * @Author Capstone Consulting, Inc.
 */

/**
 * Constructor
 */
public class InvalidPropertyException extends FrameworkException {
    /**
     * Constructor
     *
     * @param s Error text
     */
    public InvalidPropertyException(String s) {
        super(s);
    }

    public InvalidPropertyException(String s, String[] arguments) {
        super(s, arguments);
    }
}
