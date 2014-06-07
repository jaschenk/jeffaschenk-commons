package jeffaschenk.commons.frameworks.cnxidx.utility.pool;

import jeffaschenk.commons.exceptions.ResourceProviderException;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.DataMappingConstants;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.IcosDirContext;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

/**
 * This class provides access to a pool of DirContext resources for clients
 * executing outside of the Application Server's JVM and is
 * implemented as a singleton / delegator idiom.
 * <p> The ResourcePool uses several different properties from the
 * -Dconfigfile properties file.
 * <p/>
 * <p> The following parameters are used to connect to the LDAP server:<br>
 * <p> <li><b>LDAPHost</b> - The LDAP Server's address.  No default.
 * <li><b>LDAPPort</b> - The LDAP Server's port. Defaults to <i>389</i>
 * <li><b>LDAPAuthentication</b> - Defaults to <i>simple</i>
 * <li><b>LDAPUser</b> - The LDAP Server's admin account. No default.
 * <li><b>LDAPPassword</b> - The LDAP Server's admin password. No default.
 * <p> Notes: Notice, by giving default <i>(invalid)</i> values to
 * LDAPUser, and LDAPPassword - we are
 * imposing the requirement that the App and LDAP servers are
 * userid/password protected!
 */
class DirContextResourceProviderDC implements ResourceProvider, DataMappingConstants {

    /**
     * This class name used for debug purposes.
     */
    private static final String CLASS_NAME =
            DirContextResourceProviderDC.class.getName();

    /**
     * The LDAP directory url used to create a
     * DirContext object.
     */
    private String ldapFactory;

    /**
     * The LDAP directory url used to create a
     * DirContext object.
     */
    private String ldapHost;

    /**
     * The LDAP directory port used to create a
     * DirContext object.
     */
    private String ldapPort;

    /**
     * The LDAP directory authentication mechanism used to create a
     * DirContext object.
     */
    private String ldapAuthentication;

    /**
     * The LDAP directory user id used to create a
     * DirContext object.
     */
    private String ldapUser;

    private static int DEFAULT_RETRY_COUNT = 10;
    private static long DEFAULT_RETRY_SLEEP_MILLIS = 150;
    /**
     * Count of how many times to retry when ldap fails with a 51 (busy)
     */
    private int ldapRetryCount = DEFAULT_RETRY_COUNT;

    /**
     * How long to wait between retries when ldap fails with a 51 (busy)
     */
    private long ldapRetrySleepMillis = DEFAULT_RETRY_SLEEP_MILLIS;

    /**
     * The LDAP directory user password used to create a
     * DirContext object.
     */
    private String ldapPassword;

    /**
     * The search controls object to use in the isValid() method
     */
    private SearchControls isValidSrchCtrls = new SearchControls();

    /**
     * The No Attributes to be Returned, only a DN.
     */
    private String[] NO_Attributes = {"1.1"};

    /**
     * Default Constructor
     *
     * @param props Properties required for accessing the LDAP
     *              server and creating DirContexts.
     */
    public DirContextResourceProviderDC(Properties props)
            throws ResourceProviderException {

        loadFromProperties(props);

        isValidSrchCtrls.setSearchScope(SearchControls.OBJECT_SCOPE);
        isValidSrchCtrls.setReturningAttributes(NO_Attributes);
    } // End of Constructor

    /**
     * Creates a DirContext object.
     *
     * @param props Properties which are looked up and used to create
     *              the resource.
     * @return A resource.
     * @throws ResourceProviderException
     *          Thrown if the DirContext cannot be created.
     */
    public Object create(Properties props)
            throws ResourceProviderException {

        final String METHOD = "create(TypedProperties)";

        DirContext resource = null;

        try {
            resource = createDirContext();

        } catch (NamingException ne) {
            FrameworkLogger.log(CLASS_NAME, "create", FrameworkLoggerLevel.ERROR,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_CANT_CREATE_RESOURCE,
                    ne);
            throw new ResourceProviderException(MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_CANT_CREATE_RESOURCE,
                    ne);
        }

        return resource;
    }

    /**
     * Shuts down or closes the DirContext
     *
     * @param resource The DirContext object to destroy.
     * @throws ResourceProviderException
     *          Thrown if the resource provider fails.
     */
    public void destroy(Object resource)
            throws ResourceProviderException {
        final String METHOD = "destroy(resource)";

        try {
            if (resource != null)
                ((DirContext) resource).close();
        } catch (NamingException ne) {
            FrameworkLogger.log(CLASS_NAME, "destroy", FrameworkLoggerLevel.WARNING,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_CANT_CLOSE_RESOURCE,
                    ne);
            throw new ResourceProviderException(MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_CANT_CLOSE_RESOURCE,
                    ne);
        }
    }

    /**
     * Determines if the DirContext object is valid and can be used.
     * We check our DSA Entry, which for since is protected
     * we should not find anything but will indicate whether or
     * not we have a valid DirContext or not.
     *
     * @param resource The DirContext object to check
     * @return A boolean indicating validity
     */
    public boolean isValid(Object resource) {

        // ***************************************
        // Do we have a Context?
        if (resource == null) {
            return false;
        }

        // ***************************************
        // Is the Context Alive?
        try {
            ((DirContext) resource).search(ldapUser, OC_ALL_FILTER, isValidSrchCtrls);
            return true;
        } catch (NameNotFoundException nnfe) {
            // ********************************
            // Name not found condition will
            // Possible exist due to ACI
            // However, the context is hot and alive
            // So return we are ok.
            return true;
        } catch (Exception e) {
            // ********************************
            // Exception other than a not found
            // indicates we have a bad context.
            // Should perform RootCause Here.
            FrameworkLogger.log(CLASS_NAME, "isValid", FrameworkLoggerLevel.WARNING,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_RESOURCE_INVALID);
        } // End of Exception.

        return false;
    } // End of isValid Method.

    /**
     * Utility that creates a DirContext object.
     *
     * @return A DirContext object.
     * @throws javax.naming.NamingException Thrown if
     *                                      the DirContext object cannot be created.
     */
    private DirContext createDirContext() throws NamingException {

        // This is interesting code and an example that this method
        // was built from was provided by SilverStream support.

        final String METHOD = "getDirContext()";

        // Now - get an LDAP DirContext off of the Context...
        FrameworkLogger.log(CLASS_NAME, "createDirContext", FrameworkLoggerLevel.DEBUG,
                MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_GET_CONTEXT);
        try {
            Properties props = new Properties();

            // *********************************************************
            // Now add userid/password for ldap security
            // Notice how we are imposing the requirement that the LDAP
            // server be password protected here...
            //
            // TODO FIX this for SSL Protocol Support.
            //
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            props.setProperty(Context.PROVIDER_URL,
                    "ldap://" + ldapHost + ":" + ldapPort);
            props.setProperty(Context.SECURITY_AUTHENTICATION,
                    ldapAuthentication);
            props.setProperty(Context.SECURITY_PRINCIPAL, ldapUser);
            props.setProperty(Context.SECURITY_CREDENTIALS, ldapPassword);

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
            props.setProperty("java.naming.ldap.attributes.binary", BIN_ATTRIBUTE_NAMES);

            // *******************************************
            // Specify deleteRDN on a Rename.
            //
            // java.naming.ldap.deleterdn:
            // When a JNDI Rename occurs, take the default
            // and remove the RDN attribute of the old
            // naming attribute.
            //
            props.setProperty("java.naming.ldap.deleterdn", "true");

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
            props.setProperty("java.naming.ldap.derefAliases",
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
            props.setProperty("java.naming.batchsize", "0");

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
            props.setProperty("java.naming.referral",
                    System.getProperty("java.naming.referral", "ignore"));

            // *******************************************
            // Specify our Object Factories.
            //
            // java.naming.factory.object:
            // specified Classes must implement
            // the ObjectFactory or DirObjectFactory interface.
            //
            props.setProperty("java.naming.factory.object",
                    System.getProperty("java.naming.factory.object", ""));

            // *******************************************
            // Specify our State Factories.
            //
            // java.naming.factory.state:
            // specified Classes must implement
            // the StateFactory or DirStateFactory interface.
            //
            props.setProperty("java.naming.factory.state",
                    System.getProperty("java.naming.factory.state", ""));

            // *******************************************
            // Specify our Control Factories.
            //
            // java.naming.factory.control:
            // specified Classes must implement
            // the ControlFactory interface.
            //
            props.setProperty("java.naming.factory.control",
                    System.getProperty("java.naming.factory.control", ""));


            // *************************************
            // Return new IcosDirContext
            return new IcosDirContext(props, ldapRetryCount, ldapRetrySleepMillis);

        } catch (NamingException ne) {
            throw ne;
        }
    }

    // Initialize the properties by loading properties from the system
    // and the properties file.
    // Pay attention in here!
    private void loadFromProperties(Properties props)
            throws ResourceProviderException {

        final String METHOD = "loadFromProperties";

        String key = null;
        try {
            key = "LDAPHost";
            ldapHost = (String) props.get(key);
            key = "LDAPPort";
            ldapPort = (String) props.get(key);
            key = "LDAPAuthentication";
            ldapAuthentication = (String) props.get(key);
            key = "LDAPUser";
            ldapUser = (String) props.get(key);
            key = "LDAPPassword";
            ldapPassword = (String) props.get(key);
            try {
                key = "LDAPRetryCount";
                ldapRetryCount = Integer.parseInt((String)props.get(key));
                key = "LDAPRetrySleepMillis";
                ldapRetrySleepMillis = Long.parseLong((String)props.get(key));
            } catch (Exception ignore) {
                ldapRetryCount = DEFAULT_RETRY_COUNT;
                ldapRetrySleepMillis = DEFAULT_RETRY_SLEEP_MILLIS;
            }
        } catch (Exception ipe) {
            String[] arguments = new String[]{key};
            FrameworkLogger.log(CLASS_NAME, "loadFromProperties", FrameworkLoggerLevel.SEVERE,
                    MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_MISSING_PROPERTY,
                    arguments, ipe);
            throw new ResourceProviderException(MessageConstants.ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_MISSING_PROPERTY,
                    arguments, ipe);
        }
    }
}
