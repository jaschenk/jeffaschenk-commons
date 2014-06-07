package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

/**
 * Error Constants
 *
 * @authors Jeff.Schenk
 */
public interface ErrorConstants {
    // Messages should be grouped by their functional area within the application Exception.

    // ***********************
    // Generic Error Messages
    // ***********************
    public static final String ERROR
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR";
    public static final String EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.EXECPTION";
    public static final String COMMUNICATION_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0001";
    public static final String COMMUNICATION_RECEIVE_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0002";
    public static final String COMMUNICATION_EXCEPTION_STACKTRACE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0003";


    // *************************
    // Service Exceptions.
    // *************************
    public static final String SERVICE_UNABLE_TO_READ_PROPERTIES
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2001";

    public static final String SERVICE_UNABLE_TO_OBTAIN_PROPERTIES
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2002";

    public static final String SERVICE_IRR_HOST_URL_NOT_SPECIFIED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2003";

    public static final String SERVICE_UNABLE_TO_OBTAIN_AUTHENTICATION_DATA
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2004";

    public static final String SERVICE_UNABLE_TO_OBTAIN_IRRPRINCIPAL_PROPERTY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2005";

    public static final String SERVICE_UNABLE_TO_OBTAIN_IRRCREDENTIALS_PROPERTY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2006";

    public static final String SERVICE_PROPERTY_NOT_SPECIFIED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2007";

    public static final String SERVICE_PROPERTY_FILENAME_NOT_ACCESSIBLE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.2008";

    // *************************
    // Reader Exceptions.
    // *************************
    public static final String READER_CORRUPTED_STACK_COMMAND
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0401";

    public static final String READER_IOEXCEPTION_WRITING_STATUS_FILE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0402";

    public static final String READER_EXCEPTION_PROCESSING_LOG
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0403";

    public static final String READER_EXCEPTION_PROCESSING_LDIF_INPUT
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0404";

    public static final String READER_EXCEPTION_STACKTRACE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0405";

    public static final String READER_WILL_NOT_SEND_NULL_ENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0406";

    public static final String READER_EXCEPTION_IN_MAIN_LOOP
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0407";

    public static final String READER_IGNORING_UNKNOWN_STACK_COMMAND
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0408";

    public static final String READER_IOEXCEPTION_PROCESSING_FILTER_FILE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0409";

    //  **************************
    // Replica Restore Exceptions.
    // ***************************
    public static final String REPLICARESTORE_CORRUPTED_STACK_COMMAND
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0601";

    public static final String REPLICARESTORE_ROGUE_REPLICATION_COMMAND_BAD_ARGS
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0602";

    public static final String REPLICARESTORE_ROGUE_REPLICATION_COMMAND_NULL_CHANGETYPE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0603";

    public static final String REPLICARESTORE_ROGUE_REPLICATION_COMMAND_INVALID_CHANGETYPE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0604";

    public static final String REPLICARESTORE_EXCEPTION_STACKTRACE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0605";

    public static final String READER_WILL_NOT_PROCESS_NULL_ENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0606";

    public static final String REPLICARESTORE_EXCEPTION_IN_MAIN_LOOP
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0607";

    public static final String REPLICARESTORE_IGNORING_UNKNOWN_STACK_COMMAND
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0608";

    public static final String REPLICARESTORE_IOEXCEPTION_PROCESSING_FILTER_FILE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0609";

    public static final String REPLICARESTORE_EXCEPTION_PROCESSING_REPLICATION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0610";

    public static final String REPLICARESTORE_NAME_ALREADY_BOUND
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0611";

    public static final String REPLICARESTORE_EXCEPTION_PROCESSING_ADD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0612";

    public static final String REPLICARESTORE_EXCEPTION_DELETINGTREE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0613";

    public static final String REPLICARESTORE_EXCEPTION_DELETINGENTRY_NOTFOUND
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0614";

    public static final String REPLICARESTORE_EXCEPTION_DELETINGENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0615";

    public static final String REPLICARESTORE_NO_NEWRDN_FOR_RENAME
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0616";

    public static final String REPLICARESTORE_EXISTING_DN_FOR_RENAME_INVALID
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0617";

    public static final String REPLICARESTORE_NAME_ALREADY_BOUND_NEWDN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0618";

    public static final String REPLICARESTORE_EXCEPTION_RENAMINGENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0619";

    public static final String REPLICARESTORE_EXCEPTION_RENAMINGENTRY_USINGCOPY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0620";

    public static final String REPLICARESTORE_NAME_NOT_FOUND_FOR_MODIFY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0621";

    public static final String REPLICARESTORE_ATTR_ALREADY_EXISTS_USE_REPLACE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0622";

    public static final String REPLICARESTORE_NO_SUCH_ATTR_FOR_MODIFY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0623";

    public static final String REPLICARESTORE_ATTR_MODIFICATION_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0624";

    public static final String REPLICARESTORE_NAMING_EXCEPTION_FOR_MODIFY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0625";

    public static final String REPLICARESTORE_EXCEPTION_FOR_MODIFY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0626";

    public static final String REPLICARESTORE_EXCEPTION_OPENING_DIRECTORY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0627";

    public static final String REPLICARESTORE_EXCEPTION_DISABLING_FACTORIES
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0628";

    public static final String REPLICARESTORE_EXCEPTION_CLOSING_DIRECTORY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0629";

    // *************************
    // Channel Exceptions.
    // *************************
    public static final String PUBLISH_TO_CHANNEL_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0201";

    public static final String PUBLISH_TO_CHANNEL_EXCEPTION_STACK_TRACE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0202";

    // *************************
    // WebAdmin Exceptions.
    // *************************
    public static final String WEBADMIN_OBTAINING_HTTP_SOCKET_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0801";

    public static final String WEBADMIN_ACCEPTOR_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0802";

    public static final String WEBADMIN_RESPONDER_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0803";

    public static final String WEBADMIN_RESPONDER_THREAD_INVALID_OPURI
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0804";

    public static final String WEBADMIN_ACCEPTOR_IGNORING_PATTERN_SYNTAX
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.0805";

    // *************************
    // File Utility Messages.
    // *************************
    public static final String DIRLIST_REGEX_PATTERN_SYNTAX_EXCEPTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.ERROR.8001";

}
