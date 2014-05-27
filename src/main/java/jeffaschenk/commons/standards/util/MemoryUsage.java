package jeffaschenk.commons.standards.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.NumberFormat;

/**
 * Provides Simple memory Usage Object for calculating during of task or other
 * invocations.
 *
 * @author jeff.schenk
 * @version $Id: $
 */
public class MemoryUsage {

    /**
     * Logging
     */
    private static Log log = LogFactory.getLog(MemoryUsage.class);

    /**
     * Time Duration
     */
    private TimeDuration timeDuration = new TimeDuration();

    /**
     * Memory Constants
     */
    private static final String[] sizenames = new String[]{"B", "KB", "MB",
            "GB", "TB", "PB"};

    /**
     * Start Memory in Bytes
     */
    private double start = 0;
    /**
     * End Memory in Bytes
     */
    private double end = 0;
    /**
     * Start Free Memory in Bytes
     */
    private double start_free = 0;
    /**
     * End Free Memory in Bytes
     */
    private double end_free = 0;

    /**
     * Provides Default Constructor.
     */
    public MemoryUsage() {
    } // End of Constructor.

    /**
     * <p>getCurrentMemory</p>
     *
     * @return a {@link String} object.
     */
    public String getCurrentMemory() {
        double current_memory = Runtime.getRuntime().totalMemory();
        double current_free_memory = Runtime.getRuntime().freeMemory();
        return "JVM Total Memory:[" + getMemoryBytesToString(current_memory) + "], Free Memory:[" + getMemoryBytesToString(current_free_memory) + "]";
    }

    /**
     * Set Current Time as Start Time.
     */
    public void start() {
        timeDuration.start();
        start = Runtime.getRuntime().totalMemory();
        start_free = Runtime.getRuntime().freeMemory();
        end = start;
        end_free = start_free;
    } // End of start Method.

    /**
     * Set Current Time as End Time.
     */
    public void stop() {
        timeDuration.stop();
        end = Runtime.getRuntime().totalMemory();
        end_free = Runtime.getRuntime().freeMemory();
    } // End of stop Method.

    /**
     * Reset all Counters.
     */
    public void reset() {
        start = 0;
        end = 0;
        start_free = 0;
        end_free = 0;
        timeDuration.reset();
    } // End of Reset Method.

    /**
     * Get current Duration.
     *
     * @return a double.
     */
    public double getCurrentMemoryDifference() {
        timeDuration.stop();
        stop();
        return (end - start);
    } // End of getCurrentDuration Method.

    /**
     * Obtain data in String form.
     *
     * @return String representation of Memory Usage.
     */
    public String toString() {
        timeDuration.stop();
        stop();
        return "Duration:[" + timeDuration.toString() + "], JVM Start Total Memory:[" + getMemoryBytesToString(start) + "], Start Free Memory:[" + getMemoryBytesToString(start_free) + "], "
                + "Current Total Memory:[" + getMemoryBytesToString(end) + "], Current Free Memory:[" + getMemoryBytesToString(end_free) + "], "
                + "Difference Total Memory:[" + getMemoryBytesToString(end - start) + "]" + getMemoryMovement(end - start)
                + ", Difference Free Memory:[" + getMemoryBytesToString(end_free - start_free) + "]" + getMemoryMovement(end_free - start_free);
    } // End of toString Override method.

    /**
     * Log the Memory Consumption
     */
    public void toLog() {
        timeDuration.stop();
        stop();
        if (log.isInfoEnabled()) {
            log.info(
                    "Duration:[" + timeDuration.toString() +
                            "], JVM Start Total Memory:[" + getMemoryBytesToString(start) + "], Start Free Memory:[" + getMemoryBytesToString(start_free) + "]");
            log.info(
                    "JVM Current Total Memory:[" + getMemoryBytesToString(end) + "], Current Free Memory:[" + getMemoryBytesToString(end_free) + "]");
            log.info(
                    "JVM Difference Total Memory:[" + getMemoryBytesToString(end - start) + "]" + getMemoryMovement(end - start)
                            + ", Difference Free Memory:[" + getMemoryBytesToString(end_free - start_free) + "]" + getMemoryMovement(end_free - start_free));
        }
    } // End of toLog Override method.

    /**
     * Get Memory Usage in String Form.
     *
     * @param _memory double primitive.
     * @return String of Memory.
     */
    public String getMemoryBytesToString(final double _memory) {
        // ************************************
        // Set Decimal precision up to 2 digits
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        // ************************************
        // Check for Negative Value
        double bytes = _memory;
        if (bytes < 0) {
            bytes = bytes * -1;
        }
        // ************************************
        // Loop to scale number to nearest
        // xByte Level.
        for (int i = 0; i < sizenames.length; i++) {
            if ((bytes < 1024)) {
                return nf.format(bytes).toString()
                        + sizenames[i];
            }
            bytes = bytes / 1024;
        } // End of For Loop.
        // ***********************************
        // Show Bytes, if all else fails.
        return _memory + sizenames[0];
    } // End of getMemoryBytesToString Helper Method.

    /**
     * Show Memory Movement
     *
     * @return
     */
    private String getMemoryMovement(double _memory) {
        if ((_memory) == 0) {
            return "Same";
        } else if ((_memory < 0)) {
            return "Decrease";
        } else {
            return "Increase";
        }
    }

    /**
     * Main
     * Test Memory conversions.
     *
     * @param argsv an array of {@link String} objects.
     */
    public static void main(String argsv[]) {
        MemoryUsage mu = new MemoryUsage();
        mu.start();

        System.out.println(mu.getMemoryBytesToString(0));
        System.out.println(mu.getMemoryBytesToString(-128));
        System.out.println(mu.getMemoryBytesToString(700));
        System.out.println(mu.getMemoryBytesToString(1024));
        System.out.println(mu.getMemoryBytesToString(2000));
        System.out.println(mu.getMemoryBytesToString(1024 * 2));
        System.out.println(mu.getMemoryBytesToString(1024 * 3));
        System.out.println(mu.getMemoryBytesToString(5000));
        System.out.println(mu.getMemoryBytesToString(1024 * 100));
        System.out.println(mu.getMemoryBytesToString(1024 * 1000));

        System.out.println(" ");

        System.out.println(mu.getMemoryBytesToString(1024 * 1024));
        System.out.println(mu.getMemoryBytesToString(1024 * 1024 * 1024));
        System.out.println(mu.getMemoryBytesToString(Math.pow(1024, 4)));
        System.out.println(mu.getMemoryBytesToString(Math.pow(1024, 5)));
        System.out.println(mu.getMemoryBytesToString(Math.pow(1024, 5) * 2));
        System.out.println(mu.getMemoryBytesToString(Math.pow(1024, 6) * 2));


        System.out.println(mu.toString());
        Runtime.getRuntime().gc();
        System.out.println("After GC: \n");
        System.out.println(mu.toString());
    } // End of Main.
} // /:>~ End of MemoryUsage Class.
