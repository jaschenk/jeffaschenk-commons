package jeffaschenk.commons.frameworks.cnxidx.admin;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.*;

import java.util.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Java Daemon Server thread.
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2001-2002
 */

/**
 * IRRChangePoller
 * Class to run MetaLink Trigger Poller Thread.
 */
class IRRChangePoller implements Runnable, idxCMDReturnCodes {

    /**
     * IRRChangePoller
     * Class to Interface to MetaLink Triggers.
     */
    Thread t;

    private BufferedWriter CWQ;

    private IRRChangeStatus ChangeStatus;

    private static String MP = "IRRChangePoller: ";

    private static final String XMLHDR = "<?xml version='1.0' encoding='utf-8'?>";
    private static final String EOPYES = XMLHDR + "<irrchangeloggerprocess endofcycle='no' endofprocess='yes'/>" + "\n";
    private static final String EOCYES = XMLHDR + "<irrchangeloggerprocess endofcycle='yes' endofprocess='no'/>" + "\n";

    private static idxManageContext IRRSource = null;

    private static String IRRHost = null;
    private static String IRRPrincipal = null;
    private static String IRRCredentials = null;
    private static String IRRMLTuid = null;

    private static String OUTPUT_DIRECTORY = null;

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    private idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

    private boolean ExitOnException = false;

    private boolean RUNNING = true;

    /**
     * IRRChangePoller Contructor class driven.
     *
     * @param BPQout            Pipe Queue.
     * @param _ChangeStatus     Object.
     * @param _IRRHost          Source IRR LDAP URL.
     * @param _IRRPrincipal     Source IRR Principal.
     * @param _IRRCredentials   Source IRR Credentials.
     * @param _IRRMLTuid        MetaLink Trigger UID.
     * @param _OUTPUT_DIRECTORY Output Directory Name for Trapping a Stop.
     * @param _VERBOSE          Indicate Verbosity.
     * @param _DEBUG            Indicate DEBUGGING.
     * @param _ExitOnException  Indicate Exit on Exceptions.
     */
    IRRChangePoller(Writer BPQout,
                    IRRChangeStatus _ChangeStatus,
                    String _IRRHost,
                    String _IRRPrincipal,
                    String _IRRCredentials,
                    String _IRRMLTuid,
                    String _OUTPUT_DIRECTORY,
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
        VERBOSE = _VERBOSE;
        DEBUG = _DEBUG;
        ExitOnException = _ExitOnException;

        ChangeStatus = _ChangeStatus;

        // ****************************************
        // Ready the Synchronized Object and start
        // the Thread.
        //
        try {
            this.CWQ = new BufferedWriter(BPQout);
            t = new Thread(this, "IRRChangePoller");
            t.start(); // Start the Thread.
        } catch (Exception e) {
            // TODO Handle
        }
    } // End of Contructor.

    public void run() {

        // ***********************************************
        // Initialize
        long memfree;
        long CollectorCycle = 0;
        long CollectorChangesDetected = 0;
        String tname = Thread.currentThread().getName();
        CurrentTimeStamp.enableLocalTime();  // Show Local Time Not GMT.
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Thread Established for:[" + tname + "]");

        // ***********************************************
        // Initialize my LAP Timers
        idxLapTime LP_OBTAINING_TRIGGER = new idxLapTime();
        idxLapTime LP_PROCESSING_TRIGGER_LIST = new idxLapTime();
        idxLapTime LP_ENTRY_TO_PIPE = new idxLapTime();

        // **************************************************
        // Obtain Runtime Object.
        Runtime rt = Runtime.getRuntime();

        // **************************************************
        // Shutdown Indicator Filename.
        String SHUTDOWN_FILENAME = OUTPUT_DIRECTORY + System.getProperty("file.separator") +
                "IRRCHGLOG_SHUTDOWN_PROCESS";

        // ***********************************************
        // Now initiate a Connection to the Directory
        // for a LDAP Source Context for obtain leaf entries.
        System.out.println(MP + CurrentTimeStamp.getFTS() +
                " Attempting Aux Object Directory Connection to Host URL:[ldap://" + IRRHost + "]");

        IRRSource = new idxManageContext("ldap://" + IRRHost,
                IRRPrincipal,
                IRRCredentials,
                "IRRChangePoller Aux Source");

        // ************************************************
        // Exit on all Exceptions.
        IRRSource.setExitOnException(false);

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

        // ****************************************
        // Indicate the Poller is starting.
        System.out.println(MP + CurrentTimeStamp.getFTS() + " Starting MetaLink Poller...");

        // ****************************************
        // Now Enter our LOOP to obtain changes
        // from the MetaLink Trigger...
        while (RUNNING) {
            // ***********************************************
            // Now Determine if I should stop the process
            // based upon the existenance of a "SHUTDOWN" file
            // in the Output Directory.
            //
            File sFile = new File(SHUTDOWN_FILENAME);
            if (sFile.exists()) {
                System.out.println(MP + CurrentTimeStamp.getFTS() + " Detected Stop Request," +
                        " Shutdown of Change Log Process in progress.");
                break;
            }

            // ***********************************************
            // Now initiate a Connection to the Directory
            // to obtain the MLT Connection Context.
            CollectorCycle++;
            if (VERBOSE) {
                System.out.println(MP + CurrentTimeStamp.getFTS() + " Cycle:[" + CollectorCycle +
                        "], MetaLink Poller Directory Connecting to Host:[" + IRRHost + "]");
            }

            // *******************************************
            // Establish MetaLink Trigger Token.
            //MLTrigConnectionTokenSimple MLTtoken = new MLTrigConnectionTokenSimple( IRRHost,
            //				                 IRRPrincipal,
            //                                             IRRCredentials);

            // ***********************************************
            // Now initiate a Connection to the Directory
            // for a Trigger Context
            //MLTrigConnection MLTcontext = new MLTrigConnection();
            //try {
            //MLTcontext.start( MLTtoken, IRRMLTuid );
            //} catch (MLTrigException e) {
            //	System.err.println(MP+"MLT Exception Starting Connection, "+e);
            //	ChangeStatus.setError( EXIT_IRR_UNABLE_TO_OBTAIN_CONTEXT );

            // ***************************************
            // Tell the LDIF Thread to Finish.
            //	try {
            //		CWQ.write( EOPYES );
            //		CWQ.flush();
            //	} catch(Exception xe) {}
            // **********************
            // End Thread.
            // return; // End Thread.
            //} // End of Exception

            // ****************************************
            // If Debug, show total Memory.
            if (DEBUG) {
                System.out.println(MP + "Total Memory: [" +
                        rt.totalMemory() + "].");

                memfree = rt.freeMemory();

                System.out.println(MP + "Current Free Memory: [" +
                        memfree + "].");
            }

            // ***********************************************
            // Wait for a Change Trigger to be popped
            System.out.println(MP + CurrentTimeStamp.getFTS() + " Cycle:[" + CollectorCycle + "], Waiting for Changes....");
            //try {
            LP_OBTAINING_TRIGGER.Start();
            //MLTrigChangeList lchangelist = MLTcontext.getNextChanges();
            LP_OBTAINING_TRIGGER.Stop();

            // ***************************************
            // Ok, we have a change list, process
            LP_PROCESSING_TRIGGER_LIST.Start();
            // TODO -- this while will not go into effect...
            //while( lchangelist.hasMoreChanges() )
            //{
            CollectorChangesDetected++;
            String TypeofChange;
            //MLTrigChange lchange = lchangelist.getNextChange();
            //MLTrigChangeRename lRchange = null;
            /**
             switch( lchange.getOperation() ) {

             case MLTrigChange.ADD:
             TypeofChange = "ADD";
             break;

             case MLTrigChange.MODIFY:
             TypeofChange = "MODIFY";
             break;

             case MLTrigChange.DELETE:
             TypeofChange = "DELETE";
             break;

             case MLTrigChange.RENAME:
             TypeofChange = "RENAME";
             lRchange = (MLTrigChangeRename)lchange;
             break;

             default:
             TypeofChange = "UNKNOWN";
             break;
             } // End of Switch
             **/
            TypeofChange = "UNKNOWN";
            // *********************************************
            // Show the Change Information
            // And construct a XML Document to send over the
            // Pipe to our other Thread Buddy.
            if (VERBOSE) {
                System.out.println(MP + CurrentTimeStamp.getFTS() + " Operation Perform on Entry:[" +
                        TypeofChange + "]");
            }
            /**
             if ( lchange.getOperation() == MLTrigChange.RENAME )
             {
             if (VERBOSE)
             {
             System.out.println(MP+CurrentTimeStamp.getFTS()+"  olddn: " + lchange.getDN() );
             System.out.println(MP+CurrentTimeStamp.getFTS()+"  dn: " + lRchange.getNewDN() );
             }
             LP_ENTRY_TO_PIPE.Start();
             CWQ.write( XMLHDR+"<irrchange "+
             "typename=\042"+TypeofChange+"\042 "+
             "type=\042"+lchange.getOperation()+"\042 "+
             "dn=\042"+lRchange.getNewDN()+"\042   "+
             "olddn=\042"+lchange.getDN()+"\042 "+
             "/>\n" );
             LP_ENTRY_TO_PIPE.Stop();
             CWQ.flush();

             // *************************************
             // Now we need to feed the pipe with
             // any leaf entries that were affected
             // by the rename of a parent.
             //
             TreeMap LEAFmap = new TreeMap();
             try {
             LEAFmap = obtainLeafEntries( IRRSource.irrctx,
             lRchange.getNewDN(), lchange.getDN());
             } catch(Exception e) {
             System.out.println(MP+CurrentTimeStamp.getFTS()+
             "Exception obtaining leaf Entries, Ignoring Exception:["+e+"]");
             } // End of Exception.

             // *************************************
             // Now iterate through the Treemap
             // and produce additional rename
             // operations for leaf entries.
             //
             if ( (LEAFmap != null) && (LEAFmap.size() > 0 ) )
             {
             Set _Rset = LEAFmap.entrySet();
             Iterator iterator = _Rset.iterator();
             while (iterator.hasNext())
             {
             Map.Entry _Rentry = (Map.Entry)iterator.next();
             String _X500newDN = (String)_Rentry.getKey();
             String _oldDN = (String)LEAFmap.get(_X500newDN);

             LP_ENTRY_TO_PIPE.Start();
             CWQ.write( XMLHDR+"<irrchange "+
             "typename=\042"+TypeofChange+"\042 "+
             "type=\042"+lchange.getOperation()+"\042 "+
             "dn=\042"+convertX500NameToLDAPName(_X500newDN)+"\042   "+
             "olddn=\042"+_oldDN+"\042 "+
             "/>\n" );
             LP_ENTRY_TO_PIPE.Stop();
             CWQ.flush();
             if (VERBOSE)
             {
             System.out.println(MP+CurrentTimeStamp.getFTS()+
             "  leaf olddn: " + _oldDN);
             System.out.println(MP+CurrentTimeStamp.getFTS()+
             "  leaf dn: " + convertX500NameToLDAPName(_X500newDN) );
             } // End of If.

             } // End of While.
             } else {
             System.out.println(MP+CurrentTimeStamp.getFTS()+
             " No Leaf Entries for Rename to be Processed.");
             } // End of Else.

             // ***********************************
             // All other operations here.
             } else {
             if (VERBOSE)
             { System.out.println(MP+CurrentTimeStamp.getFTS()+"  dn: " + lchange.getDN() ); }
             LP_ENTRY_TO_PIPE.Start();
             CWQ.write( XMLHDR+"<irrchange "+
             "typename=\042"+TypeofChange+"\042 "+
             "type=\042"+lchange.getOperation()+"\042 "+
             "dn=\042"+lchange.getDN()+"\042 "+
             "/>\n" );
             LP_ENTRY_TO_PIPE.Stop();
             CWQ.flush();
             } // End of Else.
             **/
            // } // End of ChangeList Inner While.
            LP_PROCESSING_TRIGGER_LIST.Stop();

            // *****************************************
            // Now we have scanned through a change
            // List, tell the MLT Facility to stop
            // and that we have processed all.
            //
            System.out.println(MP + CurrentTimeStamp.getFTS() +
                    " Closing MetaLink Trigger Context, Changed Collected for this cycle:[" +
                    CollectorChangesDetected +
                    "].");
            //MLTcontext.stop(true);
            CollectorChangesDetected = 0;

            // ***************************************
            // Show the Lap Timings.
            if (VERBOSE) {
                System.out.println(MP + CurrentTimeStamp.getFTS() + " Cycle:[" + CollectorCycle +
                        "], Lap Time for Pipe Communications: " + LP_ENTRY_TO_PIPE);
                System.out.println(MP + CurrentTimeStamp.getFTS() + " Cycle:[" + CollectorCycle +
                        "], Lap Time for Obtaining a Trigger List: " + LP_OBTAINING_TRIGGER);
                System.out.println(MP + CurrentTimeStamp.getFTS() + " Cycle:[" + CollectorCycle +
                        "], Lap Time for Processing a Trigger List: " + LP_PROCESSING_TRIGGER_LIST);
            } // End of If.

            // ***********************************************
            // Reset my LAP Timers for Next Cycle.
            LP_OBTAINING_TRIGGER.Reset();
            LP_PROCESSING_TRIGGER_LIST.Reset();
            LP_ENTRY_TO_PIPE.Reset();

            // ****************************************
            // Tell the Writer Thread to CheckPoint
            // to next output file.
            //
            try {
                CWQ.write(EOCYES);
                CWQ.flush();
            } catch (Exception e) {
                System.err.println(MP + "IRR Exception Writing to Thread Pipe,\n" + e);
                ChangeStatus.setError(EXIT_GENERIC_FAILURE);
                return; // End Thread.
            } // End Of Exception.

            /**
             } catch (MLTrigException e) {
             // **************************************
             // If we have an ReturnCode of a Server
             // Operation Failure, then we simply
             // need to fall through and obtain
             // a connection again.
             // If a different error, then we
             // fail.
             //
             if ( e.getErrorCode() != MLTrigException.SERVER_OP_FAILED )
             {
             System.err.println(MP+"MLT Exception on getNextChanges RC:[" + e.getErrorCode() + "],"+ e);
             e.printStackTrace();
             ChangeStatus.setError( EXIT_GENERIC_FAILURE );

             // ************************
             // Tell other thread to end
             try {
             CWQ.write( EOPYES );
             CWQ.flush();
             } catch (Exception xe) {}
             return; // End of Thread.
             } // End of If.
             else {
             if (VERBOSE)
             { System.out.println(MP+CurrentTimeStamp.getFTS()+
             " Cycle:["+CollectorCycle+"], MetaLink Poller Ended, will retry Cycle.");
             }
             CollectorCycle--;
             } // End of Else.
             } catch (Exception e) {
             System.err.println(MP+"IRR Exception on Obtaining Child Entries,\n" + e);
             e.printStackTrace();
             ChangeStatus.setError( EXIT_GENERIC_FAILURE );

             // ************************
             // Tell other thread to end
             try {
             CWQ.write( EOPYES );
             CWQ.flush();
             } catch (Exception xe) {}

             return; // End Thread.
             } // End of exception
             **/
        } // End of Outer While.

        // ***************************************
        // Tell the LDIF Thread to Finish.
        try {
            CWQ.write(EOPYES);
            CWQ.flush();

            // *******************************
            // Wait for LDIF Thread to Finish.
            //
            Thread.sleep(10000); // 10 Seconds.

        } catch (Exception e) {
            System.err.println(MP + "IRR Exception Writing to Thread Pipe,\n" + e);
            ChangeStatus.setError(EXIT_GENERIC_FAILURE);
            return; // End Thread.
        } // End of Exception

        // ***************************************
        // Done.
        return;

    } // End of run.

    /**
     * obtainLeafEntries
     * Method to obtain all current leaf entries for the
     * existing New DN.
     * <p/>
     * Since if a Rename occurrs that affects it's leaf entries
     * we must generate the necessary rename for those leafs
     * as well.
     *
     * @param _irrctx      of IRR Instance.
     * @param _ParentNewDN Parent New DN.
     * @param _ParentOldDN Parent Old DN.
     * @return TreeMap of Entries.
     */
    private TreeMap obtainLeafEntries(DirContext _irrctx,
                                      String _ParentNewDN,
                                      String _ParentOldDN)
            throws Exception {

        // ***********************************
        // Obtain the Notification List File
        // Information.
        //
        TreeMap<String,String> LEAFmap = new TreeMap<>();

        // ***********************************
        // Set up the Search Controls.
        String[] NO_AttrIDs = {"1.1"};
        SearchControls SUBTREE_OS_ctls = new SearchControls();
        SUBTREE_OS_ctls.setReturningAttributes(NO_AttrIDs);
        SUBTREE_OS_ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(_ParentNewDN);

        // *****************************************
        // Obtain the Namespace.
        String NameSpace = null;
        try {
            NameSpace = _irrctx.getNameInNamespace();
            if (NameSpace.equals("")) {
                NameSpace = _ParentNewDN;
            }
        } catch (Exception e) {
            NameSpace = _ParentNewDN;
        } // End of exception

        // *****************************************
        // Now obtain the Leaf Entries.
        //
        try {
            NamingEnumeration sl = _irrctx.search(_ParentNewDN, "(objectclass=*)", SUBTREE_OS_ctls);

            // ********************************
            // If the Result is Null, Ignore.
            if (sl == null) {
                return (LEAFmap);
            }

            // ***********************************
            // Loop to obtain all Leaf Entry DNs
            long Rename_DNcount = 0;
            while (sl.hasMore()) {
                SearchResult si = (SearchResult) sl.next();

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

                // ********************************
                // Formulate the Old DN.
                //
                Rename_DNcount++;
                String formulatedOldDN = _ParentOldDN;
                if (Rename_DNcount != 1) {
                    // ****************************************
                    // Calculate the depth we need to obtain
                    // with Parent and Current DN.
                    //
                    int PrefixSize = ((pDN.depth() - Naming_Source.depth()) - 1);
                    String childRDN = "";

                    // ***************************************************************
                    // Parse for a specific depth of the DN.
                    //
                    idxParseDN XDN = new idxParseDN(pDN.getDN());
                    for (int zI = 0; zI <= PrefixSize; zI++) {
                        if ((XDN.getPDN() == null) ||
                                ("".equalsIgnoreCase(XDN.getPDN()))) {
                            break;
                        }

                        if ((childRDN == null) ||
                                (childRDN.equalsIgnoreCase(""))) {
                            childRDN = XDN.getRDN();
                        } else {
                            childRDN = childRDN + "," + XDN.getRDN();
                        }

                        XDN = new idxParseDN(XDN.getPDN());
                    } // End of For Loop.

                    // **************************************
                    // Fully Formulated Old Child DN.
                    formulatedOldDN = childRDN + "," + _ParentOldDN;
                    ;

                    // *******************************************
                    // Place Entry into TreeMap.
                    LEAFmap.put(pDN.getX500Name(), formulatedOldDN);

                } // End of Inner If.
            } // End of While Loop.

        } catch (NameNotFoundException nfe) {
            return (LEAFmap);
        } // End of exception

        // ********************************
        // Return the TreeMap.
        return (LEAFmap);

    } // End of obtainLeafEntries.

    /**
     * convertX500NameToLDAPName.
     *
     * @param _x500name X500Name
     * @return String LDAPName
     */
    private String convertX500NameToLDAPName(String _x500name) {

        // ***************************************
        // Initialize.
        String _ldapname = "";

        // ***************************************
        // Now Parse out the X500 Domains to
        // Create the GLuE Nodes.
        //
        StringTokenizer NODES = new StringTokenizer(_x500name, "/");
        while (NODES.hasMoreTokens()) {
            String node = (String) NODES.nextToken();
            if ((node == null) || (node.equals(""))) {
                continue;
            }

            // **********************************
            // Place the Node at the Begining of
            // the LDAP Name.
            if (_ldapname.equals("")) {
                _ldapname = node;
            } else {
                _ldapname = node + "," + _ldapname;
            }
        } // End of While.

        // ***************************************
        // Return the LDAP Name.
        return (_ldapname);
    } // End of convertX500NameToLDAPName.

} // End of Class IRRChangePoller
