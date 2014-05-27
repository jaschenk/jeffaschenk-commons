package jeffaschenk.commons.standards.util;

import com.ocpsoft.pretty.time.PrettyTime;
import com.outbid.sor.commons.container.security.object.OutbidSecuritySessionUserObject;
import com.outbid.sor.model.orm.user.RegisteredUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Utility methods to format properties in Freemarker templates.
 * <p/>
 *
 * @author (asvenss) Andreas Svensson
 */
public class FormatUtils {

    private final static Logger logger = LoggerFactory.getLogger(FormatUtils.class);

    private Map<String, DateFormat> dateFormats;

    private Map<String, DateFormat> dateTimeFormats;

    private Map<String, DateFormat> timeFormats;

    private Map<String, NumberFormat> integerNumberFormats;

    private Map<Locale, PrettyTime> prettyTimesCache;

    private InternationalizationUtils internationalizationUtils;

    private static String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private static String[] dayName = {"Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"};

    public FormatUtils(InternationalizationUtils internationalizationUtils) {
        this.internationalizationUtils = internationalizationUtils;
        this.prettyTimesCache = new Hashtable<Locale, PrettyTime>();
    }

    /**
     * Formats a {@link java.util.Date} to a date and time string in pacific timezone.
     *
     * @param date the {@link java.util.Date} to be formatted
     * @return a String of the date in the user's timezone if it can be
     *         determined, in the system timezone otherwise.
     */
    public String dateTime(Date date) {
        if (date == null) {
            return null;
        }
        Locale locale = Locale.US;

        DateFormat dateFormat = lookupDateTimeFormatInstance(locale);

        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(date);
    }

    /**
     * Formats a {@link java.util.Date} to a date and time string with the timezone that
     * the user has chosen.
     *
     * @param date           the {@link java.util.Date} to be formatted
     * @param registeredUser the {@link RegisteredUser}
     * @return a String of the date in the user's timezone if it can be
     *         determined, in the system timezone otherwise.
     */
    public String dateTime(Date date, RegisteredUser registeredUser) {
        if (date == null) {
            return null;
        }
        Locale locale = internationalizationUtils.getLocale(registeredUser);

        DateFormat dateFormat = lookupDateTimeFormatInstance(locale);
        if (registeredUser == null) {
            return dateFormat.format(date);
        }

        TimeZone timeZone = internationalizationUtils
                .getTimeZone(registeredUser);
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(date);
    }

    /**
     * Formats a {@link java.util.Date} to a date and time string with the timezone that
     * the user has chosen.
     *
     * @param date the {@link java.util.Date} to be formatted
     * @param user the {@link OutbidSecuritySessionUserObject}
     * @return a String of the date in the user's timezone if it can be
     *         determined, in the system timezone otherwise.
     */
    public String dateTime(Date date, OutbidSecuritySessionUserObject user) {
        if (date == null) {
            return null;
        }
        Locale locale = internationalizationUtils.getLocale(user);

        DateFormat dateFormat = lookupDateTimeFormatInstance(locale);

        TimeZone timeZone = internationalizationUtils.getTimeZone(user);
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(date);
    }

    /**
     * These two overridden functions help in displaying the subtitle for groups.
     * If the subtitle being passed is a string then we'll just return that string,
     * otherwise the methods for formatting a Date will be used.
     *
     * URFB-515 - Applied Pattern for Checking if a String is a Date format is properly.
     *
     */
    public String dateTime(String string, RegisteredUser registeredUser) {
        Object[] objectArray = DateUtils.valueOfAndFormatUsed(string);
        if ( (objectArray == null) || (objectArray.length < 2) || (objectArray[0] == null) ) {
            // this String is not a Valid Date String, so return original Data.
            return string;
        }
        // Cast Objects
        Date aDate = (Date) objectArray[0];
        DateFormat formatterUsed = (DateFormat) objectArray[1];

        // Fix Date, Depending if we are missing Data.
        Calendar cal = fixDatesYear(aDate);

        // Get Proper Timezone for User and apply Format
        TimeZone timeZone = internationalizationUtils.getTimeZone(registeredUser);
        formatterUsed.setTimeZone(timeZone);
        // Return
        return formatterUsed.format(cal.getTime());
    }

    public String dateTime(String string, OutbidSecuritySessionUserObject user) {
        Object[] objectArray = DateUtils.valueOfAndFormatUsed(string);
        if ( (objectArray == null) || (objectArray.length < 2) || (objectArray[0] == null) ) {
            // this String is not a Valid Date String, so return original Data.
            return string;
        }
        // Cast Objects
        Date aDate = (Date) objectArray[0];
        DateFormat formatterUsed = (DateFormat) objectArray[1];

        // Fix Date, Depending if we are missing Data.
        Calendar cal = fixDatesYear(aDate);

        // Get Proper Timezone for User and apply Format
        TimeZone timeZone = internationalizationUtils.getTimeZone(user);
        formatterUsed.setTimeZone(timeZone);
        // Return
         return formatterUsed.format(cal.getTime());
    }

    /**
     * Formats a {@link java.util.Date} to a date string with the timezone that the user
     * has chosen.
     *
     * @param date the {@link java.util.Date} to be formatted
     * @param user the {@link OutbidSecuritySessionUserObject}
     * @return a String of the date in the user's timezone if it can be
     *         determined, in the system timezone otherwise.
     */
    public String date(Date date, OutbidSecuritySessionUserObject user) {
        if (date == null) {
            return null;
        }
        Locale locale = internationalizationUtils.getLocale(user);

        DateFormat dateFormat = lookupDateFormat(locale);

        TimeZone timeZone = internationalizationUtils.getTimeZone(user);
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(date);
    }

    /**
     * Formats a {@link java.util.Date} to a date string with the timezone that the user
     * has chosen.
     *
     * @param date           the {@link java.util.Date} to be formatted
     * @param registeredUser the {@link RegisteredUser}
     * @return a String of the date in the user's timezone if it can be
     *         determined, in the system timezone otherwise.
     */
    public String date(Date date, RegisteredUser registeredUser) {
        if (date == null) {
            return null;
        }
        Locale locale = internationalizationUtils.getLocale(registeredUser);

        DateFormat dateFormat = lookupDateFormat(locale);
        if (registeredUser == null) {
            return dateFormat.format(date);
        }

        TimeZone timeZone = internationalizationUtils
                .getTimeZone(registeredUser);
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(date);
    }

    /**
     * Formats a {@link java.util.Date} to a time string with the timezone that the user
     * has chosen.
     *
     * @param date           the {@link java.util.Date} to be formatted
     * @param registeredUser the {@link RegisteredUser}
     * @return a String of the time in the user's timezone if it can be
     *         determined, in the system timezone otherwise.
     */
    public String time(Date date, RegisteredUser registeredUser) {
        if (date == null) {
            return null;
        }
        Locale locale = internationalizationUtils.getLocale(registeredUser);

        DateFormat dateFormat = lookupTimeFormat(locale);
        if (registeredUser == null) {
            return dateFormat.format(date);
        }

        TimeZone timeZone = internationalizationUtils
                .getTimeZone(registeredUser);
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(date);
    }

    public String time(Date date, OutbidSecuritySessionUserObject user) {
        if (date == null) {
            return null;
        }
        Locale locale = internationalizationUtils.getLocale(user);

        DateFormat dateFormat = lookupTimeFormat(locale);

        TimeZone timeZone = internationalizationUtils.getTimeZone(user);
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(date);
    }

    /**
     * Formats a {@link java.util.Date} to a format appropriate to the specified
     * {@link java.util.Locale}. Only the date portion of the date is shown, i.e. the time
     * is not shown. The system can be configured with one formatter per locale.
     * This is done through Spring.
     *
     * @param date   the {@link java.util.Date} to be formatted
     * @param locale the {@link java.util.Locale} to be used. The locale specific
     *               {@link java.text.DateFormat} will be used if null or no registered
     *               {@link java.text.DateFormat} can be found.
     * @return A formatted string of the date.
     */
    public String date(Date date, Locale locale) {
        return lookupDateFormat(locale).format(date);
    }

    /**
     * Formats an integer as a string.
     *
     * @param number the number to be formatted
     * @param locale the locale to be used during formatting
     * @return a properly formatted integer
     */
    public String integer(Number number, Locale locale) {
        if (locale == null) {
            locale = Locale.US;
        }
        return lookupIntegerNumberInstance(locale).format(number);
    }

    /**
     * Formats a {@link java.util.Date} to a format appropriate to the specified
     * {@link java.util.Locale}. The system can be configured with one formatter per
     * locale. This is done through Spring.
     *
     * @param date   the {@link java.util.Date} to be formatted
     * @param locale the {@link java.util.Locale} to be used. The locale specific
     *               {@link java.text.DateFormat} will be used if null or no registered
     *               {@link java.text.DateFormat} can be found.
     * @return A formatted string of the date and time.
     */
    @SuppressWarnings("deprecation")
    public String dateTime(Date date, Locale locale) {

        if (date == null) {
            return "";
        }
        return lookupDateTimeFormatInstance(locale).format(date);

    }

    /**
     * Formats a date as a duration from now, such as seen on Twitter etc.
     * &quot;2 days ago&quot;
     *
     * @param date   the date when it happened
     * @param locale the locale which will determine the language used
     * @return a {@link String} formatted in the correct language
     */
    public String duration(Date date, Locale locale) {
        return lookupPrettyTime(locale).format(date);
    }

    /**
     * Formats the live auction time according to the rules defined in
     * Outbid_Web_092110.pdf page 406.
     *
     * @param date   The live auction date
     * @param locale the locale of the user
     * @return a formatted string
     */
    public String liveAuctionTime(Date date, Locale locale, TimeZone timeZone) {
        if (date == null) {
            return "";
        }

        Date now = new Date();
        long diff = date.getTime() - now.getTime();
        DateFormat hoursAndMinutes = new SimpleDateFormat("h:mm a z");
        hoursAndMinutes.setTimeZone(timeZone);

        long diffMinutes = diff / (60 * 1000);
        logger.debug("Minutes left : " + diffMinutes);
        if (diffMinutes <= 0) {
            return "Live";
        } else if (diffMinutes <= 30) {
            return "Starting in " + diffMinutes + " minutes";
        } else if (diffMinutes <= 1440) {
            if (date.getDate() == now.getDate()) {
                return "Today - " + hoursAndMinutes.format(date);
            } else {
                return dayName[date.getDay()] + " - " + hoursAndMinutes.format(date);
            }
        } else if (diffMinutes <= 8640) {
            return dayName[date.getDay()] + " - " + hoursAndMinutes.format(date);
        } else {
            return monthName[date.getMonth()] + " " + date.getDate() + " - " + hoursAndMinutes.format(date);
        }

    }

    /**
     * Outputs the user's timezone.
     *
     * @param user the {@link OutbidSecuritySessionUserObject}
     * @return a String of the user's timezone
     */
    public String timeZone(OutbidSecuritySessionUserObject user) {
        Locale locale = internationalizationUtils.getLocale(user);

        DateFormat dateFormat = new SimpleDateFormat("z", locale);

        TimeZone timeZone = internationalizationUtils.getTimeZone(user);
        dateFormat.setTimeZone(timeZone);

        return dateFormat.format(new Date());
    }

    public void setDateFormats(Map<String, DateFormat> dateFormats) {
        this.dateFormats = dateFormats;
    }

    public void setDateTimeFormats(Map<String, DateFormat> dateTimeFormats) {
        this.dateTimeFormats = dateTimeFormats;
    }

    public void setTimeFormats(Map<String, DateFormat> timeFormats) {
        this.timeFormats = timeFormats;
    }

    public void setIntegerNumberFormats(
            Map<String, NumberFormat> integerNumberFormats) {
        this.integerNumberFormats = integerNumberFormats;
    }

    private DateFormat lookupDateFormat(Locale locale) {
        if (dateFormats != null && locale != null) {
            DateFormat dateFormat = dateFormats.get(locale.toString()
                    .toLowerCase());
            if (dateFormat != null) {
                return (DateFormat) dateFormat.clone();
            }
        }
        return DateFormat.getDateInstance(DateFormat.SHORT, locale);
    }

    private DateFormat lookupTimeFormat(Locale locale) {
        if (timeFormats != null && locale != null) {
            DateFormat dateFormat = timeFormats.get(locale.toString()
                    .toLowerCase());
            if (dateFormat != null) {
                return (DateFormat) dateFormat.clone();
            }
        }
        return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
    }

    private DateFormat lookupDateTimeFormatInstance(Locale locale) {
        if (dateTimeFormats != null && locale != null) {
            DateFormat dateFormat = dateTimeFormats.get(locale.toString()
                    .toLowerCase());
            if (dateFormat != null) {
                return (DateFormat) dateFormat.clone();
            }
        }
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.LONG, locale);
    }

    private NumberFormat lookupIntegerNumberInstance(Locale locale) {
        if (integerNumberFormats != null && locale != null) {
            NumberFormat numberFormat = integerNumberFormats.get(locale
                    .toString().toLowerCase());
            if (numberFormat != null) {
                return (NumberFormat) numberFormat.clone();
            }
        }
        return NumberFormat.getIntegerInstance(locale);
    }

    private PrettyTime lookupPrettyTime(Locale locale) {
        PrettyTime prettyTime = prettyTimesCache.get(locale);
        if (prettyTime == null) {
            prettyTime = new PrettyTime(locale);
            prettyTimesCache.put(locale, prettyTime);
        }
        return prettyTime;
    }

    /**
     * Set proper year for a Date without a Year.
     *
     * @param aDate
     * @return
     */
    private Calendar fixDatesYear(Date aDate) {
        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        if (cal.get(Calendar.YEAR) > 1970)
            { return cal; }
        cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
        // Determine Year, either this year or Next.
        if ((now.get(Calendar.MONTH) == 12) &&
                (now.get(Calendar.MONTH) > cal.get(Calendar.MONTH))) {
            cal.add(Calendar.YEAR, 1);
        }
        return cal;
    }
}
