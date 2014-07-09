<%-- 
    Document   : getTitle
    Created on : 12-Mar-2014, 18:38:10
    Author     : Simon Stanford

    Displays detailed information on a certain film.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <head>
        <title>Amazune | <c:out value='${dvd.imageLink}'/></title>
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

        <%--
        When requested, the GetDvds servlet stores a DVD object as a request attributes and directs the user to this page.  This page then reads and
        displays all details for the DVD.
        --%>
        <div id="container">          
            <div id="left_col">
                <img src="<c:out value='${dvd.imageLink}'/>" alt="" width="200">
            </div>

            <div id="right_col">
                <h1><c:out value='${dvd.name}'/></h1>
                <p class="post_meta">
                    <strong>
                        <a href="
                           <c:url value="/GetDvds">
                               <c:param name="action" value="filterByGenre"/>
                               <c:param name="genre" value="${dvd.genre}"/>
                           </c:url>">
                            <font size="3">
                            <c:out value='${dvd.genre}'/>
                            </font>
                        </a>
                    </strong>
                </p> 
                <p style="margin-right: 16px"><c:out value='${dvd.description}'/></p>
                <p style="text-align:right; margin-right: 16px">
                    <strong><c:out value='${dvd.stock}'/> in stock</strong><BR>
                    <strong>£<c:out value='${dvd.price}'/></strong><BR>
                </p>

                <%--The 'Buy' button is only displayed when the item is in stock--%>
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
            <div style="clear:both; height: 40px"></div>

            <%--
            In addition to the DVD information, the GetDvds servlet also stores five random films of the same genre as a request attribute.  The code
            below displays links to these films.
            --%>
            <div id="prod_nav">
                <strong>More from <c:out value='${dvd.genre}'/>:</strong>
                <ul>
                    <c:forEach var="dvd" items="${randomDvds}">
                        <li>
                            <%--The DVD cover image is the link to the film page for this random film--%>
                            <a href="
                               <c:url value="/GetDvds">
                                   <c:param name="action" value="getTitle" />
                                   <c:param name="id" value="${dvd.id}" />
                               </c:url>">
                                <img src="<c:out value='${dvd.imageLink}'/>" height="100">
                                <strong>
                                    <c:out value='${dvd.name}'/>
                                </strong>
                                <c:out value='£${dvd.price}'/>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="footer.jsp"/>
    </body>
</html>