<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="Cache-Control" content="no-cache" forua="true"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>User</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <%@include file="/WEB-INF/ui/views/admin/devmode/devmode-header.jsp" %>

    <style type="text/css">
        .status-0 {
            background-color: #ffff00;
        }

        .status-1 {
            background-color: #00ccff;
        }

        .status-2, .status-8 {
            background-color: #adff2f;
        }

        .status-3, .status-4, .status-5, .status-9, .status-10 {
            background-color: #ff6666;
        }
    </style>

    <script type="text/javascript">
        //        var objvalue;

        $(document).ready(function () {
            $('#sh_container').dataTable({
                sEcho:"1",
                bJQueryUI:true,
                bPaginate:true,
                sScrollX:"100%",
                iDisplayLength:10,
                bScrollCollapse:true,
                sSearch:["requestURL", "apiKey", "IPAddress"],
                sPaginationType:"full_numbers",
                aaSorting:[
                    [ 3, "desc" ]
                ],
//                Pagination related changes
                iDisplayStart:0,
                bProcessing:true,
                bServerSide:true,
                sAjaxSource:"/admin/monitoring/metrics/userApiMetrics.json"
//                "fnServerParams":function (aoData) {
//                    aoData.push({ "name":"apiKeyID", "value":$('#apiKeyChosen').val() }, {
//                        "name":"userID", "value":$('#userID').val()
//                    });
//                }
                //sh_container_first
                //sh_container_previous
                //sh_container_next
                //sh_container_last

            });
            var apiMetricsDataTable = $('#sh_container').dataTable();
            $('.dataTables_filter input')
                    .unbind('keypress keyup')
                    .bind('keypress keyup', function (e) {
                        if ($(this).val().length < 5 && e.keyCode != 13) return;
                        apiMetricsDataTable.fnFilter($(this).val());
                    });
        });


        //        function submitForm(obj) {
        //            var hiddenValue = obj.value;
        //            document.getElementById('apiKeyID').value = hiddenValue;
        //            document.forms["userMetricsForm"].submit();
        //        }

    </script>


</head>
<body>

${navCrumbs}

<div id="tabs" class="ui-tabs">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-corner-all">
        <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a
                href="<c:url value="/admin/monitoring/metrics/apiMetrics.do"/>">User Specific </a></li>
    </ul>
    <div style="padding-top: 10px;">
        <span style="float: left;padding-bottom: 10px;">
            Number of API Requests hit - <b>${totalRecords}</b>
        </span>
        <span style="float: right;padding-bottom: 10px;">
            Minimum String Size to Search is 5
        </span>
    </div>

</div>


<div class="sh_container">
    <%--<form:form id="userMetricsForm" method="POST" action="userApiMetrics.do">--%>
    <%--<table>--%>
    <%--<tr>--%>
    <%--<td>--%>
    <%--Filter by API Key--%>
    <%--<select id="apiKeyChosen" name="apiKeyChosen" onchange="submitForm(this) ">--%>
    <%--<option value="">Select</option>--%>
    <%--<option value="-1">All</option>--%>
    <%--<c:forEach var="entry" items="${authKeys}">--%>
    <%--<option value="${entry.id}" <c:if test="${entry.id == apiKeyID}">selected="yes"</c:if>>--%>
    <%--${entry.accessKey}</option>--%>
    <%--</c:forEach>--%>
    <%--</select>--%>
    <%--</td>--%>
    <%--<td>--%>
    <%--<input type="hidden" id="apiKeyID" name="apiKeyID"/>--%>
    <%--<input type="hidden" id="userID" name="userID" value="${userID}"/>--%>
    <%--</td>--%>

    <%--</tr>--%>
    <%--</table>--%>
    <%--</form:form>--%>
    <table border="1" id="sh_container">
        <thead>

        <tr>
            <th>Request URL</th>
            <th>API Key</th>
            <th>IP Address</th>
            <th>Time Accessed</th>
        </tr>
        </thead>
        <tbody>

        </tbody>

    </table>

</div>

</body>
</html>