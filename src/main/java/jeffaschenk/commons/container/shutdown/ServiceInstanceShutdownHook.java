package jeffaschenk.commons.container.shutdown;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Shutdown Service Instance Hook.
 *
 * @author Jeff.A.Schenk@gmail.com
 */
public class ServiceInstanceShutdownHook extends Thread {

     /**
     * Logging
     */
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ServiceInstanceShutdownHook.class);

    /**
     * Reference to original Spring Application Context.
     */
    private AbstractApplicationContext applicationContext;

    /**
     * Default Constructor
     * @param applicationContext
     */
    public ServiceInstanceShutdownHook(AbstractApplicationContext applicationContext) {
        super();
        this.applicationContext = applicationContext;
         logger.info("Establishing Shutdown Hook.");
        this.establishJVMShutdownHook();
         logger.info("Established Shutdown Hook.");
    }

    /**
     * Establish Our Shutdown Hook for the JVM.
     */
    private void establishJVMShutdownHook() {

        this.applicationContext.registerShutdownHook();

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                this.setName("SHUTDOWN-THREAD");
                ServiceInstanceShutdownLogger.log(this.getClass(), "INFO", "Shutdown Thread caught JVM Interrupt.");
                try {
                    ServiceInstanceShutdownLogger.log(this.getClass(), "INFO", "Ordered Shutdown Commencing.");
                    // Wait for Container to Finish all PostDestroy and Destroy Processing
                    while((applicationContext != null) && (
                            (applicationContext.isRunning()) || (applicationContext.isActive()) ) ) { Thread.sleep(100); }
                   // Done.
                   ServiceInstanceShutdownLogger.log(this.getClass(), "INFO", "Done.");
                } catch (Exception e) {
                     ServiceInstanceShutdownLogger.log(this.getClass(), "ERROR", "Embedded Tomcat Life Cycle Exception:"+e.getMessage(),e);
                }
            }

        });

    }



}
