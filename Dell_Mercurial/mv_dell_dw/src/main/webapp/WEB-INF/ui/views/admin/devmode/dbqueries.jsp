<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<html>
<head>
    <title>DB Queries Shell (DevMode)</title>
    <%@include file="devmode-header.jsp" %>
    <style type="text/css">
        #query {
            font-family: monospace;
            font-size: 14px;
        }

        .box {
            margin: 0px;
            padding: 0px;
        }

        .box .content {
            border: 1px solid #999;
            background: #DDDDDD;
        }

        .box .title {
            border: 1px solid #CCC;
            font-family: Verdana, Arial, sans-serif;
            background: #F1F1F1;
            font-size: 12px;
            padding: 5px;
            font-weight: bold;
        }

        .cell {
            vertical-align: top;
            max-height: 100px;
        }

        td {
            padding: 0;
            margin: 0;
        }

        <c:forEach var="columnDefinition" items="${dbColumnDefinitions}">
        .columnStyle_ ${columnDefinition.columnName} {
            width: ${columnDefinition.columnDisplaySize}px;
        <c:choose> <c:when test="${columnDefinition.columnDisplaySize>170}"> height: 130px;
            overflow-y: auto;
            word-wrap: break-word;
        </c:when> <c:otherwise> text-align: center;
        </c:otherwise> </c:choose>

        }

        </c:forEach>

    </style>
</head>
<body>
<%@include file="devmode-menus.jsp" %>

<div id="db-query-editor">
    <div>Note : only Select queries are allowed.<br/>LIMIT ${queryLimit} is suffixed to all queries.</div>
    <div style="color:red;">${errorMessage}</div>
    <form action="<c:url value='/admin/devmode/dbqueries.do'/>" method="post">
        <textarea id="query" name="query" cols="100" rows="3" style="width:100%;">${query}</textarea>
        <br/><br/>
        <button type="submit"> - Execute script -</button>
    </form>
</div>


<c:if test="${not empty dbColumnDefinitions}">
    <div id="shell-result" class="box">
        <div class="title">Result</div>
        <div class="content" style="width:100%; overflow-x: auto;height:500px">
            <table id="resultTable" border="1">
                <thead style="background: #333;color:#FFF">
                <tr>
                    <c:forEach var="columnDefinition" items="${dbColumnDefinitions}">
                        <td>
                            <div>${columnDefinition.columnName}</div>
                        </td>
                    </c:forEach>
                </tr>
                </thead>
                <c:forEach var="row" items="${result}">
                    <tr>
                        <c:forEach var="rowValue" items="${row}" varStatus="status">
                            <td class="cell">
                                <div class="columnStyle_${dbColumnDefinitions[status.index].columnName}">
                                    <c:choose>
                                        <c:when test="${dbColumnDefinitions[status.index].columnClassName == 'java.lang.Boolean'}">
                                            <c:choose>
                                                <c:when test="${rowValue}">true</c:when>
                                                <c:otherwise>false</c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            ${rowValue}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</c:if>
<script type="text/javascript">

    var dataTableConfig = {
        "bJQueryUI":true,
//        "bAutoWidth":false,
        "sPaginationType":"full_numbers",
//        "bScrollInfinite": true,
//        "bScrollCollapse": false,
        "sScrollX":"100%",
//        "sScrollXInner": "100%",
//        "aoColumns": [],
        "sScrollY":"370px"
    };
    dataTableConfig.totalColumnSize = ${totalColumnSize};
    <c:forEach var="columnDefinition" items="${dbColumnDefinitions}">
    <%--dataTableConfig.aoColumns.push({ "sWidth": "${columnDefinition.columnDisplaySize}px" });--%>
    </c:forEach>


    $(document).ready(function () {
        oTable = $('#resultTable').dataTable(dataTableConfig);
    });
</script>
</body>
</html>