<%@ page import="com.sourcen.core.util.WebUtils" %>
<style type="text/css">
    .menu {
        border-bottom: 1px solid #666;
        margin-bottom: 15px;
        padding: 5px;
    }

    .menu a, .menu a:visited, .menu a:hover, .menu a:active {
        color: #333;
        text-decoration: none;
    }

    .menu span.selected a {
        color: #FFF;
    }

    .menu span {
        padding: 5px 10px;
        margin: 0px 5px;
        background: #CCC;
    }

    .menu span.selected {
        background: #333366;
        color: #FFF;
    }
</style>
<div class="menu">
    <span class="<%=WebUtils.isPathActive(request,"/admin/devmode/system-info.do","selected")%>">
        <a href="<c:url value="/admin/devmode/system-info.do"/>">System Info</a>
    </span>
    <span class="<%=WebUtils.isPathActive(request,"/admin/devmode/logs.do","selected")%>">
        <a href="<c:url value="/admin/devmode/logs.do"/>">Logs</a>
    </span>
    <span class="<%=WebUtils.isPathActive(request,"/admin/devmode/config-properties.do","selected")%>">
        <a href="<c:url value="/admin/devmode/config-properties.do"/>">Config Properties</a>
    </span>
    <span class="<%=WebUtils.isPathActive(request,"/admin/devmode/caches.do","selected")%>">
        <a href="<c:url value="/admin/devmode/caches.do"/>">Caches</a>
    </span>
    <span class="<%=WebUtils.isPathActive(request,"/admin/devmode/dbqueries.do","selected")%>">
        <a href="<c:url value="/admin/devmode/dbqueries.do"/>">Db Queries</a>
    </span>
    <span class="<%=WebUtils.isPathActive(request,"/admin/devmode/groovy-shell.do","selected")%>">
        <a href="<c:url value="/admin/devmode/groovy-shell.do"/>">Groovy Shell</a>
    </span>
</div>
