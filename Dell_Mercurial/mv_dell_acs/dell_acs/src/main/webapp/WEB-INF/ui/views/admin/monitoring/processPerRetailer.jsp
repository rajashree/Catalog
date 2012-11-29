<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="Cache-Control" content="no-cache" forua="true"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>Datafiles list page</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <%@include file="/WEB-INF/ui/views/admin/devmode/devmode-header.jsp" %>
    <script type="text/javascript">

        $(document).ready(function () {

                $('#sh_container').dataTable({
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

        });
    </script>
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
        .status-3, .status-4,.status-5,.status-9,.status-10
        {
            background-color: #ff6666;
        }
    </style>
</head>
<body>

	${navCrumbs}

    <div id="tabs" class="ui-tabs">
        <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-corner-all">
            <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="<c:url value="/admin/monitoring/dataImportProcesses.do"/>">Retailer Specific </a></li>
            <li class="ui-state-default ui-corner-top "><a href="<c:url value="/admin/monitoring/activeProcesses.do"/>">Active Processes</a></li>
        </ul>
    </div>

    <div class="sh_container">
	        <table border="1" id="sh_container">
	            <thead>
	            	<tr>
			            <th style="width: 450px;">Feed file path</th>	
			            <th style="width: 110px;">Status</th>
			            <th style="width: 70px;">Processed on</th>
			            <th style="width: 180px;">Import type</th>
	            	</tr>
	            </thead>
	            <c:forEach items="${dataFiles}" var="row">
	                <tr>
	                    <td style="width: 450px;">${row.filePath}</td>
	                    <td style="width: 110px;" class="status-${row.status}">
	                        <c:choose>
	                            <c:when test="${row.status == 0}">IN QUEUE</c:when>
	                            <c:otherwise>
	                                <c:choose>
	                                    <c:when test="${row.status == 1}">PROCESSING</c:when>
	                                    <c:otherwise>
	                                        <c:choose>
	                                            <c:when test="${row.status == 2 || row.status==8}">DONE</c:when>
	                                            <c:otherwise>
	                                                <c:choose>
	                                                    <c:when test="${row.status == 3}">ERROR READING</c:when>
	                                                    <c:otherwise>
	                                                        <c:choose>
	                                                            <c:when test="${row.status == 5}">ERROR PARSING</c:when>
	                                                            <c:otherwise>
	                                                                <c:choose>
	                                                                    <c:when test="${row.status == 9}">ERROR_RESIZING</c:when>
	                                                                    <c:otherwise>
	                                                                        <c:choose>
	                                                                            <c:when test="${row.status == 10}">FILE PROCESSING ERROR</c:when>
	                                                                            <c:otherwise>
	                                                                                <c:choose>
	                                                                                    <c:when test="${row.status == 6}">IMPORTED</c:when>
	                                                                                    <c:otherwise>
	                                                                                        <c:choose>
	                                                                                            <c:when test="${row.status == 7}">RESIZING</c:when>
	                                                                                            <c:otherwise>
	                                                                                                <c:choose>
	                                                                                                    <c:when test="${row.status == 4}">ERROR EXTRACTING</c:when>
	                                                                                                </c:choose>
	                                                                                            </c:otherwise>
	                                                                                        </c:choose>
	                                                                                    </c:otherwise>
	                                                                                </c:choose>
	                                                                            </c:otherwise>
	                                                                        </c:choose>
	                                                                    </c:otherwise>
	                                                                </c:choose>
	                                                            </c:otherwise>
	                                                        </c:choose>
	                                                    </c:otherwise>
	                                                </c:choose>
	                                            </c:otherwise>
	                                        </c:choose>
	                                    </c:otherwise>
	                                </c:choose>
	                            </c:otherwise>
	                        </c:choose>
	                    </td>
	                    <td><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium"
	                                        value="${row.modifiedDate}"/></td>
	                    <td>${importType}</td>
	                </tr>
	            </c:forEach>
	        </table>

        </div>
</body>
</html>