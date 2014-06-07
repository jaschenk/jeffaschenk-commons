package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

/**
 * Message Constants
 *
 * @author Jeff.Schenk
 */
public interface MessageConstants {

    // ********************************************
    // Control Messages Generated from
    // IRRChangeLogRestoreServiceControlThread.
    // ********************************************
    public static final String BOOT
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0001";

    public static final String DETERMINE_PRIOR_STATE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0002";

    public static final String PRIOR_STATE_ACQUIRED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0003";

    public static final String PRIOR_STATE_UNKNOWN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0004";

    public static final String INITIALIZING_STACKS
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0005";

    public static final String STACKS_INITIALIZED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0006";

    public static final String INITIALIZING_RESILIENCY_COMMUNICATION_LAYER
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0007";

    public static final String RESILIENCY_MEMBERSHIP_NAME
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0008";

    public static final String RESILIENCY_PROPERTIES
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0009";

    public static final String RESILIENCY_COMMUNICATIONS_LAYER_INITIALIZED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0010";

    public static final String STARTING_PUBLISHER_THREAD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0011";

    public static final String STARTING_READER_THREAD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0012";

    public static final String STARTING_REPLICARESTORE_THREAD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0013";

    public static final String STARTING_WEBADMIN_THREAD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0014";

    public static final String BOOT_SEQUENCE_COMPLETE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0015";

    public static final String STARTING_MAIN_CONTROL_THREAD_LOOP
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0016";

    public static final String CONTROL_STACK_COMMAND_POPPED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0017";

    public static final String CONTROL_STACK_ECHO_REPLY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0018";

    public static final String CONTROL_STACK_ECHO_REPLY_WITH_EMPTY_OBJECT
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0019";

    public static final String IGNORED_UNKNOWN_COMMAND
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0020";

    public static final String CONTROL_PLACING_LOGS_TO_BE_PROCESSED_ON_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0021";

    public static final String CONTROL_RECEIVED_MESSAGE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0022";

    public static final String CONTROL_RECEIVED_UNKNOWN_MESSAGE_TYPE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0023";

    public static final String REPLICA_RESTORE_THREAD_NOT_READY_QUEUE_BUILDING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0024";

    public static final String CONTROL_DROPPING_REPLICA_RESTORE_REQUEST
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0025";

    public static final String CONTROL_DROPPING_REPLICA_RESPONSE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0026";

    public static final String CONTROL_INITIALIZED_AS_PRIMARY_COORDINATOR
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0030";

    public static final String CONTROL_MAINTAINING_STATE_AS_PRIMARY_COORDINATOR
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0031";

    public static final String CONTROL_STATE_CHANGE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0032";

    public static final String CONTROL_NOT_PRIMARY_COORDINATOR
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0033";

    public static final String CONTROL_NO_PRIMARY_COORDINATOR
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0034";

    public static final String CONTROL_PUBLISHER_THREAD_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0040";

    public static final String CONTROL_PUBLISHER_THREAD_NOT_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0041";

    public static final String CONTROL_PUBLISHER_THREAD_WATING_TO_COMPLETE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0042";

    public static final String CONTROL_PUBLISHER_THREAD_STOPPED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0043";

    public static final String CONTROL_READER_THREAD_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0044";

    public static final String CONTROL_READER_THREAD_NOT_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0045";

    public static final String CONTROL_READER_THREAD_WATING_TO_COMPLETE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0046";

    public static final String CONTROL_READER_THREAD_STOPPED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0047";

    public static final String CONTROL_REPLICARESTORE_THREAD_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0048";

    public static final String CONTROL_REPLICARESTORE_THREAD_NOT_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0049";

    public static final String CONTROL_REPLICARESTORE_THREAD_WATING_TO_COMPLETE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0050";

    public static final String CONTROL_REPLICARESTORE_THREAD_STOPPED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0051";

    public static final String CONTROL_WEBADMIN_THREAD_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0052";

    public static final String CONTROL_WEBADMIN_THREAD_NOT_RUNNING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0053";

    public static final String CONTROL_WEBADMIN_THREAD_WATING_TO_COMPLETE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0054";

    public static final String CONTROL_WEBADMIN_THREAD_STOPPED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0055";

    public static final String CONTROL_CONNECTING_TO_GROUP
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0060";

    public static final String CONTROL_ESTABLISHING_ADAPTER
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0061";

    public static final String CONTROL_REQUESTED_OPERATIONAL_MODE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0080";

    public static final String CONTROL_REQUESTED_DEFAULT_OPERATIONAL_MODE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0081";

    public static final String CONTROL_REQUEST_TO_START_READER_ACCEPTED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0082";

    public static final String CONTROL_REQUEST_TO_START_READER_IGNORED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0083";

    public static final String CONTROL_REQUEST_TO_STOP_READER_ACCEPTED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0084";

    public static final String CONTROL_REQUEST_TO_STOP_READER_IGNORED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0085";

    public static final String CONTROL_REQUEST_TO_START_REPLICA_ACCEPTED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0086";

    public static final String CONTROL_REQUEST_TO_START_REPLICA_IGNORED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0087";

    public static final String CONTROL_REQUEST_TO_STOP_REPLICA_ACCEPTED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0088";

    public static final String CONTROL_REQUEST_TO_STOP_REPLICA_IGNORED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0089";

    public static final String CONTROL_ADDING_WEBADMIN_ALLOWABLE_ADDRESS
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0090";

    public static final String CONTROL_HOUSEKEEPING_COMPLETED_WITH_FILES_PURGED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0091";

    public static final String CONTROL_HOUSEKEEPING_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0092";

    public static final String CONTROL_TRIGGERING_HOUSEKEEPING_FOR_PEER
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0093";

    public static final String ENTERING_CONTROL_METHOD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0100";

    // ****************************
    // Publisher Thread Messages.
    // ****************************
    public static final String PUBLISHER_THREAD_ESTABLISHED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0200";

    public static final String PUBLISHER_LAPTIME_FROM_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0207";

    public static final String PUBLISHER_LAPTIME_TO_CHANNEL
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0208";

    public static final String PUBLISHER_THREAD_SHUTDOWN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0209";

    // ****************************
    // Reader Thread Messages.
    // ****************************
    public static final String READER_THREAD_ESTABLISHED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0400";

    public static final String READER_LAPTIME_FROM_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0407";

    public static final String READER_LAPTIME_TO_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0408";

    public static final String READER_LAPTIME_READIO
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0409";

    public static final String READER_LAPTIME_WRITEIO
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0410";

    public static final String READER_RECEIVE_RESPONSE_ENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0420";

    public static final String READER_LOG_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0421";

    public static final String READER_PROCESSING_CHANGE_LOG
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0422";

    public static final String READER_PROCESSED_CHANGE_LOG
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0423";

    public static final String READER_QUEUEING_MODIFICATION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0424";

    public static final String READER_NO_PENDING_MODIFICATION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0425";

    public static final String READER_PROCESSING_QUEUED_MODIFICATION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0426";

    public static final String READER_SENDING_ENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0427";

    public static final String READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0428";

    public static final String READER_PREPARING_DN_EXCLUSION_FILTER
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0429";

    public static final String READER_FILTERING_DISABLED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0430";

    public static final String READER_FILTERING_ENABLED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0431";

    public static final String READER_READY_TO_PERFORM_WORK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0432";

    public static final String READER_BLOCKING_ENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0433";

    public static final String READER_PERFORMING_HOUSEKEEPING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0434";

    public static final String READER_HOUSEKEEPING_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0435";

    public static final String READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED_WAS_BLOCKED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0436";

    public static final String READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED_WAS_UNSUCCESSFUL
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0437";

    public static final String READER_CHANGE_LOG_ENTRY_RESPONSE_RETRY_IGNORED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0438";

    public static final String READER_CHANGE_LOG_ENTRY_RESPONSE_WILL_BE_RETRIED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0439";

    public static final String READER_CHECKPOINTING_LOG_FILE_AS_COMPLETE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0440";

    public static final String READER_CHANGE_LOG_ENTRY_RESPONSE_COMPLETED_WAS_IGNORED_BY_PEER
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0441";

    public static final String READER_CHECKPOINTING_LOG_FILE_AS_BLOCKED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0442";

    public static final String READER_PERFORMING_PEER_HOUSEKEEPING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0443";

    public static final String READER_PERFORMING_RETRY_TO_DESTINATION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0444";

    public static final String READER_PERFORMING_RETRY_TO_ALL
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0445";

    public static final String READER_DISMISSING_CHANGE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0446";

    public static final String READER_THREAD_SHUTDOWN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0499";

    // ********************************
    // Replica Restore Thread Messages.
    // ********************************
    public static final String REPLICARESTORE_THREAD_ESTABLISHED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0600";

    public static final String REPLICARESTORE_LAPTIME_FROM_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0607";

    public static final String REPLICARESTORE_LAPTIME_TO_DIRECTORY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0608";

    public static final String REPLICARESTORE_LAPTIME_TO_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0609";

    public static final String REPLICARESTORE_PREPARING_DN_EXCLUSION_FILTER
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0629";

    public static final String REPLICARESTORE_FILTERING_DISABLED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0630";

    public static final String REPLICARESTORE_FILTERING_ENABLED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0631";

    public static final String REPLICARESTORE_READY_TO_PERFORM_WORK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0632";

    public static final String REPLICARESTORE_PROCESSING_REPLICATION_ELEMENT
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0640";

    public static final String REPLICARESTORE_PROCESSING_ADD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0641";

    public static final String REPLICARESTORE_PROCESSING_ADD_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0642";

    public static final String REPLICARESTORE_PROCESSING_DELETE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0643";

    public static final String REPLICARESTORE_PROCESSING_DELETETREE_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0644";

    public static final String REPLICARESTORE_PROCESSING_DELETE_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0645";

    public static final String REPLICARESTORE_PROCESSING_RENAME
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0646";

    public static final String REPLICARESTORE_PROCESSING_RENAME_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0647";

    public static final String REPLICARESTORE_PROCESSING_RENAME_FORMULATED_NEW_NAME
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0648";

    public static final String REPLICARESTORE_PROCESSING_MODIFY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0649";

    public static final String REPLICARESTORE_PROCESSING_MODIFY_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0650";

    public static final String REPLICARESTORE_RENAME_SUCCESSFULLY_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0651";

    public static final String REPLICARESTORE_RENAME_SUCCESSFULLY_COMPLETED_LEAVING_EXISTING_DN_IN_PLACE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0652";

    public static final String REPLICARESTORE_ENTRY_ATTRIBUTES_ADDED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0653";

    public static final String REPLICARESTORE_ENTRY_ATTRIBUTES_MODIFIED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0654";

    public static final String REPLICARESTORE_ENTRY_ATTRIBUTES_DELETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0655";

    public static final String REPLICARESTORE_ATTEMPTING_DIRECTORY_CONNECTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0656";

    public static final String REPLICARESTORE_BLOCKING_ENTRY
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0657";

    public static final String REPLICARESTORE_PROCESSED_REPLICATION_ELEMENT_RESPONSE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0658";

    public static final String REPLICARESTORE_THREAD_SHUTDOWN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0699";

    // ****************************
    // WebAdmin Thread Messages.
    // ****************************
    public static final String WEBADMIN_THREAD_ESTABLISHED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0800";

    public static final String WEBADMIN_BOUND_TO_ADDRESS
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0801";

    public static final String WEBADMIN_ACCEPTOR_THREAD_ESTABLISHED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0802";

    public static final String WEBADMIN_RESPONDER_THREAD_ESTABLISHED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0803";

    public static final String WEBADMIN_ACCEPT_CONNECTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0804";

    public static final String WEBADMIN_DENIED_CONNECTION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0805";

    public static final String WEBADMIN_LAPTIME_FROM_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0807";

    public static final String WEBADMIN_LAPTIME_TO_STACK
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0808";

    public static final String WEBADMIN_THREAD_SHUTDOWN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0809";

    public static final String WEBADMIN_RESPONDER_THREAD_INCOMING_BYTES_READ
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0810";

    public static final String WEBADMIN_RESPONDER_THREAD_SENDING_URI
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0811";

    public static final String WEBADMIN_RESPONDER_THREAD_MEMBER_INDEX_SELECTED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0812";

    public static final String WEBADMIN_RESPONDER_THREAD_SENDING_NAMED_RESOURCE
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0813";

    public static final String WEBADMIN_RESPONDER_THREAD_FORMULATING_OPCMD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0814";

    public static final String WEBADMIN_RESPONDER_THREAD_SENDING_UNAUTH
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0815";

    public static final String WEBADMIN_ACCEPTOR_THREAD_SHUTDOWN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0816";

    public static final String WEBADMIN_RESPONDER_THREAD_SHUTDOWN
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0817";

    public static final String WEBADMIN_THREAD_SHUTDOWN_CHILD_THREADS
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0818";

    // ****************************
    // Facility Shutdown Messages.
    // ****************************
    public static final String SHUTDOWN_REQUESTED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0990";

    public static final String SHUTDOWN_FINISHED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0991";

    public static final String SHUTDOWN_STARTED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0998";

    public static final String SHUTDOWN_COMPLETED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.0999";

    // *************************
    // Generic Method Messages.
    // *************************
    public static final String ENTERING_METHOD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.1000";

    public static final String FINALIZING_METHOD
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.1001";

    // *************************
    // Service Wrapper Messages
    // *************************
    public static final String SERVICE_VERSION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.2000";

    public static final String SERVICE_CONFIGURATION
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.2001";

    public static final String SERVICE_PROPERTIES_OBTAINED
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.2002";

    public static final String SERVICE_VERIFYING_PROPERTIES
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.2003";

    public static final String SERVICE_BOOTING
            = "jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.2004";

} ///:~ End of MessageConstants
