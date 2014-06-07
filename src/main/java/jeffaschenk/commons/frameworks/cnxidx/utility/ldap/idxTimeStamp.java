package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.util.*;

/**
 * Java class for formulating a proper Timestamp for IRR functions.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxTimeStamp {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    private String myTimeStamp = "";

    private TimeZone tz;

    private Calendar rightNow;

    // ***********************************************
    // All Timestamps provide in LocalTime by Default.
    private boolean LOCALTIME = true;

    // *****************************************************************
    // idxTimeStamp Constructor
    public idxTimeStamp() {
        tz = TimeZone.getTimeZone("GMT+0");
    } // End of idxTimeStamp Constructor.

    /**
     * enableLocalTime method provides timestamps return in local time
     * and not GMT.
     */
    public void enableLocalTime() {
        LOCALTIME = true;
    } // End of enableLocalTime Method.

    /**
     * disableLocalTime method provides timestamps return in GMT time
     * and not Local Time.
     */
    public void disableLocalTime() {
        LOCALTIME = false;
    } // End of disableLocalTime Method.

    /**
     * gets the value from from the attribute
     *
     * @param Attributes attributes to get attribute value from
     * @param String     name of attribute to get value for
     * @return String the value containing the timestamp in the for of
     *         <B>YYYYMMDYHHMMSS.0Z</B>
     *         All timestamps generated will be relative to GMT+0 if LOCALTIME
     *         is set to false.
     */
    public String get() {

        // ***************************
        // Acquire a proper Time Stamp
        if (LOCALTIME) {
            rightNow = Calendar.getInstance();
        } else {
            rightNow = Calendar.getInstance(tz);
        }

        // ***************************
        // Formulate the Time Stamp.
        year = rightNow.get(rightNow.YEAR);
        myTimeStamp = myTimeStamp.valueOf(year);

        month = rightNow.get(rightNow.MONTH) + 1;
        if (month <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(month));

        day = rightNow.get(rightNow.DAY_OF_MONTH);
        if (day <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(day));

        hour = rightNow.get(rightNow.HOUR_OF_DAY);
        if (hour <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(hour));

        minute = rightNow.get(rightNow.MINUTE);
        if (minute <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(minute));

        second = rightNow.get(rightNow.SECOND);
        if (second <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(second));

        if (!LOCALTIME) {
            myTimeStamp = myTimeStamp.concat(".0Z");
        }

        return (myTimeStamp);

    } // End of get Method.

    /**
     * getFTS will obtain a timestamp suitable for printing
     * in a Log file.
     *
     * @return String the value containing the timestamp in the for of
     *         <B>YYYY-MM-DY.HH:MM:SS</B>
     *         All timestamps generated will be relative to GMT+0 if LOCALTIME
     *         is set to false.
     */
    public String getFTS() {

        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

        String myTimeStamp = "";

        Calendar rightNow;

        // ***************************
        // Acquire a proper Time Stamp
        if (LOCALTIME) {
            rightNow = Calendar.getInstance();
        } else {
            rightNow = Calendar.getInstance(tz);
        }

        // ***************************
        // Formulate the Time Stamp.
        year = rightNow.get(rightNow.YEAR);
        myTimeStamp = myTimeStamp.valueOf(year);

        myTimeStamp = myTimeStamp.concat("-");

        month = rightNow.get(rightNow.MONTH) + 1;
        if (month <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(month));

        myTimeStamp = myTimeStamp.concat("-");

        day = rightNow.get(rightNow.DAY_OF_MONTH);
        if (day <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(day));

        myTimeStamp = myTimeStamp.concat(".");

        hour = rightNow.get(rightNow.HOUR_OF_DAY);
        if (hour <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(hour));

        myTimeStamp = myTimeStamp.concat(":");

        minute = rightNow.get(rightNow.MINUTE);
        if (minute <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(minute));

        myTimeStamp = myTimeStamp.concat(":");

        second = rightNow.get(rightNow.SECOND);
        if (second <= 9) {
            myTimeStamp = myTimeStamp.concat("0");
        }
        myTimeStamp = myTimeStamp.concat(myTimeStamp.valueOf(second));

        return (myTimeStamp);

    } // End of get Method.

} ///:~ End of idxTimeStamp Class.
