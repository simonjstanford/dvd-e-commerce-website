<%-- 
    Document   : logon
    Created on : 12-Mar-2014, 18:52:05
    Author     : Simon Stanford

    Allows the user to log onto the site with their username and password to purchase items.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Login</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <%--Display a message if the user is returning to this page after providing an incorrect username/password combination.--%>
            <c:choose>
                <c:when test="${error == false || error == null}">
                    <h2>Hello, please log in:</h2>
                </c:when>
                <c:otherwise>
                    <h2>The username or password is incorrect. Please try again:</h2>
                </c:otherwise>
            </c:choose>

            <form action="<c:out value='${pageContext.request.contextPath}'/>/logon" method=POST>
                <label>Please Enter Your User Name: </label>
                <input type="text" name="username" size="25">
                <label>Please Enter Your Password: </label>
                <input type="password" size="25" name="password">
                <br><br>
                <%--
                    The code below is just for efficiency.  The action parameter value 'checkout' is sent to this JSP from the checkout servlet 
                    when a user presses the buy button in the basket JSP and they are not logged in. The code below check if this parameter exists, 
                    and if so sends it with the user data to the logon servlet.  When the logon servlet reads the value, it knows to forward the 
                    user back to the checkout JSP instead of the homepage after a successful logon.
                --%>
                <c:if test="${action == 'checkout' || param.action == 'checkout'}" >
                    <input type="hidden" name="action" value="checkout">
                </c:if>
                <input type="submit" value="Submit">
            </form>

            <%--
                This code is similar to above, except that if the user tries to purchase items without regeristering first then the action parameter
                value 'checkout' is sent to the register JSP.  This means the user is brought back to the checkout JSP instead of the homepage after 
                successfully logging on.
            --%>
            <c:choose>
                <c:when test="${action == 'checkout'}">
                    Not already registered? Please visit our 
                    <a href="
                       <c:url value="/authentication/register.jsp">
                           <c:param name="action" value="checkout" />
                       </c:url>">
                        registration page.
                    </a>
                </c:when>
                <c:otherwise>
                    Not already registered? Please visit our 
                    <a href="<c:out value='${pageContext.request.contextPath}'/>/authentication/register.jsp">registration page.</a>
                </c:otherwise>
            </c:choose>
            <div style="clear:both; height: 40px"></div>
        </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>