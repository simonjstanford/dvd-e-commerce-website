package simon.entities;

/**
 * @author Simon Stanford
 *
 * Represents a customer that has registered on the DVD website.
 */
public class User {

    //the private fields for the getter methods
    String username;
    String firstName_plain;
    String secondName_plain;
    String address_line1_plain;
    String address_line2_plain;
    String city_plain;
    String postcode_plain;

    String firstName_encrypted;
    String secondName_encrypted;
    String address_line1_encrypted;
    String address_line2_encrypted;
    String city_encrypted;
    String postcode_encrypted;

    String password;
    String salt;

    /**
     * The constructor
     * 
     * @param username the customer's username (plaintext)
     * @param firstName_plain the customer's first name (plaintext)
     * @param secondName_plain the customer's second name (plaintext)
     * @param address_line1_plain the customer's first line of address (plaintext)
     * @param address_line2_plain the customer's second line of address (plaintext)
     * @param city_plain the customer's city of residence (plaintext)
     * @param postcode_plain the customer's postcode (plaintext)
     * @param password the SHA2 512 hash of the customer's password
     * @param salt the unique salt used in the password for added security (plaintext)
     * @param firstName_encrypted the customer's first name (encrypted)
     * @param secondName_encrypted the customer's second name (encrypted)
     * @param address_line1_encrypted the customer's first line of address (encrypted)
     * @param address_line2_encrypted the customer's second line of address (encrypted)
     * @param city_encrypted the customer's city of residence (encrypted)
     * @param postcode_encrypted the customer's postcode (encrypted)
     */
    public User(String username,
            String firstName_plain,
            String secondName_plain,
            String address_line1_plain,
            String address_line2_plain,
            String city_plain,
            String postcode_plain,
            String password,
            String salt,
            String firstName_encrypted,
            String secondName_encrypted,
            String address_line1_encrypted,
            String address_line2_encrypted,
            String city_encrypted,
            String postcode_encrypted) {

        this.firstName_plain = firstName_plain;
        this.secondName_plain = secondName_plain;
        this.address_line1_plain = address_line1_plain;
        this.address_line2_plain = address_line2_plain;
        this.city_plain = city_plain;
        this.postcode_plain = postcode_plain;

        this.firstName_encrypted = firstName_encrypted;
        this.secondName_encrypted = secondName_encrypted;
        this.address_line1_encrypted = address_line1_encrypted;
        this.address_line2_encrypted = address_line2_encrypted;
        this.city_encrypted = city_encrypted;
        this.postcode_encrypted = postcode_encrypted;

        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    /**
     * @return the username of the customer
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the plan text first name of the customer
     */
    public String getFirstName_plain() {
        return firstName_plain;
    }

    /**
     * @return the plan text second name of the customer
     */
    public String getSecondName_plain() {
        return secondName_plain;
    }

    /**
     * @return the plan text first line of address for the customer
     */
    public String getAddress_line1_plain() {
        return address_line1_plain;
    }

    /**
     * @return the plan text second line of address for the customer
     */
    public String getAddress_line2_plain() {
        return address_line2_plain;
    }

    /**
     * @return the plan text city of residence for the customer
     */
    public String getCity_plain() {
        return city_plain;
    }

    /**
     * @return the plan text postcode for the customer
     */
    public String getPostcode_plain() {
        return postcode_plain;
    }

    /**
     * @return the SHA2 512bit password hash for the customer
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the unique plaintext salt for the customer. Used in the password hash for added security
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @return the encrypted text first name of the customer
     */
    public String getFirstName_encrypted() {
        return firstName_encrypted;
    }

    /**
     * @return the encrypted text second name of the customer
     */
    public String getSecondName_encrypted() {
        return secondName_encrypted;
    }

    /**
     * @return the encrypted text first line of address for the customer
     */
    public String getAddress_line1_encrypted() {
        return address_line1_encrypted;
    }

    /**
     * @return the encrypted text second line of address for the customer
     */
    public String getAddress_line2_encrypted() {
        return address_line2_encrypted;
    }

    /**
     * @return the encrypted text city of residence for the customer
     */
    public String getCity_encrypted() {
        return city_encrypted;
    }

    /**
     * @return the encrypted text postcode for the customer
     */
    public String getPostcode_encrypted() {
        return postcode_encrypted;
    }
}
