package simon;

import simon.entities.DVD;
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
@WebServlet(name = "GetDvds", urlPatterns = {"/GetDvds"})
public class GetDvds extends HttpServlet {

    @EJB
    simon.model.mysql.FetchEjb fetchEjb;
    
    /**
     * Finds DVD information in the MySQL database. An argument is passed via a request parameter that
     * specifies what information is required: all films, all films in a certain genre or an individual film.
     * The user is then forwarded to the appropriate JSP.
     *
     * The action to retrieve all films is an administrative function that only the user called 'admin' can
     * complete. This is because the servlet then forwards the user onto an admin page to add, ammend or
     * delete films.
     *
     * Precondition:    none 
     * Postcondition:   a list of films are returned and displayed to the user on the appropriate JSP.
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

        //read the actions the user would like to complete, sent as request parameters.
        String action = "unknown"; 
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }

        //execute the action required, determined by a request parameter provided by the user
        switch (action) {
            case "filterByGenre": //retrieve all films of a certain genre
                //only execute if a genre parameter has been passed
                String genre = null;
                //read the genre parameter, if it exists
                if (request.getParameter("genre") != null) {
                    genre = request.getParameter("genre");
                    //search the database for that genre
                    filterByGenre(dbUrl, dbUsername, dbPassword, genre, request, response);
                } else { //otherwise, display an error
                    displayError(response, request);
                }
                break;
            case "getAll": //retrieve all films - an administrative function that only the admin can do
                //only execute if a genre parameter has been passed...
                String username = null;
                /*
                read the username attribute, if it exists. THE USER MUST BE ADMIN.
                it is secure because the attribute is retrieved from the session, so cannot be changed by
                the user directly
                */
                if (request.getSession().getAttribute("username") != null) {
                    username = request.getSession().getAttribute("username").toString();
                    //retrieve all films in the database
                    getAllDvds(username, dbUrl, dbUsername, dbPassword, request, response);
                } else { //otherwise, display an error
                    displayError(response, request);
                }
                break;
            case "getTitle": //get an individual title
                //only execute if a filmId parameter has been passed
                Integer filmId = null;
                //read the id parameter, if it exists
                if (request.getParameter("id") != null) {
                    //search the database for that film ID
                    filmId = Integer.parseInt(request.getParameter("id"));
                    getTitle(dbUrl, dbUsername, dbPassword, filmId, request, response);
                } else { //otherwise, display an error
                    displayError(response, request);
                }
                break;
            default: //display an error if any other action is passed (including null)
                displayError(response, request);
                break;
        }
    }

    /**
     * Searches the MySQL database and returns all DVDs of a certain genre. The user is then redirected to a
     * JSP to display this information.
     *
     * Precondition:    no DVDs are displayed.
     * Postcondition:   film(s) are displayed to the user on the appropriate JSP.
     *
     * @param dbUrl the database location URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param genre the genre the user would like to view
     * @param request servlet request object
     * @param response servlet response object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void filterByGenre(String dbUrl,
            String dbUsername,
            String dbPassword,
            String genre,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher view = null; //the object used to forward the request
        ArrayList<DVD> allDvds = null; //the list of all DVDs found with a certain genre

        try {
            //search the MySQL database
            allDvds = fetchEjb.GetFilmsOfGenre(dbUrl, dbUsername, dbPassword, genre);
        } catch (Exception ex) {
            //if an exception is caught, bubble up to display the error
            throw new ServletException(ex);
        }
        
        //save the results as an attribute - the genres.jsp file will read these results
        request.setAttribute("allDvds", allDvds);
        request.setAttribute("genre", genre); //this is used to display the genre name as a title
        
        //forward the results to a JSP to display the info
        view = request.getRequestDispatcher("genres.jsp");
        view.forward(request, response);
    }

    /**
     * Searches the MySQL database and returns all DVDs in the database. The user is then redirected to a
     * JSP to display this information.  This is an administrator function that is restricted to the user
     * 'admin' as on completion the user is directed to a page to add/edit/delete the DVDs.
     *
     * Precondition:    no DVDs are displayed.
     * Postcondition:   film(s) are displayed to the user on the appropriate JSP.
     * 
     * @param dbUrl the database location URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param username the username of the person logged in
     * @param request servlet request object
     * @param response servlet response object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void getAllDvds(String username,
            String dbUrl,
            String dbUsername,
            String dbPassword,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher view = null;
        ArrayList<DVD> allDvds = null;

        if (username.equals("admin")) {
            try {
                allDvds = fetchEjb.GetAllFilms(dbUrl, dbUsername, dbPassword);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }

            request.setAttribute("allDvds", allDvds);

            view = request.getRequestDispatcher("admin/updateCatalogue.jsp");
            view.forward(request, response);
        }
    }

    /**
     * Searches the MySQL database for information on an individual film.  The user is then redirected to a
     * JSP to display the information.
     * 
     * Precondition:    no DVDs are displayed.
     * Postcondition:   info for a film is displayed to the user on the appropriate JSP.
     * 
     * @param dbUrl the database location URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param filmId
     * @param request servlet request object
     * @param response servlet response object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void getTitle(String dbUrl,
            String dbUsername,
            String dbPassword,
            Integer filmId,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        RequestDispatcher view = null;
        ArrayList<DVD> randomDvds = null;
        DVD dvd = null;

        try {
            dvd = fetchEjb.GetSingleDvd(dbUrl, dbUsername, dbPassword, filmId);
            randomDvds = fetchEjb.GetFiveRandomFilms(dbUrl, dbUsername, dbPassword, dvd.getGenre());
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
        request.setAttribute("name", request.getParameter("name"));
        request.setAttribute("dvd", dvd);
        request.setAttribute("randomDvds", randomDvds);
        view = request.getRequestDispatcher("getTitle.jsp");
        view.forward(request, response);
    }

    /**
     * Redirects the user to an error page that reports that an unknown error has occurred.
     * 
     * Precondition:    no suitable argument has been provided
     * Postcondition:   an error message is displayed to the user
     * 
     * @param request servlet request object
     * @param response servlet response object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void displayError(HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("/authentication/error.jsp?error");
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
