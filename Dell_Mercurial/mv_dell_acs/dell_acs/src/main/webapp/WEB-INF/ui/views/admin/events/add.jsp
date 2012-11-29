<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="org.springframework.validation.Errors" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.springframework.validation.FieldError" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Edit Event</title>
</head>
<body>

<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

<script type="text/javascript">

// Jquery Function Start
// For Multiple Image
$(document).ready(function() {

    var nextUploadId = 0;
    var functionCall = 0;
    var noOfInpuFieldCreated = 0;
    /*Later functionality for checking the duplicate entry.*/
    $('#contentName').blur(function() {
        var eventName = this.value;
        var eventId = null;
        if (document.getElementById('id') != null) {
            eventId = document.getElementById('id').value;
            ajaxCallForName(eventName, "update", eventId);
        } else {
            ajaxCallForName(eventName, "create", eventId)
        }
    });

    $("#thumbnailFile").bind("change", function (e) {
        //get the file path
        var file = $("#thumbnailFile").val();

        //pull out the filename
        file = file.replace(/^.*\\/i, "");

        //show to user
        //$("#ajaxMsg").html("You chose: " + file);
        doAjax(file);
    });

    $('#AddUrl').click(function() {
        $('#optional').show();
        //Create and add a paragraph

        $('<div />').attr({'id':'uploadInputField' + nextUploadId}).addClass("uploadInputField")
                .appendTo('#imageOption');

        $('<input />').attr({'type':'file', 'id':'upload' + nextUploadId,'name':'upload' + nextUploadId})

                .appendTo('#uploadInputField' + nextUploadId);

        $('<input />').attr({'type':'checkbox', 'id':'checkFile' + nextUploadId,'name':'checkFile' +
                nextUploadId,'checked':'true','onClick':'removeElement(this)'}).add('<br />')

                .appendTo('#uploadInputField' + nextUploadId);
        //remove the attached file

        nextUploadId++;
        noOfInpuFieldCreated++;


    });
    function doAjax(file) {
        $.ajax({
            url: 'ajaxResponse.do',
            data: ({fileName : file}),
            success: function(data) {
                if (data != null) {
                    if (data == 'File Selected') {
                        $('#ajaxMsg').css('color', 'green').html(data);
                        $('#validationMsg').hide();
                        $('#imageExtnStatus').val('true');
                    } else {
                        $('#ajaxMsg').css('color', 'red').html(data);
                        $('#validationMsg').hide();
                        $('#imageExtnStatus').val('false');
                    }
                }

            }
        });
    }

    // Don't allow to submit the form , if user give unsupported format file
    $('#target').submit(function() {
        var formGetWay = $('#imageExtnStatus').val();
        if (formGetWay == 'false') {
            jAlert('Unable to upload the invalid file,please choose another one', 'Image Alert');
            return false;
        } else {
            return true;
        }
    });

});
// Jquery Function End

// Function Used in Jquery  start

/*Later functionality for checking the duplicate entry.*/
function ajaxCallForName(eventName, status, eventId) {

    var name = eventName;
    var eventStatus = status;
    var eventId = eventId;
    $.ajax({
        url:'checkNameAvailability.do',
        data:({name:name,status:eventStatus,id:eventId}),
        success:function(data) {
            if (data == 'found') {
                jAlert('Event Name already exist, choose another one ', 'Duplicate Alert', function() {
                    $('#contentName').focus();
                });
            }
        }
    });
}
function removeElement(chkObj) {

    if (!(chkObj.checked)) {

        var inputID = chkObj.id.substring(9);
        var removedElementCounter = inputID;
        $('#uploadInputField' + inputID).remove();
    }
    if (noOfInpuFieldCreated == 0) {

    }
}

function unHideOptional(optional) {

    if (document.getElementById(optional).style.display == 'none') {
        document.getElementById(optional).style.display = 'block';
    }
}

// Function used in Jquery end.

// JavaScript Function Start.
function urlValidation(val, elementName, required) {

    if (required == 1 || (required == 0 && val != "")) {
        if (/^(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$/
                .test(val)) {
            document.getElementById(elementName + '_error').innerHTML = '';
        } else {
            /*document.getElementById(elementName + '_error').innerHTML = elementName + ' can take only url';*/
            jAlert(elementName + ' can take only url');
            //$('#'+elementName).focus();
        }
    }
}
function onlyLetter(val, elementName) {

    if (/^[+/-]?[0-9]+$/.test(val)) {
        jAlert(elementName + ' can take alphanumeric or character only', 'Invalid Alert');
    } else {
        if (/^[a-zA-Z0-9 _]+$/.test(val)) {
            document.getElementById(elementName + '_error').innerHTML = '';
        } else {
            /*            document.getElementById(elementName + '_error').innerHTML = elementName + ' can take only letter';*/
            jAlert(elementName + ' can take alphanumeric or character only', 'Invalid Alert');
        }
    }

}
function onlyNumber(val, elementName, required) {

    if (required == 1 || (required == 0 && val != "")) {
        if (elementName == "perNpeople" || elementName == "numberOfDays") {


            if (/^[+]?[1-9][0-9]*$/.test(val)) {
                document.getElementById(elementName + '_error').innerHTML = '';
            } else {
                /*                    document.getElementById(elementName + '_error').innerHTML =
                 elementName + ' has minimum value 1';*/
                jAlert(elementName + ' can take only positive number greater than 1');
            }
        } else if (elementName == "minimumPeople" || elementName == "facebookLikes" || elementName == "twitter" ||
                elementName == "googlePluses") {


            if (/^[+]?[0-9]+$/.test(val)) {
                document.getElementById(elementName + '_error').innerHTML = '';
            } else {
                jAlert(elementName + ' can take only positive number');
            }
        } else {
            if (/^[+]?\d+\.\d+$/.test(val) || /^[+]?[0-9]+$/.test(val)) {
                document.getElementById(elementName + '_error').innerHTML = '';
            } else {
                jAlert(elementName + ' can take only positive decimal number');
            }
        }
    }


}
function validateFloat(val, elementName) {
    if (elementName == "stars") {
        if ((/^[+]?[0-9]+$/.test(val) || /^[+]?\d+\.\d+$/.test(val)) && (val >= 0.0 && val <= 5.0)) {
            document.getElementById(elementName + '_error').innerHTML = '';
        } else {
            /*            document.getElementById(elementName + '_error').innerHTML =
             elementName + ' Can take only Positive Value upto 5';*/
            jAlert(elementName + ' can take only Positive Value up to 5', 'Invalid Value');

        }
    } else {
        if (/^[+]?[0-9]+$/.test(val)) {
            document.getElementById(elementName + '_error').innerHTML = '';
        } else {
            /*            document.getElementById(elementName + '_error').innerHTML =
             elementName + ' Can take only Positive Value upto 5';*/
            jAlert(elementName + ' can take only Positive Value', 'Invalid Value');

        }
    }
}
//JavaScript Function end.

</script>

${navCrumbs}

<c:choose>
<c:when test="${ empty event.retailerSite}">
    You can add event's for a Retailer site ONLY. <br>Click
    <a href="<c:url value='/admin/retailers/list.do'/>">here</a> to choose a retailer site.
</c:when>
<c:otherwise>
<fieldset style="padding: 10px;">
<legend><strong>Create Event</strong></legend>
<form:form id="target" modelAttribute="event" method="POST" action="validation-process.do"
           enctype="multipart/form-data">
<span id="error_message"></span>

<div>
<div class="row">
    <span>Name<b style="color:red">*</b> :</span>
    <form:input id="contentName" maxlength="255" autocomplete="false" path="name"/>
    <span id="name_error" class="error_msg">
        <form:errors path="name"/>
    </span>
</div>

<div class="row">
    <span>Description<b style="color:red">*</b> :</span>
    <form:textarea autocomplete="false" path="description"/>
    <span id="description_error" class="error_msg">
        <form:errors path="description"/>
    </span>
</div>

<div id="image" class="row">
        <%--http://www.w3schools.com/TAGS/att_input_accept.asp--%>
    <p><span>Hero Image:</span>
        <input type="file" name="thumbnailFile" id="thumbnailFile" accept="image/*">(jpg, gif, png)&nbsp;&nbsp;<a
                href="#" id="AddUrl">Attach a file</a> <br/>
        <span id="ajaxMsg" class="imageOption"></span>
        <span id="imageFormat" class="error_msg">
            <c:if test="${unsupportedFile}">
                Unsupported Format Image
            </c:if>
        </span>
    </p>

    <p id="imageOption" class="imageOption"></p>
</div>

<div class="row">
    <span>Start Date :</span>
    <spring:bind path="startDate">
        <input type="text" readonly="true" name="startDate" id="startDate"
               value="<fmt:formatDate value="${event.startDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
    </spring:bind>
    <span id="start_date_error" class="error_msg"><form:errors path="startDate"/></span>
</div>
<div class="row">
    <span>End Date :</span>
    <spring:bind path="endDate">
        <input type="text" readonly="true" name="endDate" id="endDate"
               value="<fmt:formatDate value="${event.endDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
    </spring:bind>
    <span id="end_date_error" class="error_msg"><form:errors path="endDate"/></span>
</div>

<div class="row">
    <span><form:label path="location">Location<b style="color:red">*</b>:</form:label> </span>
    <span><form:input autocomplete="false" path="location"/></span>
    <span id="location_error" class="error_msg">
        <form:errors path="location"/>
    </span>
</div>

<div class="row">
    <span>Number Of Days:</span>
    <input type="text" name="numberOfDays"
           onblur="onlyNumber(this.form.elements['numberOfDays'].value,'numberOfDays',0)"/>
                        <span id="numberOfDays_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='numberOfDays.format.error'}">
                                            <spring:message code="numberOfDays.format.error"/>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>


<div class="row">
    <span>Info Link :</span>
    <input type="text" name="infoLink" id="infoLink"
           onblur="urlValidation(this.form.elements['infoLink'].value,'infoLink', 0)"/>
    <span id="infoLink_error" class="error_msg">
        <spring:hasBindErrors name="event">
            <c:if test="${errors!=null}">
                <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                    <c:if test="${errors.fieldErrors[loop.index].code=='infoLink.format.error'}">
                        <spring:message code="infoLink.format.error"/>
                    </c:if>
                </c:forEach>
            </c:if>
        </spring:hasBindErrors>
    </span>
</div>

<div class="row">
    <span>Buy Link :</span>
    <input type="text" name="buyLink"
           onblur="urlValidation(this.form.elements['buyLink'].value,'buyLink', 0)"/>
    <span id="buyLink_error" class="error_msg">
        <spring:hasBindErrors name="event">
            <c:if test="${errors!=null}">
                <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                    <c:if test="${errors.fieldErrors[loop.index].code=='buyLink.format.error'}">
                        <spring:message code="buyLink.format.error"/>
                    </c:if>
                </c:forEach>
            </c:if>
        </spring:hasBindErrors>
    </span>
</div>

<div class="row">
    <span>Review Link :</span>
    <input type="text" name="reviewLink"
           onblur="urlValidation(this.form.elements['reviewLink'].value,'reviewLink', 0)"/>
    <span id="reviewLink_error" class="error_msg">
        <spring:hasBindErrors name="event">
            <c:if test="${errors!=null}">
                <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                    <c:if test="${errors.fieldErrors[loop.index].code=='reviewLink.format.error'}">
                        <spring:message code="reviewLink.format.error"/>
                    </c:if>
                </c:forEach>
            </c:if>
        </spring:hasBindErrors>
    </span>
</div>

<div class="row">
    <span>Price :</span>
    <input type="text" name="price"
           onblur="onlyNumber(this.form.elements['price'].value,'price',0)"/>
                        <span id="price_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='price.format.error'}">
                                            <spring:message code="price.format.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='price.empty.error'}">
                                            <spring:message code="price.empty.error"/>
                                        </c:if>--%>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>

<div class="row">
    <span>List Price :</span>
    <input type="text" name="listPrice"
           onblur="onlyNumber(this.form.elements['listPrice'].value,'listPrice',0)"/>
                        <span id="listPrice_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='listPrice.format.error'}">
                                            <spring:message code="listPrice.format.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='listPrice.empty.error'}">
                                            <spring:message code="listPrice.empty.error"/>
                                        </c:if>--%>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>


<div class="row">
    <span>Per N People :</span>
    <input type="text" name="perNpeople"
           onblur="onlyNumber(this.form.elements['perNpeople'].value,'perNpeople',0)"/>
                        <span id="perNpeople_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='perNpeople.format.error'}">
                                            <spring:message code="perNpeople.format.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='perNpeople.empty.error'}">
                                            <spring:message code="perNpeople.empty.error"/>
                                        </c:if>--%>
                                        <c:if test="${errors.fieldErrors[loop.index].code=='perNpeople.required.value'}">
                                            <spring:message code="perNpeople.required.value"/>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>

<div class="row">
    <span>Minimum People :</span>
    <input type="text" name="minimumPeople"
           onblur="onlyNumber(this.form.elements['minimumPeople'].value,'minimumPeople',0)"/>
    <span id="minimumPeople_error" class="error_msg">
        <spring:hasBindErrors name="event">
            <c:if test="${errors!=null}">
                <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                    <c:if test="${errors.fieldErrors[loop.index].code=='minimumPeople.format.error'}">
                        <spring:message code="minimumPeople.format.error"/>
                    </c:if>
                    <c:if test="${errors.fieldErrors[loop.index].code=='minimumPeople.required.error'}">
                        <spring:message code="minimumPeople.required.error"/>
                    </c:if>
                </c:forEach>
            </c:if>
        </spring:hasBindErrors>
    </span>
</div>

<div class="row">
    <span>Class Of Service :</span>
    <input type="text" name="classOfService" id="classOfService"/>
        <span id="classOfService_error" class="error_msg">
        <spring:hasBindErrors name="event">
            <c:if test="${errors!=null}">
                <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                    <c:if test="${errors.fieldErrors[loop.index].code=='classOfService.format.error'}">
                        <spring:message code="classOfService.format.error"/>
                    </c:if>
                </c:forEach>
            </c:if>
        </spring:hasBindErrors>
    </span>
</div>

<div class="row">
    <span>Facebook Likes :</span>
    <input type="text" name="facebookLikes"
           onblur="onlyNumber(this.form.elements['facebookLikes'].value,'facebookLikes',0)"/>
                        <span id="facebookLikes_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='facebookLikes.format.error'}">
                                            <spring:message code="facebookLikes.format.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='facebookLikes.empty.error'}">
                                            <spring:message code="facebookLikes.empty.error"/>
                                        </c:if>--%>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>

<div class="row">
    <span>Twitter :</span>
    <input type="text" name="twitter"
           onblur="onlyNumber(this.form.elements['twitter'].value,'twitter',0)"/>
                        <span id="twitter_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='twitter.format.error'}">
                                            <spring:message code="twitter.format.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='twitter.empty.error'}">
                                            <spring:message code="twitter.empty.error"/>
                                        </c:if>--%>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>

<div class="row">
    <span>Google+ :</span>
    <input type="text" name="googlePluses"
           onblur="onlyNumber(this.form.elements['googlePluses'].value,'googlePluses',0)"/>
                        <span id="googlePluses_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='googlePluses.format.error'}">
                                            <spring:message code="googlePluses.format.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='googlePluses.empty.error'}">
                                            <spring:message code="googlePluses.empty.error"/>
                                        </c:if>--%>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>

<div class="row">
    <span>Rating :</span>
    <input type="text" name="ratings"
           onblur="validateFloat(this.form.elements['ratings'].value,'ratings')"/>
                        <span id="ratings_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='ratings.format.error'}">
                                            <spring:message code="ratings.format.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='ratings.empty.error'}">
                                            <spring:message code="ratings.empty.error"/>
                                        </c:if>--%>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>

<div class="row">
    <span>Stars :</span>
    <input type="text" name="stars"
           onblur="validateFloat(this.form.elements['stars'].value,'stars')"/>
                        <span id="stars_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                                        <c:if test="${errors.fieldErrors[loop.index].code=='stars.format.error'}">
                                            <spring:message code="stars.format.error"/>
                                        </c:if>
                                        <c:if test="${errors.fieldErrors[loop.index].code=='stars.range.error'}">
                                            <spring:message code="stars.range.error"/>
                                        </c:if><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='stars.empty.error'}">
                                            <spring:message code="stars.empty.error"/>
                                        </c:if>--%>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
                        </span>
</div>
<div class="row">
    <table border="0">
        <tr>
            <td><span>Tag it: </span></td>
            <td align="left">
                <table border="0">
                    <tr>
                        <td><a href="#" onClick='Tagview.showTagPopup("EVENT");' class='tag items_action' title="Choose Tag" align="right">Tags</a></td>
                        <td><span id="tags"></span></td>

                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <input type="hidden" name="TagValue" id="TagValue"/>
    <!-- used to identify it doent have an entityID used only in Create JSP pages -->
    <input type="hidden" name="createnewjsptag" id="createnewjsptag" value="createnewjsptag">
    <input type="hidden" name="Tagtype" id="type" value="EVENT">


</div>

</div>
<form:hidden path="retailerSite.id"/>
<input type="hidden" name="siteId" value="${event.retailerSite.id}"/>
<input type="hidden" id="imageExtnStatus" name="imageExtnStatus" value="true"/>

<input type="submit" value="Save"/>
<a class="gbutton" href="<c:url value='/admin/library/view.do?siteID='/>${event.retailerSite.id}#events">Cancel</a>
</form:form>
</fieldset>

</c:otherwise>
</c:choose>
</body>
</html>
