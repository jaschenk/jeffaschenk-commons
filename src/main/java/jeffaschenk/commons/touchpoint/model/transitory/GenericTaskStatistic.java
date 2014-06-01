package jeffaschenk.commons.touchpoint.model.transitory;

import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.system.internal.file.services.ServiceTask;

/**
 * Simple Transitory Task Object, which represent a Generic
 * Task.
 *
 * @author jeffaschenk@gmail.com
 */
public class GenericTaskStatistic extends TaskStatistic implements GlobalConstants {

    private ServiceTask serviceTask;


    /**
     * Default Constructor
     *
     * @param serviceTask
     *
     */
    public GenericTaskStatistic(ServiceTask serviceTask) {
        this.serviceTask = serviceTask;
    }

    public ServiceTask getServiceTask() {
        return serviceTask;
    }

    public void setServiceTask(ServiceTask serviceTask) {
        this.serviceTask = serviceTask;
    }
}
