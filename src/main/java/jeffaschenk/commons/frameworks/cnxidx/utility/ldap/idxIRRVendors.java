package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import javax.naming.directory.DirContext;

/**
 * Java class for creating the necessary DIT Structure for the various
 * Vendor Resources we support in our Product.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxIRRVendors {

    private static String MP = "IRRVendors: ";

    private boolean VERBOSE = false;

    private idxIRRdit DIT = null;

    private LinkedList<VendorResource> VendorObjects = null;

    private boolean Available = false;

    private static final String DEFAULT_VENDOR_CONF = "idxIRRVendor.conf";

    /**
     * Initial Constructor used when no argument supplied.
     * Reads the internal based configuration file embedded into the
     * JAR and creates the LinkedList to be used to construct Directory
     * Container Objects.
     */
    public idxIRRVendors(idxIRRdit _dit) {
        DIT = _dit;
        VERBOSE = DIT.getVerbose();

        // ************************************
        // Construct a Vendor / Device / OS Map
        //
        VendorObjects = new LinkedList<>();

        try {
            InputStream in = this.getClass().getResourceAsStream(DEFAULT_VENDOR_CONF);

            // ******************************************
            // Open up the Input Stream.
            BufferedReader vendorconfig = new BufferedReader(
                    new InputStreamReader(in));

            String xVENDOR = null;
            String xTYPE = null;
            String xVDS = null;
            String xOS = null;
            // ******************************************
            // Loop Through the Configuration File.
            String s;
            while ((s = vendorconfig.readLine()) != null) {
                s = s.trim();
                if (s.length() <= 0) {
                    continue;
                }
                if (("/".equals(s.substring(0, 1))) ||
                        ("#".equals(s.substring(0, 1))) ||
                        ("*".equals(s.substring(0, 1)))) {
                    continue;
                }

                int sep = s.indexOf(':');
                if (sep < 1) {
                    continue;
                }

                // ***********************************
                // Start Parsing.
                String name = s.substring(0, sep);
                name = name.trim();
                name = name.replace(',', ' ');

                String value = s.substring(sep + 1);
                value = value.trim();
                value = value.replace(',', ' ');

                if ((name == null) || ("".equals(name)) ||
                        (value == null) || ("".equals(value))) {
                    continue;
                }

                // ***********************************
                // Determine the Entry Name.
                if ("vendor".equalsIgnoreCase(name)) {
                    xVENDOR = value;
                    xTYPE = "";
                    xVDS = "";
                    xOS = "";
                    VendorObjects.add(new VendorResource(xVENDOR,
                            xTYPE, xVDS, xOS));
                } else if ("type".equalsIgnoreCase(name)) {
                    xTYPE = value;
                    xVDS = "";
                    xOS = "";
                    VendorObjects.add(new VendorResource(xVENDOR,
                            xTYPE, xVDS, xOS));
                } else if ("vds".equalsIgnoreCase(name)) {
                    xVDS = value;
                    xOS = "";
                    VendorObjects.add(new VendorResource(xVENDOR,
                            xTYPE, xVDS, xOS));
                } else if ("os".equalsIgnoreCase(name)) {
                    xOS = value;
                    VendorObjects.add(new VendorResource(xVENDOR,
                            xTYPE, xVDS, xOS));
                } else {
                    continue;
                }

            } // End of While Loop.
            // ************************************
            vendorconfig.close();

        } catch (Exception e) {
            e.printStackTrace();
            Available = false;
            return;
        } // End of Exception.

        // ****************************************
        // Show Verbose Output.
        //
        if (VERBOSE) {
            System.out.println(MP + "Vendor Objects Read and Available:");
            Iterator itr = VendorObjects.iterator();
            while (itr.hasNext()) {
                Object element = itr.next();
                System.out.println(MP + "\t" + element);
            } // End of While.
        }

        // *************************************
        // Indicator Object Avilable for Use.
        Available = true;
        return;

    } // end of Constructor

    /**
     * Method to Set VERBOSE Indicator.
     *
     * @param boolean Verbose Indicator setting.
     */
    public void setVerbose(boolean _verbose) {
        VERBOSE = _verbose;
    } // end of Method

    /**
     * Method to Get VERBOSE Indicator.
     *
     * @return boolean Verbose Indicator setting.
     */
    public boolean getVerbose() {
        return (VERBOSE);
    } // end of Method

    /**
     * Method to see if this Object is Available for Use and
     * has been properly built.
     *
     * @return boolean Available Indicator setting.
     */
    public boolean isAvailable() {
        return (Available);
    } // end of Method

    /**
     * Create an new set Containers for Vendor.
     *
     * @param DirContext current established Directory Context.
     * @param String     current Customer DN.
     * @return boolean indication of operation successful or not.
     */
    public boolean CreateContainersForVendors(DirContext ctx,
                                              String CustomerDN) {

        // ***************************************
        // Is this Service Available?
        // If not return....
        if (!Available) {
            return (false);
        }

        // ***************************************
        // Construct the Initial Top Level DN
        String iDN = DIT.getVendorObjectContainerName() +
                ", " + CustomerDN;

        // ***************************************
        // Start Iteration Loop.
        Iterator itr = VendorObjects.iterator();
        while (itr.hasNext()) {
            Object vo = itr.next();
            if (vo instanceof VendorResource) {

                if ((((VendorResource) vo).getVendorName() == null) ||
                        ("".equals(((VendorResource) vo).getVendorName()))) {
                    continue;
                }

                if ((((VendorResource) vo).getVendorResourceType() == null) ||
                        ("".equals(((VendorResource) vo).getVendorResourceType()))) {  // *****************************
                    // We have a Vendor Instance.
                    if (!DIT.CreateOUContainer(ctx,
                            "ou=" + ((VendorResource) vo).getVendorName() +
                                    ", " + iDN)) {
                        return (false);
                    }
                } // End of Else if

                else if ((((VendorResource) vo).getVendorResourceModel() == null) ||
                        ("".equals(((VendorResource) vo).getVendorResourceModel()))) {  // ***********************************
                    // We have a Vendor Resource Type.
                    if (!DIT.CreateOUContainer(ctx,
                            "ou=" + ((VendorResource) vo).getVendorResourceType() +
                                    ", ou=" + ((VendorResource) vo).getVendorName() +
                                    ", " + iDN)) {
                        return (false);
                    }
                } // End of Else if

                else if ((((VendorResource) vo).getVendorResourceOS() == null) ||
                        ("".equals(((VendorResource) vo).getVendorResourceOS()))) {  // ***********************************
                    // We have a Vendor Resource Model.
                    if (!DIT.CreateOUContainer(ctx,
                            "ou=" + ((VendorResource) vo).getVendorResourceModel() +
                                    ", ou=" + ((VendorResource) vo).getVendorResourceType() +
                                    ", ou=" + ((VendorResource) vo).getVendorName() +
                                    ", " + iDN)) {
                        return (false);
                    }

                    // ************************************
                    // Generate Necessary Subtrees.
                    if (!DIT.CreateOUContainersForVCObjectTree(ctx,
                            "ou=" + ((VendorResource) vo).getVendorResourceModel() +
                                    ", ou=" + ((VendorResource) vo).getVendorResourceType() +
                                    ", ou=" + ((VendorResource) vo).getVendorName() +
                                    ", " + iDN)) {
                        return (false);
                    }

                } // End of Else if

                else { // *************************************
                    // We have a Vendor Resource OS.
                    if (!DIT.CreateOUContainer(ctx,
                            "ou=" + ((VendorResource) vo).getVendorResourceOS() +
                                    ", ou=" + ((VendorResource) vo).getVendorResourceModel() +
                                    ", ou=" + ((VendorResource) vo).getVendorResourceType() +
                                    ", ou=" + ((VendorResource) vo).getVendorName() +
                                    ", " + iDN)) {
                        return (false);
                    }

                    // ************************************
                    // Generate Necessary Subtrees.
                    if (!DIT.CreateOUContainersForVCObjectTree(ctx,
                            "ou=" + ((VendorResource) vo).getVendorResourceOS() +
                                    ", ou=" + ((VendorResource) vo).getVendorResourceModel() +
                                    ", ou=" + ((VendorResource) vo).getVendorResourceType() +
                                    ", ou=" + ((VendorResource) vo).getVendorName() +
                                    ", " + iDN)) {
                        return (false);
                    }

                } // End of Else

            } // End of If.
        } // End of While.

        return (true);

    } // End of CreateContainersForVendors class.

} ///:~ End of idxIRRVendors Class.

/**
 * Support class which defines a Vendor Object for containment.
 */
class VendorResource {
    private String VendorName;
    private String VendorResourceType;
    private String VendorResourceModel;
    private String VendorResourceOS;

    /**
     * Initial Constructor used to Construct Object.
     */
    public VendorResource(String _VendorName,
                          String _VendorResourceType,
                          String _VendorResourceModel,
                          String _VendorResourceOS) {
        VendorName = _VendorName;
        VendorResourceType = _VendorResourceType;
        VendorResourceModel = _VendorResourceModel;
        VendorResourceOS = _VendorResourceOS;

    } // End of Constructor Class.

    /**
     * Method to obtain vendor Name.
     *
     * @param String Vendor Name.
     */
    public String getVendorName() {
        return (VendorName);
    }

    /**
     * Method to obtain vendor Resource Type.
     *
     * @param String Vendor Resource Type.
     */
    public String getVendorResourceType() {
        return (VendorResourceType);
    }

    /**
     * Method to obtain vendor Resource Model.
     *
     * @param String Vendor Resource Model.
     */
    public String getVendorResourceModel() {
        return (VendorResourceModel);
    }

    /**
     * Method to obtain vendor Resource OS.
     *
     * @param String Vendor Resource OS.
     */
    public String getVendorResourceOS() {
        return (VendorResourceOS);
    }

    /**
     * Method to convert Object to String Information.
     *
     * @param String Vendor Object.
     */
    public String toString() {

        if ((VendorResourceType == null) ||
                ("".equals(VendorResourceType))) {
            return (VendorName);
        } else if ((VendorResourceModel == null) ||
                ("".equals(VendorResourceModel))) {
            return (VendorName + "," + VendorResourceType);
        } else if ((VendorResourceOS == null) ||
                ("".equals(VendorResourceOS))) {
            return (VendorName + "," + VendorResourceType +
                    "," + VendorResourceModel);
        } else {
            return (VendorName + "," + VendorResourceType +
                    "," + VendorResourceModel +
                    "," + VendorResourceOS);
        }

    } // End of Method.
} ///:~ End of VendorResource Class.
