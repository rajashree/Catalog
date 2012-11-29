<%@include file="../../includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Retailers list page</title>
</head>
<body>

${navCrumbs}

<c:choose>
    <c:when test="${ empty retailer}">
        RETAILER NOT FOUND.
    </c:when>
    <c:otherwise>

        <form:form modelAttribute="retailer" method="POST" action="edit.do">
            <table>
                <tr>
                    <td><form:label path="name">Name:</form:label></td>
                    <td><form:input path="name"/></td>
                </tr>
                <tr>
                    <td><form:label path="description">Description:</form:label></td>
                    <td><form:textarea path="description"/></td>
                </tr>
                <tr>
                    <td>Created Date:</td>
                    <td>${retailer.createdDate}</td>
                </tr>
                <tr>
                    <td>Last Modified Date:</td>
                    <td>${retailer.modifiedDate}</td>
                </tr>
                <tr>
                    <td><form:label path="active">Active:</form:label></td>
                    <td><form:checkbox path="active"/></td>
                </tr>
            </table>

            <form:hidden path="id"/>
            <input type="submit" value="Save"/>
            <a class="gbutton" href="<c:url value='/admin/retailers/list.do'/>">Cancel</a>
        </form:form>
    </c:otherwise>
</c:choose>
</body>
</html>