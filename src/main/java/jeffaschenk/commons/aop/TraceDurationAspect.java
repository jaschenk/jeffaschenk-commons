package jeffaschenk.commons.aop;

import jeffaschenk.commons.util.MemoryUsage;
import jeffaschenk.commons.util.TimeDuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect to provide Advice to methods for Performance Tuning.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
@Component
@Aspect
public class TraceDurationAspect {

    /**
     * Logging Constant <code>log</code>
     * Allow a public Logging to use this Same Logging Class
     * for Logging, namely the ResponseTimeFilter.
     */
    public static Log log = LogFactory.getLog(TraceDurationAspect.class);

    // **************************************
    // PointCuts
    // **************************************

    /**
     * Pointcut within Any Class and Method within the Persistence Layer, but
     * not including the TraceDurationAspect.
     */
    @Pointcut("( ( (execution(* jeffaschenk..*.*(..)))"
            + " || (execution(*.new(..))) "
            + " || (execution(* jeffaschenk..*.*(..)))"
            + ") && (!within(jeffaschenk.commons.aop.TraceDurationAspect)) )")
    public void inDataPersistenceLayer() {
    }

    /**
     * Advice for Named Pointcut for Around Method Profiling.
     *
     * @param pjp a {@link org.aspectj.lang.ProceedingJoinPoint} object.
     * @return  {@link java.lang.Object} object.
     * @throws java.lang.Throwable if any.
     */
    @Around("inDataPersistenceLayer()")
    public Object methodDurationProfiling(ProceedingJoinPoint pjp)
            throws Throwable {
        // ********************************
        // Start Time Duration Clock.
        TimeDuration td = new TimeDuration();
        td.start();

        // ********************************
        // Proceed with Method Operation.
        Object returnObjectValue;
        if (pjp.getArgs() != null) {
            returnObjectValue = pjp.proceed(pjp.getArgs());
        } else {
            returnObjectValue = pjp.proceed();
        }

        // *********************************
        // Stop Time Duration Clock.
        td.stop();

        // *********************************
        // Log it if we are Tracing.
        if (log.isTraceEnabled()) {
            MemoryUsage mu = new MemoryUsage();
            log.trace("[" + Thread.currentThread().getName() + "]" + " Method:["
                    + pjp.getSignature().getDeclaringType().getSimpleName()
                    + "." + pjp.getSignature().getName() + "], Duration:["
                    + td.getElapsedtoString() + "], " + mu.getCurrentMemory());
        }
        // *********************************
        // return any Values.
        return returnObjectValue;
    }

}
