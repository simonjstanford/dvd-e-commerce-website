<%-- 
    Document   : admin
    Created on : 10-Mar-2014, 10:23:48
    Author     : Simon Stanford

    The main administration page - contains links to administrative functions.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Admin</title>
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
            <h1>Admin</h1>
            <table>
                <tr>
                    <td>View Customer Orders</td>
                    <td><a href="<c:url value="/orders"><c:param name="action" value="admin"/></c:url>" class="button">Go</a></td>      
                    </tr>
                    <tr>
                        <td>Add New Film To Catalogue</td>
                        <td><a href="<c:url value="../admin/addFilm.jsp"></c:url>" class="button">Go</a></td>
                    </tr>
                    <tr>
                        <td>Edit/Delete Items From Catalogue</td>
                        <td><a href="<c:url value="../GetDvds"><c:param name="action" value="getAll"/></c:url>" class="button">Go</a></td>
                    </tr>
                </table>
                <div style="clear:both; height: 40px"></div>
            </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>