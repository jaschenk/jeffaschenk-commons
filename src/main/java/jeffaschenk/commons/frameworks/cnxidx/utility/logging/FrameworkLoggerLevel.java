package jeffaschenk.commons.frameworks.cnxidx.utility.logging;

import org.apache.log4j.Level;

/**
 * The framework logging level abstraction of log4j.
 */
public class FrameworkLoggerLevel extends Level {

    public static final FrameworkLoggerLevel SEVERE =
            new FrameworkLoggerLevel(Level.FATAL_INT, "SEVERE", Level.FATAL.getSyslogEquivalent());

    public static final FrameworkLoggerLevel ERROR =
            new FrameworkLoggerLevel(Level.ERROR_INT, "ERROR", Level.ERROR.getSyslogEquivalent());

    public static final FrameworkLoggerLevel WARNING =
            new FrameworkLoggerLevel(Level.WARN_INT, "WARNING", Level.WARN.getSyslogEquivalent());

    public static final FrameworkLoggerLevel INFO =
            new FrameworkLoggerLevel(Level.INFO_INT, "INFO", Level.INFO.getSyslogEquivalent());

    public static final FrameworkLoggerLevel DEBUG =
            new FrameworkLoggerLevel(Level.DEBUG_INT, "DEBUG", Level.DEBUG.getSyslogEquivalent());

    //---------------------------
    // Constructors
    //---------------------------

    protected FrameworkLoggerLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }
}

