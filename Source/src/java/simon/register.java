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
@WebServlet(name = "register", urlPatterns = {"/register"})
public class register extends HttpServlet {

    @EJB
    simon.model.mysql.UserEjb userEjb;
    
    /**
     * Registers a new user onto the DVD site to allow them to purchase items.
     * 
     * Precondition:    the register.JSP form must have been completed
     *                  the username and password must be at least six characters each
     *                  all parameters except address_line2 must contain a value
     * 
     * Postcondition:   the user is registered
     *                  a confirmation page is displayed
     *                  optionally, the user is redirected to the checkout page
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

        Integer result = -2; //stores the result of the database insert

        //attempt to add the user details to the database. Record the number of rows added in 'result'
        try {
            result = userEjb.AddUser(getServletContext().getInitParameter("DbUrl"),
                                           getServletContext().getInitParameter("DbUserName"),
                                           getServletContext().getInitParameter("DbPassword"),
                                           request.getParameter("firstName"),
                                           request.getParameter("secondName"),
                                           request.getParameter("address_line1"),
                                           request.getParameter("address_line2"),
                                           request.getParameter("city"),
                                           request.getParameter("postcode"),
                                           request.getParameter("username"),
                                           request.getParameter("password"),
                                           getServletContext().getInitParameter("AesEncryptionKey"));
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }

        //read the result variable to determine the course of action
        switch (result) {
            //display a confirmation page if 1 record has been updated
            case 1:
                //if an action parameter exists, forward this onto the confirmation page
                //this is to determine if the user was trying to purchase items and got redirected
                //it allows the user to be redirected back to the checkout page automatically
                if (request.getParameter("action") != null) {
                    String  action = request.getParameter("action");
                    request.setAttribute("action", action);
                }              
                request.setAttribute("name", request.getParameter("firstName"));
                RequestDispatcher viewSuccess = 
			request.getRequestDispatcher("authentication/registrationCompleted.jsp");
                viewSuccess.forward(request, response);
                break;
            //display an error message if any other value has been returned
            default:
                request.setAttribute("error", result);
                RequestDispatcher viewError = request.getRequestDispatcher("authentication/error.jsp");
                viewError.forward(request, response);
                break;
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
