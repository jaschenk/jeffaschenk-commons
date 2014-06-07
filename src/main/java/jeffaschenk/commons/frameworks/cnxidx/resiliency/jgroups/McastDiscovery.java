
package jeffaschenk.commons.frameworks.cnxidx.resiliency.jgroups;

import org.jgroups.util.Util;

import java.net.*;
import java.util.*;


/**
 * Discovers all neighbors in an IP multicast environment by using expanding ring multicasts (increasing TTL).
 * The sender multicasts a discovery packet on all available network interfaces, while also listening on
 * all interfaces. The discovery packet contains the sender's address, which is the address and port of the
 * interface on which the packet was sent. A receiver replies with an ACK back to the sender's address and port.
 * After n responses or m milliseconds, the sender terminates and computes the network interfaces which should be used.
 * The network interface is the intersection of the interface variable of all ACKs received.
 *
 * @author Bela Ban July 26 2002
 * @version $Revision: 1.3 $
 */
public class McastDiscovery {
    int ttl = 32;
    List<MessageHandler> handlers = new ArrayList<>();
    InetAddress mcast_addr = null;
    int mcast_port = 5000;
    long interval = 2000; // time between sends
    McastSender mcast_sender = null;
    boolean running = true;
    HashMap<InetAddress,List<InetAddress>> map = new HashMap<>(); // keys=interface (InetAddress), values=List of receivers (InetAddress)

    class McastSender extends Thread {

        McastSender() {
            super();
            setName("McastSender");
            setDaemon(true);
        }

        public void run() {
            MessageHandler handler;
            while (running) {
                for (Iterator it = handlers.iterator(); it.hasNext(); ) {
                    handler = (MessageHandler) it.next();
                    handler.sendDiscoveryRequest(ttl);
                }
                try {
                    sleep(interval);
                } catch (Exception ex) {
                }
            }
        }
    }


    public McastDiscovery(InetAddress mcast_addr, int mcast_port, long interval, int ttl) {
        this.mcast_addr = mcast_addr;
        this.mcast_port = mcast_port;
        this.interval = interval;
        this.ttl = ttl;
    }


    public void start() throws Exception {
        NetworkInterface intf;
        InetAddress bind_addr;
        MessageHandler handler;

        for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            intf = (NetworkInterface) en.nextElement();
            for (Enumeration en2 = intf.getInetAddresses(); en2.hasMoreElements(); ) {
                bind_addr = (InetAddress) en2.nextElement();
                map.put(bind_addr, new ArrayList<InetAddress>());
                System.out.println("-- creating receiver for " + bind_addr);
                handler = new MessageHandler(bind_addr);
                handlers.add(handler);
                handler.start();
            }
        }

        // Now start sending mcast discovery messages:
        mcast_sender = new McastSender();
        mcast_sender.start();

        System.out.println("press key to stop");
        System.out.flush();
        System.in.read();
        running = false; // Indicate all should stop....
        printValidInterfaces();
    }


    protected void printValidInterfaces() {
        InetAddress bind_interface;
        Map.Entry entry;
        List<InetSocketAddress> all_mbrs = new ArrayList<>();
        List l;
        InetSocketAddress tmp_addr;
        HashMap map_copy = (HashMap) map.clone();
        SortedSet<DiscoveryResult> s;
        Stack<DiscoveryResult>  st;
        DiscoveryResult r;


        System.out.println("\n========================================================");
        System.out.println("Responses received ordered by interface:\n");
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            entry = (Map.Entry) it.next();
            bind_interface = (InetAddress) entry.getKey();
            System.out.println(bind_interface.getHostAddress() + ":\t " + entry.getValue());
        }

        for (Iterator it = map.values().iterator(); it.hasNext(); ) {
            l = (List) it.next();
            for (Iterator it2 = l.iterator(); it2.hasNext(); ) {
                tmp_addr = (InetSocketAddress) it2.next();
                if (!all_mbrs.contains(tmp_addr))
                    all_mbrs.add(tmp_addr);
            }
        }

        for (Iterator it = all_mbrs.iterator(); it.hasNext(); ) {
            tmp_addr = (InetSocketAddress) it.next();

            // tmp_mbr has to be in all values (Lists) of map, remove entry from map if not
            for (Iterator it2 = map.entrySet().iterator(); it2.hasNext(); ) {
                entry = (Map.Entry) it2.next();
                l = (List) entry.getValue();

                if (!l.contains(tmp_addr)) {
                    //System.out.println("Member " + tmp_addr + " did not respond to interface " + entry.getKey() +
                    //	       ", removing interface");
                    it2.remove(); // remove the entry (key plus value) from map
                }
            }
        }

        if (map.size() > 0)
            System.out.println("\n-- Valid interfaces are " + map.keySet() + '\n');
        else {
            System.out.println("\nNo valid interfaces found, listing interfaces by number of responses/interface:\n" +
                    "(it is best to use the interface with the most responses)");

            s = new TreeSet<>();
            for (Iterator it = map_copy.entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry) it.next();
                r = new DiscoveryResult((InetAddress) entry.getKey(), ((List) entry.getValue()).size());
                s.add(r);
            }

            st = new Stack<>();
            for (Iterator<DiscoveryResult> it = s.iterator(); it.hasNext(); ) {
                st.push(it.next());
            }

            while (!st.empty())
                System.out.println("-- " + st.pop());

        }

        System.out.println("\nUse of any of the above interfaces in \"UDP(bind_addr=<interface>)\" will " +
                "guarantee that the members will find each other");
        System.out.println("========================================================\n\n");
    }


    class MessageHandler {
        MulticastSocket mcast_sock = null; // for receiving mcast discovery messages and sending back unicast discovery responses
        DatagramSocket sock = null;
        McastReceiver mcast_receiver = null;
        UcastReceiver ucast_receiver = null;
        InetAddress local_addr = null;
        int local_port = 0;


        class McastReceiver extends Thread {
            byte[] buf;
            DatagramPacket mcast_packet, rsp_packet;
            DiscoveryRequest req;
            DiscoveryResponse rsp;

            McastReceiver() {
                super();
                setName("McastReceiver");
                setDaemon(true);
            }

            public void run() {
                while (running) {
                    buf = new byte[16000];
                    mcast_packet = new DatagramPacket(buf, buf.length);
                    try {
                        mcast_sock.receive(mcast_packet);
                        req = (DiscoveryRequest) Util.objectFromByteBuffer(mcast_packet.getData());
                        System.out.println("<-- " + req);

                        // send response back to req.sender_addr
                        rsp = new DiscoveryResponse(new InetSocketAddress(local_addr, local_port), req.sender_addr.getAddress());
                        buf = Util.objectToByteBuffer(rsp);
                        rsp_packet = new DatagramPacket(buf, buf.length, req.sender_addr);
                        sock.send(rsp_packet);
                    } catch (Exception ex) {
                        System.err.println("McastReceiver.run(): " + ex + ", rsp_packet=" +
                                rsp_packet.getSocketAddress() + ", length=" + rsp_packet.getLength() + " bytes");
                        ex.printStackTrace();
                    }
                }
            }
        }


        class UcastReceiver extends Thread {

            UcastReceiver() {
                super();
                setName("UcastReceiver");
                setDaemon(true);
            }

            public void run() {
                DatagramPacket packet;
                byte[] buf;
                DiscoveryResponse rsp;
                List<InetAddress> l;
                InetAddress bind_interface;
                InetSocketAddress responder_addr;

                while (running) {
                    try {
                        buf = new byte[16000];
                        packet = new DatagramPacket(buf, buf.length);
                        sock.receive(packet);
                        rsp = (DiscoveryResponse) Util.objectFromByteBuffer(packet.getData());
                        System.out.println("<== " + rsp);
                        bind_interface = rsp.interface_used;
                        l = map.get(bind_interface);
                        if (l == null)
                            map.put(bind_interface, (l = new ArrayList<>()));
                        responder_addr = rsp.discovery_responder;
                        if (!l.contains(responder_addr))
                            l.add(responder_addr.getAddress());
                    } catch (Exception ex) {
                        System.err.println("UcastReceiver.run(): " + ex);
                    }
                }
            }
        }


        MessageHandler(InetAddress bind_interface) throws Exception {
            mcast_sock = new MulticastSocket(mcast_port);
            mcast_sock.setInterface(bind_interface);
            mcast_sock.setTimeToLive(ttl);
            mcast_sock.joinGroup(mcast_addr);
            sock = new DatagramSocket(0, bind_interface);
            local_addr = sock.getLocalAddress();
            local_port = sock.getLocalPort();
        }

        void start() throws Exception {
            running = true;

            // 1. start listening on unicast socket. when discovery response received --> ad to map hashmap
            ucast_receiver = new UcastReceiver();
            ucast_receiver.start();

            // 2. start listening on mcast socket. when discovery request received --> send ack
            mcast_receiver = new McastReceiver();
            mcast_receiver.start();
        }

        void stop() {
            running = false;

            if (mcast_sock != null) {
                mcast_sock.close();
                mcast_sock = null;
            }
            if (sock != null) {
                sock.close();
                sock = null;
            }
        }


        void sendDiscoveryRequest(int ttl) {
            DiscoveryRequest req;
            byte[] buf;
            DatagramPacket packet;

            req = new DiscoveryRequest(local_addr, local_port);
            System.out.println("--> " + req);

            try {
                buf = Util.objectToByteBuffer(req);
                packet = new DatagramPacket(buf, buf.length, mcast_addr, mcast_port);
                mcast_sock.send(packet);
            } catch (Exception ex) {
                System.err.println("McastDiscovery.sendDiscoveryRequest(): " + ex);
            }
        }


    } // End of MessageHandler Internal Class.


} ///:> End of McastDiscovery Class.
