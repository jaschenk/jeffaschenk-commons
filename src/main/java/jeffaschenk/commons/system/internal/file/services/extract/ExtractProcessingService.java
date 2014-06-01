package jeffaschenk.commons.system.internal.file.services.extract;

import jeffaschenk.commons.types.StatusOutputType;

/**
 * Extract Processing Service Interface
 * <p/>
 * Provides Extract Processing Services to Application Framework.
 */
public interface ExtractProcessingService {

    /**
     * Perform the Extract LifeCycle.
     */
    void performExtractLifeCycle(boolean performArchive, boolean performWait);

    /**
     * Provide status of Extract LifeCycle.
     */
    String status(StatusOutputType statusOutputType);

    /**
     * Provide Running Status
     */
    boolean isRunning();
}
