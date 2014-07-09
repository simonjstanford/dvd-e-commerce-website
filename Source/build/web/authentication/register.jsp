<%-- 
    Document   : register
    Created on : 14-Mar-2014, 11:51:11
    Author     : Simon Stanford

    Allows a user to register with the website to create a username and password.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Register</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>New User Registration</h1>
            <%--Registration form--%>
            <FORM ACTION="<c:out value='${pageContext.request.contextPath}'/>/register" METHOD="POST">
                <label>First name:</label> <INPUT TYPE="TEXT" NAME="firstName" size="70">
                <label>Second name:</label> <INPUT TYPE="TEXT" NAME="secondName" size="70"><br>
                <label>Address:</label> 
                <INPUT TYPE="TEXT" NAME="address_line1" size="70">
                <INPUT TYPE="TEXT" NAME="address_line2" size="70">
                <label>Town/City:</label> 
                <INPUT TYPE="TEXT" NAME="city" size="70">
                <label>Post Code:</label> 
                <INPUT TYPE="TEXT" NAME="postcode">
                <br>
                <br>
                <label>Username: (minium six characters)</label><INPUT TYPE="TEXT" NAME="username" size="70">
                <label>Password: (minium six characters)</label> <INPUT TYPE="PASSWORD" NAME="password" size="70"><br>
                <%--This parameter is sent if the user got to this page whilst trying to purchase items.  See logon.jsp for an explaination.--%>
                <c:if test="${param.action == 'checkout'}" >
                    <input type="hidden" name="action" value="checkout">
                </c:if>
                <BR>
                <INPUT TYPE="SUBMIT" VALUE="Register">
            </FORM>
            <div style="clear:both; height: 40px"></div>
        </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>