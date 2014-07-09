<%-- 
    Document   : orders
    Created on : 12-Mar-2014, 18:38:10
    Author     : Simon Stanford

    The navigation menu is stored separately in this file and attached to each page.  This makes updating easier and saves duplication of code.
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="header">
    <!-- Logo/Title - picture obtained from: http://www.clker.com/cliparts/5/e/W/X/2/Y/grass-hi.png-->
    <div id="site_title">
        <a href="<c:out value='${pageContext.request.contextPath}'/>/loadAmazune"> 
            <img src="<c:out value='${pageContext.request.contextPath}'/>/img/logo.png" alt="" height="70">
        </a> 
    </div>

    <!-- Main Menu -->
    <ol id="menu">
        <li><a href="<c:out value='${pageContext.request.contextPath}'/>/loadAmazune">Amazune</a></li>
        <li><a href="#">Genres</a>
            <!-- sub menu -->
            <%--Display the list of genres.  Each item is a link to all films of that genre--%>
            <ol> 
                <c:forEach var="genre" items="${genres}">
                    <li>
                        <a href="
                           <c:url value="/GetDvds">
                               <%--The 'action' parameter instructs GetDvds what action to take--%>
                               <c:param name="action" value="filterByGenre"/>
                               <c:param name="genre" value="${genre}"/>
                           </c:url>">
                            <c:out value='${genre}'/>
                        </a>
                    </li>
                </c:forEach>
            </ol>
        </li>
        <%--This is a link to the shopping basket for the current session.--%>
        <li><a href="<c:out value='${pageContext.request.contextPath}'/>/user/basket.jsp">Basket</a></li>
            <c:choose>
                <%--If the user is not logged in then show a link to log in or register--%>
                <c:when test="${username == null}">
                <li>
                    <a href="<c:out value='${pageContext.request.contextPath}'/>/authentication/logon.jsp">Log in</a>
                    <ol>
                        <li><a href="<c:out value='${pageContext.request.contextPath}'/>/authentication/register.jsp">Register</a></li>
                    </ol>
                </li>
            </c:when>
            <%--
            Otherwise, display a personalised message to the user and provide links to log off let them see their personal information and 
            previous orders.
            --%>
            <c:otherwise>
                <li>
                    <%--This is the link to view user details--%>
                    <a href="
                       <c:url value="/userdetails">
                           <c:param name="action" value="thisUser"/>
                       </c:url>
                       ">Hello, <c:out value='${firstName}'/>
                    </a>
                    <ol>
                        <li>
                            <%--This is the link to view historical orders--%>
                            <a href="
                               <c:url value="/orders">
                                   <c:param name="action" value="user"/>
                               </c:url>">
                                Orders
                            </a>
                        </li>
                        <%--This is the link to log out--%>
                        <li><a href="<c:out value='${pageContext.request.contextPath}'/>/logout">Log Out</a></li>
                    </ol>
                </li>
            </c:otherwise>
        </c:choose>
        <%--Links to the admin pages and the site documentation.  .--%>
        <%--Only the user 'admin' has access to the admin pages - only display the link if the admin is logged in--%>
        <li><a href="#">More</a>
            <ol>
                <li><a href="
                       <c:out value='${pageContext.request.contextPath}'/>/documentation.jsp">Documentation
                    </a>
                </li>

                <c:if test="${sessionScope.username == 'admin'}" >
                    <li>
                        <a href="
                           <c:out value='${pageContext.request.contextPath}'/>/admin/admin.jsp">Admin
                        </a>
                    </li>
                </c:if>
            </ol>
        </li>
    </ol>
</div>
