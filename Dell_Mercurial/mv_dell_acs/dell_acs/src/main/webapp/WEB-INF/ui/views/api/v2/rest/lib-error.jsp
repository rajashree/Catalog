<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% response.setStatus(HttpServletResponse.SC_NOT_FOUND); %>
<html>
    <head>
        <title>Error: Library not found</title>
    </head>
    <body>
        The requested library was not found.<br/><br/>
        The only allowed formats are javascript, json, js, php.
    </body>
</html>