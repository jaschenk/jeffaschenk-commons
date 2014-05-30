package jeffaschenk.commons.system.internal.scheduling;

/**
 *
 *  Provides Interface for Local Scheduling Services
 *
 *
 * @author jeffaschenk@gmail.com
 *
 */
public interface LocalSchedulingService {

    boolean scheduleAction(String actionBeanName, String cronExpression);

}
