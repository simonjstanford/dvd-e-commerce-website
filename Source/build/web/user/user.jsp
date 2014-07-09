<%-- 
    Document   : user
    Created on : 15-Mar-2014, 09:12:07
    Author     : Simon Stanford

    Displays the encrypted and decrypted information that is stored in the database for a user.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | User Details</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>User Details for <c:out value='${user.username}'/><br></h1>
            <br>
            <br>
            <fieldset>
                <%--Display the encrypted data - for demonstration purposes only--%>
                <strong><font size="4">AES 256 bit Encrypted Data:</font></strong><br>
                <c:out value='${user.firstName_encrypted}'/> <c:out value='${user.secondName_encrypted}'/><br>
                <c:out value='${user.address_line1_encrypted}'/><br>
                <%--The second line of the address is not manditory - only print it if it exists--%>
                <c:if test="${user.address_line2_encrypted != '' || user.address_line2_encrypted != null}" >
                    <c:out value='${user.address_line2_encrypted}'/><br>
                </c:if>
                <c:out value='${user.city_encrypted}'/><br>
               <c:out value=' ${user.postcode_encrypted}'/><br>
            </fieldset>
            <br>
            <br>
            <fieldset>  
                <%--Display the unencrypted data--%>
                <strong><font size="4">Un-encrypted Data:</font></strong><br>
                <c:out value='${user.firstName_plain}'/> <c:out value='${user.secondName_plain}'/><br>
                <c:out value='${user.address_line1_plain}'/><br>
                <%--The second line of the address is not manditory - only print it if it exists--%>
                <c:if test="${user.address_line2_plain != '' || user.address_line2_plain != null}" >
                    <c:out value='${user.address_line2_plain}'/><br>
                </c:if>
                <c:out value='${user.city_plain}'/><br>
                <c:out value='${user.postcode_plain}'/><br>
            </fieldset>
            <br>
            <br>
            <%--Display the password hash and salt used--%>
            <strong><font size="4">Password SHA2 512 Hash:</font></strong><br>    
            <c:out value='${user.password}'/><br>
            <br>
            <br>
            <strong><font size="4">Salt used in Hash:</font></strong><br>
            <c:out value='${user.salt}'/><br>
        </div>
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>