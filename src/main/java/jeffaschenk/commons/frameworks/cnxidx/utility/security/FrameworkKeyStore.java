package jeffaschenk.commons.frameworks.cnxidx.utility.security;

import jeffaschenk.commons.exceptions.FileException;
import jeffaschenk.commons.frameworks.cnxidx.utility.file.FileUtilities;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;
import jeffaschenk.commons.frameworks.cnxidx.utility.message.MessageConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * This class manages the Framework keystore which contains a key for
 * each known alias.
 *
 * @author Ken Rider
 * @version 1.0
 * @since 06/05/2002
 */
public class FrameworkKeyStore {

    private static String keystorePassword = "Framework";
    private static String keystoreFilename = ".jeffaschenk.commons.framework.keystore";
    private static String defaultKeyStoreFilename = System.getProperty("user.home") + File.separator + keystoreFilename;
    private static boolean useSunJCE = true;

    private static String CLASS_NAME = FrameworkKeyStore.class.getName();

    /**
     * Creates new DirectoryUser
     */
    public FrameworkKeyStore() {
        String method = "constructor";

        // Determine whether we are using a Sun JVM or not.
        if (System.getProperty("java.vm.vendor").toLowerCase().indexOf("sun") < 0) {
            useSunJCE = false;
        } else {
            // Use the default crypto provider from Sun.
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
        }
        try {
            File file = FileUtilities.locateFileAsFile(keystoreFilename);
        } catch (FileException e) {
            String[] args = new String[]{keystoreFilename};
            FrameworkLogger.log(CLASS_NAME, method, FrameworkLoggerLevel.SEVERE,
                    MessageConstants.COMMON_AUTH_KEYSTORE_NOT_FOUND, args, e);
            createKeyStore(defaultKeyStoreFilename);
        }
    }

    // Create an empty keystore.
    private void createKeyStore(String keyStoreFilename) {
        FileOutputStream fos = null;
        try {
            // Create an instance of a keystore.
            KeyStore ks;
            if (useSunJCE) {
                ks = KeyStore.getInstance("JCEKS", "SunJCE");
            } else {
                ks = KeyStore.getInstance("JCEKS");
            }

            // Convert the specified keystore password to a character array.
            char p[] = new char[keystorePassword.length()];
            keystorePassword.getChars(0, p.length, p, 0);

            // Load an empty keystore with the specified keystore password.
            ks.load(null, p);

            // Write the keystore with the specified keystore password to the specified file.
            fos = new FileOutputStream(keyStoreFilename);
            ks.store(fos, p);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    /**
     * Get the secret key from the default keystore for the specified alias and alias password.
     *
     * @param alias
     * @param password for the alias.
     */
    public SecretKey getKey(String alias, String password) {
        SecretKey key = null;
        InputStream fis = null;
        try {
            // Create an instance of a keystore.
            KeyStore ks;
            if (useSunJCE) {
                ks = KeyStore.getInstance("JCEKS", "SunJCE");
            } else {
                ks = KeyStore.getInstance("JCEKS");
            }

            // Convert the specified keystore password to a character array.
            char p[] = new char[keystorePassword.length()];
            keystorePassword.getChars(0, p.length, p, 0);

            // Load the keystore instance from the default keystore file.
            fis = FileUtilities.locateFile(keystoreFilename);
            ks.load(fis, p);

            // Convert the specified alias password to a character array.
            char c[] = new char[password.length()];
            password.getChars(0, c.length, c, 0);

            // Get the key for the specified alias and alias password.
            key = (SecretKey) ks.getKey(alias, c);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ignore) {
                }
            }
        }
        return (key);
    }

    /**
     * Store the secret key in the default keystore for the specified alias and alias password.
     *
     * @param key SecretKey Key.
     * @param alias String    Alias.
     * @param password String    Password for the alias.
     */
    public void storeKey(SecretKey key, String alias, String password) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // Create an instance of a keystore.
            KeyStore ks;
            if (useSunJCE) {
                ks = KeyStore.getInstance("JCEKS", "SunJCE");
            } else {
                ks = KeyStore.getInstance("JCEKS");
            }

            // Convert the specified keystore password to a character array.
            char p[] = new char[keystorePassword.length()];
            keystorePassword.getChars(0, p.length, p, 0);

            // Load the keystore instance from the default keystore file.
            File file = FileUtilities.locateFileAsFile(keystoreFilename);
            fis = new FileInputStream(file);
            ks.load(fis, p);

            // Use an empty certificate chain for now.
            java.security.cert.Certificate chain[] = null;

            // Convert the specified alias password to a character array.
            char c[] = new char[password.length()];
            password.getChars(0, c.length, c, 0);

            // Store the key for the specified alias and alias password.
            ks.setKeyEntry(alias, key, c, chain);

            // Write the keystore back to the default keystore file.
            fos = new FileOutputStream(file);
            ks.store(fos, p);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ignore) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    /* Generate a secret key using triple DES.
     */
    public SecretKey generateKey() {
        SecretKey key = null;
        try {
            KeyGenerator kg = KeyGenerator.getInstance("DESede");
            kg.init(new SecureRandom());
            key = kg.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (key);
    }

    /* Encrypt the given string using the given key.
     * @return byte[] Encrypted string.
     * @param String Input to encrypt.
     * @param Key Key to use to encrypt the input.
     */
    public static byte[] encrypt(String input, Key key) {
        byte[] outputBytes = null;
        try {
            byte[] iv = new byte[8];
            Cipher c = Cipher.getInstance("DESede/CFB8/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            outputBytes = c.doFinal(input.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (outputBytes);
    }

    /* Decrypt the given byte array using the given key.
     * @return String Decrypted string.
     * @param byte[] Input to decrypt.
     * @param Key Key to use to decrypt the input.
     */
    public static String decrypt(byte[] inputBytes, Key key) {
        String output = "";
        try {
            byte[] iv = new byte[8];
            Cipher c = Cipher.getInstance("DESede/CFB8/NoPadding");
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            output = new String(c.doFinal(inputBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (output);
    }

}
