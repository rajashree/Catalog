<%@ page import="java.util.Calendar" %>
<%@ page import="com.sourcen.core.App" %>
<%@ page import="com.sourcen.core.util.collections.FileBackedPropertiesProvider" %>
<%@ page import="java.util.Date" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/ui/views/includes/default.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title><spring:message code="application.name"/> : <decorator:title/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon"/>
    <link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon"/>

    <jsp:include page="template-header-scripts.jsp"/>
    <jsp:include page="template-header-css.jsp"/>
    <decorator:head/>

</head>

<body>
<div id="logo" class="admin_page">
    <a href="<c:url value="/admin/index.do"/>"> <img src="<c:url value="/images/logo.png"/>" alt="Dell"/></a>
</div>
<div id="page">
    <div id="wrapper">
        <decorator:body/>
    </div>
</div>
<div id="footer">
    Copyright &copy; <%=(Calendar.getInstance().get(Calendar.YEAR))%>
    Version
    <span style="text-decoration: underline">
        <%=ConfigurationServiceImpl.getInstance().getProperty("app.version", "1.0")%>
    </span>

</div>
<jsp:include page="template-footer-scripts.jsp"/>
</body>
</html>
