package jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments;

import jeffaschenk.commons.frameworks.cnxidx.utility.security.FrameworkDirectoryUser;

/**
 * Java Class to provide a wrapper for Command Line Principal and Credentials fir
 * command line utilities and other executables providing either direct specification
 * of principal/credential pair or specification of an Alias to extract the
 * principal/crendtials from the FRAMEWORK KeyStore.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2002
 */

public class CommandLinePrincipalCredentials {

    // ***********************************************
    // Logging Facilities.
    public static final String CLASSNAME = CommandLinePrincipalCredentials.class.getName();

    // *******************************************
    // Static Source Indicators. 
    public static final int NOT_OBTAINED = -1;
    public static final String NOT_OBTAINED_NAME = "";

    public static final int OBTAINED_FROM_UNKNOWN_SOURCE = 0;
    public static final String OBTAINED_FROM_UNKNOWN_SOURCE_NAME = "UNKNOWN";

    public static final int OBTAINED_FROM_KEYSTORE = 1;
    public static final String OBTAINED_FROM_KEYSTORE_NAME = "KEYSTORE";

    public static final int OBTAINED_FROM_COMMAND_OPTION = 2;
    public static final String OBTAINED_FROM_COMMAND_OPTION_NAME = "COMMANDOPTION";

    public static final int OBTAINED_FROM_PROPERTY = 3;
    public static final String OBTAINED_FROM_PROPERTY_NAME = "PROPERTY";

    // *******************************************
    // Internal Indicators. 
    private boolean OBTAINED = false;
    private boolean DEST_OBTAINED = false;

    // *******************************************
    // Internal Values. 
    private String IRRPrincipal = "";
    private String IRRCredentials = "";
    private String IDUAlias = "";

    private int SOURCE = NOT_OBTAINED;
    private String SOURCE_NAME = NOT_OBTAINED_NAME;

    // *******************************************
    // Internal Destination Values. 
    private String DEST_IRRPrincipal = "";
    private String DEST_IRRCredentials = "";
    private String DEST_IDUAlias = "";

    private int DEST_SOURCE = NOT_OBTAINED;
    private String DEST_SOURCE_NAME = NOT_OBTAINED_NAME;

    /**
     * Initial Constructor used when no argument supplied.
     */
    public CommandLinePrincipalCredentials() {
        // ******************************
        // Obtain the information from
        // System Properties.
        obtainProperties();
        obtainDESTProperties();
    } // end of Constructor

    /**
     * Constructor used when a Argument Parser supplied.
     *
     * @param _iap Object Containing Parsed Command Line Options.
     */
    public CommandLinePrincipalCredentials(idxArgParser _iap) {
        // ******************************
        // Obtain the information from
        // the Argument Parser.
        extractFromArgParser(_iap);
    } // end of Constructor.

    /**
     * Constructor used when a String value of Keystore Alias supplied.
     *
     * @param _iduAliasName Assumed name of Keystore Alias.
     */
    public CommandLinePrincipalCredentials(String _iduAliasName) {
        // ***************************************
        // Was the alias specified?
        if ((_iduAliasName == null) ||
                (_iduAliasName.trim().equals(""))) {
            return;
        }

        // ***************************************
        // Obtain the Local IDU Information.
        IDUAlias = _iduAliasName.trim();
        obtainIDU();
    } // end of Constructor.

    /**
     * Constructor used when a String value of Keystore Alias supplied.
     *
     * @param _iduAliasName Assumed name of Keystore Alias.
     * @param _dest         Indicator true for Destination sources.
     */
    public CommandLinePrincipalCredentials(String _iduAliasName, boolean _dest) {

        // ***************************************
        // Was the alias specified?
        if ((_iduAliasName == null) ||
                (_iduAliasName.trim().equals(""))) {
            return;
        }

        // ***************************************
        // Obtain the Local IDU Information.
        if (!_dest) {
            // **********************************
            // Obtain.
            IDUAlias = _iduAliasName.trim();
            obtainIDU();
        } else {
            // **********************************
            // Obtain.
            DEST_IDUAlias = _iduAliasName.trim();
            obtainDESTIDU();
        } // End of Else.
    } // end of Constructor.

    /**
     * Method to obtain information from the Argument Parser.
     *
     * @param _iap Parsed Command Line Arguments.
     */
    public void extractFromArgParser(idxArgParser _iap) {

        // ****************************************
        // Obtain the Local Principal/Credentials
        //
        if ((_iap.doesNameExist("idu")) &&
                (!OBTAINED)) {
            // **********************************
            // Obtain.
            IDUAlias = ((String) _iap.getValue("idu")).trim();
            obtainIDU();
        } // End of If.

        // ******************************************
        // Both IRRID and IRRPW must Exist to be
        // used from command line option.
        else if ((_iap.doesNameExist("irrid")) &&
                (_iap.doesNameExist("irrpw")) &&
                (!OBTAINED)) {
            // **********************************
            // Obtain.
            IRRPrincipal = ((String) _iap.getValue("irrid")).trim();
            IRRCredentials = ((String) _iap.getValue("irrpw")).trim();

            // **********************************
            // Specify Source for Obtaining.
            SOURCE = OBTAINED_FROM_COMMAND_OPTION;
            SOURCE_NAME = OBTAINED_FROM_COMMAND_OPTION_NAME;

            // **********************************
            // Indicate Obtained.
            OBTAINED = true;

        } // End of If.

        // ******************************************
        // If not obtained, try from them in a
        // system property.
        if (!OBTAINED) {
            obtainProperties();
        }

        // *********************************************
        // Obtain the Destination Principal/Credentials
        //
        if ((_iap.doesNameExist("destidu")) &&
                (!DEST_OBTAINED)) {
            // **********************************
            // Obtain.
            DEST_IDUAlias = ((String) _iap.getValue("destidu")).trim();
            obtainDESTIDU();
        } // End of If.

        // ******************************************
        // Both IRRID and IRRPW must Exist to be
        // used from command line option.
        else if ((_iap.doesNameExist("destirrid")) &&
                (_iap.doesNameExist("destirrpw")) &&
                (!DEST_OBTAINED)) {
            // **********************************
            // Obtain.
            DEST_IRRPrincipal = ((String) _iap.getValue("destirrid")).trim();
            DEST_IRRCredentials = ((String) _iap.getValue("destirrpw")).trim();

            // **********************************
            // Specify Source for Obtaining.
            DEST_SOURCE = OBTAINED_FROM_COMMAND_OPTION;
            DEST_SOURCE_NAME = OBTAINED_FROM_COMMAND_OPTION_NAME;

            // **********************************
            // Indicate Obtained.
            DEST_OBTAINED = true;

        } // End of If.

        // ******************************************
        // If not obtained, try from them in a
        // system property.
        if (!DEST_OBTAINED) {
            obtainDESTProperties();
        }

    } // end of extractFromargParser Method

    /**
     * getPrincipal, return the local Principal.
     *
     * @return String of local Principal.
     */
    public String getPrincipal() {
        if (OBTAINED) {
            return (IRRPrincipal);
        } else {
            return ("");
        }
    } // End of getPrincipal Method.

    /**
     * getCredentials, return the local Credentials.
     *
     * @return String of local Credentials.
     */
    public String getCredentials() {
        if (OBTAINED) {
            return (IRRCredentials);
        } else {
            return ("");
        }
    } // End of getCredentials Method.

    /**
     * getAlias, return the local IDUAlias.
     *
     * @return String of local IDUAlias.
     */
    public String getAlias() {
        if (OBTAINED) {
            return (IDUAlias);
        } else {
            return ("");
        }
    } // End of getAlias Method.

    /**
     * getSourceName, return the local Source Name.
     *
     * @return String of local Source Name.
     */
    public String getSourceName() {
        return (SOURCE_NAME);
    } // End of getSourceName Method.

    /**
     * getSource, return the local Source.
     *
     * @return int of local Source.
     */
    public int getSource() {
        return (SOURCE);
    } // End of getSource Method.

    /**
     * wasObtained, return indicator if local Source Obtained.
     *
     * @return boolean of Obtained indicator.
     */
    public boolean wasObtained() {
        return (OBTAINED);
    } // End of isObtained Method.


    /**
     * getDestPrincipal, return the local Principal.
     *
     * @return String of Dest Principal.
     */
    public String getDestPrincipal() {
        if (DEST_OBTAINED) {
            return (DEST_IRRPrincipal);
        } else {
            return ("");
        }
    } // End of getDestPrincipal Method.

    /**
     * getDestCredentials, return the Dest Credentials.
     *
     * @return String of Dest Credentials.
     */
    public String getDestCredentials() {
        if (DEST_OBTAINED) {
            return (DEST_IRRCredentials);
        } else {
            return ("");
        }
    } // End of getDestCredentials Method.

    /**
     * getDestAlias, return the Dest IDUAlias.
     *
     * @return String of Dest IDUAlias.
     */
    public String getDestAlias() {
        if (DEST_OBTAINED) {
            return (DEST_IDUAlias);
        } else {
            return ("");
        }
    } // End of getAlias Method.

    /**
     * getDestSourceName, return the Dest Source Name.
     *
     * @return String of Dest Source Name.
     */
    public String getDestSourceName() {
        return (DEST_SOURCE_NAME);
    } // End of DestSourceName Method.

    /**
     * getDestSource, return the Dest Source.
     *
     * @return int of Dest Source.
     */
    public int getDestSource() {
        return (DEST_SOURCE);
    } // End of getDestSource Method.

    /**
     * wasDestObtained, return indicator if local Source Obtained.
     *
     * @return boolean of Obtained indicator.
     */
    public boolean wasDestObtained() {
        return (DEST_OBTAINED);
    } // End of isDestObtained Method.

    /**
     * Obtained the Local Principal/Credentials
     * for an IDU Alias.
     */
    private void obtainIDU() {
        // **************************************
        // Obtain the Principal and Credentials
        try {
            FrameworkDirectoryUser idu = new FrameworkDirectoryUser(IDUAlias);
            IRRPrincipal = idu.getDN();
            IRRCredentials = idu.getPassword();
        } catch (Exception e) {
            IDUAlias = "";
            return;
        } // End of Exception.

        // **********************************
        // Specify Source for Obtaining.
        SOURCE = OBTAINED_FROM_KEYSTORE;
        SOURCE_NAME = OBTAINED_FROM_KEYSTORE_NAME;

        // **********************************
        // Indicate Obtained.
        OBTAINED = true;
    } // end of obtainIDU Method

    /**
     * Obtained the Local Principal/Credentials
     * for an IDU Alias.
     */
    private void obtainDESTIDU() {
        // **************************************
        // Obtain the Principal and Credentials
        try {
            FrameworkDirectoryUser idu = new FrameworkDirectoryUser(DEST_IDUAlias);
            DEST_IRRPrincipal = idu.getDN();
            DEST_IRRCredentials = idu.getPassword();
        } catch (Exception e) {
            DEST_IDUAlias = "";
            return;
        } // End of Exception.

        // **********************************
        // Specify Source for Obtaining.
        DEST_SOURCE = OBTAINED_FROM_KEYSTORE;
        DEST_SOURCE_NAME = OBTAINED_FROM_KEYSTORE_NAME;

        // **********************************
        // Indicate Obtained.
        DEST_OBTAINED = true;
    } // end of obtainDESTIDU Method

    /**
     * Obtained the Local Principal/Credentials
     * from a system Property Specification.
     */
    private void obtainProperties() {

        // ********************************************
        // Check for IDU specified in the Property.
        String _idu_property_value = System.getProperty("jeffaschenk.commons.frameworks.cnxidx.icos.idu");
        if ((_idu_property_value != null) &&
                (!_idu_property_value.trim().equals("")) &&
                (!OBTAINED)) {
            // **********************************
            // Obtain.
            IDUAlias = _idu_property_value.trim();
            obtainIDU();
        } // End of If.

        // *****************************************************
        // Check for IRRID and IRRPW specified in the Property.
        // Both must exist.
        String _irrprincipal_property_value = System.getProperty("jeffaschenk.commons.frameworks.cnxidx.icos.irrprincipal");
        String _irrcredentials_property_value = System.getProperty("jeffaschenk.commons.frameworks.cnxidx.icos.irrcredentials");
        if ((_irrprincipal_property_value != null) &&
                (!_irrprincipal_property_value.trim().equals("")) &&
                (_irrcredentials_property_value != null) &&
                (!_irrcredentials_property_value.trim().equals("")) &&
                (!OBTAINED)) {
            // **********************************
            // Obtain.
            IRRPrincipal = _irrprincipal_property_value.trim();
            IRRCredentials = _irrcredentials_property_value.trim();

            // **********************************
            // Specify Source for Obtaining.
            SOURCE = OBTAINED_FROM_PROPERTY;
            SOURCE_NAME = OBTAINED_FROM_PROPERTY_NAME;

            // **********************************
            // Indicate Obtained.
            OBTAINED = true;

        } // End of If.

    } // end of obtainProperties Method

    /**
     * Obtained the Destination Principal/Credentials
     * from a system Property Specification.
     */
    private void obtainDESTProperties() {

        // ********************************************
        // Check for IDU specified in the Property.
        String _idu_property_value = System.getProperty("jeffaschenk.commons.frameworks.cnxidx.icos.destination.idu");
        if ((_idu_property_value != null) &&
                (!_idu_property_value.trim().equals("")) &&
                (!DEST_OBTAINED)) {
            // **********************************
            // Obtain.
            DEST_IDUAlias = _idu_property_value.trim();
            obtainDESTIDU();
        } // End of If.

        // *****************************************************
        // Check for IRRID and IRRPW specified in the Property.
        // Both must exist.
        String _irrprincipal_property_value = System.getProperty("jeffaschenk.commons.frameworks.cnxidx.icos.destination.irrprincipal");
        String _irrcredentials_property_value = System.getProperty("jeffaschenk.commons.frameworks.cnxidx.icos.destination.irrcredentials");
        if ((_irrprincipal_property_value != null) &&
                (!_irrprincipal_property_value.trim().equals("")) &&
                (_irrcredentials_property_value != null) &&
                (!_irrcredentials_property_value.trim().equals("")) &&
                (!DEST_OBTAINED)) {
            // **********************************
            // Obtain.
            DEST_IRRPrincipal = _irrprincipal_property_value.trim();
            DEST_IRRCredentials = _irrcredentials_property_value.trim();

            // **********************************
            // Specify Source for Obtaining.
            DEST_SOURCE = OBTAINED_FROM_PROPERTY;
            DEST_SOURCE_NAME = OBTAINED_FROM_PROPERTY_NAME;

            // **********************************
            // Indicate Obtained.
            DEST_OBTAINED = true;

        } // End of If.

    } // end of obtainDESTProperties Method


    /**
     * toString Method for Class.
     */
    public String toString() {
        return ("LOCAL OBTAINED:[" + OBTAINED + "]," +
                "OBTAINED FROM:[" + SOURCE_NAME + "]," +
                "IRR Principal:[" + IRRPrincipal + "]," +
                "IRR Credentials:[" + IRRCredentials + "]," +
                "IDU Alias:[" + IDUAlias + "],\n" +
                "DEST OBTAINED:[" + DEST_OBTAINED + "]," +
                "OBTAINED FROM:[" + DEST_SOURCE_NAME + "]," +
                "IRR Principal:[" + DEST_IRRPrincipal + "]," +
                "IRR Credentials:[" + DEST_IRRCredentials + "]," +
                "IDU Alias:[" + DEST_IDUAlias + "].");
    } // end of toString Method

} ///: End of CommandLinePrincipalCredentials Class.
