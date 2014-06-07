package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.*;

/**
 * Accepts Client Incoming HTTP Connections.
 * When a connection is made,
 * it registers the new socket on the "readWriteSelector".
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

class WebAdminAcceptorThread extends Thread {

    // *******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = WebAdminAcceptorThread.class.getName();

    private static final String THREAD_NAME
            = "ServiceWebAcceptorThread";

    private static final long SELECT_WAIT_DURATION = (1000 * 2); // 2 Seconds.

    // ******************************
    // Thread Globals.
    private ServerSocketChannel ssc;
    private Selector connectSelector;
    private WebAdminConnectionList acceptedConnections;
    private boolean running = true;

    /**
     * Connection Acceptor Thread Constructor.
     *
     * @param connectSelector
     * @param list
     * @param port
     * @throws Exception
     */
    public WebAdminAcceptorThread(Selector connectSelector,
                                  WebAdminConnectionList list,
                                  int port)
            throws Exception {
        super(WebAdminAcceptorThread.THREAD_NAME);

        // ************************************
        // Save our References.
        this.connectSelector = connectSelector;
        this.acceptedConnections = list;

        // ************************************
        // Setup our Server Socket Channel.
        this.ssc = ServerSocketChannel.open();
        this.ssc.configureBlocking(false);

        // ************************************
        // Now bind address and port to Server
        // Socket Channel.
        InetSocketAddress address = new InetSocketAddress(port);
        this.ssc.socket().bind(address);

        // ************************************
        // Register the Selector and Channel.
        FrameworkLogger.log(CLASSNAME, "Constructor", FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_BOUND_TO_ADDRESS,
                new String[]{address.toString()});
        this.ssc.register(this.connectSelector, SelectionKey.OP_ACCEPT);
    } // End of Constructor.

    /**
     * Run Thread Main Loop.
     */
    public void run() {

        final String METHODNAME = "run";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_ACCEPTOR_THREAD_ESTABLISHED,
                new String[]{Thread.currentThread().getName()});

        // **************************
        // Main Acceptor Thread Loop
        try {
            while (running) {
                // *******************************
                // This Method will block this
                // Thread until we have a new
                // Client.
                int keysReady = connectSelector.select(SELECT_WAIT_DURATION);

                // *********************************
                // After unblocking, was I shutdown?
                // If, break out of loop.
                if (!running) {
                    break;
                }

                // *******************************
                // Accept this Pending Connection.
                if (keysReady > 0) {
                    acceptPendingConnections();
                }
            } // End of While Loop.

        } catch (Exception ex) {
            this.logStackTrace(ex, METHODNAME);
        } finally {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.WEBADMIN_ACCEPTOR_THREAD_SHUTDOWN,
                    new String[]{Thread.currentThread().getName()});
        } // End of Final Processing for Thread.
    } // Main Run Loop Method.

    /**
     * Accept Incoming Connection to provide Connection
     * to Responder Thread.
     *
     * @throws Exception
     */
    protected void acceptPendingConnections() throws Exception {
        // *************************
        // Obtain our Selector Keys
        // for this Channel.
        Set readyKeys = connectSelector.selectedKeys();
        // **************************
        // Loop to obtain Client
        // Connection Channel.
        for (Iterator i = readyKeys.iterator(); i.hasNext(); ) {
            SelectionKey key = (SelectionKey) i.next();
            i.remove();
            ServerSocketChannel readyChannel = (ServerSocketChannel) key.channel();
            SocketChannel incomingChannel = readyChannel.accept();

            // ****************************
            // Accept the Connection by
            // Adding to our Request List.
            acceptedConnections.push(incomingChannel);
        } // End of For Loop.
    } // End of acceptPendingConnections

    /**
     * Shutdown our Child Thread.
     */
    protected void shutdown() {
        final String METHODNAME = "shutdown";
        this.running = false;
        try {
            synchronized (ssc) {
                this.ssc.close();
                this.connectSelector.close();
            } // End of Synchronized code.
        } catch (IOException ioe) {
            this.logStackTrace(ioe, METHODNAME);
        } // End of Exception Processing.
    } // End of shutdown method.

    /**
     * Provides Logging of Stack Traces from Exceptions detail on the Nested Stack Trace
     * from Severe Runtime Errors.
     */
    private void logStackTrace(final Exception e, final String METHODNAME) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.SEVERE,
                ErrorConstants.WEBADMIN_ACCEPTOR_EXCEPTION,
                new String[]{e.getMessage(), sw.toString()});
    } // End of logStackTrace private Method.
} ///:~ End of WebAdminAcceptorThread.

