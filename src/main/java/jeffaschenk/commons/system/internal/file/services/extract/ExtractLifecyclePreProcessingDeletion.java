package jeffaschenk.commons.system.internal.file.services.extract;


import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.touchpoint.model.dao.SystemDAO;
import jeffaschenk.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Extract Pre-Processing Service Implementation
 * <p/>
 * This Component will be accessed prior to any
 * Extract Processing starting on the associated Class
 * and Table.
 *
 * @author jeffaschenk@gmail.com
 */
@Component("extractLifecyclePreProcessingDeletionComponent")
public class ExtractLifecyclePreProcessingDeletion implements GlobalConstants, ExtractLifecyclePreProcessing {

    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(ExtractLifecyclePreProcessingDeletion.class);

    @Autowired
    private SystemDAO systemDAO;

    @Value("#{systemEnvironmentProperties['extract.global.preprocessing.delete']}")
    private String preProcessingContentDeletionStringValue;
    private boolean preProcessingContentDeletion;

    /**
     * Initialize the Service Provider Interface
     */
    @PostConstruct
    public synchronized void initialize() {
        this.preProcessingContentDeletion = StringUtils.toBoolean(this.preProcessingContentDeletionStringValue, false);
        logger.info("Extract PreProcessing Deletion Service Provider Facility is Ready and Available.");
    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public synchronized void destroy() {
    }

    /**
     * Perform Pre-Processing for Specified Class.
     */
    @Override
    public boolean performPreProcessing(Class<? extends RootElement> clazz) {
        if (clazz == null) {
            return false;
        } else if ((clazz.getName().toLowerCase().contains("demographics")) ||
                (clazz.getName().toLowerCase().contains("web_payment_transaction")) ||
                (clazz.getName().toLowerCase().contains("system"))) {
            logger.warn("Ignoring Pre-Processing Clear:[" + clazz.getName() + "], as Table needs to Maintain Integrity of Life-Cycles!");
            return false;
        }
        // *****************************************
        // Remove All Entries for this Class/Table
        if (preProcessingContentDeletion) {
            Number objectsDeleted =
                    systemDAO.removeAllElementsForClass(clazz);
            logger.info("Cleared Class:[" + clazz.getName() + "], of Rows:[" + objectsDeleted + "]");
            return true;
        } else {
            logger.info("Global PreProcessing Contents Deletion Flag Setting did not allow Class:[" +
                    clazz.getName() + "] to be deleted.");
            return false;
        }
    }
}
