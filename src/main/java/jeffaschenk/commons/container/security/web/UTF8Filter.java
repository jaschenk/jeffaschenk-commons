package jeffaschenk.commons.container.security.web;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * Ensure Response and Requests are always in UTF-8 Encoding.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class UTF8Filter implements Filter, SecurityConstants {

    /**
     * Logging Constant <code>log</code>
     */
    private static Log log = LogFactory
            .getLog(UTF8Filter.class);

    /**
     * Default Constructor
     */
    public UTF8Filter() {
    }

    /**
     * Initialize the Singleton Session Cache.
     */
    @Override
    public synchronized void init(FilterConfig filterConfig) throws ServletException {
        // ************************************
        // Initialization
        log.info("UTF-8 Request and Response Encoding Filter, Initialized.");
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Destroy Implemented Spring Interface Method, invoked when bean is removed
     * from container.
     */
    @Override
    public void destroy() {
        log
                .info("UTF-8 Request and Response Encoding Filter has been Removed from the Container.");
    }

    /**
     * Check for a signed_request Parameter from a Service Provider
     * and Decode the Request.
     *
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding(UTF8);
        request.setCharacterEncoding(UTF8);
        chain.doFilter(request, response);
    }

}
