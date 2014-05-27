package jeffaschenk.commons.container.shutdown;

import jeffaschenk.commons.util.DateUtils;

import java.util.Date;

/**
 * Common Shutdown Logger.
 *
 * @author jeff.a.schenk@gmail.com
 */
public class ServiceInstanceShutdownLogger {

    public static void log(Class clazz, String severity, Object message) {

        System.err.println( DateUtils.toString(new Date(),DateUtils.miltimeOnlyFormatter)
                          +" "
                          +clazz.getName()
                          +": "
                          +severity.toUpperCase()
                          +" "+message);
    }

     public static void log(Class clazz, String severity, Object message, Throwable throwable) {

           System.err.println( DateUtils.toString(new Date(),DateUtils.miltimeOnlyFormatter)
                   +" "
                   +clazz.getName()
                   +": "
                   +severity.toUpperCase()
                   +" "+message
                   +" "+throwable.getMessage());
    }


}
