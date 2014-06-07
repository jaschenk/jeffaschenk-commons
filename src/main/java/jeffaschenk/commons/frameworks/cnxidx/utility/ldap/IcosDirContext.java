package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;

import javax.naming.*;
import javax.naming.directory.*;

/**
 * This wraps the DirContext allowing for restarting of calls
 * if the call resulted in a return code 51(busy)
 * or 53(unable to service).
 *
 * @author jeffaschenk@gmail.com
 * @version 4.4 09/28/2005
 * @since 12/12/2001
 */
@SuppressWarnings("unchecked")
public class IcosDirContext extends InitialDirContext {

    // *******************************
    // Logging Information.
    private static String CLASS_NAME = IcosDirContext.class.getName();
    public static final String PACKAGENAME = IcosDirContext.class.getPackage().getName();

    // ********************************
    // Saved Environment Reference. 
    private java.util.Hashtable cached_environment = null;

    // ********************************
    // Retry Count and Sleep Thresholds
    private int retryCount;
    private long retrySleepMillis;

    // ********************************
    // Statistic Counters.
    private idxElapsedTime elapsed_context_life;

    // ********************************
    // Global Constants.
    private static final String DIRECTORY_TIMING_CATEGORY = "Directory";
    private static final String ObjectClassName = "objectclass";
    private static final String UserPasswordName = "userpassword";

    /**
     * Constructs an initial DirContext. The environment
     * of the new context are set to null.
     */
    public IcosDirContext(int retryCount, long retrySleepMillis)
            throws NamingException {
        super();
        this.retryCount = retryCount;
        this.retrySleepMillis = retrySleepMillis;
        this.init();
    } // End of Constructor.

    /**
     * Constructs an initial DirContext using information
     * supplied in 'environment'.
     */
    public IcosDirContext(java.util.Hashtable environment, int retryCount, long retrySleepMillis)
            throws NamingException {
        super(environment);
        this.cached_environment = environment;
        this.retryCount = retryCount;
        this.retrySleepMillis = retrySleepMillis;
        this.init();
    } // End of Constructor.

    /**
     * Common Initialization.
     */
    private void init() {
        this.elapsed_context_life = new idxElapsedTime();
        String METHODNAME = "init";

        // ***************************
        // Acquire our Provide URL.
        String provider_url = null;
        if (cached_environment == null) {
            provider_url = System.getProperty(DirContext.PROVIDER_URL);
        } else {
            provider_url = (String) cached_environment.get(DirContext.PROVIDER_URL);
        } // End of Else.

        // **************************
        // Log out Initialization.
        FrameworkLogger.log(CLASS_NAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.ICOSDIRCONTEXT_INITIALIZED,
                new String[]{new Integer(this.retryCount).toString(),
                        new Long(this.retrySleepMillis).toString(),
                        new Boolean(this.isDebugModeActive()).toString(),
                        new Boolean(this.isTimingModeActive()).toString(),
                        provider_url});
    } // End of init Initialization Method.

    /**
     * Is Debug Mode Active, based upon Property Sertting?
     * Makes things more Efficient when we are not
     * in DEBUG mode to eliminate any unnecessary
     * invokcations to log data not needed.
     */
    public boolean isDebugModeActive() {
        FrameworkLogger LOG = new FrameworkLogger(this.PACKAGENAME);
        return LOG.isDebugEnabled();
    } // End of isDebugModeActive.

    /**
     * Is Timing Mode Active, based upon current Log Level?
     * Allow inline methods to determine If We are in
     * Debug Mode based upon Logging Level.
     * Makes things more Efficient when we are not
     * in DEBUG mode to eliminate any unnecessary
     * invokcations to log data not needed.
     */
    public boolean isTimingModeActive() {
        return FrameworkLogger.isLogTimingStatsOn();
    } // End of isTimingModeActive.

    /**
     * Binds 'name' to the object 'obj' and associate the
     * attributes 'attrs' with the named object.
     */
    public void bind(Name name, Object obj, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "bind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_BIND,
                    new String[]{name.toString(), formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.bind(name, obj, attrs);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of bind Method.

    /**
     * Binds 'name' to the object 'obj' and associate the
     * attributes 'attrs' with the named object.
     */
    public void bind(String name, Object obj, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "bind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_BIND,
                    new String[]{name.toString(), formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.bind(name, obj, attrs);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of bind Method.

    /**
     * Creates a new context with given attributes, and
     * binds it in the target context.
     */
    public DirContext createSubcontext(Name name, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "createSubcontext";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_CREATESUBCONTEXT,
                    new String[]{name.toString(), formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.createSubcontext(name, attrs);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of createSubcontext Method.

    /**
     * Creates a new context with given attributes,
     * and binds it in the target context.
     */
    public DirContext createSubcontext(String name, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "createSubcontext";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_CREATESUBCONTEXT,
                    new String[]{name, formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.createSubcontext(name, attrs);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of createSubcontext Method.

    /**
     * Retrieves all of the attributes associated
     * with a named object.
     */
    public Attributes getAttributes(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETATTRIBUTES,
                    new String[]{name.toString(), null},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getAttributes(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getAttributes Method.

    /**
     * Retrieves selected attributes associated
     * with a named object.
     */
    public Attributes getAttributes(Name name, String[] attrIds)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETATTRIBUTES,
                    new String[]{name.toString(), formatArray(attrIds)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getAttributes(name, attrIds);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getAttributes Method.

    /**
     * Retrieves all of the attributes associated
     * with a string-named object.
     */
    public Attributes getAttributes(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETATTRIBUTES,
                    new String[]{name, null},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getAttributes(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getAttributes Method.

    /**
     * Retrieves selected attributes associated with
     * a string-named object.
     */
    public Attributes getAttributes(String name, String[] attrIds)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETATTRIBUTES,
                    new String[]{name.toString(), formatArray(attrIds)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getAttributes(name, attrIds);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.       
    } // End of getAttributes Method.

    /**
     * Retrieves the schema associated with the named
     * object.
     */
    public DirContext getSchema(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getSchema";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETSCHEMA,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getSchema(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getSchema Method.

    /**
     * Retrieves the schema associated with the
     * string-named object.
     */
    public DirContext getSchema(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getSchema";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETSCHEMA,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getSchema(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getSchema Method.

    /**
     * Retrieves the schema object class definition for
     * the named object.
     */
    public DirContext getSchemaClassDefinition(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getSchemaClassDefinition";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETSCHEMACLASSDEFINITION,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getSchemaClassDefinition(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getSchemaCLassDefinition Method.

    /**
     * Retrieves the schema object class definition
     * associated with the (string-) named object.
     */
    public DirContext getSchemaClassDefinition(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getSchemaClassDefinition";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETSCHEMACLASSDEFINITION,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getSchemaClassDefinition(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getSchemaCLassDefinition Method.

    /**
     * Modifies the attributes associated with a named object.
     */
    public void modifyAttributes(Name name, int mod_op, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "modifyAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_MODIFYATTRIBUTES,
                    new String[]{name.toString(),
                            formatModOp(mod_op),
                            formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.modifyAttributes(name, mod_op, attrs);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of modifyAttributes Method.

    /**
     * Modifies the attributes associated with a named object
     * using an an ordered list of modifications.
     */
    public void modifyAttributes(Name name, ModificationItem[] mods)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "modifyAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_MODIFYATTRIBUTES_WITHMODITEMS,
                    new String[]{name.toString(),
                            formatModItems(mods)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.modifyAttributes(name, mods);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of modifyAttributes Method.

    /**
     * Modifies the attributes associated with a string-named
     * object.
     */
    public void modifyAttributes(String name, int mod_op, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "modifyAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_MODIFYATTRIBUTES,
                    new String[]{name,
                            formatModOp(mod_op),
                            formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.modifyAttributes(name, mod_op, attrs);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of modifyAttributes Method.

    /**
     * Modifies the attributes associated with a string-named
     * object using an an ordered list of modifications.
     */
    public void modifyAttributes(String name, ModificationItem[] mods)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "modifyAttributes";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_MODIFYATTRIBUTES_WITHMODITEMS,
                    new String[]{name.toString(),
                            formatModItems(mods)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.modifyAttributes(name, mods);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of modifyAttributes Method.

    /**
     * Binds 'name' to the object 'obj' and associates the
     * attributes 'attrs' with the named object,
     * overwriting any existing binding.
     */
    public void rebind(Name name, Object obj, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "rebind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_REBIND,
                    new String[]{name.toString(),
                            formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.rebind(name, obj, attrs);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of rebind Method.

    /**
     * Binds 'name' to the object 'obj' and associates the
     * attributes 'attrs' with the (string-) named object,
     * overwriting any existing binding.
     */
    public void rebind(String name, Object obj, Attributes attrs)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "rebind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        ;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_REBIND,
                    new String[]{name.toString(),
                            formatAttributesForLogging(attrs, true)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.rebind(name, obj, attrs);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of rebind Method.

    /**
     * Searches in a single context for objects that contain a
     * specified set of attributes.
     */
    public NamingEnumeration search(Name name, Attributes matchingAttributes)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_MATCHATTRS,
                    new String[]{name.toString(),
                            formatAttributesForLogging(matchingAttributes, false)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, matchingAttributes);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Searches in a single context for objects that contain a
     * specified set of attributes and retrieve their attributes.
     */
    public NamingEnumeration search(Name name, Attributes matchingAttributes, String[] attributesToReturn)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_MATCHATTRS_ATTR_TO_RETURN,
                    new String[]{name.toString(),
                            formatAttributesForLogging(matchingAttributes, false),
                            formatArray(attributesToReturn)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, matchingAttributes, attributesToReturn);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Searches in the named context or object for entries that
     * satisfy the given search filter.
     */
    public NamingEnumeration search(Name name, String filterExpr,
                                    Object[] filterArgs, SearchControls cons) throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_FILTER_WITHARGS,
                    new String[]{name.toString(),
                            formatSearchControls(cons),
                            formatSearchFilter(filterExpr),
                            formatArray(filterArgs)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, filterExpr, filterArgs, cons);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Searches in the named context or object for entries that
     * satisfy the given search filter.
     */
    public NamingEnumeration search(Name name, String filter, SearchControls cons)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_FILTER,
                    new String[]{name.toString(),
                            formatSearchControls(cons),
                            formatSearchFilter(filter)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, filter, cons);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Searches in a single context for objects that contain a
     * specified set of attributes.
     */
    public NamingEnumeration search(String name, Attributes matchingAttributes)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_MATCHATTRS,
                    new String[]{name,
                            formatAttributesForLogging(matchingAttributes, false)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, matchingAttributes);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Searches in a single context for objects that contain a
     * specified set of attributes and return their specified
     * attributes.
     */
    public NamingEnumeration search(String name, Attributes matchingAttributes,
                                    String[] attributesToReturn) throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_MATCHATTRS_ATTR_TO_RETURN,
                    new String[]{name.toString(),
                            formatAttributesForLogging(matchingAttributes, false),
                            formatArray(attributesToReturn)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, matchingAttributes, attributesToReturn);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Searches in the named context or object for entries that
     * satisfy the given search filter.
     */
    public NamingEnumeration search(String name, String filterExpr,
                                    Object[] filterArgs, SearchControls cons) throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_FILTER_WITHARGS,
                    new String[]{name,
                            formatSearchControls(cons),
                            formatSearchFilter(filterExpr),
                            formatArray(filterArgs)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, filterExpr, filterArgs, cons);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Searches in the named context or named object for entries
     * that satisfy the given search filter.
     */
    public NamingEnumeration search(String name, String filter, SearchControls cons)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "search";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_SEARCH_BY_FILTER,
                    new String[]{name,
                            formatSearchControls(cons),
                            formatSearchFilter(filter)},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.search(name, filter, cons);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of search Method.

    /**
     * Creates and binds a new context.
     */
    public Context createSubcontext(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "createSubcontext";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_CREATESUBCONTEXT,
                    new String[]{name.toString(), null},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.createSubcontext(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of createSubcontext Method.

    /**
     * Creates and binds a new context.
     */
    public Context createSubcontext(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "createSubcontext";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_CREATESUBCONTEXT,
                    new String[]{name, null},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.createSubcontext(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of createSubcontext Method.

    /**
     * Destroys the named context and removes it from the namespace.
     */
    public void destroySubcontext(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "destroySubcontext";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_DESTROYSUBCONTEXT,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.destroySubcontext(name);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of destroySubcontext Method.

    /**
     * Destroys the named context and removes it from the namespace.
     */
    public void destroySubcontext(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "destroySubcontext";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_DESTROYSUBCONTEXT,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.destroySubcontext(name);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of destroySubcontext Method.

    /**
     * Enumerates the names bound in the named context, along with
     * the class names of objects bound to them.
     */
    public NamingEnumeration list(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "list";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LIST,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.list(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of list Method.

    /**
     * Enumerates the names bound in the named context, along with
     * the class names of objects bound to them.
     */
    public NamingEnumeration list(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "list";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LIST,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.list(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of list Method.

    /**
     * Enumerates Names Bound.
     */
    public NamingEnumeration listBindings(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "listBindings";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LISTBINDINGS,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.listBindings(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of listBindings Method.

    /**
     * Enumerates the names bound in the named context, along with
     * the objects bound to them.
     */
    public NamingEnumeration listBindings(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "listBindings";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LISTBINDINGS,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.listBindings(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of listBindings Method.

    /**
     * Enumerates the names bound in the named context, along with
     * the objects bound to them.
     */
    public Object lookup(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "lookup";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LOOKUP,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.lookup(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of lookup Method.

    /**
     * Retrieves the named object.
     */
    public Object lookup(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "lookup";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LOOKUP,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.lookup(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of lookup Method.

    /**
     * Retrieves the named object.
     */
    public Object lookupLink(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "lookupLink";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LOOKUPLINK,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.lookupLink(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of lookupLink Method.

    /**
     * Retrieves the named object, following links except for the
     * terminal atomic component of the name.
     */
    public Object lookupLink(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "lookupLink";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_LOOKUPLINK,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.lookupLink(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of lookupLink Method.

    /**
     * Retrieves the named object, following links except for the
     * terminal atomic component of the name.
     */
    public void rebind(Name name, Object obj)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "rebind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_REBIND_OBJECT,
                    new String[]{name.toString(), obj.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.rebind(name, obj);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of rebind Method.

    /**
     * Binds a name to an object, overwriting any existing binding.
     */
    public void rebind(String name, Object obj)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "rebind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_REBIND_OBJECT,
                    new String[]{name, obj.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.rebind(name, obj);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of rebind Method.

    /**
     * Binds a name to an object, overwriting any existing binding.
     */
    public void rename(Name oldName, Name newName)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "rename";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_RENAME,
                    new String[]{oldName.toString(), newName.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.rename(oldName, newName);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of rename Method.

    /**
     * Binds a new name to the object bound to an old name, and
     * unbinds the old name.
     */
    public void rename(String oldName, String newName)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "rename";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_RENAME,
                    new String[]{oldName, newName},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.rename(oldName, newName);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of rename Method.

    /**
     * Unbinds the named object.
     */
    public void unbind(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "unbind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_UNBIND,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.unbind(name);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of unbind Method.

    /**
     * Unbinds the named object.
     */
    public void unbind(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "unbind";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_UNBIND,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.unbind(name);
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of unbind Method.

    /**
     * Compose Name.
     */
    public Name composeName(Name name, Name prefix)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "composeName";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_COMPOSENAME,
                    new String[]{name.toString(), prefix.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.composeName(name, prefix);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of composeName Method.

    /**
     * Compose Name.
     */
    public String composeName(String name, String prefix)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "composeName";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_COMPOSENAME,
                    new String[]{name, prefix},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.composeName(name, prefix);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of composeName Method.

    /**
     * Get Name in Name Space.
     */
    public String getNameInNamespace()
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getNameInNamespace";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getNameInNamespace();
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getNameInNamespace Method.

    /**
     * Get Name Parser.
     */
    public NameParser getNameParser(Name name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getNameParser";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETNAMEPARSER,
                    new String[]{name.toString()},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getNameParser(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getNameParser Method.

    /**
     * Get Name Parser.
     */
    public NameParser getNameParser(String name)
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "getNameParser";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Debug Log Method.
        if (this.isDebugModeActive()) {
            FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                    METHODNAME,
                    MessageConstants.ICOSDIRCONTEXT_GETNAMEPARSER,
                    new String[]{name},
                    sw);
        } // End of DEBUG.

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    return super.getNameParser(name);
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);
        } // End of Execption and Final Processing.
    } // End of getNameParser Method.

    /**
     * Close the Context
     */
    public void close()
            throws NamingException {
        // ********************************
        // Initialize.
        String METHODNAME = "close";
        boolean METHOD_SUCCESSFUL = true; // Assume Successful.
        String METHOD_EXCEPTION_MSG = null;
        StopWatch sw = new StopWatch();
        sw.start();
        int rerunCount = 0;

        // ********************************
        // Begin Method.
        try {
            for (; ; ) { // Forever
                try {
                    super.close();
                    return;
                } catch (NamingException ne) {
                    try {
                        rerunCount = rerun(ne, rerunCount);
                    } catch (NamingException zne) {
                        METHOD_SUCCESSFUL = false;
                        METHOD_EXCEPTION_MSG = zne.getMessage();
                        throw zne;
                    } // End of Imbedded Catch. 
                } // End of Catch.
            } // End of For Loop.
        } finally {
            sw.stop();
            elapsed_context_life.setEnd();

            // ***************************
            // Show Method Finalization.
            this.logFinal(METHODNAME, METHOD_SUCCESSFUL, METHOD_EXCEPTION_MSG, rerunCount, sw);

            // ****************************
            // Show Final Close.
            FrameworkLogger.log(CLASS_NAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.ICOSDIRCONTEXT_CLOSING,
                    new String[]{elapsed_context_life.getElapsed()});
        } // End of Execption and Final Processing.
    } // End of Close Method.


    // ******************************************
    // Private Methods.
    // ******************************************

    /**
     * Format Search Controls for Logging Facilities.
     */
    private String formatSearchControls(SearchControls cons)
            throws NamingException {
        StringBuffer sb = new StringBuffer();
        if (cons == null) {
            return "none";
        }

        // ******************************
        // Format Scope
        sb.append(" Scope:[");
        switch (cons.getSearchScope()) {
            case SearchControls.OBJECT_SCOPE:
                sb.append("OBJECT");
                break;
            case SearchControls.ONELEVEL_SCOPE:
                sb.append("ONELEVEL");
                break;
            case SearchControls.SUBTREE_SCOPE:
                sb.append("SUBTREE");
                break;
            default:
                sb.append("UNKNOWN");
        } // End of Switch.
        sb.append("] ");

        // *****************************
        // Format Settings.
        if (cons.getCountLimit() != 0) {
            sb.append("Count Limit:[" + cons.getCountLimit() + "] ");
        }
        if (cons.getTimeLimit() != 0) {
            sb.append("Time Limit:[" + cons.getTimeLimit() + "] ");
        }
        if (cons.getDerefLinkFlag()) {
            sb.append("DerefLinkFlag:[" + new Boolean(cons.getDerefLinkFlag()).toString() + "] ");
        }
        if (cons.getReturningObjFlag()) {
            sb.append("ReturnObjFlag:[" + new Boolean(cons.getReturningObjFlag()).toString() + "] ");
        }

        // *******************************
        // Format Returning Attributes
        String[] ra = cons.getReturningAttributes();
        if (ra != null) {
            sb.append("Returning Attributes:[");
            for (int i = 0; i < ra.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(ra[i]);
            } // End of For Loop.
            sb.append("]");
        } // End of If not Null.
        // *********************
        // return Formatted data
        return sb.toString();
    } // End of formatSearchControls Private Method.

    /**
     * Format Object Arrays.
     */
    private String formatArray(Object[] array) {
        StringBuffer sb = new StringBuffer();
        // *******************************
        // Loop to Format Array.
        if (array != null) {
            sb.append("[");
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(array[i].toString());
            } // End of For Loop.
            sb.append("]");
        } // End of If not Null.
        // *********************
        // return Formatted data
        return sb.toString();
    } // End of formatSearchControls Private Method.

    /**
     * Format Search Filter for Logging Facilities.
     */
    private String formatSearchFilter(String filter)
            throws NamingException {
        if (filter == null) {
            return "none";
        }
        StringBuffer sb = new StringBuffer();

        // **********************
        // Format Filter for
        // Analysis.
        // TODO
        sb.append(filter);

        // *********************
        // return Formatted data
        return sb.toString();
    } // End of formatSearchFilter Private Method.

    /**
     * Format Modification Operation for Logging Facilities.
     */
    private String formatModOp(int mod_op) {
        StringBuffer sb = new StringBuffer();
        // *****************
        // Obtain a Name.
        switch (mod_op) {
            case DirContext.ADD_ATTRIBUTE:
                sb.append("ADD ATTR");
                break;
            case DirContext.REPLACE_ATTRIBUTE:
                sb.append("REP ATTR");
                break;
            case DirContext.REMOVE_ATTRIBUTE:
                sb.append("DEL ATTR");
                break;
            default:
                sb.append("UNKNOWN");
        } // End of Switch.

        // *********************
        // return Formatted data
        return sb.toString();
    } // End of formatModOp Private Method.

    /**
     * Format Modification Operation for Logging Facilities.
     */
    private String formatModItems(ModificationItem[] mods)
            throws NamingException {
        StringBuffer sb = new StringBuffer();
        if (mods == null) {
            return "none";
        }
        LDAPAttributeStatistic lax = new LDAPAttributeStatistic();

        // ****************************************************
        // Loop Through the Modification Array.
        sb.append(" [");
        for (int i = 0; i < mods.length; i++) {
            Attribute attr = (Attribute) mods[i].getAttribute();
            sb.append("Operation:[");
            sb.append(formatModOp(mods[i].getModificationOp()));
            sb.append("] ");
            sb.append(this.formatAttributeForLogging(attr));

            // ***************************
            // Accumulate Attribute Stats.
            this.saveAttributeStatistic(lax, attr);
        } // End of For Loop.
        sb.append("] ");

        // ************************************
        // Submit the Summary for Logging.
        sb.append("Attr Summary:[" + lax.toString() + "] ");

        // *********************
        // return Formatted data
        return sb.toString();
    } // End of formatModOp Private Method.

    /**
     * Format Attributes for Logging Facilities.
     */
    private String formatAttributesForLogging(Attributes entryattrs)
            throws NamingException {
        return formatAttributesForLogging(entryattrs, false);
    } // End of formatAttributesForLogging.

    /**
     * Format Attributes for Logging Facilities.
     */
    private String formatAttributesForLogging(Attributes entryattrs, boolean summary)
            throws NamingException {
        StringBuffer sb = new StringBuffer();
        if (entryattrs == null) {
            return "";
        }

        // ****************************
        // Get a Statistic Object for 
        // this Attribute Set Summary.
        LDAPAttributeStatistic lax = new LDAPAttributeStatistic();

        // *********************************
        // First Write out Objectclasses.
        Attribute eo = entryattrs.get(ObjectClassName);
        if (eo != null) {
            sb.append(eo.getID() + ":[");
            for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                sb.append(" " + eov.next());
            }
            sb.append("] ");
        } // End of check for null if.

        // *************************************
        // Finish Obtaining remaining Attributes,
        // in no special sequence.
        for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
            Attribute attr = (Attribute) ea.next();
            if (ObjectClassName.equalsIgnoreCase(attr.getID())) {
                continue;
            }
            // *********************************************
            // Format the Attribute for Logging.
            if (!summary) {
                sb.append(this.formatAttributeForLogging(attr));
            }

            // ****************************************
            // Roll the Statistic.
            this.saveAttributeStatistic(lax, attr);

        } // End of Outer For Loop

        // ************************************
        // Submit the Summary for Logging.
        if (summary) {
            sb.append("Attr Summary:[" + lax.toString() + "] ");
        }

        // ************************************
        // Return
        return sb.toString();
    } // End of formatAttributeForLogging    

    /**
     * Accumulate Statistics About our Attribute Set for Logging a Summary.
     */
    private void saveAttributeStatistic(LDAPAttributeStatistic lax, Attribute attr) {
        // *****************************
        // Check For Null.
        if (attr == null) {
            return;
        }

        // ***************************
        // Count the Attribute
        lax.TOTAL_NUMBER_OF_ATTRIBUTES++;

        // **********************************
        // Dip into Naming Enumeration to 
        // Obtain Attribute
        try {
            for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                String Aname = attr.getID();
                Object Aobject = ev.next();
                // **********************
                // Check for Null
                if (Aobject == null) {
                    continue;
                }

                // *************************
                // Check for Binary Data.
                else if (Aobject instanceof byte[]) {
                    lax.TOTAL_NUMBER_OF_BIN_ATTRIBUTES++;
                    lax.TOTAL_SIZE_OF_ALL_ATTRIBUTES =
                            lax.TOTAL_SIZE_OF_ALL_ATTRIBUTES + ((byte[]) Aobject).length;
                    if (((byte[]) Aobject).length > lax.BIN_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE) {
                        lax.BIN_ATTRIBUTE_NAME_WITH_LARGEST_SIZE = Aname;
                        lax.BIN_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE = ((byte[]) Aobject).length;
                    } // End of Check for MAX Size.
                } // End of Else If.

                // ******************
                // Check For String.
                else if (Aobject instanceof String) {
                    String clob = (String) Aobject;
                    lax.TOTAL_NUMBER_OF_STR_ATTRIBUTES++;
                    lax.TOTAL_SIZE_OF_ALL_ATTRIBUTES =
                            lax.TOTAL_SIZE_OF_ALL_ATTRIBUTES + clob.length();
                    if (clob.length() > lax.STR_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE) {
                        lax.STR_ATTRIBUTE_NAME_WITH_LARGEST_SIZE = Aname;
                        lax.STR_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE = clob.length();
                    } // End of Check for MAX Size.
                } // End of Else If.
                // ****************************
                // Ignore Anything Else.
                else {
                    lax.TOTAL_IGNORED_ATTRIBUTES++;
                }
            } // End of For Loop.
        } catch (NamingException ne) {
            // Ignore the Exception, since we are just Logging.
        } // End of Naming Exception.  
    } // End of saveAttributeStatistic

    /**
     * Format Attributes for Logging Facilities.
     */
    private String formatAttributeForLogging(Attribute attr)
            throws NamingException {
        StringBuffer sb = new StringBuffer();

        // *****************************
        // Check For Null.
        if (attr == null) {
            return "";
        }

        // **********************************
        // Dip into Naming Enumeration to
        // Obtain Attributes.
        sb.append(" [");
        for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
            sb.append(" [");
            String Aname = attr.getID();
            Aname = Aname.toLowerCase();
            Object Aobject = ev.next();
            if (Aname.startsWith(UserPasswordName)) {
                // *****************************
                // Show A trimmed Down Version.
                sb.append(UserPasswordName + ": " + "********");
                continue;
            } // End of Password Check.

            // *****************************
            // Show Other attributes.
            if (Aobject == null) {
                sb.append(attr.getID() +
                        ":");
            } else if (Aobject instanceof byte[]) {
                sb.append(attr.getID() +
                        ": [ Binary data " + ((byte[]) Aobject).length + " in length ]");
            } else if (Aobject instanceof String) {
                String blob = (String) Aobject;
                int obloblen = blob.length();
                if (blob.length() > 64) {
                    blob = blob.substring(0, 25) + " ...... " +
                            blob.substring((blob.length() - 25));
                    sb.append(attr.getID() +
                            ": " + blob + ", Full Length:[" + obloblen + "]");
                } // end of if.
                else {
                    sb.append(attr.getID() + ": " + blob);
                } // End of Inner Else.
            } // End of Else if
            else { // Just Show the Attribute Name a Class Name.
                sb.append(attr.getID() +
                        ": " + Aobject.getClass().getName());
            } // End of Else.
            sb.append("] ");
        } // End of For Loop.

        // ************************************
        // Return
        sb.append("] ");
        return sb.toString();
    } // End of formatAttributeForLogging    

    /**
     * logFinal Private Method to log all common finalizers.
     * Timing Log and Debug Level mode must be on to
     * invoke final logging.
     */
    private void logFinal(String METHODNAME, boolean METHOD_SUCCESSFUL,
                          String METHOD_EXCEPTION_MSG,
                          int rerunCount, StopWatch sw) {
        if ((this.isTimingModeActive()) &&
                (this.isDebugModeActive())) {
            // **********************************
            // Determine which Log to show based
            // upon success or failure.
            if (METHOD_SUCCESSFUL) {
                // Show Successful Result.
                FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                        METHODNAME,
                        MessageConstants.ICOSDIRCONTEXT_METHOD_ELAPSED_TIME,
                        new String[]{METHODNAME,
                                new Integer(rerunCount).toString()},
                        sw);
            } else {
                // Show Failure Result.
                FrameworkLogger.logTiming(DIRECTORY_TIMING_CATEGORY, CLASS_NAME,
                        METHODNAME,
                        MessageConstants.ICOSDIRCONTEXT_METHOD_ELAPSED_TIME_WITH_FAILURE,
                        new String[]{METHODNAME,
                                new Integer(rerunCount).toString(),
                                METHOD_EXCEPTION_MSG},
                        sw);
            } // End of Else.
        } // End of If check for Timing Mode.      
    } // End of logFinal Private Method to log all common finalizers.

    /**
     * Retry/Rerun Recovery Routines.
     */
    private int rerun(NamingException ne, int rerunCount)
            throws NamingException {

        // ***********************************
        // Initialize to determine root cause.
        String message = ne.getMessage();

        // ***********************************
        // Do we have a LDAP Server Busy Condition?
        if ((message.indexOf("[LDAP: error code 51") >= 0) ||
                (message.indexOf("[LDAP: error code 53") >= 0)) {

            // Are we out of retries?
            rerunCount++;
            if (rerunCount >= retryCount) {
                FrameworkLogger.log(CLASS_NAME, "rerun", FrameworkLoggerLevel.SEVERE,
                        MessageConstants.ICOSDIRCONTEXT_MAX_RETRY_EXHAUSTED,
                        new String[]{new Integer(rerunCount).toString(), message});
                throw ne;
            } // End of Check for Exhausting Retries.

            // *******************************
            // retry is longer for ldap 53s
            long retry = retrySleepMillis;
            if (message.indexOf("[LDAP: error code 53") >= 0) {
                retry = retry * 20;
            } // End of Specific check for LDAP 53.

            // ********************************
            // Log the Retry Number and Count 
            FrameworkLogger.log(CLASS_NAME, "rerun", FrameworkLoggerLevel.WARNING,
                    MessageConstants.ICOSDIRCONTEXT_RETRY_REQUEST,
                    new String[]{new Integer(rerunCount).toString(), new Integer(retryCount).toString(), message});

            // *************************
            // Sleep the Wait Interval.
            try {
                Thread.sleep(retry);
            } catch (Exception ignore) {
                // Ignore.
            } // End of Catch Expression.

            // *********************************                     
            // Return to allow Method to Retry.
            return rerunCount;
        } // End of Retry.

        // *******************************************************
        // Not an exception we handle, Throw the Error Condition.
        throw ne;
    } // End of rerun Private Method.
} ///:~ End of IcosDirContext Class.
