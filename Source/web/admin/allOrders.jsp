<%-- 
    Document   : allOrders
    Created on : 18-Mar-2014, 10:55:23
    Author     : Simon Stanford

    An administrative function that displays a list of all orders placed, and provides links to mark them as dispatched or cancel them.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Admin | Orders</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--This is an administrative function.  Only allow the username 'admin' to visit this page.
        Check the session object to see if the username is 'admin', if not redirect the user to an error page.--%>
        <c:if test="${sessionScope.username != 'admin'}" >
            <jsp:forward page="/authentication/error.jsp">
                <jsp:param name="error" value="unathorised"/>
            </jsp:forward>
        </c:if>

        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>

        <div id="container">
            <h1>Orders</h1>
            <table>
                <th>Order No.</th>
                <th>User</th>
                <th>Date Placed</th>
                <th>Date Dispatched</th>
                <th>Total</th>
                    <%--The servlet that loaded this JSP provided a list of all orders placed.
                    Iterated through this list and display the results in a table--%>
                    <c:forEach var="order" items="${previousOrders}">
                    <tr>
                        <td><c:out value='${order.orderNo}'/></td>
                        <td>
                            <a href="
                               <c:url value="/userdetails">
                                   <c:param name="action" value="otherUser"/>
                                   <c:param name="username" value="${order.username}"/>
                               </c:url>">
                               <c:out value='${order.username}'/>
                            </a>
                        </td>
                        <td><c:out value='${order.orderDate}'/></td>
                        <td><c:out value='${order.dispatchDate}'/></td>
                        <td>
                            £<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.total}"/>
                        </td>  
                        <%--The button used to display the order details - this opens another JSP--%>
                        <td>
                            <a href="
                               <c:url value="/user/orderDetails.jsp">
                                   <c:param name="id" value="${order.orderNo}"/>
                               </c:url>" class="button">
                                View
                            </a>
                        </td>
                        <%--Only display the button to mark an order as dispatched if the order has not already been dispatched--%>
                        <c:if test="${order.dispatchDate == null}" >
                            <td>
                                <a href="
                                   <c:url value="/orders">
                                       <c:param name="orderNo" value="${order.orderNo}"/>
                                       <c:param name="action" value="dispatch"/>
                                   </c:url>" class="button">
                                    Mark Dispatched
                                </a>
                            </td>
                        </c:if>
                    </tr>                            
                </c:forEach>  
            </table>

        </div>
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>