package jeffaschenk.commons.frameworks.cnxidx.resiliency.jgroups;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DiscoveryResponse extends DiscoveryPacket {

    InetSocketAddress discovery_responder = null; // address of member who responds to discovery request
    InetAddress interface_used = null;

    DiscoveryResponse(InetSocketAddress discovery_responder, InetAddress interface_used) {
        this.discovery_responder = discovery_responder;
        this.interface_used = interface_used;
    }


    public String toString() {
        return "DiscoveryResponse [discovery_responder=" + discovery_responder +
                ", interface_used=" + interface_used + ']';
    }

} ///:> End of DiscoveryResponse
