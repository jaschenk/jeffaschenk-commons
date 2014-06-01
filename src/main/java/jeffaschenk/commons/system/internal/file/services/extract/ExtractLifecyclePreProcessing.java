package jeffaschenk.commons.system.internal.file.services.extract;


import jeffaschenk.commons.touchpoint.model.RootElement;

/**
 * Extract Pre Processing Service Interface
 * <p/>
 * This Component will be accessed after all
 * Extract Processing has completed on the associated Class
 * and Table.
 */
public interface ExtractLifecyclePreProcessing {

    /**
     * Provide indication if Update is Pending or not.
     * <p/>
     * If Pending, no update will occur against the Database.
     */
    boolean performPreProcessing(Class<? extends RootElement> clazz);

}
