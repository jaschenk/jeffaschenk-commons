package jeffaschenk.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple Formatter Class specific to the Client application
 *
 * @author jeffaschenk@gmail.com
 */
public class DateUtility {

    public static Date parseMMDDYYYY(final String str) {
        try {
            DateFormat formatter;
            if (StringUtils.isNotEmpty(str)) {
                if (str.length() > 7) {
                    formatter = new SimpleDateFormat("MMddyyyy");
                } else {
                    formatter = new SimpleDateFormat("Mddyyyy");
                }
                return formatter.parse(str);
            }
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Parsing Exception Encountered:[" + pe.getMessage() + "]");
        }
        return null;
    }

    public static Date parseYYYYMMDYHHMMSS(final String str) {
        try {
            DateFormat formatter;
            if (StringUtils.isNotEmpty(str)) {
                    formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                return formatter.parse(str);
            }
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Parsing Exception Encountered:[" + pe.getMessage() + "]");
        }
        return null;
    }

}
