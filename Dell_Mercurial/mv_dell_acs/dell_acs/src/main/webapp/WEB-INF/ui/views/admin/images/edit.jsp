<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="org.springframework.validation.Errors" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.springframework.validation.FieldError" %>
<%@ page import="com.dell.acs.content.EntityConstants" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Edit Image</title>
</head>
<body>

<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>
<style type="text/css">
    #preview  img {
        -webkit-transform: scale(1); /*Webkit: Scale down image to 0.8x original size*/
        -moz-transform: scale(1); /*Mozilla scale version*/
        -o-transform: scale(1); /*Opera scale version*/
        -webkit-transition-duration: 0.5s; /*Webkit: Animation duration*/
        -moz-transition-duration: 0.5s; /*Mozilla duration version*/
        -o-transition-duration: 0.5s; /*Opera duration version*/
        opacity: 0.7; /*initial opacity of images*/
        margin: auto;
    }

    #preview img:hover {
        -webkit-transform: scale(10); /*Webkit: Scale up image to 1.2x original size*/
        -moz-transform: scale(10); /*Mozilla scale version*/
        -o-transform: scale(10); /*Opera scale version*/
        box-shadow: 0px 0px 30px gray; /*CSS3 shadow: 30px blurred shadow all around image*/
        -webkit-box-shadow: 0px 0px 30px gray; /*Safari shadow version*/
        -moz-box-shadow: 0px 0px 30px gray; /*Mozilla shadow version*/
        opacity: 1;
    }

</style>
<script type="text/javascript">
    /*Later functionality for checking the duplicate entry.*/
    $(document).ready(function() {

        /*        $('#contentName').blur(function() {
         var imageName = this.value;
         var imageId = null;
         if (document.getElementById('id') != null) {
         imageId = document.getElementById('id').value;
         ajaxCallForName(imageName, "update", imageId);
         } else {
         ajaxCallForName(imageName, "create", imageId)
         }
         });*/

        $("#thumbnailFile").bind("change", function (e) {
            //get the file path
            var file = $("#thumbnailFile").val();

            //pull out the filename
            file = file.replace(/^.*\\/i, "");

            //show to user
            //$("#ajaxMsg").html("You chose: " + file);
            doAjax(file);
        });
        function doAjax(file) {
            $.ajax({
                url: 'ajaxResponse.do',
                data: ({fileName : file}),
                success: function(data) {
                    if (data != null) {
                        if (data == 'File Selected') {
                            $('#ajaxMsg').css('color', 'green').html(data);
                            $('#image_error').hide();
                            $('#imageExtnStatus').val('true');
                        } else {
                            $('#ajaxMsg').css('color', 'red').html(data);
                            $('#image_error').hide();
                            $('#imageExtnStatus').val('false');
                        }
                    }

                }
            });
        }

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
    /*
     function ajaxCallForName(imageName, status, imageId) {

     var name = imageName;
     var imageStatus = status;
     var linkId = imageId;
     $.ajax({
     url:'checkNameAvailability.do',
     data:({name:name,status:imageStatus,id:imageId}),
     success:function(data) {
     if (data == 'found') {
     alert('Link Name already exist, choose another one ');
     $('#contentName').focus();
     }
     }
     });
     }
     */
    function adjustWindow(ele) {
        var newX = ($(window).width() - $(ele).width()) / 2;
        var newY = ($(window).height() - $(ele).height()) / 2;
        $(ele.id).css("height", newY);
        $(ele.id).css("width", newX);
    }
</script>
<div id="breadcrumb">
    ${navCrumbs}
</div>
<c:choose>
    <c:when test="${invalidType ==  true}">
        <a style="color: red;"><b>NOTE : Trying to load Content type which is not of type IMAGE and is of
            type ${contentType}.</b></a>
    </c:when>
    <c:otherwise>
        <fieldset style="padding: 10px;">
            <legend><strong>Update Image</strong></legend>
            <form:form id="target" modelAttribute="document" method="POST" action="edit.do"
                       enctype="multipart/form-data">
                <div>
                    <div class="row">
                        <span>Title<b style="color:red">*</b> :</span>
                        <form:input id="contentName" autocomplete="false" path="name"/>
                                <span id="name_error" class="error_msg">
                                      <form:errors path="name"/>
                                </span>
                    </div>
                    <div class="row">
                        <span>Description :</span>
                        <form:textarea id="contentDescription" autocomplete="false" path="description"/>
                    </div>

                    <div class="row">
                        <span><form:label path="author">Author:</form:label></span>
                        <form:input path="author"/>
                    </div>

                    <div class="row">
                        <span><form:label path="source">Source:</form:label></span>
                        <form:input path="source"/>
                    </div>

                    <div class="row">
                        <span><form:label path="abstractText">Abstract Text:</form:label></span>
                        <form:input path="abstractText"/>
                    </div>

                    <div class="row">
                        <span><form:label path="publishDate">Publish Date:</form:label></span>
                        <spring:bind path="publishDate">
                            <input type="text" readonly="true" id="publishDate" name="publishDate"
                                   value="<fmt:formatDate value="${document.publishDate}" pattern="MM/dd/yyyy HH:mm"/>"/>
                        </spring:bind>
                        <span id="publish_date_error" class="error_msg"><form:errors path="publishDate"/></span>
                    </div>

                    <div class="row">
                        <p><span>Thumbnail<b style="color:red">*</b> :</span>
                            <c:choose>
                            <c:when test="${document.image != null}">
                                <%--Hover effect for Thumbnail--%>
                                    <span id="preview" onmouseover="adjustWindow(this)">
                                        <img alt="Thumbnail" id="editEventImageFileId" height="50" width="50"
                                             src="<c:url value="${document.image}"/>?t=<%=System.currentTimeMillis()%>"/>
                                            <span id="img_reset_action" class="refresh action_link"
                                                  style="margin-left: 20px"
                                                  title="Click to reset the image"
                                                  onclick="javascript:CIUtils.resetThumbnailImage();"></span> <input
                                            id="reset_img_flag" name="reset_img_flag" type="hidden"
                                            value="false"/><br/>
                                    </span>
                            </c:when>
                            <c:otherwise>
                                     <span>
                                        <img alt="Thumbnail" height="50" width="50"
                                             src="<c:url value="/images/no_image.png"/>"/>
                                            <span id="img_reset_action" class="refresh action_link"
                                                  style="margin-left: 20px"
                                                  title="Click to reset the image"
                                                  onclick="javascript:CIUtils.resetThumbnailImage();"></span> <input
                                             id="reset_img_flag" name="reset_img_flag" type="hidden"
                                             value="true"/><br/>
                                     </span>
                            </c:otherwise>
                            </c:choose>
                            <span id="ajaxMsg" class="imageOption"></span><br/>

                                    <span id="image_error" class="error_msg imageOption">
                                        <spring:hasBindErrors name="document">
                                            <c:if test="${errors!=null}">
                                                <c:if test="${errors.fieldError.code=='NotEmpty.thumbnailFile'}">
                                                    <spring:message code="NotEmpty.thumbnailFile"/>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${errors!=null}">
                                                <c:if test="${errors.fieldError.code=='thumbnailFile.extension.error'}">
                                                    <spring:message code="thumbnailFile.extension.error"/>
                                                </c:if>
                                            </c:if>
                                        </spring:hasBindErrors>
                                        <c:if test="${imageFileExtensionMatched=='false'}">
                                            <spring:message code="thumbnailFile.extension.error"/>&nbsp;&nbsp;
                                        </c:if>
                                        <c:if test="${uploadStatus=='false'}">
                                            <spring:message code="imageFile.upload.status"/> &nbsp;&nbsp;
                                        </c:if>
                                        <c:if test="${imageNotExist=='true'}">
                                            <spring:message code="imageFile.notExist"/> &nbsp;&nbsp;
                                        </c:if>
                                        <c:if test="${resetFlag=='true'}">
                                            <spring:message code="imageFile.reset.status"/> &nbsp;&nbsp;
                                        </c:if>
                                    </span><br/>
                            <span class="imageOption">
                                <input type="file" name="thumbnailFile" id="thumbnailFile" accept="image/*">(Allowed: jpg,jpeg,jpe,jfif,gif,png)
                            </span>
                    </div>
                    <div class="row">
                        <span>CDN URL :</span>${document.url}
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
                                                <a href="#" onClick='Tagview.showTagPopup("IMAGE");'
                                                   class='tag items_action' title="Choose Tag" align="right">Tags</a>
                                            </td>
                                            <td>
                                                <span id="tags"></span>
                                                <script type="text/javascript">
                                                    Tagview.trimtext('${tags}');
                                                    $('#tag').val('${tags}');
                                                    $('#TagValue').val('${tags}');
                                    </script>
                                            </td>
                                            <input type="hidden" name="TagValue" id="TagValue" value="${tags}"/>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <form:hidden path="retailerSite.id"/>
                <input type="hidden" id="id" name="id" value="${document.id}"/>
                <input type="hidden" name="type" value="<%=EntityConstants.Entities.IMAGE.getId()%>"/>
                <input type="hidden" id="imageExtnStatus" name="imageExtnStatus" value="true"/>
                <input type="submit" value="Save"/>
                <a class="gbutton"
                   href="<c:url value='/admin/library/view.do?siteID=${document.retailerSite.id}#images'/>">Cancel</a>
            </form:form>
        </fieldset>

    </c:otherwise>
</c:choose>
</body>
</html>
