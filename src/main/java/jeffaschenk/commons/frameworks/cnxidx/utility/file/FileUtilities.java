package jeffaschenk.commons.frameworks.cnxidx.utility.file;

import jeffaschenk.commons.exceptions.FileException;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.message.MessageConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * This full static class is there to provide helper methods for dealing with files.
 */
public class FileUtilities {

    //---------------------------
    // Class constants
    //---------------------------

    private static final String CLASS_NAME = FileUtilities.class.getName();

    //---------------------------
    // Constructors
    //---------------------------

    /**
     * Don't construct one of these, just use the static methods
     */
    protected FileUtilities() {
    }

    //---------------------------
    // Public Methods
    //---------------------------

    /**
     * Locate a file and return it as an input stream.  This is a standard 3 level check.
     * First we check to see if the given name is a system property.  If that isn't set
     * then we check for the file in the class path and if not found in the
     * users home directory.
     *
     * @param filename The name of the file to locate
     * @return InputStream The inputstream for the file.
     * @throws jeffaschenk.commons.exceptions.FileException if the file is not found
     */
    public static InputStream locateFile(String filename)
            throws FileException {

        InputStream inStream;

        try {
            inStream = new FileInputStream(locateFileAsFile(filename));
        } catch (FileNotFoundException e) {
            String[] arguments = new String[]{filename};
            throw new FileException(MessageConstants.COMMON_UTILITIES_FILE_NOT_FOUND, arguments, e);
        }

        return inStream;
    }

    /**
     * Locate a file and return it as a file.  This is a standard 3 level check.
     * First we check to see if the given name is a system property.  If that isn't set
     * then we check for the file in the class path and if not found in the
     * users home directory.
     *
     * @param filename The name of the file to locate
     * @return File The file itself.
     * @throws FileException if the file is not found
     */
    public static File locateFileAsFile(String filename)
            throws FileException {

        final String METHODNAME = "locateFileAsFile";

        // first step is to locate the file via a system property
        File file = null;
        try {
            String filePropertyValue = System.getProperty(filename);
            if (filePropertyValue != null) {
                file = new File(filePropertyValue);
            }
        } catch (Exception e) {
            String[] arguments = new String[]{filename};
            FrameworkLogger.log(CLASS_NAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.COMMON_UTILITIES_PROPERTY_SET_NO_FILE, arguments, e);
            file = null;
        }

        // if we didn't get the file via a system property, try the classpath
        if (file == null) {
            URL url = FileUtilities.class.getResource("/" + filename);
            if (url != null) {
                try {
                    String path = URLDecoder.decode(url.getPath(), "UTF-8");
                    file = new File(path);
                } catch (UnsupportedEncodingException e) {
                    String[] args = new String[]{url.getPath()};
                    FrameworkLogger.log(CLASS_NAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                            MessageConstants.COMMON_UTILITIES_ERROR_DECODING_FILE_URL, args, e);
                }
            }
            if (file == null) {

                // not in the classpath, try the home directory
                try {
                    String homeFilePath =
                            System.getProperty("user.home") + File.separator + filename;
                    file = new File(homeFilePath);

                    //  Test the file to make sure it is there
                    if (!file.exists()) {
                        throw new FileNotFoundException(filename);
                    }
                } catch (Exception e) {
                    String[] arguments = new String[]{filename};
                    FrameworkLogger.log(CLASS_NAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                            MessageConstants.COMMON_UTILITIES_FILE_NOT_IN_CLASSPATH, arguments, e);
                    file = null;
                }
            }
        }

        // if no input stream throw an exception
        if (file == null) {
            String[] arguments = new String[]{filename};
            throw new FileException(MessageConstants.COMMON_UTILITIES_FILE_NOT_FOUND, arguments);
        }

        // all is well, return the input stream
        return (file);
    }
}
