<%-- 
    Document   : footer
    Created on : 06-Mar-2014, 10:35:10
    Author     : Simon Stanford

    The footer is stored in separately in this file and attached to each page.  This makes updating easier and saves duplication of code.
--%>

<div id="footer">
    <!-- First Column -->
    <div class="one-fourth">
        <h3>Useful Links</h3>
        <ul class="footer_links">
            <li><a href="http://uk.linkedin.com/in/sjstanford/">LinkedIn Profile</a></li>
            <li><a href="http://www.coventry.ac.uk/">Coventry University</a></li>
        </ul>
    </div>
    <!-- Second Column -->
    <div class="one-fourth">
        <h3>Documents</h3>
        <ul class="footer_links">
            <li><a href="${pageContext.request.contextPath}//docs/supporting_document.pdf">Supporting Document</a></li>                       
        </ul>
    </div>
    <!-- Third Column -->
    <div class="one-fourth">
        <h3>Information</h3>
        <div id="social_icons"> Theme by <a href="http://www.csstemplateheaven.com">CssTemplateHeaven</a><br></div>
    </div>
    <div style="clear:both"></div>
</div>