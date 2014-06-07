package jeffaschenk.commons.exceptions;

import jeffaschenk.commons.frameworks.cnxidx.utility.message.MessageUtility;

import java.io.StringWriter;
import java.io.PrintWriter;

import java.io.IOException;

/**
 * The base class for all Framework common exceptions.
 */
public class FrameworkException extends Exception {

    //---------------------------
    // attributes
    //---------------------------

    private String nestedExceptionMessage    = null;
    private String nestedExceptionStackTrace = null;
    private String[] messageArguments = null;
    private transient Throwable throwable    = null;

    //---------------------------
    // Constructors
    //---------------------------
    
    /**
     * FrameworkException constructor with no message.
     */
    public FrameworkException() {
        super();
        initNested();
    }

    /**
     * FrameworkException constructor with String message.
     * @param messageKey The error message to be saved.
     */
    public FrameworkException(String messageKey) {
        super(messageKey);
        initNested();
    }
    
    /**
     * FrameworkException constructor with String message.
     * @param messageKey The error message key to be saved.
     * @param arguments The error message arguments to be saved.
     */
    public FrameworkException(String messageKey, String[] arguments) {
        super(messageKey);
        setMessageArguments(arguments);
        initNested();
    }

    /**
     * FrameworkException constructor with String message and causing exception.
     * @param messageKey The error message key to be saved.
     * @param throwable The underlying exception causing this exception.
     */
    public FrameworkException(String messageKey, Throwable throwable) {
        super(messageKey);
        setNestedException(throwable);
    }
    
    /**
     * FrameworkException constructor with String message key, message arguments
     * and causing exception.
     * @param messageKey The error message key to be saved.
     * @param arguments The error message arguments to be saved.
     * @param throwable The underlying exception causing this exception.
     */
    public FrameworkException(String messageKey, String[] arguments, Throwable throwable) {
        super(messageKey);
        setMessageArguments(arguments);
        setNestedException(throwable);
    }

    //---------------------------
    // Public methods
    //---------------------------

    /**
     * This returns the underlying exception that caused this exception
     * to be thrown.
     * @return Throwable Returns a valid Throwable object or null.
     */
    public Throwable getNestedException() {
        return (throwable);
    }

    /**
     * This returns the message for the underlying exception that caused
     * this exception to be thrown.
     * @return String Returns the nested exception message or "" (blank)
     */
    public String getNestedExceptionMessage() {
        return ((nestedExceptionMessage != null) ? nestedExceptionMessage : "");
    }

    /**
     * This returns the stack trace for the underlying exception that caused
     * this exception to be thrown.
     * @return String Returns the nested exception stack trace or "" (blank)
     */
    public String getNestedExceptionStackTrace() {
        return ((nestedExceptionStackTrace != null) ? nestedExceptionStackTrace : "");
    }

    /**
     * Get the stack trace for the exception as a string
     * @return The string representation of the stack trace
     */
    public String getStackTraceAsString() {
        return (getNestedExceptionStackTrace());
    }

    /**
     * Get the message for this exception without formatting characters
     * @return String of the message without formatting
     */
    public String getUnformattedMessage() {
        return unformatText(getMessage());
    }

    /**
     * Get the message for nested exception without formatting characters
     * @return String of the message without formatting
     */
    public String getUnformattedNestedExceptionMessage() {
        return unformatText(getNestedExceptionMessage());
    }

    /**
     * This sets the nested exception.
     *
     * @param throwable The throwable exception to be saved.
     */
    public void setNestedException(Throwable throwable) {
        this.throwable = throwable;
        initNested();
    }
    /**
     * Returns the message arguments
     * @return The message arguments, empty array if there are none.
     */
    public String[] getMessageArguments() {
        return ((messageArguments != null)? messageArguments: new String[] {}) ;
    }
    
    /**
     * Sets the message arguments.
     * @param messageArguments The arguments to use in the message.
     */
    public void setMessageArguments(String[] messageArguments) {
        this.messageArguments = messageArguments;
    }
    
    public String getMessage() {
        if (messageArguments != null) {
            return MessageUtility.getMessage(super.getMessage(), messageArguments);
        }
        return MessageUtility.getMessage(super.getMessage());
    }

    //---------------------------
    // Internal Methods
    //---------------------------
    
    protected void initNested() {
        nestedExceptionMessage = toString() + getNestedExceptionMessage(throwable);
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            printStackTrace(pw);
            getNestedExceptionStackTrace(pw, throwable);
            nestedExceptionStackTrace = sw.toString();
            pw.close();
            sw.close();
        } catch (IOException ignore) {
        }
    }

    protected void getNestedExceptionStackTrace(PrintWriter pw, Throwable t) {
        if (t instanceof FrameworkException) {
            pw.println(((FrameworkException) t).getNestedExceptionStackTrace());
        } else if (t != null) {
            t.printStackTrace(pw);
        }
    }

    protected String getNestedExceptionMessage(Throwable t) {
        String message = "";
        if (t instanceof FrameworkException) {
            message = "\n" + ((FrameworkException) t).getNestedExceptionMessage();
        } else if (t != null) {
            message =  "\n" + t.toString();
        }
        return (message);
    }

    protected String unformatText(String text) {
        StringBuffer buff = new StringBuffer();
        if (text != null) {
            for (int ii=0; ii < text.length(); ii++ ) {
                if (Character.isISOControl(text.charAt(ii))) {
                    buff.append(" ");
                } else {
                    buff.append(text.charAt(ii));
                }
            }
        }
        return buff.toString();
    }
}
