<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">
<c:choose>
    <c:when test="${ empty campaign.retailerSite}">
        You can add campaign's for a Retailer site ONLY. Click <a
            href="<c:url value='/admin/retailers/list.do'/>">here</a> to choose.
    </c:when>
    <c:otherwise>
        <h1>Add Campaign for site - <a
                href="http://${campaign.retailerSite.siteUrl}">${campaign.retailerSite.siteName}</a></h1>
        <%-- http://jqueryui.com/demos/autocomplete/#multiple-remote --%>
        <form:form modelAttribute="campaign" method="POST"
                   onsubmit="CIAutoComplete.setHiddenFieldValues();" action="add.do">
            <table>
                <tbody>
                <tr>
                    <td><form:label path="name">Name:</form:label></td>
                    <td><form:input autocomplete="false" path="name"/></td>
                </tr>
                <tr>
                    <td><form:label path="type">Type :</form:label></td>
                    <td>
                        <form:select path="type" valueProperty="label">
                            <form:option value="" label="Select Type"/>
                            <form:options labelProperty="description"/>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td><form:label path="startDate">Start Date:</form:label></td>
                    <td>
                            <%--<form:input autocomplete="false" path="startDate" id="startDate" />--%>
                        <spring:bind path="startDate">
                            <input type="text" autocomplete="off" name="startDate" id="startDate"
                                   value="<fmt:formatDate value="${campaign.startDate}" pattern="MM/dd/yyyy"/>"/>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><form:label path="endDate">End Date:</form:label></td>
                    <td>
                        <spring:bind path="endDate">
                            <input type="text" autocomplete="off" id="endDate" name="endDate"
                                   value="<fmt:formatDate value="${campaign.endDate}" pattern="MM/dd/yyyy"/>"/>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><form:label path="enabled">Enabled:</form:label></td>
                    <td><form:checkbox path="enabled"/></td>
                </tr>
                <tr>
                    <td>Items</td>
                    <td>
                        <input type="text" id="search_item" autocomplete="off">
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <div id="selected_items" style="display:none;">
                            <ul id="items_list"></ul>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <form:hidden path="itemReviews" id="itemReviews"/>
            <form:hidden path="items" id="campaignItems"/>
            <form:hidden path="retailerSite.id"/>
            <input type="submit" value="Save"/>
            <a href="<c:url value='/admin/retailers/list.do' />">Cancel</a>
        </form:form>
    </c:otherwise>
</c:choose>
