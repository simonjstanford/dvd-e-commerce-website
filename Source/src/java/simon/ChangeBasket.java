package simon;

import simon.entities.*;
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
@WebServlet(name = "ChangeBasket", urlPatterns = {"/ChangeBasket"})
public class ChangeBasket extends HttpServlet {

    @EJB
    simon.model.mysql.FetchEjb fetchEjb;
    
    /**
     * Adds and removes items from the basket.
     * Works by: 
     * - Connecting to the MySQL database and retrieving the DVD using it's ID which is passed as a parameter. 
     * - Retrieves the DVDs in the basket by accessing an @type{Order} object that is stored in the @type
     *   {Session} object.
     * - Checks the appropriate action to take (using another parameter) and then adds/removes the item from 
     *   the basket as appropriate.
     * 
     * Precondition:    An @type{Order} object must exist within the @type{Session} object.
     * Postcondition:   An object of type DVD has been added or removed from the Order 
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
        //Retrieve the Order object from the current session.  This acts as the basket.
        Order order = (Order) request.getSession().getAttribute("order");
        
        //Retrieve the database connection info from the deployment descriptor
        String dbUrl = getServletContext().getInitParameter("DbUrl");
        String dbUsername = getServletContext().getInitParameter("DbUserName");
        String dbPassword = getServletContext().getInitParameter("DbPassword");
        
        //read the actions the user would like to complete, sent as request parameters.
        String action = request.getParameter("action");
        Integer dvdId = Integer.parseInt(request.getParameter("id"));

        if (order != null) { //only try to add/remove the DVD if an existing Order object can be found.
            DVD dvd = null;
            
            //Fetch the DVD info for the database using the DVD ID passed in the request parameter.
            try {
                dvd = fetchEjb.GetSingleDvd(dbUrl, dbUsername, dbPassword, dvdId);
            } catch (Exception ex) {
                throw new ServletException(ex); //Bubble up the error if an exception is thrown
            }

            if (dvd != null) { //only try to add/remove the DVD if info in the database has been found
                //execute the action required determined by a request parameter provided by the user
                switch (action) {
                    case "add":
                        order.AddItem(dvd);
                        break;
                    case "remove":
                        order.RemoveItem(dvd);
                        break;
                }
                
                //re-save the ammended Order object back into the session
                request.getSession().setAttribute("order", order);
                
                //forward the request to the basket.jsp page, which will display the ammendment.
                RequestDispatcher view = request.getRequestDispatcher("user/basket.jsp");
                view.forward(request, response);
            }    
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
