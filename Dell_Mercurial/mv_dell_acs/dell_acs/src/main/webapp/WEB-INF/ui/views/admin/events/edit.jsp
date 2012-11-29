<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="org.springframework.validation.Errors" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.springframework.validation.FieldError" %>
<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@ page import="java.util.Map" %>
<%@ page isELIgnored="false" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>
 <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>

<script type="text/javascript">

$(document).ready(function() {

    var nextUploadId = 0;
    var functionCall = 0;
    var noOfInpuFieldCreated = 0;

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

    /*Later functionality for checking the duplicate entry.*/
    $('#contentName').blur(function() {

        var eventName = this.value;
        var eventId = document.getElementById('id').value;
        $.ajax({
            url:'checkNameAvailability.do',
            data:({name:eventName,status:"update",id:eventId}),
            success:function(data) {
                if (data == 'found') {
                    jAlert('Event Name already exist, choose another one ', 'Duplicate Alert', function() {
                        $('#contentName').focus();
                    });
                }
            }
        });
    });

    $('#editEventImageFileId').error(function() {
        var imageStatus = $('#reset_img_flag').value;
        $('#imageExistenceMessage').html('<spring:message code="imageFile.notExist"/>');
    });
    function doAjax(file) {
        $.ajax({
            url: 'ajaxResponse.do',
            data: ({fileName : file}),
            success: function(data) {
                if (data != null) {
                    if (data == 'File Selected') {
                        $('#ajaxMsg').css('color', 'green').html(data);
                        $('#formatError').hide();
                        $('#imageExtnStatus').val('true');
                    } else {
                        $('#ajaxMsg').css('color', 'red').html(data);
                        $('#formatError').hide();
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
function processOptionalImage(obj, key, value, flag, propertyId, retailerSiteId) {
    var elementId = obj.id;
    var flagStatus = flag;
    if (flag) {
        removeOrUpdateOptionalImage(key, value, 'update', propertyId, elementId, retailerSiteId);
    } else {
        if (!(obj.checked)) {
            removeOrUpdateOptionalImage(key, value, 'remove', propertyId, elementId, retailerSiteId);
        }
    }

}
function removeOrUpdateOptionalImage(key, value, flagStatus, propertyId, elementId, retailerSiteId) {

    var cnf = confirm("Are you sure you want to " + flagStatus + " image");
    if (cnf) {
        $.ajax({
            url: 'removeOrUpdateOptionalImageKey.do',
            data: ({imageKey : key,imageValue:value,flag:flagStatus,id:propertyId,retailerSiteId:retailerSiteId}),
            success: function(data) {
                if (data != null) {
                    if (data == "updated") {
                        $('src_' + elementId).attr({'src':'<c:url value="/images/no_image.png"/>'});
                        window.location.reload();
                    }
                    if (data == "deleted") {
                        window.location.reload();
                    }
                }
            }

        });
    }
}
function adjustWindow(ele) {
    var newX = ($(window).width() - $(ele).width()) / 2;
    var newY = ($(window).height() - $(ele).height()) / 2;
    $(ele.id).css("height", newY);
    $(ele.id).css("width", newX);
}
function hidePreview() {
    $('#preview').hide();
}
function showOptionalImages(noOfOptionalImage) {
    if (document.getElementById('optionalThubnail').style.display == "block") {
        document.getElementById('optionalThubnail').style.display = "none";
        document.getElementById('imageCounter').style.display = "none";
        document.getElementById('optionalImageLevel').innerHTML = "";
        document.getElementById('imageDisplayer').innerHTML = "Show the Optional Image";
    } else {
        document.getElementById('optionalThubnail').style.display = "block";
        if(noOfOptionalImage>0){
           document.getElementById('optionalImageLevel').innerHTML = "Optional Image";
        }else{
           document.getElementById('optionalImageLevel').innerHTML="No Optional Image Found";
        }
        document.getElementById('imageDisplayer').innerHTML = "Hide the Optional Image";
        document.getElementById('imageCounter').style.display = "block";
    }


}
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
        if (/^[a-zA-Z0-9_]+$/.test(val)) {
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
        if ((/^[+]?[0-9]+$/.test(val)|| /^[+]?\d+\.\d+$/.test(val)) && (val>=0.0 && val <= 5.0)) {
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

</script>
<%
    boolean chkImageFileExtension = ((request.getParameter("imageFileExtensionNotMatched") != null) ?
            Boolean.parseBoolean((String) request.getParameter("imageFileExtensionNotMatched")) : false);
    boolean checkErrorCodeExistence = false;
    boolean checkSpaceErrorCode = false;
%>
<c:choose>
<c:when test="${ empty event}">
    Event Not Found
</c:when>
<c:otherwise>

${navCrumbs}

<fieldset style="padding: 10px;">
<legend><strong>Edit Events</strong></legend>
<form:form id="target" modelAttribute="event" method="POST" action="edit.do" enctype="multipart/form-data">

<span id="error_message">
</span>

<div>
<div class="row">
    <span><form:label path="name">Name<b style="color:red">*</b>:</form:label></span>

    <span><form:input id="contentName" maxlength="255" autocomplete="false" value="${event.name}" path="name"/></span>
    <span id="name_error" class="error_msg">
        <form:errors path="name"/>
    </span>
</div>
<div class="row">
    <span><form:label path="description">Description<b style="color:red">*</b>:</form:label></span>
    <span><form:textarea autocomplete="false" value="${event.description}" path="description"/></span>
    <span id="description_error" class="error_msg">
        <form:errors path="description"/>
    </span>
</div>
<div id="image" class="row">
        <%--http://www.w3schools.com/TAGS/att_input_accept.asp--%>
    <p><span>Hero Image:</span>
        <c:choose>
        <c:when test="${event.properties['dell.event.thumbnail'] != null}">
            <%--Hover effect for Thumbnail--%>
                <span id="preview" onmouseover="adjustWindow(this)">
                    <img alt="Thumbnail" id="editEventImageFileId" height="50" width="50"
                         src="<c:url value="${event.properties['dell.event.thumbnail']}"/>?t=<%=System.currentTimeMillis()%>"/>
                                            <span id="img_reset_action" class="refresh action_link"
                                                  style="margin-left: 20px"
                                                  title="Click to reset the image"
                                                  onclick="javascript:CIUtils.resetThumbnailImage();"></span>
                                            <span id="resetImageMsg"></span>
                            <input id="reset_img_flag" name="reset_img_flag" type="hidden" value="false"/>
                        <%--<input type="button" name="Reset" value="Reset Image">--%>
                </span>

        </c:when>
        <c:otherwise>
                 <span>
                  <img alt="Thumbnail" height="50" width="50"
                       src="<c:url value="/images/no_image.png"/>"/>
                  </span>
        </c:otherwise>
        </c:choose>

    <p style="margin-left:152px">
        <input type="file" name="thumbnailFile" id="thumbnailFile" accept="image/*"> (jpg, gif, png)<br/>
        <span style="margin-left:276px">
            <a id="imageDisplayer" href="#" onclick="showOptionalImages('${event.properties['dell.event.total.optional.image']-event.properties['dell.event.total.deleted.optional.image']}');">Show Optional Images</a> &nbsp;&nbsp;
            <a href="#" id="AddUrl">Attach a file</a><br/>
        </span>
    </p>
    <span id="ajaxMsg" class="imageOption"></span><br/>
        <span id="formatError" class="error_msg" style="margin-left:152px">
                <%
                    boolean imageUploadedStatus = ((request.getParameter("notUploaded") != null) ?
                            Boolean.parseBoolean((String) request.getParameter("notUploaded")) : false);
                %>
            <c:choose>
                <c:when test="<%=chkImageFileExtension%>">
                    <spring:message code="thumbnailFile.extension.error"/>
                </c:when>

                <c:when test="<%=imageUploadedStatus%>">
                    <spring:message code="imageFile.upload.status"/>
                </c:when>
                <c:otherwise>
                    <span id="imageExistenceMessage"></span>
                </c:otherwise>
            </c:choose>
        </span>

    </p>
    <p id="imageOption" class="imageOption"></p>
</div>
<div class="row imageOption">
    <span id="optionalImageLevel"></span>
    <span>
        <c:choose>
            <c:when test="${noOfOptionalImage != null}">

                <c:set var="counter" value="0" scope="page"/>

                <span id="optionalThubnail" style="display:none;">

                    <table width="800px">
                        <tr>
                            <td>
                                <div class="thumbnailFileImageWrapper">
                                    <c:forEach var="optionalImage" items="${noOfOptionalImage}">
                                        <c:set var="counter" value="0" scope="page"/>
                                        <c:set var="key" value="${fn:replace(optionalImage.key,'.','dot')}"
                                               scope="page"/>
                                        <c:set var="fileName"
                                               value="${fn:substringBefore(fn:substringAfter(fn:substringAfter(fn:substringAfter(fn:substringAfter(fn:substringAfter(fn:substringAfter(optionalImage.value,'/'),'/'),'/'),'/'),'/'),'/'),'.')}"/>

                                        <div class="optionalPreview" onmouseover="adjustWindow(this)">
                                            <c:choose>
                                                <c:when test="${!(empty optionalImage.value)}">
                                                    <img id="src_${counter}" alt="Thumbnail" class="thumbnailFileImage"
                                                         height="50"
                                                         width="50"
                                                         src="<c:url value="${optionalImage.value}"/>"
                                                         onmouseover="adjustWindow(this)"/> <input
                                                        id="checkbox_${counter}"
                                                        type="checkbox"
                                                        checked="true"
                                                        onclick="processOptionalImage(this,'${optionalImage.key}','${optionalImage.value}',false,'${event.id}',${event.retailerSite.id});"/>
                                                    <span id="img_reset_action_${counter}" class="refresh action_link"
                                                          style="margin-left: 20px"
                                                          title="Click to reset the image"
                                                          onclick="processOptionalImage(this,'${optionalImage.key}','${optionalImage.value}',true,'${event.id}',${event.retailerSite.id});"></span></br>
                                                    <c:set var="counter" value="${counter+1}" scope="page"/>
                                                    <c:out value="${fileName}_${fn:substringBefore(fn:substringAfter(optionalImage.value, '/event/'),'/')}_${fn:substringAfter(optionalImage.key, 'image')}"/><br/>
                                                    <input type="file"
                                                           name="${optionalImage.key}"
                                                           id="${fileName}_${fn:substringBefore(fn:substringAfter(optionalImage.value, '/event/'),'/')}_${counter}">
                                                    <input type="hidden" name="${optionalImage.key}"
                                                           value="${optionalImage.value}">
                                                </c:when>
                                            </c:choose>
                                                <%--name="${fileName}_${fn:substringBefore(fn:substringAfter(optionalImage.value, '/event/'),'/')}_${counter}"--%>
                                        </div>

                                    </c:forEach>
                                </div>
                            </td>
                        </tr>
                    </table>
                </span>

            </c:when>
            <c:otherwise>
                <span>
                            <img alt="Thumbnail" height="50" width="50"
                                 src="<c:url value="/images/no_image.png"/>"/>
                </span>
            </c:otherwise>
        </c:choose>
    </span>
</div>
<div id="imageCounter" align="right" style="display:none">
<%--Number of Optional Image Added for
    Event: ${event.properties['dell.event.total.optional.image']-event.properties['dell.event.total.deleted.optional.image']}--%>
</div>
<div class="row">
    <span><form:label path="startDate">Start Date :</form:label></span>

    <span><spring:bind path="startDate">
        <input type="text" readonly="true" name="startDate" id="startDate"
               value="<fmt:formatDate value="${event.startDate}" pattern="MM/dd/yyyy HH:mm"/>"/>
    </spring:bind></span>
    <span id="start_date_error" class="error_msg"></span>

</div>
<div class="row">
    <span><form:label path="endDate">End Date :</form:label></span>

    <span><spring:bind path="endDate">
        <input type="text" readonly="true" id="endDate" name="endDate"
               value="<fmt:formatDate value="${event.endDate}" pattern="MM/dd/yyyy HH:mm"/>"/>
    </spring:bind></span>
    <span id="end_date_error" class="error_msg"></span>

</div>

<div class="row">
    <span><form:label path="location">Location<b style="color:red">*</b>:</form:label> </span>
    <span><form:input autocomplete="false" value="${event.location}" path="location"/></span>
    <span id="location_error" class="error_msg">
        <form:errors path="location"/>
    </span>
</div>

<div class="row">
    <span>Number Of Days:</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.numberOfDays'] != null}">

            <input type="text" name="numberOfDays" id="numberOfDays"
                   value="${event.properties['dell.event.numberOfDays']}"
                   onblur="onlyNumber(this.form.elements['numberOfDays'].value,'numberOfDays', 0)"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="numberOfDays" id="numberOfDays"
                   onblur="onlyNumber(this.form.elements['numberOfDays'].value,'numberOfDays', 0)"/>
        </c:otherwise>
    </c:choose>
    <span id="numberOfDays_error" class="error_msg">
                            <spring:hasBindErrors name="event">
                                <c:if test="${errors!=null}">
                                    <c:forEach items="${errors.fieldErrors}" varStatus="loop"><%--
                                        <c:if test="${errors.fieldErrors[loop.index].code=='numberOfDays.empty.error'}">
                                            <spring:message code="numberOfDays.empty.error"/>
                                        </c:if>--%>
                                        <c:if test="${errors.fieldErrors[loop.index].code=='numberOfDays.format.error'}">
                                            <spring:message code="numberOfDays.format.error"/>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
    </span>
     </span>
</div>
<div class="row">
    <span>Info Link :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.infoLink'] != null}">

            <input type="text" name="infoLink" id="infoLink" value="${event.properties['dell.event.infoLink']}"
                   onblur="urlValidation(this.form.elements['infoLink'].value,'infoLink',0)"/>
            <span id="infoLink_error" class="error_msg"></span>
        </c:when>
        <c:otherwise>
            <input type="text" name="infoLink" id="infoLink"
                   onblur="urlValidation(this.form.elements['infoLink'].value,'infoLink',0)"/>
            <span id="infoLink_error" class="error_msg"></span>
        </c:otherwise>
    </c:choose>
    <span class="error_msg">
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
    </span>
</div>
<div class="row">
    <span>Buy Link :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.buyLink'] != null}">

            <input type="text" name="buyLink" id="buyLink" value="${event.properties['dell.event.buyLink']}"
                   onblur="urlValidation(this.form.elements['buyLink'].value,'buyLink',0)"/>
            <span id="buyLink_error" class="error_msg"></span>
        </c:when>
        <c:otherwise>
            <input type="text" name="buyLink" id="buyLink"
                   onblur="urlValidation(this.form.elements['buyLink'].value,'buyLink',0)"/>
            <span id="buyLink_error" class="error_msg"></span>
        </c:otherwise>
    </c:choose>
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
    </span>
</div>
<div class="row">
    <span>Review Link :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.reviewLink'] != null}">

            <input type="text" name="reviewLink" id="reviewLink"
                   value="${event.properties['dell.event.reviewLink']}"
                   onblur="urlValidation(this.form.elements['reviewLink'].value,'reviewLink',0)"/>
            <span id="reviewLink_error" class="error_msg"></span>
        </c:when>
        <c:otherwise>
            <input type="text" name="reviewLink" id="reviewLink"
                   onblur="urlValidation(this.form.elements['reviewLink'].value,'reviewLink',0)"/>
            <span id="reviewLink_error" class="error_msg"></span>
        </c:otherwise>
    </c:choose>
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
    </span>
</div>
<div class="row">
    <span>Price :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.price'] != null}">

            <input type="text" name="price" id="price" value="${event.properties['dell.event.price']}"
                   onblur="onlyNumber(this.form.elements['price'].value,'price', 0)"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="price" id="price"
                   onblur="onlyNumber(this.form.elements['price'].value,'price', 0)"/>
        </c:otherwise>
    </c:choose>
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
</span>
</div>
<div class="row">
    <span>List Price :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.listPrice'] != null}">

            <input type="text" name="listPrice" id="listPrice" value="${event.properties['dell.event.listPrice']}"
                   onblur="onlyNumber(this.form.elements['listPrice'].value,'listPrice', 0)"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="listPrice" id="listPrice"
                   onblur="onlyNumber(this.form.elements['listPrice'].value,'listPrice', 0)"/>
        </c:otherwise>
    </c:choose>
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
    </span>
</div>
<div class="row">
    <span>Per N People :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.perNpeople'] != null}">
            <input type="text" name="perNpeople" id="perNpeople" value="${event.properties['dell.event.perNpeople']}"
                   onblur="onlyNumber(this.form.elements['perNpeople'].value,'perNpeople', 0)"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="perNpeople" id="perNpeople"
                   onblur="onlyNumber(this.form.elements['perNpeople'].value,'perNpeople', 0)"/>
        </c:otherwise>
    </c:choose>
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
                                    </c:forEach>
                                </c:if>
                            </spring:hasBindErrors>
    </span>
    </span>
</div>
<div class="row">
    <span>Minimum People : </span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.minimumPeople'] != null}">

            <input type="text" name="minimumPeople" id="minimumPeople"
                   value="${event.properties['dell.event.minimumPeople']}"
                   onblur="onlyNumber(this.form.elements['minimumPeople'].value,'minimumPeople', 0)"/>
            <span id="minimumPeople_error" class="error_msg"></span>
        </c:when>
        <c:otherwise>
            <input type="text" name="minimumPeople" id="minimumPeople"
                   onblur="onlyNumber(this.form.elements['minimumPeople'].value,'minimumPeople', 0)"/>
            <span id="minimumPeople_error" class="error_msg"></span>
        </c:otherwise>
    </c:choose>
    <span id="minimumPeople_error" class="error_msg">
        <spring:hasBindErrors name="event">
            <c:if test="${errors!=null}">
                <c:forEach items="${errors.fieldErrors}" varStatus="loop">
                    <c:if test="${errors.fieldErrors[loop.index].code=='minimumPeople.format.error'}">
                        <spring:message code="minimumPeople.format.error"/>
                    </c:if><%--
                    <c:if test="${errors.fieldErrors[loop.index].code=='minimumPeople.required.error'}">
                        <spring:message code="minimumPeople.required.error"/>
                    </c:if>--%>
                </c:forEach>
            </c:if>
        </spring:hasBindErrors>
    </span>
    </span>

</div>
<div class="row">
    <span>Class Of Service :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.classOfService'] != null}">

            <input type="text" name="classOfService" id="classOfService"
                   value="${event.properties['dell.event.classOfService']}"/>
            <span id="classOfService_error" class="error_msg"></span>
        </c:when>
        <c:otherwise>
            <input type="text" name="classOfService" id="classOfService"/>
            <span id="classOfService_error" class="error_msg"></span>
        </c:otherwise>
    </c:choose>
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
    </span>

</div>
<div class="row">
    <span>Facebook Likes : </span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.facebookLikes'] != null}">

            <input type="text" name="facebookLikes" id="facebookLikes"
                   value="${event.properties['dell.event.facebookLikes']}"
                   onblur="onlyNumber(this.form.elements['facebookLikes'].value,'facebookLikes', 0)"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="facebookLikes" id="facebookLikes"
                   onblur="onlyNumber(this.form.elements['facebookLikes'].value,'facebookLikes', 0)"/>
        </c:otherwise>
    </c:choose>
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
    </span>

</div>
<div class="row">
    <span>Twitter :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.twitter'] != null}">

            <input type="text" name="twitter" id="twitter" value="${event.properties['dell.event.twitter']}"
                   onblur="onlyNumber(this.form.elements['twitter'].value,'twitter', 0)"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="twitter" id="twitter"
                   onblur="onlyNumber(this.form.elements['twitter'].value,'twitter', 0)"/>
        </c:otherwise>
    </c:choose>
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
    </span>

</div>
<div class="row">
    <span>Google+ :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.googlePluses'] != null}">

            <input type="text" name="googlePluses" id="googlePluses"
                   value="${event.properties['dell.event.googlePluses']}"
                   onblur="onlyNumber(this.form.elements['googlePluses'].value,'googlePluses', 0)"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="googlePluses" id="googlePluses"
                   onblur="onlyNumber(this.form.elements['googlePluses'].value,'googlePluses', 0)"/>
        </c:otherwise>
    </c:choose>
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
    </span>

</div>
<div class="row">
    <span>Ratings :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.ratings'] != null}">

            <input type="text" name="ratings" id="ratings" value="${event.properties['dell.event.ratings']}"
                   onblur="validateFloat(this.form.elements['ratings'].value,'ratings')"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="ratings" id="ratings"
                   onblur="validateFloat(this.form.elements['ratings'].value,'ratings')"/>
        </c:otherwise>
    </c:choose>
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
    </span>
</div>
<div class="row">
    <span>Stars :</span>
    <span>
    <c:choose>
        <c:when test="${event.properties['dell.event.stars'] != null}">

            <input type="text" name="stars" id="stars" value="${event.properties['dell.event.stars']}"
                   onblur="validateFloat(this.form.elements['stars'].value,'stars')"/>
        </c:when>
        <c:otherwise>
            <input type="text" name="stars" id="stars"
                   onblur="validateFloat(this.form.elements['stars'].value,'stars')"/>
        </c:otherwise>
    </c:choose>
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
    </span>

</div>

<div class="row">
    <table>
        <tr>
            <td>
                <span>Tags :</span>
            </td>
            <td>
            <table>
                        <tr>
                            <td>
                                <a href="#" onClick='Tagview.showTagPopup("EVENT");' class='tag items_action'
                                   title="Choose Tag"
                                   align="right">Tags</a>
                            </td>
                            <td>
                                <span id="tags" name="tags"></span>
                                <script type="text/javascript">
                                    Tagview.trimtext('${tags}');
                                    $('#tag').val('${tags}');
                                    $('#TagValue').val('${tags}');
                                </script>
                            </td>
                        </tr>

                    </table>
                <input type="hidden" name="TagValue" id="TagValue" value="${tags}"/>
            </td>
        </tr>
    </table>
</div>

</div>
<form:hidden path="id"/>
<input type="hidden" id="siteID" name="siteID" value="${event.retailerSite.id}"/>
<input type="hidden" id="imageExtnStatus" name="imageExtnStatus" value="true"/>
<input type="submit" value="Update"/>
<a class="gbutton" href="<c:url value='/admin/library/view.do?siteID='/>${event.retailerSite.id}#events">Cancel</a>
</form:form>
</fieldset>
</c:otherwise>
</c:choose>
