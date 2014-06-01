package jeffaschenk.commons.system.internal.scheduling;

import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceStateType;
import jeffaschenk.commons.system.internal.scheduling.events.LifeCycleServiceType;

/**
 * LifeCycle Service State Object
 *
 * This simple POJO will provide State for distinct
 * Life Cycle Service Types.
 *
 * @author jeffaschenk@gmail.com
 */
public class LifeCycleServiceTaskState {

    private LifeCycleServiceType lifeCycleServiceType;

    private LifeCycleServiceStateType currentLifeCycleServiceState;

    private long started;

    private long done;

    /**
     * Default Constructor
     *
     * @param lifeCycleServiceType
     */
    public LifeCycleServiceTaskState(LifeCycleServiceType lifeCycleServiceType) {
        this.lifeCycleServiceType = lifeCycleServiceType;
    }

    public LifeCycleServiceType getLifeCycleServiceType() {
        return lifeCycleServiceType;
    }

    public LifeCycleServiceStateType getCurrentLifeCycleServiceState() {
        return currentLifeCycleServiceState;
    }

    public void setCurrentLifeCycleServiceState(LifeCycleServiceStateType currentLifeCycleServiceState) {
        this.currentLifeCycleServiceState = currentLifeCycleServiceState;
    }

    public long getStarted() {
        return started;
    }

    public void setStarted(long started) {
        this.started = started;
    }

    public long getDone() {
        return done;
    }

    public void setDone(long done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "LifeCycleServiceTaskState{" +
                "lifeCycleServiceType=" + lifeCycleServiceType +
                ", currentLifeCycleServiceState=" + currentLifeCycleServiceState +
                ", started=" + started +
                ", done=" + done +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LifeCycleServiceTaskState that = (LifeCycleServiceTaskState) o;

        if (lifeCycleServiceType != that.lifeCycleServiceType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lifeCycleServiceType != null ? lifeCycleServiceType.hashCode() : 0;
    }
}
