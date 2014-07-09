<%-- 
    Document   : basket
    Created on : 12-Mar-2014, 18:38:10
    Author     : Simon Stanford

    Displays a list of all items the user has clicked the 'Buy' button for within this session.  Provides the functionality to remove items and
    complete checkout.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Basket</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>Basket</h1>
            <table>
                <th>Film</th>
                <th>Quantity</th>
                <th>Line Total</th>
                    <%--
                    A new Order object is created as a session attribute when the user first visits this website.  A DVD is added to this object when 
                    a user presses a 'Buy' button next to one of the DVDs.  This code loops through each item added to the order, and displays the
                    film name, quantity ordered and price.
                    --%>
                    <c:forEach var="line" items="${order.items}">
                    <tr>
                        <td>
                            <%--The film name is a link back to the film page--%>
                            <a href="
                               <c:url value="/GetDvds">
                                   <c:param name="action" value="getTitle"/>
                                   <c:param name="id" value="${line.dvd.id}"/>
                               </c:url>">
                                <c:out value='${line.dvd.name}'/>
                            </a>
                        </td>
                        <td><c:out value='${line.quantity}'/></td>
                        <%--This formatting ensures that the cost is formatted properly--%>
                        <td>£<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${line.total}"/></td>
                        <td>
                            <%--
                            This button allows the user to reduce the quantity of this film by one.  
                            If the quantity becomes 0 then the film is removed
                            --%>
                            <a href="
                               <c:url value="/ChangeBasket">
                                   <c:param name="id" value="${line.dvd.id}"/>
                                   <c:param name="action" value="remove"/>
                               </c:url>" class="button">
                                Remove
                            </a>
                        </td>
                    </tr>
                </c:forEach>  
            </table>
            <%--The total cost of the order is shown - with correct formatting--%>
            Total: £<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.total}"/>

            <%--Only show the button for the user to pay for the order if there are items in the basket.--%>
            <c:if test="${order.total > 0 }" >
                <p style="text-align:right; margin-right: 16px">
                    <a href="<c:out value='${pageContext.request.contextPath}'/>/checkout" class="button">
                        Checkout
                    </a>
                </p>
            </c:if>
            <div style="clear:both; height: 40px"></div>
        </div>
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>