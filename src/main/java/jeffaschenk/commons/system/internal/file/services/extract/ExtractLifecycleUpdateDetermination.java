package jeffaschenk.commons.system.internal.file.services.extract;


import jeffaschenk.commons.touchpoint.model.RootElement;

/**
 * Extract Processing Service Interface
 * <p/>
 * Provides Extract Processing Services to Application Framework.
 */
public interface ExtractLifecycleUpdateDetermination {

    /**
     * Provide indication if Update is Pending or not.
     *
     * If Pending, no update will occur against the Database.
     *
     */
    boolean isUpdatePending(RootElement extractEntity);
    
}
