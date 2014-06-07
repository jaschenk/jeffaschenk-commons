package jeffaschenk.commons.frameworks.cnxidx.shell;

/**
 * Simple output formatter method class
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2002
 */

public class StringFormat {

    // *****************************************************
    // Initial Constructor.
    public StringFormat() {
    } // end of Constructor

    /**
     * Justify Left a String for Printing.
     *
     * @param _in
     * @param _i -- Length
     * @param _gap -- GAP String
     */
    public String JLeft(String _in, int _i, String _gap) {
        while (_in.length() < _i) {
            _in = _in + _gap;
        }
        return (_in);
    } // End of JLeft Method.

    /**
     * Justify Left a String for Printing.
     *
     * @param _in
     * @param _i -- Length
     */
    public String JLeft(String _in, int _i) {
        return (JLeft(_in, _i, " "));
    } // End of JLeft Method.

    /**
     * Justify Right a String for Printing.
     *
     * @param _in
     * @param _i -- Length
     * @param _gap -- GAP String
     */
    public String JRight(String _in, int _i, String _gap) {
        while (_in.length() < _i) {
            _in = _gap + _in;
        }
        return (_in);
    } // End of JRight Method.

    /**
     * Justify Right a String for Printing.
     *
     * @param _in
     * @param _i -- Length
     */
    public String JRight(String _in, int _i) {
        return (JRight(_in, _i, " "));
    } // End of JRight Method.

} ///~ End of StringFormat Class
