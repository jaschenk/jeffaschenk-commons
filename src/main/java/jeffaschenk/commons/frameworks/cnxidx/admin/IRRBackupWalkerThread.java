package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.StopWatch;
import jeffaschenk.commons.frameworks.cnxidx.utility.filtering.FilterString;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;
import jeffaschenk.commons.touchpoint.model.threads.CircularObjectStack;

import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * IRR Backup Walker Thread. Used to read Entries from Directory
 * to determine if the entries needs to be written to our Backup Thread.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

/**
 * IRRBackupWalkerThread
 * Class to run Walker Thread.
 */
class IRRBackupWalkerThread implements Runnable, idxCMDReturnCodes {

    /**
     * IRRBackupWalkerThread
     * Class to provide Walker interface to Level Searches.
     */
    Thread t;

    private CircularObjectStack[] cosout_readers;
    private IRRBackupStatusNew WalkerStatus;

    private static String MP = "IRRBackupWalkerThread: ";

    private static idxManageContext IRRSource = null;
    private static String ENTRY_SOURCE_DN = null;
    private static String SearchFilter = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;
    private static boolean BACKUP_WITH_CHILDREN = false;
    private static boolean IGNORE_VENDOR_OBJECTS = false;
    private boolean ExitOnException = false;

    // *************************************************
    // Reader Thread Filter
    private static String DN_THREAD_FILTER_CLASSIFIER_FILENAME = null;
    private boolean DN_THREAD_FILTER_AVAILABLE = false;
    private FilterString DN_THREAD_FILTER = null;
    private static final String THREAD = "THREAD";

    // **************************************************
    // Matching Filters.
    public static final String VENDOROBJECTS_PATTERN
            = "^.*ou\\s*=\\s*vendorobjects+.*";
    public static final String ACTIONGROUPS_PATTERN
            = "^.*ou\\s*=\\s*actiongroups+.*";
    public static final String FRAMEWORKDOMAIN_PATTERN
            = "^.*ou\\s*=\\s*framework+.*";
    public static final String RECYCLEBIN_PATTERN
            = "^.*ou\\s*=\\s*recycle+.*";
    public static final String CONTENT_PATTERN
            = "^.*rcu\\s*=\\s*content+.*";

    /**
     * IRRBackupWalkerThread Contructor class driven.
     *
     * @param cosout          Circular Object Stack for placing DN's on Read Queue.
     * @param WalkerStatus    Object used for Thread Status.
     * @param IRRSource       Managed DirContext
     * @param ENTRY_SOURCE_DN Specified where to begin Backup in Tree.
     * @param SearchFilter    to be used.
     * @param BACKUP_WITH_CHILDREN         Indicate if Children should be included in backup.
     * @param VERBOSE         Indicate Verbosity.
     * @param DEBUG         Indicate DEBUGGING.
     * @param ExitOnException         Indicate Exit on Exceptions.
     */
    IRRBackupWalkerThread(CircularObjectStack cosout,
                          IRRBackupStatusNew WalkerStatus,
                          idxManageContext IRRSource,
                          String ENTRY_SOURCE_DN,
                          String SearchFilter,
                          boolean BACKUP_WITH_CHILDREN,
                          boolean VERBOSE,
                          boolean DEBUG,
                          boolean ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        // Using only one reader.
        cosout_readers = new CircularObjectStack[1];
        cosout_readers[0] = cosout;
        this.ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        this.SearchFilter = SearchFilter;
        this.BACKUP_WITH_CHILDREN = BACKUP_WITH_CHILDREN;
        this.VERBOSE = VERBOSE;
        this.DEBUG = DEBUG;
        this.ExitOnException = ExitOnException;
        this.WalkerStatus = WalkerStatus;
        this.IRRSource = IRRSource;
        // ****************************************
        // Start the Thread.
        t = new Thread(this, "IRRbackup_LevelWalker");
        t.start(); // Start the Thread.
    } // End of Contructor.

    /**
     * IRRBackupWalkerThread Contructor class driven.
     *
     * @param cosout          Circular Object Stack for placing DN's on Read Queue.
     * @param WalkerStatus    Object used for Thread Status.
     * @param IRRSource       Managed DirContext
     * @param ENTRY_SOURCE_DN Specified where to begin Backup in Tree.
     * @param SearchFilter    to be used.
     * @param BACKUP_WITH_CHILDREN         Indicate if Children should be included in backup.
     * @param VERBOSE         Indicate Verbosity.
     * @param DEBUG         Indicate DEBUGGING.
     * @param ExitOnException         Indicate Exit on Exceptions.
     */
    IRRBackupWalkerThread(CircularObjectStack cosout,
                          IRRBackupStatusNew WalkerStatus,
                          idxManageContext IRRSource,
                          String ENTRY_SOURCE_DN,
                          String SearchFilter,
                          boolean BACKUP_WITH_CHILDREN,
                          boolean IGNORE_VENDOR_OBJECTS,
                          String DN_THREAD_FILTER_CLASSIFIER_FILENAME,
                          boolean VERBOSE,
                          boolean DEBUG,
                          boolean ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        // Using only one reader.
        cosout_readers = new CircularObjectStack[1];
        cosout_readers[0] = cosout;
        this.ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        this.SearchFilter = SearchFilter;
        this.BACKUP_WITH_CHILDREN = BACKUP_WITH_CHILDREN;
        this.IGNORE_VENDOR_OBJECTS = IGNORE_VENDOR_OBJECTS;
        this.DN_THREAD_FILTER_CLASSIFIER_FILENAME
                = DN_THREAD_FILTER_CLASSIFIER_FILENAME;
        this.VERBOSE = VERBOSE;
        this.DEBUG = DEBUG;
        this.ExitOnException = ExitOnException;
        this.WalkerStatus = WalkerStatus;
        this.IRRSource = IRRSource;
        // ****************************************
        // Start the Thread.
        t = new Thread(this, "IRRbackup_LevelWalker");
        t.start(); // Start the Thread.
    } // End of Contructor.

    /**
     * IRRBackupWalkerThread Contructor class driven.
     *
     * @param cosout_readers  Circular Object Stacks for placing DN's on Read Queues.
     * @param WalkerStatus    Object used for Thread Status.
     * @param IRRSource       Managed DirContext
     * @param ENTRY_SOURCE_DN Specified where to begin Backup in Tree.
     * @param SearchFilter    to be used.
     * @param BACKUP_WITH_CHILDREN         Indicate if Children should be included in backup.
     * @param VERBOSE         Indicate Verbosity.
     * @param DEBUG         Indicate DEBUGGING.
     * @param ExitOnException         Indicate Exit on Exceptions.
     */
    IRRBackupWalkerThread(CircularObjectStack[] cosout_readers,
                          IRRBackupStatusNew WalkerStatus,
                          idxManageContext IRRSource,
                          String ENTRY_SOURCE_DN,
                          String SearchFilter,
                          boolean BACKUP_WITH_CHILDREN,
                          boolean VERBOSE,
                          boolean DEBUG,
                          boolean ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        this.cosout_readers = cosout_readers;
        this.ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        this.SearchFilter = SearchFilter;
        this.BACKUP_WITH_CHILDREN = BACKUP_WITH_CHILDREN;
        this.VERBOSE = VERBOSE;
        this.DEBUG = DEBUG;
        this.ExitOnException = ExitOnException;
        this.WalkerStatus = WalkerStatus;
        this.IRRSource = IRRSource;
        // ****************************************
        // Start the Thread.
        t = new Thread(this, "IRRbackup_LevelWalker");
        t.start(); // Start the Thread.
    } // End of Contructor.

    /**
     * IRRBackupWalkerThread Contructor class driven.
     *
     * @param cosout_readers  Circular Object Stacks for placing DN's on Read Queues.
     * @param WalkerStatus    Object used for Thread Status.
     * @param IRRSource       Managed DirContext
     * @param ENTRY_SOURCE_DN Specified where to begin Backup in Tree.
     * @param SearchFilter    to be used.
     * @param BACKUP_WITH_CHILDREN         Indicate if Children should be included in backup.
     * @param VERBOSE         Indicate Verbosity.
     * @param DEBUG         Indicate DEBUGGING.
     * @param ExitOnException         Indicate Exit on Exceptions.
     */
    IRRBackupWalkerThread(CircularObjectStack[] cosout_readers,
                          IRRBackupStatusNew WalkerStatus,
                          idxManageContext IRRSource,
                          String ENTRY_SOURCE_DN,
                          String SearchFilter,
                          boolean BACKUP_WITH_CHILDREN,
                          boolean IGNORE_VENDOR_OBJECTS,
                          String DN_THREAD_FILTER_CLASSIFIER_FILENAME,
                          boolean VERBOSE,
                          boolean DEBUG,
                          boolean ExitOnException) {

        // ****************************************
        // Set My Incoming Parameters.
        //
        this.cosout_readers = cosout_readers;
        this.ENTRY_SOURCE_DN = ENTRY_SOURCE_DN;
        this.SearchFilter = SearchFilter;
        this.BACKUP_WITH_CHILDREN = BACKUP_WITH_CHILDREN;
        this.IGNORE_VENDOR_OBJECTS = IGNORE_VENDOR_OBJECTS;
        this.DN_THREAD_FILTER_CLASSIFIER_FILENAME
                = DN_THREAD_FILTER_CLASSIFIER_FILENAME;
        this.VERBOSE = VERBOSE;
        this.DEBUG = DEBUG;
        this.ExitOnException = ExitOnException;
        this.WalkerStatus = WalkerStatus;
        this.IRRSource = IRRSource;
        // ****************************************
        // Start the Thread.
        t = new Thread(this, "IRRbackup_LevelWalker");
        t.start(); // Start the Thread.
    } // End of Contructor.

    public void run() {

        // ***********************************************
        // Initialize our StopWatch to measure Duration
        // of Thread.
        StopWatch sw = new StopWatch();
        sw.start();

        // ***********************************************
        // Initialize Thread Variables.
        int DNcount = 0;
        int DNsIgnored = 0;
        String tname = Thread.currentThread().getName();
        System.out.println(MP + "Thread Established for:[" + tname + "]");

        // ***********************************************
        // Initialize my LAP Timers
        idxLapTime LP_LEVEL_SEARCH = new idxLapTime();
        idxLapTime LP_ENTRY_TO_COS = new idxLapTime();

        // ****************************************
        // Set up our Search Controls
        String[] NO_Attributes = {"1.1"};
        SearchControls OL_ctls = new SearchControls();
        OL_ctls.setReturningAttributes(NO_Attributes);
        OL_ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        // ****************************************
        // Indicate the Backup is starting.
        System.out.println(MP + "Starting Backup...");

        // *********************************************
        // Determine if we have a Reader Thread Filter?
        if (this.DN_THREAD_FILTER_CLASSIFIER_FILENAME != null) {
            System.out.println(MP + "Attempting Read of Thread Filter File Named:[" +
                    this.DN_THREAD_FILTER_CLASSIFIER_FILENAME + "].");
            try {
                DN_THREAD_FILTER_AVAILABLE = false;
                DN_THREAD_FILTER
                        = new FilterString(new File(this.DN_THREAD_FILTER_CLASSIFIER_FILENAME), true);
                DN_THREAD_FILTER_AVAILABLE = true;
            } catch (Exception e) {
                System.err.println(MP + "Warning, Unable to obtain the Thread Filter File Named:[" +
                        this.DN_THREAD_FILTER_CLASSIFIER_FILENAME +
                        "], using Default Configuration," + e);
            } // End of Exception processing.
        } // End of Check for Read Thread Filter. 

        // ****************************************
        // Obtain our initial Source Entry.
        if ((ENTRY_SOURCE_DN != null) &&
                (!ENTRY_SOURCE_DN.equals(""))) {
            LP_ENTRY_TO_COS.Start();
            cosout_readers[classifyDNtoReaderQueue(ENTRY_SOURCE_DN)].push(ENTRY_SOURCE_DN);
            LP_ENTRY_TO_COS.Stop();
            DNcount++;
        } // End of If.

        // *****************************************
        // Now obtain the IRR Entries for Backup.
        //
        idxDNLinkList myChildrenList = new idxDNLinkList();
        myChildrenList.addFirst(ENTRY_SOURCE_DN);
        try {
            // *****************************
            // Obtain all Subsequent Levels
            while (myChildrenList.IsNotEmpty()) {
                String myDN = myChildrenList.popfirst();
                // *****************************************
                // Parse the Destination Entry DN to be sure
                // we have any Quotes....
                idxParseDN Naming_Source = new idxParseDN(myDN);

                LP_LEVEL_SEARCH.Start();

                NamingEnumeration nes =
                        IRRSource.irrctx.search(Naming_Source.getDNwithQuotes(),
                                SearchFilter, OL_ctls);

                LP_LEVEL_SEARCH.Stop();
                if (LP_LEVEL_SEARCH.getCurrentDuration() > 1000) {
                    System.out.println(MP + "Warning ** LEVEL Search took " +
                            LP_LEVEL_SEARCH.getElapsedtoString() +
                            " to Complete for Level:[" +
                            Naming_Source.getDN() + "]");
                } // End of If.

                // *****************************************
                // Loop Throught the Results...
                while (nes.hasMore()) {
                    SearchResult srs = (SearchResult) nes.next();
                    String RDN = srs.getName();

                    // *****************************
                    // Acquire the correct DNs.
                    String SourceDN = RDN;
                    if ((myDN != null) &&
                            (!"".equals(myDN))) {
                        SourceDN = RDN + "," + myDN;
                    }

                    // *****************************
                    // Check for Excluding Entries
                    if (this.IGNORE_VENDOR_OBJECTS) {
                        // *******************************
                        // Check for Ignore Vendor Objects
                        if (SourceDN.toLowerCase().matches(VENDOROBJECTS_PATTERN)) {
                            DNsIgnored++;
                            continue;
                        }

                    } // End of Check for Ignoring any Vendor Objects.

                    // *****************************
                    // Now place the Childs DN in
                    // the Queue to process it's
                    // Children on the next iteration.
                    //
                    myChildrenList.addLast(SourceDN);

                    // *****************************
                    // Now Send the Search Result
                    // Entry to our output thread.
                    //
                    LP_ENTRY_TO_COS.Start();
                    cosout_readers[classifyDNtoReaderQueue(SourceDN)].push(SourceDN);
                    LP_ENTRY_TO_COS.Stop();
                    DNcount++;
                } // End of Inner While.
                // **************************************
                // If we are not obtaining Children, end.
                if (!BACKUP_WITH_CHILDREN) {
                    break;
                }
            } // End of Outer While.

        } catch (Exception e) {
            System.err.println(MP + "IRR Exception on Obtaining Child Entries,\n" + e);
            e.printStackTrace();
            WalkerStatus.setError(EXIT_IRR_BACKUP_FAILURE);
        } // End of exception

        // *******************************************
        // Show number of entries passed through the
        // Stack.
        if (DNcount > 0) {
            System.out.println(MP + "Successful Backup Walker Thread Completed with Entries Passed to Reader Stack:[" +
                    DNcount + "]");
        } else {
            System.out.println(MP + "Exception On Backup Walker Thread Completed with Entries Passed to Reader Stack:[" +
                    DNcount + "]");
        } // End of Else Check.

        // *******************************************
        // Show number of entries passed through the
        // Stack.
        if (DNsIgnored > 0) {
            System.out.println(MP + "Entries Ignored:[" +
                    DNsIgnored + "], Filters: Ignore Vendor Objects:[" +
                    this.IGNORE_VENDOR_OBJECTS + "]");
        } // End of Checking for Any Ignored DNs.

        // ***************************************
        // Show the Lap Timings.
        System.out.println(MP + "Lap Time for Level Searches: " + LP_LEVEL_SEARCH);
        System.out.println(MP + "Lap Time for Stack Communications: " + LP_ENTRY_TO_COS);

        // ***************************************
        // Show the Duration of Thread.
        sw.stop();
        System.out.println(MP + "Thread Duration: " + sw.getElapsedTimeString());

        // ***************************************
        // Done.
        return;

    } // End of run.

    /**
     * Classify DN to Reader Queue.
     * Stack #  Usage
     * -------  -------------------------------
     * -0-    Primary and Default.
     * -1-    Actiongroups and Framework
     * -2-    VendorObjects if Queue Available.
     */
    private int classifyDNtoReaderQueue(String DN) {
        int reader_queue = 0;
        if ((DN == null) ||
                (DN.trim().equalsIgnoreCase(""))) {
            return reader_queue;
        }
        // *******************************
        // Proceed to Classify the
        // DN to A Reader Queue Based upon
        // number of Reader Queues.
        if (this.cosout_readers.length <= 1) {
            return reader_queue;
        }

        // *************************************
        // If we have an Available FilterString
        // Object to Group our Matches, then use
        // it to override our hardcoded
        // classifications.
        if (DN_THREAD_FILTER_AVAILABLE) {
            String groupname = DN_THREAD_FILTER.obtainGroupValueWithMatch(DN);
            if ((groupname != null) &&
                    (!groupname.trim().equalsIgnoreCase(FilterString.FILTER_GROUP_DEFAULT)) &&
                    (groupname.trim().toUpperCase().startsWith(this.THREAD))) {
                int groupthread = -1;
                try {
                    groupthread = Integer.parseInt(groupname.substring(this.THREAD.length()));
                    groupthread = (groupthread - 1); // Make it Relative to Zero.
                } catch (NumberFormatException nfe) {
                    groupthread = -1;
                } // End of Exception.
                if ((groupthread >= 0) &&
                        (groupthread < cosout_readers.length)) {
                    return groupthread;
                }
            } // End of Inner Check for a groupname hit from Filter File.
        } // End of Check using Overriding Thread Filter.

        // *************************
        // When two Readers Are
        // Available...
        if (this.cosout_readers.length == 2) {
            // **************************
            // Direct Entries
            // to Specific Reader Queues.
            if (DN.toLowerCase().matches(ACTIONGROUPS_PATTERN)) {
                reader_queue = 0;
            }
            if (DN.toLowerCase().matches(FRAMEWORKDOMAIN_PATTERN)) {
                reader_queue = 1;
            }
            if (DN.toLowerCase().matches(RECYCLEBIN_PATTERN)) {
                reader_queue = 1;
            }
            if (DN.toLowerCase().matches(CONTENT_PATTERN)) {
                reader_queue = 0;
            }
            if (!this.IGNORE_VENDOR_OBJECTS) {
                if (DN.toLowerCase().matches(VENDOROBJECTS_PATTERN)) {
                    reader_queue = 1;
                }
            } // End of Check for Using Ignoring Objects.

        } // End of Else If.
        // *************************
        // When the Max Readers Are
        // Available...
        else {
            // **************************
            // Direct Entries
            // to Specific Reader Queues.
            if (DN.toLowerCase().matches(ACTIONGROUPS_PATTERN)) {
                reader_queue = 1;
            }
            if (DN.toLowerCase().matches(FRAMEWORKDOMAIN_PATTERN)) {
                reader_queue = 1;
            }
            if (DN.toLowerCase().matches(RECYCLEBIN_PATTERN)) {
                reader_queue = 1;
            }
            if (DN.toLowerCase().matches(CONTENT_PATTERN)) {
                reader_queue = 1;
            }

            // ********************
            // Direct VendorObjects
            // to Specific Thread.
            if (!this.IGNORE_VENDOR_OBJECTS) {
                if (DN.toLowerCase().matches(VENDOROBJECTS_PATTERN)) {
                    reader_queue = 2;
                }
            } // End of Check for Using Ignoring Objects.
            else {
                // **************************************
                // Vendor Objects are Ignored so use this
                // Thread accordingly.
                //
                // TODO
                if (reader_queue != 1) {
                    reader_queue = 2;
                }
            } // End of Else.
        } // End of Else.
        return reader_queue;
    } // End of classifyDNtoReaderQueue Private Method.
} ///:~ End of Class IRRBackupWalkerThread
