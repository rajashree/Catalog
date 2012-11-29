<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="org.springframework.validation.Errors" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.springframework.validation.FieldError" %>
<%@ page import="com.dell.acs.content.EntityConstants" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Add Document</title>
</head>
<body>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

<script type="text/javascript">
    $(document).ready(function() {
        var formGetWay = true;
        var fileSize = 0;
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

            //pull out the filename
            file = file.replace(/^.*\\/i, "");

            //show to user
            //$("#ajaxMsg").html("You chose: " + file);
            doAjax(file, "image");
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
                doAjax(file, "document");
            }
        });

        function doAjax(file, fileType) {
            $.ajax({
                url: 'ajaxResponse.do',
                data: ({fileName : file,fileType:fileType}),
                success: function(data) {
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
                                formGetWay = false;
                            }

                        }
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
                jAlert('File has no size', 'File Size Alert');
                formGetWay = false;
            } else {
                formGetWay = true;
            }
            return formGetWay;
        });
    });
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

${navCrumbs}

<c:choose>
    <c:when test="${ empty document.retailerSite}">
        You can add document's for a Retailer site ONLY. <br>Click
        <a href="<c:url value='/admin/retailers/list.do'/>">here</a> to choose a retailer site.
    </c:when>
    <c:otherwise>

        <fieldset style="padding: 10px;">
            <legend><strong>Create Document</strong></legend>
                <%-- http://jqueryui.com/demos/autocomplete/#multiple-remote --%>
            <form:form id="target" modelAttribute="document" method="POST" action="add.do"
                       enctype="multipart/form-data">
                <span id="error_message"></span>
                <table>
                    <tbody>

                    <tr>
                        <td><form:label path="name">Name :<b style="color:red">*</b> :</form:label></td>

                        <td><form:input id="contentName" maxlength="255" autocomplete="false" path="name"/>
                            <span id="name_error" class="error_msg">
                                    <form:errors path="name"/>
                            </span></td>
                    </tr>


                    <tr>
                        <td><form:label path="description">Description:<b style="color:red">*</b> :</form:label></td>
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
                                       value="<fmt:formatDate value="${document.publishDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                            </spring:bind>
                            <span id="publish_date_error" class="error_msg"><form:errors path="publishDate"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td><form:label path="startDate">Start Date:</form:label></td>
                        <td>
                            <spring:bind path="startDate">
                                <input type="text" readonly="true" name="startDate" id="startDate"
                                       value="<fmt:formatDate value="${document.startDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                            </spring:bind>
                            <span id="start_date_error" class="error_msg"><form:errors path="startDate"/></span>
                        </td>
                    </tr>
                    <tr>


                        <td><form:label path="endDate">End Date:</form:label></td>
                        <td>
                            <spring:bind path="endDate">
                                <input type="text" readonly="true" id="endDate" name="endDate"
                                       value="<fmt:formatDate value="${document.endDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                            </spring:bind>
                            <span id="end_date_error" class="error_msg"><form:errors path="endDate"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td>Image:</td>
                        <td>
                            <input type="file" name="thumbnailFile" id="thumbnailFile" accept="image/*">(Allowed File
                            :jpg,jpeg,jpe,jfif,gif,png)

                            <div id="ajaxImageMsg">
                                <c:if test="${show}">
                                    <c:if test="${docImageExtensionStatus}"><span class="error_msg"><spring:message
                                            code="thumbnailFile.extension.error"/></span></c:if>
                                </c:if>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td>Attachment:<b style="color:red">*</b></td>
                        <td>
                            <input type="file" name="documentFile" id="documentFile">(Allowed File
                            :pdf,doc,docx,xls,xlsx,ppt,pptx,txt)
                            <div id="ajaxDocMsg">
                                <c:if test="${show}">
                                    <c:if test="${docFileExtensionNotMatched}"><span class="error_msg"><spring:message
                                            code="doc.extension.error"/></span></c:if>
                                </c:if>
                            </div>
                            <span class="error_msg">
                                <c:if test="${docStatus}">
                                    <spring:message code="documentFile.null.error"/>
                                </c:if>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td>Tag it:</td>

                        <td>
                            <table>
                                <tr>

                                    <td>
                                        <a href="#" onClick='Tagview.showTagPopup("DOCUMENT");' class='tag items_action'
                                           title="Choose Tag" align="right">Tags</a>
                                    </td>
                                    <td><span id="tags"></span></td>
                                    <input type="hidden" name="TagValue" id="TagValue"/>
                                    <!-- used to identify it doent have an entityID used only in Create JSP pages -->
                                    <input type="hidden" name="createnewjsptag" id="createnewjsptag"
                                           value="createnewjsptag">
                                    <input type="hidden" name="Tagtype" id="type" value="DOCUMENT">
                                </tr>
                            </table>

                        </td>

                    </tr>

                    </tbody>
                </table>
                <form:hidden path="retailerSite.id"/>
                <form:hidden path="type" value="<%=EntityConstants.Entities.DOCUMENT.getId()%>"/>
                <input type="hidden" id="imageExtnStatus" name="imageExtnStatus" value="true"/>
                <input type="hidden" id="docExtnStatus" name="docExtnStatus" value="true"/>
                <input type="submit" value="Save"/>
                <a class="gbutton"
                   href="<c:url value='/admin/library/view.do?siteID='/>${document.retailerSite.id}#documents">Cancel</a>
            </form:form>
        </fieldset>
    </c:otherwise>
</c:choose>
</body>
</html>