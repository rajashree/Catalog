<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>

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
    </c:if>

    <c:if test="${fn:length(status) gt 0 && status == 'uploaded'}">
        ( <span class="uploaded"
                title="Item url are updated with the tracker links">Tracker links uploaded</span> )
    </c:if>
</div>

<script type="text/javascript">
    var tmpReviewArray = [];
    SITE_ID = <c:out value="${campaign.retailerSite.id}"/>;
</script>

<% String campaignID = request.getParameter("campaignID"); %>
    <div id="campaign_items">
        <!-- Render Products -->
        <fieldset>
            <legend><strong>Products</strong></legend>
            <c:if test="${fn:length(products) gt 0}">
                <ul style="border: 1px #000000 solid;">
                    <li class="header" onclick="CItems.toggleItemsBlock('c_items');" title="Click to minimize/maximize">
                        <span class="title">Title</span>
                        <span class="reviews">Reviews#</span>
                        <span class="stars">Stars#</span>
                        <span class="listPrice">List Price</span>
                        <span class="price">Price</span>
                        <span>Actions</span>
                    </li>
                    <li class="items" id="c_items">
                        <ul <c:if test='${(fn:length(products)) > 6}'>style="margin:0;height: <c:out value='${(fn:length(products)*20)}'/>px; overflow-y: scroll;"</c:if>>
                            <!-- Items -->
                            <c:forEach items="${products}" var="item">
                                <li>
                                    <span class="${item.itemType} title" style="width: 50%">
                                        <c:choose>
                                            <c:when test="${item.itemType == 'event'}">
                                                <a target="new" href="<c:url value='/admin/events/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                            </c:when>
                                            <c:otherwise>
                                                ${item.itemName}
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${ ! empty item.properties.cloned && item.properties.cloned}">
                                            <%--<a href="<c:url value='/admin/campaign/clone-item.do'/>?id=${item.id}&campaignID=${item.campaign.id}"--%>
                                            <a href="<c:url value='/admin/campaign/clone-item.do'/>?id=${item.id}&productID=${item.itemID}&campaignID=${item.campaign.id}"
                                               title="Edit cloned item"><span class="edit_clone status_icon"></span></a>
                                        </c:if>
                                        <span class="status_icon" id="review_count_${item.id}"></span>
                                    </span>
                                    <span class="reviews">${(item.reviews != null)?item.reviews: 0}</span>
                                    <span class="stars">${(item.stars != null)?item.stars: 0}</span>
                                    <span class="listPrice">${(item.listPrice != null)?item.listPrice: 0.0}</span>
                                    <span class="price">${(item.price != null)?item.price: 0.0}</span>
                                    <span class="actions">
                                        <c:if test="${ ! empty item.properties.reviews}">
                                            <script type="text/javascript">
                                                if (tmpReviewArray == undefined) {
                                                    tmpReviewArray = new Object();
                                                }
                                                tmpReviewArray['review_${item.id}'] = '<c:out value="${item.properties.reviews}" />';
                                                var len = tmpReviewArray['review_${item.id}'].split(',').length;
                                                if (len > 0) {
                                                    $('#review_count_${item.id}').html('(' + len + ')');
                                                    $('#review_count_${item.id}').attr('title', len + " review(s) selected");
                                                    $('#review_count_${item.id}').addClass("review_count")
                                                } else {
                                                    $('#review_count_${item.id}').removeClass("review_count")
                                                }

                                            </script>
                                        </c:if>
                                        <span style="width: 105px; display: inline-block;">
                                            <a href="#" onClick='CIReview.showReviewPopup(${item.id}, ${item.itemID}, tmpReviewArray["review_${item.id}"]);'
                                               class='review items_action' title="Choose reviews for item">Reviews</a>
                                            <a href="#" onClick='CItems.removeItem(${item.id});'
                                               class='trash items_action' title="Remove the item from campaign">Remove</a>
                                            <c:if test="${item.itemType == 'product'}">
                                                <c:choose>
                                                    <c:when test="${ ! empty item.properties.cloned && item.properties.cloned}">
                                                        <a class="disabled_clone items_action" title="This item is already cloned">Clone</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a class="clone items_action" title="Clone this item"
                                                           href="<c:url value='/admin/campaign/clone-item.do'/>?id=${item.id}&campaignID=${item.campaign.id}&productID=${item.itemID}">
                                                            Clone
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <a class="properties items_action" title="Manage item properties"
                                               href="<c:url value='/admin/entities/properties/manage-properties.do?type=campaignItem'/>&id=${item.id}">
                                                Properties
                                            </a>
                                        </span>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
            </c:if>
            <c:if test="${fn:length(products) eq 0}">
                <span style="padding:5px;">No products are associated to the campaign.</span>
            </c:if>
        </fieldset>

        <!-- Render Events -->
        <fieldset>
            <legend><strong>Events</strong></legend>
                <c:if test="${fn:length(events) gt 0}">
                    <ul style="border: 1px #000000 solid;">
                    <li class="header" onclick="CItems.toggleItemsBlock('c_events');" title="Click to minimize/maximize">
                        <span class="title">Title</span>
                        <span class="reviews">Reviews#</span>
                        <span class="stars">Stars#</span>
                        <span class="listPrice">List Price</span>
                        <span class="price">Price</span>
                        <span>Actions</span>
                    </li>
                    <li class="items" id="c_events">
                        <ul <c:if test='${(fn:length(events)) > 6}'>style="margin:0;height: <c:out value='${(fn:length(events)*20)}'/>px; overflow-y: scroll;"</c:if>>
                            <!-- Items -->
                            <c:forEach items="${events}" var="item">
                                <li>
                                    <span class="${item.itemType} title" style="width: 50%">
                                        <a target="new" href="<c:url value='/admin/events/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                        <span class="status_icon" id="review_count_${item.id}"></span>
                                    </span>
                                    <span class="reviews">${item.reviews}</span>
                                    <span class="stars">${item.stars}</span>
                                    <span class="listPrice">${item.listPrice}</span>
                                    <span class="price">${item.price}</span>
                                    <span class="actions">
                                        <c:if test="${ ! empty item.properties.reviews}">
                                            <script type="text/javascript">
                                                if (tmpReviewArray == undefined) {
                                                    tmpReviewArray = new Object();
                                                }
                                                tmpReviewArray['review_${item.id}'] = '<c:out value="${item.properties.reviews}" />';
                                                var len = tmpReviewArray['review_${item.id}'].split(',').length;
                                                if (len > 0) {
                                                    $('#review_count_${item.id}').html('(' + len + ')');
                                                    $('#review_count_${item.id}').attr('title', len + " review(s) selected");
                                                    $('#review_count_${item.id}').addClass("review_count")
                                                } else {
                                                    $('#review_count_${item.id}').removeClass("review_count")
                                                }

                                            </script>
                                        </c:if>
                                        <span style="width: 105px; display: inline-block;">
                                            <a href="#" onClick='CIReview.showReviewPopup(${item.id}, ${item.itemID}, tmpReviewArray["review_${item.id}"]);'
                                               class='review items_action' title="Choose reviews for item">Reviews</a>
                                            <a href="#" onClick='CItems.removeItem(${item.id});'
                                               class='trash items_action' title="Remove the item from campaign">Remove</a>
                                            <a class="properties items_action" title="Manage item properties"
                                               href="<c:url value='/admin/entities/properties/manage-properties.do?type=campaignItem'/>&id=${item.id}">
                                                Properties
                                            </a>
                                        </span>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
                </c:if>
                <c:if test="${fn:length(events) eq 0}">
                    <span style="padding:5px;">No events are associated to the campaign.</span>
                </c:if>
        </fieldset>

        <!-- Render Documents -->
        <fieldset>
            <legend><strong>Documents</strong></legend>
                <c:if test="${fn:length(documents) gt 0}">
                    <ul style="margin-top: 10px;border: 1px #000000 solid;">
                    <li class="header" onclick="CItems.toggleItemsBlock('c_documents');" title="Click to minimize/maximize">
                        <span class="doctitle">Title</span>
                        <span class="docurl">Document</span>
                        <span class="docdate">Start Date</span>
                        <span class="docdate">End Date</span>
                        <span class="actions">Actions</span>
                    </li>
                    <li class="items" id="c_documents">
                        <ul <c:if test='${(fn:length(documents)) > 5}'> style="margin:0;height: <c:out value='${(fn:length(documents)*30)}'/>px; overflow-y: scroll;"</c:if>>
                            <!-- Render Documents -->
                            <c:forEach items="${documents}" var="item">
                                <li>
                                    <span class="${item.itemType} doctitle">
                                        <a target="new" href="<c:url value='/admin/document/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                    </span>
                                    <span class="docurl">
                                        <a href="<c:url value="${item.properties.document}"/>"><c:out value="${item.properties.docName}"/></a>
                                    </span>
                                    <span class="docdate"><fmt:formatDate value="${item.properties.startDate}" pattern="MM/dd/yyyy hh:mm"/></span>
                                    <span class="docdate"><fmt:formatDate value="${item.properties.endDate}" pattern="MM/dd/yyyy hh:mm"/></span>
                                    <span class="actions">
                                        <a href="#" onClick='CItems.removeItem(${item.id});'
                                           class='trash items_action' title="Remove the item from campaign">Remove</a>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
                </c:if>
                <c:if test="${fn:length(documents) eq 0}">
                    <span style="padding:5px;">No documents are associated to the campaign.</span>
                </c:if>
        </fieldset>

        <!-- Render Image -->
        <fieldset>
            <legend><strong>Image</strong></legend>
            <c:if test="${fn:length(images) gt 0}">
                <ul style="margin-top: 10px;border: 1px #000000 solid;">
                    <li class="header" onclick="CItems.toggleItemsBlock('c_images');" title="Click to minimize/maximize">
                        <span class="doctitle">Title</span>
                        <span class="docurldbl">URL</span>
                        <span class="actions">Actions</span>
                    </li>
                    <li class="items" id="c_images">
                        <ul <c:if test='${(fn:length(images)) > 5}'> style="margin:0;height: <c:out value='${(fn:length(images)*30)}'/>px; overflow-y: scroll;"</c:if>>
                            <!-- Render Image -->
                            <c:forEach items="${images}" var="item">
                                <li>
                                    <span class="${item.itemType} doctitle">
                                        <a target="new" href="<c:url value='/admin/images/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                    </span>
                                    <span class="docurldbl">${item.url}</span>
                                    <span class="actions"">
                                        <a href="#" onClick='CItems.removeItem(${item.id});'
                                           class='trash items_action' title="Remove the item from campaign">Remove</a>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
            </c:if>
            <c:if test="${fn:length(documents) eq 0}">
                <span style="padding:5px;">No images are associated to the campaign.</span>
            </c:if>
        </fieldset>

        <!-- Render Link -->
        <fieldset>
            <legend><strong>Links</strong></legend>
            <c:if test="${fn:length(links) gt 0}">
                <ul style="margin-top: 10px;border: 1px #000000 solid;">
                    <li class="header" onclick="CItems.toggleItemsBlock('c_links');" title="Click to minimize/maximize">
                        <span class="doctitle">Title</span>
                        <span class="docurldbl">URL</span>
                        <span class="actions">Actions</span>
                    </li>
                    <li class="items" id="c_links">
                        <ul <c:if test='${(fn:length(links)) > 5}'> style="margin:0;height: <c:out value='${(fn:length(links)*30)}'/>px; overflow-y: scroll;"</c:if>>
                            <!-- Render Links -->
                            <c:forEach items="${links}" var="item">
                                <li>
                                    <span class="${item.itemType} doctitle">
                                        <a target="new" href="<c:url value='/admin/link/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                    </span>
                                    <span class="docurldbl">${item.url}</span>
                                    <span class="actions">
                                        <a href="#" onClick='CItems.removeItem(${item.id});'
                                           class='trash items_action' title="Remove the item from campaign">Remove</a>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
            </c:if>
            <c:if test="${fn:length(documents) eq 0}">
                <span style="padding:5px;">No images are associated to the campaign.</span>
            </c:if>
        </fieldset>

        <!-- Render Video -->
        <fieldset>
            <legend><strong>Video</strong></legend>
            <c:if test="${fn:length(videos) gt 0}">
                <ul style="margin-top: 10px;border: 1px #000000 solid;">
                    <li class="header" onclick="CItems.toggleItemsBlock('c_videos');" title="Click to minimize/maximize">
                        <span class="doctitle">Title</span>
                        <span class="docurl">Image</span>
                        <span class="docurl">URL</span>
                        <span class="actions">Actions</span>
                    </li>
                    <li class="items" id="c_videos">
                        <ul <c:if test='${(fn:length(videos)) > 5}'> style="margin:0;height: <c:out value='${(fn:length(videos)*30)}'/>px; overflow-y: scroll;"</c:if>>
                            <!-- Render Video -->
                            <c:forEach items="${videos}" var="item">
                                <li>
                                    <span class="${item.itemType} doctitle">
                                        <a target="new" href="<c:url value='/admin/images/edit.do?id=${item.itemID}'/>">${item.itemName}</a>
                                    </span>
                                    <span class="docurl">${item.image}</span>
                                    <span class="docurl">${item.url}</span>
                                    <span class="actions">
                                        <a href="#" onClick='CItems.removeItem(${item.id});'
                                           class='trash items_action' title="Remove the item from campaign">Remove</a>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
            </c:if>
            <c:if test="${fn:length(documents) eq 0}">
                <span style="padding:5px;">No images are associated to the campaign.</span>
            </c:if>
        </fieldset>

    </div>
<fieldset style="padding: 10px;">
    <legend><strong>Items Filter</strong></legend>
    <!-- Campaign items selection popup - START -->
    <div id="items_filter" title="Choose items for campaign" style="height:30px;">
        <div id="search_panel">
            <form name="search_items_form" id="search_items_form" action="searchItems.json"
                  onsubmit="CItems.applyFilter(); return false;">
                <div id="filter_options" style="padding-right:10px;float: left;width: 87%;">
                    <div id="filter_item">
                        <select id="itemType" name="type" onchange="CItems.showFilterOptions(event);"
                                style="width: 100px;margin-right: 10px;float: left;">
                            <option value="">-</option>
                            <option value="product">Product</option>
                            <option value="event">Event</option>
                            <option value="document">Document</option>
                            <option value="video">Video</option>
                            <option value="image">Image</option>
                            <option value="link">Link</option>
                        </select>
                        <div id="product_filters" style="display: none;"></div>

                        <div id="video_filters" style="display: none;"></div>

                        <div id="event_doc_filters" style="display: none;">
                            <span>Key Word: <input style="width:90px;" type="text" id="keyword"></span>
                            <span>Start Date: <input style="width:90px;" readonly="true" autocomplete="off" type="text" id="startDate"></span>
                            <span>End Date:<input style="width:90px;" readonly="true"  type="text" id="endDate"></span>
                            <span id="location_field" style="display: none;">Location:<input type="text" id="location" style="width:90px;"></span>
                        </div>
                    </div>
                </div>
                <div style="float: right;padding-left:10px;padding-bottom:10px">
                    <input type="button" class="search" title="Click to search" onclick="CItems.applyFilter();">
                    <input type="button" class="cancel" title="Reset the search filter" onclick="CItems.resetFilters();">
                </div>
            </form>
        </div>
    </div>
    <!-- Campaign items selection popup - END -->

    <!-- Place holder for displaying the filtered results : START -->
    <div id="items_container" style="padding-top: 10px;display: none;">
        <div id="filtered_results_panel" style="height: 300px;overflow-x: scroll;padding: 5px;width: 100% !important;">
            <div class="sh_container">
                <table class="datatable sh_header" style="padding-top: 10px; width: 100%;">
                    <thead id="filtered_items_header"></thead>
                </table>
                <div class="sh_body">
                    <table class="datatable">
                        <tbody id="filtered_items"></tbody>
                    </table>
                </div>

            </div>
        </div>
        <div id="item_action_buttons" style="padding-top: 10px;">
            <input type="button" class="gbutton" value="Add Items"
                   onclick="CItems.saveItems(<%=campaignID%>)">
            <input type="button" class="gbutton" value="Reset" onclick="CItems.resetFilters();">
        </div>
    </div>
    <span id="no_items" style="color:red;display: none;"><strong>No items matching the criteria</strong></span>
    <!-- Place holder for displaying the filtered results : END -->
</fieldset>

<!-- Campaign items review selection popup - START -->
<div id="item-review-popup" style="display: none;">
    <div class="pop_up_bg"></div>
    <div id="reviews_table" class="reviews_table">
        <div class="popup_header">
            <span class="pop_up_title">
                Choose Reviews
                &nbsp;&nbsp;
                -- Minimum Star Rating&nbsp;&nbsp;<select id="starCount" onchange="CIReview.updateReviews()">
                    <option value="5">5</option>
                    <option value="4">4</option>
                    <option value="3" selected="selected">3</option>
                    <option value="2">2</option>
                    <option value="1">1</option>
                    <option value="0">0</option>
                </select>
            </span>
            <span class="ui-icon ui-icon-closethick" onclick="CIReview.closePopup(CIReview.campaignItemID);">Close</span>
        </div>
        <div class="popup_content" id="popup_content">
            <!-- Place holder for item reviews from server -->
            <table id="reviews_list"></table>
        </div>
        <div class="popup_actions">
            <span onclick="CIReview.closePopup(CIReview.campaignItemID);">Close</span>
            <span onclick="CIReview.postSelectedReviews(CIReview.campaignItemID, CIReview.itemID);">OK</span>
        </div>
    </div>
</div>
<!-- Campaign items review selection popup - END -->
</c:otherwise>
</c:choose>