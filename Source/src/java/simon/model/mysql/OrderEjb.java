package simon.model.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import simon.entities.DVD;
import simon.entities.Order;
import simon.entities.OrderLine;

/**
 * Carries out order related functions, e.g. adding and removing orders.
 * 
 * This is a Singleton Enterprise Java Bean which ensures that only one instance of this object is created. 
 * 
 * @author Simon Stanford
 */
@Singleton
public class OrderEjb {

    @EJB
    simon.model.mysql.FetchEjb fetchEjb;

    // <editor-fold defaultstate="collapsed" desc="Order manipulation methods">
    /**
     * Creates an order in the database.
     * 
     * Preconditions: an order object has been created.
     * Postcondition: the order table size is +1
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param customerUserName the username of the customer creating the order
     * @param order the order that is to be added to the database
     * @return true if the order is created, false otherwise
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Boolean CreateOrder
	(String dbUrl, String dbUsername, String dbPassword, String customerUserName, Order order)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	//only complete an order if the desired quantities for all item are in stock
	//check all items in the order, if any are in stock then set inStock to false
        Boolean inStock = true;
        for (OrderLine line : order.getItems()) {
            if (!inStock(dbUrl, dbUsername, dbPassword, line.getDvd().getId(), line.getQuantity())) {
                inStock = false;
            }
        }
	
        Integer result = 0; //holds the number of database rows inserted

	//only continue order processing if all items are in stock
        if (inStock) {
	    //connect to the database and execute the INSERT to add the order details to the orderInfo table
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
            try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
                if (connection != null) {
                    PreparedStatement insertStatement = 
			    connection.prepareStatement("INSERT INTO orderInfo VALUES(null,?, ?, null);");
                    insertStatement.setString(1, customerUserName);
                    insertStatement.setTimestamp(2, order.getOrderDate());
		    //execute the statement and add the number of rows changed to the result variable
                    result += insertStatement.executeUpdate(); 
                }
            }
	    
	    //connect to the database and execute the INSERT to add the items in an order to the orders table
            try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
                if (connection != null) {
                    for (OrderLine line : order.getItems()) {
                        PreparedStatement insertStatement = connection.prepareStatement
	("INSERT INTO orders "
		+ "VALUES"
		+ "(null,"
		+ "(SELECT orderNumber "
		+ "FROM orderInfo "
		+ "WHERE customerUsername = ? "
		+ "ORDER BY orderNumber DESC LIMIT 1), ?, ?);");
                        insertStatement.setString(1, customerUserName);
                        insertStatement.setString(2, line.getDvd().getId().toString());
                        insertStatement.setString(3, line.getQuantity().toString());
			//execute the statement and add the number of rows changed to the result variable
                        result += insertStatement.executeUpdate();
                    }
                }
            }
        }

	//if at least one row has changed then adjust the stock quantites and return true
        if (result > 0) {
            for (OrderLine line : order.getItems()) {
                reduceStock(dbUrl, dbUsername, dbPassword, line.getDvd().getId(), line.getQuantity());
            }
            return true;
        } else {
            return false; //if the purchase has failed (i.e. not enough stock) then return false
        }
    }

    /**
     * Deletes an order from the database
     * 
     * Precondition: an order must exist with a matching order number.
     * Postcondition: the size of the orderInfo database is -1. All entries in the orders table with matching
     * order number are removed
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param order the order to be deleted from the database
     * @return true if the order is deleted, false otherwise
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public Boolean DeleteOrder(String dbUrl, String dbUsername, String dbPassword, Order order)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver

        Integer resultOrdersTable = -1;  //variable to record the number of database rows changed
	//connect to the database and execute the DELETE from the orders table
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = 
			connection.prepareStatement("DELETE FROM orders WHERE orderNumber = ?;");
                ps.setInt(1, order.getOrderNo());
                resultOrdersTable = ps.executeUpdate();
            }
        }

        Integer resultOrderInfoTable = -1;
	//connect to the database and execute the DELETE from the orderInfo table
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = 
			connection.prepareStatement("DELETE FROM orderInfo WHERE orderNumber = ?;");
                ps.setInt(1, order.getOrderNo());
                resultOrderInfoTable = ps.executeUpdate();
            }
        }

	//if at least 1 rows has been deleted from both tables increase the stock levels and return true
        if (resultOrdersTable > 0 && resultOrderInfoTable > 0) {
            for (OrderLine line : order.getItems()) {
                increaseStock(dbUrl, dbUsername, dbPassword, line.getDvd().getId(), line.getQuantity());
            }
            return true;
        } else {
            return false; //else return false
        }

    }

    /**
     * Gets a list of orders.  If a username is provided, all orders for that username are returned. If not,
     * all orders for all users are returned.
     * 
     * Precondition: none
     * Postcondition: list of orders is returned
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param customerUsername the customer username to retrieve the orders for. If null then all orders are
     * returned.
     * @return a list of all orders by a certain customer, or all customers if executed by the admin
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public ArrayList<Order> GetOrders
	(String dbUrl, String dbUsername, String dbPassword, String customerUsername)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	    
        ArrayList<Order> results = new ArrayList<>(); //holds the list of orders
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver

	//connect to the database and execute the SELECT
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = null;
		//if customUsername is null then all orders are returned
                if (customerUsername == null) {
                    ps = connection.prepareStatement("SELECT orderNumber, "
                            + "datePlaced, "
                            + "dateDispatched, "
                            + "customerUsername "
                            + "FROM "
                            + "orderInfo;");
                } else {
		    //if customerUsername is not null the orders for that username are returned
                    ps = connection.prepareStatement("SELECT orderNumber, "
                            + "datePlaced, "
                            + "dateDispatched, "
                            + "customerUsername "
                            + "FROM "
                            + "orderInfo "
                            + "WHERE "
                            + "customerUsername = ?;");
                    ps.setString(1, customerUsername);
                }
		
		//the query is executed. results are parsed into an order object and added to a list
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Integer orderNumber = rs.getInt("orderNumber");
                    Timestamp datePlaced = rs.getTimestamp("datePlaced");
                    Timestamp dateDispatched = rs.getTimestamp("dateDispatched");
                    String username = rs.getString("customerUsername");
                    ArrayList<OrderLine> dvds = getOrderDetails(dbUrl, dbUsername, dbPassword, orderNumber);
                    results.add(new Order(dvds, datePlaced, dateDispatched, orderNumber, username));
                }
            }
        }
        return results; //the list is returned
    }

    /**
     * Gets all items ordered for a given order number.
     * 
     * Precondition: an order must exist with a matching order number
     * Postcondition: the items for the order are returned
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param orderNumber the unique order number to retrieve the details for
     * @return a list of all items in an order
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    private ArrayList<OrderLine> getOrderDetails
	(String dbUrl, String dbUsername, String dbPassword, Integer orderNumber)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	    
        ArrayList<OrderLine> dvdList = new ArrayList(); //holds the list of items
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver

	//connect to the database and execute the SELECT
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement
	("SELECT itemId, quantity FROM orders WHERE orderNumber = ?;");
                ps.setInt(1, orderNumber);
		
		//execute the query and parse the results into a list of OrderLine objects
		//add the object to the list
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    DVD dvd = fetchEjb.GetSingleDvd(dbUrl, dbUsername, dbPassword, rs.getInt("itemId"));
                    Integer quantity = rs.getInt("quantity");
                    dvdList.add(new OrderLine(dvd, quantity));
                }
            }
            return dvdList; //return the results
        }
    }

    /**
     * Marks an order as dispatched.
     * 
     * Precondition: an order with a matching order number must exist
     * Postcondition: the dateDispatched field for the given order number is changed to the provided date
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param orderNo the unique order number to mark as dispatched
     * @param dispatchDate the date to use as the dispatch date
     * @return the number of rows updated in the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    public int MarkDispatched
	(String dbUrl, String dbUsername, String dbPassword, Integer orderNo, Timestamp dispatchDate)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver

	//connect to the database and execute the UPDATE
        Integer result = -1; //holds the number of rows updated
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement
	("UPDATE orderInfo SET dateDispatched = ? WHERE orderNumber = ?;");
                ps.setTimestamp(1, dispatchDate);
                ps.setInt(2, orderNo);
                result = ps.executeUpdate(); //execute the update statement
            }
        }
        return result; //return the number of rows updated
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stock amendment methods">
    
    /**
     * Determines if a given film ID has the specified stock level.
     * 
     * Precondition: a film must exist with a matching film ID
     * Postcondition: the result is returned
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param filmId the unique id of the film
     * @param quantityDesired the quantity the customer is requesting
     * @return true if there is at least the item quantity the customer is requesting, false otherwise
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    private Boolean inStock(String dbUrl, String dbUsername, String dbPassword, int filmId, int quantityDesired)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        int actualStockLevel = -1; //holds the stock quantity of the specified ID 
	
	//connect to the database and execute the SELECT
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = 
			connection.prepareStatement("SELECT quantity FROM stock WHERE id = ?;");
                ps.setInt(1, filmId);
                ResultSet rs = ps.executeQuery();

		//execute the query and parse the actual stock level into a variable
                while (rs.next()) {
                    actualStockLevel = rs.getInt("quantity");
                }
            }

	    //return true if the actual stock level is greater or equal to the quantity desired.
            if (actualStockLevel >= quantityDesired) {
                return true;
            } else {
                return false; //otherwise return false
            }
        }
    }
   
    /**
     * Increases the stock level of a given film by a specified amount.
     * 
     * Precondition: a film must exist in the database with the matching id
     * Postcondition: the stock level of a given film increases by the specified amount
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param filmId the unique id of the film
     * @param quantity the quantity to increase the stock level by
     * @return the number of rows updated in the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    private int increaseStock(String dbUrl, String dbUsername, String dbPassword, int filmId, int quantity)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        int result = -1; //holds the number of records changed
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver

	//connect to the database and execute the UPDATE
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = 
			connection.prepareStatement("UPDATE stock SET quantity = quantity + ? WHERE id = ?;");
                ps.setInt(1, quantity);
                ps.setInt(2, filmId);
                result = ps.executeUpdate(); //execute the statement and record the no. of records changed.
            }
            return result; //return the number of records changed
        }
    }
    
    /**
     * Decreases the stock level of a given film by a specified amount.
     * 
     * Precondition: a film must exist in the database with the matching id
     * Postcondition: the stock level of a given film decreases by the specified amount
     * 
     * @param dbUrl database connection string
     * @param dbUsername database username
     * @param dbPassword database password
     * @param filmId the unique id of the film
     * @param quantity the quantity to decrease the stock level by
     * @return the number of rows updated in the database
     * @throws SQLException exceptions related to the database
     * @throws ClassNotFoundException exceptions related to the retrieving the MySQL driver instance
     * @throws InstantiationException exceptions related to instantiating the database connection object.
     * @throws IllegalAccessException exceptions related to unauthorised database access.
     */
    private int reduceStock(String dbUrl, String dbUsername, String dbPassword, int filmId, int quantity)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        int result = -1; //holds the number of rows updated
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //load and initialise the MySQL driver

	//connect to the database and execute the UPDATE
        try (final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            if (connection != null) {
                PreparedStatement ps = 
			connection.prepareStatement("UPDATE stock SET quantity = quantity - ? WHERE id = ?;");
                ps.setInt(1, quantity);
                ps.setInt(2, filmId);
		
		//execute the update statement and record the number of rows changed
                result = ps.executeUpdate();
            }
            return result; //return the number of rows changed
        }
    }// </editor-fold>
}
