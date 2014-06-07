package jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Java Class to provide a standard verifier interface, drivers Verification
 * Rules, once they have been established.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxArgVerifier {

    // ***********************************************
    // Logging Facilities.
    public static final String CLASSNAME = idxArgVerificationRules.class.getName();

    // ***********************************************
    // Globals
    private boolean VERBOSE = false;

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxArgVerifier() {
    } // end of Constructor

    /**
     * Method to Set VERBOSE Indicator.
     *
     * @param _verbose sets VERBOSE indicator.
     */
    public void setVerbose(boolean _verbose) {
        VERBOSE = _verbose;
    } // end of Method

    /**
     * Method to Get VERBOSE Indicator.
     *
     * @return boolean VERBOSE indicator.
     */
    public boolean getVerbose() {
        return (VERBOSE);
    } // end of Method

    /**
     * Drives the Verification Process.
     *
     * @param _MP       Output Logging String.
     * @param _Zin Constructed Parser Object.
     * @param _VAR   Constructed Link List of Verification Rules.
     * @return boolean Indicates whether or not Verification Process successful or not.
     */
    public boolean Verify(String _MP,
                          idxArgParser _Zin,
                          LinkedList _VAR) {

        // ***************************************
        // Run the Verification Rule Set.
        Iterator itr = _VAR.iterator();
        while (itr.hasNext()) {
            Object element = itr.next();
            if (element instanceof idxArgVerificationRules) {
                if (VERBOSE) {
                    ((idxArgVerificationRules) element).show(_Zin);
                }

                Object zv = ((idxArgVerificationRules) element).isValid(_Zin);
                if (zv instanceof Boolean) {
                    if (!((Boolean) zv).booleanValue()) {
                        System.out.print(_MP + ((idxArgVerificationRules) element).getName());
                        System.out.println(", Is not Valid.");
                        return (false);
                    }
                } // End of Boolean Instanceof.
                else if (zv instanceof String) {
                    System.out.print(_MP + ((idxArgVerificationRules) element).getName());
                    System.out.println(", " + zv);
                    return (false);
                } // End of String Instanceof.
                else {
                    System.out.print(_MP + ((idxArgVerificationRules) element).getName());
                    System.out.println(", Unknown Object Returned:[" + zv + "]");
                } // End of Unknown Instanceof.

            } // End of If.
        } // End of While.

        return (true);
    } // end of Method

} ///: End of idxArgVerifier Class.
