package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;


/**
 * Java Class for accumulating Status and Statistics among various
 * JNDI Functions and Class.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxLapTime {

    private long start = 0;    // Start Time in Milliseconds.
    private long end = 0;    // End Time in Milliseconds.
    private long duration = 0;    // Duration Time in Milliseconds.

    private long laps = 0;    // Number of Laps.
    private long average = 0;    // Avg Duration Time in Milliseconds.
    private long minimum = 0;   // Min Duration Time in Milliseconds.
    private long maximum = 0;   // Max Duration Time in Milliseconds.
    private long total = 0;    // Tot Duration Time in Milliseconds.

    /**
     * Provides Common Constructor for Calculating Elapsed Time.
     * Does nothing.
     */
    public idxLapTime() {
    } // End of idxStatus Constructor.

    /**
     * Set Current Time as Start Time.
     */
    public void Start() {
        start = System.currentTimeMillis();
        end = start;
        duration = 0;
        laps++;
    } // End of Start Method.

    /**
     * Set Current Time as End Time.
     */
    public void Stop() {
        end = System.currentTimeMillis();
        duration = (end - start);
        total = total + duration;
        if ((duration < minimum) ||
                (laps <= 1)) {
            minimum = duration;
        }
        if ((duration > maximum) ||
                (laps <= 1)) {
            maximum = duration;
        }
        average = total / laps;
    } // End of Stop Method.

    /**
     * Reset all Counters.
     */
    public void Reset() {
        start = 0;
        end = 0;
        duration = 0;

        laps = 0;
        average = 0;
        minimum = 0;
        maximum = 0;
        total = 0;
    } // End of Reset Method.

    /**
     * Get current Duration.
     */
    public long getCurrentDuration() {
        return (duration);
    } // End of getCurrentDuration Method.

    /**
     * Obtain data in String form.
     *
     * @return String.
     */
    public String toString() {
        return ("Min: " + getElapsedtoString(minimum) + ", " +
                "Max: " + getElapsedtoString(maximum) + ", " +
                "Avg: " + getElapsedtoString(average) + " for " +
                laps + " Iterations.");
    }

    /**
     * Get Elapsed Timing in String Form.
     *
     * @return String of Duration.
     */
    public String getElapsedtoString() {
        return (getElapsedtoString(duration));
    } // End of getElaspedtoString with no Parameters.

    /**
     * Get Elapsed Timing in String Form.
     *
     * @param _duration A Duration, in milliseconds.
     * @return String of Duration.
     */
    public String getElapsedtoString(long _duration) {
        long hours;
        long minutes;
        long seconds;

        // **************************************
        // First Convert Duration into Seconds.
        long timeInSeconds = _duration / 1000;

        if (timeInSeconds <= 0) {
            return (_duration + " ms");
        }

        // *****************************
        // Now Convert the seconds.
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);

        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);

        seconds = timeInSeconds;

        if (hours > 0) {
            return (hours + " hour(s), " +
                    minutes + " minute(s), " +
                    seconds + " second(s)");
        } else if (minutes > 0) {
            return (minutes + " minute(s), " +
                    seconds + " second(s)");
        } else {
            return (seconds + " second(s)");
        }

    } // End of getElapsedtoString Method Class.

} ///:~ End of idxLapTime Class.
