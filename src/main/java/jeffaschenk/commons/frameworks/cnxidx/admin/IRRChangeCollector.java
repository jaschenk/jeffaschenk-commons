package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Daemon Server Thread.
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2001-2003
 */

/**
 * IRRChangeCollector
 * Class to run Entry Collector and LDIF Writer Thread.
 */
class IRRChangeCollector implements Runnable, idxCMDReturnCodes {

    /**
     * IRRChangeCollector
     * Class to provide Entry Collector and Writer LDIF Thread from Entries obtain via pipe.
     */
    Thread t;

    private BufferedReader CRQ;

    private IRRChangeStatus ChangeStatus;

    private static final String MP = "IRRChangeWriter: ";

    private idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String IRRMLTuid = null;

    private static String OUTPUT_DIRECTORY = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean APPEND = false;

    private static int OBUFSIZE = 0;
    private static long FILETIMESPAN = 0;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private String CurrentActiveFileName = null;

    private boolean ExitOnException = false;

    private static final String ObjectClassName = "objectclass";

    /**
     * IRRbackupOutput Contructor class driven.
     *
     * @param _BPQin  Pipe.
     * @param _ChangeStatus
     * @param _IRRHost  Source IRR LDAP URL.
     * @param _IRRPrincipal  Source IRR Principal.
     * @param _IRRCredentials  Source IRR Credentials.
     * @param _IRRMLTuid  Source IRR MetaLink Trigger UID.
     * @param _OUTPUT_DIRECTORY  Destination Output Filename.
     * @param _OBUFSIZE     BufferedWriter Output Buffer Size.
     * @param _FILETIMESPAN    File Time Span Window.
     * @param _APPEND Indicate if Output Should be Appended.
     * @param _VERBOSE Indicate Verbosity.
     * @param _DEBUG Indicate DEBUGGING.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    IRRChangeCollector(Reader _BPQin,
                       IRRChangeStatus _ChangeStatus,
                       String _IRRHost,
                       String _IRRPrincipal,
                       String _IRRCredentials,
                       String _IRRMLTuid,
                       String _OUTPUT_DIRECTORY,
                       int _OBUFSIZE,
                       long _FILETIMESPAN,
                       boolean _APPEND,
                       boolean _VERBOSE,
                       boolean _DEBUG,
                       boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        IRRHost = _IRRHost;
        IRRPrincipal = _IRRPrincipal;
        IRRCredentials = _IRRCredentials;
        IRRMLTuid = _IRRMLTuid;
        OUTPUT_DIRECTORY = _OUTPUT_DIRECTORY;
        OBUFSIZE = _OBUFSIZE;
        FILETIMESPAN = _FILETIMESPAN;
        APPEND = _APPEND;
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

        ChangeStatus = _ChangeStatus;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        //
        this.CRQ = new BufferedReader(_BPQin);
        t = new Thread(this, "IRRChangeCollector");
        t.start(); // Start the Thread
    } // End of Contructor.

    /**
     * run
     * Thread to Output LDIF Backup Image.
     */
    public void run() {

        // ***********************************************
        // Initialize
        long memfree;

        String ZEN = null;
        int DNcount = 0;
        int OutputCycle = 0;

        long OutputCycleStart = System.currentTimeMillis();
        long OutputCycleStop = 0;
        long OutputCycleDuration = 0;

        String tname = Thread.currentThread().getName();
        CurrentTimeStamp.enableLocalTime();  // Show Local Time Not GMT.
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Thread Established for:[" + tname + "]");

        // ***********************************************
        // Initialize my LAP Timers
        idxLapTime LP_ENTRY_SEARCH = new idxLapTime();
        idxLapTime LP_ENTRY_TO_LDIF = new idxLapTime();
        idxLapTime LP_ENTRY_FROM_PIPE = new idxLapTime();
        idxLapTime LP_XML_TO_OBJECT = new idxLapTime();

        // ***********************************************
        // Construct the JDOM NonValidating SAXBuilder.
        org.jdom.input.SAXBuilder IRRsaxBuilder = new org.jdom.input.SAXBuilder();

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Attempting Source Object Directory Connection to Host URL:[" + IRRHost + "]");

        IRRSource = new idxManageContext(IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRChangeCollector Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(ExitOnException);

        // ************************************************
        // Now Try to Open and Obtain Context.
        try {
            IRRSource.open();
        } catch (Exception e) {
            System.err.println(MP + e);
            ChangeStatus.setError(EXIT_IRR_CLOSE_FAILURE);
            return;
        } // End of exception

        // *****************************************
        // Disable the Factories.
        try {
            IRRSource.disableDSAEFactories();
        } catch (Exception e) {
            System.err.println(MP + e);
            ChangeStatus.setError(EXIT_GENERIC_FAILURE);
            return;
        } // End of exception  

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        //******************************************
        // Set up our Search Filter.
        String SearchFilter = "(objectclass=*)";

        //******************************************
        // Set up our Search Controls.
        String[] ALL_AttrIDs = {"*"};
        SearchControls OS_ctls = new SearchControls();
        OS_ctls.setReturningAttributes(ALL_AttrIDs);
        OS_ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

        // **************************************
        // Open up the File Output Stream.
        BufferedWriter LDIFOUT = null;
        try {
            // ***********************************
            // Check for the Directory Existence.
            // If not available, create the
            // Directory.
            File outdir = new File(OUTPUT_DIRECTORY);
            if (!outdir.exists()) {
                outdir.mkdirs();
            } else if (!outdir.isDirectory()) {
                System.err.println(MP + "Unable to create Backup Output Directory Path for Incremental Change LDIF Output Files. ");
                ChangeStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
                return; // End Thread.
            } // End of If.

            // ************************************
            // Obtain our LDIF Log File.
            OutputCycle++;
            LDIFOUT = OpenLDIFOutput(OUTPUT_DIRECTORY, OutputCycle, OBUFSIZE);
        } catch (Exception e) {
            System.err.println(MP + "Exception opening Incremental Change LDIF Output File. " + e);
            ChangeStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
            return; // End Thread.
        } // End of exception

        // **************************************
        // Loop to process commands from Walker
        // Thread.
        try {
            while (true) {
                LP_ENTRY_FROM_PIPE.Start();
                ZEN = CRQ.readLine();
                LP_ENTRY_FROM_PIPE.Stop();

                // ***************************
                // Ignore Null.
                if ((ZEN == null) ||
                        ("".equals(ZEN))) {
                    continue;
                }

                // ***************************
                // Incoming Request must be
                // a valid XML Document.
                // Parse it and determine
                // processing.
                //
                Document Zdoc = null;
                Element Zeroot = null;
                try {

                    LP_XML_TO_OBJECT.Start();

                    // ****************************************
                    // Prep String data to XML Reader.
                    InputStream in = new ByteArrayInputStream((byte[]) ZEN.getBytes());

                    // ****************************************
                    // Parse the Request.
                    Zdoc = IRRsaxBuilder.build(in);

                    // ****************************************
                    // Obtain the Element Root.
                    if (Zdoc != null) {
                        Zeroot = Zdoc.getRootElement();
                    }

                    LP_XML_TO_OBJECT.Stop();

                } catch (JDOMException e) {
                    System.err.println(MP + "JDOM Exception Processing XML Document, " + e);
                    ChangeStatus.setError(EXIT_GENERIC_FAILURE);
                    return; // End Thread.
                } // End of Exception.

                // ***************************
                // Ignore a Root Element.
                if (Zeroot == null) {
                    continue;
                }

                // ***************************
                // Check to see if this is a
                // Process Notification.
                //
                if (Zeroot.getName().equalsIgnoreCase("irrchangeloggerprocess")) {

                    // **********************************
                    // End of cycle?
                    String EOC = Zeroot.getAttributeValue("endofcycle");
                    if ((EOC != null) &&
                            ((EOC.equalsIgnoreCase("yes")) ||
                                    (EOC.equalsIgnoreCase("true")))) {
                        // ****************************
                        // Process the End of the Cycle.
                        try {

                            // *******************************************
                            // First Determine if we have elasped the
                            // Duration window, if not, simple flush output
                            // and continue.
                            //
                            OutputCycleStop = System.currentTimeMillis();
                            OutputCycleDuration = (OutputCycleStop - OutputCycleStart);
                            if (OutputCycleDuration >= FILETIMESPAN) {
                                CloseLDIFOutput(LDIFOUT, OUTPUT_DIRECTORY, OutputCycle, DNcount);

                                OutputCycle++;
                                OutputCycleStart = System.currentTimeMillis();
                                DNcount = 0;
                                LDIFOUT = OpenLDIFOutput(OUTPUT_DIRECTORY, OutputCycle, OBUFSIZE);

                            } else {
                                // ************************************
                                // We have not met our Window, so
                                // simple flush existing output, but
                                // do not cycle a new file.
                                //
                                LDIFOUT.flush();
                            } // End of Else.
                            continue;
                        } catch (Exception e) {
                            System.err.println(MP + "Exception opening Incremental Change LDIF Output File. " + e);
                            ChangeStatus.setError(EXIT_IRR_BACKUP_LDIF_OUTPUT_FAILURE);
                            return; // End Thread.
                        } // End of exception
                    } // End of if.

                    // **********************************
                    // End of Process?
                    String EOP = Zeroot.getAttributeValue("endofprocess");
                    if ((EOP != null) &&
                            ((EOP.equalsIgnoreCase("yes")) ||
                                    (EOP.equalsIgnoreCase("true")))) {
                        break;
                    }

                    // **********************************
                    // Ok, Nothing to do, continue...
                    continue;

                } // End of Process Notification if.

                // ***************************
                // Check to see if this is a
                // Change Notification.
                //
                else if (!Zeroot.getName().equalsIgnoreCase("irrchange")) {
                    System.out.println(MP + CurrentTimeStamp.getFTS() + " Warning ** Unknown Process Event detected:[" +
                            Zeroot.getName() +
                            "], Ignoring.");

                    continue;
                } // End of Unknown Notification.

                // ******************************************
                // Now we do have a change notification,
                // Extract the Information and process.
                //
                // ChangeTypes of Notification is:
                // 1: Add
                // 2: Modify
                // 3: Delete
                // 4: Rename (moddn or modrdn).
                //
                String ChangeTypeName = Zeroot.getAttributeValue("typename");
                String ChangeType = Zeroot.getAttributeValue("type");
                String cDN = Zeroot.getAttributeValue("dn");
                String coldDN = Zeroot.getAttributeValue("olddn");

                if ((ChangeTypeName == null) ||
                        (ChangeType == null) ||
                        (cDN == null)) {
                    System.out.println(MP + CurrentTimeStamp.getFTS() + " Warning ** Required Information for Change not Present" +
                            ", Ignoring Event.");

                    continue;
                } // End of Unknown Notification.

                // ******************************************
                // Generate a Header for the Change.
                //
                LDIFOUT.write("# ***********************************************\n");
                LDIFOUT.write("# Change Type:[" + ChangeTypeName + "].\n");
                LDIFOUT.write("# DN:[" + cDN + "]\n");
                if (coldDN != null) {
                    LDIFOUT.write("# OLD DN:[" + coldDN + "]\n");
                }
                LDIFOUT.write("\n");

                // *****************************************
                // Parse the Destination Entry DN to be sure
                // we have any Quotes....
                idxParseDN Naming_Source = new idxParseDN(cDN);
                cDN = Naming_Source.getDNwithQuotes();

                // *****************************************
                // Obtain the Namespace.
                String NameSpace = null;
                try {
                    NameSpace = IRRSource.irrctx.getNameInNamespace();
                    if (NameSpace.equals("")) {
                        NameSpace = cDN;
                    }
                } catch (Exception e) {
                    NameSpace = cDN;
                } // End of exception

                // *****************************************
                // Now obtain the Source Entry.
                //
                try {
                    LP_ENTRY_SEARCH.Start();
                    NamingEnumeration sl = IRRSource.irrctx.search(cDN, SearchFilter, OS_ctls);
                    LP_ENTRY_SEARCH.Stop();
                    if (LP_ENTRY_SEARCH.getCurrentDuration() > 1000) {
                        System.out.println(MP + CurrentTimeStamp.getFTS() + " Warning ** Entry Search took " +
                                LP_ENTRY_SEARCH.getElapsedtoString() +
                                " to Complete to Obtain:[" +
                                cDN + "]");
                    } // End of If.

                    // ********************************
                    // If the Result is Null, Ignore.
                    if (sl == null) {
                        continue;
                    }

                    LP_ENTRY_TO_LDIF.Start();
                    while (sl.hasMore()) {
                        SearchResult si = (SearchResult) sl.next();
                        DNcount++;

                        String DN = null;
                        if (NameSpace.equals("")) {
                            DN = si.getName();
                        } else if (si.getName().equals("")) {
                            DN = NameSpace;
                        } else {
                            DN = si.getName() + "," + NameSpace;
                        }

                        // ************************************
                        // Parse the Current DN.
                        idxParseDN pDN = new idxParseDN(DN);

                        // ******************************************
                        // Write out the DN.
                        // Do not write out a JNDI Quoted DN.
                        // That is not LDIF Compliant.
                        //
                        if (pDN.isQuoted()) {
                            idxIRROutput.WriteLDIF("dn", pDN.getDN(), LDIFOUT);
                        } else {
                            idxIRROutput.WriteLDIF("dn", DN, LDIFOUT);
                        }

                        // Obtain the entries Attributes.
                        Attributes entryattrs = si.getAttributes();

                        // Obtain ObjectClass First.
                        Attribute eo = entryattrs.get(ObjectClassName);
                        for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                            idxIRROutput.WriteLDIF(eo.getID(), eov.next(), LDIFOUT);
                        }

                        // Obtain Naming Attribute Next.
                        if (!"".equals(pDN.getNamingAttribute())) {
                            Attribute en = entryattrs.get(pDN.getNamingAttribute());
                            for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                                idxIRROutput.WriteLDIF(en.getID(), env.next(), LDIFOUT);
                            }
                        } // End of Naming Attribute.

                        // Finish Obtaining remaining Attributes,
                        // in no special sequence.
                        for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                            Attribute attr = (Attribute) ea.next();

                            if ((!ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                                    (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                                for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                                    idxIRROutput.WriteLDIF(attr.getID(), ev.next(), LDIFOUT);
                                }
                            } // End of If
                        } // End of Outer For Loop

                        idxIRROutput.WriteLDIF("", "", LDIFOUT);
                        LP_ENTRY_TO_LDIF.Stop();
                        if (LP_ENTRY_TO_LDIF.getCurrentDuration() > 1000) {
                            System.out.println(MP + CurrentTimeStamp.getFTS() + "Warning ** Entry to LDIF took " +
                                    LP_ENTRY_TO_LDIF.getElapsedtoString() +
                                    " to Complete for:[" +
                                    DN + "]");
                        }

                    } // End of Inner While Loop
                } catch (NameNotFoundException nfe) {
                    if (VERBOSE) {
                        System.out.println(MP + CurrentTimeStamp.getFTS() + " IRR Name Not Found for DN:[" + cDN + "], Ignoring.");
                    }
                } catch (Exception e) {
                    System.err.println(MP + "IRR Exception on IRRChangeLogger, Obtaining Source Entry, " + e);
                    e.printStackTrace();
                    ChangeStatus.setError(EXIT_IRR_BACKUP_FAILURE);
                    return; // End Thread.
                } // End of exception

            } // End of Outer While Loop.
        } catch (Exception e) {
            System.err.println(MP + "IRR Exception on IRRChangeLogger, Obtaining Data From Thread Pipe, " + e);
            e.printStackTrace();
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Show number of entries backed up.
        try {
            CloseLDIFOutput(LDIFOUT, OUTPUT_DIRECTORY, OutputCycle, DNcount);
        } catch (Exception e) {
            System.err.println(MP + "Exception Closing Collection LDIF Output File. " + e);
            ChangeStatus.setError(EXIT_GENERIC_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Close up Shop.
        System.out.println(MP + "Closing Directory Context.");
        try {
            IRRSource.close();
        } catch (Exception e) {
            System.err.println(e);
            ChangeStatus.setError(EXIT_IRR_CLOSE_FAILURE);
            return; // End Thread.
        } // End of exception

        // ***************************************
        // Show the Lap Timings.
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Final Lap Time for Entry Search: " + LP_ENTRY_SEARCH);
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Final Lap Time for Entry to LDIF Output: " + LP_ENTRY_TO_LDIF);
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Final Lap Time for Pipe Communication: " + LP_ENTRY_FROM_PIPE);
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Final Lap Time for XML to Object: " + LP_XML_TO_OBJECT);

        // ***************************************
        // Done.
        return;

    } // End of run.

    /**
     * OpenLDIFOutput
     * Method to Prep the LDIF Output File for current Cycle.
     */
    public BufferedWriter OpenLDIFOutput(String _OUTPUT_DIRECTORY,
                                         int _Cycle,
                                         int _OBUFSIZE)
            throws IOException, Exception {

        // ***********************************************
        // Initialize
        CurrentActiveFileName = obtainCurrentOutputFileName(_OUTPUT_DIRECTORY);

        // ****************************************
        // Determine if the file already exists....
        // If it does, that is not good....
        File cFile = new File(CurrentActiveFileName);
        if (cFile.exists()) {
            System.out.println(MP + CurrentTimeStamp.getFTS() + " New Current Active File:[" +
                    CurrentActiveFileName +
                    "], already exists, possible duplicate process running ending this process.");

            // ********************************
            // Throw Exception.
            throw new Exception("Error ** New Current Active File Already Exists, " +
                    " possible duplicate process running ending this process.");
        } // End of If.

        // **************************************
        // Open up the File Output Stream.
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Preparing new Current Incremental Change Output File:[" +
                CurrentActiveFileName + "].");
        BufferedWriter _LDIFOUT = null;
        if (_OBUFSIZE <= 0) {
            _OBUFSIZE = 131072;
        } // 128KB Buffer.

        _LDIFOUT = new BufferedWriter(new FileWriter(CurrentActiveFileName, false), _OBUFSIZE);

        _LDIFOUT.write("version: 1\n");

        _LDIFOUT.write("# ***********************************************\n");
        _LDIFOUT.write("# FRAMEWORK IRR Directory LDIF Incremental Change.\n");
        _LDIFOUT.write("# Source Host: [" + IRRHost + "]\n");
        _LDIFOUT.write("# Start Time: " + CurrentTimeStamp.get() + "\n");
        _LDIFOUT.write("# ***********************************************\n");
        _LDIFOUT.write("\n");

        return (_LDIFOUT);
    } // End of OpenLDIFOutput Method.

    /**
     * CloseLDIFOutput
     * Method to Close the LDIF Output File for current Cycle.
     */
    public void CloseLDIFOutput(BufferedWriter _LDIFOUT,
                                String _OUTPUT_DIRECTORY,
                                int _Cycle,
                                int _DNcount)
            throws IOException, Exception {

        // ***************************************
        // Show number of entries backed up.
        _LDIFOUT.write("# ***********************************************\n");
        _LDIFOUT.write("# End of IRR Incremental Changes\n");
        _LDIFOUT.write("# End Time: " + CurrentTimeStamp.get() + "\n");
        _LDIFOUT.write("# Entries Contained in LDIF Collection:[" +
                _DNcount + "]\n");
        _LDIFOUT.write("# ***********************************************\n");
        _LDIFOUT.write("\n");

        System.out.println(MP + CurrentTimeStamp.getFTS() + " Closing out Cycle[" +
                _Cycle + "] " +
                "Incremental Changes Logged:[" + _DNcount + "].");

        // ***************************************
        // Close our Output File.
        _LDIFOUT.flush();
        _LDIFOUT.close();

        // ***********************************************
        // Now Rename the Existing Current to a new
        // TimeStamped Filename.
        //
        // Renaming is critical for the LogRestoreDriver
        // to find the correct files in Sequence to 
        // Play back.
        // See <CODE>IRRChangeLogRestoreDriver</CODE> for
        // information regarding processing this file.
        //
        String fCycle = new Integer(_Cycle).toString();
        while (fCycle.length() < 12) {
            fCycle = "0" + fCycle;
        }

        String NEW = OUTPUT_DIRECTORY + System.getProperty("file.separator") +
                "IRRCHGLOG_" + CurrentTimeStamp.get().substring(0, 12) + "_" + fCycle + ".ldif";


        File cFile = new File(CurrentActiveFileName);
        File nFile = new File(NEW);
        if (!cFile.renameTo(nFile)) {
            System.out.println(MP + CurrentTimeStamp.getFTS() + "Error ** Unable to Rename Current To New Archive " +
                    " LDIF Incremental Change File.");
            throw new Exception("Error ** Unable to Rename Current To New Archive " +
                    " LDIF Incremental Change File.");
        } // End of If.

    } // End of CloseLDIFOutput Method.

    /**
     * obtainCurrentOutputFileName
     * Method to obtain a new current OutputFileName
     */
    public String obtainCurrentOutputFileName(String _OUTPUT_DIRECTORY) {

        // ***********************************************
        // Initialize
        String CURRENT = _OUTPUT_DIRECTORY + System.getProperty("file.separator") +
                "IRRCHGLOG_CURRENT_" + CurrentTimeStamp.get().substring(0, 12) + ".ldif";

        // *****************************
        // Return new Filename.
        return (CURRENT);
    } // End of obtainCurrentOutputFileName Method.


} // End of Class IRRChangeCollector
