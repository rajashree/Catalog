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
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.datepicker.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui-timepicker-addon.js"/>"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#tabs").tabs({ selected: 0, cache:false });
            $("#tabs").bind("tabsselect",function(event, tab) {
                //if(tab.tab.hash == '#gaAccountTab' || tab.tab.hash == '#webPropertiesTab') return;
                if(tab.index == 1) {
                    $(location).attr('href',basePath+"admin/retailers/list.do");
                } else if(tab.index == 2) {
                    $(location).attr('href',basePath+"admin/monitoring/dataImportMonitor.do");
                } else if(tab.index == 3) {
                    $(location).attr('href',basePath+"admin/monitoring/systemMonitoring.do");
                }
            });
            $("#webPropertiesDataTable input[name=initializationDate]:").each(function() {
                var isExists = $(this).parent().parent().find("input[name=isExists]").val();
                var isActive = $(this).parent().parent().find("input[name=isActive]").val();
                if(isExists == "false" || isActive == "true") {
                    generateDateTimePicker(this, isActive, isExists);
                } else {
                    $(this).focus(function() {
                        $('#dialog-confirm').html("<p>Please click on \"Activate\" button to update Initialization date</p>");
                        $("#dialog-confirm").dialog({
                            modal: true,
                            title:"Info",
                            buttons: {
                                'ok': function() {
                                    $(this).dialog('close');
                                }
                            }
                        });
                    })
                }
            });
            $('#preloader').hide();
        });



        function generateDateTimePicker(input, isActive, isExists) {
            $(input).datetimepicker({
                showOn: "button",
                dateFormat: "yy-mm-dd",
                buttonImage:"<c:url value="/images/cal.png"/>",
                buttonImageOnly: true,
                showButtonPanel:false,
                showMinute:false,
                timeFormat: 'hh:mm:ss',
                closeOnSelect:true,
                maxDate: new Date(),
                inline: true,
                onSelect:function(dateText, inst) {
                    $.datepicker._hideDatepicker();
                    if(isExists == "true" && isActive == "true") {

                        var newDate = new Date($(inst.input).val().replace(/-/g, '/'));
                        var oldDate = new Date(inst.lastVal.replace(/-/g, '/'));
                        if(newDate.getTime() != oldDate.getTime()) {
                            if(newDate.getTime() > oldDate.getTime())
                                $('#dialog-confirm').html("<p>Do you want to remove all the Events and Page Views till the date "+$(inst.input).val()+"</p>");
                            else
                                $('#dialog-confirm').html("<p>Do you want to change the initialization date to "+$(inst.input).val()+"</p>");
                            $("#dialog-confirm").dialog({
                                modal: true,
                                title:"Confirmation",
                                close:function() {
                                    //$('#'+inst.id).datetimepicker("setDate", oldDate);
                                },
                                buttons: {
                                    'Yes': function() {
                                        updateInitializationDate(inst, true);
                                        $(this).dialog('close');
                                    },
                                    'No': function() {
                                        if(newDate.getTime() > oldDate.getTime())
                                            updateInitializationDate(inst, false);
                                        else
                                            $('#'+inst.id).datetimepicker("setDate", oldDate);
                                        $(this).dialog('close');
                                    }
                                }
                            });
                        }
                    }
                }

            });
        }

        function updateInitializationDate(inst, delPrevDateData) {
            var profileId = $(inst.input).parent().parent().find("#profileId").html();
            var data= {profileId:profileId, delPrevDateData:delPrevDateData, newDate: $(inst.input).val()};
            $('#preloader').show();
            $.ajax({
                url:"updateInitializationDate.json",
                type:"POST",
                data:{data:JSON.stringify(data)},
                dataType:'json',
                success:function(data) {
                    $('#dialog-confirm').html("<p>" + data.message + "</p>");
                    $("#dialog-confirm").dialog({
                        modal: true,
                        title:"Info",
                        close:function() {
                            $('#preloader').hide();
                            location.reload();
                        },
                        buttons: {
                            'Ok': function() {
                                $('#preloader').hide();
                                $(this).dialog('close');
                            }
                        }
                    });
                },
                error:function(){
                    $('#dialog-confirm').html("<p>Error updating Initialization date.</p>");
                    $("#dialog-confirm").dialog({
                        modal: true,
                        title:"Info",
                        close:function() {
                            $('#preloader').hide();
                            location.reload();
                        },
                        buttons: {
                            'Ok': function() {
                                $('#preloader').hide();
                                $(this).dialog('close');
                            }
                        }
                    });
                }
            });
        }

        function updateStatus(wpId, active) {
            var data = {webPropertyId:wpId, active: active};
            $('#preloader').show();
            $.ajax({
                url:"updateStatus.json",
                type:"POST",
                data:{data:JSON.stringify(data)},
                dataType:'json',
                success:function(data) {
                    location.reload();
                },
                error:function(){
                    location.reload();
                }
            });
        }

        function synchronize(wp, target) {
            var propertyList = wp.replace(/[\{\}]/g,"").split(',');
            var webProperty = {};
            for (var i = 0; i < propertyList.length; i++) {
                var nameValuePair = propertyList[i].split("=");
                webProperty[$.trim(nameValuePair[0])] = $.trim(nameValuePair[1]);
            }
            webProperty.initializationDate = $(target).parent().parent().find("input[name=initializationDate]:").val();
            $('#preloader').show();
            $.ajax({
                url:"synchronize.json",
                type:"POST",
                data:{data:JSON.stringify(webProperty)},
                dataType:'json',
                success:function(data) {
                    location.reload()
                },
                error:function(){
                    location.reload()
                }
            });
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
    <div id="webPropertiesTab">
        <div>
            <table id="webPropertiesDataTable" class="datatable">
                <thead>
                <tr class="hdr_bg">
                    <th align="left">Web Property</th>
                    <th align="left">Profile ID</th>
                    <th align="left">Profile Name</th>
                    <th align="left">Account ID</th>
                    <th align="left">Account Name</th>
                    <th align="left">Initialization Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <c:choose>
                    <c:when test="${fn:length(webProperties) > 0}">
                        <c:forEach var="webProperty" varStatus="true" items="${webProperties}">
                            <c:choose>
                                <c:when test="${webProperty.isExists == true && webProperty.active == false}">
                                    <tr width="100%" style="background-color: #e3e3e3;">
                                </c:when>
                                <c:when test="${webProperty.isExists == false}">
                                    <tr width="100%" style="background-color: #FFFFCC;">
                                </c:when>
                                <c:when test="${webProperty.isExists == true && webProperty.active == true}">
                                    <tr width="100%" style="background-color: #e8ffd5">
                                </c:when>
                            </c:choose>

                            <%--<td id="selection" align="center"><input type="checkbox" name="selected_sync_webProperties" onclick="onSyncRowClick(this)"/></td>--%>
                            <td id="webProperty">${webProperty.webPropertyId}</td>
                            <td id="profileId">${webProperty.profileId}</td>
                            <td id="profileName">${webProperty.profileName}</td>
                            <td id="accountId">${webProperty.accountId}</td>
                            <td id="accountName">${webProperty.accountName}</td>

                            <td>
                                <input type="text" readonly="true" name="initializationDate"
                                       value="<fmt:formatDate value="${webProperty.initializationDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                            </td>
                            <td align="center">
                                <input type="hidden" name="isExists" value="${webProperty.isExists}">
                                <input type="hidden" name="isActive" value="${webProperty.active}">
                                <c:choose>
                                    <c:when test="${webProperty.isExists == true}">
                                        <c:choose>
                                            <c:when test="${webProperty.active == true}">
                                                <input style="width:100px" type="button" value="Deactivate" onclick="updateStatus('${webProperty.webPropertyId}',false)"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input style="width:100px" type="button" value="Activate" onclick="updateStatus('${webProperty.webPropertyId}',true)"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <input style="width:100px" type="button" value="Synchronize" onclick="synchronize('${webProperty}', this)"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            </tr>
                        </c:forEach>

                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="7" align="center"><p>No items found</p></td></tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </div>
        <div>

        </div>
    </div>
    <div id="yellowfinConfigTab"></div>
    <div id="dataImportMonitor"></div>
    <div id="systemMonitoring"></div>
</div>
<%--<div style="float: right; top:20px;">--%>
<%--<div>--%>
<%--<div style="width:10px; height: 10px; border: 1px solid #000"></div>--%>
<%--</div>--%>
<%--</div>--%>
<div id="dialog-confirm" style="overflow-x: auto;">

</div>
</body>
</html>
