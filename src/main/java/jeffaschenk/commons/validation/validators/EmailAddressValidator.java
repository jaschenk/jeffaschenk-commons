package jeffaschenk.commons.validation.validators;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Provide deep Email Address Validation
 *
 * originally published by:
 * http://www.devdaily.com/blog/post/java/java-email-address-validation-class by alvin on July 24, 2007.
 *
 * @author jeffaschenk@gmail.com
 *         Date: Apr 9, 2010
 *         Time: 4:29:26 PM
 */

/**
 * A class to provide stronger validation of email addresses.
 * devdaily.com, no rights reserved. :)
 */
public class EmailAddressValidator {

    /**
     * Is Valid EmailAddress?
     *
     * @param emailAddress
     * @return boolean True if valid email Address, otherwise false.
     */
    public static boolean isValidEmailAddress(String emailAddress) {
        // a null string is invalid
        if (emailAddress == null) {
            return false;
        }

        // a string without a "@" is an invalid email address
        if (emailAddress.indexOf("@") < 0) {
            return false;
        }

        // a string only more than one at "@" is an invalid email address
        if (emailAddress.indexOf("@") != emailAddress.lastIndexOf("@")) {
            return false;
        }

        // a string without a "."  is an invalid email address
        if (emailAddress.indexOf(".") < 0) {
            return false;
        }

        // a string without a "."  is an invalid email address
        if (emailAddress.indexOf(".") < 0) {
            return false;
        }

        try {
            InternetAddress internetAddress = new InternetAddress(emailAddress);
            return true;
        } catch (AddressException ae) {
            // log exception
            return false;
        }
    }

}

