<%-- 
    Document   : orderDetails
    Created on : 17-Mar-2014, 13:45:57
    Author     : Simon Stanford

    Displays the details for a particular order.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Order: <c:out value='${param.id}'/></title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>Order: <c:out value='${param.id}'/></h1>
            <%--
            The orders servlet saves a session attribute for a list of orders.  This JSP is called by the orders JSP, and is sent an order number as a
            parameter.  The code below iterates through the list of orders and finds the matching order number, and displays it.
            --%>
            <c:forEach var="order" items="${previousOrders}">
                <c:if test="${order.orderNo == param.id }" >
                    <table>
                        <th>Film</th>
                        <th>Quantity</th>
                        <th>Line Total</th>
                            <%--Each film in the order is displayed--%>
                            <c:forEach var="line" items="${order.items}">
                            <tr>
                                <td>
                                    <%--The film title is a link back to the film page--%>
                                    <a href="
                                       <c:url value="/GetDvds">
                                           <c:param name="action" value="getTitle"/>
                                           <c:param name="id" value="${line.dvd.id}"/>
                                       </c:url>">
                                        <c:out value='${line.dvd.name}'/>
                                    </a>
                                </td>
                                <td><c:out value='${line.quantity}'/></td>
                                <%--This formatting ensures that price totals are displayed correctly--%>
                                <td>£<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${line.total}"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                    <%--This formatting ensures that price totals are displayed correctly--%>
                    Total: £<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.total}"/>
                    <%--
                    The user has the functionality is cancel an order only if that order is not yet dispatched.  The code below only displays the 
                    dispatch button if there is no dispatch date.
                    --%>
                    <c:if test="${order.dispatchDate == null}" >	
                        <p style="text-align:right; margin-right: 16px">
                            <a href="
                               <c:url value="/orders">
                                   <c:param name="orderNo" value="${order.orderNo}"/>
                                   <%--This parameter tells the orders servlet what action to take--%>
                                   <c:param name="action" value="cancel"/>
                               </c:url>" class="button">
                                Cancel Order
                            </a>
                        </p>
                    </c:if>
                </c:if>
            </c:forEach>  
            <div style="clear:both; height: 40px"></div>
        </div>
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>