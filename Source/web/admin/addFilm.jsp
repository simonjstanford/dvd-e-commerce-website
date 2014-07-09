<%-- 
    Document   : addFilm
    Created on : 10-Mar-2014, 13:11:36
    Author     : Simon Stanford

    Allows an administrator to add a new film to the database.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Admin | Add Film To Catalogue</title>
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
            <h1>Add Film To Catalogue</h1>

            <%--Enter the details of a new film and submit them to a servlet--%>
            <FORM ACTION="../admin/AmmendDatabase" METHOD="POST">
                Film Name: <input type="text" name="name"><br>
                Image Link: <input type="text" name="imageLink"><br>
                Genre:<br>
                <select name="genre">
                    <option value="Action & Adventure">Action & Adventure</option>
                    <option value="Comedy">Comedy</option>
                    <option value="Crime & Thriller">Crime & Thriller</option>
                    <option value="Drama">Drama</option>
                    <option value="Horror">Horror</option>
                    <option value="SciFi">SciFi</option>
                </select><br>
                Price: <input type="text" name="price"><br>
                Stock: <input type="text" name="stock"><br>
                Description: <textarea cols="40" rows="5" name="description"></textarea><br>
                <input type="hidden" value="addFilm" name="action"> <%--this field tells the servlet what action to take--%>
                <input type="submit" value="Submit">
            </FORM>
            <div style="clear:both; height: 40px"></div>
        </div>
                
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html> 