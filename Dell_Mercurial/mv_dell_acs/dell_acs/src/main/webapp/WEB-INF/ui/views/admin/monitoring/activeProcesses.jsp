<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <%@include file="/WEB-INF/ui/views/admin/devmode/devmode-header.jsp" %>
    <style type="text/css">
        .status-1 {
            background-color: #00ccff;
        }
        .status-7 {
            background-color: #9999ff;
        }
        .sh_container .sh_body {
                max-height: 550px !important;
        }
    </style>
</head>
<body>

	${navCrumbs}

    <div id="tabs" class="ui-tabs">
        <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-corner-all">
            <li class="ui-state-default ui-corner-top "><a href="<c:url value="/admin/monitoring/dataImportProcesses.do"/>">Retailer Specific </a></li>
            <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="<c:url value="/admin/monitoring/activeProcesses.do"/>">Active Processes</a></li>

        </ul>
    </div>



        <fieldset>
            <legend><strong>Currently running feeds</strong></legend>
                <div class="sh_container">
                    <table class="datatable sh_header">
                        <thead>
                        <th style="width: 450px;">Feed file path</th>
                        <th style="width: 110px;">Status</th>
                        <th>Started on</th>
                        </thead>
                    </table>
                    <div class="sh_body">
                        <table class="datatable" style="width:98%">
                            <tbody>
                            <c:forEach items="${dataFiles}" var="row">
                                    <c:set var="feed_status" value="${row.status}"></c:set>

                                    <tr>
                                        <td style="width: 450px;">${row.filePath}</td>
                                        <td style="width: 110px;" class="status-${feed_status}">
                                            <c:choose>
                                                <c:when test="${feed_status == 1}">PROCESSING</c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${feed_status == 7}">RESIZING</c:when>
                                                            </c:choose>
                                                        </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${row.modifiedDate}"/>
                                        </td>

                                    </tr>

                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
        </fieldset>
</body>
</html>