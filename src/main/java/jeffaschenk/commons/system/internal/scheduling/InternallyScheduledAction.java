package jeffaschenk.commons.system.internal.scheduling;

import fr.dyade.jdring.AlarmListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Abstract Class for various Internally Scheduled Actions, triggered at certain
 * intervals within a specified CRON type of configuration.
 *
 * @author jeffaschenk@gmail.com
 */
@Component("alarmListener")
public abstract class InternallyScheduledAction implements AlarmListener {


    /**
     * Injected Common System Environment Property to check to see if this facility
     * is enabled or not.
     */
    @Value("#{systemEnvironmentProperties['internal.scheduling.tasks.enabled']}")
    private boolean serviceEnabled;
    
    /**
     * Verifies Service is Enabled or Not.
     *
     * @return boolean indicator
     */
    public boolean isServiceEnabled() {
        return serviceEnabled;
    }

    /**
     * setter for {@link #serviceEnabled}
     * @param serviceEnabled
     */
    public void setServiceEnabled(boolean serviceEnabled) {
		this.serviceEnabled = serviceEnabled;
	}

}
