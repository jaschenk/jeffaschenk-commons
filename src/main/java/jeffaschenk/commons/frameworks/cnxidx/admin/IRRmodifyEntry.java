package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.CommandLinePrincipalCredentials;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgParser;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerificationRules;
import jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments.idxArgVerifier;
import jeffaschenk.commons.frameworks.cnxidx.utility.idxLDIFReader;
import jeffaschenk.commons.frameworks.cnxidx.utility.idxLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import java.util.logging.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Command line utility, driven from properties and command
 * line parameters to modify an existing entry within the Directory.
 * <br>
 * <b>Usage:</b><br>
 * IRRmodifyEntry &lt;Required Parameters&gt; &lt;Optional Parameters&gt;
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
 * --verbose
 * 	Specify Additional Logging Information.
 * --version
 * 	Display Version information and exit.
 * --?
 * 	This Display.
 *
 * </pre>
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2002
 */

public class IRRmodifyEntry implements idxCMDReturnCodes {

    private static String VERSION = "Version: 3.0 2003-09-12, " +
            "FRAMEWORK, Incorporated.";

    // *******************************
    // Common Logging Facility.
    public static final String CLASSNAME = IRRmodifyEntry.class.getName();
    public static idxLogger IDXLOG = new idxLogger();
    private static String MP = CLASSNAME + ": ";

    // *******************************
    // Modification DN and LinkedList
    String CURRENT_MODDN = "";
    LinkedList<ModificationItem> CURRENT_MODLIST = new LinkedList<>();

    // *******************************
    // Constants.
    private static final String LDIF_ADD_CHANGETYPE = "add";
    private static final String LDIF_DELETE_CHANGETYPE = "delete";
    private static final String LDIF_MODRDN_CHANGETYPE = "modrdn";
    private static final String LDIF_MODDN_CHANGETYPE = "moddn";
    private static final String LDIF_MODIFY_CHANGETYPE = "modify";

    private static final String LDIF_ATTRIBUTE_DELETE = "delete";
    private static final String LDIF_ATTRIBUTE_REPLACE = "replace";
    private static final String LDIF_ATTRIBUTE_ADD = "add";

    private static final String NEWRDN = "newrdn";
    private static final String DELETEOLDRDN = "deleteoldrdn";
    private static final String NEWSUPERIOR = "newsuperior";

    private static final String CONTROL = "control";
    private static final String TREE_DELETE_CONTROL = "1.2.840.113556.1.4.805";

    // ****************************
    // Runtime Statistics
    private int entries_exceptions = 0;
    private int entries_skipped = 0;
    private int entries_processed = 0;
    private int entries_modified = 0;
    private int entries_renamed = 0;
    private int entries_deleted = 0;
    private int entries_added = 0;

    // ****************************
    // Attribute Statistics
    private int total_attributes_modified = 0;
    private int total_attributes_deleted = 0;
    private int total_attributes_added = 0;

    private int attributes_modified = 0;
    private int attributes_deleted = 0;
    private int attributes_added = 0;

    // Action status - we may need to try updates more than once
    // NOTE: this value remaining false will cause infinate looping.
    private boolean isLdapUpdate = false;  // set true if we don't get certain types of errors

    /**
     * Usage
     * Class to print Usage parameters and simple exit.
     */
    static void Usage() {

        System.err.println(MP + "Usage:");
        System.err.println(MP + "IRRmodifyEntry <Required Parameters> <Optional Parameters>");

        System.err.println("\n" + MP + "Required Parameters are:");

        System.err.println(MP + "--hosturl ");
        System.err.println("\tSpecify Source IRR(Directory) LDAP URL, ldap://hostname.acme.com");
        System.err.println(MP + "--irrid ");
        System.err.println("\tSpecify Source IRR(Directory) LDAP BIND DN, cn=irradmin,o=icosdsa");
        System.err.println(MP + "--irrpw ");
        System.err.println("\tSpecify Source IRR(Directory) LDAP BIND Password");
        System.err.println(MP + "--idu ");
        System.err.println("\tSpecify FRAMEWORK Keystore Alias to obtain IRRID and IRRPW.");

        System.err.println(MP + "--infile ");
        System.err.println("\tSpecify Full File System Path of LDIF Modification Entry.");

        System.err.println("\n" + MP + "Optional Parameters are:");

        System.err.println(MP + "--verbose");
        System.err.println("\tSpecify Additional Logging Information.");

        System.err.println(MP + "--version");
        System.err.println("\tDisplay Version information and exit.");

        System.err.println(MP + "--?");
        System.err.println("\tThe Above Display.");

        System.exit(EXIT_USAGE);

    } // End of Subclass


    /**
     * IRRmodifyEntry Contructor class driven from
     * Main or other Class Caller.
     */
    public IRRmodifyEntry() {
    } // End of Constructor for IRRmodifyEntry.

    /**
     * Set the correct Message Prefix for this instance of the Function Utility.
     *
     * @param _mp Name of Message Prefix.
     */
    public void setMP(String _mp) {
        if (_mp != null) {
            MP = _mp + ": ";
        }
    } // End of setMP Method.

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
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entries Processed: [" + entries_processed + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entry Exceptions: [" + entries_exceptions + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entries Skipped: [" + entries_skipped + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entries Added: [" + entries_added + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entries Deleted: [" + entries_deleted + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entries Renamed: [" + entries_renamed + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Entries Modified: [" + entries_modified + "]");
    } // End of dumpStats Method.

    /**
     * dumpStats Method displays all statistics for this utility.
     */
    public void dumpAttributeStats() {
        String METHODNAME = "dumpAttributeStats";
        IDXLOG.fine(CLASSNAME, METHODNAME, "Total Attributes Added: [" + total_attributes_added + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Total Attributes Deleted: [" + total_attributes_deleted + "]");
        IDXLOG.fine(CLASSNAME, METHODNAME, "Total Attributes Modified: [" + total_attributes_modified + "]");
    } // End of dumpAttributeStats.

    /**
     * perform Method performs the requested IRR Function Utility.
     *
     * @param irrctx of Destination Directory where modifications are to be applied.
     * @param INPUT_FILENAME     Full Filename Path of Modification LDIF Structure.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    public void perform(DirContext irrctx,
                        String INPUT_FILENAME)
            throws Exception, idxIRRException {

        // ****************************************
        // Run a Modification Function.
        String METHODNAME = "perform";
        IDXLOG.info(CLASSNAME, METHODNAME, "Entering to perform Modification, Driven by FileName:[" +
                INPUT_FILENAME + "].");

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // *****************************************
        // Initialize.
        BufferedReader LDIFIN = null;

        Attributes current_entry_attributes = null;
        String current_dn = null;
        String LDIFVersion = null;

        String IDXCHANGETYPE = null;

        isLdapUpdate = false;

        // CPR07763
        // The remote host can go away - allow this to happen and not lose transactions.
        while (!isLdapUpdate) { // until this is set true, loop with a 10 minute delay
            // reuse the same file over again if needed.

            // *****************************************
            // Catch our Exceptions.
            try {

                // *****************************************
                // Open up the Input Stream from a File.
                LDIFIN = new BufferedReader(
                        new FileReader(INPUT_FILENAME), 16384);

                // ******************************************
                // Obtain our Reader Instance with our Input.
                idxLDIFReader ldif = new idxLDIFReader(LDIFIN);

                // ******************************************
                // Process the Entire Input Stream.
                while (ldif.hasMore()) {

                    current_entry_attributes = ldif.getNextModEntry();
                    current_dn = ldif.getCurrentDN();

                    // ****************************************************
                    // Check for a Pending Modification to Process.
                    if ((!CURRENT_MODLIST.isEmpty()) &&
                            (!CURRENT_MODDN.equalsIgnoreCase(current_dn))) {
                        process_pending_modification(irrctx);
                    }

                    // ****************************************************
                    // Skip, if nothing here, could be a seperator.
                    //
                    if ((current_dn == null) ||
                            (current_dn.equals("")) ||
                            (current_entry_attributes == null)) {
                        continue;
                    }

                    // ****************************************************
                    // Obtain Modification Control Information for Entry.
                    //
                    Attribute myattr = current_entry_attributes.get("changetype");
                    if (myattr != null) {
                        Object myValue = myattr.get();
                        IDXCHANGETYPE = ((String) myValue).trim();
                        current_entry_attributes.remove("changetype");

                        // ***************************************
                        // If we have a changetype and we have
                        // something pending, then that indicates that
                        // we need to process the new change before
                        // we move on.
                        //
                        if (!CURRENT_MODLIST.isEmpty()) {
                            process_pending_modification(irrctx);
                        }

                    } else if (!CURRENT_MODLIST.isEmpty()) {
                        // **************************************
                        // If I have a Pending MODLIST,
                        // Assume we are still in that Modification
                        // for the Entry.
                        IDXCHANGETYPE = LDIF_MODIFY_CHANGETYPE;
                    } else {
                        // **************************************
                        // Assume Changetype is always Add,
                        // if there are no pending Mods.
                        IDXCHANGETYPE = LDIF_ADD_CHANGETYPE;
                    } // End of Else.

                    // *************************************************
                    // Remove the DN not needed, from the Attribute set.
                    current_entry_attributes.remove("dn");

                    // ****************************************************
                    // Verify the Change Type.

                    isLdapUpdate = true; // allow it to fail in the following modules
                    // unless something sets the lsLdapUpdate = false, we should run like
                    // normal.

                    if (IDXCHANGETYPE.equalsIgnoreCase(LDIF_ADD_CHANGETYPE)) {
                        addEntry(irrctx, current_dn, current_entry_attributes);
                    } else if (IDXCHANGETYPE.equalsIgnoreCase(LDIF_DELETE_CHANGETYPE)) {
                        deleteEntry(irrctx, current_dn, current_entry_attributes);
                    } else if (IDXCHANGETYPE.equalsIgnoreCase(LDIF_MODIFY_CHANGETYPE)) {
                        modifyEntry(irrctx, current_dn, current_entry_attributes);
                    } else if ((IDXCHANGETYPE.equalsIgnoreCase(LDIF_MODRDN_CHANGETYPE)) ||
                            (IDXCHANGETYPE.equalsIgnoreCase(LDIF_MODDN_CHANGETYPE))) {
                        modDNEntry(irrctx, current_dn, current_entry_attributes);
                    }

                    // ****************************************************
                    // Falling through indicates we have an invalid
                    // Change Type, report it and continue.
                    //
                    else {
                        IDXLOG.warning(CLASSNAME, METHODNAME,
                                "Unknown Change Type Encountered:[" +
                                        IDXCHANGETYPE + "] for DN:[" + current_dn + "], Entry bypassed.");
                        entries_exceptions++;

                        // ********************************
                        // Ignore the Entry.
                        continue;
                    } // End of Else.

                } // End of LDIF Input While Loop.

                // ****************************************************
                // Check for a Pending Modification to Process.
                if (!CURRENT_MODLIST.isEmpty()) {
                    process_pending_modification(irrctx);
                }

            } catch (Exception e) {
                IDXLOG.severe(CLASSNAME, METHODNAME, "Exception during processing LDIF Input. " + e);
                e.printStackTrace();
                throw e;
            } // End of exception

            // ***************************************
            // Close our Input File.
            try {
                LDIFIN.close();
            } catch (Exception e) {
                IDXLOG.severe(CLASSNAME, METHODNAME, "Exception closing LDIF Input. " + e);
                e.printStackTrace();
                throw e;
            } // End of exception

            // should we re-try this again? - if isLdapUpdate = false, then we failed
            if (!isLdapUpdate) { // wait 10 minutes and try again
                // 10 minutes = 1000 * 60 * 10  milliseconds
                long waitTime = 1000 * 60 * 10;
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                }
            } else {
                break; // it did work.
            }
        } // close While Loop

        // ****************************************
        // Any Entries Restored?
        if (entries_processed == 0) {
            IDXLOG.warning(CLASSNAME, METHODNAME, "No Entries were Processed.");
        } // End of If.

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Show Modification Timings
        IDXLOG.info(CLASSNAME, METHODNAME, "Modification Complete using FileName:[" +
                INPUT_FILENAME + "], Elapsed Time: " + elt.getElapsed());
    } // End of Perform Method.

    /**
     * addEntry, Performs add of Entry.
     *
     * @param irrctx of Destination Directory where modifications are to be applied.
     * @param current_dn     Current DN.
     * @param current_entry_attributes Current set of Modification Attributes.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void addEntry(DirContext irrctx,
                          String current_dn,
                          Attributes current_entry_attributes)
            throws Exception, idxIRRException {

        String METHODNAME = "addEntry";
        IDXLOG.fine(CLASSNAME, METHODNAME, "Processing Add for DN:[" + current_dn + "].");
        if (IDXLOG.logger.isLoggable(Level.FINEST)) {
            showModification("ADD ENTRY WITH ATTRIBUTE", current_entry_attributes);
        } // End of Check for Logging.

        // ****************************
        // Count the Entries Processed
        entries_processed++;

        // ***********************************
        // Perform the Add/bind of the Entry.
        try {
            irrctx.bind(current_dn, null, current_entry_attributes);
            entries_added++;
            IDXLOG.fine(CLASSNAME, METHODNAME, "Entry Added for DN:[" + current_dn + "].");
            isLdapUpdate = true;
        } catch (NameAlreadyBoundException e) {
            IDXLOG.warning(CLASSNAME, METHODNAME, "DN Already Bound for add/bind operation, DN:[" +
                    current_dn + "], " + e);
            entries_exceptions++;
            isLdapUpdate = true;
        } catch (Exception e_bind) {
            IDXLOG.warning(CLASSNAME, METHODNAME, "Error Performing add/bind operation for DN:[" +
                    current_dn + "], " + e_bind);
            IDXLOG.warning(CLASSNAME, METHODNAME, "LDIF Sync function will wait 10 minutes and " +
                    "try [" + current_dn + "] again");

            isLdapUpdate = false;
            entries_exceptions++;

        } // End of Exception Processing.
    } // End of addEntry Method.

    /**
     * deleteEntry, Performs delete of Entry.
     *
     * @param irrctx of Destination Directory where modifications are to be applied.
     * @param current_dn     Current DN.
     * @param current_entry_attributes Current set of Modification Attributes.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void deleteEntry(DirContext irrctx,
                             String current_dn,
                             Attributes current_entry_attributes)
            throws Exception, idxIRRException {

        String METHODNAME = "deleteEntry";
        IDXLOG.fine(CLASSNAME, METHODNAME, "Processing Delete for DN:[" + current_dn + "].");
        if (IDXLOG.logger.isLoggable(Level.FINEST)) {
            showModification("DELETE ENTRY", current_entry_attributes);
        } // End of Check for Logging.

        // ****************************
        // Count the Entries Processed
        entries_processed++;

        // *******************************
        // Obtain Additional Values for
        // performing the rename.
        //
        Attribute eo = null;

        // ************************************
        // Eventually there is to be a
        // new function to perform a
        // tree delete control.
        // See RFC2849.
        //
        // Now check for that TREE Deletion
        // control.
        // ************************************
        boolean deletetree = false;
        String _control = (
                (eo = current_entry_attributes.get(CONTROL)) != null ? (String) eo.get() : null);
        if ((_control != null) &&
                (_control.startsWith(TREE_DELETE_CONTROL))) {
            _control = _control.trim().toLowerCase();
            if (_control.endsWith("true")) {
                deletetree = true;
            }
        } // End of If.

        // ***************************************
        // Perform a Tree Delete if Required
        // Based upon control.
        if (deletetree) {
            // ****************************************
            // Initailize Constructor.
            IRRdeleteEntry deleteFUNCTION = new IRRdeleteEntry();

            // ****************************************
            // Perform Function.
            try {
                deleteFUNCTION.perform(irrctx,
                        current_dn,
                        true,
                        false,
                        false);
                entries_deleted++;
                IDXLOG.fine(CLASSNAME, METHODNAME, "Entry and Tree Deleted for DN:[" + current_dn + "].");
                isLdapUpdate = true;
            } catch (Exception et) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "Error Performing delete/unbind on entire tree operation for DN:[" +
                        current_dn + "], " + et);
                IDXLOG.warning(CLASSNAME, METHODNAME, "LDIF Sync function will wait 10 minutes and " +
                        "try [" + current_dn + "] again");

                isLdapUpdate = false;
                entries_exceptions++;
            } // End of Exception Processing.
        } // End of Delete Tree.
        else {
            // *****************************
            // Perform the delete/unbind of
            // the single Entry.
            try {
                irrctx.unbind(current_dn);
                entries_deleted++;
                IDXLOG.fine(CLASSNAME, METHODNAME, "Entry Deleted for DN:[" + current_dn + "].");
                isLdapUpdate = true;
            } catch (NameNotFoundException e) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "DN not found for delete/unbind operation for DN:[" +
                        current_dn + "], " + e);
                isLdapUpdate = true;
                entries_exceptions++;
            } catch (Exception e_unbind) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "Error Performing delete/unbind operation for DN:[" +
                        current_dn + "], " + e_unbind);
                IDXLOG.warning(CLASSNAME, METHODNAME, "LDIF Sync function will wait 10 minutes and " +
                        "try [" + current_dn + "] again");
                isLdapUpdate = false;
                entries_exceptions++;
            } // End of Exception Processing.
        } // End of Else.
    } // End of deleteEntry Method.

    /**
     * modDNEntry, Performs Rename/MODRDN/MODDN of Entry.
     *
     * @param irrctx of Destination Directory where modifications are to be applied.
     * @param current_dn     Current DN.
     * @param current_entry_attributes Current set of Modification Attributes.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void modDNEntry(DirContext irrctx,
                            String current_dn,
                            Attributes current_entry_attributes)
            throws Exception, idxIRRException {


        String METHODNAME = "modDNEntry";
        IDXLOG.fine(CLASSNAME, METHODNAME, "Processing Rename for DN:[" + current_dn + "].");
        if (IDXLOG.logger.isLoggable(Level.FINEST)) {
            showModification("RENAME ENTRY", current_entry_attributes);
        } // End of Check for Logging.

        isLdapUpdate = true;

        // ****************************
        // Count the Entries Processed
        entries_processed++;

        // *******************************
        // Obtain Additional Values for
        // performing the rename.
        //
        Attribute eo = null;

        // *********************
        // New RDN.
        String _newrdn = (
                (eo = current_entry_attributes.get(NEWRDN)) != null ? (String) eo.get() : null);
        if ((_newrdn == null) ||
                (_newrdn.equalsIgnoreCase(""))) {
            IDXLOG.warning(CLASSNAME, METHODNAME, "No newrdn value specified for modrdn/rename operation for DN:[" +
                    current_dn + "].");
            entries_exceptions++;
            return;
        } // End of if.

        // *************************
        // Delete Old RDN Indicator.
        boolean deleteoldrdn = true;
        String _deleteoldrdn = (
                (eo = current_entry_attributes.get(DELETEOLDRDN)) != null ? (String) eo.get() : null);
        if ((_deleteoldrdn == null) ||
                (_deleteoldrdn.equalsIgnoreCase("0"))) {
            deleteoldrdn = false;
        }

        // *********************
        // New Superior.
        String _newsuperior = (
                (eo = current_entry_attributes.get(NEWSUPERIOR)) != null ? (String) eo.get() : null);

        // *********************
        // Formulate the new DN
        String _newDN = _newrdn;
        if ((_newsuperior != null) &&
                (!_newsuperior.equalsIgnoreCase(""))) {
            _newDN = _newDN + "," + _newsuperior;
        } else {
            idxParseDN zDN = new idxParseDN(current_dn);
            if (!zDN.isValid()) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "Existing DN value specified for modrdn/rename operation is Invalid, DN:[" +
                        current_dn + "].");
                entries_exceptions++;
                return;
            } // End of If.

            // ************************
            // Now Construct new DN.
            if (!zDN.getPDN().equalsIgnoreCase("")) {
                _newDN = _newDN + "," + zDN.getPDN();
            }
        } // End of Else.

        // ***********************************
        // Perform the ModDN/ModRDN/Rename
        // of the Entry.
        IDXLOG.fine(CLASSNAME, METHODNAME, "Formulated New DN:[" + _newDN + "].");

        // *************************
        // Determine if we have a
        // simple Rename or a Copy.
        if (deleteoldrdn) {
            // ***********************************
            // Perform the Add/bind of the Entry.
            try {
                irrctx.rename(current_dn, _newDN);
                entries_renamed++;
                IDXLOG.fine(CLASSNAME, METHODNAME, "Entry Renamed New DN:[" + _newDN + "].");
                isLdapUpdate = true;
            } catch (NameAlreadyBoundException e) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "DN Already Bound for modrdn/rename operation, Old DN:[" +
                        current_dn + "]," +
                        "New DN:[" +
                        _newDN + "], " + e);
                isLdapUpdate = true;
                entries_exceptions++;
            } catch (Exception e_bind) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "Error Performing modrdn/rename operation for Old DN:[" +
                        current_dn + "], " +
                        "New DN:[" +
                        _newDN + "], " + e_bind);
                isLdapUpdate = true;
                entries_exceptions++;
            } // End of Exception Processing.


            // ****************************
            // Since the delete old rdn
            // is false, we must perform
            // a copy from one container
            // to the other.
            //
        } else {
            // ****************************************
            // Initialize Constructor.
            IRRcopyEntry copyFUNCTION = new IRRcopyEntry();

            // ****************************************
            // Perform Function.
            try {
                copyFUNCTION.perform(irrctx,
                        current_dn,
                        irrctx,
                        _newDN,
                        false,
                        true,
                        false,
                        false);

                entries_renamed++;
                IDXLOG.fine(CLASSNAME, METHODNAME, "Entry Renamed New DN:[" + _newDN + "], leaving existing DN in place.");
                isLdapUpdate = true;
            } catch (Exception e_bind) {
                IDXLOG.warning(CLASSNAME, METHODNAME, "Error Performing modrdn/rename (copy) operation for Old DN:[" +
                        current_dn + "], " +
                        "New DN:[" +
                        _newDN + "], " + e_bind);
                isLdapUpdate = true;
                entries_exceptions++;
            } // End of Exception Processing.

        } // End of Else.

    } // End of modDNEntry Method.

    /**
     * modifyEntry, Performs Modifications on Entry.
     *
     * @param irrctx of Destination Directory where modifications are to be applied.
     * @param current_dn     Current DN.
     * @param current_entry_attributes Current set of Modification Attributes.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void modifyEntry(DirContext irrctx,
                             String current_dn,
                             Attributes current_entry_attributes)
            throws Exception, idxIRRException {

        String METHODNAME = "modifyEntry";

        isLdapUpdate = true; // never test for failure in this module, so assume it will get trapped elsewhere

        // *********************************
        // Only Show the Processing of the
        // Modify for the Entry once.
        if ((CURRENT_MODDN == null) ||
                (CURRENT_MODDN.equalsIgnoreCase(""))) {
            IDXLOG.fine(CLASSNAME, METHODNAME, "Processing Modify for DN:[" + current_dn + "].");
        }

        // ****************************************************
        // Ok, now we have a valid changetype for Entry,
        // now determine which attributes are added, replaced
        // or deleted.  Obtain all of the maintenance Contructs.
        //
        Attribute Attribute_Deletions = current_entry_attributes.get(LDIF_ATTRIBUTE_DELETE);
        current_entry_attributes.remove(LDIF_ATTRIBUTE_DELETE);
        Attributes Value_Deletions = obtainModifications(Attribute_Deletions, current_entry_attributes, true);

        Attribute Attribute_Replacements = current_entry_attributes.get(LDIF_ATTRIBUTE_REPLACE);
        current_entry_attributes.remove(LDIF_ATTRIBUTE_REPLACE);
        Attributes Value_Replacements = obtainModifications(Attribute_Replacements, current_entry_attributes, false);

        Attribute Attribute_Additions = current_entry_attributes.get(LDIF_ATTRIBUTE_ADD);
        current_entry_attributes.remove(LDIF_ATTRIBUTE_ADD);
        Attributes Value_Additions = obtainModifications(Attribute_Additions, current_entry_attributes, false);

        // *******************************************
        // Show Current DN and the Attributes.
        IDXLOG.finest(CLASSNAME, METHODNAME, "Queueing Modification Set for DN:[" + current_dn + "].");

        // *******************************************
        // Show Possible Attribute Deletions.
        if (Value_Deletions != null) {
            attributes_deleted = Value_Deletions.size();
            if (IDXLOG.logger.isLoggable(Level.FINEST)) {
                showModification("DELETE ATTRIBUTE", Value_Deletions);
            } // End of FINEST Check.
        } // End of If.

        // *******************************************
        // Show Possible Attribute Replacements.
        if (Value_Replacements != null) {
            attributes_modified = Value_Replacements.size();
            if (IDXLOG.logger.isLoggable(Level.FINEST)) {
                showModification("REPLACE ATTRIBUTE", Value_Replacements);
            } // End of Verbosity.
        } // End of If.

        // *******************************************
        // Show Possible Attribute Additions.
        if (Value_Additions != null) {
            attributes_added = Value_Additions.size();
            if (IDXLOG.logger.isLoggable(Level.FINEST)) {
                showModification("ADD ATTRIBUTE", Value_Additions);
            } // End of Verbosity.
        } // End of If.

        // *******************************************
        // Setup the Deletions.
        if (Value_Deletions != null) {
            CURRENT_MODDN = current_dn;
            setupModifications(irrctx.REMOVE_ATTRIBUTE, Value_Deletions);
        } // End of If.

        // *******************************************
        // Setup the Replacements.
        if (Value_Replacements != null) {
            CURRENT_MODDN = current_dn;
            setupModifications(irrctx.REPLACE_ATTRIBUTE, Value_Replacements);
        } // End of If.

        // *******************************************
        // Setup the Additions.
        if (Value_Additions != null) {
            CURRENT_MODDN = current_dn;
            setupModifications(irrctx.ADD_ATTRIBUTE, Value_Additions);
        } // End of If.


        // *********************************************
        // Ok this ModEntry has been Queued for the
        // Overall modficiation of the Entry,
        // when the DN Changes or we run out of
        // changes this queued change will be
        // run.

        return;

    } // End of modifyEntry method.    

    /**
     * process pending modifications that were sequentially queued to process in bluk.
     *
     * @param irrctx of Destination Directory where modifications are to be applied.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void process_pending_modification(DirContext irrctx)
            throws Exception, idxIRRException {

        String METHODNAME = "process_pending_modification";
        IDXLOG.finer(CLASSNAME, METHODNAME, "Processing Pending Modification for DN:[" + CURRENT_MODDN + "].");

        // ****************************
        // Count the Entries Processed
        entries_processed++;

        // ****************************
        // Check the Queue.
        if ((CURRENT_MODDN == null) ||
                (CURRENT_MODDN.equalsIgnoreCase("")) ||
                (CURRENT_MODLIST.isEmpty())) {
            resetModList();
            IDXLOG.warning(CLASSNAME, METHODNAME, "No Pending Modifications Found for DN:[" + CURRENT_MODDN + "].");
        } // End of Pending Check.

        // ****************************************************
        // Now Create a new Modification Item List for the
        // Entry and then process it.
        ModificationItem[] EntryModSet =
                new ModificationItem[CURRENT_MODLIST.size()];

        // ****************************************************
        // Clear some counters.
        attributes_modified = 0;
        attributes_deleted = 0;
        attributes_added = 0;

        // *******************************************
        // Iterate through the List List for the
        // Entry.
        for (int modItem = 0; modItem < CURRENT_MODLIST.size(); modItem++) {
            EntryModSet[modItem] = (ModificationItem) CURRENT_MODLIST.get(modItem);

            // ***************************
            // Count the Mods.
            if (EntryModSet[modItem].getModificationOp() == irrctx.ADD_ATTRIBUTE) {
                attributes_added++;
            } else if (EntryModSet[modItem].getModificationOp() == irrctx.REPLACE_ATTRIBUTE) {
                attributes_modified++;
            } else if (EntryModSet[modItem].getModificationOp() == irrctx.REMOVE_ATTRIBUTE) {
                attributes_deleted++;
            }

        } // End of For Loop.

        // *******************************************
        // Now Apply the Modifications to the Entry.
        try {
            irrctx.modifyAttributes(CURRENT_MODDN, EntryModSet);
            entries_modified++;
            IDXLOG.fine(CLASSNAME, METHODNAME, "Entry Modified DN:[" + CURRENT_MODDN + "].");

            // ********************************************
            // Show the stats for the Entry.

            if (attributes_added > 0) {
                IDXLOG.finer(CLASSNAME, METHODNAME, "Entry Attributes Added: [" + attributes_added + "]");
            }
            if (attributes_deleted > 0) {
                IDXLOG.finer(CLASSNAME, METHODNAME, "Entry Attributes Deleted: [" + attributes_deleted + "]");
            }
            if (attributes_modified > 0) {
                IDXLOG.finer(CLASSNAME, METHODNAME, "Entry Attributes Modified: [" + attributes_modified + "]");
            }

            // ***************************************************
            // Accumulate the Totals for the Maintenance Activity.
            total_attributes_modified = total_attributes_modified + attributes_modified;
            total_attributes_deleted = total_attributes_deleted + attributes_deleted;
            total_attributes_added = total_attributes_added + attributes_added;

        } catch (NameNotFoundException nnfe) {
            entries_exceptions++;
            IDXLOG.warning(CLASSNAME, METHODNAME, "Entry Not Found for DN:[" +
                    CURRENT_MODDN + "], Ignoring Modification.");
        } catch (AttributeInUseException aiu) {
            entries_exceptions++;
            IDXLOG.warning(CLASSNAME, METHODNAME, "Attribute already exists for DN:[" +
                    CURRENT_MODDN + "], use Replace to properly Modify Entry.");
        } catch (NoSuchAttributeException nsa) {
            entries_exceptions++;
            IDXLOG.warning(CLASSNAME, METHODNAME, "Attribute and/or Value does not exist for DN:[" +
                    CURRENT_MODDN + "], Ignoring Modification for Entry.");
        } catch (AttributeModificationException ame) {
            entries_exceptions++;
            IDXLOG.warning(CLASSNAME, METHODNAME, "Attribute Modification Exception occurred for DN:[" +
                    CURRENT_MODDN + "]," + ame);
        } catch (NamingException ne) {
            entries_exceptions++;
            IDXLOG.warning(CLASSNAME, METHODNAME, "IRR Exception occurred for DN:[" +
                    CURRENT_MODDN + "]," + ne);
        } catch (Exception e) {
            entries_exceptions++;
            IDXLOG.warning(CLASSNAME, METHODNAME, "Exception Processing Modification Set for DN:[" +
                    CURRENT_MODDN + "]," + e);
        } // End of exception.

        // **********************************
        // Clear Modification List.
        resetModList();

    } // End of process_pending_modification method.    

    /**
     * resetModList, clears and resets Mod List for next possible Entry Modification.
     */
    private void resetModList() {

        String METHODNAME = "resetModList";

        // ****************************
        // Clear the LinkedList and DN.
        CURRENT_MODDN = "";
        CURRENT_MODLIST.clear();

    } // End of resetModList Method.

    /**
     * obtainModifications, Obtains Modifications and places into Attributes Object
     * for processing.
     *
     * @param _modattr  Modifcation Attribute Driver.
     * @param _entryattrs Complete set of Modifications.
     * @param _theseAreDeletions    Indicates Modifications are deletions or not.
     * @return Attributes Generated based only on incoming Attribute.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private Attributes obtainModifications(Attribute _modattr,
                                           Attributes _entryattrs,
                                           boolean _theseAreDeletions)
            throws Exception, idxIRRException {

        // **********************************************
        // Check incoming Parameters.
        if ((_modattr == null) ||
                (_entryattrs == null)) {
            return (null);
        }

        // **********************************************
        // Initialize.
        Attributes _valueModifications = new BasicAttributes(true);

        // **********************************************
        // Loop through the Attribute Driver Values.
        for (NamingEnumeration ev = _modattr.getAll(); ev.hasMore(); ) {
            Object Aobject = ev.next();
            String Aname = (String) Aobject;
            Attribute modentry = _entryattrs.get(Aname);

            if (modentry != null) {
                _valueModifications.put(modentry);
            } else if (_theseAreDeletions) {
                _valueModifications.put(new BasicAttribute(Aname));
            } else {
                // NOOP
                // Simple Ignore adding information if this
                // is a replace or add.
                ;
            } // End of Else.

        } // End of Outer For Loop.

        // **********************************************
        // Return Categorized Modifications.
        return (_valueModifications);

    } // End of obtainModifications Method.

    /**
     * setupModifications, Setup Bulk Modificationitem from incoming Attributes Object.
     *
     * @param _modtype        Modifcation Type.
     * @param _entryattrs Incoming attributes to be placed into Modificationitem Queue.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void setupModifications(int _modtype, Attributes _entryattrs)
            throws Exception, idxIRRException {

        // **********************************************
        // Check incoming Parameters.
        if (_entryattrs == null) {
            return;
        }

        // **********************************************************
        // Process the incoming Attributes into the Modification Set.
        for (NamingEnumeration ea = _entryattrs.getAll(); ea.hasMore(); ) {
            Attribute attr = (Attribute) ea.next();
            CURRENT_MODLIST.addLast(new ModificationItem(_modtype, attr));
        } // End of For Loop.

        // **********************************************
        // Return
        return;
    } // End of setupModifications Method.


    /**
     * showModification, provides display of modification for Entry.
     *
     * @param _operation     Operation Performed.
     * @param _entryattrs to be displayed.
     * @throws idxIRRException for any specific IRR unrecoverable errors during function.
     * @throws Exception       for any unrecoverable errors during function.
     */
    private void showModification(String _operation, Attributes _entryattrs) throws Exception, idxIRRException {

        String METHODNAME = "showModification";

        // **********************************************
        // Show the incoming Modification Set.
        for (NamingEnumeration ea = _entryattrs.getAll(); ea.hasMore(); ) {
            Attribute attr = (Attribute) ea.next();
            if (attr.size() == 0) {
                IDXLOG.finest(CLASSNAME, METHODNAME, _operation + "-> " + attr.getID() + ": ");
                continue;
            } // End of If.

            // **********************************************
            // Loop through the Attribute Values.
            for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                String Aname = attr.getID();
                Aname = Aname.toLowerCase();
                Object Aobject = ev.next();
                if (Aname.startsWith("userpassword")) {
                    // *****************************
                    // Show A trimmed Down Version.
                    IDXLOG.finest(CLASSNAME, METHODNAME, _operation + "-> " + "UserPassword: " +
                            "********");
                } else if (Aobject instanceof byte[]) {
                    IDXLOG.finest(CLASSNAME, METHODNAME, _operation + "-> " + attr.getID() +
                            ": [ Binary data " + ((byte[]) Aobject).length + " in length ]");
                } else if (Aobject instanceof String) {
                    if (((String) Aobject).length() > 64) {
                        IDXLOG.finest(CLASSNAME, METHODNAME, _operation + "-> " + attr.getID() +
                                ": " + ((String) Aobject).substring(0, 10) +
                                " ..... " +
                                (((String) Aobject).substring(((String) Aobject).length() - 10)) +
                                ", Full Length:[" + ((String) Aobject).length() + "]");
                    } else {
                        IDXLOG.finest(CLASSNAME, METHODNAME, _operation + "-> " + attr.getID() +
                                ": " + Aobject);
                    } // End of Inner Else.
                } else { // Show normal Attributes as is...
                    IDXLOG.finest(CLASSNAME, METHODNAME, _operation + "-> " + attr.getID() +
                            ": " + "UNKNOWN TYPE, NOT BINARY or STRING");
                } // End of Else.

            } // End of Inner For Loop.
        } // End of Outer For Loop

    } // End of showModification Method.


    /**
     * Main
     *
     * @param args Incoming Argument Array.
     * @see jeffaschenk.commons.frameworks.cnxidx.admin.IRRmodifyEntry
     */
    public static void main(String[] args) {

        idxManageContext IRRSource = null;

        String IRRHost = null;
        String IRRPrincipal = null;
        String IRRCredentials = null;

        String INPUT_FILENAME = null;

        boolean VERBOSE = false;

        String METHODNAME = "main";

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
        if ((Zin.doesNameExist("?")) ||
                (Zin.doesNameExist("usage"))) {
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

        // **************************************************
        // Load up the RunTime Arguments.
        //
        IRRHost = (String) Zin.getValue("hosturl");
        System.out.println(MP + "IRR Host URL:[" + IRRHost + "]");

        INPUT_FILENAME = ((String) Zin.getValue("infile")).trim();
        System.out.println(MP + "Input File:[" + INPUT_FILENAME + "]");

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + "Attempting Source Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "ModificationEntry Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(true);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            System.exit(EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT);
        } // End of exception

        // ************************************************
        // Disable Factories.
        try {
            IRRSource.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of exception

        // ****************************************
        // Initailize Constructor.
        IRRmodifyEntry FUNCTION = new IRRmodifyEntry();

        // ****************************************
        // Perform Function.
        try {
            FUNCTION.perform(IRRSource.irrctx, INPUT_FILENAME);

            // ****************************************
            // Show the Statistics
            FUNCTION.dumpStats();
            FUNCTION.dumpAttributeStats();

        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Performing IRRmodifyEntry.\n" + e);
            System.exit(EXIT_GENERIC_FAILURE);
        } // End of Exception.

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Destination Directory Context.");
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(EXIT_IRR_CLOSE_FAILURE);
        } // End of exception

        // ****************************************
        // Note The End Time.
        elt.setEnd();

        // ****************************************
        // Exit
        System.out.println(MP + "Done, Elapsed Time: " + elt.getElapsed());
        System.exit(EXIT_SUCCESSFUL);

    } // End of Main

} // End of Class IRRmodifyEntry
