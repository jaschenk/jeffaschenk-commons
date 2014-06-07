package jeffaschenk.commons.touchpoint.model.threads;

/**
 * CircularObjectStack.java
 * Provides a Stack Accessible from Multiple Threads by
 * Synchronizing access methods for the Stack.
 * <p/>
 * Created on September 29, 2005, 7:16 PM
 *
 * @author jeff.schenk
 */
public class CircularObjectStack {
    private CircularObjectStackNode head_stacknode = null;
    private CircularObjectStackNode tail_stacknode = null;

    // **************************
    // Constants for Switch.
    private static final int STACK_PUSH_OPERATION = 1;
    private static final int STACK_POP_OPERATION = 2;
    private static final int STACK_INSERT_OPERATION = 3;
    private static final int STACK_GETNEXT_OPERATION = 4;

    /**
     * Creates a new instance of CircularObjectStack
     */
    public CircularObjectStack() {
    }

    /**
     * push, Will Insert an Object Reference to the Bottom of the Stack.
     */
    public synchronized void push(Object o) {
        this.stackOperation(o, STACK_PUSH_OPERATION);
    } // End of push Method.

    /**
     * insert, Will Insert an Object Reference to the Top of the Stack.
     */
    public synchronized void insert(Object o) {
        this.stackOperation(o, STACK_INSERT_OPERATION);
    } // End of insert Method.

    /**
     * locate, Will locate an Object in the Stack.
     */
    public synchronized Object locate(Object o) {
        if (this.head_stacknode == null) {
            return null;
        }
        // ********************************
        // Iterate to Find Object In Stack.
        CircularObjectStackNode stacknode = this.head_stacknode;
        do {
            if (stacknode.object == o) {
                return stacknode.object;
            }
            stacknode = stacknode.next_object;
        } while (stacknode != this.head_stacknode);
        // ***************************************
        // Return Null for a Not Found Condition.
        return null;
    } // End of locate Method.

    /**
     * showsStack in String Form.
     */
    public synchronized String showStack() {
        StringBuffer sb = new StringBuffer();
        if (this.head_stacknode == null) {
            return null;
        }
        // ********************************
        // Iterate to show Objects In Stack.
        CircularObjectStackNode stacknode = this.head_stacknode;
        sb.append("[");
        do {
            sb.append(" -> ");
            sb.append(stacknode.object.toString());
            stacknode = stacknode.next_object;
        } while (stacknode != this.head_stacknode);
        // ***************************************
        // Return String
        sb.append("]");
        return sb.toString();
    } // End of showStack Method.

    /**
     * showsStack in String Form.
     */
    public synchronized String showReverseStack() {
        StringBuffer sb = new StringBuffer();
        if (this.tail_stacknode == null) {
            return null;
        }
        // ********************************
        // Iterate to show Objects In Stack.
        CircularObjectStackNode stacknode = this.tail_stacknode;
        sb.append("[");
        do {
            sb.append(" <- ");
            sb.append(stacknode.object.toString());
            stacknode = stacknode.previous_object;
        } while (stacknode != this.tail_stacknode);
        // ***************************************
        // Return String
        sb.append("]");
        return sb.toString();
    } // End of showStack Method.

    /**
     * getNext, Will Obtain Next Object from the Stack.
     * Will continue Circular Path Never Ending, until
     * Stack is Empty.
     */
    public synchronized Object getNext() {
        return this.stackOperation(null, STACK_GETNEXT_OPERATION);
    } // End of getNext Method.

    /**
     * pop, Will Obtain Last Object from the Stack.
     * Will continue Circular Path Never Ending, unit
     * Stack is Empty.
     */
    public synchronized Object pop() {
        return this.stackOperation(null, STACK_POP_OPERATION);
    } // End of pop Method.

    /**
     * hasModeNodes provides an indication if
     * we in fact at this time have more Nodes in the
     * Stack.
     * If this is true and you perform a getNext/pull or
     * pop and you can receive a null object.  This indicates
     * that another thread has already taken the last
     * object from the stack.
     */
    public synchronized boolean hasMoreNodes() {
        if (this.head_stacknode == null) {
            return false;
        }
        return true;
    } // End of hasMoreNodes Method.

    /**
     * Size Method to obtian number of objects
     * in the Node Stack.
     */
    public synchronized int size() {
        int size = 0;
        if (this.head_stacknode == null) {
            return size;
        }
        // ********************************
        // Iterate to Find Object In Stack.
        CircularObjectStackNode stacknode = this.head_stacknode;
        do {
            size++;
            stacknode = stacknode.next_object;
        } while (stacknode != this.head_stacknode);
        // ***************************************
        // Return Null for a Not Found Condition.
        return size;
    } // End of size Method.

    // ************************************
    // PRIVATE COMMON METHODS.
    // ************************************

    /**
     * Private Method to perform a Find of an Object in the Stack.
     */
    private synchronized CircularObjectStackNode find(Object o) {
        if (this.head_stacknode == null) {
            return null;
        }
        // ********************************
        // Iterate to Find Object In Stack.
        CircularObjectStackNode stacknode = this.head_stacknode;
        do {
            if (stacknode.object == o) {
                return stacknode;
            }
            stacknode = stacknode.next_object;
        } while (stacknode != this.head_stacknode);
        // ***************************************
        // Return Null for a Not Found Condition.
        return null;
    } // End of find Method.

    /**
     * stackOperation, Will provide common private method to synchronize Insert
     * Push, POP, GetNext behavior to/from the Object Stack.
     */
    private synchronized Object stackOperation(Object o, int operation) {
        // **************************
        // Initialize.
        boolean push = false;
        Object robject = null;

        // *****************************
        // Switch based Upon Operation.
        switch (operation) {
            // *********************************
            // Perform PUSH or INSERT to place
            // Entry onto the Stack.
            case STACK_PUSH_OPERATION:
                push = true;
            case STACK_INSERT_OPERATION:
                CircularObjectStackNode stacknode = new CircularObjectStackNode();
                stacknode.object = o;
                if (this.head_stacknode == null) {
                    stacknode.next_object =
                            stacknode.previous_object = stacknode;
                    this.head_stacknode = this.tail_stacknode = stacknode;
                } // End of If check for First Insertion. 
                else if (push) {
                    stacknode.previous_object = this.tail_stacknode;
                    stacknode.next_object = this.head_stacknode;
                    this.tail_stacknode.next_object = stacknode;
                    this.head_stacknode.previous_object = stacknode;
                    this.tail_stacknode = stacknode;
                } // End of Push to Bottom of Stack.
                else {
                    stacknode.next_object = this.head_stacknode;
                    stacknode.previous_object = this.tail_stacknode;
                    this.head_stacknode.previous_object = stacknode;
                    this.tail_stacknode.next_object = stacknode;
                    this.head_stacknode = stacknode;
                } // End of Insert to Top of Stack.
                break;

            // *********************************
            // Perform Getnext, final processing
            // will deleted this object from the
            // stack.
            case STACK_GETNEXT_OPERATION:
                if (this.head_stacknode != null) {
                    robject = this.head_stacknode.object;
                    this.removeHead(robject);
                } // End of Check for Nothing in Stack.
                break;

            // *********************************
            // Perform POP, final processing
            // will deleted this object from the
            // stack.
            case STACK_POP_OPERATION:
                if (this.tail_stacknode != null) {
                    robject = this.tail_stacknode.object;
                    this.removeTail(robject);
                } // End of Check for Nothing in Stack.
                break;

            // *********************************
            // Default do Nothing.
            default:
        } // End of Switch statement.

        // ***************************
        // Return Object, could
        // be "NULL" depending upon
        // Operation.
        return robject;

    } // End of stackOperation Common synchronized Private Method. 

    /**
     * Private Remove Method to remove a reference from the top of the stack.
     */
    private synchronized void removeHead(Object o) {
        //System.out.println("++ Removing Current Head Object:"+o.toString());
        // ******************************
        // Check for Last Node in the
        // Stack.
        if (this.head_stacknode == this.tail_stacknode) {
            this.head_stacknode = this.tail_stacknode = null;
            return;
        } // End of Check for Last Node in Stack.

        // ************************************
        // Remove Reference and Join Together.
        this.head_stacknode = this.head_stacknode.next_object;
        this.head_stacknode.previous_object = this.tail_stacknode;
        this.tail_stacknode.next_object = this.head_stacknode;
    } // End of deleteHead Method.

    /**
     * Private Remove Method to remove a reference from the bottom of the stack.
     */
    private synchronized void removeTail(Object o) {
        //System.out.println("++ Removing Current Tail Object:"+o.toString());
        // ******************************
        // Check for Last Node in the
        // Stack.
        if (this.head_stacknode == this.tail_stacknode) {
            this.head_stacknode = this.tail_stacknode = null;
            return;
        } // End of Check for Last Node in Stack.
        // ************************************
        // Remove Reference and Join Together.
        this.tail_stacknode = this.tail_stacknode.previous_object;
        this.tail_stacknode.next_object = this.head_stacknode;
        this.head_stacknode.previous_object = this.tail_stacknode;
    } // End of deleteTail Method.
} ///:~ End of CircularObjectStack Class.
