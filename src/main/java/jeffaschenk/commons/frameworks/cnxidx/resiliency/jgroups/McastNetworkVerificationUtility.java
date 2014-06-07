package jeffaschenk.commons.frameworks.cnxidx.resiliency.jgroups;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap.IRRChangeLogRestoreService;
import jeffaschenk.commons.frameworks.cnxidx.shell.Shell;
import org.jgroups.util.Util;

/**
 * Multicast Network Verification Utility allows an Instance to perform
 * Sending, Receiving or Discovering Members of the Multicast Group.
 * <p/>
 * These facilities have been adapted from the Test Utilities found in the
 * JGroups Library, these are:
 * org.jgroups.tests.McastDiscovery1_4
 * org.jgroups.tests.McastReceiverTest1_4
 * org.jgroups.tests.McastSenderTest1_4
 * <p/>
 * Bascially all of the information for driving the Verification Utility will
 * come from the Service Properties, which should have been set up prior
 * to using this utility.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2006
 */
public class McastNetworkVerificationUtility extends Shell
        implements idxCMDReturnCodes {
    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = McastNetworkVerificationUtility.class.getName();

    // **************************************
    // BackEnd Information
    public static final String BACKEND = "MULTICAST";

    // ********************************
    // Default IP Addresses.
    public static final String IPV4_LOOPBACK_ADDRESS
            = "127.0.0.1";
    public static final String IPV6_LOOPBACK_ADDRESS
            = "0:0:0:0:0:0:0:1";
    public static final String IPV6_UNSPECIFIED_ADDRESS
            = "0:0:0:0:0:0:0:0";
    public static final String IPV6_SHORT_LOOPBACK_ADDRESS
            = "::1";
    public static final String IPV6_SHORT_UNSPECIFIED_ADDRESS
            = "::";

    // ********************************
    // Default Shell Prompt
    public static final String shell_name = "idxmcast";
    public static final String shell_prompt = shell_name + "> ";
    public static final String discover_shell_prompt = shell_name + " Discover> ";
    public static final String receive_shell_prompt = shell_name + " Receive> ";
    public static final String send_shell_prompt = shell_name + " Send> ";

    // ********************************
    // Static Values.
    public static final String ALL = "All";

    // ********************************
    // Global Fields.
    public static final String DEFAULT_MULTICAST_ADDRESS = "224.0.0.35";
    private String MULTICAST_ADDRESS = null;

    public static final int DEFAULT_MULTICAST_PORT = 45566;
    private String MULTICAST_PORT = null;

    public static final String DEFAULT_BIND_ADDRESS = ALL;
    private String BIND_ADDRESS = null;

    private boolean PROPERTIES_NOT_FOUND = false;
    private boolean PROERTIES_FAILED_TO_LOAD = false;

    // *****************************
    // Time to Live
    public static final int DEFAULT_TTL = 32;
    private int TTL = DEFAULT_TTL;

    // *****************************
    // Discovery Internal.
    // in Milliseconds.
    public static final long DEFAULT_DISCOVERY_INTERVAL = 2000;
    private long DISCOVERY_INTERVAL = 2000;


    /**
     * Default Constructor.
     */
    public McastNetworkVerificationUtility() {
        super(false);
        super.setBackEnd(McastNetworkVerificationUtility.BACKEND);
        super.setPrompt(McastNetworkVerificationUtility.shell_prompt);
    } // End of Constructor.

    /**
     * Constructor, providing all necessary input fields
     * to begin a verification process.
     */
    public McastNetworkVerificationUtility(String MULTICAST_ADDRESS,
                                           String MULTICAST_PORT,
                                           boolean PROPERTIES_NOT_FOUND,
                                           boolean PROERTIES_FAILED_TO_LOAD) {
        super(false);
        super.setBackEnd(McastNetworkVerificationUtility.BACKEND);
        super.setPrompt(McastNetworkVerificationUtility.shell_prompt);
        // **********************
        // Save our defaults.
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.MULTICAST_PORT = MULTICAST_PORT;
        this.PROPERTIES_NOT_FOUND = PROPERTIES_NOT_FOUND;
        this.PROERTIES_FAILED_TO_LOAD = PROERTIES_FAILED_TO_LOAD;
    } // End of Constructor.

    /**
     * Command Line Interactive Mode Shell.
     */
    public int CMDprocess(LinkedList _cmdargs)
            throws InterruptedException, IOException {

        // ********************
        // Do we have any
        // Command at All?
        if (_cmdargs.size() < 1) {
            return (0);
        }

        // ********************
        // Check for Command
        String COMMAND = (String) _cmdargs.get(0);

        if (COMMAND.equalsIgnoreCase("discover")) {
            return (CMDdiscover());
        }

        if (COMMAND.equalsIgnoreCase("receive")) {
            return (CMDreceive());
        }

        if (COMMAND.equalsIgnoreCase("send")) {
            return (CMDsend());
        }

        if (COMMAND.equalsIgnoreCase("set")) {
            return (CMDset(_cmdargs));
        }

        if (COMMAND.equalsIgnoreCase("show")) {
            return (CMDshow());
        }

        if (COMMAND.equalsIgnoreCase("showver")) {
            return (CMDshowver());
        }

        if (COMMAND.equalsIgnoreCase("echo")) {
            return (CMDecho(_cmdargs));
        }

        // *******************************
        // Check for a Help/usage Command.
        if ((COMMAND.equalsIgnoreCase("help")) ||
                (COMMAND.equalsIgnoreCase("usage")) ||
                (COMMAND.startsWith("?"))) {
            return (CMDusage());
        }

        // ***************************
        // Check for an Exit Command.
        if ((COMMAND.equalsIgnoreCase("exit")) ||
                (COMMAND.equalsIgnoreCase("end")) ||
                (COMMAND.equalsIgnoreCase("quit"))) {
            return (SHELL_RETURN_CODE_EXIT);
        }


        // ***************************
        // If we fell through then we
        // have an invalid command.
        displayMsg("Specified Command '" + COMMAND + "', is Invalid.");
        displayMsg("Please use 'help' for Commands and Usage.");

        // ********************
        // Return
        return (0);
    } // End of CMDprocess Method.

    // **************************************************************
    // Commands
    // **************************************************************

    /**
     * usage command to simply display valid commands.
     */
    public int CMDusage() {
        // **********************
        // Show Usage.
        displayMsg("FRAMEWORK Data Resiliency Multicast Network Verification Utility");
        displayMsg("Usage: " + CLASSNAME);
        displayMsg("\tAvailable Functions:");
        displayMsg("\tdiscover      - Discover Multicast Members in Group.");
        displayMsg("\treceive       - Receive Data from Multicast Members in Group.");
        displayMsg("\tsend          - Send Data To Multicast Members in Group.");
        displayMsg("\tset mcastaddr - Sets Multicast Address.");
        displayMsg("\tset mcastport - Sets Multicast Port.");
        displayMsg("\tset bindaddr  - Sets Local Network Interface to use or All.");
        displayMsg("\tset ttl       - Sets Time to Live.");
        displayMsg("\tset interval  - Sets Send or Discovery Interval.");
        displayMsg("\tshow          - Shows Current Settings for Multicast address and Port.");
        displayMsg("\tshowver       - Shows JGroups Version in current CLASSPATH.");
        // **********************
        // Return
        return (0);
    } // End of usage Command.

    /**
     * echo command to simply echo back information to the requestor.
     */
    public int CMDecho(LinkedList _cmdargs) {
        String echoMsg = "";
        for (int i = 1; i < _cmdargs.size(); i++) {
            if ((echoMsg == null) || (echoMsg.equalsIgnoreCase(""))) {
                echoMsg = (String) _cmdargs.get(i);
            } else {
                echoMsg = echoMsg + " " + (String) _cmdargs.get(i);
            }
        } // End of For Loop.

        // **********************
        // Perform the Echo.
        displayMsg(echoMsg);

        // **********************
        // Return
        return (0);
    } // End of echo Command.

    /**
     * Show Current Multicast Settings.
     */
    public int CMDshow() {
        // *****************************************
        // Show our current Settings.
        displayMsg(" MULTICAST ADDRESS:[" +
                this.MULTICAST_ADDRESS + "], " +
                " MULTICAST PORT:[" +
                this.MULTICAST_PORT + "], " +
                " LOCAL BIND ADDRESS:[" +
                this.showLocalBindAddr() + "].");
        // **********************
        // Return
        return (0);
    } // End of show Command.

    /**
     * Show JGroups Version.
     */
    public int CMDshowver() {
        // *****************************************
        // Show the JGroups Version Information Tag.
        displayMsg(org.jgroups.Version.printVersion());
        // **********************
        // Return
        return (0);
    } // End of showver Command.

    /**
     * Set command to set various parameters for running utilities.
     */
    public int CMDset(LinkedList _cmdargs) {
        String echoMsg = "";
        for (int i = 1; i < _cmdargs.size(); i++) {
            if ((echoMsg == null) || (echoMsg.equalsIgnoreCase(""))) {
                echoMsg = (String) _cmdargs.get(i);
            } else {
                echoMsg = echoMsg + " " + (String) _cmdargs.get(i);
            }
        } // End of For Loop.

        // **********************
        // Perform the Echo.
        displayMsg(echoMsg);

        // **********************
        // Return
        return (0);
    } // End of set Command.

    /**
     * Discover.
     */
    public int CMDdiscover() {

        // **************************************
        // Set our new Prompt.
        super.setPrompt(discover_shell_prompt);

        // ******************************
        // Instantiate the Discovery thread.
        try {

            // *****************************
            // Show Initial Message for User.
            displayMsg("Do you wish to  begin Discovery of the specified MultiCast Group? (Y|N) ");
            if (super.CMDPrompt()) {
                // *****************************
                // Start the Discovery.
                new McastDiscovery(
                        InetAddress.getByName(this.MULTICAST_ADDRESS),
                        Integer.parseInt(this.MULTICAST_PORT),
                        DISCOVERY_INTERVAL,
                        TTL).start();
            } // End of Check to Proceed.


        } catch (Exception ex) {
            ex.printStackTrace();
        } // End of Exception Processing.

        // ***************************
        // Always ensure we have
        // the right prompt.
        super.setPrompt(McastNetworkVerificationUtility.shell_prompt);

        // **********************
        // Show the End.
        displayMsg("End of Interactive Discover Session.");


        // **********************
        // Return
        return (0);
    } // End of discover Command.

    /**
     * Receive.
     */
    public int CMDreceive() {

        // *****************************************
        // Array of Receiver Threads.
        McastAckReceiverThread[] ack_receiver = null;

        // *********************************************
        // Initialize Local Variables for Send Function.
        MulticastSocket[] sockets = null;
        InetAddress bind_addr = null;

        // **************************************
        // Set our new Prompt.
        super.setPrompt(receive_shell_prompt);

        // ******************************************
        // Now build Array of Multicast Sockets to
        // use.
        try {
            if (this.showLocalBindAddr().equalsIgnoreCase(
                    McastNetworkVerificationUtility.ALL)) {
                Vector<InetAddress> v = new Vector<>();

                for (Enumeration en = NetworkInterface.getNetworkInterfaces();
                     en.hasMoreElements(); ) {
                    NetworkInterface intf = (NetworkInterface) en.nextElement();
                    for (Enumeration e2 = intf.getInetAddresses();
                         e2.hasMoreElements(); ) {
                        bind_addr = (InetAddress) e2.nextElement();

                        // *********************************
                        // Accept only valid Addresses on
                        // Interfaces.
                        if (this.isValidInterface(bind_addr)) {
                            v.addElement(bind_addr);
                        }
                    } // End of Inner For Loop.
                } // End of Outer For Loop.

                // ***************************************
                // Size our Arrays Accordingly.
                sockets = new MulticastSocket[v.size()];
                ack_receiver = new McastAckReceiverThread[v.size()];

                // ***************************************
                // Loop through created Vector and
                // instantiate the Multicast socket for
                // each interface.
                for (int i = 0; i < v.size(); i++) {
                    sockets[i] = new MulticastSocket(this.getMulticastPortInteger());
                    sockets[i].setTimeToLive(this.TTL);
                    sockets[i].setInterface((InetAddress) v.elementAt(i));
                    sockets[i].joinGroup(this.getMulticastInetAddress());
                    ack_receiver[i] =
                            new McastAckReceiverThread(sockets[i]);
                    ack_receiver[i].start();
                } // End of For Loop.
            } else {
                // ***************************************
                // Resize our Arrays Accordingly.
                sockets = new MulticastSocket[1];
                ack_receiver = new McastAckReceiverThread[1];

                // *************************************
                // Ok, we have a specific interface to
                // bind to so use that address.
                sockets[0] = new MulticastSocket(this.getMulticastPortInteger());
                sockets[0].setTimeToLive(this.TTL);
                bind_addr = this.getBindInetAddress();
                if (bind_addr == null) {
                    sockets[0] = null;
                } else {
                    sockets[0].setInterface(bind_addr);
                    sockets[0].joinGroup(this.getMulticastInetAddress());
                    ack_receiver[0] =
                            new McastAckReceiverThread(sockets[0]);
                    ack_receiver[0].start();
                } // End of Inner Else.
            } // End of Else.

            // *********************************
            // Show all Interfaces.
            for (int i = 0; i < sockets.length; i++) {
                if (sockets[i] == null) {
                    continue;
                }
                displayMsg("Socket #" + (i + 1) + '=' +
                        sockets[i].getLocalAddress() +
                        ':' +
                        sockets[i].getLocalPort() +
                        ", ttl=" +
                        sockets[i].getTimeToLive() +
                        ", bind interface=" +
                        sockets[i].getInterface());
            } // End of For Loop.

            // *****************************
            // Now loop until the user ends.
            displayMsg("Will Receive from MultiCast Group, " +
                    "enter \042quit\042 or \042exit\042 to end function.");
            while (true) {
                // ********************************************
                // Prompt and verify we got something.
                super.displayPrompt();
                String svalue = this.CMDPromptForString();
                if ((svalue == null) ||
                        (svalue.trim().equalsIgnoreCase(""))) {
                    continue;
                }

                // ********************************************
                // Check to see if the data entered indicates
                // we should end?
                if (svalue.startsWith("quit") || svalue.startsWith("exit")) {
                    break;
                }
            } // End of While Loop.
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } // End of Exception Processing.

        // ***************************
        // Always ensure we have
        // the right prompt.
        super.setPrompt(McastNetworkVerificationUtility.shell_prompt);

        // **********************
        // Show the End.
        displayMsg("End of Interactive Send Session.");

        // **********************
        // Return
        return (0);
    } // End of receive Command.

    /**
     * Send.
     */
    public int CMDsend() {

        // *****************************************
        // Array of Receiver Threads.
        McastAckReceiverThread[] ack_receiver = null;

        // *********************************************
        // Initialize Local Variables for Send Function.
        MulticastSocket[] sockets = null;
        InetAddress bind_addr = null;
        DatagramPacket packet;
        byte[] buf = new byte[0];

        // **************************************
        // Set our new Prompt.
        super.setPrompt(send_shell_prompt);

        // ******************************************
        // Now build Array of Multicast Sockets to
        // use.
        try {
            if (this.showLocalBindAddr().equalsIgnoreCase(
                    McastNetworkVerificationUtility.ALL)) {
                Vector<InetAddress> v = new Vector<>();

                for (Enumeration en = NetworkInterface.getNetworkInterfaces();
                     en.hasMoreElements(); ) {
                    NetworkInterface intf = (NetworkInterface) en.nextElement();
                    for (Enumeration e2 = intf.getInetAddresses();
                         e2.hasMoreElements(); ) {
                        bind_addr = (InetAddress) e2.nextElement();

                        // *********************************
                        // Accept only valid Addresses on
                        // Interfaces.
                        if (this.isValidInterface(bind_addr)) {
                            v.addElement(bind_addr);
                        }
                    } // End of Inner For Loop.
                } // End of Outer For Loop.

                // ***************************************
                // Size our Arrays Accordingly.
                sockets = new MulticastSocket[v.size()];
                ack_receiver = new McastAckReceiverThread[v.size()];

                // ***************************************
                // Loop through created Vector and
                // instantiate the Multicast socket for
                // each interface.
                for (int i = 0; i < v.size(); i++) {
                    sockets[i] = new MulticastSocket(this.getMulticastPortInteger());
                    sockets[i].setTimeToLive(this.TTL);
                    sockets[i].setInterface((InetAddress) v.elementAt(i));
                    sockets[i].joinGroup(this.getMulticastInetAddress());
                    ack_receiver[i] =
                            new McastAckReceiverThread(sockets[i],
                                    McastNetworkVerificationUtility.send_shell_prompt,
                                    false);
                    ack_receiver[i].start();
                } // End of For Loop.
            } else {
                // ***************************************
                // Resize our Arrays Accordingly.
                sockets = new MulticastSocket[1];
                ack_receiver = new McastAckReceiverThread[1];

                // *************************************
                // Ok, we have a specific interface to
                // bind to so use that address.
                sockets[0] = new MulticastSocket(this.getMulticastPortInteger());
                sockets[0].setTimeToLive(this.TTL);
                bind_addr = this.getBindInetAddress();
                if (bind_addr == null) {
                    sockets[0] = null;
                } else {
                    sockets[0].setInterface(bind_addr);
                    sockets[0].joinGroup(this.getMulticastInetAddress());
                    ack_receiver[0] =
                            new McastAckReceiverThread(sockets[0],
                                    McastNetworkVerificationUtility.send_shell_prompt,
                                    false);
                    ack_receiver[0].start();
                } // End of Inner Else.
            } // End of Else.

            // *********************************
            // Show all Interfaces.
            for (int i = 0; i < sockets.length; i++) {
                if (sockets[i] == null) {
                    continue;
                }

                displayMsg("Socket #" + (i + 1) + '=' +
                        sockets[i].getLocalAddress() +
                        ':' +
                        sockets[i].getLocalPort() +
                        ", ttl=" +
                        sockets[i].getTimeToLive() +
                        ", bind interface=" +
                        sockets[i].getInterface());
            } // End of For Loop.

            // *****************************
            // Now loop until the user ends.
            displayMsg("Enter Data to Send to MultiCast Group, " +
                    "enter \042quit\042 or \042exit\042 to end function.");
            while (true) {
                // ********************************************
                // Prompt and verify we got something.
                super.displayPrompt();
                String svalue = this.CMDPromptForString();
                if ((svalue == null) ||
                        (svalue.trim().equalsIgnoreCase(""))) {
                    continue;
                }

                // ********************************************
                // Check to see if the data entered indicates
                // we should end?
                if (svalue.startsWith("quit") || svalue.startsWith("exit")) {
                    break;
                }

                // ********************************************************
                // Send the Serialized Object Data to all members of Group.
                buf = Util.objectToByteBuffer(svalue);

                packet = new DatagramPacket(buf,
                        buf.length,
                        this.getMulticastInetAddress(),
                        this.getMulticastPortInteger());
                send(packet, sockets); // send on all interfaces
            } // End of While Loop.
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } // End of Exception Processing.

        // ***************************
        // Always ensure we have
        // the right prompt.
        super.setPrompt(McastNetworkVerificationUtility.shell_prompt);

        // **********************
        // Show the End.
        displayMsg("End of Interactive Send Session.");

        // **********************
        // Return
        return (0);
    } // End of send Command.

    /**
     * Main to provide immediate use of utility Function invoked from
     * Shell Script.
     *
     * @param args
     */
    public static void main(String[] args) {

        // ****************************************
        // Local Objects
        Properties RunConfig = new Properties();

        // ****************************************
        // Obtain the RunTime Properties from
        // a local Property file.
        boolean DEFAULTS_FAILED_TO_LOAD = false;
        boolean DEFAULTS_NOT_FOUND = false;
        try {
            String PFileName = System.getProperty(
                    IRRChangeLogRestoreService.FRAMEWORK_CONFIG_PROPERTIES,
                    IRRChangeLogRestoreService.DEFAULT_PROPERTIES_FILENAME);
            File PF = new File(PFileName);
            if (PF.exists()) {
                // *************************************
                // Try to load the Properties.
                RunConfig.load(new FileInputStream(PF));
            } else {
                DEFAULTS_NOT_FOUND = true;
            } // End of Else.
        } catch (Exception e) {
            DEFAULTS_FAILED_TO_LOAD = true;
        } // End of Exception.

        // **************************
        // Instantiate our Object.
        McastNetworkVerificationUtility shell =
                new McastNetworkVerificationUtility(
                        RunConfig.getProperty(
                                IRRChangeLogRestoreService.MULTICAST_ADDRESS_PNAME),
                        RunConfig.getProperty(
                                IRRChangeLogRestoreService.MULTICAST_PORT_PNAME),
                        DEFAULTS_NOT_FOUND,
                        DEFAULTS_FAILED_TO_LOAD);

        // ********************************
        // Begin the Interactive Session.
        System.exit(shell.CMDInteractiveShell());
    } // End of Main.

    // *******************************************************************
    // PRIVATE METHODS
    // *******************************************************************

    /**
     * Show Local Bind Address for Display.
     */
    private String showLocalBindAddr() {
        if ((this.BIND_ADDRESS == null) ||
                (this.BIND_ADDRESS.equalsIgnoreCase(""))) {
            return McastNetworkVerificationUtility.DEFAULT_BIND_ADDRESS;
        } else {
            return this.BIND_ADDRESS;
        }
    } // End of showLocalBindAddr private method.

    /**
     * Get Multicast port as an Integer.
     *
     * @return int Port in Integer Form or Default.
     */
    private int getMulticastPortInteger() {
        try {
            return Integer.parseInt(this.MULTICAST_PORT);
        } catch (NumberFormatException nfe) {
            return DEFAULT_MULTICAST_PORT;
        } // End of Exception processing.
    } // End of getMulticastPortInteger private Method.

    /**
     * Get Multicast Address Object.
     *
     * @return InnetAddress
     */
    private InetAddress getMulticastInetAddress() {
        try {
            if ((this.MULTICAST_ADDRESS == null) ||
                    (this.MULTICAST_ADDRESS.equalsIgnoreCase(""))) {
                this.MULTICAST_ADDRESS = DEFAULT_MULTICAST_ADDRESS;
            }

            return InetAddress.getByName(this.MULTICAST_ADDRESS);
        } catch (Exception e) {
            this.displayErrorMsg("Unable to obtain InetAddress Object for " +
                    "Specified Multicast Address.");
            return null;
        } // End of Exception processing.
    } // End of getMulticastPortInteger private Method.

    /**
     * Get Bind Address Object.
     *
     * @return InnetAddress
     */
    private InetAddress getBindInetAddress() {
        try {
            if ((this.BIND_ADDRESS == null) ||
                    (this.BIND_ADDRESS.equalsIgnoreCase(""))) {
                return null;
            }

            return InetAddress.getByName(this.BIND_ADDRESS);
        } catch (Exception e) {
            this.displayErrorMsg("Unable to obtain InetAddress Object for " +
                    "Specified Bind Address.");
            return null;
        } // End of Exception processing.
    } // End of getBindInetAddress private Method.

    /**
     * Performs a send of a Data Packet across Multicast sockets interfaces.
     *
     * @param packet
     * @param sockets
     */
    private static void send(DatagramPacket packet, MulticastSocket[] sockets) {
        if (packet == null || sockets == null) {
            return;
        }

        // *********************************
        // Loop Through array of Multicast
        // Sockets.
        for (int i = 0; i < sockets.length; i++) {
            try {
                if (sockets[i] != null) {
                    sockets[i].send(packet);
                }
            } catch (Exception ex) {
                System.err.println("** Send Failure: " + ex);
            } // End of Exception processing.
        } // End of For Loop.
    } // End of static send method.

    /**
     * Check to make sure the Network Interface Address is valid and
     * should be included to send/receive processing.
     *
     * @param bind_addr
     * @return boolean indicator whether to include or exclude this
     *         interface from being included to send or receove on.
     */
    private boolean isValidInterface(InetAddress bind_addr) {

        // **********************************
        // Check for Null incoming parameter.
        if (bind_addr == null) {
            return false;
        }

        // *********************************
        // Ignore any Link Local Addresses.
        if (bind_addr.isLinkLocalAddress()) {
            return false;
        }

        // *********************************
        // Now get the Interface Address and
        // determine if we should use this or
        // not.  If we find a IPV6 Loopback,
        // we ignore since we will already
        // have a IPV4 Loopback address.
        // If not we will get additional
        // packets due to the duel local
        // addresses.
        //
        try {
            String saddr =
                    InetAddress.getByAddress(bind_addr.getAddress()).toString();
            if ((saddr == null) ||
                    (saddr.equalsIgnoreCase(""))) {
                return false;
            }

            // ***************************
            // Strip of a leading
            // slah if there is one.
            if (saddr.startsWith("/")) {
                saddr = saddr.substring(1);
            }

            // *****************************
            // Now Ignore a IPV6 Loopback
            // or unspecified IPV6 Address.
            if ((saddr.equalsIgnoreCase(McastNetworkVerificationUtility.IPV6_LOOPBACK_ADDRESS)) ||
                    (saddr.equalsIgnoreCase(McastNetworkVerificationUtility.IPV6_SHORT_LOOPBACK_ADDRESS)) ||
                    (saddr.equalsIgnoreCase(McastNetworkVerificationUtility.IPV6_UNSPECIFIED_ADDRESS)) ||
                    (saddr.equalsIgnoreCase(McastNetworkVerificationUtility.IPV6_SHORT_UNSPECIFIED_ADDRESS))) {
                return false;
            }
        } catch (UnknownHostException uhe) {
            return false;
        } // End of Exception processing.

        // *******************************
        // Return indicating we can use
        // this interface/address.
        return true;
    } // End of isValidInterface private Method.

} ///:> End of Class.
