package jeffaschenk.commons.frameworks.cnxidx.resiliency.jgroups;

import org.jgroups.util.Util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class McastAckReceiverThread implements Runnable {

    // *******************************************
    // Globals Fields.
    private DatagramSocket sock;
    private DatagramPacket packet;
    private byte[] buf;
    private Thread t = null;
    private String prompt = null;
    private boolean show_received_data = true;

    /**
     * Default Constructor.
     *
     * @param sock
     */
    McastAckReceiverThread(DatagramSocket sock) {
        this.sock = sock;
    } // End of Constructor.

    /**
     * Constructor with String Prompt.
     *
     * @param sock
     * @param prompt
     */
    McastAckReceiverThread(DatagramSocket sock, String prompt) {
        this.sock = sock;
        this.prompt = prompt;
    } // End of Constructor.

    /**
     * Constructor to provide flag if we should infact show
     * received packets or not.  Normally a sender will
     * received, but just not show the packets.
     *
     * @param sock
     * @param prompt
     * @param show_received_data
     */
    McastAckReceiverThread(DatagramSocket sock, String prompt,
                           boolean show_received_data) {
        this.sock = sock;
        this.prompt = prompt;
        this.show_received_data = show_received_data;
    } // End of Constructor.

    /**
     * Thread Processing Loop.
     */
    public void run() {

        try {
            while (t != null) {
                buf = new byte[16384];
                packet = new DatagramPacket(buf, buf.length);

                // *****************************
                // We will block until we
                // receive a packet.
                sock.receive(packet);

                // ******************************
                // Unserialize the Object that
                // was sent.
                Object robject = null;
                try {
                    robject = Util.objectFromByteBuffer(packet.getData());
                } catch (Exception e) {
                    robject = null;
                } // End of Exception Processing.

                // ******************************
                // Do we need to show the data?
                if (!this.show_received_data) {
                    continue;
                }

                // ******************************
                // Check for Null Object.
                if (robject == null) {
                    continue;
                }

                // ******************************
                // Ignore any Discovery Packets.
                if ((robject instanceof DiscoveryPacket) ||
                        (robject instanceof DiscoveryResult)) {
                    continue;
                }

                // *****************************
                // Show the Received Data.
                System.out.println("    << Received packet sender:" +
                        packet.getAddress().getHostAddress() + ':' +
                        packet.getPort() + ", Data:[" + new String(robject.toString())
                        + "]");
                if (this.prompt != null) {
                    System.out.print(this.prompt);
                }
            } // End of While Loop.
        } catch (Exception e) {
            System.err.println(e);
        } // end of Exception processing.
    } // End of Run.

    /**
     * Start the Thread.
     */
    void start() {
        t = new Thread(this, "McastAckReceiverThread");
        t.start();
    } // End of start method.

    /**
     * Stop the Thread.
     */
    void stop() {
        if (t != null && t.isAlive()) {
            t = null;
            try {
                sock.close();
            } catch (Exception e) {
            } // End of Exception processing.
        } // End of If Check for valid Thread.
    } // End of Stop Method.

} ///:> End of McastAckReceiverThread.
