<%@ page import="com.sourcen.core.util.DateUtils" %>
<%@ page import="com.sourcen.core.util.FileSystem" %>
<%@ page import="com.sourcen.core.util.StringUtils" %>
<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Data Import Monitor</title>
    <%--<style type="text/css" title="currentStyle">--%>
    <%--@import "<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>";--%>
    <%--@import "<c:url value="/scripts/libs/jquery/dataTables/css/demo_table_jui.css"/>";--%>
    <%--@import "<c:url value="/scripts/libs/jquery/dataTables/themes/ui-lightness/jquery-ui-1.8.4.custom.css"/>";--%>
    <%--</style>--%>

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


    <script type="text/javascript">

        $(document).ready(function(){

            $("#tabs").tabs({ selected: 2 });
            $("#tabs").bind("tabsselect",function(event, tab) {
                if(tab.index == 0) {
                    $(location).attr('href',basePath+"admin/googleanalytics/webProperties.do");
                } else if(tab.index == 1) {
                    $(location).attr('href',basePath+"admin/retailers/list.do");
                }  else if(tab.index == 3) {
                    $(location).attr('href',basePath+"admin/monitoring/systemMonitoring.do");
                }
            });

            $('#sh_container').dataTable({
                "bJQueryUI":true,
                "bPaginate":true,
                "sScrollX":"100%",
                "iDisplayLength":15,
                "aLengthMenu": [[15,30, 45, 60, -1], [15,30, 45, 60, "All"]],
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
                    [ 2, "asc" ]
                ]
            });
        });
        function setDSValue(obj) {
            var hiddenValue = obj.value;
            if(obj.value == 1){
                $('#wp').removeAttr('disabled');

            }else{
                $("#wp").val(0);
                document.getElementById('webpropertyprofile').value = 0;
                $('#wp').attr('disabled', 'disabled');
            }
            document.getElementById('datasource').value = hiddenValue;
        }

        function setWPValue(obj) {
            var hiddenValue = obj.value;
            document.getElementById('webpropertyprofile').value = hiddenValue;
        }
        function processNow(obj) {
            $("#preloader").show();
            $.ajax({
                url:basePath+"admin/monitoring/processBatch.json?id="+obj,
                type:"POST",
                dataType:'json',
                success:function(data) {
                    if(data.status == "success"){
                        alert(data.message);
                        location.reload()
                    }else{
                        alert(data.message);
                    }

                },
                error:function(){
                    alert("Error occured.  Please try again")
                }
            });

        }

        function submitForm(obj){
            $("#preloader").show();
            obj.submit();
        }
    </script>
</head>
<body>
<%!
    public String checkErrorFileLocation(String filepath,String startDate) throws IOException {
        if (filepath != null) {
            String path = StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "logs" + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR +"error_feeds";
            if(filepath.contains(startDate)){
                return path+filepath.replace(".csv","")+"_errors.csv";
            }if(filepath.contains(".csv")){
                return path+StringUtils.getSimpleString(filepath.substring(0, (filepath.length() - 3 - 1)))+"_"+startDate+"_errors.csv";
            }else{
                return path+StringUtils.getSimpleString(filepath.substring(0, (filepath.length())))+"_"+startDate+"_errors.csv";

            }

        } else {
            return null;
        }

    }
%>
<%!
    public String checkLogFileLocation(String filepath,String startDate) throws IOException {
        if (filepath != null) {
            String path = StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "logs" + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR +
                    "logs_com_dell_dw_dataimport_";
            if(filepath.contains(startDate)){
                filepath = filepath.replace(startDate,"");
                return path+StringUtils.getSimpleString(filepath.replace(".csv",""))+startDate+".log";
            }else{
                return path+StringUtils.getSimpleString(filepath.replace(".csv",""))+"_"+startDate+".log";

            }
        } else {
            return null;
        }

    }
%>
<div id="tabs">
<ul>
    <li><a href="#webPropertiesTab">GA Web Properties</a></li>
    <li><a href="#yellowfinConfigTab">Yellowfin Configuration</a></li>
    <li><a href="#dataImportMonitor">Data Import Monitor</a></li>
    <li><a href="#systemMonitoring">System Monitoring</a></li>
</ul>
<div id="webPropertiesTab"></div>
<div id="yellowfinConfigTab"></div>
<div id="dataImportMonitor" >
<div id="dataImportContainer">
<fieldset style="padding: 5px;">
    <legend><strong>Please choose a Datasource</strong></legend>
    <div id="ds" class="clearfix">

        <form:form method="POST" action="dataImportMonitor.do">
            <table class="fleft">
                <tr class="form_txtcolor">
                    <td>
                        <select id="dsId" onchange="setDSValue(this)">
                            <option name="" value="0">Select Datasource</option>
                            <c:forEach var="entry" items="${datasources}">
                                <c:choose>
                                    <c:when test="${prepopulateData.datasource == entry.id}">
                                        <option name="" value="${entry.id}"  selected="selected">${entry.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option name="" value="${entry.id}">${entry.name}</option>

                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </td>
                    </td>
                    <td>

                        <input type="hidden" id="datasource" name="datasource" value="${prepopulateData.datasource}">
                    </td>
                    <td>
                        <select width="150" style="width: 150px" id="wp" onchange="setWPValue(this)" <c:if test="${prepopulateData.datasource != 1}">disabled="true"</c:if>>
                            <option name="" value="0">Select Web Property</option>
                            <c:forEach var="entry" items="${webpropertyprofiles}">
                                <c:choose>
                                    <c:when test="${prepopulateData.webpropertyprofile == entry.id}">
                                        <option name="" value="${entry.id}"  selected="selected">${entry.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option name="" value="${entry.id}">${entry.name}</option>

                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <input type="hidden" id="webpropertyprofile" name="webpropertyprofile"  value="${prepopulateData.webpropertyprofile}">
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${fn:contains(prepopulateData.processStatus, '0')}">
                                <input type="checkbox"  name="processStatus" value="0" checked="true">
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox"  name="processStatus" value="0">
                            </c:otherwise>
                        </c:choose>
                        Pending Processes
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${fn:contains(prepopulateData.processStatus, '1')}">
                                <input type="checkbox"  name="processStatus" value="1" checked="true">
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox"  name="processStatus" value="1">
                            </c:otherwise>
                        </c:choose>
                        Active Processes
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${fn:contains(prepopulateData.processStatus, '3-4-5-6')}">
                                <input type="checkbox"  name="processStatus" value="3-4-5-6" checked="true">
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox"  name="processStatus" value="3-4-5-6">
                            </c:otherwise>
                        </c:choose>
                        Failed Processes
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${fn:contains(prepopulateData.processStatus, '2')}">
                                <input type="checkbox"  name="processStatus" value="2" checked="true">
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox"  name="processStatus" value="2">
                            </c:otherwise>
                        </c:choose>
                        Completed Processes
                    </td>

                </tr>
            </table>
            <div class="fright">
                <input type="submit" value="Submit" onclick="submitForm()"/>
            </div>

        </form:form>
    </div>
</fieldset>

<div style="padding-top: 5px;">
    <table border="1" id="sh_container"  class="datatable">
        <thead>
        <tr class="hdr_bg">
            <th style="width: 110px;">Feed file path</th>
            <th style="width: 80px;">End Point</th>
            <th style="width: 110px;">Status</th>
            <th style="width: 110px;">Processed on</th>
            <th style="width: 85px;">Log Files</th>
            <th style="width: 85px;">Error Files</th>
            <th style="width: 85px;">Actions</th>
        </tr>
        </thead>
        <c:forEach items="${dataSchedulerBatches}" var="row">
            <tr>
                <td style="width: 110px;">${row.filePath}</td>
                <td style="width: 80px;">${row.endpoint}</td>
                <td style="width: 110px;" class="status-${row.status}">
                    <c:choose>
                        <c:when test="${row.status == 0}">IN QUEUE</c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${row.status == 1}">PROCESSING</c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${row.status == 2}">DONE</c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${row.status == 3}">ERROR READING</c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${row.status == 5}">ERROR PARSING</c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${row.status == 6}">ERROR WRITING</c:when>
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
                <td style="width: 110px;"><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium"
                                    value="${row.lastProcessedDate}"/></td>
                <td style="width: 85px;">
                    <c:set var="filePath" value="${row.filePath}" scope="request"/>
                    <c:set var="startDate" value="${row.startDate}" scope="request"/>
                    <%
                        final String filePath = (String) request.getAttribute("filePath");
                        final String startDate = DateUtils.TIMESTAMP_DATEFORMAT.format(request.getAttribute("startDate")).replaceAll(":", "_");
                        if(!WebUtils.isNullOrEmpty(filePath)){
                            String logFilePath = checkLogFileLocation(filePath,startDate);
                            File logFile = FileSystem.getDefault().getFile(logFilePath, false, false);
                            if (logFile.exists()) {
                    %>
                    <a id="logFileLink"
                       href="<%=request.getContextPath()%>/errorFileDownload?logFilePath=<c:out value="${row.filePath}"/>&startDate=<c:out value="${row.startDate}"/>">Log
                        File</a>
                    <%
                        }
                    }else{
                    %>
                    N/A
                    <%
                        }
                    %>
                </td>
                <td style="width: 85px;">
                    <c:set var="filePath" value="${row.filePath}" scope="request"/>
                    <c:set var="startDate" value="${row.startDate}" scope="request"/>
                    <c:set var="status" value="${row.status}" scope="request"/>
                    <%
                        final String errFilePath = (String) request.getAttribute("filePath");
                        final String startDate2= DateUtils.TIMESTAMP_DATEFORMAT.format(request.getAttribute("startDate")).replaceAll(":", "_");
                        final int status = (Integer) request.getAttribute("status");
                        if(!WebUtils.isNullOrEmpty(filePath)){
                            String errorFilePath = checkErrorFileLocation(errFilePath,startDate2);
                            File errorFile = FileSystem.getDefault().getFile(errorFilePath, false, false);
                            if (errorFile.exists() && status != 2){
                    %>
                    <a id="errorFileLink"
                       href="<%=request.getContextPath()%>/errorFileDownload?errorFilePath=<c:out value="${row.filePath}"/>&startDate=<c:out value="${row.startDate}"/>">Error
                        File</a>
                    <%
                        }
                    }else{
                    %>
                    N/A
                    <%
                        }
                    %>
                </td>
                <td style="width: 85px;">
                    <c:choose>
                        <c:when test="${fn:contains('3-4-5-6',row.status) || fn:contains('0',row.status)}">
                            <a href="javascript:processNow(${row.id})"/>Process Now</a>
                        </c:when>
                        <c:otherwise>
                            N/A
                        </c:otherwise>
                    </c:choose>


                </td>

            </tr>
        </c:forEach>
    </table>
</div>
</div>
</div>
<div id="systemMonitoring"></div>
</div>
</body>
</html>