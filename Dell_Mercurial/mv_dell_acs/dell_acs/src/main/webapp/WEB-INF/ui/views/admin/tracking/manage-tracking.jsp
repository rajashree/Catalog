<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>


<html>
<head>
    <title>Pixel Tracking - Manage</title>

    <style type="text/css">
        body {
            font-family: Verdana, Arial, san-serif;
        }
    </style>
</head>
<body>


<c:if test="${pixelTrackerConfigError == true }">
    <span> No PixelTracking system configured. Please choose the PixelTracker for <a href="<c:url value='/admin/retailerSites/edit.do?id='/>${siteID}">Retailer Site.</a> </span>


</c:if>

</body>
</html>