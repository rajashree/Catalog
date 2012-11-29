<%@include file="../../views/includes/default.jsp" %>
<c:choose>
    <c:when test="${param.entity != null && param.edit == null}">
        VIEW MODE ::: Display tags related to entity
    </c:when>
    <c:when test="${param.edit && param.entity != null}">
        EDIT MODE :::
        Display tags related to entity for ${param.entity} and also display the auto complete box to choose the tags ${param.edit}
    </c:when>
</c:choose>



<%--
    <!-- View Mode -->
    <jsp:include page="../../includes/tags.jsp">
        <jsp:param name="entity" value="DOCUMENT"/>
    </jsp:include>

    <!-- Edit Mode -->
    <jsp:include page="../../includes/tags.jsp">
        <jsp:param name="edit" value="true"/>
        <jsp:param name="entity" value="DOCUMENT"/>
    </jsp:include>
--%>