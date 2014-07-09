<%-- 
    Document   : UpdateCatelogue
    Created on : 10-Mar-2014, 10:44:32
    Author     : Simon Stanford

    An administrative function that displays a list of all films in the database and provides links to edit or delete them.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Admin | Update Catalogue</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--This is an administrative function.  Only allow the username 'admin' to visit this page.
            Check the session object to see if the username is 'admin', if not redirect the user to an error page.--%>
        <c:if test="${sessionScope.username != 'admin'}" >
            <jsp:forward page="/authentication/error.jsp">
                <jsp:param name="error" value="unathorised"/>
            </jsp:forward>
        </c:if>

        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>Update Catalogue</h1>

            <%--If the user has returned to this page after making an amendment, display the number of changes made--%>
            <c:if test="${result > 0 }" >
                <c:out value='${result}'/> film(s) amended.
            </c:if>

            <table>
                <th>Image</th>
                <th>Film</th>
                <th>Genre</th>
                <th>Price</th>
                <th>Stock</th>
                    <%--The servlet that forwarded to this JSP provided a list of all films in the database.  
                    Iterate through these dvds and display them in a table.--%>
                    <c:forEach var="dvd" items="${allDvds}">
                    <tr>
                        <td><img src="<c:out value='${dvd.imageLink}'/>" height="100"></td>
                        <td><c:out value='${dvd.name}'/></td>
                        <td><c:out value='${dvd.genre}'/></td>
                        <td><c:out value='${dvd.price}'/></td>
                        <td><c:out value='${dvd.stock}'/></td>
                        <%--This is the button to edit a film.  Details of the film are sent to the editFilm JSP as parameters--%>
                        <td>
                            <a href="
                               <c:url value="/admin/editFilm.jsp">
                                   <c:param name="id" value="${dvd.id}"/>
                                   <c:param name="name" value="${dvd.name}"/>
                                   <c:param name="genre" value="${dvd.genre}"/>
                                   <c:param name="price" value="${dvd.price}"/>
                                   <c:param name="imageLink" value="${dvd.imageLink}"/>
                                   <c:param name="description" value="${dvd.description}"/>
                                   <c:param name="stock" value="${dvd.stock}"/>
                                   <c:param name="action" value="edit"/>
                               </c:url>" class="button">
                                Edit
                            </a>
                        </td>
                        <%--This is the button to delete the film--%>
                        <td>
                            <a href="
                               <c:url value="/admin/AmmendDatabase">
                                   <%--The film ID is sent to a servlet as a parameter to process--%>
                                   <c:param name="id" value="${dvd.id}"/>
                                   <%--This parameter tells the servlet what action to take--%>
                                   <c:param name="action" value="delete"/> 
                               </c:url>" class="button">
                                Delete
                            </a>
                        </td>
                    </tr>                            
                </c:forEach>  
            </table>
            <div style="clear:both; height: 40px"></div>
        </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>