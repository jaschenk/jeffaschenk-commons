package jeffaschenk.commons.system.internal.file.services.pull;


import jeffaschenk.commons.types.StatusOutputType;

/**
 * Secure Network Copy/Pull Processing Service Interface
 * <p/>
 * Provides Import/Pull of Data from External Secure Drop Locations
 * <p/>
 * The completion of a Copy Pull Service would then Trigger a Import to the
 * Internal Database.  Additional LifeCycles may be provided Pre or Post
 * of any respective service.
 */
public interface SecureNetworkPullService {

    /**
     * Perform the Import LifeCycle.
     */
    void performImportLifeCycle(boolean performArchive, boolean performWait);

    /**
     * Provide status of Extract LifeCycle.
     */
    String status(StatusOutputType statusOutputType);

    /**
     * Provide Running Status
     */
    boolean isRunning();


}
