package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.util.LinkedList;
import java.util.List;

/**
 * Maintains Connections List for Accepting and
 * Responding to Web Admin Requests.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

class WebAdminConnectionList {

    // *************************************
    // Globals.

    // *************************************
    // Channel Connection List.
    private List<SocketChannel> list = new LinkedList<>();

    // **************************************
    // Selector to be Notified.
    private Selector selectorToNotify;

    public WebAdminConnectionList(Selector sel) {
        this.selectorToNotify = sel;
    }

    public synchronized void push(SocketChannel newlyConnectedChannel) {
        list.add(newlyConnectedChannel);
        selectorToNotify.wakeup();
    } // End of push public Method.

    public synchronized SocketChannel removeFirst() {
        if (list.size() == 0) {
            return null;
        }
        return list.remove(0);
    } // End of RemoveFirst public Method.
} ///:~ End of WebAdminConnectionList Class.

