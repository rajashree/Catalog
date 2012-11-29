<%@ page import="com.dell.acs.content.EntityConstants" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Add Article</title>
</head>
<body>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

<%--<script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>--%>


${navCrumbs}

<c:choose>
    <c:when test="${ empty article.retailerSite}">
        You can add Articles for a Retailer site ONLY. <br>Click
        <a href="<c:url value='/admin/retailers/list.do'/>">here</a> to choose a retailer site.
    </c:when>
    <c:otherwise>

        <fieldset style="padding: 10px;">
            <legend><strong>Create Article</strong></legend>
                <%-- http://jqueryui.com/demos/autocomplete/#multiple-remote --%>
            <form:form modelAttribute="article" commandName="article" method="POST" action="add.do"
                       enctype="multipart/form-data">
                <span id="error_message"><form:errors/></span>
                <table>
                    <tbody>
                    <tr>

                        <td><form:label path="name">Title:<b style="color:red">*</b> :</form:label></td>
                        <td>
                            <form:input autocomplete="false" path="name" id="name"/>
                            <span id="name_error" class="error_msg">
                                 <form:errors path="name"/>
                            </span>
                        </td>

                    </tr>
                    <tr>
                        <td><form:label path="description">Description :</form:label></td>
                        <td>
                            <form:textarea path="description" id="contentDescription"/>
                        </td>

                    </tr>

                    <tr>
                        <td><form:label path="body">Body:<b style="color:red">*</b></form:label></td>
                        <td><form:textarea path="body" id="articleBody"/>
                             <span id="body_error" class="error_msg">
                                 <form:errors path="body"/>
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
                        <td>
                            <table border="0">
                                <tr>
                                    <td><span>Tag it: </span></td>
                                    <td align="left">
                                        <table>
                                          <tr>
                                            <td>
                                        <a href="#" onClick='Tagview.showTagPopup("ARTICLE");' class='tag items_action' title="Choose Tag" align="right">Tags</a>
                                                </td>
                                              <td>
                                                <span id="tags"></span>
                                            </td>
                                            </tr>
                                            </table>
                                    </td>
                                </tr>
                            </table>

                            <input type="hidden" name="TagValue" id="TagValue" />
                            <!-- used to identify it doent have an entityID used only in Create JSP pages -->
                            <input type="hidden" name="createnewjsptag" id="createnewjsptag" value="createnewjsptag">
                            <input type="hidden" name="Tagtype" id="type" value="ARTICLE">
                        </td>
                    </tr>
                    </tbody>
                </table>
                <form:hidden path="retailerSite.id"/>
                <form:hidden path="type" value="<%=EntityConstants.Entities.ARTICLE.getId()%>"/>
                <input id="siteId" type="hidden" value="${article.retailerSite.id}"/>
                <input type="submit" value="Save"/>
                <a class="gbutton"
                   href="<c:url value='/admin/library/view.do?siteID='/>${article.retailerSite.id}#articles">Cancel</a>
            </form:form>
        </fieldset>
    </c:otherwise>
</c:choose>
</body>
</html>