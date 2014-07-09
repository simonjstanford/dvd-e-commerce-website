<%-- 
    Document   : documentation
    Created on : 11-Mar-2014, 15:21:01
    Author     : Simon Stanford

    Displays a list of links to development documentation for the website.
--%>

<!DOCTYPE HTML>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <head>
        <title>Amazune | Assignment Documentation</title>
        <meta charset="utf-8">
        <!-- CSS Files -->
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
        <link rel="stylesheet" type="text/css" media="screen" href="<c:out value='${pageContext.request.contextPath}'/>/menu/css/simple_menu.css">
    </head>
    <body>
        <%--The navigation menu is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/header.jsp"/>
        <div id="container">
            <h1>Assignment Documentation</h1>
            <table>
                <th>Document</th>
                <th>Description</th>
                <tr>
                    <td>Assignment Brief</td>
                    <td>The original assignment brief and marking criteria.</td>
                    <td><a href="<c:out value='${pageContext.request.contextPath}'/>/docs/M30CDE-CW-201213-4.pdf" class="button">Open</a></td>               
                </tr> 
                <tr>
                    <td>Supporting Document</td>
                    <td>Contains use cases, design overview, database ER diagram, evaluation report and testing..</td>
                    <td><a href="<c:out value='${pageContext.request.contextPath}'/>/docs/supporting_document.pdf" class="button">Open</a></td>               
                </tr>                            
            </table>
            <div style="clear:both; height: 40px"></div>
        </div>
        <%--The footer is stored in a separate file and attached to each page.  This makes updating easier and saves duplication of code.--%>
        <jsp:include page="/footer.jsp"/>
    </body>
</html>