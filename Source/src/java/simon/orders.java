/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simon;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import simon.entities.Order;

/**
 *
 * @author Simon Stanford
 */
@WebServlet(name = "orders", urlPatterns = {"/orders"})
public class orders extends HttpServlet {

    @EJB
    simon.model.mysql.OrderEjb orderEjb;

    /**
     * Searches for orders placed in the MySQL database. Can be used to find either orders for a specific 
     * customer, or as an administrative function to find all orders by all customers.
     *
     * Precondition: the user must have logged in. orders must have been placed, otherwise no orders will 
     * appear. The user 'admin' must be logged in if searching for all orders from all customers.
     *
     * Postcondition: A list of orders is saved as a session attribute. The user if forwarded to a JSP to 
     * display the results.
     *
     *
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Retrieve the database connection info from the deployment descriptor
        String dbUrl = getServletContext().getInitParameter("DbUrl");
        String dbUsername = getServletContext().getInitParameter("DbUserName");
        String dbPassword = getServletContext().getInitParameter("DbPassword");

        String action = "unknown"; //the action to perform
        String customerUsername = null; //the username calling the action

        //read the action passed as a paramater by the user
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }

        //read the username stored in the session
        //this ensures that only orders for the user logged in are retrieved
        if (request.getSession().getAttribute("username") != null) {
            customerUsername = request.getSession().getAttribute("username").toString();
        }

        Integer orderNo = -1;
        if (request.getParameter("orderNo") != null) {
            orderNo = Integer.parseInt(request.getParameter("orderNo"));
        }

        //do an action-specific task
        switch (action) {
            case "user":
                showUserOrders(dbUrl, dbUsername, dbPassword, customerUsername, request, response);
                break;
            case "admin":
                showAllOrders(customerUsername, dbUrl, dbUsername, dbPassword, request, response);
                break;
            case "dispatch":
                markDispatched(orderNo, customerUsername, dbUrl, dbUsername, dbPassword, request, response);
                showAllOrders(customerUsername, dbUrl, dbUsername, dbPassword, request, response);
                break;
            case "cancel":
                cancelOrder(orderNo, customerUsername, dbUrl, dbUsername, dbPassword, request);
                //after cancelling and order, if user is admin send him to the admin page
                if (customerUsername.equals("admin")) {
                    showAllOrders(customerUsername, dbUrl, dbUsername, dbPassword, request, response);
                } else {
                    showUserOrders(dbUrl, dbUsername, dbPassword, customerUsername, request, response);
                }
                break;
            default:
                displayError(request, response);
                break;
        }
    }

    /**
     * Displays all orders made by a single user.
     *
     * Precondition: none, if no orders have been placed then no orders will be seen 
     * Postcondition: orders are displayed
     *
     * @param dbUrl the database URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param customerUsername the username of the customer whose orders to display
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void showUserOrders(String dbUrl,
            String dbUsername,
            String dbPassword,
            String customerUsername,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Order> orders = null; //the list of orders found
        RequestDispatcher view;

        try {
            orders = orderEjb.GetOrders(dbUrl, dbUsername, dbPassword, customerUsername);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }
        //save the orders found as a session attribute
        request.getSession().setAttribute("previousOrders", orders);
        //forward the request to a JSP to display the results
        view = request.getRequestDispatcher("user/orders.jsp");
        view.forward(request, response);
    }

    /**
     * Displays all orders made by a all users. 
     * This is an administrative function that can only be executed by the username 'admin'.
     *
     * Precondition: none, if no orders have been placed then no orders will be seen 
     * Postcondition: orders are displayed
     *
     * @param loggedInUsername the username of the person logged in
     * @param dbUrl the database URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void showAllOrders(String loggedInUsername,
            String dbUrl,
            String dbUsername,
            String dbPassword,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Order> orders = null; //the list of orders found
        //if the action is 'admin', then find all orders from all customers
        //only the user 'admin' can do this, as it displays sensitive information of all users
        if (loggedInUsername.equals("admin")) {
            try {
                orders = orderEjb.GetOrders(dbUrl, dbUsername, dbPassword, null);
            } catch (Exception ex) {
                //allow exceptions to bubble up to the calling method, so they are displayed
                throw new ServletException(ex);
            }

            //save the orders found as a session attribute
            request.getSession().setAttribute("previousOrders", orders);

            //forward the request to a JSP to display the results
            RequestDispatcher view = request.getRequestDispatcher("admin/allOrders.jsp");
            view.forward(request, response);
        } else {
            displayError(request, response);
        }
    }

    /**
     * Marks a single order number as dispatched. 
     * This is an administrative function that can only be executed by the username 'admin'.
     *
     * Precondition: an order must have been placed 
     * Postcondition: the order's dispatchDate field is set to the current date and time.
     *
     * @param orderNo the order number to mark as dispatched
     * @param loggedInUsername the username of the person logged in
     * @param dbUrl the database URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param request servlet request
     * @param response servlet response
     * @throws NumberFormatException
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void markDispatched(Integer orderNo, String loggedInUsername,
            String dbUrl,
            String dbUsername,
            String dbPassword,
            HttpServletRequest request,
            HttpServletResponse response)
            throws NumberFormatException, IOException, ServletException {

        //if the action is 'admin', then find mark the specified order as dispatched
        //only the user 'admin' can do this.
        if (loggedInUsername.equals("admin") && orderNo != -1) {
            Timestamp orderDate = new Timestamp(new Date().getTime()); //record the current time

            try {
                orderEjb.MarkDispatched(dbUrl, dbUsername, dbPassword, orderNo, orderDate);
            } catch (Exception ex) {
                //allow exceptions to bubble up to the calling method, so they are displayed
                throw new ServletException(ex);
            }
        } else {
            displayError(request, response);
        }
    }

    /**
     * Sends the user to an error page.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void displayError(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher view;
        //display an error message if unable to process the request
        view = request.getRequestDispatcher("authentication/error.jsp");
        view.forward(request, response);
    }

    /**
     * Cancels an order made by a customer.
     * 
     * Preconditions: An order must be placed by a customer. The date dispatched date must be null.
     * Postconditions: The order is deleted from the database.
     * 
     * @param orderNo the order number to mark as dispatched
     * @param customerUsername the username of the person logged in
     * @param dbUrl the database URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param request servlet request
     * @param response servlet response
     * @return true if the order has been cancelled, otherwise false
     * @throws ServletException 
     */
    private Boolean cancelOrder (Integer orderNo, 
	    String customerUsername, 
	    String dbUrl, 
	    String dbUsername, 
	    String dbPassword, 
	    HttpServletRequest request) 
	    throws ServletException {
	
        //retrieve the orders session attributes - this contains all order for the customer, or all orders
	//from all customers if the admin has executed the function.
	ArrayList<Order> orders = (ArrayList<Order>) request.getSession().getAttribute("previousOrders");
        //find the order that matches the provided order number
	Order order = null;
        for (Order o : orders) {
            if (o.getOrderNo() == orderNo) {
                order = o;
            }
        }

	//try to cancel the order only if the username is the same as in the order number or the user is admin
	//also only cancel if the dispatch date is null
        Boolean result = false;
        if ((customerUsername.equals(order.getUsername()) || customerUsername.equals("admin")) 
		&& order.getDispatchDate() == null) {
            try {
                result = orderEjb.DeleteOrder(dbUrl, dbUsername, dbPassword, order);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }

        return result;

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
