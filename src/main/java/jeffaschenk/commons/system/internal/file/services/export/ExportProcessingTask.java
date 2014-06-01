package jeffaschenk.commons.system.internal.file.services.export;

import jeffaschenk.commons.system.internal.file.services.ServiceTask;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;
import jeffaschenk.commons.touchpoint.model.RootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Task to Perform File Export Processing in a
 * distinct thread.
 */
public class ExportProcessingTask extends ServiceTask implements Runnable {

    private ExportProcessingService exportProcessingService;

    /**
     * Default Constructor
     *
     * @param exportProcessingService
     */
    public ExportProcessingTask(ExportProcessingService exportProcessingService) {
        this.exportProcessingService = exportProcessingService;
        this.setLifeCycleServiceType(LifeCycleServiceType.EXPORT);
    }

    public void run() {
        // **********************************
        // Perform the Export Life Cycle
        try {
            List<Class<? extends RootElement>> exportEntityClasses =
                    new ArrayList<>(2);

            // TODO Add Relevant Classes...
            //exportEntityClasses.add(wga_demographics_profile_update.class);
            //exportEntityClasses.add(wga_web_payment_transaction.class);

            this.exportProcessingService.performExportLifeCycle(exportEntityClasses, true);
        } catch (Exception e) {
            logger.error("Exception Encountered during Export Life-Cycle Processing: " + e.getMessage(), e);
        }
    } // End of Class Thread.
}
