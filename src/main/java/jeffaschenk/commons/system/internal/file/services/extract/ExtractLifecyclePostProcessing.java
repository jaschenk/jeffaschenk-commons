package jeffaschenk.commons.system.internal.file.services.extract;


import jeffaschenk.commons.touchpoint.model.RootElement;

/**
 * Extract Processing Service Interface
 * <p/>
 * This Component will be accessed after all
   * Extract Processing has completed on the associated Class
   * and Table.
 */
public interface ExtractLifecyclePostProcessing {

    /**
     * Provide indication if Update is Pending or not.
     *
     * If Pending, no update will occur against the Database.
     *
     */
    boolean performPostProcessing(Class<? extends RootElement> clazz);

}
