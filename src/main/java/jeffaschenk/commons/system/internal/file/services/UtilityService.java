package jeffaschenk.commons.system.internal.file.services;

import jeffaschenk.commons.system.internal.file.services.extract.ExtractLifecyclePostProcessing;
import jeffaschenk.commons.system.internal.file.services.extract.ExtractLifecyclePreProcessing;
import jeffaschenk.commons.system.internal.file.services.extract.ExtractLifecycleUpdateDetermination;
import jeffaschenk.commons.touchpoint.model.RootElement;

import java.io.File;

/**
 * Utility Service Interface
 * <p/>
 * Provides Common utility Services to Application Framework.
 *
 * @author jeffaschenk@gmail.com
 *
 */
public interface UtilityService {

    /**
     * Obtain the Class for the specified Extract File Name.
     *
     * @param extractFileName
     * @return Class<? extends RootElement>
     */
    public Class<? extends RootElement> getClassBasedOnExtractFilename(final String extractFileName);

    /**
     * Obtain the Default Class
     *
     * @return Class<? extends RootElement>
     */
    public Class<? extends RootElement> getDefaultClassInstance();

    /**
     * Provides Indication if File Zone Directory is valid or not.
     *
     * @param zoneDirectory
     * @return boolean
     */
    public boolean isZoneDirectoryValid(File zoneDirectory);

    /**
     * Utility Service to lookup and provide associated Update  Module Class
     * per specified Extract Entity Name.
     * <p/>
     * Yes, there is order in the world!
     *
     * @param extractEntityClassName
     * @return ExtractLifecycleUpdateDetermination
     */
    public ExtractLifecyclePreProcessing getBeanBasedOnExtractEntityClassNameForPreProcessing(final String extractEntityClassName);

    /**
     * Utility Service to lookup and provide associated Update  Module Class
     * per specified Extract Entity Name.
     * <p/>
     * Yes, there is order in the world!
     *
     * @param extractEntityClassName
     * @return ExtractLifecycleUpdateDetermination
     */
    public ExtractLifecycleUpdateDetermination getBeanBasedOnExtractEntityClassNameForUpdateDetermination(final String extractEntityClassName);

    /**
     * Utility Service to lookup and provide associated Update  Module Class
     * per specified Extract Entity Name.
     * <p/>
     * Yes, there is order in the world!
     *
     * @param extractEntityClassName
     * @return ExtractLifecycleUpdateDetermination
     */
    public ExtractLifecyclePostProcessing getBeanBasedOnExtractEntityClassNameForPostProcessing(final String extractEntityClassName);

}
