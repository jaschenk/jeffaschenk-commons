package jeffaschenk.commons.frameworks.cnxidx.utility.logging;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.message.MessageUtility;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Framework logging abstraction of log4j.
 */
public class FrameworkLogger {

    //---------------------------
    // Class constants
    //---------------------------
    private static final String CLASS_NAME = FrameworkLogger.class.getName();
    public static final String PROPERTIES_FILENAME = ".framework.logger";
    public static String LOG_LAYOUT = "%p: %d{MMdd:HHmmss}: %c{2}: THREAD:%t %m%n";

    public static final String NO_CLASS_NAME = "NO_CLASSNAME";
    public static final String NO_METHOD_NAME = "NO_METHODNAME";
    public static final String CATEGORY_NAME_AUDIT = "AUDIT";
    public static final String CATEGORY_NAME_TIMING = "TIMING";

    //---------------------------
    // attributes
    //---------------------------

    protected Logger logger = null;
    protected String sourceClass = null;
    protected String sourceMethod = null;

    private static boolean logTimingStats = true;

    //---------------------------
    // Constructors
    //---------------------------

    /**
     * Construct to use the root logger (protected so nobody really uses this)
     */
    protected FrameworkLogger() {
        logger = Logger.getRootLogger();
    }

    /**
     * Construct to use the logger for the class name and method name
     *
     * @param sourceClass  The fully qualified name of the class
     * @param sourceMethod The name of the method
     */
    public FrameworkLogger(String sourceClass, String sourceMethod) {

        this.sourceClass = sourceClass;
        this.sourceMethod = sourceMethod;
        logger = Logger.getLogger(sourceClass + '.' + sourceMethod);
    }

    /**
     * Construct to use the logger for the class name and method name
     *
     * @param loggerName Logger Name
     */
    public FrameworkLogger(String loggerName) {

        this.sourceClass = loggerName;
        this.sourceMethod = "";
        logger = Logger.getLogger(sourceClass);
    }

    //---------------------------
    // class initialization
    //---------------------------

    static {
        try {
            // if an existing Framework logger is up and running
            // don't override it with this logger
            boolean initLogger = true;
            java.util.Enumeration loggers = LogManager.getCurrentLoggers();
            Logger logger;
            String name;
            while (loggers.hasMoreElements()) {
                logger = (Logger) loggers.nextElement();
                name = logger.getName().toLowerCase();
                if (name.indexOf("jeffaschenk.commons") >= 0) {
                    initLogger = false;
                    break;
                }
            } // End of While Loop.    

            // in-code initialization is to log just errors to system out
            if (initLogger) {
                Properties p = new Properties();
                p.setProperty("log4j.rootCategory", "ERROR" + ", stdout");
                p.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
                p.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
                p.setProperty("log4j.appender.stdout.layout.ConversionPattern", LOG_LAYOUT);
                LogManager.resetConfiguration();
                PropertyConfigurator.configure(p);

                // now attempt to load from the real property file
                // (if it fails we get the in-code settings from above)
                //reloadConfiguration();
            }
        } catch (Exception e) {
            System.out.println("Logging initialization failed.");
            e.printStackTrace();
        }
    }

    //---------------------------
    // Public Methods
    //---------------------------

    /**
     * Indicate if the Debug Level is Enabled for our current
     * Category/Level.
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    } // End of isDebugEnabled public Method.

    /**
     * Indicate if the Info Level is Enabled for our current
     * Category/Level.
     */
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    } // End of isInfoEnabled public Method.

    /**
     * Audit an entry
     *
     * @param userID     A userid to be logged with the audit entry.
     * @param messageKey The message key of the message to log
     * @param userID     The user that is being auditted.
     */
    public void audit(String userID, String messageKey) {

        auditp(userID, messageKey, null);
    }

    /**
     * Audit an entry
     *
     * @param userID     The user that is being auditted.
     * @param messageKey The message key of the message to log
     * @param arguments  The arguments to be used in the message
     */
    public void audit(String userID, String messageKey, Object[] arguments) {

        auditp(userID, messageKey, arguments);
    }

    /**
     * Get the currently set source class name
     *
     * @return String for the source class name attribute
     */
    public String getSourceClass() {
        return (sourceClass);
    }

    /**
     * Get the currently set source method name
     *
     * @return String for the method name attribute
     */
    public String getSourceMethod() {
        return (sourceMethod);
    }

    /**
     * Log a message
     *
     * @param level      The level of the message to log
     * @param messageKey The message key of the message to log
     */
    public void log(FrameworkLoggerLevel level, String messageKey) {
        logp(level, sourceClass, sourceMethod, messageKey, null);
    }

    /**
     * Log a message
     *
     * @param level      The level of the message to log
     * @param methodName method name
     * @param messageKey The message key of the message to log
     */
    public void log(FrameworkLoggerLevel level, String methodName, String messageKey) {
        logp(level, sourceClass, methodName, messageKey, null);
    }

    /**
     * Log a message
     *
     * @param level      The level of the message to log
     * @param messageKey The message key of the message to log
     * @param arguments  The arguments to be substituted in the message.
     */
    public void log(FrameworkLoggerLevel level, String messageKey, Object[] arguments) {
        logp(level, sourceClass, sourceMethod, messageKey, arguments);
    }

    /**
     * Log a message
     *
     * @param level      The level of the message to log
     * @param methodName method name
     * @param messageKey The message key of the message to log
     * @param arguments  The arguments to be substituted in the message.
     */
    public void log(FrameworkLoggerLevel level, String methodName, String messageKey, Object[] arguments) {
        logp(level, sourceClass, methodName, messageKey, arguments);
    }

    /**
     * Log a message with an exception
     *
     * @param level      The level of the message to log
     * @param messageKey The message key of the message to log
     * @param thrown     The exception to log
     */
    public void log(FrameworkLoggerLevel level, String messageKey, Throwable thrown) {
        logp(level, sourceClass, sourceMethod, messageKey, null, thrown);
    }

    /**
     * Log a message with an exception
     *
     * @param level      The level of the message to log
     * @param methodName method name
     * @param messageKey The message key of the message to log
     * @param thrown     The exception to log
     */
    public void log(FrameworkLoggerLevel level, String methodName, String messageKey, Throwable thrown) {
        logp(level, sourceClass, methodName, messageKey, null, thrown);
    }

    /**
     * Log a message with an exception
     *
     * @param level      The level of the message to log
     * @param messageKey The message key of the message to log
     * @param arguments  The arguments to be used in the message
     * @param thrown     The exception to log
     */
    public void log(FrameworkLoggerLevel level, String messageKey, Object[] arguments, Throwable thrown) {
        logp(level, sourceClass, sourceMethod, messageKey, arguments, thrown);
    }

    /**
     * Log a message with an exception
     *
     * @param level      The level of the message to log
     * @param methodName method name
     * @param messageKey The message key of the message to log
     * @param arguments  The arguments to be used in the message
     * @param thrown     The exception to log
     */
    public void log(FrameworkLoggerLevel level, String methodName, String messageKey, Object[] arguments, Throwable thrown) {
        logp(level, sourceClass, methodName, messageKey, arguments, thrown);
    }

    /**
     * Record a timing stats
     *
     * @param timingCategory The category of the timing log entry.
     * @param source         The fully qualified name of the class
     * @param callingMethod  The method this log entry is for
     * @param messageKey     The message key of the message to log
     * @param arguments      The arguments to be used in the message
     * @param stopWatch      An instance of a stop watch that has the recorded time
     */
    public static void logTiming(String timingCategory, String source,
                                 String callingMethod, String messageKey,
                                 Object[] arguments, StopWatch stopWatch) {

        logTimingp(timingCategory, source, callingMethod, messageKey, arguments, stopWatch);
    }

    /**
     * Record a timing stats
     *
     * @param timingCategory The category of the timing log entry.
     * @param source         The fully qualified name of the class
     * @param callingMethod  The method this log entry is for
     * @param messageKey     The message key of the message to log
     * @param stopWatch      An instance of a stop watch that has the recorded time
     */
    public static void logTiming(String timingCategory, String source,
                                 String callingMethod, String messageKey,
                                 StopWatch stopWatch) {
        logTimingp(timingCategory, source, callingMethod, messageKey, null, stopWatch);
    }

    /**
     * Log an audit method.
     *
     * @param userID     - The id of the user the log event is for.
     * @param messageKey - The message key of the message to log.
     * @param arguments  - The parameters to use in the message.
     */
    static public void auditStatic(String userID, String messageKey, Object[] arguments) {
        FrameworkLogger iLogger = new FrameworkLogger(NO_CLASS_NAME, NO_METHOD_NAME);
        iLogger.audit(userID, messageKey, arguments);
    }

    /**
     * Log an audit method.
     *
     * @param userID     - The id of the user the log event is for.
     * @param messageKey - The message key of the message to log.
     */
    static public void auditStatic(String userID, String messageKey) {
        FrameworkLogger iLogger = new FrameworkLogger(NO_CLASS_NAME, NO_METHOD_NAME);
        iLogger.audit(userID, messageKey);
    }

    /**
     * Static version to log a message
     *
     * @param sourceClass  The fully qualified name of the class
     * @param sourceMethod The name of the method
     * @param level        The level of the message to log
     * @param messageKey   The message key of the message to log
     */
    public static void log(String sourceClass, String sourceMethod, FrameworkLoggerLevel level,
                           String messageKey) {
        FrameworkLogger iLogger = new FrameworkLogger(sourceClass, sourceMethod);
        iLogger.log(level, messageKey);
    }

    /**
     * Static version to log a message with arguments.
     *
     * @param sourceClass  The fully qualified name of the class
     * @param sourceMethod The name of the method
     * @param level        The level of the message to log
     * @param messageKey   The message key of the message to log
     * @param arguments    The arguments to use in the message.
     */
    public static void log(String sourceClass, String sourceMethod, FrameworkLoggerLevel level,
                           String messageKey, Object[] arguments) {
        FrameworkLogger iLogger = new FrameworkLogger(sourceClass, sourceMethod);
        iLogger.log(level, messageKey, arguments);
    }

    /**
     * Static version to log a message with a throwable exception
     *
     * @param sourceClass  The fully qualified name of the class
     * @param sourceMethod The name of the method
     * @param level        The level of the message to log
     * @param messageKey   The message key of the message to log
     * @param thrown       The exception to log
     */
    public static void log(String sourceClass, String sourceMethod, FrameworkLoggerLevel level,
                           String messageKey, Throwable thrown) {
        FrameworkLogger iLogger = new FrameworkLogger(sourceClass, sourceMethod);
        iLogger.log(level, messageKey, thrown);
    }

    /**
     * Static version to log a message with a throwable exception
     *
     * @param sourceClass  The fully qualified name of the class
     * @param sourceMethod The name of the method
     * @param level        The level of the message to log
     * @param messageKey   The message key of the message to log
     * @param arguments    The arguments to use in the message.
     * @param thrown       The exception to log
     */
    public static void log(String sourceClass, String sourceMethod, FrameworkLoggerLevel level,
                           String messageKey, Object[] arguments, Throwable thrown) {
        FrameworkLogger iLogger = new FrameworkLogger(sourceClass, sourceMethod);
        iLogger.log(level, messageKey, arguments, thrown);
    }

    /**
     * Static version of logging a timing status message.
     *
     * @param timingCategory The category of the timing log entry.
     * @param sourceClass    The calling class for the log entry
     * @param sourceMethod   The method this log entry is for
     * @param messageKey     The message key to log.
     * @param arguments      The arguments to substitute into the message.
     * @param stopWatch      An instance of a stop watch that has the recorded time
     */
    public static void logTiming(String timingCategory, Class sourceClass,
                                 String sourceMethod, String messageKey,
                                 Object[] arguments, StopWatch stopWatch) {
        String callingClassName = sourceClass.getName();
        logTiming(timingCategory, callingClassName, sourceMethod, messageKey, arguments, stopWatch);
    }

    /**
     * Static version of logging a timing status message.
     *
     * @param timingCategory The category of the timing log entry.
     * @param sourceClass    The calling class for the log entry
     * @param sourceMethod   The method this log entry is for
     * @param messageKey     The message key to log.
     * @param stopWatch      An instance of a stop watch that has the recorded time
     */
    public static void logTiming(String timingCategory, Class sourceClass,
                                 String sourceMethod, String messageKey,
                                 StopWatch stopWatch) {
        logTiming(timingCategory, sourceClass, sourceMethod, messageKey, null, stopWatch);
    }

    /**
     * Set the source class name
     *
     * @param sourceClass String for the source class name
     */
    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
        logger = Logger.getLogger(sourceClass + '.' + sourceMethod);
    }

    /**
     * Set the source method name
     *
     * @param sourceMethod String for the source method name
     */
    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
        logger = Logger.getLogger(sourceClass + '.' + sourceMethod);
    }

    //---------------------------
    // Internal methods
    //---------------------------

    /**
     * Audit an entry
     *
     * @param userID     The user that is being auditted.
     * @param messageKey The message key of the message to log
     * @param arguments  The arguments to be used in the message
     */
    public void auditp(String userID, String messageKey, Object[] arguments) {
        String msgText = getMessage(messageKey, arguments);

        FrameworkLogger iLogger = new FrameworkLogger(CATEGORY_NAME_AUDIT);

        StringBuffer str = new StringBuffer();
        str.append(" UserID:");
        str.append(userID);

        str.append(" MSG:");
        str.append(msgText);
        iLogger.logger.info(str.toString());
    }

    /**
     * 1.3 version of logp.  Remove this when we go to 1.4 logging
     */
    protected void logp(FrameworkLoggerLevel level, String source, String method,
                        String messageKey, Object[] arguments) {
        logger.log(MessageUtility.getLevel(messageKey, level), getMessage(messageKey, arguments));
    }

    /**
     * 1.3 version of logp.  Remove this when we go to 1.4 logging
     */
    protected void logp(FrameworkLoggerLevel level, String source, String method,
                        String messageKey, Object[] arguments, Throwable thrown) {
        logger.log(MessageUtility.getLevel(messageKey, level), getMessage(messageKey, arguments), thrown);
    }

    /**
     * Record a timing stats
     *
     * @param timingCategory The category of the timing log entry.
     * @param sourceClass    The fully qualified name of the class
     * @param callingMethod  The method this log entry is for
     * @param messageKey     The message key of the message to log
     * @param arguments      The arguments to be used in the message
     * @param stopWatch      An instance of a stop watch that has the recorded time
     */
    protected static void logTimingp(String timingCategory, String sourceClass,
                                     String callingMethod, String messageKey,
                                     Object[] arguments, StopWatch stopWatch) {

        if (logTimingStats) {
            String msgText = getMessage(messageKey, arguments);

            FrameworkLogger iLogger = new FrameworkLogger(CATEGORY_NAME_TIMING);

            msgText = (msgText != null) ? msgText.replace('\t', ' ')
                    // don't want this tab when we
                    // import log file into an excel
                    // spreadsheet
                    : "";
            // Don't allow msgText
            // to be NULL.
            StringBuffer str = new StringBuffer();
            sourceClass = sourceClass.substring(sourceClass.lastIndexOf(".") + 1);

            str.append("\t").append(stopWatch.toString());
            str.append("\t").append(timingCategory);
            str.append("\t").append(sourceClass).append(".").append(callingMethod);
            str.append("\tMSG: ").append(msgText);

            iLogger.logger.info(str.toString());
        }
    } // End of logTimingp Protected method.

    /*
     * set the Log Timing Stats on or Off.
     */
    public static void setLogTimingStats(boolean newLogTimingStats) {
        logTimingStats = newLogTimingStats;
    } // End of setLogTimingStats

    /*
     * Provides Indicator to determine if Logging Stats are active or not.
     */
    public static boolean isLogTimingStatsOn() {
        return logTimingStats;
    } // End of isLogTimingStatsOn

    /**
     * Helper method that builds a string that has the current date formatted into a string.
     *
     * @return the formated string version of the current date.
     */
    private static String getDTS() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMdd.HHmmss");
        String dateTimeStamp = formatter.format(new Date());
        return dateTimeStamp;
    }

    // Helper method to get the message from the Message Utility
    private static String getMessage(String messageKey, Object[] arguments) {
        // use the arguments if they are provided.
        if ((arguments != null) && (arguments.length > 0)) {
            return MessageUtility.getMessage(messageKey, arguments);
        }

        return MessageUtility.getMessage(messageKey);
    }
}

