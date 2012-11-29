<%@ page import="com.dell.acs.content.EntityConstants" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Edit Videos</title>
</head>
<body>

<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

<script type="text/javascript">
    /*Later functionality for checking the duplicate entry.*/
    $(document).ready(function() {

        /*     $('#contentName').blur(function() {
         var videoName = this.value;
         var videoId = null;
         if (document.getElementById('id') != null) {
         videoId = document.getElementById('id').value;
         ajaxCallForName(videoName, "update", videoId);
         } else {
         ajaxCallForName(videoName, "create", videoId)
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
    /*     function ajaxCallForName(videoName, status, videoId) {

     var name = videoName;
     var videoStatus = status;
     var videoId = videoId;
     $.ajax({
     url:'checkNameAvailability.do',
     data:({name:name,status:videoStatus,id:videoId}),
     success:function(data) {
     if (data == 'found') {
     alert('Video Name already exist, choose another one ');
     $('#contentName').focus();
     }
     }
     });
     }*/
</script>
<div id="breadcrumb">
    ${navCrumbs}
</div>
<c:choose>
    <c:when test="${invalidType ==  true}">
        <a style="color: red;"><b>NOTE : Trying to load Content type which is not of type ARTICLE and is of
            type ${contentType}.</b></a>
    </c:when>
    <c:otherwise>
        <fieldset style="padding: 10px;">
            <legend><strong>Edit Video</strong></legend>
            <form:errors/>
            <form:form id="target" modelAttribute="document" method="POST" action="edit.do"
                       enctype="multipart/form-data">
                <div>
                    <div class="row">
                        <span>Title<b style="color:red">*</b> :</span>
                        <form:input id="contentName" autocomplete="false" path="name"/>
                                <span id="title_error" class="error_msg">
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
                        <p><span>Thumbnail:<b style="color:red">*</b></span>
                            <c:choose>
                            <c:when test="${document.image != null}">
                                <%--Hover effect for Thumbnail--%>
                                    <span id="preview">
                                        <img alt="Thumbnail" id="editEventImageFileId" height="50" width="100"
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
                            <span id="validationMsg" class="error_msg imageOption">
                            <spring:hasBindErrors name="document">
                                <c:if test="${errors!=null}">
                                    <c:if test="${errors.fieldError.code=='NotEmpty.thumbnailFile'}">
                                        <spring:message code="NotEmpty.thumbnailFile"/> &nbsp;&nbsp;
                                    </c:if>
                                    <c:if test="${errors.fieldError.code=='thumbnailFile.extension.error'}">
                                        <spring:message code="thumbnailFile.extension.error"/>
                                    </c:if>
                                </c:if>
                            </spring:hasBindErrors>
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

                            <span id="ajaxMsg" class="imageOption"></span><br/>

                            <span class="imageOption">
                                <input type="file" name="thumbnailFile" id="thumbnailFile" accept="image/*">(Allowed:jpg,jpeg,jpe,jfif,gif,png)
                            </span>
                    </div>
                    <div class="row">
                        <span>Video URL<b style="color:red">*</b> :</span>
                        <form:input autocomplete="false" path="url"/> Supported url http,https,www
                                <span id="url_error" class="error_msg">
                                    <form:errors path="url"/>
                                </span>
                    </div>

                    <div class="row">
                        <table border="0">
                            <tr>
                                <td>
                                    <span>TAG it</span></td>
                                <td>
                                    <table>
                                        <tr>
                                            <td>
                                                <a href="#" onClick='Tagview.showTagPopup("VIDEO");'
                                                   class='tag items_action'
                                                   title="Choose Tag" align="right">Tags</a>
                                            </td>
                                            <td>
                                                <span id="tags"></span>
                                                <script type="text/javascript">
                                                    Tagview.trimtext('${tags}');
                                                    $('#tag').val('${tags}');
                                                    $('#TagValue').val('${tags}');
                                                </script>
                                            </td>
                                        </tr>
                                    </table>


                                </td>
                                <input type="hidden" name="TagValue" id="TagValue" value="${tags}"/>
                            </tr>
                        </table>
                    </div>

                </div>
                <input type="hidden" name="type" value="<%=EntityConstants.Entities.VIDEO.getId()%>"/>
                <form:hidden path="retailerSite.id"/>
                <input type="hidden" id="id" name="id" value="${document.id}"/>
                <input type="hidden" id="imageExtnStatus" name="imageExtnStatus" value="true"/>
                <input type="submit" value="Save"/>
                <a class="gbutton"
                   href="<c:url value='/admin/library/view.do?siteID=${document.retailerSite.id}#videos'/>">Cancel</a>
            </form:form>
        </fieldset>
    </c:otherwise>
</c:choose>
</body>
</html>
