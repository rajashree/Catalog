<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Retailers list page</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
    <script type="text/javascript" language="javascript"
            src="<c:url value="/js/jquery.ui.tabs.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.tabs.js"/>"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#tabs").tabs({ selected: 1 });
            $("#tabs").bind("tabsselect",function(event, tab) {
                if(tab.tab.hash == '#gaAccountsTab' || tab.tab.hash == '#retailersTab') return;
                if(tab.index == 0) {
                    $(location).attr('href',basePath+"admin/googleanalytics/webProperties.do");
                } else if(tab.index == 2) {
                    $(location).attr('href',basePath+"admin/monitoring/dataImportMonitor.do");
                }  else if(tab.index == 3) {
                    $(location).attr('href',basePath+"admin/monitoring/systemMonitoring.do");
                }
            });

            $("#sub-tabs").tabs({ selected: 0 });
            $("#sub-tabs").bind("tabsselect",function(event, subTab) {
                if(subTab.index == 1) {
                    $(location).attr('href',basePath+"admin/googleanalytics/gaAccounts.do");
                }
            });
        });
    </script>
</head>
<body>
<div id="tabs">
    <ul>
        <li><a href="#webPropertiesTab">GA Web Properties</a></li>
        <li><a href="#yellowfinConfigTab">Yellowfin Configuration</a></li>
        <li><a href="#dataImportMonitor">Data Import Monitor</a></li>
        <li><a href="#systemMonitoring">System Monitoring</a></li>
    </ul>
    <div id="webPropertiesTab"></div>
    <div id="yellowfinConfigTab">
        <div id="sub-tabs">
            <ul>
                <li><a href="#retailersTab">Retailers</a></li>
                <li><a href="#gaAccountsTab">GA Accounts</a></li>
            </ul>
            <div id="retailersTab">
                <table class="datatable">
                    <thead>
                    <tr class="hdr_bg">
                        <th>ID</th>
                        <th>Retailer ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Email</th>
                        <th align="center" colspan="2">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${retailers}" var="row">
                        <tr>
                            <td style="width: 5%;">${row.id}</td>
                            <td>${row.retailerId}</td>
                            <td>${row.name}</td>
                            <td>${row.description}</td>
                            <td>${row.emailAddress}</td>
                            <td style="text-align: center;width: 15%;">
                              <a class='btnEdit' href='<c:url value="/admin/retailers/edit.do?id=${row.id}"/>'>edit</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div id="gaAccountsTab"></div>
        </div>

        <div id="dataImportMonitor"></div>
        <div id="systemMonitoring"></div>
    </div>
</body>
</html>