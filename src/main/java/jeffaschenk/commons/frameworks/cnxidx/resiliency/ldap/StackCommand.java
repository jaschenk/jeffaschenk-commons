package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.io.Serializable;

/**
 * Provides a Common StackCommand Object to efficently cycle through a
 * Stack. @see IRRChangeLogRestoreServiceControl
 *
 * @author jeff.schenk
 * @since 2005.10.26
 */
public class StackCommand implements Serializable {

    // ****************************************
    // Global Variables for Control Language
    public static final int CL_END_OF_THREAD = -1;
    public static final int CL_ECHO = 0;
    public static final int CL_ECHO_REPLY = 1;
    public static final int CL_STATUS = 2;
    public static final int CL_STATUS_REPLY = 3;
    public static final int CL_RESET_STATS = 4;
    public static final int CL_SHUTDOWN = 9;

    // **********************************************
    // Control State Commands and get distributed to
    // the Control Stack.
    public static final int CL_WAS_PRIMARY = 10;
    public static final int CL_PRIMARY = 11;
    public static final int CL_PEER = 12;

    //  ****************************************
    // Process Commands for the Restore Replica Stack.
    public static final int CL_REPLICATE = 20;
    public static final int CL_REPLICATE_RESPONSE = 21;

    //  ****************************************
    // Process Commands for the Log Reader Stack.
    public static final int CL_PROCESS_LOGS = 30;
    public static final int CL_MARK_POINT = 32;
    public static final int CL_MARK_AS_PROCESSED = 33;
    public static final int CL_REMOVE_MARK_POINT = 34;
    public static final int CL_REMOVE_LOG = 35;

    // **********************************************
    // Control State Commands and get distributed to
    // the Control Stack.
    public static final int CL_START_READER = 60;
    public static final int CL_STOP_READER = 61;
    public static final int CL_READER_READY = 62;

    public static final int CL_START_REPLICA = 70;
    public static final int CL_STOP_REPLICA = 71;
    public static final int CL_REPLICA_READY = 72;

    public static final int CL_HOUSEKEEPING_BEGIN_PRIMARY = 80;
    public static final int CL_HOUSEKEEPING_BEGIN_PEER = 81;
    public static final int CL_HOUSEKEEPING_READY = 82;
    public static final int CL_HOUSEKEEPING_DONE = 83;

    // ********************************************
    // Default Catch all For Error Processing.
    public static final int CL_INVALID = 99;

    // *****************************************
    // Fields
    private int commandType = 0;
    private String[] arguments;
    private Object originator;
    private Object destination;
    private ChangeIdentifier identifier;
    private Object object;

    /**
     * Default Constructor for Stack Command.
     */
    public StackCommand() {
    }

    /**
     * Constructor specifying All Fields.
     */
    public StackCommand(int commandType,
                        String[] arguments,
                        Object originator,
                        Object destination,
                        ChangeIdentifier identifier,
                        Object object) {
        this.commandType = commandType;
        this.arguments = arguments;
        this.originator = originator;
        this.destination = destination;
        this.identifier = identifier;
        this.object = object;
    } // End of Constructor.


    /**
     * Constructor specifying Command Type and Originator.
     */
    public StackCommand(int commandType, Object originator) {
        this.commandType = commandType;
        this.originator = originator;
        this.destination = null;
        this.arguments = null;
        this.identifier = null;
        this.object = null;
    } // End of Constructor.

    /**
     * Constructor specifying All Fields.
     */
    public StackCommand(int commandType, Object originator, String[] arguments) {
        this.commandType = commandType;
        this.originator = originator;
        this.destination = null;
        this.arguments = arguments;
        this.identifier = null;
        this.object = null;
    } // End of Constructor.

    /**
     * Constructor specifying All Fields.
     */
    public StackCommand(int commandType, Object originator, Object object) {
        this.commandType = commandType;
        this.originator = originator;
        this.destination = null;
        this.arguments = null;
        this.identifier = null;
        this.object = object;
    } // End of Constructor.

    /**
     * Constructor specifying All Fields.
     */
    public StackCommand(int commandType, Object originator, Object destination, Object object) {
        this.commandType = commandType;
        this.originator = originator;
        this.destination = destination;
        this.arguments = null;
        this.identifier = null;
        this.object = object;
    } // End of Constructor.

    /**
     * Get Command Type
     */
    public int getCommandType() {
        return this.commandType;
    } // End of get Command Type.

    /**
     * Set Command Type
     */
    public void setCommandType(int commandType) {
        this.commandType = commandType;
    } // End of get Command Type.

    /**
     * Get Command Arguments
     */
    public String[] getArguments() {
        return this.arguments;
    } // End of get Command Arguments.

    /**
     * Set Command Arguments
     */
    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    } // End of get Command Arguments.

    /**
     * Get Originator
     */
    public Object getOriginator() {
        return this.originator;
    } // End of get Originator.

    /**
     * Set Originator
     */
    public void setOriginator(Object originator) {
        this.originator = originator;
    } // End of set Originator

    /**
     * Get Destination
     */
    public Object getDestination() {
        return this.destination;
    } // End of get Destination.

    /**
     * Set Destination
     */
    public void setDestination(Object destination) {
        this.destination = destination;
    } // End of set Destination

    /**
     * Get Identifier
     */
    public ChangeIdentifier getIdentifier() {
        return this.identifier;
    } // End of get Identifier.

    /**
     * Set Identifier
     */
    public void setIdentifier(ChangeIdentifier identifier) {
        this.identifier = identifier;
    } // End of set Identifier.

    /**
     * Get Object
     */
    public Object getObject() {
        return this.object;
    } // End of get Object.

    /**
     * Set Object
     */
    public void setObject(Object object) {
        this.object = object;
    } // End of set Object.

    /**
     * toString.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("StackCommand:[ commandName:[" + this.getCommandTypeName() + "]");
        sb.append(", commandType:[" + this.commandType + "]");
        sb.append(", Originator:[" + this.originator + "]");
        sb.append(", Destination:[" + this.destination + "]");
        if (this.identifier == null) {
            sb.append(", ID:[" + this.identifier + "]");
        } else {
            sb.append(", " + this.identifier);
        }
        sb.append(", Object Payload:[" + object + "]");
        sb.append("]");
        return sb.toString();
    } // End of tostring Method.

    /**
     * Obtains Command Type Name.
     */
    public String getCommandTypeName() {
        String name = null;
        switch (this.commandType) {
            case CL_END_OF_THREAD:
                name = "End of Thread";
                break;
            case CL_ECHO:
                name = "Echo";
                break;
            case CL_ECHO_REPLY:
                name = "Echo Reply";
                break;
            case CL_STATUS:
                name = "Status";
                break;
            case CL_STATUS_REPLY:
                name = "Status Reply";
                break;
            case CL_WAS_PRIMARY:
                name = "Was Primary";
                break;
            case CL_PRIMARY:
                name = "Primary";
                break;
            case CL_PEER:
                name = "Peer";
                break;
            case CL_START_READER:
                name = "Start Reader";
                break;
            case CL_STOP_READER:
                name = "Stop Reader";
                break;
            case CL_READER_READY:
                name = "Reader Ready";
                break;
            case CL_START_REPLICA:
                name = "Start Reader";
                break;
            case CL_STOP_REPLICA:
                name = "Stop Reader";
                break;
            case CL_REPLICA_READY:
                name = "Replica Ready";
                break;
            case CL_HOUSEKEEPING_BEGIN_PRIMARY:
                name = "Begin Housingkeeping for Primary";
                break;
            case CL_HOUSEKEEPING_BEGIN_PEER:
                name = "Begin Housekeeping for Peer";
                break;
            case CL_HOUSEKEEPING_READY:
                name = "Housekeeping Ready";
                break;
            case CL_HOUSEKEEPING_DONE:
                name = "Housekeeping Done";
                break;
            case CL_REPLICATE:
                name = "Replicate Change";
                break;
            case CL_REPLICATE_RESPONSE:
                name = "Replicate Change Response";
                break;
            case CL_PROCESS_LOGS:
                name = "Process Logs";
                break;
            case CL_MARK_POINT:
                name = "Mark Point";
                break;
            case CL_MARK_AS_PROCESSED:
                name = "Mark As Processed";
                break;
            case CL_REMOVE_MARK_POINT:
                name = "Remove Mark Point";
                break;
            case CL_REMOVE_LOG:
                name = "Remove Log";
                break;
            case CL_SHUTDOWN:
                name = "Shutdown";
                break;
            case CL_INVALID:
                name = "Invalid";
                break;
            default:
                name = "Unknown";
        } // End of Switch.
        return name;
    } // End of getCommandTypeName.

} ///:~ End of StackCommand Class.
