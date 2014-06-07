package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.io.*;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Provides class for accessing the IRR Schema (DC-Directory) and provides
 * a cached access of the schema objects for multiple accesses without
 * requiring a Directory Context to be always established.  Only needs
 * an initial Directory Context to obtain information, then Tree Maps are
 * built with the obtained schema objects.
 * <p/>
 * Java Class to provide a facility for accessing the current Directory
 * Schema and querying objects in the Directory.  Upon initialization we
 * will obtain the entire Directory schema and save in a List Container.
 * Additional methods will provide query and search capabilities against
 * the in memory schema to save in time and access.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxIRRschema {

    private TreeMap<String,Attributes> ocTM = new TreeMap<>(); // Objectclass

    private TreeMap<String,Attributes> atTM = new TreeMap<>(); // Attributes

    private TreeMap<String,Attributes> syTM = new TreeMap<>(); // Syntax

    private TreeMap<String,Attributes> unknownTM = new TreeMap<>(); // Unknown Objects

	/* ***************************************************************
     * Attribute TreeMap values would consist of an Attributes Object
	 * Content example would be the following
	 *	{syntax=SYNTAX: IA5 String, 
	 *       name=NAME: cnxidaGlobalCfgVersionURI, 
	 *	 single-value=SINGLE-VALUE: true, 
         *       numericoid=NUMERICOID: 1.3.6.1.4.1.6099.4.1.19
	 *	 no-user-modification=NO-USER-MODIFICATION: true, 
	 *	 usage=USAGE: directoryOperation
	 *	}
	 */

    public int SYNTAXunknown = 0;

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxIRRschema() {
        boolean rc = create();
        createSyntax();
    } // end of Constructor

    /**
     * Initial Constructor used when argument supplied.
     *
     * @param myctx available Directoru Context to obtain schema.
     */
    public idxIRRschema(DirContext myctx) {
        boolean rc = create(myctx);
        createSyntax();
    } // end of Constructor

    /**
     * Create Method with no Argument
     *
     * @return boolean Indicates if Directory Context was successfully opened or not.
     */
    private boolean create() {

        // *************************************************
        // Initialize and acquire System Properties.
        Properties myenv = System.getProperties();
        System.setProperty("java.naming.factory.initial",
                "com.sun.jndi.ldap.LdapCtxFactory");

        // *************************************************
        // Now initialize a Directory Context.
        try {
            // *****************************************
            // The following properties, better be set!
            // java.naming.provider.url
            // java.naming.security.principal
            // java.naming.security.authentication
            // java.naming.security.credentials

            DirContext myctx = new InitialDirContext(myenv);

            // *************************************************
            // Now Obtain Schema.
            return (create(myctx));

        } catch (Exception e) {
            System.err.println("IRR Exception on Obtaining Directory Context." + e);
            e.printStackTrace();
            return (false);
        } // End of exception

    } // end of create Method

    /**
     * Create Method with Argument
     *
     * @param myctx of current available IRR Directory Connection to obtain schema.
     * @return boolean Indicates if Directory Context was successfully opened or not.
     */
    private boolean create(DirContext myctx) {

        // ***********************************************
        // Ok, now get the schema and formulate our
        // TreeMap.
        try {
            // Get the schema tree root
            DirContext schema = myctx.getSchema("");

            // Obtain contents of root
            NamingEnumeration bds = schema.list("");
            while (bds.hasMore()) {
                String schemaobjectname =
                        ((NameClassPair) (bds.next())).getName();

                // Get the schema tree root
                DirContext ocs = (DirContext) schema.lookup(schemaobjectname);

                // List contents of root
                NamingEnumeration bdsi = ocs.list("");
                while (bdsi.hasMore()) {
                    String ocname = ((NameClassPair) (bdsi.next())).getName();

                    // Get object's attributes
                    Attributes ocAttrs = ocs.getAttributes(ocname);
                    String objectname = (String) ocname;
                    objectname = objectname.toLowerCase();

                    if (schemaobjectname.equalsIgnoreCase("ClassDefinition")) {
                        //System.out.println("Objectclass:" + objectname);
                        ocTM.put(objectname, ocAttrs);
                    } else if (schemaobjectname.equalsIgnoreCase("AttributeDefinition")) {
                        //System.out.println("Attribute:" + objectname);
                        atTM.put(objectname, ocAttrs);
                    } else {
                        unknownTM.put(objectname, ocAttrs);
                        System.err.println("Unknown Schema ObjectName: " +
                                schemaobjectname + ": [" + objectname + "]");
                        System.err.println(ocAttrs);
                        System.err.println("");
                    } // End of Else.

                } // End of Inner While.
                ocs.close();
            } // End of Outer While.
            schema.close();

        } catch (NamingException e) {
            System.err.println("IRR Error Obtaining Schema: " + e);
            e.printStackTrace();
            return (false);
        } // End of Exception

        return (true);

    } // end of Method

    //* *****************************************
    //* *****************************************
    //  Attribute Specific Methods
    //* *****************************************
    //* *****************************************

    /**
     * Method used to get Attribute List Value
     *
     * @param Name of Object.
     * @param Type of Object.
     * @return ArrayList of Object's Attributes.
     */
    private List<Attributes> getAttributeListValue(String Name, String Type) {
        Name = Name.toLowerCase();
        List<Attributes> myList = new ArrayList<>();
        Attributes myAttrs =  atTM.get(Name);
        if (myAttrs == null) {
            return (myList);
        }
        Attribute myValue = myAttrs.get(Type);
        if (myValue == null) {
            return (myList);
        }

        try {
            for (NamingEnumeration myEnum = myValue.getAll();
                 myEnum.hasMore(); ) {
                myList.add((Attributes)myEnum.next());

            } // End of For Loop

            return (myList);

        } catch (NamingException e) {
            System.err.println("IRRschema Error Obtaining Schema Attribute Name Value from Map: " + e);
            e.printStackTrace();
            return (myList);
        } // End of Exception
    } // End of Method.


    /**
     * Method used to get Attribute Content Value
     *
     * @param Name of Object.
     * @param Type of Object.
     * @return String of Object's Attribute Contents.
     */
    private String getAttributeValue(String Name, String Type) {
        Name = Name.toLowerCase();
        Attributes myAttrs = (Attributes) atTM.get(Name);
        if (myAttrs == null) {
            return ("");
        }
        Attribute myValue = myAttrs.get(Type);
        if (myValue == null) {
            return ("");
        }

        try {
            return ((String) myValue.get());
        } catch (NamingException e) {
            System.err.println("IRRschema Error Obtaining Schema Attribute Value from Map: " + e);
            e.printStackTrace();
            return ("");
        } // End of Exception

    } // End of Method.

    /**
     * Method used to get Attribute Syntax Name
     *
     * @param Name of Object.
     * @return String of Attribute Syntax Name Value.
     */
    public String getAttributeSyntaxName(String Name) {
        return (getAttributeValue(Name, "SYNTAX"));
    } // End of Method.

    /**
     * Method used to get Attribute Syntax
     *
     * @param Name of Object.
     * @return int of Attribute Syntax.
     */
    public int getAttributeSyntax(String Name) {
        String value = getAttributeValue(Name, "SYNTAX");
        return (getSyntaxIRRid(value));
    } // End of Method.

    /**
     * Method used to get Attribute Syntax OID
     *
     * @param  Name of Object.
     * @return String of Syntax OID.
     */
    public String getAttributeSyntaxOID(String Name) {
        String value = getAttributeValue(Name, "SYNTAX");
        return (getSyntaxOID(value));
    } // End of Method.

    /**
     * Method used to determine Attribute is Human-Readable or not.
     *
     * @param Name of Object.
     * @return boolean indicates if Attribute is Human-Readable or not.
     */
    public boolean IsAttributeSyntaxHumanReadable(String Name) {
        String value = getAttributeValue(Name, "SYNTAX");
        return (IsSyntaxHumanReadable(value));
    } // End of Method.

    /**
     * Method used to get Attribute OID
     *
     * @param Name of Object.
     * @return String of Attribute OID.
     */
    public String getAttributeOID(String Name) {
        return (getAttributeValue(Name, "NUMERICOID"));
    } // End of Method.

    /**
     * Method used to get Attribute Name
     *
     * @param Name of Object.
     * @return ArrayList of all Attributes Names.
     */
    public List<Attributes> getAttributeName(String Name) {
        return (getAttributeListValue(Name, "NAME"));
    } // End of Method.

    /**
     * Method used to get Objects Usage
     *
     * @param Name of Object.
     * @return String of Attributes Usage.
     */
    public String getAttributeUsage(String Name) {
        return (getAttributeValue(Name, "USAGE"));
    } // End of Method.

    /**
     * Method used to determine Attribute is SINGLE-VALUE
     *
     * @param Name of Object.
     * @return boolean indicates if Attribute is SINGLE-VALUED or not.
     */
    public boolean isAttributeSingleValued(String Name) {
        String value = getAttributeValue(Name, "SINGLE-VALUE");
        if (value.equalsIgnoreCase("true")) {
            return (true);
        } else {
            return (false);
        }
    } // End of Method.

    /**
     * Method used to determine Attribute is MULTI-VALUE
     *
     * @param Name of Object.
     * @return boolean indicates if Attribute is MULTI-VALUED or not.
     */
    public boolean isAttributeMultiValued(String Name) {
        String value = getAttributeValue(Name, "SINGLE-VALUE");
        if (value.equalsIgnoreCase("true")) {
            return (false);
        } else {
            return (true);
        }
    } // End of Method.

    /**
     * Method used to determine Attribute is Not Modifiable.
     *
     * @param Name of Object.
     * @return boolean indicates if Attribute is Modifiable or not.
     */
    public boolean isAttributeUserModifiable(String Name) {
        String value = getAttributeValue(Name, "NO-USER-MODIFICATION");
        if (value.equalsIgnoreCase("true")) {
            return (false);
        } else {
            return (true);
        }
    } // End of Method.

    /**
     * Method used to determine Attribute is Binary.
     *
     * @param Name of Object.
     * @return boolean indicates if Attribute is Binary or not.
     */
    public boolean isAttributeBinary(String Name) {

        String OID = getAttributeValue(Name, "NUMERICOID");

        // Standard RFC2256 Specified.
        // photo
        if (OID.equals("0.9.2342.19200300.100.1.7")) {
            return (true);
        }
        // personalSignature
        else if (OID.equals("0.9.2342.19200300.100.1.53")) {
            return (true);
        }
        // audio
        else if (OID.equals("0.9.2342.19200300.100.1.55")) {
            return (true);
        }
        // jpegPhoto
        else if (OID.equals("0.9.2342.19200300.100.1.60")) {
            return (true);
        }
        // javaSerilaizedData
        else if (OID.equals("1.3.6.1.4.1.42.2.27.4.1.8")) {
            return (true);
        }
        // thumbnailPhoto
        else if (OID.equals("1.3.6.1.4.1.1466.101.120.35")) {
            return (true);
        }
        // thumbnailLogo
        else if (OID.equals("1.3.6.1.4.1.1466.101.120.36")) {
            return (true);
        }
        // userPassword
        else if (OID.equals("2.5.4.35")) {
            return (true);
        }
        // userCertificate
        else if (OID.equals("2.5.4.36")) {
            return (true);
        }
        // cACertificate
        else if (OID.equals("2.5.4.37")) {
            return (true);
        }
        // authorityRevocationList
        else if (OID.equals("2.5.4.38")) {
            return (true);
        }
        // certificateRevocationList
        else if (OID.equals("2.5.4.39")) {
            return (true);
        }
        // crossCertificatePair
        else if (OID.equals("2.5.4.40")) {
            return (true);
        }
        // x500UniqueIdentifier
        else if (OID.equals("2.5.4.45")) {
            return (true);
        }

        // IRR FRAMEWORK SPECIFIC
        // cnxidaAuthenticationCredentials
        else if (OID.equals("1.3.6.1.4.1.6099.4.1.7")) {
            return (true);
        }
        // cnxidaAuthenticationPassword
        else if (OID.equals("1.3.6.1.4.1.6099.4.1.9")) {
            return (true);
        }
        // cnxidaXCompressObjectBlob
        else if (OID.equals("1.3.6.1.4.1.6099.4.2.16")) {
            return (true);
        }

        // DCL SPECIFIC
        // LocCredentials
        else if (OID.equals("2.6.5.2.14")) {
            return (true);
        }
        // ErrorLog
        else if (OID.equals("1.2.826.0.1.1578918.2.2.3")) {
            return (true);
        }
        // LocalRoutingInformation
        else if (OID.equals("2.6.5.2.26")) {
            return (true);
        }
        // voiceDialByNameNumber
        else if (OID.equals("2.16.840.1.113694.1.2.1.1.11")) {
            return (true);
        }
        // changes
        else if (OID.equals("2.16.840.1.113730.3.1.8")) {
            return (true);
        }


        // Default
        else {
            return (false);
        }

    } // End of Method.


    //* *****************************************
    //* *****************************************
    //  ObjectClass Specific Methods
    //* *****************************************
    //* *****************************************

    /**
     * Method used to get Objectclasses List Value.
     *
     * @param Name of Object.
     * @param Type of Object.
     * @return ArrayList containing Named Object Values.
     */
    private List<Attributes> getObjectClassListValue(String Name, String Type) {
        Name = Name.toLowerCase();
        List<Attributes> myList = new ArrayList<>();
        Attributes myAttrs = ocTM.get(Name);
        if (myAttrs == null) {
            return (myList);
        }
        Attribute myValue = myAttrs.get(Type);
        if (myValue == null) {
            return (myList);
        }

        try {
            for (NamingEnumeration myEnum = myValue.getAll();
                 myEnum.hasMore(); ) {
                myList.add((Attributes)myEnum.next());

            } // End of For Loop

            return (myList);

        } catch (NamingException e) {
            System.err.println("IRRschema Error Obtaining Schema ObjectClass Name Value from Map: " + e);
            e.printStackTrace();
            return (myList);
        } // End of Exception
    } // End of Method.


    /**
     * Method used to get Objectclasses Content Value.
     *
     * @param Name of Object.
     * @param Type of Object.
     * @return String containing Named Object Value.
     */
    private String getObjectClassValue(String Name, String Type) {
        Name = Name.toLowerCase();
        Attributes myAttrs = (Attributes) ocTM.get(Name);
        if (myAttrs == null) {
            return ("");
        }
        Attribute myValue = myAttrs.get(Type);
        if (myValue == null) {
            return ("");
        }

        try {
            return ((String) myValue.get());
        } catch (NamingException e) {
            System.err.println("IRRschema Error Obtaining Schema ObjectClass Value from Map: " + e);
            e.printStackTrace();
            return ("");
        } // End of Exception

    } // End of Method.

    /**
     * Method used to get Objectclasses OID.
     *
     * @param Name of Object.
     * @return String containing Object OID.
     */
    public String getObjectClassOID(String Name) {
        return (getObjectClassValue(Name, "NUMERICOID"));
    } // End of Method.

    /**
     * Method used to get Objectclasses Required Attributes.
     *
     * @param Name of Object.
     * @return ArrayList containing Objectclasses Required Attributes.
     */
    public List<Attributes> getObjectClassRequiredAttributes(String Name) {
        return (getObjectClassListValue(Name, "MUST"));
    } // End of Method.

    /**
     * Method used to get Objectclasses Optional Attributes.
     *
     * @param Name of Object.
     * @return ArrayList containing Objectclasses Optional Attributes.
     */
    public List<Attributes> getObjectClassOptionalAttributes(String Name) {
        return (getObjectClassListValue(Name, "MAY"));
    } // End of Method.

    /**
     * Method used to get Objectclasses Superior.
     *
     * @param Name of Object.
     * @return String containing Objectclasses Superior.
     */
    public String getObjectClassSuperior(String Name) {
        return (getObjectClassValue(Name, "SUP"));
    } // End of Method.

    /**
     * Method used to determine if Objectclass is Structural.
     *
     * @param Name of Object.
     * @return boolean indicates if Objectclass is Structural or not.
     */
    public boolean isObjectClassStructural(String Name) {
        String value = getObjectClassValue(Name, "STRUCTURAL");
        if (value.equalsIgnoreCase("true")) {
            return (true);
        } else {
            return (false);
        }
    } // End of Method.

    /**
     * Method used to determine if Objectclass is Auxiliary.
     *
     * @param Name of Object.
     * @return boolean indicates if Objectclass is Auxiliary or not.
     */
    public boolean isObjectClassAuxiliary(String Name) {
        String value = getObjectClassValue(Name, "AUXILIARY");
        if (value.equalsIgnoreCase("true")) {
            return (true);
        } else {
            return (false);
        }
    } // End of Method.

    /**
     * Method used to determine if Objectclass is Abstract.
     *
     * @param Name of Object.
     * @return boolean indicates if Objectclass is Abstract or not.
     */
    public boolean isObjectClassAbstract(String Name) {
        String value = getObjectClassValue(Name, "ABSTRACT");
        if (value.equalsIgnoreCase("true")) {
            return (true);
        } else {
            return (false);
        }
    } // End of Method.

    //* *****************************************
    //* *****************************************
    //  Syntax Specific Methods
    //* *****************************************
    //* *****************************************

    /**
     * Method used to generate the SyntaxDefinition TreeMap.
     * Needs to be created, since current DCL Schema does not
     * provide SYNTAX definitions.
     *
     * <pre>
     *               Syntax to OID, per IETF Draft:
     *               LDAPv3 Attribute Syntax Definitions
     *             Syntax Name                 H-R*   OID
     *             ~~~~~~~~~~~~~~~~~~~~~~~~~   ~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *             ACI Item                        N  1.3.6.1.4.1.1466.115.121.1.1
     *             Access Point                    Y  1.3.6.1.4.1.1466.115.121.1.2
     *             Attribute Type Description      Y  1.3.6.1.4.1.1466.115.121.1.3
     *             Audio                           N  1.3.6.1.4.1.1466.115.121.1.4
     *             Binary                          N  1.3.6.1.4.1.1466.115.121.1.5
     *             Bit String                      Y  1.3.6.1.4.1.1466.115.121.1.6
     *             Boolean                         Y  1.3.6.1.4.1.1466.115.121.1.7
     *             Certificate                     N  1.3.6.1.4.1.1466.115.121.1.8
     *             Certificate List                N  1.3.6.1.4.1.1466.115.121.1.9
     *             Certificate Pair                N  1.3.6.1.4.1.1466.115.121.1.10
     *             Country String                  Y  1.3.6.1.4.1.1466.115.121.1.11
     *             DN                              Y  1.3.6.1.4.1.1466.115.121.1.12
     *             Data Quality Syntax             Y  1.3.6.1.4.1.1466.115.121.1.13
     *             Delivery Method                 Y  1.3.6.1.4.1.1466.115.121.1.14
     *             Directory String                Y  1.3.6.1.4.1.1466.115.121.1.15
     *             DIT Content Rule Description    Y  1.3.6.1.4.1.1466.115.121.1.16
     *             DIT Structure Rule Description  Y  1.3.6.1.4.1.1466.115.121.1.17
     *             DL Submit Permission            Y  1.3.6.1.4.1.1466.115.121.1.18
     *             DSA Quality Syntax              Y  1.3.6.1.4.1.1466.115.121.1.19
     *             DSE Type                        Y  1.3.6.1.4.1.1466.115.121.1.20
     *             Enhanced Guide                  Y  1.3.6.1.4.1.1466.115.121.1.21
     *             Facsimile Telephone Number      Y  1.3.6.1.4.1.1466.115.121.1.22
     *             Fax                             N  1.3.6.1.4.1.1466.115.121.1.23
     *             Generalized Time                Y  1.3.6.1.4.1.1466.115.121.1.24
     *             Guide                           Y  1.3.6.1.4.1.1466.115.121.1.25
     *             IA5 String                      Y  1.3.6.1.4.1.1466.115.121.1.26
     *             INTEGER                         Y  1.3.6.1.4.1.1466.115.121.1.27
     *             JPEG                            N  1.3.6.1.4.1.1466.115.121.1.28
     *             LDAP Syntax Description         Y  1.3.6.1.4.1.1466.115.121.1.54
     *             LDAP Schema Definition          Y  1.3.6.1.4.1.1466.115.121.1.56
     *             LDAP Schema Description         Y  1.3.6.1.4.1.1466.115.121.1.57
     *             Master And Shadow Access Points Y  1.3.6.1.4.1.1466.115.121.1.29
     *             Matching Rule Description       Y  1.3.6.1.4.1.1466.115.121.1.30
     *             Matching Rule Use Description   Y  1.3.6.1.4.1.1466.115.121.1.31
     *             Mail Preference                 Y  1.3.6.1.4.1.1466.115.121.1.32
     *             MHS OR Address                  Y  1.3.6.1.4.1.1466.115.121.1.33
     *             Modify Rights                   Y  1.3.6.1.4.1.1466.115.121.1.55
     *             Name And Optional UID           Y  1.3.6.1.4.1.1466.115.121.1.34
     *             Name Form Description           Y  1.3.6.1.4.1.1466.115.121.1.35
     *             Numeric String                  Y  1.3.6.1.4.1.1466.115.121.1.36
     *             Object Class Description        Y  1.3.6.1.4.1.1466.115.121.1.37
     *             Octet String                    Y  1.3.6.1.4.1.1466.115.121.1.40
     *             OID                             Y  1.3.6.1.4.1.1466.115.121.1.38
     *             Other Mailbox                   Y  1.3.6.1.4.1.1466.115.121.1.39
     *             Postal Address                  Y  1.3.6.1.4.1.1466.115.121.1.41
     *             Protocol Information            Y  1.3.6.1.4.1.1466.115.121.1.42
     *             Presentation Address            Y  1.3.6.1.4.1.1466.115.121.1.43
     *             Printable String                Y  1.3.6.1.4.1.1466.115.121.1.44
     *             Substring Assertion             Y  1.3.6.1.4.1.1466.115.121.1.58
     *             Subtree Specification           Y  1.3.6.1.4.1.1466.115.121.1.45
     *             Supplier Information            Y  1.3.6.1.4.1.1466.115.121.1.46
     *             Supplier Or Consumer            Y  1.3.6.1.4.1.1466.115.121.1.47
     *             Supplier And Consumer           Y  1.3.6.1.4.1.1466.115.121.1.48
     *             Supported Algorithm             N  1.3.6.1.4.1.1466.115.121.1.49
     *             Telephone Number                Y  1.3.6.1.4.1.1466.115.121.1.50
     *             Teletex Terminal Identifier     Y  1.3.6.1.4.1.1466.115.121.1.51
     *             Telex Number                    Y  1.3.6.1.4.1.1466.115.121.1.52
     *             UTC Time                        Y  1.3.6.1.4.1.1466.115.121.1.53
     *
     *             H-R: Denotes Human-Readble Syntax.
     *  </pre>
     */
    private void createSyntax() {

        createSyntaxEntry("ACI Item", false, "1.3.6.1.4.1.1466.115.121.1.1", 1);
        createSyntaxEntry("Access Point", true, "1.3.6.1.4.1.1466.115.121.1.2", 2);
        createSyntaxEntry("Attribute Type Description", true, "1.3.6.1.4.1.1466.115.121.1.3", 3);
        createSyntaxEntry("Audio", false, "1.3.6.1.4.1.1466.115.121.1.4", 4);
        createSyntaxEntry("Binary", false, "1.3.6.1.4.1.1466.115.121.1.5", 5);
        createSyntaxEntry("Bit String", true, "1.3.6.1.4.1.1466.115.121.1.6", 6);
        createSyntaxEntry("Boolean", true, "1.3.6.1.4.1.1466.115.121.1.7", 7);
        createSyntaxEntry("Certificate", false, "1.3.6.1.4.1.1466.115.121.1.8", 8);
        createSyntaxEntry("Certificate List", false, "1.3.6.1.4.1.1466.115.121.1.9", 9);
        createSyntaxEntry("Certificate Pair", false, "1.3.6.1.4.1.1466.115.121.1.10", 10);
        createSyntaxEntry("Country String", true, "1.3.6.1.4.1.1466.115.121.1.11", 11);
        createSyntaxEntry("DN", true, "1.3.6.1.4.1.1466.115.121.1.12", 12);
        createSyntaxEntry("Data Quality Syntax", true, "1.3.6.1.4.1.1466.115.121.1.13", 13);
        createSyntaxEntry("Delivery Method", true, "1.3.6.1.4.1.1466.115.121.1.14", 14);
        createSyntaxEntry("Directory String", true, "1.3.6.1.4.1.1466.115.121.1.15", 15);
        createSyntaxEntry("DIT Content Rule Description", true, "1.3.6.1.4.1.1466.115.121.1.16", 16);
        createSyntaxEntry("DIT Structure Rule Description", true, "1.3.6.1.4.1.1466.115.121.1.17", 17);
        createSyntaxEntry("DL Submit Permission", true, "1.3.6.1.4.1.1466.115.121.1.18", 18);
        createSyntaxEntry("DSA Quality Syntax", true, "1.3.6.1.4.1.1466.115.121.1.19", 19);
        createSyntaxEntry("DSE Type", true, "1.3.6.1.4.1.1466.115.121.1.20", 20);
        createSyntaxEntry("Enhanced Guide", true, "1.3.6.1.4.1.1466.115.121.1.21", 21);
        createSyntaxEntry("Facsimile Telephone Number", true, "1.3.6.1.4.1.1466.115.121.1.22", 22);
        createSyntaxEntry("Fax", false, "1.3.6.1.4.1.1466.115.121.1.23", 23);
        createSyntaxEntry("Generalized Time", true, "1.3.6.1.4.1.1466.115.121.1.24", 24);
        createSyntaxEntry("Guide", true, "1.3.6.1.4.1.1466.115.121.1.25", 25);
        createSyntaxEntry("IA5 String", true, "1.3.6.1.4.1.1466.115.121.1.26", 26);
        createSyntaxEntry("INTEGER", true, "1.3.6.1.4.1.1466.115.121.1.27", 27);
        createSyntaxEntry("JPEG", false, "1.3.6.1.4.1.1466.115.121.1.28", 28);
        createSyntaxEntry("LDAP Syntax Description", true, "1.3.6.1.4.1.1466.115.121.1.54", 54);
        createSyntaxEntry("LDAP Schema Definition", true, "1.3.6.1.4.1.1466.115.121.1.56", 56);
        createSyntaxEntry("LDAP Schema Description", true, "1.3.6.1.4.1.1466.115.121.1.57", 57);
        createSyntaxEntry("Master And Shadow Access Points", true, "1.3.6.1.4.1.1466.115.121.1.29", 29);
        createSyntaxEntry("Matching Rule Description", true, "1.3.6.1.4.1.1466.115.121.1.30", 30);
        createSyntaxEntry("Matching Rule Use Description", true, "1.3.6.1.4.1.1466.115.121.1.31", 31);
        createSyntaxEntry("Mail Preference", true, "1.3.6.1.4.1.1466.115.121.1.32", 32);
        createSyntaxEntry("MHS OR Address", true, "1.3.6.1.4.1.1466.115.121.1.33", 33);
        createSyntaxEntry("Modify Rights", true, "1.3.6.1.4.1.1466.115.121.1.55", 55);
        createSyntaxEntry("Name And Optional UID", true, "1.3.6.1.4.1.1466.115.121.1.34", 34);
        createSyntaxEntry("Name Form Description", true, "1.3.6.1.4.1.1466.115.121.1.35", 35);
        createSyntaxEntry("Numeric String", true, "1.3.6.1.4.1.1466.115.121.1.36", 36);
        createSyntaxEntry("Object Class Description", true, "1.3.6.1.4.1.1466.115.121.1.37", 37);
        createSyntaxEntry("Octet String", true, "1.3.6.1.4.1.1466.115.121.1.40", 40);
        createSyntaxEntry("OID", true, "1.3.6.1.4.1.1466.115.121.1.38", 38);
        createSyntaxEntry("Other Mailbox", true, "1.3.6.1.4.1.1466.115.121.1.39", 39);
        createSyntaxEntry("Postal Address", true, "1.3.6.1.4.1.1466.115.121.1.41", 41);
        createSyntaxEntry("Protocol Information", true, "1.3.6.1.4.1.1466.115.121.1.42", 42);
        createSyntaxEntry("Presentation Address", true, "1.3.6.1.4.1.1466.115.121.1.43", 43);
        createSyntaxEntry("Printable String", true, "1.3.6.1.4.1.1466.115.121.1.44", 44);
        createSyntaxEntry("Substring Assertion", true, "1.3.6.1.4.1.1466.115.121.1.58", 58);
        createSyntaxEntry("Subtree Specification", true, "1.3.6.1.4.1.1466.115.121.1.45", 45);
        createSyntaxEntry("Supplier Information", true, "1.3.6.1.4.1.1466.115.121.1.46", 46);
        createSyntaxEntry("Supplier Or Consumer", true, "1.3.6.1.4.1.1466.115.121.1.47", 47);
        createSyntaxEntry("Supplier And Consumer", true, "1.3.6.1.4.1.1466.115.121.1.48", 48);
        createSyntaxEntry("Supported Algorithm", false, "1.3.6.1.4.1.1466.115.121.1.49", 49);
        createSyntaxEntry("Telephone Number", true, "1.3.6.1.4.1.1466.115.121.1.50", 50);
        createSyntaxEntry("Teletex Terminal Identifier", true, "1.3.6.1.4.1.1466.115.121.1.51", 51);
        createSyntaxEntry("Telex Number", true, "1.3.6.1.4.1.1466.115.121.1.52", 52);
        createSyntaxEntry("UTC Time", true, "1.3.6.1.4.1.1466.115.121.1.53", 53);

        return;

    } // End of Method.

    /**
     * Method used to create Syntax Entry.
     *
     * @param Name of Syntax.
     * @param HR Human Readable indicator.
     * @param OID -- Object Identifier.
     * @param IRRsynid  --  Internal Syntax Id.
     */
    private void createSyntaxEntry(String Name,
                                   boolean HR,
                                   String OID,
                                   int IRRsynid) {

        Boolean bHR = new Boolean(HR);

        // Create attributes to be associated with object
        Attributes attrs = new BasicAttributes(true); // case-ignore
        attrs.put("name", Name);
        attrs.put("oid", OID);
        attrs.put("HR", bHR.toString());
        attrs.put("IRRsynid", Integer.toString(IRRsynid));

        syTM.put(Name.toLowerCase(), attrs);

        return;
    } // End of Method.

    // **********************************************
    // Method used to get Objects List Value
    private List<Attributes> getSyntaxListValue(String Name, String Type) {
        Name = Name.toLowerCase();
        List<Attributes> myList = new ArrayList<>();
        Attributes myAttrs = syTM.get(Name);
        if (myAttrs == null) {
            return (myList);
        }
        Attribute myValue = myAttrs.get(Type);
        if (myValue == null) {
            return (myList);
        }

        try {
            for (NamingEnumeration myEnum = myValue.getAll();
                 myEnum.hasMore(); ) {
                myList.add((Attributes)myEnum.next());

            } // End of For Loop

            return (myList);

        } catch (NamingException e) {
            System.err.println("IRRschema Error Obtaining Schema Attribute Name Value from Map: " + e);
            e.printStackTrace();
            return (myList);
        } // End of Exception
    } // End of Method.

    /**
     * Method used to get SYNTAX Content Value.
     *
     * @param Name of Syntax.
     * @param Type Attribute Type or Name.
     * @return String Requested Attribute.
     */
    private String getSyntaxValue(String Name, String Type) {
        Name = Name.toLowerCase();
        Attributes myAttrs = (Attributes) syTM.get(Name);
        if (myAttrs == null) {
            return ("");
        }
        Attribute myValue = myAttrs.get(Type);
        if (myValue == null) {
            return ("");
        }

        try {
            return ((String) myValue.get());
        } catch (NamingException e) {
            System.err.println("IRRschema Error Obtaining Schema Syntax Value from Map: " + e);
            e.printStackTrace();
            return ("");
        } // End of Exception

    } // End of Method.

    /**
     * Method used to get SYNTAX OID.
     *
     * @param Name of Syntax.
     * @return String OID.
     */
    public String getSyntaxOID(String Name) {
        return (getSyntaxValue(Name, "OID"));
    } // End of Method.

    /**
     * Method used to get Internal SYNTAX ID.
     *
     * @param Name of Syntax.
     * @return int Internal Syntax ID.
     */
    public int getSyntaxIRRid(String Name) {
        String value = getSyntaxValue(Name, "IRRsynid");
        if (value.equals("")) {
            return (SYNTAXunknown);
        }
        try {
            Integer Ivalue = Integer.valueOf(value);
            return (Ivalue.intValue());
        } catch (NumberFormatException e) {
            return (SYNTAXunknown);
        } catch (Exception e) {
            System.err.println("IRRschema Error Schema Syntax IRRsynid from Map: " + e);
            e.printStackTrace();
            return (SYNTAXunknown);
        } // End of Exception
    } // End of Method.

    /**
     * Method used to Determine if Syntax is
     * Human Readable.
     *
     * @param Name of Syntax.
     * @return boolean indicates if Syntax is Human-Readable or not.
     */
    public boolean IsSyntaxHumanReadable(String Name) {
        String value = getSyntaxValue(Name, "HR");
        if (value.equalsIgnoreCase("true")) {
            return (true);
        } else {
            return (false);
        }
    } // End of Method.

    /**
     * Method used to dump All Objects contained within TreeMap in XML Document Format.
     *
     * @param myTM        Can be the Attribute, Objectclass or Syntax TreeMap.
     * @param SO Directed output of show or display.
     * @param sname         current level Tag Name.
     * @throws java.io.IOException
     */
    private void ShowListContents(TreeMap myTM, BufferedWriter SO, String sname)
            throws IOException {


        Set mySet = myTM.entrySet();
        Iterator itr = mySet.iterator();
        while (itr.hasNext()) {
            Map.Entry oit = (Map.Entry) itr.next();
            if ((sname == null) || (sname.equals(""))) {
                SO.write("\t<" + oit.getKey() + ">\n");
            } else {
                SO.write("\t<" + sname + ">\n");
            }

            Attributes entryattrs = (Attributes) oit.getValue();
            if (entryattrs == null) {
                continue;
            }
            try {

                // **************************************************
                // Always show the Name first.
                Attribute myattr = entryattrs.get("name");
                for (NamingEnumeration ev = myattr.getAll(); ev.hasMore(); ) {
                    SO.write("\t\t<" + myattr.getID() +
                            ">" + ev.next() + "</" +
                            myattr.getID() + ">\n");
                } // End of name For Loop.

                // **************************************************
                // Now Show all other attributes.
                for (NamingEnumeration ea = entryattrs.getAll();
                     ea.hasMore(); ) {
                    myattr = (Attribute) ea.next();
                    if (!"name".equalsIgnoreCase(myattr.getID())) {
                        for (NamingEnumeration ev = myattr.getAll(); ev.hasMore(); ) {
                            SO.write("\t\t<" + myattr.getID() +
                                    ">" + ev.next() + "</" +
                                    myattr.getID() + ">\n");
                        } // End of Inner For Loop.
                    } // End of if Name.
                } // End of For Loop

            } catch (NamingException e) {
                System.err.println("IRRschema Error Obtaining Schema Value from Map: " + e);
                e.printStackTrace();
            } // End of Exception
            if ((sname == null) || (sname.equals(""))) {
                SO.write("\t</" + oit.getKey() + ">\n");
            } else {
                SO.write("\t</" + sname + ">\n");
            }
        } // End of While.

    } // End of Method.

    /**
     * Method used to print All Objects in Array Lists in XML
     * formmated document, used for Schema Backup.
     *
     * @param SO of output file stream.
     *
     */
    public void printAll(BufferedWriter SO) throws IOException {

        SO.write("<IRRSchema>\n");

        SO.write("\n<SyntaxDefinition>\n");
        ShowListContents(syTM, SO, "SyntaxEntry");
        SO.write("</SyntaxDefinition>\n");

        SO.write("<AttributeDefinition>\n");
        ShowListContents(atTM, SO, "Attribute");
        SO.write("</AttributeDefinition>\n");

        SO.write("\n<ClassDefinition>\n");
        ShowListContents(ocTM, SO, "ObjectClass");
        SO.write("</ClassDefinition>\n");

        SO.write("\n<UnknownDefinition>\n");
        ShowListContents(unknownTM, SO, null);
        SO.write("</UnknownDefinition>\n\n");

        SO.write("</IRRSchema>\n\n");

    } // End of Method.

} ///: End of idxIRRschema Class.
