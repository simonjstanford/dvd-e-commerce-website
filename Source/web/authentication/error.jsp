<%-- 
    Document   : registrationCompleted
    Created on : 14-Mar-2014, 13:02:51
    Author     : Simon Stanford

    Displays an error page to the user.  The message changes dependent on the error code sent to the page.  The errors relate to authentication.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Error</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>Error :-(</h1>
            <c:choose>
                <c:when test="${error == -10}">
                    The username you have chosen is already in use.  Please return to the registration page and use a different username.
                </c:when>
                <c:when test="${error == -9}">
                    The registration form was incomplete.  Please return to the registration page and complete all fields.
                </c:when>
                <c:when test="${error == -8}">
                    Make sure that both your username and password are at least six characters long.  Please return to the registration page and try again.
                </c:when>
                <c:when test="${param.error == 'unathorised'}">
                    You do not have access to this page.
                </c:when>
                <c:otherwise>
                    An unknown error has occurred.  Please try again.
                </c:otherwise>
            </c:choose>
            <br>
            <br>
            <br>
        </div>
	<%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
	<jsp:include page="/footer.jsp"/>
    </body>
</html>