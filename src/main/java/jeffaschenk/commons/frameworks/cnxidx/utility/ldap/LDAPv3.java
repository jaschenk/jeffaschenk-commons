package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

public interface LDAPv3 extends LDAPv2 {
    /***************************************************************************/
  /* Named Constants (option names from Java LDAP API, values made up)       */
    /***************************************************************************/
    /**
     * used to set/get the option PREFERRED_LANGUAGE on an LDAPConnection object
     */
    public static final int PREFERRED_LANGUAGE = 10;


    /**
     * used to set/get the option SERVERCONTROLS on an LDAPConnection object
     */
    public static final int SERVERCONTROLS = 11;

    /**
     * used to set/get the option CLIENTCONTROLS on an LDAPConnection object
     */
    public static final int CLIENTCONTROLS = 12;

}


