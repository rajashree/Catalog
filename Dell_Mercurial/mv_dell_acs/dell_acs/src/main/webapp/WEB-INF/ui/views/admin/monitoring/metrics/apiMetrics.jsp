<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>API Key Statistics</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <%@include file="/WEB-INF/ui/views/admin/devmode/devmode-header.jsp" %>

    <script type="text/javascript">
        function setHiddenFieldValue(obj) {

            var hiddenValue = obj.value;
            document.getElementById('userID').value = hiddenValue;
        }

        function validateUserId(obj) {
            var userID = document.getElementById('userChosen').value;

            if (userID == null || userID == '' || userID == 0) {
                console.log("No user has been chosen");
                $('#invalid-site').show().fadeOut(2000, "linear");
                return false;
            }


        }
    </script>
    <style>
        #invalid-site {
            margin-bottom: 20px;
            color: red;
            font-weight: bold;
        }


    </style>

</head>
<body>

${navCrumbs}

<div id="tabs" class="ui-tabs">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-corner-all">
        <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a
                href="<c:url value="/admin/monitoring/metrics/apiMetrics.do"/>">API Metrics</a></li>
    </ul>
</div>

<fieldset>
    <legend><strong>Please choose a User</strong></legend>

    <div id="retailer">
        <div id="invalid-site" style="display:none">
            <span> Please select any User.</span>
        </div>

        <form:form method="POST" action="userApiMetrics.do">
            <table>
                <tr>
                    <td>
                        <select id="userChosen" onchange="setHiddenFieldValue(this)">
                            <option name="" value="0">Select User</option>
                            <c:forEach var="entry" items="${userList}">
                                <option name="" value="${entry.id}">${entry.username}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <input type="hidden" id="userID" name="userID">
                    </td>
                </tr>
            </table>
            <input type="submit" value="Submit" onclick="return validateUserId(this);"/>
            <a class="gbutton" href="javascript: history.back();">Cancel</a>
        </form:form>
    </div>
</fieldset>
</body>
</html>