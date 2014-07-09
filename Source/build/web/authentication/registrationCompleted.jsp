<%--
    Document   : registrationCompleted
    Created on : 14-Mar-2014, 13:02:51
    Author     : Simon Stanford

    A confirmation page that notifies the user that they have successfully registered.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Registration Complete</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>Thank you, <c:out value='${name}'/></h1>
            <%--
            See logon.jsp for an explaination - if the user tried to purchase items before registering, the link is changed to ensure the user is
            brought back to the checkout JSP after registering and logging on.
            --%>
            <c:choose>
                <c:when test="${action == 'checkout'}">
                    Registration is now complete.  Please now
                    <a href="
                       <c:url value="/authentication/logon.jsp">
                           <c:param name="action" value="checkout" />
                       </c:url>">
			log in
                    </a>
		    to complete your order.
                </c:when>
                <c:otherwise>
                    Registration is now complete.  Please now <a href="<c:out value='${pageContext.request.contextPath}'/>/authentication/logon.jsp">log in</a>.
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