package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;


/**
 * Java Class which provides the common system.exit return codes for the
 * various Java command line utilities.  This allows for the return codes
 * from various commands to be interpreted outside the JVM.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public interface idxCMDReturnCodes {

    public static final int EXIT_SUCCESSFUL = 0;                           // Success.

    public static final int EXIT_GENERIC_FAILURE = 1;                      // Generic Failure.
    public static final int EXIT_USAGE = 2;                                // Usage Requested.
    public static final int EXIT_VERSION = 3;                              // Version Requested.

    public static final int EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT = 10;         // IRR Failure.
    public static final int EXIT_IRR_UNKNOWN_HOST = 11;                     // IRR Failure.
    public static final int EXIT_IRR_CONNECTION_REFUSED = 12;               // IRR Failure.
    public static final int EXIT_IRR_GENERIC_COMMUNICATION_EXCEPTION = 13;  // IRR Failure.
    public static final int EXIT_IRR_UNABLE_TO_AUTHENTICATE = 18;           // IRR Failure.
    public static final int EXIT_IRR_CLOSE_FAILURE = 19;                    // IRR Failure.

    public static final int EXIT_IRR_ADD_FAILURE = 21;                      // IRR Failure.
    public static final int EXIT_IRR_DELETE_FAILURE = 22;                   // IRR Failure.
    public static final int EXIT_IRR_MODIFY_FAILURE = 23;                   // IRR Failure.
    public static final int EXIT_IRR_COPY_FAILURE = 24;                     // IRR Failure.
    public static final int EXIT_IRR_BACKUP_FAILURE = 25;                   // IRR Failure.
    public static final int EXIT_IRR_BACKUP_SCHEMA_OUTPUT_FAILURE = 26;     // IRR Failure.
    public static final int EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE = 27;       // IRR Failure.
    public static final int EXIT_IRR_BACKUP_LDIF_OUTPUT_CLOSE_FAILURE = 28; // IRR Failure.
    public static final int EXIT_IRR_SEARCH_OPERATION_FAILURE = 29;         // IRR Failure.

    public static final int EXIT_IRR_SEARCH_NO_ENTRIES_FOUND = 30;          // Search no Entries Found.

    public static final int EXIT_IRR_GMD_ENTRY_NOTFOUND_AFTER_WRITE = 40;   // GMD Load,  Entry Not Found.
    public static final int EXIT_IRR_GMD_ENTRY_MOD_FAILURE = 41;            // GMD Load.
    public static final int EXIT_IRR_GMD_ENTRY_EXISTS_FOR_ADD = 42;         // GMD Load.
    public static final int EXIT_IRR_GMD_ENTRY_WRITE_FAILURE = 43;          // GMD Load.
    public static final int EXIT_IRR_GMD_FAILURE = 44;                      // GMD Failure.
    public static final int EXIT_IRR_GMD_LDIF_OUTPUT_FAILURE = 45;          // GMD Failure.
    public static final int EXIT_IRR_GMD_LDIF_OUTPUT_CLOSE_FAILURE = 46;    // GMD Failure.

    public static final int EXIT_IRR_KNOWLEDGE_ENTRY_NOTFOUND_AFTER_WRITE = 50;   // Knowledge Load,  Entry Not Found.
    public static final int EXIT_IRR_KNOWLEDGE_ENTRY_MOD_FAILURE = 51;            // Knowledge Load.
    public static final int EXIT_IRR_KNOWLEDGE_ENTRY_EXISTS_FOR_ADD = 52;         // Knowledge Load.
    public static final int EXIT_IRR_KNOWLEDGE_ENTRY_WRITE_FAILURE = 53;          // Knowledge Load.
    public static final int EXIT_IRR_KNOWLEDGE_FAILURE = 54;                      // Knowledge Failure.
    public static final int EXIT_IRR_KNOWLEDGE_LDIF_OUTPUT_FAILURE = 55;          // Knowledge Failure.
    public static final int EXIT_IRR_KNOWLEDGE_LDIF_OUTPUT_CLOSE_FAILURE = 56;    // Knowledge Failure.
    public static final int EXIT_IRR_KNOWLEDGE_INVALID_TREE = 57;   // Knowledge Load,  Tree Invalid, Not Found.

    public static final int EXIT_IRR_ERROR_PROCESSING_INPUT_FILE = 60;      // External IO.
    public static final int EXIT_IRR_ERROR_COMPRESSING_DATA = 61;           // Compression IO.

    public static final int EXIT_IRR_RESTORE_FAILURE = 70;                   // IRR Failure.
    public static final int EXIT_IRR_RESTORE_LDIF_INPUT_FAILURE = 71;        // IRR Failure.
    public static final int EXIT_IRR_RESTORE_LDIF_INPUT_CLOSE_FAILURE = 72;  // IRR Failure.
    public static final int EXIT_IRR_RESTORE_BINDING_ENTRY = 73;             // IRR Failure.
    public static final int EXIT_IRR_RESTORE_NO_ENTRIES_RESTORED = 74;                   // IRR Failure.

    public static final int EXIT_IRR_GET_FAILURE = 80;                       // IRR Failure.
    public static final int EXIT_IRR_GET_INVALID_FILTER_FAILURE = 81;        // IRR Failure.

    public static final int EXIT_IRR_INSTALL_CUSTOMER_FAILURE = 90;          // IRR Failure.

    public static final int EXIT_ICOS_UNAVAILABLE = 100;          // ICOS Unavailable.
    public static final int EXIT_ICOS_IRR_UNAVAILABLE = 101;          // ICOS IRR Unavailable.
    public static final int EXIT_ICOS_IRR_EXCEPTION = 102;          // ICOS IRR Exception.
    public static final int EXIT_ICOS_RMI_FAILURE = 103;          // ICOS RMI Failure.
    public static final int EXIT_ICOS_EJB_ACCESS_FAILURE = 104;          // ICOS EJB Failure.

} ///:~ End of idxCMDReturnCodes Interface.
