package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.util.Hashtable;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

/**
 * Java class for Adding and Deleting a Simple Test Entry in the Directory.
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2002
 */

public class idxIRRXEntry {

    private static String MP = "idxIRRXEntry: ";

    private static String DEFAULT_NAME = "xen=TestEntry";

    private boolean VERBOSE = false;

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxIRRXEntry() {
    } // end of Constructor

    /**
     * Method to Set VERBOSE Indicator.
     *
     * @param _verbose Indicator to set VERBOSE.
     */
    public void setVerbose(boolean _verbose) {
        VERBOSE = _verbose;
    } // end of Method

    /**
     * Method to get VERBOSE Indicator.
     *
     * @return boolean indicator of VERBOSE.
     */
    public boolean getVerbose() {
        return (VERBOSE);
    } // end of Method

    /**
     * Generic Private method class to
     * write/bind entry to Directory Instance.
     *
     * @param ctx current established Directory Context.
     * @param EntryDN     current fully qualified DN to be written.
     * @param attrs Attribute Enumeration Set for Entry.
     * @return boolean indication of operation successful or not.
     */
    private boolean writeEntry(DirContext ctx,
                               String EntryDN,
                               Attributes attrs)
            throws idxIRRException {

        // ***********************************
        // Perform bind
        try {
            if (VERBOSE) {
                System.out.println(MP + "Adding:[" + EntryDN + "]");
            }

            if (attrs.size() == 0) {
                System.err.println(MP + "IRR Error with entry of [" +
                        EntryDN +
                        "], no Attributes for Entry, looks like a Glue Node.");

                return (false);

            } // end of If.

            // Proceed with Bind.
            ctx.bind(EntryDN, null, attrs);

        } catch (javax.naming.NameAlreadyBoundException e) {
            // ****************************************************
            // Ok, we caught ourselves adding an existing entry.
            // Try and rebind it.
            //
            if (VERBOSE) {
                System.out.println(MP + "Existing entry detected," +
                        " for " + EntryDN + ", attempting to rebind entry.");
            } // End of If.

            // rebind entry.
            try {
                ctx.rebind(EntryDN, null, attrs);
            } catch (Exception re) {
                throw new idxIRRException("Exception within writeEntry()," + re);
            } // End of Exception.
        } catch (Exception e) {
            throw new idxIRRException("Exception within writeEntry()," + e);
        } // End of exception

        // **********************
        // Return
        return (true);

    } // End of writeEntry class.

    /**
     * Generic Private method class to
     * delete/unbind entry from Directory Instance.
     *
     * @param ctx current established Directory Context.
     * @param EntryDN     current fully qualified DN to be deleted.
     * @return boolean indication of operation successful or not.
     */
    private boolean deleteEntry(DirContext ctx, String EntryDN)
            throws idxIRRException {

        // ***********************************
        // Perform bind
        try {
            if (VERBOSE) {
                System.out.println(MP + "Removing:[" + EntryDN + "]");
            }

            // Proceed with Bind.
            ctx.unbind(EntryDN);

        } catch (NameNotFoundException nnfe) {
            // *******************************
            // Return as if Deletion occurred.
            return (true);
        } catch (Exception e) {
            throw new idxIRRException("Exception within deleteEntry()," + e);
        } // End of exception

        // **********************
        // Return
        return (true);

    } // End of deleteEntry class.


    /**
     * Creates a Test Entry.
     *
     * @param ctx current established Directory Context.
     * @param Container     current Container where Entry should be placed.
     * @return boolean indication of operation successful or not.
     */
    public boolean create(DirContext ctx, String Container)
            throws idxIRRException {

        // Parse DN.
        String EntryDN = DEFAULT_NAME + "," + Container;
        idxParseDN Naming_Source = new idxParseDN(EntryDN);

        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("cnxidoXDocument");

        // Create attributes to be associated with object
        Attributes attrs = new BasicAttributes(true); // case-ignore
        attrs.put(oc);
        attrs.put("xen", Naming_Source.getNamingValue());

        // *****************************
        // Obtain the Current Timestamp
        idxTimeStamp CurrentTimeStamp = new idxTimeStamp();

        // *****************************
        // Obtain the Current Principal
        String _IRRprincipal = obtainIRRPrincipal(ctx);

        // *********************************
        // Realize the Remaining Attributes.
        attrs.put("cnxidaDesc", "TEST ENTRY FOR DIRECTORY ACCESSIBILITY FUNCTIONS");
        attrs.put("cnxidaInstallTime", CurrentTimeStamp.get());
        attrs.put("cnxidaInstallBy", _IRRprincipal);
        attrs.put("cnxidaLastModifyTime", CurrentTimeStamp.get());
        attrs.put("cnxidaLastModifyBy", _IRRprincipal);

        // Perform the Bind...
        return (writeEntry(ctx, EntryDN, attrs));

    } // End of create class.

    /**
     * Removes a Test Entry.
     *
     * @param ctx current established Directory Context.
     * @param Container     current Container where Entry should be removed from.
     * @return boolean indication of operation successful or not.
     */
    public boolean remove(DirContext ctx, String Container)
            throws idxIRRException {

        // Parse DN.
        String EntryDN = DEFAULT_NAME + "," + Container;
        idxParseDN Naming_Source = new idxParseDN(EntryDN);

        // Perform the UnBind...
        return (deleteEntry(ctx, EntryDN));
    } // End of remove class.

    /**
     * Determines if an Entry already Exists in the IRR Directory or not.
     *
     * @param ctx current established Source JNDI Directory Context
     * @param Container     Container DN of entry to be check for existence.
     * @return boolean indication of entry exists or not.
     */
    public boolean doesEntryExist(DirContext ctx, String Container)
            throws idxIRRException {

        // *****************
        // Parse DN.
        String EntryDN = DEFAULT_NAME + "," + Container;
        idxParseDN Naming_Source = new idxParseDN(EntryDN);

        // ********************************
        // Lookup the Entry.
        try {
            ctx.lookup(EntryDN);
        } catch (NamingException e) {
            return (false);
        } catch (Exception e) {
            throw new idxIRRException("Exception within doesEntryExist()," + e);
        } // End of Exception

        // *************************
        // Return.
        return (true);

    } // End of doesEntryExist class.

    /**
     * Obtains current Principal from existing Directory Context.
     *
     * @param ctx current established Directory Context.
     * @return String of IRR Principal.
     */
    private String obtainIRRPrincipal(DirContext ctx) {
        try {
            Hashtable irrenv = ctx.getEnvironment();
            return ((String) irrenv.get(DirContext.SECURITY_PRINCIPAL));
        } catch (NamingException ne) {
            return ("");
        } // End of Exception.
    } // End of obtainIRRPrincipal Method.

} ///:~ End of idxIRRXEntry Class.
