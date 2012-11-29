<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<html>
<head>
    <title>Pixel Tracking - LinkTracker</title>

    <style type="text/css">
        body {
            font-family: Verdana, Arial, san-serif;
        }
    </style>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
    <!-- Ajax file upload -->
    <script type="text/javascript" src="<c:url value="/js/ajaxUpload/ajaxfileupload.js"/>"></script>
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

<script type="text/javascript">
    var tmpReviewArray = [];
    SITE_ID = <c:out value="${campaign.retailerSite.id}"/>;
</script>

<% String campaignID = request.getParameter("campaignID"); %>

<fieldset style="padding: 5px;">
    <legend><strong> Campaign Items </strong></legend>
    <c:if test="${fn:length(items) == 0}">
        Please add some items to the campaign - <a
            href="<c:url value='/admin/campaign/edit.do?id='/>${campaign.id}"> ${campaign.name} </a>
    </c:if>

    <c:if test="${fn:length(items) gt 0}">
        <div id="campaign_items">
            <strong>Products and Events</strong>
            <ul style="border: 1px #000000 solid;">
                <li class="header" onclick="CItems.toggleItemsBlock('c_items');"
                    title="Click to minimize/maximize">
                    <span class="title">Title</span>
                    <span class="reviews">Reviews#</span>
                    <span class="stars">Stars#</span>
                    <span class="listPrice">List Price</span>
                    <span class="price">Price</span>
                </li>
                <li class="items" id="c_items">
                    <ul
                            <c:if test='${(fn:length(items)) > 6}'>style="margin:0;height: <c:out value='${(fn:length(items)*20)}'/>px; overflow-y: scroll;"
                    </c:if>>
                        <!-- Items -->
                        <c:forEach items="${items}" var="item">
                            <c:if test="${item.itemType == 'product' || item.itemType == 'event'}">
                                <li>
                                        <span class="${item.itemType} title" style="width: 50%">
                                            <c:choose>
                                                <c:when test="${item.itemType == 'event'}">
                                                    <a target="new"
                                                       href="<c:url value='/admin/events/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                                </c:when>
                                                <c:otherwise>
                                                    ${item.itemName}
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${ ! empty item.properties.cloned && item.properties.cloned}">
                                                <a href="<c:url value='/admin/campaign/clone-item.do'/>?id=${item.id}&productID=${item.itemID}&campaignID=${item.campaign.id}"
                                                   title="Edit cloned item"><span class="edit_clone status_icon"></span></a>
                                            </c:if>
                                            <span class="status_icon" id="review_count_${item.id}"></span>
                                        </span>
                                    <span class="reviews">${item.reviews}</span>
                                    <span class="stars">${item.stars}</span>
                                    <span class="listPrice">${item.listPrice}</span>
                                    <span class="price">${item.price}</span>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </li>
            </ul>
            <!-- Render Documents -->
            <c:if test="${fn:length(documents) gt 0}">
                <strong>Documents</strong>
                <ul style="margin-top: 10px;border: 1px #000000 solid;">
                    <li class="header" onclick="CItems.toggleItemsBlock('c_documents');"
                        title="Click to minimize/maximize">
                        <span style="width: 35%;">Title</span>
                        <span style="width:23%;">Document</span>
                        <span style="width:14%;">Start Date</span>
                        <span style="width:14%;">End Date</span>

                    </li>
                    <li class="items" id="c_documents">
                        <ul <c:if test='${(fn:length(documents)) > 5}'> style="margin:0;height: <c:out
                                value='${(fn:length(documents)*30)}'/>px; overflow-y: scroll;"</c:if>>
                            <!-- Render Documents -->
                            <c:forEach items="${documents}" var="item">
                                <li>
                                        <span class="${item.itemType}" style="width: 34%">
                                            <a target="new"
                                               href="<c:url value='/admin/document/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                        </span>
                                        <span style="width: 23%">
                                            <a href="<c:url value="${item.properties.document}"/>"><c:out
                                                    value="${item.properties.docName}"/></a>
                                        </span>
                                            <span style="width: 14%"><fmt:formatDate
                                                    value="${item.properties.startDate}"
                                                    pattern="MM/dd/yyyy hh:mm"/></span>
                                            <span style="width: 14%"><fmt:formatDate value="${item.properties.endDate}"
                                                                                     pattern="MM/dd/yyyy hh:mm"/></span>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
            </c:if>
        </div>
        <div class="items_action_panel">

            <%@include file="pixel-errors.jspf" %>

            <div style="float: left;">
                <form action="<c:url value='/admin/campaign/downloadLinks.do?campaignID='/><%=campaignID%>"
                      method="POST">
                    <input type="submit" value="Download items">
                </form>
            </div>
            <div style="float: right;">
                <form name="uploadLinks" action="/admin/campaign/uploadCampaignItems.do"
                      enctype="multipart/form-data"
                      method="POST">
                    <input type="file" name="uploadFile" id="uploadFile">
                    <input type="hidden" name="campaignID" id="campaignID">
                    <input type="button" id="upload_btn" value="Update items with tracker links"
                           title="Upload tracker links"
                           onclick="return ajaxFileUpload();">
                    <script type="text/javascript">
                        function ajaxFileUpload() {

                            // Check if the file is selected before clicking on Update
                            var fileName = $('#uploadFile').val();
                            if (fileName.length > 0) {
                                //starting setting some animation when the ajax starts and completes
                                $("#loading")
                                        .ajaxStart(function() {
                                    $(this).show();
                                })
                                        .ajaxComplete(function() {
                                    $(this).hide();
                                });

                                /*
                                 prepareing ajax file upload
                                 url: the url of script file handling the uploaded files
                                 fileElementId: the file type of input element id and it will be the index of  $_FILES Array()
                                 dataType: it support json, xml
                                 secureuri:use secure protocol
                                 success: call back function when the ajax complete
                                 error: callback function when the ajax failed

                                 */
                                $.ajaxFileUpload({
                                    url: basePath + 'admin/campaign/uploadCampaignItems.json?pixelTrackerType=linktracker&campaignID='.concat(requestParams.campaignID),
                                    secureuri:false,
                                    dataType: 'json',
                                    fileElementId:'uploadFile',
                                    success: function (data, status) {
                                        if (typeof(data.error) != 'undefined') {
                                            if (data.error != '') {
                                                alert(data.error);
                                            } else {
                                                alert(data.msg);
                                            }
                                        }
                                    },
                                    error: function (data, status, e) {
                                        var ajaxResponse = data.responseText;
                                        //console.log('response ',ajaxResponse);

                                        var success = "Successfully";
                                        var error1 = "Invalid";
                                        var error2 = "DURL-NotFound";
                                        var error3 = "DURL-Found";
                                        if( ajaxResponse != undefined || ajaxResponse != null ) {

                                            if (ajaxResponse.indexOf(success) != -1) {
                                                console.log(success);
                                                $('#pixel-success').show().fadeOut(7000, "linear");
                                            } else if (ajaxResponse.indexOf(error1) != -1) {
                                                console.log(error1);
                                                $('#pixel-invalid').show().fadeOut(7000, "linear");
                                            } else if (ajaxResponse.indexOf(error2) != -1) {
                                                console.log(error2);
                                                $('#pixel-invalid-lt').show().fadeOut(7000, "linear");
                                            } else if (ajaxResponse.indexOf(error3) != -1) {
                                                console.log(error3);
                                                $('#pixel-invalid-mv').show().fadeOut(7000, "linear");
                                            } else {
                                                console.log('Unsupported condition for pixel tracking.');
                                            }
                                        } else {
                                            console.log('Invalid CSV file.');
                                            $('#pixel-invalid-file').show().fadeOut(7000, "linear");
                                        }
                                    }
                                });
                            }
                            return false;
                        }
                    </script>
                </form>
            </div>
        </div>
    </c:if>
</fieldset>

</c:otherwise>
</c:choose>

</body>
</html>

