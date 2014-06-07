package jeffaschenk.commons.touchpoint.model.threads;

/**
 * CircularObjectStackNode.java
 * <p/>
 * Created on September 29, 2005, 7:12 PM
 *
 * @author jeff.schenk
 */
public class CircularObjectStackNode {

    protected Object object = null;
    protected CircularObjectStackNode next_object = null;
    protected CircularObjectStackNode previous_object = null;

    /**
     * Creates a new instance of CircularObjectStackNode
     */
    protected CircularObjectStackNode() {
    }
} ///:~ End of CircularObjectStackNode Object.
