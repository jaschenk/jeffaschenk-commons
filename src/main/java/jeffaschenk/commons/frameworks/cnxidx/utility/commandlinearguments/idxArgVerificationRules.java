package jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxParseDN;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Java Class to provide a standard argument verification interface
 * for incoming arguments to a utility with a Main class.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxArgVerificationRules {
    // ***********************************************
    // Logging Facilities.
    public static final String CLASSNAME = idxArgVerificationRules.class.getName();

    // ***********************************************
    // Globals
    private Boolean FALSE = new Boolean(false);
    private Boolean TRUE = new Boolean(true);

    private String Name;
    private boolean Required;
    private boolean StringObject;
    private boolean Viewable;
    private String Rules;

    /**
     * Initial Constructor used to Construct Object.
     *
     * @param _Name         Name of Argument.
     * @param _Required     Indicates if Required or not.
     * @param _StringObject Indicates if a String Object or not.
     * @param _Viewable     Indicates if argument is viewable.
     * @param _Rules        Name of Rules Method.
     */
    public idxArgVerificationRules(String _Name,
                                   boolean _Required,
                                   boolean _StringObject,
                                   boolean _Viewable,
                                   String _Rules) {
        Name = _Name.toLowerCase();
        Required = _Required;
        Viewable = _Viewable;
        StringObject = _StringObject;
        Rules = _Rules;

        if (Rules == null) {
            Rules = "verifyCommon";
        }

    } // End of Constructor Class.

    /**
     * Initial Constructor used to Construct Object.
     *
     * @param _Name         Name of Argument.
     * @param _Required     Indicates if Required or not.
     * @param _StringObject Indicates if a String Object or not.
     */
    public idxArgVerificationRules(String _Name,
                                   boolean _Required,
                                   boolean _StringObject) {
        Name = _Name.toLowerCase();
        Required = _Required;
        StringObject = _StringObject;
        Rules = "verifyCommon";
        Viewable = false;

    } // End of Constructor Class.

    /**
     * Initial Constructor used to Construct Object.
     *
     * @param _Name         Name of Argument.
     * @param _Required     Indicates if Required or not.
     * @param _StringObject Indicates if a String Object or not.
     * @param _Rules        Name of Rules Method.
     */
    public idxArgVerificationRules(String _Name,
                                   boolean _Required,
                                   boolean _StringObject,
                                   String _Rules) {
        Name = _Name.toLowerCase();
        Required = _Required;
        StringObject = _StringObject;
        Rules = _Rules;
        Viewable = false;

        if (Rules == null) {
            Rules = "verifyCommon";
        }

    } // End of Constructor Class.


    /**
     * Method to show contents of Entry to STDOUT.
     * Perform Simple print output of Entry.
     *
     * @param _INA which contains the Named Object.
     */
    public void show(idxArgParser _INA) {
        if (_INA.doesNameExist(Name)) {
            String Type = "Object"; // Assume Object.
            String Value = null;
            if (_INA.getValue(Name) instanceof Boolean) {
                Type = "Boolean";
                Value = ((Boolean) _INA.getValue(Name)).toString();
            } else if (_INA.getValue(Name) instanceof String) {
                Type = "String," +
                        " Length:[" + ((String) _INA.getValue(Name)).length() + "]";
                Value = (String) _INA.getValue(Name);
            } // End of Else If.

            // ********************************
            // Show Message.
            FrameworkLogger.log(CLASSNAME, "show", FrameworkLoggerLevel.INFO,
                    MessageConstants.ARGPARSER_SHOW_PARSED,
                    new String[]{Name, Value, Type});
        } // End of If.
        else {
            // ********************************
            // Show Message.
            FrameworkLogger.log(CLASSNAME, "show", FrameworkLoggerLevel.INFO,
                    MessageConstants.ARGPARSER_SHOW_PARSED_VALUE_NOT_SPECIFIED,
                    new String[]{Name});
        } // End of Else.

    } // End of Method.

    /**
     * Method to Perform Validation Routines for the specified
     * Object.
     *
     * @param _INA which contains the Named Object.
     * @return Object Returns String Error Message or boolean "TRUE" if entry validates.
     */
    public Object isValid(idxArgParser _INA) {
        String emsg = null;

        Class[] argStringClass = {String.class};
        Class[] argBooleanClass = {Boolean.class};

        // ********************************
        // Verify the entry exists if
        // a required entry.
        //
        if ((Required) && (!_INA.doesNameExist(Name))) {
            emsg = "Required Argument Named:[" + Name + "], has not been specified.";
            return (emsg);
        }

        if ((!Required) && (!_INA.doesNameExist(Name))) {
            return (TRUE);
        }

        // ********************************
        // Verify the Object is correct
        // instance either boolean or String.
        //
        if ((_INA.getValue(Name) instanceof Boolean) &&
                (StringObject)) {
            emsg = "Object Argument Named:[" + Name + "], has no value association.";
            return (emsg);
        }

        if ((_INA.getValue(Name) instanceof String) &&
                (!StringObject)) {
            emsg = "Object Argument Named:[" + Name + "], has a value association, must be boolean.";
            return (emsg);
        }

        if ((_INA.getValue(Name) instanceof String) &&
                (StringObject) &&
                (((String) _INA.getValue(Name)).length() <= 0)) {
            emsg = "Object Argument Named:[" + Name + "], has a NULL Value association, must have a valid non-null String Value.";
            return (emsg);
        }

        // ********************************
        // Now invoke the routine that
        // will perform the Data Rule check.
        // If no rules specified, assume all
        // ok.
        //
        if (Rules == null) {
            return (TRUE);
        }
        try {
            Method method = null;
            if (_INA.getValue(Name) instanceof Boolean) {
                method = getClass().getMethod(Rules, argBooleanClass);
            } else {
                method = getClass().getMethod(Rules, argStringClass);
            }
            Object[] args = {_INA.getValue(Name)};

            return (method.invoke(this, args));

        } catch (InvocationTargetException e) {
            emsg = "Invocation Target Exception encountered while processing " +
                    "Object Argument Named:[" +
                    Name + "],\n" + e;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            emsg = "No Such Method Named:[" + Rules +
                    "], for Verification of Object Argument Named:[" +
                    Name + "],\n" + e;
        } catch (Exception e) {
            emsg = "Exception encountered while processing Object Argument Named:[" +
                    Name + "],\n" + e;
            e.printStackTrace();
        } // End of Exception.

        return (emsg);

    } // End of Method.

    /**
     * Obtain Name of Object.
     *
     * @return String Name of Object.
     */
    public String getName() {
        return (Name);
    }

    /**
     * Method returns boolean to indicate if Named Object is Required.
     *
     * @return boolean indicates if named object is required.
     */
    public boolean isRequired() {
        return (Required);
    }

    /**
     * Method returns boolean if Named Object is a String Object.
     *
     * @return boolean indicates if named object is a String Object.
     */
    public boolean isStringObject() {
        return (StringObject);
    }

    /**
     * Method returns Name of Verification Rule.
     *
     * @return String returned containing Rules Name.
     */
    public String getRulesName() {
        return (Rules);
    }

    /**
     * Provides conversion of Object to a Information String.
     *
     * @return String returned containing Named Object Information.
     */
    public String toString() {
        return (Name + "," + Required + "," + StringObject + "," + Rules);
    } // End of Method.

    // *****************************************************
    // Dynamically Called Method, using Java Reflection.
    // *****************************************************

    /**
     * Common Verification Method.
     *
     * @param _in Named Objects incoming String content Value.
     * @return boolean indicates if Value is valid or not.
     */
    public boolean verifyCommon(String _in) {
        if ((_in == null) ||
                (_in.length() <= 0)) {
            return (false);
        }
        return (true);
    } // End of Method.

    /**
     * Common Verification Method.
     *
     * @param _in Named Objects incoming boolean content Value.
     * @return boolean indicates if Value is valid or not.
     */
    public boolean verifyCommon(Boolean _in) {
        if (_in == null) {
            return (false);
        }
        return (true);
    } // End of Method.

    /**
     * Top Domain Verification Method.
     *
     * @param _in Named Objects incoming String content Value.
     * @return boolean indicates if Value is valid or not.
     */
    public Object verifyTopDomainName(String _in) {
        _in = _in.trim();
        if (_in.length() < 4) {
            String emsg = "TopDomain Name:[" + _in +
                    "] Invalid, must begin with \042dc=\042";
            return (emsg);
        } // End of If.

        _in = _in.toLowerCase();
        idxParseDN pDN = new idxParseDN(_in);

        if (("".equals(pDN.getNamingAttribute())) ||
                (!"dc".equalsIgnoreCase(pDN.getNamingAttribute()))) {
            String emsg = "TopDomain Name:[" + _in +
                    "] Invalid, must begin with \042dc=\042";
            return (emsg);
        } // End of If.

        return (TRUE);
    } // End of Method.

    /**
     * Verify Domain Container Name Contents.
     *
     * @param _in Named Objects incoming String content Value.
     * @return Object A True boolean indicates Value is valid, a
     *         String Object Returned contains an Syntax Error Message.
     *         <p/>
     *         <PRE>
     *         A Note from John Strassner:
     *         hmmm, depends if you want to use RFC2253 or draft-ietf-ldapbis-dn-06 as
     *         the reference. I'm assuming the former.
     *         The first character MUST NOT be a space or an octothorpe ('#'), and MUST
     *         NOT have a space at the end. It also MUST NOT contain any of the following
     *         characters UNLESS they are escaped: ",", "+", """, "\", "<", ">" or ";".
     *         Non-ASCII characters may be escaped using ASCII 092 ('\') followed by two
     *         hex digits (which form a single byte to represent the character code).
     *         <p/>
     *         I don't see that logic captured here. What I'm worried about is that if
     *         we claim to be X.500 compliant, we're going to have to be able to translate
     *         from ASN.1 to a String defined in RFC2253 and vice-versa.
     *         <p/>
     *         I assume that the comma is prevented because you don't want to deal with
     *         multi-part names (which is fine, I just want to make sure).
     *         <p/>
     *         not strictly true, they could be escaped, but if your intent is to disallow
     *         this foolish X.500-ism, I heartily agree. ;-)
     *         </PRE>
     *         <p/>
     *         <PRE>
     *         A Note from Jeff Schenk:
     *         In response to John, this verification routine provides what we would like
     *         to see and support for a customer's top level domain name. Anything that is detected as
     *         not valid, may be valid to a specification, but is not recommended by FRAMEWORK for
     *         use.
     *         <p/>
     *         This may become an issue for foreign language support and hence will need to revised.
     *         <p/>
     *         A Multi-Valued naming Attribute is defined by the use of a "+" sign in LDAP and not
     *         a comma. A comma is the default AVA seperator.
     *         <p/>
     *         </PRE>
     */
    public Object verifyDCContainerName(String _in) {
        _in = _in.trim();
        if ((_in == null) ||
                (_in.indexOf(',') != -1) ||
                (_in.indexOf('\042') != -1) ||
                (_in.indexOf('\047') != -1) ||
                (_in.indexOf('\057') != -1) ||
                (_in.indexOf('\074') != -1) ||
                (_in.indexOf('\076') != -1) ||
                (_in.indexOf('\077') != -1) ||
                (_in.indexOf('\134') != -1) ||
                (_in.indexOf('\140') != -1) ||
                (_in.indexOf(':') != -1) ||
                (_in.indexOf(';') != -1) ||
                (_in.indexOf('.') != -1) ||
                (_in.indexOf('=') != -1) ||
                (_in.indexOf('*') != -1) ||
                (_in.indexOf(' ') != -1)) {
            String emsg = "Domain Container Name Invalid, " +
                    "Must not contain any of these characters:[,\042\047\057\074\076\077\134\140:;.=*], " +
                    "and contain no spaces.";
            return (emsg);
            /**
             * ^not strictly true, they could be escaped, but if your intent is to disallow
             * this foolish X.500-ism, I heartily agree. ;-)
             * [JAS] That is absolutely correct. :)
             */
        } // End of If.

        return (TRUE);
    } // End of Method.

    /**
     * Verify OrganizationalUnit Container Name Contents.
     *
     * @param _in Named Objects incoming String content Value.
     * @return Object A True boolean indicates Value is valid, a
     *         String Object Returned contains an Syntax Error Message.
     *         <p/>
     *         <PRE>
     *         A Note from Jeff Schenk:
     *         In response to John, this verification routine provides what we would like
     *         to see and support for a customer's top level domain name. Anything that is detected as
     *         not valid, may be valid to a specification, but is not recommended by FRAMEWORK for
     *         use.
     *         <p/>
     *         This may become an issue for foreign language support and hence will need to revised.
     *         <p/>
     *         A Multi-Valued naming Attribute is defined by the use of a "+" sign in LDAP and not
     *         a comma. A comma is the default AVA seperator.
     *         <p/>
     *         </PRE>
     */
    public Object verifyOUContainerName(String _in) {
        _in = _in.trim();
        if ((_in == null) ||
                (_in.indexOf(',') != -1) ||
                (_in.indexOf('\042') != -1) ||
                (_in.indexOf('\047') != -1) ||
                (_in.indexOf('\057') != -1) ||
                (_in.indexOf('\074') != -1) ||
                (_in.indexOf('\076') != -1) ||
                (_in.indexOf('\077') != -1) ||
                (_in.indexOf('\134') != -1) ||
                (_in.indexOf('\140') != -1) ||
                (_in.indexOf(':') != -1) ||
                (_in.indexOf(';') != -1) ||
                (_in.indexOf('.') != -1) ||
                (_in.indexOf('=') != -1) ||
                (_in.indexOf('*') != -1)) {
            String emsg = "OU|Realm Container Name Invalid, " +
                    "Must not contain any of these characters:[,\042\047\057\074\076\077\134\140:;.=*].";
            return (emsg);
            /**
             * ^not strictly true, they could be escaped, but if your intent is to disallow
             * this foolish X.500-ism, I heartily agree. ;-)
             * [JAS] That is absolutely correct. :)
             */
        } // End of If.

        return (TRUE);
    } // End of Method.

    /**
     * Verify Password Contents.
     *
     * @param _in Named Objects incoming String content Value.
     * @return Object A True boolean indicates Value is valid, a
     *         String Object Returned contains an Syntax Error Message.
     */
    public Object verifyPassword(String _in) {
        _in = _in.trim();
        if ((_in.length() < 6) ||
                (_in.indexOf(' ') != -1)) {
            String emsg = "Password is Invalid, " +
                    "Must be at least 6 Characters in length, containing no spaces.";
            return (emsg);
        } // End of If.

        if (_in.length() > 8) {
            String emsg = "Password is Invalid, " +
                    "Maximum is 8 Characters in length, containing no spaces.";
            return (emsg);
        } // End of If.

        return (TRUE);

    } // End of Method.

} ///:~ End of idxArgVerificationRules Class.
