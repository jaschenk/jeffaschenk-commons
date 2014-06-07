package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;


/**
 * Class to provide exception handling within IRR utility functions.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision: #3 $
 * Developed 2001
 */

public class idxIRRException extends Exception {
    private int IRRRC = 0;

    public idxIRRException() {
    }

    public idxIRRException(String emsg) {
        super(emsg);
    } // End of Constructor.

    public idxIRRException(String emsg, int _IRRRC) {
        super(emsg);
        IRRRC = _IRRRC;
    } // End of Constructor.

    public void setRC(int _IRRRC) {
        IRRRC = _IRRRC;
    } // End of Method.

    public int getRC() {
        return (IRRRC);
    } // End of Method.

} ///:~ End of idxIRRExecption Class.

