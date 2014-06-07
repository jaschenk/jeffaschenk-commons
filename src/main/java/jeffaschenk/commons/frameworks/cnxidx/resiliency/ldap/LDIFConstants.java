package jeffaschenk.commons.frameworks.cnxidx.resiliency.ldap;


/**
 * Provides Common Interface for LDIF Constants.
 *
 * @author jeff.schenk
 * @since 2005.11.02
 */

public interface LDIFConstants {

    // *******************************
    // Constants.
    public static final String LDIF_ADD_CHANGETYPE = "add";
    public static final String LDIF_DELETE_CHANGETYPE = "delete";
    public static final String LDIF_MODRDN_CHANGETYPE = "modrdn";
    public static final String LDIF_MODDN_CHANGETYPE = "moddn";
    public static final String LDIF_MODIFY_CHANGETYPE = "modify";

    public static final String LDIF_ATTRIBUTE_DELETE = "delete";
    public static final String LDIF_ATTRIBUTE_REPLACE = "replace";
    public static final String LDIF_ATTRIBUTE_ADD = "add";

    public static final String NEWRDN = "newrdn";
    public static final String DELETEOLDRDN = "deleteoldrdn";
    public static final String NEWSUPERIOR = "newsuperior";

    public static final String CONTROL = "control";
    public static final String TREE_DELETE_CONTROL = "1.2.840.113556.1.4.805";

} ///:~ End of LDIFConstants Class.
