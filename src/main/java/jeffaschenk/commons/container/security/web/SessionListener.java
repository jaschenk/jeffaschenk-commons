/**
 * Qnow
 */
package jeffaschenk.commons.container.security.web;

import jeffaschenk.commons.util.TimeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * Session Listener
 *
 * @author Jeff.Schenk
 * @version $Id: $
 */
public class SessionListener implements HttpSessionListener {

    // ***************************************
    // Logging
    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(SessionListener.class);

    /**
     * {@inheritDoc}
     * <p/>
     * Session Creation
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("Session created for Id:[" + httpSessionEvent.getSession().getId() + "], Created:[" + TimeUtils.getDate(httpSessionEvent.getSession().getCreationTime()) + "]");
        while (httpSessionEvent.getSession().getAttributeNames().hasMoreElements()) {
            String attributeName =
                    httpSessionEvent.getSession().getAttributeNames().nextElement().toString();
            Object attributeValue = httpSessionEvent.getSession().getAttribute(attributeName);
            log.info("Session Attribute Name:[" + attributeName + "], Attribute Value:[" + attributeValue.toString() + "]");
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Session Destroyed
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        log.info("Session destroyed for Id:[" + httpSessionEvent.getSession().getId() + "], Last Access Time:[" + TimeUtils.getDate(httpSessionEvent.getSession().getLastAccessedTime()) + "]");
    }

}
