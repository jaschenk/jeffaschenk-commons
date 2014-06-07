package jeffaschenk.commons.frameworks.cnxidx.utility;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.regex.*;

/**
 * Java Class to provide low level File Reader Capabilities for
 * clear-text ASCII Files.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2001-2003
 */

public class idxFileReader {

    // Charset and decoder for ISO-8859-15
    private static Charset charset = Charset.forName("ISO-8859-15");
    private static CharsetDecoder decoder = charset.newDecoder();

    // Pattern used to parse lines
    private static Pattern linePattern
            = Pattern.compile(".*\r?\n");

    // Counters.
    private int lines = 0;
    private int bytes = 0;

    /**
     * Initial Constructor.
     */
    public idxFileReader() {
    } // End of Constructor.

    /**
     * get Lines Counted.
     */
    public int getLineCount() {
        return (lines);
    } // End of getLineCount.

    /**
     * get Bytes Counted.
     */
    public int getByteCount() {
        return (bytes);
    } // End of getByteCount.


    /**
     * Simply Counts the number of lines in a file, using standard IO.
     */
    public void countLines(String INPUT_FILENAME) throws IOException {

        String inputline = null;
        lines = 0;
        bytes = 0;
        boolean hasMore = true;

        // *******************************************
        // Open the file.
        BufferedReader myin = new BufferedReader(
                new FileReader(INPUT_FILENAME), 16384);

        // *******************************************
        // Process the Incoming Data.
        while (hasMore) {
            // *********************************************
            // Read an Input Line.
            // Force the rest of the data out, if we reach EOF
            // before a End of Entry deliminter.
            inputline = myin.readLine();
            if (inputline == null) {
                inputline = "";
                hasMore = false;
                continue;
            } // End of inputline NULL Check.

            // ***************************
            // Count Bytes.
            bytes += inputline.length();

            // ***************************
            // Count the lines.
            lines++;

        } // End of While Loop.

        // ****************************
        // Close the File.
        myin.close();

        // ***************************
        // Return
        return;

    } // End of countLines Method

    /**
     * Simply Counts the number of lines in a file, using New IO.
     */
    public void countLinesNIO(CharBuffer cb) throws IOException {

        lines = 0;
        bytes = 0;
        Matcher lm = linePattern.matcher(cb);    // Line matcher

        while (lm.find()) {
            CharSequence cs = lm.group();    // The current line
            lines++;                            // Count the Lines.
            bytes += cs.length();               // Count the Bytes. 

            if (lm.end() == cb.limit()) {
                break;
            }
        } // End of While Loop.

        // ***************************
        // Return 
        return;

    } // End of countLines method.

    /**
     * Simply Counts the number of lines in a file, using New IO.
     */
    public void countLinesNIO(String INPUT_FILENAME) throws IOException {

        // Open the file and then get a channel from the stream
        File f = new File(INPUT_FILENAME);
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();

        // Get the file's size and then map it into memory
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

        // Decode the file into a char buffer
        CharBuffer cb = decoder.decode(bb);

        // Perform the search
        countLinesNIO(cb);

        // Close the channel and the stream
        fc.close();

        // ***********************
        // Return.
        return;

    } // End of countLines Method.

    /**
     * main to provide command line capability.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java idxFileReader file...");
            return;
        }

        // ************************
        // Create an Elapsed Timer.
        idxElapsedTime elt = new idxElapsedTime();

        // ****************************************
        // Obtain a file reader.
        idxFileReader ifr = new idxFileReader();

        // *****************************
        // Loop through files specified.
        for (int i = 0; i < args.length; i++) {
            File f = new File(args[i]);
            try {

                // ****************************************    	
                // Note The Start Time.
                elt.setStart();

                // *******************************
                // Perform Standard IO.
                ifr.countLines(args[i]);

                // ****************************************    	
                // Note The End Time.
                elt.setEnd();
                System.out.println("");
                System.out.println("File:[" + args[i] + "], Read using Standard IO, Elapsed Time: " + elt.getElapsed());
                System.out.println("Lines:[" + ifr.getLineCount() + "], Bytes:[" + ifr.getByteCount() + "].");

                // ****************************************    	
                // Note The Start Time.
                elt.setStart();

                // *******************************
                // Perform Standard IO.
                ifr.countLinesNIO(args[i]);

                // ****************************************    	
                // Note The End Time.
                elt.setEnd();
                System.out.println("");
                System.out.println("File:[" + args[i] + "], Read using Standard New IO, Elapsed Time: " + elt.getElapsed());
                System.out.println("Lines:[" + ifr.getLineCount() + "], Bytes:[" + ifr.getByteCount() + "].");

            } catch (IOException x) {
                System.err.println(f + ": " + x);
            }
        } // End of For Loop.
    } // End of Main.


} ///:~ End of idxFileReader Class
