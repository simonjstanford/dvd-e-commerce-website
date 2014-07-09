package simon;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Simon Stanford
 */
@WebServlet(name = "logon", urlPatterns = {"/logon"})
public class logon extends HttpServlet {

    @EJB
    simon.model.mysql.UserEjb userEjb;
    
    /**
     * Authenticates a user.  Confirms a username and password combination provided by the user with
     * records in the MySQL database.
     * 
     * Precondition:    the user has provided a username/password combination in the logon.jsp file
     * Postcondition:   The user is authenticated.  
     *                  Username and first name variables are stored in the session
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

        //read the customer username and password sent by the user
        String customerUsername = request.getParameter("username");
        String customerPassword = request.getParameter("password");

        //Retrieve the database connection info from the deployment descriptor
        String dbUrl = getServletContext().getInitParameter("DbUrl");
        String dbUsername = getServletContext().getInitParameter("DbUserName");
        String dbPassword = getServletContext().getInitParameter("DbPassword");

        //check the username/password that the user has provided.  
        //If authenticated, the CheckPassword() function will return true.
        Boolean verified = false;
        try {
            verified = userEjb.CheckPassword(dbUrl,
                    dbUsername,
                    dbPassword,
                    customerUsername,
                    customerPassword);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }

        //only continue if the username/password combination has been authenticated.
        if (verified) {
            //retrieve the encryption key from the deployment descriptor
            String encryptionKey = getServletContext().getInitParameter("AesEncryptionKey");
            
            //retrieve the customers first name using the username and the encryption key.
            //this is used in the header to display their name
            String customerFirstName = null;  
            try {
                customerFirstName = userEjb.GetFirstName(dbUrl,
                        dbUsername,
                        dbPassword,
                        customerUsername,
                        encryptionKey);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }

            //store the username and customer's first name.  The username value is used to determine
            //the user logged in, and allows access to the administrator functions if this is 'admin'
            request.getSession().setAttribute("username", customerUsername);
            request.getSession().setAttribute("firstName", customerFirstName);

            //return the user to the checkout page if they were redirected whilst trying to purchase items
            if (request.getParameter("action") != null) {
                String action = action = request.getParameter("action");
                switch (action) {
                    case "checkout":
                        RequestDispatcher view = request.getRequestDispatcher("/checkout");
                        view.forward(request, response);
                        break;
                }
            } else {
                //if not redirect, just sent the user to the homepage
                RequestDispatcher view = request.getRequestDispatcher("/loadAmazune");
                view.forward(request, response);
            }
        } else {
            //display an error message if the username/password combination is not authenticated
            request.setAttribute("error", true);
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
