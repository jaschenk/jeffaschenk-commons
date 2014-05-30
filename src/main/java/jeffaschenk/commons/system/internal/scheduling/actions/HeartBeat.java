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
 *Simple HeartBeat
 *
 * @author jeffaschenk@gmail.com
 */
@Component("heartBeat")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HeartBeat extends InternallyScheduledAction {
    private final static Logger logger = LoggerFactory.getLogger(HeartBeat.class);

    /**
     * Perform the Actual CleanUp Action Task
     */
    @Override
    public void handleAlarm(AlarmEntry alarmEntry) {
        logger.warn("** HEARTBEAT **"+alarmEntry.toString());
    }

}
