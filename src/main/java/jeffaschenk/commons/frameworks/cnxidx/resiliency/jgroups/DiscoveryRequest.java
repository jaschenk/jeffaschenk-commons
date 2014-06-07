package jeffaschenk.commons.frameworks.cnxidx.resiliency.jgroups;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DiscoveryRequest extends DiscoveryPacket {
    InetSocketAddress sender_addr = null;

    DiscoveryRequest(InetAddress addr, int port) {
        sender_addr = new InetSocketAddress(addr, port);
    }


    public String toString() {
        return "DiscoveryRequest [sender_addr=" + sender_addr + ']';
    }

} ///:> End of DiscoveryRequest Class.
