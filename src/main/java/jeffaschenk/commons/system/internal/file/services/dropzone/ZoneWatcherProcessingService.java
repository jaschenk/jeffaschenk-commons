package jeffaschenk.commons.system.internal.file.services.dropzone;


import jeffaschenk.commons.types.StatusOutputType;

/**
 * Zone Watcher Processing Service Interface
 * <p/>
 * Provides Extract Processing Services to Application Framework.
 */
public interface ZoneWatcherProcessingService {

    /**
     * Provide status of Zone Watcher Processing Service.
     */
    String status(StatusOutputType statusOutputType);

    /**
     * Provide Running Status
     */
    boolean isRunning();

}
