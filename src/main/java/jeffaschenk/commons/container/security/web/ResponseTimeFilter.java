package jeffaschenk.commons.container.security.web;

import jeffaschenk.commons.aop.TraceDurationAspect;
import jeffaschenk.commons.util.TimeDuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Response Time Filter
 * <p/>
 * As used from the Reference:
 * http://www.java2s.com/Tutorial/Java/0400__Servlet/Simplefilterformeasuringservletresponsetimes.htm
 */
public class ResponseTimeFilter implements Filter {

    protected FilterConfig config;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        TimeDuration td = new TimeDuration();
        td.start();
        chain.doFilter(request, response);
        td.stop();
        String name = "servlet";
        if (request instanceof HttpServletRequest) {
            name = ((HttpServletRequest) request).getRequestURI();
        }
        if (TraceDurationAspect.log.isTraceEnabled()) {
            TraceDurationAspect.log.trace("[" + Thread.currentThread().getName() + "]    URI:[" + name + "], Duration:[" + td.getElapsedtoString() + "]");
        }
    }
}
