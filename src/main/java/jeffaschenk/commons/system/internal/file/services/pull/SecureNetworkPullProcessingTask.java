package jeffaschenk.commons.system.internal.file.services.pull;


import jeffaschenk.commons.system.internal.file.services.ServiceTask;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;

/**
 * Task to Perform File Import Processing in a
 * distinct thread.
 */
public class SecureNetworkPullProcessingTask extends ServiceTask implements Runnable {

    private SecureNetworkPullService secureNetworkPullService;

    /**
     * Default Constructor
     *
     * @param secureNetworkPullService
     */
    public SecureNetworkPullProcessingTask(SecureNetworkPullService secureNetworkPullService) {
        this.secureNetworkPullService = secureNetworkPullService;
        this.setLifeCycleServiceType(LifeCycleServiceType.IMPORT);
    }

    public void run() {
        // **********************************
        // Perform the Extract Life Cycle
        try {
            this.secureNetworkPullService.performImportLifeCycle(true, true); // Archive and Wait.
        } catch (Exception e) {
            logger.error("Exception Encountered during Secure Network Pull Life-cycle Processing: " + e.getMessage(), e);
        }
    } // end of Thread

}
