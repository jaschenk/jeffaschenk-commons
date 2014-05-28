package jeffaschenk.commons.container.security.web;

import jeffaschenk.commons.container.security.AuthenticationException;
import jeffaschenk.commons.container.security.constants.SecurityConstants;
import jeffaschenk.commons.container.security.monitor.SecurityServiceMonitor;
import jeffaschenk.commons.container.security.object.AuthenticationToken;
import jeffaschenk.commons.container.security.object.SecuritySessionUserObject;
import jeffaschenk.commons.util.StringUtils;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

/**
 * AuthenticationFilter Implemented authenticated Session
 * Authentication Filter.
 * <p/>
 * Spring Container application Context Aware to initialize and save Security
 * Session information for all Authenticated elements.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter implements SecurityConstants {

    /**
     * Logging Constant <code>log</code>
     */
    protected static Log log = LogFactory
            .getLog(AuthenticationFilter.class);

    /**
     * Injected Service Provider AppId
     */
    @Value("#{systemEnvironmentProperties['serviceProvider.appId']}")
    private String serviceProviderAppId;

    /**
     * Injected Service Provider Secret
     */
    @Value("#{systemEnvironmentProperties['serviceProvider.secret']}")
    private String serviceProviderSecret;

    /**
     * Injected Service Provider Graph Api Url
     */
    @Value("#{systemEnvironmentProperties['serviceProvider.graph.apiUrl']}")
    private String serviceProviderGraphApiUrl;

    /**
     * Injected Service Provider Authorize Url
     */
    @Value("#{systemEnvironmentProperties['serviceProvider.authorizeUrl']}")
    private String serviceProviderAuthorizeUrl;

    /**
     * Injected Service Provider Authorize Url
     */
    @Value("#{systemEnvironmentProperties['serviceProvider.accessTokenUrl']}")
    private String serviceProviderAccessTokenUrl;

    /**
     * Injected Service Provider Redirect Url
     */
    @Value("#{systemEnvironmentProperties['serviceProvider.redirectUrl']}")
    private String serviceProviderRedirectUrl;

    /**
     * Injected Service Provider Required Extended Permissions
     */
    @Value("#{systemEnvironmentProperties['serviceProvider.extended.permissions']}")
    private String serviceProviderExtendedPermissions;

    @Value("#{systemEnvironmentProperties['serviceProvider.permission.dialog.form.default']}")
    private String serviceProviderPermissionDialogFormDefault;

    @Value("#{systemEnvironmentProperties['host']}")
    private String host;

    @Value("#{systemEnvironmentProperties['serviceProvider.deauthorization.callback']}")
    private String serviceProviderDeAuthorizationCallback;

    /**
     * Header Names
     */
    private String usernameHeader = USERNAME_HeaderPropertyName;
    private String passwordHeader = PASSWORD_HeaderPropertyName;

    /**
     * Default Constructor
     */
    public AuthenticationFilter() {
        super(SPRING_SECURITY_CHECK_URL_HOOK);
    }

    /**
     * Security Service Monitor
     */
    private SecurityServiceMonitor securityServiceMonitor;

    public void setSecurityServiceMonitor(SecurityServiceMonitor securityServiceMonitor) {
        this.securityServiceMonitor = securityServiceMonitor;
    }

    /**
     * Autowired Services
     */
    //@Autowired
    //private FacebookServiceProvider facebookServiceProvider;

    /**
     * Initialize the Singleton Session Cache.
     */
    @PostConstruct
    public synchronized void init() {
        // ************************************
        // Initialization
        log.info("AuthenticationFilter starting to Initialize.");
        // *********************************************
        // Create a blank redirect strategy
        // to prevent Spring automatically
        // returning page content in the output stream.
        SavedRequestAwareAuthenticationSuccessHandler srh = new SavedRequestAwareAuthenticationSuccessHandler();
        this.setAuthenticationSuccessHandler(srh);
        srh.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest httpservletrequest,
                                     HttpServletResponse httpservletresponse, String s) throws IOException {
                //do nothing, no redirect
            }
        });
        // ***************************************
        // Proceed with additional Initialization
        log.info("AuthenticationFilter has been Initialized");
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Destroy Implemented Spring Interface Method, invoked when bean is removed
     * from container.
     */
    @PreDestroy
    public void destroy() {
        log
                .info("AuthenticationFilter has been Removed from the Container.");
    }


    /**
     * Attempt authentication for Asynchronous Request.
     *
     * @param request
     * @param response
     * @return Authentication - Authentication Object
     * @throws org.springframework.security.core.AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws org.springframework.security.core.AuthenticationException {
        // ***************************
        // Obtain the Parameters for
        // the Login Attempt.
        String username = request.getParameter(usernameHeader);
        String password = request.getParameter(passwordHeader);
        // *********************************
        // Determine if the Authentication
        // can be allowed due to excessive
        // failed log-in attempts.
        //
        if (securityServiceMonitor.isLoginAttemptAllowed(username, request)) {
            securityServiceMonitor.monitorLogInAttemptRequest(username, request);

            log.info("Attempting Authentication for Principal:[" + username + "] for Request Context Path:[" + request.getContextPath() + "]");


            SecuritySessionUserObject securitySessionUserObject = new SecuritySessionUserObject(username);
            securitySessionUserObject.setPassword(password);
            AuthenticationToken authRequest =
                    new AuthenticationToken(username, securitySessionUserObject);


            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            throw new AuthenticationException("Unable to allow Authentication Attempt at this time due to excessive Log-In Attempts!");
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * successfulAuthentication
     * Provides Override for additional Successful Authentication Processing.
     */
    @Transactional(rollbackFor = {JSONException.class})
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, authResult);

        // ***************************************************
        // Now get the Session context, so we can initialize
        // and maintain a Security Service Object.
        if ((authResult != null)
                && (authResult.isAuthenticated())) {
            // *****************************************
            // Proceed with Request.
            log.info("Successful Authentication for Principal:[" + authResult.getPrincipal() + "], of Requested Resource:[" + request.getRequestURI() + "]");
            HttpSession session = request.getSession();
            session.setAttribute(SecuritySessionUserObject.SECURITY_SESSION_USER_OBJECT_NAME,
                    authResult.getDetails());
            if (((SecuritySessionUserObject) authResult.getDetails()).getSecuritySessionProfileObject() != null) {
                Locale locale =
                        LocaleUtils.toLocale(((SecuritySessionUserObject) authResult.getDetails()).getSecuritySessionProfileObject().getUserLocale());
                session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
            }
            // *********************************
            // Generate our Response.
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", true);
            } catch (JSONException je) {
                log.error("successfulAuthentication JSONException Encountered:[" + je.getMessage() + "]", je);
            }
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
            Writer out = responseWrapper.getWriter();
            out.write(jsonObject.toString());
            out.close();
            // **************************************************
            // Indicate Successful Log-in to the Service Monitor.
            securityServiceMonitor.monitorLogInAttemptRequest(authResult.getPrincipal(), request, true);
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * unsuccessfulAuthentication
     * Provides Override for unsuccessful Authentication Processing.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException failed) throws IOException, ServletException {

        // **************************************
        // Obtain the Original Parameters for
        // the Login Attempt.
        String username = request.getParameter(usernameHeader);
        // *********************************
        // Provide Failure Response
        log.info("Unsuccessful Authentication for Principal:[" + username + "], of Requested Resource:[" + request.getContextPath() + "]");
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
        Writer out = responseWrapper.getWriter();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("success", false);
            jsonObject.put("errors", failed.getMessage());
        } catch (JSONException je) {
            log.error("unsuccessfulAuthentication JSONException Encountered:[" + je.getMessage() + "]", je);
        }
        out.write(jsonObject.toString());
        out.close();
        // **************************************************
        // Indicate Successful Log-iin to the Service Monitor.
        securityServiceMonitor.monitorLogInAttemptRequest(username, request, false);
    }

    // ******************************************
    // Available Overrides.
    // ******************************************

    /**
     * Check for a signed_request Parameter from a Service Provider
     * and Decode the Request.
     *
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Performing doFilter Request:[" + ((HttpServletRequest) req).getRequestURI() + "]");
        }
        // *************************************************
        // Check for a Access_token from an
        // OAUTH Redirect for Authentication Web Flow.
        if (((HttpServletRequest) req).getRequestURI().endsWith(OAUTH_REDIRECT)) {
            // **********************************************************************************
            // Step 0, Initial OAUTH Redirect from our JavaScript Code.
            if (StringUtils.isEmpty(req.getParameter(CODE))) {
                this.reDirectToServiceProviderCode((HttpServletRequest) req, (HttpServletResponse) res);
                return;
            }
            // **********************************************************************************
            // Step 1 Receive Code and Send Request for Access Token, Sending our App Credentials
            String code = req.getParameter(CODE);
            // **********************************************************************************
            // Step 2 We must Exchange this Code for the Access Token, so perform the
            // rest call to the service provider.

        } // End of OAUTH Web Flow
        // ***********************************
        // Continue with Filter Chain.
        super.doFilter(req, res, chain);
    }

    /**
     * Redirect to Service Provider to Obtain authorized Token.
     *
     * @param reason   - Logging Information, regarding why the Redirect is issued.
     * @param request
     * @param response
     * @throws IOException
     */
    private void reDirectToServiceProviderPermissions(String reason, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Redirecting to Allow User to Permit Application rights, Reason:[" + reason + "]");

        // TODO : May need to determine the type of Display if using a Handheld Device or not.

        String redirectUri = request.getRequestURI();
        redirectUri = redirectUri.substring(request.getContextPath().length() + 1);

        String slash = (this.serviceProviderRedirectUrl.endsWith("/")) ? "" : "/";

        // ********************************************
        // Perform Redirect to Obtain Authorized Token
        // once the User has Authorized the Application.
        String url = this.serviceProviderAuthorizeUrl
                + "?" + "client_id=" + this.serviceProviderAppId
                + "&" + "redirect_uri=" + this.serviceProviderRedirectUrl + slash + redirectUri
                + "&" + "scope=" + this.serviceProviderExtendedPermissions
                + "&" + "display=" + this.serviceProviderPermissionDialogFormDefault;
        //response.sendRedirect(url);
        response.setContentType("text/html");
        response.getWriter().write("<html><body><script language=\"javascript\">window.open('" + url + "', '_parent', '')</script>" +
                "<a href=\"" + url + "\">Launch</a> Application.</body></html>");
    }

    /**
     * Construct Code Request
     */
    private void reDirectToServiceProviderCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // TODO : May need to determine the type of Display if using a Handheld Device or not.

        // ********************************************
        // Perform Redirect to Obtain Authorized Token
        // once the User has Authorized the Application.
        response.sendRedirect(this.serviceProviderAuthorizeUrl
                + "?" + "type=" + "web_server"
                + "&" + "client_id=" + this.serviceProviderAppId
                + "&" + "redirect_uri=" + this.host + "/" + OAUTH_REDIRECT
                + "&" + "scope=" + this.serviceProviderExtendedPermissions
                + "&" + "display=" + this.serviceProviderPermissionDialogFormDefault);
    }

    /**
     * Construct Access Token Request
     *
     * @param code -- OAUTH Initial Code Received to exchange for access token.
     */
    private String constructServiceProviderAccessTokenUrl(String code) {
        // ***************************************************
        // Construct Redirect URL to Obtain Authorized Token
        // once the User has Authorized the Application.
        // ** Very important the same URL must be used from
        // ** initial Request, otherwise service provider
        // ** will not validate request.
        //
        return this.serviceProviderAccessTokenUrl
                + "?" + "client_id=" + this.serviceProviderAppId
                + "&" + "redirect_uri=" + this.host + "/" + OAUTH_REDIRECT
                + "&" + "client_secret=" + this.serviceProviderSecret
                + "&" + "scope=" + this.serviceProviderExtendedPermissions
                + "&" + "code=" + code;
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        log.debug("Invoking getAuthenticationManager");
        return super.getAuthenticationManager();
    }

    @Override
    public void setAuthenticationManager
            (AuthenticationManager
                     authenticationManager) {
        log.debug("Invoking setAuthenticationManager");
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public String getFilterProcessesUrl
            () {
        log.debug("Invoking getFilterProcessesUrl");
        return super.getFilterProcessesUrl();
    }

    @Override
    public void setAuthenticationFailureHandler
            (AuthenticationFailureHandler
                     failureHandler) {
        log.debug("Invoking setAuthenticationFailureHandler");
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public void setAuthenticationSuccessHandler
            (AuthenticationSuccessHandler
                     successHandler) {
        log.debug("Invoking setAuthenticationSuccessHandler");
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public void setSessionAuthenticationStrategy
            (SessionAuthenticationStrategy
                     sessionStrategy) {
        log.debug("Invoking setSessionAuthenticationStrategy");
        super.setSessionAuthenticationStrategy(sessionStrategy);
    }

    @Override
    public void setAllowSessionCreation
            (
                    boolean allowSessionCreation) {
        log.debug("Invoking setAllowSessionCreation");
        super.setAllowSessionCreation(allowSessionCreation);
    }

    @Override
    protected boolean getAllowSessionCreation
            () {
        log.debug("Invoking getAllowSessionCreation");
        return super.getAllowSessionCreation();
    }

    @Override
    public AuthenticationDetailsSource getAuthenticationDetailsSource
            () {
        log.debug("Invoking getAuthenticationDetailsSource");
        return super.getAuthenticationDetailsSource();
    }

    @Override
    public void setMessageSource
            (MessageSource
                     messageSource) {
        log.debug("Invoking setMessageSource");
        super.setMessageSource(messageSource);
    }

    @Override
    public void setAuthenticationDetailsSource
            (AuthenticationDetailsSource
                     authenticationDetailsSource) {
        log.debug("Invoking setAuthenticationDetailsSource");
        super.setAuthenticationDetailsSource(authenticationDetailsSource);
    }

    @Override
    public void setApplicationEventPublisher
            (ApplicationEventPublisher
                     eventPublisher) {
        log.debug("Invoking setApplicationEventPublisher");
        super.setApplicationEventPublisher(eventPublisher);
    }

    @Override
    public void setContinueChainBeforeSuccessfulAuthentication
            (
                    boolean continueChainBeforeSuccessfulAuthentication) {
        log.debug("Invoking setContinueChainBeforeSuccessfulAuthentication");
        super.setContinueChainBeforeSuccessfulAuthentication(continueChainBeforeSuccessfulAuthentication);
    }

    @Override
    public void setRememberMeServices
            (RememberMeServices
                     rememberMeServices) {
        log.debug("Invoking setRememberMeServices");
        super.setRememberMeServices(rememberMeServices);
    }

    @Override
    public RememberMeServices getRememberMeServices
            () {
        log.debug("Invoking getRememberMeServices");
        return super.getRememberMeServices();
    }

    @Override
    public void setFilterProcessesUrl
            (String
                     filterProcessesUrl) {
        log.debug("Invoking setFilterProcessesUrl");
        super.setFilterProcessesUrl(filterProcessesUrl);
    }

    @Override
    public void afterPropertiesSet
            () {
        log.debug("Invoking afterPropertiesSet");
        super.afterPropertiesSet();
    }

    @Override
    protected boolean requiresAuthentication
            (HttpServletRequest
                     request, HttpServletResponse
                    response) {
        log.debug("Invoking requiresAuthentication");
        return super.requiresAuthentication(request, response);
    }

}
