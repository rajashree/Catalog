<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<html>
<head>
    <title>Configuration Properties (DevMode)</title>
    <%@include file="devmode-header.jsp" %>
    <style type="text/css">
        td div.key {
            width: 550px;
            word-break: normal;
            overflow-x: auto;
            vertical-align: top;
            /*text-align: right;*/
        }

        td div.val {
            width: 300px;
            word-wrap: break-word;
            word-break: normal;
            vertical-align: top;
        }
    </style>
</head>
<body>
<%@include file="devmode-menus.jsp" %>
<div style="background: #FFF">
    <c:forEach items="${configurations}" var="entry">
        <c:set var="editable" value="${entry.key[1]}"/>
        <div style="padding:25px 0 5px 0;font-weight:bold;font-size: 16px;">${entry.key[0]} (editable: ${editable})
        </div>
        <table border="1" id="${entry.key[3]}">
            <thead>
            <tr style="background: #666;color:#FFF">
                <td class="key">Name</td>
                <td class="val">Value</td>
                <td style="width: 100px;">Actions</td>
            </tr>
            </thead>
            <c:set var="entryValue" value="${entry.value}"/>
            <c:forEach items="${entryValue}" var="configItemEntry">
                <tr>
                    <td>
                        <div class="key">${configItemEntry.key}</div>
                    </td>
                    <td>
                        <div class="val">${configItemEntry.value}</div>
                    </td>
                    <td>
                            <%--<c:if test="${editable}">--%>
                            <%--<a href="#" onclick="inpu"--%>
                            <%--</c:if>--%>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </c:forEach>
</div>

<script type="text/javascript">

    $(document).ready(function () {
        <c:forEach items="${configurations}" var="entry">
        if ($('#${entry.key[3]}').length > 0) {
            $('#${entry.key[3]}').dataTable({
                "bJQueryUI":true,
                "bPaginate":true,
                "sScrollX":"100%",
                "iDisplayLength":10,
                "bScrollCollapse":true,
                "sPaginationType":"full_numbers",
                "aaSorting":[
                    [ 1, "asc" ]
                ]
            });
        }
        </c:forEach>
    });
</script>

</body>
</html>