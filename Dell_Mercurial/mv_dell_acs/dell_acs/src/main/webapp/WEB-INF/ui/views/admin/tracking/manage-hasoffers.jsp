<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>



<html>
<head>
    <title>Pixel Tracking - HasOffers</title>

    <style type="text/css">
        body {
            font-family: Verdana, Arial, san-serif;
        }
    </style>
</head>
<body>


<c:choose>
    <c:when test="${ ! empty error}">
        <div id="error-box" class="error-box" style="background-color:#ffe4e1;">
            <div>
                <span class="error-icon"></span>
                    ${error}
            </div>
        </div>
    </c:when>
    <c:otherwise>
    
	    ${navCrumbs}
	    
	    <c:if test="${fn:length(status) gt 0 && status == 'downloaded'}">
	    	<div id="breadcrumb">
		        ( <span title="Items are downloaded with the links. Uploading of items with the tracker URL is pending"
		                class="downloaded">
		                Items downloaded</span> )
			</div>
	    </c:if>
	
	    <c:if test="${fn:length(status) gt 0 && status == 'uploaded'}">
	    	<div id="breadcrumb">
		        ( <span class="uploaded"
		                title="Item url are updated with the tracker links">Tracker links uploaded</span> )
	        </div>
	    </c:if>

        <fieldset style="padding: 5px;">
            <legend><strong> Campaign Items </strong></legend>

            HasOffers Pixel integration coming soon :)

        </fieldset>
    </c:otherwise>
</c:choose>

</body>
</html>