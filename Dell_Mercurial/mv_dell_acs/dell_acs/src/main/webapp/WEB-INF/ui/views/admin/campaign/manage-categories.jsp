<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>

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
<div id="container" style="padding:20px;">
<fieldset style="padding: 10px;height: 100%;">
    <legend><strong>${campaign.name}</strong></legend>
    <div id="selected_items" class="demo" style="padding-top:10px;padding-bottom:20px;height:100%;">
        <!-- render items here -->
        <ul>
            <li id="product_root_node" rel="container">
                <a href="#"><strong>Items</strong><%-- [ <em>${fn:length(items)} items </em>]--%></a>
                <ul>
                    <c:forEach items="${items}" var="item">
                        <li class="leaf_node" id="${item.id}" nodeType="product" rel="product" class="menu_item">
                            <a title="${item.itemName}" href="#">
                                <c:if test="${fn:length(item.itemName) > 100}">
                                    ${fn:substring(item.itemName, 0, 100)}... (${item.itemID})
                                </c:if>
                                <c:if test="${fn:length(item.itemName) < 100}">
                                    ${item.itemName} (${item.itemID})</a>
                                </c:if>
                                <c:if test="${item.price > 0}">
                                    <strong>$${item.price}</strong>
                                </c:if>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </li>

            <li id="events_root_node" rel="container">
                <a href="#"><strong>Events</strong><%-- [ <em>${fn:length(items)} items </em>]--%></a>
                <ul>
                    <c:forEach items="${events}" var="item">
                        <li class="leaf_node" id="${item.id}" nodeType="event" rel="event" class="menu_item">                            
                            <c:if test="${fn:length(item.itemName) > 100}">
                                <a title="${item.itemName}"href="#">${fn:substring(item.itemName, 0, 100)}... (${item.itemID})</a>
                            </c:if>
                            <c:if test="${fn:length(item.itemName) < 100}">
                                <a title="${item.itemName}"href="#">${item.itemName} (${item.itemID})</a>
                            </c:if>
                            <c:if test="${item.price > 0}">
                                <a title="${item.itemName}" href="#"></a><strong>$${item.price}</strong></a>
                            </c:if>                            
                        </li>
                    </c:forEach>
                </ul>
            </li>

            <li id="document_root_node" rel="container">
                <a href="#"><strong>Documents</strong><%-- [ <em>${fn:length(items)} items </em>]--%></a>
                <ul>
                    <c:forEach items="${documents}" var="item">
                        <li class="leaf_node" id="${item.id}" nodeType="document" rel="document" class="menu_item">                            
                            <c:if test="${fn:length(item.itemName) > 100}">
                                <a title="${item.itemName}"href="#">${fn:substring(item.itemName, 0, 100)}... (${item.itemID})</a>
                            </c:if>
                            <c:if test="${fn:length(item.itemName) < 100}">
                                <a title="${item.itemName}" href="#">${item.itemName} (${item.itemID})</a>
                            </c:if>                            
                        </li>
                    </c:forEach>
                </ul>
            </li>

            <li id="image_root_node" rel="container">
                <a href="#"><strong>Images</strong><%-- [ <em>${fn:length(items)} items </em>]--%></a>
                <ul>
                    <c:forEach items="${images}" var="item">
                        <li class="leaf_node" id="${item.id}" nodeType="image" rel="image" class="menu_item">
                            <c:if test="${fn:length(item.itemName) > 100}">
                                <a title="${item.itemName}"href="#">${fn:substring(item.itemName, 0, 100)}... (${item.itemID})</a>
                            </c:if>
                            <c:if test="${fn:length(item.itemName) < 100}">
                                <a title="${item.itemName}" href="#">${item.itemName} (${item.itemID})</a>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </li>

            <li id="link_root_node" rel="container">
                <a href="#"><strong>Links</strong><%-- [ <em>${fn:length(items)} items </em>]--%></a>
                <ul>
                    <c:forEach items="${links}" var="item">
                        <li class="leaf_node" id="${item.id}" nodeType="link" rel="link" class="menu_item">
                            <c:if test="${fn:length(item.itemName) > 100}">
                                <a title="${item.itemName}"href="#">${fn:substring(item.itemName, 0, 100)}... (${item.itemID})</a>
                            </c:if>
                            <c:if test="${fn:length(item.itemName) < 100}">
                                <a title="${item.itemName}" href="#">${item.itemName} (${item.itemID})</a>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </li>

            <li id="video_root_node" rel="container">
                <a href="#"><strong>Videos</strong><%-- [ <em>${fn:length(items)} items </em>]--%></a>
                <ul>
                    <c:forEach items="${videos}" var="item">
                        <li class="leaf_node" id="${item.id}" nodeType="video" rel="video" class="menu_item">
                            <c:if test="${fn:length(item.itemName) > 100}">
                                <a title="${item.itemName}"href="#">${fn:substring(item.itemName, 0, 100)}... (${item.itemID})</a>
                            </c:if>
                            <c:if test="${fn:length(item.itemName) < 100}">
                                <a title="${item.itemName}" href="#">${item.itemName} (${item.itemID})</a>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </li>

        </ul>
    </div>
</fieldset>

<fieldset style="padding: 10px;height: 100%;margin-top: 20px;">
    <legend><strong>Output Result Formatter</strong></legend>
    <div style="height: 100%">
        <span id="help" class="help" title="How-to manage categories and items.">Help</span>
        <div>
            Create custom categories using category and format the output result for this campaign.<br></br>
            <!-- <p style="overflow-wrap: normal;"><strong>Note:</strong><br></br>In re-ordering process please
        drop the dragged item upon another item which will will be used as a reference node for calculating the
        position of dropped node.</p> -->
        </div>
        <div>
            <span class="add category_action" id="create" title="Add category">Add Category</span>
            <span class="edit category_action" id="rename" title="Edit category name">Edit Category</span>
            <span class="remove category_action" id="delete" title="Remove category">Remove Category</span>
            <span class="refresh category_action" id="refresh" title="Reload the category tree">Reload</span>
            <!-- Error / success message place holders -->
            <span id="category-error-box" style="color: red;"></span>
            <span id="category-success-box" style="color: green;"></span>
            <%-- <a class="save category_action" id="save" title="Save Tree">Save</a>--%>
        </div>
        <div id="categories" style="margin-top: 10px;height:100%;padding-bottom: 20px;"></div>
    </div>
</fieldset>

<script type="text/javascript">
$(function () {
    var targetNode = undefined;
    var treeReload = false;
    var TREE_MODEL = "";
    var nodeData = {};

    $("#selected_items").jstree({
        core:{ html_titles : true },
        ui : { select_limit : 1 },
        crrm:{
            move:{
            	always_copy : true,
                check_move : function (node) {
                    var object = node.o;
                    var nodeType = object.attr("nodeType");
                    var relAttr = object.attr("rel");
                    //window.console.log(nodeType, relAttr);
                    // Disable 'container' items dnd
                    if (nodeType == "category") {
                        return false;
                    }
                    // Don't allow nodes from Category tree to drop in selected items tree
                    else if (nodeType == "category" && relAttr == 'container') {
                        return false;
                    }
                    // Allow only 'items' type nodes
                    else if (nodeType == "product" && relAttr == 'product') {
                        return false;
                    }
                    // Ignore if the target drop node is Selected Items - ROOT node
                    else if (node.cr == -1 || node.cr != null) {
                        return false;
                    }
                    // Default false - do not allow anything
                    else {
                        return false;
                    }
                }
            }
        },
        types:{
            valid_children:[ "container" ],
            root:{
//                            icon:{
//                                image:"http://static.jstree.com/v.1.0rc/_docs/_drive.png"
//                            },
                valid_children:[ "container" ],
                hover_node:false,
                select_node:function () {
                    return false;
                }
            },
            container:{
//                            icon:{
//                                image:"http://static.jstree.com/v.1.0rc/_docs/_drive.png"
//                            },
                valid_children:[ 'product' ]
            }
        },
        plugins:[ "themes", "html_data", "dnd", "ui", "crrm", "types" ]

    });

    var getCampaignCategories = function(isRefresh){
        $.ajax({ url:'category/campaign-categories.json',
            data:{ campaignID:requestParams.campaignID },
            dataType:"json",
            success:function (response, status) {
                TREE_MODEL = response;
                if(! isRefresh){
                    buildCategoryTree(response);
                }else{
                    //Refresh the tree
                    window.console.log('Refresh tree data');
                    $.jstree._reference('#categories')._get_settings().json_data.data=TREE_MODEL;
                    $("#categories").jstree("refresh");
                    $.jstree._reference("#categories").open_all();
                    $.jstree._reference("#categories").deselect_all();
                }
            },
            error:function (e) {
                window.console.log('Unable to process the request', e);
                if(e.readyState == 4){
                    window.location.reload();
                }
            }
        });
    };

    var movedNodePosition = "last";
    var referenceNode = undefined;
    var selectedNode = undefined;


    var buildCategoryTree = function (treeModelData) {
        // Create js-tree instance
        $("#categories").jstree({
            json_data:{
                ajax: false,
                data:treeModelData
            },
            core:{
                html_titles: true,
                strings:{ new_node:"New Category" }
            },
            dnd:{
                open_timeout:50,
                drag_check:function (data) {
                    if (data.r.attr("nodeType") == "product"
                            || data.r.attr("nodeType") == "event"
                            || data.r.attr("nodeType") == "image"
                            || data.r.attr("nodeType") == "link"
                            || data.r.attr("nodeType") == "video"
                            || data.r.attr("nodeType") == "document") {
                        return { inside:true };
                    }
                    return false;
                }
            },
            types:{
                valid_children:[ "root" ],
                //max_depth: 6,
                root:{
                    // <%--icon:{--%>
                    // <%--image:"<c:url value='/images/root_category.png'/>"--%>
                    // <%--},--%>
                    valid_children: [ "container" ],
                    hover_node:true,
                    select_node:function () {
                        return true;
                    }
                },
                container:{
                    // <%--icon:{--%>
                    // <%--image:"<c:url value='/images/category.png'/>"--%>
                    // <%--},--%>
                    valid_children: [ 'product', 'container' ]
                },
                items:{
                    <%--icon:{--%>
                    <%--image:"<c:url value='/images/document.png'/>"--%>
                    <%--}--%>
                }
            },
            crrm:{
                move:{
                	always_copy : true,
                    check_move : function (node) {
                        var allowNodeMove = false;
                        // Reset the node data set previously
                        nodeData = {};

                        // Scenarios for dropping a node from items tree to category tree
                        var nodeObject = node.o;
                        var targetParentObject = node.cr;   // same as 'node.cr'
                        var sourceParentObject = node.op

                        var movedNodePosition = node.p;
                        var referenceNode = node.r[0];

                        // Disallowing the Selected items root node DND
                        if(nodeObject.attr("id") == "product_root_node"
                                || nodeObject.attr("id") == "events_root_node"
                                || nodeObject.attr("id") == "image_root_node"
                                || nodeObject.attr("id") == "link_root_node"
                                || nodeObject.attr("id") == "video_root_node"
                                || nodeObject.attr("id") == "document_root_node"){
                            return false;
                        }




                        // Update the reference node details
                        if(referenceNode != undefined && movedNodePosition != undefined){
                            nodeData.position=movedNodePosition;
                            nodeData.referenceNodeID=referenceNode.attributes.getNamedItem('id').value;
                            nodeData.referenceNodeType=referenceNode.attributes.getNamedItem('nodeType').value
                        }else{
                            nodeData.position='last';
                        }

                        // If drop target is a ROOT node the don't allow to drop the item
                        if (targetParentObject == -1 && referenceNode != undefined
                                && referenceNode.attributes != undefined
                                && referenceNode.attributes.getNamedItem('nodeType').value != "category") {
                            console.log('Disallow the item drop on ROOT nodes.');
                            return false;
                        }

                        // Retrieve the dragged node details
                        var nodeType = nodeObject.attr("nodeType");
                        var nodeID = nodeObject.attr("id");
                        var nodeParentID = nodeObject.attr("parentID");
                        var nodeParentType = nodeObject.attr("parentType");

                        // Set the node details
                        nodeData.nodeType= nodeType;
                        nodeData.nodeID= nodeID;


                        // Disallow the dropping of Categories on-to an Item
                        /*  Node Detail -
                         nodeType: "category"
                         parentID: "107"
                         parentType: undefined
                         position: "before"
                         referenceNodeID: "1163"
                         referenceNodeType: "product"
                         */
                        if(nodeData.nodeType == "category" && ( nodeData.referenceNodeType == "product"
                                || nodeData.referenceNodeType == "event"
                                || nodeData.referenceNodeType == "image"
                                || nodeData.referenceNodeType == "link"
                                || nodeData.referenceNodeType == "video"
                                || nodeData.referenceNodeType == "document" )){
                            console.log("Disallow the dropping of Categories on-to an Item");
                            return false;
                        }

                        // Disallow if the item is dropping above the Category node
                        /* Node Details -
                         action: "addItem"
                         newParentID: "107"
                         newParentType: "category"
                         nodeID: "1168"
                         nodeType: "product"
                         parentID: "108"
                         parentType: "category"
                         position: "before"
                         referenceNodeID: "108"
                         referenceNodeType: "category"
                         */
                        if(nodeData.referenceNodeType == "category" && ( nodeData.nodeType == "product"
                                || nodeData.nodeType == "event"
                                || nodeData.nodeType == "image"
                                || nodeData.nodeType == "link"
                                || nodeData.nodeType == "video"
                                || nodeData.nodeType == "document" ) && nodeData.position == "before" ){
                            console.log("Disallow if the item is dropping above the Category node");
                            return false;
                        }



                        // Retrieve the targetNode data
                        // Reference node is NOT null and node.cr == -1 then its a add to category ( referring - referenceNode)
                        var targetParentType, targetParentID;
                        if(referenceNode != null || referenceNode != undefined){
                            // Drop on category node
                            if(targetParentObject == -1){
                                targetParentType = referenceNode.attributes.getNamedItem('nodeType').value;
                                nodeData.newParentType = targetParentType;
                                targetParentID = referenceNode.attributes.getNamedItem('id').value;
                                nodeData.newParentID = targetParentID;
                                // add the item to category - referenceNode
                                nodeData.action="addItem";
                                allowNodeMove = true;
                                console.log("Item is dropped to a specific position in category node.");
                            }
                            nodeData.parentID = nodeParentID;
                            nodeData.parentType=nodeParentType;
                        }

                        // If reference node is NULL then its a item drop on category node
                        if (targetParentObject != -1 && targetParentObject != undefined &&
                                targetParentObject != null) {
                            if(nodeData.newParentType == undefined){
                                nodeData.newParentType = targetParentObject.attr("nodeType");
                            }
                            if(nodeData.newParentID == undefined){
                                nodeData.newParentID = targetParentObject.attr("id");
                            }
                            nodeData.action="addItem";
                            allowNodeMove = true;
                            console.log("Item is dropped on category node. Item will be added in last position");
                        }

                        // Check for item drop on a ITEM node - START

                        // Check for drop on the item rather in between items or on category
                        /*
                         action: "addItem"
                         newParentID: "847"
                         newParentType: "document"
                         nodeID: "844"
                         nodeType: "product"
                         position: "last"
                         referenceNodeID: "847"
                         referenceNodeType: "document"
                         */
                        if(nodeData.newParentType == "product"
                                ||  nodeData.newParentType == "document"
                                ||  nodeData.newParentType == "image"
                                ||  nodeData.newParentType == "link"
                                ||  nodeData.newParentType == "video"
                                ||  nodeData.newParentType == "event"){
                            console.log("Disallow the item drop on items");
                            return false;
                        }
                        // Check for item drop on a ITEM node -  END


                        // Check for items ( 'product', 'document' and 'even' ) that are allowed to be
                        // dropped within or on 'Category' node
                        // ITEMS DnD - START
                        else if (targetParentType == 'category'  && !allowNodeMove ) {
                            // If target is 'category' then check for the nodeType
//                            if(nodeType == "product"){
//                                window.console.log('ALLOWED to drop for node - ' + targetParentID);
//                                targetNode = targetParentObject;
//                                allowNodeMove = true;
//                            }
//                            // Allow only 'event' type nodes - DnD
//                            else if (nodeType == "event") {
//                                window.console.log(nodeType + ' is allowed to drop for node - ' + targetParentID);
//                                targetNode = targetParentObject;
//                                allowNodeMove = true;
//                            }
//                            // Allow only 'document' type nodes - DnD
//                            else if (nodeType == "document") {
//                                window.console.log('Document is allowed to drop for node - ' + targetParentID);
//                                targetNode = targetParentObject;
//                                allowNodeMove = true;
//                            }
                            // Allow only 'product, document, event, image, link, video' type nodes - DnD
                            if (nodeType == "product"
                                    || nodeType == "document"
                                    || nodeType == "event"
                                    || nodeType == "image"
                                    || nodeType == "link"
                                    || nodeType == "video") {
                                window.console.log('Document is allowed to drop for node - ' + targetParentID);
                                targetNode = targetParentObject;
                                allowNodeMove = true;
                            }
                        }
                        // ITEMS DnD - END

                        // CHECK - To switch between the actions - START
                        // moveItem - Move Item Action
                        if(nodeData.parentType == "category" && nodeData.parentID == nodeData.newParentID){
                            nodeData.action="moveItem";
                        }

                        // move - Move Category Action
                        /*
                         action: "move"
                         newParentID: "112"
                         newParentType: "category"
                         nodeID: "114"
                         nodeType: "category"
                         parentID: "113"
                         parentType: undefined
                         position: "last"
                         referenceNodeID: "112"
                         referenceNodeType: "category"
                         */
                        if(nodeData.newParentType == nodeData.referenceNodeType && nodeData.nodeType =="category"){
                            nodeData.action="move";
                        }
                        // CHECK - To switch between the actions - END

                        // If valid container then return true
                        if(allowNodeMove){
                            if(targetParentType != undefined){
                                nodeData.newParentType= targetParentType;
                            }
                            if(targetParentID != undefined){
                                nodeData.newParentID= targetParentID;
                            }
                            return true;
                        }else{
                            // Any other scenarios is NOT SUPPORTED( Need to include if missed any)
                            window.console.log('NOT ALLOWED BY DEFAULT - FOR ALL UN-HANDLED VALUES');
                            return false;
                        }
                    }
                }
            },
            plugins:[ "themes", "dnd", "ui", "crrm", "json_data", "types"/*, "sort", "search"*/]

        });

        setTimeout(function(){
            $.jstree._reference('#categories')._get_settings().json_data.data=TREE_MODEL;
            $.jstree._reference('#categories').refresh();
            $.jstree._reference("#categories").open_all();
        }, 1000);

        // Bind the events  to 'categories' tree

        // Move node
        $("#categories").bind("move_node.jstree", function (e, data) {
            //var node = prepareMovedNodeData(e, data);
            window.console.log('MOVE NODE -> ', nodeData);

            /*
             action: "addItem"
             newParentID: "111"
             newParentType: "category"
             nodeID: "1222"
             nodeType: "product"
             parentID: undefined
             parentType: undefined
             position: "last"
             referenceNodeID: "111"
             referenceNodeType: "category"
             */
            // Default params
            var params = {
                itemID:nodeData.nodeID,
                //id: nodeData.newParentID,
                parentID: nodeData.newParentID,
                oldParentID:nodeData.parentID,
                referenceNodeID:nodeData.referenceNodeID,
                campaignID:requestParams.campaignID
            };



            // Set category moveItem related action parameters
            if(nodeData.action == 'moveItem'){
                console.log("Change the item " + nodeData.nodeID + " order and place "+ nodeData.position + " " + nodeData.referenceNodeID);
                params.id = nodeData.nodeID; // node.nodeID; // Moving item id
                params.parentID = nodeData.parentID; //node.oldParentID; // Ordering item category ID
                params.moved = nodeData.position; //movedNodePosition; // after/before reference node
                params.referenceNodeID = nodeData.referenceNodeID; //referenceNode.id; // Reference node
                params.action = nodeData.action; //'moveItem'; // action
            }
            // Set category move related action parameters
            else if(nodeData.action == "move"){
                // categoryID = itemID; // Source Category which is being moved under a new parent category
                // targetCategoryID = id; // Target Category
                console.log("Moving category (" + nodeData.nodeID + ") from " + nodeData.parentID + " category to " + nodeData.newParentID);
                params.id = nodeData.newParentID;
                params.itemID = nodeData.nodeID;
            }

            // Set default action 'addItem' ONLY if its a category move then action param is updated below
            if(params.action == undefined && nodeData.action == undefined){
                console.log("Add item " + nodeData.nodeID + " to category "+ nodeData.parentID);
                params.action = 'addItem';
            }else{
                params.action = nodeData.action;
            }

            /* Update the position parameter */
            if(nodeData.moved == undefined){
                params.moved = nodeData.position;
            }

            if(params.moved == "before" || params.moved == "after"){
                params.referenceNodeID = nodeData.referenceNodeID;
            }

            $.ajax({
                url:"category/category-actions.json",
                dataType:"json",
                data:params,
                type:'POST',
                success:function (response, status) {
                    var message = "Item added to category successfully !";
                    if(params.action == "move"){
                        message = "Item was moved successfully to a category!";
                    }else if(params.action == "moveItem"){
                        message = "Item was ordered successfully !";
                    }
                    // Reset the data
                    nodeData = {};
                    handleSuccess('categories', message);
                },
                error:function (xhr, tStatus, err) {
                    if(err.readyState == 4){
                        window.location.reload();
                    }else{
                        handleError(xhr.responseText, 'categories');
                    }
                }
            });
        });

        // Create node listener binding
        $("#categories").bind("create.jstree", function (e, data) {
            var name = data.rslt.name;
            var parent = data.rslt.parent;
            var object = data.rslt.obj;
            var position = data.rslt.position;
            var childID;
            var parentID = -1;
            var parentType = undefined;
            if (parent != -1) {
                parentID = parent.attr("id");
                parentType = parent.attr("nodetype");
            }

            // Return a message can't create and then move the tree associated entities - product, event, document
            // and upcoming entities to a static array
            if(parentType != undefined && (
                    parentType == 'product' ||
                    parentType == 'document' ||
                    parentType == 'event' ||
                    parentType == 'image' ||
                    parentType == 'link' ||
                    parentType == 'video' )){
                handleError("Can't create a category under an item.");
                jQuery.jstree.rollback(data.rlbk);
                return false;
            }

            var params = {
                name:name,
                position:position,
                parentID:parentID,
                campaignID:requestParams.campaignID,
                action:'create'
            };

            // Check if the user is trying to create a category on the item .

            $.ajax({
                url:"category/category-actions.json",
                dataType:"json",
                // a - action param [ 1 = Create, 2 = Delete, 3 = Update ] are possible values
                data:params,
                type:'POST',
                success:function (response, status) {
                    // Update the properties for the newly created node
                    childID = response.data.id;
                    parentID = response.data.parentID;
                    object.attr("parentID", parentID);
                    // ID will be a categoryID
                    object.attr("id", childID);
                    window.console.log("name", name, " id - " + childID, ' Parent - ', parentID, "position - ", position);
                    handleSuccess('categories', 'Category created successfully !');
                },
                error:function (xhr, tStatus, err) {
                    window.console.log('Unable to process the request', err);
                    if(err.readyState == 4){
                        window.location.reload();
                    }else{
                        handleError(xhr.responseText, 'categories');
                    }
                }
            });
        });

        // Rename node listener binding
        $("#categories").bind("rename.jstree", function (e, data) {
            // new_name: "Update - Node1"
            // old_name: "Node1"
            var name = data.rslt.new_name;
            var object = data.rslt.obj;
            // Category object id
            var id = object.attr("id");
            window.console.log('RENAME - ', data.rslt.old_name, ' to ', name, " ID - ", object.attr("id"));
            $.ajax({
                url:"category/category-actions.json",
                dataType:"json",
                data:{ name:name, id:id, campaignID:requestParams.campaignID, action:'rename' },
                type:'POST',
                success:function (response, status) {
                    handleSuccess('categories', 'Category renamed successfully !');
                },
                error:function (xhr, tStatus, err) {
                    window.console.log('Unable to process the request', err);
                    if(err.readyState == 4){
                        window.location.reload();
                    }else{
                        handleError(xhr.responseText, 'categories');
                    }

                }
            });
        });

        // Remove node listener binding
        $("#categories").bind("remove.jstree", function (e, data) {
            var successMessage = 'Category removed successfully !';
            var object = data.rslt.obj;
            var node = new Object();
            node.nodeID = object.attr("id");
            node.nodeType = object.attr("nodeType");
            node.parentID = object.attr("parentID");

            window.console.log('REMOVE - ID : ', object.attr("id"));
            var params = {
                id: node.nodeID,
                campaignID:requestParams.campaignID,
                action:'remove'
            };

            if(node.nodeType == 'product'
                    || node.nodeType == 'event'
                    || node.nodeType == 'document'
                    || node.nodeType == 'image'
                    || node.nodeType == 'link'
                    || node.nodeType == 'video' ){
                node.parentID = object.attr("parentID");
                params.parentID = node.parentID;
                successMessage = "Item successfully removed form category !!"
            }

            var ins = $.jstree._reference('#categories'); //._get_settings().json_data.data
            //delete_node

            //persistJSONTree('#categories', function(){
                ///console.log("Within call-back method - ", params);
            //});
            $.ajax({
                url:"category/category-actions.json",
                dataType:"json",
                data:params,
                type:'POST',
                success:function (response, status) {
                    handleSuccess('categories', successMessage);
                },
                error:function (xhr, tStatus, err) {
                    if(err.readyState == 4){
                        window.location.reload();
                    }else{
                        handleError(xhr.responseText, 'categories');
                    }
                }
            });
        });
    };

    // Returns Node object with the related information of parent(NewParent and OldParent in case of dnd) and currentlt
    // selected node
    //@Deprecated
    var getNodeData = function (nodeObject) {
        var node = new Object();
        // Node details
        var itemID = nodeObject.data.obj.attr("id");
        var nodeType = nodeObject.data.obj.attr("nodeType");
        node.nodeID = itemID;
        node.nodeType = nodeType;

        // Source node details
        var dragNodeParentID = nodeObject.data.obj.attr("parentID");
        node.oldParentID = dragNodeParentID;
        node.oldParentNodeType = nodeObject.data.obj.attr("nodeType");

        // Target node details - Useful only when the drag is for 'items' type of node
        var targetParentObject = nodeObject.event.target.parentNode;
        var dropNodeElementAttributes = targetParentObject.attributes;
        var dropNodeType = undefined;
        var dropNodeParentID = undefined;
        if (dropNodeElementAttributes != undefined ){
            if(dropNodeElementAttributes.getNamedItem('nodeType') != null){
                dropNodeType = dropNodeElementAttributes.getNamedItem('nodeType').value;
            }
            if(dropNodeElementAttributes.getNamedItem('id') != null){
                dropNodeParentID = dropNodeElementAttributes.getNamedItem('id').value;
            }
        }

        // If dropNodeParentID parent is null in nodeObject fetch the value from 'targetNode'
        if (dropNodeParentID == '' || dropNodeParentID == null || dropNodeParentID == undefined) {
            window.console.log(' Try to get the parent node from targetNode object : ', targetNode);
            if(targetNode != null){
                dropNodeParentID = targetNode.attr("id");
                dropNodeType = targetNode.attr("nodeType");
            }
        }

        node.newParentID = dropNodeParentID
        node.newParentNodeType = dropNodeType;
        return node;
    };

    getCampaignCategories(false);

    var persistJSONTree = function (treeID, callback) {
        var ins = $.jstree._reference(treeID);
        var data = window.JSON.stringify(ins.get_json(ins.get_container_ul()));
        $.ajax({
            url:"category/save-category-tree.json",
            data:{ treeData: data, campaignID:requestParams.campaignID },
            dataType:"json",
            type:'POST',
            success:function (response, status) {
                handleSuccess('categories', 'Category tree updated successfully !');
                if($.isFunction(callback)){
                    callback();
                }
            },
            error:function (xhr, tStatus, err) {
                if(err.readyState == 4){
                    window.location.reload();
                }else{
                    handleError(xhr.responseText, 'categories');
                }
            }
        });
    };

    // Return the tree instance.
    var getInstance = function (treeID) {
        var instance = undefined;
        if(treeID == undefined || treeID == ''){
            treeID = 'catgories';
        }
        if (treeID.indexOf('#') == -1) {
            treeID = '#'.concat(treeID);
        }
        instance = $.jstree._reference(treeID);
        return instance;
    };


    // Create button 'click' listener
    $("#create").click(function () {
        $("#categories").jstree("create");
    });
    // Rename button 'click' listener
    $("#rename").click(function () {
        $("#categories").jstree("rename");
    });

    $("#save").click(function () {
        persistJSONTree('#categories', $.noop());
    });

    // Delete button 'click' listener
    $("#delete").click(function () {
        var cnf = confirm('Are you sure you want to delete ?');
        if(cnf){
            $("#categories").jstree("remove");
        }
    });
    // Refresh button 'click' listener
    $("#refresh").click(function () {
        $('#category-success-box').show();
        handleSuccess(undefined, "Category tree refreshed");
        // Destroy the tree and re-build it
        reloadTree('categories');
    });

    $("#help").click(function () {
        var helpUrl = '<c:url value="/category_dnd_help.swf"/>';
        var helpWindow = window.open(helpUrl, "Categories Tree Help", "width=800,height=600");
        helpWindow.onload =  function() { helpWindow.document.title="Manage Categories Help" };

    });

    // Error handler
    var handleError = function (responseMessage, treeID) {
        $('#category-error-box').show();
        $('#category-error-box').html(responseMessage);
        $('#category-error-box').fadeOut(5000);
        // Reset the data
        movedNodePosition = "";
        referenceNode = undefined;
        targetNode = undefined;
        reloadTree(treeID);
    };

    // Success handler
    var handleSuccess = function (treeID, message) {
        $('#category-success-box').empty();
        $('#category-success-box').show();
        // Destroy the tree and re-build it
        if(treeID != undefined){
            reloadTree(treeID);
        }
        $('#category-success-box').html(message);
        $('#category-success-box').fadeOut(5000);
    };

    // Refresh the tree
    var reloadTree = function (treeID) {
        getCampaignCategories(true)
    };

});

// Check for session time-out
$(document).ready(
        function() {
            $("body").ajaxError(
                    function(e,request) {
                        if (e.readyState == 4) {
                            // Reload the window now
                            window.location.reload();
                        }
                    }
            );
        }
);
</script>
</div>
</c:otherwise>
</c:choose>
