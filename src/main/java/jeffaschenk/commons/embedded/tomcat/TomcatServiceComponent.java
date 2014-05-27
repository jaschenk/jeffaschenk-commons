package jeffaschenk.commons.embedded.tomcat;

import jeffaschenk.commons.container.shutdown.ServiceInstanceShutdownLogger;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;


/**
 * Embed Tomcat as our WEB Interface Layer.
 */
@Service("tomcat")
public class TomcatServiceComponent implements ApplicationContextAware {
    /**
     * Logging
     */
    private final static Logger logger = LoggerFactory.getLogger(TomcatServiceComponent.class);


    /**
     * Our Tomcat Instance Object.
     */
    private Tomcat tomcat;

    /**
     * Initial Startup Parameters
     */
    private String appBase;
    private Integer port;

    /**
     * Initialization Indicator.
     */
    private boolean initialized = false;


    /**
     * Task Executor
     */
    @Autowired
    TaskExecutor taskExecutor;

    /**
     * Default Constructor
     */
    public TomcatServiceComponent() {
        this.appBase = System.getProperty("user.home") + "/touchpoint" + "/app";
        this.port = 8080;
    }

    /**
     * Default Constructor
     *
     * @param appBase
     * @param port
     */
    public TomcatServiceComponent(String appBase, Integer port) {
        this.appBase = appBase;
        this.port = port;
    }

    /**
     * Spring Application Context,
     * used to obtain access to Resources on
     * Classpath.
     */
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Initialize the MemCached Service Provider Interface
     */
    @PostConstruct
    public synchronized void initialize() {

        logger.info("Starting Background Thread for Embedded Tomcat Service Facility");
        taskExecutor.execute(new startTomcatTask());
        logger.info("Completed Background Embedded Tomcat Service Facility Thread Initialization.");

    }

    /**
     * Destroy Service
     * Invoked during Termination of the Spring Container.
     */
    @PreDestroy
    public synchronized void destroy() {
        ServiceInstanceShutdownLogger.log(this.getClass(), "INFO", "Stopping Embedded Tomcat Service Facility.");
        try {
          this.tomcat.stop();
          this.tomcat.destroy();
        } catch(LifecycleException lifecycleException) {
            ServiceInstanceShutdownLogger.log(this.getClass(), "ERROR", "Embedded Tomcat Life Cycle Exception:"+lifecycleException,lifecycleException);
        }
    }

    // ************************************************
    // Private Inner Classes
    // ************************************************

    private class startTomcatTask extends Thread {


        /**
         * Background Task Constructor with All necessary parameters.
         */
        public startTomcatTask() {
        }

        @Override
        public void run() {
            try {
                tomcat = new Tomcat();
                tomcat.setPort(port);

                tomcat.setBaseDir(".");

                tomcat.getHost().setAppBase(appBase);
                String contextPath = "/";



                tomcat.setBaseDir(".");

                //Context ctx = tomcat.addWebapp("/", System.getProperty("user.dir") + "/jeffaschenk.commons.touchpoint/web/context.xml");
                tomcat.setHostname("localhost");

                //File contextFile = new File(System.getProperty("user.dir") + "/build/web/META-INF/context.xml");
                //ctx.setConfigFile(contextFile.toURI().toURL());

                tomcat.enableNaming();



                // Add AprLifecycleListener
                StandardServer server = (StandardServer) tomcat.getServer();
                AprLifecycleListener listener = new AprLifecycleListener();
                server.addLifecycleListener(listener);

                tomcat.addWebapp(contextPath, appBase);
                tomcat.start();

                // Blocking Call
                tomcat.getServer().await();

            } catch (ServletException se) {
                logger.error("Servlet Exception Initializing the Tomcat ContextPath with AppBase:[" + appBase + "]", se);
            } catch (Exception ce) {
                logger.error("Exception during thread running for Embedded Tomcat Execution " + ce.getMessage(), ce);
            } finally {
              ServiceInstanceShutdownLogger.log(this.getClass(), "WARN", "Completed Background Thread for Embedded Tomcat Execution.");
            }

        }
    }


}
