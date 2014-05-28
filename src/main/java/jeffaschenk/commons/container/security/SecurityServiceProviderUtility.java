package jeffaschenk.commons.container.security;

import jeffaschenk.commons.container.security.constants.SecurityConstants;
import jeffaschenk.commons.util.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.UrlBase64;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.FixedByteArraySaltGenerator;
import org.jasypt.salt.SaltGenerator;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Provides a common Static class for Security providers.
 * <p/>
 * Using Bouncycastle, @see @link http://www.bouncycastle.org
 * as our Security Provider and using Jasypt as our
 * common interface to simplify security methodologies, but keep
 * security intact.
 * <p/>
 * CAUTION CAUTION CAUTION CAUTION CAUTION
 * <p/>
 * Please take care in modifying this method
 * as it could effect persisted encrypted
 * DATA.
 * <p/>
 * CAUTION CAUTION CAUTION CAUTION CAUTION
 *
 * @author jeffaschenk@gmail.com
 *         Date: Mar 31, 2010
 *         Time: 1:26:51 PM
 */
public class SecurityServiceProviderUtility {

    /**
     * Only Allow for Static Use.
     */
    private SecurityServiceProviderUtility() {
    }

    /**
     * Logging Constant <code>log</code>
     */
    protected static Log log = LogFactory
            .getLog(SecurityServiceProviderUtility.class);

    /**
     * Public Globals
     */
    public static final int MAX_UNENCRYPTED_CREDENTIALS_LENGTH = 16;

    /**
     * Bouncy Castle Algorithm Names
     */
    private static final String BC_ALGORITHM_NAME = "WHIRLPOOL";
    private static final String BC_ALGORITHM_NAME_PBE_MD5_TRIPLE_DES = "PBEWithMD5AndTripleDES";

    /**
     * Bouncy Castle Constants
     */
    private static final int KEY_OBTENTION_ITERATIONS = 1000;
    private static final String STRING_OUTPUT_TYPE_HEXADECIMAL = "hexadecimal";

    /**
     * Byte Array Static Key Salt,
     * used for Bidirectional Digest.
     */
    private static final byte[] SALT = {0x0f, 0x02, 0x03, 0x14, 0x25, 0x36, 0x47, 0x4f, 0x0f, 0x0c, 0x01};

    /**
     * String Static Key Salt Password,
     * used for Message Encryption and Decryption.
     */
    private static final String SALT_PW = 0x2f + "F!!91qlfsn" + 0x22;

    /**
     * Private helper method to create a Salt Generator Instance.
     *
     * @return SaltGenerator used in Bi-Directional Digest.
     */
    private static SaltGenerator getSaltGenerator() {
        FixedByteArraySaltGenerator saltGenerator = new FixedByteArraySaltGenerator();
        saltGenerator.setSalt(SALT);
        return saltGenerator;
    }

    /**
     * Unidirectional Digest
     *
     * @param message incoming message String
     * @return String Unidirectional Digest
     */
    public static String unidirectional_digest(final String message) {
        initializeDefaultCryptographyProvider();
        StandardStringDigester digester = new StandardStringDigester();
        digester.setAlgorithm(BC_ALGORITHM_NAME);
        return digester.digest(message);
    }

    /**
     * Bidirectional Digest
     *
     * @param message incoming message String
     * @return String Bidirectional Digest
     */
    public static String bidirectional_digest(final String message) {
        initializeDefaultCryptographyProvider();
        StandardStringDigester digester = new StandardStringDigester();
        digester.setAlgorithm(BC_ALGORITHM_NAME);
        digester.setSaltGenerator(getSaltGenerator());
        return digester.digest(message);
    }

    /**
     * Bidirectional Digest Match
     *
     * @param message incoming message String
     * @param digest  Bidirectional Digest
     * @return boolean indicator, True if String and Digest Match, False if not.
     */
    public static boolean checkDigest(final String message, final String digest) {
        initializeDefaultCryptographyProvider();
        StandardStringDigester digester = new StandardStringDigester();
        digester.setAlgorithm(BC_ALGORITHM_NAME);
        digester.setSaltGenerator(getSaltGenerator());
        return digester.matches(message, digest);
    }

    /**
     * Encrypt Message into a Base 64 String Data
     *
     * @param message to be encrypted
     * @return String BASE64 encoded Encrypted String of Message.
     */
    public static String encryptMessage(String message) {
        initializeDefaultCryptographyProvider();
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(SALT_PW);
        return basicTextEncryptor.encrypt(message);
    }

    /**
     * DeCrypt a BASE64 Encoded Encrypted Message to Plain text.
     *
     * @param encryptedMessage
     * @return String - Decrypted Message String
     */
    public static String decryptMessage(String encryptedMessage) {
        initializeDefaultCryptographyProvider();
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(SALT_PW);
        return basicTextEncryptor.decrypt(encryptedMessage);
    }

    /**
     * Checks the Message and Encrypted Message are the Same.
     *
     * @param message
     * @param encryptedMessage
     * @return boolean indicator, True if Message equals that of the encryptedMessage after decryption applied.
     */
    public static boolean checkMessage(String message, String encryptedMessage) {
        if ((message == null) || (message.isEmpty())) {
            return false;
        }
        if ((encryptedMessage == null) || (encryptedMessage.isEmpty())) {
            return false;
        }
        return decryptMessage(encryptedMessage).equals(message);
    }

    /**
     * Encrypt a Password
     *
     * @param password plain text password
     * @return String encrypted String which is in Hexidecimal.
     */
    public static String encryptPassword(final String password) {
        initializeDefaultCryptographyProvider();
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return encryptToHexadecimalString(strongPasswordEncryptor.encryptPassword(password));
    }

    /**
     * Checks a plain Text Password against a Encrypted String
     *
     * @param password
     * @param encryptedPassword
     * @return boolean indicating if credentials are equal or not.
     */
    public static boolean checkPassword(final String password, final String encryptedPassword) {
        if ((password == null) || (password.isEmpty())) {
            return false;
        }
        if ((encryptedPassword == null) || (encryptedPassword.isEmpty())) {
            return false;
        }
        initializeDefaultCryptographyProvider();
        String cs_encryptedPassword = SecurityServiceProviderUtility.decryptFromHexadecimalString(encryptedPassword);
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return strongPasswordEncryptor.checkPassword(password, cs_encryptedPassword);
    }

    /**
     * Encrypt a Message Text to a Hexadecimal String usable for a WEB Url.
     *
     * @param message to be encrypted
     * @return String of a Encrypted Hexadecimal String
     */
    public static String encryptToHexadecimalString(String message) {
        initializeDefaultCryptographyProvider();
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(SALT_PW);
        encryptor.setAlgorithm(BC_ALGORITHM_NAME_PBE_MD5_TRIPLE_DES);
        encryptor.setKeyObtentionIterations(KEY_OBTENTION_ITERATIONS);
        encryptor.setStringOutputType(STRING_OUTPUT_TYPE_HEXADECIMAL);
        return encryptor.encrypt(message);
    }

    /**
     * Decrypt a Message Test from a Hexadecimal String.
     *
     * @param encryptedHexadecimalMessage hexadecimal message string to be decrypted
     * @return String of decrypted String.
     */
    public static String decryptFromHexadecimalString(String encryptedHexadecimalMessage) {
        initializeDefaultCryptographyProvider();
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(SALT_PW);
        encryptor.setAlgorithm(BC_ALGORITHM_NAME_PBE_MD5_TRIPLE_DES);
        encryptor.setKeyObtentionIterations(KEY_OBTENTION_ITERATIONS);
        encryptor.setStringOutputType(STRING_OUTPUT_TYPE_HEXADECIMAL);
        return encryptor.decrypt(encryptedHexadecimalMessage);
    }

    /**
     * Checks the Message and Encrypted Message are the Same.
     *
     * @param message
     * @param encryptedHexadecimalMessage
     * @return boolean indicator, True if Message equals that of the encryptedMessage after decryption applied.
     */
    public static boolean checkHexadecimalStringMessage(String message, String encryptedHexadecimalMessage) {
        if ((message == null) || (message.isEmpty())) {
            return false;
        }
        if ((encryptedHexadecimalMessage == null) || (encryptedHexadecimalMessage.isEmpty())) {
            return false;
        }
        return decryptFromHexadecimalString(encryptedHexadecimalMessage).equals(message);
    }


    /**
     * Check and Verify a Service Providers Signature
     * <p/>
     * Note:  This could change based upon Service Provider.
     *
     * @param expectedSignature
     * @param data
     * @param secret
     * @return boolean indicating if signature is as expected or not.
     */
    public static boolean checkServiceProviderSignature(byte[] expectedSignature, String data, String secret) {
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), SecurityConstants.HMAC_SHA256_TRUE_ALGORITHM_NAME);
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(SecurityConstants.HMAC_SHA256_TRUE_ALGORITHM_NAME);
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            if (log.isDebugEnabled()) {
                log.debug("Comparing Computed Signature:[" + StringUtils.getHexString(rawHmac) + "]");
                log.debug("     With Expected Signature:[" + StringUtils.getHexString(expectedSignature) + "]");
            }
            return (Arrays.areEqual(rawHmac, expectedSignature));
        } catch (Exception e) {
            log.error("Exception Occurred During checking of Provider Signature: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Helper private method to strip last 3 bytes off of Signature Byte Array.
     *
     * @param inArray
     * @param trunc   - last number of bytes to be truncated
     * @return byte[] Byte Array Truncated by specificed number of bytes.
     */
    private static byte[] stripLastBytesFromArray(byte[] inArray, int trunc) {
        byte[] newByteArray = new byte[inArray.length - trunc];
        for (int i = 0; i < inArray.length - trunc; i++) {
            newByteArray[i] = inArray[i];
        }
        return newByteArray;
    }

    /**
     * Decodes Base64 Incoming URL Data from a Service Provider, such as
     * Facebook.
     *
     * @param data
     * @return byte[] Decoded Byte Array.
     */
    public static byte[] decodeBase64Data(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        // *******************************
        // Decode the Payload
        Base64 base64 = new Base64(true);
        return base64.decode(data.getBytes());
    }

    /**
     * Encode Base 64 Data for presenting to a Service Provider, such as
     * Facebook.
     *
     * @param data
     * @return byte[] encoded Byte Array
     */
    public static byte[] encodeBase64Data(byte[] data) {
        if (data == null) {
            return null;
        }
        // *******************************
        // Encode the Payload
        UrlBase64 base64 = new UrlBase64();
        return base64.encode(data);
    }

    /**
     * Initialize the Default Cryptography Implementation.
     */
    public static void initializeDefaultCryptographyProvider() {
        if (Security.getProvider("BC") == null) {
            BouncyCastleProvider bcProv = new BouncyCastleProvider();
            if (Security.getProviders().length <= 0) {
                Security.insertProviderAt(bcProv, 1);
            } else {
                Security.addProvider(bcProv);
            }
        }
    }

    /**
     * Decode the Signed Request Payload or return null if exception occurs.
     *
     * @param incoming_signed_request
     * @return String of decoded Payload.
     */
    public static String decodeFacebookSignedRequestPayLoad(final String incoming_signed_request, final String serviceProviderSecret) {
        try {
            if ((StringUtils.isEmpty(incoming_signed_request)) ||
                    (StringUtils.isEmpty(serviceProviderSecret))) {
                log.error("Unable to Decode Signed Request, since Signed Request and/or Service Provider Secret has not been specified.");
                return null;
            }
            // *******************************
            // Perform Additional Processing
            // for signed Request Verification
            String[] signed_request = incoming_signed_request.split("\\.", 2);
            // ******************************
            // Decode Signature
            byte[] decodedSignature = decodeBase64Data(signed_request[0]);
            if (log.isDebugEnabled()) {
                log.debug("Decoded Signature:[" + StringUtils.getHexString(decodedSignature) + "]");
            }
            // **************************
            // Verify Signature.
            if (checkServiceProviderSignature(decodedSignature, signed_request[1], serviceProviderSecret)) {
                log.info("Service Provider Signature and Content Verified.");
            } else {
                log.warn("Service Provider Signature and/or Content did not verify correctly, check configured Secret Phrase!");
                return null;
            }
            // *******************************
            // Decode the Payload
            String decodedPayload = new String(decodeBase64Data(signed_request[1]));
            if (log.isDebugEnabled()) {
                log.debug("Initial Decoded Payload:[" + decodedPayload + "]");
                log.debug("Last String Bytes of PayLoad:[" +
                        decodedPayload.substring(decodedPayload.length() - 3) + "]");
                log.debug("   Last Hex Bytes of PayLoad:[" +
                        StringUtils.getHexString(decodedPayload.substring(decodedPayload.length() - 3).getBytes()) + "]");
            }
            // *******************************************
            /// Return decoded Payload.
            return decodedPayload;
        } catch (Exception exception) {
            log.error("Unable to perform Decode of Signed Request Payload:[" + exception.getMessage() + "]", exception);
            return null;
        }
    }

    /**
     * Provide Command Line Interface (CLI) for these methods.
     * <p/>
     * TODO
     *
     * @param args Test Argument Strings
     * @throws Exception - Exceptions could occur if Provider or Algorithms are not found.
     */
    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            System.out.println("** UniDirectional Digest:");
            System.out.println("In String:[" + arg + "], Out uni-Digest String:[" + unidirectional_digest(arg) + "]");
            System.out.println("");

            System.out.println("** BiDirectional Digest:");
            String bad = bidirectional_digest(arg);
            System.out.println("In String:[" + arg + "], Out bi-Digest String:[" + bad + "]");
            System.out.println("In String:[" + arg + "], Matches Digest:[" + checkDigest(arg, bad) + "]");
            System.out.println("");

            System.out.println("** BiDirectional Base64 Encoded Text:");
            bad = encryptMessage(arg);
            System.out.println("In String:[" + arg + "], Out Encrypted String:[" + bad + "]");
            System.out.println("In Encrypted String:[" + bad + "] is String:[" + decryptMessage(bad) + "], Matches:[" + checkMessage(arg, bad) + "]");
            System.out.println("");

            System.out.println("** BiDirectional Hexadecimal Encoded Text:");
            String asid = "0000000000000000000000000005533";
            String ad = arg + ":" + asid;
            bad = encryptToHexadecimalString(ad);
            System.out.println("In String:[" + ad + "], Out Encrypted String:[" + bad + "]");
            System.out.println("In Encrypted String:[" + bad + "] is String:[" + decryptFromHexadecimalString(bad) + "], Matches:[" + checkHexadecimalStringMessage(ad, bad) + "]");
            System.out.println("");


        }
    }

}
