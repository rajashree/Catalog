<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%
    // check if this is a Javascript request.
    if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
        response.setContentType("text/json");
        response.getWriter().write("{\"status\":false, \"data\" : [], \"success\":false}");
    } else {
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>404 | Page not found</title>
    <style type="text/css">
        body {
            font-family: Verdana, Arial, sans-serif;
            font-size: 12px;
        }

        .page_announcement {
            font-family: Arial, sans-serif;
            font-size: 12px;
            font-weight: bold;
            color: #333;
            padding: 10px;
            width: 800px;
            margin: 20px auto;
            text-align: center;
            border: 1px solid #CCC;
            background: #EEE;
        }
    </style>
</head>

<body>
<div style="text-align: center;">
    <img alt="We are sorry, the page you requested cannot be found." src="<c:url value="/images/errors/404.jpg"/>"/>
</div>
<div class="page_announcement">
    We are sorry, the page you requested cannot be found.
</div>
<!-- NEED A LOT OF SPACE so that we can show our custom page instead of IE8, or Chrome's error pages. -->
<!--              -->
<!--              -->
<!--              -->
<!--              -->
<!--              -->
<!--              -->
<!--              -->
<!--              -->
<!--              -->
<!--              -->
</body>
</html>
<% } // end JSON check. %>