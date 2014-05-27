/**
 * QNow
 */
package jeffaschenk.commons.standards.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility Class for handling Dates.
 *
 * @author Jeff.Schenk
 * @version $Id: $
 */
public class DateUtils {

	/** Constant <code>fullDateFormatter</code> */
	public static DateFormat fullDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** Constant <code>dateOnlyFormatter</code> */
    public static DateFormat dateOnlyFormatter = new SimpleDateFormat("yyyy-MM-dd");

    /** Constant <code>miltimeOnlyFormatter</code> */
    public static DateFormat miltimeOnlyFormatter = new SimpleDateFormat("HH:mm:ss.SSS z");

    /** Constant <code>timeOnlyFormatter</code> */
    public static DateFormat timeOnlyFormatter = new SimpleDateFormat("h:mm a z");

    /** Constant <code>auctionDateFormatter</code> */
    public static DateFormat auctionDateFormatter = new SimpleDateFormat("MMM d - h:mm a z");

    /** Constant <code>monthDayYearDateOnlyFormatter</code> */
    public static DateFormat monthDayYearDateOnlyFormatter = new SimpleDateFormat("MM/dd/yyyy");

    public static DateFormat dateAndTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");

    private static DateFormat[] formatters = {fullDateFormatter, dateOnlyFormatter, monthDayYearDateOnlyFormatter, dateAndTimeFormatter, auctionDateFormatter};

    private static DateFormat[] timeformatters = {miltimeOnlyFormatter, timeOnlyFormatter, dateAndTimeFormatter};

    /**
     * Utility class only, so can't create these guys
     */
    private DateUtils() {
    }

    /**
     * Tries to parse a Date out of the specified string. If a date cannot be parsed, null
     * will be returned from this method.
     *
     * @param dateString date string to parse
     * @return Date or null if string is not a valid date
     */
    public static Date valueOf(String dateString) {
        Date date = null;

        for (int formatterIndex = 0; formatterIndex < formatters.length && date == null; ++formatterIndex) {
            try {
                date = formatters[formatterIndex].parse(dateString);
            } catch (ParseException e) {
                // bad date
            }
        }

        return date;
    }

    /**
     * Tries to parse a Date out of the specified string. If a date cannot be parsed, null
     * will be returned from this method.
     *
     * @param dateString date string to parse
     * @return Object[] Array Containing Object[0] Date or Null, Object[1] Formatter Used.
     */
    public static Object[] valueOfAndFormatUsed(String dateString) {
        Date date = null;
        DateFormat formatterUsed = null;

        for (int formatterIndex = 0; formatterIndex < formatters.length && date == null; ++formatterIndex) {
            try {
                date = formatters[formatterIndex].parse(dateString);
                formatterUsed = formatters[formatterIndex];
            } catch (ParseException e) {
                // bad date
            }
        }

        return new Object[]{date, formatterUsed};
    }

     /**
     * Tries to parse a Date out of the specified string. If a date cannot be parsed, null
     * will be returned from this method.
     *
     * @param dateString date string to parse
     * @return Date or null if string is not a valid date
     */
    public static Date valueOf(String dateString, DateFormat dateFormat) {
        Date date = null;

            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                // bad date
            }

        return date;
    }

    /**
     * Tries to parse a Date out of the specified string. If a date cannot be parsed, null
     * will be returned from this method.
     *
     * @return Date or null if string is not a valid date
     * @param timeString a {@link String} object.
     */
    public static Date valueOfTime(String timeString) {
        Date date = null;

        for (int formatterIndex = 0; formatterIndex < timeformatters.length && date == null; ++formatterIndex) {
            try {
                date = timeformatters[formatterIndex].parse(timeString);
            } catch (ParseException e) {
                // bad date
            }
        }

        return date;
    }

    /**
     * Returns the string representation of the specified date
     *
     * @param date date to return string for
     * @return string representation of date
     */
    public static String toString(Date date) {
        return fullDateFormatter.format(date);
    }

    /**
     * Returns the string representation of the specified date
     * using/applying a specific Date Formatter.
     *
     * @param date
     * @param dateFormatter
     * @param dateFormatter a {@link java.text.DateFormat} object.
     * @return String representation of date
     */
    public static String toString(Date date, DateFormat dateFormatter) {
    	return dateFormatter.format(date);
    }


    public static DateFormat[] getFormatters() {
        return formatters;
    }

    public static DateFormat[] getTimeformatters() {
        return timeformatters;
    }
}
