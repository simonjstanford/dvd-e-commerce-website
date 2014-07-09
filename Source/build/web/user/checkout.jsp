<%-- 
    Document   : checkout
    Created on : 15-Mar-2014, 21:40:25
    Author     : Simon Stanford

    Allows a user to purchase items in the basket.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Checkout</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">          
            <h1>Checkout</h1>
            <br>
            <%--Display the delivery address--%>
            <strong>Your shipment will be sent to:</strong><br>
            <c:out value=' ${user.firstName_plain}'/> <c:out value='${user.secondName_plain}'/> <br>
            <c:out value='${user.address_line1_plain}'/><br>
            <%--The second line of the address is not manditory - only print it if it exists--%>
            <c:if test="${user.address_line2_plain != '' || user.address_line2_plain != null}" >
                <c:out value='${user.address_line2_plain}'/><br>
            </c:if>
            <c:out value='${user.city_plain}'/><br>
            <c:out value='${user.postcode_plain}'/><br>
            <br>
            <br>
            <%--Enter payment information--%>
            <strong>Please enter payment details for your purchase totalling £
                <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.total}"/>:
            </strong><br>
            <form action="<c:out value='${pageContext.request.contextPath}'/>/pay" method=post>
                <label>Please enter your credit card number: (try 4988864099669548)</label>
                <input type="text" name="cardNo" size="25">
                <%--For security, the user's password must be entered before continuing--%>
                <label>Please confirm your password: </label>
                <input type="password" size="25" name="password">
                <br><br>
                <input type="submit" value="Pay">
            </form>
            <div style="clear:both; height: 40px"></div>
        </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>