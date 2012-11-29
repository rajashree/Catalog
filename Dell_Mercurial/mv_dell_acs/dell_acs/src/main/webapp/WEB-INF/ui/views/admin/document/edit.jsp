<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="org.springframework.validation.Errors" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.springframework.validation.FieldError" %>
<%@ page import="com.dell.acs.content.EntityConstants" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Edit Document</title>
</head>
<body>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">
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

    $(document).ready(function () {
        var formGetWay = true;
        var fileSize = 0;
        var dbDocFile = $('#docInfo').attr('value');
        /*Later functionality for checking the duplicate entry.*/
        /*        $('#contentName').blur(function() {
         var documentName = this.value;
         var documentId =null;
         if (document.getElementById('id') != null) {
         documentId=document.getElementById('id').value;
         ajaxCallForName(documentName, "update", documentId);
         } else {
         ajaxCallForName(documentName, "create", documentId)
         }
         });*/


        //bind to change event to determine when user chooses a file to upload
        $("#thumbnailFile").bind("change", function (e) {
            //get the file path

            var file = $("#thumbnailFile").val();
            var fileType = "image";
            //pull out the filename
            file = file.replace(/^.*\\/i, "");

            //show to user
            //$("#ajaxMsg").html("You chose: " + file);
            doAjax(file, fileType);

        });

        $("#documentFile").bind("change", function (e) {
            //get the file path

            var file = $("#documentFile").val();
            var fileType = "document";
            //pull out the filename
            file = file.replace(/^.*\\/i, "");
            fileSize = this.files[0].size;

            if (fileSize == 0) {
                formGetWay = false;
            } else {
                //show to user
                //$("#ajaxMsg").html("You chose: " + file);
                doAjax(file, fileType);
            }

        });

        function doAjax(file, fileType) {
            $('#fileNotExist').hide();
            $('#fileExtension').hide();
            $('#uploadStatusFalse').hide();
            $('#resetImageStatusTrue').hide();

            $.ajax({
                url:'ajaxResponse.do',
                data:({fileName:file,fileType:fileType}),
                success:function (data, textStatus, response) {

                    /*                    var type = response.getResponseHeader('Content-Type');
                     var subtype = type.substr(0, type.indexOf(";"));
                     if (subtype == 'text/html') {
                     window.location.reload();
                     }*/
                    if (data != null) {
                        if (data == 'image') {
                            $('#ajaxImageMsg').css('color', 'green').html("image file selected");
                            $('#imageExtnStatus').val('true');
                        } else if (data == 'document') {
                            $('#ajaxDocMsg').css('color', 'green').html("document file selected");
                            $('#docExtnStatus').val('true');
                        } else {

                            if (data == 'imageRejected') {
                                $('#imageExtnStatus').val('false');
                                $('#ajaxImageMsg').css('color', 'red')
                                        .html("file has unsupported format, please upload another file");
                            } else {
                                $('#docExtnStatus').val('false');
                                $('#ajaxDocMsg').css('color', 'red')
                                        .html("file has unsupported format, please upload another file");
                                $('#serverError').html("");
                                formGetWay = false;
                            }

                        }
                    }
                },
                error: function(e) {
                    alert("error");
                    //window.location.reload();
                    window.console.log('Unable to process the request', e);
                    if (e.readyState == 4) {
                        window.location.reload();
                    }
                }
            });
        }

        // Don't allow to submit the form , if user give unsupported format file
        $('#target').submit(function() {

            var imageStatus = $('#imageExtnStatus').val();
            var docStatus = $('#docExtnStatus').val();


            if (imageStatus == 'false') {
                jAlert('Unable to upload the invalid file,please choose another one', 'Image Alert');
                formGetWay = false;
            } else if (docStatus == 'false') {
                jAlert('Unable to upload the invalid file,please choose another one', 'Document Alert');
                formGetWay = false;
            } else if (fileSize == 0) {
                if (dbDocFile != '') {
                    formGetWay = true;
                } else {
                    jAlert('document file has no size', 'File Size Alert');
                    formGetWay = false;
                }
            } else {
                formGetWay = true;
            }
            return formGetWay;
        });


    });
    function adjustWindow(ele) {
        var newX = ($(window).width() - $(ele).width()) / 2;
        var newY = ($(window).height() - $(ele).height()) / 2;
        $(ele.id).css("height", newY);
        $(ele.id).css("width", newX);
    }
    /*Later functionality for checking the duplicate entry.*/
    /*    function ajaxCallForName(documentName, status, documentId) {

     var name=documentName;
     var documentStatus=status;
     var documentId=documentId;
     $.ajax({
     url:'checkNameAvailability.do',
     data:({name:name,status:documentStatus,id:documentId}),
     success:function(data) {
     if (data == 'found') {
     alert('Document Name already exist, choose another one ');
     $('#contentName').focus();
     }
     }
     });
     }*/

</script>

<%
    boolean chkImageFileExtension = ((request.getParameter("imageFileExtensionNotMatched") != null) ?
            Boolean.parseBoolean((String) request.getParameter("imageFileExtensionNotMatched")) : false);
    boolean chkDocFileExtension = ((request.getParameter("docFileExtensionNotMatched") != null) ?
            Boolean.parseBoolean((String) request.getParameter("docFileExtensionNotMatched")) : false);
%>

${navCrumbs}
<c:choose>
<c:when test="${invalidType ==  true}">
    <a style="color: red;"><b>NOTE : Trying to load Content type which is not of type DOCUMENT and is of
        type ${contentType}.</b></a>
</c:when>
<c:otherwise>
<c:choose>
<c:when test="${document != null}">

<fieldset style="padding: 10px;">
<legend><strong>Edit Document</strong></legend>
<form:form id="target" name="form1" modelAttribute="document" commandName="document" method="POST"
           enctype="multipart/form-data"
           action="edit.do">
<span id="error_message"></span>
<table>
<tr>
    <td><form:label path="name">Name:<b style="color:red">*</b></form:label></td>

    <td><form:input id="contentName" maxlength="255" autocomplete="false" path="name"
                    onblur="CIAutoComplete.validateFormFields();"/>
                    <span id="name_error" class="error_msg">
                    <form:errors path="name"/>
                    </span>
    </td>
</tr>

<tr>
    <td><form:label path="description">Description:<b style="color:red">*</b></form:label></td>
    <td><form:textarea path="description"/>
                <span id="description_error" class="error_msg">
                   <form:errors path="description"/>
                </span>
    </td>
</tr>

<tr>
    <td><form:label path="author">Author:</form:label></td>
    <td><form:input path="author"/></td>
</tr>

<tr>
    <td><form:label path="source">Source:</form:label></td>
    <td><form:input path="source"/></td>
</tr>

<tr>
    <td><form:label path="abstractText">Abstract Text:</form:label></td>
    <td><form:input path="abstractText"/></td>
</tr>

<tr>
    <td><form:label path="publishDate">Publish Date:</form:label></td>
    <td>
        <spring:bind path="publishDate">
            <input type="text" readonly="true" id="publishDate" name="publishDate"
                   value="<fmt:formatDate value="${document.publishDate}" pattern="MM/dd/yyyy HH:mm"/>"/>
        </spring:bind>
        <span id="publish_date_error" class="error_msg"><form:errors path="publishDate"/></span>
    </td>
</tr>
<tr>
    <td><form:label path="startDate">Start Date:</form:label></td>
    <td>
        <spring:bind path="startDate">
            <input type="text" readonly="true" name="startDate" id="startDate"
                   value="<fmt:formatDate value="${document.startDate}" pattern="MM/dd/yyyy HH:mm"/>"/>
        </spring:bind>
        <span id="start_date_error" class="error_msg"></span>
    </td>
</tr>

<tr>
    <td><form:label path="endDate">End Date:</form:label></td>
    <td>
        <spring:bind path="endDate">
            <input type="text" readonly="true" name="endDate" id="endDate"
                   value="<fmt:formatDate value="${document.endDate}" pattern="MM/dd/yyyy HH:mm"/>"/>
        </spring:bind>
        <span id="end_date_error" class="error_msg"></span>
    </td>
</tr>

<tr>
    <td>Image:</td>
    <td>
        <c:if test="${imageStatus=='true'}">
            <spring:bind path="image">
                <c:choose>
                    <c:when test="${document.image != null}">
                        <!-- Hover effect for Thumbnail -->
                        <div id="preview" onmouseover="adjustWindow(this)">
                            <img alt="Image" id="ImageFileId" height="50" width="50"
                                 src="<c:url value="${document.image}"/>?t=<%=System.currentTimeMillis()%>"
                                    />
                                            <span id="img_reset_action" class="refresh action_link"
                                                  style="margin-left: 20px"
                                                  title="Click to reset the image"
                                                  onclick="javascript:CIUtils.resetThumbnailImage();"></span> <input
                                id="reset_img_flag" name="reset_img_flag" type="hidden"
                                value="false"/>

                        </div>
                    </c:when>
                    <c:otherwise>
                        <img alt="Image" height="50" width="50"
                             src="<c:url value="/images/no_image.png"/>"/><br/>
                    </c:otherwise>
                </c:choose>
            </spring:bind>
            <input type="file" name="thumbnailFile" id="thumbnailFile">(Allowed File :jpg,jpeg,jpe,jfif,gif,png)
                        <span class="error_msg">
                                    <div id="ajaxImageMsg">

                                    </div>
                                    <c:choose>
                                        <c:when test="<%=chkImageFileExtension%>">
                                            <div id="fileExtension">
                                                <spring:message code="thumbnailFile.extension.error"/><br/>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <%--                                            <div id="fileNotExist">
                                                <c:if test="${imageExistOrAccessStatus=='false'}">
                                                    <spring:message code="imageFile.notExist"/></br>
                                                </c:if>
                                            </div>--%>
                                        </c:otherwise>
                                    </c:choose>
                        <%--                            <div>
                            <c:if test="${docImageExtensionStatus}"><span class="error_msg">
                                <spring:message code="thumbnailFile.extension.error"/></span>
                            </c:if>
                        </div>--%>

                                    <c:if test="${uploadStatus=='false'}">
                                        <div id="uploadStatusFalse">
                                            <spring:message code="imageFile.upload.status"/>
                                        </div>
                                    </c:if>
                        </span>
        </c:if>

        <c:if test="${imageStatus=='false'}">
            <div>
                <img alt="Image" height="50" width="50"
                     src="<c:url value="/images/no_image.png"/>"/>
                                            <span id="img_reset_action" class="refresh action_link"
                                                  style="margin-left: 20px"
                                                  title="Click to reset the image"
                                                  onclick="javascript:CIUtils.resetThumbnailImage();"></span><input
                    id="reset_img_flag" type="hidden" value="false"/>
            </div>
            <div>
                <input type="file" name="thumbnailFile" id="thumbnailFile">(jpg, gif, png)
                <div id="ajaxImageMsg">

                </div>
                    <%--                                    <div>
                        <c:if test="${docImageExtensionStatus}"><span class="error_msg">
                    <spring:message code="thumbnailFile.extension.error"/></span>
                        </c:if>
                    </div>--%>
                                <span id="image_error" class="error_msg">
                                    <c:if test="${resetImageStatus=='true'}">
                                        <div id="resetImageStatusTrue">
                                            <spring:message code="imageFile.reset.status"/>
                                        </div>
                                    </c:if>
                                    <c:choose>

                                        <c:when test="<%=chkImageFileExtension%>">
                                            <div id="fileExtension">
                                                <spring:message code="thumbnailFile.extension.error"/><br/>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <%--                                            <div id="fileNotExist">
                                                <c:if test="${imageExistOrAccessStatus=='false'}">
                                                    <spring:message code="imageFile.notExist"/></br>
                                                </c:if>
                                            </div>--%>
                                        </c:otherwise>
                                    </c:choose>

<%--                                    <c:if test="${ImageExtensionNotMatched}">
                                        <c:if test="${imageExtensionMatchStatus=='false'}">
                                            <spring:message code="thumbnailFile.extension.error"/>
                                        </c:if>
                                    </c:if>--%>
                                  </span>
            </div>
        </c:if>

    </td>
</tr>

<tr>
    <td>Document:<b style="color:red">*</b></td>
    <td>
        <input type="file" name="documentFile" id="documentFile"> (Allowed File :pdf,doc,docx,xls,xlsx,ppt,pptx,txt)
        <div id="ajaxDocMsg">

        </div>
        <div class="error_msg">
            <c:choose>
                <c:when test="<%=chkDocFileExtension%>">
                    <div id="fileExtension">
                        <spring:message code="doc.extension.error"/><br/>
                    </div>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${docFileExistenceStatus=='true'}">
            Uploaded document:
            <a href="<c:url value="${document.document}"/>"
               title="Right-click the link and choose 'Save Link As...' to save the document"><c:out
                    value="${docName}"/></a>
            <span style="font-size: 9px;">( Right-click the link and choose "Save Link As..." to save the document)</span>
        </c:if>
        <div id="serverError">
            <c:if test="${docFlag!='false'}">
                <c:if test="${docFileExistenceStatus=='false'}">
                    <span id="docError" class="error_msg">Either document not exist or Access Denied</span>
                </c:if>
                            <span class="error_msg">
                                <c:if test="${docStatus}">
                                    <spring:message code="documentFile.null.error"/>
                                </c:if>
                            </span>
            </c:if>
        </div>

    </td>

</tr>
<tr>
    <td>Tag it:</td>
    <td>
        <table>
            <tr>
                <td>
                    <table>
                        <tr>
                            <td>
                                <a href="#" onClick='Tagview.showTagPopup("DOCUMENT");' class='tag items_action'
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
    </td>
</tr>

</tbody>
</table>
<form:hidden path="retailerSite.id"/>
<input type="hidden" id="imageExtnStatus" name="imageExtnStatus" value="true"/>
<input type="hidden" id="docExtnStatus" name="docExtnStatus" value="true"/>
<input type="hidden" id="docInfo" name="dbDocValue" value="${document.document}"/>
<form:hidden path="version"/>
<form:hidden path="id"/>
<input type="submit" value="Update">
<a class="gbutton"
   href="<c:url value='/admin/library/view.do?siteID='/>${document.retailerSite.id}#documents">Cancel</a>
</form:form>
</fieldset>
</c:when>
</c:choose>
</c:otherwise>
</c:choose>
</body>
</html>

