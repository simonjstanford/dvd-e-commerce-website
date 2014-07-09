package simon.model;


import javax.servlet.ServletException;

/**
 * Performs security services such as password hashing, encryption/decryption and credit card authentication.
 * The interface abstracts the implementation from the webapp. This allows the algorithms to be easily 
 * changed.
 *
 * @author Simon Stanford
 */
public interface iSecurity {
	String Hash (String password, String salt);
	String Encrypt(String plainText, String key) throws ServletException;
	String Decrypt(String encryptedText, String key) throws ServletException;
	boolean ValidateCreditCard(String cardNumber);
}