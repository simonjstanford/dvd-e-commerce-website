<%--
    Document   : genres
    Created on : 12-Mar-2014, 18:38:10
    Author     : Simon Stanford

    Displays a list of all films within a certain genre
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <head>
        <title>Amazune | <c:out value='${genre}'/></title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
        <!-- Taken from http://stackoverflow.com/questions/6385293/simple-two-column-html-layout-without-using-tables -->
        <style type="text/css">
            #wrap {
                width:950px;
                margin:0 auto;
            }
            #left_col {
                float:left;
                width:200px;
            }
            #right_col {
                float:right;
                width:750px;
            }
        </style>

    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="header.jsp"/>

        <div id="container">
            <h1><c:out value='${genre}'/></h1>
            <br>
            <br>
            <%--
            This page is reached from the GetDVDs servlet, which stores a session attribute of all DVDs of a certain genre.  The code below iterates 
            through the list of DVD and displays them.
            --%>
            <c:forEach var="dvd" items="${allDvds}">
                <div>
                    <div id="left_col">
                        <img src="<c:out value='${dvd.imageLink}'/>" width="130">
                    </div>
                    <div id="right_col">
                        <%--The film title is a link to the main film page--%>
                        <a href="
                           <c:url value="/GetDvds">
                               <c:param name="action" value="getTitle" />
                               <c:param name="id" value="${dvd.id}" />
                           </c:url>">
                            <font size="4">
                            <c:out value='${dvd.name}'/>
                            </font>
                        </a>
                        <p style="margin-right: 16px">${dvd.shortDescription}</p>
                        <p style="text-align:right; margin-right: 16px">
                            <strong><c:out value='${dvd.stock}'/> in stock</strong><BR>
                            <strong>£<c:out value='${dvd.price}'/></strong><BR>
                        </p>
                        <%--The 'Buy' button is only displayed if there are any items in stock--%>
                        <c:if test="${dvd.stock > 0}" >
                            <p style="text-align:right; margin-right: 16px">
                                <a href="
                                   <c:url value="/ChangeBasket">
                                       <c:param name="id" value="${dvd.id}"/>
                                       <c:param name="action" value="add"/>
                                   </c:url>" class="button">
                                    Buy Now
                                </a>
                            </p>
                        </c:if>
                    </div>
                </div>
                <div style="clear:both; height: 40px"></div>
            </c:forEach>
            <div style="clear:both; height: 40px"></div>
        </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="footer.jsp"/>
    </body>
</html>