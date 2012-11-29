<%@include file="../../includes/default.jsp" %>
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
    function adjustWindow(ele) {
        var newX = ($(window).width() - $(ele).width()) / 2;
        var newY = ($(window).height() - $(ele).height()) / 2;
        $(ele.id).css("height", newY);
        $(ele.id).css("width", newX);
    }
    function hidePreview() {
        $('#preview').hide();
    }
</script>

<c:choose>
    <c:when test="${campaign != null}">

		${navCrumbs}

        <fieldset style="padding: 10px;">
            <legend><strong>Edit Campaign</strong></legend>
            <form:form modelAttribute="campaign" method="POST" enctype="multipart/form-data">
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
                                <spring:bind path="thumbnail">
                                    <c:choose>
                                        <c:when test="${campaign.properties['dell.campaign.thumbnail'] != null}">
                                            <!-- Hover effect for Thumbnail -->
                                            <div id="preview" onmouseover="adjustWindow(this)">
                                                <img alt="Thumbnail" id="thumbnailFileImage" height="50" width="50"
                                                     src="<c:url value="${campaign.properties['dell.campaign.thumbnail']}"/>"
                                                />
                                                <span id="img_reset_action" class="refresh action_link"
                                                      style="margin-left: 20px;width: 100px;padding-left: 16px;"
                                                      title="Click to reset the image"
                                                      onclick="javascript:CIUtils.resetThumbnailImage();"> ( Reset Image ) </span>
                                                <%--<input type="button" name="Reset" value="Reset Image">--%>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <img alt="Thumbnail" height="50" width="50"
                                                 src="<c:url value="/images/no_image.png"/>"/>
                                        </c:otherwise>
                                    </c:choose>
                                </spring:bind>
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
                                <!-- data time ref : http://www.tutorialspoint.com/jsp/jstl_format_formatdate_tag.htm -->
                                <spring:bind path="startDate">
                                    <input type="text" readonly="true" name="startDate" id="startDate"
                                        value='<fmt:formatDate  type="both"
                                            value="${campaign.startDate}" pattern="MM/dd/yyyy HH:mm"/>'/>
                                </spring:bind>
                                <span id="start_date_error" class="error_msg"></span>
                            </td>
                        </tr>

                        <tr>
                            <td><form:label path="endDate">End Date:</form:label></td>
                            <td>
                                <spring:bind path="endDate">
                                    <input type="text" readonly="true" name="endDate" id="endDate"
                                        value='<fmt:formatDate type="both"
                                        value="${campaign.endDate}" pattern="MM/dd/yyyy HH:mm"/>'/>
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
                        <%--<td><form:checkbox path="packageType" value="${packageType}"/></td>--%>
                        <%--</tr>--%>
                        <% String id = request.getParameter("id"); %>
                        <tr>
                            <td colspan="2">
                                <a href="<c:url value='/admin/campaign/manage-items.do?campaignID='/><%=id%>">
                                    Items
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a href="<c:url value='/admin/campaign/manage-categories.do?campaignID='/><%=id%>">
                                    Categories
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a href="<c:url value='/admin/tracking/manage-tracking.do?siteID='/>${campaign.retailerSite.id}&campaignID=<%=id%>">
                                    PixelTracking
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a href="<c:url value='/admin/entities/properties/manage-properties.do?type=campaign&id='/><%=id%>">
                                    Properties
                                </a>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div id="item-review-popup"
                     class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"
                     title="Choose reviews" style="display:none;">
                    <table id="reviews_list"></table>
                </div>
                <form:hidden path="retailerSite.id"/>
                <form:hidden path="version"/>
                <form:hidden path="id"/>
                <input type="hidden" name="resetThumbnail" id="resetThumbnail" value="false">
                <input type="button" value="Update" onclick="CIAutoComplete.setHiddenFieldValues('edit');">
                <%--<input type="submit" value="Update">--%>
                <%--<a class="gbutton"  href="<c:url value='/admin/campaign/list.do?siteID=' />${campaign.retailerSite.id}">Cancel</a>--%>
                <a class="gbutton" href="<c:url value='/admin/campaign/list.do?siteID='/>${campaign.retailerSite.id}">Cancel</a>
            </form:form>
        </fieldset>
    </c:when>
</c:choose>


