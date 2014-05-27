package jeffaschenk.commons.container.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Component;

/**
 * Container Event Listener
 * <p/>
 * Listens for the Login Success.
 *
 * @author jeffaschenk@gmail.com
 */
@Component("authenticationEventListener")
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Authentication Event:[" + event.toString() + "]");
        }
    }


}

