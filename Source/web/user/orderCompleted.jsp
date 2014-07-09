<%-- 
    Document   : orderCompleted
    Created on : 16-Mar-2014, 02:35:15
    Author     : Simon Stanford

    Displays a confirmation page to the user after successfully completing a purchase.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Order Complete</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>Thank you, <c:out value='${firstName}'/></h1>
            <%--The link to the orders servlet includes a parameter that instructs the servlet to retrieve all orders for the user logged in--%>
            Your order is now complete.  To track your order's progress please visit the 
            <a href="
               <c:url value="/orders">
                   <c:param name="action" value="user"/>
               </c:url>">
                orders page
            </a>.
            <br>
            <br>
            <br>
        </div>
	<%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
	<jsp:include page="/footer.jsp"/>
    </body>
</html>
