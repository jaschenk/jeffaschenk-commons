package jeffaschenk.commons.frameworks.cnxidx.utility.message;

/**
 * @author john.leichner
 */
public interface MessageConstants {
    // Messages should be grouped by their functional area within the application

    // Generic messages
    public static final String METHOD_START = "jeffaschenk.commons.0001";
    public static final String METHOD_END = "jeffaschenk.commons.0002";
    public static final String CANNOT_BE_NULL = "jeffaschenk.commons.0003";

    // Utility messages
    public static final String COMMON_UTILITIES_PROPERTY_SET_NO_FILE = "jeffaschenk.commons.utilities.0001";
    public static final String COMMON_UTILITIES_FILE_NOT_IN_CLASSPATH = "jeffaschenk.commons.utilities.0002";
    public static final String COMMON_UTILITIES_FILE_NOT_FOUND = "jeffaschenk.commons.utilities.0003";
    public static final String COMMON_UTILITIES_PROPERTY_FILE_NOT_READ = "jeffaschenk.commons.utilities.0004";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_INVALID_SECTION = "jeffaschenk.commons.utilities.0005";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_NO_SECTION_TO_CLOSE = "jeffaschenk.commons.utilities.0006";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_SECURITY_VIOLATION = "jeffaschenk.commons.utilities.0007";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_IOEXCEPTION = "jeffaschenk.commons.utilities.0008";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_CANNOT_WRITE_TO_FILE = "jeffaschenk.commons.utilities.0009";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_INVALID_LOG_ENTRY = "jeffaschenk.commons.utilities.0010";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_ERROR_CLOSING_LOG = "jeffaschenk.commons.utilities.0011";
    public static final String COMMON_UTILITIES_EXECUTION_LOGGER_PRINT_WRITER_NOT_CLOSED = "jeffaschenk.commons.utilities.0012";
    public static final String COMMON_UTILITIES_ERROR_DECODING_FILE_URL = "jeffaschenk.commons.utilities.0013";
    public static final String COMMON_UTILITIES_COMPARING_URLS = "jeffaschenk.commons.utilities.0014";
    public static final String COMMON_UTILITIES_COMPARING_ADDRESSES = "jeffaschenk.commons.utilities.0015";
    public static final String COMMON_UTILITIES_COMPARING_URLS_FAILED = "jeffaschenk.commons.utilities.0016";
    public static final String COMMON_UTILITIES_PROPERTY_STREAM_NOT_READ = "jeffaschenk.commons.utilities.0017";

    // Registration manager Messages
    public static final String COMMON_REGISTRATION_MANAGER_FOUND_MATCHING = "jeffaschenk.commons.registry.0001";
    public static final String COMMON_REGISTRATION_MANAGER_FAIL_LOAD_COMP_DESC_CONTINUING = "jeffaschenk.commons.registry.0002";
    public static final String COMMON_REGISTRATION_MANAGER_FAILED_LOAD_COMPONENTS = "jeffaschenk.commons.registry.0003";
    public static final String COMMON_REGISTRATION_MANAGER_FAIL_LOAD_PROPERTY_FOR = "jeffaschenk.commons.registry.0004";

    // Communication manager Messages
    public static final String COMMON_COMMUNICATION_MANAGER_JMS_EXCEPTION_SETTING_PROPERTY = "jeffaschenk.commons.communication.0001";
    public static final String COMMON_COMMUNICATION_MANAGER_FAILED_CREATE_OBJECT_MESSAGE = "jeffaschenk.commons.communication.0002";
    public static final String COMMON_COMMUNICATION_MANAGER_FAILED_CREATE_PUBLISHER = "jeffaschenk.commons.communication.0003";
    public static final String COMMON_COMMUNICATION_MANAGER_FAILED_CREATE_SESSION = "jeffaschenk.commons.communication.0004";
    public static final String COMMON_COMMUNICATION_MANAGER_FAILED_CREATE_CONNECTION = "jeffaschenk.commons.communication.0005";
    public static final String COMMON_COMMUNICATION_MANAGER_NAMING_EXCEPTION_TOPIC = "jeffaschenk.commons.communication.0006";
    public static final String COMMON_COMMUNICATION_MANAGER_FAIL_MESSAGE_SEND = "jeffaschenk.commons.communication.0007";
    public static final String COMMON_COMMUNICATION_MANAGER_NAMING_EXCEPTION_CONNECTION_FACTORY = "jeffaschenk.commons.communication.0008";

    // Security Messages
    public static final String COMMON_AUTH_KEYSTORE_NOT_FOUND = "jeffaschenk.commons.utility.security.Authentication.0001";

}
