<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@include file="../../includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Retailer's list page</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <style type="text/css">
        .ui-widget-header {
            border: none !important;
            background: none !important;
        }

        .ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited {
            color: rgb(28, 148, 196);
            text-decoration: none;
        }

        .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default {
            background: rgb(209, 206, 206);
            font-weight: bold;
            color: rgb(28, 148, 196);

        }

        .ui-state-hover, .ui-widget-content .ui-state-hover, .ui-widget-header .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus, .ui-widget-header .ui-state-focus {
            background: rgb(209, 206, 206);
            font-weight: bold;
            color: rgb(28, 148, 196);
        }

        /* overrides for ui-tab styles */
        #widget-docs ul.ui-tabs-nav { padding:0 0 0 8px; }
        #widget-docs .ui-tabs-nav li { margin:5px 5px 0 0; }

        #widget-docs .ui-tabs-nav li a:link,
        #widget-docs .ui-tabs-nav li a:visited,
        #widget-docs .ui-tabs-nav li a:hover,
        #widget-docs .ui-tabs-nav li a:active { font-size:14px; padding:4px 1.2em 3px; color:#fff; }

        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:link,
        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:visited,
        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:hover,
        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:active { color:#e6820E; }

        #widget-docs .ui-tabs-panel { padding:20px 9px; font-size:12px; line-height:1.4; color:#000; }

        #widget-docs .ui-widget-content a:link,
        #widget-docs .ui-widget-content a:visited { color:#1b75bb; text-decoration:none; }
        #widget-docs .ui-widget-content a:hover,
        #widget-docs .ui-widget-content a:active { color: rgba(240, 240, 240, 0.52); }
    </style>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.core.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.widget.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.tabs.js"/>"></script>
    <script type="text/javascript">
        $(function() {
            $("#tabs").tabs();
        });
    </script>
</head>
<body>

	${navCrumbs}
	<div id="tabs">
	    <ul>
	        <li><a href="#active">Active</a></li>
	        <li><a href="#inactive">Inactive</a></li>
	    </ul>
	    
	    <div id="active" style="border-top: 1px solid rgb(204, 204, 204);">
			<table class="datatable">
			    <thead>
			    <tr>
			        <th align="left">ID</th>
			        <th align="left">Name</th>
			        <th align="left">Description</th>
			        <th align="left">Site URL</th>
			        <th align="center" colspan="2">Action</th>
			    </tr>
			    </thead>
			    <tbody>
			    <c:forEach items="${activeRetailers}" var="row">
			        <tr>
			            <td style="width: 5%;">${row.id}</td>
			            <td>${row.name}</td>
			            <td>${row.description}</td>
			            <td>${row.url}</td>
			            <td style="text-align: center;width: 15%;">
			                <a style="padding-right: 10px;"
			                   href="<c:url value="/admin/retailerSites/list.do?retailerId=${row.id}"/>">view sites</a>
			                <a href="<c:url value="/admin/retailers/edit.do?id=${row.id}"/>">edit</a>
			            </td>
			        </tr>
			    </c:forEach>
			    </tbody>
			</table>
			<a href="<c:url value="/admin/retailers/add.do"/>" class="gbutton">ADD RETAILER</a>
	    </div>
	 
		<div id="inactive" style="border-top: 1px solid rgb(204, 204, 204);">
			<table class="datatable">
			    <thead>
			    <tr>
			        <th align="left">ID</th>
			        <th align="left">Name</th>
			        <th align="left">Description</th>
			        <th align="left">Site URL</th>
			        <th align="center" colspan="2">Action</th>
			    </tr>
			    </thead>
			    <tbody>
			    <c:forEach items="${inactiveRetailers}" var="row">
			        <tr>
			            <td style="width: 5%;">${row.id}</td>
			            <td>${row.name}</td>
			            <td>${row.description}</td>
			            <td>${row.url}</td>
			            <td style="text-align: center;width: 15%;">
			                <a style="padding-right: 10px;"
			                   href="<c:url value="/admin/retailerSites/list.do?retailerId=${row.id}"/>">view sites</a>
			                <a href="<c:url value="/admin/retailers/edit.do?id=${row.id}"/>">edit</a>
			            </td>
			        </tr>
			    </c:forEach>
			    </tbody>
			</table>
		</div>
	</div>	
</body>
</html>