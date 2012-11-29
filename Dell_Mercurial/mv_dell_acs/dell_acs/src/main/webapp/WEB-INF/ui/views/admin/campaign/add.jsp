<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

${navCrumbs}

<c:choose>
    <c:when test="${ empty campaign.retailerSite}">
        You can add campaign's for a Retailer site ONLY. <br>Click
        <a href="<c:url value='/admin/retailers/list.do'/>">here</a> to choose a retailer site.
    </c:when>
    <c:otherwise>
        <fieldset style="padding: 10px;">
            <legend><strong>Create Campaign</strong></legend>
                <%-- http://jqueryui.com/demos/autocomplete/#multiple-remote --%>
            <form:form modelAttribute="campaign" method="POST" action="add.do" enctype="multipart/form-data">
                <span id="error_message"></span>
                <table>
                    <tbody>
                    <tr>
                        <td><form:label path="name">Name:</form:label></td>
                        <td><form:input maxlength="255" autocomplete="false" path="name"
                                        onblur="CIAutoComplete.validateFormFields();"/>
                            <span id="name_error" class="error_msg"></span></td>
                    </tr>

                    <tr>
                        <td>Thumbnail:</td>
                        <td>
                            <input type="file" name="thumbnailFile" id="thumbnailFile" accept="image/*">
                        </td>
                    </tr>
                    <!-- Disabled as per : EXTERNALINTERACTIVEADS-7 -->
                    <%--<tr>--%>
                        <%--<td><form:label path="type">Type :</form:label></td>--%>
                        <%--<td>--%>
                            <%--<form:select path="type" valueProperty="label">--%>
                                <%--<form:option value="" label="Select Type"/>--%>
                                <%--<form:options labelProperty="description"/>--%>
                            <%--</form:select>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td><form:label path="startDate">Start Date:</form:label></td>
                        <td>
                            <spring:bind path="startDate">
                                <input type="text" readonly="true" name="startDate" id="startDate"
                                       value="<fmt:formatDate value="${campaign.startDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                            </spring:bind>
                            <span id="start_date_error" class="error_msg"></span>
                        </td>
                    </tr>
                    <tr>
                        <td><form:label path="endDate">End Date:</form:label></td>
                        <td>
                            <spring:bind path="endDate">
                                <input type="text" readonly="true" id="endDate" name="endDate"
                                       value="<fmt:formatDate value="${campaign.endDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                            </spring:bind>
                            <span id="end_date_error" class="error_msg"></span>
                        </td>
                    </tr>
                    <tr>
                        <td><form:label path="enabled">Enabled:</form:label></td>
                        <td><form:checkbox path="enabled"/></td>
                    </tr>
                    <!-- Disabled as per : EXTERNALINTERACTIVEADS-7 -->
                    <%--<tr>--%>
                        <%--<td><form:label path="packageType">Package type campaign</form:label></td>--%>
                        <%--<td><form:checkbox path="packageType"/></td>--%>
                    <%--</tr>--%>
                    </tbody>
                </table>
                <form:hidden path="retailerSite.id"/>
                <input type="submit" value="Save"/>
                <a class="gbutton" href="<c:url value='/admin/campaign/list.do?siteID='/>${campaign.retailerSite.id}">Cancel</a>
            </form:form>
        </fieldset>
    </c:otherwise>
</c:choose>
