<%@ page import="com.dell.acs.content.EntityConstants" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Edit Link</title>
</head>
<body>

<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

<script type="text/javascript">
    /*Later functionality for checking the duplicate entry.*/
    /*    $(document).ready(function() {

     $('#contentName').blur(function() {
     var linkName = this.value;
     var linkId = null;
     if (document.getElementById('id') != null) {
     linkId = document.getElementById('id').value;
     ajaxCallForName(linkName, "update", linkId);
     } else {
     ajaxCallForName(linkName, "create", linkId)
     }
     });

     });
     function ajaxCallForName(linkName, status, linkId) {

     var name=linkName;
     var linkStatus=status;
     var linkId=linkId;
     $.ajax({
     url:'checkNameAvailability.do',
     data:({name:name,status:linkStatus,id:linkId}),
     success:function(data) {
     if (data == 'found') {
     alert('Link Name already exist, choose another one ');
     $('#contentName').focus();
     }
     }
     });
     }*/
</script>
${navCrumbs}

<c:choose>
    <c:when test="${invalidType ==  true}">
        <a style="color: red;"><b>NOTE : Trying to load Content type which is not of type LINK and is of
            type ${contentType}.</b></a>
    </c:when>
    <c:otherwise>
        <fieldset style="padding: 10px;">
            <legend><strong>Edit Link</strong></legend>
            <form:form modelAttribute="document" method="POST" action="edit.do" enctype="multipart/form-data">
                <div>
                    <div class="row">
                        <span>Title<b style="color:red">*</b> :</span>
                        <form:input id="contentName" maxlength="255" autocomplete="false" path="name"/>
                                <span id="title_error" class="error_msg">
                                    <form:errors path="name"/>
                                </span>
                    </div>
                    <div class="row">
                        <span>Description :</span>
                        <form:textarea id="contentDescription" maxlength="255" autocomplete="false" path="description"/>
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
                        <span>URL<b style="color:red">*</b> :</span>
                        <form:input autocomplete="false" path="url"/> Supported protocols: http, https,www
                                <span id="url_error" class="error_msg">
                                    <form:errors path="url"/>
                                </span>
                    </div>

                    <div class="row">

                        <table>
                            <tr>
                                <td><span>Tag it</span></td>
                                <td>
                                    <table>
                                        <tr>
                                            <td>
                                                <a href="#" onClick='Tagview.showTagPopup("LINK");'
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
                                    <input type="hidden" name="TagValue" id="TagValue" value="${tags}"/>

                                </td>
                            </tr>
                        </table>

                    </div>
                </div>
                <form:hidden path="retailerSite.id"/>
                <input type="hidden" name="type" value="<%=EntityConstants.Entities.LINK.getId()%>"/>
                <input type="hidden" id="id" name="id" value="${document.id}"/>
                <input type="submit" value="Save"/>
                <a class="gbutton"
                   href="<c:url value='/admin/library/view.do?siteID=${document.retailerSite.id}#links'/>">Cancel</a>
            </form:form>
        </fieldset>

    </c:otherwise>
</c:choose>
</body>
</html>
