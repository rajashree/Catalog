<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="java.util.Enumeration" %>
<%--
  ~ Copyright (c) Sourcen Inc. 2004-2012
  ~ All rights reserved.
  --%>

<script type="text/javascript">
    var basePath = '<c:url value="/"/>';
    var requestParams = {};
    <%Enumeration<String> paramNames = request.getParameterNames();
     while (paramNames.hasMoreElements()) {
        String paramName = paramNames.nextElement();%>requestParams['<%=paramName%>'] = '<%=request.getParameter(paramName)%>';
    <%}%>

    // simple handler for window.console.log
    try {
        window.console.log(new Date() + 'Initialized window.console.log');
    } catch (e) {
        try {
            console.log(new Date() + 'Initialized console.log');
            window.console = console;
        } catch (e2) {
            // we didn't find any loggers.
            window.console = {
                log:function () {
                    if (requestParams['alertConsole'] == 'true') {
                        var msg = "";
                        for (var i = 0; i < arguments.length; i++) {
                            msg += arguments[i];
                        }
                        alert(new Date() + " - " + msg);
                    }
                }
            };
            window.console.log('initialized custom logger');
        }
    }
    if (console == undefined) {
        console = window.console;
    }
</script>