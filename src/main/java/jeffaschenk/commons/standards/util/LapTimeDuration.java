/**
 * QNow
 */
package jeffaschenk.commons.standards.util;


/**
 * Java Class for accumulating LapTimes for a Iterative Task.
 *
 * @author jeff.schenk
 * @version $Id: $
 */
public class LapTimeDuration extends TimeDuration {
	
	private long laps       = 0;    // Number of Laps.
	private long average    = 0;    // Average Duration Time in Milliseconds.
	private long minimum    = 0;    // Minimum Duration Time in Milliseconds.
	private long maximum    = 0;    // Maximum Duration Time in Milliseconds.
	private long total      = 0;    // Total Duration Time in Milliseconds.

    	/**
    	 * Provides Default Constructor.
    	 */
    	public LapTimeDuration() {
        } // End of Constructor.	

	/**
	 * Set Current Time as Start Time.
	 */
	public void start() {
		super.start();
		laps++;
        } // End of Start Method.

	/**
	 * Stop and Set Current Time as End Time.
	 */
	public void stop() {
		super.stop();
		total = total + super.getCurrentDuration();
	        if ( ( super.getCurrentDuration() < minimum ) || 
		     ( laps <= 1 ) ) 
		     { minimum = super.getCurrentDuration(); }
	        if ( ( super.getCurrentDuration() > maximum ) ||
		     ( laps <= 1 ) ) 
		     { maximum = super.getCurrentDuration(); }
		  average = total / laps; 
        } // End of stop Method.

	/**
	 * Reset all Counters.
	 */
	public void reset() {
		super.reset();

		laps       = 0;
		average    = 0;
		minimum    = 0;
		maximum    = 0;
		total      = 0; 
        } // End of Reset Method.

	/**
	 * {@inheritDoc}
	 *
	 * Obtain data in String form.
	 */
	@Override
	public String toString() {
		return( "Min: "+ super.getElapsedtoString( minimum ) + ", "+ 
		        "Max: "+ super.getElapsedtoString( maximum ) + ", "+ 
		        "Avg: "+ super.getElapsedtoString( average ) + " for "+
			laps + " Iterations.");
	} // End of toString Override.
} ///:~ End of LapTimeDuration Class.
