<%@ include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.Calendar" %>
<%--
  ~ Copyright (c) Sourcen Inc. 2004-2012
  ~ All rights reserved.
  --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title><spring:message code="application.name"/> : Admin Console : <decorator:title/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon"/>
    <link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon"/>

    <jsp:include page="template-header-scripts.jsp"/>
    <jsp:include page="template-header-css.jsp"/>
    <decorator:head/>

</head>

<body>
<div id="page">
    <div id="mainmenu" style="padding:5px 15px 0px 0px;display: block;">
        <span class="logout_btn"><a href="<c:url value="/logout.do"/>">Logout</a></span>
    </div>
    <div class="logo"><img src="<c:url value='/images/logo.png'/>"/></div>
    <%--<h3 style="padding-left: 10px;"><%= ConfigurationServiceImpl.getInstance().getProperty("application.admin.console.title", "Dell Datawarehouse Admin Console")%></h3>--%>

    <div id="wrapper">
        <decorator:body/>
    </div>
</div>
<div id="footer"> Copyright &copy; <%=(Calendar.getInstance().get(Calendar.YEAR))%></div>
<script>

    $(document).ready(function() {
        $('body').append('<div id="preloader"><img src="<c:url value="/images/preloader.gif"/>"/><div id="preloader-mask"></div></div>');
    });

</script>
<jsp:include page="template-footer-scripts.jsp"/>
</body>
</html>
