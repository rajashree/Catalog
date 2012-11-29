<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%--
  ~ Copyright (c) Sourcen Inc. 2004-2012
  ~ All rights reserved.
  --%>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Internal System Error Error</title>
    <style type="text/css">
        body {
            font-family: Verdana, Arial, sans-serif;
            font-size: 12px;
        }

        ul {
            line-height: 20px;
        }

        .stacktrace {
            padding-top: 10px;
            font-family: "Courier New", monospace;
            font-size: 14px;
            line-height: 18px;
            display: block;
            list-style: decimal;
            border: 1px solid #CCC;
            background-color: #F1F1F1;
        }

        .oddRow {
            background-color: #FFFFFF;
        }

        .evenRow {
            background-color: #F1F3FF;
        }
    </style>
</head>
<body>
<h3>An System Error occured</h3>

<p>We are sorry for any inconvinence caused. A serious error occurred in our system and has been logged.</p>
<%

    String requestUri = StringEscapeUtils.escapeHtml(request.getAttribute("javax.servlet.error.request_uri") != null ? request.getAttribute("javax.servlet.error.request_uri").toString() : "");
    String statusCode = StringEscapeUtils.escapeHtml(request.getAttribute("javax.servlet.error.status_code") != null ? request.getAttribute("javax.servlet.error.status_code").toString() : "");
    String errorMessage = StringEscapeUtils.escapeHtml(request.getAttribute("javax.servlet.error.message") != null ? request.getAttribute("javax.servlet.error.message").toString() : "");
    String exceptionType = StringEscapeUtils.escapeHtml(request.getAttribute("javax.servlet.error.exception_type") != null ? request.getAttribute("javax.servlet.error.exception_type").toString() : "");

    Throwable errorException = (request.getAttribute("javax.servlet.error.exception") != null) ? ((Throwable) request.getAttribute("javax.servlet.error.exception")) : null;
    String errorExceptionString = (errorException != null) ? errorException.getMessage() : "";

    Boolean stackTracesEnabled = System.getProperty("ee.isStackTracesEnabled").equalsIgnoreCase("true");

    if (stackTracesEnabled) {
%>
<ul>
    <li>Error Code: <strong><%=statusCode%>
    </strong></li>
    <li>Exception Type: <strong><%=exceptionType%>
    </strong></li>
    <li>Error Message: <strong><%=errorMessage%><%=errorExceptionString%>
    </strong></li>
    <li>Request URI: <strong><%=requestUri%>
    </strong></li>
    <% if (exception != null) { %>
    <li>
        <div style="padding-top: 20px;"><strong>Stack Trace:</strong></div>
        <ul class="stacktrace">
            <li class="oddRow"><strong><%=exceptionType%> : <%=errorExceptionString%>
            </strong></li>
            <%
                if (errorException != null) {
                    StackTraceElement[] elements = exception.getStackTrace();
                    if (elements != null) {
                        for (int i = 0; i < elements.length; i++) { %>
            <li class="<%=(i % 2 == 0) ? "evenRow" : "oddRow"%>">
                <% if (i == 0) { %><strong><% } %>
                <%=StringEscapeUtils.escapeHtml(elements[i] != null ? elements[i].toString() : "") %>
                <% if (i == 0) { %></strong> <% } %>
            </li>
            <% } %>
            <% }
            }%>
        </ul>
    </li>
    <% }
    %>
</ul>
<% } // END - if (Configurator.isDevMode() || stackTracesEnabled) { %>
</body>
</html>