package jeffaschenk.commons.system.internal.scheduling.events;

import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * LifeCycle Service Event
 *
 * This Event is published by the various Services which
 * will provide the main Scheduler LifeCycle information when
 * various Tasks have started and completed.
 *
 * @author jeffaschenk@gmail.com
 */
public class LifeCycleServicesEvent extends ApplicationEvent {

    private LifeCycleServiceType lifeCycleServiceType;

    private LifeCycleServiceStateType lifeCycleServiceStateType;

    private long timestamp_when_event_published;
    
    private String message;
    
    private List<Object> payload = new ArrayList<Object>();


    /**
     * Default Constructor for building Life Cycle Service Event.
     * @param source
     * @param lifeCycleServiceType
     * @param lifeCycleServiceStateType
     */
    public LifeCycleServicesEvent(Object source, LifeCycleServiceType lifeCycleServiceType,
                                  LifeCycleServiceStateType lifeCycleServiceStateType, long timestamp) {
        super(source);
        this.lifeCycleServiceType = lifeCycleServiceType;
        this.lifeCycleServiceStateType = lifeCycleServiceStateType;
        this.timestamp_when_event_published = timestamp;
    }

    /**
     * Default Constructor for building Life Cycle Service Event.
     * @param source
     * @param lifeCycleServiceType
     * @param lifeCycleServiceStateType
     * @param message
     */
    public LifeCycleServicesEvent(Object source, LifeCycleServiceType lifeCycleServiceType,
                                  LifeCycleServiceStateType lifeCycleServiceStateType, 
                                  long timestamp, String message) {
        super(source);
        this.lifeCycleServiceType = lifeCycleServiceType;
        this.lifeCycleServiceStateType = lifeCycleServiceStateType;
        this.timestamp_when_event_published = timestamp;
        this.message = message;
    }

    public LifeCycleServiceType getLifeCycleServiceType() {
        return lifeCycleServiceType;
    }

    public void setLifeCycleServiceType(LifeCycleServiceType lifeCycleServiceType) {
        this.lifeCycleServiceType = lifeCycleServiceType;
    }

    public LifeCycleServiceStateType getLifeCycleServiceStateType() {
        return lifeCycleServiceStateType;
    }

    public void setLifeCycleServiceStateType(LifeCycleServiceStateType lifeCycleServiceStateType) {
        this.lifeCycleServiceStateType = lifeCycleServiceStateType;
    }

    public long getTimestamp_when_event_published() {
        return timestamp_when_event_published;
    }

    public void setTimestamp_when_event_published(long timestamp_when_event_published) {
        this.timestamp_when_event_published = timestamp_when_event_published;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getPayload() {
        return payload;
    }

    public void setPayload(List<Object> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "LifeCycleServicesEvent{" +
                "lifeCycleServiceType=" + lifeCycleServiceType +
                ", lifeCycleServiceStateType=" + lifeCycleServiceStateType +
                ", timestamp_when_event_published=" + timestamp_when_event_published +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LifeCycleServicesEvent that = (LifeCycleServicesEvent) o;

        if (timestamp_when_event_published != that.timestamp_when_event_published) return false;
        if (lifeCycleServiceStateType != that.lifeCycleServiceStateType) return false;
        if (lifeCycleServiceType != that.lifeCycleServiceType) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (payload != null ? !payload.equals(that.payload) : that.payload != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lifeCycleServiceType != null ? lifeCycleServiceType.hashCode() : 0;
        result = 31 * result + (lifeCycleServiceStateType != null ? lifeCycleServiceStateType.hashCode() : 0);
        result = 31 * result + (int) (timestamp_when_event_published ^ (timestamp_when_event_published >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }
}
