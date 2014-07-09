package simon.model.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Singleton;

/**
 * Carries out website administration functions, e.g. adding, editing and removing films.
 *
 * This is a Singleton Enterprise Java Bean which ensures that only one instance of this object is created.
 *
 * @author Simon Stanford
 */
@Singleton
public class AdminEjb {

    // <editor-fold defaultstate="collapsed" desc="DVD Insert, Update and Delete methods.">
    /**
     * Inserts a new DVD into the database.
     *
     * Precondition: none 
     * Postcondition: database size is +1
     *
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param name new film title
     * @param genre new film genre
     * @param price new film price
     * @param imageLink link to an image of the dvd cover
     * @param description new film description
     * @return the number of rows that have been inserted into the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Integer InsertTitle(String dbUrl,
	    String userName,
	    String password,
	    String name,
	    String genre,
	    String price,
	    String imageLink,
	    String description)
	    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	Integer result = -1; //variable to record the number of database rows inserted
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	//connect to the database and execute the INSERT
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = connection.prepareStatement("INSERT INTO dvd "
			+ "(name, genre, price, imageLink, description) "
			+ "VALUES(?, (select id from genre where genre = ?), ?, ?, ?);");
		ps.setString(1, name);
		ps.setString(2, genre);
		ps.setString(3, price);
		ps.setString(4, imageLink);
		ps.setString(5, description);

		result = ps.executeUpdate();
	    }
	}
	return result; //return the number of rows changed
    }

    /**
     * Update an existing title.
     *
     * Precondition: the title must exist in the database 
     * Postcondition: data in one row of the database is changed
     *
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param id the unique id of the film
     * @param newName new film title
     * @param genre new film genre
     * @param price new film price
     * @param imageLink link to an image of the dvd cover
     * @param description new film description
     * @return the number of rows that have been updated in the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Integer UpdateTitle(String dbUrl,
	    String userName,
	    String password,
	    int id,
	    String newName,
	    String genre,
	    String price,
	    String imageLink,
	    String description)
	    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	Integer result = -1; //variable to record the number of database rows changed
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the UPDATE
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = connection.prepareStatement("UPDATE dvd "
			+ "SET name = ?, "
			+ "genre = (select id from genre where genre = ?), "
			+ "price = ?, "
			+ "imageLink = ?, "
			+ "description = ? "
			+ "WHERE id = ?;");

		ps.setString(1, newName);
		ps.setString(2, genre);
		ps.setString(3, price);
		ps.setString(4, imageLink);
		ps.setString(5, description);
		ps.setInt(6, id);
		result = ps.executeUpdate();
	    }
	}
	return result; //return the number of rows changed
    }

    /**
     * Deletes a DVD from the database.
     * 
     * Precondition: a DVD with the film ID must exist in the database
     * Postcondition: the database size is -1
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param filmId the unique id of the film
     * @return the number of rows that have been removed from the databaes
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Integer DeleteTitle(String dbUrl, String userName, String password, Integer filmId)
	    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	Integer result = -1; //variable to record the number of database rows changed
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the DELETE
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = connection.prepareStatement("DELETE FROM dvd WHERE id = ? LIMIT 1;");
		ps.setInt(1, filmId);
		result = ps.executeUpdate();
	    }
	}
	return result;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stock Insert, Update and Delete methods.">
    
    /**
     * Insert stock information into the database for a DVD.
     * 
     * Precondition: the DVD table must contain a film with a matching ID
     * Postcondition: the stock table size is +1
     * 
     * @param dbUrl database connection string
     * @param dbUserName database username
     * @param dbPassword database password
     * @param filmId the unique id of the film
     * @param stock the  stock quantity
     * @return the number of rows inserted into the database
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public Integer InsertStock(String dbUrl,
	    String dbUserName,
	    String dbPassword,
	    int filmId,
	    int stock)
	    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	Integer result = -1; //variable to record the number of database rows changed
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the INSERT
	try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
	    if (connection != null) {
		PreparedStatement ps = null;
		ps = connection.prepareStatement("INSERT INTO stock VALUES (?, ?)");
		ps.setInt(1, filmId);
		ps.setInt(2, stock);
		result = ps.executeUpdate();
	    }
	}
	return result; //return the number of rows changed
    }

    /**
     * Updates the stock information in the database for a specified film ID
     * 
     * Precondition: the film ID must exist in the dvd and stock tables
     * Postcondition: the stock quantity for the specified ID is set to the specifed quantity
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param filmId the unique id of the film
     * @param stock the new stock quantity
     * @return the number of rows that have been updated in the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Integer UpdateStock(String dbUrl,
	    String userName,
	    String password,
	    int filmId,
	    int stock)
	    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	Integer result = -1; //variable to record the number of database rows changed
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the UPDATE
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = null;
		ps = connection.prepareStatement("UPDATE stock SET quantity = ? WHERE id = ?");
		ps.setInt(1, stock);
		ps.setInt(2, filmId);
		result = ps.executeUpdate();
	    }
	}
	return result; //return the number of rows changed
    }

    /**
     * Deletes stock information for a specified film ID from the stock table.  Used when deleting a film.
     * 
     * Precondition: a film with a matching ID exists in the stock table. A film with a matching ID must NOT
     * exist in the dvd table
     * Postcondition: the size of the stock table is -1
     * 
     * @param dbUrl database connection string
     * @param dbUserName database username
     * @param dbPassword database password
     * @param filmId the unique id of the film
     * @return the number of rows that have been deleted in the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Integer DeleteStock(String dbUrl,
	    String dbUserName,
	    String dbPassword,
	    int filmId)
	    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	Integer result = -1; //variable to record the number of database rows changed
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the DELETE
	try (final Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
	    if (connection != null) {
		PreparedStatement ps = null;
		ps = connection.prepareStatement("DELETE FROM stock WHERE id = ?");
		ps.setInt(1, filmId);
		result = ps.executeUpdate();
	    }
	}
	return result; //return the number of rows changed
    }// </editor-fold>

    /**
     * Gets the film ID for a film.  Assumes that data in all of the rows uniquely identifies a film.
     * 
     * Precondition: a film must exist in the database with matching information
     * Postcondition: a film ID is returned
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param name the film name
     * @param genre the film genre
     * @param price the film price
     * @param imageLink the image link of the film DVD cover
     * @param description the film description
     * @return the unique ID of the film. -1 is returned if no film is found with that id
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public int GetFilmID(String dbUrl,
	    String userName,
	    String password,
	    String name,
	    String genre,
	    String price,
	    String imageLink,
	    String description)
	    throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

	int filmId = -1; //holds the film id number, initialised to -1 as the default error
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the SELECT
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = connection.prepareStatement("SELECT id FROM dvd "
			+ "WHERE name = ? "
			+ "AND price = ? "
			+ "AND genre = (SELECT id FROM genre WHERE genre = ?) "
			+ "AND description = ? "
			+ "AND imagelink = ?;");

		ps.setString(1, name);
		ps.setString(2, price);
		ps.setString(3, genre);
		ps.setString(4, description);
		ps.setString(5, imageLink);

		//execute the query and parse the results into a variable to return
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
		    filmId = rs.getInt("ID");
		}
	    }
	    return filmId; //return the film id, will be -1 if no film found
	}
    }
}
