<%-- 
    Document   : orderError
    Created on : 16-Mar-2014, 02:36:07
    Author     : Simon Stanford

    Displays error messages related to the ordering of items
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Order Error</title>
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
            <%--The message changes dependent on the error code sent to the page. --%>
            <c:choose>
                <c:when test="${error == 'authenticationError'}">
                    The password you entered was incorrect. Please try again.
                </c:when>
                <c:when test="${error == 'cardError'}">
                    There was a problem with the card details that you entered. Please try again.
                </c:when>
		<c:when test="${error == 'stockError'}">
                    Unfortunately we do not have enough stock for one of the items in your basket.  Please check and try again.
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