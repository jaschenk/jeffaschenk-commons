package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;

/**
 * Java Class which provides the common utility function to obtain an LDAP
 * Directory Context.  Used throughout the IRR utility and Admin Functions.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxManageContext implements DataMappingConstants, idxCMDReturnCodes {

    private Hashtable<String,String> irrenv = null;

    public IcosDirContext irrctx = null;

    private String IRRHost = null;
    private String IRRPrincipal = null;
    private String IRRCredentials = null;
    private String IRRSecurityMethod = null;
    private String IRRContextName = null;

    private boolean ExitOnException = false;

    private boolean Available = false;

    private boolean VERBOSE = false;

    private static String MP = "idxManageContext: ";

    private int retryCount;
    private long retrySleepMillis;

    private static final int DEFAULT_RETRY_COUNT = 10;
    private static final long DEFAULT_RETRY_SLEEP_MILLIS = 150;

    /**
     * Basic Contructor with Principal and Credentials.
     * The Standard Default Host of localhost will be used.
     *
     * @param _principal Principal (DN) to be used for Context.
     * @param _credentials Credentials (password) to be used for Context.
     */
    public idxManageContext(String _principal,
                            String _credentials) {

        // *******************************************
        // Initialize our Private Variables.
        IRRHost = "ldap://localhost";
        IRRPrincipal = _principal;
        IRRCredentials = _credentials;
        IRRSecurityMethod = "simple";
        IRRContextName = "IRRContext";

        // *******************************************
        // Initialize our Environment.
        setirrenv();

    } // End of idxManageContext Constructor.

    /**
     * Basic Contructor with LDAP Host, Principal and Credentials.
     *
     * @param _hosturl LDAP Host URL to be used for Context.
     * @param _principal Principal (DN) to be used for Context.
     * @param _credentials Credentials (password) to be used for Context.
     */
    public idxManageContext(String _hosturl,
                            String _principal,
                            String _credentials) {

        // *******************************************
        // Initialize our Private Variables.
        IRRHost = _hosturl;
        IRRPrincipal = _principal;
        IRRCredentials = _credentials;
        IRRSecurityMethod = "simple";
        IRRContextName = "IRRContext";

        // *******************************************
        // Initialize our Environment.
        setirrenv();

    } // End of idxManageContext Constructor.

    /**
     * Basic Contructor with LDAP Host, Principal and Credentials.
     *
     * @param _hosturl LDAP Host URL to be used for Context.
     * @param _principal Principal (DN) to be used for Context.
     * @param _credentials Credentials (password) to be used for Context.
     * @param _contextname Internal Context Name to be used for Context.
     */
    public idxManageContext(String _hosturl,
                            String _principal,
                            String _credentials,
                            String _contextname) {

        // *******************************************
        // Initialize our Private Variables.
        IRRHost = _hosturl;
        IRRPrincipal = _principal;
        IRRCredentials = _credentials;
        IRRSecurityMethod = "simple";
        IRRContextName = _contextname;

        // *******************************************
        // Initialize our Environment.
        setirrenv();

    } // End of idxManageContext Constructor.

    /**
     * Full Primary Contructor with all Options Specified.
     *
     * @param _hosturl LDAP Host URL to be used for Context.
     * @param _principal Principal (DN) to be used for Context.
     * @param _credentials Credentials (password) to be used for Context.
     * @param _securitymethod Security Method to be used for Context.
     * @param _contextname Internal Context Name for Message Display.
     */
    public idxManageContext(String _hosturl,
                            String _principal,
                            String _credentials,
                            String _securitymethod,
                            String _contextname) {

        // *******************************************
        // Initialize our Private Variables.
        IRRHost = _hosturl;
        IRRPrincipal = _principal;
        IRRCredentials = _credentials;
        IRRSecurityMethod = _securitymethod;
        IRRContextName = _contextname;

        // *******************************************
        // Initialize our Environment.
        setirrenv();

    } // End of idxManageContext Constructor.

    /**
     * Basic Contructor with properties Specified to obtain LDAP Host, Principal and Credentials.
     *
     * @param _props Properties To Obtain and load Variables.
     */
    public idxManageContext(Properties _props) {

        // *******************************************
        // Initialize our Private Variables.
        String key = "LDAPHost";
        IRRHost = _props.getProperty(key);
        if (!IRRHost.startsWith("ldap://")) {
            IRRHost = "ldap://" + IRRHost;
        }

        key = "LDAPPort";
        if (_props.containsKey(key)) {
            IRRHost = IRRHost + ":" + _props.getProperty(key);
        }

        key = "LDAPUser";
        IRRPrincipal = _props.getProperty(key);

        key = "LDAPPassword";
        IRRCredentials = _props.getProperty(key);

        IRRSecurityMethod = "simple";
        IRRContextName = "IRRContext";

        // *******************************************
        // Initialize our Environment for the Context.
        setirrenv();

    } // end of idxManageContext Constructor.

    /**
     * Basic Contructor with properties Specified to obtain LDAP Host, Principal and Credentials.
     *
     * @param _props To Obtain and load Variables.
     * @param _ContextName     Context Name.
     */
    public idxManageContext(Properties _props,
                            String _ContextName) {

        // *******************************************
        // Initialize our Private Variables.
        String key = "LDAPHost";
        IRRHost = _props.getProperty(key);
        if (!IRRHost.startsWith("ldap://")) {
            IRRHost = "ldap://" + IRRHost;
        }

        key = "LDAPPort";
        if (_props.containsKey(key)) {
            IRRHost = IRRHost + ":" + _props.getProperty(key);
        }

        key = "LDAPUser";
        IRRPrincipal = _props.getProperty(key);

        key = "LDAPPassword";
        IRRCredentials = _props.getProperty(key);

        IRRSecurityMethod = "simple";
        IRRContextName = _ContextName;

        // *******************************************
        // Initialize our Environment for the Context.
        setirrenv();

    } // End of idxManageContext Constructor.

    // ***************************************************
    // TODO Apply Change for SSL Protocol Support.
    // Create a new Constructor that will create an
    // SSL socket to the correct SSL socket on the
    // Directory.
    // ***************************************************

    /**
     * setExitOnException method to set ExitOnException boolean indicator.
     *
     * @param _flag Indicator value to set ExitOnException.
     */
    public void setExitOnException(boolean _flag) {
        ExitOnException = _flag;
    } // End of setExitOnException.

    /**
     * isExitOnException method to return ExitOnException boolean indicator.
     *
     * @return boolean current setting of ExitOnException.
     */
    public boolean isExitOnException() {
        return (ExitOnException);
    } // End of isExitOnException.

    /**
     * isAvailable method to return Available boolean indicator.
     *
     * @return boolean current setting of Available.
     */
    public boolean isAvailable() {
        return (Available);
    } // End of isAvailable.

    /**
     * setVerbose method to set VERBOSE boolean indicator.
     *
     * @param _flag Indicator value to set VERBOSE.
     */
    public void setVerbose(boolean _flag) {
        VERBOSE = _flag;
    } // End of setVerbose.

    /**
     * setRetryCount method to set RetryCount.
     *
     * @param _retryCount RetryCount.
     */
    public void setRetryCount(int _retryCount) {
        retryCount = _retryCount;
    } // End of setRetryCount.

    /**
     * setRetryWaitTime method to set WaitTime.
     *
     * @param _WaitTime WaitTime Milliseconds.
     */
    public void setRetryWaitTime(long _WaitTime) {
        retrySleepMillis = _WaitTime;
    } // End of setRetryCount.

    /**
     * setirrenv method to initialize and set environment properties.
     */
    private void setirrenv() {

        // *******************************************
        // Indicate the Context is not Available yet.
        Available = false;

        // *******************************************
        // Assume No Retries.
        retryCount = 0;
        retrySleepMillis = 0;

        // *******************************************
        // Assume we will not exit on Exception, let
        // Object Owner perform Exception.
        setExitOnException(false);

        // *******************************************
        // Acquire System Properties.
        irrenv = new Hashtable<>();

        // *******************************************
        // Initialize Environment Properties.
        irrenv.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");

        irrenv.put(Context.PROVIDER_URL,
                IRRHost);

        irrenv.put(Context.SECURITY_PRINCIPAL,
                IRRPrincipal);

        irrenv.put(Context.SECURITY_AUTHENTICATION,
                IRRSecurityMethod);

        irrenv.put(Context.SECURITY_CREDENTIALS,
                IRRCredentials);

        // *******************************************
        // Initialize Additional LDAP Properties.

        // *******************************************
        // Specify any non-String Attributes not in
        // Standard supplied JNDI specification.
        // The property must be in sync with
        // idxIRRSchema class as well as our
        // Directory Schema.
        //
        // Specify Each attribute deliminted by a space.
        // See DataMappingContants.
        //
        irrenv.put("java.naming.ldap.attributes.binary",
                BIN_ATTRIBUTE_NAMES);

        // *******************************************
        // Specify LDAP Version 3 Protocol.
        // This is the default for JNDI.
        irrenv.put("java.naming.ldap.version",
                "3");

        // *******************************************
        // Specify deleteRDN on a Rename.
        //
        // java.naming.ldap.deleterdn:
        // When a JNDI Rename occurs, take the default
        // and remove the RDN attribute of the old
        // naming attribute.
        //
        irrenv.put("java.naming.ldap.deleterdn", "true");

        // *******************************************
        // Specify our DeReferencing Alias Property.
        //
        // java.naming.ldap.derefAliases:
        // Specifies DeReferencing Rule:
        // always    = Always Dereference Aliases, DEFAULT.
        // never     = Never Dereference Aliases.
        // finding   = Dereferences Aliases only during name resolution.
        // searching = Dereferences Aliases only after name resolution.
        //
        irrenv.put("java.naming.ldap.derefAliases",
                System.getProperty("java.naming.ldap.derefAliases", "always"));


        // *******************************************
        // Initialize Additional Naming Properties.

        // *******************************************
        // Specify BatchSize.
        //
        // java.naming.batchsize (Context.BATCHSIZE):
        // Sets the recommended size limit for the number of search results
        // held by a returned NamingEnumeration.
        // If not specified then the default batchsize is 1.
        // This helps to ensure the smallest memory footprint possible
        // by the class library.
        // A value of 0 disables batchsize and indicates that a
        // search will block until all results are collected.
        //
        irrenv.put("java.naming.batchsize", "0");

        // *******************************************
        // Specify REFERRAL.
        //
        // java.naming.referral (Context.REFERRAL):
        // Sets how the application will handle REFERRALS
        // when provided from the Directory Server.
        //
        // Since the DCL Directory implementation
        // specifies that REFERRALS will be chained,
        // then if we receive a referral, there is a
        // potential that a Realm or subordinate
        // Directory instance is not running or down.
        //
        // If we set this to "follow", and a
        // Directory Instance is down, then response
        // time for all Search operations will be
        // very long.
        //
        // Currently this is set to Ignore.
        // Meaning, if a REFERRAL is received from
        // a request, than the REFERRAL Entity is
        // Simple Ignored and normal processing will
        // continue.
        // We will rely on the DCL Directory Implementation
        // to provide automatic following of the
        // Referrals, before we get information returned
        // to us.
        //
        // Otherwise the default is to throw the
        // Exception which is not correct in our
        // environment.
        // Another option is to "follow" the
        // Referral, but in our case if a Referral
        // is received we know a Directory Instance
        // is down.
        //
        irrenv.put("java.naming.referral",
                System.getProperty("java.naming.referral", "ignore"));

        // *******************************************
        // Specify our Object Factories.
        //
        // java.naming.factory.object:
        // specified Classes must implement
        // the ObjectFactory or DirObjectFactory interface.
        //
        irrenv.put("java.naming.factory.object",
                System.getProperty("java.naming.factory.object", ""));

        // *******************************************
        // Specify our State Factories.
        //
        // java.naming.factory.state:
        // specified Classes must implement
        // the StateFactory or DirStateFactory interface.
        //
        irrenv.put("java.naming.factory.state",
                System.getProperty("java.naming.factory.state", ""));

        // *******************************************
        // Specify our Control Factories.
        //
        // java.naming.factory.control:
        // specified Classes must implement
        // the ControlFactory interface.
        //
        irrenv.put("java.naming.factory.control",
                System.getProperty("java.naming.factory.control", ""));

        // **************************************************
        // Obtain Other Parameters from the Incoming System
        // Properties.
        try {
            retryCount = Integer.parseInt(
                    System.getProperty("user.dcl.dcd.retrycount", "0"));
        } catch (Exception e) {
            retryCount = DEFAULT_RETRY_COUNT;
        } // End of Exception.

        try {
            retrySleepMillis = Integer.parseInt(
                    System.getProperty("user.dcl.dcd.retrysleepmillis", "0"));
        } catch (Exception e) {
            retrySleepMillis = DEFAULT_RETRY_SLEEP_MILLIS;
        } // End of Exception.

        if (retryCount == 0) {
            retryCount = DEFAULT_RETRY_COUNT;
        }
        if (retrySleepMillis == 0) {
            retrySleepMillis = DEFAULT_RETRY_SLEEP_MILLIS;
        }

    } // End of setirrenv Method.

    /**
     * open method to initialize and obtain new IRR Directory Context.
     *
     * @throws idxIRRException if unable to obtain Context.
     */
    public void open() throws idxIRRException {

        // ***************************************************
        // Make sure we do not already have a context.
        if ((irrctx != null) ||
                (Available)) {
            throw new idxIRRException(MP + "IRR Context Already exists or Available for " +
                    IRRContextName + ".");
        } // End of If.
        // ***************************************************
        // Try and Obtain a new Context.
        try {
            // *******************************************
            // Obtain a new Directory Context.
            //irrctx = new InitialDirContext(irrenv);
            irrctx = new IcosDirContext(irrenv,
                    retryCount,
                    retrySleepMillis);
            Available = true; // Indicate Available.

        } catch (javax.naming.CommunicationException e) {
            // ********************************************
            // Ok, this is way to generic, let us get to
            // Root Cause.
            String rootcause = getRootCause(e, VERBOSE);
            if (rootcause.indexOf("java.net.connectexception: connection refused") >= 0) {
                if (ExitOnException) {
                    System.err.println(MP + "IRR Connection Refused to Host " +
                            IRRHost + " for " +
                            IRRContextName +
                            " Directory Context.");
                    System.exit(EXIT_IRR_CONNECTION_REFUSED);
                } else {
                    throw new idxIRRException(MP + "IRR Connection Refused to Host " +
                            IRRHost + " for " +
                            IRRContextName +
                            " Directory Context.");
                } // end of ExitOnException Else
            } // End of If.
            else if (rootcause.indexOf("java.net.unknownhostexception") >= 0) {
                if (ExitOnException) {
                    System.err.println(MP + "IRR Unknown Host " +
                            IRRHost + " specified for " +
                            IRRContextName +
                            " Directory Context.");
                    System.exit(EXIT_IRR_UNKNOWN_HOST);
                } else {
                    throw new idxIRRException("");
                } // end of ExitOnException Else
            } // End of Else if.
            else {
                if (ExitOnException) {
                    System.err.println(MP + "IRR Communication Exception to Host " +
                            IRRHost + " specified for " +
                            IRRContextName +
                            " Directory Context," + e);
                    System.exit(EXIT_IRR_GENERIC_COMMUNICATION_EXCEPTION);
                } else {
                    e.printStackTrace();
                    throw new idxIRRException(MP + "IRR Communication Exception to Host " +
                            IRRHost + " specified for " +
                            IRRContextName +
                            " Directory Context," + e);
                } // end of ExitOnException Else
            } // End of Else.
        } catch (javax.naming.AuthenticationException e) {
            // *******************************************
            // Can not Authenticate.
            if (ExitOnException) {
                System.err.println(MP + "IRR Unable to Authenticate to Host " +
                        IRRHost + " specified for " +
                        IRRContextName +
                        " Directory Context.");
                System.exit(EXIT_IRR_UNABLE_TO_AUTHENTICATE);
            } else {
                throw new idxIRRException(MP + "IRR Unable to Authenticate to Host " +
                        IRRHost + " specified for " +
                        IRRContextName +
                        " Directory Context.");
            } // end of ExitOnException Else
        } catch (Exception e) {
            // *******************************************
            // Something Else Happened.
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception Obtaining Context for Host " +
                        IRRHost + " specified for " +
                        IRRContextName +
                        " Directory Context.\n" + e);
                System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
            } else {
                throw new idxIRRException(MP + "IRR Exception Obtaining Context for Host " +
                        IRRHost + " specified for " +
                        IRRContextName +
                        " Directory Context. " + e);
            } // end of ExitOnException Else
        } // End of exception processing.

    } // End of open Method.


    /**
     * close method to close existing IRR Directory Context.
     *
     * @throws idxIRRException if unable to obtain Context.
     */
    public void close() throws idxIRRException {

        // ***************************************************
        // Make sure we already have a context.
        if ((irrctx == null) ||
                (!Available) ||
                (!(irrctx instanceof IcosDirContext))) {
            throw new idxIRRException(MP + "IRR Context does not exist or Available for " +
                    IRRContextName + ".");
        } // End of If.

        // ***************************************************
        // Perform the Close.
        try {
            ((IcosDirContext) irrctx).close();
        } catch (Exception e) {
            // *******************************************
            // Something Else Happened.
            if (ExitOnException) {
                System.err.println(MP + "IRR Exception Closing Context for Host " +
                        IRRHost + " specified for " +
                        IRRContextName +
                        " Directory Context.\n" + e);
                System.exit(EXIT_IRR_CLOSE_FAILURE);
            } else {
                throw new idxIRRException(MP + "IRR Exception Closing Context for Host " +
                        IRRHost + " specified for " +
                        IRRContextName +
                        " Directory Context. " + e);
            } // end of ExitOnException Else
        } // End of exception

        Available = false; // Indicate is not Available.
        irrctx = null;     // Finalize the Context.
    } // End of close method.

    /**
     * disableAliasDereferencing method to override default or obtained
     * property to NEVER Dereference Alias Entries.
     *
     * @throws idxIRRException if unable to perform.
     */
    public void disableAliasDereferencing() throws idxIRRException {

        // ***************************************************
        // Make sure we already have a context.
        if ((irrctx == null) ||
                (!Available) ||
                (!(irrctx instanceof IcosDirContext))) {
            throw new idxIRRException(MP + "IRR Context does not exist or Available for " +
                    IRRContextName + ".");
        } // End of If.

        // *****************************************
        // Set the dereference alias property to
        // NEVER!
        // If the property already exists this will
        // Overwrite it.
        try {
            irrctx.addToEnvironment("java.naming.ldap.derefAliases", "never");
        } catch (Exception e) {
            throw new idxIRRException(MP + "IRR Context Exception within disableAliasDereferencing, for " +
                    IRRContextName + ", " + e);
        } // End of Exception.

    } // End of disableAliasDeferencing method.

    /**
     * enableAliasDereferencing method to override default or obtained
     * property to ALWAYS Dereference Alias Entries.
     *
     * @throws idxIRRException if unable to perform.
     */
    public void enableAliasDereferencing() throws idxIRRException {

        // ***************************************************
        // Make sure we already have a context.
        if ((irrctx == null) ||
                (!Available) ||
                (!(irrctx instanceof IcosDirContext))) {
            throw new idxIRRException(MP + "IRR Context does not exist or Available for " +
                    IRRContextName + ".");
        } // End of If.

        // *****************************************
        // Set the dereference alias property to
        // NEVER!
        // If the property already exists this will
        // Overwrite it.
        try {
            irrctx.addToEnvironment("java.naming.ldap.derefAliases", "always");
        } catch (Exception e) {
            throw new idxIRRException(MP + "IRR Context Exception within enableAliasDereferencing, for " +
                    IRRContextName + ", " + e);
        } // End of Exception.

    } // End of enableAliasDeferencing method.

    /**
     * showAliasDereferencing method to simple show current
     * property of Dereference Alias Entries.
     *
     * @throws idxIRRException if unable to perform.
     */
    public void showAliasDereferencing() throws idxIRRException {

        // ***************************************************
        // Make sure we already have a context.
        if ((irrctx == null) ||
                (!Available) ||
                (!(irrctx instanceof IcosDirContext))) {
            throw new idxIRRException(MP + "IRR Context does not exist or Available for " +
                    IRRContextName + ".");
        } // End of If.

        // *****************************************
        // Show the dereference alias property.
        try {
            Hashtable IRRproperties = irrctx.getEnvironment();
            String _derefAliases = ((String) IRRproperties.get("java.naming.ldap.derefAliases"));
            if ((_derefAliases == null) ||
                    (_derefAliases.equals(""))) {
                _derefAliases = "always";
            }

            // **********************************
            // Output to Standard Output the
            // Setting.
            System.out.println(MP + "IRR Dereferenced Aliases Setting for Host:[" +
                    IRRHost +
                    "] Context Name:[" +
                    IRRContextName +
                    "] is set to:[" +
                    _derefAliases + "].");

        } catch (Exception e) {
            throw new idxIRRException(MP + "IRR Context Exception within showAliasDereferencing, for " +
                    IRRContextName + ", " + e);
        } // End of Exception.

    } // End of showAliasDeferencing method.

    /**
     * disableDSAEFactories method to disable the DSAE Object and State
     * factories.
     *
     * @throws idxIRRException if unable to perform.
     */
    public void disableDSAEFactories() throws idxIRRException {

        // ***************************************************
        // Make sure we already have a context.
        if ((irrctx == null) ||
                (!Available) ||
                (!(irrctx instanceof IcosDirContext))) {
            throw new idxIRRException(MP + "IRR Context does not exist or Available for " +
                    IRRContextName + ".");
        } // End of If.

        // ************************************************
        // Remove the State and Object Factory Properties.
        try {
            Hashtable env = irrctx.getEnvironment();
            if (env.containsKey(Context.OBJECT_FACTORIES)) {
                irrctx.removeFromEnvironment(Context.OBJECT_FACTORIES);
            }
            if (env.containsKey(Context.STATE_FACTORIES)) {
                irrctx.removeFromEnvironment(Context.STATE_FACTORIES);
            }
        } catch (Exception e) {
            throw new idxIRRException(MP + "IRR Context Exception within disableDSAEFactories, for " +
                    IRRContextName + ", " + e);
        } // End of Exception.

    } // End of disableDSAEFactories method.

    /**
     * enableDSAEFactories method to Enable the use of the DSAE Object and
     * State Factories.
     *
     * @throws idxIRRException if unable to perform.
     */
    public void enableDSAEFactories() throws idxIRRException {

        // ***************************************************
        // Make sure we already have a context.
        if ((irrctx == null) ||
                (!Available) ||
                (!(irrctx instanceof IcosDirContext))) {
            throw new idxIRRException(MP + "IRR Context does not exist or Available for " +
                    IRRContextName + ".");
        } // End of If.

        // *****************************************
        // Set the State and Object Factories.
        // If the property already exists this will
        // Overwrite it.
        /**
        try {
            irrctx.addToEnvironment(Context.STATE_FACTORIES, FRAMEWORK_DEFAULT_STATE_FACTORY);
            irrctx.addToEnvironment(Context.OBJECT_FACTORIES, FRAMEWORK_DEFAULT_OBJECT_FACTORY);
        } catch (Exception e) {
            throw new idxIRRException(MP + "IRR Context Exception within enableDSAEFactories, for " +
                    IRRContextName + ", " + e);
        } // End of Exception.
        **/
    } // End of enableDSAEFactories method.

    /**
     * showDSAEFactories method to simple show current
     * property of DSAE Object and State Factories.
     *
     * @throws idxIRRException if unable to perform.
     */
    public void showDSAEFactories() throws idxIRRException {

        // ***************************************************
        // Make sure we already have a context.
        if ((irrctx == null) ||
                (!Available) ||
                (!(irrctx instanceof IcosDirContext))) {
            throw new idxIRRException(MP + "IRR Context does not exist or Available for " +
                    IRRContextName + ".");
        } // End of If.

        // *****************************************
        // Show the JNDI Factory Settings.
        try {
            Hashtable env = irrctx.getEnvironment();

            String OBJECT_FACTORY = "NOT SET";
            if (env.containsKey(Context.OBJECT_FACTORIES)) {
                OBJECT_FACTORY = (String) env.get(Context.OBJECT_FACTORIES);
            }

            String STATE_FACTORY = "NOT SET";
            if (env.containsKey(Context.STATE_FACTORIES)) {
                STATE_FACTORY = (String) env.get(Context.STATE_FACTORIES);
            }

            // **********************************
            // Output to Standard Output the
            // Setting.
            System.out.println(MP + "IRR Factory Settings for Host:[" +
                    IRRHost +
                    "] Context Name:[" +
                    IRRContextName +
                    "] are:");
            System.out.println("\tObject Factories:[" + OBJECT_FACTORY + "]");
            System.out.println("\tState Factories:[" + STATE_FACTORY + "]");

        } catch (Exception e) {
            throw new idxIRRException(MP + "IRR Context Exception within showDSAEFactories, for " +
                    IRRContextName + ", " + e);
        } // End of Exception.

    } // End of showDSAEFactories method.


    /**
     * getRootCause method to obtain root cause within an Exception.
     *
     * @param e Object
     * @param _showflag   Flag to indicate if Root Cause should be displayed.
     * @return String of Lower Case Normalized Root Cause.
     */
    private String getRootCause(Object e,
                                boolean _showflag) {

        String rootcause = e.toString();
        rootcause = rootcause.toLowerCase();
        if ((rootcause == null) || ("".equals(rootcause))) {
            return ("None");
        }
        int rci = rootcause.indexOf("[root exception is");
        if (rci >= 0) {
            rootcause = rootcause.substring(rci);
        } else {
            return ("None");
        }

        if (_showflag) {
            System.err.println(MP +
                    "Root Cause Found:" + rootcause);
        } // End of If.

        return (rootcause);
    } // End of getRootCause Method.

} ///:~ End of idxManageContext Class.
