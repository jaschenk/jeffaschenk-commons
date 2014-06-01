package jeffaschenk.commons.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Simple Formatter Class specific to the Client application
 *
 * @author jeffaschenk@gmail.com
 */
public class NumberUtility {

    public static BigDecimal toBigDecimal(final String str) {
        try {
            return (StringUtils.isEmpty(str) ? null : new BigDecimal(str));
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static BigDecimal toBigDecimal(final String str, boolean zeroDefault) {
        try {
            return (StringUtils.isEmpty(str) ? new BigDecimal("0.00") : new BigDecimal(str));
        } catch (NumberFormatException nfe) {
            return new BigDecimal("0.00");
        }
    }

    public static Integer toInteger(final String str) {
        return (StringUtils.isEmpty(str) ? 0 : Integer.valueOf(str));
    }

    public static String formatSliceNumber(final long number) {
        NumberFormat formatter = new DecimalFormat("00000");
        return formatter.format(number);
    }

    public static String formatZipCode(final Number zipCode) {
        DecimalFormat df = new DecimalFormat("000000000");
        df.setParseIntegerOnly(true);
        /** Format a number as Zip +4, code obtain from POI authored by:James May. */
        String result = df.format(zipCode);
        if (result.startsWith("0000"))
        {
            return result.substring(4, 9);
        } else {
            return result.substring(0, 9);
        }
    }

    public static String formatLast4ssn(final Number last4ssn) {
        DecimalFormat df = new DecimalFormat("0000");
        df.setParseIntegerOnly(true);
        return df.format(last4ssn);
    }

    public static String formatRowCount(final long number) {
        NumberFormat formatter = new DecimalFormat("###,###,###,###,##0");
        return formatter.format(number);
    }

    public static String formatAmount(final BigDecimal number) {
        NumberFormat formatter = new DecimalFormat(".00");
        return formatter.format(number);
    }

}
