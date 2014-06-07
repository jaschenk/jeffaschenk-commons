package jeffaschenk.commons.frameworks.cnxidx.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Provides Object to manipulate a DCL (Data Connection Limited) TX(I|E|O) File.
 * This Object is used by the @see IRRShell Class.
 *
 * @author jeff.schenk
 * @since 2005/11/21
 */
public class TXFile implements Serializable {

    // ******************************
    // Static Globals
    protected static final String TX_SEP = ".";
    protected static final String TXI_TYPE = "txi";
    protected static final String TXO_TYPE = "txo";
    protected static final String TXE_TYPE = "txe";

    // ******************************
    // File Name.
    private String TXFILENAME = null;

    // ******************************
    // Append Indicator.
    private boolean APPEND = false;

    /**
     * TXIfile Contructor class driven.
     *
     * @param String TX filename.
     */
    public TXFile(String _infile) {
        this.TXFILENAME = _infile;
    } // End of Constructor for TXFile.

    /**
     * TXIfile Contructor class driven.
     *
     * @param String  TX filename.
     * @param boolean Indicator to Append.
     */
    public TXFile(String _infile, boolean _append) {
        this.TXFILENAME = _infile;
        this.APPEND = _append;
    } // End of Constructor for TXFile.

    /**
     * set Append Indicator.
     *
     * @param boolean Indicator to Append.
     */
    public void setAppend(boolean _append) {
        this.APPEND = _append;
    } // End of Method.

    /**
     * Will provide indicator if TX File exists
     * or not.
     *
     * @return
     */
    public boolean exists() {
        if ((this.TXFILENAME == null) ||
                (this.TXFILENAME.equalsIgnoreCase(""))) {
            return false;
        }
        // ********************************
        // Create a File Object to
        // Determine Existence.
        File f = new File(this.TXFILENAME);
        return f.exists();
    } // End of doesTXFileExist Method.

    /**
     * append Data from String buffer to TX File.
     *
     * @param StringBuffer Contents to be Appended to TX file.
     */
    public void append(String data) throws IOException {
        setAppend(true);
        this.put(data);
    } // End of Append Method.

    /**
     * put Data from String buffer to TX File.
     *
     * @param StringBuffer Contents to be Written to TX file.
     */
    public void put(String data)
            throws IOException {

        // ***************************************
        // Open up the TX file, with possible
        // Appending capability.
        //
        BufferedWriter TX
                = new BufferedWriter(new FileWriter(TXFILENAME, APPEND));

        // ***************************************
        // Write the Data to the TX file.
        TX.write(data);

        // ***************************************
        // Close our TXI File.
        TX.flush();
        TX.close();

        // *********************************
        // Return.
        return;
    } // End of put.    

    /**
     * get will obtain contents of TX File.
     */
    public String get()
            throws IOException,
            FileNotFoundException {

        StringBuffer TXBuffer = new StringBuffer();
        // ***********************************
        // Open the TXI File.
        BufferedReader TX = new BufferedReader(
                new FileReader(TXFILENAME), 16384);

        // ************************************
        // Read contents.
        String str = null;
        while ((str = TX.readLine()) != null) {
            // readLine does not return line termination characters
            TXBuffer.append(str + "\n");
        } // End of While.

        // ************************
        // Close the File.
        TX.close();
        // *****************************************
        // Return the String Data.
        return TXBuffer.toString();
    } // End of toString Method.

    /**
     * Will obtain TX File Name.
     */
    public String getTXFileName() {
        return this.TXFILENAME;
    } // End of getTXFileName.

    /**
     * Will obtain TX File Name, less
     * the type of the file. Meaning,
     * each TXFile has a three (3)
     * character suffix, prefixed by a "." dot.
     * So, we truncate the Type off of the
     * Filename, just a Helper for classes
     * that want to execute this TX Script.
     */
    public String getTXFileNameWithoutType() {
        if ((this.TXFILENAME.endsWith(TXFile.TX_SEP + TXFile.TXI_TYPE)) ||
                (this.TXFILENAME.endsWith(TXFile.TX_SEP + TXFile.TXE_TYPE)) ||
                (this.TXFILENAME.endsWith(TXFile.TX_SEP + TXFile.TXO_TYPE))) {
            return this.TXFILENAME.substring(0, (this.TXFILENAME.length() - 4));
        } else {
            return this.TXFILENAME;
        }
    } // End of getTXFileName.

    /**
     * Will obtain TX File Type.
     * Meaning,
     * each TXFile has a three (3)
     * character suffix, prefixed by a "." dot.
     * So, we obtain just the Type of the
     * file.
     */
    public String getTXFileType() {
        if ((this.TXFILENAME.endsWith(TXFile.TX_SEP + TXFile.TXI_TYPE)) ||
                (this.TXFILENAME.endsWith(TXFile.TX_SEP + TXFile.TXE_TYPE)) ||
                (this.TXFILENAME.endsWith(TXFile.TX_SEP + TXFile.TXO_TYPE))) {
            return this.TXFILENAME.substring((this.TXFILENAME.length() - 3));
        } else {
            return this.TXFILENAME;
        }
    } // End of getTXFileType.

    /**
     * toString Override, will obtain TX File Name.
     */
    public String toString() {
        return this.TXFILENAME;
    } // End of toString Method.

    /**
     * Provides verification of TXO or TXE files.
     * "True"   returned if no errors.
     * "False"  returned if Errors Exists in the File.
     *
     * @return boolean indicator.
     */
    public boolean verifyContents() throws IOException,
            FileNotFoundException {

        // **************************************************
        // Determine our File Type.
        if (this.getTXFileType().equalsIgnoreCase(TXFile.TXO_TYPE)) {
            // **************************************************
            // Obtain the Output Contents and normalize it.
            String output = this.get().toLowerCase();

            // **************************************************
            // Check for A Successful Bind.
            if (output.indexOf("bind successful.") < 0) {
                return false;
            }

            // **************************************************
            // Check for A Successful ChangeContext.
            if (output.indexOf("changecontext successful.") < 0) {
                return false;
            } // End of If.

            // **************************************************
            // Check for any errors.
            if (output.indexOf("error encountered") >= 0) {
                return false;
            }

        } else if ((this.getTXFileType().equalsIgnoreCase(TXFile.TXO_TYPE)) &&
                (this.exists())) {
            // **************************************************
            // If the TXE has any contents, then we have an Error
            // Condition.
            if (!this.get().equalsIgnoreCase("")) {
                return false;
            }
        } // End of Else.

        // ********************************
        // Return Output File Verification
        // Indicator.
        return true;
    } // End of verifyContents Method.
} ///:~ End of TXFIle Object Class.
