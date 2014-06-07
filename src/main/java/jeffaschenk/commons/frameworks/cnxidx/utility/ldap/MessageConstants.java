// *****************************************************************************
// MessageConstants.java
//
// *****************************************************************************
// I N T E L L I D E N,  I N C O R P O R A T E D
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// The information contained herein is copyrighted by and is Confidential and
// Proprietary to FRAMEWORK Inc.  All information contained herein,
// remains and shall remain the property of FRAMEWORK Inc. and no license
// or other rights to any information contained herein, or derivatives thereof,
// is granted or implied, except as authorized by FRAMEWORK Inc. in a
// separate agreement granting such rights. Viewer of this information and all
// associated documentation agrees not to disclose any of said information
// contained herein. 
// For the purposes of this statement, Confidential and Proprietary includes,
// but is not limited to information, drawings, specifications, sketches,
// models samples, data, computer programs, documentation or know-how. 
// Confidential and Proprietary also extends to (i)  for copyrightable or
// copyrighted material, any translation, abridgement, revision or other form
// in which an existing work may be recast, transformed or adapted;
// (ii) for patented or patentable material, any improvement thereof; and
// (iii) for material which is protected by trademark, any new material
// which may be protected by copyright, patent and/or trade secret.
//
// *****************************************************************************
package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

/**
 * @authors john.leichner, Jeff.Schenk
 */
public interface MessageConstants {
    // Messages should be grouped by their functional area within the application

    // Directory Context Extension Messages.
    public static final String ICOSDIRCONTEXT_REMOVED_INTERNAL_ATTRIBUTE
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0001";
    public static final String ICOSDIRCONTEXT_REMOVE_INTERNAL_ATTRIBUTE_FAILED
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0002";
    public static final String ICOSDIRCONTEXT_REMOVED_INTERNAL_ATTRIBUTE_FROM_MODITEM
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0003";
    public static final String ICOSDIRCONTEXT_REMOVE_INTERNAL_ATTRIBUTE_FAILED_FROM_MODITEM
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0004";
    public static final String ICOSDIRCONTEXT_RETRY_REQUEST
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0005";
    public static final String ICOSDIRCONTEXT_MAX_RETRY_EXHAUSTED
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0006";

    // Directory Statistic/Debug Messages.
    public static final String ICOSDIRCONTEXT_INITIALIZED
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0010";
    public static final String ICOSDIRCONTEXT_CLOSING
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0099";
    public static final String ICOSDIRCONTEXT_BIND
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0100";
    public static final String ICOSDIRCONTEXT_CREATESUBCONTEXT
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0105";
    public static final String ICOSDIRCONTEXT_GETATTRIBUTES
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0106";
    public static final String ICOSDIRCONTEXT_GETSCHEMA
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0107";
    public static final String ICOSDIRCONTEXT_GETSCHEMACLASSDEFINITION
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0108";
    public static final String ICOSDIRCONTEXT_DESTROYSUBCONTEXT
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0109";
    public static final String ICOSDIRCONTEXT_REBIND
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0200";
    public static final String ICOSDIRCONTEXT_REBIND_OBJECT
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0201";
    public static final String ICOSDIRCONTEXT_RENAME
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0202";
    public static final String ICOSDIRCONTEXT_UNBIND
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0203";
    public static final String ICOSDIRCONTEXT_COMPOSENAME
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0204";
    public static final String ICOSDIRCONTEXT_GETNAMEPARSER
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0205";
    public static final String ICOSDIRCONTEXT_LIST
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0206";
    public static final String ICOSDIRCONTEXT_LISTBINDINGS
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0207";
    public static final String ICOSDIRCONTEXT_LOOKUP
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0208";
    public static final String ICOSDIRCONTEXT_LOOKUPLINK
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0209";

    // Modification Messages
    public static final String ICOSDIRCONTEXT_MODIFYATTRIBUTES
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0300";
    public static final String ICOSDIRCONTEXT_MODIFYATTRIBUTES_WITHMODITEMS
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0301";

    // Search Messages
    public static final String ICOSDIRCONTEXT_SEARCH_BY_MATCHATTRS
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0401";
    public static final String ICOSDIRCONTEXT_SEARCH_BY_MATCHATTRS_ATTR_TO_RETURN
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0402";
    public static final String ICOSDIRCONTEXT_SEARCH_BY_FILTER_WITHARGS
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0403";
    public static final String ICOSDIRCONTEXT_SEARCH_BY_FILTER
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0404";

    // Finialize Messages.
    public static final String ICOSDIRCONTEXT_METHOD_ELAPSED_TIME
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0900";

    public static final String ICOSDIRCONTEXT_METHOD_ELAPSED_TIME_WITH_FAILURE
            = "jeffaschenk.commons.utility.ldap.IcosDirContext.0910";

} ///:~ End of MessageConstants
