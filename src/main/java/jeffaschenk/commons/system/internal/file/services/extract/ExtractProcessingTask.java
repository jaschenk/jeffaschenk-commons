package jeffaschenk.commons.system.internal.file.services.extract;

import jeffaschenk.commons.system.internal.file.services.ServiceTask;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;

/**
 * Task to Perform File Extract Processing in a
 * distinct thread.
 */
public class ExtractProcessingTask extends ServiceTask implements Runnable {

    private ExtractProcessingService extractProcessingService;

    /**
     * Default Constructor
     *
     * @param extractProcessingService
     */
    public ExtractProcessingTask(ExtractProcessingService extractProcessingService) {
        this.extractProcessingService = extractProcessingService;
        this.setLifeCycleServiceType(LifeCycleServiceType.EXTRACT);
    }

    public void run() {
        // **********************************
        // Perform the Extract Life Cycle
        try {
            this.extractProcessingService.performExtractLifeCycle(true, true); // Archive and Wait.
        } catch (Exception e) {
            logger.error("Exception Encountered during Extract Life-cycle Processing: " + e.getMessage(), e);
        }
    } // end of Thread

}
