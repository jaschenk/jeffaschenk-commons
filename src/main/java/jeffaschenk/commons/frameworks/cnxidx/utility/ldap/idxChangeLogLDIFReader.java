package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.io.BufferedReader;
import java.io.IOException;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

/**
 * Java Class to process and read as input an LDIF file or stream.
 * Functionality for LDIF version detection,
 * comments and other enhancements and fixes applied for compliance with
 * IETF RFC2849 - LDAP Data Interchange Format.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxChangeLogLDIFReader {

    private boolean hasMore = true;
    private String myCurrent_DN = null;
    private BufferedReader myin = null;
    private String LDIFVersion = "";

    /**
     * Initial Constructor.
     */
    public idxChangeLogLDIFReader(BufferedReader in) {
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

        String inputDN = null;

        StringBuffer entry_value = null;
        StringBuffer entry_dn = null;

        Attributes entry = null;

        boolean entry_value_encoded = false;

        int position;
        int from;
        int to;

        boolean First_Line = true;


        // *******************************************
        // Process the Incoming LDIF Data.
        while ((inputline = myin.readLine()) != null) {

            // *********************************************
            // If this is our first line, just for a version
            // indication.
            if (First_Line) {
                if ((inputline.length() > 8) &&
                        ("version".equalsIgnoreCase(inputline.substring(0, 7)))) {
                    // ****************************
                    // Got a Version Line
                    // So obtain the Version.
                    LDIFVersion = inputline.substring(8);
                    LDIFVersion = LDIFVersion.trim();

                    // **********************
                    // Continue on....
                    First_Line = false;
                    continue;
                }

                First_Line = false;
            } // End of First Line.

            // *********************************************
            // If a Comment, check to see if the comment
            // is an Incremental LOG Change Comment.
            // If it is change the operation.
            //
            if ((inputline.length() != 0) && (inputline.charAt(0) == '#')) {
                if (inputline.startsWith("# Change Type:[")) {
                    attributeName = "IDXCHANGETYPE";
                    int lix = inputline.lastIndexOf(']');
                    if (lix <= 0) {
                        continue;
                    }
                    String evalue = inputline.substring(15, lix);

                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            evalue, false);
                    attributeName = null;
                    entry_value = null;
                } else if (inputline.startsWith("# DN:[")) {
                    attributeName = "IDXCHANGETYPEDN";

                    int lix = inputline.lastIndexOf(']');
                    if (lix <= 0) {
                        continue;
                    }
                    String evalue = inputline.substring(6, lix);

                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            evalue, false);
                    attributeName = null;
                    entry_value = null;
                    entry_dn = new StringBuffer(evalue);
                } else if (inputline.startsWith("# OLD DN:[")) {
                    attributeName = "IDXCHANGETYPEOLDDN";
                    int lix = inputline.lastIndexOf(']');
                    if (lix <= 0) {
                        continue;
                    }
                    String evalue = inputline.substring(10, lix);

                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            evalue, false);
                    attributeName = null;
                    entry_value = null;
                }

                continue;
            }

            // *************************************************
            // Do I have a new Attribute and not a Continuation?
            position = inputline.indexOf(":");
            if (position != -1 && inputline.charAt(0) != ' ') {

                if (attributeName != null && entry_value != null) {
                    if (entry == null) {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attributeName,
                            entry_value.toString(), entry_value_encoded);
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
                            entry_value.toString(), entry_value_encoded);
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
                     boolean encoded) {

        Attribute vals = entry.get(attribute);

        if (vals == null) {
            vals = new BasicAttribute(attribute);
        }

        if (encoded) {
            vals.add(idxIRRBase64.decode(value.toCharArray()));
        } else {
            vals.add(value);
        }

        entry.put(vals);

    } // End of Private add Method.

} ///:~ End of idxChangeLogLDIFReader Class
