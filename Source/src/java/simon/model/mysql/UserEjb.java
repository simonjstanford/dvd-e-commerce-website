package simon.model.mysql;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Singleton;
import javax.servlet.ServletException;
import simon.entities.User;
import simon.model.*;

/**
 * Carries out user related functions, e.g. password checking, and retrieving user details.
 * 
 * This is a Singleton Enterprise Java Bean which ensures that only one instance of this object is created. 
 * 
 * @author Simon Stanford
 */
@Singleton
public class UserEjb {
    
    /**
     * Authenticates a given username/password combination.
     * 
     * Preconditions: none
     * Postconditions: the username/password combination is authenticated
     * 
     * @param dbUrl database connection string
     * @param dbUserName database username
     * @param dbPassword database password
     * @param userName the customer username to check 
     * @param password the customer password to check
     * @return true if the username/password combination is verified, false otherwise
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Boolean CheckPassword
	(String dbUrl, String dbUserName, String dbPassword, String userName, String password)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
        
	String salt = null;//contains the unique salt added to strengthen password security
        String existingUsername = null; //contains the username that has the matching password
        
	//connect to the database and execute
	//first retrieve the salt from the database. Each user has a unique salt
	try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            if (connection != null) {
                PreparedStatement searchStatement = connection.prepareStatement
	("SELECT salt FROM customer WHERE username = ?;");
                searchStatement.setString(1, userName);
                ResultSet rs = searchStatement.executeQuery();
                while (rs.next()) {
                    salt = rs.getString("salt");
                }
            }
        }
	
	//connect to the database and execute
	//the username stored in the database is returned that matches the username/password combination
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            if (connection != null) {
                iSecurity security = new Security();
                PreparedStatement searchStatement = connection.prepareStatement
	("SELECT username FROM customer WHERE username = ? AND password = ?;");
                searchStatement.setString(1, userName); //the username is stored as plain text
		/*
		the password is not stored - only a hash is stored.  To verify the password, the provided
		password is mixed with the unique salt and hashed using the same algorith.  If this hash 
		matches the hash stored in the database (and is stored against the matching username) then
		the user is authenticated
		*/
                searchStatement.setString(2, security.Hash(password, salt));
                ResultSet rs = searchStatement.executeQuery();
                while (rs.next()) {
                    existingUsername = rs.getString("username");
                }
            }
        }
	
	//if a user has been found with a matching username/password combination then return true
        if (userName.equals(existingUsername)) {
            return true;
        } else {
            return false; //else return false
        }
    }

    /**
     * Gets the customer's first name from a provided username.
     * 
     * Precondition: a user exists with the provide username
     * Postcondition: the first name is returned
     * 
     * @param dbUrl database connection string
     * @param dbUserName database username
     * @param dbPassword database password
     * @param userName the customer username
     * @param encryptionKey the key used for decryption
     * @return the first name for the customer with the specified username
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     * @throws ServletException 
     */
    public String GetFirstName
	(String dbUrl, String dbUserName, String dbPassword, String userName, String encryptionKey)
            throws SQLException, ClassNotFoundException, InstantiationException, 
		IllegalAccessException, ServletException {
	    
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
        String firstName = null; //holds the first name
        iSecurity security = new Security(); //used to decrypt the results returned from the database
	
	//connect to the database and execute
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            if (connection != null) {
                PreparedStatement searchStatement = 
			connection.prepareStatement("SELECT firstName FROM customer WHERE username = ?;");
                searchStatement.setString(1, userName);
		
		//execute the query and parse the results
		//data is encrypted in the database, so must be decrypted usin the same key
                ResultSet rs = searchStatement.executeQuery();
                while (rs.next()) {
                    firstName = security.Decrypt(rs.getString("firstName"), encryptionKey);
                }
            }
        }
        return firstName; //return the results
    }

    /**
     * Gets all customer details for a specified username
     * 
     * Preconditions: a user exists with the provide username
     * Postcondition: the user information is returned
     * 
     * @param dbUrl database connection string
     * @param dbUserName database username
     * @param dbPassword database password
     * @param userName the unique username of the user
     * @param encryptionKey the key used for decryption
     * @return a User object representing the specified user
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     * @throws ServletException 
     */
    public User GetUser
	(String dbUrl, String dbUserName, String dbPassword, String userName, String encryptionKey)
            throws SQLException, ClassNotFoundException, 
		InstantiationException, IllegalAccessException, ServletException {
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//object used to hold the results of the query
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
        String username;
        String password;
        String salt;
	
	//connect to the database and execute
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            if (connection != null) {
                iSecurity security = new Security();//used to decrypt the results returned from the database
		
                PreparedStatement searchStatement = 
			connection.prepareStatement("SELECT * FROM customer WHERE username = ?;");
                searchStatement.setString(1, userName);
		
		//execute the statement. Parse results into a User object
		//data is encrypted in the database, so must be decrypted usin the same key
                ResultSet rs = searchStatement.executeQuery();
                while (rs.next()) {
                    firstName_plain = security.Decrypt(rs.getString("firstName"), encryptionKey);
                    secondName_plain = security.Decrypt(rs.getString("secondName"), encryptionKey);
                    address_line1_plain = security.Decrypt(rs.getString("address_line1"), encryptionKey);
                    address_line2_plain = security.Decrypt(rs.getString("address_line2"), encryptionKey);
                    city_plain = security.Decrypt(rs.getString("city"), encryptionKey);
                    postcode_plain = security.Decrypt(rs.getString("postcode"), encryptionKey);
                    username = rs.getString("username");
                    password = rs.getString("password");
                    salt = rs.getString("salt");
                    firstName_encrypted = rs.getString("firstName");
                    secondName_encrypted = rs.getString("secondName");
                    address_line1_encrypted = rs.getString("address_line1");
                    address_line2_encrypted = rs.getString("address_line2");
                    city_encrypted = rs.getString("city");
                    postcode_encrypted = rs.getString("postcode");
		    
		    //return the User object
                    return new User(username,
                            firstName_plain,
                            secondName_plain,
                            address_line1_plain,
                            address_line2_plain,
                            city_plain,
                            postcode_plain,
                            password,
                            salt,
                            firstName_encrypted,
                            secondName_encrypted,
                            address_line1_encrypted,
                            address_line2_encrypted,
                            city_encrypted,
                            postcode_encrypted);
                }
            }
        }
        return null; //return null if an error occurs
    }

    /**
     * Adds a new user to the database
     * 
     * Preconditions: a user cannot exist with the same username
     * Postconditions: the user table size is +1
     * 
     * @param dbUrl database connection string
     * @param dbUserName database username
     * @param dbPassword database password
     * @param firstName the first name of the user
     * @param secondName the second name of the user
     * @param address1 the first line of the address for the user
     * @param address2 the second line of the address for the user
     * @param city the city of residence for the user
     * @param postcode the user's post code
     * @param userName the user's chosen username
     * @param password the user's chosen password
     * @param encryptionKey the encryption key used for encryption/decryption
     * @return the number of rows updated in the database, or a negative number representing an error code
     * errors: 
     * -10) the username is already taken
     * -9) a required field was blank 
     * -8) the username or password was below 6 characters.  'admin' is the exception to this rule
     * -1) an unknown error has occurred
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     * @throws ServletException 
     */
    public Integer AddUser(String dbUrl,
            String dbUserName,
            String dbPassword,
            String firstName,
            String secondName,
            String address1,
            String address2,
            String city,
            String postcode,
            String userName,
            String password,
            String encryptionKey)
            throws SQLException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException, ServletException {

        Integer result = -1; //holds the number of rows updated or the error code
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
        String existingUsername = null; //holds the username of a duplicate username, if any
	
	//connect to the database and execute
	//determine if a user already has taken the specified username
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            if (connection != null) {
                PreparedStatement searchStatement = 
			connection.prepareStatement("SELECT username FROM customer WHERE username = ?;");
                searchStatement.setString(1, userName);
		
		//execute the query and parse the results
		//save the username if a user with a matching username exists
                ResultSet rs = searchStatement.executeQuery();
                while (rs.next()) {
                    existingUsername = rs.getString("username");
                }
            }
        }
	
	//if the user has not entered essential data then return error -9
        if (firstName == "" || secondName == "" || address1 == "" || 
		city == "" || postcode == "" || userName == "" || password == "") {
            return -9;
        }
	
	//if the user has not entered the minimum length for username and password retunr error -8
	//'admin' is the exception to this rule
        if ((userName.length() < 6 || password.length() < 6) && !userName.equals("admin")) {
            return -8;
        }
	
	//only continue if the chosen username does not match an existing username
        if (!userName.equals(existingUsername)) {
	    //generate a unique random salt for added security
            String salt = new BigInteger(512, new SecureRandom()).toString(32);
            iSecurity security = new Security(); //used for encryption and hashing
            
	    //connect to the database and execute
            try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
                if (connection != null) {
                    PreparedStatement insertStatement = 
			    connection.prepareStatement("INSERT INTO customer "
				    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);");
		    
		    //encrypt or hash all sensitive data
                    insertStatement.setString(1, userName);
                    insertStatement.setString(2, security.Encrypt(firstName, encryptionKey));
                    insertStatement.setString(3, security.Encrypt(secondName, encryptionKey));
                    insertStatement.setString(4, security.Encrypt(address1, encryptionKey));
                    insertStatement.setString(5, security.Encrypt(address2, encryptionKey));
                    insertStatement.setString(6, security.Encrypt(city, encryptionKey));
                    insertStatement.setString(7, security.Encrypt(postcode, encryptionKey));
                    insertStatement.setString(8, security.Hash(password, salt));
                    insertStatement.setString(9, salt);
		    
		    //execute the statement and record the number of rows changed
                    result = insertStatement.executeUpdate();
                }
            }
            return result; //return the number of rows changed
        } else {
            return -10; //return error -10 if an error occured during SQL execution.
        }
    }
}
