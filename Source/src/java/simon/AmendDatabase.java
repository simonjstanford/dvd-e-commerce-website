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
@WebServlet(name = "AmmendDatabase", urlPatterns = {"/admin/AmmendDatabase"})
public class AmendDatabase extends HttpServlet {

    @EJB
    simon.model.mysql.AdminEjb adminEjb;

    /**
     * An administrator function that allows the user 'admin' to add, remove and update items in the MySQL database.
     *
     * Precondition: the logged in user must be 'admin' Postcondition: the database is amended
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

        int rowsChanged = -1; //used to track the number of rows changed in the database to display to the user

        //Retrieve the database connection info and encryption key from the deployment descriptor
        String dbUrl = getServletContext().getInitParameter("DbUrl");
        String dbUsername = getServletContext().getInitParameter("DbUserName");
        String dbPassword = getServletContext().getInitParameter("DbPassword");

        //read the username stored in the session
        String customerUsername = "unknown";
        if (request.getSession().getAttribute("username") != null) {
            customerUsername = request.getSession().getAttribute("username").toString();
        }

        //read the action passed as a paramater by the user
        String action = "unknown";
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }

        //as this is an administrator function, only execute appropriate code if the logged in user is 'admin'
        if (customerUsername.equals("admin")) {
            //take appropriate action
            switch (action) {
                case "delete":
                    //delete the film - this also deletes the stock from the stock table
                    rowsChanged = deleteFilm(dbUrl,
                            dbUsername,
                            dbPassword,
                            Integer.parseInt(request.getParameter("id")));

                    //display the changes
                    forwardRequest(request, rowsChanged, response);
                    break;
                case "addFilm":
                    //add the film - this also adds the stock from the stock table
                    rowsChanged = addFilm(dbUrl, dbUsername, dbPassword, request.getParameter("name"),
                            request.getParameter("genre"),
                            request.getParameter("price"),
                            request.getParameter("imageLink"),
                            request.getParameter("description"),
                            Integer.parseInt(request.getParameter("stock")));

                    //display the changes
                    forwardRequest(request, rowsChanged, response);
                    break;
                case "updateFilm":
                    //update the film details - this also updates the stock from the stock table
                    rowsChanged = updateFilm(dbUrl, dbUsername, dbPassword,
                            Integer.parseInt(request.getParameter("id")),
                            request.getParameter("newName"),
                            request.getParameter("genre"),
                            request.getParameter("price"),
                            request.getParameter("imageLink"),
                            request.getParameter("description"),
                            Integer.parseInt(request.getParameter("stock")));

                    //display the changes
                    forwardRequest(request, rowsChanged, response);
                    break;
                default:
                    //display an error if the action is unknown
                    displayError(response, request);
                    break;
            }
        } else { //display an error message if the user is not 'admin'
            displayError(response, request);
        }
    }

    /**
     * Deletes a film from the MySQL database.
     *
     * Precondition: a film must exist in the database. Postcondition: the size of the film database is -1.
     *
     * @param filmId the ID of the film to delete
     * @param dbUrl the database URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private int deleteFilm(String dbUrl,
            String dbUsername,
            String dbPassword,
            Integer filmId)
            throws ServletException, IOException {

        Integer result = -2;
        try {
            //delete the data out of the stock table
            adminEjb.DeleteStock(dbUrl, dbUsername, dbPassword, filmId);
            
            //delete the film from the dvd table
            result = adminEjb.DeleteTitle(dbUrl, dbUsername, dbPassword, filmId);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }
        return result;
    }

    /**
     * Adds a film to the MySQL database.
     *
     * Precondition: none. Postcondition the size of the film database is +1
     *
     * @param dbUrl the database URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param name the name of the film to add
     * @param genre the genre of the film to add
     * @param price the price of the film to add
     * @param imageLink the image link for the film to add
     * @param description the description of the film to add
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private int addFilm(String dbUrl,
            String dbUsername,
            String dbPassword,
            String name,
            String genre,
            String price,
            String imageLink,
            String description,
            int stock)
            throws ServletException, IOException {

        Integer result = -2;

        try {
            //add the film to the film table
            result = adminEjb.InsertTitle(dbUrl,
                    dbUsername,
                    dbPassword,
                    name, genre, price, imageLink, description);

            //fetch the new ID number
            int filmId = adminEjb.GetFilmID(dbUrl, dbUsername, dbPassword, name, genre, price, imageLink, description);

            //using the id number, update the stock levels
            result = adminEjb.InsertStock(dbUrl, dbUsername, dbPassword, filmId, stock);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }

        return result;
    }

    /**
     * Amends the information kept for a film in the MySQL database.
     *
     * Precondition: a film must exist in the database. Postcondition film information is altered
     *
     * @param dbUrl the database URL
     * @param dbUsername the database username
     * @param dbPassword the database password
     * @param id the ID of the film to update
     * @param newName the amended film name
     * @param genre the amended film genre
     * @param price the amended film price
     * @param imageLink the amended film imageLink
     * @param description the amended film description
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private int updateFilm(String dbUrl,
            String dbUsername,
            String dbPassword,
            int filmId,
            String newName,
            String genre,
            String price,
            String imageLink,
            String description,
            int stock)
            throws ServletException, IOException {

        Integer result = -2;
        try {
            //update the film information
            result = adminEjb.UpdateTitle(dbUrl,
                    dbUsername,
                    dbPassword,
                    filmId, newName, genre, price, imageLink, description);
            
            //update the stock levels
            adminEjb.UpdateStock(dbUrl, dbUsername, dbPassword, filmId, stock);
        } catch (Exception ex) {
            //allow exceptions to bubble up to the calling method, so they are displayed to the user
            throw new ServletException(ex);
        }
        return result;
    }

    /**
     * Displays an error message to the user
     *
     * Precondition: none. Postcondition: an error message is displayed
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void displayError(HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException {

        RequestDispatcher view = request.getRequestDispatcher("/authentication/error.jsp?error");
        view.forward(request, response);
    }

    /**
     * Forwards the user to a JSP to view all DVDs in the database.
     *
     * Precondition: DVDs must exist in the database Postcondition: DVDs are displayed
     *
     * @param request servlet request
     * @param result
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void forwardRequest(HttpServletRequest request, Integer result, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher view;
        request.setAttribute("result", result);
        view = request.getRequestDispatcher("/GetDvds?action=getAll");
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
