package jeffaschenk.commons.system.internal.scheduling.actions;

import fr.dyade.jdring.AlarmEntry;
import jeffaschenk.commons.system.internal.scheduling.InternallyScheduledAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * *
 * Implementation of a Scheduled Actions:
 * <p/>
 * Schedule Necessary Clean Up Activities:
 * <p/>
 *
 * @author jeffaschenk@gmail.com
 */
@Component("CleanUp")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CleanUp extends InternallyScheduledAction {
    private final static Logger logger = LoggerFactory.getLogger(CleanUp.class);

    /**
     * Perform the Actual CleanUp Action Task
     */
    @Override
    public void handleAlarm(AlarmEntry alarmEntry) {
        if (this.isServiceEnabled()) {
            logger.warn("Starting Scheduled Clean Up Task.");

            // TODO DO SOME WORK...

            logger.warn("Completed Scheduled Clean Up Task.");
        } else {
           logger.warn("Alarm Service not Enabled, Ignoring Request!");
        }
    }

}
