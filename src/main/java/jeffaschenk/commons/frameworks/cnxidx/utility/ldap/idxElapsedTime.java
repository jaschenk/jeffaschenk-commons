package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;


/**
 * Java Class for accumulating Status and Statistics among various
 * JNDI Functions and Class.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxElapsedTime {

    private long start = 0;       // Start Time in Milliseconds.
    private long end = 0;         // End Time in Milliseconds.
    private long duration = 0;    // Duration Time in Milliseconds.

    /**
     * Provides Common Constructor for Calculating Elapsed Time.
     * Automatically Sets the Start Time.
     */
    public idxElapsedTime() {
        start = System.currentTimeMillis();
    } // End of idxStatus Constructor.

    /**
     * Set Current Time as Start Time.
     */
    public void setStart() {
        start = System.currentTimeMillis();
    } // End of setStart Method.

    /**
     * Set Current Time as End Time.
     */
    public void setEnd() {
        end = System.currentTimeMillis();
        duration = (end - start);
    } // End of setEnd Method.

    /**
     * Get Last Operation
     */
    public long getDurationMillis() {
        duration = (end - start);
        return (duration);
    } // End of getDurationMillis Method.

    /**
     * Get Elapsed Timing in String Form.
     */
    public String getElapsed() {
        long hours;
        long minutes;
        long seconds;

        // **************************************
        // First Convert Duration into Seconds.
        long timeInSeconds = getDurationMillis() / 1000;

        if (timeInSeconds <= 0) {
            return (getDurationMillis() + " ms");
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

    } // End of getElapsed Method Class.

} ///:~ End of idxElaspedTime Class.
