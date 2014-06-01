package jeffaschenk.commons.system.internal.file.services.pull;

import com.jcraft.jsch.*;
import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.system.internal.file.services.UtilityService;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceStateType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServicesEvent;
import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.touchpoint.model.dao.SystemDAO;
import jeffaschenk.commons.touchpoint.model.transitory.ImportLoadStatistic;
import jeffaschenk.commons.types.StatusOutputType;
import jeffaschenk.commons.util.StringUtils;
import jeffaschenk.commons.util.TimeDuration;
import jeffaschenk.commons.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Secure Network Pull Service Processing Implementation
 *
 * @author jeffaschenk@gmail.com
 */
@Service("secureNetworkPullService")
public class SecureNetworkPullServiceImpl implements GlobalConstants, SecureNetworkPullService,
        ApplicationContextAware, ApplicationEventPublisherAware {

    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(SecureNetworkPullServiceImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Global AutoWired Properties
     */
    @Value("#{systemEnvironmentProperties['secure.mbox.hostname']}")
    private String secureMBoxHostname;

    @Value("#{systemEnvironmentProperties['secure.mbox.principal']}")
    private String secureMBoxPrincipal;

    @Value("#{systemEnvironmentProperties['secure.mbox.credential']}")
    private String secureMBoxCredential;

    @Value("#{systemEnvironmentProperties['secure.mbox.directory']}")
    private String secureMBoxDirectory;


    /**
     * Utility and DAO Services.
     */
    @Autowired
    private UtilityService utilityService;

    @Autowired
    private SystemDAO systemDAO;

    /**
     * Global Statistic Counters
     */

    private int currentTotalFilesProcessed = 0;

    private long currentTimeProcessingStarted = 0;

    private long currentTimeProcessingEnded = 0;

    private List<ImportLoadStatistic> currentProcessingStatistic = new ArrayList<ImportLoadStatistic>();

    private int lastTotalFilesProcessed = 0;

    private long lastTimeProcessingStarted = 0;

    private long lastTimeProcessingEnded = 0;

    private List<ImportLoadStatistic> lastProcessingStatistic = new ArrayList<ImportLoadStatistic>();

    /**
     * Spring Application Context,
     * used to obtain access to Resources on
     * Classpath.
     */
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Spring Application Event Publisher
     */
    private ApplicationEventPublisher publisher;

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Initialize the Service Provider Interface
     */
    @PostConstruct
    public synchronized void initialize() {
        logger.info("Extract Processing Service Provider Facility is Ready and Available.");
        this.initialized = true;
    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public synchronized void destroy() {
        if (this.initialized) {
            logger.info("Extract Processing Service Provider Facility has been Shutdown.");
        }
    }

    /**
     * Provide status of Import LifeCycle.
     */
    public String status(StatusOutputType statusOutputType) {
        StringBuilder sb = new StringBuilder();

        if (statusOutputType.equals(StatusOutputType.HTML)) {
            // Show HTML Status
            sb.append("<table>");
            if (currentTimeProcessingStarted != 0) {
                sb.append("<tr><td align='left' colspan=2><b>Current Processing:</b></td></tr>");
                sb.append("<tr><td align='right'><b>Files Processed:</b></td><td align='right'>" + currentTotalFilesProcessed + "</td></tr>");
                sb.append("<tr><td align='right'><b>Time Started:</b></td><td align='right'>" + TimeUtils.getDate(this.currentTimeProcessingStarted) + "</td></tr>");
                if (this.currentTimeProcessingStarted != this.currentTimeProcessingEnded) {
                    sb.append("<tr><td align='right'><b>Time Ended:</b></td><td align='right'>" + TimeUtils.getDate(this.currentTimeProcessingEnded) + "</td></tr>");
                    sb.append("<tr><td align='right'><b>Duration:</b></td><td align='right'>" + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.currentTimeProcessingEnded),
                            TimeUtils.getDate(this.currentTimeProcessingStarted))) + "</td></tr>");
                } else {
                    sb.append("<tr bgcolor='#00CD66'><td align='right'><b><i>Running</i></b></td></tr>");
                }

            } else {
                sb.append("<tr bgcolor='#BC8F8F'><td align='left' colspan=2><b><i>No Current Statistics at this time.</i></b></td></tr>");
            }
            sb.append("<tr><td colspan=2>&nbsp;</td></tr>");
            if (lastTimeProcessingStarted != 0) {
                sb.append("<tr><td align='left' colspan=2><b>Previous Processing:</b></td></tr>");
                sb.append("<tr><td align='right'><b>Files Processed:</b></td><td align='right'>" + lastTotalFilesProcessed + "</td></tr>");
                sb.append("<tr><td align='right'><b>Time Started: </b></td><td align='right'>" + TimeUtils.getDate(this.lastTimeProcessingStarted) + "</td></tr>");
                sb.append("<tr><td align='right'><b>Time Ended: </b></td><td align='right'>" + TimeUtils.getDate(this.lastTimeProcessingEnded) + "</td></tr>");
                sb.append("<tr><td align='right'><b>Duration: </b></td><td align='right'>" + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.lastTimeProcessingEnded),
                        TimeUtils.getDate(this.lastTimeProcessingStarted))) + "</td></tr>");
            } else {
                sb.append("<tr bgcolor='#BC8F8F'><td align='left' colspan=2><b><i>No Previous Statistics at this time.</i></b></td></tr>");
            }
            sb.append("</table>");
        } else {
            // Show Textual Status
            if (currentTimeProcessingStarted != 0) {
                sb.append("\tCurrent Processing:\n");
                sb.append("\t   Files Processed: " + currentTotalFilesProcessed + '\n');
                sb.append("\t      Time Started: " + TimeUtils.getDate(this.currentTimeProcessingStarted) + '\n');
                if (this.currentTimeProcessingStarted != this.currentTimeProcessingEnded) {
                    sb.append("\t        Time Ended: " + TimeUtils.getDate(this.currentTimeProcessingEnded) + '\n');
                    sb.append("\t          Duration: " + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.currentTimeProcessingEnded),
                            TimeUtils.getDate(this.currentTimeProcessingStarted))) + '\n');
                } else {
                    sb.append("\t        * Running * " + '\n');
                }
            } else {
                sb.append("\tNo Current Statistics at this time.\n");
            }
            sb.append("\n");
            if (lastTimeProcessingStarted != 0) {
                sb.append("\tPrevious Processing:\n");
                sb.append("\t    Files Processed: " + lastTotalFilesProcessed + '\n');
                sb.append("\t       Time Started: " + TimeUtils.getDate(this.lastTimeProcessingStarted) + '\n');
                sb.append("\t         Time Ended: " + TimeUtils.getDate(this.lastTimeProcessingEnded) + '\n');
                sb.append("\t           Duration: " + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.lastTimeProcessingEnded),
                        TimeUtils.getDate(this.lastTimeProcessingStarted))) + '\n');
            } else {
                sb.append("\tNo Previous Statistics at this time.\n");
            }

        }

        return sb.toString();
    }

    /**
     * Provide Running Status
     */
    public boolean isRunning() {
        if ((this.currentTimeProcessingStarted != 0) &&
                (this.currentTimeProcessingStarted == this.currentTimeProcessingEnded)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Perform the Import LifeCycle.
     */
    @Override
    public synchronized void performImportLifeCycle(boolean performArchive, boolean performWait) {
        // *****************************************
        // Initialize
        TimeDuration td = new TimeDuration();
        td.start();

        // *****************************************
        // Publish a Life Cycle Services Event
        LifeCycleServicesEvent event = new LifeCycleServicesEvent(this, LifeCycleServiceType.IMPORT,
                LifeCycleServiceStateType.BEGIN, TimeUtils.now());
        publisher.publishEvent(event);
        // *****************************************
        // Place existing current into Previous
        // processing.
        this.lastTotalFilesProcessed = this.currentTotalFilesProcessed;
        this.lastTimeProcessingStarted = this.currentTimeProcessingStarted;
        this.lastTimeProcessingEnded = this.currentTimeProcessingEnded;
        this.lastProcessingStatistic = new ArrayList<ImportLoadStatistic>(this.currentProcessingStatistic);

        this.currentTotalFilesProcessed = 0;
        this.currentTimeProcessingStarted = TimeUtils.now();
        this.currentTimeProcessingEnded = this.currentTimeProcessingStarted;
        this.currentProcessingStatistic = new ArrayList<ImportLoadStatistic>();

        int files_skipped = 0;

        // ***********************************
        // Secure Channel
        ChannelSftp channelSftp = null;
        // ***********************************
        // If in Archive mode, clean-up
        // from previous run, by removing
        // previous Processed Files.
        try {

            JSch jsch = new JSch();
            int port = 22;

            Session session = jsch.getSession(secureMBoxPrincipal, secureMBoxHostname, port);
            session.setPassword(secureMBoxCredential);

            //
            // Setup Strict HostKeyChecking so that we dont get
            // the unknown host key exception
            //
            java.util.Properties config = new
                    java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            channelSftp.cd("./" + secureMBoxDirectory);

            java.util.Vector vv = channelSftp.ls("./");
            if (vv != null) {
                for (int ii = 0; ii < vv.size(); ii++) {
                    Object obj = vv.elementAt(ii);
                    if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                        ChannelSftp.LsEntry ls_entry = (com.jcraft.jsch.ChannelSftp.LsEntry) obj;   // Cast
                        if ( (ls_entry.getAttrs().isDir()) || (ls_entry.getFilename().startsWith(".")) )
                            { continue; }

                        // TODO Interrogate File Name and Timestamp.
                        logger.info("FOUND Directory Entry:[" + ls_entry.getFilename() +
                                "], File Size:" + ls_entry.getAttrs().getSize()+"B, " +
                                " Mod Time:[" + ls_entry.getAttrs().getMtimeString() + "]");

                    }
                }
            }

            // TODO Determine which file to obtain.....

            // *********************************
            // Obtain the Class Definition for this File.
            Class<? extends RootElement> clazz = this.utilityService.getDefaultClassInstance();

            // *********************************
            // Instantiate our new Statistic
            ImportLoadStatistic statistic = new ImportLoadStatistic(null, clazz, this.systemDAO.getRowCount(clazz));
            statistic.setPerformArchive(performArchive);
            statistic.setFileViaSecureNetworkChannel(true);

            statistic.setNetworkFileName("sample.txt");     // TODO Fix Me

            // ***********************************
            // Obtain Our Secure InputStream
            // and perform the Import.
            InputStream inputStream = channelSftp.get(statistic.getNetworkFileName());
            this.parseImportFile(statistic, inputStream);
            this.currentTotalFilesProcessed++;
            logger.info("Processed: " + statistic);
            // *****************************************
            // Publish a Life Cycle Services Event
            event = new LifeCycleServicesEvent(this, LifeCycleServiceType.IMPORT,
                    LifeCycleServiceStateType.DONE, TimeUtils.now());
            publisher.publishEvent(event);
            // ********************************************************
            // All Spawned Tasks Completed.
            this.currentTimeProcessingEnded = TimeUtils.now();
            td.stop();

        } catch (SftpException e) {
            // TODO

        } catch (JSchException e) {
            // TODO

        } finally

        {
            if (channelSftp != null) {
                  channelSftp.disconnect();
            }

            // *******************************************
            // Show Statistics.
            logger.info("This Cycle Number of Processed Files:[" + this.currentTotalFilesProcessed + "], Skipped Files:[" +
                    files_skipped + "],  Duration:[" + td.getElapsedtoString() + "]");
        }
    }

    /**
     * Helper method to Parse an incoming Import Files to JnJ Objects
     * and Persistent Store.
     */
    protected ImportLoadStatistic parseImportFile(ImportLoadStatistic statistic, InputStream inputStream) {
        // **********************************
        // Log the Extract Process
        logger.info("Processing Import Secure Network Channel for:[" +
                statistic.getNetworkFileName() + "]");
        // **********************************
        // Read Extract File
        // and initialize
        BufferedReader input_reader = null;
        String input_line;
        try {
            // **************************************************
            // Begin to Processing the File.
            input_reader = new BufferedReader(new InputStreamReader(inputStream), GlobalConstants.READER_BUFFER_SIZE);
            // Read Each Line of the Extract File.
            while ((input_line = input_reader.readLine()) != null) {
                statistic.incrementRowsReadFromFile();
                // Save Header Line.
                if (statistic.getRowsReadFromFile() == 1) {
                    statistic.setPipeHeader(new String(input_line));
                    statistic.incrementRowsSkippedHeader();
                    continue;
                }
                // Skip Any Blank Lines.
                if (StringUtils.isEmpty(input_line)) {
                    statistic.incrementRowsSkipped();
                    continue;
                }
                // Skip Any Lines with no Identifier
                if ((input_line.startsWith(DEFAULT_GLOBAL_PIPE_CHARACTER)) ||
                        (!input_line.contains(DEFAULT_GLOBAL_PIPE_CHARACTER))) {
                    statistic.incrementRowsSkippedNoID();
                    String errorMessage = "Row:[" + statistic.getRowsReadFromFile() + "] Error Message: "
                            + "NO ALT ID Found!";
                    statistic.getErrorMessages().add(errorMessage);
                    continue;
                }
                // *****************************
                // Parse the Extract Line Input.
                List<String> parsedInput = RootElement.parseExtractFileRow(input_line);
                if ((parsedInput == null) || (parsedInput.isEmpty())) {
                    statistic.incrementRowsSkipped();
                    continue;
                }
                // *****************************
                // Marshal into Object.
                RootElement extractElement = null;
                try {
                    extractElement = statistic.getAssociatedClass().newInstance();
                    if (extractElement == null) {
                        String errorMessage = "Row:[" + statistic.getRowsReadFromFile() + "] Error Message: "
                                + "Extract Element was Null, Logic Error!";
                        statistic.incrementRowsInError();
                        statistic.getErrorMessages().add(errorMessage);
                        if (logger.isTraceEnabled()) {
                            logger.trace(errorMessage);
                        }
                        continue;
                    }
                    // ***********************************
                    // Initialize the Object.
                    extractElement.initializeFromParsedExtract(parsedInput);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Processing File Row " + statistic.getRowsReadFromFile() + ": " + extractElement.toString());
                    }
                    // *********************************
                    // Persist to Database.
                    if (statistic.getExistingRowCount() == 0) {
                        systemDAO.createEntity(extractElement);
                    } else {
                        systemDAO.createOrUpdateEntity(extractElement);
                    }
                    statistic.incrementRowsProcessed();
                } catch (Exception e) {
                    String stackTrace = getStackTraceAsString(e);  // Consume Stack to Parse.
                    // Set up Default Message.
                    StringBuffer errorMessage = new StringBuffer().append("Row:[" + statistic.getRowsReadFromFile() + "]");
                    if (extractElement != null) {
                        errorMessage.append(" Alt Id:[" + extractElement.getAlternateId() + "]");
                        errorMessage.append(" Table:[" + extractElement.getTableName() + "]");
                    }
                    // Inspect Stack Trace Output
                    if ((StringUtils.isNotEmpty(stackTrace)) && (stackTrace.contains(CONSTRAINT_VIOLATION_EXCEPTION))) {
                        // Try to Find the Primary Key Violation
                        int index = stackTrace.indexOf(CAUSED_BY_BATCH_UP_EXCEPTION);
                        if (index > 0) {
                            String message = stackTrace.substring(index + CAUSED_BY_BATCH_UP_EXCEPTION.length());
                            index = message.indexOf(FOR_KEY);
                            if (index > 0) {
                                message = message.substring(0, index);
                            }
                            errorMessage.append(message + NEWLINE);
                        }
                    } else {
                        errorMessage.append(" Error Message: "
                                + e.getMessage() + ((e.getCause() == null) ? "" : ", Cause: " + e.getCause().getMessage()));
                    }
                    statistic.incrementRowsInError();
                    statistic.getErrorMessages().add(errorMessage.toString());
                    if (logger.isTraceEnabled()) {
                        logger.error(errorMessage.toString(), e);
                    }
                }
            } // End of While Loop.

            // **********************************
            // We completed an Entire File.
            statistic.setProcessed(true);
            statistic.setStopTime(TimeUtils.now());
        } catch (IOException ioe) {
            String errorMessage = "Fatal IOException Encountered processing Secure Channel File Copy:["
                    + statistic.getNetworkFileName() + "], " + ioe.getMessage();
            // ****************************************
            // We had an IO Exception processing File.
            statistic.setProcessed(false);
            statistic.setStopTime(TimeUtils.now());
            statistic.incrementFileErrors();
            statistic.getErrorMessages().add(errorMessage);
            logger.error(errorMessage, ioe);
        } finally {
            if (input_reader != null) {
                try {
                    input_reader.close();
                } catch (IOException ioe) {
                    // NoOP;
                }
            }
        }
        // *********************************
        // return Statistic for this Load.
        return statistic;
    }

    /**
     * Private Helper Method to Allow Analyzing Stack Trace to
     * pull goodies out as needed.
     *
     * @param exception
     * @return  String
     */
    private String getStackTraceAsString(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.print(" [ ");
        pw.print(exception.getClass().getName());
        pw.print(" ] ");
        pw.print(exception.getMessage());
        exception.printStackTrace(pw);
        return sw.toString();
    }

}
