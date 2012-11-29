<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><%= ConfigurationServiceImpl.getInstance().getProperty("application.name", "Dell DW")%></title>
<link type="text/css" href="<c:url value="/scripts/libs/jquery/dataTables/themes/ui-lightness/jquery-ui-1.8.4.custom.css"/>" rel="stylesheet"/>
<script type="text/javascript" language="javascript"
        src="<c:url value="/scripts/libs/jquery/jquery-1.7.2.min.js"/>"></script>
<script type="text/javascript" language="javascript"
        src="<c:url value="/js/jquery-ui.min.js"/>"></script>
<script type="text/javascript" language="javascript"
        src="<c:url value="/js/jquery.ui.tabs.js"/>"></script>
<script type="text/javascript" language="javascript"
        src="<c:url value="/scripts/libs/jquery/dataTables/jquery.dataTables-1.9.1.min.js"/>"></script>
<link type="text/css" href="<c:url value="/scripts/libs/jquery/dataTables/css/demo_table_jui.css"/>" rel="stylesheet"/>
<link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
<link  type="text/css" href="<c:url value="/css/jquery/jquery.jqplot.min.css"/>" rel="stylesheet" />


<script type="text/javascript"  src="<c:url value="/js/jquery.jqplot.min.js"/>"></script>
<script type="text/javascript"  src="<c:url value="/js/plugins/jqplot.dateAxisRenderer.min.js"/>"></script>
<script type="text/javascript"  src="<c:url value="/js/plugins/jqplot.highlighter.min.js"/>"></script>
<script type="text/javascript"  src="<c:url value="/js/plugins/jqplot.pointLabels.min.js"/>"></script>

<script type="text/javascript">
$(document).ready(function(){
    $("#tabs").tabs({ selected: 3 });
    $("#tabs").bind("tabsselect",function(event, tab) {
        if(tab.index == 0) {
            $(location).attr('href',basePath+"admin/googleanalytics/webProperties.do");
        } else if(tab.index == 1) {
            $(location).attr('href',basePath+"admin/retailers/list.do");
        }  else if(tab.index == 2) {
            $(location).attr('href',basePath+"admin/monitoring/dataImportMonitor.do");
        }
    });


    //Yellowfin
    /*$.getScript("http://yellowfin.marketvine.com:8080/JsAPI", loadChart);

     function loadChart(){
     var options = {};
     options.reportId="60223"
     options.elementId="ordersChart"
     options.username="admin@dell.com"
     options.password="123456"
     options.height="105px"
     options.width="255px"
     options.shotTitle="false"
     options.showInfo="false"
     options.showFitlers="false"
     options.showSections="false"
     options.showSeries="false"
     options.showPageLinks="false"
     options.showExport="false"
     yellowfin.loadReport(options);
     }
     */


    $( "#addEndpointContainer" ).dialog({
        autoOpen: false,
        height: 300,
        width: 500,
        modal: true,
        buttons: {
            Submit: function() {
                var intRegex = /^\d+$/;
                if($( "#endpointUrl" ).val().length <= 0 || $( "#endpointName" ).val().length <= 0 || $( "#thresholdLimit" ).val().length <= 0  || $( "#endpointType" ).val() == "0" || $( "#httpMethod" ).val() == "0" )
                    updateValidationError("All fields are required");
                else if(!intRegex.test($("#thresholdLimit").val())){
                    updateValidationError("Threshold Limit should be a whole number");
                }
                else{
                    $.ajax({
                        url: basePath+"admin/monitoring/saveOrUpdateEndpoint.json",
                        type: "POST",
                        data: ({
                            endpointId:$("#endpointId").val(),
                            endpointUrl:$( "#endpointUrl" ).val(),
                            endpointName:$( "#endpointName" ).val(),
                            endpointType:$( "#endpointType" ).val(),
                            httpMethod:$( "#httpMethod" ).val(),
                            thresholdLimit:$("#thresholdLimit").val()
                        }),
                        success: function(data) {
                            if(data.status == "success"){
                                alert("Endpoint added successfully");
                                location.reload();
                                /*
                                 alert(data.message);
                                 $( "#addEndpointContainer" ).dialog( "close" );
                                 if($( "#endpointType" ).val() == "1")
                                 getCSEndpointsDetails();
                                 else
                                 getExternalEndpointsDetails();*/
                            }else if(data.status == "failure"){
                                alert(data.message);
                            }
                        },
                        error:function() {

                        }
                    })
                }

            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        },
        close: function() {
        }
    });

    $( "#addServerContainer" ).dialog({
        autoOpen: false,
        height: 320,
        width: 500,
        modal: true,
        buttons: {
            Submit: function() {
                var intRegex = /^\d+$/;
                if($( "#serverName" ).val().length <= 0 || $( "#ip" ).val().length <= 0 || $( "#serverType" ).val() == "0"  )
                    updateValidationError("All fields are required");
                //if(!intRegex.test($("#port").val())){
                if($( "#port" ).val().length > 0 && !intRegex.test($("#port").val())){
                    updateValidationError("Port Number should be numeric value");
                }
                else{
                    $.ajax({
                        url: basePath+"admin/monitoring/saveOrUpdateServer.json",
                        type: "POST",
                        data: (
                        {
                            id:$("#id").val(),
                            serverName:$( "#serverName" ).val(),
                            ip:$( "#ip" ).val(),
                            serverType:$( "#serverType" ).val(),
                            port:$( "#port" ).val(),
                            monitoringEndpoint:$( "#monitoringEndpoint" ).val()
                        }),
                        success: function(data) {
                            if(data.status == "success"){
                                alert("Server added successfully");
                                location.reload();
                                /*alert(data.message);
                                 $( "#addServerContainer" ).dialog( "close" );
                                 getServerHealthDetails();*/
                            }else if(data.status == "failure"){
                                alert(data.message);
                            }
                        },
                        error:function() {
                        }
                    })
                }
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        },
        close: function() {
        }
    });

    $( "#addExternalEndpoint" )
            .button()
            .click(function() {

        $("#addEndpointForm").get(0).reset();
        $( "#addEndpointContainer" ).dialog( "open" );
        $("#endpointType").val("2");
    });
    $( "#addCSEndpoint" )
            .button()
            .click(function() {
        $("#addEndpointForm").get(0).reset();
        $( "#addEndpointContainer" ).dialog( "open" );
        $("#endpointType").val("1");
    });

    $( "#addServer" )
            .button()
            .click(function() {
        $("#addServerForm").get(0).reset();
        $( "#addServerContainer" )
                .dialog("option", "title", "Add Server")
                .dialog( "open" );
    });


    function updateValidationError(text ) {
        $( ".validationTips" )
                .text(text);
        setTimeout(function() {
            $( ".validationTips" ).text("");
        }, 1500 );
    }

    getAppHealthDetails();
    getCSEndpointsDetails();
    getExternalEndpointsDetails();
    getMerchantFeeds();
    getServerHealthDetails();
    getMarketvineOrderDetails();
    getMarketvinePixelDetails();

});

// App Health Request
function getAppHealthDetails() {
    $.ajax({
        url: basePath+"admin/monitoring/getAppHealthDetails.json",
        type: "POST",
        success: function(data) {
            parseAppHealthDetails(data.details)
        },
        complete: setTimeout(function() {getAppHealthDetails()}, 300000)
    })

}


// Content Server Endpoints
function getCSEndpointsDetails() {
    $.ajax({
        url: basePath+"admin/monitoring/getCSEndpointsDetails.json",
        type: "POST",
        success: function(data) {
            parseCSEndpointsDetails(data.csEndpoints)
        },
        complete: setTimeout(function() {getCSEndpointsDetails()}, 300000),
        error:function() {
        }
    })
}

// External Endpoints
function getExternalEndpointsDetails() {
    $.ajax({
        url: basePath+"admin/monitoring/getExternalEndpointsDetails.json",
        type: "POST",
        success: function(data) {
            parseExternalEndpointsDetails(data.externalEndpoints)
        },
        error:function() {
        },
        complete: setTimeout(function() {getExternalEndpointsDetails()}, 300000)
    })
}

// Merchant Feeds
function getMerchantFeeds() {
    $.ajax({
        url: basePath+"admin/monitoring/getMerchantFeeds.json",
        type: "POST",
        success: function(data) {
        },
        error:function() {
        },
        complete: setTimeout(function() {getMerchantFeeds()}, 300000)

    })
}

// Server Health
function getServerHealthDetails() {
    $.ajax({
        url: basePath+"admin/monitoring/getServerHealthDetails.json",
        type: "POST",
        success: function(data) {
            parseServerHealthDetails(data.serverHealthDetails)
        },
        error:function() {
        },
        complete: setTimeout(function() {getServerHealthDetails()}, 300000)
    })
}

// Marketvine Pixel
function getMarketvinePixelDetails() {
    $('#marketvinePixelStatus').html("<span class='iconLoader'>-</span>");
    var innerHtml = '';
    $.ajax({
        url: basePath+"admin/monitoring/getMarketvinePixelDetails.json",
        type: "POST",
        success: function(data) {
            if(data.status == "success"){
                innerHtml = "<span class='iconRight'></span>";
            }else{
                innerHtml = "<span class='iconWrong'></span>";
            }
            $('#marketvinePixelStatus').html(innerHtml);
        },
        error:function() {
            innerHtml = "Ajax call failed. Retry the operation";
            $('#marketvinePixelStatus').html(innerHtml);
        }
    })
};

//Marketvine Pixel Orders
function getMarketvineOrderDetails(){
    $.ajax({
        url: basePath+"admin/monitoring/getMarketvineOrderDetails.json",
        type: "POST",
        success: function(data) {
            parseMarketvineOrderDetails(data.details)
        },
        error:function() {
        },
        complete: setTimeout(function() {getMarketvineOrderDetails()}, 300000)
    })
}

function parseAppHealthDetails(details) {
    var innerHtml = '';
    for(var i = 0; i < details.length; i++) {
        var detail = details[i];
        innerHtml += "<tr>";
        innerHtml += "<td>" + detail.appName + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.pageViewsToday + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.avgDailyPageViews + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.loadTime + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.avgLoadTime + "</td>";
        innerHtml += "</tr>";
    }

    if(details.length > 0){
        $('#appHealthTableBody').html(innerHtml);
    }
    $('#appHealthTable').dataTable({
        "bJQueryUI":true,
        "bPaginate":true,
        "sScrollX":"100%",
        "iDisplayLength":5,
        "aLengthMenu": [[5,10, 25, 50, -1], [5,10, 25, 50, "All"]],
        "bScrollCollapse":true,
        "bRetrieve" : true,
        "sPaginationType":"full_numbers",
        "oLanguage": {
            "sSearch": "" ,
            "oPaginate": {
                "sFirst": "<<",
                "sLast":">>",
                "sNext":">",
                "sPrevious":"<"
            }
        },
        "aaSorting":[
            [ 0, "asc" ]
        ]
    });





}

function parseServerHealthDetails(details) {
    var innerHtml = '';
    for(var i = 0; i < details.length; i++) {
        var detail = details[i];
        innerHtml += "<tr>";
        innerHtml += "<td>" + detail.serverName + "</td>";
        innerHtml += "<td>" + detail.ip + "</td>";
        innerHtml += "<td style='text-align: center'>" + detail.uptime + "</td>";
        innerHtml += "<td class='numberCell'>" + ((detail.connections == -1)?"N/A":detail.connections) + "</td>";
        innerHtml += "<td><div id='progressbar"+i+"'></div><div class='progressbarValue'>"+(isNaN(parseInt(detail.totalMemory))?0: parseInt(detail.totalMemory))+"</div></td>";

        innerHtml +="<td><div class='wrapperactions'><button class='btnEdit'  onclick='editServerDetails("+detail.serverId+")'></button>";
        innerHtml +="<button class='btnDelete' onclick='deleteServerEvent("+detail.serverId+")'></button></div></td>";
        innerHtml += "</tr>";

    }
    $('#serverHealthTableBody').html(innerHtml);
    for(var i = 0; i < details.length; i++) {
        var detail = details[i];
        detail.memory = (i+1)*10;
        $( "#progressbar"+i ).progressbar({
            value: (isNaN(parseFloat(detail.memory))?0: parseFloat(detail.memory))
        });


    }

    jQuery.fn.dataTableExt.oSort['memoryUsage-asc']  = function(x,y) {

        return (($(x).get(0).getAttribute("aria-valuenow") < $(y).get(0).getAttribute("aria-valuenow")) ? -1 : (($(x).get(0).getAttribute("aria-valuenow") > $(y).get(0).getAttribute("aria-valuenow")) ?  1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['memoryUsage-desc'] = function(x,y) {
        return (($(x).get(0).getAttribute("aria-valuenow") < $(y).get(0).getAttribute("aria-valuenow")) ?  1 : (($(x).get(0).getAttribute("aria-valuenow") > $(y).get(0).getAttribute("aria-valuenow")) ? -1 : 0));
    };

    $('#serverHealthTable').dataTable({
        "bJQueryUI":true,
        "bPaginate":true,
        "sScrollX":"100%",
        "aLengthMenu": [[5,10, 25, 50, -1], [5,10, 25, 50, "All"]],
        "iDisplayLength":5,
        "bScrollCollapse":true,
        "bRetrieve" : true,
        "sPaginationType":"full_numbers",
        "oLanguage": {
            "sSearch": "" ,
            "oPaginate": {
                "sFirst": "<<",
                "sLast":">>",
                "sNext":">",
                "sPrevious":"<"
            }
        },
        "aoColumns" : [
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":true,"asSorting": [ "desc", "asc" ] },
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":true, "asSorting":["desc","asc"], "sType" : "memoryUsage"  },
            {"bSortable":false}
        ],
        "aaSorting":[
            [ 0, "asc" ]
        ]
    });
}

function parseCSEndpointsDetails(details) {
    var innerHtml = '';
    for(var i = 0; i < details.length; i++) {
        var detail = details[i];
        innerHtml += "<tr>";
        innerHtml += "<td>" + detail.endpointName + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.responseTime + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.avgResponseTime + "</td>";

        if((detail.responseTime > detail.thresholdLimit)  || detail.responseTime <= 0)
            innerHtml += "<td><span class='iconReject'>WRONG</span></td>";
        else
            innerHtml +="<td><span class='iconApprove'>RIGHT</span></td>";
        innerHtml +="<td><div class='wrapperactions'><button class='btnEdit' onclick='editEndpointDetails("+detail.endpointId+")'></button>";
        innerHtml +="<button class='btnDelete' onclick='deleteEndpointEvent("+detail.endpointId+",1)'></button></div></td>";
        innerHtml += "</tr>";
    }
    $('#csEndpointTableBody').html(innerHtml);
    $('#csEndpointTable').dataTable({
        "bJQueryUI":true,
        "bAutoWidth": false,
        "bPaginate":true,
        "sScrollX":"100%",
        "aLengthMenu": [[5,10, 25, 50, -1], [5,10, 25, 50, "All"]],
        "iDisplayLength":5,
        "bRetrieve" : true,
        "bScrollCollapse":true,
        "sPaginationType":"full_numbers",
        "oLanguage": {
            "sSearch": "" ,

            "oPaginate": {
                "sFirst": "<<",
                "sLast":">>",
                "sNext":">",
                "sPrevious":"<"
            }
        },
        "aoColumns" : [
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":true,"asSorting": [ "desc", "asc" ] },
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":false}
        ],
        "aaSorting":[
            [ 0, "asc" ]
        ]
    });
}

function parseExternalEndpointsDetails(details) {
    var innerHtml = '';
    for(var i = 0; i < details.length; i++) {
        var detail = details[i];
        innerHtml += "<tr>";
        innerHtml += "<td>" + detail.endpointName + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.responseTime + "</td>";
        innerHtml += "<td class='numberCell'>" + detail.avgResponseTime + "</td>";

        if((detail.responseTime > detail.thresholdLimit)  || detail.responseTime <= 0)
            innerHtml += "<td><span class='iconReject'>WRONG</span></td>";
        else
            innerHtml +="<td><span class='iconApprove'>RIGHT</span></td>";
        innerHtml +="<td><div class='wrapperactions'><button class='btnEdit' onclick='editEndpointDetails("+detail.endpointId+")'></button>";
        innerHtml +="<button class='btnDelete' onclick='deleteEndpointEvent("+detail.endpointId+",2)'></button></div></td>";
        innerHtml += "</tr>";
    }
    $('#externalEndpointTableBody').html(innerHtml);
    $('#externalEndpointTable').dataTable({
        "bJQueryUI":true,
        "bAutoWidth": false,
        "bPaginate":true,
        "sScrollX":"100%",
        "aLengthMenu": [[5,10, 25, 50, -1], [5,10, 25, 50, "All"]],
        "iDisplayLength":5,
        "bRetrieve" : true,
        "bScrollCollapse":true,
        "sPaginationType":"full_numbers",
        "oLanguage": {
            "sSearch": "" ,
            "oPaginate": {
                "sFirst": "<<",
                "sLast":">>",
                "sNext":">",
                "sPrevious":"<"
            }
        },
        "aoColumns" : [
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":true,"asSorting": [ "desc", "asc" ] },
            {"bSortable":true, "asSorting": [ "desc", "asc" ] },
            {"bSortable":false}
        ],
        "aaSorting":[
            [ 0, "asc" ]
        ]
    });
}


function parseMarketvineOrderDetails(details) {
    if(details != null && details.length >0){
        var result = "[[";
        for (var i = 0; i < details.length ; i++) {
            result = result+ "['" + details[i][1] + "'," + details[i][0] + "]";
            if(i != details.length -1 ){
                result = result + ",";
            }else{
                result = result + "]]";
            }
        }
        var plot2 = $.jqplot('ordersChart', eval(result), {
            animate: true,
            animateReplot: true,
        title:"Total Orders",
            axes: {
                xaxis: {
                    renderer: $.jqplot.DateAxisRenderer
                }
            },
            highlighter: {
                show: true,
                showLabel: true,
                tooltipAxes: 'xy',
                yvalues: 1,
                sizeAdjust: 7.5 ,
                tooltipLocation : 'n',
                formatString:'%s, %d Order(s)'

            }
        });
    }else{
       $("#ordersChart").html("<div style='margin-top:200px'><b>No Orders data to display</b></div>");
    }

}


function deleteEndpointEvent(target,endpoinType) {
    $('#dialog-confirm').html("<p>Do you want to remove the endpoint?");
    $("#dialog-confirm").dialog({
        modal: true,
        title:"Confirmation",
        buttons: {
            'Yes': function() {
                deleteEndpoint(target,endpoinType);
                $(this).dialog('close');
            },
            'No': function() {
                $(this).dialog('close');
            }
        }
    });
}

function deleteEndpoint(id,endpoinType){
    $.ajax({
        url: basePath+"admin/monitoring/deleteEndpoint.json?endpointId="+id,
        type: "POST",
        success: function(data) {
            if(data.status == "success"){
                alert("Endpoint deleted successfully");
                location.reload();
                /*if(endpoinType == 1)
                 getCSEndpointsDetails();
                 else
                 getExternalEndpointsDetails();*/
            }else if(data.status == "failure"){
                alert(data.message);
            }else{
                alert("Delete Endpoint failed");
            }
        },
        error:function() {
            alert("Ajax call failed. Retry the operation");
        }
    })
}

function deleteServerEvent(target) {
    $('#dialog-confirm').html("<p>Do you want to remove the server?");
    $("#dialog-confirm").dialog({
        modal: true,
        title:"Confirmation",
        buttons: {
            'Yes': function() {
                deleteServer(target);
                $(this).dialog('close');
            },
            'No': function() {
                $(this).dialog('close');
            }
        }
    });
}

function deleteServer(id){
    $.ajax({
        url: basePath+"admin/monitoring/deleteServer.json?serverId="+id,
        type: "POST",
        success: function(data) {
            if(data.status == "success"){
                alert("Server deleted successfully");
                location.reload();
                //getServerHealthDetails();
            }else if(data.status == "failure"){
                alert(data.message);
            }else{
                alert("Delete Server failed");
            }
        },
        error:function() {
            alert("Ajax call failed. Retry the operation");
        }
    })
}

function editServerDetails(target){
    $.ajax({
        url: basePath+"admin/monitoring/editServer.json?serverId="+target,
        type: "GET",
        success: function(data) {
            if(data.status == "success"){
                $( "#addServerContainer" )
                        .dialog("option", "title", "Edit Server")
                        .dialog( "open");

                populate('#addServerForm', data.data);
                $("#serverName").attr("readonly","readonly")
            }else if(data.status == "failure"){
                alert(data.message);
            }else{
                alert("Edit Server details failed");
            }
        },
        error:function() {
            alert("Ajax call failed. Retry the operation");
        }
    })
}


function editEndpointDetails(target){
    $.ajax({
        url: basePath+"admin/monitoring/editEndpoint.json?endpointId="+target,
        type: "GET",
        success: function(data) {
            if(data.status == "success"){
                $( "#addEndpointContainer" )
                        .dialog("option", "title", "Edit Endpoint")
                        .dialog( "open");
                populate('#addEndpointForm', data.data);
                $("#endpointName").attr("readonly","readonly")
            }else if(data.status == "failure"){
                alert(data.message);
            }else{
                alert("Edit Endpoint details failed");
            }
        },
        error:function() {
            alert("Ajax call failed. Retry the operation");
        }
    })
}


function populate(frm, data){
    $.each(data, function(key, value){
        $('[name='+key+']', frm).val(value);
    });
}


</script>
</head>
<body>


<div id="addEndpointContainer" title="Add Endpoint">
    <form id="addEndpointForm">
        <p class="validationTips"></p>
        <input type="hidden" name="endpointType" id="endpointType" value=""/>
        <input type="hidden" name="endpointId" id="endpointId"/>
        <div class="wrapperInputElement">
            <label for="endpointName">Endpoint Name</label>
            <input type="text" name="endpointName" id="endpointName" value="" />
        </div>
        <div class="wrapperInputElement">
            <label for="endpointUrl">Endpoint Url</label>
            <input type="text" name="endpointUrl" id="endpointUrl" value="" />
        </div>
        <div class="wrapperInputElement">
            <label for="httpMethod">Http Method</label>
            <select name="httpMethod" id="httpMethod">
                <option name="" value="0">Select Http Method</option>
                <option name="" value="GET">GET</option>
                <option name="" value="POST">POST</option>
            </select>
        </div>
        <div class="wrapperInputElement">
            <label for="thresholdLimit">Threshold Limit</label>
            <input type="text" name="thresholdLimit" id="thresholdLimit" value="" />
        </div>
    </form>
</div>

<div id="addServerContainer" title="Add Server">
    <form id="addServerForm">
        <p class="validationTips"></p>
        <input type="hidden" name="id" id="id"/>
        <div class="wrapperInputElement">
            <label for="serverName">Server Name</label>
            <input type="text" name="serverName" id="serverName" value="" />
        </div>
        <div class="wrapperInputElement">
            <label for="ip">IP / Hostname</label>
            <input type="text" name="ip" id="ip" value="" />
        </div>
        <div class="wrapperInputElement">
            <label for="port">Port (optional)</label>
            <input type="text" name="port" id="port" value="" />
        </div>
        <div class="wrapperInputElement">
            <label for="serverType">Server Type</label>
            <select name="serverType" id="serverType">
                <option name="" value="0">Select Server Type</option>
                <option name="" value="MSSQL">MSSQL</option>
                <option name="" value="LINUX_APACHE">LINUX_APACHE</option>
                <option name="" value="LINUX_TOMCAT">LINUX_TOMCAT</option>
            </select>
        </div>
        <div class="wrapperInputElement">
            <label for="monitoringEndpoint">Monitoring Endpoint (optional)</label>
            <input type="text" name="monitoringEndpoint" id="monitoringEndpoint" value="" />
        </div>

    </form>
</div>


<div id="tabs">
    <ul>
        <li><a href="#webPropertiesTab">GA Web Properties</a></li>
        <li><a href="#yellowfinConfigTab">Yellowfin Configuration</a></li>
        <li><a href="#dataImportMonitor">Data Import Monitor</a></li>
        <li><a href="#systemMonitoring">System Monitoring</a></li>
    </ul>
    <div id="webPropertiesTab"></div>
    <div id="yellowfinConfigTab"></div>
    <div id="dataImportMonitor"></div>
    <div id="systemMonitoring">
        <div id="serverHealthContainer">
            <fieldset title="Server Health">
                <legend><strong>Server Health</strong><button id="addServer">Add Server</button></legend>
                <table id="serverHealthTable" border="1" class="datatable">
                    <thead>
                    <th>Server Name</th>
                    <th>IP / Hostname</th>
                    <th>Uptime</th>
                    <th>DB Connections</th>
                    <th>Memory Usage</th>
                    <th class="{sorter: false}">Actions</th>
                    </thead>
                    <tbody id="serverHealthTableBody">
                    </tbody>
                </table>
            </fieldset>
        </div>
        <div class="wrapperForTwoSections">
            <div id="externalEndpointContainer">
                <fieldset title="External Endpoints">
                    <legend>
                        <strong>External Endpoints</strong>
                        <button id="addExternalEndpoint">Add Endpoint</button>
                    </legend>

                    <table id="externalEndpointTable" border="1" class="datatable">
                        <thead>
                        <th>Endpoint</th>
                        <th>Response(ms)</th>
                        <th>Avg Response(ms)</th>
                        <th>Status</th>
                        <th>Actions</th>
                        </thead>
                        <tbody id="externalEndpointTableBody">
                        </tbody>
                    </table>
                </fieldset>
            </div>
            <div id="merchantFeedsContainer">
                <fieldset title="Merchant Feeds">
                    <legend><strong>Merchant Feeds</strong></legend>
                    <table id="merchantFeedTable" border="1" class="datatable">
                        <thead>
                        <th>Merchant Feed</th>
                        <th>Import Date</th>
                        <th>Status</th>
                        <th>Status</th>
                        </thead>
                        <tbody id="merchantFeedTableBody">
                        </tbody>
                    </table>
                </fieldset>
            </div>
        </div>
        <div class="wrapperForTwoSections">
            <div id="csEndpointContainer">
                <fieldset title="Content Server Endpoints">
                    <legend>
                        <strong>Content Server Endpoints</strong>
                        <button id="addCSEndpoint">Add Endpoint</button>
                    </legend>
                    <table id="csEndpointTable" border="1" class="datatable">
                        <thead>
                        <th>Endpoint</th>
                        <th>Response(ms)</th>
                        <th>Avg Response(ms)</th>
                        <th>Status</th>
                        <th>Actions</th>
                        </thead>
                        <tbody id="csEndpointTableBody">
                        </tbody>
                    </table>
                </fieldset>
            </div>
            <div id="marketvinePixelContainer">
                <fieldset title="Marketvine Pixel">
                    <legend><strong>Marketvine Pixel</strong></legend>
                    <div class="fleft">
                        <a  class="linkPixelTest" href="javascript:getMarketvinePixelDetails()">Test Pixel</a>
                    </div>
                    <div class="wrapperPixelStatus">
                        <div class="txtPixelStatus">Status :</div>
                        <div id="marketvinePixelStatus"></div>
                    </div>
                    <div id="ordersChart" class="chartOrders">
                    </div>
                </fieldset>

            </div>
        </div>
        <div id="appHealthContainer">
            <fieldset title="App Health">
                <legend><strong>App Health</strong></legend>
                <table id="appHealthTable" border="1" class="datatable">
                    <thead>
                    <th>App Name</th>
                    <th>Page Views Today</th>
                    <th>Avg Daily Page Views</th>
                    <th>Load Time(ms)</th>
                    <th>Avg Load Time(ms)</th>
                    </thead>
                    <tbody id="appHealthTableBody">
                    </tbody>
                </table>
            </fieldset>
        </div>

    </div>
</div>
<div id="dialog-confirm" style="overflow-x: auto;"/>
</body>
</html>

