<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Retailers edit page</title>
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
                } else if(tab.index == 1) {
                    $(location).attr('href',basePath+"admin/retailers/list.do");
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
                <c:choose>
                    <c:when test="${ empty retailer}">
                        RETAILER NOT FOUND.
                    </c:when>
                    <c:otherwise>

                        <form:form modelAttribute="retailer" method="POST" action="edit.do">
                            <table>
                                <tr>
                                    <td><form:label path="name">Name:</form:label></td>
                                    <td>${retailer.name}</td>
                                </tr>
                                <tr>
                                    <td><form:label path="description">Description:</form:label></td>
                                    <td>${retailer.description}</td>
                                </tr>
                                <tr>
                                    <td><form:label path="retailerId">Retailer ID:</form:label></td>
                                    <td>${retailer.retailerId}</td>
                                </tr>
                                <tr>
                                    <td><form:label path="emailAddress">Email:</form:label></td>
                                    <td><form:input path="emailAddress"/></td>
                                </tr>
                            </table>
                            <form:hidden path="id"/>
                            <div class="retailaction_btn">
                            <input type="submit" value="Save"/>
                            <a  href="javascript: history.back();">Cancel</a></div>

                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>
            <div id="gaAccountsTab"></div>
            <div id="dataImportMonitor"></div>
            <div id="systemMonitoring"></div>
        </div>
</body>
</html>