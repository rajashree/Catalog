<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>System Status</title>
</head>
<body>
<div>
    <table class="datatable">
        <tbody>
            <tr><td><strong>Server URL</strong></td><td><span>${serverUrl}</span></td></tr>
        </tbody>
    </table>
    <fieldset style="margin-top: 10px;margin-bottom: 10px;">
        <legend><strong>Database Information</strong></legend>
        <table class="datatable">
            <tbody>
                <tr><td>Connection </td> <td><span class="active_${dbStatus}">Enabled/Disabled</span></td></tr>
                <tr><td>Database Provider</td> <td>  <span> ${dbServer} </span></td></tr>
                <c:if test="${dbStatus}">
                    <tr><td>Schema</td><td><span> ${dbName} </span></td></tr>
                    <tr><td>Connection Url </td><td><span> ${dbUrl} </span></td></tr>
                    <%--<tr><td>Max Connection </td><td><span> ${maxConnections} </span></td></tr>--%>
                    <tr><td>Auto-Commit </td><td><span> ${dbAutoCommit} </span></td></tr>
                    <%--<tr><td>Server Up Time </td><td> <span> ${dbServerUPTime} </span></td></tr>--%>
                </c:if>
            </tbody>
        </table>
    </fieldset>

    <fieldset>
        <legend><strong>Jobs enabled/disabled information</strong></legend>
        <table class="datatable">
            <tbody>
            <c:forEach items="${jobs}" var="entry">
                <tr>
                    <td><span>${entry.key}</span></td>
                    <td><span class="active_${entry.value}">Enabled/Disabled</span></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </fieldset>
</div>

