<%-- 
    Document   : orders
    Created on : 12-Mar-2014, 18:56:10
    Author     : Simon Stanford

    Displays a list of all orders made by the user logged on.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Orders</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" 
	      href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" 
	      href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>
        
        <div id="container">
            <h1>Orders</h1>
	    <table>
                <th>Order No.</th>
                <th>Date Placed</th>
                <th>Date Dispatched</th>
                <th>Total</th>
                    <%--
                    The orders servlet stores a list of all orders made by the user logged in as a session attribute.  This code iterates through all
                    those orders and displays them in a table.
                    --%>
		    <c:forEach var="order" items="${previousOrders}">
                    <tr>
                        <td><c:out value='${order.orderNo}'/></td>
                        <td><c:out value='${order.orderDate}'/></td>
                        <td><c:out value='${order.dispatchDate}'/></td>
                        <td>
                            <%--This code ensures that the order price is formatted correctly--%>
			    £<fmt:formatNumber type="number" maxFractionDigits="2" 
					      minFractionDigits="2" value="${order.total}"/>
			</td>         
                        <td>
                            <%--
                            This button enables the user to see more details of a particular order via an order number sent as a paramter to the 
                            orderDetails JSP.
                            --%>
                            <a href="
			       <c:url value="/user/orderDetails.jsp">
				   <c:param name="id" value="${order.orderNo}"/>
			       </c:url>" class="button">
				View
			    </a>
			</td>		
                    </tr>                            
                </c:forEach>  
            </table>
        </div>
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>