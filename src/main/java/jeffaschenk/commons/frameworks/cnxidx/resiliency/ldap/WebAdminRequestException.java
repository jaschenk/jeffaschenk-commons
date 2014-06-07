package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;

/**
 * Web Admin Request Exception Class.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 */

class WebAdminRequestException extends Exception {

    public static final WebAdminRequestException INVALID_REQUEST = new WebAdminRequestException(400);
    public static final WebAdminRequestException PAGE_NOT_FOUND = new WebAdminRequestException(404);
    public static final WebAdminRequestException INTERNAL_SERVER_ERROR = new WebAdminRequestException(500);

    private int errorCode;

    public WebAdminRequestException(int errorCode) {
        super();

        this.errorCode = errorCode;
    }

    public int hashCode() {
        return errorCode;
    }

    public boolean equals(Object that) {
        try {
            return this.errorCode == ((WebAdminRequestException) that).errorCode;
        } catch (ClassCastException cce) {
            return false;
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String toString() {
        return "" + errorCode;
    }
} ///:~ End of WebAdminRequestException Class.
