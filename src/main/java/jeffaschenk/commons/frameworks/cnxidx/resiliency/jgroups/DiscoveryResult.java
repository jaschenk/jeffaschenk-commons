package jeffaschenk.commons.frameworks.cnxidx.resiliency.jgroups;

import java.net.InetAddress;

public class DiscoveryResult implements Comparable {

    InetAddress bind_interface = null;
    int num_responses = 0;

    DiscoveryResult(InetAddress bind_interface, int num_responses) {
        this.bind_interface = bind_interface;
        this.num_responses = num_responses;
    }

    public int compareTo(Object other) {
        DiscoveryResult oth = (DiscoveryResult) other;
        return num_responses == oth.num_responses ? 0 : (num_responses < oth.num_responses ? -1 : 1);
    }

    public String toString() {
        return bind_interface.getHostAddress() + ":\t " + num_responses;
    }
}
