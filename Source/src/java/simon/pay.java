package simon;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import simon.entities.Order;
import simon.model.Security;
import simon.model.iSecurity;
/**
 * @author Simon Stanford
 */
@WebServlet(name = "pay", urlPatterns = {"/pay"})
public class pay extends HttpServlet {

    @EJB
    simon.model.mysql.OrderEjb orderEjb;

    @EJB
    simon.model.mysql.UserEjb userEjb;
    
    /**
     * Processes a purchase. Will only complete if the username/password combination and credit card number
     * is verified.
     * 
     * Precondition:    at least one item is in the basket
     *                  the user has logged in
     *                  the user supplies a matching password for the username
     *                  the user supplies a credit card number
     * 
     * Postcondition:   the order is placed
     *                  the database is updated with the order
     *                  the user is directed to a confirmation page
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

        String customerUsername = null; //the username calling the action
        String customerPassword = null; //the password provided by the user
        String cardNo = null; //the credit card number provided by the user

        //read the username stored in the session
        if (request.getSession().getAttribute("username") != null) {
            customerUsername = request.getSession().getAttribute("username").toString();
        }

        //read the password sent by the customer
        if (request.getParameter("password") != null) {
            customerPassword = request.getParameter("password");
        }

        //read the card number sent by the customer
        if (request.getParameter("cardNo") != null) {
            cardNo = request.getParameter("cardNo");
        }

        //verify the supplied password against the username stored as a session attribute
        Boolean passwordVerified = false;
        try {
            passwordVerified = userEjb.CheckPassword(dbUrl,
                    dbUsername,
                    dbPassword,
                    customerUsername,
                    customerPassword);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }

        //only continue if the username/password has been verified
        if (passwordVerified) {
            iSecurity security = new Security();
            
            //verify the credit card number - only continue if it has been verified
            if (security.ValidateCreditCard(cardNo)) {
                Boolean orderCreated = false;
                Timestamp orderDate = new Timestamp(new Date().getTime()); //record the current time

                //retrieve the order object stored as a session attribute
                //store the current time as the order date
                Order order = (Order) request.getSession().getAttribute("order");
                order.setOrderDate(orderDate);

                //store the order in the MySQL database
                try {
                    orderCreated = orderEjb.CreateOrder(dbUrl,
                            dbUsername,
                            dbPassword,
                            customerUsername,
                            order);
                } catch (Exception ex) {
                    //allow exceptions to bubble up to the calling method, so they are displayed to the user
                    throw new ServletException(ex);
                }
                
                //display a completion message to the user if the database insertion was successfull
                if (orderCreated) {
                    request.getSession().setAttribute("order", new Order());
                    RequestDispatcher view = request.getRequestDispatcher("/user/orderCompleted.jsp");
                    view.forward(request, response);
                } else {
                    //display an error message if there was a problem adding the order to the database
                    request.setAttribute("error", "stockError");
                    RequestDispatcher view = request.getRequestDispatcher("/user/orderError.jsp");
                    view.forward(request, response);
                }
            } else {
                //display an error message if the credit card number is not verified
                request.setAttribute("error", "cardError");
                RequestDispatcher view = request.getRequestDispatcher("/user/orderError.jsp");
                view.forward(request, response);
            }
        } else {
            //display an error message if the username/password combination is not authenticated
            request.setAttribute("error", "authenticationError");
            RequestDispatcher view = request.getRequestDispatcher("/user/orderError.jsp");
            view.forward(request, response);
        }

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
