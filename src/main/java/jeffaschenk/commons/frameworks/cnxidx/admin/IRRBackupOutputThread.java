package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;

import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * IRR Backup Output Thread.  Will Read Directory Entry and
 * backup the data to an LDIF compilent Data file.
 * Backup output conforms to the LDIF Specification: RFC2849.

 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

/**
 * IRRBackupOutputThread
 * Class to run Output Thread.
 */
class IRRBackupOutputThread implements Runnable, idxCMDReturnCodes {

    /**
     * IRRBackupOutputThread
     * Class to provide Output Thread to Write LDIF from Entries obtain via an
     * Object Stack.
     */
    Thread t;

    private CircularObjectStack cosin;

    private IRRBackupStatusNew WriterStatus;

    private static String MP = "IRRBackupOutputThread: ";

    private static String ENTRY_SOURCE_DN = null;

    private static String OUTPUT_FILENAME = null;
    private static int DN_SEGMENTATION_OUTPUT = 0;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean APPEND = false;
    private static boolean NICE = true;

    private static int OBUFSIZE = 0;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    /**
     * IRRBackupOutputThread Contructor class driven.
     *
     * @param cosin           Circular Object Stack for placing DN's on Read Queue.
     * @param WriterStatus    Object used for Thread Status.
     * @param ENTRY_SOURCE_DN Specified where to begin Backup in Tree.
     * @param OUTPUT_FILENAME          Destination Output Filename.
     * @param OBUFSIZE             BufferedWriter Output Buffer Size.
     * @param APPEND         Indicate if Output Should be Appended.
     * @param NICE           Indicate if Output Should be nicely formatted.
     * @param DN_SEGMENTATION_OUTPUT Indicates if Output should be segmented by DN.
     * @param VERBOSE         Indicate Verbosity.
     * @param DEBUG         Indicate DEBUGGING.
     * @param ExitOnException         Indicate Exit on Exceptions.
     */
    IRRBackupOutputThread(CircularObjectStack cosin,
                          IRRBackupStatusNew WriterStatus,
                          String ENTRY_SOURCE_DN,
                          String OUTPUT_FILENAME,
                          int OBUFSIZE,
                          boolean APPEND,
                          boolean NICE,
                          int DN_SEGMENTATION_OUTPUT,
                          boolean VERBOSE,
                          boolean DEBUG,
                          boolean ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        this.cosin = cosin;
        this.ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        this.OUTPUT_FILENAME = OUTPUT_FILENAME;
        this.OBUFSIZE = OBUFSIZE;
        this.APPEND = APPEND;
        this.NICE = NICE;
        this.DN_SEGMENTATION_OUTPUT
                = DN_SEGMENTATION_OUTPUT;
        this.VERBOSE = VERBOSE;
        this.DEBUG = DEBUG;
        this.ExitOnException = ExitOnException;
        this.WriterStatus = WriterStatus;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        t = new Thread(this, "IRRbackup_LDIFWriter");
        t.start(); // Start the Thread
    } // End of Contructor.

    /**
     * run
     * Thread to Output LDIF Backup Image.
     */
    public void run() {

        // ***********************************************
        // Initialize our StopWatch to measure Duration
        // of Thread.
        StopWatch sw = new StopWatch();
        sw.start();

        // ***********************************************
        // Initialize Thread Variables.
        Object ZEN = null;
        IRRBackupSearchResult searchresultwrapper = null;
        long DNcount = 0;
        long SEGMENT_DNcount = 0;
        long SEGMENT = 0;
        long memfree;
        String tname = Thread.currentThread().getName();
        System.out.println(MP + "LDIF Output Thread Established for:[" + tname + "]");

        // ***********************************************
        // Initialize my LAP Timers
        idxLapTime LP_ENTRY_TO_LDIF = new idxLapTime();
        idxLapTime LP_ENTRY_FROM_COS = new idxLapTime();

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        // ******************************************
        // Prepare to Open up the File Output Stream.
        System.out.println(MP + "Preparing Backup Output File...");
        if (OBUFSIZE <= 0) {
            OBUFSIZE = 131072;
        } // 128KB Buffer.
        System.out.println(MP + "Backup Output File Buffer Size:[" + OBUFSIZE + "]");

        // ********************************************
        // Open Output Stream and Initialize.
        BufferedWriter LDIFOUT = null;
        try {
            LDIFOUT = this.openOutput(OUTPUT_FILENAME, APPEND, OBUFSIZE, SEGMENT);
        } catch (Exception e) {
            System.err.println(MP + "Exception opening Backup LDIF Output File. " + e);
            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
            return; // End Thread.
        } // End of exception

        // **************************************
        // Loop to process commands from Walker
        // Thread.
        try {
            while (true) {
                searchresultwrapper = null;
                ZEN = null;
                LP_ENTRY_FROM_COS.Start();
                if (cosin.hasMoreNodes()) {
                    ZEN = cosin.getNext();
                }
                LP_ENTRY_FROM_COS.Stop();

                // ***************************
                // Did anything get pulled
                // from stack?
                if (ZEN == null) {
                    t.sleep(1000);
                    continue;
                } // End of Nothing in Stack yet to Process.

                // ***************************
                // Should We End Thread?
                if (ZEN instanceof String) {
                    // ***************************
                    // Should We End Thread?
                    if (IRRBackupNew.END_OF_DATA.equals((String) ZEN)) {
                        break;
                    }

                    // *****************************
                    // Is the Entry a LDIF Comment?
                    // Or a Simple NewLine?
                    if ((((String) ZEN).startsWith("#")) ||
                            (((String) ZEN).equals("\n"))) {
                        try {
                            LDIFOUT.write((String) ZEN);
                            continue;
                        } catch (Exception e) {
                            System.err.println(MP + "Exception Writing to Backup LDIF Output File. " + e);
                            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
                            return; // End Thread.
                        } // End of exception
                    } // End of If.		
                } // End of Check for String.
                else if (!(ZEN instanceof IRRBackupSearchResult)) {
                    continue;
                }
                // ***********************************
                // Ok, this must be a SearchResult, so
                // Process.
                //
                searchresultwrapper = (IRRBackupSearchResult) ZEN; // Cast to Appropreiate Object.
                if (searchresultwrapper == null) {
                    continue;
                }

                LP_ENTRY_TO_LDIF.Start();
                String DN = searchresultwrapper.searchresult.getName();
                if ((DN == null) ||
                        (DN.trim().equalsIgnoreCase(""))) {
                    DN = searchresultwrapper.DistinguishedName;
                }

                // ******************************************
                // Write out the DN.
                // Do not write out a JNDI Quoted DN.
                // That is not LDIF Compliant.
                //
                idxParseDN pDN = new idxParseDN(DN);
                DNcount++;
                if (NICE) {
                    LDIFOUT.write("dn: ");
                    if (pDN.isQuoted()) {
                        LDIFOUT.write(pDN.getDN());
                    } else {
                        LDIFOUT.write(DN);
                    }

                    LDIFOUT.write("\n");
                } else {
                    if (pDN.isQuoted()) {
                        idxIRROutput.WriteLDIF("dn", pDN.getDN(), LDIFOUT);
                    } else {
                        idxIRROutput.WriteLDIF("dn", DN, LDIFOUT);
                    }
                } // End of DN Output.

                // Obtain the entries Attributes.
                Attributes entryattrs = searchresultwrapper.searchresult.getAttributes();

                // Obtain ObjectClass First.
                Attribute eo = entryattrs.get(IRRBackupReaderThread.ObjectClassName);
                for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                    idxIRROutput.WriteLDIF(eo.getID(), eov.next(), LDIFOUT);
                }

                // Obtain Naming Attribute Next.
                // One will not exist for an Alias.
                if (!"".equals(pDN.getNamingAttribute())) {
                    Attribute en = entryattrs.get(pDN.getNamingAttribute());
                    if (en != null) {
                        for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                            idxIRROutput.WriteLDIF(en.getID(), env.next(), LDIFOUT);
                        }
                    } // End of Inner If.
                } // End of Naming Attribute.

                // Finish Obtaining remaining Attributes,
                // in no special sequence.
                for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                    Attribute attr = (Attribute) ea.next();

                    if ((!IRRBackupReaderThread.ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                            (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                        for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                            idxIRROutput.WriteLDIF(attr.getID(), ev.next(), LDIFOUT);
                        }
                    } // End of If
                } // End of Outer For Loop

                idxIRROutput.WriteLDIF("", "", LDIFOUT);
                LP_ENTRY_TO_LDIF.Stop();
                if (LP_ENTRY_TO_LDIF.getCurrentDuration() > 1000) {
                    System.out.println(MP + "Warning ** Entry to LDIF took " +
                            LP_ENTRY_TO_LDIF.getElapsedtoString() +
                            " to Complete for:[" +
                            DN + "]");
                } // End of Warning Message due to time spent processing.

                // **********************************
                // Check here to Segment the Output
                // File based upon Number of
                // DN Entries.
                if (DN_SEGMENTATION_OUTPUT > 0) {
                    SEGMENT_DNcount++;
                    if (SEGMENT_DNcount >= DN_SEGMENTATION_OUTPUT) {
                        // ******************************
                        // Close off our Existing
                        // Output File.
                        this.closeOutput(LDIFOUT, SEGMENT_DNcount, SEGMENT, -1);

                        // ******************************
                        // Prepare a new FileName.
                        SEGMENT++;
                        SEGMENT_DNcount = 0;
                        LDIFOUT = this.openOutput(OUTPUT_FILENAME, APPEND, OBUFSIZE, SEGMENT);
                    } // End of Check for Making an Output Segmentation.
                } // End of Segmentation Check.

            } // End of Outer While Loop.
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception on IRRbackup, Performing Output Data Loop, " + e);
            return; // End Thread.
        } // End of exception.        

        // ***************************************
        // Show number of entries backed up.
        if (DNcount > 0) {
            System.out.println(MP + "Successful Backup, Entries Included In Backup:[" +
                    DNcount + "], Output Segments:[" + (SEGMENT + 1) + "].");
        } else {
            System.out.println(MP + "No Entries were Included in Backup:[" +
                    DNcount + "]");
        } // End of Else.

        // ***************************************
        // Close our Output File/Last Segment.
        try {
            this.closeOutput(LDIFOUT, SEGMENT_DNcount, SEGMENT, DNcount);
        } catch (Exception e) {
            System.err.println(MP + "Exception closing Output File. " + e);
            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_CLOSE_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Show the Lap Timings.
        System.out.println(MP + "Lap Time for Entry to LDIF Output: " + LP_ENTRY_TO_LDIF);
        System.out.println(MP + "Lap Time for Stack Communication: " + LP_ENTRY_FROM_COS);

        // ***************************************
        // Show the Duration of Thread.
        sw.stop();
        System.out.println(MP + "Thread Duration: " + sw.getElapsedTimeString());

        // ***************************************
        // Done.
        return;

    } // End of run.

    /*
     * Private method to Open up Output Writer.
     */
    private BufferedWriter openOutput(String outputfilename,
                                      boolean append,
                                      int outputbufsize,
                                      long segment)
            throws Exception {

        // **********************************
        // Prepare Filename.
        String filename = outputfilename;
        if (!filename.toLowerCase().endsWith(".ldif")) {
            filename = filename + ".ldif";
        }

        // **********************************
        // Determine if we have a new Segment
        if (segment > 0) {
            filename = filename + ".segment_" + this.formatSegment(segment);
        } // End of Check for Segmentation File Name Setup.

        // **********************************
        // Create a File Object.
        File outputfile = new File(filename);

        // **********************************
        // Open File.
        BufferedWriter LDIFOUT = new BufferedWriter(
                new FileWriter(outputfile, append), outputbufsize);

        // **********************************
        // Create Header Information.
        if (!APPEND) {
            LDIFOUT.write("version: 1\n");
        }

        LDIFOUT.write("# ***********************************************\n");
        LDIFOUT.write("# FRAMEWORK IRR Directory LDIF Backup.\n");
        if (ENTRY_SOURCE_DN.equals("")) {
            LDIFOUT.write("# Source DN: [" + "ROOT" + "]\n");
        } else {
            LDIFOUT.write("# Source DN: [" + ENTRY_SOURCE_DN + "]\n");
        }
        LDIFOUT.write("# Start Time: " + CurrentTimeStamp.get() + "\n");
        if (segment > 0) {
            LDIFOUT.write("# Backup Segment: [" + this.formatSegment(segment) + "]\n");
        }
        LDIFOUT.write("# ***********************************************\n");
        LDIFOUT.write("\n");

        // ****************************************
        // Return BufferedWriter.
        return LDIFOUT;
    } // End of openOutput Private Method.

    /*
     * Private method to Open up Output Writer.
     */
    private void closeOutput(BufferedWriter LDIFOUT,
                             long dncount, long segment, long finalcount)
            throws Exception {

        // ***************************************
        // Show number of entries backed up.
        try {
            LDIFOUT.write("# **************************************************************\n");
            LDIFOUT.write("# End of IRR Backup LDIF Segment\n");
            LDIFOUT.write("# End Time: " + CurrentTimeStamp.get() + "\n");
            if (segment > 0) {
                LDIFOUT.write("# Segment Number: " + this.formatSegment(segment) + "\n");
                LDIFOUT.write("# Entries Contained in this LDIF Backup Segment:[" +
                        dncount + "]\n");
            } // End of Check to Show Segment Size.
            if (finalcount >= 0) {
                LDIFOUT.write("# Total Entries Contained in All Backup LDIF Segments:[" +
                        finalcount + "]\n");
            } // End of Check to Show Final Count.
            LDIFOUT.write("# **************************************************************\n");
            LDIFOUT.write("\n");
        } catch (Exception e) {
            System.err.println(MP + "Exception closing Backup LDIF Output File. " + e);
            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
            throw e; // Rethrow Exception.
        } // End of exception

        // ***************************************
        // Close our Output File.
        try {
            LDIFOUT.flush();
            LDIFOUT.close();
        } catch (Exception e) {
            System.err.println(MP + "Exception closing Output File. " + e);
            WriterStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_CLOSE_FAILURE);
            throw e; // Rethrow Exception.
        } // End of exception
    } // End of closeOutput Private Method.

    /*
     * Private Method to format the Segment Number into a String.
     */
    private String formatSegment(long segment) {
        String segstr = Long.toString(segment);
        for (int i = 0; i <= 7 && segstr.length() <= 7; i++) {
            segstr = "0" + segstr;
        }
        return segstr;
    } // End of formatSegment Private method.

} ///:~ End of Class IRRBackupOutputThread
