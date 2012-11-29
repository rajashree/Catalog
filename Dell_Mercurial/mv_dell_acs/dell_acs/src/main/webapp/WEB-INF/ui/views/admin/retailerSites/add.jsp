<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Retailers list page</title>
</head>
<body>

${navCrumbs}

<%if (WebUtils.getParameter(request, "duplicate", false)) {%>
<div style="color:#ff0000">Duplicate: Site
    "<%=WebUtils.getParameter(request, "siteName", "")%>" already exists
</div>
<% } %>

<%if (WebUtils.getParameter(request, "enumPixelTrackerMissing", false)) {%>
<div style="color:#ff0000">Please choose a Pixel Tracker.</div>
<% } %>

<%if (WebUtils.getParameter(request, "enumDataImportMissing", false)) {%>
<div style="color:#ff0000">Please choose a Data Import Type.</div>
<% } %>

<form:form modelAttribute="retailerSite" method="POST" action="add.do">
    <table>
        <tr>
            <td><form:label path="siteName">siteName<b style="color:red">*</b> :</form:label></td>
            <td>
                <form:input path="siteName"/>
                <span class="error_msg"><form:errors path="siteName"/></span>
            </td>
        </tr>
        <tr>
            <td><form:label path="siteUrl">siteUrl<b style="color:red">*</b> :</form:label></td>
            <td>
                <form:input path="siteUrl"/>
                <span class="error_msg"><form:errors path="siteUrl"/></span>
            </td>
        </tr>
        <tr>
            <td><form:label path="logoUri">logoUrl<b style="color:red">*</b> :</form:label></td>
            <td>
                <form:input path="logoUri"/>
                <span class="error_msg"><form:errors path="logoUri"/></span>
            </td>
        </tr>

        <!-- Type of PixelTracking -->
        <tr>
            <td><form:label path="enumPixelTracker">Pixel Tracker :</form:label></td>
            <td>
                <form:select path="enumPixelTracker" valueProperty="label">
                    <form:option value="" label="Select Type"/>
                    <form:options labelProperty="description"/>
                </form:select>
            </td>
        </tr>

        <!-- Type of DataImport-->
        <tr>
            <td><form:label path="enumDataImport">Data Import Type:</form:label></td>
            <td>
                <form:select path="enumDataImport" valueProperty="label">
                    <form:option value="" label="Select Type"/>
                    <form:options labelProperty="description"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td><form:label path="active">Active:</form:label></td>
            <td><form:checkbox path="active"/></td>
        </tr>


    </table>
    <form:hidden path="retailer.id"/>

    <input type="submit" value="Save"/>
    <a class="gbutton" href="<c:url value='/admin/retailerSites/list.do?retailerId='/>${retailerSite.retailer.id}">Cancel</a>
</form:form>

</body>
</html>