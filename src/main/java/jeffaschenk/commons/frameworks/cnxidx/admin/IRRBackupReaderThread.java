package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxCMDReturnCodes;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxManageContext;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxParseDN;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;

import javax.naming.*;
import javax.naming.directory.*;

/**
 * IRR Backup Reader Thread.  Will Read Directory Entry and
 * pass element to Queue to be eventually written to an LDIF compilent Data file.
 * Backup output conforms to the LDIF Specification: RFC2849.

 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

/**
 * IRRBackupReaderThread
 * Class to run Reader Thread.
 */
class IRRBackupReaderThread implements Runnable, idxCMDReturnCodes {

    /**
     * IRRBackupReaderThread
     * Class to provide Reader Thread to initiate Read of Directory Entries
     * complete set of attributes.
     */
    Thread t;
    private String THREAD_NAME;
    private int instance;

    private CircularObjectStack cosin; // Input Stack.

    private CircularObjectStack cosout; // Output Stack.

    private IRRBackupStatusNew ReaderStatus;

    private static String MP = "IRRBackupReaderThread";

    private static idxManageContext IRRSource = null;

    private static String ENTRY_SOURCE_DN = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    private boolean ExitOnException = false;

    public static final String ObjectClassName = "objectclass";
    public static final String DEFAULT_SEARCH_FILTER = "(" + ObjectClassName + "=*)";

    /**
     * IRRBackupOutputThread Contructor class driven.
     *
     * @param cosin            Circular Object Stack for placing DN's on Read Queue.
     * @param cosout           Circular Object Stack for placing LDAP Entries on Output Stack.
     * @param ReaderStatus     Object used for Thread Status.
     * @param IRRSource        Managed DirContext
     * @param ENTRY_SOURCE_DN  Specified where to begin Backup in Tree.
     * @param _VERBOSE         Indicate Verbosity.
     * @param _DEBUG           Indicate DEBUGGING.
     * @param _ExitOnException Indicate Exit on Exceptions.
     */
    IRRBackupReaderThread(
            int instance,
            CircularObjectStack cosin,
            CircularObjectStack cosout,
            IRRBackupStatusNew ReaderStatus,
            idxManageContext IRRSource,
            String ENTRY_SOURCE_DN,
            boolean _VERBOSE,
            boolean _DEBUG,
            boolean _ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        this.cosin = cosin;
        this.cosout = cosout;
        this.ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        this.VERBOSE = _VERBOSE;
        this.DEBUG = _DEBUG;
        this.ExitOnException = _ExitOnException;
        this.ReaderStatus = ReaderStatus;
        this.IRRSource = IRRSource;
        this.instance = instance;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        THREAD_NAME = "IRRbackup_ReaderThread" + (this.instance + 1);
        t = new Thread(this, THREAD_NAME);
        t.start(); // Start the Thread
    } // End of Contructor.

    /**
     * run
     * Thread to Read LDAP Entry all Attributes and post
     * Object to Output Stack to be ripped as an LDIF Backup Image.
     */
    public void run() {

        // ***********************************************
        // Initialize our StopWatch to measure Duration
        // of Thread.
        StopWatch sw = new StopWatch();
        sw.start();

        // ***********************************************
        // Initialize Thread Variables.
        String ZEN = null;
        int DNcount = 0;
        long memfree;
        String tname = Thread.currentThread().getName();
        System.out.println(MP + (instance + 1) + ": Thread Established for:[" + tname + "]");

        // ***********************************************
        // Initialize my LAP Timers
        idxLapTime LP_ENTRY_SEARCH = new idxLapTime();
        idxLapTime LP_ENTRY_TO_COS = new idxLapTime();
        idxLapTime LP_ENTRY_FROM_COS = new idxLapTime();

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        //******************************************
        // Set up our Search Controls.
        String[] ALL_AttrIDs = {"*"};
        SearchControls OS_ctls = new SearchControls();
        OS_ctls.setReturningAttributes(ALL_AttrIDs);
        OS_ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

        // **************************************
        // Loop to process commands from Walker
        // Thread.
        try {
            while (true) {
                ZEN = null; // BE sure to Clear Previous Entry.
                LP_ENTRY_FROM_COS.Start();
                if (cosin.hasMoreNodes()) {
                    ZEN = (String) cosin.getNext();
                }
                LP_ENTRY_FROM_COS.Stop();

                // ***************************
                // Did anything get pulled
                // from stack?
                if ((ZEN == null) ||
                        ("".equals(ZEN))) {
                    t.sleep(1000);
                    continue;
                } // End of Nothing in Stack yet to Process.

                // ***************************
                // Should We End Thread?
                if (IRRBackupNew.END_OF_DATA.equals(ZEN)) {
                    break;
                }

                // *****************************
                // Is the Entry a LDIF Comment?
                // Or a Simple NewLine?
                if ((ZEN.startsWith("#")) ||
                        (ZEN.equals("\n"))) {
                    continue; // Ignore Comments and Blank Lines.
                } // End of If.

                // *****************************
                // Ok, this must be a DN, so
                // Process.
                //

                // *****************************************
                // Parse the Destination Entry DN to be sure
                // we have any Quotes....
                idxParseDN Naming_Source = new idxParseDN(ZEN);
                ZEN = Naming_Source.getDNwithQuotes();

                // *****************************************
                // Obtain the Namespace.
                String NameSpace = null;
                try {
                    NameSpace = IRRSource.irrctx.getNameInNamespace();
                    if (NameSpace.equals("")) {
                        NameSpace = ZEN;
                    }
                } catch (Exception e) {
                    {
                        NameSpace = ZEN;
                    }
                } // End of exception

                // *****************************************
                // Now obtain the Source Entry.
                //
                try {

                    LP_ENTRY_SEARCH.Start();
                    NamingEnumeration senum =
                            IRRSource.irrctx.search(ZEN, DEFAULT_SEARCH_FILTER, OS_ctls);
                    LP_ENTRY_SEARCH.Stop();
                    if (LP_ENTRY_SEARCH.getCurrentDuration() > 1000) {
                        System.out.println(MP + (instance + 1) + ": Warning ** Entry Search took " +
                                LP_ENTRY_SEARCH.getElapsedtoString() +
                                " to Complete to Obtain:[" +
                                ZEN + "]");
                    } // End of Warning Check for Long Search.
                    // ****************************
                    // Ignore any NULL Results.
                    if (senum == null) {
                        continue;
                    }
                    // ***************************
                    // Obtain each SearchResult
                    // and place on Output Stack.
                    while (senum.hasMore()) {
                        SearchResult sr = (SearchResult) senum.next();
                        IRRBackupSearchResult isr = new IRRBackupSearchResult(ZEN, sr);
                        LP_ENTRY_TO_COS.Start();
                        cosout.push(isr);
                        LP_ENTRY_TO_COS.Stop();
                        DNcount++;
                    } // End of Enumeration While Loop.
                } catch (Exception e) {
                    System.err.println(MP + (instance + 1) + ": IRR Exception on IRRbackup, Obtaining Source Entry, " + e);
                    e.printStackTrace();
                    ReaderStatus.setError(EXIT_IRR_BACKUP_FAILURE);
                    return; // End Thread.
                } // End of exception
            } // End of Outer While Loop.
        } catch (Exception e) {
            System.err.println(MP + (instance + 1) + ": IRR Exception on IRRbackup, Obtaining Data From Object Stack. " + e);
            return; // End Thread.
        } // End of exception

        // *******************************************
        // Show number of entries passed through the
        // Stack.
        if (DNcount > 0) {
            System.out.println(MP + (instance + 1) + ": Successful Backup Reader Thread Completed with Entries Passed to Output Stack:[" +
                    DNcount + "]");
        } else {
            System.out.println(MP + (instance + 1) + ": Backup Reader Thread Completed, " +
                    " However with NO Entries Passed to Output Stack, since no DN entries were obtained from Stack.");
        }

        // ***************************************
        // Show the Lap Timings.
        System.out.println(MP + (instance + 1) + ": Lap Time for Entry Search: " + LP_ENTRY_SEARCH);
        System.out.println(MP + (instance + 1) + ": Lap Time for Entry to Output Stack: " + LP_ENTRY_TO_COS);
        System.out.println(MP + (instance + 1) + ": Lap Time for Entry From Input Stack: " + LP_ENTRY_FROM_COS);

        // ***************************************
        // Show the Duration of Thread.
        sw.stop();
        System.out.println(MP + (instance + 1) + ": Thread Duration: " + sw.getElapsedTimeString());

        // ***************************************
        // Done.
        return;

    } // End of run.

} ///:~ End of Class IRRBackupReaderThread
