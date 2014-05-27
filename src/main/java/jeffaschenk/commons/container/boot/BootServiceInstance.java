package jeffaschenk.commons.container.boot;

import jeffaschenk.commons.environment.SetUpSpringContainerBootStrap;
import jeffaschenk.commons.container.shutdown.ServiceInstanceShutdownHook;

/**
 * Boot Spring Container Service Instance
 *
 * @author Jeff.A.Schenk@gmail.com
 */
public class BootServiceInstance {

    public static void main(String[] args) throws Exception {

                 new ServiceInstanceShutdownHook(SetUpSpringContainerBootStrap.init());

       }

}
