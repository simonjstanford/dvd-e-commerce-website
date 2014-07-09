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
 * @author Simon Stanford
 */
@WebServlet(name = "userdetails", urlPatterns = {"/userdetails"})
public class userdetails extends HttpServlet {

    @EJB
    simon.model.mysql.UserEjb userEjb;
    
    /**
     * Retrieves user details such as name and address.
     *
     * Precondition:    the user must have registered 
     *                  the user must be logged in 
     * Postcondition:   a User object is stored as a request attribute 
     *                  the user is forwarded to a JSP to display the information
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

        //Retrieve the database connection info and encryption key from the deployment descriptor
        String dbUrl = getServletContext().getInitParameter("DbUrl");
        String dbUsername = getServletContext().getInitParameter("DbUserName");
        String dbPassword = getServletContext().getInitParameter("DbPassword");
        String aesEncryptionKey = getServletContext().getInitParameter("AesEncryptionKey");

        RequestDispatcher view = null; //object used to forward the request
        User user = null; //stores the user information
        String action = "unknown";
        String loggedInUser = null;

        //read the username currently logged in, stored as a session attribute
        if (request.getSession().getAttribute("username") != null) {
            loggedInUser = request.getSession().getAttribute("username").toString();
        }

        //read the action passed as a paramater by the user
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }

        //take the appropriate acction
        switch (action) {
            //get the details for the user currently logged in
            case "thisUser":
                try {
                    user = userEjb.GetUser(dbUrl,
                            dbUsername,
                            dbPassword,
                            loggedInUser,
                            aesEncryptionKey);
                } catch (Exception ex) {
                    //allow exceptions to bubble up to the calling method, so they are displayed to the user
                    throw new ServletException(ex);
                }
                break;
            //get the details for a different user
            //this is an administrative function that only the user 'admin' can do
            case "otherUser":
                if (loggedInUser.equals("admin")) { //make sure that the logged in user is 'admin'
                    //read the username those details are needed
                    String customerUsername = null;
                    if (request.getParameter("username") != null) {
                        customerUsername = request.getParameter("username");
                    }

                    //retrieve the user details from the database
                    try {
                        user = userEjb.GetUser(dbUrl,
                                dbUsername,
                                dbPassword,
                                customerUsername,
                                aesEncryptionKey);
                    } catch (Exception ex) {
                        //allow exceptions to bubble up to the calling method, so they are displayed
                        throw new ServletException(ex);
                    }
                } else { //show an error message if the logged in user is not 'admin'
                    view = request.getRequestDispatcher("/authentication/error.jsp?error=unathorised");
                    view.forward(request, response);
                }
                break;
            //show an error message if the action is unknown
            default:
                view = request.getRequestDispatcher("/authentication/error.jsp?error=unathorised");
                view.forward(request, response);
        }

        //save the info retrieved from the database as a request attribute
        //forward the user to a JSP to display.
        request.setAttribute("user", user);
        view = request.getRequestDispatcher("/user/user.jsp");
        view.forward(request, response);
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
