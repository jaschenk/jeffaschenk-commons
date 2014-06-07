/*
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jeffaschenk.commons.frameworks.cnxidx.utility.filtering;


/**
 * @author john.leichner
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class HexCharacterFilter {

    /**
     * Method to replace illegal hex characters (those less than 0x20 and not 0x09, 0x0A
     * or 0x0D) with the hex value.
     *
     * @param text The text to clean up.
     * @return The valid text with the invalid characters replaced with hex codes.
     */
    public static String filterHexCharacters(String text) {
        if ((text == null) || (text.length() == 0)) {
            return text;
        }
        StringBuffer result = new StringBuffer(text.length());
        byte[] bytes = text.getBytes();

        for (int i = 0; i < bytes.length; i++) {
            //any byte over 0x20, or 0x09, 0x0A or 0x0D are allowed, others
            // are converted to hex strings.
            if (bytes[i] < 32) {
                if ((bytes[i] != 9) &&
                        (bytes[i] != 10) &&
                        (bytes[i] != 13)) {
                    StringBuffer hexString = new StringBuffer();
                    hexString.append(Integer.toHexString((char) bytes[i]));
                    if (hexString.length() == 1) {
                        hexString.insert(0, "0x0");
                    } else {
                        hexString.insert(0, "0x");
                    }
                    result.append(hexString.toString());
                } else {
                    result.append((char) bytes[i]);
                }
            } else {
                result.append((char) bytes[i]);
            }
        }

        return result.toString();
    }

    public static void main(String args[]) {
        byte b = '\n';
        StringBuffer buff = new StringBuffer();
        buff.append("This is a test");
        buff.append((char) b);
        buff.append("this is only a test.");
        buff.append((char) b);

        b = 0x0;
        buff.append((char) b);
        b = 0x0C;
        buff.append((char) b);
        buff.append("The test is now complete");

        System.out.println("Pre-scrub - \n[" + buff.toString() + "].");
        String clean = HexCharacterFilter.filterHexCharacters(buff.toString());
        System.out.println("Post-scrub - \n[" + clean + "].");

    }
}
