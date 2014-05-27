package jeffaschenk.commons.standards.util;

import java.io.*;

/**
 * Utility Class for String Handling extensions specific to environment and data artifacts.
 *
 * @author Jeff.Schenk
 * @version $Id: $
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * Returns the pretty printed version of the specified class.
     *
     * @param clazz class to pretty print
     * @return pretty printed version of class name
     */
    public static String getPrettyPrintedClassName(Class<?> clazz) {
        // ensure proxied classes are resolved to their "real" names
        String className = clazz.getSimpleName();
        // this is no different than the logic for a method name
        return getPrettyPrintedTypeFromMethodName(className);
    }

    /**
     * Returns the pretty printed version of the specified class name.
     *
     * @param className name of class to pretty print
     * @return pretty printed version of class name
     */
    public static String getPrettyPrintedClassName(String className) {
        // this is no different than the logic for a method name
        return getPrettyPrintedTypeFromMethodName(className);
    }

    /**
     * Returns the pretty printed version of the specified property name. For example, commonName will be
     * returned as Common Name.
     *
     * @param propertyName property name to pretty print
     * @return pretty printed version of property name
     */
    public static String getPrettyPrintedPropertyName(String propertyName) {
        // this is no different than the logic for a method name, except we capitalize the first letter
        return getPrettyPrintedTypeFromMethodName(StringUtils.capitalize(propertyName));
    }

    /**
     * This method will return the object type from the specified method name.
     *
     * @param name method name to pretty print
     * @return pretty printed version of type the method is "working" with
     */
    public static String getPrettyPrintedTypeFromMethodName(String name) {
        StringBuilder sb = new StringBuilder();

        if ((name != null) && (!name.trim().isEmpty())) {
            char[] nameCharArray = name.toCharArray();

            boolean foundUpper = false;
            char previousChar = 0, currentChar;

            int nameCharArrayLength = nameCharArray.length;

            for (int i = 0; i < nameCharArrayLength; i++) {
                currentChar = nameCharArray[i];
                // we ignore all chars until an upper case is found
                if (foundUpper) {
                    if (Character.isUpperCase(currentChar)) {
                        if (Character.isLowerCase(previousChar)) {
                            // if previous is lower and current is upper, append a space:
                            // FooBar -> Foo Bar
                            sb.append(" ");
                        } else if ((i < nameCharArrayLength - 1) && (Character.isLowerCase(nameCharArray[i + 1]))) {
                            // if previous is upper, current is upper, but _next_ is lower, append a space:
                            // FOOBar -> FOO Bar
                            sb.append(" ");
                        }
                    }
                    sb.append(currentChar);
                } else if (Character.isUpperCase(currentChar)) {
                    foundUpper = true;
                    sb.append(currentChar);
                }
                previousChar = currentChar;
            }
        }

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Capitalizes a String changing the first letter to title case as per {@link Character#toTitleCase(char)}.
     */
    public static String capitalize(String str) {
        String capilizedStr = str;

        int len;
        if (str != null && (len = str.length()) > 0) {
            capilizedStr = new StringBuilder(len).append(Character.toTitleCase(str.charAt(0)))
                    .append(str.substring(1))
                    .toString();
        }

        return capilizedStr;
    }

    /**
     * to Boolean from String
     *
     * @param str
     * @return Boolean can be nul so, that a default could be applied.
     */
    public static Boolean toBoolean(String str, Boolean aDefault) {
        if (StringUtils.isEmpty(str)) {
            return aDefault;
        } else if ((str.equalsIgnoreCase("false")) ||
                (str.equalsIgnoreCase("disable")) ||
                (str.equalsIgnoreCase("no")) ||
                (str.equalsIgnoreCase("0"))) {
            return false;
        } else if ((str.equalsIgnoreCase("true")) ||
                (str.equalsIgnoreCase("enable")) ||
                (str.equalsIgnoreCase("yes")) ||
                (str.equalsIgnoreCase("1"))) {
            return true;
        }
        return aDefault;
    }

    /**
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     *
     * @param is - InputStream
     * @return String of Response Contents
     * @throws java.io.IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * HEX to Character Table.
     */
    static final byte[] HEX_CHAR_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
            (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

    /**
     * Get Hexidecimal String from Raw Byte Array.
     *
     * @param raw
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getHexString(byte[] raw)
            throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, "ASCII");
    }

    /**
     * Hexadecimal Character Array.
     */
    static final char[] hexChar = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * Perform Unicode Escape on Specified String.
     *
     * @param s
     * @return
     */
    public static String unicodeEscape(final String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >> 7) > 0) {
                sb.append("\134").append("u");       // Begin encoding of character.
                sb.append(hexChar[(c >> 12) & 0xF]); // append the hex character for the left-most 4-bits
                sb.append(hexChar[(c >> 8) & 0xF]);  // hex for the second group of 4-bits from the left
                sb.append(hexChar[(c >> 4) & 0xF]);  // hex for the third group
                sb.append(hexChar[c & 0xF]);         // hex for the last group, e.g., the right most 4-bits
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
        * Perform Unicode Escape on Specified String.
        *
        * @param s
        * @return
        */
       public static String unicodeHTMLEscape(final String s) {
           StringBuilder sb = new StringBuilder();
           for (int i = 0; i < s.length(); i++) {
               char c = s.charAt(i);
               char[] hexChars = new char[4];
               if ((c >> 7) > 0) {
                   sb.append("&#");                     // Begin encoding of character.
                   hexChars[0] = hexChar[(c >> 12) & 0xF]; // append the hex character for the left-most 4-bits
                   hexChars[1] = hexChar[(c >> 8) & 0xF];  // hex for the second group of 4-bits from the left
                   hexChars[2] = hexChar[(c >> 4) & 0xF];  // hex for the third group
                   hexChars[3] = hexChar[c & 0xF];         // hex for the last group, e.g., the right most 4-bits
                   String aString = "" + hexChars[0] + "" + hexChars[1] + "" + hexChars[2] + "" + hexChars[3];
                   // Make Hex String into decimal Number.
                   Integer a = Integer.valueOf(aString, 16);
                   sb.append(a);
                   sb.append(";");
               } else {
                   sb.append(c);
               }
           }
           return sb.toString();
       }

    /**
     * Check the validity of the Length of a UTF8 String.
     *
     * @param min
     * @param max
     * @param string
     * @return
     */
    public static boolean isUTF8StringLengthValid(int min, int max, String string) {
        if (string == null) { return false; }
        try {
            byte[] bytes = string.getBytes("UTF-8");
            return ( (bytes.length >= min) && (bytes.length <= max));
        } catch (UnsupportedEncodingException uee) {
             // Ignore...
        }
        return false;
    }


    public static int getUTF8StringLength(String string) {
        if (string == null) { return 0; }
        try {
            return string.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException uee) {
             // Ignore...
        }
        return 0;
    }

    /**
     * Determines if the String has any UTF-8 Type Characters in it's Data.
     *
     * @param string
     * @return
     */
    public static boolean isStringUTF8(String string) {
        if (string == null) { return false; }
        return isUTF8StringLengthValid(0, string.length(), string);
    }

    /**
     * Transform NotAllowed UTF8 Characters to Normal Ascii Characters.
     *
     *
     * U+2018	‘	e2 80 98	&#x2018;	‘ 	LEFT SINGLE QUOTATION MARK
     * U+2019	’	e2 80 99	&#x2019;	’ 	RIGHT SINGLE QUOTATION MARK
     * U+201A	‚	e2 80 9a	&#x201A;	‚ 	SINGLE LOW-9 QUOTATION MARK
     * U+201B	‛	e2 80 9b	&#x201B;	‛ 	SINGLE HIGH-REVERSED-9 QUOTATION MARK
     * U+201C	“	e2 80 9c	&#x201C;	“ 	LEFT DOUBLE QUOTATION MARK
     * U+201D	”	e2 80 9d	&#x201D;	” 	RIGHT DOUBLE QUOTATION MARK
     * U+201E	„	e2 80 9e	&#x201E;	„ 	DOUBLE LOW-9 QUOTATION MARK
     * U+201F	‟	e2 80 9f	&#x201F;	‟ 	DOUBLE HIGH-REVERSED-9 QUOTATION MARK
     *
     *
     * Please add as applicable in Transformation.
     * @link http://www.utf8-chartable.de/unicode-utf8-table.pl
     */
    public static String transformNotAllowedUTF8Characters(String string) {
        if ( (string == null) || (string.isEmpty()) )
            { return string; }
        return string.replace('\u2018','\'').replace('\u2019','\'').replace('\u201A','\'').replace('\u201B','\'')
                        .replace('\u201C','\"').replace('\u201D','\"').replace('\u201E','\"').replace('\u201F','\"');
    }

}
