package jeffaschenk.commons.system.internal.file.services;

import jeffaschenk.commons.system.internal.file.services.extract.ExtractLifecyclePostProcessing;
import jeffaschenk.commons.system.internal.file.services.extract.ExtractLifecyclePreProcessing;
import jeffaschenk.commons.system.internal.file.services.extract.ExtractLifecycleUpdateDetermination;
import jeffaschenk.commons.touchpoint.model.wrappers.ExtractMappings;
import jeffaschenk.commons.touchpoint.model.wrappers.PostProcessingMappings;
import jeffaschenk.commons.touchpoint.model.wrappers.PreProcessingMappings;
import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.touchpoint.model.wrappers.UpdateProcessingMappings;
import jeffaschenk.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;


/**
 * Utilit Service Implementation
 *
 * @author jeffaschenk@gmail.com
 */
@Service("utilityService")
public class UtilityServiceImpl implements UtilityService, ApplicationContextAware {

    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(UtilityServiceImpl.class);

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;

    /**
     * Extract Mappings
     */
    @Autowired
    private ExtractMappings extractMappings;

    public ExtractMappings getExtractMappings() {
        return extractMappings;
    }

    public void setExtractMappings(ExtractMappings extractMappings) {
        this.extractMappings = extractMappings;
    }

    /**
     * Pre Processing Mappings
     */
    @Autowired
    private PreProcessingMappings preProcessingMappings;

    public PreProcessingMappings getPreProcessingMappings() {
        return preProcessingMappings;
    }

    public void setUpdateProcessingMappings(PreProcessingMappings preProcessingMappings) {
        this.preProcessingMappings = preProcessingMappings;
    }

    /**
     * Update Mappings
     */
    @Autowired
    private UpdateProcessingMappings updateProcessingMappings;

    public UpdateProcessingMappings getUpdateProcessingMappings() {
        return updateProcessingMappings;
    }

    public void setUpdateProcessingMappings(UpdateProcessingMappings updateProcessingMappings) {
        this.updateProcessingMappings = updateProcessingMappings;
    }

    /**
     * Post Processing Mappings
     */
    @Autowired
    private PostProcessingMappings postProcessingMappings;

    public PostProcessingMappings getPostProcessingMappings() {
        return postProcessingMappings;
    }

    public void setPostProcessingMappings(PostProcessingMappings postProcessingMappings) {
        this.postProcessingMappings = postProcessingMappings;
    }


    /**
     * Spring Application Context,
     * used to obtain access to Resources on
     * Classpath.
     */
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Initialize the Service Provider Interface
     */
    @PostConstruct
    public synchronized void initialize() {
        logger.info("Starting Utility Service Provider Facility.");

        // *************************************
        // Show Filter Mapping
        if ((this.extractMappings == null) || (this.extractMappings.getFilterMappings() == null) ||
                (this.extractMappings.getFilterMappings().size() <= 0)) {
            logger.error("Unable to initialize the Extract FileName to Object Class Mappings, Utility Service not Initialized!");
            return;
        }
        // **************************************
        // Log Runtime Mappings
        logger.info("Extract Filter Mappings: " + this.extractMappings.toString());


        // Service Ready and loaded for action.
        this.initialized = true;
    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public synchronized void destroy() {
        if (this.initialized) {
            logger.info("Ending Utility Service Provider Facility.");
        }
    }

    /**
     * Utility Service to lookup and provide associate Object Model Class
     * per specified Extract FileName.
     * <p/>
     * Yes, there is order in the world!
     *
     * @param extractFileName
     * @return Class<? extends RootElement>
     */
    @Override
    public Class<? extends RootElement> getClassBasedOnExtractFilename(final String extractFileName) {
        if (StringUtils.isEmpty(extractFileName)) {
            throw new IllegalArgumentException("Extract File Name must be specified.");
        }
        String className = this.extractMappings.getMappedClassName(extractFileName);
        if (className == null) {
            return null;
        }
        try {
            return RootElement.class.forName(className).asSubclass(RootElement.class);
        } catch (ClassNotFoundException cnfe) {
            logger.error("Class Name:[" + className + "], is not defined in the current Object model, Returning Null.");
        }
        return null;
    }

    /**
     * Utility Service to obtain default class.
     *
     * @return Class<? extends RootElement>
     */
    @Override
    public Class<? extends RootElement> getDefaultClassInstance() {
        try {
            return RootElement.class.forName(RootElement.class.getName()).asSubclass(RootElement.class);
        } catch (ClassNotFoundException cnfe) {
            logger.error("Class Name:[" + RootElement.class.getName() + "], is not defined in the current Object model, Returning Null.");
        }
        return null;
    }

    /**
     * Helper method to determine if Directory is valid.
     *
     * @param zoneDirectory
     * @return boolean inidicator - true if Zone directory is Valid.
     */
    @Override
    public boolean isZoneDirectoryValid(File zoneDirectory) {
        return ((zoneDirectory.exists()) &&
                (zoneDirectory.canRead()) &&
                (zoneDirectory.canWrite()) &&
                (zoneDirectory.isDirectory()));

    }

    /**
     * Utility Service to lookup and provide associated Update Component Bean
     * per specified Extract Entity Name.
     * <p/>
     * Yes, there is order in the world!
     *
     * @param extractEntityClassName
     * @return ExtractLifecycleUpdateDetermination Bean
     */
    @Override
    public ExtractLifecycleUpdateDetermination getBeanBasedOnExtractEntityClassNameForUpdateDetermination(final String extractEntityClassName) {
        if (StringUtils.isEmpty(extractEntityClassName)) {
            throw new IllegalArgumentException("Extract Entity ClassName Name must be specified.");
        }
        String beanName = this.updateProcessingMappings.getMappedClassName(extractEntityClassName);
        if (beanName == null) {
            return null;
        }
        return (ExtractLifecycleUpdateDetermination)
                this.applicationContext.getBean(beanName);

    }

    /**
     * Utility Service to lookup and provide associated Update  Module Class
     * per specified Extract Entity Name.
     * <p/>
     * Yes, there is order in the world!
     *
     * @param extractEntityClassName
     * @return ExtractLifecycleUpdateDetermination
     */
    @Override
    public ExtractLifecyclePreProcessing getBeanBasedOnExtractEntityClassNameForPreProcessing(String extractEntityClassName) {
        if (StringUtils.isEmpty(extractEntityClassName)) {
            throw new IllegalArgumentException("Extract Entity ClassName Name must be specified.");
        }
        String beanName = this.preProcessingMappings.getMappedClassName(extractEntityClassName);
        if (beanName == null) {
            return null;
        }
        return (ExtractLifecyclePreProcessing)
                this.applicationContext.getBean(beanName);
    }

    /**
     * Utility Service to lookup and provide associated Update  Module Class
     * per specified Extract Entity Name.
     * <p/>
     * Yes, there is order in the world!
     *
     * @param extractEntityClassName
     * @return ExtractLifecycleUpdateDetermination
     */
    @Override
    public ExtractLifecyclePostProcessing getBeanBasedOnExtractEntityClassNameForPostProcessing(String extractEntityClassName) {
        if (StringUtils.isEmpty(extractEntityClassName)) {
            throw new IllegalArgumentException("Extract Entity ClassName Name must be specified.");
        }
        String beanName = this.postProcessingMappings.getMappedClassName(extractEntityClassName);
        if (beanName == null) {
            return null;
        }
        return (ExtractLifecyclePostProcessing)
                this.applicationContext.getBean(beanName);
    }


}
