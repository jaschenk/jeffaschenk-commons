package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 * Java Class to Implement a JNDI Named Parser for the IRR.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxNameParser {

    private static final Properties syntax = new Properties();

    static {

        syntax.put("jndi.syntax.direction", "left_to_right");
        syntax.put("jndi.syntax.separator", ",");
        syntax.put("jndi.syntax.ignorecase", "true");
        syntax.put("jndi.syntax.escape", "\\");
        syntax.put("jndi.syntax.trimblanks", "true");
        syntax.put("jndi.syntax.separator.ava", ",");
        syntax.put("jndi.syntax.separator.typeval", "=");

    } // End of Static

    /**
     * Parse a CompoundName from a String Object.
     *
     * @param name DN.
     * @return CompoundName JNDI Naming Object based upon established syntax.
     * @throws InvalidNameException if incoming DN String is invalid.
     */
    public static CompoundName parse(String name) throws NamingException {
        return new CompoundName(name, syntax);
    } // End of Name

} ///:~ End of idxNameParser Class
