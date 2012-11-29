<%@ page import="com.dell.acs.managers.CampaignManager" %>
<%@ page import="com.sourcen.core.config.ConfigurationServiceImpl" %>
<%@ page import="java.util.Enumeration" %>
<link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
<link type="text/css" href="<c:url value="/css/jquery/tree_style.css"/>" rel="stylesheet"/>
<%--<script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.min.js"/>"></script>--%>
<%--<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.8.18.custom.min.js"/>"></script>--%>

<script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
<!-- Common for all -->
<script type="text/javascript" src="<c:url value="/js/jquery.ui.core.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.widget.js"/>"></script>
<!-- Common for all -->
<!-- dialog.js dependents START -->
<script type="text/javascript" src="<c:url value="/js/jquery.ui.mouse.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.draggable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.button.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.resizable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.slider.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.dialog.js"/>"></script>
<!-- dialog.js dependents END -->
<!-- Auto complete and date-picker dependent Start -->
<script type="text/javascript" src="<c:url value="/js/jquery.ui.position.js"/>"></script>
<!-- Auto complete and date-picker dependent END -->

<script type="text/javascript" src="<c:url value="/js/jquery.ui.autocomplete.js"/>"></script>

<script type="text/javascript" src="<c:url value="/js/jquery.ui.datepicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-timepicker-addon.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-sliderAccess.js"/>"></script>

<!-- Tree -->
<script type="text/javascript" src="<c:url value="/js/tree/jquery.jstree.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/tree/jquery.hotkeys.js"/>"></script>

<!-- Ajax file upload -->
<script type="text/javascript" src="<c:url value="/js/ajaxUpload/ajaxfileupload.js"/>"></script>

<script type="text/javascript">

    $.ajaxSetup({
        cache: false
    });

    <%
        Integer maxDepth = ConfigurationServiceImpl.getInstance().getIntegerProperty(CampaignManager.CAMPAIGN_CATEGORY_MAX_DEPTH, 2);
    %>
    var MAX_CATEGORY_DEPTH = <%=maxDepth%>;

    var SITE_ID = -1;

</script>


<%--<script type="text/javascript" src="<c:url value="/js/jquery.ui.sortable.js"/>"></script>--%>


<%-- Scripts for Create mode --%>
<script type="text/javascript">

var CIUtils = {
    resetThumbnailImage: function() {
        $('#resetThumbnail').val(true);
        $('#thumbnailFileImage').remove();
        document.getElementById('reset_img_flag').value = 'true';
        // Hide reset link
        $('#img_reset_action').hide();
    }
};


// var errorMessage = "Please address the below errors before proceeding further - ";
var errorMessage = "";
var hasError = false;
/* Helper object to bind all methods related to campaign item selection */
var CIAutoComplete = {
    items   : [],
    QUERY_MIN_LENGTH: 2,
    escapeHtml: function (str) {
        return String(str)
                .replace(/&/g, '&amp;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&apos;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;');
    },
    createListItem: function(itemObj, addOrderField) {
        console.log(addOrderField, itemObj);
        $('#selected_items').show();
        $('#item_table').append(CIReview.buildItemRow(itemObj));
    },
    // @Deprecated
    buildItemRow: function(itemObj) {
        var rowHTML = "<tr id='row_" + itemObj.itemID + "'>" +
                "<td>" + itemObj.itemName + "</td>" +
                "<td onClick='CIReview.showReviewPopup(" + itemObj.itemID + ");'>Choose reviews</td>" +
                "<td onClick='CIAutoComplete.removeListItem(" + itemObj.itemID + ");'>Remove</td>" +
                "</tr>";
        return rowHTML;
    },
    // Set the values for campaign items and product reviews
    setHiddenFieldValues: function(action) {
        this.validateFormFields();
        if (hasError) {
            //alert('Please resolve the errors before submitting the form');
            return false;
        } else {
            if (this.items.length > 0) {
                var ids = [];
                for (var idx = 0; idx < this.items.length; idx++) {
                    ids.push(this.items[idx].itemID)
                }
                var itemsJsonString = window.JSON.stringify(this.items);
                $('#campaignItems').val(itemsJsonString);
            }

            if (CIReview.reviews.length > 0) {
                var reviewsJsonString = window.JSON.stringify(CIReview.reviews);
                $('#itemReviews').val(reviewsJsonString);
            }
            var campaignForm = document.forms['campaign'];
            if (action == 'add') {
                campaignForm.action = "add.do";
            } else {
                campaignForm.action = "edit.do";
            }
            campaignForm.submit();
        }

    },
    validateFormFields: function() {
        // Validation is applied for the fields - Name, Start date, End Date, Items

        // Name
        if ($('#name') != undefined && $('#name').val() != undefined && $.trim($('#name').val()).length == 0) {
            hasError = true;
            $('#name_error').html("Name field can't be empty");
        } else {
            hasError = false;
            $('#name_error').empty();
        }
        /*        // Start date
         if ($.trim($('#startDate').val()).length == 0) {
         hasError = true;
         $('#start_date_error').html("Start date field can't be empty");
         } else {
         hasError = false;
         $('#start_date_error').empty();
         }
         // End date
         if ($.trim($('#endDate').val()).length == 0) {
         hasError = true;
         $('#end_date_error').html("End date field can't be empty")
         } else {
         hasError = false;
         $('#end_date_error').empty();
         }*/
    },

    // isAlreadySelected
    checkSelected: function(item) {
        for (var i = 0; i < this.items.length; i++) {
            var ci = this.items[i];
            if (item.itemID == ci.itemID) {
                return true;
            }
        }
    },

    removeListItem: function(itemID) {
        var row_id = '#row_' + itemID;
        try {
            // Removes the selected item from the items array
            var tmp = [];
            for (var idx = 0; idx < this.items.length; idx++) {
                if (itemID != this.items[idx].itemID) {
                    tmp.push(this.items[idx]);
                }
            }
            this.items = tmp;

            // Remove the table row for that element
            $(row_id).remove();

            // Remove from the Review list as well
            CIReview.reviews[itemID] = null;
            // Hide the div if there are NO items available to display
            if (this.items.length == 0) {
                $('#selected_items').hide();
            }
        } catch(e) {
            // Unable to remove the element
        }
    },
    updatePriority: function(priority, itemID) {
        console.log('Update the campaign priority - ', priority, itemID);
    }
};

/* Helper object to initialize, validate the date fields */
// @deprecated
var CIDateFields = {
    initDateFields: function(fields) {
        // Date-picker component's initialization
//            for(var i = 0; i < fields.length; i++){
//                $("#"+fields[i]).datepicker();
//            }
        // Currently we have only 2 fields startDate and endDate

        var startDateField = $("#startDate");
        startDateField.timepicker({
            onSelect: function (selectedDateTime) {
                var start = $(this).datetimepicker('getDate');
                $('#endDate').datetimepicker('option', 'minDate', new Date(start.getTime()));
            }
        });
        startDateField.datepicker({
            //startDateField.datetimepicker({
            showOn: "button",
            // http://localhost:9090/dell_acs/images/logo.png
            buttonImage: "<c:url value="/images/cal.png"/>",
            buttonImageOnly: true,
            dateFormat: 'mm/dd/yy hh:mm'
        });
        var endDateField = $("#endDate");
        endDateField.timepicker({
            onSelect: function (selectedDateTime) {
                var end = $(this).datetimepicker('getDate');
                $('#startDate').datetimepicker('option', 'maxDate', new Date(end.getTime()));
            }
        });
        endDateField.datepicker({
            //endDateField.datetimepicker({
            showOn: "button",
            buttonImage: "<c:url value="/images/cal.png"/>",
            buttonImageOnly: true,
            dateFormat: 'mm/dd/yy hh:mm',
            onSelect : function() {
                //CIDateFields.validateDateFields();
                console.log('selected');
            }
        });

        //endDateField.minDate = startDateField.date
    },
    validateDateFields: function() {
        var startDateValue = $("#startDate").val();
        var endDateValue = $("#endDate").val();
        // Check for MM/dd/yyy
        var startDate = new Date(startDateValue);
        var endDate = new Date(endDateValue);
        //window.console.log((startDate > endDate) , (startDate < endDate));
        if (startDate > endDate) {
            $('#start_date_error').html('Please choose a valid start date and end date');
            hasError = true;
            return false;
        } else {
            hasError = false;
            $('#start_date_error').empty();
        }

        // Format check - MM/dd/yyyy
        var dateFormat = /\b\d{1,2}[\/]\d{1,2}[\/]\d{4}\b/;
        // var dateFormat = /^(0?[1-9]{1,2})\/(0?[1-9]{1,2})\/[1-9]\d{3}$/;
        if (startDateValue.trim().length > 0 && !dateFormat.test(startDateValue)) {
            $('#start_date_error').html('Incorrect start date format. Enter as mm/dd/yyyy.')
            hasError = true;
            return false;
        } else {
            hasError = false;
            $('#start_date_error').empty();
        }
        if (endDateValue.trim().length > 0 && !dateFormat.test(endDateValue)) {
            $('#end_date_error').html('Incorrect end date format. Enter as mm/dd/yyyy.')
            hasError = true;
            return false;
        } else {
            hasError = false;
            $('#end_date_error').empty();
        }
    }
};

/* Helper object to bind all methods related to campaign item reviews */
var CIReview = {
    GET_URL : "",
    POPUP_ID : "",
    MAX_REVIEWS: 3,
    reviews: [],
    reviewItems: [],
    tmpCopied: false,
    reviewsChanged: false,
    campaignItemID: undefined,
    itemID: undefined,
    init: function(id) {
        CIReview.GET_URL = "reviews.json";
        CIReview.POPUP_ID = id;
        CIReview.reviews = [];
    },

    showReviewPopup: function(campaignItemID, itemID, valueString) {
        var values = [];
        if (valueString != undefined && valueString != '') {
            values = valueString.split(',');
        }
        CIReview.getItemReviews(itemID, campaignItemID, values);
        CIReview.campaignItemID = campaignItemID;
        CIReview.itemID = itemID;
    },

    closePopup: function(campaignItemID) {
        // Hide popup and empty the reviews list
        $('#item-review-popup').hide();
        $('#reviews_list').empty();
        $('body').removeAttr("style");
        // Clear the tmpReview array data for the selected campaignItem
        var key = "review_".concat(campaignItemID).toString();
        // console.log("BEFORE :: ", tmpReviewArray[key], CIReview.reviewItems);
        // Reset the CIReview.reviewItems array upon close/cancel
        CIReview.reviewItems = tmpReviewArray[key].split(',');
        // console.log("AFTER  :: ",tmpReviewArray[key], CIReview.reviewItems);
    },

    postSelectedReviews: function(campaignItemID, itemID) {
        if (CIReview.reviewsChanged && campaignItemID != undefined && itemID != undefined) {
            CIReview.reviews.push({itemID: itemID, value: finalValue});
            var finalValue = CIReview.reviewItems.toString();
            // If greater than 0 and review already exists then iterate over the reviews and
            // update the value for the item
            //window.console.log(CIReview.reviews.length, CIReview.checkItemReviewExists(itemID), itemID);
            if (CIReview.reviews.length > 0 && CIReview.checkItemReviewExists(itemID)) {
                // Copy the values to a tmpReviews array then assign it back CIReview.reviews array
                var tmpReviews = [];
                for (var idx = 0; idx < CIReview.reviews.length; idx++) {
                    var curItem = CIReview.reviews[idx];
                    //window.console.log(curItem);
                    if (curItem.itemID == itemID) {
                        //window.console.log('Update the value for item - ' + itemID);
                        tmpReviews.push({itemID: itemID, value: finalValue});
                    } else {
                        // window.console.log('NO-NEED to change the value for item - ' + itemID);
                        tmpReviews.push(curItem);
                    }
                }

                //window.console.log('Assign the tmpReview data to CIReview.reviews');
                //window.console.log(CIReview.reviews, tmpReviews);
                CIReview.reviews = [];
                CIReview.reviews = tmpReviews;
                window.console.log(CIReview.reviews);
            } else {
                CIReview.reviews.push({itemID: itemID, value: finalValue});
            }
            // Reset the temp reviewItems array to accommodate next set of reviews
            CIReview.reviewItems = [];
            //window.console.log(CIReview.reviews, CIReview.reviewItems.toString());
            CIReview.saveItemReviews(campaignItemID, itemID);
        }
    },

    addItemReviews: function(el, campaignItemID, itemID, reviewID) {
        //window.console.log(el.checked);
        CIReview.reviewsChanged = true;
        var key = "review_".concat(campaignItemID).toString();
        window.console.log(key, '------- ', tmpReviewArray[key]);
        if (!CIReview.tmpCopied && tmpReviewArray[key] != undefined && tmpReviewArray[key] != '') {
            CIReview.reviewItems = tmpReviewArray[key].split(',');
            CIReview.tmpCopied = true;
        }
        window.console.log(CIReview.reviewItems);
        if (el.checked) {
            if (CIReview.reviewItems.length < 3) {
                CIReview.reviewItems.push(reviewID);
            } else {
                el.checked = false;
                alert('Can select maximum of - ' + CIReview.MAX_REVIEWS + ' reviews');
            }
            window.console.log(CIReview.reviewItems.toString());
        } else {
            // Remove from array list
            var tmp = [];
            window.console.log('REMOVE - ', reviewID);
            window.console.log('BEFORE ', CIReview.reviewItems.toString());
            for (var i = 0; i < CIReview.reviewItems.length; i++) {
                if (CIReview.reviewItems[i] != reviewID) {
                    tmp.push(CIReview.reviewItems[i]);
                }
            }
            CIReview.reviewItems = tmp;
            window.console.log('AFTER ', CIReview.reviewItems.toString());
        }
    },

    saveItemReviews: function(campaignItemID, itemID) {
        $.ajax({
            url: "setItemProperty.json",
            dataType:"json",
            data: {campaignItemId:campaignItemID, name: 'dell.campaign.item.reviews', value: CIReview.reviews[0].value},
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
    },

    getItemReviews: function(itemID, campaignItemID, selectedValues) {
        $.ajax({ url: 'reviews.json',
            data:{campaignItemID: campaignItemID, itemID: itemID},
            dataType:"json",
            success:function (response, status) {
                // Display the popup
                $('#item-review-popup').show();
                $('body').css("overflow", "hidden");

                // Reset the rendered reviews in pop-up
                $('#reviews_list').empty();
                // Set the default height
                //$('.reviews_table').css("height", "500px");
                //$('.popup_content').css("height", "436px");
                $('#reviews_table').removeClass("no_data");
                // $('.popup_content').css("height", "50px");
                $('#popup_content').removeClass("no_content");

                var result = response.data;
                if (result.length > 0) {
                    for (var i = 0; i < result.length; i++) {
                        var checkedString = '';
                        checkedString =
                                (($.inArray(result[i].id.toString(), selectedValues)) >= 0) ? " checked='checked' " :
                                        "";
                        var row = "<tr>" +
                                "<td>" +
                                "<div class='review_block'>" +
                                "<input " +
                                "type='checkbox' " +
                                "name='reviewItem'" +
                                "id='review_" + result[i].id + "'" +
                                checkedString +
                                "onchange='CIReview.addItemReviews(this, " + campaignItemID + "," + itemID + ","
                                + result[i].id + ");'>" +
                                "<span>" + result[i].title + "<span>" +

                                "<p>" + result[i].review + "</p>" +
                                "</div></td>" +
                                "</tr>";
                        $('#reviews_list').append(row);
                    }
                } else {
                    var noReviewsRow = "<tr><td> No reviews found for selected item. </td></tr>"
                    $('#reviews_list').append(noReviewsRow);

                    // $('.reviews_table').css("height", "114px");
                    $('#reviews_table').addClass("no_data");
                    // $('.popup_content').css("height", "50px");
                    $('#popup_content').addClass("no_content");
                }
            },
            error:function (e) {
                if (e.readyState == 4) {
                    window.location.reload();
                }
                window.console.log('Unable to process the request', e);
            }
        });
    },

    // Check reviews already selected for an Item with id = itemID
    checkItemReviewExists: function(itemID) {
        for (var i = 0; i < CIReview.reviews.length; i++) {
            if (CIReview.reviews[i].itemID == itemID) {
                return true;
            }
        }
        return false;
    },

    checkForValueExists: function(valueString, reviewID) {
        if (valueString.length > 0) {
            var values = valueString.split(',');
            for (var idx = 0; idx < vals.length; idx++) {
                if (values[i] == reviewID.toString()) {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

};

var CItems = {
    POPUP_ID : '#items_filter',
    items : new Object(),
    products: [],
    categories: [],
    videos: [],
    events: [],
    documents:[],
    whitePapers:[],
    lastSelectedCategory: 0,
    // @ deprecated
    showItemSelectionPopup: function() {
        var dialog = $(CItems.POPUP_ID).dialog({
            autoOpen: false,
            height: 546,
            width: 848,
            closeOnEscape: true,
            modal: true,
            draggable: true,
            open: function(event, ui) {
                // on open do-nothing
            },
            buttons: {
                "OK": function() {
                    // Render the selected items to -
                    // item_table
                }
            },
            close: function() {
                $(CItems.POPUP_ID).dialog("close");
            }
        });
        $(CItems.POPUP_ID).dialog("open");
    },

    addItems: function(el, itemID, elOverride, typeOverride) {
        var type = '';
        var checked = '';

        if (typeOverride != undefined) {
            type = typeOverride;
        } else {
            type = $('#itemType').val();
        }

        if (elOverride != undefined) {
            checked = elOverride;
        } else {
            checked = el.checked;
        }
        // product, whitepaper, videos
        //window.console.log(el.checked);
        if (checked) {
            // Check the object is initialized for the itemType
            if (CItems.items[type] != undefined) {
                CItems.items[type].push(itemID);
            } else {
                // If not initialize the object for the itemType
                CItems.items[type] = [];
                CItems.items[type].push(itemID);
            }
        } else {
            // Remove from array list
            var tmp = [];
            window.console.log('REMOVE - ', itemID);
            window.console.log('BEFORE ', CItems.items[type]);
            for (var i = 0; i < CItems.items[type].length; i++) {
                if (CItems.items[type][i] != itemID) {
                    tmp.push(CItems.items[type][i]);
                }
            }
            CItems.items[type] = tmp;
            window.console.log('BEFORE ', CItems.items[type]);
        }
    },

    saveItems: function(campaignID) {
        var postUrl = "saveItems.json?";
        var params = [];
        params.push("campaignID=" + campaignID);
        // Get the items for the campaign
        // Add Product support
        if (CItems.items.product != undefined) {
            params.push("products=" + CItems.items.product.join(','));
        }
        // Add Event support
        if (CItems.items.event != undefined) {
            params.push("events=" + CItems.items.event.join(','));
        }
        // Add Document support
        if (CItems.items.document != undefined) {
            params.push("documents=" + CItems.items.document.join(','));
        }
        // Add Videos support
        if (CItems.items.videos != undefined) {
            params.push("videos=" + CItems.items.videos.join(','));
        }
        if (params.length > 0) {
            postUrl = postUrl + params.join('&')
        }
        $.ajax({ url: postUrl,
            dataType:"json",
            type: 'POST',
            success:function (response, status) {
                if (response.success) {
                    window.location.reload();
                }
            },
            error:function (e) {
                console.log('Unable to process the request', e);
            }
        });
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
            $('#items_container').hide();
            if (v == "product") {
                CItems.getCategoriesWithParent(e);
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
            CItems.resetFilters();
        }
    },

    resetFilters: function() {
        $('#no_items').hide();
        $('#itemType').val("");
        $('#items_container').hide();
        $('#filtered_items').empty();
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
                var options = [];
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
                        CItems.getCategoriesWithParent(e, this.value);
                        CItems.lastSelectedCategory = this.value;
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

    applyFilter: function() {
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
        params.siteID = SITE_ID;
        // TBD - Support for videos
        //}else if(itemType == 'videos'){
        //    searchItemsURL = searchItemsURL.concat();
        // Ajax call
        $.ajax({ url: "searchItems.json",
            data: params,
            dataType:"json",
            success:function (response, status) {
                //console.log(response.data, response.data.length);
                $('#items_container').show();
                if (response.data.length > 0) {
                    $('#no_items').hide();
                    var rowsHTML = '';
                    // Generate the header
                    var headerHTML = "<td style='width: 12px;'></td>";
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
                        rowsHTML = rowsHTML + CItems.itemRowBuilder(itemType, response.data[i]);
                    }
                    $('#filtered_items').html(rowsHTML);
                } else {
                    $('#no_items').show();
                    $('#items_container').hide();
                }
            },
            error:function (e) {
                console.log('Unable to process the request', e);
            }
        });
    },

    itemRowBuilder: function(itemType, itemObj) {
        var rowHTML = '';
        if (itemType == 'product') {
            rowHTML = "<tr id='row_" + itemObj.id + "' style='border-bottom: 1px solid;height: 25px;'>" +
                    "<td style='width: 12px;'><input onchange='CItems.addItems(this," + itemObj.id +
                    ")' type='checkbox' name='items' id='item_" + itemObj.id + "' data-id='" + itemObj.id + "'></td>" +
                    "<td style='width: 512px;'>" + itemObj.title + "</td>" +
                    "<td style='width: 90px;'>" + itemObj.reviews + "</td>" +
                    "<td style='width: 85px;'>" + itemObj.stars + "</td>" +
                    "<td style='width: 80px;'>" + itemObj.listPrice + "</td>" +
                    "<td>" + itemObj.price + "</td>" +
                    "</tr>";
        } else if (itemType == 'event') {
            rowHTML = "<tr id='row_" + itemObj.id + "' style='border-bottom: 1px solid;height: 25px;'>" +
                    "<td style='width: 12px;'><input onchange='CItems.addItems(this," + itemObj.id +
                    ")' type='checkbox' name='items' id='item_" + itemObj.id + "' data-id='" + itemObj.id + "'></td>" +
                    "<td style='width: 138px;'>" + itemObj.title + "</td>" +
                    "<td style='width: 100px;'>" + itemObj.location + "</td>" +
                    "<td style='width: 120px;'>" + itemObj.startDate + "</td>" +
                    "<td style='width: 120px;'>" + itemObj.endDate + "</td>" +
                    "<td style='width: 75px;'>" + itemObj.stars + "</td>" +
                    "<td style='width: 80px;'>" + itemObj.ratings + "</td>" +
                    "<td style='width: 110px;'>" + itemObj.listPrice + "</td>" +
                    "<td>" + itemObj.price + "</td>" +
                    "</tr>";
        } else if (itemType == 'document') {
            rowHTML = "<tr id='row_" + itemObj.id + "' style='border-bottom: 1px solid;height: 25px;'>" +
                    "<td style='width: 12px;'><input onchange='CItems.addItems(this," + itemObj.id +
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

$(function() {
    // CIAutoComplete.initAutoCompleteField('search_item');
    // DateTime picker which does both validation and allows user choose date and time
    // http://trentrichardson.com/examples/timepicker/
    $('#startDate').datetimepicker({
        showOn: "button",
        // http://localhost:9090/dell_acs/images/logo.png
        buttonImage: "<c:url value="/images/cal.png"/>",
        buttonImageOnly: true,
        dateFormat: 'mm/dd/yy',
        timeFormat: 'hh:mm',
        onClose: function(dateText, inst) {
            var endDateTextBox = $('#endDate');
            if (endDateTextBox.val() != '') {
                var testStartDate = new Date(dateText);
                var testEndDate = new Date(endDateTextBox.val());
                if (testStartDate > testEndDate)
                    endDateTextBox.val(dateText);
            }
            else {
                // If the value is NOT SELECTED THEN SET THE 'endDate'
                if (endDateTextBox.val() == undefined) {
                    endDateTextBox.val(dateText);
                }

            }
        },
        onSelect: function (selectedDateTime) {
            var start = $(this).datetimepicker('getDate');
            var endDateVal = $('#endDate').val();
            $('#endDate').datetimepicker('option', 'minDate', new Date(start.getTime()));
            $('#endDate').val(endDateVal);
            CIAutoComplete.validateFormFields();
        }
    });
    $('#endDate').datetimepicker({
        showOn: "button",
        // http://localhost:9090/dell_acs/images/logo.png
        buttonImage: "<c:url value="/images/cal.png"/>",
        buttonImageOnly: true,
        dateFormat: 'mm/dd/yy',
        timeFormat: 'hh:mm',
        onClose: function(dateText, inst) {
            var startDateTextBox = $('#startDate');
            if (startDateTextBox.val() != '') {
                var testStartDate = new Date(startDateTextBox.val());
                var testEndDate = new Date(dateText);
                if (testStartDate > testEndDate)
                    startDateTextBox.val(dateText);
            }
            else {
                startDateTextBox.val(dateText);
            }
        },
        onSelect: function (selectedDateTime) {
            var end = $(this).datetimepicker('getDate');
            $('#startDate').datetimepicker('option', 'maxDate', new Date(end.getTime()));
            CIAutoComplete.validateFormFields();
        }
    });

    //CIReview.init("#item-review-popup");
});

</script>