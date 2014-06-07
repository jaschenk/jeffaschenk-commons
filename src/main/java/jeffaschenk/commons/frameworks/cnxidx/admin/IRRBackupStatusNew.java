package jeffaschenk.commons.frameworks.cnxidx.admin;

/**
 * Java Backup Status Object.  Used my Reader and Writer Threads.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

class IRRBackupStatusNew {
    /**
     * IRRBackupStatusNew
     * Class to provide Status class for Backup Thread.
     */
    private int BackupError = 0;

    IRRBackupStatusNew() {
    } // Constructor.

    /**
     * Set Error Indicator Value.
     *
     * @param _x Thread Error Code.
     */
    public void setError(int _x) {
        BackupError = _x;
    }

    /**
     * Get Error Indicator Value.
     *
     * @return int Thread Error Code.
     */
    public int getError() {
        return (BackupError);
    }
} ///:~ End of Class IRRBackupStatusNew
