<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><%= ConfigurationServiceImpl.getInstance().getProperty("application.name", "Dell DW")%></title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
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
                } else if(tab.index == 3) {
                    $(location).attr('href',basePath+"admin/monitoring/systemMonitoring.do");
                }
            });

            $("#sub-tabs").tabs({ selected: 1 });
            $("#sub-tabs").bind("tabsselect",function(event, subTab) {
                if(subTab.index == 0) {
                    $(location).attr('href',basePath+"admin/retailers/list.do");
                }
            });
        });
        function updateEmail() {
            var gaAccounts = [];
            $("#gaAccountsDataTable tr").each(function(){
                if (!this.rowIndex) return; // skip headers
                var gaAccount = {};
                gaAccount["accountId"] = $(this).find('#accountId').html();
                gaAccount["email"] = $(this).find('#email input').val();
                gaAccounts.push(gaAccount);
            });

            if(gaAccounts.length > 0) {
                $.ajax({
                    url:basePath+"admin/googleanalytics/updateAccounts.json",
                    type:"POST",
                    data:{gaAccounts:JSON.stringify(gaAccounts)},
                    dataType:'json',
                    success:function(data) {
                        alert("Accounts update successful");
                        location.reload()
                    },
                    error:function(){location.reload()}
                });
            }
        }
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
            <div id="retailersTab"></div>
            <div id="gaAccountsTab">
                <div>
                    <table id="gaAccountsDataTable" class="datatable">
                        <thead>
                            <tr class="hdr_bg">
                                <th align="left">Account ID</th>
                                <th align="left">Account Name</th>
                                <th align="left">Email</th>
                            </tr>
                        </thead>
                        <c:choose>
                            <c:when test="${fn:length(gaAccounts) > 0}">
                                <c:forEach var="gaAccount" varStatus="true" items="${gaAccounts}">
                                    <tr width="100%">
                                        <td id="accountId">${gaAccount.accountId}</td>
                                        <td id="accountName">${gaAccount.accountName}</td>
                                        <td id="email">
                                            <input type="text" value="${gaAccount.email}" style="width:200px"/>

                                        </td>
                                    </tr>
                                </c:forEach>

                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="3" align="center"><p>No data found</p></td></tr>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>
                <div id="action_buttons">
                    <input type="button" value="Update" onclick="updateEmail()"/>
                </div>
            </div>
        </div>
    </div>

    <div id="dataImportMonitor"></div>
    <div id="systemMonitoring"></div>
</div>

</body>
</html>
