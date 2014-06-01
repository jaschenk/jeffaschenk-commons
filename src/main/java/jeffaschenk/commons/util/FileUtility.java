package jeffaschenk.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Simple File Utility
 *
 * @author jeffaschenk@gmail.com
 */
public class FileUtility {

    /**
     * Fix a PHI Authorization File.
     *
     * This file contains large textual fields, which can have embedded NewLines
     * which cause our common Line Parse to not detect fields correctly.
     *
     * This method fixes those issues and write a secondary file to be used to process.
     *
     * @param existingPHIFile
     * @param newPHIFile
     * @throws java.io.IOException
     */
    public static void fixPHI_AUTHORIZATION_FILE(File existingPHIFile, File newPHIFile) throws IOException {
               if (existingPHIFile == null)
               {
                   throw new IllegalArgumentException("Existing PHI File was not specified.");
               }
               if (!existingPHIFile.canRead())
               {
                   throw new IllegalArgumentException("Unable to read Existing PHI File:["+existingPHIFile+"]");
               }
                if (newPHIFile == null)
               {
                   throw new IllegalArgumentException("Temporary PHI File was not specified.");
               }
               // Fix
               FileInputStream input_reader = new FileInputStream(existingPHIFile);
               FileOutputStream output_writer = new FileOutputStream(newPHIFile);
               int numberOfPipes = 0;
               while (input_reader.available() > 0) {
                   byte[] bytes = new byte[8192];
                   int chunk_size = input_reader.read(bytes);

                   for (int i = 0; i < chunk_size; i++) {
                       if (bytes[i] == 0x0A) // Carriage Return
                       {
                           if (numberOfPipes >= 3) {
                               numberOfPipes = 0;
                           }
                           output_writer.write(bytes[i]);
                       } else if (bytes[i] == 0x0D) // NewLine
                       {
                           output_writer.write(0x20);
                       } else if (bytes[i] == 0x7C) {
                           output_writer.write(bytes[i]);
                           numberOfPipes++;

                       } else {
                           output_writer.write(bytes[i]);
                       }
                   }
               }
               input_reader.close();
               output_writer.flush();
               output_writer.close();
       }



}
