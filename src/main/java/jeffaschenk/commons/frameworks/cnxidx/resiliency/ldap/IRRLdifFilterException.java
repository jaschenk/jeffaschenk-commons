package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

/**
 * Class to provide exception handling for ldif filter activity.
 *
 * @version 1.0 $Revision: #1 $
 * Developed 2001-2003
 */

public class IRRLdifFilterException extends Exception {

    private int IRRRC = 0;

    public IRRLdifFilterException() {
    }

    public IRRLdifFilterException(String emsg) {
        super(emsg);
    } // End of Constructor.

    public IRRLdifFilterException(String emsg, int _IRRRC) {
        super(emsg);
        IRRRC = _IRRRC;
    } // End of Constructor.

    public void setRC(int _IRRRC) {
        IRRRC = _IRRRC;
    } // End of Methods.

    public int getRC() {
        return (IRRRC);
    } // End of Methods.

} ///:~ End of IRRLdifFilterException Class.

