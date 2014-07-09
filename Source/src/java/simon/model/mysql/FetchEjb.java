package simon.model.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ejb.Singleton;
import simon.entities.DVD;

/**
 * Carries out database query functions, e.g. retrieving a list of films of a certain genre.
 *
 * This is a Singleton Enterprise Java Bean which ensures that only one instance of this object is created.
 *
 * @author Simon Stanford
 */
@Singleton
public class FetchEjb {

    /**
     * Retrieves all films in the database.
     * 
     * Precondition: none
     * Postcondition: a list of all films is returned
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @return a list of all DVDs
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public ArrayList GetAllFilms(String dbUrl, String userName, String password)
	    throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
	return executeSearch(dbUrl, userName, password, null);
    }

    /**
     * Retrieves all films in the database of a specified genre
     * 
     * Precondition: none
     * Postcondition: a list of all films is returned for a certain genre
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param filterByGenre the genre to filter the dvds by
     * @return a list of all DVDs of the specified genre
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public ArrayList GetFilmsOfGenre(String dbUrl, String userName, String password, String filterByGenre)
	    throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
	return executeSearch(dbUrl, userName, password, filterByGenre);
    }

    /**
     * Gets a single DVD from the database using the specified film ID
     * 
     * Precondition: a film must exist in the database with the specified ID
     * Postcondition: the film is returned
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param filmId the unique id of the film
     * @return a single DVD with the unique ID provided
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public DVD GetSingleDvd(String dbUrl, String userName, String password, Integer filmId)
	    throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	ArrayList<DVD> results = new ArrayList<>(); //a list of all matching results found
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the SELECT
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = connection.prepareStatement("SELECT "
			+ "dvd.id, "
			+ "dvd.name, "
			+ "genre.genre, "
			+ "dvd.price, "
			+ "dvd.description, "
			+ "dvd.imageLink, "
			+ "stock.quantity "
			+ "FROM dvd "
			+ "INNER JOIN genre "
			+ "ON dvd.genre = genre.id "
			+ "INNER JOIN stock "
			+ "ON dvd.id = stock.id "
			+ "WHERE dvd.id = ?"
			+ ";");
		ps.setInt(1, filmId);
		
		//execute the query and parse the results into a dvd object. 
		//Add all results into a list
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
		    Integer id = rs.getInt("ID");
		    String name = rs.getString("NAME");
		    String price = rs.getString("PRICE");
		    String genre = rs.getString("GENRE");
		    String description = rs.getString("DESCRIPTION");
		    String imageLink = rs.getString("IMAGELINK");
		    Integer quantity = rs.getInt("QUANTITY");
		    results.add(new DVD(id, name, genre, price, description, imageLink, quantity));
		}
	    }
	}
	
	//if at least one result is found, return the first item in the list, else return null
	if (results.size() > 0) {
	    return results.get(0);
	} else {
	    return null; //return the results
	}
    }

    /**
     * Retrieves a list of DVDs from the database. If no genre is specified to filter the result, all dvds
     * are returned.
     * 
     * Precondition: none
     * Postcondition: results are returned
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param filterGenre the genre to filter the DVDs by. If null then all DVDs are returned
     * @return a list of DVDs, either all DVDs or filtered by genre
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    private ArrayList<DVD> executeSearch(String dbUrl, String userName, String password, String filterGenre) 
	    throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
	ArrayList<DVD> results = new ArrayList<>(); //a list of all matching results found
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the SELECT
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = null;
		//get all dvds if no filter genre is provided
		if (filterGenre == null) {
		    ps = connection.prepareStatement("SELECT "
			    + "dvd.id, "
			    + "dvd.name, "
			    + "genre.genre, "
			    + "dvd.price, "
			    + "dvd.description, "
			    + "dvd.imageLink, "
			    + "stock.quantity "
			    + "FROM dvd "
			    + "INNER JOIN genre "
			    + "ON dvd.genre = genre.id "
			    + "INNER JOIN stock "
			    + "ON dvd.id = stock.id "
			    + "ORDER BY name"
			    + ";");
		} else {
		    ps = connection.prepareStatement("SELECT "
			    + "dvd.id, "
			    + "dvd.name, "
			    + "genre.genre, "
			    + "dvd.price, "
			    + "dvd.description, "
			    + "dvd.imageLink, "
			    + "stock.quantity "
			    + "FROM dvd "
			    + "INNER JOIN genre "
			    + "ON dvd.genre = genre.id "
			    + "INNER JOIN stock "
			    + "ON dvd.id = stock.id "
			    + "WHERE genre.genre = ? "
			    + "ORDER BY name"
			    + ";");
		    ps.setString(1, filterGenre);
		}
		
		//execute the query and parse the results into a dvd object. 
		//Add all results into a list
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
		    int id = Integer.parseInt(rs.getString("ID"));
		    String name = rs.getString("NAME");
		    String price = rs.getString("PRICE");
		    String genre = rs.getString("GENRE");
		    String description = rs.getString("DESCRIPTION");
		    String imageLink = rs.getString("IMAGELINK");
		    Integer quantity = rs.getInt("QUANTITY");
		    results.add(new DVD(id, name, genre, price, description, imageLink, quantity));
		}
	    }
	}
	return results; //return the results
    }

    /**
     * Retrieves five random DVDs from the database. If no genre is specified to filter the result, 
     * a random section of all dvds are returned.
     * 
     * Precondition: none
     * Postcondition: results are returned
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @param searchGenre the genre to filter the DVDs by. If null then a selection of DVDs is returned that
     * ignores the genre
     * @return A list of random films
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public ArrayList<DVD> GetFiveRandomFilms
	(String dbUrl, String userName, String password, String searchGenre)
	    throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	
	ArrayList<DVD> results = new ArrayList<>(); //a list of all matching results found
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the SELECT
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = null;
		//ignore the genre if no genre filter is provided
		if (searchGenre == null) {
		    ps = connection.prepareStatement("SELECT "
			    + "dvd.id, "
			    + "dvd.name, "
			    + "genre.genre, "
			    + "dvd.price, "
			    + "dvd.description, "
			    + "dvd.imageLink, "
			    + "stock.quantity "
			    + "FROM dvd "
			    + "INNER JOIN genre "
			    + "ON dvd.genre = genre.id "
			    + "INNER JOIN stock "
			    + "ON dvd.id = stock.id "
			    + "ORDER BY RAND() LIMIT 5"
			    + ";");
		} else {
		    ps = connection.prepareStatement("SELECT "
			    + "dvd.id, "
			    + "dvd.name, "
			    + "genre.genre, "
			    + "dvd.price, "
			    + "dvd.description, "
			    + "dvd.imageLink, "
			    + "stock.quantity "
			    + "FROM dvd "
			    + "INNER JOIN genre "
			    + "ON dvd.genre = genre.id "
			    + "INNER JOIN stock "
			    + "ON dvd.id = stock.id "
			    + "WHERE genre.genre = ? "
			    + "ORDER BY RAND() LIMIT 5"
			    + ";");
		    ps.setString(1, searchGenre);
		}
		
		//execute the query and parse the results into a dvd object. 
		//Add all results into a list
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
		    int id = Integer.parseInt(rs.getString("ID"));
		    String name = rs.getString("NAME");
		    String price = rs.getString("PRICE");
		    String genre = rs.getString("GENRE");
		    String description = rs.getString("DESCRIPTION");
		    String imageLink = rs.getString("IMAGELINK");
		    Integer quantity = rs.getInt("QUANTITY");
		    results.add(new DVD(id, name, genre, price, description, imageLink, quantity));
		}
	    }
	}
	return results; //return the results
    }

    /**
     * Returns a list of all genres used in the database
     * 
     * Preconditions: none
     * Postconditions: results are retuned
     * 
     * @param dbUrl database connection string
     * @param userName database username
     * @param password database password
     * @return a list of all genres in the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public ArrayList<String> GetGenres(String dbUrl, String userName, String password)
	    throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	ArrayList<String> results = new ArrayList<>(); //a list of all matching results found
	Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
	
	//connect to the database and execute the SELECT
	try (final Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
	    if (connection != null) {
		PreparedStatement ps = connection.prepareStatement("SELECT genre.genre FROM genre;");
		
		//execute the query and parse the results into list 
		//Add all results into a list
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
		    results.add(rs.getString("GENRE"));
		}
	    }
	}
	return results; //return the results
    }
}
