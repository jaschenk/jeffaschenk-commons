package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

/**
 * @author Jeff.Schenk@gmail.com
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
