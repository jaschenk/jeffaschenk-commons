package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.io.BufferedWriter;

import javax.naming.CompoundName;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.NoSuchAttributeException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * Java class for supporting utility functions, such as Copy, Move and
 * deletion of Entries. Can be called from EJB or standard JVM environment.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxIRRutil {
    private static String MP = "idxIRRutil: ";

    public static String[] OpAttrIDs = {"CreateTimeStamp",
            "CreatorsName",
            "ModifyTimeStamp",
            "ModifiersName", "*"};

    public static String[] NO_Attributes = {"1.1"};

    public static String[] OC_Attribute = {"objectclass"};

    private boolean VERBOSE = false;

    private boolean DEBUG = false;

    private idxIRRschema SCHEMA = null;   // IRR Directory Schema.
    private boolean schemaAvailable = false;

    private idxStatus STATUS = null;   // Internal Status.

    // *****************************************************
    // Initial Constructor used when no argument supplied.
    public idxIRRutil() {
        STATUS = new idxStatus("idxIRRutil");
    } // end of Constructor

    // *****************************************************
    // Initial Constructor used when arguments supplied.
    public idxIRRutil(idxIRRschema _SCHEMA) {
        SCHEMA = _SCHEMA;
        schemaAvailable = true;
        STATUS = new idxStatus("idxIRRutil");
    } // end of Constructor

    // *****************************************************
    // Method to Set VERBOSE Indicator.
    public void setVerbose(boolean _verbose) {
        VERBOSE = _verbose;
    } // end of Method

    // *****************************************************
    // Method to Set DEBUG Indicator.
    public void setDebug(boolean _debug) {
        DEBUG = _debug;
    } // end of Method

    /**
     * Set the correct Message Prefix for this instance of the Function Utility.
     *
     * @param String Name of Message Prefix.
     */
    public void setMP(String _mp) {
        if (_mp != null) {
            MP = _mp + ": ";
        }
    } // End of setMP Method.

    /**
     * Deletes Entry from IRR Directory.  Performs a JNDI unbind operation
     * for the specified DN.
     *
     * @param DirContext current established JNDI Directory Context
     * @param String     DN to be deleted.
     * @param boolean    Verbose Indicator.
     * @param idxStatus  Status Class.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void DeleteEntry(DirContext _ctxSource,
                            String _DNSource,
                            idxStatus _Status)
            throws idxIRRException {

        // *****************************************
        // Now Delete Entry.
        //
        _Status.setLastOp("DeleteEntry");
        _Status.setLastOpResource(_DNSource);
        _Status.setLastOpStatus(-1);
        try {
            if (canEntryBeDeleted(_DNSource)) {
                idxParseDN Naming_Dest = new idxParseDN(_DNSource);
                _DNSource = Naming_Dest.getDNwithQuotes();
                _ctxSource.unbind(_DNSource);

                _Status.AccumCounter("DeletedEntries");
                _Status.setLastOpStatus(1);

                if (VERBOSE) {
                    System.out.println(MP + "DeleteEntry:[" + _DNSource + "], Successful.");
                }
            } else {
                if (VERBOSE) {
                    System.out.println(MP + "DeleteEntry:[" + _DNSource + "], Not Allowed to be Deleted.");
                }
                _Status.AccumCounter("NonDeletableEntries");
                _Status.setLastOpStatus(2);
                return;
            } // End of Else.

        } catch (NameNotFoundException e) {
            if (VERBOSE) {
                System.err.println(MP + "DeleteEntry: DN Entry was Not Found for Delete, " + e);
            }
            _Status.AccumCounter("DeleteErrors");
            _Status.setLastOpStatus(3);
            return;
        } // End of exception
        catch (Exception e) {
            _Status.setLastOpStatus(0);
            _Status.AccumCounter("OtherErrors");
            throw new idxIRRException("DeleteEntry() Error Performing IRR Delete,\n" + e);
        } // End of exception

    } // End of DeleteEntry


    /**
     * Obtains all of an Entry's children DNs contained in the IRR.
     *
     * @param DirContext    current established JNDI Directory Context
     * @param String        DN for which children are to be discovered.
     * @param idxDNLinkList used to supply returned children for entry.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void AcquireChildren(DirContext ctxSource,
                                String DNSource,
                                idxDNLinkList dnLevelList)
            throws Exception {

        // ***************************
        // Create my Internal List.
        idxDNLinkList myChildrenList = new idxDNLinkList();

        // ***************************
        // Obtain First Level
        try {
            addChildrentoList(ctxSource,
                    DNSource,
                    myChildrenList,
                    true);
        } catch (Exception e) {
            throw e; // Re-throw the exception.
        } // End of exception

        // ********************************
        // Obtain all Subsequent Levels
        // Pop it off and save it and then
        // obtain next level.
        while (myChildrenList.IsNotEmpty()) {
            String poppedDN = myChildrenList.popfirst();
            // *****************************
            // Now place the Childs DN in
            // the Queue to process it's
            // Children on the next iteration.
            //
            dnLevelList.addLast(poppedDN);
            try {
                addChildrentoList(ctxSource,
                        poppedDN,
                        myChildrenList,
                        false);
            } catch (Exception e) {
                throw e; // Re-throw Exception.
            } // End of exception

        } // End of While Loop.

    } // End of AcquireChildren

    /**
     * Copies an entry from one Directory Context to another as well as
     * to a different container.
     *
     * @param DirContext current established Source JNDI Directory Context
     * @param String     DN of source entry.
     * @param DirContext current established Destination JNDI Directory Context
     * @param String     DN of Destination entry.
     * @param boolean    indicator to determine if existing destination entry
     *                   should be deleted or not.
     * @param Attributes JNDI Attributes Object for Entry to be copied.
     * @param idxStatus  Source Common Status Object.
     * @param idxStatus  Destination Common Status Object.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void CopyEntry(DirContext ctxSource,
                          String DNSource,
                          DirContext ctxDest,
                          String DNDest,
                          boolean OVERWRITE_DESTINATION_ENTRY,
                          Attributes entryattrs,
                          idxStatus _StatusSource,
                          idxStatus _StatusDest)
            throws idxIRRException {

        _StatusSource.setLastOp("CopyEntry");
        _StatusSource.setLastOpResource(DNSource);
        _StatusSource.setLastOpStatus(-1);

        _StatusDest.setLastOp("CopyEntry");
        _StatusDest.setLastOpResource(DNDest);
        _StatusDest.setLastOpStatus(-1);

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
        // Now obtain the Source Entry.
        //
        try {
            if (entryattrs == null) {
                NamingEnumeration nes = ctxSource.search(DNSource,
                        "(objectclass=*)", OS_ctls);

                SearchResult srs = (SearchResult) nes.next();

                entryattrs = srs.getAttributes();
            }
            _StatusSource.AccumCounter("ReadEntries");
            _StatusSource.setLastOpStatus(1);

            // *****************************************
            // Now remove the Naming Attribute Values.
            if (!"".equals(Naming_Source.getNamingAttribute())) {
                entryattrs.remove(Naming_Source.getNamingAttribute());
            }
            // *****************************************
            // Parse the Destination Entry DN to be sure
            // we have any Quotes....
            idxParseDN Naming_Dest = new idxParseDN(DNDest);
            DNDest = Naming_Dest.getDNwithQuotes();

            // *****************************************
            // If schema is Available then, perform a
            // check for Entry DN Attributes that may
            // Need to change.
            //
            if ((schemaAvailable) && (SCHEMA != null)) {
                try {
                    checkforFluidDNs(ctxSource,
                            DNSource,
                            ctxDest,
                            DNDest,
                            entryattrs,
                            _StatusDest);

                } catch (Exception e_FluidDNs) {
                    _StatusDest.AccumCounter("FluidDNErrors");
                } // End of exception
                _StatusDest.setLastOp("CopyEntry");
            } // End of If Schema Available.

            // *****************************************
            // Now write out the new Destination Entry.
            if (VERBOSE) {
                System.out.println(MP + "Processing Destination Write of Entry:[" + DNDest + "]");
            }
            try {
                if (entryattrs.size() == 0) {
                    if (VERBOSE) {
                        System.out.println(MP + "Bind of Destination entry invalid, since entry is a Glue Node.");
                    } // End of Verbose If.

                    // Force User to Clean up the Source Directory.
                    throw new idxIRRException("Entry detected as a Glue Node (No Attributes) in CopyEntry()" +
                            "\n...Source DN:[" + DNSource + "]" +
                            "\n.....Dest DN:[" + DNDest + "]");

                } // end of If.

                // Proceed with Bind of Entry to Directory.
                ctxDest.bind(DNDest, null, entryattrs);

                _StatusDest.AccumCounter("AddedEntries");
                _StatusDest.setLastOpStatus(1);

            } catch (javax.naming.NameAlreadyBoundException e) {
                // ****************************************************
                // Ok, we caught ourselves adding an existing entry.
                // If our OVERWRITE Flag is Set, simple Rebind the Entry.
                if (OVERWRITE_DESTINATION_ENTRY) {
                    if (VERBOSE) {
                        System.out.println(MP + "...Existing entry detected, attempting modification of Destination Entry");
                    }
                    try {
                        ctxDest.rebind(DNDest, null, entryattrs);
                        _StatusDest.AccumCounter("ReboundEntries");
                        _StatusDest.setLastOpStatus(1);

                    } catch (javax.naming.ContextNotEmptyException e_rebind) {
                        _StatusDest.AccumCounter("NonReboundableEntries");
                        _StatusDest.setLastOpStatus(1);
                        if (VERBOSE) {
                            System.out.println(MP + "...Unable to rebind Destination entry, since entry is a non-leaf entry.");
                        } // End of Verbose If.
                        return; // This is really Ok, So return ok.

                    } catch (Exception e_rebind) {
                        _StatusDest.AccumCounter("ReboundErrors");
                        _StatusDest.setLastOpStatus(-1);
                        throw new idxIRRException("Error Performing IRR Rebind, in CopyEntry()" +
                                "\n...Source DN:[" + DNSource + "]" +
                                "\n.....Dest DN:[" + DNDest + "]" +
                                "\n" + e_rebind);
                    } // End of exception
                } else {
                    if (VERBOSE) {
                        System.out.println(MP + "Unable to Rebind Destination entry, since Overwrite flag was not set.");
                    } // End of Verbose If.
                    return; // This is really ok, So return ok.
                } // End of Else.

            } catch (Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                }
                throw new idxIRRException("Error Performing IRR Write, in CopyEntry()," +
                        "\n...Source DN:[" + DNSource + "]" +
                        "\n.....Dest DN:[" + DNDest + "]" +
                        "\n" + e);
            } // End of exception


        } catch (NameNotFoundException e) {
            _StatusSource.AccumCounter("ReadErrors");
            _StatusSource.setLastOpStatus(-1);
            System.out.println(MP + "Source DN Entry was Not Found," +
                    "\n...Source DN:[" + DNSource + "]" +
                    "\n.....Dest DN:[" + DNDest + "]" +
                    "\n" + e);
            return;
        } // End of exception
        catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            throw new idxIRRException("Error Performing IRR Function, in CopyEntry()," +
                    "\n...Source DN:[" + DNSource + "]" +
                    "\n.....Dest DN:[" + DNDest + "]" +
                    "\n" + e);
        } // End of exception

        return;

    } // End of CopyEntry


    /**
     * Copies all children entries from one Directory Context to another as well as
     * to a different container.
     *
     * @param DirContext    current established Source JNDI Directory Context
     * @param String        DN of source entry.
     * @param DirContext    current established Destination JNDI Directory Context
     * @param String        DN of Destination entry.
     * @param boolean       indicator to determine if existing destination entry
     *                      should be deleted or not.
     * @param idxDNLinkList containing all children DNs to be copied.
     * @param idxStatus     Common Status for Source Object.
     * @param idxStatus     Common Status for Destination Object.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void CopyChildren(DirContext ctxSource,
                             String DNSource,
                             DirContext ctxDest,
                             String DNDest,
                             boolean OVERWRITE_DESTINATION_ENTRY,
                             idxDNLinkList dnLevelList,
                             idxStatus _StatusSource,
                             idxStatus _StatusDest)
            throws idxIRRException {

        SearchControls OL_ctls = new SearchControls();
        OL_ctls.setReturningAttributes(NO_Attributes);
        OL_ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(DNSource);
        DNSource = Naming_Source.getDNwithQuotes();

        // *****************************************
        // Now obtain the Source Entry.
        //
        try {
            NamingEnumeration nes = ctxSource.search(DNSource,
                    "(objectclass=*)", OL_ctls);

            while (nes.hasMore()) {
                SearchResult srs = (SearchResult) nes.next();
                String RDN = srs.getName();

                // *******************************
                // Acquire the correct Source DNs.
                String ThisSourceDN = null;
                if ((DNSource != null) &&
                        (!"".equals(DNSource))) {
                    ThisSourceDN = RDN + "," + DNSource;
                } else {
                    ThisSourceDN = RDN;
                }

                // *******************************
                // Acquire the correct Source DNs.
                String ThisDestDN = null;
                if ((DNDest != null) &&
                        (!"".equals(DNDest))) {
                    ThisDestDN = RDN + "," + DNDest;
                } else {
                    ThisDestDN = RDN;
                }

                // *****************************
                // Perform the Child Copy
                // Let the CopyEntry Method
                // Obtain the entire Object
                // Entry.
                CopyEntry(ctxSource, ThisSourceDN,
                        ctxDest, ThisDestDN,
                        OVERWRITE_DESTINATION_ENTRY,
                        null,
                        _StatusSource,
                        _StatusDest);

                // *****************************
                // Now place the Childs DN in
                // the Queue to process it's
                // Children on the next iteration.
                //
                dnLevelList.addLast(ThisSourceDN);

            } // End of While.

        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            throw new idxIRRException("Error Performing IRR Search for Children, in CopyChildren()");
        } // End of exception

        return;

    } // End of CopyChildren

    /**
     * Determines if an Entry already Exists in the IRR Directory or not.
     *
     * @param DirContext current established Source JNDI Directory Context
     * @param String     DN of entry to be check for existence.
     * @return boolean indication of entry exists or not.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public boolean DoesEntryExist(DirContext ctx, String EntryDN)
            throws idxIRRException {

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(EntryDN);
        EntryDN = Naming_Source.getDNwithQuotes();

        try {
            ctx.lookup(EntryDN);
        } catch (javax.naming.NamingException e) {
            return (false);
        } // End of Exception.
        catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            throw new idxIRRException("Error Performing IRR Lookup, in DoesEntryExist(),\n" + e);
        } // End of Exception

        return (true);

    } // End of DoesEntryExist class.

    /**
     * Deletes Existing entries from the IRR Directory.
     * This is a prepartory step for a Copy or Move.
     *
     * @param DirContext current established Source JNDI Directory Context
     * @param String     DN of entry to be Deleted.
     * @param boolean    Indicator for Child Entries are to be deleted.
     * @param idxStatus  Status Class.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void DeleteExistingEntries(DirContext ctxSource,
                                      String ENTRY_SOURCE_DN,
                                      boolean DELETE_WITH_CHILDREN,
                                      idxStatus _Status)
            throws idxIRRException {

        // *******************************************
        // Now Delete our Source Entry, if no children
        // are to be deleted.
        if (!DELETE_WITH_CHILDREN) {
            try {
                DeleteEntry(ctxSource, ENTRY_SOURCE_DN, _Status);
            } catch (Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                }
                throw new idxIRRException("Error IRR Deleting Child Entry,in DeleteExistingEntries(),\n" + e);
            } // End of exception
        } // End of if
        // ****************************************
        // Delete Children
        //
        else {
            idxDNLinkList myChildrenList = new idxDNLinkList();

            // *******************************
            // Acquire all Children Entries
            try {
                AcquireChildren(ctxSource, ENTRY_SOURCE_DN,
                        myChildrenList);
            } catch (Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                }
                throw new idxIRRException("Error IRR Deleting Child Entry,in DeleteExistingEntries(),\n" + e);
            } // End of exception

            // ***************************
            // Delete All Children in
            // reverse order.
            while (myChildrenList.IsNotEmpty()) {
                String RDN = myChildrenList.poplast();
                try {
                    DeleteEntry(ctxSource, RDN, _Status);
                } catch (Exception e) {
                    if (DEBUG) {
                        e.printStackTrace();
                    }
                    throw new idxIRRException("Error IRR Deleting Child Entry,in DeleteExistingEntries(),\n" + e);
                } // End of exception

            } // End of While Loop.
        } // End of Else.

        // *************************************
        // Return to Caller.
        return;

    } ///: End of DeleteExistingEntries Class.

    /**
     * Determines if an Entry can be deleted.  Some entries can not without
     * causing disruption in the DIT as well as part of the internal Access
     * control which is not exposed via LDAP.  Access Control is only
     * accessible from the X.500 or from DAP.
     *
     * @param String DN of entry can be deleted or not.
     * @return boolean indication entry can be deleted or not.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public boolean canEntryBeDeleted(String EntryDN)
            throws idxIRRException {

        // ****************************************
        // Now that we have a compound name
        // Determine if we can delete the Entry...
        idxParseDN pDN = new idxParseDN(EntryDN);

        // Is this Entry the ICOSDSA Organization?
        // If so, do not allow the delete.
        if ((!"".equals(pDN.getNamingAttribute())) &&
                ("o".equalsIgnoreCase(pDN.getNamingAttribute())) &&
                ("icosdsa".equalsIgnoreCase(pDN.getNamingValue()))) {
            return (false);
        }

        // Is this Entry start with a domain Container?
        // If so, do not allow the delete.
        if ((!"".equals(pDN.getNamingAttribute())) &&
                ("dc".equalsIgnoreCase(pDN.getNamingAttribute()))) {
            return (false);
        }

        // Is this Entry the Framework Organizational Unit?
        // If so, do not allow the delete.
        if ((!"".equals(pDN.getNamingAttribute())) &&
                ("ou".equalsIgnoreCase(pDN.getNamingAttribute())) &&
                ("framework".equalsIgnoreCase(pDN.getNamingValue()))) {
            return (false);
        }

        // Is this Entry an Framework hidden Security Entry?
        // These are named "cn=framework....,ou=framework,...
        // If so, do not allow the delete.
        if ((!"".equals(pDN.getNamingAttribute())) &&
                ("cn".equalsIgnoreCase(pDN.getNamingAttribute()))) {
            String CommonName = pDN.getNamingValue();
            CommonName = CommonName.toLowerCase();

            String PDN = pDN.getPDN();

            CompoundName pName = null;
            CompoundName tName = null;
            idxNameParser myParser = new idxNameParser();

            try {
                pName = myParser.parse(PDN);
                tName = myParser.parse("ou=framework");
            } catch (Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                }
                throw new idxIRRException("Error Formulating Compound Name in canEntryBeDeleted(),\n" + e);
            } // End of exception

            if ((CommonName.equals("framework user")) &&
                    (pName.startsWith(tName))) {
                return (false);
            } else if ((CommonName.equals("framework process master")) &&
                    (pName.startsWith(tName))) {
                return (false);
            }

        } // End of If CommonName


        // Is this Entry the ICOSDSA IRRADMIN Admin Entry?
        // This is named "cn=irradmin,o=icosdsa
        // If so, do not allow the delete.
        if ((!"".equals(pDN.getNamingAttribute())) &&
                ("cn".equalsIgnoreCase(pDN.getNamingAttribute()))) {
            String CommonName = pDN.getNamingValue();
            CommonName = CommonName.toLowerCase();

            String PDN = pDN.getPDN();

            CompoundName pName = null;
            CompoundName tName = null;
            idxNameParser myParser = new idxNameParser();

            try {
                pName = myParser.parse(PDN);
                tName = myParser.parse("o=icosdsa");
            } catch (Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                }
                throw new idxIRRException("Error Formulating Compound Name in canEntryBeDeleted(),\n" + e);
            } // End of exception

            if ((CommonName.equals("irradmin")) &&
                    (pName.startsWith(tName))) {
                return (false);
            }

        } // End of If CommonName for IRRADMIN.

        // *******************************
        // Falling through indicates the
        // Entry can be deleted.
        return (true);

    } // End of canEntryBeDeleted class.

    /**
     * Determines if the any of the Attributes are of FluidDN or DN Syntax
     * and then attempts to properly resolve those values.
     *
     * @param DirContext current established Source JNDI Directory Context
     * @param String     DN of source entry.
     * @param DirContext current established Destination JNDI Directory Context
     * @param String     DN of Destination entry.
     * @param Attributes JNDI Attributes Object for Entry to be copied.
     * @param idxStatus  Destination Common Status Object.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void checkforFluidDNs(DirContext ctxSource,
                                 String DNSource,
                                 DirContext ctxDest,
                                 String DNDest,
                                 Attributes entryattrs,
                                 idxStatus _StatusDest)
            throws idxIRRException {

        _StatusDest.setLastOp("checkforFluidDNs");
        _StatusDest.setLastOpResource(DNDest);
        _StatusDest.setLastOpStatus(-1);

        // ********************************************
        // Now check each attribute for syntax and
        // if the syntax is a DN, then we need to
        // perform further processing.
        //
        try {
            // ****************************************
            // Now parse out the incoming DNs.
            idxParseDN sDN = new idxParseDN(DNSource);
            idxParseDN dDN = new idxParseDN(DNDest);

            for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                Attribute attr = (Attribute) ea.next();
                String Syntax = SCHEMA.getAttributeSyntaxName(attr.getID());
                if (!"DN".equalsIgnoreCase(Syntax)) {
                    continue;
                }

                // ********************************************************
                // Create new Attribute to contain Destination Changes.
                //
                boolean fixed = false;
                Attribute newFvalues = new BasicAttribute(attr.getID());

                // ********************************************************
                // Yes we do have an Attribute with DN Syntax.
                // Now determine if DN needs or can be fixed to properly
                // reflect the entries new destination.
                //
                for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                    String currentfDN = (String) ev.next();
                    idxParseDN fDN = new idxParseDN(currentfDN);
                    _StatusDest.AccumCounter("FluidDNDetections");
                    if (VERBOSE) {
                        System.out.println(MP + "Fluid DN Found for " + attr.getID() +
                                " (Syntax=" + Syntax + ")" +
                                ": [" + fDN.getDN() + "]");
                    } // End of Verbose.

                    // ************************************************************************
                    // First Fix Domain Traversal.
                    // Set up Compound names for tests.
                    // fName will be CompoundName Attribute Contents which was a DN.
                    // dName will be CompoundName of the Destination DN, which was formulated.
                    // sName will be CompoundName of the Source DN, which was read.
                    //
                    CompoundName fName = null;
                    CompoundName dName = null;
                    CompoundName sName = null;
                    idxNameParser myParser = new idxNameParser();

                    // ***************************************************************
                    // Create a CompoundName from the Attribute Contents we detected.
                    // Using Only the Domain.
                    try {
                        fName = myParser.parse(fDN.getDomain());
                    } catch (Exception e) {
                        if (DEBUG) {
                            e.printStackTrace();
                        }
                        _StatusDest.AccumCounter("FluidDNErrors");
                        throw new idxIRRException("Error Formulating fName Compound Name in checkforFluidDNs(),\n" + e);
                    } // End of exception

                    // ***************************************************************
                    // Create a CompoundName from the Destination DN of the new Entry.
                    // Using Only the Domain.
                    try {
                        dName = myParser.parse(dDN.getDomain());
                    } catch (Exception e) {
                        if (DEBUG) {
                            e.printStackTrace();
                        }
                        _StatusDest.AccumCounter("FluidDNErrors");
                        throw new idxIRRException("Error Formulating dName Compound Name in checkforFluidDNs(),\n" + e);
                    } // End of exception

                    // ***************************************************************
                    // Create a CompoundName from the Source DN of the Current Entry.
                    // Using Only the Domain.
                    try {
                        sName = myParser.parse(sDN.getDomain());
                    } catch (Exception e) {
                        if (DEBUG) {
                            e.printStackTrace();
                        }
                        _StatusDest.AccumCounter("FluidDNErrors");
                        throw new idxIRRException("Error Formulating sName Compound Name in checkforFluidDNs(),\n" + e);
                    } // End of exception

                    // ***********************************************
                    // Is the Domain of the Source the same as
                    // the Domain of the Attribute DN and
                    // Source and Destination Domains are different?
                    //
                    if ((fName.equals(sName)) &&
                            (!sName.equals(dName))) {
                        // *******************
                        // Yes, now fix it.
                        fixed = true;
                        _StatusDest.AccumCounter("FluidDNDomainTraversals");
                        if ((!"".equals(fDN.getDNLessDomain())) &&
                                (!"".equals(dDN.getDomain()))) {
                            currentfDN = fDN.getDNLessDomain() + "," + dDN.getDomain();
                        } else if (("".equals(fDN.getDNLessDomain())) &&
                                (!"".equals(dDN.getDomain()))) {
                            currentfDN = dDN.getDomain();
                        } else {
                            currentfDN = fDN.getDNLessDomain();
                        }

                        if (VERBOSE) {
                            System.out.println(MP + "Fixed Domain Traversal for " + attr.getID() +
                                    ": [" + currentfDN + "]");
                        } // End of Verbose.

                    } // End of If.

                    // **********************************
                    // Second Fix child entry Traversal.
                    // Set up Compound names for tests.
                    //
                    fDN = new idxParseDN(currentfDN);
                    try {
                        fName = myParser.parse(fDN.getDNLessDomain());
                        dName = myParser.parse(dDN.getDNLessDomain());
                        sName = myParser.parse(sDN.getDNLessDomain());
                    } catch (Exception e) {
                        if (DEBUG) {
                            e.printStackTrace();
                        }
                        _StatusDest.AccumCounter("FluidDNErrors");
                        throw new idxIRRException("Error Formulating Compound Name in checkforFluidDNs(),\n" + e);
                    } // End of exception

                    if ((fName.endsWith(sName)) &&
                            (!sName.equals(dName))) {
                        // *******************
                        // Yes, now fix it.
                        int x = currentfDN.toLowerCase().indexOf(sDN.getDNLessDomain().toLowerCase());
                        if (x != -1) {
                            fixed = true;
                            _StatusDest.AccumCounter("FluidDNChildTraversals");
                            if ((!"".equals(dDN.getDNLessDomain())) &&
                                    (!"".equals(fDN.getDomain()))) {
                                currentfDN = currentfDN.substring(0, x) + dDN.getDNLessDomain() +
                                        "," + fDN.getDomain();
                            } else if (("".equals(dDN.getDNLessDomain())) &&
                                    (!"".equals(fDN.getDomain()))) {
                                currentfDN = currentfDN.substring(0, x) +
                                        "," + fDN.getDomain();
                            } else {
                                currentfDN = currentfDN.substring(0, x);
                            }

                            if (VERBOSE) {
                                System.out.println(MP + "Fixed Child Traversal for " + attr.getID() +
                                        ": [" + currentfDN + "]");
                            } // End of Verbose.
                        } /// End of Inner If.
                    } // End of outer if.

                    // **********************************
                    // Finally stuff attribute into new
                    // Attribute context.
                    //
                    newFvalues.add(currentfDN);

                } // End of Inner For Loop.

                // **********************************
                // Now remove existing Attribute
                // and Replace with the new value set.
                //
                entryattrs.remove(newFvalues.getID());
                entryattrs.put(newFvalues);
                if (fixed) {
                    _StatusDest.AccumCounter("FluidDNModifications");
                }

            } // End of Outer For Loop

        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            _StatusDest.AccumCounter("FluidDNErrors");
            throw new idxIRRException("Exception in checkforFluidDNs(),\n" + e);
        } // End of Exception.

        _StatusDest.setLastOpStatus(1);

    } // End of checkforFluidDNs Method.


    /**
     * Obtains Entry from Directory Context and lets an Output Write Class
     * write the data out.  Usually to a file or Standard output.
     *
     * @param DirContext     current established Source JNDI Directory Context
     * @param String         DN of source entry.
     * @param String         Search Filter.
     * @param BufferedWriter Output Writer class search results sent.
     * @param boolean        NICE DN Output Indicator.
     * @param idxStatus      Source Common Status Object.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void ObtainEntryForOutput(DirContext ctxSource,
                                     String DNSource,
                                     String SearchFilter,
                                     BufferedWriter LDIFOUT,
                                     boolean _NICE,
                                     idxStatus _StatusSource)
            throws idxIRRException {

        //******************************************
        // Initialize Status Counter.
        _StatusSource.setLastOp("ObtainEntryforOutput");
        _StatusSource.setLastOpResource(DNSource);
        _StatusSource.setLastOpStatus(-1);

        //******************************************
        // Make sure we have a valid DN.
        // If the Source Entry is Blank or caller
        // trying to obtain ROOT, just ignore and
        // return gracefully.
        //
        if (("".equals(DNSource)) ||
                (DNSource == null)) {
            return;
        }

        //******************************************
        // Set up our Search Filter.
        if (SearchFilter == null) {
            SearchFilter = "(objectclass=*)";
        }

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
        try {
            int Count = idxIRROutput.LDIFSearchList(
                    ctxSource.search(DNSource, SearchFilter, OS_ctls),
                    NameSpace, LDIFOUT, _NICE);
            for (; Count > 0; Count--) {
                _StatusSource.AccumCounter("ReadEntries");
            }
            _StatusSource.setLastOpStatus(1);

        } catch (NameNotFoundException e) {
            _StatusSource.AccumCounter("ReadErrors");
            _StatusSource.setLastOpStatus(-1);
            throw new idxIRRException("Source DN was not found, in ObtainEntryForOutput()");
        } // End of exception
        catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            throw new idxIRRException("Error Performing IRR Function, in ObtainEntryForOutput(),\n" + e);
        } // End of exception

    } // End of ObtainEntryForOutput

    /**
     * Obtains Children Entries from the IRR Directory for output
     * one level at a time for speed, such as a used in a Backup.
     * This class will only obtain entries one level at a time and
     * objectclass attributes to speed up the search.  Almost simulates
     * a paged results control, but this does have more overhead,
     * since we are obtaining the entry twice.  But this allows us to
     * apply a secondary filter.  We are only gettting the DN and
     * objectclass on the first call, which should provide for a very quick
     * access.
     *
     * @param DirContext     current established Source JNDI Directory Context
     * @param String         Source DN of the base entry to be read.
     * @param String         Filter to be used to acquire entries.
     * @param BufferedWriter Output Writer class search results sent.
     * @param boolean        NICE LDIF Output Indicator.
     * @param idxDNLinkList  containing all children DNs to be copied.
     * @param idxStatus      Status Class.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void ObtainChildrenForOutput(DirContext ctxSource,
                                        String DNSource,
                                        String _SearchFilter,
                                        BufferedWriter LDIFOUT,
                                        boolean _NICE,
                                        idxDNLinkList dnLevelList,
                                        idxStatus _StatusSource)
            throws idxIRRException {

        // ****************************************
        // Obtain Children
        //
        SearchControls OL_ctls = new SearchControls();
        OL_ctls.setReturningAttributes(NO_Attributes);
        OL_ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(DNSource);
        DNSource = Naming_Source.getDNwithQuotes();

        // *****************************************
        // Now obtain the Source Entry.
        //
        try {
            NamingEnumeration nes = ctxSource.search(DNSource,
                    "(objectclass=*)", OL_ctls);

            while (nes.hasMore()) {
                SearchResult srs = (SearchResult) nes.next();
                String RDN = srs.getName();

                // *****************************
                // Acquire the correct DNs.
                String ThisSourceDN = null;
                if ((DNSource != null) &&
                        (!"".equals(DNSource))) {
                    ThisSourceDN = RDN + "," + DNSource;
                } else {
                    ThisSourceDN = RDN;
                }

                // *****************************
                // Object the Child Entry
                ObtainEntryForOutput(ctxSource,
                        ThisSourceDN,
                        _SearchFilter,
                        LDIFOUT,
                        _NICE,
                        _StatusSource);

                // *****************************
                // Now place the Childs DN in
                // the Queue to process it's
                // Children on the next iteration.
                //
                dnLevelList.addLast(ThisSourceDN);

            } // End of While.

        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            throw new idxIRRException("Error Performing IRR Search for Children, in ObtainChildrenForOutput(),\n" + e);
        } // End of exception

    } ///: End of ObtainChildrenForOutput Class.


    /**
     * Obtains Children Entries from the IRR Directory for Queuing
     * one level at a time for speed, such as a used in a Backup.
     * This class will only obtain entries one level at a time and
     * objectclass attributes to speed up the search.  Almost simulates
     * a paged results control, but this does have more overhead,
     * since we are obtaining the entry twice.  But this allows us to
     * apply a secondary filter.  We are only gettting the DN and
     * objectclass on the first call, which should provide for a very quick
     * access.
     *
     * @param DirContext    current established Source JNDI Directory Context
     * @param String        Source DN of the base entry to be read.
     * @param String        Filter to be used to acquire entries.
     * @param idxDNLinkList containing all children DNs to be copied.
     * @param idxStatus     Status Class.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void ObtainChildrenForQueue(DirContext ctxSource,
                                       String DNSource,
                                       String _SearchFilter,
                                       idxDNLinkList dnLevelList,
                                       idxStatus _StatusSource)
            throws idxIRRException {

        // ****************************************
        // Obtain Children
        //
        SearchControls OL_ctls = new SearchControls();
        OL_ctls.setReturningAttributes(NO_Attributes);
        OL_ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(DNSource);
        DNSource = Naming_Source.getDNwithQuotes();

        // *****************************************
        // Now obtain the Source Entry.
        //
        try {
            NamingEnumeration nes = ctxSource.search(DNSource,
                    "(objectclass=*)", OL_ctls);

            while (nes.hasMore()) {
                SearchResult srs = (SearchResult) nes.next();
                String RDN = srs.getName();

                // *****************************
                // Acquire the correct DNs.
                String ThisSourceDN = null;
                if ((DNSource != null) &&
                        (!"".equals(DNSource))) {
                    ThisSourceDN = RDN + "," + DNSource;
                } else {
                    ThisSourceDN = RDN;
                }

                // *****************************
                // Now place the Childs DN in
                // the Queue to process it's
                // Children on the next iteration.
                //
                dnLevelList.addLast(ThisSourceDN);

            } // End of While.

        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            throw new idxIRRException("Error Performing IRR Search for Children, in ObtainChildrenForQueue(),\n" + e);
        } // End of exception

    } ///: End of ObtainChildrenForQueue Class.

    /**
     * Obtains Children Entries from the IRR Directory by walking
     * one level at a time for speed, such as a used in a Backup.
     * This class will only obtain entries one level at a time and
     * objectclass attributes to speed up the search.  Almost simulates
     * a paged results control, but this does have more overhead,
     * since we are obtaining the entry twice.  But this allows us to
     * apply a secondary filter.  We are only gettting the DN and
     * objectclass on the first call, which should provide for a very quick
     * access.
     *
     * @param DirContext    current established Source JNDI Directory Context
     * @param String        Source DN of the base entry to be read.
     * @param String        Filter to be used to acquire entries.
     * @param idxDNLinkList containing all children DNs to be copied.
     * @param boolean       indicates whether or not to ignore a note found condition.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void addChildrentoList(DirContext ctxSource,
                                  String DNSource,
                                  idxDNLinkList dnLevelList,
                                  boolean _IgnoreNotFound)
            throws idxIRRException {

        // ****************************************
        // Obtain Children
        //
        SearchControls OL_ctls = new SearchControls();
        OL_ctls.setReturningAttributes(NO_Attributes);
        OL_ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        // *****************************************
        // Parse the Destination Entry DN to be sure
        // we have any Quotes....
        idxParseDN Naming_Source = new idxParseDN(DNSource);
        DNSource = Naming_Source.getDNwithQuotes();

        // *********************************************
        // Now obtain the Source Entries at this level.
        //
        try {
            NamingEnumeration nes = ctxSource.search(DNSource,
                    "(objectclass=*)", OL_ctls);

            while (nes.hasMore()) {
                SearchResult srs = (SearchResult) nes.next();
                String RDN = srs.getName();

                // *****************************
                // Acquire the correct DNs.
                String ThisSourceDN = null;
                if ((DNSource != null) &&
                        (!"".equals(DNSource))) {
                    ThisSourceDN = RDN + "," + DNSource;
                } else {
                    ThisSourceDN = RDN;
                }

                // *****************************
                // Now place the Childs DN in
                // the Queue to process it's
                // Children on the next iteration.
                //
                dnLevelList.addLast(ThisSourceDN);

            } // End of While.

        } catch (NameNotFoundException e) {
            if (!_IgnoreNotFound) {
                throw new idxIRRException("NameNotFoundException for entry of [" +
                        DNSource +
                        "] while performing IRR Search for Children, in addChildrentoList()");
            }
        } catch (Exception e) {
            throw new idxIRRException("Exception Performing IRR Search for Children, in addChildrentoList(),\n" + e);
        } // End of exception

    } ///: End of addChildrentoList Class.

    /**
     * Removes a Attribute from a Directory Entry.
     *
     * @param DirContext current established Source JNDI Directory Context
     * @param String     current DN of Entry which is to be removed.
     * @param String     current Attribute Name to be removed.
     * @param boolean    indicates whether or not to ignore a NoSuchAttribute Exception.
     * @throws idxIRRException if any non-recoverable errors encountered.
     */
    public void RemoveAttribute(DirContext ctxSource,
                                String SourceDN,
                                String AttributeName,
                                boolean _IgnoreNoSuchAttribute)
            throws idxIRRException {

        ;
        try {

            ModificationItem[] irrmods = new ModificationItem[1];
            irrmods[0] = new ModificationItem(
                    DirContext.REMOVE_ATTRIBUTE,
                    new BasicAttribute(AttributeName));

            ctxSource.modifyAttributes(SourceDN, irrmods);

        } catch (NoSuchAttributeException nsae) {
            if (_IgnoreNoSuchAttribute) {
                return;
            }
            throw new idxIRRException("Exception Performing IRR Removal of Attribute[" +
                    AttributeName + "], from Entry[" +
                    SourceDN + "],\n" + nsae);

        } catch (Exception e) {
            throw new idxIRRException("Exception Performing IRR Removal of Attribute[" +
                    AttributeName + "], from Entry[" +
                    SourceDN + "],\n" + e);
        } // End of Exception.
    } ///: End of RemoveAttribute.

} ///:~ End of idxIRRutil Class
