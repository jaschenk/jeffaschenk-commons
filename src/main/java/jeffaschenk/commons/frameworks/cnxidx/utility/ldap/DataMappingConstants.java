package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

/**
 * This is a helper interface for directory constants including partial DN's
 *
 * @author jeffaschenk@gmail.com
 * @version 1.0
 * @since 06/18/2001
 */
public interface DataMappingConstants {

    /** Abbreviations
     OC = Object Class
     NA = Naming Attribute
     CFG = Configuration
     DEV = Device
     **/

    /**
     * Misc. CONSTANTS *
     */
    public static final String OC = "objectclass";
    public static final String OC_ALL_FILTER = "(objectclass=*)";

    public static final String RESOURCE_TYPE_OC = "dcdOrgUnit";
    public static final String RESOURCE_TYPE_NA = "ou"; //ex. ou=routers,...

    /**
     * CONSISTENCY CONSTANTS *
     */
    public static final String CON_REALM_NA = "dc";
    public static final String CON_LAST_MOD_TIME = "cnxidaLastModifyTime";
    public static final String CON_LAST_MOD_TIME_MILLISECONDS = "cnxidaLockToken";
    public static final String CON_LAST_MOD_BY = "cnxidaLastModifyBy";
    public static final String CON_CREATED_TIME = "cnxidaInstallTime";
    public static final String CON_CREATED_BY = "cnxidaInstallBy";

    /**
     * DEVICE CONSTANTS *
     */
    public static final String DEV_VTMOS_OC = "cnxidoVTMOS";
    public static final String DEV_KNOWLEDGE_OC = "cnxidoXKnowledge";
    public static final String DEV_FACTORY_OC = "DeviceComInterfaceFactory";
    public static final String DEV_SCRIPT_OC = "DeviceScript";
    public static final String DEV_NA = "xen";
    public static final String DEV_MODEL_NA = "ou";
    public static final String DEV_TYPE_NA = "ou";
    public static final String DEV_VENDOR_NA = "ou";
    public static final String DEV_OS_NA = "ou";
    public static final String DEV_ATTRIBUTE_NA = "xen";
    public static final String DEV_FACTORY_NA = "xen";
    public static final String DEV_COMPRESSION_FORMAT = "cnxidaXCompressionFormat";
    public static final String DEV_COMPRESSED_BLOB = "cnxidaXCompressedObjectBlob";
    public static final String DEV_BLOB = "cnxidaxobjectblob";
    public static final String DEV_ELEMENTS_SUPPORTED = "cnxidaElementsSupported";
    public static final String DEV_REGEX_SUPPORTED = "cnxidaRegExSupported";
    public static final String DEV_XPREFIX = "cnxidaXPrefix";
    public static final String DEV_XROOT_ELEMENT = "cnxidaXRootElement";
    public static final String DEV_X_ELEMENT_OC = "cnxidoXElement";
    public static final String DEV_X_ELEMENT_TOP = "top";
    public static final String DEV_X_VALUE = "cnxidaXValue";
    public static final String DEV_X_ELEMENT_NA = "xen";
    public static final String DEV_X_ATTRIBUTE_OC = "cnxidoXAttribute";
    public static final String DEV_X_ATTRIBUTE_TOP = "top";
    public static final String DEV_ELEMENT_MASK = "cnxidaElementMask";
    public static final String DEV_SCHEMA_VERSION = "cnxidaXSchemaVersion";

    // Command UOW command attribute names
    public static final String COMMAND_RESOURCE_TYPE = "CommandResource";
    public static final String COMMAND_TYPE_PROPERTY_NAME = "CommandType";
    public static final String COMMAND_IS_PARENT_PROPERTY_NAME = "IsParent";
    public static final String UOW_COMMAND_RESOURCE_KEY_PROPERTY_NAME = "ResourceKey";
    public static final String UOW_COMMAND_CONFIGURATION_KEY_PROPERTY_NAME = "ConfigurationKey";
    public static final String UOW_COMMAND_EXECUTION_HIERARCHY_PROPERTY_NAME = "ExecutionHierarchy";
    public static final String UOW_COMMAND_NAME = "command";
    public static final String UOW_COMMAND_ID = "rdu=command";

    //TODO: Move these back to the IRM package when the backwards references are resolved.
    public static final String NR_OC = "cnxidoResource";
    public static final String NR_NA = "reuid";
    public static final String NR_HOST_NAME = "ren";
    public static final String NR_ACTUAL_INFO = "cnxidaResourceInformation";
    public static final String NR_ACTUAL_MODEL = "cnxidaActualModel";
    public static final String NR_LOCK_EXECUTION_KEY = "cnxidaLockExecutionKey";
    public static final String NR_LOCK_ROOT_EXECUTION_KEY = "cnxidaLockRootExecutionKey";
    public static final String NR_MODEL = "cnxidaModel";
    public static final String NR_TYPE = "cnxidaType";
    public static final String NR_VENDOR = "cnxidaVendor";
    public static final String NR_ACTUAL_OS_VERSION = "cnxidaActualOSVersion";
    public static final String NR_ACTIVE_STATE = "cnxidaREState";
    public static final String NR_CHILD_OC = "cnxidoResourceManagement";
    public static final String CFG_OC = "cnxidoResourceGlobalElement";
    public static final String CFG_CURRENT_CFG_DN_AS_OF_LAST_SAVE = "cnxidaCurrentCfgDN";
    public static final String CFG_DOM_VALUE = "cnxidaXValue";
    public static final String CFG_OS_VERSION = "cnxidaGlobalOSVersion";
    public static final String CFG_ACTUAL_OS_VERSION = "cnxidaActualOSVersion";
    public static final String CFG_EDIT_STATE = "cnxidaGlobalCfgState";
    public static final String CFG_NA = "ceuid";
    public static final String CFG_NAME = "cn";
    public static final String CFG_EDIT_STATE_VERSIONED = "versioned";
    public static final String CFG_COMMENT = "cnxidaComment";
    //TODO: Move these back to the ADMIN package when the backwards references are resolved.
    public static final String PS_VALUE = "cnxidaXValue";

    // **************************************************
    // New Attribute FrameworkUserKey.
    public static final String IDX_USER_KEY_NAME = "FrameworkUserKey";

    /**
     * Binary Attribute Names *
     */
    public static final String BIN_ATTRIBUTE_NAMES = "cnxidaXCompressedObjectBlob " +
            "cnxidaBinaryDataCompressedBlob " +
            "cnxidaBinaryDataBlob " +
            "cnxidaGlobalCfgVersionCompressedBlob " +
            "FrameworkUserKey " +
            "cnxidaAuthenticationCredentials " +
            "cnxidaAuthenticationPassword " +
            "LocCredentials " +
            "ErrorLog " +
            "LocalRoutingInformation " +
            "voiceDialByNameNumber " +
            "changes " +
            "userPassword;hash-md5 " +
            "DSAEJavaSerializedData " +
            "javaSerializedData " +
            "entrustPolicyCertificate " +
            "entrustRoamFileEncInfo " +
            "entrustRoamingCAPAB " +
            "entrustRoamingEOP " +
            "entrustRoamingPAB " +
            "entrustRoamingProfile " +
            "entrustRoamingPRV " +
            "entrustRoamingRecipList " +
            "entrustRoamingSLA";

    /*
    ** PDN = Partial Distinguished Name
    ** These are constants the represent all the partial dn's that are used
    ** throughout the system to build up full dn's from pieces.
    */
    public static final String PDN_FRAMEWORK = "ou=framework";
    public static final String PDN_DOMAIN_OBJECTS = "ou=domainobjects";
    public static final String PDN_OPER_STATE = "cn=work queue state";
    public static final String PDN_SYS_OPER_STATE = "cn=system work queue state";
    public static final String PDN_ICOS_STATE = "cn=icos state";
    public static final String PDN_ACTION_GROUP = "ou=actiongroups";
    public static final String PDN_RECYCLE = "ou=recycle";
    public static final String PDN_PEOPLE = "ou=people";

    public static final String PDN_NET_RES_CFG = "cn=CFG";
    public static final String PDN_NET_RES_UOW = "cn=UOW";
    public static final String PDN_NET_RES_CNT = "cn=CONTENT";

    public static final String PDN_HASH = "xen=hash";
    public static final String PDN_NC_GENERATION_DATA = "xen=NativeCommandGenerationData";
    public static final String PDN_DEVICESCRIPT_DATA = "xen=DeviceScript";
    public static final String PDN_KNOWLEDGE = "ou=knowledge";
    public static final String PDN_VENDOROBJECTS = "ou=vendorobjects";
    public static final String PDN_SUPPORTED = "ou=supported";
    public static final String PDN_VENDOR_MASK = "ou=vendorMask";
    public static final String PDN_HASHLETS = "ou=hashlets";

    public static final String PDN_APP_TCKT_WORKFLOW = "cn=workflow";
    public static final String PDN_UOW_SUBCLASS_NODE = "xen=UowProperties";

    public static final String PDN_CUSTOMER_OBJECTS = "ou=customerobjects";
    public static final String PDN_POLICIES = "ou=policies";

    public static final String PDN_ELEMENT_PERMISSIONS = "ou=elementpermissions";
    public static final String PDN_ACTIVITIES = "ou=activities";
    public static final String PDN_ACCESS_DOCUMENT = "xen=ResourceAccessDocument";
    public static final String PDN_COMMAND_RESTRICTION = "xen=RestrictedCommands";
    public static final String COMMAND_RESTRICTION_DELIMETER = ":";


    // Resource config stuff
    public static final String NR_ONLINE = "ONLINE";
    public static final String NR_DEFAULT_CONFIG = "DefaultConfiguration";
    public static final String NR_OFFLINE = "OFFLINE";

    public static final String NR_ONLINE_FILTER =
            "(" + NR_ACTIVE_STATE + "=" + NR_ONLINE + ")";
    public static final String NR_DEFAULT_CONFIG_FILTER =
            "(" + NR_ACTIVE_STATE + "=" + NR_DEFAULT_CONFIG + ")";
    public static final String NR_OFFLINE_FILTER =
            "(" + NR_ACTIVE_STATE + "=" + NR_OFFLINE + ")";

    public static final String NR_ONLINE_OR_DEFCONFIG_FILTER =
            "(|" + NR_ONLINE_FILTER + NR_DEFAULT_CONFIG_FILTER + ")";

    public static final String NR_CURRENT_CONFIG_FILTER =
            "(&" + NR_ONLINE_FILTER +
                    "(" + CFG_CURRENT_CFG_DN_AS_OF_LAST_SAVE + "=*)" +
                    ")";

}
