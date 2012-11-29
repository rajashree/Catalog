<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="org.springframework.validation.Errors" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.springframework.validation.FieldError" %>
<%@ page import="com.dell.acs.content.EntityConstants" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Add Link</title>
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
<div id="breadcrumb">
    ${navCrumbs}
</div>
<c:choose>
    <c:when test="${ empty document.retailerSite}">
        You can add link's for a Retailer site ONLY. <br>Click
        <a href="<c:url value='/admin/retailers/list.do'/>">here</a> to choose a retailer site.
    </c:when>
    <c:otherwise>
        <fieldset style="padding: 10px;">
            <legend><strong>Add Link</strong></legend>
            <form:form modelAttribute="document" method="POST" action="add.do" enctype="multipart/form-data">
                <div>
                    <div class="row">
                        <span>Title<b style="color:red">*</b> :</span>
                        <form:input id="contentName" path="name"/>
                                <span id="name_error" class="error_msg">
                                    <form:errors path="name"/>
                                </span>
                    </div>
                    <div class="row">
                        <span>Description :</span>
                        <form:textarea id="contentDescription" path="description"/>
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
                                   value="<fmt:formatDate value="${document.publishDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                        </spring:bind>
                        <span id="publish_date_error" class="error_msg"><form:errors path="publishDate"/></span>
                    </div>


                    <div class="row">
                        <span>Link<b style="color:red">*</b> :</span>
                        <form:input id="contentUrl" path="url"/> Supported protocols: http, https,www
                                <span id="url_error" class="error_msg">
                                    <form:errors path="url"/>
                                </span>
                    </div>
                    <div class="row">
                        <table border="0">
                            <tr>
                                <td><span>Tag it: </span></td>
                                <td align="left">
                                    <table>
                                        <tr>
                                            <td>
                                                <a href="#"
                                                   onClick='Tagview.showTagPopup("Link");'
                                                   class='tag items_action'
                                                   title="Choose Tag" align="right">Tags</a>
                                            </td>
                                            <td>
                                                <span id="tags"></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>

                        <input type="hidden" name="TagValue" id="TagValue"/>
                        <!-- used to identify it doent have an entityID used only in Create JSP pages -->
                        <input type="hidden" name="createnewjsptag" id="createnewjsptag" value="createnewjsptag">
                        <input type="hidden" name="Tagtype" id="type" value="LINK">

                    </div>


                </div>
                <form:hidden path="retailerSite.id"/>
                <input type="hidden" name="type" value="<%=EntityConstants.Entities.LINK.getId()%>"/>
                <input type="submit" value="Save"/>
                <a class="gbutton"
                   href="<c:url value='/admin/library/view.do?siteID=${document.retailerSite.id}#links'/>">Cancel</a>
            </form:form>
        </fieldset>

    </c:otherwise>
</c:choose>
</body>
</html>
