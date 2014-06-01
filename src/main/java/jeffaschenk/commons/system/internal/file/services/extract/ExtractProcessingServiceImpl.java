package jeffaschenk.commons.system.internal.file.services.extract;


import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.system.internal.file.services.ServiceTask;
import jeffaschenk.commons.system.internal.file.services.UtilityService;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceStateType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServicesEvent;
import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.touchpoint.model.dao.SystemDAO;
import jeffaschenk.commons.touchpoint.model.transitory.ExtractLoadStatistic;
import jeffaschenk.commons.types.StatusOutputType;
import jeffaschenk.commons.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.*;


/**
 * Extract processing Service Implementation
 *
 * @author jeffaschenk@gmail.com
 */
@Service("extractProcessingService")
public class ExtractProcessingServiceImpl implements GlobalConstants, ExtractProcessingService,
        ApplicationContextAware, ApplicationEventPublisherAware {

    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(ExtractProcessingServiceImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Global AutoWired Properties
     */
    @Value("#{systemEnvironmentProperties['drop.zone.os.file.directory']}")
    private String dropZoneFileDirectoryName;

    @Value("#{systemEnvironmentProperties['processed.zone.os.file.directory']}")
    private String processedZoneFileDirectoryName;

    @Value("#{systemEnvironmentProperties['drop.zone.perform.file.slicing']}")
    private String performFileSlicingString;
    private boolean performFileSlicing;

    /**
     * Utility and DAO Services.
     */
    @Autowired
    private UtilityService utilityService;

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * Global Statistic Counters
     */

    private int currentTotalFilesProcessed = 0;

    private long currentTimeProcessingStarted = 0;

    private long currentTimeProcessingEnded = 0;

    private List<ExtractLoadStatistic> currentProcessingStatistic = new ArrayList<ExtractLoadStatistic>();

    private int lastTotalFilesProcessed = 0;

    private long lastTimeProcessingStarted = 0;

    private long lastTimeProcessingEnded = 0;

    private List<ExtractLoadStatistic> lastProcessingStatistic = new ArrayList<ExtractLoadStatistic>();

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
        this.performFileSlicing = StringUtils.toBoolean(this.performFileSlicingString, false);
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
     * Provide status of Export LifeCycle.
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

                //    private List<ExtractLoadStatistic> currentProcessingStatistic = new ArrayList<ExtractLoadStatistic>();

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

                //    private List<ExtractLoadStatistic> lastProcessingStatistic = new ArrayList<ExtractLoadStatistic>();
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

                //    private List<ExtractLoadStatistic> currentProcessingStatistic = new ArrayList<ExtractLoadStatistic>();
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

                //    private List<ExtractLoadStatistic> lastProcessingStatistic = new ArrayList<ExtractLoadStatistic>();
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
     * Perform the Extract LifeCycle.
     */
    @Override
    public synchronized void performExtractLifeCycle(boolean performArchive, boolean performWait) {
        // *****************************************
        // Initialize
        TimeDuration td = new TimeDuration();
        td.start();

        // *****************************************
        // Publish a Life Cycle Services Event
        LifeCycleServicesEvent event = new LifeCycleServicesEvent(this, LifeCycleServiceType.EXTRACT,
                LifeCycleServiceStateType.BEGIN, TimeUtils.now());
        publisher.publishEvent(event);
        // *****************************************
        // Place existing current into Previous
        // processing.
        this.lastTotalFilesProcessed = this.currentTotalFilesProcessed;
        this.lastTimeProcessingStarted = this.currentTimeProcessingStarted;
        this.lastTimeProcessingEnded = this.currentTimeProcessingEnded;
        this.lastProcessingStatistic = new ArrayList<ExtractLoadStatistic>(this.currentProcessingStatistic);

        this.currentTotalFilesProcessed = 0;
        this.currentTimeProcessingStarted = TimeUtils.now();
        this.currentTimeProcessingEnded = this.currentTimeProcessingStarted;
        this.currentProcessingStatistic = new ArrayList<ExtractLoadStatistic>();

        int files_skipped = 0;

        // *****************************************
        // Initialize Processing map for Classes
        Map<Class<? extends RootElement>, Long> currentStartingRowCountStatistics =
                new HashMap<>();

        // ***********************************
        // Acquire drop zone directory.
        File dropZoneDirectory = new File(dropZoneFileDirectoryName);

        // ***********************************
        // If in Archive mode, clean-up
        // from previous run, by removing
        // previous Processed Files.
        try {
            if (performArchive) {
                this.cleanupPreviousProcessing();
            }
            // ************************************
            // Prepare to read any Compressed Files
            // Check for archive files, prior to
            // Slicing.
            for (File extractFile : dropZoneDirectory.listFiles()) {
                if (ArchiveUtility.isArchive(extractFile)) {
                    // *******************************************
                    // Extract the Compressed Archive
                    logger.info("Processing Compressed Archive:[" + extractFile.getName() + "]");
                    try {
                        if (ArchiveUtility.decompressArchiveFile(extractFile, dropZoneDirectory)) {
                            logger.info("Compressed Extract Archive File:[" + extractFile.getName() + "], successfully Decompressed.");
                        } else {
                            logger.error("Compressed Extract Archive File:[" + extractFile.getName() + "], was not Decompressed Successfully, " +
                                    "please see previous Messages!");
                        }
                    } catch (IOException ioe) {
                        logger.error("IO Exception occurred Decompressing Extract Archive File:["
                                + extractFile.getName() + "], " + ioe.getMessage());
                    }
                    // *******************************************
                    // Move the Archive to the Processed Zone.
                    String processedFileName =
                            processedZoneFileDirectoryName + File.separator + extractFile.getName();
                    extractFile.renameTo(new File(processedFileName));
                }
            }

            // ***********************************
            // Analyze the Files in this Drop.
            // Determine if any need to be
            // split for faster processing.
            if (this.performFileSlicing) {
                this.performFileSlicingFunction();
            }
            // **************************************
            // Perform the Main File Processing Loop.
            int tasks_queued = 0;
            if (!this.utilityService.isZoneDirectoryValid(dropZoneDirectory)) {
                logger.warn("Unable to process Extract Files due to Drop zone Directory:["
                        + dropZoneDirectory.getAbsolutePath() + "] is Invalid!");
                return;
            }
            // ************************************
            // Begin File Loop
            List<ParseExtractFileTask> taskList = new ArrayList<ParseExtractFileTask>();
            for (File extractFile : dropZoneDirectory.listFiles()) {
                // ****************************************************
                // Move any Original Sliced Files over to Processed.
                if ((performArchive) && (extractFile.getName().contains(FILE_SLICED))) {
                    String processedFileName =
                            processedZoneFileDirectoryName + File.separatorChar + extractFile.getName();
                    extractFile.renameTo(new File(processedFileName));
                    continue;
                }
                // ****************************************************
                // Remove any Previous *NEW* Temporary Files.
                if (extractFile.getName().startsWith(NEW_FILE_PREFIX)) {
                    extractFile.delete();
                    continue;
                }
                // *********************************
                // Obtain the Class Definition for this File.
                Class<? extends RootElement> clazz = this.utilityService.getClassBasedOnExtractFilename(extractFile.getName());
                if (clazz == null) {
                    logger.warn("Skipping File:["
                            + extractFile.getName() + "], since not associated to a WGA DB Class!");
                    files_skipped++;
                    continue;
                }

                // ****************************************
                // Check for PreProcessing for this File,
                // only perform PreProcessing once for the
                // first segment or shard.
                //
                if (!currentStartingRowCountStatistics.containsKey(clazz)) {
                    // We do not have a current Row Count for this object/table
                    // So First perform any PReProcessing for this table.
                    ExtractLifecyclePreProcessing
                            extractLifecyclePreProcessing
                            = this.utilityService.getBeanBasedOnExtractEntityClassNameForPreProcessing(clazz.getName());
                    if (extractLifecyclePreProcessing != null) {
                        logger.info("Performing Pre-Processing for Entity:[" + clazz.getName() + "]");
                        if (extractLifecyclePreProcessing.performPreProcessing(clazz)) {
                            logger.info("Pre-Processing for Entity:[" + clazz.getName() + "], was successfully run.");
                        } else {
                            logger.warn("Pre-Processing for Entity:[" + clazz.getName() + "], was not successfully run.");
                        }
                    }
                    // ***************************************
                    // Add our Class and exiting Row Count
                    // after any pre-Processing.
                    currentStartingRowCountStatistics.put(clazz, this.systemDAO.getRowCount(clazz));
                }

                // *********************************
                // Instantiate our new Statistic
                Long rowCount = currentStartingRowCountStatistics.get(clazz);
                ExtractLoadStatistic statistic =
                        new ExtractLoadStatistic(extractFile, clazz, ((rowCount != null) ? rowCount : 0L));
                logger.info("Established Pre-Processing Row Count for Class:[" + clazz.getName() + "] used was:[" + rowCount + "]");
                // *********************************
                // Construct a Thread for each File
                // and let each execute.
                statistic.setPerformArchive(performArchive);
                ParseExtractFileTask task = new ParseExtractFileTask(statistic);
                taskList.add(task);
                taskExecutor.execute(task);
                tasks_queued++;
            } // End of Extract File located in the Drop zone For Loop.

            // *********************************************************
            // Now Wait until all our Threads have completed processing.
            if (performWait) {
                long sleep_time = 1000; // Only one Tic.
                while (true) {
                    // ********************************************************
                    // Checked for a Processed
                    int tasks_completed = 0;
                    Iterator<ParseExtractFileTask> taskIterator = taskList.iterator();
                    while (taskIterator.hasNext()) {
                        ParseExtractFileTask task = taskIterator.next();
                        if (task.getStatistic().isDone()) {
                            tasks_completed++;
                            if (!task.getStatistic().isSaved()) {
                                task.getStatistic().setSaved(true);
                                currentProcessingStatistic.add(task.getStatistic());
                                this.currentTotalFilesProcessed++;
                            }
                        }
                    }
                    if (tasks_completed == tasks_queued) {
                        logger.info("All Queued Tasks have Completed, Ending Parent Wait Loop.");
                        break;
                    }
                    if (taskExecutor.getActiveCount() == 0) {
                        logger.info("Task Executor has no Active Running Tasks, Ending Parent Wait Loop.");
                        break;
                    }
                    // ***************************
                    // Sleep for a sec or so....
                    this.sleep(sleep_time);
                } // End of While Wait Loop.
            } // End of perform Wait.
            // *****************************************
            // All Processing has completed, so now
            // perform any post processing for any of the
            // Tables/Objects which were effected in this
            // Extract run.
            for (Class<? extends RootElement> postProcessingClazz : currentStartingRowCountStatistics.keySet()) {
                // We do not have a current Row Count for this object/table
                // So First perform any PReProcessing for this table.
                ExtractLifecyclePostProcessing
                        extractLifecyclePostProcessing
                        = this.utilityService.getBeanBasedOnExtractEntityClassNameForPostProcessing(postProcessingClazz.getName());
                if (extractLifecyclePostProcessing != null) {
                    logger.info("Post-Processing for Entity:[" + postProcessingClazz.getName() + "]");
                    if (extractLifecyclePostProcessing.performPostProcessing(postProcessingClazz)) {
                        logger.info("Post-Processing for Entity:[" + postProcessingClazz.getName() + "], was successfully run.");
                    } else {
                        logger.warn("Post-Processing for Entity:[" + postProcessingClazz.getName() + "], was not successfully run.");
                    }
                }
            }
            // *****************************************
            // Publish a Life Cycle Services Event
            event = new LifeCycleServicesEvent(this, LifeCycleServiceType.EXTRACT,
                    LifeCycleServiceStateType.DONE, TimeUtils.now());
            publisher.publishEvent(event);
            // ********************************************************
            // All Spawned Tasks Completed.
            this.currentTimeProcessingEnded = TimeUtils.now();
            td.stop();
        } finally {
            // *******************************************
            // Show Statistics.
            logger.info("This Cycle Number of Processed Files:[" + this.currentTotalFilesProcessed + "], Skipped Files:[" +
                    files_skipped + "],  Duration:[" + td.getElapsedtoString() + "]");
        }
    }

    /**
     * Helper Method to Slice Extract Files into several
     * Files, depending upon size to process these
     * Larger Files Faster.
     */
    private void performFileSlicingFunction() {
        // ***************************************
        // Acquire drop zone directory.
        File dropZoneDirectory = new File(dropZoneFileDirectoryName);
        if (!this.utilityService.isZoneDirectoryValid(dropZoneDirectory)) {
            return;
        }
        // ************************************
        // Begin File Loop
        for (File extractFile : dropZoneDirectory.listFiles()) {
            Class<? extends RootElement> clazz = this.utilityService.getClassBasedOnExtractFilename(extractFile.getName());
            if (clazz == null) {
                continue;
            }
            if ((extractFile.getName().contains(FILE_SLICE)) ||
                    (extractFile.getName().contains(FILE_SLICED))) {
                continue;
            }
            if (extractFile.length() <= FILE_SLICE_THRESHOLD) {
                continue;
            }
            // **********************************
            // Slice Extract File
            logger.warn("FileName:[" + extractFile.getName() + "], being Sliced into Segments.");
            BufferedReader input_reader;
            FileInputStream file_input_stream;
            String input_line;

            BufferedWriter output_writer = null;
            FileOutputStream file_output_stream;

            long rows = 0;
            long slices = 0;
            long rows_in_slice = 0;
            String header = null;

            try {
                file_input_stream = new FileInputStream(extractFile);
                input_reader = new BufferedReader(new InputStreamReader(file_input_stream), READER_BUFFER_SIZE);
                // Read Each Line of the Extract File to Duplicate in a new Slice.
                while ((input_line = input_reader.readLine()) != null) {
                    rows++;
                    // Save Header Line.
                    if (rows == 1) {
                        header = input_line.trim();
                        continue;
                    }
                    // **************************************************
                    // Now we have Data determine if we need a new
                    // Slice?
                    if ((output_writer == null) || (rows_in_slice >= FILE_SLICE_SIZE)) {
                        if (output_writer != null) {
                            // Close Existing Slice.
                            output_writer.flush();
                            output_writer.close();
                        }
                        slices++;
                        rows_in_slice = 0;
                        String sliceFileName = extractFile.getName();
                        sliceFileName.trim();
                        sliceFileName = sliceFileName.replace(EXTRACT_FILE_SUFFIX.toLowerCase(), "").replace(EXTRACT_FILE_SUFFIX.toUpperCase(), "");
                        sliceFileName = extractFile.getParent() + File.separatorChar +
                                sliceFileName +
                                FILE_SLICE + NumberUtility.formatSliceNumber(slices) + EXTRACT_FILE_SUFFIX;

                        File slice = new File(sliceFileName);
                        file_output_stream = new FileOutputStream(slice);
                        output_writer = new BufferedWriter(new OutputStreamWriter(file_output_stream));

                        output_writer.write(header);
                        output_writer.newLine();
                        rows_in_slice++;
                    }
                    // **************************************************
                    // Write Slice Row.
                    output_writer.write(input_line);
                    output_writer.newLine();
                    rows_in_slice++;
                } // End of Inner While.

                // ***************************************************
                // Close Up File Processing, Rename Existing File
                // to new Sliced Name.
                output_writer.flush();
                output_writer.close();
                input_reader.close();

                extractFile.renameTo(new File(extractFile.getParent() + File.separatorChar + FILE_SLICED + extractFile.getName()));

            } catch (Exception e) {
                logger.error("Exception Occurred while Slicing FileName:[" + extractFile.getName() + "]", e);
            }

        } // End of Extract File located in the Drop zone For Loop.
    }

    /**
     * Private Helper to Sleep the current Thread.
     *
     * @param sleep_time
     */
    private void sleep(long sleep_time) {
        try {
            Thread.sleep(sleep_time);
        } catch (InterruptedException ie) {
            // NoOp
        }
    }

    /**
     * Helper method to Parse an incoming Extract File to WGA Objects
     * and Persistent Store.  Multiple Threads will use this Method.
     * Needs to maintain Thread-Safety.
     */
    protected ExtractLoadStatistic parseExtractFile(ExtractLoadStatistic statistic) {
        // **********************************
        // Log the Extract Process
        logger.info("Processing Extract FileName:[" + statistic.getExtractFile().getName() +
                "], File Size:[" + statistic.getExtractFile().length() + "]B, Existing DB Row Count:[" + statistic.getExistingRowCount() + "]");
        // **********************************
        // Obtain Random Next for Sequencing.
        Random rand = new Random();
        // **********************************
        // Read Extract File
        // and initialize
        BufferedReader input_reader = null;
        FileInputStream file_input_stream = null;
        String input_line;
        try {
            // **************************************************
            // Is this a PHI Authorization File?
            // If, Apply a Fix to Original Data File and process
            // newly fixed temporary file.
            if (statistic.getExtractFile().getName().contains(PHI_FILE_PREFIX)) {
                File newPHIFixedFile = new File(statistic.getExtractFile().getParent() + File.separatorChar +
                        NEW_FILE_PREFIX + statistic.getExtractFile().getName());
                FileUtility.fixPHI_AUTHORIZATION_FILE(statistic.getExtractFile(), newPHIFixedFile);
                file_input_stream = new FileInputStream(newPHIFixedFile);
            } else {
                file_input_stream = new FileInputStream(statistic.getExtractFile());
            }
            // ***********************************
            // Check for any special processing
            // Interface available based upon
            // Entity Class.
            // Cache this for during this phase of
            // parse.
            ExtractLifecycleUpdateDetermination
                    extractLifecycleUpdateDetermination =
                    this.utilityService.getBeanBasedOnExtractEntityClassNameForUpdateDetermination(statistic.getAssociatedClass().getName());
            // **************************************************
            // Begin to Processing the File.
            input_reader = new BufferedReader(new InputStreamReader(file_input_stream), READER_BUFFER_SIZE);
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
                    // ***********************************
                    // Check for any special processing
                    // necessary for Entities which
                    // updates are performed and
                    // the backing update transactions
                    // have yet to make it to
                    if ((extractLifecycleUpdateDetermination != null) &&
                            (extractLifecycleUpdateDetermination.isUpdatePending(extractElement))) {
                        logger.warn("Skipping Update from Incoming Extract, " +
                                "since pending Update Detected for:[" + extractElement.getAlternateId() + "]");
                        statistic.incrementRowsSkippedDueToPendingUpdates();
                    } else {
                        // *********************************
                        // Persist to Database.
                        if (statistic.getExistingRowCount() == 0) {
                            systemDAO.createEntity(extractElement);
                        } else {
                            systemDAO.createOrUpdateEntity(extractElement);
                        }
                        statistic.incrementRowsProcessed();
                    } // End of Check for Pending Lifecycle Updates.
                } catch (Exception e) {
                    String stackTrace = getStackTraceAsString(e);  // Consume Stack to Parse.
                    // Set up Default Message.
                    StringBuffer errorMessage = new StringBuffer().append("Row:[" + statistic.getRowsReadFromFile() + "]");
                    if (extractElement != null) {
                        errorMessage.append(" Alt Id:[" + extractElement.getAlternateId() + "]");
                        errorMessage.append(" Table:[" + extractElement.getTableName() + "]");
                        errorMessage.append(GlobalConstants.NEWLINE+ "Element:[" + extractElement.toString() + "]"
                                +GlobalConstants.NEWLINE);
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
            String errorMessage = "Fatal IOException Encountered processing Extract Filename:[" + statistic.getExtractFile() + "], " + ioe.getMessage();
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
     * Helper method to clean up Previous Processing.
     */
    private void cleanupPreviousProcessing() {
        logger.warn("Cleaning up previously precessed files.");
        File processedZoneDirectory = new File(processedZoneFileDirectoryName);
        if (!this.utilityService.isZoneDirectoryValid(processedZoneDirectory)) {
            logger.warn("Unable to clean-up previous Processing due to Processed zone Directory:["
                    + processedZoneDirectory.getAbsolutePath() + "] is Invalid!");
            return;
        }
        // ************************************
        // Clean-up Loop.
        for (File processedFile : processedZoneDirectory.listFiles()) {
            processedFile.delete();
        }
    }


    /**
     * Private Helper Method to Allow Analyzing Stack Trace to
     * pull goodies out as needed.
     *
     * @param exception
     * @return String
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

    /**
     * Simple Inner Class to Perform File Extract Processing in a
     * distinct thread.
     */
    private class ParseExtractFileTask extends ServiceTask implements Runnable {

        private ExtractLoadStatistic statistic;

        public ExtractLoadStatistic getStatistic() {
            return statistic;
        }

        public void setStatistic(ExtractLoadStatistic statistic) {
            this.statistic = statistic;
        }

        /**
         * Default Constructor
         *
         * @param statistic
         */
        public ParseExtractFileTask(ExtractLoadStatistic statistic) {
            this.statistic = statistic;
        }

        public void run() {
            // *****************
            // Process File.
            parseExtractFile(this.statistic);
            // ***************************************
            // Now Perform end of file processing.
            // Timestamp and move file to processed
            // saved zone.
            if ((statistic.isPerformArchive()) &&
                    (StringUtils.isNotEmpty(processedZoneFileDirectoryName)) &&
                    (!processedZoneFileDirectoryName.equals(dropZoneFileDirectoryName))) {

                // **************************************************
                // Is this a PHI Authorization File?
                // If, removed Fixed File.
                if (statistic.getExtractFile().getName().contains(PHI_FILE_PREFIX)) {
                    new File(statistic.getExtractFile().getParent() + File.separatorChar +
                            NEW_FILE_PREFIX + statistic.getExtractFile().getName());
                    File newPHIFixedFile = new File(statistic.getExtractFile().getParent() + File.separatorChar +
                            NEW_FILE_PREFIX + statistic.getExtractFile().getName());
                    if (newPHIFixedFile.exists()) {
                        newPHIFixedFile.delete();
                    }
                }

                // ***************************************************
                // Archive the Processed File.
                String processedFileName =
                        processedZoneFileDirectoryName + File.separatorChar + statistic.getExtractFile().getName();
                statistic.setArchived(true);
                statistic.getExtractFile().renameTo(new File(processedFileName));
            } // End of Performing Archive of Processed File
            statistic.setDone(true);
            logger.info("Processed: " + statistic);
        } // End of Inner Class Thread.
    } // End of Inner Class


}
