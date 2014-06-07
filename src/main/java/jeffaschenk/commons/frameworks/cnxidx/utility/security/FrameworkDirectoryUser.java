package jeffaschenk.commons.frameworks.cnxidx.utility.security;

import jeffaschenk.commons.exceptions.FileException;
import jeffaschenk.commons.frameworks.cnxidx.utility.file.FileUtilities;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.message.MessageConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.SecretKey;

/**
 * This class manages the Framework directory user file which contains an
 * encrypted DN and password for known aliases.
 *
 * @author Ken Rider
 * @version 1.0
 * @since 06/05/2002
 */
public class FrameworkDirectoryUser {

    private final static String CLASS_NAME = FrameworkDirectoryUser.class.getName();

    private final static String USAGE =
            " FrameworkDirectoryUser --set <alias> <dn> <password>" +
                    "\n FrameworkDirectoryUser --get <alias>" +
                    "\n FrameworkDirectoryUser --encrypt <alias> <password>" +
                    "\n FrameworkDirectoryUser --decrypt <alias> <encrypted password>" +
                    "\n Example: " +
                    "\n   java FrameworkDirectoryUser --set process \"cn=Framework Process Master,ou=Framework,dc=test,dc=com\" MyPassword" +
                    "\n   java FrameworkDirectoryUser --get process" +
                    "\n   java FrameworkDirectoryUser --encrypt process MyPassword" +
                    "\n   java FrameworkDirectoryUser --decrypt process MyEncryptedPassword";

    private Map<String,String> knownAlias = new HashMap<>();
    private String directoryUserFileName = ".framework.directoryuser";
    private String defaultDirectoryUserFilename = System.getProperty("user.home") + File.separator + directoryUserFileName;
    private FrameworkKeyStore iks = new FrameworkKeyStore();

    private String alias = "";
    private String dn = "";
    private String password = "";
    private String aliasPassword = "";
    private SecretKey aliasKey;

    /**
     * Creates a new FrameworkDirectoryUser.
     *
     * @param alias for this Framework directory user.
     */
    public FrameworkDirectoryUser(String alias) {
        init();
        // If this alias is not in the list of known aliases throw an exception.
        if (knownAlias.containsKey(alias)) {
            this.alias = alias;
            this.aliasPassword = (String) knownAlias.get(alias);
            this.aliasKey = iks.getKey(this.alias, this.aliasPassword);
            // If there isn't a key for this alias, generate one.
            if (this.aliasKey == null) {
                // Generate a key for this alias.
                this.aliasKey = iks.generateKey();
                // Store the key for this alias.
                iks.storeKey(this.aliasKey, this.alias, this.aliasPassword);
            }
        }
    }

    private void init() {
        String method = "init";

        knownAlias.put("admin", "Glendive");
        knownAlias.put("process", "Hardin");
        knownAlias.put("user", "Ronan");
        knownAlias.put("superuser", "Sheridan");
        try {
            File file = FileUtilities.locateFileAsFile(directoryUserFileName);
        } catch (FileException e) {
            String[] args = new String[]{directoryUserFileName};
            FrameworkLogger.log(CLASS_NAME, method, FrameworkLoggerLevel.SEVERE,
                    MessageConstants.COMMON_AUTH_KEYSTORE_NOT_FOUND, args, e);
            createDirectoryUserFile(defaultDirectoryUserFilename);
        }
    }

    private void createDirectoryUserFile(String fileName) {
        // Create the directory user file.
        try {
            File directoryUserFile = new File(fileName);
            directoryUserFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // For each known alias...
        for (Iterator i = knownAlias.keySet().iterator(); i.hasNext(); ) {
            String alias = (String) i.next();
            // If there isn't a key for this alias...
            SecretKey key = iks.getKey(alias, (String) knownAlias.get(alias));
            if (key == null) {
                // Generate a key for this alias.
                key = iks.generateKey();
                // Store the key for this alias.
                iks.storeKey(key, alias, (String) knownAlias.get(alias));
            }
        }
    }

    private Hashtable<String,String> loadFile() {
        String dul = null;
        BufferedReader in = null;
        Hashtable<String,String> results = new Hashtable<>();
        try {
            in = new BufferedReader(new FileReader(
                    FileUtilities.locateFileAsFile(directoryUserFileName)));
            boolean eof = false;
            do {
                String line = in.readLine();
                if (line == null) {
                    eof = true;
                } else {
                    StringTokenizer tokenString = new StringTokenizer(line, " ", false);
                    String key = tokenString.nextToken();
                    String remainder = line.substring(key.length() + 1);
                    results.put(key, remainder);
                }
            } while (!eof);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignore) {
                }
            }
        }

        return results;
    }

    private void saveFile(Hashtable data) throws FileNotFoundException, FileException {
        PrintWriter duo = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FileUtilities.locateFileAsFile(directoryUserFileName), false);
            duo = new PrintWriter(out);

            Enumeration keys = data.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String values = (String) data.get(key);
                duo.println(key + " " + values);
            }
        } finally {
            if (duo != null) {
                try {
                    duo.flush();
                } catch (Exception ignore) {
                }
                try {
                    duo.close();
                } catch (Exception ignore) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    private static String byteArrayToHexString(byte[] convert) {
        String converted = "";
        for (int i = 0; i < convert.length; i++) {
            converted += ("00000000".substring(Integer.toHexString(convert[i]).length()) +
                    Integer.toHexString(convert[i])).substring(6);
        }
        return converted;
    }

    private static byte[] hexStringToByteArray(String convert) {
        byte[] converted = new byte[convert.length() / 2];
        for (int i = 0; i < converted.length; i++) {
            converted[i] = (byte) Integer.parseInt(convert.substring(2 * i, 2 * i + 2), 16);
        }
        return converted;
    }

    /**
     * @return String Alias of this Framework directory user.
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * @return SecretKey secret key of this Framework directory user.
     */
    public SecretKey getSecretKey() {
        return this.aliasKey;
    }

    /**
     * @return String DN of this Framework directory user.
     */
    public String getDN() {
        if (this.dn.equals("")) {
            Hashtable entries = loadFile();
            String info = (String) entries.get(alias);
            if (info != null) {
                StringTokenizer tokenString = new StringTokenizer(info, " ", false);
                this.dn = FrameworkKeyStore.decrypt(hexStringToByteArray(tokenString.nextToken()), this.aliasKey);
            }
        }
        return this.dn;
    }

    /**
     * @return String Password of this Framework directory user.
     */
    public String getPassword() {
        if (this.password.equals("")) {
            Hashtable entries = loadFile();
            String info = (String) entries.get(alias);
            if (info != null) {
                StringTokenizer tokenString = new StringTokenizer(info, " ", false);
                String ignore = tokenString.nextToken();
                this.password = FrameworkKeyStore.decrypt(hexStringToByteArray(tokenString.nextToken()), this.aliasKey);
            }
        }
        return this.password;
    }

    /**
     * @param dn DN to set for this Framework directory user.
     * @param password Password to set for this Framework directory user.
     */
    public void setDNAndPassword(String dn, String password) {
        // Encrypt the DN and password.
        this.dn = dn;
        byte[] encryptedDN = FrameworkKeyStore.encrypt(dn, this.aliasKey);
        this.password = password;
        byte[] encryptedPassword = FrameworkKeyStore.encrypt(password, this.aliasKey);

        // Write the alias, encrypted DN and encrypted password to the directory user file.
        try {
            Hashtable<String,String> data = loadFile();
            data.put(alias, byteArrayToHexString(encryptedDN) + " "
                    + byteArrayToHexString(encryptedPassword));
            saveFile(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt a string.
     *
     * @param string to encrypt.
     * @return String encrypted hex string.
     */
    public String encrypt(String string) {
        return byteArrayToHexString(FrameworkKeyStore.encrypt(string, this.aliasKey));
    }

    /**
     * Decrypt a hex string.
     *
     * @param string in hex to decrypt.
     * @return String decrypted string.
     */
    public String decrypt(String string) {
        return FrameworkKeyStore.decrypt(hexStringToByteArray(string), this.aliasKey);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Check usage
        if ((args.length == 4) && args[0].equalsIgnoreCase("--set")) {
            FrameworkDirectoryUser idu = new FrameworkDirectoryUser(args[1]);
            idu.setDNAndPassword(args[2], args[3]);
        } else if ((args.length == 2) && args[0].equalsIgnoreCase("--get")) {
            FrameworkDirectoryUser idu = new FrameworkDirectoryUser(args[1]);
            System.out.println("DN       = " + idu.getDN());
            System.out.println("Password = " + idu.getPassword());
        } else if ((args.length == 3) && args[0].equalsIgnoreCase("--encrypt")) {
            FrameworkDirectoryUser idu = new FrameworkDirectoryUser(args[1]);
            System.out.println("Clear Text     = " + args[2]);
            System.out.println("Encrypted Text = " + idu.encrypt(args[2]));
        } else if ((args.length == 3) && args[0].equalsIgnoreCase("--decrypt")) {
            FrameworkDirectoryUser idu = new FrameworkDirectoryUser(args[1]);
            System.out.println("Encrypted Text = " + args[2]);
            System.out.println("Clear Text     = " + idu.decrypt(args[2]));
        } else {
            System.out.println(USAGE);
            System.exit(1);
        }

    }

}
