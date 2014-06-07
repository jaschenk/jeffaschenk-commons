package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

public interface LDAPv2 {
    /***************************************************************************/
  /* Named Constants (option names specified in Java LDAP API, integer value */
  /* made up)                                                                */
    /***************************************************************************/
    /**
     * Used to set/get the option DEREF on an LDAPConnection object
     */
    public static final int DEREF = 1;

    /**
     * Used to set/get the option TIMELIMIT on an LDAPConnection object
     */
    public static final int TIMELIMIT = 2;

    /**
     * Used to set/get the option REFERRALS on an LDAPConnection object
     */
    public static final int REFERRALS = 3;

    /**
     * Used to set/get the option REFERRALS_REBIND_PROC on an LDAPConnection
     * object.
     */
    public static final int REFERRALS_REBIND_PROC = 4;

    /**
     * Used to set/get the option REFERRALS_HOP_LIMIT an LDAPConnection object
     */
    public static final int REFERRALS_HOP_LIMIT = 5;

    /**
     * Used to set/get the option BATCHSIZE on an LDAPConnection object
     */
    public static final int BATCHSIZE = 6;

    /**
     * Used to set/get the option SIZELIMIT on an LDAPConnection object.
     */
    public static final int SIZELIMIT = 7;

    /***************************************************************************/
  /* Named Constants (option values)                                         */
    /***************************************************************************/
    /**
     * Used to indicate behaviour in dereferencing aliases.
     * <p/>
     * <p>Used Integer wrapped when setting behaviour using
     * LDAPConnection.setOption(), or as is when constructing or using the
     * accessors of an LDAPSearchConstraints object.
     */
    public static final int DEREF_NEVER = 0;

    /**
     * Used to indicate behaviour in dereferencing aliases.
     * <p/>
     * <p>Used Integer wrapped when setting behaviour using
     * LDAPConnection.setOption(), or as is when constructing or using the
     * accessors of an LDAPSearchConstraints object.
     */
    public static final int DEREF_SEARCHING = 1;

    /**
     * Used to indicate behaviour in dereferencing aliases.
     * <p/>
     * <p>Used Integer wrapped when setting behaviour using
     * LDAPConnection.setOption(), or as is when constructing or using the
     * accessors of an LDAPSearchConstraints object.
     */
    public static final int DEREF_FINDING = 2;

    /**
     * Used to indicate behaviour in dereferencing aliases.
     * <p/>
     * <p>Used Integer wrapped when setting behaviour using
     * LDAPConnection.setOption(), or as is when constructing or using the
     * accessors of an LDAPSearchConstraints object.
     */
    public static final int DEREF_ALWAYS = 3;


    /***************************************************************************/
  /* Named Constants (scope values)                                          */
    /***************************************************************************/
    /**
     * Used to indicate the scope of a search.
     */
    public static final int SCOPE_BASE = 0;

    /**
     * Used to indicate the scope of a search.
     */
    public static final int SCOPE_ONE = 1;

    /**
     * Used to indicate the scope of a search.
     */
    public static final int SCOPE_SUB = 2;

}



