package simon.model;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Performs security services such as password hashing, encryption/decryption and credit card authentication.
 *
 * @author Simon Stanford
 */
public class Security implements iSecurity {

    /**
     * Converts a password and salt into a SHA2 512 bit hash.
     *
     * Precondition: none Postcondition: password converted to hash
     *
     * @param password the password to hash
     * @param salt the salt to add for added security
     * @return a hash of both the password and salt
     */
    public String Hash(String password, String salt) {
        return DigestUtils.sha512Hex(password + salt);
    }

    /**
     * Encrypts a string using AES 256bit encryption.
     *
     * Precondition: none Postcondition: the data is encrypted
     *
     * Taken from: http://deveshsharma.info/2012/12/13/aes-encryption-in-java/
     *
     * @param plainText the text to encrypt
     * @param key the key used for encryption and decryption
     * @return the encrypted text
     * @throws ServletException
     */
    public String Encrypt(String plainText, String key) throws ServletException {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

            return new Base64().encodeAsString(encryptedTextBytes);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }
    }

    /**
     * Decrypts a string using AES 256bit encryption.
     *
     * Precondition: data has been encrypted Postcondition: the data is decrypted
     *
     * Taken from: http://deveshsharma.info/2012/12/13/aes-encryption-in-java/
     *
     * @param encryptedText the text to decrypt
     * @param key the key used for encryption and decryption
     * @return the decrypted text
     * @throws ServletException if a servlet-specific error occurs
     */
    public String Decrypt(String encryptedText, String key) throws ServletException {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);
            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

            return new String(decryptedTextBytes);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }
    }

    /**
     * Verifies a credit card number.
     *
     * Precondition: none Postcondition: a card is verified, or not
     *
     * @param cardNumber the card number to verify
     * @return true if verified, false otherwise
     */
    public boolean ValidateCreditCard(java.lang.String cardNumber) {
	//only check card numbers that are the correct length
        if (cardNumber.length() == 16) {
            simon.model.CreditCardValidationService service = new simon.model.CreditCardValidationService();
            simon.model.CreditCardValidationServiceSoap port = service.getCreditCardValidationServiceSoap();
            return port.validateCreditCard(cardNumber);
        } else {
            return false;
        }

    }
}
