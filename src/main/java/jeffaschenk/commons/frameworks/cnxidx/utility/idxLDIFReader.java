package jeffaschenk.commons.frameworks.cnxidx.utility;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxIRRBase64;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.URL;
import java.net.MalformedURLException;
import javax.naming.directory.*;

/**
 * Java Class to process and read as input an LDIF file or stream.
 * Functionality for LDIF version detection,
 * comments and other enhancements and fixes applied for compliance with
 * IETF RFC2849 - LDAP Data Interchange Format.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2001-2003
 */

public class idxLDIFReader {

    private String myCurrent_DN = null;

    private BufferedReader myin = null;

    private String LDIFVersion = "";

    private boolean hasMore = true;
    private boolean First_Line = true;

    /**
     * Initial Constructor.
     */
    public idxLDIFReader(BufferedReader in) {
        myin = in;
    } // End of Constructor.

    /**
     * Obtains the next LDIF Entry found in our BufferedReader
     * input.  Will return an Attributes Object, containing all
     * of the Attributes for the obtained entry.
     *
     * @return Attributes All Attributes for the current entry.
     * @throws java.io.IOException if problems reading BufferedReader
     */
    public Attributes getNextEntry() throws IOException {

        String inputline = null;

        String attributeName = null;

        StringBuffer entry_value = null;
        StringBuffer entry_dn = null;

        Attributes entry = null;

        boolean entry_value_encoded = false;
        boolean entry_value_has_reference = false;

        int position;
        int from;
        int to;

        // *******************************************
        // Check the End of Input.
        if (!hasMore) {
            return (null);
        }

        // *******************************************
        // Process the Incoming LDIF Data.
        while (hasMore) {
            // *********************************************
            // Read an Input Line.
            // Force the rest of the data out, if we reach EOF
            // before a End of Entry deliminter.
            inputline = myin.readLine();
            if (inputline == null) {
                inputline = "";
                hasMore = false;
            } // End of inputline NULL Check.

            // *********************************************
            // If this is our first line, check for a version
            // indication.
            if (First_Line) {
                if (checkForVersion(inputline)) {
                    continue;
                }
            } // End of First Line

            // *********************************************
            // If a Comment Ignore it.
            if ((inputline.length() != 0) && (inputline.charAt(0) == '#')) {
                continue;
            }

            // *************************************************
            // Do I have a new Attribute and not a Continuation?
            position = inputline.indexOf(":");
            if (position != -1 && inputline.charAt(0) != ' ') {

                // **************************************
                // Save out Previous Attribute
                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded,
                            entry_value_has_reference);
                } // End of if not null attribute or value.

                to = position;
                from = position + 1;

                if (inputline.length() > from) {
                    if (inputline.charAt(from) == ':') {
                        entry_value_encoded = true;
                        from++;
                    } else {
                        entry_value_encoded = false;
                    } // End of Else

                    if (inputline.charAt(from) == '<') {
                        entry_value_has_reference = true;
                        from++;
                    } else {
                        entry_value_has_reference = false;
                    } // End of Else.

                    if (inputline.charAt(from) == ' ') {
                        from++;
                    }

                    attributeName = inputline.substring(0, to).toLowerCase();
                    entry_value = new StringBuffer(inputline.substring(from));
                } else {
                    attributeName = inputline.substring(0, to).toLowerCase();
                    entry_value = new StringBuffer("");
                }

                if (entry_dn == null) {
                    entry_dn = entry_value;
                    attributeName = null;
                } // End of If.

                // *************************************************
                // Do I have an Entry Seperator Line?
            } else if (inputline.length() == 0) {

                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded,
                            entry_value_has_reference);
                    attributeName = null;
                    entry_value = null;
                } // End of If.

                // has no dn?
                if (entry_dn == null) {
                    continue;
                }

                myCurrent_DN = entry_dn.toString();
                return (entry);

            } else if (inputline.charAt(0) == ' ') {
                entry_value.append(inputline.substring(1));
            } // end of Else if.

        } // End of While.
        hasMore = false;
        myCurrent_DN = null;
        return null;

    } // End of getNextEntry Method

    /**
     * Obtains the next LDIF Entry found in our BufferedReader
     * input, this method Preserves Case for the Attribute Name or ID.
     * Will return an Attributes Object, containing all
     * of the Attributes for the obtained entry.
     *
     * @return Attributes All Attributes for the current entry.
     * @throws java.io.IOException if problems reading BufferedReader
     */
    public Attributes getNextEntryPreserveCase() throws IOException {

        String inputline = null;

        String attributeName = null;

        StringBuffer entry_value = null;
        StringBuffer entry_dn = null;

        Attributes entry = null;

        boolean entry_value_encoded = false;
        boolean entry_value_has_reference = false;

        int position;
        int from;
        int to;

        // *******************************************
        // Check the End of Input.
        if (!hasMore) {
            return (null);
        }

        // *******************************************
        // Process the Incoming LDIF Data.
        while (hasMore) {
            // *********************************************
            // Read an Input Line.
            // Force the rest of the data out, if we reach EOF
            // before a End of Entry deliminter.
            inputline = myin.readLine();
            if (inputline == null) {
                inputline = "";
                hasMore = false;
            } // End of inputline NULL Check.

            // *********************************************
            // If this is our first line, check for a version
            // indication.
            if (First_Line) {
                if (checkForVersion(inputline)) {
                    continue;
                }
            } // End of First Line

            // *********************************************
            // If a Comment Ignore it.
            if ((inputline.length() != 0) && (inputline.charAt(0) == '#')) {
                continue;
            }

            // *************************************************
            // Do I have a new Attribute and not a Continuation?
            position = inputline.indexOf(":");
            if (position != -1 && inputline.charAt(0) != ' ') {

                // **************************************
                // Save out Previous Attribute
                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(false);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded,
                            entry_value_has_reference);
                } // End of if not null attribute or value.

                to = position;
                from = position + 1;

                if (inputline.length() > from) {
                    if (inputline.charAt(from) == ':') {
                        entry_value_encoded = true;
                        from++;
                    } else {
                        entry_value_encoded = false;
                    } // End of Else

                    if (inputline.charAt(from) == '<') {
                        entry_value_has_reference = true;
                        from++;
                    } else {
                        entry_value_has_reference = false;
                    } // End of Else.

                    if (inputline.charAt(from) == ' ') {
                        from++;
                    }

                    attributeName = inputline.substring(0, to);
                    entry_value = new StringBuffer(inputline.substring(from));
                } else {
                    attributeName = inputline.substring(0, to);
                    entry_value = new StringBuffer("");
                }

                if (entry_dn == null) {
                    entry_dn = entry_value;
                    attributeName = null;
                } // End of If.

                // *************************************************
                // Do I have an Entry Seperator Line?
            } else if (inputline.length() == 0) {

                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(false);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded,
                            entry_value_has_reference);
                    attributeName = null;
                    entry_value = null;
                } // End of If.

                // has no dn?
                if (entry_dn == null) {
                    continue;
                }

                myCurrent_DN = entry_dn.toString();
                return (entry);

            } else if (inputline.charAt(0) == ' ') {
                entry_value.append(inputline.substring(1));
            } // end of Else if.

        } // End of While.
        hasMore = false;
        myCurrent_DN = null;
        return null;

    } // End of getNextEntryPreserveCase Method

    /**
     * Obtains the next LDIF Entry found in our BufferedReader
     * input.  Will return an Attributes Object, containing all
     * of the Attributes for the current Modification structure
     * for the obtained entry.
     *
     * @return Attributes Current Modification Attributes
     *         for the current entry.
     * @throws java.io.IOException if problems reading BufferedReader
     */
    public Attributes getNextModEntry() throws IOException {

        String inputline = null;

        String attributeName = null;

        StringBuffer entry_value = null;
        StringBuffer entry_dn = null;

        Attributes entry = null;

        boolean entry_value_encoded = false;
        boolean entry_value_has_reference = false;

        int position;
        int from;
        int to;

        // *******************************************
        // Check the End of Input.
        if (!hasMore) {
            return (null);
        }

        // *******************************************
        // Process the Incoming LDIF Data.
        while (hasMore) {
            // *********************************************
            // Read an Input Line.
            // Force the rest of the data out, if we reach EOF
            // before a End of Entry deliminter.
            inputline = myin.readLine();
            if (inputline == null) {
                inputline = "";
                hasMore = false;
            } // End of inputline NULL Check.

            // *********************************************
            // If this is our first line, check for a version
            // indication.
            if (First_Line) {
                if (checkForVersion(inputline)) {
                    continue;
                }
            } // End of First Line

            // *********************************************
            // If a Comment Ignore it.
            if ((inputline.length() != 0) && (inputline.charAt(0) == '#')) {
                continue;
            }

            // *************************************************
            // Do I have a new Attribute and not a Continuation?
            position = inputline.indexOf(":");
            if (position != -1 && inputline.charAt(0) != ' ') {

                // **************************************
                // Save out Previous Attribute
                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded,
                            entry_value_has_reference);

                    if (attributeName.equalsIgnoreCase("dn")) {
                        entry_dn = entry_value;
                        myCurrent_DN = entry_dn.toString();
                    } // End of If.

                } // End of if not null attribute or value.

                to = position;
                from = position + 1;

                if (inputline.length() > from) {
                    if (inputline.charAt(from) == ':') {
                        entry_value_encoded = true;
                        from++;
                    } else {
                        entry_value_encoded = false;
                    } // End of Else

                    if (inputline.charAt(from) == '<') {
                        entry_value_has_reference = true;
                        from++;
                    } else {
                        entry_value_has_reference = false;
                    } // End of Else.

                    if (inputline.charAt(from) == ' ') {
                        from++;
                    }

                    attributeName = inputline.substring(0, to).toLowerCase();
                    entry_value = new StringBuffer(inputline.substring(from));
                } else {
                    attributeName = inputline.substring(0, to).toLowerCase();
                    entry_value = new StringBuffer("");
                } // End of Inner Else.

                // *************************************************
                // Do I have end of a Modification Seperator?
            } else if ((inputline.length() != 0) && (inputline.charAt(0) == '-')) {
                // **************************************
                // Save out Previous Attribute
                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded,
                            entry_value_has_reference);

                    if (attributeName.equalsIgnoreCase("dn")) {
                        entry_dn = entry_value;
                        myCurrent_DN = entry_dn.toString();
                    } // End of If.

                    attributeName = null;
                    entry_value = null;

                } // End of If.

                // **********************************
                // Has no dn?
                if (myCurrent_DN == null) {
                    continue;
                }
                return (entry);

                // *************************************************
                // Do I have an Entry Seperator Line?
            } else if (inputline.length() == 0) {
                // **************************************
                // Save out Previous Attribute
                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded,
                            entry_value_has_reference);

                    if (attributeName.equalsIgnoreCase("dn")) {
                        entry_dn = entry_value;
                        myCurrent_DN = entry_dn.toString();
                    } // End of If.

                    attributeName = null;
                    entry_value = null;
                } // End of If.

                // **********************************
                // Has no dn?
                if (myCurrent_DN == null) {
                    continue;
                }
                return (entry);

                // *************************************************
                // Do I have an Attribute Continuation?
            } else if (inputline.charAt(0) == ' ') {
                entry_value.append(inputline.substring(1));
            } // end of Else if.

        } // End of While.

        hasMore = false;
        myCurrent_DN = null;
        return null;
    } // End of getNextModEntry Method


    /**
     * Obtains the current DN found during our getNextEntry
     * method.
     *
     * @return String of current DN.
     */
    public String getCurrentDN() {
        return (myCurrent_DN);
    } // End of getCurrentDN Method.

    /**
     * Provides an Iteration indicator method.
     *
     * @return boolean indicator if additional entries need
     *         to be processed.
     */
    public boolean hasMore() {
        return (hasMore);
    } // End of hasMore Method.

    /**
     * Obtains detected LDIF Version.
     *
     * @return String of LDIF Version.
     */
    public String getVersion() {
        return (LDIFVersion);
    } // End of getVersion Method.

    /**
     * Private method for creating Attributes Object of
     * current LDIF entry being formulated.
     */
    private void add(Attributes entry,
                     String attribute,
                     String value,
                     boolean encoded,
                     boolean reference) {

        Attribute vals = entry.get(attribute);

        if (vals == null) {
            vals = new BasicAttribute(attribute);
        }

        if (encoded) {
            vals.add(idxIRRBase64.decode(value.toCharArray()));
        } else if (reference) {
            vals.add(dereferenceValueWithNIO(value));
        } else {
            vals.add(value);
        }

        // ***************************
        // Place Value into Attributes
        // Entry.
        entry.put(vals);

    } // End of Private add Method.

    /**
     * Private method for dereferencing an Attribute from
     * a Specified URL.
     */
    private byte[] dereferenceValue(String _inrefurl) {

        // *********************************************
        // Use an InputStream to obtain the File on the
        // local filesystem only.

        try {
            // ********************************
            // We shall only accept local
            // File Path Specifications
            // with the URL.
            URL url = new URL(_inrefurl);
            String protocol = url.getProtocol();
            if ((protocol == null) ||
                    (!protocol.equalsIgnoreCase("file"))) {
                return (_inrefurl.getBytes());
            }

            String filename = url.getFile();
            if ((filename == null) ||
                    (filename.equalsIgnoreCase(""))) {
                return (_inrefurl.getBytes());
            }

            // ***********************************
            // Create a File Object for this file.
            File f = new File(filename);
            if (!f.exists()) {
                return (_inrefurl.getBytes());
            }

            // **************************************
            // Check to verify the length.
            Long filesize = new Long(f.length());
            if (filesize.compareTo(new Long(Integer.MAX_VALUE)) > 0) {       // Since our Directory can not accept a
                // Attribute value more than 2BG, simple
                // just drop in the URL.
                return (_inrefurl.getBytes());
            } // End of If.

            // **************************************
            // Open up a File Stream.
            BufferedInputStream bins =
                    new BufferedInputStream(new FileInputStream(f), 16384);

            // ***********************************
            // Create a Byte Array for the file
            // and read the entire file in one
            // operation.
            byte[] buffer = new byte[(int) f.length()];
            bins.read(buffer, 0, buffer.length);

            // ***************************
            // Close the Streams
            bins.close();

            // **************************************
            // Obtain the File as a Byte Array from
            // our input Buffer.
            return (buffer);

            // ***********************************
            // Any Exceptions, we shall
            // simple return the incoming
            // Reference URL.
        } catch (final MalformedURLException e) {
            return (_inrefurl.getBytes());
        } catch (final IOException e) {
            return (_inrefurl.getBytes());
        } // end of Exception clause.

    } // End of Private dereferenceValue Method.

    /**
     * Private method for dereferencing an Attribute from
     * a Specified URL.
     */
    private byte[] dereferenceValueWithNIO(String _inrefurl) {

        // ***************************************
        // Use NIO to obtain the File on the
        // local filesystem only.
        Long filesize = new Long(0);
        ByteBuffer buffer = null;

        try {
            // ********************************
            // We shall only accept local
            // File Path Specifications
            // with the URL.
            URL url = new URL(_inrefurl);
            String protocol = url.getProtocol();
            if ((protocol == null) ||
                    (!protocol.equalsIgnoreCase("file"))) {
                return (_inrefurl.getBytes());
            }

            String filename = url.getFile();
            if ((filename == null) ||
                    (filename.equalsIgnoreCase(""))) {
                return (_inrefurl.getBytes());
            }

            // ***********************************
            // Open up a File Stream and a
            // NIO Channel.
            FileInputStream fin = new FileInputStream(filename);
            FileChannel fc = fin.getChannel();
            filesize = new Long(fc.size());
            if (filesize.compareTo(new Long(Integer.MAX_VALUE)) > 0) {       // Since our Directory can not accept a
                // Attribute value more than 2BG, simple
                // just drop in the URL.
                return (_inrefurl.getBytes());
            } // End of If.

            // ****************************
            // Create a ByteBuffer.
            buffer = ByteBuffer.allocate(filesize.intValue());
            fc.read(buffer);
            buffer.flip();  // Reposition to Beginning for Subsequent Reads.

            // ***************************
            // Close the Channel and File.
            fc.close();
            fin.close();

            // **************************************
            // Obtain the File as a Byte Array from
            // our input Buffer.
            if (buffer.hasArray()) {
                //byte[] xBytes = new byte[ filesize.intValue() ];
                //xBytes = buffer.array();
                return (buffer.array());
            } else {
                return (_inrefurl.getBytes());
            } // End of Else.

            // ***********************************
            // Any Exceptions, we shall
            // simple return the incoming
            // Reference URL.
        } catch (final MalformedURLException e) {
            return (_inrefurl.getBytes());
        } catch (final IOException e) {
            return (_inrefurl.getBytes());
        } // end of Exception clause.

    } // End of Private dereferenceValueWithNIO Method.

    /**
     * Private method for creating Attributes Object of
     * current LDIF entry being formulated.
     */
    private boolean checkForVersion(String _inputline) {
        // *********************************************
        // If this is our first line, just for a version
        // indication.
        if (First_Line) {
            First_Line = false;
            if ((_inputline.length() > 8) &&
                    ("version".equalsIgnoreCase(_inputline.substring(0, 7)))) {
                // ****************************
                // Got a Version Line
                // So obtain the Version.
                LDIFVersion = _inputline.substring(8);
                LDIFVersion = LDIFVersion.trim();
                return (true);
            } // End of Inner If.
        } // End of First Line.
        return (false);
    } // End of Private checkForVersion Method.

} ///:~ End of idxLDIFReader Class
