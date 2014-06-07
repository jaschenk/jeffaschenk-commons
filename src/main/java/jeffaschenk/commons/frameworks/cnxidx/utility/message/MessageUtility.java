package jeffaschenk.commons.frameworks.cnxidx.utility.message;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;

public class MessageUtility {
    public static final String LEVEL_TAG = "_level";
    public static final String MESSAGE_FILE_NAME = ".message";
    public static final String ERROR_FILE_NAME = ".error";

    /**
     * Loads the properties file.  The file loaded will be named
     * "message_en.properties" if the default locale is "en" or
     * "message_fr.properties" if the default locale is "fr".  If it cannot
     * find the proper file, message.properties will be loaded.  If that file
     * is missing
     */
    protected static Hashtable<String,ResourceBundle> MESSAGES = new Hashtable<>();

    /**
     * Populates a standard message with no parameters from the resource bundle
     * properties file.
     *
     * @param messageName The key to the desired message (see MessageConstants)
     * @return The desired message.  The messageName will be returned if the
     *         requested message is not found.
     */
    public static String getMessage(String messageName) {
        try {
            String message = findMessage(messageName);

            // if the message has not been found for the key - the same key object is returned
            // from the above call, so the following is intentionally an object comparison
            if (messageName == message) {
                // didn't find the message, return the key
                return message;
            }
            // the message was found, format and return with the key prepended for reference
            return "[" + messageName + "]  " + message;
        } catch (Exception e) {
            return messageName;
        }
    }

    /**
     * This method does the same thing as getMessage() but does not prepend the message key
     * to the message.  If the message is not found, the key is returned.
     *
     * @param messageName The key to the desired message (see MessageConstants)
     * @return The desired message.  The messageName will be returned if the
     *         requested message is not found.
     */
    public static String getMessageNoKey(String messageName) {
        try {
            return findMessage(messageName);
        } catch (Exception e) {
            return messageName;
        }
    }

    /**
     * Returns a standard message with parameters substituted from the resource
     * bundle properties file.
     *
     * @param messageName The key to the desired message (see MessageConstants)
     * @param param       The parameter to use for substitution in the message.
     * @return The desired string with the parameter substituted.
     */
    public static String getMessage(String messageName, Object param) {
        return getMessage(messageName, new Object[]{param});
    }

    /**
     * Returns a standard message with parameters substituted from the resource
     * bundle properties file.
     *
     * @param messageName The key to the desired message (see MessageConstants)
     * @param param       The parameters to use for substitution in the message.
     * @return The desired string with the parameters substituted.
     */
    public static String getMessage(String messageName, Object[] param) {
        try {
            String message = findMessage(messageName);
            String formattedMessage = MessageFormat.format(message, param);

            // if the key was not found, we should append the parameters to the key before
            // returning it, so the user at least gets the data.
            if (messageName == message) {
                // didn't find the message, return the formatted key -but add the parameters so
                // the user has a chance to get the idea of the message

                StringBuffer buff = new StringBuffer(formattedMessage);

                for (int i = 0; i < param.length; i++) {
                    buff.append("[" + (String) param[i] + "]");
                }

                return buff.toString();
            }
            // the message was found, format and return with the key prepended for reference
            return "[" + messageName + "]  " + formattedMessage;
        } catch (Exception e) {
            return messageName;
        }
    }

    /**
     * Does the same thing as getMessage() but does not prepend the message key to the message.
     * The message key is returned if the message is not found.
     *
     * @param messageName The key to the desired message (see MessageConstants)
     * @param param       The parameters to use for substitution in the message.
     * @return The desired string with the parameters substituted.
     */
    public static String getMessageNoKey(String messageName, Object[] param) {
        try {
            return MessageFormat.format(findMessage(messageName), param);
        } catch (Exception e) {
            return messageName;
        }
    }

    /**
     * Returns the logging level specified for a message if it exists.  If the
     * level is not found in the resource, the value passed in is returned.
     *
     * @param messageKey   The message key (MessageConstant) to find the loging
     *                     level for.
     * @param levelDefault The logging level to return if the logging level cannot
     *                     be determined for the specified messag key.
     * @return The loggin level for the specified message, default if not found.
     */
    public static FrameworkLoggerLevel getLevel(String messageKey, FrameworkLoggerLevel levelDefault) {
        try {
            String loggingLevel = findMessage(messageKey + LEVEL_TAG);

            if (loggingLevel.equalsIgnoreCase("SEVERE")) {
                return FrameworkLoggerLevel.SEVERE;
            } else if (loggingLevel.equalsIgnoreCase("ERROR")) {
                return FrameworkLoggerLevel.ERROR;
            } else if (loggingLevel.equalsIgnoreCase("WARNING")) {
                return FrameworkLoggerLevel.WARNING;
            } else if (loggingLevel.equalsIgnoreCase("INFO")) {
                return FrameworkLoggerLevel.INFO;
            } else if (loggingLevel.equalsIgnoreCase("DEBUG")) {
                return FrameworkLoggerLevel.DEBUG;
            } // none of these, then use the default

        } catch (MissingResourceException mre) {
            // Ignore, just means it was not specified.
        }

        // all else fails, use the default
        return levelDefault;
    }

    /**
     * Searches for the string key in the existing ResourceBundles.  If it is
     * not found, this method will try loading a new message bundle based on
     * the "dot" notation in the key (see loadResource).
     *
     * @param key The message key to search for.
     * @return The message found, or key if no message can be found.
     */
    private static String findMessage(String key) {
        if (key == null) {
            return null;
        }

        synchronized (MESSAGES) {
            Enumeration values = MESSAGES.elements();

            while (values.hasMoreElements()) {
                ResourceBundle aBundle = (ResourceBundle) (values.nextElement());

                try {
                    return aBundle.getString(key);
                } catch (MissingResourceException me) {
                    // indicates that this bundle does not have the key.. continue
                }
            }
        }

        // Did not find the key - try loading a new bundle
        return loadBundle(key);
    }

    /**
     * Tries to load a ResourceBundle based on a key.  Returns the message
     * for the key if the resource bundle is found and it contains the key.
     *
     * @param key The key to the message to be returned.
     * @return The message corresponding to key, if found, key otherwise.
     */
    private static String loadBundle(String key) {
        int location = key.lastIndexOf(".");
        if (location == key.length() - 1) {
            // Indicates that the "." is at the end of the string.  If this is
            // the case, this may not be a message key at all, but a message that
            // ends with a period.  Don't search for a message in that case.
            // Look for the next "."
            String tempString = key.substring(0, location - 1);
            location = tempString.lastIndexOf(".");
        }
        String subString = "";
        if (location != -1) {
            subString = key.substring(0, location);
        }
        // true when we find the bundle to load
        boolean foundBundle = false;

        // true when we found the bundle already loaded but missing the key we are looking for.
        boolean haveMessageBundle = false;
        boolean haveErrorBundle = false;
        String reply = null;

        while ((location != -1) && !foundBundle) {
            if (haveMessageBundle && haveErrorBundle) {
                // we found both bundles, but the message key isn't in them - break here.
                break;
            }

            try {
                String name = subString + MESSAGE_FILE_NAME;

                // if we already have this bundle, don't reload
                if (MESSAGES.containsKey(name)) {
                    haveMessageBundle = true;
                } else {
                    ResourceBundle newBundle = ResourceBundle.getBundle(name);
                    foundBundle = true;
                    synchronized (MESSAGES) {
                        MESSAGES.put(name, newBundle);
                    }

                    // See if we got the message in this resource bundle
                    reply = newBundle.getString(key);
                }
            } catch (MissingResourceException me) {
                // means the file wasn't found, or the message was not in 
                // the new bundle, so continue
            }
            try {
                String name = subString + ERROR_FILE_NAME;

                // if we already have this bundle, don't reload
                if (MESSAGES.containsKey(name)) {
                    haveErrorBundle = true;
                } else {
                    ResourceBundle newBundle = ResourceBundle.getBundle(name);
                    foundBundle = true;
                    synchronized (MESSAGES) {
                        MESSAGES.put(name, newBundle);
                    }

                    if (reply == null) {
                        reply = newBundle.getString(key);
                    }
                }
            } catch (MissingResourceException me) {
                // means the file wasn't found, or the message was not in 
                // the new bundle, so continue
            }

            location = subString.lastIndexOf(".");
            if (location != -1) {
                subString = subString.substring(0, location);
            }
        }
        if (reply != null) {
            return reply;
        }

        return key;
    }

    /**
     * Refresh message that dumps all the loaded messages so they can be reloaded and pick up
     * any changes made to the messages.
     */
    public static void refresh() {
        synchronized (MESSAGES) {
            MESSAGES = new Hashtable<>();
        }
    }

}