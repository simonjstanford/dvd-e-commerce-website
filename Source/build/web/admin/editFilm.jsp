<%-- 
    Document   : editFilm
    Created on : 10-Mar-2014, 13:58:03
    Author     : Simon Stanford

    An administrative function that enables an administrator to edit film information, including stock quantities.
--%>


<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Admin | Edit "<c:out value='${param.name}'/>"</title>
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

        <%--The updateCatalogue JSP sends this page DVD information via parameters. 
        These are used in the heading and to pre-populate textboxes --%>
        <div id="container">
            <h1>Edit <i><c:out value='${param.name}'/></i></h1>

            <FORM ACTION="../admin/AmmendDatabase" METHOD="POST">
                Film Name: <input type="text" name="newName" value="<c:out value='${param.name}'/>"><br>
                Image Link: <input type="text" name="imageLink" value="<c:out value='${param.imageLink}'/>"><br>
                Genre:<br>
                <%--The select list of genres is built by iterative through the list of genres generated at startup.
                If the genre name is equal to the genre parameter sent from the updateCatalogue JSP then it will be the default selection.--%>
                <select name="genre">
                    <c:forEach var="genre" items="${genres}">
                        <c:choose>
                            <c:when test="${genre == param.genre}">
                                <option value="<c:out value='${genre}'/>" selected><c:out value='${genre}'/></option>
                            </c:when>               
                            <c:otherwise>
                                <option value="<c:out value='${genre}'/>"><c:out value='${genre}'/></option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select><br>
                Price: <input type="text" name="price" value="<c:out value='${param.price}'/>"><br>
                Stock: <input type="text" name="stock" value="<c:out value='${param.stock}'/>"><br>
                Description: <textarea cols="40" rows="5" name="description"><c:out value='${param.description}'/></textarea><br>

                <%--the film id is also sent to the servlet to identify the film in the database--%>
                <input type="hidden" value="<c:out value='${param.id}'/>" name="id"> 
                <%--this field tells the servlet what action to take--%>
                <input type="hidden" value="updateFilm" name="action"> 
                <input type="submit" value="Submit">
            </FORM>
            <div style="clear:both; height: 40px"></div>
        </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html> 