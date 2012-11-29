<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Retailers list page</title>
</head>
<body>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>

${navCrumbs}

<form:form modelAttribute="retailer" method="POST" action="add.do">
    <table>
        <tr>
            <td><form:label path="name">Name:</form:label></td>
            <td><form:input path="name"/>
                <form:errors path="name" cssClass="error"/>
            </td>
        </tr>
        <tr>
            <td><form:label path="description">Description:</form:label></td>
            <td><form:textarea path="description"/>
                <form:errors path="description" cssClass="error"/>
            </td>
        </tr>
        <tr>
            <td><form:label path="active">Active:</form:label></td>
            <td><form:checkbox path="active"/></td>
        </tr>
    </table>

    <input type="submit" value="Save"/>
    <a class="gbutton" href="<c:url value='/admin/retailers/list.do'/>">Cancel</a>
</form:form>

</body>
</html>