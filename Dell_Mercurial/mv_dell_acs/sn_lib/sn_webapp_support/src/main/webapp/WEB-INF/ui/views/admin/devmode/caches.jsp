<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<html>
<head>
    <title>System Caches (DevMode)</title>
    <%@include file="devmode-header.jsp" %>
</head>
<body>
<%@include file="devmode-menus.jsp" %>
<div class="caches">
    <c:if test="${fn:length(caches)>0}">
        <div><b>CACHES</b></div>
        <form action="<c:url value="/admin/devmode/caches.do" />" method="POST">
            <div>
                <table border="1" id="caches">
                    <thead>
                    <tr>
                        <td></td>
                        <td>Name</td>
                        <td>effective(%)</td>
                        <td>Hits</td>
                        <td>Misses</td>
                        <td>Size</td>
                        <td>memoryStore Size</td>
                        <td>offHeapStore Size</td>
                        <td>diskStore Size</td>

                        <td>Avg Get Time</td>
                        <td>Searches per Second</td>
                        <td>Avg Search Time</td>
                        <td>eviction Count</td>
                        <td>writer Queue</td>


                        <td>onDisk Hits</td>
                        <td>onDisk Misses</td>
                        <td>offHeap Hits</td>
                        <td>offHeap Misses</td>
                        <td>inMemory Hits</td>
                        <td>inMemory Misses</td>
                    </tr>
                    </thead>
                    <c:forEach var="entry" items="${caches}">
                        <tr>
                            <td><input type="checkbox" name="selectedCaches" value="${entry.key}"/></td>
                            <td>${entry.value['cacheName']}</td>
                            <td>${entry.value['effectivePercentage']}%</td>
                            <td>${entry.value['cacheHits']}</td>
                            <td style="<c:if test="${entry.value['cacheMisses']>entry.value['cacheHits']}">background: #FFAAAA</c:if>">
                                    ${entry.value['cacheMisses']}</td>

                            <td>${entry.value['size']}</td>
                            <td>${entry.value['memoryStoreSize']}</td>
                            <td>${entry.value['offHeapStoreSize']}</td>
                            <td>${entry.value['diskStoreSize']}</td>

                            <td>${entry.value['averageGetTime']}</td>
                            <td>${entry.value['searchesPerSecond']}</td>
                            <td>${entry.value['averageSearchTime']}</td>
                            <td>${entry.value['evictionCount']}</td>
                            <td>${entry.value['writerQueueLength']}</td>


                            <td>${entry.value['onDiskHits']}</td>
                            <td style="<c:if test="${entry.value['onDiskMisses']>entry.value['onDiskHits']}">background: #FFAAAA</c:if>">
                                    ${entry.value['onDiskMisses']}</td>

                            <td>${entry.value['offHeapHits']}</td>
                            <td style="<c:if test="${entry.value['offHeapMisses']>entry.value['offHeapHits']}">background: #FFAAAA</c:if>">
                                    ${entry.value['offHeapMisses']}</td>

                            <td>${entry.value['inMemoryHits']}</td>
                            <td style="<c:if test="${entry.value['inMemoryMisses']>entry.value['inMemoryHits']}">background: #FFAAAA</c:if>">
                                    ${entry.value['inMemoryMisses']}</td>
                        </tr>
                    </c:forEach>
                </table>

            </div>
            <div class="actions">
                <input type="submit" name="action" value="clear selected caches"/>
            </div>
        </form>
    </c:if>


</div>


<script type="text/javascript">

    $(document).ready(function () {
        if ($('#caches').length > 0) {
            $('#caches').dataTable({
                "bJQueryUI":true,
                "bPaginate":true,
                "sScrollX":"100%",
                "iDisplayLength":15,
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