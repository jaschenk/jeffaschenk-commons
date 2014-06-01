package jeffaschenk.commons.system.internal.scheduling;

import jeffaschenk.commons.system.internal.file.services.ServiceTask;

import java.util.Date;

/**
 *
 *  Provides Interface for Local Scheduling Services
 *
 *
 * @author jeffaschenk@gmail.com
 *
 */
public interface LocalSchedulingService {

    /**
     * Schedule Runnable Wrapper Interface
     *
     * @param runnableTask
     * @param scheduledTask
     * @return boolean indicating if Task Scheduled or not.
     */
    boolean scheduleTask(ServiceTask runnableTask, Date scheduledTask);

    /**
     * Schedule Runnable Wrapper Interface
     * @return int providing  active Thread count.
     */
    int getActiveThreadPoolTasks();

    /**
     * Obtain our current schedule
     * @return String - HTML Current Schedule Status.
     */
    String getCurrentSchedule();

    /**
     * Obtain our current schedule
     *
     * @return String - HTML Current Schedule Status.
     */
    String getFullCurrentSchedule();

    /**
     * Provides ability to schedule an Action based upon a
     * cron Expression.
     *
     * @param actionBeanName
     * @param cronExpression
     * @return boolean indicating if Action Scheduled or not.
     */
    boolean scheduleAction(String actionBeanName, String cronExpression);

    /**
     * Provides ability to remove an Action based upon a
     * the name of the Action.
     *
     * @param actionBeanName
     * @return boolean indicating if Action has een removed.
     */
    boolean removeAction(String actionBeanName);

    /**
     * Resolve the CRON Key Class
     *
     * @param cronClassKey
     * @return String
     */
    String resolveCronClassKey(String cronClassKey);

}
