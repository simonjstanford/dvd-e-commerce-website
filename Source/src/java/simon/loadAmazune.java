package simon;

import simon.entities.DVD;
import simon.entities.Order;
import java.io.IOException;
import java.util.ArrayList;
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
@WebServlet(name = "loadAmazune", urlPatterns = {"/loadAmazune"})
public class loadAmazune extends HttpServlet {

    @EJB
    simon.model.mysql.FetchEjb fetchEjb;
    
    /**
     * Loads all necessary information to display the homepage and navigation bar to the user and instantiates
     * all necessary objects. Retrieves all genres and information on five random films held within the MySQL 
     * database.  Forwards the user to a JSP to display the results. 
     * 
     * Precondition:    none
     * Postcondition:   the homepage is shown.
     *                  The Order object is initialised
     *                  A list of genres is stored as a session attribute
     *                  A list of five random films is stored as a session attribute
     *                  The user is redirected to another JSP.
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

        /*
        Retrieve a list of all genres and five random films from the MySQL database.
        The list of genres will be used to dynamically created the dropdown 'genres' menu in the header, and
        the random DVDs will be displayed as quick links for the user to click on.
        */
        ArrayList<DVD> randomDvds = null;
        ArrayList<String> genres = null;
        try {
            genres = fetchEjb.GetGenres(dbUrl, dbUsername, dbPassword);
            randomDvds = fetchEjb.GetFiveRandomFilms(dbUrl, dbUsername, dbPassword, null);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }

        //store the random DVDs as a request attribute as it does not need to persist past one page
        request.setAttribute("randomDvds", randomDvds);
        
        //store the list of genres as a session attribute, as it will not change for the life of the session
        request.getSession().setAttribute("genres", genres);

        /*
        create an order object if an one has not already been created.  This is the object that acts as the
        basket for the user.  The condition check occurs because this servlet is called every time the user
        visits the homepage, thus displaying five different random films each time - the code should not 
        remove all items out of the basket on each visit to the homepage.
        */
        if (request.getSession().getAttribute("order") == null) {
            request.getSession().setAttribute("order", new Order());
        }

        //forward the user onto the homepage to homepage JSP to display the results
        RequestDispatcher view = request.getRequestDispatcher("amazune.jsp");
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
