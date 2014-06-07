package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to Restore the Entire, a portion (container) or even
 * a single entry and optionally all of its children to
 * the IRR Directory from a LDIF format input file on disk.
 * Backup input conforms to the LDIF Specification: RFC2849.
 * However the Input Change Log fil must have been produced from the
 * IRRChangeLogger utility.
 * <br>
 * <b>Usage:</b><br>
 * IRRChangeLogRestore &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
 * <br>
 * <b>Required Parameters are:</b>
 * <pre>
 * --hosturl
 * 	Specify IRR(Directory) LDAP URL, ldap://hostname.acme.com
 * --irrid
 * 	Specify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa
 * --irrpw
 * 	Specify IRR(Directory) LDAP BIND Password
 * --idu
 * 	Specify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.
 * --infile
 * 	Specify Full Input file Path of Change Log to be read.
 * </pre>
 * <b>Optional Parameters are:</b>
 * <pre>
 * --jar
 * 	Specify LDIF infile specifies a JAR path.
 * --restoreonlyDN
 * 	Specify to restore only current DN.
 * --withchildren
 * 	Specify to restore specified restoreonlyDN and all it's children.
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */


public class IRRChangeLogRestore implements idxCMDReturnCodes {

    public static String VERSION = "Version: 3.0 2003-09-12, " +
            "FRAMEWORK, Incorporated.";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRChangeLogRestore.class.getName();
    public static idxLogger IDXLOG = new idxLogger();

    public static String MP = CLASSNAME + ": ";

    // ****************************
    // Runtime Statistics
    private int entries_exceptions = 0;
    private int entries_skipped = 0;
    private int entries_processed = 0;
    private int entries_modified = 0;
    private int entries_renamed = 0;
    private int entries_deleted = 0;
    private int entries_added = 0;

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRChangeLogRestore <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");
        System.err.println(MP + "--infile ");
        System.err.println("\tSpecify Full Input file Path of Change Logfile to be read and restored.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--jar ");
        System.err.println("\tSpecify LDIF Input is located in a JAR.");

        System.err.println(MP + "--restoreonlyDN ");
        System.err.println("\tSpecify to restore only current DN.");

        System.err.println(MP + "--withchildren ");
        System.err.println("\tSpecify to restore specified restoreonlyDN and all it's children.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass

    /**
     * IRRChangeLogRestore Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRChangeLogRestore() {
    } // End of Constructor for IRRChangeLogRestore.

    /**
     * clearStats Method resets all statistics for this utility classs.
     */
    public void clearStats() {
        entries_exceptions = 0;
        entries_skipped = 0;
        entries_processed = 0;
        entries_modified = 0;
        entries_renamed = 0;
        entries_deleted = 0;
        entries_added = 0;
    } // End of clearStats Method.

    /**
     * obtainStats Method to obtain all statistics for this utility classs.
     */
    public int[] obtainStats() {
        int[] stats = new int[7];
        stats[0] = entries_exceptions;
        stats[1] = entries_skipped;
        stats[2] = entries_processed;
        stats[3] = entries_modified;
        stats[4] = entries_renamed;
        stats[5] = entries_deleted;
        stats[6] = entries_added;
        return (stats);
    } // End of clearStats Method.

    /**
     * dumpStats Method displays all statistics for this utility.
     */
    public void dumpStats() {
        String METHODNAME = "dumpStats";
        IDXLOG.info(CLASSNAME, METHODNAME, "Entries Processed: [" + entries_processed + "]");
        IDXLOG.info(CLASSNAME, METHODNAME, "Entry Exceptions: [" + entries_exceptions + "]");
        IDXLOG.info(CLASSNAME, METHODNAME, "Entries Skipped: [" + entries_skipped + "]");
        IDXLOG.info(CLASSNAME, METHODNAME, "Entries Added: [" + entries_added + "]");
        IDXLOG.info(CLASSNAME, METHODNAME, "Entries Deleted: [" + entries_deleted + "]");
        IDXLOG.info(CLASSNAME, METHODNAME, "Entries Renamed: [" + entries_renamed + "]");
        IDXLOG.info(CLASSNAME, METHODNAME, "Entries Modified: [" + entries_modified + "]");
    } // End of dumpStats Method.

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform(DirContext irrctx, String INPUT_FILENAME)
            throws Exception, idxIRRException {
        perform(irrctx, INPUT_FILENAME, "", false, false, false);
    } // End of Perform Method.                    

    /**
     * perform Method class performs the requested IRR Function Utility.
     *
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform(DirContext irrctx,
                        String INPUT_FILENAME,
                        String RESTOREONLY_SOURCEDN,
                        boolean INJAR,
                        boolean RESTOREONLY_WITH_CHILDREN,
                        boolean RESTOREONLY)
            throws Exception, idxIRRException {

        String METHODNAME = "perform";
        IDXLOG.finer(CLASSNAME, METHODNAME, "Entering to Restore LDIF from Input File:[" +
                INPUT_FILENAME + "].");

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // *******************************************
        // Initialize.
        CompoundName RESTOREONLY_Name = null;
        CompoundName Incoming_Name = null;
        CompoundName Control_Name = null;

        // *******************************************
        // Acquire a Name Parser.
        idxNameParser myParser = new idxNameParser();

        // ********************************************
        // Create a Compound Name for our SourceDN
        //
        if (RESTOREONLY) {
            try {
                // ***********************************************
                // Now determine if SourceDN is Valid.
                idxParseDN zSdn = new idxParseDN(RESTOREONLY_SOURCEDN);
                if (!zSdn.isValid()) {
                    IDXLOG.severe(CLASSNAME, METHODNAME, "Restore Only Source DN [" +
                            RESTOREONLY_SOURCEDN +
                            "] is Invalid, unable to continue.");

                    throw new idxIRRException(MP + "Restore Only Source DN [" +
                            RESTOREONLY_SOURCEDN +
                            "] is Invalid, unable to continue.",
                            EXIT_IRR_RESTORE_FAILURE);
                } // End of If.

                RESTOREONLY_Name = myParser.parse(RESTOREONLY_SOURCEDN);

            } catch (Exception e) {
                IDXLOG.severe(CLASSNAME, METHODNAME, "Restore Only Source DN [" +
                        RESTOREONLY_SOURCEDN +
                        "] is Invalid, unable to continue.");

                throw new idxIRRException(MP + "Restore Only Source DN [" +
                        RESTOREONLY_SOURCEDN +
                        "] is Invalid, unable to continue.",
                        EXIT_IRR_RESTORE_FAILURE);
            } // End of exception
        } // End of If.

        // *****************************************
        // Open Input File and Start Restore Process
        if (INJAR) {
            IDXLOG.finer(CLASSNAME, METHODNAME, "Starting Directory Restore using LDIF JAR Class:[" + INPUT_FILENAME + "]");
        } else {
            IDXLOG.finer(CLASSNAME, METHODNAME, "Starting Directory Restore using LDIF Input File:[" + INPUT_FILENAME + "]");
        } // End of Else.

        // *****************************************
        // Initialize.
        BufferedReader LDIFIN = null;
        clearStats();

        Attributes current_entry_attributes = null;
        String current_dn = null;
        String LDIFVersion = null;

        String IDXCHANGETYPE = null;
        String IDXCHANGETYPEDN = null;
        String IDXCHANGETYPEOLDDN = null;

        boolean IDXCHANGEBYPASS = false;

        // *****************************************
        // Open up Input.
        try {
            if (INJAR) {
                // ******************************************
                // Open up the Input Stream from a JAR.
                LDIFIN = new BufferedReader(
                        new InputStreamReader(
                                ClassLoader.getSystemResourceAsStream(INPUT_FILENAME)), 16384);
            } else {

                // ******************************************
                // Open up the Input Stream from a File.
                LDIFIN = new BufferedReader(
                        new FileReader(INPUT_FILENAME), 16384);

            } // End of Else.

            // ******************************************
            // Obtain our Reader Instance with our Input.
            idxChangeLogLDIFReader ldif = new idxChangeLogLDIFReader(LDIFIN);

            Incoming_Name = null;
            while (ldif.hasMore()) {

                current_entry_attributes = ldif.getNextEntry();
                current_dn = ldif.getCurrentDN();

                if ((current_dn == null) ||
                        (current_dn.equals(""))) {
                    continue;
                }

                // *************************************
                // Detect the Version, if possible.
                if ((LDIFVersion == null) ||
                        (LDIFVersion.equals(""))) {
                    LDIFVersion = ldif.getVersion();
                    if ((LDIFVersion != null) &&
                            (!LDIFVersion.equals(""))) {
                        IDXLOG.finer(CLASSNAME, METHODNAME, "LDIF VERSION Detected:[" +
                                LDIFVersion + "]");
                    }
                } // End of LDIF Version Detection.

                // *********************************************
                // Obtain Change Control Information for Entry.
                //
                Attribute myattr = current_entry_attributes.get("IDXCHANGETYPE");
                if (myattr != null) {
                    Object myValue = myattr.get();
                    IDXCHANGETYPE = ((String) myValue).trim();
                    current_entry_attributes.remove("IDXCHANGETYPE");

                    // *******************************
                    // Obtain the DN.
                    myattr = current_entry_attributes.get("IDXCHANGETYPEDN");
                    if (myattr == null) {
                        IDXCHANGETYPEOLDDN = null;
                    } else {
                        myValue = myattr.get();
                        IDXCHANGETYPEDN = ((String) myValue).trim();
                        current_entry_attributes.remove("IDXCHANGETYPEDN");
                    } // End of Else.

                    // *******************************
                    // Obtain the Old DN if Available.
                    myattr = current_entry_attributes.get("IDXCHANGETYPEOLDDN");
                    if (myattr == null) {
                        IDXCHANGETYPEOLDDN = null;
                    } else {
                        myValue = myattr.get();
                        IDXCHANGETYPEOLDDN = ((String) myValue).trim();
                        current_entry_attributes.remove("IDXCHANGETYPEOLDDN");
                    } // End of Else.

                    IDXLOG.finer(CLASSNAME, METHODNAME, "Change Type Detected:[" + IDXCHANGETYPE + "], " +
                            "DN:[" + IDXCHANGETYPEDN + "], " +
                            "Old DN:[" + IDXCHANGETYPEOLDDN + "]");

                    // **********************************************
                    // Now Determine if the Entry should be processed
                    //
                    IDXCHANGEBYPASS = false;

                    if ((IDXCHANGETYPE == null) ||
                            (IDXCHANGETYPEDN == null)) {
                        IDXCHANGEBYPASS = true;
                        IDXLOG.warning(CLASSNAME, METHODNAME, "Bypassing Invalid Change Type Detected:[" + IDXCHANGETYPE + "], " +
                                "DN:[" + IDXCHANGETYPEDN + "], " +
                                "Old DN:[" + IDXCHANGETYPEOLDDN + "]");
                        continue;
                    } // End of If.

                    if ((IDXCHANGETYPE.equalsIgnoreCase("RENAME")) &&
                            ((IDXCHANGETYPEOLDDN == null) || (IDXCHANGETYPEOLDDN.equals("")))) {
                        IDXCHANGEBYPASS = true;
                        IDXLOG.warning(CLASSNAME, METHODNAME, "Bypassing Invalid Rename Change:[" + IDXCHANGETYPE + "], " +
                                "DN:[" + IDXCHANGETYPEDN + "], " +
                                "Old DN:[" + IDXCHANGETYPEOLDDN + "]");
                        continue;
                    } // End of If.


                    // ***************************************
                    // If we have a RestoreOnlyDN Specified
                    // filter out the ones we do not need.
                    //
                    if (RESTOREONLY) {

                        // ************************************
                        // If we have a rename operation,
                        // make sure we use the old dn, as that is
                        // what will be effected from the change.
                        //
                        String ctDN = IDXCHANGETYPEDN;
                        if (IDXCHANGETYPE.equalsIgnoreCase("RENAME")) {
                            ctDN = IDXCHANGETYPEOLDDN;
                        }

                        try {
                            Incoming_Name = myParser.parse(ctDN);
                        } catch (Exception e) {
                            IDXLOG.severe(CLASSNAME, METHODNAME, "Exception Creating incoming Control Compound Name for DN:[" +
                                    ctDN + "]" + e);
                            entries_exceptions++;
                            IDXCHANGEBYPASS = true;
                            continue;
                        } // End of exception

                        // ****************************************************
                        // If No Children, Compare Incoming and the RestoreOnly
                        if (!RESTOREONLY_WITH_CHILDREN) {
                            if (!Incoming_Name.equals(RESTOREONLY_Name)) {
                                entries_skipped++;
                                IDXLOG.finer(CLASSNAME, METHODNAME, "Skipping DN-> " + ctDN);

                                // **************************************
                                // Clear out the Operation Data.
                                IDXCHANGEBYPASS = true;
                                IDXCHANGETYPE = null;
                                IDXCHANGETYPEDN = null;
                                IDXCHANGETYPEOLDDN = null;

                                continue;
                            }

                        } else {
                            // **********************************************************
                            // If Children, Compare Incoming has a suffix of RestoreOnly
                            if (!Incoming_Name.endsWith(RESTOREONLY_Name)) {
                                entries_skipped++;
                                IDXLOG.finer(CLASSNAME, METHODNAME, "Skipping DN-> " + ctDN);

                                // **************************************
                                // Clear out the Operation Data.
                                IDXCHANGEBYPASS = true;
                                IDXCHANGETYPE = null;
                                IDXCHANGETYPEDN = null;
                                IDXCHANGETYPEOLDDN = null;

                                continue;
                            } // End of If.
                        } // End of Else.
                    } // End of If RestoreOnly


                    // ********************************************
                    // Only Exception, is that all Deletes happen
                    // now, since there may or may not be a
                    // Entry.
                    //
                    if ((!IDXCHANGEBYPASS) &&
                            (IDXCHANGETYPE.equalsIgnoreCase("DELETE"))) {
                        entries_processed++;
                        IDXLOG.finer(CLASSNAME, METHODNAME, "Processing Delete of DN-> " + IDXCHANGETYPEDN);
                        try {
                            irrctx.unbind("\042" + IDXCHANGETYPEDN + "\042");
                            entries_deleted++;
                            IDXLOG.finer(CLASSNAME, METHODNAME, "Deletion of DN, Successful.");
                        } catch (Exception e) {
                            IDXLOG.warning(CLASSNAME, METHODNAME, "Deletion Exception for DN:[" +
                                    IDXCHANGETYPEDN + "],\n" + e);
                            entries_exceptions++;
                        } // End of Exception.
                    } // End of Delete Process.

                    continue; // Get Next LDIF Entry.
                } // End of If for Control Information.

                // ***************************************
                // Now Perform Operation on the
                // Change Log Entry, basically play the
                // Operation.
                //
                // Skip the bypasses and the Deletes.
                //
                if ((IDXCHANGETYPE == null) ||
                        (IDXCHANGETYPEDN == null)) {
                    IDXLOG.warning(CLASSNAME, METHODNAME, "ChangeType and/or Entry DN is invalid, skipping.");
                    entries_exceptions++;
                    continue;
                } // End of If.


                if ((IDXCHANGETYPE.equalsIgnoreCase("DELETE"))) {
                    continue;
                }

                entries_processed++;
                if (IDXCHANGEBYPASS) {
                    continue;
                }

                // ***************************************
                // First make sure our control DN equals
                // the control DN for consistency.
                //
                Incoming_Name = myParser.parse(current_dn);
                Control_Name = myParser.parse(IDXCHANGETYPEDN);
                if (!Incoming_Name.equals(Control_Name)) {
                    IDXLOG.warning(CLASSNAME, METHODNAME, "Control DN and Entry DN do not Match," +
                            "\n   Control DN:[" +
                            IDXCHANGETYPEDN + "]," +
                            "\n     Entry DN:[" +
                            current_dn + "],");
                    entries_exceptions++;
                    continue;
                } // End of If.

                // ***************************************
                // Now Determine what operation needs
                // to be performed.
                //

                // ADD
                if (IDXCHANGETYPE.equalsIgnoreCase("ADD")) {
                    IDXLOG.finer(CLASSNAME, METHODNAME, "Processing Add of DN-> " + IDXCHANGETYPEDN);
                    try {
                        irrctx.bind("\042" + current_dn + "\042",
                                null, current_entry_attributes);
                        entries_added++;
                        IDXLOG.finer(CLASSNAME, METHODNAME, "Add of DN, Successful.");
                    } catch (Exception e) {
                        IDXLOG.warning(CLASSNAME, METHODNAME, "Add Exception for DN:[" +
                                IDXCHANGETYPEDN + "],\n" + e);
                        entries_exceptions++;
                    } // End of Exception.
                } // End of Add Process.

                // MODIFY
                else if (IDXCHANGETYPE.equalsIgnoreCase("MODIFY")) {
                    IDXLOG.finer(CLASSNAME, METHODNAME, "Processing Modification of DN-> " + IDXCHANGETYPEDN);
                    try {
                        // ***********************************************
                        // Now remove ObjectClass from the Attributes.
                        current_entry_attributes.remove("objectclass");

                        // ***********************************************
                        // Now remove he Internal Replication Attribute.
                        current_entry_attributes.remove("cnxidaguid");

                        // ***********************************************
                        // Now remove the Naming Attribute as Well.
                        idxParseDN zXdn = new idxParseDN(IDXCHANGETYPEDN);
                        current_entry_attributes.remove(zXdn.getNamingAttribute());

                        if (current_entry_attributes.size() > 0) {

                            // ********************************************
                            // Attempt to remove any attributes on the
                            // existing source entry which do not exist in
                            // our current journal entry.
                            try {
                                RemoveNonExistentAttributes(irrctx, current_dn,
                                        current_entry_attributes);
                            } catch (Exception e) {
                                IDXLOG.warning(CLASSNAME, METHODNAME, "Removal of NonExistentAttributes Exception for DN:[" +
                                        IDXCHANGETYPEDN + "],\n" + e);
                                entries_exceptions++;
                            } // End of Exception.

                            // ********************************************
                            // Now Attempt to perform the modication.
                            irrctx.modifyAttributes("\042" + current_dn + "\042",
                                    irrctx.REPLACE_ATTRIBUTE, current_entry_attributes);
                            entries_modified++;
                            IDXLOG.finer(CLASSNAME, METHODNAME, "Modification of DN, Successful.");
                        } else {
                            entries_skipped++;
                            IDXLOG.finer(CLASSNAME, METHODNAME, "Modification of DN, Skipped.");
                        } // End of Else.

                    } catch (Exception e) {
                        IDXLOG.severe(CLASSNAME, METHODNAME, "Modification Exception for DN:[" +
                                IDXCHANGETYPEDN + "],\n" + e);
                        entries_exceptions++;
                    } // End of Exception.
                } // End of Modify Process.

                // RENAME
                else if (IDXCHANGETYPE.equalsIgnoreCase("RENAME")) {
                    IDXLOG.finer(CLASSNAME, METHODNAME, "Processing Rename of Old DN-> " + IDXCHANGETYPEOLDDN);
                    try {
                        irrctx.rename("\042" + IDXCHANGETYPEOLDDN + "\042",
                                "\042" + current_dn + "\042");
                        entries_renamed++;
                        IDXLOG.finer(CLASSNAME, METHODNAME, "Rename of DN, Successful.");

                        // ************************************
                        // Now that the Entry has been renamed,
                        // simply perform a modification
                        // to ensure attributes are in a
                        // consistent state.
                        //

                        // ***********************************************
                        // Now remove ObjectClass from the Attributes.
                        current_entry_attributes.remove("objectclass");

                        // ***********************************************
                        // Now remove he Internal Replication Attribute.
                        current_entry_attributes.remove("cnxidaguid");

                        // ***********************************************
                        // Now remove the Naming Attribute as Well.
                        idxParseDN zXdn = new idxParseDN(current_dn);
                        current_entry_attributes.remove(zXdn.getNamingAttribute());

                        if (current_entry_attributes.size() > 0) {
                            // ********************************************
                            // Attempt to remove any attributes on the
                            // existing source entry which do not exist in
                            // our current journal entry.
                            try {
                                RemoveNonExistentAttributes(irrctx, current_dn,
                                        current_entry_attributes);
                            } catch (Exception e) {
                                IDXLOG.warning(CLASSNAME, METHODNAME, "Removal of NonExistentAttributes Exception for DN:[" +
                                        IDXCHANGETYPEDN + "],\n" + e);
                                entries_exceptions++;
                            } // End of Exception.

                            // ********************************************
                            // Now Attempt to perform the modication.
                            irrctx.modifyAttributes("\042" + current_dn + "\042",
                                    irrctx.REPLACE_ATTRIBUTE, current_entry_attributes);
                            entries_modified++;
                            IDXLOG.finer(CLASSNAME, METHODNAME, "Modification of DN, Successful.");
                        } else {
                            IDXLOG.finer(CLASSNAME, METHODNAME, "Modification of DN, Skipped.");
                        } // End of Else.

                    } catch (NameNotFoundException nnfe) {
                        // ********************************************************
                        // Name Not Found condition
                        // Indicates that the oldDN Name does not exist any longer,
                        // so attempt to add the entry with the new DN.
                        //
                        IDXLOG.warning(CLASSNAME, METHODNAME, "Attempting Add of DN->" +
                                IDXCHANGETYPEDN +
                                ", since RENAME resulted in a Not Found for the Old DN.");
                        try {
                            irrctx.bind("\042" + current_dn + "\042",
                                    null, current_entry_attributes);
                            entries_added++;
                            IDXLOG.finer(CLASSNAME, METHODNAME, "Add of DN, Successful.");
                        } catch (Exception e) {
                            IDXLOG.warning(CLASSNAME, METHODNAME, "Add Exception for DN:[" +
                                    IDXCHANGETYPEDN + "],\n" + e);
                            entries_exceptions++;
                        } // End of Exception.

                    } catch (Exception e) {
                        IDXLOG.warning(CLASSNAME, METHODNAME, "Rename Exception for DN:[" +
                                IDXCHANGETYPEOLDDN + "],\n" + e);
                        entries_exceptions++;
                    } // End of Exception.
                } // End of Rename Process.

                // UNKNOWN
                else {
                    IDXLOG.warning(CLASSNAME, METHODNAME, "Unknown Change Type Encountered:[" +
                            IDXCHANGETYPE + "] for DN:[" + IDXCHANGETYPEDN + "].");
                    entries_exceptions++;
                } // End of Else.

            } // End of While Loop.

        } catch (Exception e) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "Exception during processing  LDIF Input." + e);
            throw new idxIRRException(MP + "Exception during processing LDIF Input. " + e,
                    EXIT_IRR_RESTORE_LDIF_INPUT_FAILURE);
        } // End of exception


        // ***************************************
        // Close our Input File.
        try {
            LDIFIN.close();
        } catch (Exception e) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "Exception closing LDIF Input." + e);
            throw new idxIRRException("Exception closing LDIF Input. " + e,
                    EXIT_IRR_RESTORE_LDIF_INPUT_CLOSE_FAILURE);
        } // End of exception

        // ****************************************
        // Any Entries Restored?
        if (entries_processed == 0) {
            IDXLOG.warning(CLASSNAME, METHODNAME, "No Entries were Processed.");
        } // End of If.

        // ****************************************    	
        // Show the Statistics
        dumpStats();

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Show Restore Timings.
        IDXLOG.finer(CLASSNAME, METHODNAME, "Driven Restore Complete, Elapsed Time: " + elt.getElapsed());

    } // End of Perform Method.


    /**
     * Removes any Attributes which do not exist in the current
     * Entry about to be restored.  This provides the capability to remove
     * Attributes which have been removed during a modification.
     *
     * @param ctxSource current established Source JNDI Directory Context
     * @param DNSource     DN of source entry.
     * @param NEW_ENTRYATTRS of current entry.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    private void RemoveNonExistentAttributes(DirContext ctxSource, String DNSource,
                                             Attributes NEW_ENTRYATTRS)
            throws idxIRRException, NamingException {

        String METHODNAME = "RemoveNonExistentAttributes";
        // *************************************************
        // Parse the DN to Obtain the DN's Naming Attribute.
        idxParseDN xDN = new idxParseDN(DNSource);

        // *************************************
        // Now obtain the Existing Entry's
        // Attributes.
        //
        Attributes CURRENT_ENTRYATTRS = null;
        try {
            CURRENT_ENTRYATTRS = ObtainExistingAttributes(ctxSource, DNSource);
        } catch (Exception e) {
            IDXLOG.severe(CLASSNAME, METHODNAME, "Exception Performing RemoveNonExistentAttributes for Entry:[" +
                    DNSource + "], " + e);

            throw new idxIRRException("Exception Performing RemoveNonExistentAttributes for Entry:[" +
                    DNSource + "], " + e);
        } // End of Exception.

        if (CURRENT_ENTRYATTRS == null) {

            IDXLOG.info(CLASSNAME, METHODNAME, "Existing Attributes not obtained for Entry:[" +
                    DNSource + "]");
            return;
        }

        // ******************************************
        // Now spin through the existing attributes
        // and compare existenance against our
        // current set we will be modifying.
        //
        for (NamingEnumeration eaids = CURRENT_ENTRYATTRS.getIDs(); eaids.hasMore(); ) {
            String CURRENT_ATTRID = (String) eaids.next();

            // ***********************************************
            // Now check for certain attributes that will not
            // and should not change.
            if ((CURRENT_ATTRID.equalsIgnoreCase("objectclass")) ||
                    (CURRENT_ATTRID.equalsIgnoreCase("cnxidaguid")) ||
                    (CURRENT_ATTRID.equalsIgnoreCase(xDN.getNamingAttribute()))) {
                continue;
            }

            // ****************************************
            // If the Attribute does not exist in our
            // new attribute set, then remove the
            // attribute from the entry on the
            // Directory Server.
            Attribute NEW_ATTR = NEW_ENTRYATTRS.get(CURRENT_ATTRID);
            if (NEW_ATTR == null) {
                RemoveAttribute(ctxSource, DNSource, CURRENT_ATTRID);
            }

        } // End of For Loop.

        // *************************************
        // Return.
        return;
    } // End of RemoveNonExistentAttributes

    /**
     * Removes a Attribute from a Directory Entry.
     *
     * @param ctxSource current established Source JNDI Directory Context
     * @param DNSource     current DN of Entry which is to be removed.
     * @param AttributeName     current Attribute Name to be removed.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    private void RemoveAttribute(DirContext ctxSource,
                                 String DNSource,
                                 String AttributeName)
            throws idxIRRException {

        try {
            ModificationItem[] irrmods = new ModificationItem[1];
            irrmods[0] = new ModificationItem(
                    ctxSource.REMOVE_ATTRIBUTE,
                    new BasicAttribute(AttributeName));

            ctxSource.modifyAttributes(DNSource, irrmods);

        } catch (NoSuchAttributeException nsae) {
            return;
        } catch (Exception e) {
            throw new idxIRRException("Exception Performing IRR Removal of Attribute[" +
                    AttributeName + "], from Entry[" +
                    DNSource + "],\n" + e);
        } // End of Exception.
    } ///: End of RemoveAttribute.

    /**
     * Obtains Entry from Directory Context and returns Attributes to
     * Calling class method.
     *
     * @param ctxSource current established Source JNDI Directory Context
     * @param DNSource     DN of source entry.
     * @return Attributes Enumeration of all Attributes for this entry.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    private Attributes ObtainExistingAttributes(DirContext ctxSource, String DNSource)
            throws idxIRRException {

        //******************************************
        // Make sure we have a valid DN.
        // If the Source Entry is Blank or caller
        // trying to obtain ROOT, just ignore and
        // return gracefully.
        //
        if (("".equals(DNSource)) ||
                (DNSource == null)) {
            return (null);
        }

        //******************************************
        // Set up our Search Filter.
        String SearchFilter = "(objectclass=*)";

        //******************************************
        // Set up our Search Controls.
        String[] ALL_AttrIDs = {"*"};
        SearchControls OS_ctls = new SearchControls();
        OS_ctls.setReturningAttributes(ALL_AttrIDs);
        OS_ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(DNSource);
        DNSource = Naming_Source.getDNwithQuotes();

        // *****************************************
        // Obtain the Namespace.
        String NameSpace = null;
        try {
            NameSpace = ctxSource.getNameInNamespace();
            if (NameSpace.equals("")) {
                NameSpace = DNSource;
            }
        } catch (Exception e) {
            {
                NameSpace = DNSource;
            }
        } // End of exception

        // *****************************************
        // Now obtain the Source Entry.
        //
        Attributes entryattrs = null;
        try {
            NamingEnumeration sl =
                    ctxSource.search(DNSource, SearchFilter, OS_ctls);

            // ******************************
            // If nothing return.
            if (sl == null) {
                return (null);
            }

            // ******************************
            // Loop, only will have one entry
            // but loop to close off
            // Enumeration.
            while (sl.hasMore()) {
                SearchResult si = (SearchResult) sl.next();
                // Obtain Attributes
                entryattrs = si.getAttributes();
            } // End of While Loop

        } catch (NameNotFoundException e) {
            return (null);
        } // End of exception
        catch (Exception e) {
            throw new idxIRRException("Error Performing Method ObtainExistingAttributes() on Entry:[" +
                    DNSource + "],\n" + e);
        } // End of exception

        // *************************************
        // Return the Entries Attributes.
        return (entryattrs);
    } // End of ObtainExistingAttributes

    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRChangeLogRestore
     * @see IRRChangeLogger
     */
    public static void main(String[] args) {

        long starttime, endtime;

        // ****************************************
        // Local Objects
        idxManageContext IRRDest = null;
        String IRRHost = null;
        String IRRPrincipal = null;
        String IRRCredentials = null;

        String INPUT_FILENAME = null;
        String RESTOREONLY_SOURCEDN = null;

        boolean INJAR = false;
        boolean RESTOREONLY_WITH_CHILDREN = false;
        boolean RESTOREONLY = false;
        boolean VERBOSE = false;

        // ****************************************
        // Send the Greeting.
        System.out.println(MP + VERSION);

        // ****************************************
        // Parse the incoming Arguments and
        // create objects for each entity.
        //
        idxArgParser Zin = new idxArgParser();
        Zin.parse(args);

        // ***************************************
        // Do I have any unnamed Values?
        if (!Zin.IsUnNamedEmpty()) {
            System.out.println(MP + "Unknown Values Encountered, Terminating Process.");
            Zin.showUnNamed();
            Usage();
        } // End of If.

        // ***************************************
        // Was Version Info Requested?
        if (Zin.doesNameExist("version")) {
            System.exit(EXIT_VERSION);
        }

        // ***************************************
        // Was Help Info Requested?
        if (Zin.doesNameExist("?")) {
            Usage();
        }

        // ***************************************
        // Was Verbosity Requested?
        if (Zin.doesNameExist("verbose")) {
            VERBOSE = true;
        }

        // ***************************************
        // Show Arguments if Verbose Selected.
        if (VERBOSE) {
            Zin.show();
        }

        // ***************************************
        // Build our verification Rule Set.
        //
        // idxArgVerificationRules Parameters are:
        // String Name of argument name.
        // Boolean Required Argument Indicator.
        // Boolean StringObject Argument Indicator.
        // String Name of Value Verification Routine.
        //
        LinkedList<idxArgVerificationRules> VAR = new LinkedList<>();

        VAR.add(new idxArgVerificationRules("hosturl",
                true, true));

        VAR.add(new idxArgVerificationRules("irrid",
                false, true));

        VAR.add(new idxArgVerificationRules("irrpw",
                false, true));

        VAR.add(new idxArgVerificationRules("idu",
                false, true));

        VAR.add(new idxArgVerificationRules("infile",
                true, true));

        VAR.add(new idxArgVerificationRules("restoreonlydn",
                false, true));

        VAR.add(new idxArgVerificationRules("withchildren",
                false, false));

        VAR.add(new idxArgVerificationRules("jar",
                false, false));

        VAR.add(new idxArgVerificationRules("noschemacheck",
                false, false));

        VAR.add(new idxArgVerificationRules("schemacheck",
                false, false));

        VAR.add(new idxArgVerificationRules("verbose",
                false, false));

        // ***************************************
        // Run the Verification Rule Set.
        // If we do not have a positive return,
        // then an invalid argument was detected,
        // so show Usage and die. 
        //
        idxArgVerifier AV = new idxArgVerifier();
        AV.setVerbose(VERBOSE);
        if (!AV.Verify(MP, Zin, VAR)) {
            Usage();
        }

        // ***************************************
        // Obtain Authentication Principal and
        // Credentials from the KeyStore or
        // the command line.
        //
        CommandLinePrincipalCredentials clPC =
                new CommandLinePrincipalCredentials(Zin);

        // **************************************************
        // Load up the Principal/Credentials.
        //
        if (clPC.wasObtained()) {
            IRRPrincipal = clPC.getPrincipal();
            System.out.println(MP + "IRR ID:[" + IRRPrincipal + "]");

            IRRCredentials = clPC.getCredentials();
            //System.out.println(MP+"IRR Password:["+IRRCredentials+"]");
        } else {
            System.out.println(MP + "Required Principal and Credentials not Specified, unable to continue.");
            Usage();
        } // End of Else.

        // *****************************************
        // For all Specified Boolean indicators,
        // set them appropreiately.
        //
        if (Zin.doesNameExist("withchildren")) {
            RESTOREONLY_WITH_CHILDREN = true;
        }

        if (Zin.doesNameExist("jar")) {
            INJAR = true;
        }

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        INPUT_FILENAME = ((String) Zin.getValue("infile")).trim();
        if (!INJAR) {
            System.out.println(MP + "Input File:[" + INPUT_FILENAME + "]");
        } else {
            System.out.println(MP + "Input JAR Class:[" + INPUT_FILENAME + "]");
        } // End of Else.

        if (Zin.doesNameExist("restoreonlydn")) {
            RESTOREONLY_SOURCEDN = ((String) Zin.getValue("restoreonlydn")).trim();
        }

        // ************************************************
        // Show Operational Parameters
        if ((RESTOREONLY_SOURCEDN == null) ||
                (RESTOREONLY_SOURCEDN.equals("")) ||
                (RESTOREONLY_SOURCEDN.equals("*"))) {
            System.out.println(MP + "All Input entries will be Restored.");
            RESTOREONLY_SOURCEDN = "";
            RESTOREONLY = false;
            RESTOREONLY_WITH_CHILDREN = false;
        } else {
            System.out.println(MP + "Only DN: [" + RESTOREONLY_SOURCEDN + "] " +
                    "will be restored from the LDIF Input Source.");
            RESTOREONLY = true;
            if (RESTOREONLY_WITH_CHILDREN) {
                System.out.println(MP + "Children will be restored for above DN.");
            }
        } // End of Else.

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Destination Directory Connection to Host URL:[" + IRRHost + "]");

        IRRDest = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "Restore Destination");

        // ************************************************
        // Exit on all Exceptions.
        IRRDest.setExitOnException(true);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRDest.open();
        } catch (Exception e) {
            System.err.println(MP + "Error Opening Directory Context: " + e);
            System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
        } // End of exception

        // *****************************************
        // Disable the Factories.
        try {
            IRRDest.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + "Error Disabling DSAE Factories: " + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of exception  


        // ****************************************
        // Initailize Constructor.
        IRRChangeLogRestore FUNCTION = new IRRChangeLogRestore();

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform(IRRDest.irrctx,
                    INPUT_FILENAME,
                    RESTOREONLY_SOURCEDN,
                    INJAR,
                    RESTOREONLY_WITH_CHILDREN,
                    RESTOREONLY);
        } catch (idxIRRException ire) {
            System.err.println(MP + " " + ire.getMessage());
            System.exit(ire.getRC());
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRChangeLogRestore.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Destination Directory Context.");
        try {
            IRRDest.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(EXIT_IRR_CLOSE_FAILURE);
        } // End of exception

        // ****************************************
        // Show the Statistics
        FUNCTION.dumpStats();

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRChangeLogRestore
