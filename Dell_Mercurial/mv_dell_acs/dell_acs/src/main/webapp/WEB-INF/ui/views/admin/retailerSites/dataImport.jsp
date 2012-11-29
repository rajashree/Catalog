<%@ page import="com.sourcen.core.util.FileSystem" %>
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.sourcen.core.util.StringUtils" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%
    //Refresh the page every 30 seconds automatically.
    //response.setHeader("Refresh", "30");
%>
<head>
    <meta http-equiv="Cache-Control" content="no-cache" forua="true"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>Retailers list page</title>
    <%@include file="../../includes/header-scripts.jsp" %>
    <script type="text/javascript">
        function verifyExtension() {
            var value = document.getElementById("dataFile").value;
            if (value != '') {
                value = value.toLowerCase();
                if (!value.match(/(\.txt|\.csv|\.tab|\.zip|\.CSV|\.ZIP|\.gz|\.GZ)$/)) {
                    alert("valid extensions are .zip .csv.gz .csv .tab");
                    document.getElementById("dataFile").focus();
                    return false;
                }
            }
            return true;
        }

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

        .status-3, .status-4, .status-5, .status-9, .status-10 {
            background-color: #ff6666;
        }
    </style>
</head>
<body>
<%!
    public boolean checkErrorFileExistenceStatus(String filepath, HttpServletRequest request) throws IOException {
        if (filepath != null) {
            String filePath = filepath;
            final String extension = FilenameUtils.getExtension(filePath);
            final String errorFilePath =
                    filePath.substring(0, filePath.length() - extension.length() - 1) +
                            "_errors." + extension;
            File errorFile = FileSystem.getDefault()
                    .getFile(errorFilePath, false, false);
            if (errorFile.exists()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
%>
<%!
    public String checkLogFileLocation(String filepath) throws IOException {
        if (filepath != null) {
            String searchString = "/feeds";
            String replacementString =
                    StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "logs" + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR +
                            "dataimportcom_dell_acs_dataimport_dataFile_logs_";
            String logFilePath = StringUtils.replace(filepath, searchString, replacementString);
            String logFileName = StringUtils.remove(logFilePath, "-").replace(".", "_").concat(".log");
            return logFileName;
        } else {
            return null;
        }

    }
%>
<c:choose>
<c:when test="${empty retailerSite}">
    RETAILER SITE NOT FOUND.
</c:when>
<c:otherwise>

${navCrumbs}

<div>
    <fieldset>
        <legend><strong>Feed download</strong></legend>
        <c:choose>
            <c:when test="${empty downloadedFileList}">
                No Retailer feed for available to download from the FTP.
            </c:when>
            <c:otherwise>
                <div id="downloadedFileList">
                        <%-- TODO: Why is this condition here? --%>
                    <c:choose>
                        <c:when test="${ empty downloadedFileList}">
                            DOWNLOADED FILE NOT AVAILABLE
                        </c:when>
                        <c:otherwise>
                            <div class="sh_container">
                                <table class="datatable sh_header">
                                    <thead>
	                                    <th style="width: 500px;">Downloaded Zip File</th>
	                                    <th>Downloaded Zip Status</th>
                                    </thead>
                                </table>
                                <div class="sh_body">
                                    <table class="datatable">
                                        <tbody>
                                        <c:forEach items="${downloadedFileList}" var="fileList">
                                            <tr>
                                                <td style="width: 500px;"><c:out value="${fileList.key}"/></td>
                                                <td><c:out value="${fileList.value}"/></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </fieldset>
</div>
<br/>
<br/>

<fieldset>
    <legend><strong>Feed processing</strong></legend>
    <div class="sh_container">
        <table class="datatable sh_header">
            <thead>
	            <th style="width: 450px;">File Path</th>
	            <th style="width: 110px;">Status</th>
	            <th>Processed On</th>
	            <th>Files</th>
            </thead>
        </table>
        <div class="sh_body">
            <table class="datatable" style="width:99%;">
                <tbody>
                <c:forEach items="${dataFiles}" var="row">
                    <c:set var="feed_status" value="${row.status}"></c:set>

                    <tr>
                        <td style="width: 450px;">${row.filePath}</td>
                        <td style="width: 110px;" class="status-${feed_status}">
                            <c:choose>
                                <c:when test="${feed_status == 0}">IN QUEUE</c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${feed_status == 1}">PROCESSING</c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${feed_status == 2 || row.status==8}">DONE</c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${feed_status == 3}">ERROR READING</c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${feed_status == 5}">ERROR PARSING</c:when>
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when test="${feed_status == 9}">ERROR_RESIZING</c:when>
                                                                        <c:otherwise>
                                                                            <c:choose>
                                                                                <c:when test="${feed_status == 10}">FILE PROCESSING ERROR</c:when>
                                                                                <c:otherwise>
                                                                                    <c:choose>
                                                                                        <c:when test="${feed_status == 6}">IMPORTED</c:when>
                                                                                        <c:otherwise>
                                                                                            <c:choose>
                                                                                                <c:when test="${feed_status == 7}">RESIZING</c:when>
                                                                                                <c:otherwise>
                                                                                                    <c:choose>
                                                                                                        <c:when test="${feed_status == 4}">ERROR EXTRACTING</c:when>
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

                        <td>

                            <c:choose>
                                <c:when test="${row.status == 3}">
                                    <c:set var="errorFilePath" value="${row.filePath}" scope="request"/>
                                    <%
                                        final String filePath = (String) request.getAttribute("errorFilePath");
                                        boolean errorFileLocationStatus =
                                                checkErrorFileExistenceStatus(filePath, request);
                                        if (errorFileLocationStatus) {
                                    %>
                                    <a href="<%=request.getContextPath()%>/errorFileDownload?errorFilePath=<c:out value="${row.filePath}"/>">Error
                                        File</a>
                                    <%
                                        }
                                    %>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${row.status == 5}">
                                            <c:set var="errorFilePath" value="${row.filePath}" scope="request"/>
                                            <%
                                                final String filePath = (String) request.getAttribute("errorFilePath");
                                                boolean errorFileLocationStatus =
                                                        checkErrorFileExistenceStatus(filePath, request);
                                                if (errorFileLocationStatus) {
                                            %>
                                            <a href="<%=request.getContextPath()%>/errorFileDownload?errorFilePath=<c:out value="${row.filePath}"/>">Error
                                                File</a>
                                            <%
                                                }
                                            %>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${row.status == 10}">
                                                    <c:set var="errorFilePath" value="${row.filePath}" scope="request"/>
                                                    <%
                                                        final String filePath =
                                                                (String) request.getAttribute("errorFilePath");
                                                        boolean errorFileLocationStatus =
                                                                checkErrorFileExistenceStatus(filePath, request);
                                                        if (errorFileLocationStatus) {
                                                    %>
                                                    <a href="<%=request.getContextPath()%>/errorFileDownload?errorFilePath=<c:out value="${row.filePath}"/>">Error
                                                        File</a>
                                                    <%
                                                        }
                                                    %>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${row.status == 9}">
                                                            <c:set var="errorFilePath" value="${row.filePath}"
                                                                   scope="request"/>
                                                            <%
                                                                final String filePath =
                                                                        (String) request.getAttribute("errorFilePath");
                                                                boolean errorFileLocationStatus =
                                                                        checkErrorFileExistenceStatus(filePath,
                                                                                request);
                                                                if (errorFileLocationStatus) {
                                                            %>
                                                            <a href="<%=request.getContextPath()%>/errorFileDownload?errorFilePath=<c:out value="${row.filePath}"/>">Error
                                                                File</a>
                                                            <%
                                                                }
                                                            %>
                                                        </c:when>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>

                        </td>
                        <td>
                            <c:if test="${row.status !=0}">
                                <c:set var="filePath" value="${row.filePath}" scope="request"/>
                                <%
                                    final String filePath =
                                            (String) request.getAttribute("filePath");
                                    String logFilePath = checkLogFileLocation(filePath);
                                    File logFile = FileSystem.getDefault().getFile(logFilePath, false, false);
                                    if (logFile.exists()) {
                                %>
                                    <a id="logFileLink"
                                       href="<%=request.getContextPath()%>/errorFileDownload?logFilePath=<c:out value="${row.filePath}"/>">Log
                                        File</a>
                                <%
                                    }
                                %>
                            </c:if>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</fieldset>

<fieldset>
    <legend><strong>Feed file upload</strong></legend>

    <form:form modelAttribute="dataFileUploadItem" action="dataImport.do" method="post"
               enctype="multipart/form-data"
               onSubmit="return verifyExtension();">
        <table class="">
            <tr>
                <td><form:label for="dataFile" path="dataFile">File</form:label><br/></td>
                <td><form:input path="dataFile" id="dataFile" type="file"/></td>
            </tr>
                <%--<tr>--%>
                <%--<td>--%>
                <%--<form:select path="importType">--%>
                <%--<form:option value="Product" label="Products" />--%>
                <%--<form:option value="ProductImage" label="Product Images"/>--%>
                <%--<form:option value="ProductReview" label="Product Reviews"/>--%>
                <%--</form:select>--%>
                <%--</td>--%>
                <%--</tr>--%>
            <tr>


                <td colspan="2"><form:hidden path="retailerSiteId"/><input type="submit" value="Upload"/></td>
            </tr>
        </table>
    </form:form>
</fieldset>
</c:otherwise>
</c:choose>

</body>
</html>