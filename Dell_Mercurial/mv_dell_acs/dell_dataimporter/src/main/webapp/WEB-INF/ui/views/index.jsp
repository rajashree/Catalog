<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<c:redirect url="/admin/index.do" />--%>
<div id="page">
    <div id="wrapper">

        <h1 style="text-align:center;">Dell Content Server Service</h1>
        <ul style="list-style: none;">
            <li><a href="<c:url value="/rest/api/json/v1/advertisers"/>">Get Advertisers</a></li>
            <li><a href="<c:url value="/recommendedproducts.jsp"/>">Product Recommendations</a></li>
            <li><a href="<c:url value="/recommendedproductsJSON.jsp"/>">Product Recommendations as Json</a></li>
            <li><a href="<c:url value="/admin/index.do"/>">Admin login</a></li>
        </ul>
        <a href="<c:url value="/fb-login.do"/>">Facebook Login</a><br/>
    </div>
</div>

