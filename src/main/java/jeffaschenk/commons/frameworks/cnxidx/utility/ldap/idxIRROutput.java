package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TreeMap;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

/**
 * Java class for providing either simple display print form of a
 * NamingEnumerated Search result or true RFC complient LDIF form of
 * output for IRR Directory Entries.
 *
 * @author jeff.schenk
 * @version 2.0 $Revision
 * Developed 2001-2002
 */

public class idxIRROutput {

    private static String MP = "idxIRROutput: ";

    private static String ObjectClassName = "objectclass";
    private static String UserPasswordName = "userpassword";
    private static String cnxidaXObjectBlob = "cnxidaxobjectblob";

    /**
     * Print Directory Entry Attribute and value pairs from a
     * search result.
     * We will report on all objectclasses and naming attributes
     * first.  The userPassword Attribute will always be displayed
     * as hidden in a "********" form.  Long attribute values, such
     * as the cnxidoxobjectblob will display a shortened display.
     *
     * @param NamingEnumeration JNDI search results.
     * @param String            Current DN base to properly reflect full DN.
     * @return int count of entries processed.
     */
    public static int PrintSearchList(NamingEnumeration sl, String dnbase) {

        int EntryCount = 0;
        if (sl == null) {
            return (EntryCount);
        } else {
            try {
                while (sl.hasMore()) {
                    SearchResult si = (SearchResult) sl.next();
                    EntryCount++;

                    // *****************************
                    // Formulate the DN.
                    //
                    String DN = null;
                    if (dnbase.equals("")) {
                        DN = si.getName();
                    } else if (!si.isRelative()) {
                        DN = si.getName();
                    } else if (si.getName().equals("")) {
                        DN = dnbase;
                    } else {
                        DN = si.getName() + "," + dnbase;
                    }

                    // ************************************
                    // Is the DN from a deReference Alias?
                    // If so, then we have a URL, we need
                    // to remove the URL.
                    //
                    if (!si.isRelative()) {
                        DN = extractDNfromURL(DN);
                    }

                    // ******************************************
                    // Write out the DN.
                    // Do not write out a JNDI Quoted DN.
                    // That is not LDIF Compliant.
                    //
                    idxParseDN pDN = new idxParseDN(DN);
                    if (pDN.isQuoted()) {
                        System.out.println("dn: " + pDN.getDN());
                    } else {
                        System.out.println("dn: " + DN);
                    }

                    // Obtain Attributes
                    Attributes entryattrs = si.getAttributes();

                    // First Write out Objectclasses.
                    Attribute eo = entryattrs.get(ObjectClassName);
                    if (eo != null) {
                        for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                            System.out.println(eo.getID() + ": " + eov.next());
                        }
                    } // End of check for null if.


                    // Obtain Naming Attribute Next.
                    if (!"".equals(pDN.getNamingAttribute())) {
                        Attribute en = entryattrs.get(pDN.getNamingAttribute());
                        if (en != null) {
                            for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                                System.out.println(en.getID() + ": " + env.next());
                            }
                        } // End of check for null if.
                    } // End of Naming Attribute.


                    // Finish Obtaining remaining Attributes,
                    // in no special sequence.
                    for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                        Attribute attr = (Attribute) ea.next();

                        if ((!ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                                (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                            for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                                String Aname = attr.getID();
                                Aname = Aname.toLowerCase();
                                Object Aobject = ev.next();
                                if (Aname.startsWith(UserPasswordName)) {
                                    // *****************************
                                    // Show A trimmed Down Version.
                                    System.out.println(UserPasswordName + ": " +
                                            "********");

                                } else if (cnxidaXObjectBlob.equalsIgnoreCase(attr.getID())) {
                                    // *****************************
                                    // Show A trimmed Down Version.
                                    String blob;
                                    blob = (String) Aobject;
                                    blob = blob.replace('\n', ' ');
                                    int obloblen = blob.length();

                                    if (blob.length() > 64) {
                                        blob = blob.substring(0, 25) + " ...... " +
                                                blob.substring((blob.length() - 25));

                                    } // end of if.
                                    System.out.println(attr.getID() +
                                            ": " + blob + ", Full Length:[" + obloblen + "]");

                                } else if (Aobject instanceof byte[]) {
                                    System.out.println(attr.getID() +
                                            ": [ Binary data " + ((byte[]) Aobject).length + " in length ]");
                                } else { // Show normal Attributes as is...
                                    System.out.println(attr.getID() +
                                            ": " + Aobject);
                                } // End of Else.

                            } // End of Inner For Loop.
                        } // End of If.
                    } // End of Outer For Loop

                    System.out.println("");

                } // End of While Loop
            } catch (NamingException e) {
                System.err.println(MP + "Cannot continue listing search results - " + e);
                return (-1);
            } catch (Exception e) {
                System.err.println(MP + "Cannot continue listing search results - " + e);
                e.printStackTrace();
                return (-1);
            } // End of Exception

        } // End of Else

        return (EntryCount);

    } // End of PrintSearchList

    /**
     * Print Directory Entry Attribute and value pairs from a
     * search result.
     * We will report on all objectclasses and naming attributes
     * first.  The userPassword Attribute will always be displayed
     * as hidden in a "********" form.
     * If the NICE is TRUE then the Long attribute values, such
     * as the cnxidoxobjectblob will display in full.
     *
     * @param NamingEnumeration JNDI search results.
     * @param String            Current DN base to properly reflect full DN.
     * @param boolean           NICE Output Indicator.
     * @return int count of entries processed.
     */
    public static int PrintSearchList(NamingEnumeration sl, String dnbase, boolean _NICE) {

        // ***************************************************
        // If not nice output, return the abridged version.
        if (!_NICE) {
            return (PrintSearchList(sl, dnbase));
        }

        int EntryCount = 0;
        if (sl == null) {
            return (EntryCount);
        } else {
            try {
                while (sl.hasMore()) {
                    SearchResult si = (SearchResult) sl.next();
                    EntryCount++;

                    // *************************************
                    // Formulate the DN.
                    //
                    String DN = null;
                    if (dnbase.equals("")) {
                        DN = si.getName();
                    } else if (!si.isRelative()) {
                        DN = si.getName();
                    } else if (si.getName().equals("")) {
                        DN = dnbase;
                    } else {
                        DN = si.getName() + "," + dnbase;
                    }

                    // ************************************
                    // Is the DN from a deReference Alias?
                    // If so, then we have a URL, we need
                    // to remove the URL.
                    //
                    if (!si.isRelative()) {
                        DN = extractDNfromURL(DN);
                    }

                    // ******************************************
                    // Write out the DN.
                    // Do not write out a JNDI Quoted DN.
                    // That is not LDIF Compliant.
                    //
                    idxParseDN pDN = new idxParseDN(DN);
                    if (pDN.isQuoted()) {
                        System.out.println("dn: " + pDN.getDN());
                    } else {
                        System.out.println("dn: " + DN);
                    }

                    // Obtain Attributes
                    Attributes entryattrs = si.getAttributes();

                    // First Write out Objectclasses.
                    Attribute eo = entryattrs.get(ObjectClassName);
                    if (eo != null) {
                        for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                            System.out.println(eo.getID() + ": " + eov.next());
                        }
                    } // End of check for null if.


                    // Obtain Naming Attribute Next.
                    if (!"".equals(pDN.getNamingAttribute())) {
                        Attribute en = entryattrs.get(pDN.getNamingAttribute());
                        if (en != null) {
                            for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                                System.out.println(en.getID() + ": " + env.next());
                            }
                        } // End of check for null if.
                    } // End of Naming Attribute.


                    // Finish Obtaining remaining Attributes,
                    // in no special sequence.
                    for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                        Attribute attr = (Attribute) ea.next();

                        if ((!ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                                (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                            for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                                String Aname = attr.getID();
                                Aname = Aname.toLowerCase();
                                Object Aobject = ev.next();
                                if (Aname.startsWith(UserPasswordName)) {
                                    // *****************************
                                    // Show A trimmed Down Version.
                                    System.out.println(UserPasswordName + ": " +
                                            "********");

                                } else if (cnxidaXObjectBlob.equalsIgnoreCase(attr.getID())) {
                                    String blob;
                                    blob = (String) Aobject;
                                    System.out.println(attr.getID() +
                                            ": Data Length:[" + blob.length() + "]");
                                    System.out.println(attr.getID() +
                                            ": " + blob);

                                } else if (Aobject instanceof byte[]) {
                                    System.out.println(attr.getID() +
                                            ": [ Binary data " + ((byte[]) Aobject).length + " in length ]");
                                } else { // Show normal Attributes as is...
                                    System.out.println(attr.getID() +
                                            ": " + Aobject);
                                } // End of Else.

                            } // End of Inner For Loop.
                        } // End of If.
                    } // End of Outer For Loop

                    System.out.println("");

                } // End of While Loop
            } catch (NamingException e) {
                System.err.println(MP + "Cannot continue listing search results - " + e);
                return (-1);
            } catch (Exception e) {
                System.err.println(MP + "Cannot continue listing search results - " + e);
                e.printStackTrace();
                return (-1);
            } // End of Exception

        } // End of Else

        return (EntryCount);

    } // End of PrintSearchList


    /**
     * Output Directory Entries in RFC complient LDIF
     * Attribute and value pairs from a search result.
     * We will report on all objectclasses and naming attributes
     * first. Normal Base64 attribute encoding will apply as well.
     *
     * @param NamingEnumeration JNDI search results.
     * @param String            Current DN base to properly reflect full DN.
     * @param BufferedWriter    output stream where LDIF written.
     * @param boolean           NICE DN Output indicator.
     * @return int count of entries processed.
     * @throws Exception when unrecoverable error occurs.
     */
    public static int LDIFSearchList(NamingEnumeration sl,
                                     String dnbase,
                                     BufferedWriter LDIFOUT,
                                     boolean _NICE)
            throws Exception, IOException {

        int EntryCount = 0;
        if (sl == null) {
            return (EntryCount);
        } else {
            try {
                while (sl.hasMore()) {
                    SearchResult si = (SearchResult) sl.next();
                    EntryCount++;

                    // ******************************************
                    // Formulate the DN.
                    //
                    String DN = null;
                    if (dnbase.equals("")) {
                        DN = si.getName();
                    } else if (!si.isRelative()) {
                        DN = si.getName();
                    } else if (si.getName().equals("")) {
                        DN = dnbase;
                    } else {
                        DN = si.getName() + "," + dnbase;
                    }

                    // ************************************
                    // Is the DN from a deReference Alias?
                    // If so, then we have a URL, we need
                    // to remove the URL.
                    //
                    if (!si.isRelative()) {
                        DN = extractDNfromURL(DN);
                    }

                    // ******************************************
                    // Write out the DN.
                    // Do not write out a JNDI Quoted DN.
                    // That is not LDIF Compliant.
                    //
                    idxParseDN pDN = new idxParseDN(DN);
                    if (_NICE) {
                        LDIFOUT.write("dn: ");

                        if (pDN.isQuoted()) {
                            LDIFOUT.write(pDN.getDN());
                        } else {
                            LDIFOUT.write(DN);
                        }

                        LDIFOUT.write("\n");

                    } else {
                        if (pDN.isQuoted()) {
                            WriteLDIF("dn", pDN.getDN(), LDIFOUT);
                        } else {
                            WriteLDIF("dn", DN, LDIFOUT);
                        }
                    } // End of DN Output.

                    // Obtain the entries Attributes.
                    Attributes entryattrs = si.getAttributes();

                    // Obtain ObjectClass First.
                    Attribute eo = entryattrs.get(ObjectClassName);
                    if (eo != null) {
                        for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                            WriteLDIF(eo.getID(), eov.next(), LDIFOUT);
                        }
                    } // End of check for null if.

                    // Obtain Naming Attribute Next.
                    if (!"".equals(pDN.getNamingAttribute())) {
                        Attribute en = entryattrs.get(pDN.getNamingAttribute());
                        if (en != null) {
                            for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                                WriteLDIF(en.getID(), env.next(), LDIFOUT);
                            }
                        } // End of check for null if.
                    } // End of Naming Attribute.

                    // Finish Obtaining remaining Attributes,
                    // in no special sequence.
                    for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                        Attribute attr = (Attribute) ea.next();

                        if ((!ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                                (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                            for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                                WriteLDIF(attr.getID(), ev.next(), LDIFOUT);
                            }
                        } // End of If
                    } // End of Outer For Loop

                    WriteLDIF("", "", LDIFOUT);

                } // End of While Loop
            } catch (NamingException e) {
                System.err.println(MP + "Naming Exception, Cannot continue obtaining search results, " + e);
                throw e;
            } catch (Exception e) {
                System.err.println(MP + "Exception, Cannot continue obtaining search results, " + e);
                e.printStackTrace();
                throw e;
            } // End of Exception

        } // End of Else
        return (EntryCount);

    } // End of LDIFSearchList

    /**
     * Output Attributes and incoming DN in RFC complient LDIF
     * Attribute and value pairs.
     * We will report on all objectclasses and naming attributes
     * first. Normal Base64 attribute encoding will apply as well.
     *
     * @param String         Full DN.
     * @param Attributes     JNDI Attributes Object.
     * @param BufferedWriter output stream where LDIF written.
     * @param boolean        NICE DN output indicator.
     * @throws Exception when unrecoverable error occurs.
     */
    public static void EntryToLDIF(String SourceDN,
                                   Attributes entryattrs,
                                   BufferedWriter LDIFOUT,
                                   boolean _NICE)
            throws Exception, IOException {

        if (entryattrs == null) {
            return;
        }

        try {
            // ******************************************
            // Write out the DN.
            // Do not write out a JNDI Quoted DN.
            // That is not LDIF Compliant.
            //
            idxParseDN pDN = new idxParseDN(SourceDN);
            if (_NICE) {
                LDIFOUT.write("dn: ");

                if (pDN.isQuoted()) {
                    LDIFOUT.write(pDN.getDN());
                } else {
                    LDIFOUT.write(SourceDN);
                }

                LDIFOUT.write("\n");

            } else {
                if (pDN.isQuoted()) {
                    WriteLDIF("dn", pDN.getDN(), LDIFOUT);
                } else {
                    WriteLDIF("dn", SourceDN, LDIFOUT);
                }
            } // End of DN Output.

            // Obtain ObjectClass First.
            Attribute eo = entryattrs.get(ObjectClassName);
            if (eo != null) {
                for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                    WriteLDIF(eo.getID(), eov.next(), LDIFOUT);
                }
            } // End of check for null if.

            // Obtain Naming Attribute Next.
            if (!"".equals(pDN.getNamingAttribute())) {
                Attribute en = entryattrs.get(pDN.getNamingAttribute());
                if (en != null) {
                    for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                        WriteLDIF(en.getID(), env.next(), LDIFOUT);
                    }
                } // End of check for null if.
            } // End of Naming Attribute.

            // Finish Obtaining remaining Attributes,
            // in no special sequence.
            for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                Attribute attr = (Attribute) ea.next();

                if ((!ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                        (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                    for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                        WriteLDIF(attr.getID(), ev.next(), LDIFOUT);
                    }
                } // End of If
            } // End of Outer For Loop

            WriteLDIF("", "", LDIFOUT);
        } catch (Exception e) {
            System.err.println(MP + "EntryToLDIF() Exception, Unable to Formulate LDIF Output, " + e);
            e.printStackTrace();
            throw e;
        } // End of Exception

    } // End of EntryToLDIF

    /**
     * Output Attributes and incoming DN in RFC complient LDIF
     * Attribute and value pairs, while preserving the case
     * of the Attribute Name or ID.
     * We will report on all objectclasses and naming attributes
     * first. Normal Base64 attribute encoding will apply as well.
     *
     * @param String         Full DN.
     * @param Attributes     JNDI Attributes Object.
     * @param BufferedWriter output stream where LDIF written.
     * @param boolean        NICE DN output indicator.
     * @throws Exception when unrecoverable error occurs.
     */
    public static void EntryToLDIFPreservingAttributeNameCase(String SourceDN,
                                                              Attributes entryattrs,
                                                              BufferedWriter LDIFOUT,
                                                              boolean _NICE)
            throws Exception, IOException {

        if (entryattrs == null) {
            return;
        }

        TreeMap<String,String> AttrNameMap = new TreeMap<>();
        String aname = null;

        try {
            // ******************************************
            // Write out the DN.
            // Do not write out a JNDI Quoted DN.
            // That is not LDIF Compliant.
            //
            idxParseDN pDN = new idxParseDN(SourceDN);
            if (_NICE) {
                LDIFOUT.write("dn: ");

                if (pDN.isQuoted()) {
                    LDIFOUT.write(pDN.getDN());
                } else {
                    LDIFOUT.write(SourceDN);
                }

                LDIFOUT.write("\n");

            } else {
                if (pDN.isQuoted()) {
                    WriteLDIF("dn", pDN.getDN(), LDIFOUT);
                } else {
                    WriteLDIF("dn", SourceDN, LDIFOUT);
                }
            } // End of DN Output.

            // Since this method preserves case of the Attribute
            // Name, we must formulate the lookup table for
            // Attribute Name Normalization.
            //
            for (NamingEnumeration avl = entryattrs.getIDs(); avl.hasMore(); ) {
                aname = (String) avl.next();
                AttrNameMap.put(aname.toLowerCase(), aname);
            } // End of For Loop.

            // Obtain ObjectClass First.
            aname = (String) AttrNameMap.get(ObjectClassName.toLowerCase());
            Attribute eo = entryattrs.get(aname);
            if (eo != null) {
                for (NamingEnumeration eov = eo.getAll(); eov.hasMore(); ) {
                    WriteLDIF(eo.getID(), eov.next(), LDIFOUT);
                }
            } // End of check for null if.

            // Obtain Naming Attribute Next.
            if (!"".equals(pDN.getNamingAttribute())) {
                aname = (String) AttrNameMap.get(pDN.getNamingAttribute().toLowerCase());
                Attribute en = entryattrs.get(aname);
                if (en != null) {
                    for (NamingEnumeration env = en.getAll(); env.hasMore(); ) {
                        WriteLDIF(en.getID(), env.next(), LDIFOUT);
                    }
                } // End of check for null if.
            } // End of Naming Attribute.

            // Finish Obtaining remaining Attributes,
            // in no special sequence.
            for (NamingEnumeration ea = entryattrs.getAll(); ea.hasMore(); ) {
                Attribute attr = (Attribute) ea.next();

                if ((!ObjectClassName.equalsIgnoreCase(attr.getID())) &&
                        (!pDN.getNamingAttribute().equalsIgnoreCase(attr.getID()))) {
                    for (NamingEnumeration ev = attr.getAll(); ev.hasMore(); ) {
                        WriteLDIF(attr.getID(), ev.next(), LDIFOUT);
                    }
                } // End of If
            } // End of Outer For Loop

            WriteLDIF("", "", LDIFOUT);
        } catch (Exception e) {
            System.err.println(MP + "EntryToLDIF() Exception, Unable to Formulate LDIF Output, " + e);
            e.printStackTrace();
            throw e;
        } // End of Exception

    } // End of EntryToLDIFPreservingAttributeNameCase


    /**
     * Outputs RFC complient LDIF data.
     * Normal Base64 attribute encoding will apply as well.
     *
     * @param String         Atrribute Name.
     * @param Object         Attribute Object Value.
     * @param BufferedWriter output stream where LDIF written.
     */
    public static void WriteLDIF(String AttributeName,
                                 Object obvalue,
                                 BufferedWriter LDIFOUT)
            throws IOException {

        String strvalue;
        String Tag;
        char[] jd;

        int LDIFLineSize = 76;
        char[] LDIFLineSeperator = {'\n'};

        // **************************************************
        // Tag and get data value into character byte form.
        if (obvalue instanceof byte[]) {
            Tag = AttributeName + ":: ";
            jd = idxIRRBase64.encode((byte[]) obvalue);
        } else {
            strvalue = obvalue.toString();
            if (ShouldEncode(strvalue)) {
                Tag = AttributeName + ":: ";
                jd = idxIRRBase64.encode(strvalue.getBytes());
            } else if ((strvalue.equals("")) &&
                    (AttributeName.equals(""))) {
                LDIFOUT.write(LDIFLineSeperator);
                return;
            } else if (strvalue.equals("")) {
                Tag = AttributeName + ":";
                LDIFOUT.write(Tag);
                LDIFOUT.write(LDIFLineSeperator);
                return;
            } else {
                Tag = AttributeName + ": ";
                jd = strvalue.toCharArray();
            }
        } // End of Else.


        // *************************************
        // Now Write the Data and first line.
        int linelen = LDIFLineSize + 1 - Tag.length();

        LDIFOUT.write(Tag);

        if (jd.length + Tag.length() <= LDIFLineSize) {
            LDIFOUT.write(jd);
            LDIFOUT.write(LDIFLineSeperator);
            return;
        }

        // **************************************************
        // Write out all subsequent LDIF continuation lines.
        LDIFOUT.write(jd, 0, linelen);
        LDIFOUT.write(LDIFLineSeperator);

        for (int jl = linelen; jl < jd.length; jl += LDIFLineSize) {
            LDIFOUT.write(" ");

            if (jl + LDIFLineSize > jd.length) {
                LDIFOUT.write(jd, jl, jd.length - jl);
            } else {
                LDIFOUT.write(jd, jl, LDIFLineSize);
            }

            LDIFOUT.write(LDIFLineSeperator);

        } // End of For Loop.


    } // End of WriteLDIF

    /**
     * Class to determine if the string data should be Base64 encoded or not.
     * This follows the standard LDIF rules for encoding as presented in the
     * RFC.
     *
     * @param String Attribute Value String.
     * @return boolean indicator whether value should be Base64 encoded
     *         or not.
     */
    public static boolean ShouldEncode(String str) {

        char SAFE_CHAR_EXCEPTIONS[] = {'\n', '\r', '\t', 0};
        char SAFE_INIT_CHAR_EXCEPTIONS[] = {'\n', '\r', '\t', 32, ':', '<', 0};

        // Are there safe initial character exceptions in the content?
        for (int ji = 0; ji < SAFE_INIT_CHAR_EXCEPTIONS.length; ji++) {
            if (str.indexOf(SAFE_INIT_CHAR_EXCEPTIONS[ji]) == 0) {
                return (true);
            }
        }

        // Are there safe character exceptions in the content?
        for (int ji = 0; ji < SAFE_CHAR_EXCEPTIONS.length; ji++) {
            if (str.indexOf(SAFE_CHAR_EXCEPTIONS[ji]) != -1) {
                return (true);
            }
        }

        // Is there a trailing space?
        if (str.endsWith(" ")) {
            return (true);
        }


        return (false);
    } // End of ShouldEncode.

    /**
     * Class to output the current Directory Schema to STDOUT.
     *
     * @param DirContext current established Directory Context.
     */
    public static void PrintSchema(DirContext ctx) {

        try {
            // Get the schema tree root
            DirContext schema = ctx.getSchema("");

            // List contents of root
            NamingEnumeration bds = schema.list("");
            while (bds.hasMore()) {
                String schemaobjectname = ((NameClassPair) (bds.next())).getName();
                System.out.println("Schema Object Name: [" + schemaobjectname + "]");


                // Get the schema tree root
                DirContext ocs = (DirContext) schema.lookup(schemaobjectname);

                // List contents of root
                NamingEnumeration bdsi = ocs.list("");
                while (bdsi.hasMore()) {
                    String ocname = ((NameClassPair) (bdsi.next())).getName();
                    System.out.println(schemaobjectname + ": [" + ocname + "]");

                    // Get object's attributes
                    Attributes ocAttrs = ocs.getAttributes(ocname);
                    System.out.println(ocAttrs);

                } // End of Inner While.
                ocs.close();
            } // End of Outer While.
            schema.close();


        } catch (NamingException e) {
            System.err.println(MP + "Error Obtaining Schema: " + e);
            e.printStackTrace();
        } // End of Exception

    } // End of PrintSchema Class

    /**
     * Class to extract the DN from a URL CompoundName.
     *
     * @param String Incoming URL with DN, from an Alias.
     * @return String Extracted DN.
     */
    public static String extractDNfromURL(String _DN) {

        // ***************************
        // Make sure we have a string.
        if ((_DN == null) ||
                (_DN.equals(""))) {
            return (_DN);
        }

        // ***************************
        // Create an LDAPURL Object
        // from the incoming String.
        //
        LDAPUrl _ldapurl = null;
        try {
            _ldapurl = new LDAPUrl(_DN);
        } catch (java.net.MalformedURLException mue) {
            // *******************************
            // This is a malformed URL, return
            // Null.
            return ("");
        } // End of Exception.

        // ***************************
        // Extract the DN.
        //
        return (_ldapurl.getDN());
    } // End of extractDNfromURL Class

} ///:~ End of idxIRROutput Class
