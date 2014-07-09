<%--
    Document   : amazune
    Created on : 21-Feb-2014, 17:19:35
    Author     : Simon Stanford

    The homepage for the DVD website.  The user gets to this page after first visiting the loadAmazune servlet, which instantiates appropriate objects
    as session attributes and retrieves the film genres and a list of random DVDs from the database.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Amazune</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="menu/css/simple_menu.css">
        <!--JS FILES -->
        <script src="js/jquery.tools.min.js"></script>
        <script>
            $(function() {
                $("#prod_nav ul").tabs("#panes > div", {
                    effect: 'fade',
                    fadeOutSpeed: 400
                });
            });
        </script>
        <script>
            $(document).ready(function() {
                $(".pane-list li").click(function() {
                    window.location = $(this).find("a").attr("href");
                    return false;
                });
            });
        </script>
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="header.jsp"/>

        <div id="container">
            <!-- tab panes -->
            <div id="prod_wrapper">
                <div id="panes">
                    <%--Display the five random DVDs sent from loadAmazune--%>
                    <c:forEach var="dvd" items="${randomDvds}">
                        <div> 
                            <img src="<c:out value='${dvd.imageLink}'/>" height="200">
                            <%--The film title is a link to the film page--%>
                            <a href="
                               <c:url value="/GetDvds">
                                   <c:param name="action" value="getTitle" />
                                   <c:param name="id" value="${dvd.id}" />
                               </c:url>">
                                <font size="5">
                                <c:out value='${dvd.name}'/>
                                </font>
                            </a>
                            <p>
                                <%--The genre is a link to a list of all films in that genre--%>
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
                                <br>
                                <c:out value='${dvd.shortDescription}'/>
                            </p>
                            <p style="text-align:right; margin-right: 16px">
                                <strong><c:out value='${dvd.stock}'/> in stock</strong><BR>
                                <strong>£<c:out value='${dvd.price}'/></strong><BR>
                            </p>
                            <%--The 'Buy' button is only displayed when there is > 0 items in stock--%>
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
                    </c:forEach>         
                </div>
                <!-- END tab panes -->
                <br clear="all">
                <!-- navigator -->
                <%--This is the second half of the code to display the 5 random films - Javascript that came with the template shows/hides them--%>
                <div id="prod_nav">
                    <ul>
                        <c:forEach var="dvd" items="${randomDvds}" varStatus="n" >
                            <li>
                                <a href="<c:out value='${n.count}'/>">
                                    <img src="<c:out value='${dvd.imageLink}'/>" height="100">
                                    <strong>
                                        <c:out value='${dvd.name}'/>
                                    </strong> 
                                    £<c:out value='${dvd.price}'/>
                                </a></li>
                            </c:forEach>
                    </ul>
                </div>
                <!-- close navigator -->
            </div>
            <!-- END prod wrapper -->
            <div style="clear:both"></div>
            <h1 style="padding: 20px 0">About this site</h1>
            <blockquote>
                This is a test e-commerce web site developed by Simon Stanford for Coventry University's Open Systems Application Development (M30CDE)
                module.  The web site employs servlets, JSP and MySQL to create a dynamic experience, and uses a website template for aesthetics.
                Customer information is encrypted within the database using an AES-256 encryption algorithm, and passwords are stored in a salted 
                SHA2-512 hash. The connection between the client and the server is secured using a self signed TLS 1.2 certificate.
            </blockquote>
            <p style="text-align:right; margin-right: 16px; margin-bottom: 15px"><a href="documentation.jsp" class="button" style="font-size: 18px">Find out more</a></p>
        </div>
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
