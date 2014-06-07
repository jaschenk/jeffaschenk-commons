package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;
import org.jgroups.Address;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Responds to the Clients Incoming HTTP Connections.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */
class WebAdminResponderThread extends Thread {

    // *******************************
    // Common Logging Facility.
    private static final String CLASSNAME
            = WebAdminResponderThread.class.getName();

    private static final String THREAD_NAME
            = "ServiceWebResponderThread";

    private static final long SELECT_WAIT_DURATION = (1000 * 2); // 2 Seconds.

    // ********************************
    // Read Buffer size.
    private static final int READ_BUFFER_SIZE = 2048;

    // ********************************
    // Globals
    private Selector readSelector;
    private WebAdminConnectionList acceptedConnections;
    private ByteBuffer[] responseBuffers = new ByteBuffer[2];
    private ByteBuffer[] unauthorizedresponseBuffers = new ByteBuffer[2];
    private ByteBuffer readBuffer;
    private Charset charset;
    private CharsetDecoder charsetDecoder;
    private LinkedList allow_list = null;

    // ***********************************************
    // Thread Objects
    private IRRChangeLogRestoreServiceControlThread
            CONTROL_THREAD = null;    // Provides interface to Local Cmds.
    private CircularObjectStack cosin = null;
    private CircularObjectStack cosout = null;
    private boolean running = true;

    // ***********************************************
    // Predefined matching Patterns which equate to
    // a String name.
    private static final String ANY_IPV4ADDR_PATTERN =
            "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

    private static final String ANY_NAME =
            "any";

    private static final String ALL_NAME =
            "all";

    // ***********************************************
    // Operation array, Operations available for
    // Each Resiliency Instance.
    private static final String SENDECHO_VALUE
            = "send.echo";

    private static final String STARTREADER_VALUE
            = "start.reader";

    private static final String STOPREADER_VALUE
            = "stop.reader";

    private static final String STARTREPLICA_VALUE
            = "start.replicarestore";

    private static final String STOPREPLICA_VALUE
            = "stop.replicarestore";

    private static final String RESETSTAT_VALUE
            = "reset.statistics";

    private static final String SHUTDOWN_INSTANCE_VALUE
            = "shutdown.instance";

    private static final String[] OPERATION_COMMAND_ARRAY = {
            SENDECHO_VALUE,
            STARTREADER_VALUE,
            STOPREADER_VALUE,
            STARTREPLICA_VALUE,
            STOPREPLICA_VALUE,
            RESETSTAT_VALUE,
            SHUTDOWN_INSTANCE_VALUE
    }; // End of Operation Array.

    private static final String[] OPERATION_NAME_ARRAY = {
            "Send Echo",
            "Start Reader",
            "Stop Reader",
            "Start Replica Restore",
            "Stop Replica Restore",
            "Reset Statistics",
            "Shutdown Instance"
    }; // End of Operation Array.

    private static final String SHUTDOWNALL_VALUE
            = "shutdown-all-instances";

    private static final String SHUTDOWNALL_NAME
            = "Shutdown All Instances";

    private static final String CLIENT_NOT_AUTHORIZED
            = "Client Not Authorized";

    // ***********************************************
    // FRAMEWORK Product Specific Title Information
    private static final String PRODUCT_COMPONENT_TITLE
            = "Data Resiliency Administration";

    private static final String FRAMEWORK_PAGE_TITLE
            = "FRAMEWORK " + PRODUCT_COMPONENT_TITLE + " for";

    private static final String MEMBER_NAME = "/member/";

    private static final String LOCAL_VALUE = "/local";
    private static final String LOCAL_NAME = "Local Instance Status";

    private static final String LOCAL_REFRESH_VALUE = "/local/refresh";
    private static final String LOCAL_REFRESH_NAME = "Auto Refresh of Local Instance Status";

    private static final String OPERATION_NAME = "/operation/";

    private static final String FRAMEWORK_LOGO
            = "<img width=311 height=75 src=\042/web/framework.jpg\042>";

    // ******************************************
    // HTML Specific Status Page Variables.
    private static final String SPAN_STYLE_BEGIN =
            "<span style='font-size:10.0pt;font-family:Arial;color:#00000'>";
    private static final String SPAN_STYLE_END =
            "</span>";

    private static final String HTMLHEADER_NO_CACHE =
            "<head>\n" +
                    "<meta http-equiv=\042Pragma\042 content=\042no-cache\042>\n" +
                    "<meta http-equiv=\042expires\042 content=\0420\042>";

    private static final String HTMLHEADER =
            "<head>\n";

    private static final String HTMLREFRESH =
            "<meta http-equiv=\042refresh\042 content=\04215\042>";

    private static final String HTMLTITLE = "<title>";
    private static final String HTMLTITLE_END = "</title>";

    private static final String OPERATION_SELECTION_JAVASCRIPT
            = "<script type=\042text/javascript\042 language=\042JavaScript\042>" +
            "function formHandler(form){" +
            "var URL = document.form.operation.options[document.form.operation.selectedIndex].value;" +
            "window.location.href = URL; }" +
            "</script>";

    private static final String HTMLHEADEREND_BODYBEGIN =
            "</head>" +
                    "<body>" +
                    "<div class=HeaderSection>" +
                    FRAMEWORK_LOGO + "<br>" +
                    "<b>" +
                    "<span style='font-size:16.0pt;font-family:Arial;color:#3366FF'>" +
                    PRODUCT_COMPONENT_TITLE +
                    "</span></b><br>" +
                    "</div>\n";

    private static final String UNAUTH_HTMLHEADEREND_BODYBEGIN =
            "</head>" +
                    "<body>" +
                    "<H1>" + CLIENT_NOT_AUTHORIZED + "</H1>" +
                    "<b><span style='font-size:12.0pt;font-family:Arial;color:#3366FF'>" +
                    "Access Restricted by IP Address." +
                    "</span></b>\n";

    private static final String HTML_GROUP_MEMBERSHIP_BEGIN =
            "<div class=MembershipSection>" +
                    "<p><b><u><span style='font-size:16.0pt;font-family:Arial;color:#3366FF'>" +
                    "Group Membership List</span></u></b>\n" +
                    "<br>" +
                    "<span style='font-size:8.0pt;font-family:Arial;color:#000000'>" +
                    "Click on Member Address to obtain remote status." +
                    "</span>";

    private static final String HTML_GROUP_MEMBERSHIP_END =
            "</p></div>\n";

    private static final String HTML_OPERATION_FORM_BEGIN =
            "<form name=\042form\042>\n" +
                    "<select name=\042operation\042 size=1>\n" +
                    "<option value=\042\042>Perform Operation...\n";

    private static final String HTML_OPERATION_VALUE =
            "<option value=\042%1\042>%2\n";

    private static final String HTML_OPERATION_FORM_END =
            "</select>\n" +
                    "<input type=button value=\042Proceed\042 onClick=\042javascript:formHandler(this)\042>" +
                    "</form>\n";

    private static final String HTML_LOCAL_INSTANCE_STATUS_BEGIN =
            "<div class=LocalSection>" +
                    "<p><b><u><span style='font-size:16.0pt;font-family:Arial;color:#3366FF'>" +
                    "Local Instance Status</span></u></b></p>\n";

    private static final String HTML_LOCAL_INSTANCE_STATUS_END =
            "</div>\n";

    private static final String HTML_REMOTE_INSTANCE_STATUS_BEGIN =
            "<div class=RemoteSection>" +
                    "<p><b><u><span style='font-size:16.0pt;font-family:Arial;color:#3366FF'>" +
                    "Remote Peer Instance Status</span></u></b></p>\n";

    private static final String HTML_REMOTE_INSTANCE_STATUS_END =
            "</div>\n";

    private static final String HTML_OPERATION_RESPONSE_BEGIN =
            "<div class=OperationSection>" +
                    "<p><b><u><span style='font-size:16.0pt;font-family:Arial;color:#3366FF'>" +
                    "Operation Response</span></u></b></p>\n";

    private static final String HTML_OPERATION_RESPONSE_END =
            "</div>\n";

    private static final String HTMLFOOTER =
            "<hr>\n" +
                    "</body>\n" +
                    "</html>";

    // *******************************************
    // Common HTML Markup Constants.
    protected static final String HR = "<hr>";
    protected static final String BEGIN_TABLE = "<TABLE>";
    protected static final String END_TABLE = "</TABLE>";
    protected static final String BEGIN_ROW = "<tr>";
    protected static final String END_ROW = "</tr>";
    protected static final String BEGIN_COL = "<td>";
    protected static final String END_COL = "</td>";
    protected static final String BEGIN_PARA = "<p>";
    protected static final String END_PARA = "</p>";
    protected static final String BOLD = "<b>";
    protected static final String END_BOLD = "</b>";
    protected static final String UNDERLINE = "<u>";
    protected static final String END_UNDERLINE = "</u>";
    protected static final String BEGIN_GROUP_URI = "<a href=\042" + MEMBER_NAME;
    protected static final String BEGIN_GROUP_URI_ANCHOR_END = "\042>";
    protected static final String END_GROUP_URI = "</a>";

    protected static final String COMPONENT_BEGIN =
            "<b><u><span style='font-size:12.0pt;font-family:Arial;color:#3366FF'>";
    protected static final String COMPONENT_END = "</span></b></u>";

    protected static final String COMPONENT_ONLINE_BEGIN =
            "<b><u><SPAN style=\042FONT-SIZE: 12pt; COLOR: #3366ff; FONT-FAMILY: Arial\042>";
    protected static final String COMPONENT_ONLINE_END = "</SPAN></b></u>" +
            "&nbsp;&nbsp;<b><SPAN style=\042FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial\042>" +
            "<FONT style=\042BACKGROUND-COLOR: #33cc00\042 color=#000000>" +
            "Running" +
            "</FONT></SPAN></b>";

    protected static final String COMPONENT_OFFLINE_BEGIN =
            "<b><u><SPAN style=\042FONT-SIZE: 12pt; COLOR: #3366ff; FONT-FAMILY: Arial\042>";
    protected static final String COMPONENT_OFFLINE_END = "</SPAN></b></u>" +
            "&nbsp;&nbsp;<b><SPAN style=\042FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial\042>" +
            "<FONT style=\042BACKGROUND-COLOR: #990000\042 color=#FFFFFF>" +
            "Stopped" +
            "</FONT></SPAN></b>";

    protected static final String POSITIVE_VALUE =
            "<b><SPAN style=\042FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial\042>" +
                    "<FONT style=\042BACKGROUND-COLOR: #33cc00\042 color=#000000>";
    protected static final String POSITIVE_VALUE_END = "</SPAN></b></u>";

    protected static final String NEGATIVE_VALUE =
            "<b><SPAN style=\042FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial\042>" +
                    "<FONT style=\042BACKGROUND-COLOR: #990000\042 color=#FFFFFF>";
    protected static final String NEGATIVE_VALUE_END = "</SPAN></b></u>";

    protected static final String CAUTION_VALUE =
            "<b><SPAN style=\042FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial\042>" +
                    "<FONT style=\042BACKGROUND-COLOR: ##ffff00\042 color=#FFFFFF>";
    protected static final String CAUTION_VALUE_END = "</SPAN></b></u>";

    protected static final String WARNING_VALUE =
            "<b><SPAN style=\042FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial\042>" +
                    "<FONT style=\042BACKGROUND-COLOR: #ff9900\042 color=#FFFFFF>";
    protected static final String WARNING_VALUE_END = "</SPAN></b></u>";

    protected static final String NEUTRAL_VALUE =
            "<b><SPAN style=\042FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial\042>" +
                    "<FONT style=\042BACKGROUND-COLOR: #3333ff\042 color=#000000>";
    protected static final String NEUTRAL_VALUE_END = "</SPAN></b></u>";

    protected static final String BREAK = "<br>";

    protected static final String NOT_AVAILABLE = BOLD + "N/A" + END_BOLD;

    /**
     * WebAdminResponder Thread Constructor.
     *
     * @param readSelector
     * @param acceptedConnections
     * @param cosin
     * @param cosout
     * @throws Exception
     */
    public WebAdminResponderThread(Selector readSelector,
                                   WebAdminConnectionList acceptedConnections,
                                   IRRChangeLogRestoreServiceControlThread CONTROL_THREAD,
                                   CircularObjectStack cosin,
                                   CircularObjectStack cosout,
                                   LinkedList allow_list) throws Exception {

        super(WebAdminResponderThread.THREAD_NAME);

        this.readSelector = readSelector;
        this.acceptedConnections = acceptedConnections;
        this.readBuffer = ByteBuffer.allocateDirect(READ_BUFFER_SIZE);

        this.charset = Charset.forName("ISO-8859-1");
        this.charsetDecoder = charset.newDecoder();
        this.responseBuffers[0] = initializeResponseHeader();
        this.unauthorizedresponseBuffers[0] = initializeUNAUTHResponseHeader();

        // *********************************
        // Set up the Stack to Communicate
        this.CONTROL_THREAD = CONTROL_THREAD;
        this.cosin = cosin;
        this.cosout = cosout;

        // *********************************
        // Save a Reference to Allow List
        this.allow_list = allow_list;

    } // End of Constructor.

    /**
     * WebAdminResponder Main Loop to Respond to Requests.
     */
    public void run() {
        final String METHODNAME = "run";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                MessageConstants.WEBADMIN_RESPONDER_THREAD_ESTABLISHED,
                new String[]{Thread.currentThread().getName()});

        // **************************
        // Main Responder Thread Loop
        try {
            while (running) {
                registerNewChannels();
                // **********************************
                // Any Selector Keys ready for a
                // Response.
                int keysReady = readSelector.select(SELECT_WAIT_DURATION);

                // *********************************
                // Was I shutdown?
                // If, break out of loop.
                if (!running) {
                    break;
                }

                // ***********************************
                // Any Connection Ready?
                if (keysReady > 0) {
                    acceptPendingRequests();
                }
            } // End of While Loop.
        } catch (Exception ex) {
            this.logStackTrace(ex, METHODNAME);
        } finally {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.WEBADMIN_RESPONDER_THREAD_SHUTDOWN,
                    new String[]{Thread.currentThread().getName()});
        } // End of Final Processing for Thread.
    } // End of Run Method.

    /**
     * Register any new Channels for us to Respond.
     *
     * @throws Exception
     */
    protected void registerNewChannels() throws Exception {
        SocketChannel channel;
        while (null != (channel = acceptedConnections.removeFirst())) {
            channel.configureBlocking(false);
            channel.register(readSelector, SelectionKey.OP_READ, new StringBuffer());
        } // End of While Loop.
    } // End of registerNewChannels

    /**
     * Accepts any pending incoming requests
     *
     * @throws Exception
     */
    protected void acceptPendingRequests() throws Exception {
        Set readyKeys = readSelector.selectedKeys();

        for (Iterator i = readyKeys.iterator(); i.hasNext(); ) {
            SelectionKey key = (SelectionKey) i.next();
            i.remove();
            // ******************************
            // Now Read and Process Request.
            readRequest(key);
            key.cancel();
        } // End of For Loop.
    } // End of acceptPendingRequests Method.

    /**
     * Read pending request from current Selector Key and
     * will perform request.
     *
     * @param key
     * @throws Exception
     */
    protected void readRequest(SelectionKey key) throws Exception {
        final String METHODNAME = "readRequest";
        SocketChannel incomingChannel = (SocketChannel) key.channel();
        try {
            while (incomingChannel.read(readBuffer) > 0) {
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                        MessageConstants.WEBADMIN_RESPONDER_THREAD_INCOMING_BYTES_READ,
                        new String[]{Integer.toString(readBuffer.capacity() - readBuffer.remaining())});

                readBuffer.flip();
                String result = charsetDecoder.decode(readBuffer).toString();
                readBuffer.clear();

                StringBuffer requestString = (StringBuffer) key.attachment();
                requestString.append(result);

                if (result.endsWith("\n\n") || result.endsWith("\r\n\r\n")) {
                    handleCompletedRequest(requestString.toString(), incomingChannel);
                    break;
                }
            } // End of While Loop.
        } catch (WebAdminRequestException re) {
            sendError(incomingChannel, re);
        } catch (IOException ioe) {
            sendError(incomingChannel, WebAdminRequestException.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            sendError(incomingChannel, WebAdminRequestException.INTERNAL_SERVER_ERROR);
        } // End of Exception Processing.
    } // end of readRequest Method.

    /**
     * Handle completed received data from Selector, now perform request
     * based upon URI.
     *
     * @param request
     * @param channel
     * @throws WebAdminRequestException
     * @throws java.io.IOException
     * @throws Exception
     */
    protected void handleCompletedRequest(String request, SocketChannel channel)
            throws WebAdminRequestException, IOException, Exception {

        final String METHODNAME = "handleCompletedRequest";

        // *********************************
        // Ok, now determine if this
        // Client is allowed Access.
        if (this.isClientAllowed(channel.socket().getInetAddress().toString())) {
            // ****************************
            // Accept the Connection by
            // Adding to our Request List.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.INFO,
                    MessageConstants.WEBADMIN_ACCEPT_CONNECTION,
                    new String[]{channel.socket().getInetAddress().toString()});
        } else {
            // ****************************
            // Deny the Connection by
            // ringing out the proper
            // protocols, otherwise
            // we will have FIN_WAITs.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    MessageConstants.WEBADMIN_DENIED_CONNECTION,
                    new String[]{channel.socket().getInetAddress().toString()});
            sendUnauthorizedPage(channel);
            channel.close();
            return;
        } // End of Else.

        // **************************************
        // Ok Accept the Connetion and perform
        // the Request.
        StringTokenizer tok = new StringTokenizer(request);
        tok.nextToken();                       // skip the method
        String path = tok.nextToken();         // grab the URI, ignore the rest

        // ***************************************
        // Now determine which page or resource to
        // send or which operation to perform.
        if ((path == null) ||
                (path.equalsIgnoreCase("")) ||
                (path.equalsIgnoreCase("/")) ||
                (path.equalsIgnoreCase(LOCAL_VALUE))) {
            sendLocalStatusPage(path, channel, request, false);
        } else if (path.equalsIgnoreCase(LOCAL_REFRESH_VALUE)) {
            sendLocalStatusPage(path, channel, request, true);
        } else if (path.startsWith(MEMBER_NAME)) {
            sendRemoteStatusPage(path, channel, request, false);
        } else if (path.startsWith(OPERATION_NAME)) {
            sendOperationResponsePage(path, channel, request, false);
        } else {
            sendResource(path, channel, request);
        }

        // *********************************************
        // Now Close off the Client Socket after
        // processing the request, never place in
        // final block, if error occurs, sendError
        // method will be invoked.
        channel.close();
    } // End of handleCompletedRequest Method.

    /**
     * Initialize HTTP OK Response Header.
     *
     * @return ByteBuffer containing encoded header.
     * @throws Exception
     */
    protected ByteBuffer initializeResponseHeader() throws Exception {
        // Pre-load a "good" HTTP response as characters.
        CharBuffer chars = CharBuffer.allocate(128);
        chars.put("HTTP/1.1 200 OK\n");
        chars.put("Connection: close\n");
        chars.put("Server: FRAMEWORK RESILIENCY\n");
        chars.put("Content-Type: text/html\n");
        chars.put("\n");
        chars.flip();

        // ****************************************************
        // Translate the Unicode characters into ASCII bytes.
        ByteBuffer buffer = charset.newEncoder().encode(chars);

        // **********************
        // Return.
        return buffer;
    } // End of initializeResponseHeader Method.

    /**
     * Initialize HTTP Unauthorized Response Header.
     *
     * @return ByteBuffer containing encoded header.
     * @throws Exception
     */
    protected ByteBuffer initializeUNAUTHResponseHeader() throws Exception {
        // Pre-load a "UnAuthorized" HTTP response as characters.
        CharBuffer chars = CharBuffer.allocate(128);
        chars.put("HTTP/1.1 401 Unauthorized\n");
        chars.put("Connection: close\n");
        chars.put("Server: FRAMEWORK RESILIENCY\n");
        chars.put("Content-Type: text/html\n");
        chars.put("\n");
        chars.flip();

        // ****************************************************
        // Translate the Unicode characters into ASCII bytes.
        ByteBuffer buffer = charset.newEncoder().encode(chars);

        // **********************
        // Return.
        return buffer;
    } // End of initializeResponseHeader Method.

    /**
     * Encode the Response.
     *
     * @param response
     * @return
     * @throws Exception
     */
    protected ByteBuffer encodeResponse(String response) throws Exception {
        CharBuffer chars = CharBuffer.allocate(response.length());
        chars.put(response);
        chars.flip();
        // Translate the Unicode characters into ASCII bytes.
        ByteBuffer buffer = charset.newEncoder().encode(chars);
        return buffer;
    }

    /**
     * Send Unauthorized Page.
     */
    protected void sendUnauthorizedPage(SocketChannel channel)
            throws WebAdminRequestException, IOException, Exception {

        final String METHODNAME = "sendUnauthorizedPage";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                MessageConstants.WEBADMIN_RESPONDER_THREAD_SENDING_UNAUTH,
                new String[]{channel.socket().getInetAddress().toString()});

        // *************************************
        // Formulate Header of Page
        StringBuffer sb =
                new StringBuffer(this.buildUnAuthorizedHeader());

        // ******************************************
        // Finish Up Page.
        sb.append(WebAdminResponderThread.HTMLFOOTER);

        // *************************************
        // Encode and Rewind our Buffer for
        // Presentation.
        this.unauthorizedresponseBuffers[1] = this.encodeResponse(sb.toString());
        this.unauthorizedresponseBuffers[1].rewind();

        // **************************************
        // Present Page to Channel.
        this.unauthorizedresponseBuffers[0].rewind();
        channel.write(this.unauthorizedresponseBuffers);
    } // End of sendUnauthorizedPage Method.

    /**
     * Build Unauthorized Header Page.
     */
    protected String buildUnAuthorizedHeader() {

        // *************************************
        // Formulate Header Page
        StringBuffer sb = new StringBuffer();
        sb.append(WebAdminResponderThread.HTMLHEADER);
        sb.append(WebAdminResponderThread.HTMLTITLE);
        sb.append(WebAdminResponderThread.FRAMEWORK_PAGE_TITLE + " " +
                CLIENT_NOT_AUTHORIZED);
        sb.append(WebAdminResponderThread.HTMLTITLE_END);

        // ******************************************
        // Show Not Authorized.
        sb.append(WebAdminResponderThread.UNAUTH_HTMLHEADEREND_BODYBEGIN);

        // ******************************************
        // Return.
        return sb.toString();
    } // End of buildUnAuthorizedHeader Method.

    /**
     * Build Common Header for all Pages.
     *
     * @param autorefresh
     */
    protected String buildCommonHeader(boolean autorefresh) {

        // *************************************
        // Formulate Header Page
        StringBuffer sb = new StringBuffer();
        if (autorefresh) {
            sb.append(WebAdminResponderThread.HTMLHEADER_NO_CACHE);
        } else {
            sb.append(WebAdminResponderThread.HTMLHEADER);
        }

        sb.append(WebAdminResponderThread.HTMLTITLE);
        sb.append(WebAdminResponderThread.FRAMEWORK_PAGE_TITLE + " " +
                this.CONTROL_THREAD.my_addr.toString() +
                ", http:" +
                this.CONTROL_THREAD.WEBADMIN_PORT);
        sb.append(WebAdminResponderThread.HTMLTITLE_END);

        // ******************************************
        // Check to see if user request auto refresh?
        if (autorefresh) {
            sb.append(WebAdminResponderThread.HTMLREFRESH);
        }

        // ******************************************
        // Add any JavaScripts We Need to Establish.
        sb.append(WebAdminResponderThread.OPERATION_SELECTION_JAVASCRIPT);

        // ******************************************
        // Finish Up Header and Begin Body.
        sb.append(WebAdminResponderThread.HTMLHEADEREND_BODYBEGIN);

        // ******************************************
        // First show our Membership list
        // and Operation Command List.
        sb.append(this.generateMemberShipList());

        // ******************************************
        // Return.
        return sb.toString();
    } // End of buildCommonHeader Method.

    /**
     * Send Local Status Page.
     */
    protected void sendLocalStatusPage(String uri, SocketChannel channel,
                                       String request,
                                       boolean autorefresh)
            throws WebAdminRequestException, IOException, Exception {

        final String METHODNAME = "sendLocalStatusPage";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.WEBADMIN_RESPONDER_THREAD_SENDING_URI,
                new String[]{uri});

        // *************************************
        // Formulate Header of Page
        StringBuffer sb =
                new StringBuffer(this.buildCommonHeader(autorefresh));

        // ******************************************
        // Obtain Local Status from Components.
        sb.append(WebAdminResponderThread.HTML_LOCAL_INSTANCE_STATUS_BEGIN);
        sb.append(this.CONTROL_THREAD.obtainLocalStatus(false));
        sb.append(WebAdminResponderThread.HTML_LOCAL_INSTANCE_STATUS_END);

        // ******************************************
        // Finish Up Page.
        sb.append(WebAdminResponderThread.HTMLFOOTER);

        // *************************************
        // Encode and Rewind our Buffer for
        // Presentation.
        this.responseBuffers[1] = this.encodeResponse(sb.toString());
        this.responseBuffers[1].rewind();

        // **************************************
        // Present Page to Channel.
        this.responseBuffers[0].rewind();
        channel.write(this.responseBuffers);
    } // End of send LocalStatusPage Method.

    /**
     * Send Remote Status Page.
     */
    protected void sendRemoteStatusPage(String uri, SocketChannel channel,
                                        String request,
                                        boolean autorefresh)
            throws WebAdminRequestException, IOException, Exception {

        final String METHODNAME = "sendRemoteStatusPage";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.WEBADMIN_RESPONDER_THREAD_SENDING_URI,
                new String[]{uri});

        // *************************************
        // Formulate Header of Page
        StringBuffer sb =
                new StringBuffer(this.buildCommonHeader(autorefresh));

        // ******************************************
        // Check if we should obtain Remote Instance
        // Status.
        sb.append(this.buildRemoteStatusPage(uri));

        // ******************************************
        // Finish Up Page.
        sb.append(WebAdminResponderThread.HTMLFOOTER);

        // *************************************
        // Encode and Rewind our Buffer for
        // Presentation.
        this.responseBuffers[1] = this.encodeResponse(sb.toString());
        this.responseBuffers[1].rewind();

        // **************************************
        // Present Page to Channel.
        this.responseBuffers[0].rewind();
        channel.write(this.responseBuffers);
    } // End of sendRemoteStatusPage Method.

    /**
     * Build Status Page of Remote Peers.
     */
    protected StringBuffer buildRemoteStatusPage(String uri) {

        final String METHODNAME = "buildRemoteStatusPage";

        // ******************************************
        // TODO - Put this Method Under a Timer!

        // ******************************************
        // Obtain Remote Status from Components.
        StringBuffer sb = new StringBuffer();
        sb.append(WebAdminResponderThread.HTML_REMOTE_INSTANCE_STATUS_BEGIN);

        // **********************************************
        // Now determine based upon the uri which member
        // I should use.
        String member_token = null;
        int member_index = 0;
        if ((uri != null) && (uri.startsWith(MEMBER_NAME))) {
            member_token = uri.substring((MEMBER_NAME).length()).trim();
        } else {
            member_token = "0";
        }
        try {
            member_index = Integer.parseInt(member_token);
        } catch (NumberFormatException nfe) {
            member_index = 0;
        }
        if (member_index > (this.CONTROL_THREAD.members.size() - 1)) {
            member_index = 0;
        }

        // **************************************************
        // Ok Now issue the Command over the Remote Bus.
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.WEBADMIN_RESPONDER_THREAD_MEMBER_INDEX_SELECTED,
                new String[]{Integer.toString(member_index),
                        this.CONTROL_THREAD.members.get(member_index).toString()});

        this.cosout.push(new StackCommand(StackCommand.CL_STATUS,
                this.CONTROL_THREAD.my_addr,
                this.CONTROL_THREAD.members.get(member_index),
                null));

        // ***************************************************
        // Now Loop TO Obtain Reply on our Local Stack.
        while (true) {
            StackCommand instackcommand = null;
            if (cosin.hasMoreNodes()) {
                instackcommand = (StackCommand) cosin.getNext();
            }
            // ***************************
            // Did anything get pulled
            // from stack?
            if (instackcommand != null) {
                // ******************************
                // Did our Running State trip to
                // not based upon a End of Thread
                // command? If so, never place this
                // Command on the BUS.
                if ((instackcommand.getCommandType() ==
                        StackCommand.CL_STATUS_REPLY) &&
                        (instackcommand.getDestination().equals(this.CONTROL_THREAD.my_addr))) {
                    sb.append((String) instackcommand.getObject());
                    break; // Break from the While Loop.
                } // End of Check for End of Thread.
                else {
                    // ********************************
                    // We Entered Here due to a Roque
                    // Command. we Could have received
                    // a Command for our parent, so place
                    // back on the queue.
                    cosin.push(instackcommand);
                } // End of Else.
            } // End of Nothing in Stack yet to Process.
        } // End of  While Loop.

        // **************************************************
        // Finishup this Section.
        sb.append(WebAdminResponderThread.HTML_REMOTE_INSTANCE_STATUS_END);

        // **************************************
        // Return.
        return sb;
    } // End of buildRemoteStatusPage Method.

    /**
     * Send Operation Response.
     */
    protected void sendOperationResponsePage(String uri, SocketChannel channel,
                                             String request,
                                             boolean autorefresh)
            throws WebAdminRequestException, IOException, Exception {

        final String METHODNAME = "sendOperationResponsePage";
        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.WEBADMIN_RESPONDER_THREAD_SENDING_URI,
                new String[]{uri});

        // *************************************
        // Formulate Header of Page
        StringBuffer sb =
                new StringBuffer(this.buildCommonHeader(autorefresh));

        // ******************************************
        // Now Check the incoming uri string path to
        // determine and trigger the operation to be
        // performed.
        sb.append(this.processOperation(uri));

        // ******************************************
        // Finish Up Page.
        sb.append(WebAdminResponderThread.HTMLFOOTER);

        // *************************************
        // Encode and Rewind our Buffer for
        // Presentation.
        this.responseBuffers[1] = this.encodeResponse(sb.toString());
        this.responseBuffers[1].rewind();

        // **************************************
        // Present Page to Channel.
        this.responseBuffers[0].rewind();
        channel.write(this.responseBuffers);
    } // End of sendOperationResponsePage Method.

    /**
     * Process the Operation and build any additional details
     * for formulating a response.
     */
    protected String processOperation(String uri) {

        final String METHODNAME = "processOperation";

        // ******************************************
        // Initialize.
        StringBuffer sb = new StringBuffer();

        // **************************************************************
        // Build the Operation Response Header.
        sb.append(WebAdminResponderThread.HTML_OPERATION_RESPONSE_BEGIN);

        // **********************************************
        // Now determine based upon the uri what command
        // should be executed against which member in group.
        if ((uri == null) ||
                (!uri.startsWith(WebAdminResponderThread.OPERATION_NAME)))

        {
            // **************************************************
            // Ok Show Invalid Operation URI.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                    ErrorConstants.WEBADMIN_RESPONDER_THREAD_INVALID_OPURI,
                    new String[]{uri});

            // **************************************************
            // Show in HTML Form as Well for the End User.
            sb.append(
                    WebAdminResponderThread.BEGIN_PARA +
                            WebAdminResponderThread.BOLD +
                            "Invalid Operation URI:[" +
                            WebAdminResponderThread.END_BOLD +
                            uri +
                            WebAdminResponderThread.BOLD +
                            "]" +
                            WebAdminResponderThread.END_BOLD +
                            WebAdminResponderThread.END_PARA);

            // **************************************************
            // Finishup this Section.
            sb.append(WebAdminResponderThread.HTML_OPERATION_RESPONSE_END);

            // **************************************************
            // return
            return sb.toString();
        } // End of Null or invalid Operation Name.

        // ************************************************************
        // Make a usable operation string stripping out encoded blanks.
        String operation = uri.substring(
                WebAdminResponderThread.OPERATION_NAME.length()).replaceAll("%20", " ");

        // ******************************************************
        // Now we seem to have a valid Operation prefix now verify
        // we have a valid address to send the command.
        Object cmddestaddr = null;
        int i = operation.lastIndexOf(" ");
        if (i > 0) {
            // **********************************************
            // Obtain the Stringified Address.
            String destaddr = operation.substring(i).trim();
            operation = operation.substring(0, i);

            // **********************************************
            // Now Obtain real Destination.
            for (int r = 0; r < this.CONTROL_THREAD.members.size(); r++) {
                if (this.CONTROL_THREAD.members.get(r).toString().equalsIgnoreCase(destaddr)) {
                    cmddestaddr = this.CONTROL_THREAD.members.get(r);
                }
            } // End of Foor Loop.
        } // End of If for Destination Address.

        // ***************************************************
        // Now Formulate a Stack Command based upon
        // Operation and Destnation Address if one exists.
        StackCommand opstackcommand =
                this.formulateOperationStackCommand(operation, cmddestaddr);

        // ****************************************************
        // Now determine if the command was good or not.
        // If good send it on it's way, if not show issue.
        if (opstackcommand == null) {
            // ************************
            // Abandon Illegal Request.
            if (cmddestaddr == null) {
                sb.append(
                        WebAdminResponderThread.BEGIN_PARA +
                                WebAdminResponderThread.BOLD +
                                "Abandoned Invalid Operation:[" +
                                WebAdminResponderThread.END_BOLD +
                                operation +
                                WebAdminResponderThread.BOLD +
                                "]" +
                                WebAdminResponderThread.END_BOLD +
                                WebAdminResponderThread.END_PARA);
            } else {
                sb.append(
                        WebAdminResponderThread.BEGIN_PARA +
                                WebAdminResponderThread.BOLD +
                                "Abandoned Invalid Operation:[" +
                                WebAdminResponderThread.END_BOLD +
                                operation +
                                WebAdminResponderThread.BOLD +
                                "]" +
                                WebAdminResponderThread.END_BOLD +
                                WebAdminResponderThread.BREAK);

                sb.append(
                        WebAdminResponderThread.BEGIN_PARA +
                                WebAdminResponderThread.BOLD +
                                "Address Destination:[" +
                                WebAdminResponderThread.END_BOLD +
                                cmddestaddr.toString() +
                                WebAdminResponderThread.BOLD +
                                "]" +
                                WebAdminResponderThread.END_BOLD +
                                WebAdminResponderThread.END_PARA);
            } // End of Inner Else.
        } else {
            // ***************************
            // Execute Operation Request,
            // by placing command onto
            // bus.
            this.cosout.push(opstackcommand);

            // ******************************
            // Now Show Operation was Placed
            // for execution.
            if (cmddestaddr == null) {
                sb.append(
                        WebAdminResponderThread.BEGIN_PARA +
                                WebAdminResponderThread.BOLD +
                                "Acknowledged Operation:[" +
                                WebAdminResponderThread.END_BOLD +
                                operation +
                                WebAdminResponderThread.BOLD +
                                "]" +
                                WebAdminResponderThread.END_BOLD +
                                WebAdminResponderThread.END_PARA);
            } else {
                sb.append(
                        WebAdminResponderThread.BEGIN_PARA +
                                WebAdminResponderThread.BOLD +
                                "Acknowledged Operation:[" +
                                WebAdminResponderThread.END_BOLD +
                                operation +
                                WebAdminResponderThread.BOLD +
                                "]" +
                                WebAdminResponderThread.END_BOLD +
                                WebAdminResponderThread.BREAK);

                sb.append(
                        WebAdminResponderThread.BEGIN_PARA +
                                WebAdminResponderThread.BOLD +
                                "Address Destination:[" +
                                WebAdminResponderThread.END_BOLD +
                                cmddestaddr.toString() +
                                WebAdminResponderThread.BOLD +
                                "]" +
                                WebAdminResponderThread.END_BOLD +
                                WebAdminResponderThread.END_PARA);
            } // End of Inner Else.
        } // End of Else

        // **************************************************
        // Finishup this Section.
        sb.append(WebAdminResponderThread.HTML_OPERATION_RESPONSE_END);

        // **************************************
        // Return.
        return sb.toString();
    } // End of processOperation Method.

    /**
     * Formulate a StackCommand from a incoming Operation and optional Address.
     *
     * @param operation
     * @param destination member object.
     * @return StackCommand Object, if null invalid operation.
     */
    protected StackCommand formulateOperationStackCommand(String operation,
                                                          Object destination) {
        // ****************************
        // Initialize.
        final String METHODNAME = "formulateOperationStackCommand";
        int command = StackCommand.CL_INVALID;
        Object payload = null;

        // ***********************************
        // Log our Method incoming Parameters.
        if (destination == null) {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.WEBADMIN_RESPONDER_THREAD_FORMULATING_OPCMD,
                    new String[]{operation, "none specified"});
        } else {
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.WEBADMIN_RESPONDER_THREAD_FORMULATING_OPCMD,
                    new String[]{operation, destination.toString()});
        } // End of Else.

        // ***********************************
        // Now Validate and Formulate the
        // Command to be issued.
        if (operation == null) {
            return null;
        }

        // ***********************************
        // Check for a Full Shutdown.
        if (operation.equalsIgnoreCase(WebAdminResponderThread.SHUTDOWNALL_VALUE)) {
            return new StackCommand(StackCommand.CL_SHUTDOWN, this.CONTROL_THREAD.my_addr);
        } // End of Check for Shutdown.

        // ***********************************************
        // Now all remaining commands need to have a
        // Destination Member.
        if (destination == null) {
            return null;
        }

        // ***********************************************
        // Check for Send Echo Command.
        if (operation.equalsIgnoreCase(WebAdminResponderThread.SENDECHO_VALUE)) {
            command = StackCommand.CL_ECHO;
            payload = null; // TODO Add something to payload.
        } // End of Check for Shutdown.

        // *************************************************
        // Check for Valid Commands, which are just commands.
        else if (operation.equalsIgnoreCase(WebAdminResponderThread.STARTREADER_VALUE)) {
            command = StackCommand.CL_START_READER;
        } else if (operation.equalsIgnoreCase(WebAdminResponderThread.STOPREADER_VALUE)) {
            command = StackCommand.CL_STOP_READER;
        } else if (operation.equalsIgnoreCase(WebAdminResponderThread.STARTREPLICA_VALUE)) {
            command = StackCommand.CL_START_REPLICA;
        } else if (operation.equalsIgnoreCase(WebAdminResponderThread.STOPREPLICA_VALUE)) {
            command = StackCommand.CL_STOP_REPLICA;
        } else if (operation.equalsIgnoreCase(WebAdminResponderThread.RESETSTAT_VALUE)) {
            command = StackCommand.CL_RESET_STATS;
        } else if (operation.equalsIgnoreCase(WebAdminResponderThread.SHUTDOWN_INSTANCE_VALUE)) {
            command = StackCommand.CL_SHUTDOWN;
        } // End of Command Checking.

        // *****************************
        // Return the StackCommand.
        if (command != StackCommand.CL_INVALID) {
            return new StackCommand(command,
                    this.CONTROL_THREAD.my_addr,
                    destination, payload);
        } else {
            return null;
        }
    } // End of formulateOperationStackCommand Private Method.

    /**
     * Locate the requested file and send it off.
     * This is usually for our logo and favicon.ico.
     */
    protected void sendResource(String uri, SocketChannel channel, String request)
            throws WebAdminRequestException, IOException {
        final String METHODNAME = "sendResource";

        // ************************************
        // Create our internal uri and obtain
        // resource.  The resourcename has
        // to be directed to our web package
        // to pick anything external up.
        String resourcename = uri;
        if (!resourcename.startsWith("/web")) {
            if (resourcename.startsWith("/")) {
                resourcename = "web" + resourcename;
            } else {
                resourcename = "web" + "/" + resourcename;
            }
        } // end of Check to fix URI.
        else {
            resourcename = resourcename.substring(1);
        } // End of Else.

        FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                MessageConstants.WEBADMIN_RESPONDER_THREAD_SENDING_NAMED_RESOURCE,
                new String[]{resourcename});

        // ***********************************
        // Now Obtain the Input Stream.
        InputStream ris = this.getClass().getResourceAsStream(resourcename);
        if (ris == null) {
            throw WebAdminRequestException.PAGE_NOT_FOUND;
        }

        // ********************************
        // Stuff the Bytes into our
        // Response Buffer.
        byte[] bytes = this.getBytes(ris);
        this.responseBuffers[1] = ByteBuffer.allocateDirect(bytes.length);
        this.responseBuffers[1].order(ByteOrder.nativeOrder());
        this.responseBuffers[1].put(bytes);

        // ***************************
        // Close off the Input Stream
        ris.close();

        // ****************************************
        // Perform the Write to the Client Channel.
        this.responseBuffers[0].rewind();
        this.responseBuffers[1].rewind();
        channel.write(this.responseBuffers);
    } // End of sendResource Response.

    /**
     * Send HTML Informational Error.
     *
     * @param channel
     * @param error
     * @throws Exception
     */
    protected void sendError(SocketChannel channel,
                             WebAdminRequestException error)
            throws Exception {
        ByteBuffer buffer = null;
        CharBuffer chars = CharBuffer.allocate(128);
        chars.put("HTTP/1.0 " + error.getErrorCode() + " OK\n");
        chars.put("Connection: close\n");
        chars.put("Server: FRAMEWORK RESILIENCY\n");
        chars.put("\n");
        chars.flip();

        // Translate the Unicode characters into ASCII bytes.
        buffer = charset.newEncoder().encode(chars);
        buffer.rewind();

        // ************************
        // Write the Buffer and
        // Close the Client Channel.
        channel.write(buffer);
        channel.close();
    } // End of SendError.

    /**
     * Shutdown our Child Thread.
     */
    protected void shutdown() {
        final String METHODNAME = "shutdown";
        this.running = false;
        try {
            this.readSelector.close();
        } catch (IOException ioe) {
            this.logStackTrace(ioe, METHODNAME);
        } // End of Exception Processing.
    } // End of shutdown method.

    /**
     * Get InputStream Byte Data.
     *
     * @param is InputStream from Channel.
     * @return
     * @throws java.io.IOException
     */
    private byte[] getBytes(InputStream is)
            throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        int n;
        baos.reset();
        while ((n = is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, n);
        }
        return baos.toByteArray();
    } // End of getBytes Private Method.

    /**
     * Determines if the incoming Client is Allowed
     * access to our WebAdmin Interface.
     *
     * @param clientaddress
     * @return
     */
    private boolean isClientAllowed(final String clientaddress) {
        final String METHODNAME = "isClientAllowed";
        // ****************************
        // Do we have an Allow List?
        if (allow_list == null) {
            return false;
        }

        // ****************************
        // Do we have a clientaddress?
        if ((clientaddress == null) ||
                (clientaddress.trim().equalsIgnoreCase(""))) {
            return false;
        }

        // *********************************
        // Synitize the Stringified Address
        String ca = new String(clientaddress);
        if (ca.startsWith("/")) {
            ca = ca.substring(1);
        }

        // ******************************
        // Now loop through our patterns
        for (int ae = 0; ae < this.allow_list.size(); ae++) {
            String allow_entry = (String) this.allow_list.get(ae);
            // *************************
            // Do we have a direct hit?
            if (allow_entry.equalsIgnoreCase(ca)) {
                return true;
            }

            // **************************************
            // Do we have a ANY or OR
            // Allow? if so force a
            // pattern, not really ncessary but
            // it looks better.
            else if ((allow_entry.equalsIgnoreCase(WebAdminResponderThread.ANY_NAME)) ||
                    (allow_entry.equalsIgnoreCase(WebAdminResponderThread.ALL_NAME))) {
                allow_entry = WebAdminResponderThread.ANY_IPV4ADDR_PATTERN;
            }

            try {
                if (Pattern.matches(allow_entry, ca)) {
                    return true;
                }
            } catch (PatternSyntaxException pse) {
                // *********************************************
                // Ignore Exception, it is not a valid pattern.
                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                        ErrorConstants.WEBADMIN_ACCEPTOR_IGNORING_PATTERN_SYNTAX,
                        new String[]{ca, pse.getMessage()});
            } // End of Exception Processing.
        } // End of For Loop.

        // ***************************
        // Deny Access.
        return false;
    } // End of isClientAllowed Private Method.

    // ***********************************************
    // Static Protected Methods used as Helpers to
    // Build a Web Page.
    // ***********************************************

    /**
     * Build a 2 Column Row
     */
    protected static String build2ColumnRow(String left, String right) {
        return WebAdminResponderThread.BEGIN_ROW +
                WebAdminResponderThread.BEGIN_COL +
                WebAdminResponderThread.BOLD +
                WebAdminResponderThread.SPAN_STYLE_BEGIN +
                left + ":" +
                WebAdminResponderThread.SPAN_STYLE_END +
                WebAdminResponderThread.END_BOLD +
                WebAdminResponderThread.END_COL +
                WebAdminResponderThread.BEGIN_COL +
                WebAdminResponderThread.SPAN_STYLE_BEGIN +
                right +
                WebAdminResponderThread.SPAN_STYLE_END +
                WebAdminResponderThread.END_COL +
                WebAdminResponderThread.END_ROW;
    } // End of build2ColumnRow

    /**
     * Build a 3 Column Row
     *
     * @param left
     * @param center
     * @param right
     * @return
     */
    protected static String build3ColumnRow(String left, String center, String right) {
        return WebAdminResponderThread.BEGIN_ROW +
                WebAdminResponderThread.BEGIN_COL +
                WebAdminResponderThread.BOLD +
                WebAdminResponderThread.SPAN_STYLE_BEGIN +
                left + ":" +
                WebAdminResponderThread.SPAN_STYLE_END +
                WebAdminResponderThread.END_BOLD +
                WebAdminResponderThread.END_COL +
                WebAdminResponderThread.BEGIN_COL +
                WebAdminResponderThread.SPAN_STYLE_BEGIN +
                center +
                WebAdminResponderThread.SPAN_STYLE_END +
                WebAdminResponderThread.END_COL +
                WebAdminResponderThread.BEGIN_COL +
                WebAdminResponderThread.SPAN_STYLE_BEGIN +
                right +
                WebAdminResponderThread.SPAN_STYLE_END +
                WebAdminResponderThread.END_COL +
                WebAdminResponderThread.END_ROW;
    } // End of build2ColumnRow

    /**
     * Build HTML for Membership List.
     *
     * @param vector
     * @param showlocalreturn
     * @return
     */
    protected static String buildMembershipList(List<Address> vector, boolean showlocalreturn) {
        StringBuffer sb = new StringBuffer();
        sb.append(WebAdminResponderThread.HTML_GROUP_MEMBERSHIP_BEGIN);
        sb.append(WebAdminResponderThread.BEGIN_TABLE);
        sb.append(WebAdminResponderThread.SPAN_STYLE_BEGIN);
        for (int i = 0; i < vector.size(); i++) {
            sb.append(
                    WebAdminResponderThread.BEGIN_ROW +
                            WebAdminResponderThread.BEGIN_COL +
                            WebAdminResponderThread.BOLD);
            if (i == 0) {
                sb.append(WebAdminResponderThread.SPAN_STYLE_BEGIN +
                        "Primary Coordinator:" +
                        WebAdminResponderThread.SPAN_STYLE_END);
            } else {
                sb.append(WebAdminResponderThread.SPAN_STYLE_BEGIN +
                        "Secondary PEER:" +
                        WebAdminResponderThread.SPAN_STYLE_END);
            } // End of Else.
            sb.append(
                    WebAdminResponderThread.END_BOLD +
                            WebAdminResponderThread.END_COL +
                            WebAdminResponderThread.BEGIN_COL +
                            WebAdminResponderThread.BEGIN_GROUP_URI +
                            Integer.toString(i) +
                            WebAdminResponderThread.BEGIN_GROUP_URI_ANCHOR_END +
                            WebAdminResponderThread.SPAN_STYLE_BEGIN +
                            vector.get(i).toString() +
                            WebAdminResponderThread.SPAN_STYLE_END +
                            WebAdminResponderThread.END_GROUP_URI +
                            WebAdminResponderThread.END_COL +
                            WebAdminResponderThread.END_ROW);
        } // End of For Loop.
        sb.append(WebAdminResponderThread.END_TABLE);

        // *****************************
        // Add the Operation Pull Down
        sb.append(WebAdminResponderThread.HTML_OPERATION_FORM_BEGIN);

        // **********************************
        // Add a Static Local Status Command
        if (showlocalreturn) {
            sb.append(
                    WebAdminResponderThread.buildOperationValue(
                            WebAdminResponderThread.LOCAL_VALUE,
                            WebAdminResponderThread.LOCAL_NAME));

            sb.append(
                    WebAdminResponderThread.buildOperationValue(
                            WebAdminResponderThread.LOCAL_REFRESH_VALUE,
                            WebAdminResponderThread.LOCAL_REFRESH_NAME));
        } // End of if to show local return.

        // **************************
        // Loop to Create Option List
        for (int i = 0; i < vector.size(); i++) {
            sb.append(
                    WebAdminResponderThread.buildOperationValues(
                            vector.get(i).toString()));
        } // End of For Loop to Build Available Operations.

        // ************************************
        // Add a Static SHUTDOWN-ALL Command
        sb.append(
                WebAdminResponderThread.buildOperationValue(
                        WebAdminResponderThread.SHUTDOWNALL_VALUE,
                        WebAdminResponderThread.SHUTDOWNALL_NAME));

        // ******************************
        // Finish off the Pull Down Form
        // and group membership area.
        sb.append(WebAdminResponderThread.HTML_OPERATION_FORM_END);
        sb.append(WebAdminResponderThread.HTML_GROUP_MEMBERSHIP_END);

        // ************************
        // Return String.
        return sb.toString();
    } // End of buildMembershipList

    /**
     * Build Operation Pull Down Value.
     *
     * @param value
     * @param name
     * @return
     */
    private static String buildOperationValues(final String paddrname) {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < WebAdminResponderThread.OPERATION_COMMAND_ARRAY.length; j++) {
            sb.append(
                    WebAdminResponderThread.buildOperationValue(
                            WebAdminResponderThread.OPERATION_COMMAND_ARRAY[j] + " " + paddrname,
                            WebAdminResponderThread.OPERATION_NAME_ARRAY[j] + " for " + paddrname));
        } // End of For Loop.
        // *************************
        // Return the String Buffer
        return sb.toString();
    } // End of buildOperationValue private Method.

    /**
     * Build Operation Pull Down Value.
     *
     * @param value
     * @param name
     * @return
     */
    private static String buildOperationValue(final String value,
                                              final String name) {
        if ((value != null) &&
                ((value.equalsIgnoreCase(WebAdminResponderThread.LOCAL_VALUE)) ||
                        (value.equalsIgnoreCase(WebAdminResponderThread.LOCAL_REFRESH_VALUE)))) {
            return
                    WebAdminResponderThread.zapHtml(
                            new String[]{value,
                                    name},
                            WebAdminResponderThread.HTML_OPERATION_VALUE);

        } else {
            return
                    WebAdminResponderThread.zapHtml(
                            new String[]{WebAdminResponderThread.OPERATION_NAME +
                                    value,
                                    name},
                            WebAdminResponderThread.HTML_OPERATION_VALUE);
        } // end of Else.
    } // End of buildOperationValue private Method.

    /**
     * Perform Replacement of Parametrized Values in Static HTML.
     *
     * @param sarray
     * @param html
     * @return String of zapped Html.
     */
    private static String zapHtml(final String[] sarray, final String html) {
        if ((sarray == null) ||
                (sarray.length == 0)) {
            return "";
        }
        // *****************************************
        // Formulate a new Parameterized HTML Value.
        StringBuffer sb = new StringBuffer(html);
        for (int p = 0; p < sarray.length; p++) {
            int i = sb.indexOf("%" + Integer.toString(p + 1));
            if (i > 0) {
                sb.replace(i, (i + 2), sarray[p]);
            } // End of Zap.
        } // End of For Loop.

        // *******************************
        // Return Option HTML
        return sb.toString();
    } // End of zapHtml.

    /**
     * Helper Method to Generate The MemberShip List HTML.
     *
     * @return String of HTML Formatted List.
     */
    private String generateMemberShipList() {
        StringBuffer sb = new StringBuffer();
        sb.append(
                WebAdminResponderThread.buildMembershipList(
                        this.CONTROL_THREAD.members, true));
        sb.append(WebAdminResponderThread.HR);
        // ***************
        // Return
        return sb.toString();
    } // End of private Helper Method.

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
                ErrorConstants.WEBADMIN_RESPONDER_EXCEPTION,
                new String[]{e.getMessage(), sw.toString()});
    } // End of logStackTrace private Method.
} ///:~ End of WebAdminResponderThread Class.

