
var manageHelper = function (categoryPanelId,         // html id of category container's panel 
                             itemPanelId,             // html id of item container's panel
                             createCategoryButtonId,  // html id of category add button 
                             deleteCategoryButtonId,  // html id of category delete button
                             deleteItemButtonId,      // html id of item delete button
                             campaignId) {            // uid for retailerSite's campaign
    
    var typeContainer = 'container';
    
    var obj = {};
   
    // data model   
    obj.model = function(campaignId) {
        return {
            data : {},
            
            filterContainers : function () {
                function filter(root) {         
                   var result = new Array();
                   
                   for (var e in root) {
                       if (root[e].type == typeContainer) {
                           result[e] = { 
                               data : root[e].title,
                               attr : {
                                        dataId : root[e].id,
                                        datatype : root[e].type                                            
                                      },
                               children : []
                           };
                                                           
                           result[e].children = filter(root[e].children);
                       }
                   }                                   
                   return result; 
               }
               
               return filter(obj.model.data);
            },
            
            _findContainer : function (findId) {
                function search(branch) {
                    for (var e in branch) {
                        if (branch[e].type == typeContainer) {
                            if (branch[e].id == findId) {
                                return branch[e].children;
                            }
                            else {
                                var result = search(branch[e].children);
                                if (result != null)
                                    return result;                                
                            }
                        }
                    }
                    return null;
                }
                
                return search(obj.model.data);                
            },
            
            filterItems : function (containerId) {               
                var result = new Array();
                var root = this._findContainer(containerId);
                    
                if (root != null) {
                    for (var e in root) {
                        if (root[e].type != typeContainer) {
                            result[e] = { 
                                data : root[e].title,
                                attr : {
                                         dataId : root[e].id,
                                         datatype : root[e].type                                            
                                       }
                            };
                        }
                    }     
                }               
                return result;              
            },
            
            _addItems : function (containerId, items) {
                var root = this._findContainer(containerId);
                
                if (root != null) {
                    for (item in items) {
                        root.push({
                           type : items[item].type,
                           id : items[item].id,
                           title : items[item].title
                        });
                    }
                }
            },
            
            _deleteItem : function (containerId, itemId) {
                var root = this._findContainer(containerId);
                
                if (root != null) {
                    for (item in root)
                        if (root[item].id == itemId)
                            root = undefined;
                }
            },
          
            get : function(updateCategoryTree) {
                $.ajax({
                       url : 'manage/tree.json',
                       data : { campaignID : campaignId },
                       dataType : "json",
                       success : function (response, status) {                      
                            obj.model.data = response;
                            updateCategoryTree(obj.model.filterContainers());
                       }
                });   
            }
        };
    }(campaignId);
    
    // trees
    obj.trees = function(categoryPanelId, itemPanelId) {
        var trees = {};
        
        function setPanelContent(panelId, panel, json) {
            var reference = $.jstree._reference(panelId);               
            reference._get_settings().json_data.data = json;
            panel.jstree('refresh');
            reference.open_all();
            reference.deselect_all();               
        }
        
        // item tree panel
        trees.items = function(itemPanelId) {
            var itemPanel = $(itemPanelId);
            
            return {
                tree : itemPanel.jstree({
                    core : {
                        html_titles : true
                    },
                    json_data : {
                        ajax : false,
                        data : new Array(),
                        progressive_render : false
                    },
                    types : {
                        max_depth : 1,
                        valid_children : "all"
                    },
                    ui : {
                        select_limit : 1
                    },
                    dnd : {
                        copy_modifier : false
                    },
                    crrm : {
                        move : {
                            default_position : "before",
                            check_move : function(m) { return true; }  
                        }
                    },
                    plugins : [ "themes", "dnd", "ui", "crrm", "json_data", "types" ]                
                }),
                
                deleteButton : $(deleteItemButtonId).click(function (e) {
                    var node = itemPanel.jstree('get_selected');
                    if (node.length > 0) {
                        node = node.first();
                        
alert('to-do');
//                        itemPanel.jstree('remove', node .first());
                    }
                }),                
                
                _clear : function() {
                    setPanelContent(itemPanelId, itemPanel, new Array());    
                },
                
                _update : function(categoryId) {
                    setPanelContent(itemPanelId, itemPanel, obj.model.filterItems(categoryId));
                }               
            };    
        }(itemPanelId);
            
        // category tree panel
        trees.categories = function(categoryPanelId) {
            var categoryPanel = $(categoryPanelId);
            var idOfSelectedContainer = null;
            return {
                tree : categoryPanel.jstree({
                    core : {
                        html_titles : true,
                        strings : { new_node: "New Category" }
                    },
                    json_data : {
                        ajax : false,
                        data : new Array(),
                        progressive_render : false
                    },
                    ui : {
                        select_limit : 1
                    },
                    dnd : {
                        copy_modifier : false
                    },
                    crrm : {
                        move : {
                            default_position : "before",
                            check_move : function(m) { return true; }  
                        }
                    },                          
                    plugins : [ "themes", "dnd", "ui", "crrm", "json_data", "types" ]
                })
                .bind("dblclick.jstree", function (e) { 
                    // convert double click on container to rename
                    var node = $(e.target).closest("li").first();
                    categoryPanel.jstree('select_node', node);
                    categoryPanel.jstree('rename', node);
                })
                .bind("select_node.jstree", function (e, d) { 
                    // capture node select, show category's items
                    var node = d.rslt.obj.first();
                    trees.items._update(node.attr("dataId"));
                    idOfSelectedContainer = node.attr("dataId");
                })                   
                .bind("before.jstree", function(e, d) {
                    if (d.func === 'close_node') {
                        e.stopImmediatePropagation(); 
                        return false;
                    } 
                }),
                
                createButton : $(createCategoryButtonId).click(function (e) { 
                    categoryPanel.jstree('create'); 
                }),
                                
                deleteButton : $(deleteCategoryButtonId).click(function (e) {
                    var node = categoryPanel.jstree('get_selected');
                    if (node.length > 0) {
                        if (confirm('Are you sure?')) {
                            
// todo -- must delete category and children in model                            
                            
                            categoryPanel.jstree('remove', node.first());
                            $.jstree._reference(categoryPanelId).deselect_all();
                            categoryPanel.jstree('select_node', 'ul > li:first');
                        }
                    }
                }),
                                
                _update : function(json) {
                    trees.items._clear();
                    setPanelContent(categoryPanelId, categoryPanel, json);
                    categoryPanel.jstree('select_node', 'ul > li:first');
                 },
                         
                selected : function() {
                    return idOfSelectedContainer;
                }
            };
        }(categoryPanelId); 
        
        trees.show = function(json) {
            trees.categories._update(json);
        };
        
        return trees;
    }(categoryPanelId, itemPanelId);
    
    obj.addItems = function(containerId, items) {
        obj.model._addItems(containerId, items);
        obj.trees.items._update(containerId);
    };
    
    obj.deleteItem = function(containerId, itemId) {
        obj.model._deleteItem(containerId, itemId);
        obj.trees.items._update(containerId);
    };
    
    // retrieve and display tree
    obj.model.get(obj.trees.show);
      
    return obj;
};

var CManageItem = function(manageHelper) {
    return {
        POPUP_ID : '#items_filter',
        selectedItems : [],     // list of element offsets for data in allItems
        allItems : [],          // { id, type, title } for each item
        lastSelectedCategory: 0,
    
        addItems: function(el, allItemsId) {
            if (el.checked) 
                CManageItem.selectedItems.push(allItemsId);
            else {
                var temporary = new Array();
                for (item in CManageItem.selectedItems) {
                    if (item.id != allItemsId)
                        temporary.push(item);
                }
                CManageItem.selectedItems = temporary;
            }
        },
        
        toggleAllItems: function(el, itemType) {
            if (el.checked == false) 
                CManageItem.selectedItems = new Array();
            else {
                for (var aiElement in CManageItem.allItems)
                    CManageItem.selectedItems.push(aiElement);
            }                
            
            for (var i = 0; i < CManageItem.allItems.length; ++i)
                $("#item_" + CManageItem.allItems[i].id).attr("checked", el.checked);
        },
    
        saveItems: function(campaignID) {
            items = new Array();
            for (var siElement in CManageItem.selectedItems) {  
                items.push({
                    type : CManageItem.allItems[CManageItem.selectedItems[siElement]].type, 
                    id : CManageItem.allItems[CManageItem.selectedItems[siElement]].id, 
                    title :CManageItem.allItems[CManageItem.selectedItems[siElement]].title
                });
            }
            
            manageHelper.addItems(manageHelper.trees.categories.selected(), items);
            this.resetFilters();

            
//            var postUrl = "saveItems.json?";
//            var params = [];
//            params.push("campaignID=" + campaignID);
//            // Get the selectedItems for the campaign
//            // Add Product support
//            if (CManageItem.selectedItems.product != undefined) {
//                params.push("products=" + CManageItem.selectedItems.product.join(','));
//            }
//            // Add Event support
//            if (CManageItem.selectedItems.event != undefined) {
//                params.push("events=" + CManageItem.selectedItems.event.join(','));
//            }
//            // Add Document support
//            if (CManageItem.selectedItems.document != undefined) {
//                params.push("documents=" + CManageItem.selectedItems.document.join(','));
//            }
//            // Add Videos support
//            if (CManageItem.selectedItems.videos != undefined) {
//                params.push("videos=" + CManageItem.selectedItems.videos.join(','));
//            }
//            if (params.length > 0) {
//                postUrl = postUrl + params.join('&');
//            }
//            $.ajax({ url: postUrl,
//                dataType:"json",
//                type: 'POST',
//                success:function (response, status) {
//                   if (response.success) {
//                        window.location.reload();
//                    }
//                },
//                error:function (e) {
//                    console.log('Unable to process the request', e);
//                }
//            });
        },
    
        removeItem: function(itemID) {
    
            var cnf = confirm('Are you sure you want to delete this item ?');
            //console.log(cnf);
            if (cnf) {
                $.ajax({
                    url: "deleteItem.json",
                    dataType:"json",
                    data: {itemID: itemID, campaignID: requestParams.campaignID},
                    type: 'POST',
                    success:function (response, status) {
                        if (response.success) {
                            window.location.reload();
                        }
                    },
                    error:function (e) {
                        window.console.log('Unable to process the request', e);
                    }
                });
            }
        },
    
        downloadLinks: function() {
            $.ajax({
                url: "downloadLinks.do?campaignID=".concat(requestParams.campaignID)
            }).done(function (data) {
                if (console && console.log) {
                    window.console.log("Completed file download");
                }
            });
        },
    
        showFilterOptions: function(e) {
            var v = $('#itemType').val();
            if (v != undefined && v != '') {
                if (v == "product") {
                    CManageItem.getCategoriesWithParent(e);
                    $('#product_filters').show();
                    $('#product_filters')
                            .append('<span style="padding-right: 4px;">Title: <input style="width:90px;" type="text" id="searchTerm"></span>');
                    $('#video_filters').hide();
                    $('#event_doc_filters').hide();
                    // Clear the filtered data on change
                    $('#filtered_items').empty();
                } else if (v == "event") {
                    $('#event_doc_filters').show();
                    $('#location_field').show();
                    // Re-Set other filter items
                    $('#video_filters').hide();
                    $('#filtered_items').empty();
                    $('#product_filters').empty();
    
                } else if (v == "document") {
                    $('#event_doc_filters').show();
                    $('#location_field').hide();
                    // Re-Set other filter items
                    $('#filtered_items').empty();
                    $('#video_filters').empty();
                    $('#product_filters').empty();
                } else if (v == "videos") {
                    $('#video_filters').hide();
                    $('#event_doc_filters').hide();
                    // Re-Set other filter items
                    $('#filtered_items').empty();
                    $('#product_filters').empty();
                }
            } else {
                CManageItem.resetFilters();
            }
        },
    
        resetFilters: function() {
            $('#results_no_items').hide();
            $('#results_items').show();
            $('#itemType').val("");
            $('#filtered_items').empty();
            $(filtered_items_header).empty();
            // Hide all Category related select boxes when itemType = 'product'
            $('#product_filters').empty();
            // $('select[id^="cat_"]').hide();
            // Handle for event, document and videos
    
            //Rest all input types of div - event_doc_filters
            $('div[id^="event_doc_filters"] input').val("");
            $('#event_doc_filters').hide();
            $('#location_field').hide();
        },
    
        getCategoriesWithParent: function(e, parentCategoryID) {
            // To fetch the subcategories for a selected category
            // TODO: Change here to support different taxonomies
            window.console.log('Get categories for - ' + parentCategoryID, $('#itemType').val());
            var params = {};
            if (parentCategoryID == undefined) {
                parentCategoryID = 0;
            }
    
            // Reset if any next occuring select to hide r remove it.
            if (e.delegateTarget != undefined) {
                // window.console.log('CE ', delegatedTargetID, ' >> ', $(delegatedTargetID).nextAll());
                // To remove all dependent childerns for the selected category
                $(e.delegateTarget).nextAll('select[id^="cat_"]').remove();
            }
    
            params = { parentCategoryID: parentCategoryID, campaignID: requestParams.campaignID};
            $.ajax({ url: "item-categories.json",
                data:params,
                dataType:"json",
                success:function (response, status) {
                    var options = new Array();
                    var jsonObj = response.data;
                    // When we get 'jsonObj.length' as undefined only when SOME DATA exists in the response otherwise
                    // server will return an Empty array. So we have this condition to render the UI untill we have the data
                    if (jsonObj != undefined && (jsonObj.length == undefined)) {
                        var categoryComboBox = $("<select></select>");
                        options.push('<option value=""> - </option>');
                        for (var v in jsonObj) {
                            options.push('<option value="' + v + '">' + jsonObj[v] + '</option>');
                        }
                        categoryComboBox.attr("id", 'cat_'.concat(parentCategoryID));
                        categoryComboBox.attr("name", 'cat_'.concat(parentCategoryID));
                        // TODO: Set the data and on 'change' event reset all children categories
                        categoryComboBox.append(options.join(''));
                        categoryComboBox.bind({  change: function(e) {
                            CManageItem.getCategoriesWithParent(e, this.value);
                            CManageItem.lastSelectedCategory = this.value;
                        }});
                        // filter_options - div
                        $('#product_filters').append(categoryComboBox);
                    }
                },
                error:function (e) {
                    console.log('Unable to process the request', e);
                }
            });
        },
    
        applyFilter: function(siteId) {
            // Server call with updated search parameters for products/documents/events
            var itemType = $('#itemType').val();
            var params = {};
            params.itemType = itemType;
            if (itemType == 'event') {
                params.keyword = $('#keyword').val();
                params.location = $('#location').val();
                params.startDate = $('#startDate').val();
                params.endDate = $('#endDate').val();
            } else if (itemType == 'document') {
                params.keyword = $('#keyword').val();
                params.startDate = $('#startDate').val();
                params.endDate = $('#endDate').val();
            } else {
                var lastElement = $('select[id^="cat_"]:last');
                // Pass the last category selected to search with the sub-categories of last selected category
                if (lastElement.val() != "" && lastElement.val() != undefined) {
                    params.categoryID = lastElement.val();
                } else {
                    params.categoryID = lastElement.prev().val();
                }
                // Get all category id's - EXTERNALINTERACTIVEADS-289
                params.categories = $('select[id^="cat_"]').map(
                        function() {
                            return $(this).val();
                        }).get().toString();
                params.searchTerm = $('#searchTerm').val();
            }
            params.siteID = siteId;
            // TBD - Support for videos
            //}else if(itemType == 'videos'){
            //    searchItemsURL = searchItemsURL.concat();
            // Ajax call
            $.ajax({ url: "searchItems.json",
                data: params,
                dataType:"json",
                success:function (response, status) {
                    //console.log(response.data, response.data.length);
                    
                    CManageItem.allItems = [];
                    
                    if (response.data.length > 0) {
                        $('#result_no_items').hide();
                        $('#result_items').show();
                        var rowsHTML = '';
                        // Generate the header
                        var headerHTML = "<td style='width: 12px;'>" + 
                                           "<input type='checkbox' onchange='CManageItem.toggleAllItems(this,\"" + itemType + "\")' />" + 
                                         "</td>";
                        if (itemType == 'product') {
                            headerHTML = headerHTML.concat("<td style='width: 512px;'>Title</td>");
                            headerHTML = headerHTML.concat("<td style='width: 90px;'>Reviews</td>");
                            headerHTML = headerHTML.concat("<td style='width: 85px;'>Stars</td>");
                            headerHTML = headerHTML.concat("<td style='width: 80px;'>List Price</td>");
                            headerHTML = headerHTML.concat("<td>Price</td>");
                        } else if (itemType == 'event') {
                            headerHTML = headerHTML.concat("<td style='width: 138px;'>Title</td>");
                            headerHTML = headerHTML.concat("<td style='width: 100px;'>Location</td>");
                            headerHTML = headerHTML.concat("<td style='width: 120px;'>Start Date</td>");
                            headerHTML = headerHTML.concat("<td style='width: 120px;'>End Date</td>");
                            headerHTML = headerHTML.concat("<td style='width: 75px;'>Stars</td>");
                            headerHTML = headerHTML.concat("<td style='width: 85px;'>Ratings</td>");
                            headerHTML = headerHTML.concat("<td style='width: 110px;'>List Price</td>");
                            headerHTML = headerHTML.concat("<td>Price</td>");
                        } else if (itemType == 'document') {
                            headerHTML = headerHTML.concat("<td style='width: 170px;'>Title</td>");
                            headerHTML = headerHTML.concat("<td style='width: 110px;'>Start Date</td>");
                            headerHTML = headerHTML.concat("<td style='width: 110px;'>End Date</td>");
                            headerHTML = headerHTML.concat("<td>Document</td>");
                        }
    
                        $('#filtered_items_header').html(headerHTML);
    
                        for (var i = 0; i < response.data.length; i++) {
                            CManageItem.allItems[i] = {
                                type : itemType,
                                id : response.data[i].id,
                                title : response.data[i].title
                            };
                            
                            // ...add row to HTML
                            rowsHTML = rowsHTML + CManageItem.itemRowBuilder(i, itemType, response.data[i]);
                        }
                        $('#filtered_items').html(rowsHTML);
                    } else {
                        $('#results_no_items').show();
                        $('#results_items').hide();
                    }
                },
                error:function (e) {
                    console.log('Unable to process the request', e);
                }
            });
        },
    
        itemRowBuilder: function(allItemsElement, itemType, itemObj) {
            var ifNullReturnEmptyString = function(s) {
                if (s == null) 
                    return "";
                return s;
            };  
            
            var rowHTML = '';
            if (itemType == 'product') {
                rowHTML = "<tr id='row_" + itemObj.id + "' style='border-bottom: 1px solid;height: 25px;'>" +
                        "<td style='width: 12px;'><input onchange='CManageItem.addItems(this," + allItemsElement +
                        ")' type='checkbox' name='items' id='item_" + itemObj.id + "' data-id='" + itemObj.id + "'></td>" +
                        "<td style='width: 512px;'>" + itemObj.title + "</td>" +
                        "<td style='width: 90px;'>" + itemObj.reviews + "</td>" +
                        "<td style='width: 85px;'>" + itemObj.stars + "</td>" +
                        "<td style='width: 80px;'>" + itemObj.listPrice + "</td>" +
                        "<td>" + itemObj.price + "</td>" +
                        "</tr>";
            } else if (itemType == 'event') {
                rowHTML = "<tr id='row_" + itemObj.id + "' style='border-bottom: 1px solid;height: 25px;'>" +
                        "<td style='width: 12px;'><input onchange='CManageItem.addItems(this," + allItemsElement +
                        ")' type='checkbox' name='items' id='item_" + itemObj.id + "' data-id='" + itemObj.id + "'></td>" +
                        "<td style='width: 138px;'>" + itemObj.title + "</td>" +
                        "<td style='width: 100px;'>" + itemObj.location + "</td>" +
                        "<td style='width: 120px;'>" + ifNullReturnEmptyString(itemObj.startDate) + "</td>" +
                        "<td style='width: 120px;'>" + ifNullReturnEmptyString(itemObj.endDate) + "</td>" +
                        "<td style='width: 75px;'>" + itemObj.stars + "</td>" +
                        "<td style='width: 80px;'>" + itemObj.ratings + "</td>" +
                        "<td style='width: 110px;'>" + itemObj.listPrice + "</td>" +
                        "<td>" + itemObj.price + "</td>" +
                        "</tr>";
            } else if (itemType == 'document') {
                rowHTML = "<tr id='row_" + itemObj.id + "' style='border-bottom: 1px solid;height: 25px;'>" +
                        "<td style='width: 12px;'><input onchange='CManageItem.addItems(this," + allItemsElement +
                        ")' type='checkbox' name='items' id='item_" + itemObj.id + "' data-id='" + itemObj.id + "'></td>" +
                        "<td style='width: 170px;'>" + itemObj.title + "</td>" +
                        "<td style='width: 110px;'>" + itemObj.startDate + "</td>" +
                        "<td style='width: 110px;'>" + itemObj.endDate + "</td>" +
                        "<td>" + itemObj.document + "</td>" +
                        "</tr>";
            }
            return rowHTML;
        },
        toggleItemsBlock: function(divID) {
            //$('#c_items').toggle("slow");
            $('#'.concat(divID)).toggle("slow");
        }   
    };
};

