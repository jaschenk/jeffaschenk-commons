package jeffaschenk.commons.frameworks.cnxidx.utility;

import java.util.logging.*;

/**
 * Java Class to provide common logging for IRR utilities.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */

public class idxLogger {
    public static final String CLASSNAME = idxLogger.class.getName();
    public static Logger logger = Logger.getLogger("jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger");

    // ****************************
    // Available Logging Levels.
    //
    // SEVERE (highest value)
    // WARNING
    // INFO
    // CONFIG
    // FINE
    // FINER
    // FINEST (lowest value)
    //
    // ****************************


    /**
     * Initial Constructor.
     */
    public idxLogger() {
    } // End of Constructor.

    /**
     * Entering Message.
     */
    public void entering(String _sourceclass, String _sourcemethod) {
        if (logger.isLoggable(Level.FINER)) {
            logger.entering(_sourceclass, _sourcemethod);
        } // End of If.
    } // End of Entering Logging Method.

    /**
     * Exiting Message.
     */
    public void exiting(String _sourceclass, String _sourcemethod) {
        if (logger.isLoggable(Level.FINER)) {
            logger.exiting(_sourceclass, _sourcemethod);
        } // End of If.
    } // End of Exiting Logging Method.

    /**
     * Generic Message Log with Level Specified.
     */
    public void log(Level _level, String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(_level)) {
            logger.logp(_level, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of generic Log Logging Method.

    /**
     * FINEST
     */
    public void finest(String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.logp(Level.FINEST, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of finest Logging Method.

    /**
     * FINER
     */
    public void finer(String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(Level.FINER)) {
            logger.logp(Level.FINER, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of finer Logging Method.

    /**
     * FINE
     */
    public void fine(String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of fine Logging Method.

    /**
     * CONFIG
     */
    public void config(String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(Level.CONFIG)) {
            logger.logp(Level.CONFIG, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of config Logging Method.

    /**
     * INFO
     */
    public void info(String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(Level.INFO)) {
            logger.logp(Level.INFO, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of info Logging Method.

    /**
     * WARNING
     */
    public void warning(String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.logp(Level.WARNING, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of warning Logging Method.

    /**
     * SEVERE
     */
    public void severe(String _sourceclass, String _sourcemethod, String _msg) {
        if (logger.isLoggable(Level.SEVERE)) {
            logger.logp(Level.SEVERE, _sourceclass, _sourcemethod, _msg);
        } // End of If.
    } // End of severe Logging Method.

} ///:~ End of idxLogger Class
