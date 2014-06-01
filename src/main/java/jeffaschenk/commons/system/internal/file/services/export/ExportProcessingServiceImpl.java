package jeffaschenk.commons.system.internal.file.services.export;


import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.system.internal.file.services.ServiceTask;
import jeffaschenk.commons.system.internal.file.services.UtilityService;

import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceStateType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServicesEvent;
import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.touchpoint.model.dao.SystemDAO;
import jeffaschenk.commons.touchpoint.model.transitory.ExportStatistic;
import jeffaschenk.commons.types.StatusOutputType;
import jeffaschenk.commons.util.TimeDuration;
import jeffaschenk.commons.util.TimeStamp;
import jeffaschenk.commons.util.TimeUtils;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Export processing Service Implementation
 *
 * @author jeffaschenk@gmail.com
 */
@Service("exportProcessingService")
public class ExportProcessingServiceImpl implements GlobalConstants, ExportProcessingService,
        ApplicationContextAware, ApplicationEventPublisherAware {

    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(ExportProcessingServiceImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Global AutoWired Properties
     */
    @Value("#{systemEnvironmentProperties['export.zone.os.file.directory']}")
    private String exportZoneFileDirectoryName;

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

    private int currentTotalExportsProcessed = 0;

    private long currentTimeProcessingStarted = 0;

    private long currentTimeProcessingEnded = 0;

    private List<ExportStatistic> currentProcessingStatistic = new ArrayList<ExportStatistic>();

    private int lastTotalExportsProcessed = 0;

    private long lastTimeProcessingStarted = 0;

    private long lastTimeProcessingEnded = 0;

    private List<ExportStatistic> lastProcessingStatistic = new ArrayList<ExportStatistic>();


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
        logger.info("Export Processing Service Provider Facility is Ready and Available.");
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
                sb.append("<tr><td align='right'><b>Exports Processed:</b></td><td align='right'>" + currentTotalExportsProcessed + "</td></tr>");
                sb.append("<tr><td align='right'><b>Time Started:</b></td><td align='right'>" + TimeUtils.getDate(this.currentTimeProcessingStarted) + "</td></tr>");
                if (this.currentTimeProcessingStarted != this.currentTimeProcessingEnded) {
                    sb.append("<tr><td align='right'><b>Time Ended:</b></td><td align='right'>" + TimeUtils.getDate(this.currentTimeProcessingEnded) + "</td></tr>");
                    sb.append("<tr><td align='right'><b>Duration:</b></td><td align='right'>" + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.currentTimeProcessingEnded),
                            TimeUtils.getDate(this.currentTimeProcessingStarted))) + "</td></tr>");
                } else {
                    sb.append("<tr bgcolor='#00CD66'><td align='right'><b><i>Running</i></b></td></tr>");
                }
                //     List<ExportStatistic>
            } else {
                sb.append("<tr bgcolor='#BC8F8F'><td align='left' colspan=2><b><i>No Current Statistics at this time.</i></b></td></tr>");
            }
            sb.append("<tr><td colspan=2>&nbsp;</td></tr>");
            if (lastTimeProcessingStarted != 0) {
                sb.append("<tr><td align='left' colspan=2><b>Previous Processing:</b></td></tr>");
                sb.append("<tr><td align='right'><b>Exports Processed:</b></td><td align='right'>" + lastTotalExportsProcessed + "</td></tr>");
                sb.append("<tr><td align='right'><b>Time Started:</b></td><td align='right'>" + TimeUtils.getDate(this.lastTimeProcessingStarted) + "</td></tr>");
                sb.append("<tr><td align='right'><b>Time Ended:</b></td><td align='right'>" + TimeUtils.getDate(this.lastTimeProcessingEnded) + "</td></tr>");
                sb.append("<tr><td align='right'><b>Duration: </b></td><td align='right'>" + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.lastTimeProcessingEnded),
                        TimeUtils.getDate(this.lastTimeProcessingStarted))) + "</td></tr>");

                //     List<ExportStatistic>
            } else {
                sb.append("<tr bgcolor='#BC8F8F'><td align='left' colspan=2><b><i>No Previous Statistics at this time.</i></b></td></tr>");
            }
            sb.append("</table>");
        } else {
            // Show Textual Status
            if (currentTimeProcessingStarted != 0) {
                sb.append("\tCurrent Processing:\n");
                sb.append("\t Exports Processed: " + currentTotalExportsProcessed + '\n');
                sb.append("\t      Time Started: " + TimeUtils.getDate(this.currentTimeProcessingStarted) + '\n');
                if (this.currentTimeProcessingStarted != this.currentTimeProcessingEnded) {
                    sb.append("\t        Time Ended: " + TimeUtils.getDate(this.currentTimeProcessingEnded) + '\n');
                    sb.append("\t          Duration: " + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.currentTimeProcessingEnded),
                            TimeUtils.getDate(this.currentTimeProcessingStarted))) + '\n');
                } else {
                    sb.append("\t        * Running * " + '\n');
                }
                //     List<ExportStatistic>
            } else {
                sb.append("\tNo Current Statistics at this time.\n");
            }
            sb.append("\n");


            if (lastTimeProcessingStarted != 0) {
                sb.append("\tPrevious Processing:\n");
                sb.append("\t Exports Processed: " + lastTotalExportsProcessed + '\n');
                sb.append("\t       Time Started: " + TimeUtils.getDate(this.lastTimeProcessingStarted) + '\n');
                sb.append("\t         Time Ended: " + TimeUtils.getDate(this.lastTimeProcessingEnded) + '\n');
                sb.append("\t           Duration: " + TimeDuration.getElapsedToTextString(TimeUtils.getTimeDifference(TimeUtils.getDate(this.lastTimeProcessingEnded),
                        TimeUtils.getDate(this.lastTimeProcessingStarted))) + '\n');

                //     List<ExportStatistic>
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
     * Perform the Export LifeCycle.
     *
     * @param exportEntityClasses
     * @param performWait
     */
    @Override
    public synchronized void performExportLifeCycle
    (List<Class<? extends RootElement>> exportEntityClasses, boolean performWait) {
        // *****************************************
        // Initialize
        TimeDuration td = new TimeDuration();
        td.start();

        // *****************************************
        // Publish a Life Cycle Services Event
        LifeCycleServicesEvent event = new LifeCycleServicesEvent(this, LifeCycleServiceType.EXPORT,
                LifeCycleServiceStateType.BEGIN, TimeUtils.now());
        publisher.publishEvent(event);
        // *****************************************
        // Place existing current into Previous
        // processing.
        this.lastTotalExportsProcessed = this.currentTotalExportsProcessed;
        this.lastTimeProcessingStarted = this.currentTimeProcessingStarted;
        this.lastTimeProcessingEnded = this.currentTimeProcessingEnded;
        this.lastProcessingStatistic = new ArrayList<ExportStatistic>(this.currentProcessingStatistic);

        this.currentTotalExportsProcessed = 0;
        this.currentTimeProcessingStarted = TimeUtils.now();
        this.currentTimeProcessingEnded = this.currentTimeProcessingStarted;
        this.currentProcessingStatistic = new ArrayList<ExportStatistic>();

        int exports_queued = 0;

        // **************************************
        // Perform the Main File Processing Loop.
        try {
            // Acquire Export directory.
            File exportDirectory = new File(exportZoneFileDirectoryName);
            if (!this.utilityService.isZoneDirectoryValid(exportDirectory)) {
                logger.warn("Unable to process Export due to Export Directory:["
                        + exportDirectory.getAbsolutePath() + "] is Invalid!");
                this.currentTimeProcessingEnded = TimeUtils.now();
                td.stop();
                return;
            }

            // ************************************
            // Begin Export Task Loop
            List<ExportWorkerTask> taskList = new ArrayList<ExportWorkerTask>();
            Iterator<Class<? extends RootElement>> entityClassIterator =
                    exportEntityClasses.iterator();

            while (entityClassIterator.hasNext()) {
                Class<? extends RootElement> clazz = entityClassIterator.next();
                // *********************************
                // Construct our Export File Name
                File exportFile = new File(exportDirectory.getAbsolutePath() + File.separatorChar +
                        clazz.getSimpleName()+ "s" + "_" + TimeStamp.getTimeStamp() + EXPORT_FILE_SUFFIX);
                // *********************************
                // Instantiate our new Statistic
                ExportStatistic statistic = new ExportStatistic(exportFile, clazz, this.systemDAO.getRowCount(clazz));

                // *********************************
                // Construct a Thread for each File
                // and let each execute.
                ExportWorkerTask task = new ExportWorkerTask(statistic);
                taskList.add(task);
                taskExecutor.execute(task);
                exports_queued++;
            } // End of Export  Loop.

            // *********************************************************
            // Now Wait until all our Threads have completed processing.
            if (performWait) {
                long sleep_time = 1000; // Only one Tic.
                while (true) {
                    // ********************************************************
                    // Checked for a Processed
                    Iterator<ExportWorkerTask> taskIterator = taskList.iterator();
                    while (taskIterator.hasNext()) {
                        ExportWorkerTask task = taskIterator.next();
                        if ((task.getStatistic().isDone()) && (!task.getStatistic().isSaved())) {
                            task.getStatistic().setSaved(true);
                            this.currentProcessingStatistic.add(task.getStatistic());
                            this.currentTotalExportsProcessed++;
                        }
                    }
                    if (this.currentTotalExportsProcessed == exports_queued) {
                        break;
                    }
                    // ***************************
                    // Sleep for a sec or so....
                    this.sleep(sleep_time);
                } // End of While Wait Loop.

                // *****************************************
                // Publish a Life Cycle Services Event
                event = new LifeCycleServicesEvent(this, LifeCycleServiceType.EXPORT,
                        LifeCycleServiceStateType.DONE, TimeUtils.now());
                publisher.publishEvent(event);
                // ********************************************************
                // All Spawned Tasks Completed.
                this.currentTimeProcessingEnded = TimeUtils.now();
                td.stop();
            }
        } finally {
            // *******************************************
            // Show Statistics.
            logger.info("This Cycle Number of Processed Exports:[" + this.currentTotalExportsProcessed + "], " +
                    "Duration:[" + td.getElapsedtoString() + "]");
        }

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
     * Helper method to Export WGA Objects from
     * Persistent Store to Export File for Client Consumption
     * to recycle back into their data update process.
     */
    protected ExportStatistic export(ExportStatistic statistic) {
        // **********************************
        // Log the Export Process
        logger.info("Processing Export for Entities:[" + statistic.getAssociatedClass().getSimpleName() + "] " +
                ", Existing DB Row Count:[" + statistic.getExistingRowCount() + "], Export FileName:[" +
                statistic.getExportFile());
        BufferedWriter output_writer = null;
        FileOutputStream file_output_stream;
        List<? extends RootElement> elements;
        try {
            file_output_stream = new FileOutputStream(statistic.getExportFile());
            output_writer = new BufferedWriter(new OutputStreamWriter(file_output_stream));
            // Now obtain a list of all Entity Objects
            // May need to page this, to reduce memory footprint depending upon
            // Number of Elements.
            elements = systemDAO.getAllElementsForClass(statistic.getAssociatedClass());
            // Export Each Element to our Export File.
            Iterator<? extends RootElement> elementIterator = elements.iterator();
            while (elementIterator.hasNext()) {
                RootElement rootElement = elementIterator.next();
                if (statistic.getRowsHeader() == 0) {
                    // Output Export Header....
                    output_writer.write(rootElement.getExportHeader());
                    output_writer.newLine();
                    statistic.incrementRowsHeader();
                }
                // Marshal Object to Export Format
                if (rootElement.isEmptyForExport())
                {
                    // Eliminate Null Rows, should not occur,
                    // but in our tests it did from copied data from original demographics.
                    continue;
                }
                output_writer.write(rootElement.export());
                output_writer.newLine();
                statistic.incrementRowsWrittenToFile();
            }
            // ************************************
            // Set Statistic Task Object to Done.
            statistic.setProcessed(true);
            statistic.setDone(true);
            statistic.setStopTime(TimeUtils.now());
            // ************************************
            // Determine if exported any data
            if ((statistic.getExportFile().length() == 0) &&
                    (statistic.getRowsWrittenToFile() == 0) &&
                    (statistic.getRowsHeader() == 0)) {

                if (output_writer != null) {
                    try {
                        output_writer.flush();
                        output_writer.close();
                    } catch (IOException ioe) {
                        // NoOP;
                    }
                    output_writer = null;
                }

                // Remove the Zero Byte File.
                statistic.getExportFile().delete();
            }
        } catch (IOException ioe) {
            String errorMessage = "Fatal IOException Encountered Writing Export Filename:[" + statistic.getExportFile() + "], " + ioe.getMessage();
            // ****************************************
            // We had an IO Exception processing File.
            statistic.setProcessed(false);
            statistic.setDone(true);
            statistic.setStopTime(TimeUtils.now());
            statistic.incrementFileErrors();
            statistic.getErrorMessages().add(errorMessage);
            logger.error(errorMessage, ioe);
        } catch (Exception e) {
            String errorMessage = "Runtime Exception Encountered Writing Export Filename:[" + statistic.getExportFile() + "], " + e.getMessage();
            // ****************************************
            // We had an IO Exception processing File.
            statistic.setProcessed(false);
            statistic.setDone(true);
            statistic.setStopTime(TimeUtils.now());
            statistic.incrementFileErrors();
            statistic.getErrorMessages().add(errorMessage);
            logger.error(errorMessage, e);
        }    finally {
            if (output_writer != null) {
                try {
                    output_writer.flush();
                    output_writer.close();
                } catch (IOException ioe) {
                    // NoOP;
                }
            }
            // Free Large List
            elements = null;
        }
        // *********************************
        // return Statistic for this Load.
        return statistic;
    }

    /**
     * Task to Perform File Export Processing in a
     * distinct thread.
     */
    protected class ExportWorkerTask extends ServiceTask implements Runnable {

        private ExportStatistic statistic;

        public ExportStatistic getStatistic() {
            return statistic;
        }

        public void setStatistic(ExportStatistic statistic) {
            this.statistic = statistic;
        }

        /**
         * Default Constructor
         *
         * @param statistic
         */
        public ExportWorkerTask(ExportStatistic statistic) {
            this.statistic = statistic;
        }


        public void run() {
            // **********************
            // Export Entity to File.
            export(this.statistic);
            statistic.setDone(true);
            logger.info("Processed: " + statistic);
        } // End of Inner Class Thread.


    }

}
