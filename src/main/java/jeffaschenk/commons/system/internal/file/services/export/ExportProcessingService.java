package jeffaschenk.commons.system.internal.file.services.export;


import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.types.StatusOutputType;

import java.util.List;

/**
 * Export Processing Service Interface
 * <p/>
 * Provides Export Processing Services to Application Framework.
 * @author jeffaschenk@gmail.com
 */
public interface ExportProcessingService {

    /**
     * Perform the Export LifeCycle.
     *
     * @param exportEntityClasses
     * @param performWait
     */
    public void performExportLifeCycle(List<Class<? extends RootElement>> exportEntityClasses, boolean performWait);

    /**
     * Provide status of Export LifeCycle.
     */
    String status(StatusOutputType statusOutputType);

    /**
     * Provide Running Status
     */
    boolean isRunning();
}
