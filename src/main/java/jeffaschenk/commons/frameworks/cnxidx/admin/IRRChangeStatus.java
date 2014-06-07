package jeffaschenk.commons.frameworks.cnxidx.admin;

/**
 * Java Daemon Server Thread Status Object.
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2001-2002
 */

class IRRChangeStatus {
    /**
     * IRRChangeStatus
     * Class to provide Status class for Backup Thread.
     */
    private int ChangeError = 0;

    IRRChangeStatus() {
    } // Contructor.

    /**
     * Set Error Indicator Value.
     *
     * @param _x Thread Error Code.
     */
    public void setError(int _x) {
        ChangeError = _x;
    }

    /**
     * Get Error Indicator Value.
     *
     * @return int Thread Error Code.
     */
    public int getError() {
        return (ChangeError);
    }

} // End of Class IRRChangeStatus
