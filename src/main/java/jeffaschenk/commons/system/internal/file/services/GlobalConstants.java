package jeffaschenk.commons.system.internal.file.services;

/**
 * Global Constants
 *
 * @author jeffaschenk@gmail.com
 *
 */
public interface GlobalConstants {

    public static final String NEWLINE = System.getProperty("line.separator");

    public static final int READER_BUFFER_SIZE = ((1024*1024)*128); // 128MB Buffer.

    public static final long FILE_SLICE_THRESHOLD = ((1024*1024)*1); // 1MB Buffer.

    public static final long FILE_SLICE_SIZE = 100000; // Lines per Slice.

    public static final long MAXIMUM_FILE_SLICES = 99999;

    public static final String FILE_SLICE = "_SLICE";

    public static final String FILE_SLICED = "SLICED_";

    public static final String DEFAULT_GLOBAL_PIPE_CHARACTER = "|";

    public static final String EXTRACT_FILE_SUFFIX = ".txt";

    public static final String NEW_FILE_PREFIX = "NEW_";

    public static final String DEFAULT_GLOBAL_EXPORT_CHARACTER = "|";

    public static final String EXPORT_FILE_SUFFIX = ".csv";

    public static final String PHI_FILE_PREFIX = "PHI_AUTHORIZATION_FILE";

    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "ConstraintViolationException";

    public static final String CAUSED_BY_BATCH_UP_EXCEPTION = "java.sql.BatchUpdateException:";

    public static final String FOR_KEY = "for key";

    public final static String CRON_ELEMENT_SEPARATORS = "-,/";

    public static final long DEFAULT_EXTRACT_EXECUTION_TIME_WINDOW = ((1000*60)*60); // 60 Minutes in Millis

}
