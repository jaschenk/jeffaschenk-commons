package jeffaschenk.commons.container.event;

import org.springframework.context.ApplicationEvent;

/**
 * Simple Shutdown Event
 */
public class ShutdownEvent extends ApplicationEvent {


    public ShutdownEvent(Object source) {
        super(source);
    }

}
