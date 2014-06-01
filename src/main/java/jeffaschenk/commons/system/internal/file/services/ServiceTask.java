package jeffaschenk.commons.system.internal.file.services;

import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Service Definition for all Service Tasks.
 *
 * @author jeffaschenk@gmail.com
 */
public abstract class ServiceTask implements Runnable {

    /**
     * Logging
     */
    protected final static Logger logger = LoggerFactory.getLogger(ServiceTask.class);

    private LifeCycleServiceType lifeCycleServiceType;

    public LifeCycleServiceType getLifeCycleServiceType() {
        return lifeCycleServiceType;
    }

    public void setLifeCycleServiceType(LifeCycleServiceType lifeCycleServiceType) {
        this.lifeCycleServiceType = lifeCycleServiceType;
    }

}
