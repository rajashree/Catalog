<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<html>
<head>
    <title>System Info (DevMode)</title>
    <%@include file="devmode-header.jsp" %>
    <style type="text/css">
        #quartzThreads td span.clickable {
            cursor: pointer;
            text-decoration: underline;
        }
    </style>
</head>
<body>
<%@include file="devmode-menus.jsp" %>


<div>
    <h3>Memory Usage</h3>

    <div>
        <!-- heap -->
        <h3>Heap</h3>
        <table class="datatable">
            <thead>
            <th align="left">Committed Memory</th>
            <th align="left">Initial size of Memory</th>
            <th align="left">Maximum amount of Memory</th>
            <th align="left">Amount of used Memory</th>
            </thead>
            <tbody>
            <tr>
                <td>${heapUsage_committed}</td>
                <td>${heapUsage_init}</td>
                <td>${heapUsage_max}</td>
                <td>${heapUsage_used} </td>

            </tr>
            </tbody>
        </table>
        <!-- nonheap -->
        <h3>Non-Heap</h3>
        <table class="datatable">
            <thead>
            <th align="left">Committed Memory</th>
            <th align="left">Initial size of Memory</th>
            <th align="left">Maximum amount of Memory</th>
            <th align="left">Amount of used Memory</th>
            </thead>
            <tbody>
            <tr>
                <td> ${nonHeapUsage_committed}</td>
                <td>${nonHeapUsage_init}</td>
                <td>${nonHeapUsage_max}</td>
                <td>${nonHeapUsage_used} </td>

            </tr>
            </tbody>
        </table>
    </div>
</div>
<br/><br/><br/>

<div>
    <h3>Quartz Threads</h3>
    <table class="datatable" id="quartzThreads">
        <thead>
        <th align="left">Name</th>
        <th align="left">State</th>
        <th align="left">ThreadId</th>
        <th align="left">CPU Time(sec)</th>
        <th align="left">WaitedTime</th>
        <th align="left">WaitedCount</th>
        <th align="left">BlockedTime</th>
        <th align="left">BlockedCount</th>
        </thead>
        <tbody>
        <c:forEach items="${quartzThreads}" var="entry">
            <c:set var="threadInfo" value="${entry.value[0]}"/>
            <tr>
                <td><span class="clickable"
                          onclick="displayThreadStack(${threadInfo.threadId})">${threadInfo.threadName}</span></td>
                <td>${threadInfo.threadState}</td>
                <td>${threadInfo.threadId}</td>
                <td>${entry.value[1]}</td>
                <td>${threadInfo.waitedTime}</td>
                <td>${threadInfo.waitedCount}</td>
                <td>${threadInfo.blockedCount}</td>
                <td>${threadInfo.blockedTime}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
        <div>Thread Stack Info</div>
        <div id="currentThreadStack">

        </div>
    </div>
</div>


<script type="text/javascript">

    var threadStacks = [];
    <c:forEach items="${quartzThreads}" var="entry">
    threadStacks[${entry.key}] = ${entry.value[3]};
    </c:forEach>

    function displayThreadStack(threadId) {

        value = threadStacks[threadId];

        $('#currentThreadStack').html("");
        for (j = 0; j < value.length; j++) {
            var div = $('<div />').html(value[j]);
            $('#currentThreadStack').append(div);

        }
    }

    $(document).ready(function () {

        if ($('#quartzThreads').length > 0) {
            $('#quartzThreads').dataTable({
                "bJQueryUI":true,
                "bPaginate":true,
                "sScrollX":"100%",
                "iDisplayLength":10,
                "bScrollCollapse":true,
                "sPaginationType":"full_numbers",
                "aaSorting":[
                    [ 2, "desc" ]
                ]
            });
        }

    });
</script>

</body>
</html>