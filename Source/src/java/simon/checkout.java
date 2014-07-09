package simon;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import simon.entities.User;

/**
 *
 * @author Simon Stanford
 */
@WebServlet(name = "checkout", urlPatterns = {"/checkout"})
public class checkout extends HttpServlet {

    @EJB
    simon.model.mysql.UserEjb userEjb;
    
    /**
     * Displays the checkout page to enable the user to confirm purchase of items.
     * 
     * Precondition:    the user must have registered and logged on.  If not, the user is redirected
     *                  the user must have added items to the basket. If not, no items will be available for
     *                  purchase.
     * Postcondition:   A summary of the purchase is displayed to the user.
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
        
        User user = null; //this will hold the User object for the customer making the purchase
        String loggedInUser = null; //this will hold the name of the user logged in to the session

        //only process the checkout if the user is logged in
        if (request.getSession().getAttribute("username") != null) {
            //retrieve the name of the user logged in from the session data
            loggedInUser = request.getSession().getAttribute("username").toString();
            
            //retrieve the encryption key from the deployment descriptor.  This will decode the customer
            //information held in the MySQL database
            String encryptionKey = getServletContext().getInitParameter("AesEncryptionKey");
            
            //retrieve the user details from the MySQL database using the variables above
            try {
                user = userEjb.GetUser(dbUrl, dbUsername, dbPassword, loggedInUser, encryptionKey);
            } catch (Exception ex) {
                //allow exceptions to bubble up to the calling method, so they are displayed to the user
                throw new ServletException(ex);
            }
            
            //save the newly created User object to the session
            request.getSession().setAttribute("user", user);
            
            //forward the user to the checkout page to confirm the purchase
            RequestDispatcher viewSuccess = request.getRequestDispatcher("/user/checkout.jsp");
            viewSuccess.forward(request, response);
        //if the user is not logged in, send them to the logon screen
        } else {
            //send a parameter to the logon page that indicates that it should send the user to the basket
            //page after logging in, so they can perform checkout
            request.setAttribute("action", "checkout");
            
            //forward the user to the logon page
            RequestDispatcher view = request.getRequestDispatcher("/authentication/logon.jsp");
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
