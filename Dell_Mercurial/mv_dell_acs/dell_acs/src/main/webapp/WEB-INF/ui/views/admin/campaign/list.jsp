<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>

<script type="text/javascript">
    function deleteCampaign(campaignID){
        var cnf = confirm('Are you sure you want to delete the campaign ?');
        if(cnf){
            $.ajax({
                url: "delete.json",
                dataType:"json",
                data: {id: campaignID},
                type: 'POST',
                success:function (response, status) {
                    if (response.success) {
                        window.location.reload();
                    }
                },
                error:function (e) {
                    console.log('Unable to process the request', e);
                }
            });
        }
    }
</script>

${navCrumbs}

<c:choose>
    <c:when test="${empty campaigns}">
        <c:choose>
            <c:when test="${empty retailerSite}">
                You can add campaign for a Retailer site ONLY. Click
                <a href="<c:url value='/admin/retailers/list.do'/>">here</a> to choose.
                <a href="<c:url value='/admin/retailers/list.do' />">Cancel</a>
            </c:when>
            <c:otherwise>
                Currently site has no campaigns. Please click on 'Add Campaign' button to add.
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
            <table class="datatable">
                <thead>
	                <tr>
	                    <th style="text-align: left;">ID</th>
	                    <th style="text-align: left;">Name</th>
	                    <th style="text-align: left;">Site Name</th>
	                    <th style="text-align: center;">Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${campaigns}" var="row">
                    <tr>
                        <td>${row.id}</td>
                        <td>
                            <c:if test="${not empty row.properties}">
                                <c:out value="${param.number}"/>
                                <c:choose>
                                    <c:when test="${row.properties['dell.campaign.items.downloaded']}">
                                            <span class="downloaded"
                                                  title="Tracker links are downloaded. Uploading of tracker links is pending"></span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${row.properties['dell.campaign.items.uploaded']}">
                                                    <span class="uploaded"
                                                          title="Tracker links are updated for the campaign"></span>
                                            </c:when>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            ${row.name}
                        </td>
                        <td>
                            <c:if test="${row.retailerSite != null}">
	                            ${row.retailerSite.siteName}
                            </c:if>
                        </td>
                        <td  width="8%" align="center">
                            <a href="<c:url value='edit.do?id=${row.id}'/>"><span class="edit action_link"></span></a>
                            <a onclick="deleteCampaign(${row.id});" href="#"><span class="remove action_link"></span></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

    </c:otherwise>
</c:choose>
<div>
    <a href="<c:url value='/admin/campaign/add.do?siteID='/>${retailerSite.id}" class="gbutton">ADD CAMPAIGN</a>
</div>