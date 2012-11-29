<%@ page import="com.sourcen.core.config.ConfigurationServiceImpl" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Login</title>
</head>
<body>


<div id="login_form">
    <form action="<c:url value="/login-process.do"/>" method="post">
        <div class="form-item">
            <label for="name">Username</label>
            <input type="text" size="16" name="username" id="name" class="form-text"
                   value="<%=(ConfigurationServiceImpl.getInstance().isDevMode()) ? "admin" : "" %>"/>
        </div>
        <div class="form-item">
            <label for="password">Password</label>
            <input type="password" size="16" name="password" id="password" class="form-text"
                   value="<%=(ConfigurationServiceImpl.getInstance().isDevMode()) ? "admin" : "" %>"/>
        </div>
        <input type="submit" value="Submit"/>
    </form>
</div>


</body>
</html>