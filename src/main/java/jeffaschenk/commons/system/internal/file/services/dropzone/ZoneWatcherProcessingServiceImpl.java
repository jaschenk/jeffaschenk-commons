package jeffaschenk.commons.system.internal.file.services.dropzone;


import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.system.internal.file.services.extract.ExtractProcessingService;
import jeffaschenk.commons.system.internal.file.services.extract.ExtractProcessingTask;
import jeffaschenk.commons.system.internal.scheduling.LocalSchedulingService;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceStateType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServicesEvent;
import jeffaschenk.commons.touchpoint.model.transitory.WatcherStatistic;
import jeffaschenk.commons.types.StatusOutputType;
import jeffaschenk.commons.types.WatcherStatisticType;
import jeffaschenk.commons.util.StringUtils;
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
import java.io.IOException;
import java.nio.file.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;


/**
 * Zone Watcher Processing Service Implementation
 *
 * @author jeffaschenk@gmail.com
 */
@Service("zoneWatcherProcessingService")
public class ZoneWatcherProcessingServiceImpl implements ZoneWatcherProcessingService,
        ApplicationContextAware, ApplicationEventPublisherAware, GlobalConstants {

    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(ZoneWatcherProcessingServiceImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Global Last Time Extract Processing triggered.
     */
    private long lastTimeExtractProcessingTriggered = -1;

    /**
     * Global AutoWired Properties
     */
    @Value("#{systemEnvironmentProperties['drop.zone.os.file.directory']}")
    private String dropZoneFileDirectoryName;

    @Value("#{systemEnvironmentProperties['drop.zone.magic.number.of.files.trigger']}")
    private String magicDropZoneNumberOfFilesTriggerString;
    private int magicDropZoneNumberOfFilesTrigger;

    @Value("#{systemEnvironmentProperties['drop.zone.magic.filename.prefix.trigger']}")
    private String magicDropZoneFilenamePrefixTrigger;

    /**
     * Extract Service
     */
    @Autowired
    private ExtractProcessingService extractProcessingService;

    /**
     * Task Executor
     */
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * Scheduler Service
     */
    @Autowired
    private LocalSchedulingService schedulerService;

    /**
     * Zone Watcher Thread Object
     */
    private ZoneWatcherTask zoneWatcherTask;

    /**
     * Singleton Globals for Watch Service
     */
    private WatchService watcher;
    private Map<WatchKey, WatcherStatistic> watcherStatistics = new HashMap<WatchKey, WatcherStatistic>();

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

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
        logger.info("Zone Watcher Service Provider Facility is Initializing.");
        this.magicDropZoneNumberOfFilesTrigger =
                (StringUtils.isEmpty(this.magicDropZoneNumberOfFilesTriggerString)?
                    -1:Integer.parseInt(this.magicDropZoneNumberOfFilesTriggerString));
        try {
            if (StringUtils.isEmpty(this.dropZoneFileDirectoryName)) {
                logger.info("No Zone Watcher Directory specified, Service Provider Facility is not Available.");
                this.initialized = false;
                return;
            }
            this.watcher = FileSystems.getDefault().newWatchService();
            // Register the Drop Zone Directory to Monitor
            this.registerDirectoryToWatch(WatcherStatisticType.DROP_ZONE, Paths.get(dropZoneFileDirectoryName));

            // Now Start the Actual Service Worker Thread to perform the Watching Facility.
            zoneWatcherTask = new ZoneWatcherTask();
            this.taskExecutor.execute(zoneWatcherTask);
            this.initialized = true;
            logger.info("Zone Watcher Service Provider Facility is Ready and Available.");
        } catch (IOException ioe) {
            logger.error("Issue Establishing File System Watch Service, unable to provide Zone Watcher Services!", ioe);
        }
    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public synchronized void destroy() {
        try {
            if (this.initialized) {
                if (this.zoneWatcherTask != null) {
                    this.zoneWatcherTask.setStopProcess(true);
                    this.watcherStatistics.clear();
                    this.watcher.close();
                }
                logger.info("Zone Watcher Service Provider Facility has been Shutdown.");
            }
        } catch (IOException ioe) {
            logger.error("Issue Finalizing File System Watch Service!", ioe);
        }
    }

    /**
     * Provide status of Zone Watcher Processing Service.
     */
    @Override
    public String status(StatusOutputType statusOutputType) {
        StringBuilder sb = new StringBuilder();
        // TODO

        return sb.toString();
    }

    /**
     * Provide Running Status
     */
    public boolean isRunning() {
        return (this.initialized);
    }

    /**
     * Register the Specified directory with the WatchService
     */
    private void registerDirectoryToWatch(WatcherStatisticType watcherStatisticType, Path dir) throws IOException {
        WatchKey key = dir.register(this.watcher, ENTRY_CREATE, ENTRY_DELETE);
        this.watcherStatistics.put(key, new WatcherStatistic(watcherStatisticType, dir));
    }

    /**
     * Zone Watcher Task Thread.
     */
    private class ZoneWatcherTask implements Runnable {

        private ZoneWatcherTask() {
        }

        private boolean stopProcess = false;

        protected boolean isStopProcess() {
            return stopProcess;
        }

        protected void setStopProcess(boolean stopProcess) {
            this.stopProcess = stopProcess;
        }

        /**
         * Perform Zone Watcher Task.
         */
        public void run() {
            try {
                // Avoid a Spring Bug, which would cause a Hang
                // when we publish an event too quickly at startup.
                Thread.sleep(10*1000);
            } catch (InterruptedException x) {
                // NoOp
            }
            // ************************************
            // Publish a Life Cycle Services Event
            LifeCycleServicesEvent event = new LifeCycleServicesEvent(this, LifeCycleServiceType.ZONE,
                    LifeCycleServiceStateType.BEGIN, TimeUtils.now());
            publisher.publishEvent(event);
            // ************************************
            // Begin Zone Thread Loop.
            while (true) {
                if (isStopProcess()) {
                    break;
                }
                // *****************************************
                // Wait for key to be signalled based upon
                // Registered Events to appear
                WatchKey watchKey;
                try {
                    watchKey = watcher.take();
                } catch (InterruptedException x) {
                    if (isStopProcess()) {
                        break;
                    }
                    return;
                }
                // Find our Statistic Bucket Reference..
                WatcherStatistic watcherStatistic = watcherStatistics.get(watchKey);
                if (watcherStatistic == null) {
                    continue;
                }
                // ****************************************
                // Poll for Events
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                    if (isStopProcess()) {
                        break;
                    }
                    WatchEvent.Kind kind = watchEvent.kind();
                    // We have Overflowed the event Stack,
                    // for now Ignore.
                    if (kind == OVERFLOW) {
                        logger.warn("Overflow has occurred on Watcher Event Stack, some Events may have been Lost and not Processed!");
                        continue;
                    }

                    // Context for directory entry event is the file name of entry
                    WatchEvent<Path> ev = cast(watchEvent);
                    Path name = ev.context();
                    Path child = watcherStatistic.getWatcherPath().resolve(name);

                    // Log the Event
                    if (logger.isWarnEnabled()) {
                        logger.warn("Zone Watcher Event:[" + kind.name() + "], File:[" + child + "]");
                    }

                    // Update Statistic Buckets
                    if (kind == ENTRY_CREATE) {
                        watcherStatistic.incrementCycleFileCreations();
                        watcherStatistic.incrementTotalFileCreations();
                        // If we are monitoring for a specific file to be created
                        // for triggering an Extract run.
                        if ((StringUtils.isNotEmpty(magicDropZoneFilenamePrefixTrigger)) &&
                                (child.toFile().getName().startsWith(magicDropZoneFilenamePrefixTrigger))) {
                            watcherStatistic.setCycleFilenamePrefixTriggerPresent(true);
                            // Remove the Trigger File.
                            child.toFile().delete();
                        }
                    } else if (kind == ENTRY_DELETE) {
                        watcherStatistic.incrementCycleFileDeletions();
                        watcherStatistic.incrementTotalFileDeletions();
                    }

                    // ********************************************
                    // Review the Statistic Bucket just used and
                    // determine if any Tasks should be auto-started
                    // based upon new Files being present.
                    //
                    if ((watcherStatistic.getWatcherStatisticType() == WatcherStatisticType.DROP_ZONE) &&
                            (!extractProcessingService.isRunning())) {
                        // Is our Magic Number Processing Used?
                        if ((magicDropZoneNumberOfFilesTrigger > 0) &&
                                (watcherStatistic.getCycleFileCreations() >= magicDropZoneNumberOfFilesTrigger)) {
                            triggerProcess(watcherStatistic, "->DropZone Number of Files Trigger Detected:[" + magicDropZoneNumberOfFilesTrigger + "]");
                            // Clear Cycle Statistics for this Object.
                            watcherStatistic.setCycleFileCreations(0);
                            watcherStatistic.setCycleFileDeletions(0);
                            // Is our Magic FileName Prefix Specified?
                        } else if (StringUtils.isNotEmpty(magicDropZoneFilenamePrefixTrigger) &&
                                (watcherStatistic.isCycleFilenamePrefixTriggerPresent())) {
                            triggerProcess(watcherStatistic, "->DropZone Filename Prefix Trigger Detected:[" + magicDropZoneFilenamePrefixTrigger + "]");
                            // Clear Cycle Statistics for this Object.
                            watcherStatistic.setCycleFilenamePrefixTriggerPresent(false);
                            watcherStatistic.setCycleFileCreations(0);
                            watcherStatistic.setCycleFileDeletions(0);
                        }
                    } // End of Check for Extract Trigger.

                    //
                    // Place other Auto Trigger Events Here, based upon
                    // Task to be Scheduled or Executed.
                    //

                } // End of Inner Polling Loop per Key
                // ********************************************
                // reset key and remove from set if directory
                // no longer accessible
                boolean valid = watchKey.reset();
                if (!valid) {
                    watcherStatistics.remove(watchKey);
                    // all directories are inaccessible
                    if (watcherStatistics.isEmpty()) {
                        break;
                    }
                }
            } // Outer while Loop
            // Publish a Life Cycle Services Event
            event = new LifeCycleServicesEvent(this, LifeCycleServiceType.ZONE,
                    LifeCycleServiceStateType.DONE, TimeUtils.now());
            publisher.publishEvent(event);
            logger.warn("Zone Watcher Processing Thread has Ended.");
        } // End of run Method

        /**
         * Common Private Helper method to perform/schedule the Extract process
         *
         * @param watcherStatistic
         * @param whyScheduled     - string indicating why we where triggered.
         */
        private void triggerProcess(WatcherStatistic watcherStatistic, String whyScheduled) {
            logger.warn("Zone Watcher Detected a Cycle of New Extract Files located in the Drop Zone, Scheduling Extract " + whyScheduled);
            if (extractProcessingService.isRunning())
            {
                logger.warn("However, Zone Watcher Detected processing of an Existing Extract already running at this time.");
            } else {

            //else if ( (lastTimeExtractProcessingTriggered <= 0) ||
            //            ((TimeUtils.now() - lastTimeExtractProcessingTriggered) >= DEFAULT_EXTRACT_EXECUTION_TIME_WINDOW) ) {
                lastTimeExtractProcessingTriggered = TimeUtils.now();
                watcherStatistic.setTimeLastTrigger(lastTimeExtractProcessingTriggered);
                // Schedule Task for Now.
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 60);
                schedulerService.scheduleTask(new ExtractProcessingTask(extractProcessingService), cal.getTime());
           // } else {
           //     logger.warn("However, Zone Watcher Detected a Cycle has already been scheduled previously at " +
           //             TimeUtils.getDate(lastTimeExtractProcessingTriggered)
           //             + " and will not submit another Extract at this time.");
           // }
           }
        }

    } /// End of Inner Class

}
