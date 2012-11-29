<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>

<script type="text/javascript" src="<c:url value="/js/ManageHelper.js"/>"></script>

${navCrumbs}

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
<div id="container" style="padding:10px;">

<!--  work zone starts -->

<style type="text/css">
    .panel {
        display: inline-block;
        width: 48%;
        height: 25em;
    }
    .panel .jstree-default.jstree-focused {
        background : inherit;
        border : none;
        border-color : lightgrey !important;
    }
</style>

<fieldset class="panel">
    <legend><strong>Categories</strong></legend>
    <div id="categories" style="height: 19em; overflow-x: hidden; overflow-y: auto; border: 1px solid lightgrey;"></div>
    <div style="height: 1.5em; padding: 1em 0px 0px 10px;">
        <input id="createCategory" type="button" class="gbutton" value="Add Category">
        <input id="deleteCategory" type="button" class="gbutton" value="Delete Category">
        
        <!-- Error / success message place holders -->
        <span id="category-error-box" style="color: red;"></span>
        <span id="category-success-box" style="color: green;"></span>
        <%-- <a class="save category_action" id="save" title="Save Tree">Save</a>--%>
    </div>
</fieldset>

<fieldset class="panel" style="float: right;">
    <legend><strong>Items</strong></legend>
    <div id="categoryItems" style="height: 19em; overflow-x: hidden; overflow-y: auto; border: 1px solid lightgrey;"></div>
    <div style="height: 1.5em; padding: 1em 0px 0px 10px;">
        <input id="deleteItem" type="button" class="gbutton" value="Delete Item"">
    </div>    
</fieldset>


<div style="display:block;"> <!--  temporary -->
<fieldset style="height: 20em;">
    <legend><strong>Search</strong></legend>
	<div style="padding: 0px 10px;">
	    <!-- Campaign items selection popup - START -->
	    <div id="items_filter" title="Choose items for campaign" style="height:30px;">
	        <div id="search_panel">
	            <form name="search_items_form" id="search_items_form" action="searchItems.json"
	                  onsubmit="CManageItem.applyFilter(); return false;">
	                <div id="filter_options" style="padding-right:10px;float: left;width: 87%;">
	                    <div id="filter_item">
	                        <select id="itemType" name="type" onchange="CManageItem.showFilterOptions(event);"
	                                style="width: 100px;margin-right: 10px;float: left;">
	                            <option value="">-</option>
	                            <option value="product">Product</option>
	                            <option value="event">Event</option>
	                            <option value="document">Document</option>
	                            <option value="video">Video</option>
	                        </select>
	                        <div id="product_filters" style="display: none;"></div>
	
	                        <div id="video_filters" style="display: none;"></div>
	
	                        <div id="event_doc_filters" style="display: none;">
	                            <span>Keyword: <input style="width:90px;" type="text" id="keyword"></span>
	                            <span>Start: <input style="width:90px;" readonly="true" autocomplete="off" type="text" id="startDate"></span>
	                            <span>End:<input style="width:90px;" readonly="true"  type="text" id="endDate"></span>
	                            <span id="location_field" style="display: none;">Location:<input type="text" id="location" style="width:90px;"></span>
	                        </div>
	                    </div>
	                </div>
	                <div style="float: right;padding-left:10px;">
	                    <input type="button" class="search" title="Click to search" onclick="CManageItem.applyFilter(${campaign.retailerSite.id});">
	                    <input type="button" class="cancel" title="Reset the search filter" onclick="CManageItem.resetFilters();">
	                </div>
	            </form>
	        </div>
	    </div>
	    <!-- Campaign items selection popup - END -->
	
	    <!-- Place holder for displaying the filtered results : START -->
	    <div id="items_container" style="width: 100%; height: 10.5em; margin-top: 10px; overflow-y: auto; overflow-x: hidden; border: 1px solid grey;">
            <div id="results_no_items" style="color: red; display: none; height: 100%;">
                <strong>No items matching the criteria</strong>
            </div>
            <div id="results_items" class="sh_container" style="height: 100%;">
                <table class="datatable sh_header" style="height: 100%; width: 100%;">
                    <thead id="filtered_items_header"></thead>
                    <tbody id="filtered_items"></tbody>
                </table>
            </div>
	    </div>
        <div id="item_action_buttons" style="padding-top: 10px;">
            <input type="button" class="gbutton" value="Add Items to Category" onclick="CManageItem.saveItems(${campaign.id})">
            <input type="button" class="gbutton" value="Clear" onclick="CManageItem.resetFilters();">
        </div>
  	    <!-- Place holder for displaying the filtered results : END -->
	</div>
</fieldset>

<script type="text/javascript">
	$(function () {
	     var helper = manageHelper('#categories', '#categoryItems', '#createCategory', '#deleteCategory', '#deleteItem', requestParams.campaignID);
	     CManageItem = CManageItem(helper);
	});
</script>

<!--  work zone ends -->

</div>
</c:otherwise>
</c:choose>
