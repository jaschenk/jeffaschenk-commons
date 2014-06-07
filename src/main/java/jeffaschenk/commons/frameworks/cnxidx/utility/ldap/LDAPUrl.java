/* ************************************************************************* */
/* $Workfile:   lvclurll.jp  $ $Revision: #2 $                               */
/* COPYRIGHT (C) 1998 DATA CONNECTION LIMITED                                */
/*****************************************************************************/
/* LVCLURLL.JP                                                               */
/*                                                                           */
/* Package: uk.co.datcon.odg.dclava                                          */
/*****************************************************************************/
package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.net.*;
import java.util.*;

public class LDAPUrl {

    /**
     * Constructs an LDAPUrl object using the string provided
     *
     * @param xiurl the URL to base the LDAPUrl object on, as a String
     * @throws java.net.MalformedURLException thrown if the String fails to conform to
     *                                        the correct syntax for an LDAP URL
     */
    public LDAPUrl(String xiurl)
            throws MalformedURLException {
        decodeURL(xiurl);
    }

    /**
     * Constructs an LDAPUrl object with the specified parameters.
     * <p/>
     * <p>All parameters not directly specified will be set to their default
     * values (search scope base, filter (objectclass=*)).
     * <p/>
     * <p>Note that this constructor assumes these have NO escaped characters in
     * them (no %HH sequences). Any such escaped sequences will be mangled, as
     * the initial % character will be escaped.
     *
     * @param xihost the host to connect to
     * @param xiport the port to connect to
     * @param xidn   the DN to base the operation on
     */
    public LDAPUrl(String xihost,
                   int xiport,
                   String xidn) {
        this(xihost, xiport, xidn, null, LDAPv2.SCOPE_BASE, DEFAULT_FILTER);
    }

    /**
     * constructs an LDAPUrl object with the specified parameters.
     * <p/>
     * <p>Note that this constructor assumes these have NO escaped characters in
     * them (no %HH sequences; \HH filter sequences are OK)). Any such escaped
     * sequences will be mangled, as the initial % character will be escaped
     *
     * @param xihost      the host to connect to
     * @param xiport      the port to connect to
     * @param xidn        the DN to base the operation on
     * @param xiattrnames the list of attributes to return, or null for all user
     *                    attributes. The identifier "*" may also be included in
     *                    the array to indicate all user attributes are to be
     *                    returned (LDAPv3 only, useful if you also want some
     *                    non-user attributes)
     * @param xiscope     scope for searches, must be one of "base", "one" or "sub"
     * @param xifilter    a filter, as specified in
     *                    "draft-ietf-asid-ldapv3-filter-03.txt".
     *                    Note that \HH (not %HH) escapes may be present here, as
     *                    specified in the draft RFC. This does not support
     *                    \<character> type escapes (as specified in RFC 1960).
     */
    public LDAPUrl(String xihost,
                   int xiport,
                   String xidn,
                   String[] xiattrnames,
                   int xiscope,
                   String xifilter) {
        StringBuffer lurl;   /* we need to create the String representation of   */
                         /* this URL, based on the arguments above. They are */
                         /* encoded and added to this buffer as we construct */
                         /* the String representation                        */
        int lloop;  /* used to loop through list of attrnames           */

        lurl = new StringBuffer("ldap://");

        /*************************************************************************/
    /* We call encode on dn and filter in constructing the String            */
    /* representation of this URL as they may contain dangerous characters.  */
    /* The others must contain only safe characters.                         */
        /*************************************************************************/
        m_host = xihost;
        m_port = xiport;
        m_dn = xidn;
        m_attrnames = xiattrnames;
        m_scope = xiscope;
        if (xifilter != null) {
            m_filter = xifilter;
        } else {
            m_filter = DEFAULT_FILTER;
        }

        /*************************************************************************/
    /* Include hostport info, if present.                                    */
        /*************************************************************************/
        if (m_host != null) {
            lurl.append(m_host);
            lurl.append(":");
            lurl.append(m_port);
        }
        lurl.append("/");

        /*************************************************************************/
    /* DN must be non-null if other arguments are (see BNF for valid LDAP    */
    /* URLs). If DN isn't present, stop encoding the String representation   */
    /* of this URL.                                                          */
        /*************************************************************************/
        if (m_dn != null) {
            /***********************************************************************/
      /* DN may contain unsafe characters, so we encode it                   */
            /***********************************************************************/
            lurl.append(encode(m_dn));
            lurl.append("?");

            /***********************************************************************/
      /* Attrnames are optional; if present, add them as a comma delimited   */
      /* list.                                                               */
            /***********************************************************************/
            if (m_attrnames != null) {
                lurl.append(m_attrnames[0]);

                for (lloop = 1; lloop < m_attrnames.length; lloop++) {
                    lurl.append(",");
                    lurl.append(m_attrnames[lloop]);
                }
            }

            lurl.append("?");

            /***********************************************************************/
      /* Scope must be one of base, one or sub. Default to base.             */
            /***********************************************************************/
            switch (m_scope) {
                case LDAPv2.SCOPE_BASE:
                    lurl.append("base");
                    break;
                case LDAPv2.SCOPE_ONE:
                    lurl.append("one");
                    break;
                case LDAPv2.SCOPE_SUB:
                    lurl.append("sub");
                    break;
                default:
                    lurl.append("base");
                    break;
            }

            /***********************************************************************/
      /* Filter may contain unsafe characters, so we encode it               */
            /***********************************************************************/
            lurl.append("?");
            lurl.append(encode(m_filter));

            /***********************************************************************/
      /* This doesn't yet support extensions, so we are done here            */
      /*                                                                     */
      /* vv Extensions                                                       */
            /***********************************************************************/

            m_url = lurl.toString();
        }
    }

    /***************************************************************************/
  /* Package visibility named constants                                      */
    /**
     * ***********************************************************************
     */
    static final String DEFAULT_HOST = null;
    static final int DEFAULT_PORT = 389;
    static final String DEFAULT_DN = "";
    static final String[] DEFAULT_ATTRNAMES = null;
    static final int DEFAULT_SCOPE = LDAPv2.SCOPE_BASE;
    static final String DEFAULT_FILTER = "(objectClass=*)";

    /***************************************************************************/
  /* Private instance variables. All but m_url do not have dangerous         */
  /* characters escaped.                                                     */
    /***************************************************************************/

    /***************************************************************************/
  /* The string representation of this URL, fully escaped.                   */
    /**
     * ***********************************************************************
     */
    private String m_url;

    /***************************************************************************/
  /* Host to connect to.                                                     */
    /**
     * ***********************************************************************
     */
    private String m_host;

    /***************************************************************************/
  /* Port to connect to.                                                     */
    /**
     * ***********************************************************************
     */
    private int m_port;

    /***************************************************************************/
  /* DN of object to base search from.                                       */
    /**
     * ***********************************************************************
     */
    private String m_dn;

    /***************************************************************************/
  /* Array of attribute names to return (null or {"*"} for all user          */
  /* attributes (note that '*' is an LDAPv3 representation of all user       */
  /* attributes)).                                                           */
    /**
     * ***********************************************************************
     */
    private String[] m_attrnames;

    /***************************************************************************/
  /* Scope of search, takes value of one of SCOPE_BASE, SCOPE_ONE or         */
  /* SCOPE_SUB from LDAPv2.                                                  */
    /**
     * ***********************************************************************
     */
    private int m_scope;

    /***************************************************************************/
  /* Search filter (default (objectclass=*)).                                */
    /**
     * ***********************************************************************
     */
    private String m_filter;

    /***************************************************************************/
  /* Accessors (public visibility)                                           */
  /*                                                                         */
  /* Any of these except getUrl() may return null (or zero for the integers) */
  /* in the event that this LDAPUrl object is storing a non-LDAP URL.        */
  /*                                                                         */
  /* toString() is overridden to return the string representation of this    */
  /* URL.                                                                    */
    /***************************************************************************/

    /***************************************************************************/
  /* Note this returns a reference to the array, not a copy.                 */

    /**
     * ***********************************************************************
     */
    public String[] getAttributeArray() {
        return (m_attrnames);
    }

    public Enumeration getAttributes() {
        int lloop;
        Vector<String> lattrnames;
        Enumeration lresult;

        lattrnames = new Vector<>(m_attrnames.length);

        for (lloop = 0; lloop < m_attrnames.length; lloop++) {
            lattrnames.addElement(m_attrnames[lloop]);
        }

        lresult = lattrnames.elements();

        return (lresult);
    }

    public String getDN() {
        return (m_dn);
    }

    public String getFilter() {
        return (m_filter);
    }

    public String getHost() {
        return (m_host);
    }

    public int getPort() {
        return (m_port);
    }

    public int getScope() {
        return (m_scope);
    }

    /**
     * returns the String URL this object represents
     */
    public String getUrl() {
        return (m_url);
    }

    /**
     * returns the String URL this object represents
     */
    public String toString() {
        return (m_url);
    }

/*****************************************************************************/
/* Public methods (static)                                                   */
/*****************************************************************************/
/*****************************************************************************/
/* $Workfile:   lvmludec.mtd  $ $Revision: #2 $                           */
/* COPYRIGHT (C) 1998 DATA CONNECTION LIMITED                                */
/*****************************************************************************/
/**

 Package: uk.co.datcon.odg.dclava
 */


    /**
     * Decodes a String with %HH escaped characters into an ordinary String
     *
     * @param xiurlencoded the escaped String
     * @return String      the decoded string
     * @throws java.net.MalformedURLException thrown if there are any sequences that
     *                                        could not be decoded, like a badly formed
     *                                        %HH sequence (i.e. '%H H'). It does no
     *                                        other checking of the String (e.g. it
     *                                        doesn't check that it begins ldap:)
     *                                        <p/>
     *                                        Called by LDAPUrl(String) to decode the components of an URL string.
     *                                        <p/>
     *                                        Scans through replacing %HH sequences with the relevant character,
     *                                        throwing an exception upon encountering a bad sequence.
     */
    public static String decode(String xiurlencoded)
            throws MalformedURLException {
        StringBuffer lbuffer;     /* characters are added to this as they are      */
                            /* decoded                                       */
        int loffset;     /* offset into the undecoded string              */
        int llength;     /* length of the undecoded string                */
        int lintvalue;   /* integer value of HH escape sequence           */
        char lchar;       /* character value of %HH escape sequence        */

        lbuffer = new StringBuffer();
        loffset = 0;
        llength = xiurlencoded.length();

        /***************************************************************************/
  /* Loop through characters adding unescaped ones to buffer, and adding     */
  /* decoded value of %HH escape sequences to buffer.                        */
        /***************************************************************************/
        while (loffset < llength) {
            lchar = xiurlencoded.charAt(loffset);
            if (lchar != '%') {
                /***********************************************************************/
      /* Add normal character                                                */
                /***********************************************************************/
                lbuffer.append(lchar);
                loffset++;
            } else {
                /***********************************************************************/
      /* Attempt to decode escape sequence                                   */
                /***********************************************************************/
                try {
                    loffset++;

                    /*********************************************************************/
        /* parse the next two characters into an integer.                    */
                    /*********************************************************************/
                    lintvalue = Integer.parseInt(
                            xiurlencoded.substring(loffset, loffset + 2),
                            16);
                    lchar = (char) lintvalue;
                    lbuffer.append(lchar);
                    loffset += 2;
                } catch (StringIndexOutOfBoundsException e) {
                    /*********************************************************************/
        /* This is thrown if there are not at least 2 characters following   */
        /* the escape character '%'. If that is the case, the string is      */
        /* malformed and we throw an exception                               */
                    /*********************************************************************/
                    throw new MalformedURLException();
                } catch (NumberFormatException e) {
                    /*********************************************************************/
        /* This is thrown if the two characters following an escape          */
        /* character '%' are not valid hex characters.                       */
                    /*********************************************************************/
                    throw new MalformedURLException();
                }
            }
        }

        return (lbuffer.toString());

    }

/* $Workfile:   lvmluenc.mtd  $ $Revision: #2 $                           */
/* COPYRIGHT (C) 1998 DATA CONNECTION LIMITED                                */
/**

 Package: uk.co.datcon.odg.dclava
 */


    /**
     * Encodes unsafe characters as %HH
     *
     * @param xitoencode the string to encode
     * @return the encoded string
     *         <p/>
     *         This methods encodes a String, escaping all unsafe characters to %HH
     *         form. It would be inappropriate to call this method on an entire URL, as
     *         it would encode all the delimiter characters, like "/", "?" and such,
     *         thereby mangling the URL
     *         <p/>
     *         If this is passed null, it returns an empty string "".
     *         <p/>
     *         Scans through the string adding safe characters to a buffer and
     *         adding %HH escapes to the buffer in place of unsafe characters, then
     *         returns the contents of the buffer
     *         <p/>
     *         The characters escaped are
     *         <p/>
     *         <>"#%{}|\^~[]`/?:@=&;, and " " (space)
     *         <p/>
     *         as they are unsafe <RFC1738 sec 2.2 and draft-ietf-asid-ldapv3-url-04.txt
     *         sec 3>
     */
    public static String encode(String xitoencode) {
        StringBuffer lencoded;
        int loffset;
        int llength;
        char lchar;

        lencoded = new StringBuffer();

        /***************************************************************************/
  /* If xiencoded is null, we will return an empty string, "". Otherwise,    */
  /* encode any unsafe characters.                                           */
        /***************************************************************************/
        if (xitoencode != null) {
            loffset = 0;
            llength = xitoencode.length();

            /*************************************************************************/
    /* Loop adding characters to the buffer, encoding where necessary.       */
            /*************************************************************************/
            while (loffset < llength) {
                lchar = xitoencode.charAt(loffset);
                loffset++;

                /***********************************************************************/
      /* The following characters are unsafe, so we encode them. Others are  */
      /* added directly to the buffer intact.                                */
                /***********************************************************************/
                if ((lchar == '<') ||
                        (lchar == '>') ||
                        (lchar == '\"') ||
                        (lchar == '#') ||
                        (lchar == '%') ||
                        (lchar == '{') ||
                        (lchar == '}') ||
                        (lchar == '|') ||
                        (lchar == '\\') ||
                        (lchar == '^') ||
                        (lchar == '~') ||
                        (lchar == '[') ||
                        (lchar == ']') ||
                        (lchar == '`') ||
                        (lchar == '/') ||
                        (lchar == '?') ||
                        (lchar == ':') ||
                        (lchar == '@') ||
                        (lchar == '=') ||
                        (lchar == '&') ||
                        (lchar == ';') ||
                        (lchar == ',') ||
                        (lchar == ' ')) {
                    lencoded.append("%");

                    /*********************************************************************/
        /* Integer.toHexString encodes integers in the least possible number */
        /* of hex digits, so for characters < 0x10 we need to add a leading  */
        /* 0 to the hex escape sequence.                                     */
        /*                                                                   */
        /* vv Unicode characters > 0xFF would break this                     */
                    /*********************************************************************/
                    if (lchar < 0x10) {
                        lencoded.append("0");
                        lencoded.append(Integer.toHexString((int) lchar));
                    } else {
                        lencoded.append(Integer.toHexString((int) lchar));
                    }
                } else {
                    lencoded.append(lchar);
                }
            }
        }

        return (lencoded.toString());

    }


/*****************************************************************************/
/* Default (package) visibility methods                                      */
/*****************************************************************************/
/*****************************************************************************/
/* $Workfile:   lvmludur.mtd  $ $Revision: #2 $                          */
/* COPYRIGHT (C) 1998 DATA CONNECTION LIMITED                                */
/*****************************************************************************/
/**

 Package: uk.co.datcon.odg.dclava
 */


    /**
     * Decodes unsafe characters from %HH, parses the URL
     *
     * @param xiurl the URL to decode
     * @throws java.net.MalformedURLException thrown if the URL can't be parsed properly
     *                                        <p/>
     *                                        Called by the constructors of this class
     *                                        <p/>
     *                                        Parses and decodes the URL, storing parsed contents in the private
     *                                        instance variables of this class.
     */
    void decodeURL(String xiurl)
            throws MalformedURLException {
        StringTokenizer ltokens;        /* xiurl broken into tokens                */
        StringTokenizer lattrtokens;    /* comma delimited list of attributes      */
                                  /* broken into tokens                      */
        String ltoken;         /* one token                               */
        String ldecodedtoken;  /* one token with %HH escapes removed      */
        int lnumtokens;     /* number of tokens in xiurl               */
        int lnumread;       /* number of tokens already processed      */
        int lnumattrs;      /* number of attributes (tokens in         */
                                  /* lattrtokens)                            */
        int lloop;          /* loop index                              */


        /***************************************************************************/
  /* The form of LDAP URLs is                                                */
  /*                                                                         */
  /*   "ldap://" [hostport] ["/" [dn ["?" [attributes] ["?" [scope]          */
  /*             ["?" [filter] ["?" extensions ]]]]]]                        */
  /*                                                                         */
  /* hostport is defined in RFC 1959 to be                                   */
  /*                                                                         */
  /*   hostport = host [ ":" port ]                                          */
  /*                                                                         */
  /* Throughout the following, we use the example URL                        */
  /*                                                                         */
  /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)                             */
  /*                                                                         */
        /***************************************************************************/

        /***************************************************************************/
  /* Check that we have actually got a String of positive length             */
        /***************************************************************************/
        if ((xiurl == null) || (xiurl.length() == 0)) {
            throw new MalformedURLException();
        }

        /***************************************************************************/
  /* Break xiurl into tokens. There must be at least one, so we should have  */
  /* lnumtokens > 0 and the first call to nextToken should not be in danger  */
  /* of throwing a NoSuchElementException.                                   */
  /*                                                                         */
  /* After the following block of commands we would have                     */
  /*                                                                         */
  /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)                             */
  /*       |                                                                 */
  /*       current offset                                                    */
  /*                                                                         */
  /* ltoken     = "ldap"                                                     */
  /* lnumtokens = 15                                                         */
  /* lnumread   = 1                                                          */
        /***************************************************************************/
        m_url = xiurl;
        ltokens = new StringTokenizer(xiurl, ":/?", true);
        lnumtokens = ltokens.countTokens();
        ltoken = ltokens.nextToken();
        lnumread = 1;

        /***************************************************************************/
  /* Check that it is an LDAP URL (as opposed to ftp, http, etc)             */
  /*                                                                         */
  /* This will implicitly check that lnumtokens is at least 4.               */
        /***************************************************************************/
        if (!(xiurl.toLowerCase().startsWith("ldap://"))) {
            throw new MalformedURLException("Not an LDAP URL");
        }

        /***************************************************************************/
  /* Discard the next three tokens; we know they are ":", "/" and "/", or an */
  /* exception would have been thrown before this point.                     */
  /*                                                                         */
  /* After the following block of commands we would have                     */
  /*                                                                         */
  /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)                             */
  /*          |                                                              */
  /*          current offset                                                 */
  /*                                                                         */
  /* ltoken     = "/"                                                        */
  /* lnumtokens = 15                                                         */
  /* lnumread   = 4                                                          */
        /***************************************************************************/
        ltoken = ltokens.nextToken();
        ltoken = ltokens.nextToken();
        ltoken = ltokens.nextToken();
        lnumread += 3;

        /***************************************************************************/
  /* All the remaining elements of the URL are optional (see <draft-ietf-    */
  /* asid-ldapv3-url-04.txt>                                                 */
  /*                                                                         */
  /* Set them to their default values, then decode the remainder of the URL  */
        /***************************************************************************/
        m_host = DEFAULT_HOST;
        m_port = DEFAULT_PORT;
        m_dn = DEFAULT_DN;
        m_attrnames = DEFAULT_ATTRNAMES;
        m_scope = DEFAULT_SCOPE;
        m_filter = DEFAULT_FILTER;

        if (lnumread < lnumtokens) {
            /*************************************************************************/
    /* We have tokens remaining, so read the next one. If it isn't "/", we   */
    /* must have hostport specified (it is optional).                        */
            /*************************************************************************/
            ltoken = ltokens.nextToken();
            lnumread++;

            /*************************************************************************/
    /* If ltoken is not '/', we must have hostport present, so we extract    */
    /* it.  Otherwise use the default host & port.                           */
            /*************************************************************************/
            if (!(ltoken.equals("/"))) {
                /***********************************************************************/
      /* Read in host; this must be present                                  */
                /***********************************************************************/

                m_host = ltoken;

                /***********************************************************************/
      /* Port may be present. If it is, the next tokens will be ":" and      */
      /* "<port number>"                                                     */
                /***********************************************************************/
                if (lnumread < lnumtokens) {
                    ltoken = ltokens.nextToken();
                    lnumread++;
                    if (ltoken.equals(":")) {
                        /*********************************************************************/
          /* Port is present; read it in.  If it doesn't parse properly as a   */
          /* number, throw an exception.                                       */
                        /*********************************************************************/
                        if (lnumread < lnumtokens) {
                            try {
                                m_port = Integer.parseInt(ltokens.nextToken());
                                lnumread++;
                            } catch (NumberFormatException e) {
                                throw new MalformedURLException("Invalid port specification");
                            }

                            /*****************************************************************/
            /* Read in the next token, if present, so when we exit this      */
            /* branch of the if statement we are in the same position as if  */
            /* we had gone through the other (e.g.  about to read the DN).   */
            /* We verify that the next token (if present) is '/', throwing   */
            /* an exception if it isn't.                                     */
                            /*****************************************************************/
                            if (lnumread < lnumtokens) {
                                ltoken = ltokens.nextToken();
                                lnumread++;
                                if (ltoken.equals("/")) {
                                } else {
                                    throw new MalformedURLException();
                                }
                            }
                        } else {
                            throw new MalformedURLException("Port not specified after \":\"");
                        }
                    } else if (!(ltoken.equals("/"))) {
                        /*********************************************************************/
          /* The token after hostname wasn't ":" and it wasn't "/", so this is */
          /* a badly formed URL.                                               */
                        /*********************************************************************/
                        throw new MalformedURLException("\":\" or \"/\" expected after host)");
                    } else {
                        /*********************************************************************/
          /* We have the host name, but port wasn't specified so use the       */
          /* default.                                                          */
                        /*********************************************************************/
                    }

                } else {
                    /*********************************************************************/
        /* No more tokens; use default port.                                 */
                    /*********************************************************************/
                }
            } else {
                /***********************************************************************/
      /* Use default host and port                                           */
                /***********************************************************************/
            }

            if (lnumread < lnumtokens) {
                /***********************************************************************/
      /* If there are more tokens, then DN must be present (DN is optional   */
      /* only if there are no more tokens; if there are more tokens, the     */
      /* first must be DN)                                                   */
      /*                                                                     */
      /* At this point in our example, we have                               */
      /*                                                                     */
      /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)                         */
      /*                     |                                               */
      /*                     current offset                                  */
      /*                                                                     */
      /* ltoken = "/"                                                        */
      /* lnumtokens = 15                                                     */
      /* lnumread = 8                                                        */
      /*                                                                     */
      /* If host and port had not been present, we would have at this point  */
      /*                                                                     */
      /*   ldap:///c=GB?sn,cn?sub(cn=ISF)                                    */
      /*           |                                                         */
      /*           current offset                                            */
      /*                                                                     */
      /* We can be sure that the next token will be the entire DN in URL     */
      /* encoded form, as any characters that might have confused the        */
      /* StringTokenizer should already be encoded (if this is a valid URL). */
      /*                                                                     */
      /* If this is not a valid URL and dangerous characters are present in  */
      /* the DN, we hope to, but don't guarantee, that we catch them at a    */
      /* later point. Rigourous checking of the URL would require checking   */
      /* that all decoded attribute descriptions and filters conformed       */
      /* exactly to their respective syntaxes wrt allowed characters (e.g.   */
      /* no '=' in attribute descriptions). We consider checking that to be  */
      /* an unnecessary degree of rigour.                                    */
                /***********************************************************************/
                ltoken = ltokens.nextToken();
                lnumread++;
                m_dn = decode(ltoken);

                if (lnumread < lnumtokens) {
                    /*********************************************************************/
        /* BNF for LDAP URLS indicates if more tokens are present, the first */
        /* must be "?". Ensure that it is.                                   */
                    /*********************************************************************/
                    ltoken = ltokens.nextToken();
                    lnumread++;
                    if (ltoken.equals("?")) {
                    } else {
                        throw new MalformedURLException();
                    }

                    if (lnumread < lnumtokens) {
                        /*******************************************************************/
          /* There are more tokens. If the next is not "?", we have a list   */
          /* of attributes. Otherwise, we have an empty list (signifying all */
          /* user attributes).                                               */
                        /*******************************************************************/
                        ltoken = ltokens.nextToken();
                        lnumread++;

                        if (!(ltoken.equals("?"))) {
                            /*****************************************************************/
            /* We have a list of attribute descriptions.  The entire list is */
            /* extracted as the next token, and we extract the attributes    */
            /* from this list with another StringTokenizer.                  */
            /*                                                               */
            /* At this point in our example, we have                         */
            /*                                                               */
            /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)                   */
            /*                               |                               */
            /*                               current offset                  */
            /* ltoken = "sn,cn"                                              */
            /* lnumtokens = 15                                               */
            /* lnumread = 11                                                 */
            /*                                                               */
                            /*****************************************************************/

                            /*****************************************************************/
            /* Attribute list non-empty, parse it into a String array        */
                            /*****************************************************************/
                            lattrtokens = new StringTokenizer(ltoken, ",");
                            lnumattrs = lattrtokens.countTokens();
                            m_attrnames = new String[lnumattrs];

                            /*****************************************************************/
            /* Loop through attributes adding them to array                  */
            /*                                                               */
            /* In our example, we end up with the array { "sn", "cn" }       */
                            /*****************************************************************/
                            lloop = 0;
                            while (lloop < lnumattrs) {
                                m_attrnames[lloop] = decode(lattrtokens.nextToken());

                                lloop++;
                            }

                            /*****************************************************************/
            /* If more tokens are present, the next must be "?".  Throw an   */
            /* exception otherwise.                                          */
                            /*****************************************************************/
                            if (lnumread < lnumtokens) {
                                ltoken = ltokens.nextToken();
                                lnumread++;
                                if (ltoken.equals("?")) {
                                } else {
                                    throw new MalformedURLException();
                                }
                            }
                        } else {
                            /*****************************************************************/
            /* Attribute list empty.                                         */
                            /*****************************************************************/
                            m_attrnames = null;
                        }

                        /*******************************************************************/
          /* Extract scope, if present                                       */
          /*                                                                 */
          /* At this point in our example, we have                           */
          /*                                                                 */
          /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)                     */
          /*                                |                                */
          /*                                current offset                   */
          /*                                                                 */
          /* ltoken = "?"                                                    */
          /* lnumtokens = 15                                                 */
          /* lnumread = 12                                                   */
          /*                                                                 */
                        /*******************************************************************/
                        if (lnumread < lnumtokens) {
                            /*****************************************************************/
            /* If the next token is not "?", we have a scope specification   */
                            /*****************************************************************/
                            ltoken = ltokens.nextToken();
                            lnumread++;

                            if (!(ltoken.equals("?"))) {
                                /***************************************************************/
              /* Scope present                                               */
              /*                                                             */
              /* At this point in our example, we have                       */
              /*                                                             */
              /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)                 */
              /*                                   |                         */
              /*                                   current offset            */
              /*                                                             */
              /* ltoken = "sub"                                              */
              /* lnumtokens = 15                                             */
              /* lnumread = 13                                               */
              /*                                                             */
                                /***************************************************************/
                                ldecodedtoken = decode(ltoken);

                                if (ldecodedtoken.equals("base")) {
                                    m_scope = LDAPv2.SCOPE_BASE;
                                } else if (ldecodedtoken.equals("one")) {
                                    m_scope = LDAPv2.SCOPE_ONE;
                                } else if (ldecodedtoken.equals("sub")) {
                                    m_scope = LDAPv2.SCOPE_SUB;
                                } else {
                                    throw new MalformedURLException();
                                }

                                /***************************************************************/
              /* If more tokens are present, the next must be "?".  Throw an */
              /* exception otherwise.                                        */
                                /***************************************************************/
                                if (lnumread < lnumtokens) {
                                    ltoken = ltokens.nextToken();
                                    lnumread++;
                                    if (ltoken.equals("?")) {
                                    } else {
                                        throw new MalformedURLException();
                                    }
                                }
                            } else {
                                /***************************************************************/
              /* Scope not present, using scope base                         */
                                /***************************************************************/
                                m_scope = LDAPv2.SCOPE_BASE;
                            }

                            /*****************************************************************/
            /* Extract filter, if present                                    */
                            /*****************************************************************/
                            if (lnumread < lnumtokens) {
                                ltoken = ltokens.nextToken();
                                lnumread++;

                                if (!(ltoken.equals("?"))) {
                                    /*************************************************************/
                /* Filter present                                            */
                /*                                                           */
                /* At this point in our example, we have                     */
                /*                                                           */
                /*   ldap://myhost:389/c=GB?sn,cn?sub?(cn=ISF)               */
                /*                                            |              */
                /*                                            current offset */
                /*                                                           */
                /* ltoken = "(cn=ISF)                                        */
                /* lnumtokens = 15                                           */
                /* lnumread = 15                                             */
                /*                                                           */
                                    /*************************************************************/
                                    ldecodedtoken = decode(ltoken);
                                    m_filter = ldecodedtoken;

                                    /*************************************************************/
                /* If more tokens are present, the next must be "?".  Throw  */
                /* an exception otherwise.                                   */
                                    /*************************************************************/
                                    if (lnumread < lnumtokens) {
                                        ltoken = ltokens.nextToken();
                                        lnumread++;
                                        if (ltoken.equals("?")) {
                                        } else {
                                            throw new MalformedURLException();
                                        }
                                    }
                                } else {
                                    /*************************************************************/
                /* Filter not present, using default                         */
                                    /*************************************************************/
                                    m_filter = DEFAULT_FILTER;
                                }

                                /***************************************************************/
              /* If there are any more tokens left, they are part of an      */
              /* extension specification.  We don't know how to deal with    */
              /* any extentions.  If they're not critical we discard them,   */
              /* and if they are critical we throw an exception saying we    */
              /* don't support any extentions.                               */
                                /***************************************************************/
                                if (lnumread < lnumtokens) {
                                    ltoken = ltokens.nextToken();
                                    lnumread++;

                                    /*************************************************************/
                /* If critical extension present, throw exception; we can't  */
                /* deal with it.                                             */
                                    /*************************************************************/
                                    if (ltoken.indexOf("!") != -1) {
                                        throw new MalformedURLException(
                                                "Critical Extension not supported");
                                    }

                                    /*************************************************************/
                /* The previous token should have been the last; if the URL  */
                /* is valid any of the delimiter characters present in       */
                /* extension values would have been %HH escaped. Therefore,  */
                /* if there are any more tokens we don't have a well formed  */
                /* URL.                                                      */
                                    /*************************************************************/
                                    if (ltokens.hasMoreElements()) {
                                        throw new MalformedURLException();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}

