package jeffaschenk.commons.util;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;

/**
 * Zip File Utility Class
 *
 * @author jeffaschenk@gmail.com
 */
public class ArchiveUtility {

    /**
     * Ensure Class is not instantiated.
     */
    private ArchiveUtility() {
    }

    /**
     * Determine if the file is an Archive?
     *
     * @param file
     * @return boolean
     */
    public static boolean isArchive(File file) {
        if ((file.getName().toLowerCase().endsWith(".zip")) ||
                (file.getName().toLowerCase().endsWith(".tar")) ||
                (file.getName().toLowerCase().endsWith("gz"))) {
            return true;
        }
        return false;
    }
    
    /**
     * Decompress Archive File
     *
     * @param file
     * @param outputDirectory
     * @return boolean
     * @throws java.io.IOException
     */
    public static boolean decompressArchiveFile(File file, File outputDirectory) throws IOException {
        if ((!file.exists()) || (!file.canRead())) {
            throw new IllegalAccessError("Compressed File:[" + file.getName() +
                    "] is not Accessible!");
        }
        if ((!outputDirectory.exists()) || (!outputDirectory.canWrite()) || (!outputDirectory.isDirectory())) {
            throw new IllegalAccessError("Output Directory:[" + outputDirectory.getAbsolutePath() +
                    "] is not Accessible, or not a valid File System Directory!");
        }
        // Check for Zip Type
        if (file.getName().toLowerCase().endsWith(".zip")) {
            ZipFile zipFile = new ZipFile(file);
            try {
            // Obtain the Entries in the Zip.
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                extractZipEntry(zipFile, entries.nextElement(), outputDirectory);
            }
            return true;
            } finally {
                zipFile.close();
            }
            // Check for a Tar Type
        } else if (file.getName().toLowerCase().endsWith(".tar")) {
            TarArchiveInputStream tarBallInputStream = null;
            try {
                tarBallInputStream =
                        (TarArchiveInputStream)
                                new ArchiveStreamFactory().createArchiveInputStream("tar", new FileInputStream(file));
                TarArchiveEntry entry = null;
                // Loop Through Entries in TarBall
                while ((entry = (TarArchiveEntry) tarBallInputStream.getNextEntry()) != null) {
                    extractTarBallEntry(tarBallInputStream, entry, outputDirectory);
                }
                return true;
            } catch (ArchiveException ae) {
                throw new RuntimeException("Encountered an Archive Exception:[" + ae.getMessage() + "]", ae);
            } finally {
                tarBallInputStream.close();
            }
            // Check for a GZip Type
        } else if (file.getName().toLowerCase().endsWith("gz")) {
            File uncompressedFile = unGzip(file, outputDirectory);
            decompressArchiveFile(uncompressedFile, outputDirectory);
            return true;
        } else {
            throw new IllegalArgumentException("Compressed File:[" + file.getName() +
                    "] Archive Type in unknown to this Utility Function!");
        }
    }


    /**
     * Private Helper Method to Perform Extract of Compressed File with the Zip.
     *
     * @param zipFile
     * @param entry
     * @param outputDirectory
     * @throws java.io.IOException
     */
    private static void extractZipEntry(ZipFile zipFile, ZipArchiveEntry entry, File outputDirectory)
            throws IOException {

        BufferedInputStream content = new BufferedInputStream(zipFile.getInputStream(entry), 16384);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputDirectory + File.separator + entry.getName()));
        try {
            final byte[] buffer = new byte[16384];
            int n = 0;
            while (-1 != (n = content.read(buffer))) {
                fileOutputStream.write(buffer, 0, n);
            }

        } finally {
            content.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

    /**
     * Private Helper Method to Perform Extract for a Tarball Entry.
     *
     * @param tarArchiveInputStream
     * @param entry
     * @param outputDirectory
     * @throws java.io.IOException
     */
    private static void extractTarBallEntry(TarArchiveInputStream tarArchiveInputStream, TarArchiveEntry entry, File outputDirectory)
            throws IOException {
        final File outputFile = new File(outputDirectory, entry.getName());
        if (entry.isDirectory()) {
            if (!outputFile.exists()) {
                if (!outputFile.mkdirs()) {
                    throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
                }
            }
        } else {
            final OutputStream outputFileStream = new FileOutputStream(outputFile);
            IOUtils.copy(tarArchiveInputStream, outputFileStream);
            outputFileStream.close();
        }
    }

    /**
     * Ungzip an input file into an output file.
     * <p>
     * The output file is created in the output folder, having the same name
     * as the input file, minus the '.gz' extension.
     *
     * @param compressedInputFile     the input .gz file
     * @param outputDir     the output directory file.
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     *
     * @return File with the ungzipped content.
     */
    private static File unGzip(final File compressedInputFile, final File outputDir) throws IOException {
        String uncompressedOutputFileName = null;
        if (compressedInputFile.getName().endsWith(".gz"))
        {
            uncompressedOutputFileName =
                    compressedInputFile.getName().substring(0, compressedInputFile.getName().length() - 3);
        } else if (compressedInputFile.getName().endsWith(".tgz"))
        {
            uncompressedOutputFileName =
                    compressedInputFile.getName().substring(0, compressedInputFile.getName().length() - 3) +
                            "tar";
        } else {
            throw new IllegalArgumentException("Compressed File:[" + compressedInputFile.getName() +
                    "] Archive Type in unknown to this Utility Function!");
        }

        // Un-Compress.
        final File outputFile = new File(outputDir, uncompressedOutputFileName);

        final GZIPInputStream in = new GZIPInputStream(new FileInputStream(compressedInputFile));
        final FileOutputStream out = new FileOutputStream(outputFile);

        for (int c = in.read(); c != -1; c = in.read()) {
            out.write(c);
        }

        in.close();
        out.close();

        return outputFile;
    }


}
