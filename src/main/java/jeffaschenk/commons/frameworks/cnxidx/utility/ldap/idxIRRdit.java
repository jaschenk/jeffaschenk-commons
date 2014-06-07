package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

/**
 * Java class for manipulation of the IRR (DIT) Directory Information Tree.
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2002
 */

public class idxIRRdit implements DataMappingConstants {

    private static final String MP = "IRRdit: ";

    private static final String DEFAULT_FRAMEWORK_CONTAINER = "ou=Framework";

    private static final String SYSTEM_CONTAINER_CLASS = "ICOSSystemContainer";

    private static final String ICOSResourceDescriptor = "ICOSResourceDescriptor";

    private static final String ICOSResourceContainerDescriptor = "ICOSResourceContainerDescriptor";

    private static final String DEFAULT_RESOURCE_CONTAINER = "rcu=Default";

    private static final String VendorObjectContainerName =
            "ou=vendorobjects" +
                    "," + DEFAULT_FRAMEWORK_CONTAINER;

    private boolean VERBOSE = false;

    private idxIRRschema SCHEMA = null;   // IRR Directory Schema.
    private boolean schemaAvailable = false;

    private idxStatus STATUS = null;   // Internal Status.

    private idxIRRVendors Vendors = null;

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxIRRdit() {
        STATUS = new idxStatus("idxIRRdit");
        STATUS.setOpStatus(1);
    } // end of Constructor

    /**
     * Initial Constructor used when arguments supplied.
     *
     * @param idxIRRschema Object class containing Existing IRR Schema.
     */
    public idxIRRdit(idxIRRschema _SCHEMA) {
        SCHEMA = _SCHEMA;
        schemaAvailable = true;
        STATUS = new idxStatus("idxIRRdit");
        STATUS.setOpStatus(1);
    } // end of Constructor

    /**
     * Initial Constructor used when arguments supplied.
     *
     * @param idxStatus Object class containing Existing Status.
     */
    public idxIRRdit(idxStatus _STATUS) {
        STATUS = _STATUS;
    } // end of Constructor

    /**
     * Method to Set VERBOSE Indicator.
     *
     * @param boolean Indicator to set VERBOSE.
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
     * Method to show Statistics Indicator.
     */
    public void getStats() {
        STATUS.show();
        return;
    } // end of Method

    /**
     * Method to get VendorObjectContainerName.
     *
     * @return String Vendor Object Container Name.
     */
    public String getVendorObjectContainerName() {
        return (VendorObjectContainerName);
    } // end of Method

    /**
     * Generic Private method class to
     * write/bind entries to formulate the Directory Information Tree or
     * DIT.
     *
     * @param DirContext current established Directory Context.
     * @param String     current fully qualified DN to be written.
     * @param Attributes Attribute Enumeration Set for Entry.
     * @return boolean indication of operation successful or not.
     */
    private boolean writeEntry(DirContext ctx,
                               String EntryDN,
                               Attributes attrs) {

        // ***********************************
        // Perform bind
        try {
            STATUS.setLastOp("idxIRRdit.writeEntry()");
            STATUS.setLastOpResource(EntryDN);
            STATUS.setLastOpStatus(0);

            if (VERBOSE) {
                System.out.println(MP + "Adding:[" + EntryDN + "]");
            }

            if (attrs.size() == 0) {
                System.err.println(MP + "IRR Error with entry of [" +
                        EntryDN +
                        "], no Attributes for Entry, looks like a Glue Node.");

                STATUS.AccumCounter("AddErrors");
                return (false);

            } // end of If.

            // Proceed with Bind.
            ctx.bind(EntryDN, null, attrs);

        } catch (javax.naming.NameAlreadyBoundException e) {
            // ****************************************************
            // Ok, we caught ourselves adding an existing entry.
            if (VERBOSE) {
                System.out.println(MP + "Existing entry detected," +
                        " for " + EntryDN + ", Ignoring.");
            }
            STATUS.AccumCounter("NonReboundableEntries");
            STATUS.setLastOpStatus(1);
            return (true);
        } catch (Exception e) {
            System.err.println(MP + "IRR Error while binding entry of [" +
                    EntryDN + "]\n" + e);
            STATUS.AccumCounter("AddErrors");
            return (false);
        } // End of exception
        STATUS.AccumCounter("AddedEntries");
        STATUS.setLastOpStatus(1);
        return (true);

    } // End of writeEntry class.

    /**
     * Creates an Operational Admin Account.
     *
     * @param DirContext current established Directory Context.
     * @param String     current Fully Qualified Customer Domain DN to be written.
     * @return boolean indication of operation successful or not.
     */
    private boolean CreateOperationalAdminAccount(DirContext ctx,
                                                  String FrameworkDN,
                                                  String FrameworkPassword) {

        // Parse DN.
        idxParseDN Naming_Source = new idxParseDN(FrameworkDN);

        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("inetOrgPerson");
        oc.add("organizationalPerson");
        oc.add("dcdPerson");
        oc.add("person");

        // Create attributes to be associated with object
        Attributes attrs = new BasicAttributes(true); // case-ignore
        attrs.put(oc);
        attrs.put("cn", Naming_Source.getNamingValue());
        attrs.put("sn", "framework");
        attrs.put("userpassword", FrameworkPassword);

        // Perform the Bind...
        return (writeEntry(ctx, FrameworkDN, attrs));

    } // End of CreateOperationAdminAccount class.

    /**
     * Creates an new Operational State Domain Object.
     *
     * @param DirContext current established Directory Context.
     * @param String     current Fully Qualified Customer Domain DN to be written.
     * @param String     Principal Installation By.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOpStateDomainObject(DirContext ctx,
                                             String CustomerDN,
                                             String _IRRprincipal) {

        // Formulate the DN.
        String FrameworkDN = "cn=operational state" +
                ", ou=domainobjects" +
                ", " + DEFAULT_FRAMEWORK_CONTAINER +
                ", " + CustomerDN;

        // Parse DN.
        idxParseDN Naming_Source = new idxParseDN(FrameworkDN);

        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("cnxidoFrameworkDomain");

        // Create attributes to be associated with object
        Attributes attrs = new BasicAttributes(true); // case-ignore
        attrs.put(oc);
        attrs.put("cn", Naming_Source.getNamingValue());
        attrs.put("cnxidaState", "RUNNING");
        attrs.put("cnxidaDesc", "FRAMEWORK");
        attrs.put("cnxidaComment", "FRAMEWORK Operational State");

        idxTimeStamp CurrentTimeStamp = new idxTimeStamp();
        CurrentTimeStamp.enableLocalTime();  // Enable Local Time Stamp.

        attrs.put("cnxidaInstallTime", CurrentTimeStamp.get());

        attrs.put("cnxidaInstallBy", _IRRprincipal);

        // Perform the Bind...
        return (writeEntry(ctx, FrameworkDN, attrs));

    } // End of CreateOpStateDomainObject class.

    /**
     * Creates the Operational Admin Account.
     *
     * @param DirContext current established Directory Context.
     * @param String     current Fully Qualified Customer Domain DN.
     * @param String     current Admin Password.
     * @param String     current Read Password.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOperationalAdminAccounts(DirContext ctx,
                                                  String CustomerDN,
                                                  String AdminPassword,
                                                  String ReadPassword) {

        // Formulate the Framework Admin Account DN.
        String FrameworkDN = "cn=Framework Process Master" +
                ", " + DEFAULT_FRAMEWORK_CONTAINER +
                ", " + CustomerDN;

        if (!CreateOperationalAdminAccount(ctx,
                FrameworkDN,
                AdminPassword)) {
            return (false);
        }

        // Formulate the Framework User ReadOnly Account DN.
        FrameworkDN = "cn=Framework User" +
                ", " + DEFAULT_FRAMEWORK_CONTAINER +
                ", " + CustomerDN;

        return (CreateOperationalAdminAccount(ctx,
                FrameworkDN,
                ReadPassword));

    } // End of CreateOperationalAdminAccounts class.

    /**
     * Creates an new Domain Container (DC) for realizing
     * DIT containment.  This establishes a non-FRAMEWORK Realm
     * container.
     *
     * @param DirContext current established Directory Context.
     * @param String     current fully qualified DN of DC to be written.
     * @param boolean    Indicator for Tag of Framework Realm.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateDCContainer(DirContext ctx,
                                     String EntryDN,
                                     boolean FRAMEWORK_REALM) {

        // Parse incoming DN.
        idxParseDN Naming_Source = new idxParseDN(EntryDN);

        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("domain");
        if (FRAMEWORK_REALM) {
            oc.add("cnxidoRealm");
        }

        // Create attributes to be associated with object
        Attributes attrs = new BasicAttributes(true); // case-ignore
        attrs.put(oc);
        attrs.put("dc", Naming_Source.getNamingValue());

        // ***************************************************
        // If this is an FRAMEWORK Realm,
        // Set the Install and Modify Operational Attributes.
        if (FRAMEWORK_REALM) {
            // *****************************
            // Obtain the Current Timestamp
            idxTimeStamp CurrentTimeStamp = new idxTimeStamp();
            CurrentTimeStamp.enableLocalTime();  // Enable Local Time Stamp.

            // *****************************
            // Obtain the Current Principal
            String _IRRprincipal = obtainIRRPrincipal(ctx);

            // *****************************
            // Realize the Attributes.
            attrs.put("cnxidaInstallTime", CurrentTimeStamp.get());
            attrs.put("cnxidaInstallBy", _IRRprincipal);
            attrs.put("cnxidaLastModifyTime", CurrentTimeStamp.get());
            attrs.put("cnxidaLastModifyBy", _IRRprincipal);
        } // End of If.

        // Perform the Bind...
        return (writeEntry(ctx, EntryDN, attrs));

    } // End of CreateDCContainer class.

    /**
     * Creates an new Domain Container (DC) for realizing
     * DIT containment.  This establishes a non-FRAMEWORK Realm
     * container.
     *
     * @param DirContext current established Directory Context.
     * @param String     current fully qualified DN of DC to be written.
     * @param boolean    Indicator for Tag of Framework Realm.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateDCContainer(DirContext ctx,
                                     String EntryDN) {
        return (CreateDCContainer(ctx, EntryDN, false));
    }
    // End of CreateDCContainer method with no Realm indication.

    /**
     * Creates an new OrganizationalUnit (OU) container for realizing
     * DIT containment.
     *
     * @param DirContext current established Directory Context.
     * @param String     current fully qualified DN of OU to be written.
     * @param boolean    Is this an FRAMEWORK Realm?
     * @param boolean    Is this an FRAMEWORK System Container?
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOUContainer(DirContext ctx,
                                     String EntryDN,
                                     boolean FRAMEWORK_REALM,
                                     boolean FRAMEWORK_SYSTEM_CONTAINER) {

        // **********************************************
        // Create attributes to Establish Entry.
        Attributes attrs = new BasicAttributes(true); // case-ignore

        // **********************************************
        // Parse incoming DN.
        idxParseDN Naming_Source = new idxParseDN(EntryDN);

        // **********************************************
        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("organizationalunit");
        oc.add("dcdOrgUnit");

        // *************************************
        // Should this be classified as a System
        // container?
        if (FRAMEWORK_SYSTEM_CONTAINER) {
            oc.add(SYSTEM_CONTAINER_CLASS);

            // *************************************
            // Is this System Container the Default
            // FRAMEWORK Container?
            if (EntryDN.startsWith(DEFAULT_FRAMEWORK_CONTAINER)) {
                // *************************************
                // Yes, tag the System Container with
                // the Version Information.
                attrs.put("cnxidaSysApplicationVersion",
                        Version.SysApplicationVersion);

                attrs.put("cnxidaSysIRRMasterSchemaVersion",
                        Version.SysIRRMasterSchemaVersion);

                Attribute sp = new BasicAttribute("cnxidaSysProperty");
                sp.add("BUILDNAME=" +
                        Version.BuildName);
                attrs.put(sp);
            } // End of If.
        } // End of Outer If.

        // *************************************
        // Should this be classified as a Realm
        // container?
        if (FRAMEWORK_REALM) {
            oc.add("cnxidoRealm");
        }

        // **********************************************
        // Create attributes to be associated with object
        attrs.put(oc);
        attrs.put("ou", Naming_Source.getNamingValue());

        // **************************************************
        // If this is an FRAMEWORK Realm,
        // Set the Install and Modify Operational Attributes.
        if (FRAMEWORK_REALM) {
            // *****************************
            // Obtain the Current Timestamp
            idxTimeStamp CurrentTimeStamp = new idxTimeStamp();
            CurrentTimeStamp.enableLocalTime();  // Enable Local Time Stamp.

            // *****************************
            // Obtain the Current Principal
            String _IRRprincipal = obtainIRRPrincipal(ctx);

            // *****************************
            // Realize the Attributes.
            attrs.put("cnxidaInstallTime", CurrentTimeStamp.get());
            attrs.put("cnxidaInstallBy", _IRRprincipal);
            attrs.put("cnxidaLastModifyTime", CurrentTimeStamp.get());
            attrs.put("cnxidaLastModifyBy", _IRRprincipal);
        } // End of If.

        // Perform the Bind...
        return (writeEntry(ctx, EntryDN, attrs));

    } // End of CreateOUContainer class.

    /**
     * Creates an new OrganizationalUnit (OU) container for realizing
     * DIT containment.
     *
     * @param DirContext current established Directory Context.
     * @param String     current fully qualified DN of OU to be written.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOUContainer(DirContext ctx,
                                     String EntryDN) {
        return (CreateOUContainer(ctx, EntryDN, false, false));
    }
    // End of CreateOUContainer method with no Realm indication.


    /**
     * Creates an new set of OrganizationalUnits (OU)
     * for realizing the FRAMEWORK Instance Specific.
     * These Entries on on every primary and realm instance.
     *
     * @param DirContext current established Directory Context.
     * @param String     current parent DN.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOUContainersForInstanceObjectTree(DirContext ctx,
                                                           String EntryDN) {

        if (!CreateOUContainer(ctx, "ou=Actiongroups, " + EntryDN, false, true)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=Recycle, " + EntryDN, false, true)) {
            return (false);
        }

        return (true);

    } // End of CreateOUContainersForInstanceObjectTree class.

    /**
     * Creates an new set of OrganizationalUnits (OU)
     * for realizing the Framework Distributed Object Tree.
     *
     * @param DirContext current established Directory Context.
     * @param String     current parent DN.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOUContainersForFrameworkObjectTree(DirContext ctx,
                                                            String EntryDN) {

        EntryDN = DEFAULT_FRAMEWORK_CONTAINER + "," + EntryDN;
        if (!CreateOUContainer(ctx, EntryDN, false, true)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=activities," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=elementpermissions," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=domainobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=vendorobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=customerobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=policies,ou=customerobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=actions,ou=customerobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=knowledge,ou=customerobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=rules,ou=customerobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=presentation,ou=customerobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=reports,ou=customerobjects," + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=resourcecommandsets,ou=customerobjects," + EntryDN)) {
            return (false);
        }

        return (true);

    } // End of CreateOUContainersForFrameworkObjectTree class.

    /**
     * Creates an new set of OrganizationalUnits (OU)
     * for realizing the Site Specific and sundry Distributed Object Tree.
     *
     * @param DirContext current established Directory Context.
     * @param String     current parent DN.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOUContainersForSiteObjectTree(DirContext ctx,
                                                       String EntryDN) {

        // **********************************************
        // Create the People Container.
        if (!CreateOUContainer(ctx, "ou=People, " + EntryDN, false, true)) {
            return (false);
        }

        return (true);

    } // End of CreateOUContainersForSiteObjectTree class.

    /**
     * Initiates the Build of the VendorObjects Area.
     *
     * @param DirContext current established Directory Context.
     * @param String     current parent DN.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateVendorObjectContainers(DirContext ctx,
                                                String EntryDN) {

        idxIRRVendors Vendors = new idxIRRVendors(this);
        if (Vendors.isAvailable()) {
            return (Vendors.CreateContainersForVendors(ctx,
                    EntryDN));
        }

        return (false);

    } // End of Class.

    /**
     * Creates an new set of OrganizationalUnits (OU)
     * for realizing the Vendor or Customer Object containers.
     *
     * @param DirContext current established Directory Context.
     * @param String     current parent DN.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateOUContainersForVCObjectTree(DirContext ctx,
                                                     String EntryDN) {

        if (!CreateOUContainer(ctx, "ou=actions, " + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=devplugins, " + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=knowledge, " + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=methods, " + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=policies, " + EntryDN)) {
            return (false);
        }

        if (!CreateOUContainer(ctx, "ou=rules, " + EntryDN)) {
            return (false);
        }

        return (true);

    } // End of CreateOUContainersForVCObjectTree class.

    /**
     * Creates an new ResourceContainerUnit (RCU) container for realizing
     * DIT containment.
     *
     * @param DirContext current established Directory Context.
     * @param String     current fully qualified DN of RCU to be written.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateResourceContainerUnit(DirContext ctx,
                                               String EntryDN) {

        // **********************************************
        // Create attributes to Establish Entry.
        Attributes attrs = new BasicAttributes(true); // case-ignore

        // **********************************************
        // Parse incoming DN.
        idxParseDN Naming_Source = new idxParseDN(EntryDN);

        // **********************************************
        // Create Objectclass Multivalued Attribute Set.
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add(ICOSResourceDescriptor);
        oc.add(ICOSResourceContainerDescriptor);

        // *************************************
        // Create the Additional Attributes.
        attrs.put("cnxidaResourceDataType", "CONTAINER");
        attrs.put("cnxidaRDescriptorState", "ONLINE");

        // *****************************
        // Obtain the Current Timestamp
        idxTimeStamp CurrentTimeStamp = new idxTimeStamp();
        CurrentTimeStamp.enableLocalTime();  // Enable Local Time Stamp.

        // *****************************
        // Obtain the Current Principal
        String _IRRprincipal = obtainIRRPrincipal(ctx);

        // *****************************
        // Realize the Attributes.
        attrs.put("cnxidaInstallTime", CurrentTimeStamp.get());
        attrs.put("cnxidaInstallBy", _IRRprincipal);
        attrs.put("cnxidaLastModifyTime", CurrentTimeStamp.get());
        attrs.put("cnxidaLastModifyBy", _IRRprincipal);

        // **********************************************
        // Final attributes to be associated with object
        attrs.put(oc);
        attrs.put("rcu", Naming_Source.getNamingValue());

        // Perform the Bind...
        return (writeEntry(ctx, EntryDN, attrs));

    } // End of CreateResourceContainerUnit class.

    /**
     * Creates the Default Resource Container.
     *
     * @param DirContext current established Directory Context.
     * @param String     current parent DN.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateDefaultResourceContainerUnit(DirContext ctx,
                                                      String EntryDN) {
        // **********************************************
        // Create the Default Resource Container, which
        // the customer can rename.
        return (CreateResourceContainerUnit(ctx, DEFAULT_RESOURCE_CONTAINER + ", " + EntryDN));
    } // End of DefaultResourceContainerUnit class.

    /**
     * Obtains current Principal from existing Directory Context.
     *
     * @param DirContext current established Directory Context.
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

} ///:~ End of idxIRRdit Class.
